package com.zxjk.duoduo.network.response;

import java.util.List;

public class GetBetInfoDetailsResponse {

    /**
     * groupOwner : {"nick":"丁浩","betMoney":"30000.00","points":"1.44","scorePoints":"140000","status":"2","lastMoney":"1682495.50","thisOverMoney":"1822495.50","createTime":"1556118793888","niuniuCardType":"牛九","baijialeCardType":"牛九","daxiaodxCardType":"牛九"}
     * betInfo : {"百家乐":[],"大小单双合":[{"nick":"刘二","betCardType":"小双","betMoney":"10000.00","points":"5.87","settlementCardType":"大双","status":"1","scorePoints":"30000","pumpingRate":"0.00","lastMoney":"950000.00","thisOverMoney":"920000.00","createTime":"1556118745826"},{"nick":"grant","betCardType":"小双","betMoney":"10000.00","points":"1.22","settlementCardType":"小单","status":"1","scorePoints":"30000","pumpingRate":"0.00","lastMoney":"586534.50","thisOverMoney":"556534.50","createTime":"1556118742297"}],"牛牛":[{"nick":"陕西西安","betCardType":"","betMoney":"10000.00","points":"0.35","settlementCardType":"牛八","status":"1","scorePoints":"80000","pumpingRate":"0.00","lastMoney":"1217000.00","thisOverMoney":"1137000.00","createTime":"1556118745571"}]}
     */

    private GroupOwnerBean groupOwner;
    private BetInfoBean betInfo;

    public GroupOwnerBean getGroupOwner() {
        return groupOwner;
    }

    public void setGroupOwner(GroupOwnerBean groupOwner) {
        this.groupOwner = groupOwner;
    }

    public BetInfoBean getBetInfo() {
        return betInfo;
    }

    public void setBetInfo(BetInfoBean betInfo) {
        this.betInfo = betInfo;
    }

    public static class GroupOwnerBean {
        /**
         * nick : 丁浩
         * betMoney : 30000.00
         * points : 1.44
         * scorePoints : 140000
         * status : 2
         * lastMoney : 1682495.50
         * thisOverMoney : 1822495.50
         * createTime : 1556118793888
         * niuniuCardType : 牛九
         * baijialeCardType : 牛九
         * daxiaodxCardType : 牛九
         */

        private String nick;
        private String betMoney;
        private String points;
        private String scorePoints;
        private String status;
        private String lastMoney;
        private String thisOverMoney;
        private String createTime;
        private String niuniuCardType;
        private String baijialeCardType;
        private String daxiaodxCardType;

        public String getNick() {
            return nick;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }

        public String getBetMoney() {
            return betMoney;
        }

        public void setBetMoney(String betMoney) {
            this.betMoney = betMoney;
        }

        public String getPoints() {
            return points;
        }

        public void setPoints(String points) {
            this.points = points;
        }

        public String getScorePoints() {
            return scorePoints;
        }

        public void setScorePoints(String scorePoints) {
            this.scorePoints = scorePoints;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getLastMoney() {
            return lastMoney;
        }

        public void setLastMoney(String lastMoney) {
            this.lastMoney = lastMoney;
        }

        public String getThisOverMoney() {
            return thisOverMoney;
        }

        public void setThisOverMoney(String thisOverMoney) {
            this.thisOverMoney = thisOverMoney;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getNiuniuCardType() {
            return niuniuCardType;
        }

        public void setNiuniuCardType(String niuniuCardType) {
            this.niuniuCardType = niuniuCardType;
        }

        public String getBaijialeCardType() {
            return baijialeCardType;
        }

        public void setBaijialeCardType(String baijialeCardType) {
            this.baijialeCardType = baijialeCardType;
        }

        public String getDaxiaodxCardType() {
            return daxiaodxCardType;
        }

        public void setDaxiaodxCardType(String daxiaodxCardType) {
            this.daxiaodxCardType = daxiaodxCardType;
        }
    }

    public static class BetInfoBean {
        private List<Bean> baijiale;
        private List<Bean> daxiaodanshuanghe;
        private List<Bean> niuniu;

        public List<Bean> getBaijiale() {
            return baijiale;
        }

        public void setBaijiale(List<Bean> baijiale) {
            this.baijiale = baijiale;
        }

        public List<Bean> getDaxiao() {
            return daxiaodanshuanghe;
        }

        public void setDaxiao(List<Bean> daxiao) {
            this.daxiaodanshuanghe = daxiao;
        }

        public List<Bean> getNiuniu() {
            return niuniu;
        }

        public void setNiuniu(List<Bean> niuniu) {
            this.niuniu = niuniu;
        }

        public static class Bean {
            /**
             * nick : 刘二
             * betCardType : 小双
             * betMoney : 10000.00
             * points : 5.87
             * settlementCardType : 大双
             * status : 1
             * scorePoints : 30000
             * pumpingRate : 0.00
             * lastMoney : 950000.00
             * thisOverMoney : 920000.00
             * createTime : 1556118745826
             */

            private String nick;
            private String betCardType;
            private String betMoney;
            private String points;
            private String settlementCardType;
            private String status;
            private String scorePoints;
            private String pumpingRate;
            private String lastMoney;
            private String thisOverMoney;
            private String createTime;

            public String getNick() {
                return nick;
            }

            public void setNick(String nick) {
                this.nick = nick;
            }

            public String getBetCardType() {
                return betCardType;
            }

            public void setBetCardType(String betCardType) {
                this.betCardType = betCardType;
            }

            public String getBetMoney() {
                return betMoney;
            }

            public void setBetMoney(String betMoney) {
                this.betMoney = betMoney;
            }

            public String getPoints() {
                return points;
            }

            public void setPoints(String points) {
                this.points = points;
            }

            public String getSettlementCardType() {
                return settlementCardType;
            }

            public void setSettlementCardType(String settlementCardType) {
                this.settlementCardType = settlementCardType;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getScorePoints() {
                return scorePoints;
            }

            public void setScorePoints(String scorePoints) {
                this.scorePoints = scorePoints;
            }

            public String getPumpingRate() {
                return pumpingRate;
            }

            public void setPumpingRate(String pumpingRate) {
                this.pumpingRate = pumpingRate;
            }

            public String getLastMoney() {
                return lastMoney;
            }

            public void setLastMoney(String lastMoney) {
                this.lastMoney = lastMoney;
            }

            public String getThisOverMoney() {
                return thisOverMoney;
            }

            public void setThisOverMoney(String thisOverMoney) {
                this.thisOverMoney = thisOverMoney;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }
        }
    }
}
