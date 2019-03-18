package com.zxjk.duoduo.utils;

import android.content.Context;

public class UserPreferencesUtils {
    private static volatile UserPreferencesUtils mPreferences;
    private SharedPreManagerUtils sharedPreManager;
    public final static String USERINFO = "userinfo";
    //USERINFO里面键
    private static final String KEY_USER_PHONE = "mobile";
    private static final String KEY_USER_COUNTRY_CODE = "country_code";

    private static final String KEY_USER_TOKEN = "token";
    private static final String KEY_USER_TOKEN_RONG = "token_rong";
    private static final String KEY_USER_ID = "uid";

    private static final String KEY_USER_COINS = "coins";
    private static final String KEY_USER_DIAMOND = "diamond";
    private static final String KEY_COLLECT_HAND_NUM = "collect_hand_num";
    static final String KEY_ZHANGYU_ID = "uuid";
    static final String KEY_USER_NICK = "nick";
    static final String KEY_NICKNAME_TIMES = "nickname_times";

    static final String KEY_USER_sex = "sex";
    static final String KEY_USER_PID_CODE = "pid_code";

    static final String KEY_USER_JWTTOKEN = "jwtToken";
    static final String KEY_USER_AGENT_COINS = "agent_coins";
    static final String KEY_USER_NICKNAME_COINS = "nickname_coins";
    static final String KEY_USER_AGENT_LEVEL = "agent_level";
    static final String KEY_USER_OPEN_SERVICE = "close_service";
    private final String KEY_USER_PASSWORD = "password";

    private UserPreferencesUtils(Context context) {
        sharedPreManager = SharedPreManagerUtils.getInstance(context);
    }

    public static UserPreferencesUtils getInstance(Context context) {
        if (mPreferences == null) {
            mPreferences = new UserPreferencesUtils(context);
        }
        return mPreferences;
    }

    /**
     * 设置用户手机号
     */
    public void setUserMobile(String mobile) {
        sharedPreManager.putString(KEY_USER_PHONE, mobile);
    }

    /**
     * 获取用户帐号
     */
    public String getUserPhone() {
        return sharedPreManager.getString(KEY_USER_PHONE, "");
    }

    public void setUserPassword(String password) {
        sharedPreManager.putString(KEY_USER_PASSWORD, password);
    }

    public String getUserPassword() {
        return sharedPreManager.getString(KEY_USER_PASSWORD, "");
    }


    /**
     * 获取用户手机号国际区号
     */
    public String getUserCountryCode() {
        return sharedPreManager.getString(KEY_USER_COUNTRY_CODE, "86");
    }

    /**
     * 设置用户手机号国际区号
     */
    public void setUserCountryCode(String countryCode) {
        sharedPreManager.putString(KEY_USER_COUNTRY_CODE, countryCode);
    }

    /**
     * 设置用户TOKEN
     */
    public void setUserToken(String token) {
        sharedPreManager.putString(KEY_USER_TOKEN, token);
    }

    /**
     * 获取用户TOKEN
     */
    public String getUserToken() {
        return sharedPreManager.getString(KEY_USER_TOKEN, "");
    }

    /**
     * 设置用户UID
     */
    public void setUserId(String account) {
        sharedPreManager.putString(KEY_USER_ID, account);
    }

    /**
     * 获取用户帐号
     */
    public String getUserId() {
        return sharedPreManager.getString(KEY_USER_ID, "");
    }

    /**
     * 设置用户金币余额
     */
    public void setCoins(int coins) {
        sharedPreManager.putInt(KEY_USER_COINS, coins);
    }

    /**
     * 获取用户昵称
     */
    public int getCoins() {
        return sharedPreManager.getInt(KEY_USER_COINS, 0);
    }

    /**
     * 设置用户宝石
     */
    public void setDiamond(int diamond) {
        sharedPreManager.putInt(KEY_USER_DIAMOND, diamond);
    }

    /**
     * 获取用户宝石
     */
    public int getDiamond() {
        return sharedPreManager.getInt(KEY_USER_DIAMOND, 0);
    }

    //新增用户战鱼号
    public void setZYId(String zyid) {
        sharedPreManager.putString(KEY_ZHANGYU_ID, zyid);
    }

    public String getZYId() {
        return sharedPreManager.getString(KEY_ZHANGYU_ID, "0");
    }

    /**
     * 设置牌谱收藏数量
     */
    public void setCollectHandNum(int diamond) {
        sharedPreManager.putInt(KEY_COLLECT_HAND_NUM, diamond);
    }

    /**
     * 获取牌谱收藏数量
     */
    public int getCollectHandNum() {
        return sharedPreManager.getInt(KEY_COLLECT_HAND_NUM, 0);
    }

    /**
     * 设置第几次修改昵称
     *
     * @param nickname_times
     */
    public void setNicknameTimes(int nickname_times) {
        sharedPreManager.putInt(KEY_NICKNAME_TIMES, nickname_times);
    }

    /**
     * 获取nicknametimes
     *
     * @return
     */
    public int getNicknameTimes() {
        return sharedPreManager.getInt(KEY_NICKNAME_TIMES, 0);
    }


//    public void saveUser(User user) {
//        sharedPreManager.putString(KEY_USER_ID, user.getId());
//        sharedPreManager.putString(KEY_USER_TOKEN, user.getToken());
//        sharedPreManager.putString(KEY_USER_TOKEN_RONG, user.getRongToken());
//        sharedPreManager.putString(KEY_USER_PHONE, user.getMobile());
//        sharedPreManager.putString(KEY_USER_NICK, user.getNick());
//        sharedPreManager.putInt(KEY_USER_sex, user.getSex());
//    }

    public void clear() {
        sharedPreManager.clear();
    }

    public void setRongToken(String rongToken) {
        sharedPreManager.putString(KEY_USER_TOKEN_RONG, rongToken);
    }

    public String getRongToken() {
        return sharedPreManager.getString(KEY_USER_TOKEN_RONG);
    }
}
