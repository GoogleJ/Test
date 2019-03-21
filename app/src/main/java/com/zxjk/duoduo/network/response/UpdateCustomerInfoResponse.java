package com.zxjk.duoduo.network.response;

/**
 * @author Administrator
 * @// TODO: 2019\3\21 0021 实名认证 
 */
public class UpdateCustomerInfoResponse {

    /**
     * id : 4
     * duoduoId : 10000004
     * nick : 你好11
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
     * updateTime : 2019-02-23 16:33:57.0
     * createTime : null
     * isDelete : 0
     * token : null
     * remark : null
     */

    private String id;
    private String duoduoId;
    private String nick;
    private Object realname;
    private String mobile;
    private String password;
    private Object email;
    private Object headPortrait;
    private Object sex;
    private Object signature;
    private Object walletAddress;
    private Object idCard;
    private Object isShowRealname;
    private String updateTime;
    private Object createTime;
    private String isDelete;
    private Object token;
    private Object remark;

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

    public Object getRealname() {
        return realname;
    }

    public void setRealname(Object realname) {
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

    public Object getEmail() {
        return email;
    }

    public void setEmail(Object email) {
        this.email = email;
    }

    public Object getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(Object headPortrait) {
        this.headPortrait = headPortrait;
    }

    public Object getSex() {
        return sex;
    }

    public void setSex(Object sex) {
        this.sex = sex;
    }

    public Object getSignature() {
        return signature;
    }

    public void setSignature(Object signature) {
        this.signature = signature;
    }

    public Object getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(Object walletAddress) {
        this.walletAddress = walletAddress;
    }

    public Object getIdCard() {
        return idCard;
    }

    public void setIdCard(Object idCard) {
        this.idCard = idCard;
    }

    public Object getIsShowRealname() {
        return isShowRealname;
    }

    public void setIsShowRealname(Object isShowRealname) {
        this.isShowRealname = isShowRealname;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Object getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Object createTime) {
        this.createTime = createTime;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public Object getToken() {
        return token;
    }

    public void setToken(Object token) {
        this.token = token;
    }

    public Object getRemark() {
        return remark;
    }

    public void setRemark(Object remark) {
        this.remark = remark;
    }
}
