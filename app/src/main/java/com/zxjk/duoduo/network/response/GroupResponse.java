package com.zxjk.duoduo.network.response;

import java.io.Serializable;

/**
 * @author Administrator
 * @// TODO: 2019\3\29 0029  关于群组的实体 
 */
public class GroupResponse implements Serializable {

    /**
     * id : 15
     * goupType : 0
     * groupNikeName : null
     * groupHeadPortrait : null
     * groupSign : null
     * groupNotice : null
     * groupOwnerId : 4
     * updataTime : null
     * createTime : 2019-02-22 16:25:56
     * isDelete : 0
     * isInviteConfirm : 0
     */

    private String id;
    private String goupType;
    private String groupNikeName;
    private String groupHeadPortrait;
    private String groupSign;
    private String groupNotice;
    private String groupOwnerId;
    private String updataTime;
    private String createTime;
    private String isDelete;
    private String isInviteConfirm;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoupType() {
        return goupType;
    }

    public void setGoupType(String goupType) {
        this.goupType = goupType;
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

    public String getUpdataTime() {
        return updataTime;
    }

    public void setUpdataTime(String updataTime) {
        this.updataTime = updataTime;
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
}
