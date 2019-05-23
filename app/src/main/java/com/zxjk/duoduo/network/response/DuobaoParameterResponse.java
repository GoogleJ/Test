package com.zxjk.duoduo.network.response;

public class DuobaoParameterResponse {

    /**
     * duobaoMultiple : 49.00
     * minimumBetAmount : 50.00
     * maximumBetAmount : 100.00
     * expect : 2019057
     */

    private String duobaoMultiple;
    private String minimumBetAmount;
    private String maximumBetAmount;
    private String expect;

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
}
