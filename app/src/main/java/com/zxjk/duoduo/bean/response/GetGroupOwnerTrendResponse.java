package com.zxjk.duoduo.bean.response;

import java.util.List;

public class GetGroupOwnerTrendResponse {

    private List<NiuNiuTrendBean> niuNiuTrend;
    private List<BandDTrendBean> bandDTrend;

    public List<NiuNiuTrendBean> getNiuNiuTrend() {
        return niuNiuTrend;
    }

    public void setNiuNiuTrend(List<NiuNiuTrendBean> niuNiuTrend) {
        this.niuNiuTrend = niuNiuTrend;
    }

    public List<BandDTrendBean> getBandDTrend() {
        return bandDTrend;
    }

    public void setBandDTrend(List<BandDTrendBean> bandDTrend) {
        this.bandDTrend = bandDTrend;
    }

    public static class NiuNiuTrendBean {
        /**
         * points : 7
         * settlementCardType : 牛七
         * redPackagePoints : 0.98
         * expect : 06142160001期
         * createTime : 1560523947786
         */

        private int points;
        private String settlementCardType;
        private String redPackagePoints;
        private String expect;
        private String createTime;

        public int getPoints() {
            return points;
        }

        public void setPoints(int points) {
            this.points = points;
        }

        public String getSettlementCardType() {
            return settlementCardType;
        }

        public void setSettlementCardType(String settlementCardType) {
            this.settlementCardType = settlementCardType;
        }

        public String getRedPackagePoints() {
            return redPackagePoints;
        }

        public void setRedPackagePoints(String redPackagePoints) {
            this.redPackagePoints = redPackagePoints;
        }

        public String getExpect() {
            return expect;
        }

        public void setExpect(String expect) {
            this.expect = expect;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }

    public static class BandDTrendBean {
        /**
         * points : 7
         * settlementCardType : 七点
         * redPackagePoints : 0.98
         * expect : 06142160001期
         * createTime : 1560523947786
         */

        private int points;
        private String settlementCardType;
        private String redPackagePoints;
        private String expect;
        private String createTime;

        public int getPoints() {
            return points;
        }

        public void setPoints(int points) {
            this.points = points;
        }

        public String getSettlementCardType() {
            return settlementCardType;
        }

        public void setSettlementCardType(String settlementCardType) {
            this.settlementCardType = settlementCardType;
        }

        public String getRedPackagePoints() {
            return redPackagePoints;
        }

        public void setRedPackagePoints(String redPackagePoints) {
            this.redPackagePoints = redPackagePoints;
        }

        public String getExpect() {
            return expect;
        }

        public void setExpect(String expect) {
            this.expect = expect;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }
}
