package com.zxjk.duoduo.bean;

/**
 * @author Administrator
 * @// TODO: 2019\3\25 0025 支付类型的实体 
 */
public class AddPayInfoBean {

    private String payType;
    private String payPicture;
    private String payNumber;
    private String wechatNick;
    private String zhifubaoNumber;
    private String openBank;


    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayPicture() {
        return payPicture;
    }

    public void setPayPicture(String payPicture) {
        this.payPicture = payPicture;
    }

    public String getPayNumber() {
        return payNumber;
    }

    public void setPayNumber(String payNumber) {
        this.payNumber = payNumber;
    }

    public String getWechatNick() {
        return wechatNick;
    }

    public void setWechatNick(String wechatNick) {
        this.wechatNick = wechatNick;
    }

    public String getZhifubaoNumber() {
        return zhifubaoNumber;
    }

    public void setZhifubaoNumber(String zhifubaoNumber) {
        this.zhifubaoNumber = zhifubaoNumber;
    }

    public String getOpenBank() {
        return openBank;
    }

    public void setOpenBank(String openBank) {
        this.openBank = openBank;
    }



}
