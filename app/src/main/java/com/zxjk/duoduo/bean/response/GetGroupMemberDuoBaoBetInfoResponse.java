package com.zxjk.duoduo.bean.response;

import java.util.List;

public class GetGroupMemberDuoBaoBetInfoResponse {

    /**
     * expect : 2019059
     * openCode : 19
     * betInfo : [{"betCode":"2","betMoneyForCode":"100.00"},{"betCode":"2","betMoneyForCode":"100.00"}]
     * betSum : 300.00
     * opentime : 2019-05-23 21:35:10
     * winerList : ["grant","丁"]
     */

    private String expect;
    private String openCode;
    private String betSum;
    private String opentime;
    private List<BetInfoBean> betInfo;
    private List<String> winerList;

    public String getExpect() {
        return expect;
    }

    public void setExpect(String expect) {
        this.expect = expect;
    }

    public String getOpenCode() {
        return openCode;
    }

    public void setOpenCode(String openCode) {
        this.openCode = openCode;
    }

    public String getBetSum() {
        return betSum;
    }

    public void setBetSum(String betSum) {
        this.betSum = betSum;
    }

    public String getOpentime() {
        return opentime;
    }

    public void setOpentime(String opentime) {
        this.opentime = opentime;
    }

    public List<BetInfoBean> getBetInfo() {
        return betInfo;
    }

    public void setBetInfo(List<BetInfoBean> betInfo) {
        this.betInfo = betInfo;
    }

    public List<String> getWinerList() {
        return winerList;
    }

    public void setWinerList(List<String> winerList) {
        this.winerList = winerList;
    }

    public static class BetInfoBean {
        /**
         * betCode : 2
         * betMoneyForCode : 100.00
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
