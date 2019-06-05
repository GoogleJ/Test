package com.zxjk.duoduo.network.request;

import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.ui.msgpage.JinDuoBaoActivity;

import java.util.List;

public class GroupGamebettingForDuobaoRequest {

    private String playName = "香港金多寶";
    private String playId = "81";
    private String customerId = Constant.userId;

    private String groupId;
    private String expect;
    private String countMoney;
    private List<JinDuoBaoActivity.XiaZhuBean> duoBaoBetInfoBeanList;
    private String payPwd;

    public String getPayPwd() {
        return payPwd;
    }

    public void setPayPwd(String payPwd) {
        this.payPwd = payPwd;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getExpect() {
        return expect;
    }

    public void setExpect(String expect) {
        this.expect = expect;
    }

    public String getPlayName() {
        return playName;
    }

    public void setPlayName(String playName) {
        this.playName = playName;
    }

    public String getPlayId() {
        return playId;
    }

    public void setPlayId(String playId) {
        this.playId = playId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCountMoney() {
        return countMoney;
    }

    public void setCountMoney(String countMoney) {
        this.countMoney = countMoney;
    }

    public List<JinDuoBaoActivity.XiaZhuBean> getDuoBaoBetInfoBeanList() {
        return duoBaoBetInfoBeanList;
    }

    public void setDuoBaoBetInfoBeanList(List<JinDuoBaoActivity.XiaZhuBean> duoBaoBetInfoBeanList) {
        this.duoBaoBetInfoBeanList = duoBaoBetInfoBeanList;
    }
}
