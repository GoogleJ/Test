package com.zxjk.duoduo.network.response;

/**
 * @author Administrator
 * @// TODO: 2019\3\21 0021 好友相关实体 
 */
public class FriendInfoResponse {


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
    private String email;
    private String headPortrait;
    private String sex;
    private String signature;
    private String walletAddress;
    private String idCard;
    private String isShowRealname;
    private String updataTime;
    private String createTime;
    private String isDelete;
    private String token;
    private String remark;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private String address;


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

    public String getUpdataTime() {
        return updataTime;
    }

    public void setUpdataTime(String updataTime) {
        this.updataTime = updataTime;
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
}
