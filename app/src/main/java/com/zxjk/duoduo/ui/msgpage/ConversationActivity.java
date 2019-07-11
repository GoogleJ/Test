package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.shehuan.nicedialog.BaseNiceDialog;
import com.shehuan.nicedialog.NiceDialog;
import com.shehuan.nicedialog.ViewConvertListener;
import com.shehuan.nicedialog.ViewHolder;
import com.trello.rxlifecycle3.android.ActivityEvent;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.bean.request.GroupGamebettingRequeust;
import com.zxjk.duoduo.bean.response.BaseResponse;
import com.zxjk.duoduo.bean.response.GetBetConutBygroupIdResponse;
import com.zxjk.duoduo.bean.response.GetGroupOwnerTrendResponse;
import com.zxjk.duoduo.bean.response.GroupResponse;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.rx.RxException;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.EnlargeImageActivity;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.grouppage.ChatInformationActivity;
import com.zxjk.duoduo.ui.grouppage.GroupChatInformationActivity;
import com.zxjk.duoduo.ui.msgpage.rongIM.BasePluginExtensionModule;
import com.zxjk.duoduo.ui.msgpage.rongIM.CusConversationFragment;
import com.zxjk.duoduo.ui.msgpage.rongIM.message.BusinessCardMessage;
import com.zxjk.duoduo.ui.msgpage.rongIM.message.RedPacketMessage;
import com.zxjk.duoduo.ui.msgpage.rongIM.message.TransferMessage;
import com.zxjk.duoduo.ui.msgpage.rongIM.plugin.AudioVideoPlugin;
import com.zxjk.duoduo.ui.msgpage.rongIM.plugin.BusinessCardPlugin;
import com.zxjk.duoduo.ui.msgpage.rongIM.plugin.FilePlugin;
import com.zxjk.duoduo.ui.msgpage.rongIM.plugin.PhotoSelectorPlugin;
import com.zxjk.duoduo.ui.msgpage.rongIM.plugin.RedPacketPlugin;
import com.zxjk.duoduo.ui.msgpage.rongIM.plugin.SightPlugin;
import com.zxjk.duoduo.ui.msgpage.rongIM.plugin.TransferPlugin;
import com.zxjk.duoduo.ui.msgpage.rongIM.plugin.game.GameDownScorePlugin;
import com.zxjk.duoduo.ui.msgpage.rongIM.plugin.game.GameDuobaoPlugin;
import com.zxjk.duoduo.ui.msgpage.rongIM.plugin.game.GameJiaoYiPlugin;
import com.zxjk.duoduo.ui.msgpage.rongIM.plugin.game.GameRecordPlugin;
import com.zxjk.duoduo.ui.msgpage.rongIM.plugin.game.GameRulesPlugin;
import com.zxjk.duoduo.ui.msgpage.rongIM.plugin.game.GameStartPlugin;
import com.zxjk.duoduo.ui.msgpage.rongIM.plugin.game.GameUpScorePlugin;
import com.zxjk.duoduo.ui.msgpage.rongIM.rongTab.SampleTab;
import com.zxjk.duoduo.ui.msgpage.widget.GamePopupWindow;
import com.zxjk.duoduo.ui.widget.dialog.ExpiredEnvelopesDialog;
import com.zxjk.duoduo.ui.widget.dialog.RedEvelopesDialog;
import com.zxjk.duoduo.utils.CommonUtils;
import org.jetbrains.annotations.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.rong.imkit.IExtensionModule;
import io.rong.imkit.RongExtension;
import io.rong.imkit.RongExtensionManager;
import io.rong.imkit.RongIM;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imkit.widget.adapter.MessageListAdapter;
import io.rong.imlib.MessageTag;
import io.rong.imlib.RongCommonDefine;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.imlib.typingmessage.TypingStatus;
import io.rong.message.InformationNotificationMessage;
import io.rong.message.LocationMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;
import razerdp.basepopup.QuickPopupBuilder;
import razerdp.basepopup.QuickPopupConfig;

import static com.zxjk.duoduo.Constant.CODE_SUCCESS;
import static com.zxjk.duoduo.Constant.CODE_UNLOGIN;

@SuppressLint("CheckResult")
public class ConversationActivity extends BaseActivity {
    private CusConversationFragment fragment;
    private MessageListAdapter messageAdapter;

    private Disposable gameWindowDisposable;

    private TextView tvTitle;
    private String targetId;
    private UserInfo targetUserInfo;
    private GroupResponse groupResponse;
    private RongIM.OnSendMessageListener onSendMessageListener;
    private RongIMClient.OnReceiveMessageListener onReceiveMessageListener;
    private RongIMClient.TypingStatusListener typingStatusListener;
    private RongIM.ConversationClickListener conversationClickListener;
    private RongExtension extension;
    //游戏popwindow跳转计时器
    private long timeLeft;
    private GamePopupWindow gamePopupWindow;
    public ArrayList<String> memberIds;

    private LineChart chart;
    private TextView tvChartTitle;
    private LinearLayout llChart;
    private ArrayList<String> niuniuRules;
    private ArrayList<String> otherRules;
    //1:牛牛 2:其他
    private int currentOwnerGameType = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String conversationType = resolvePlugin();

        setContentView(R.layout.activity_conversation);

        llChart = findViewById(R.id.llChart);
        tvChartTitle = findViewById(R.id.tvChartTitle);
        findViewById(R.id.rl_back).setOnClickListener(v -> finish());
        RelativeLayout rl_end = findViewById(R.id.rl_end);
        rl_end.setVisibility(View.VISIBLE);
        rl_end.setOnClickListener(v -> detail());
        extension = findViewById(io.rong.imkit.R.id.rc_extension);

