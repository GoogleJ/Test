package com.zxjk.duoduo.network.response;

import java.util.List;

public class GetRedPackageRecordResponse {

    /**
     * redpackageList : [{"nick":"中兴极客14","money":0.1037,"createTime":"1551326357494","message":"恭喜发财，大吉大利","TYPE":"1","redPackageType":"1"},{"nick":"中兴极客15","money":15.0001,"createTime":"1551333742449","message":"恭喜发财，大吉大利","TYPE":"2","redPackageType":"2"}]
     * totalRecord : {"count":2,"total_money":15.1038}
     */

    private TotalRecordBean totalRecord;
    private List<RedpackageListBean> redpackageList;

    public TotalRecordBean getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(TotalRecordBean totalRecord) {
        this.totalRecord = totalRecord;
    }

    public List<RedpackageListBean> getRedpackageList() {
        return redpackageList;
    }

    public void setRedpackageList(List<RedpackageListBean> redpackageList) {
        this.redpackageList = redpackageList;
    }

    public static class TotalRecordBean {
        /**
         * count : 2
         * total_money : 15.1038
         */

        private int count;
        private double total_money;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public double getTotal_money() {
            return total_money;
        }

        public void setTotal_money(double total_money) {
            this.total_money = total_money;
        }
    }

    public static class RedpackageListBean {
        /**
         * nick : 中兴极客14
         * money : 0.1037
         * createTime : 1551326357494
         * message : 恭喜发财，大吉大利
         * TYPE : 1
         * redPackageType : 1
         */

        private String nick;
        private double money;
        private String createTime;
        private String message;
        private String TYPE;
        private String redPackageType;

        public String getNick() {
            return nick;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getTYPE() {
            return TYPE;
        }

        public void setTYPE(String TYPE) {
            this.TYPE = TYPE;
        }

        public String getRedPackageType() {
            return redPackageType;
        }

        public void setRedPackageType(String redPackageType) {
            this.redPackageType = redPackageType;
        }
    }
}
