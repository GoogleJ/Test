package com.zxjk.duoduo.bean.response;

import java.util.List;

public class GetDuoBaoIntegralDetailsResponse {
    /**
     * customerId :
     * title : 金多寶第2019064期
     * time : 1559995449000
     * integral :
     * remainingIntegral : 561195.00
     * type : 2
     * expect : 2019064
     * betSum : 350
     * betCount : 2
     * betInfo : [{"betCode":"01","betMoneyForCode":"150"},{"betCode":"08","betMoneyForCode":"50"},{"betCode":"18","betMoneyForCode":"50"},{"betCode":"24","betMoneyForCode":"100"}]
     */

    private String customerId;
    private String title;
    private String time;
    private String integral;
    private String remainingIntegral;
    private String type;
    private String expect;
    private String betSum;
    private String betCount;
    private List<BetInfoBean> betInfo;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public String getRemainingIntegral() {
        return remainingIntegral;
    }

    public void setRemainingIntegral(String remainingIntegral) {
        this.remainingIntegral = remainingIntegral;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExpect() {
        return expect;
    }

    public void setExpect(String expect) {
        this.expect = expect;
    }

    public String getBetSum() {
        return betSum;
    }

    public void setBetSum(String betSum) {
        this.betSum = betSum;
    }

    public String getBetCount() {
        return betCount;
    }

    public void setBetCount(String betCount) {
        this.betCount = betCount;
    }

    public List<BetInfoBean> getBetInfo() {
        return betInfo;
    }

    public void setBetInfo(List<BetInfoBean> betInfo) {
        this.betInfo = betInfo;
    }

    public static class BetInfoBean {
        /**
         * betCode : 01
         * betMoneyForCode : 150
         */

        private String betCode;
        private String betMoneyForCode;

        public String getBetCode() {
            return betCode;
        }

        public void setBetCode(String betCode) {
            this.betCode = betCode;
        }

        public String getBetMoneyForCode() {
            return betMoneyForCode;
        }

        public void setBetMoneyForCode(String betMoneyForCode) {
            this.betMoneyForCode = betMoneyForCode;
        }
    }
}
