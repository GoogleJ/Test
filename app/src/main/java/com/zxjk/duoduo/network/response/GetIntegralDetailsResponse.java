package com.zxjk.duoduo.network.response;

public class GetIntegralDetailsResponse {
    /**
     * title : 上分
     * time : 1555685330417
     * integral : 30000.00
     * remainingIntegral :
     * type : 0
     * settlementCardType :
     */

    private String title;
    private String time;
    private String integral;
    private String remainingIntegral;
    private String type;
    private String settlementCardType;

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

    public String getSettlementCardType() {
        return settlementCardType;
    }

    public void setSettlementCardType(String settlementCardType) {
        this.settlementCardType = settlementCardType;
    }
}
