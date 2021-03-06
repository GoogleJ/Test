package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.bean.request.SendGroupRedPackageRequest;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.bean.response.GetBetConutBygroupIdResponse;
import com.zxjk.duoduo.bean.response.GroupResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.msgpage.rongIM.message.RedPacketMessage;
import com.zxjk.duoduo.ui.widget.dialog.SelectPopupWindow;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.MD5Utils;
import com.zxjk.duoduo.utils.MoneyValueFilter;

import java.text.NumberFormat;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;


@SuppressLint("CheckResult")
public class GroupRedPacketActivity extends BaseActivity implements SelectPopupWindow.OnPopWindowClickListener {
    // 红包类型：1.拼手气红包  2.普通红包
    private String redType = "1";

    private TextView tvRed1;
    private TextView tvRed2;
    private View red1Line;
    private View red2Line;
    private TextView tvGroupRedLabel;
    private EditText etGroupRed1;
    private EditText etGroupRed2;
    private EditText etGroupRed3;
    private TextView tvRedTips;
    private TextView tvGroupRedGroupnums;
    private TextView tvGroupRedMoney;

    private GroupResponse group;

    private SelectPopupWindow selectPopupWindow;

    private NumberFormat nf;

    private String isGame = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_red_packet);
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.m_red_envelopes_title_text));
        TextView tv_end = findViewById(R.id.tv_end);
        tv_end.setVisibility(View.VISIBLE);
        tv_end.setText(getString(R.string.redrecord));
        tv_end.setOnClickListener(v -> jump2List());

        findViewById(R.id.rl_back).setOnClickListener(v -> finish());

        String groupId = getIntent().getStringExtra("groupId");
        isGame = getIntent().getStringExtra("isGame");
        if (TextUtils.isEmpty(isGame)) {
            isGame = "1";
        }
        if (isGame.equals("0")) {
            Observable.timer(30, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                    .compose(bindToLifecycle())
                    .subscribe(s -> {
                        ToastUtils.showShort(R.string.game_timeup);
                        finish();
                    });
        }

        nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);

        tvRed1 = findViewById(R.id.tvRed1);
        tvRed2 = findViewById(R.id.tvRed2);
        red1Line = findViewById(R.id.red1Line);
        red2Line = findViewById(R.id.red2Line);
        tvGroupRedLabel = findViewById(R.id.tvGroupRedLabel);
        etGroupRed1 = findViewById(R.id.etGroupRed1);
        etGroupRed2 = findViewById(R.id.etGroupRed2);
        etGroupRed3 = findViewById(R.id.etGroupRed3);
        tvRedTips = findViewById(R.id.tvRedTips);
        tvGroupRedGroupnums = findViewById(R.id.tvGroupRedGroupnums);
        tvGroupRedMoney = findViewById(R.id.tvGroupRedMoney);
        etGroupRed1.setFilters(new InputFilter[]{new MoneyValueFilter()});

        selectPopupWindow = new SelectPopupWindow(this, this);

        addTextWatcher();

        getGroupInfo(groupId);

        GetBetConutBygroupIdResponse fromeGame = (GetBetConutBygroupIdResponse) getIntent().getSerializableExtra("fromeGame");
        if (fromeGame != null) {
            tvRed2.setClickable(false);
            tvRed2.setEnabled(false);
            etGroupRed1.setText(String.valueOf(fromeGame.getMoney()));
            etGroupRed2.setText(String.valueOf(fromeGame.getCount()));
            etGroupRed1.setEnabled(false);
            etGroupRed2.setEnabled(false);
        }
    }

    private void addTextWatcher() {
        etGroupRed1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (redType.equals("1")) {
                    tvGroupRedMoney.setText(s);
                    return;
                }
                if (s.toString().equals("")) {
                    tvGroupRedMoney.setText("0.00");
                    return;
                }
                String trim = etGroupRed2.getText().toString().trim();
                if (!TextUtils.isEmpty(trim)) {
                    tvGroupRedMoney.setText(nf.format(Integer.parseInt(trim) * Double.parseDouble(s.toString())));
                }
            }
        });
        etGroupRed2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (redType.equals("1")) {
                    return;
                }
                if (s.toString().equals("")) {
                    tvGroupRedMoney.setText("0.00");
                    return;
                }
                String trim = etGroupRed1.getText().toString().trim();
                if (!TextUtils.isEmpty(trim)) {
                    tvGroupRedMoney.setText(nf.format(Double.parseDouble(trim) * Integer.parseInt(s.toString())));
                }
            }
        });
    }

    private void getGroupInfo(String groupId) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getGroupByGroupId(groupId)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.normalTrans())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .subscribe(response -> {
                    group = response;
                    tvGroupRedGroupnums.setText("本群共" + group.getCustomers().size() + "人");
                    changeType();
                }, this::handleApiError);
    }

    public void normalRed(View view) {
        redType = "2";
        changeType();
    }

    public void shouqiRed(View view) {
        redType = "1";
        changeType();
    }

    private void changeType() {
        if (group == null) {
            ToastUtils.showShort(R.string.error_retry);
            return;
        }
        if (redType.equals("1")) {
            //拼手气红包
            tvRed1.setTextColor(ContextCompat.getColor(GroupRedPacketActivity.this, R.color.textcolor1));
            tvRed2.setTextColor(ContextCompat.getColor(GroupRedPacketActivity.this, R.color.textcolor3));
            red1Line.setVisibility(View.VISIBLE);
            red2Line.setVisibility(View.INVISIBLE);
            tvGroupRedLabel.setText(getString(R.string.totalmoney));
            tvRedTips.setText(R.string.redtips1);
        } else if (redType.equals("2")) {
            //普通红包
            tvRed1.setTextColor(ContextCompat.getColor(GroupRedPacketActivity.this, R.color.textcolor3));
            tvRed2.setTextColor(ContextCompat.getColor(GroupRedPacketActivity.this, R.color.textcolor1));
            red1Line.setVisibility(View.INVISIBLE);
            red2Line.setVisibility(View.VISIBLE);
            tvGroupRedLabel.setText(getString(R.string.singlemoney));
            tvRedTips.setText(R.string.redtips2);
        }
    }

    //发送红包
    public void sendRed(View view) {
        String price = etGroupRed1.getText().toString().trim();
        String num = etGroupRed2.getText().toString().trim();

        if (TextUtils.isEmpty(price)) {
            ToastUtils.showShort(R.string.input_money);
            return;
        }

        if (TextUtils.isEmpty(num)) {
            ToastUtils.showShort(R.string.input_num);
            return;
        }

        if (Double.parseDouble(price) == 0) {
            ToastUtils.showShort(R.string.input_money1);
            return;
        }

        if (Double.parseDouble(num) == 0) {
            ToastUtils.showShort(R.string.input_num1);
            return;
        }

        KeyboardUtils.hideSoftInput(this);
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int winHeight = getWindow().getDecorView().getHeight();
        selectPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, winHeight - rect.bottom);
    }

    //跳转红包记录
    public void jump2List() {
        startActivity(new Intent(this, RedPackageListActivity.class));
    }


    @Override
    public void onPopWindowClickListener(String psw, boolean complete) {
        if (!complete) {
            return;
        }

        String price = etGroupRed1.getText().toString().trim();
        String num = etGroupRed2.getText().toString().trim();
        String message = etGroupRed3.getText().toString().trim();

        SendGroupRedPackageRequest request = new SendGroupRedPackageRequest();
        request.setPayPwd(MD5Utils.getMD5(psw));
        request.setGroupId(group.getGroupInfo().getId());
        request.setType(redType);
        request.setMessage(TextUtils.isEmpty(message) ? getString(R.string.m_red_envelopes_label) : message);
        request.setTotalAmount(tvGroupRedMoney.getText().toString());
        request.setIsGame(isGame);
        request.setNumber(num);
        if (redType.equals("2")) {
            request.setMoney(price);
        }

        ServiceFactory.getInstance().getBaseService(Api.class)
                .sendGroupRedPackage(GsonUtils.toJson(request))
                .compose(bindToLifecycle())
                .compose(RxSchedulers.normalTrans())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(GroupRedPacketActivity.this)))
                .subscribe(s -> {
                    RedPacketMessage redPacketMessage = new RedPacketMessage();
                    redPacketMessage.setIsGame(isGame);
                    redPacketMessage.setFromCustomer(Constant.userId);
                    redPacketMessage.setRemark(TextUtils.isEmpty(message) ? getString(R.string.m_red_envelopes_label) : message);
                    redPacketMessage.setRedId(s.getId());
                    Message groupRedMessage = Message.obtain(group.getGroupInfo().getId(), Conversation.ConversationType.GROUP, redPacketMessage);
                    RongIM.getInstance().sendMessage(groupRedMessage, null, null,
                            new IRongCallback.ISendMessageCallback() {
                                @Override
                                public void onAttached(Message message) {

                                }

                                @Override
                                public void onSuccess(Message message) {
                                    finish();
                                }

                                @Override
                                public void onError(Message message, RongIMClient.ErrorCode errorCode) {

                                }
                            });
                }, this::handleApiError);
    }
}
