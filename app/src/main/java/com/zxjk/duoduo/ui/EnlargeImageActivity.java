package com.zxjk.duoduo.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shehuan.nicedialog.BaseNiceDialog;
import com.shehuan.nicedialog.NiceDialog;
import com.shehuan.nicedialog.ViewConvertListener;
import com.shehuan.nicedialog.ViewHolder;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.FriendInfoResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.grouppage.AgreeGroupChatActivity;
import com.zxjk.duoduo.ui.minepage.scanuri.Action1;
import com.zxjk.duoduo.ui.minepage.scanuri.BaseUri;
import com.zxjk.duoduo.ui.msgpage.AddFriendDetailsActivity;
import com.zxjk.duoduo.ui.msgpage.FriendDetailsActivity;
import com.zxjk.duoduo.ui.msgpage.GroupQRActivity;
import com.zxjk.duoduo.ui.msgpage.TransferActivity;
import com.zxjk.duoduo.ui.widget.PinchImageView;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.GlideUtil;
import com.zxjk.duoduo.utils.SaveImageUtil;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;

/**
 * *********************
 * Administrator
 * *********************
 * 2019/5/17
 * *********************
 * 头像放大
 * *********************
 */
public class EnlargeImageActivity extends BaseActivity {

    @BindView(R.id.pic)
    PinchImageView pic;

    @BindView(R.id.iv)
    SubsamplingScaleImageView iv;

    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setNavBarVisibility(this, false);
        setContentView(R.layout.activity_enlarge_image);
        ButterKnife.bind(this);
        String imageUrl = getIntent().getStringExtra("image");
        if (imageUrl.equals("GameRules")) {
            CommonUtils.initDialog(this).show();
            pic.setVisibility(View.GONE);
            iv.setVisibility(View.VISIBLE);
            iv.setOnImageEventListener(new SubsamplingScaleImageView.DefaultOnImageEventListener() {
                @Override
                public void onReady() {
                    super.onReady();
                    CommonUtils.destoryDialog();
                }
            });
            iv.setImage(ImageSource.resource(R.drawable.gamerules));
            iv.setOnClickListener(v -> finish());

        } else {
            GlideUtil.loadNormalImg(pic, imageUrl, new RequestListener<Bitmap>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                    bitmap = resource;
                    pic.setImageBitmap(bitmap);
                    return true;
                }
            });
            pic.setOnClickListener(v -> finishAfterTransition());

            pic.setOnLongClickListener(v -> {
                NiceDialog.init().setLayoutId(R.layout.layout_general_dialog6).setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                        holder.setText(R.id.tv_photograph, "保存图片");
                        holder.setText(R.id.tv_photo_select, "识别二维码");

                        //识别二维码
                        holder.setOnClickListener(R.id.tv_photo_select, v -> {
                            dialog.dismiss();
                            decode(bitmap, "解析二维码失败");
                        });

                        //保存图片
                        holder.setOnClickListener(R.id.tv_photograph, v1 -> getPermisson(g -> {
                            //保存到手机
                            dialog.dismiss();
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
                        }, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE));

                        //取消
                        holder.setOnClickListener(R.id.tv_cancel, v -> dialog.dismiss());

                    }
                }).setShowBottom(true)
                        .setOutCancel(true)
                        .setDimAmount(0.5f)
                        .show(getSupportFragmentManager());
                return true;
            });
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void decode(final Bitmap bitmap, final String errorTip) {

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                return QRCodeDecoder.syncDecodeQRCode(bitmap);
            }

            @Override
            protected void onPostExecute(String result) {
                if (TextUtils.isEmpty(result)) {
                    ToastUtils.showShort(errorTip);
                } else {
                    parseResult(result);
                }
            }

            @SuppressLint("CheckResult")
            private void parseResult(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Object schem = jsonObject.opt("schem");
                    if (!schem.equals("com.zxjk.duoduo")) {
                        throw new RuntimeException();
                    }

                    Object action = jsonObject.opt("action");

                    if (action.equals("action1")) {
                        BaseUri<Action1> uri = new Gson().fromJson(result, new TypeToken<BaseUri<Action1>>() {
                        }.getType());
                        Intent intent = new Intent(EnlargeImageActivity.this, TransferActivity.class);
                        intent.putExtra("fromScan", true);
                        intent.putExtra("money", uri.data.money);
                        intent.putExtra("userId", uri.data.userId);
                        startActivity(intent);
                        finish();
                    } else if (action.equals("action2")) {
                        BaseUri<String> uri = new Gson().fromJson(result, new TypeToken<BaseUri<String>>() {
                        }.getType());

                        String userId = uri.data;

                        if (userId.equals(Constant.userId)) {
                            //扫到了自己的二维码
                            finish();
                            return;
                        }

                        if (Constant.friendsList == null) {
                            ServiceFactory.getInstance().getBaseService(Api.class)
                                    .getFriendListById()
                                    .compose(bindToLifecycle())
                                    .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(EnlargeImageActivity.this)))
                                    .compose(RxSchedulers.normalTrans())
                                    .subscribe(friendInfoResponses -> {
                                        if (Constant.friendsList == null) {
                                            Constant.friendsList = friendInfoResponses;
                                        }

                                        handleFriendList(userId);
                                    }, EnlargeImageActivity.this::handleApiError);
                        } else {
                            handleFriendList(userId);
                        }
                    } else if (action.equals("action3")) {
                        BaseUri<GroupQRActivity.GroupQRData> uri = new Gson().fromJson(result, new TypeToken<BaseUri<GroupQRActivity.GroupQRData>>() {
                        }.getType());
                        Intent intent = new Intent(EnlargeImageActivity.this, AgreeGroupChatActivity.class);
                        intent.putExtra("inviterId", uri.data.inviterId);
                        intent.putExtra("groupId", uri.data.groupId);
                        intent.putExtra("groupName", uri.data.groupName);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    ToastUtils.showShort(R.string.decode_qr_failure);
                }
            }
        }.execute();
    }

    private void handleFriendList(String userId) {
        if (userId.equals(Constant.userId)) {
            //扫到了自己
            Intent intent = new Intent(this, FriendDetailsActivity.class);
            intent.putExtra("friendId", userId);
            startActivity(intent);
            return;
        }
        for (FriendInfoResponse f : Constant.friendsList) {
            if (f.getId().equals(userId)) {
                //扫到了自己的好友，进入详情页（可聊天）
                Intent intent = new Intent(this, FriendDetailsActivity.class);
                intent.putExtra("searchFriendDetails", f);
                startActivity(intent);
                finish();
                return;
            }
        }

        //扫到了陌生人，进入加好友页面
        Intent intent = new Intent(this, AddFriendDetailsActivity.class);
        intent.putExtra("newFriendId", userId);
        startActivity(intent);
        finish();
    }

}
