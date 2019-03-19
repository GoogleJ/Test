package com.zxjk.duoduo;

import com.blankj.utilcode.util.DeviceUtils;
import com.zxjk.duoduo.network.response.LoginResponse;

public class Constant {
    //阿里OSS上传地址
    public static final String OSS_URL = "https://zhongxingjike.oss-cn-hongkong.aliyuncs.com/upload/";

    public static final String BASE_URL = "http://192.168.0.108:8085/";

    public static final int CODE_SUCCESS = 0;

    public static final int CODE_UNLOGIN = 601;

//    public static String userId = "4";

//    public static String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI0IiwiaXNzIjoiZHVvZHVvIiwianRpIjoiMzIxNjU0MTExIn0.JdddTri7172qkvHKXDfmRDPR6HatCTpHxiAkdiI7GQY";

//    public static String phoneUuid ="321654111";

    public static String userId="20";
    public static String token="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyMCIsImlzcyI6ImR1b2R1byIsImp0aSI6IjA0OjRmOjRjOjljOjgwOjFkIn0.MAxAE2cluTL2y7ZKi2_vWYCW6YKfWmUle_-OIOlB6qM";
    public static String phoneUuid = DeviceUtils.getMacAddress()
            .equals("") ? DeviceUtils.getAndroidID() : DeviceUtils.getMacAddress();
//    public static String userId="";
//    public static String token="";


//    public static String language = Locale.getDefault().getLanguage();
    public static String language = "zh-TW";

    public static LoginResponse currentUser = new LoginResponse();





}
