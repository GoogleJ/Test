package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shehuan.nicedialog.BaseNiceDialog;
import com.shehuan.nicedialog.NiceDialog;
import com.shehuan.nicedialog.ViewConvertListener;
import com.shehuan.nicedialog.ViewHolder;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.request.GroupGamebettingForDuobaoRequest;
import com.zxjk.duoduo.network.response.DuobaoParameterResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.widget.dialog.SelectPopupWindow;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.MD5Utils;
import com.zxjk.duoduo.utils.RecyclerItemAverageDecoration;

import java.util.ArrayList;
import java.util.List;

public class JinDuoBaoActiviity extends BaseActivity implements SelectPopupWindow.OnPopWindowClickListener {

    private SelectPopupWindow selectPopupWindow;

    private TextView tvDuoBaoQiShu;
    private TextView tvDuoBaoNum1;
    private TextView tvDuoBaoNum2;
    private RecyclerView recycler;
    private TextView tvDuoBaoMoney;
    private TextView tvDuoBaoPeilv;

    private BaseQuickAdapter adapter;

    private ArrayList<XiaZhuBean> data = new ArrayList<>(49);
    private List<XiaZhuBean> data1 = new ArrayList<>();
    private List<XiaZhuBean> data2 = new ArrayList<>();

    private BaseNiceDialog dialog;

