package com.zxjk.duoduo.network.response;

import java.io.Serializable;

/**
 * @author Administrator
 * @// TODO: 2019\3\21 0021 好友相关实体 
 */
public class FriendInfoResponse implements Serializable {


    /**
     * id : 1
     * duoduoId : 10000001
     * nick : null
     * realname : null
     * mobile : 18202987805
     * password : e10adc3949ba59abbe56e057f20f883e
     * email : null
     * headPortrait : null
     * sex : null
     * signature : null
     * walletAddress : null
     * idCard : null
     * isShowRealname : null
     * updataTime : null
     * createTime : null
     * isDelete : 0
     * token : null
     * remark : null
     */

    private String id;
    private String duoduoId;
    private String nick;
    private String realname;
    private String mobile;
    private String password;
    private String address;
    private String email;
    private String headPortrait;
    private String sex;
    private String signature;
    private String walletAddress;
    private String idCard;
    private String isShowRealname;
    private String updateTime;
    private String createTime;
    private String isDelete;
    private String token;
    private String remark;
    private String rongToken;
    private String payPwd;
    private String isFirstLogin;
    private String renegeNumber;
    private String isConfine;
    private String  sortLetters;
    private String status;

    public FriendInfoResponse(FriendInfoResponse friendInfoResponse) {
        this.id =friendInfoResponse. id;
        this.duoduoId =friendInfoResponse. duoduoId;
        this.nick =friendInfoResponse. nick;
        this.realname =friendInfoResponse. realname;
        this.mobile =friendInfoResponse. mobile;
        this.password =friendInfoResponse. password;
        this.address =friendInfoResponse. address;
        this.email =friendInfoResponse. email;
        this.headPortrait =friendInfoResponse. headPortrait;
        this.sex =friendInfoResponse. sex;
        this.signature =friendInfoResponse. signature;
        this.walletAddress =friendInfoResponse. walletAddress;
        this.idCard =friendInfoResponse. idCard;
        this.isShowRealname =friendInfoResponse. isShowRealname;
        this.updateTime =friendInfoResponse. updateTime;
        this.createTime =friendInfoResponse. createTime;
        this.isDelete =friendInfoResponse. isDelete;
        this.token =friendInfoResponse. token;
        this.remark =friendInfoResponse. remark;
        this.rongToken =friendInfoResponse. rongToken;
        this.payPwd =friendInfoResponse. payPwd;
        this.isFirstLogin =friendInfoResponse. isFirstLogin;
        this.renegeNumber =friendInfoResponse. renegeNumber;
        this.isConfine =friendInfoResponse. isConfine;
        this.sortLetters =friendInfoResponse. sortLetters;
        this.status=friendInfoResponse.status;
    }
    public FriendInfoResponse() {

    }

    @Override
    public String toString() {
        return "FriendInfoResponse{" +
                "id='" + id + '\'' +
                ", duoduoId='" + duoduoId + '\'' +
                ", nick='" + nick + '\'' +
                ", realname='" + realname + '\'' +
                ", mobile='" + mobile + '\'' +
                ", password='" + password + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", headPortrait='" + headPortrait + '\'' +
                ", sex='" + sex + '\'' +
                ", signature='" + signature + '\'' +
                ", walletAddress='" + walletAddress + '\'' +
                ", idCard='" + idCard + '\'' +
                ", isShowRealname='" + isShowRealname + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", createTime='" + createTime + '\'' +
                ", isDelete='" + isDelete + '\'' +
                ", token='" + token + '\'' +
                ", remark='" + remark + '\'' +
                ", rongToken='" + rongToken + '\'' +
                ", payPwd='" + payPwd + '\'' +
                ", isFirstLogin='" + isFirstLogin + '\'' +
                ", renegeNumber='" + renegeNumber + '\'' +
                ", isConfine='" + isConfine + '\'' +
                ", sortLetters='" + sortLetters + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDuoduoId() {
        return duoduoId;
    }

    public void setDuoduoId(String duoduoId) {
        this.duoduoId = duoduoId;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getIsShowRealname() {
        return isShowRealname;
    }

    public void setIsShowRealname(String isShowRealname) {
        this.isShowRealname = isShowRealname;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRongToken() {
        return rongToken;
    }

    public void setRongToken(String rongToken) {
        this.rongToken = rongToken;
    }

    public String getPayPwd() {
        return payPwd;
    }

    public void setPayPwd(String payPwd) {
        this.payPwd = payPwd;
    }

    public String getIsFirstLogin() {
        return isFirstLogin;
    }

    public void setIsFirstLogin(String isFirstLogin) {
        this.isFirstLogin = isFirstLogin;
    }

    public String getRenegeNumber() {
        return renegeNumber;
    }

    public void setRenegeNumber(String renegeNumber) {
        this.renegeNumber = renegeNumber;
    }

    public String getIsConfine() {
        return isConfine;
    }

    public void setIsConfine(String isConfine) {
        this.isConfine = isConfine;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }
}