        if (conversationType.equals("system")) {
            rl_end.setVisibility(View.INVISIBLE);
            tvTitle = findViewById(R.id.tv_title);
            targetId = getIntent().getData().getQueryParameter("targetId");
            if (targetId.equals("147")) {
                tvTitle.setText("支付凭证");
            } else if (targetId.equals("349")) {
                tvTitle.setText("对局结果");
            } else {
                tvTitle.setText("系统消息");
            }
            extension.removeAllViews();
            return;
        }

        onReceiveMessageListener = onReceiveMessage();

        RongIM.setOnReceiveMessageListener(onReceiveMessageListener);

        registerSendMessageListener();

        handleBean(conversationType);

        handleClickMsg();

        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        fragment = (CusConversationFragment) fragments.get(0);
        messageAdapter = fragment.getMessageAdapter();
        messageAdapter.setMaxMessageSelectedCount(9);
    }

    private void registerSendMessageListener() {
        onSendMessageListener = new RongIM.OnSendMessageListener() {
            @Override
            public Message onSend(Message message) {
                MessageContent content = message.getContent();
                if (content instanceof TextMessage) {
                    if (!TextUtils.isEmpty(((TextMessage) message.getContent()).getExtra()) &&
                            ((TextMessage) message.getContent()).getExtra().equals("start")
                            && ((TextMessage) content).getContent().equals("开始下注")) {
                        //发送完"开始下注" 计时23S
                        runOnUiThread(() -> Observable.intervalRange(1, 23, 1, 1, TimeUnit.SECONDS)
                                .doOnDispose(() -> {
                                    CommonUtils.destoryDialog();
                                    ToastUtils.showShort(R.string.xiazhu_cancel);
                                })
                                .doOnError(t -> CommonUtils.destoryDialog())
                                .doOnComplete(CommonUtils::destoryDialog)
                                .flatMap(aLong -> {
                                    if (aLong == 23) {
                                        return ServiceFactory.getInstance().getBaseService(Api.class)
                                                .getBetConutBygroupId(groupResponse.getGroupInfo().getId());
                                    }
                                    return Observable.just(aLong);
                                })
                                .compose(bindUntilEvent(ActivityEvent.STOP))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnSubscribe(disposable -> {
                                    CommonUtils.initDialog(ConversationActivity.this, "请耐心等待群员下注(23S)");
                                    if (CommonUtils.getDialog() == null) return;
                                    CommonUtils.getDialog().setCancelable(false);
                                    CommonUtils.getDialog().show();
                                })
                                .subscribe(response -> {
                                    if (response instanceof BaseResponse) {
                                        BaseResponse response1 = (BaseResponse) response;
                                        if (response1.code == CODE_SUCCESS) {
                                            GetBetConutBygroupIdResponse data = (GetBetConutBygroupIdResponse) response1.data;
                                            if (data.getCount() == 0) {
                                                ToastUtils.showShort(R.string.noxiazhu);
                                                return;
                                            }
                                            Intent intent = new Intent(ConversationActivity.this, GroupRedPacketActivity.class);
                                            intent.putExtra("isGame", "0");
                                            intent.putExtra("groupId", groupResponse.getGroupInfo().getId());
                                            intent.putExtra("fromeGame", data);
                                            startActivity(intent);
                                        } else if (response1.code == CODE_UNLOGIN) {
                                            Observable.error(new RxException.DuplicateLoginExcepiton("重复登录"));
                                        } else {
                                            Observable.error(new RxException.ParamsException(response1.msg, response1.code));
                                        }
                                    } else if (response instanceof Long) {
                                        TextView textView = CommonUtils.getDialog().findViewById(R.id.tv_dialog_content);
                                        textView.setText("请耐心等待群员下注(" + (23 - ((Long) response) + "S)"));
                                    }
                                }, ConversationActivity.this::handleApiError));
                    }
                }
                return message;
            }

            @Override
            public boolean onSent(Message message, RongIM.SentMessageErrorCode sentMessageErrorCode) {
                return false;
            }
        };

        RongIM.getInstance().setSendMessageListener(onSendMessageListener);
    }

    @NotNull
    private RongIMClient.OnReceiveMessageListener onReceiveMessage() {
        return (message, i) -> {
            if (message.getContent() instanceof TransferMessage) {
                //收到一条转账消息(已领取)
                for (int j = 0; j < messageAdapter.getCount(); j++) {
                    MessageContent content = messageAdapter.getItem(j).getContent();
                    if (content instanceof TransferMessage) {
                        TransferMessage t = (TransferMessage) content;
                        if (t.getTransferId().equals(((TransferMessage) message.getContent()).getTransferId())) {
                            int finalJ = j;
                            RongIM.getInstance().setMessageExtra(messageAdapter.getItem(j).getMessageId()
                                    , "1", new RongIMClient.ResultCallback<Boolean>() {
                                        @Override
                                        public void onSuccess(Boolean aBoolean) {
                                            messageAdapter.getItem(finalJ).setExtra("1");
                                            messageAdapter.notifyDataSetInvalidated();
                                        }

                                        @Override
                                        public void onError(RongIMClient.ErrorCode errorCode) {

                                        }
                                    });
                        }
                    }
                }
            } else if (message.getContent() instanceof TextMessage) {
                //收到 "开始下注" 消息 并且不是群主（群主无法参与）
                if (!TextUtils.isEmpty(((TextMessage) message.getContent()).getExtra())
                        && message.getTargetId().equals(targetId)
                        && message.getSenderUserId().equals(groupResponse.getGroupInfo().getGroupOwnerId())
                        && ((TextMessage) message.getContent()).getExtra().equals("start")
                        && !Constant.userId.equals(groupResponse.getGroupInfo().getGroupOwnerId()))
                    runOnUiThread(() -> ServiceFactory.getInstance().getBaseService(Api.class)
                            .getGroupGameParameter(groupResponse.getGroupInfo().getId())
                            .compose(bindUntilEvent(ActivityEvent.STOP))
                            .compose(RxSchedulers.normalTrans())
                            .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(ConversationActivity.this)))
                            .subscribe(getGroupGameParameterResponse -> {
                                if (TextUtils.isEmpty(getGroupGameParameterResponse.getBalanceHK())) {
                                    ToastUtils.showShort(R.string.noscore);
                                    return;
                                }
                                gamePopupWindow = new GamePopupWindow(ConversationActivity.this);
                                gamePopupWindow.setGroupId(groupResponse.getGroupInfo().getId());
                                gamePopupWindow.setOnCommit((data, time) -> {
                                    Double maxMoney;
                                    GroupGamebettingRequeust requeust1 = GsonUtils.fromJson(data, GroupGamebettingRequeust.class);

                                    if (requeust1.getPlayName().equals("牛牛")) {
                                        //牛牛的最大赔率
                                        maxMoney = CommonUtils.mul(Double.parseDouble(requeust1.getBetMoneny()), Double.parseDouble("16"));
                                    } else {
                                        //其他类型的最大赔率
                                        maxMoney = CommonUtils.mul(Double.parseDouble(requeust1.getBetMoneny()), Double.parseDouble(requeust1.getMultiple()));
                                    }

                                    String balanceHK = getGroupGameParameterResponse.getBalanceHK();
                                    //下注金额
                                    BigDecimal data1 = new BigDecimal(requeust1.getBetMoneny());
                                    //剩余金额
                                    BigDecimal data2 = new BigDecimal(balanceHK);
                                    //最大赔率金额
                                    BigDecimal data3 = new BigDecimal(maxMoney);
                                    //下注金额 <= 剩余金额
                                    if (data1.compareTo(data2) <= 0) {
                                        //最大赔率 <= 剩余金额
                                        if (requeust1.getPlayName().equals("百家乐") || data3.compareTo(data2) <= 0) {
                                            gamePopupWindow.dismiss();
                                            //确认下注
                                            NiceDialog.init().setLayoutId(R.layout.layout_dialog_fragment)
                                                    .setConvertListener(new ViewConvertListener() {
                                                        @Override
                                                        protected void convertView(ViewHolder viewHolder, BaseNiceDialog baseNiceDialog) {
                                                            TextView tv_type = viewHolder.getView(R.id.tv_type);
                                                            TextView tv_bet = viewHolder.getView(R.id.tv_bet);
                                                            TextView tv_theOdds = viewHolder.getView(R.id.tv_theOdds);
                                                            TextView tvGameCountDown = viewHolder.getView(R.id.tvGameCountDown);
                                                            GroupGamebettingRequeust requeust = GsonUtils.fromJson(data, GroupGamebettingRequeust.class);
                                                            tv_type.setText(requeust.getPlayName());
                                                            tv_bet.setText(requeust.getBetMoneny());
                                                            if (requeust.getPlayName().equals("牛牛")) {
                                                                tv_theOdds.setVisibility(View.GONE);
                                                            } else {
                                                                tv_theOdds.setVisibility(View.VISIBLE);
                                                                tv_theOdds.setText(requeust.getBetCardType());
                                                            }
                                                            Disposable subscribe = Observable.interval(0, 1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                                                                    .take(time)
                                                                    .compose(bindUntilEvent(ActivityEvent.STOP))
                                                                    .subscribe(l -> {
                                                                        timeLeft = time - l;
                                                                        tvGameCountDown.setText((time - l) + "");
                                                                        if (l == time - 1) {
                                                                            ToastUtils.showShort(R.string.timeout_game);
                                                                            baseNiceDialog.dismiss();
                                                                        }
                                                                    }, t -> {
                                                                    });

                                                            viewHolder.getView(R.id.tv_cancel).setOnClickListener(v -> {
                                                                baseNiceDialog.dismiss();
                                                                subscribe.dispose();
                                                                if (timeLeft - 1 > 0) {
                                                                    gameWindowDisposable = gamePopupWindow.show(getGroupGameParameterResponse, timeLeft - 1);
                                                                } else {
                                                                    ToastUtils.showShort(R.string.timeout_game);
                                                                }
                                                            });

                                                            viewHolder.getView(R.id.tv_determine).setOnClickListener(v ->
                                                                    ServiceFactory.getInstance().getBaseService(Api.class)
                                                                            .groupGamebetting(data)
                                                                            .compose(RxSchedulers.ioObserver())
                                                                            .compose(RxSchedulers.normalTrans())
                                                                            .compose(bindToLifecycle())
                                                                            .subscribe(s -> {
                                                                                subscribe.dispose();
                                                                                baseNiceDialog.dismiss();
                                                                                ToastUtils.showShort(R.string.xiazhuchenggong);
                                                                            }, t -> {
                                                                                if (t instanceof RxException.ParamsException &&
                                                                                        ((RxException.ParamsException) t).getCode() == 502) {
                                                                                    //超时
                                                                                    ToastUtils.showShort(t.getMessage());
                                                                                    subscribe.dispose();
                                                                                    baseNiceDialog.dismiss();
                                                                                    return;
                                                                                }
                                                                                handleApiError(t);
                                                                            }));
                                                        }
                                                    })
                                                    .setDimAmount(0.5f)
                                                    .setOutCancel(false)
                                                    .show(getSupportFragmentManager());
                                        } else {
                                            gamePopupWindow.dismiss();
                                            //1:1
                                            GroupGamebettingRequeust gr = new GroupGamebettingRequeust();
                                            if (requeust1.getPlayName().equals("牛牛")) {
                                                gr.setBetCardType("");
                                            } else {
                                                gr.setBetCardType(requeust1.getBetCardType());
                                            }
                                            gr.setGroupId(requeust1.getGroupId());
                                            gr.setMultiple("1");
                                            gr.setPlayId(requeust1.getPlayId());
                                            gr.setBetMoneny(requeust1.getBetMoneny());
                                            gr.setCustomerId(requeust1.getCustomerId());
                                            gr.setPlayName(requeust1.getPlayName());
                                            String dataJson = GsonUtils.toJson(gr);
                                            NiceDialog.init().setLayoutId(R.layout.layout_general_dialog5).setConvertListener(new ViewConvertListener() {
                                                @Override
                                                protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                                                    TextView tvGameCountDown = holder.getView(R.id.tvGameCountDown);
                                                    Disposable dd = Observable.interval(0, 1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                                                            .take(time)
                                                            .compose(bindUntilEvent(ActivityEvent.STOP))
                                                            .subscribe(l -> {
                                                                timeLeft = time - l;
                                                                tvGameCountDown.setText((time - l) + "");
                                                                if (l == time - 1) {
                                                                    ToastUtils.showShort(R.string.timeout_game);
                                                                    dialog.dismiss();
                                                                }
                                                            }, t -> {
                                                            });
                                                    //否
                                                    holder.setOnClickListener(R.id.tv_cancel, v13 -> {
                                                        dd.dispose();
                                                        dialog.dismiss();
                                                        if (timeLeft - 1 > 0) {
                                                            gameWindowDisposable = gamePopupWindow.show(getGroupGameParameterResponse, timeLeft - 1);
                                                        } else {
                                                            ToastUtils.showShort(R.string.timeout_game);
                                                        }
                                                    });
                                                    //是
                                                    holder.setOnClickListener(R.id.tv_notarize, v12 -> ServiceFactory.getInstance().getBaseService(Api.class)
                                                            .groupGamebetting(dataJson)
                                                            .compose(RxSchedulers.ioObserver())
                                                            .compose(RxSchedulers.normalTrans())
                                                            .compose(bindToLifecycle())
                                                            .subscribe(s -> {
                                                                dd.dispose();
                                                                dialog.dismiss();
                                                                ToastUtils.showShort(getString(R.string.xiazhuchenggong));
                                                            }, ConversationActivity.this::handleApiError));
                                                    //关闭
                                                    holder.setOnClickListener(R.id.iv_close, v1 -> {
                                                        dd.dispose();
                                                        dialog.dismiss();
                                                    });
                                                }
                                            }).setDimAmount(0.5f)
                                                    .setOutCancel(false)
                                                    .show(getSupportFragmentManager());
                                        }
                                    } else {
                                        //积分不足
                                        ToastUtils.showShort(getString(R.string.jifen_buzhu) + balanceHK);
                                    }
                                });

                                gameWindowDisposable = gamePopupWindow.show(getGroupGameParameterResponse, 20);
                            }, ConversationActivity.this::handleApiError));
            }
            return false;
        };
    }

    private void registerOnTitleChange() {
        typingStatusListener = (type, targetId1, typingStatusSet) -> {
            //当输入状态的会话类型和targetID与当前会话一致时，才需要显示
            if (type.equals(Conversation.ConversationType.PRIVATE) && targetId1.equals(getIntent().getData().getQueryParameter("targetId"))) {
                //count表示当前会话中正在输入的用户数量，目前只支持单聊，所以判断大于0就可以给予显示了
                int count = typingStatusSet.size();
                if (count > 0) {
                    Iterator iterator = typingStatusSet.iterator();
                    TypingStatus status = (TypingStatus) iterator.next();
                    String objectName = status.getTypingContentType();

                    MessageTag textTag = TextMessage.class.getAnnotation(MessageTag.class);
                    MessageTag voiceTag = VoiceMessage.class.getAnnotation(MessageTag.class);
                    if (objectName.equals(textTag.value())) {
                        //显示“对方正在输入”
                        runOnUiThread(() -> tvTitle.setText(R.string.conversation_inputing));
                    } else if (objectName.equals(voiceTag.value())) {
                        //显示"对方正在讲话"
                        runOnUiThread(() -> tvTitle.setText(R.string.conversation_speaking));
                    }
                } else {
                    //当前会话没有用户正在输入，标题栏仍显示原来标题
                    runOnUiThread(() -> tvTitle.setText(targetUserInfo.getName()));
                }
            }
        };
        RongIMClient.setTypingStatusListener(typingStatusListener);
    }

    private String resolvePlugin() {
        //获取聊天类型（单聊、群聊）
        List<String> pathSegments = getIntent().getData().getPathSegments();
        return pathSegments.get(pathSegments.size() - 1);
    }

    private void handleBean(String conversationType) {
        targetId = getIntent().getData().getQueryParameter("targetId");

        List<IExtensionModule> moduleList = RongExtensionManager.getInstance().getExtensionModules();

        if (moduleList != null) {
            for (IExtensionModule module : moduleList) {
                if (module instanceof BasePluginExtensionModule) {
                    SampleTab sampleTab = (SampleTab) ((BasePluginExtensionModule) module).getList().get(1);
                    sampleTab.setTargetId(targetId);
                    sampleTab.setConversationType(conversationType);
                    break;
                }
            }
        }

        if (conversationType.equals("private")) {
            targetUserInfo = RongUserInfoManager.getInstance().getUserInfo(targetId);
            if (null == targetUserInfo) {
                // 私聊且未缓存
                ServiceFactory.getInstance().getBaseService(Api.class)
                        .getCustomerInfoById(targetId)
                        .compose(RxSchedulers.normalTrans())
                        .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                        .compose(bindToLifecycle())
                        .subscribe(loginResponse -> {
                            targetUserInfo = new UserInfo(targetId, loginResponse.getNick(), Uri.parse(loginResponse.getHeadPortrait()));
                            RongUserInfoManager.getInstance().setUserInfo(targetUserInfo);
                            handlePrivate();
                        }, ConversationActivity.this::handleApiError);
            } else {
                // 本地有缓存（私聊） 直接加载
                handlePrivate();
            }
        } else if (conversationType.equals("group")) {
            // 群聊必须每次请求
            ServiceFactory.getInstance().getBaseService(Api.class)
                    .getGroupByGroupId(targetId)
                    .compose(RxSchedulers.normalTrans())
                    .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                    .compose(bindToLifecycle())
                    .subscribe(groupInfo -> {
                        if (null != RongUserInfoManager.getInstance().getGroupInfo(groupInfo.getGroupInfo().getId())
                                && !RongUserInfoManager.getInstance().getGroupInfo(groupInfo.getGroupInfo().getId()).getName().equals(groupInfo.getGroupInfo().getGroupNikeName())) {
                            RongUserInfoManager.getInstance().setGroupInfo(new Group(groupInfo.getGroupInfo().getId(), groupInfo.getGroupInfo().getGroupNikeName(), Uri.parse(groupInfo.getGroupInfo().getHeadPortrait())));
                        }
                        memberIds = new ArrayList<>(groupInfo.getCustomers().size());
                        for (GroupResponse.CustomersBean bean : groupInfo.getCustomers()) {
                            memberIds.add(bean.getId());
                        }

                        List<IPluginModule> pluginModules = extension.getPluginModules();

                        if (groupInfo.getGroupInfo().getIsDelete().equals("1")) {
                            Iterator<IPluginModule> iterator = pluginModules.iterator();
                            while (iterator.hasNext()) {
                                IPluginModule next = iterator.next();
                                iterator.remove();
                                extension.removePlugin(next);
                            }
                        }

                        if (groupInfo.getGroupInfo().getGroupType().equals("1")) {
                            //游戏plugin

                            Iterator<IPluginModule> iterator = pluginModules.iterator();
                            while (iterator.hasNext()) {
                                IPluginModule next = iterator.next();
                                iterator.remove();
                                extension.removePlugin(next);
                            }
                            if (!groupInfo.getGroupInfo().getIsDelete().equals("1")) {
                                extension.addPlugin(new PhotoSelectorPlugin());
                                //上分 区分游戏群类型、是否群主
                                GameUpScorePlugin gameUpScorePlugin = new GameUpScorePlugin();
                                if (groupInfo.getGroupInfo().getGroupOwnerId().equals(Constant.userId)) {
                                    gameUpScorePlugin.setGroup(true);
                                } else {
                                    gameUpScorePlugin.setGroup(false);
                                }
                                gameUpScorePlugin.setGameType(groupInfo.getGroupInfo().getGameType());
                                extension.addPlugin(gameUpScorePlugin);
                                extension.addPlugin(new SightPlugin());
                                extension.addPlugin(new AudioVideoPlugin());
                                extension.addPlugin(new GameRecordPlugin());
                                extension.addPlugin(new GameDownScorePlugin());
                                GameRulesPlugin gameRulesPlugin = new GameRulesPlugin();
                                if (groupInfo.getGroupInfo().getGameType().equals("4")) {
                                    //多宝群
                                    if (!groupInfo.getGroupInfo().getGroupOwnerId().equals(Constant.userId)) {
                                        extension.addPlugin(new GameDuobaoPlugin());
                                    }
                                    gameRulesPlugin.duobao = "true";
                                } else if (!groupInfo.getGroupInfo().getGameType().equals("4") && groupInfo.getGroupInfo().getGroupOwnerId().equals(Constant.userId)) {
                                    //普通游戏群 只有群主才能开始下注
                                    extension.addPlugin(new GameStartPlugin());
                                }
                                extension.addPlugin(new GameJiaoYiPlugin());
                                extension.addPlugin(new FilePlugin());
                                extension.addPlugin(gameRulesPlugin);
                            }
                            Constant.ownerIdForGameChat = groupInfo.getGroupInfo().getGroupOwnerId();
                        } else {
                            //群组plugin
                            if (!groupInfo.getGroupInfo().getIsDelete().equals("1")) {
                                Iterator<IPluginModule> iterator = pluginModules.iterator();
                                while (iterator.hasNext()) {
                                    IPluginModule next = iterator.next();
                                    if (next instanceof TransferPlugin || next instanceof BusinessCardPlugin) {
                                        iterator.remove();
                                        extension.removePlugin(next);
                                    }
                                }
                            }
                        }
                        groupResponse = groupInfo;
                        initView();
                    }, ConversationActivity.this::handleApiError);
        }
    }

    private void handlePrivate() {
        initView();
        if (targetId.equals(Constant.userId)) {
            List<IPluginModule> pluginModules = extension.getPluginModules();
            Iterator<IPluginModule> iterator = pluginModules.iterator();
            while (iterator.hasNext()) {
                IPluginModule next = iterator.next();
                if (next instanceof TransferPlugin || next instanceof RedPacketPlugin) {
                    iterator.remove();
                    extension.removePlugin(next);
                }
            }
        }
    }

    private void handleClickMsg() {
        conversationClickListener = new RongIM.ConversationClickListener() {
            @Override
            public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo, String s) {
                if (conversationType == Conversation.ConversationType.GROUP) {
                    CommonUtils.resolveFriendList(ConversationActivity.this, userInfo.getUserId());
                } else {
                    Intent intent = new Intent(ConversationActivity.this, FriendDetailsActivity.class);
                    intent.putExtra("friendId", userInfo.getUserId());
                    startActivity(intent);
                }
                return true;
            }

            @Override
            public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo, String s) {
                return false;
            }

            @Override
            public boolean onMessageClick(Context context, View view, Message message) {
                switch (message.getObjectName()) {
                    case "RC:LBSMsg":
                        LocationMessage locationMessage = (LocationMessage) message.getContent();
                        Intent intent6 = new Intent(context, MessageLocationActivity.class);
                        intent6.putExtra("location", locationMessage);
                        startActivity(intent6);
                        return true;
                    case "MMyCardMsg":
                        BusinessCardMessage businessCardMessage = (BusinessCardMessage) message.getContent();
                        CommonUtils.resolveFriendList(ConversationActivity.this, businessCardMessage.getUserId());
                        break;
                    case "RC:ImgMsg":
                        Intent intent5 = new Intent(ConversationActivity.this, EnlargeImageActivity.class);
                        ArrayList<Message> messageList = new ArrayList<>();
                        RongIMClient.getInstance().getHistoryMessages(message.getConversationType(), message.getTargetId(), "RC:ImgMsg"
                                , message.getMessageId(), Integer.MAX_VALUE, RongCommonDefine.GetMessageDirection.FRONT, new RongIMClient.ResultCallback<List<Message>>() {
                                    @Override
                                    public void onSuccess(List<Message> messages) {
                                        if (messages != null) {
                                            Collections.reverse(messages);
                                            messageList.addAll(messages);
                                        }
                                        intent5.putExtra("index", messageList.size());
                                        messageList.add(message);
                                        RongIMClient.getInstance().getHistoryMessages(message.getConversationType(), message.getTargetId(), "RC:ImgMsg"
                                                , message.getMessageId(), Integer.MAX_VALUE, RongCommonDefine.GetMessageDirection.BEHIND, new RongIMClient.ResultCallback<List<Message>>() {
                                                    @Override
                                                    public void onSuccess(List<Message> messages) {
                                                        if (messages != null) {
                                                            messageList.addAll(messages);
                                                        }
                                                        Bundle bundle = new Bundle();
                                                        bundle.putParcelableArrayList("images", messageList);
                                                        intent5.putExtra("images", bundle);
                                                        intent5.putExtra("image", "");
                                                        startActivity(intent5,
                                                                ActivityOptionsCompat.makeSceneTransitionAnimation(ConversationActivity.this,
                                                                        view, "12").toBundle());
                                                    }

                                                    @Override
                                                    public void onError(RongIMClient.ErrorCode errorCode) {

                                                    }
                                                });
                                    }

                                    @Override
                                    public void onError(RongIMClient.ErrorCode errorCode) {

                                    }
                                });
                        return true;
                    case "app:transfer":
                        //转账
                        Intent intent = new Intent(context, TransferInfoActivity.class);
                        intent.putExtra("msg", message);
                        intent.putExtra("targetUserInfo", targetUserInfo);
                        startActivity(intent);
                        break;
                    case "MRedPackageMsg":
                        //红包
                        RedPacketMessage redPacketMessage = (RedPacketMessage) message.getContent();
                        //获取红包状态
                        ServiceFactory.getInstance().getBaseService(Api.class)
                                .getRedPackageStatus(redPacketMessage.getRedId(), redPacketMessage.getIsGame())
                                .compose(bindToLifecycle())
                                .compose(RxSchedulers.normalTrans())
                                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(ConversationActivity.this)))
                                .subscribe(s -> {
                                    if (TextUtils.isEmpty(message.getExtra())) {
                                        Constant.tempMsg = message;
                                    }
                                    if (s.getRedPackageState().equals("1")) {
                                        //红包已过期
                                        ExpiredEnvelopesDialog dialog = new ExpiredEnvelopesDialog(ConversationActivity.this);
                                        dialog.show(RongUserInfoManager.getInstance().getUserInfo(message.getSenderUserId()),
                                                true, redPacketMessage.getRedId());
                                    }
                                    if (s.getRedPackageState().equals("3")) {
                                        Intent intent1 = new Intent(context, PeopleUnaccalimedActivity.class);
                                        intent1.putExtra("isGame", redPacketMessage.getIsGame());
                                        intent1.putExtra("id", redPacketMessage.getRedId());
                                        startActivity(intent1);
                                    }
                                    if (s.getRedPackageState().equals("2")) {
                                        if (message.getConversationType().equals(Conversation.ConversationType.PRIVATE)) {
                                            Intent intent1 = new Intent(context, PeopleUnaccalimedActivity.class);
                                            intent1.putExtra("id", redPacketMessage.getRedId());
                                            if (message.getSenderUserId().equals(Constant.userId)) {
                                                intent1.putExtra("isShow", false);
                                            }
                                            startActivity(intent1);
                                        } else {
                                            //手慢了，已被领完
                                            ExpiredEnvelopesDialog dialog = new ExpiredEnvelopesDialog(ConversationActivity.this);
                                            dialog.show(RongUserInfoManager.getInstance().getUserInfo(message.getSenderUserId()),
                                                    false, redPacketMessage.getRedId());
                                        }
                                    }
                                    if (s.getRedPackageState().equals("0")) {
                                        //可领取
                                        RedEvelopesDialog dialog = new RedEvelopesDialog(ConversationActivity.this);
                                        if (message.getConversationType().equals(Conversation.ConversationType.PRIVATE) && message.getSenderUserId().equals(Constant.userId)) {
                                            Intent intent1 = new Intent(context, PeopleUnaccalimedActivity.class);
                                            intent1.putExtra("isShow", false);
                                            intent1.putExtra("id", redPacketMessage.getRedId());
                                            startActivity(intent1);
                                        } else if (message.getConversationType().equals(Conversation.ConversationType.GROUP)) {
                                            dialog.setOnOpenListener(() -> ServiceFactory.getInstance().getBaseService(Api.class)
                                                    .receiveGroupRedPackage(redPacketMessage.getRedId(), redPacketMessage.getIsGame())
                                                    .compose(bindToLifecycle())
                                                    .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(ConversationActivity.this)))
                                                    .compose(RxSchedulers.normalTrans())
                                                    .subscribe(s2 -> {
                                                        if (!message.getSenderUserId().equals(Constant.userId)) {
                                                            InformationNotificationMessage message1 = InformationNotificationMessage.obtain(Constant.currentUser.getNick() + "领取了"
                                                                    + s2.getSendCustomerInfo().getUsernick() + "的红包");
                                                            RongIM.getInstance().sendDirectionalMessage(Conversation.ConversationType.GROUP, groupResponse.getGroupInfo().getId(), message1, new String[]{message.getSenderUserId()}
                                                                    , null, null, null);
                                                        } else {
                                                            InformationNotificationMessage message1 = InformationNotificationMessage.obtain("你领取了你的红包");
                                                            RongIM.getInstance().sendDirectionalMessage(Conversation.ConversationType.GROUP, groupResponse.getGroupInfo().getId(), message1, new String[]{Constant.userId}
                                                                    , null, null, null);
                                                        }

                                                        Intent intent1 = new Intent(context, PeopleUnaccalimedActivity.class);
                                                        intent1.putExtra("id", redPacketMessage.getRedId());
                                                        intent1.putExtra("isGame", redPacketMessage.getIsGame());
                                                        if (redPacketMessage.getIsGame().equals("0")) {
                                                            //如果是游戏，20S内不允许查看红包记录页
                                                            Constant.canCheckRedRecord.incrementAndGet();
                                                            Observable.timer(20, TimeUnit.SECONDS, Schedulers.io())
                                                                    .subscribe(a -> Constant.canCheckRedRecord.decrementAndGet());
                                                        }
                                                        startActivity(intent1);
                                                    }, ConversationActivity.this::handleApiError));
                                            dialog.show(message, RongUserInfoManager.getInstance().getUserInfo(message.getSenderUserId()));
                                        } else {
                                            dialog.setOnOpenListener(() -> ServiceFactory.getInstance().getBaseService(Api.class)
                                                    .receivePersonalRedPackage(redPacketMessage.getRedId())
                                                    .compose(bindToLifecycle())
                                                    .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(ConversationActivity.this)))
                                                    .compose(RxSchedulers.normalTrans())
                                                    .subscribe(s1 -> {
                                                        InformationNotificationMessage message1 = InformationNotificationMessage.obtain(Constant.currentUser.getNick() + "领取了" +
                                                                s1.getSendUserInfo().getUsernick() + "的红包");
                                                        RongIM.getInstance().sendDirectionalMessage(Conversation.ConversationType.PRIVATE, targetId, message1, new String[]{targetId}
                                                                , null, null, null);

                                                        Intent intent2 = new Intent(ConversationActivity.this, PeopleRedEnvelopesActivity.class);
                                                        intent2.putExtra("msg", message);
                                                        startActivity(intent2);
                                                    }, ConversationActivity.this::handleApiError));
                                            dialog.show(message, RongUserInfoManager.getInstance().getUserInfo(message.getSenderUserId()));
                                        }
                                    }
                                }, t -> ToastUtils.showShort(RxException.getMessage(t)));
                        break;
                }
                return false;
            }

            @Override
            public boolean onMessageLinkClick(Context context, String s, Message message) {
                return false;
            }

            @Override
            public boolean onMessageLongClick(Context context, View view, Message message) {
                return false;
            }
        };
        RongIM.setConversationClickListener(conversationClickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Constant.tempMsg != null) {
            for (int i = 0; i < messageAdapter.getCount(); i++) {
                if (messageAdapter.getItem(i).getMessageId() == Constant.tempMsg.getMessageId()) {
                    RongIM.getInstance().setMessageExtra(Constant.tempMsg.getMessageId(), "1", null);
                    messageAdapter.getItem(i).setExtra("1");
                    messageAdapter.notifyDataSetInvalidated();
                    Constant.tempMsg = null;
                    break;
                }
            }
        }

    }

    private void detail() {
        if (groupResponse != null && groupResponse.getGroupInfo().getGroupType().equals("1")
                && !groupResponse.getGroupInfo().getGameType().equals("4")) {
            QuickPopupBuilder.with(this)
                    .contentView(R.layout.popup_conversation)
                    .config(new QuickPopupConfig()
                            .withClick(R.id.tv1, v -> handlePopwindow(1), true)
                            .withClick(R.id.tv2, v -> handlePopwindow(2), true)
                            .backgroundColor(Color.TRANSPARENT)
                            .gravity(Gravity.BOTTOM)
                            .withShowAnimation(AnimationUtils.loadAnimation(this, R.anim.push_scale_in))
                            .withDismissAnimation(AnimationUtils.loadAnimation(this, R.anim.push_scale_out)))
                    .show(R.id.rl_end);
        } else {
            handleNormalDetail();
        }
    }

    private void handlePopwindow(int flag) {
        if (flag == 1) {
            handleNormalDetail();
        } else {
            initChart();
        }
    }

    private void handleNormalDetail() {
        if (groupResponse != null && groupResponse.getGroupInfo().getIsDelete().equals("1")) {
            ToastUtils.showShort(R.string.deleted_group);
            return;
        }
        List<String> pathSegments = getIntent().getData().getPathSegments();
        String conversationType = pathSegments.get(pathSegments.size() - 1);

        Intent intent = new Intent(ConversationActivity.this, ChatInformationActivity.class);
        intent.putExtra("bean", targetUserInfo);

        if (conversationType.equals("private")) {
            startActivityForResult(intent, 1000);
        } else {
            if (groupResponse == null) {
                return;
            }
            Intent intent1 = new Intent(this, GroupChatInformationActivity.class);
            intent1.putExtra("group", groupResponse);
            startActivityForResult(intent1, 1000);
        }
    }

    private void initView() {
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(targetUserInfo == null ? (groupResponse.getGroupInfo().getGroupNikeName() + "(" + groupResponse.getCustomers().size() + ")") : targetUserInfo.getName());
        registerOnTitleChange();
    }

    private void initChart() {
        llChart.setVisibility(llChart.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        if (llChart.getVisibility() == View.GONE) {
            return;
        }
        chart = findViewById(R.id.chart);
        chart.setNoDataText("暂无数据");

        initRules();

        getChartData();
    }

    public void switchChart(View view) {
        currentOwnerGameType = currentOwnerGameType == 1 ? 2 : 1;
        setupChart(chart, getData(getGroupOwnerTrendResponse));
    }

    public void refreshChart(View view) {
        getChartData();
    }

    private GetGroupOwnerTrendResponse getGroupOwnerTrendResponse;

    private void getChartData() {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getGroupOwnerTrend(targetId)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver())
                .compose(RxSchedulers.normalTrans())
                .subscribe(s -> {
                    getGroupOwnerTrendResponse = s;
                    setupChart(chart, getData(s));
                }, this::handleApiError);
    }

    private void initRules() {
        niuniuRules = new ArrayList<>();
        niuniuRules.add("牛一");
        niuniuRules.add("牛二");
        niuniuRules.add("牛三");
        niuniuRules.add("牛四");
        niuniuRules.add("牛五");
        niuniuRules.add("牛六");
        niuniuRules.add("牛七");
        niuniuRules.add("牛八");
        niuniuRules.add("牛九");
        niuniuRules.add("牛牛");
        niuniuRules.add("金牛");
        niuniuRules.add("对子");
        niuniuRules.add("正顺");
        niuniuRules.add("倒顺");
        niuniuRules.add("满牛");
        niuniuRules.add("豹子");

        otherRules = new ArrayList<>();
        otherRules.add("零点");
        otherRules.add("一点");
        otherRules.add("二点");
        otherRules.add("三点");
        otherRules.add("四点");
        otherRules.add("五点");
        otherRules.add("六点");
        otherRules.add("七点");
        otherRules.add("八点");
        otherRules.add("九点");
    }

    private void setupChart(LineChart chart, LineData data) {
        if (data == null) {
            return;
        }

        chart.getDescription().setEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setScaleYEnabled(false);
        chart.setPinchZoom(false);

        chart.setData(data);

        chart.getLegend().setEnabled(false);
        chart.getAxisLeft().setEnabled(false);
        chart.getAxisRight().setEnabled(false);
        chart.getXAxis().setEnabled(false);
        chart.setDoubleTapToZoomEnabled(false);

        chart.notifyDataSetChanged();
        chart.setVisibleXRangeMaximum(12);
        chart.moveViewToX(data.getEntryCount());
        if (data.getEntryCount() <= 2) {
            return;
        }
        int animTime = data.getEntryCount() > 13 ? 1800 : (data.getEntryCount() * 150 - 150);
        chart.animateX(animTime);
    }

    private LineData getData(GetGroupOwnerTrendResponse r) {
        if (r == null || r.getNiuNiuTrend().size() == 0) {
            return null;
        }

        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < (currentOwnerGameType == 1 ? r.getNiuNiuTrend().size() : r.getBandDTrend().size()); i++) {
            values.add(new Entry(i, currentOwnerGameType == 1 ?
                    r.getNiuNiuTrend().get(i).getPoints() :
                    r.getBandDTrend().get(i).getPoints()));
        }

        tvChartTitle.setText(currentOwnerGameType == 1 ? "牛牛" : "大小单、百家乐");
        LineDataSet set = new LineDataSet(values, "");
        set.setLineWidth(1f);
        set.setCircleRadius(3f);
        set.setCircleHoleRadius(1f);
        set.setColor(ContextCompat.getColor(this, R.color.colorTheme));
        set.setCircleColor(ContextCompat.getColor(this, R.color.colorTheme));
        set.setCircleHoleColor(ContextCompat.getColor(this, R.color.colorTheme));
        set.setDrawValues(true);
        set.setValueTextSize(10);
        set.setHighlightEnabled(false);
        set.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float v) {
                return currentOwnerGameType == 1 ?
                        niuniuRules.get((int) v - 1) : otherRules.get((int) v);
            }
        });

        return new LineData(set);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == 1000) {
            if (groupResponse != null) {
                tvTitle.setText(data.getStringExtra("title") + "(" + groupResponse.getCustomers().size() + ")");
            } else {
                tvTitle.setText(data.getStringExtra("title"));
            }
        }
    }

    @Override
    protected void onDestroy() {
        onReceiveMessageListener = null;
        onSendMessageListener = null;
        typingStatusListener = null;
        conversationClickListener = null;
        RongIM.setOnReceiveMessageListener(null);
        RongIMClient.setTypingStatusListener(null);
        RongIM.getInstance().setSendMessageListener(null);
        RongIM.setConversationClickListener(null);
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        if (gameWindowDisposable != null && !gameWindowDisposable.isDisposed()) {
            gamePopupWindow.dismiss();
        }
        super.onStop();
    }
}
