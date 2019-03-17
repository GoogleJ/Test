package com.zxjk.duoduo.network.response;

import java.io.Serializable;

public class LoginResponse implements Serializable {

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsAuthentication() {
        return isAuthentication;
    }

    public void setIsAuthentication(String isAuthentication) {
        this.isAuthentication = isAuthentication;
    }

    /**
     * id : 14
     * duoduoId : 10000000
     * nick :
     * realname :
     * mobile : 15249047866
     * password : e10adc3949ba59abbe56e057f20f883e
     * email :
     * headPortrait : https://zhongxingjike.oss-cn-hongkong.aliyuncs.com/upload/FC888443-8DAF-4501-B7A7-5EAD7139657A.jpg
     * sex : 0
     * signature :
     * walletAddress :
     * idCard :
     * isShowRealname : 0
     * updateTime : 1551081292117
     * createTime : 1551081292117
     * isDelete : 0
     * token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxNCJ9.KHSJP1cnYY7vrIGqJkULoBJbsqLdZV0DkdhPyCNgIy0
     * remark :
     * payPwd :
     * rongToken : juanMR8aw2u04GgR+7Uk5702a5n+rzXoZ3Aj3/5nogndTFT6YOzQPMZFP/QmJpCO7EUA7BEGUpwqFHLxR/8ruA==
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
    /**
     * 违约次数
     */
    private String renegeNumber;
    /**
     * 是否限制买卖账单
     */
    private String isConfine;
    private String status;
    /**
     * 是否实名认证
     */
    private String isAuthentication;

    public LoginResponse(String id) {
        this.id = id;
    }

    public LoginResponse() {
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
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
                ", status='" + status + '\'' +
                ", isAuthentication='" + isAuthentication + '\'' +
                '}';
    }
}
