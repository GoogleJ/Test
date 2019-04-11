package com.zxjk.duoduo;

import android.text.TextUtils;

import com.blankj.utilcode.util.DeviceUtils;
import com.zxjk.duoduo.bean.AddPayInfoBean;
import com.zxjk.duoduo.network.response.FriendInfoResponse;
import com.zxjk.duoduo.bean.VerifiedBean;
import com.zxjk.duoduo.network.response.CreateWalletResponse;
import com.zxjk.duoduo.network.response.GroupChatResponse;
import com.zxjk.duoduo.network.response.GroupResponse;
import com.zxjk.duoduo.network.response.LoginResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import java.util.Locale;

public class Constant {
    //阿里OSS上传地址
    public static final String OSS_URL = "https://zhongxingjike.oss-cn-hongkong.aliyuncs.com/upload/";
    //    public static final String BASE_URL = "http://192.168.1.3:8085/";
//    public static final String BASE_URL = "http://192.168.0.115:8085/";
        public static final String BASE_URL="http://192.168.0.110:8085/";
//    public static final String BASE_URL = "http://47.75.115.118:8086/";

    public static final int CODE_SUCCESS = 0;

    public static final int CODE_UNLOGIN = 601;

    public static final String FLAG_IS_DELETE = "1";
    public static final String FLAG_FIRSTLOGIN = "0";
    public static final String HEAD_LOCATION = "86-";
    public static String userId = "";
    public static String token = "";
    public static String phoneUuid =
            TextUtils.isEmpty(DeviceUtils.getMacAddress()) ? DeviceUtils.getAndroidID() : DeviceUtils.getMacAddress();
    public static String language = Locale.getDefault().getLanguage();
    public static LoginResponse currentUser = new LoginResponse();

    public static void clear() {
        Constant.token = "";
        Constant.userId = "";
        Constant.phoneUuid = "";
        currentUser = null;
        walletResponse = null;
        friendsList = new ArrayList<>();
    }

    /**
     * 新增（修改收款方式）实体
     */
    public static CreateWalletResponse walletResponse;
    public static GroupChatResponse groupChatResponse = new GroupChatResponse();
    public static List<FriendInfoResponse> friendsList = new ArrayList();

}
