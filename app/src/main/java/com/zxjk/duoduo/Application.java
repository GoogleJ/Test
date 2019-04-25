package com.zxjk.duoduo;

import android.app.ActivityManager;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.blankj.utilcode.util.Utils;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zxjk.duoduo.ui.msgpage.rongIMAdapter.BasePluginExtensionModule;
import com.zxjk.duoduo.ui.msgpage.rongIMAdapter.BusinessCardMessage;
import com.zxjk.duoduo.ui.msgpage.rongIMAdapter.BusinessCardProvider;
import com.zxjk.duoduo.ui.msgpage.rongIMAdapter.GroupCardMessage;
import com.zxjk.duoduo.ui.msgpage.rongIMAdapter.GroupCardProvider;
import com.zxjk.duoduo.ui.msgpage.rongIMAdapter.RedPacketMessage;
import com.zxjk.duoduo.ui.msgpage.rongIMAdapter.RedPacketProvider;
import com.zxjk.duoduo.ui.msgpage.rongIMAdapter.TransferMessage;
import com.zxjk.duoduo.ui.msgpage.rongIMAdapter.TransferProvider;
import com.zxjk.duoduo.utils.WeChatShareUtil;

import java.util.List;

import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.IExtensionModule;
import io.rong.imkit.RongExtensionManager;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.ImageMessage;
import io.rong.message.RichContentMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

public class Application extends android.app.Application {

    public static OSS oss;

    private static final String APP_ID = "wx95412ba899539c33";

    private void regToWx() {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        WeChatShareUtil.wxShare = WXAPIFactory.createWXAPI(this, APP_ID, true);
        // 将应用的appId注册到微信
        WeChatShareUtil.wxShare.registerApp(APP_ID);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //微信分享
        regToWx();

        //工具类初始化
        Utils.init(this);

        //OSS初始化
        new Thread(this::initOSS).start();

        MultiDex.install(this);

        //融云初始化
        RongIM.init(this);
        RongIM.registerMessageType(RedPacketMessage.class);
        RongIM.registerMessageType(BusinessCardMessage.class);
        RongIM.registerMessageType(TransferMessage.class);
        RongIM.registerMessageType(GroupCardMessage.class);
        RongIM.registerMessageTemplate(new RedPacketProvider());
        RongIM.registerMessageTemplate(new TransferProvider());
        RongIM.registerMessageTemplate(new BusinessCardProvider());
        RongIM.registerMessageTemplate(new GroupCardProvider());
        RongIM.getInstance().setMessageAttachedUserInfo(true);
        setMyExtensionModule();
    }

    @Override
    public void onTerminate() {
        Constant.clear();
        super.onTerminate();
    }

    //初始化阿里云OSS上传服务
    private void initOSS() {
        String AK = "LTAI8wpEjXW0r2y9";
        String SK = "4Dv0UhSB16KLPKC8GR6DuH9GTEGil7";
        OSSPlainTextAKSKCredentialProvider ossPlainTextAKSKCredentialProvider =
                new OSSPlainTextAKSKCredentialProvider(AK, SK);
        String endpoint = "oss-cn-hongkong.aliyuncs.com";
        oss = new OSSClient(this, endpoint, ossPlainTextAKSKCredentialProvider);

    }

    /**
     * 这个是对融云的方法进行的包装
     *
     * @param context
     * @return
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    private class MySendMessageListener implements RongIM.OnSendMessageListener {

        /**
         * 消息发送前监听器处理接口（是否发送成功可以从 SentStatus 属性获取）。
         *
         * @param message 发送的消息实例。
         * @return 处理后的消息实例。
         */
        @Override
        public Message onSend(Message message) {
            //开发者根据自己需求自行处理逻辑
            return message;
        }

        /**
         * 消息在 UI 展示后执行/自己的消息发出后执行,无论成功或失败。
         *
         * @param message              消息实例。
         * @param sentMessageErrorCode 发送消息失败的状态码，消息发送成功 SentMessageErrorCode 为 null。
         * @return true 表示走自已的处理方式，false 走融云默认处理方式。
         */
        @Override
        public boolean onSent(Message message, RongIM.SentMessageErrorCode sentMessageErrorCode) {

            if (message.getSentStatus() == Message.SentStatus.FAILED) {
                if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_CHATROOM) {
                    //不在聊天室
                } else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_DISCUSSION) {
                    //不在讨论组
                } else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_GROUP) {
                    //不在群组
                } else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.REJECTED_BY_BLACKLIST) {
                    //你在他的黑名单中
                }
            }

            MessageContent messageContent = message.getContent();

            if (messageContent instanceof TextMessage) {//文本消息
                TextMessage textMessage = (TextMessage) messageContent;
//                Log.d(TAG, "onSent-TextMessage:" + textMessage.getContent());
            } else if (messageContent instanceof ImageMessage) {//图片消息
                ImageMessage imageMessage = (ImageMessage) messageContent;
//                Log.d(TAG, "onSent-ImageMessage:" + imageMessage.getRemoteUri());
            } else if (messageContent instanceof VoiceMessage) {//语音消息
                VoiceMessage voiceMessage = (VoiceMessage) messageContent;
//                Log.d(TAG, "onSent-voiceMessage:" + voiceMessage.getUri().toString());
            } else if (messageContent instanceof RichContentMessage) {//图文消息
                RichContentMessage richContentMessage = (RichContentMessage) messageContent;
//                Log.d(TAG, "onSent-RichContentMessage:" + richContentMessage.getContent());
            } else {
//                Log.d(TAG, "onSent-其他消息，自己来判断处理");
            }

            return false;
        }
    }

    //修改会话页底部按钮
    public static void setMyExtensionModule() {
        List<IExtensionModule> moduleList = RongExtensionManager.getInstance().getExtensionModules();

        if (moduleList != null) {
            IExtensionModule defaultModule = null;
            for (IExtensionModule module : moduleList) {
                if (module instanceof DefaultExtensionModule) {
                    defaultModule = module;
                    break;
                }
            }
            RongExtensionManager.getInstance().unregisterExtensionModule(defaultModule);
            RongExtensionManager.getInstance().registerExtensionModule(new BasePluginExtensionModule());
        }
    }
}
