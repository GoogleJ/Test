package com.zxjk.duoduo.network.response;

import java.util.List;

public class DuobaoParameterResponse {


    /**
     * duobaoMultiple : 41.50
     * minimumBetAmount : 1
     * maximumBetAmount : 100
     * expect : 2019063
     * chineseZodiac : [{"zodiac":"鼠","code":"12,24,36,48","sort":"00"},{"zodiac":"牛","code":"11,23,35,47","sort":"01"},{"zodiac":"虎","code":"10,22,34,46","sort":"02"},{"zodiac":"兔","code":"09,21,33,45","sort":"03"},{"zodiac":"龙","code":"08,20,32,44","sort":"04"},{"zodiac":"蛇","code":"07,19,31,43","sort":"05"},{"zodiac":"马","code":"06,18,30,42","sort":"06"},{"zodiac":"羊","code":"05,17,29,41","sort":"07"},{"zodiac":"猴","code":"04,16,28,40","sort":"08"},{"zodiac":"鸡","code":"03,15,27,39","sort":"09"},{"zodiac":"狗","code":"02,14,26,38","sort":"10"},{"zodiac":"猪","code":"01,13,25,37,49","sort":"11"}]
     */

    private String duobaoMultiple;
    private String minimumBetAmount;
    private String maximumBetAmount;
    private String expect;
    private List<ChineseZodiacBean> chineseZodiac;

    public String getDuobaoMultiple() {
        return duobaoMultiple;
    }

    public void setDuobaoMultiple(String duobaoMultiple) {
        this.duobaoMultiple = duobaoMultiple;
    }

    public String getMinimumBetAmount() {
        return minimumBetAmount;
    }

    public void setMinimumBetAmount(String minimumBetAmount) {
        this.minimumBetAmount = minimumBetAmount;
    }

    public String getMaximumBetAmount() {
        return maximumBetAmount;
    }

    public void setMaximumBetAmount(String maximumBetAmount) {
        this.maximumBetAmount = maximumBetAmount;
    }

    public String getExpect() {
        return expect;
    }

    public void setExpect(String expect) {
        this.expect = expect;
    }

    public List<ChineseZodiacBean> getChineseZodiac() {
        return chineseZodiac;
    }

    public void setChineseZodiac(List<ChineseZodiacBean> chineseZodiac) {
        this.chineseZodiac = chineseZodiac;
    }

    public static class ChineseZodiacBean implements Cloneable{
        /**
         * zodiac : 鼠
         * code : 12,24,36,48
         * sort : 00
         */

        private String zodiac;
        private String code;
        private String sort;
        private int money;
        private boolean isChecked;

        public Object clone() {
            ChineseZodiacBean o = null;
            try {
                o = (ChineseZodiacBean) super.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return o;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }

        public String getZodiac() {
            return zodiac;
        }

        public void setZodiac(String zodiac) {
            this.zodiac = zodiac;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }
    }
}
