package com.zxjk.duoduo.network.response;

public class GetRebateByIdResponse {

    /**
     * id : 18
     * groupId : 25
     * customerId : 14
     * inviterId : 1
     * createDate : 1555413178259
     * isOwner :
     * grade : 代理级
     * directNum : 3
     * teamNum : 6
     * currentDirectPer : 23.0000
     * currentTeamPer : 66.5000
     * rebateRate : 0.0080
     * rebateAmount : 0.2250
     * rebateTotalAmount : 0.4500
     */

    private String id;
    private String groupId;
    private String customerId;
    private String inviterId;
    private long createDate;
    private String isOwner;
    private String grade;
    private String directNum;
    private String teamNum;
    private String currentDirectPer;
    private String currentTeamPer;
    private String rebateRate;
    private String rebateAmount;
    private String rebateTotalAmount;
    private String teamTotalPer;

    public String getTeamTotalPer() {
        return teamTotalPer;
    }

    public void setTeamTotalPer(String teamTotalPer) {
        this.teamTotalPer = teamTotalPer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getInviterId() {
        return inviterId;
    }

    public void setInviterId(String inviterId) {
        this.inviterId = inviterId;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public String getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(String isOwner) {
        this.isOwner = isOwner;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getDirectNum() {
        return directNum;
    }

    public void setDirectNum(String directNum) {
        this.directNum = directNum;
    }

    public String getTeamNum() {
        return teamNum;
    }

    public void setTeamNum(String teamNum) {
        this.teamNum = teamNum;
    }

    public String getCurrentDirectPer() {
        return currentDirectPer;
    }

    public void setCurrentDirectPer(String currentDirectPer) {
        this.currentDirectPer = currentDirectPer;
    }

    public String getCurrentTeamPer() {
        return currentTeamPer;
    }

    public void setCurrentTeamPer(String currentTeamPer) {
        this.currentTeamPer = currentTeamPer;
    }

    public String getRebateRate() {
        return rebateRate;
    }

    public void setRebateRate(String rebateRate) {
        this.rebateRate = rebateRate;
    }

    public String getRebateAmount() {
        return rebateAmount;
    }

    public void setRebateAmount(String rebateAmount) {
        this.rebateAmount = rebateAmount;
    }

    public String getRebateTotalAmount() {
        return rebateTotalAmount;
    }

    public void setRebateTotalAmount(String rebateTotalAmount) {
        this.rebateTotalAmount = rebateTotalAmount;
    }
}
