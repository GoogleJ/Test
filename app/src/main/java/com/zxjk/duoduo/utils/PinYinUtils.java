package com.zxjk.duoduo.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinYinUtils {

    public static String converterToFirstSpell(String chines) {
        StringBuilder pinyinName = new StringBuilder();
        char[] nameChar = chines.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (char aNameChar : nameChar) {
            if (aNameChar > 128) {
                try {
                    String[] strings = PinyinHelper.toHanyuPinyinStringArray(
                            aNameChar, defaultFormat);
                    if (strings == null) {
                        return "#";
                    }
                    pinyinName.append(strings[0].charAt(0));
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pinyinName.append(aNameChar);
            }
        }
        return pinyinName.toString().substring(0, 1).toUpperCase();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 123; i++) {
            char c = (char) i;
            System.out.print(i);
            System.out.println(c);
        }
    }
}
