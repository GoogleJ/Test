package com.zxjk.duoduo.network.response;

public class GetTeamInfoResponse {


    /**
     * nick : 高贵的前端开发
     * currentTeamPer : 0.00
     * grade :
     * currentDirectPer : 0.00
     * duoduoId : 88657885
     * betTotalMoney : 0.00
     * teamNum : 1
     */

    private String nick;
    private String currentTeamPer;
    private String grade;
    private String currentDirectPer;
    private String duoduoId;
    private String betTotalMoney;
    private int teamNum;

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getCurrentTeamPer() {
        return currentTeamPer;
    }

    public void setCurrentTeamPer(String currentTeamPer) {
        this.currentTeamPer = currentTeamPer;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getCurrentDirectPer() {
        return currentDirectPer;
    }

    public void setCurrentDirectPer(String currentDirectPer) {
        this.currentDirectPer = currentDirectPer;
    }

    public String getDuoduoId() {
        return duoduoId;
    }

    public void setDuoduoId(String duoduoId) {
        this.duoduoId = duoduoId;
    }

    public String getBetTotalMoney() {
        return betTotalMoney;
    }

    public void setBetTotalMoney(String betTotalMoney) {
        this.betTotalMoney = betTotalMoney;
    }

    public int getTeamNum() {
        return teamNum;
    }

    public void setTeamNum(int teamNum) {
        this.teamNum = teamNum;
    }
}
