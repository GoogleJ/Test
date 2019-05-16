package com.zxjk.duoduo.network.response;

import java.io.Serializable;
import java.util.List;

/**
 * @author Administrator
 */
public class GroupResponse implements Serializable {


    /**
     * groupInfo : {"id":"71","groupType":"0","groupNikeName":"Zhaochen_4的讨论组","groupHeadPortrait":"","groupSign":"","groupNotice":"","groupOwnerId":"17","updateTime":"","createTime":"1553581323053","isDelete":"0","isInviteConfirm":"0","headPortrait":""}
     * customers : [{"id":"17","duoduoId":"","nick":"Zhaochen_4","realname":"","mobile":"","password":"","address":"","email":"","headPortrait":"https://zhongxingjike.oss-cn-hongkong.aliyuncs.com/upload/A324124A-A577-4980-AF66-684D1CE44376.jpg","sex":"","signature":"","walletAddress":"","idCard":"","isShowRealname":"","updateTime":"","createTime":"","isDelete":"","token":"","remark":"","rongToken":"","payPwd":"","isFirstLogin":"","renegeNumber":"","isConfine":"","status":"","isAuthentication":"","onlineService":""},{"id":"14","duoduoId":"","nick":"14号用户","realname":"","mobile":"","password":"","address":"","email":"","headPortrait":"https://zhongxingjike.oss-cn-hongkong.aliyuncs.com/upload/FC888443-8DAF-4501-B7A7-5EAD7139657A.jpg","sex":"","signature":"","walletAddress":"","idCard":"","isShowRealname":"","updateTime":"","createTime":"","isDelete":"","token":"","remark":"","rongToken":"","payPwd":"","isFirstLogin":"","renegeNumber":"","isConfine":"","status":"","isAuthentication":"","onlineService":""},{"id":"4","duoduoId":"","nick":"丁浩","realname":"","mobile":"","password":"","address":"","email":"","headPortrait":"https://zhongxingjike.oss-cn-hongkong.aliyuncs.com/upload/E1795CE5-2A56-4C53-A9EE-E916FBBFC383.jpg","sex":"","signature":"","walletAddress":"","idCard":"","isShowRealname":"","updateTime":"","createTime":"","isDelete":"","token":"","remark":"","rongToken":"","payPwd":"","isFirstLogin":"","renegeNumber":"","isConfine":"","status":"","isAuthentication":"","onlineService":""},{"id":"26","duoduoId":"","nick":"王倩","realname":"","mobile":"","password":"","address":"","email":"","headPortrait":"https://zhongxingjike.oss-cn-hongkong.aliyuncs.com/upload/C5B24F00-AD58-4DF4-83FD-DC6217CC24BA.jpg","sex":"","signature":"","walletAddress":"","idCard":"","isShowRealname":"","updateTime":"","createTime":"","isDelete":"","token":"","remark":"","rongToken":"","payPwd":"","isFirstLogin":"","renegeNumber":"","isConfine":"","status":"","isAuthentication":"","onlineService":""}]
     */

    private GroupInfoBean groupInfo;
    private String maxNumber;
    private List<CustomersBean> customers;

    public String getMaxNumber() {
        return maxNumber;
    }

    public void setMaxNumber(String maxNumber) {
        this.maxNumber = maxNumber;
    }

    public GroupInfoBean getGroupInfo() {
        return groupInfo;
    }

    public void setGroupInfo(GroupInfoBean groupInfo) {
        this.groupInfo = groupInfo;
    }

    public List<CustomersBean> getCustomers() {
        return customers;
    }

    public void setCustomers(List<CustomersBean> customers) {
        this.customers = customers;
    }

    public static class GroupInfoBean implements Serializable {
        /**
         * id : 71
         * groupType : 0
         * groupNikeName : Zhaochen_4的讨论组
         * groupHeadPortrait :
         * groupSign :
         * groupNotice :
         * groupOwnerId : 17
         * updateTime :
         * createTime : 1553581323053
         * isDelete : 0
         * isInviteConfirm : 0
         * headPortrait :
         */

        private String id;
        private String groupType;
        private String groupNikeName;
        private String groupHeadPortrait;
        private String groupSign;
        private String groupNotice;
        private String groupOwnerId;
        private String updateTime;
        private String createTime;
        private String isDelete;
        private String isInviteConfirm;
        private String headPortrait;
        private String systemPumpingRate;

        public String getSystemPumpingRate() {
            return systemPumpingRate;
        }

        public void setSystemPumpingRate(String systemPumpingRate) {
            this.systemPumpingRate = systemPumpingRate;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGroupType() {
            return groupType;
        }

        public void setGroupType(String groupType) {
            this.groupType = groupType;
        }

        public String getGroupNikeName() {
            return groupNikeName;
        }

        public void setGroupNikeName(String groupNikeName) {
            this.groupNikeName = groupNikeName;
        }

        public String getGroupHeadPortrait() {
            return groupHeadPortrait;
        }

        public void setGroupHeadPortrait(String groupHeadPortrait) {
            this.groupHeadPortrait = groupHeadPortrait;
        }

        public String getGroupSign() {
            return groupSign;
        }

        public void setGroupSign(String groupSign) {
            this.groupSign = groupSign;
        }

        public String getGroupNotice() {
            return groupNotice;
        }

        public void setGroupNotice(String groupNotice) {
            this.groupNotice = groupNotice;
        }

        public String getGroupOwnerId() {
            return groupOwnerId;
        }

        public void setGroupOwnerId(String groupOwnerId) {
            this.groupOwnerId = groupOwnerId;
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

        public String getIsInviteConfirm() {
            return isInviteConfirm;
        }

        public void setIsInviteConfirm(String isInviteConfirm) {
            this.isInviteConfirm = isInviteConfirm;
        }

        public String getHeadPortrait() {
            return headPortrait;
        }

        public void setHeadPortrait(String headPortrait) {
            this.headPortrait = headPortrait;
        }
    }

    public static class CustomersBean implements Serializable {
        /**
         * id : 17
         * duoduoId :
         * nick : Zhaochen_4
         * realname :
         * mobile :
         * password :
         * address :
         * email :
         * headPortrait : https://zhongxingjike.oss-cn-hongkong.aliyuncs.com/upload/A324124A-A577-4980-AF66-684D1CE44376.jpg
         * sex :
         * signature :
         * walletAddress :
         * idCard :
         * isShowRealname :
         * updateTime :
         * createTime :
         * isDelete :
         * token :
         * remark :
         * rongToken :
         * payPwd :
         * isFirstLogin :
         * renegeNumber :
         * isConfine :
         * status :
         * isAuthentication :
         * onlineService :
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
        private boolean checked;
        private String firstLetter;

        public String getFirstLetter() {
            return firstLetter;
        }

        public void setFirstLetter(String firstLetter) {
            this.firstLetter = firstLetter;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
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

    }
}
