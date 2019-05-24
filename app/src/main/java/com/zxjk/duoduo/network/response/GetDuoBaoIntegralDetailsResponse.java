package com.zxjk.duoduo.network.response;

public class GetDuoBaoIntegralDetailsResponse {

    /**
     * title : 金多寶第2019060期
     * time : 1558710821803
     * integral : 300.00
     * remainingIntegral : 400.00
     * type : 2
     * expect : 2019060
     */

    private String title;
    private String time;
    private String integral;
    private String remainingIntegral;
    private String type;
    private String expect;

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
