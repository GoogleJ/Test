package com.zxjk.duoduo.network.response;

import java.util.List;

public class GetGameClassResponse {

    private List<GroupClassBean> groupClass;
    private List<CommissionConfigBean> commissionConfig;

    public List<GroupClassBean> getGroupClass() {
        return groupClass;
    }

    public void setGroupClass(List<GroupClassBean> groupClass) {
        this.groupClass = groupClass;
    }

    public List<CommissionConfigBean> getCommissionConfig() {
        return commissionConfig;
    }

    public void setCommissionConfig(List<CommissionConfigBean> commissionConfig) {
        this.commissionConfig = commissionConfig;
    }

    public static class GroupClassBean {
        /**
         * id : 1
         * typeName : 朋友小聚
         * numberLimit : 10
         * guaranteeFee : 5000.00
         * minimumBetAmount : 1.00
         * maximumBetAmount : 50.00
         * proportionOfFees : 0.01
         * englishName :
         */

        private String id;
        private String typeName;
        private String numberLimit;
        private String guaranteeFee;
        private String minimumBetAmount;
        private String maximumBetAmount;
        private String proportionOfFees;
        private String englishName;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public String getNumberLimit() {
            return numberLimit;
        }

        public void setNumberLimit(String numberLimit) {
            this.numberLimit = numberLimit;
        }

        public String getGuaranteeFee() {
            return guaranteeFee;
        }

        public void setGuaranteeFee(String guaranteeFee) {
            this.guaranteeFee = guaranteeFee;
        }

        public String getMinimumBetAmount() {
            return minimumBetAmount;
        }

        public void setMinimumBetAmount(String minimumBetAmount) {
            this.minimumBetAmount = minimumBetAmount;
        }

        public String getMaximumBetAmount() {
            return maximumBetAmount;
        }

        public void setMaximumBetAmount(String maximumBetAmount) {
            this.maximumBetAmount = maximumBetAmount;
        }

        public String getProportionOfFees() {
            return proportionOfFees;
        }

        public void setProportionOfFees(String proportionOfFees) {
            this.proportionOfFees = proportionOfFees;
        }

        public String getEnglishName() {
            return englishName;
        }

        public void setEnglishName(String englishName) {
            this.englishName = englishName;
        }
    }

    public static class CommissionConfigBean {
        /**
         * id : 1
         * min : 100000
         * max : 10000
         * grade : 会员级
         * commission : 0.00500000
         * groupId :
         * createTime :
         * remarks :
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
