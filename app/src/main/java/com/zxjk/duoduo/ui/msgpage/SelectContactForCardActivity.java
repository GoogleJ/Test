package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
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
import com.zxjk.duoduo.ui.HomeActivity;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.msgpage.adapter.SelectForCardAdapter;
import com.zxjk.duoduo.ui.msgpage.rongIMAdapter.BusinessCardMessage;
import com.zxjk.duoduo.utils.CommonUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.ImageMessage;


/**
 * author L
 * create at 2019/5/8
 * description: 推荐给好友  选择联系人
 */
@SuppressLint("CheckResult")
public class SelectContactForCardActivity extends BaseActivity implements TextWatcher {

    EditText searchEdit;
    RecyclerView mRecyclerView;
    SelectForCardAdapter mAdapter;
    String userId;
    int userType;
    String friendInfoResponseId;
    String friendInfoId;
    String contactResponseId;
    String businessCardMessageId;

    List<FriendInfoResponse> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contact_for_card);

        initView();
        initRecyclerView();
    }

    private void initRecyclerView() {
        userType = getIntent().getIntExtra("userType", 0);
        friendInfoResponseId = getIntent().getStringExtra("friendInfoResponseId");
        friendInfoId = getIntent().getStringExtra("friendInfoId");
        contactResponseId = getIntent().getStringExtra("contactResponseId");
        userId = getIntent().getStringExtra("userId");
        businessCardMessageId = getIntent().getStringExtra("businessCardMessageId");
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        searchEdit.addTextChangedListener(SelectContactForCardActivity.this);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new SelectForCardAdapter();
        mRecyclerView.setAdapter(mAdapter);
        getFriendListById();

        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            boolean fromShare = getIntent().getBooleanExtra("fromShare", false);
            if (fromShare) {
                CommonUtils.initDialog(this).show();
                saveBitmapFile(Constant.shareGroupQR);
                Uri uri = Uri.fromFile(new File(getExternalCacheDir(), "1.jpg"));
                ImageMessage obtain = ImageMessage.obtain(uri, uri, false);
                Message obtain1 = Message.obtain(list.get(position).getId(), Conversation.ConversationType.PRIVATE, obtain);

                RongIM.getInstance().sendImageMessage(obtain1, null, null, new RongIMClient.SendImageMessageCallback() {
                    @Override
                    public void onAttached(Message message) {

                    }

                    @Override
                    public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                        CommonUtils.destoryDialog();
                    }

                    @Override
                    public void onSuccess(Message message) {
                        CommonUtils.destoryDialog();
                        ToastUtils.showShort(R.string.share_success);
                        finish();
                    }

                    @Override
                    public void onProgress(Message message, int i) {

                    }
                });
                return;
            }
            int intentType = getIntent().getIntExtra("intentType", 0);
            if (intentType == 0) {
                if (userType == 0) {
                    // SelectContactForCardActivity.this.getFriendGourp(mAdapter.getData().get(position).getId());
                }
            } else if (intentType == 1) {
                if (userType == 0) {
                    SelectContactForCardActivity.this.getFriendInfo(userId, position);
                } else if (userType == 1) {
                    SelectContactForCardActivity.this.getFriendInfo(friendInfoResponseId, position);
                } else if (userType == 2) {
                    SelectContactForCardActivity.this.getFriendInfo(friendInfoId, position);
                } else if (userType == 3) {
                    SelectContactForCardActivity.this.getFriendInfo(contactResponseId, position);
                } else {
                    SelectContactForCardActivity.this.getFriendInfo(businessCardMessageId, position);
                }
            }
        });
    }

    private void initView() {
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.select_contacts));
        searchEdit = findViewById(R.id.search_select_contact);
        mRecyclerView = findViewById(R.id.card_recycler_view);
        findViewById(R.id.rl_back).setOnClickListener(v -> finish());
    }

    /**
     * 获取好友列表
     */
    private void getFriendListById() {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getFriendListById()
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(friendInfoResponses -> {
                    this.list = friendInfoResponses;
                    mAdapter.setNewData(friendInfoResponses);
                }, this::handleApiError);
    }

    /**
     * 获取好友详情
     *
     * @param s
     * @param position
     */
    private void getFriendInfo(String s, int position) {
        if (!TextUtils.isEmpty(userId)) {
            FriendInfoResponse infoResponse = mAdapter.getData().get(position);
            NiceDialog.init().setLayoutId(R.layout.layout_general_dialog4).setConvertListener(new ViewConvertListener() {
                @Override
                protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                    holder.setText(R.id.tv_content, getString(R.string.share_business_card));
                    holder.setText(R.id.tv_cancel, getString(R.string.cancel));
                    holder.setText(R.id.tv_notarize, getString(R.string.ok));
                    holder.setOnClickListener(R.id.tv_cancel, v -> dialog.dismiss());
                    holder.setOnClickListener(R.id.tv_notarize, v -> {
                        dialog.dismiss();
                        BusinessCardMessage message = new BusinessCardMessage();
                        message.setDuoduo(infoResponse.getDuoduoId());
                        message.setIcon(infoResponse.getHeadPortrait());
                        message.setUserId(infoResponse.getId());
                        message.setName(infoResponse.getNick());
                        Message message1 = Message.obtain(userId, Conversation.ConversationType.PRIVATE, message);
                        RongIM.getInstance().sendMessage(message1, null, null, new IRongCallback.ISendMessageCallback() {
                            @Override
                            public void onAttached(Message message) {
                            }

                            @Override
                            public void onSuccess(Message message) {
                                dialog.dismiss();
                                Intent intent = new Intent(SelectContactForCardActivity.this, HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                RongIM.getInstance().startPrivateChat(SelectContactForCardActivity.this, userId, mAdapter.getData().get(position).getNick());

                            }

                            @Override
                            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                            }
                        });
                    });

                }
            }).setDimAmount(0.5f).setOutCancel(false).show(getSupportFragmentManager());

        } else {
            ServiceFactory.getInstance().getBaseService(Api.class)
                    .getFriendInfoById(s)
                    .compose(bindToLifecycle())
                    .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                    .compose(RxSchedulers.normalTrans())
                    .subscribe(response -> NiceDialog.init().setLayoutId(R.layout.layout_general_dialog4).setConvertListener(new ViewConvertListener() {
                        @Override
                        protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                            holder.setText(R.id.tv_content, getString(R.string.share_business_card));
                            holder.setText(R.id.tv_cancel, getString(R.string.cancel));
                            holder.setText(R.id.tv_notarize, getString(R.string.ok));
                            holder.setOnClickListener(R.id.tv_cancel, v -> dialog.dismiss());
                            holder.setOnClickListener(R.id.tv_notarize, v -> {
                                dialog.dismiss();
                                BusinessCardMessage message = new BusinessCardMessage();
                                message.setDuoduo(response.getDuoduoId());
                                message.setIcon(response.getHeadPortrait());
                                message.setUserId(response.getId());
                                message.setName(response.getNick());
                                Message message1 = Message.obtain(mAdapter.getData().get(position).getId(), Conversation.ConversationType.PRIVATE, message);
                                RongIM.getInstance().sendMessage(message1, null, null, new IRongCallback.ISendMessageCallback() {
                                    @Override
                                    public void onAttached(Message message) {
                                    }

                                    @Override
                                    public void onSuccess(Message message) {
                                        dialog.dismiss();
                                        Intent intent = new Intent(SelectContactForCardActivity.this, HomeActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        RongIM.getInstance().startPrivateChat(SelectContactForCardActivity.this, mAdapter.getData().get(position).getId(), mAdapter.getData().get(position).getNick());

                                    }

                                    @Override
                                    public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                                    }
                                });
                            });

                        }
                    }).setDimAmount(0.5f).setOutCancel(false).show(getSupportFragmentManager()), this::handleApiError);
        }


    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String groupname = s.toString();
        List<FriendInfoResponse> groupnamelist = search(groupname); //查找对应的群组数据
        mAdapter.setNewData(groupnamelist);
    }

    /**
     * 模糊查询
     *
     * @param str
     * @return
     */
    private List<FriendInfoResponse> search(String str) {
        List<FriendInfoResponse> filterList = new ArrayList<FriendInfoResponse>();// 过滤后的list
        if (str.matches("^([0-9]|[/+]).*")) {// 正则表达式 匹配以数字或者加号开头的字符串(包括了带空格及-分割的号码)
            String simpleStr = str.replaceAll("\\-|\\s", "");
            for (FriendInfoResponse contact : list) {
                if (contact.getNick() != null) {
                    if (contact.getNick().contains(simpleStr) || contact.getNick().contains(str)) {
                        if (!filterList.contains(contact)) {
                            filterList.add(contact);
                        }
                    }
                }
            }
        } else {
            for (FriendInfoResponse contact : list) {
                if (contact.getNick() != null) {
                    //姓名全匹配,姓名首字母简拼匹配,姓名全字母匹配
                    boolean isNameContains = contact.getNick().toLowerCase(Locale.CHINESE)
                            .contains(str.toLowerCase(Locale.CHINESE));

//                    boolean isSortKeyContains = contact.sortKey.toLowerCase(Locale.CHINESE).replace(" ", "")
//                            .contains(str.toLowerCase(Locale.CHINESE));
//
//                    boolean isSimpleSpellContains = contact.sortToken.simpleSpell.toLowerCase(Locale.CHINESE)
//                            .contains(str.toLowerCase(Locale.CHINESE));
//
//                    boolean isWholeSpellContains = contact.sortToken.wholeSpell.toLowerCase(Locale.CHINESE)
//                            .contains(str.toLowerCase(Locale.CHINESE));

                    if (isNameContains) {
                        if (!filterList.contains(contact)) {
                            filterList.add(contact);
                        }
                    }
                }
            }
        }
        return filterList;
    }

    public void saveBitmapFile(Bitmap bitmap) {
        File file = new File(getExternalCacheDir(), "1.jpg");//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
