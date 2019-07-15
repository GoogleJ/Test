package com.zxjk.duoduo.ui.msgpage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.bean.response.BaseResponse;
import com.zxjk.duoduo.bean.response.GetGameInfoByGroupIdResponse;
import com.zxjk.duoduo.bean.response.GroupResponse;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.minepage.scanuri.BaseUri;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.ImageUtil;
import com.zxjk.duoduo.utils.SaveImageUtil;
import com.zxjk.duoduo.utils.WeChatShareUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

@SuppressLint("CheckResult")
public class GameQRActivity extends BaseActivity {

    private CircleImageView ivGroupHead;
    private ImageView ivQR;
    private TextView tvGroupName;
    private TextView tvGroupOwner;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private RecyclerView recyclerView;
    private RelativeLayout rlContent;

    private int imgSize;
    private BaseUri uri = new BaseUri("action3");
    private String uri2Code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenUtils.setFullScreen(this);
        setContentView(R.layout.activity_game_qr);

        initView();

        initData();
    }

    private void initData() {
        GroupResponse data = (GroupResponse) getIntent().getSerializableExtra("data");

        loadHead(data);

        tvGroupName.setText(data.getGroupInfo().getGroupNikeName());

        imgSize = ScreenUtils.getScreenWidth() - CommonUtils.dip2px(this, 160);
        ViewGroup.LayoutParams layoutParams = ivQR.getLayoutParams();
        layoutParams.height = imgSize;
        ivQR.setLayoutParams(layoutParams);

        uri.data = new GroupQRActivity.GroupQRData();
        ((GroupQRActivity.GroupQRData) uri.data).groupId = data.getGroupInfo().getId();
        ((GroupQRActivity.GroupQRData) uri.data).inviterId = Constant.userId;
        ((GroupQRActivity.GroupQRData) uri.data).groupName = data.getGroupInfo().getGroupNikeName();
        uri2Code = new Gson().toJson(uri);

        Observable.create((ObservableOnSubscribe<Bitmap>) e ->
                e.onNext(QRCodeEncoder.syncEncodeQRCode(uri2Code, imgSize, Color.BLACK)))
                .flatMap((Function<Bitmap, ObservableSource<BaseResponse<GetGameInfoByGroupIdResponse>>>) b -> {
                    runOnUiThread(() -> ivQR.setImageBitmap(b));
                    return ServiceFactory.getInstance().getBaseService(Api.class).getGameInfoByGroupId(data.getGroupInfo().getId());
                }).compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(bindToLifecycle())
                .compose(RxSchedulers.normalTrans())
                .subscribe(r -> {
                    if (r.getPlayCommissionConfigList().size() == 0) {
                        recyclerView.setVisibility(View.GONE);
                        tv3.setVisibility(View.GONE);
                    }

                    tvGroupOwner.setText("群主:" + r.group.getGroupOwnerNick());
                    String playId = r.group.getPlayId();

                    String str = "游戏类型：";
                    switch (r.getGroup().getGameType()) {
                        case "1":
                            if (playId.contains("9"))
                                str += "牛牛，";

                            if (playId.contains("16"))
                                str += "大小单，";

                            if (playId.contains("25"))
                                str += "百家乐，";
                            str = str.substring(0, str.length() - 1);
                            tv2.setText("抽水比例：" + Double.parseDouble(r.getGroup().getPumpingRate()) * 100 + "%");
                            break;
                        case "2":
                            if (playId.contains("91"))
                                str += "牛牛，";

                            if (playId.contains("98"))
                                str += "大小单，";

                            if (playId.contains("107"))
                                str += "百家乐，";
                            str = str.substring(0, str.length() - 1);
                            tv2.setText("抽水比例：" + Double.parseDouble(r.getGroup().getPumpingRate()) * 100 + "%");
                            break;
                        case "3":
                            if (playId.contains("255"))
                                str += "牛牛，";

                            if (playId.contains("262"))
                                str += "大小单，";

                            if (playId.contains("271"))
                                str += "百家乐，";
                            str = str.substring(0, str.length() - 1);
                            tv2.setText("抽水比例：" + Double.parseDouble(r.getGroup().getPumpingRate()) * 100 + "%");
                            break;
                        case "4":
                            str += "香港金多宝";
                            recyclerView.setVisibility(View.GONE);
                            //tv4.setVisibility(View.VISIBLE);
                            tv2.setText("倍率：" + r.getGroup().getDuobaoMultiple());
                            if (r.getPlayCommissionConfigList().size() != 0) {
                                tv3.setText("游戏返佣：" + Double.parseDouble(r.getPlayCommissionConfigList().get(0).getCommission()) * 100 + "%");
                                tv4.setText("单个号码最大下注金额：" + r.getPlayCommissionConfigList().get(0).getMax());
                            }
                            break;
                        default:
                    }

                    tv1.setText(str);

                    if (recyclerView.getVisibility() != View.GONE) {
                        recyclerView.setLayoutManager(new GridLayoutManager(GameQRActivity.this, 2));
                        recyclerView.setAdapter(new BaseQuickAdapter<GetGameInfoByGroupIdResponse.PlayCommissionConfigListBean, BaseViewHolder>(
                                R.layout.item_gameqr, r.getPlayCommissionConfigList()) {
                            @Override
                            protected void convert(BaseViewHolder helper, GetGameInfoByGroupIdResponse.PlayCommissionConfigListBean item) {
                                helper.setText(R.id.tv1, item.getGrade())
                                        .setText(R.id.tv2, item.getRemarks());
                            }
                        });
                    }

                }, this::handleApiError);
    }

    private void initView() {
        findViewById(R.id.rl_back).setOnClickListener(v -> finish());

        ivGroupHead = findViewById(R.id.ivGroupHead);
        ivQR = findViewById(R.id.ivQR);
        tvGroupName = findViewById(R.id.tvGroupName);
        tvGroupOwner = findViewById(R.id.tvGroupOwner);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        recyclerView = findViewById(R.id.recyclerView);
        rlContent = findViewById(R.id.rlContent);
        rlContent.setDrawingCacheEnabled(true);
    }

    public void save(View view) {
        getPermisson(findViewById(R.id.tvSave), g -> {
            //保存到手机
            Bitmap bitmap = rlContent.getDrawingCache();
            if (bitmap == null) {
                return;
            }

            SaveImageUtil.get().savePic(bitmap, success -> {
                if (success) {
                    ToastUtils.showShort(R.string.savesucceed);
                    return;
                }
                ToastUtils.showShort(R.string.savefailed);
            });
        }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    public void duoduo(View view) {
        RongIMClient.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                Bitmap bitmap = rlContent.getDrawingCache();
                if (bitmap == null) {
                    return;
                }
                Constant.shareGroupQR = bitmap;
                Intent intent = new Intent(GameQRActivity.this, ShareGroupQRActivity.class);
                intent.putParcelableArrayListExtra("data", (ArrayList<Conversation>) conversations);
                startActivity(intent);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    public void wechat(View view) {
        Bitmap bitmap = rlContent.getDrawingCache();
        if (bitmap == null) {
            return;
        }
        WeChatShareUtil.shareImg(this, 0, bitmap);
    }

    public void timeline(View view) {
        Bitmap bitmap = rlContent.getDrawingCache();
        if (bitmap == null) {
            return;
        }
        WeChatShareUtil.shareImg(this, 1, bitmap);
    }

    private void loadHead(GroupResponse data) {

        String s = "";
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < data.getCustomers().size(); i++) {
            stringBuilder.append(data.getCustomers().get(i).getHeadPortrait() + ",");
            if (i == data.getCustomers().size() - 1 || i == 8) {
                s = stringBuilder.substring(0, stringBuilder.length() - 1);
                break;
            }
        }

        ImageUtil.loadGroupPortrait(ivGroupHead, s, 80, 3);
    }
}
