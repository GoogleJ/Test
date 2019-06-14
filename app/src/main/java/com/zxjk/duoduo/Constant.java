package com.zxjk.duoduo;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.blankj.utilcode.util.DeviceUtils;
import com.zxjk.duoduo.bean.response.CreateWalletResponse;
import com.zxjk.duoduo.bean.response.FriendInfoResponse;
import com.zxjk.duoduo.bean.response.LoginResponse;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import io.rong.imlib.model.Message;

public class Constant {
    //省市区数据
    public static final String CITY_DATA = "china_city_data.json";
    //阿里OSS上传地址
    public static final String OSS_URL = "https://zhongxingjike2.oss-cn-hongkong.aliyuncs.com/upload/";
//    public static final String BASE_URL = "https://192.168.0.104:8085/";
    public static final String BASE_URL = "https://duoduoweb.zzgb.net.cn";
    public static final String APP_CODE = "fb0e95b069f74f29a2f972f9454d7d1a";
    public static boolean isVerifyVerision = false;

    public static final int CODE_SUCCESS = 0;

    public static final int CODE_UNLOGIN = 601;

    public static final String FLAG_IS_DELETE = "1";
    public static final String FLAG_FIRSTLOGIN = "0";
    public static String HEAD_LOCATION = "86";
    public static String userId = "";
    public static String token = "";
    public static String phoneUuid =
            TextUtils.isEmpty(DeviceUtils.getMacAddress()) ? DeviceUtils.getAndroidID() : DeviceUtils.getMacAddress();
    public static String language = Locale.getDefault().toString().replace("_", "-");

    public static String authentication = "";

    public static LoginResponse currentUser = new LoginResponse();

    public static final String LOCAL_CHANNEL_ID = "rc_notification_id";

    public static int messageCount = 0;

    public static CreateWalletResponse walletResponse;
    public static List<FriendInfoResponse> friendsList;
    public static Message tempMsg;
    public static String changeGroupName = null;
    public static String ownerIdForGameChat = "";

    //是否可以查看红包记录页，默认为0，可查看。 每开启一个游戏
    public static AtomicInteger canCheckRedRecord = new AtomicInteger(0);

    //分享群二维码
    public static Bitmap shareGroupQR;

    public static void clear() {
        Constant.token = "";
        Constant.userId = "";
        Constant.phoneUuid = "";
        currentUser = null;
        walletResponse = null;
        friendsList = null;
        tempMsg = null;
        changeGroupName = null;
        ownerIdForGameChat = null;
        shareGroupQR = null;
    }

}
