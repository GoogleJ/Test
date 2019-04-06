package com.zxjk.duoduo.network.response;

import java.io.Serializable;

public class GetOverOrderResponse implements Serializable {

    /**
     * id : 6
     * buyId :
     * buyDuoduoId :
     * sellId : 4
     * sellDuoduoId :
     * bothOrderId : 15518722638311000000510000004
     * number : 500
     * money : 500.00
     * currency : 1
     * payType : 1
     * createTime : 1551872263831
     * closeTime : 1551876423945
     * status : 1
     * picture : https://zhongxingjike.oss-cn-hongkong.aliyuncs.com/upload/FC888443-8DAF-4501-B7A7-5EAD7139657A.jpg
     * isBuyPay : 1
     * payTime :
     * isDelete : 0
     */

    private String id;
    private String buyId;
    private String buyDuoduoId;
    private String sellId;
    private String sellDuoduoId;
    private String bothOrderId;
    private String number;
    private String money;
    private String currency;
    private String payType;
    private String createTime;
    private String closeTime;
    private String status;
    private String picture;
    private String isBuyPay;
    private String payTime;
    private String isDelete;

    public String getSellPayType() {
        return sellPayType;
    }

    public void setSellPayType(String sellPayType) {
        this.sellPayType = sellPayType;
    }

    private String sellPayType;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBuyId() {
        return buyId;
    }

    public void setBuyId(String buyId) {
        this.buyId = buyId;
    }

    public String getBuyDuoduoId() {
        return buyDuoduoId;
    }

    public void setBuyDuoduoId(String buyDuoduoId) {
        this.buyDuoduoId = buyDuoduoId;
    }

    public String getSellId() {
        return sellId;
    }

    public void setSellId(String sellId) {
        this.sellId = sellId;
    }

    public String getSellDuoduoId() {
        return sellDuoduoId;
    }

    public void setSellDuoduoId(String sellDuoduoId) {
        this.sellDuoduoId = sellDuoduoId;
    }

    public String getBothOrderId() {
        return bothOrderId;
    }

    public void setBothOrderId(String bothOrderId) {
        this.bothOrderId = bothOrderId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getIsBuyPay() {
        return isBuyPay;
    }

    public void setIsBuyPay(String isBuyPay) {
        this.isBuyPay = isBuyPay;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }
}
