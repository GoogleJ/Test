package com.zxjk.duoduo.bean.response;

import java.util.List;

public class GetGroupGameParameterResponse {

    /**
     * parentList : [{"playId":"9","playName":"牛牛","childList":[{"playId":"15","playName":"豹子","multiple":"6"}]},{"playId":"16","playName":"大小单双合","childList":[{"playId":"17","playName":"小","multiple":"6"},{"playId":"18","playName":"大","multiple":"6"}]},{"playId":"25","playName":"百家乐","childList":[{"playId":"26","playName":"庄","multiple":"6"},{"playId":"27","playName":"闲","multiple":"6"},{"playId":"28","playName":"合","multiple":"6"},{"playId":"29","playName":"庄对","multiple":"6"},{"playId":"30","playName":"闲对","multiple":"6"}]}]
     * maxBet : 50000.00
     * minBet : 50.00
     */

    private String maxBet;
    private String minBet;
    private String balanceHK;

    public String getBalanceHK() {
        return balanceHK;
    }

    public void setBalanceHK(String balanceHK) {
        this.balanceHK = balanceHK;
    }

    private List<ParentListBean> parentList;

    public String getMaxBet() {
        return maxBet;
    }

    public void setMaxBet(String maxBet) {
        this.maxBet = maxBet;
    }

    public String getMinBet() {
        return minBet;
    }

    public void setMinBet(String minBet) {
        this.minBet = minBet;
    }

    public List<ParentListBean> getParentList() {
        return parentList;
    }

    public void setParentList(List<ParentListBean> parentList) {
        this.parentList = parentList;
    }

    public static class ParentListBean {
        /**
         * playId : 9
         * playName : 牛牛
         * childList : [{"playId":"15","playName":"豹子","multiple":"6"}]
         */

        private String playId;
        private String playName;
        private List<ChildListBean> childList;

        public String getPlayId() {
            return playId;
        }

        public void setPlayId(String playId) {
            this.playId = playId;
        }

        public String getPlayName() {
            return playName;
        }

        public void setPlayName(String playName) {
            this.playName = playName;
        }

        public List<ChildListBean> getChildList() {
            return childList;
        }

        public void setChildList(List<ChildListBean> childList) {
            this.childList = childList;
        }

        public static class ChildListBean {
            /**
             * playId : 15
             * playName : 豹子
             * multiple : 6
             */

            private String playId;
            private String playName;
            private String multiple;
            private boolean isChecked;

            public boolean isChecked() {
                return isChecked;
            }

            public void setChecked(boolean checked) {
                isChecked = checked;
            }

            public String getPlayId() {
                return playId;
            }

            public void setPlayId(String playId) {
                this.playId = playId;
            }

            public String getPlayName() {
                return playName;
            }

            public void setPlayName(String playName) {
                this.playName = playName;
            }

            public String getMultiple() {
                return multiple;
            }

            public void setMultiple(String multiple) {
                this.multiple = multiple;
            }
        }
    }
}
