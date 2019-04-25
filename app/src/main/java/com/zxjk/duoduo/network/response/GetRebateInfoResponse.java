package com.zxjk.duoduo.network.response;

public class GetRebateInfoResponse {

    /**
     * id : 9
     * min : 11000.00
     * max : 30000.40
     * grade : 超级会员
     * commission : 0.0060
     * groupId : 25
     */

    private String id;
    private String min;
    private String max;
    private String grade;
    private String commission;
    private String groupId;

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
}
