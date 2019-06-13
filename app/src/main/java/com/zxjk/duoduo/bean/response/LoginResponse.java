package com.zxjk.duoduo.bean.response;

import android.os.Parcel;
import android.os.Parcelable;

public class LoginResponse implements Parcelable {
    /**
     * id : 4
     * duoduoId : 10000004
     * nick : 4号用户
     * realname :
     * mobile : 18202987805
     * password : 14e1b600b1fd579f47433b88e8d85291
     * address : beijing
     * email :
     * headPortrait : https://zhongxingjike.oss-cn-hongkong.aliyuncs.com/upload/FC888443-8DAF-4501-B7A7-5EAD7139657A.jpg
     * sex : 0
     * signature : 饿了了
     * walletAddress :
     * idCard :
     * isShowRealname : 0
     * updateTime : 1552839507662
     * createTime :
     * isDelete : 0
     * token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI0IiwiaXNzIjoiZHVvZHVvIiwianRpIjoiMzIxNjU0MTExIn0.JdddTri7172qkvHKXDfmRDPR6HatCTpHxiAkdiI7GQY
     * remark :
     * rongToken : NxgHGk1qzACem1Js1tLQE702a5n+rzXoZ3Aj3/5nogmk0P2rdaAtUQ+ptQpowx2j1NHoPEbm8e4qFHLxR/8ruA==
     * payPwd :
     * isFirstLogin : 1
     * renegeNumber : 0
     * isConfine : 0
     * status :
     * isAuthentication : 1.未实名 2.认证中 0.已实名
     * onlineService : https://tb.53kf.com/code/client/10197755/1
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
    private String status;
    private String isAuthentication;
    private String onlineService;

    public LoginResponse(String id) {
        this.id = id;
    }

    public LoginResponse() {
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

    public String getOnlineService() {
        return onlineService;
    }

    public void setOnlineService(String onlineService) {
        this.onlineService = onlineService;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.duoduoId);
        dest.writeString(this.nick);
        dest.writeString(this.realname);
        dest.writeString(this.mobile);
        dest.writeString(this.password);
        dest.writeString(this.address);
        dest.writeString(this.email);
        dest.writeString(this.headPortrait);
        dest.writeString(this.sex);
        dest.writeString(this.signature);
        dest.writeString(this.walletAddress);
        dest.writeString(this.idCard);
        dest.writeString(this.isShowRealname);
        dest.writeString(this.updateTime);
        dest.writeString(this.createTime);
        dest.writeString(this.isDelete);
        dest.writeString(this.token);
        dest.writeString(this.remark);
        dest.writeString(this.rongToken);
        dest.writeString(this.payPwd);
        dest.writeString(this.isFirstLogin);
        dest.writeString(this.renegeNumber);
        dest.writeString(this.isConfine);
        dest.writeString(this.status);
        dest.writeString(this.isAuthentication);
        dest.writeString(this.onlineService);
    }

    protected LoginResponse(Parcel in) {
        this.id = in.readString();
        this.duoduoId = in.readString();
        this.nick = in.readString();
        this.realname = in.readString();
        this.mobile = in.readString();
        this.password = in.readString();
        this.address = in.readString();
        this.email = in.readString();
        this.headPortrait = in.readString();
        this.sex = in.readString();
        this.signature = in.readString();
        this.walletAddress = in.readString();
        this.idCard = in.readString();
        this.isShowRealname = in.readString();
        this.updateTime = in.readString();
        this.createTime = in.readString();
        this.isDelete = in.readString();
        this.token = in.readString();
        this.remark = in.readString();
        this.rongToken = in.readString();
        this.payPwd = in.readString();
        this.isFirstLogin = in.readString();
        this.renegeNumber = in.readString();
        this.isConfine = in.readString();
        this.status = in.readString();
        this.isAuthentication = in.readString();
        this.onlineService = in.readString();
    }

    public static final Parcelable.Creator<LoginResponse> CREATOR = new Parcelable.Creator<LoginResponse>() {
        @Override
        public LoginResponse createFromParcel(Parcel source) {
            return new LoginResponse(source);
        }

        @Override
        public LoginResponse[] newArray(int size) {
            return new LoginResponse[size];
        }
    };
}
