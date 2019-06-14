package com.zxjk.duoduo.bean.response;

import java.util.List;

public class GetGameInfoByGroupIdResponse {

    /**
     * playCommissionConfigList : [{"id":"366","min":"1.00","max":"100000.00","grade":"会员级","commission":"0.0050","groupId":"152","createTime":"","remarks":"每万返佣50元"},{"id":"367","min":"100001.00","max":"500000.00","grade":"超级会员","commission":"0.0080","groupId":"152","createTime":"","remarks":"每万返佣80元"},{"id":"368","min":"500001.00","max":"1000000.00","grade":"代理级","commission":"0.0100","groupId":"152","createTime":"","remarks":"每万返佣100元"},{"id":"369","min":"1000001.00","max":"3000000.00","grade":"超级代理","commission":"0.0120","groupId":"152","createTime":"","remarks":"每万返佣120元"},{"id":"370","min":"3000001.00","max":"5000000.00","grade":"总代级","commission":"0.0140","groupId":"152","createTime":"","remarks":"每万返佣140元"},{"id":"371","min":"5000001.00","max":"10000000.00","grade":"超级总代","commission":"0.0160","groupId":"152","createTime":"","remarks":"每万返佣160元"},{"id":"372","min":"10000001.00","max":"20000000.00","grade":"总监","commission":"0.0180","groupId":"152","createTime":"","remarks":"每万返佣180元"},{"id":"373","min":"20000001.00","max":"50000000.00","grade":"超级总监","commission":"0.0200","groupId":"152","createTime":"","remarks":"每万返佣200元"}]
     * group : {"id":"152","groupType":"1","groupNikeName":"朋友小聚","groupHeadPortrait":"","groupSign":"","groupNotice":"","groupOwnerId":"5","updateTime":"","createTime":"1560318044053","isDelete":"0","isInviteConfirm":"0","headPortrait":"","duoduoId":"","pumpingRate":"0.01","systemPumpingRate":"","gameType":"1","playId":"9,25,16","duobaoMultiple":""}
     */

    public GroupBean group;
    private List<PlayCommissionConfigListBean> playCommissionConfigList;

    public GroupBean getGroup() {
        return group;
    }

    public void setGroup(GroupBean group) {
        this.group = group;
    }

    public List<PlayCommissionConfigListBean> getPlayCommissionConfigList() {
        return playCommissionConfigList;
    }

    public void setPlayCommissionConfigList(List<PlayCommissionConfigListBean> playCommissionConfigList) {
        this.playCommissionConfigList = playCommissionConfigList;
    }

    public static class GroupBean {
        /**
         * id : 152
         * groupType : 1
         * groupNikeName : 朋友小聚
         * groupHeadPortrait :
         * groupSign :
         * groupNotice :
         * groupOwnerId : 5
         * updateTime :
         * createTime : 1560318044053
         * isDelete : 0
         * isInviteConfirm : 0
         * headPortrait :
         * duoduoId :
         * pumpingRate : 0.01
         * systemPumpingRate :
         * gameType : 1
         * playId : 9,25,16
         * duobaoMultiple :
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
        private String duoduoId;
        private String pumpingRate;
        private String systemPumpingRate;
        private String gameType;
        private String playId;
        private String duobaoMultiple;
        private String groupOwnerNick;

        public String getGroupOwnerNick() {
            return groupOwnerNick;
        }

        public void setGroupOwnerNick(String groupOwnerNick) {
            this.groupOwnerNick = groupOwnerNick;
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

        public String getDuoduoId() {
            return duoduoId;
        }

        public void setDuoduoId(String duoduoId) {
            this.duoduoId = duoduoId;
        }

        public String getPumpingRate() {
            return pumpingRate;
        }

        public void setPumpingRate(String pumpingRate) {
            this.pumpingRate = pumpingRate;
        }

        public String getSystemPumpingRate() {
            return systemPumpingRate;
        }

        public void setSystemPumpingRate(String systemPumpingRate) {
            this.systemPumpingRate = systemPumpingRate;
        }

        public String getGameType() {
            return gameType;
        }

        public void setGameType(String gameType) {
            this.gameType = gameType;
        }

        public String getPlayId() {
            return playId;
        }

        public void setPlayId(String playId) {
            this.playId = playId;
        }

        public String getDuobaoMultiple() {
            return duobaoMultiple;
        }

        public void setDuobaoMultiple(String duobaoMultiple) {
            this.duobaoMultiple = duobaoMultiple;
        }
    }

    public static class PlayCommissionConfigListBean {
        /**
         * id : 366
         * min : 1.00
         * max : 100000.00
         * grade : 会员级
         * commission : 0.0050
         * groupId : 152
         * createTime :
         * remarks : 每万返佣50元
         */

        private String id;
        private String min;
        private String max;
        private String grade;
        private String commission;
        private String groupId;
        private String createTime;
        private String remarks;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMin() {
            return min;
        }

        public void setMin(String min) {
            this.min = min;
        }

        public String getMax() {
            return max;
        }

        public void setMax(String max) {
            this.max = max;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getCommission() {
            return commission;
        }

        public void setCommission(String commission) {
            this.commission = commission;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }
    }
}
