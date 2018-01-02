package com.happy.moment.clip.doll.bean;

/**
 * Created by Devin on 2017/11/20 21:24
 * E-mail:971060378@qq.com
 * 微信第三方登录返回的个人信息
 */

public class UserInfo extends BaseBean {

    /**
     * firstLogin : 0
     * headImg : http://wx.qlogo.cn/mmopen/vi_32/gFw8ozHiag7rQWRz2E0uk3Y0MN4YQK3IZbZHxGvCOdqaWEmYS5elHtIxZxAXRS2PqDuIQW5hgxicuV8Na3fTU6jw/0
     * inviteCode : NL1TMH
     * isInvited : 1
     * lastLoginTime : 1512376044000
     * nickName : haha
     * notifyNum : 1
     * state : 0
     * role: 0
     * userId : 1000002
     */

    private int firstLogin;
    private String headImg;
    private String inviteCode;
    private int isInvited;
    private long lastLoginTime;
    private String nickName;
    private int notifyNum;
    private int state;
    private int userId;
    private int role;

    public int getFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(int firstLogin) {
        this.firstLogin = firstLogin;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public int getIsInvited() {
        return isInvited;
    }

    public void setIsInvited(int isInvited) {
        this.isInvited = isInvited;
    }

    public long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getNotifyNum() {
        return notifyNum;
    }

    public void setNotifyNum(int notifyNum) {
        this.notifyNum = notifyNum;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
