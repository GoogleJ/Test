package com.zxjk.duoduo;

import com.blankj.utilcode.util.DeviceUtils;
import com.zxjk.duoduo.bean.AddPayInfoBean;
import com.zxjk.duoduo.network.response.FriendInfoResponse;
import com.zxjk.duoduo.bean.VerifiedBean;
import com.zxjk.duoduo.network.response.CreateWalletResponse;
import com.zxjk.duoduo.network.response.GroupChatResponse;
import com.zxjk.duoduo.network.response.GroupResponse;
import com.zxjk.duoduo.network.response.LoginResponse;

public class Constant {
    //阿里OSS上传地址
    public static final String OSS_URL = "https://zhongxingjike.oss-cn-hongkong.aliyuncs.com/upload/";
    public static final String BASE_URL = "http://192.168.0.115:8085/";
//    public static final String BASE_URL = "http://192.168.1.3:8085/";
//    public static final String BASE_URL="http://192.168.0.110:8085/";

    public static final int CODE_SUCCESS = 0;

    public static final int CODE_UNLOGIN = 601;

    public static final String FLAG_IS_Delete = "1";
    public static final String FLAG_FIRSTLOGIN = "0";

    public static final String HEAD_LOCATION = "86-";

    public static String userId = "";
    public static String token = "";
    public static String phoneUuid = DeviceUtils.getMacAddress()
            .equals("") ? DeviceUtils.getAndroidID() : DeviceUtils.getMacAddress();

    public static String language = "zh-TW";

    public static LoginResponse currentUser = new LoginResponse();

    public static void clear() {
        currentUser = null;
    }

    /**
     * 新增（修改收款方式）实体
     */
    public static AddPayInfoBean  payInfoBean=new AddPayInfoBean();


    /**
     * 群Id
     */
    public static String groupId="";

    /**
     * 点击的群的详细信息
     */
    public static GroupChatResponse groupResponse=new GroupChatResponse();

    /**
     * 获取好友列表
     */
    public static FriendInfoResponse friendInfoResponse=new FriendInfoResponse();



    public static CreateWalletResponse walletResponse;

}