    private DuobaoParameterResponse response;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jin_duo_bao);

        selectPopupWindow = new SelectPopupWindow(this, this);

        ServiceFactory.getInstance().getBaseService(Api.class)
                .duobaoParameter(getIntent().getStringExtra("groupId"))
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .compose(bindToLifecycle())
                .subscribe(response -> {
                    this.response = response;
                    initView();
                }, this::handleApiError);
    }

    private void initView() {
        while (data.size() != 50) {
            data.add(new XiaZhuBean(data.size() > 9 ? (data.size() + "") : ("0" + data.size()), ""));
        }
        data.remove(0);

        data1 = data.subList(0, 24);
        data2 = data.subList(24, data.size());

        findViewById(R.id.rl_back).setOnClickListener(v -> finish());

        ((TextView) findViewById(R.id.tv_title)).setText(R.string.jinduobao);

        recycler = findViewById(R.id.recycler);
        tvDuoBaoMoney = findViewById(R.id.tvDuoBaoMoney);
        tvDuoBaoPeilv = findViewById(R.id.tvDuoBaoPeilv);
        tvDuoBaoQiShu = findViewById(R.id.tvDuoBaoQiShu);
        tvDuoBaoNum1 = findViewById(R.id.tvDuoBaoNum1);
        tvDuoBaoNum2 = findViewById(R.id.tvDuoBaoNum2);

        tvDuoBaoNum1.setOnClickListener(v -> {
            tvDuoBaoNum1.setTextColor(ContextCompat.getColor(JinDuoBaoActiviity.this, R.color.black));
            tvDuoBaoNum2.setTextColor(ContextCompat.getColor(JinDuoBaoActiviity.this, R.color.colorDuoBao));
            adapter.setNewData(data1);
        });

        tvDuoBaoNum2.setOnClickListener(v -> {
            tvDuoBaoNum1.setTextColor(ContextCompat.getColor(JinDuoBaoActiviity.this, R.color.colorDuoBao));
            tvDuoBaoNum2.setTextColor(ContextCompat.getColor(JinDuoBaoActiviity.this, R.color.black));
            adapter.setNewData(data2);
        });

        recycler.setLayoutManager(new GridLayoutManager(this, 6));

        int decoration = ScreenUtils.getScreenWidth() / 6 / 7;
        int itemHeight = (ScreenUtils.getScreenWidth() - decoration / 2 * 2 * 7) / 6;

        adapter = new BaseQuickAdapter<XiaZhuBean, BaseViewHolder>(R.layout.item_xiazhu_duobao, data1) {
            @Override
            protected void convert(BaseViewHolder helper, XiaZhuBean item) {
                TextView tv1 = helper.getView(R.id.tv1);
                ViewGroup.LayoutParams layoutParams = tv1.getLayoutParams();
                layoutParams.height = itemHeight;
                tv1.setLayoutParams(layoutParams);

                tv1.setText(item.betCode);
                if (!TextUtils.isEmpty(item.betMoney)) {
                    tv1.setTextColor(ContextCompat.getColor(JinDuoBaoActiviity.this, R.color.white));
                    tv1.setBackgroundResource(R.drawable.shape_duobaoxiazhu_selected);
                } else {
                    tv1.setTextColor(ContextCompat.getColor(JinDuoBaoActiviity.this, R.color.text_color1));
                    tv1.setBackgroundResource(R.drawable.shape_duobaoxiazhu_normal);
                }

                TextView tv2 = helper.getView(R.id.tv2);
                if (!TextUtils.isEmpty(item.betMoney)) {
                    tv2.setVisibility(View.VISIBLE);
                    tv2.setText(item.betMoney + "HK");
                } else {
                    tv2.setVisibility(View.GONE);
                }
            }
        };

        adapter.setOnItemClickListener((adapter, view, position) -> {
            XiaZhuBean bean = (XiaZhuBean) adapter.getData().get(position);
            dialog = NiceDialog.init().setLayoutId(R.layout.layout_dialog_duobaoxiazhu)
                    .setConvertListener(new ViewConvertListener() {
                        @Override
                        protected void convertView(ViewHolder holder, BaseNiceDialog baseNiceDialog) {
                            holder.setText(R.id.tvRate, "下注范围为" + response.getMinimumBetAmount() + "-" + response.getMaximumBetAmount());
                            holder.setText(R.id.tv, bean.betCode);
                            EditText et = holder.getView(R.id.et);

                            holder.setOnClickListener(R.id.ivXiaZhu1, v -> {
                                //10
                                String trim = et.getText().toString().trim();
                                if (!TextUtils.isEmpty(trim)) {
                                    et.setText(Integer.parseInt(trim) + 10 + "");
                                } else {
                                    et.setText(10 + "");
                                }
                            });

                            holder.setOnClickListener(R.id.ivXiaZhu2, v -> {
                                //50
                                String trim = et.getText().toString().trim();
                                if (!TextUtils.isEmpty(trim)) {
                                    et.setText(Integer.parseInt(trim) + 50 + "");
                                } else {
                                    et.setText(50 + "");
                                }
                            });

                            holder.setOnClickListener(R.id.ivXiaZhu3, v -> {
                                //100
                                String trim = et.getText().toString().trim();
                                if (!TextUtils.isEmpty(trim)) {
                                    et.setText(Integer.parseInt(trim) + 100 + "");
                                } else {
                                    et.setText(100 + "");
                                }
                            });

                            holder.setOnClickListener(R.id.ivXiaZhu4, v -> {
                                //200
                                String trim = et.getText().toString().trim();
                                if (!TextUtils.isEmpty(trim)) {
                                    et.setText(Integer.parseInt(trim) + 200 + "");
                                } else {
                                    et.setText(200 + "");
                                }
                            });

                            holder.setOnClickListener(R.id.ivXiaZhu5, v -> {
                                //500
                                String trim = et.getText().toString().trim();
                                if (!TextUtils.isEmpty(trim)) {
                                    et.setText(Integer.parseInt(trim) + 500 + "");
                                } else {
                                    et.setText(500 + "");
                                }
                            });

                            holder.setOnClickListener(R.id.tv_cancel, v -> dialog.dismiss());

                            holder.setOnClickListener(R.id.tv_determine, v -> {
                                String trim = et.getText().toString().trim();

                                if (TextUtils.isEmpty(trim)) {
                                    dialog.dismiss();
                                    bean.betMoney = trim;
                                    adapter.notifyDataSetChanged();
                                    return;
                                }

                                int i = Integer.parseInt(trim);
                                if (i < Float.parseFloat(response.getMinimumBetAmount()) || i > Float.parseFloat(response.getMaximumBetAmount())) {
                                    ToastUtils.showShort(R.string.duobaoxiazhufail);
                                    return;
                                }

                                dialog.dismiss();

                                bean.betMoney = trim;
                                adapter.notifyDataSetChanged();

                                String total = "0";
                                ArrayList<XiaZhuBean> finalData = new ArrayList<>();
                                for (XiaZhuBean bean : data1) {
                                    if (!TextUtils.isEmpty(bean.betMoney)) {
                                        finalData.add(bean);
                                        total = Integer.parseInt(bean.betMoney) + Integer.parseInt(total) + "";
                                    }
                                }
                                for (XiaZhuBean bean : data2) {
                                    if (!TextUtils.isEmpty(bean.betMoney)) {
                                        finalData.add(bean);
                                        total = Integer.parseInt(bean.betMoney) + Integer.parseInt(total) + "";
                                    }
                                }
                                tvDuoBaoMoney.setText("下注总金额：" + total);
                            });
                        }
                    })
                    .setDimAmount(0.5f)
                    .setOutCancel(false);
            dialog.show(getSupportFragmentManager());
        });

        recycler.addItemDecoration(new RecyclerItemAverageDecoration(decoration / 2 * 2, decoration, 6));
        recycler.setAdapter(adapter);

        tvDuoBaoQiShu.setText("期数：" + response.getExpect());
        tvDuoBaoPeilv.setText(response.getDuobaoMultiple());
    }

    private ArrayList<XiaZhuBean> finalData11;
    private String total11;

    //下注
    @SuppressLint("CheckResult")
    public void onBet(View view) {
        finalData11 = new ArrayList<>();
        total11 = "0";
        for (XiaZhuBean bean : data1) {
            if (!TextUtils.isEmpty(bean.betMoney)) {
                finalData11.add(bean);
                total11 = Integer.parseInt(bean.betMoney) + Integer.parseInt(total11) + "";
            }
        }
        for (XiaZhuBean bean : data2) {
            if (!TextUtils.isEmpty(bean.betMoney)) {
                finalData11.add(bean);
                total11 = Integer.parseInt(bean.betMoney) + Integer.parseInt(total11) + "";
            }
        }

        if (finalData11.size() == 0) {
            ToastUtils.showShort(R.string.qingxiazhu);
            return;
        }

        KeyboardUtils.hideSoftInput(this);
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int winHeight = getWindow().getDecorView().getHeight();
        selectPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, winHeight - rect.bottom);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onPopWindowClickListener(String psw, boolean complete) {
        if (complete) {
            GroupGamebettingForDuobaoRequest request = new GroupGamebettingForDuobaoRequest();
            request.setExpect(response.getExpect());
            request.setCountMoney(total11);
            request.setPayPwd(MD5Utils.getMD5(psw));
            request.setGroupId(getIntent().getStringExtra("groupId"));
            request.setDuoBaoBetInfoBeanList(finalData11);

            ServiceFactory.getInstance().getBaseService(Api.class)
                    .groupGamebettingForDuobao(GsonUtils.toJson(request))
                    .compose(bindToLifecycle())
                    .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                    .compose(RxSchedulers.normalTrans())
                    .subscribe(s -> {
                        ToastUtils.showShort(R.string.xiazhuchenggong);
                        finish();
                    }, this::handleApiError);
        }
    }

    public class XiaZhuBean {
        String betCode;
        String betMoney;

        XiaZhuBean(String number, String money) {
            this.betCode = number;
            this.betMoney = money;
        }
    }
}
