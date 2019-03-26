package com.zxjk.duoduo;

import com.blankj.utilcode.util.DeviceUtils;
import com.zxjk.duoduo.bean.AddPayInfoBean;
import com.zxjk.duoduo.bean.VerifiedBean;
import com.zxjk.duoduo.network.response.LoginResponse;

public class Constant {
    //阿里OSS上传地址
    public static final String OSS_URL = "https://zhongxingjike.oss-cn-hongkong.aliyuncs.com/upload/";

    public static final String BASE_URL = "http://192.168.0.114:8085/";

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
     * 实名认证实体
     */
    public static VerifiedBean verifiedBean=new VerifiedBean();
}
