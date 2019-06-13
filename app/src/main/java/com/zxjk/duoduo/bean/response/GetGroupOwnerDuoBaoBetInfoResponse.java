package com.zxjk.duoduo.bean.response;

public class GetGroupOwnerDuoBaoBetInfoResponse {


    /**
     * title : grant
     * time : 1558703898014
     * integral : 200.00
     * remainingIntegral : 700.00
     * type : 1
     * expect : 2019059
     */

    private String title;
    private String time;
    private String integral;
    private String remainingIntegral;
    private String type;
    private String expect;
    private String customerId;

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
}
