package com.zxjk.duoduo.network.response;

import java.util.List;

public class GetNumbeOfTransactionResponse {

    /**
     * hkPrice : 0.88
     * NumbeOfTransaction : ["100","500","1000","5004"]
     */

    private String hkPrice;
    private List<String> NumbeOfTransaction;

    public String getHkPrice() {
        return hkPrice;
    }

    public void setHkPrice(String hkPrice) {
        this.hkPrice = hkPrice;
    }

    public List<String> getNumbeOfTransaction() {
        return NumbeOfTransaction;
    }

    public void setNumbeOfTransaction(List<String> NumbeOfTransaction) {
        this.NumbeOfTransaction = NumbeOfTransaction;
    }
}
