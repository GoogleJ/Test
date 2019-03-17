package com.zxjk.duoduo;

import com.blankj.utilcode.util.DeviceUtils;
import java.util.Locale;

public class Constant {

    public static final String BASE_URL = "http://192.168.0.108:8085/";

    public static final int CODE_SUCCESS = 0;

    public static String userId = "";

    public static String token = "";

    public static String phoneUuid = DeviceUtils.getMacAddress()
            .equals("") ? DeviceUtils.getAndroidID() : DeviceUtils.getMacAddress();

    public static String language = Locale.getDefault().getLanguage();
}
