package com.zxjk.duoduo.utils;

import android.content.Context;
import android.content.res.Resources;


import com.zxjk.duoduo.R;
import com.zxjk.duoduo.bean.CountryEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 国家代码解析选择工具类
 */
public class CountryCodeConstantsUtils {
    public static final String CODE_CHINA = "86";
    public static final String CODE_CHINA_TAIWAN = "886";
    public static final String CODE_CHINA_HK = "852";

    public static final int REQUESTCODE_COUNTRY_SELECT = 200;//国家选择

    /**
     * 获取国家列表
     *
     * @param context
     * @return
     */
    public static ArrayList<CountryEntity> getCountryList(Context context) {
        ArrayList<CountryEntity> countryList = new ArrayList<CountryEntity>();
        Resources res = context.getResources();
        String[] countries = res.getStringArray(R.array.country);
        String[] countryCodes = res.getStringArray(R.array.country_code);
        int size = countries.length;
        for (int i = 0; i < size; i++) {
            String countryName = countries[i];
            CountryEntity countryEntity = new CountryEntity();
            countryEntity.countryName = (countryName);
            countryEntity.countryCode = (countryCodes[i]);
            countryEntity.sortKey = (PinYinUtils.getLeadingLo(countryName));
            countryEntity.pinyin = (PinYinUtils.getPinYin(countryName));
            countryList.add(countryEntity);
        }
        return countryList;
    }

    public static Map<String, String> getCountryCodeMap(Context context) {
        HashMap<String, String> countryMap = new HashMap<String, String>();
        Resources res = context.getResources();
        String[] countries = res.getStringArray(R.array.country);
        String[] countryCodes = res.getStringArray(R.array.country_code);
        int size = countries.length;
        for (int i = 0; i < size; i++) {
            countryMap.put(countryCodes[i], countries[i]);
        }
        return countryMap;
    }
}
