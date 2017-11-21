package com.happy.moment.clip.doll.bean;

/**
 * Created by Devin on 2017/11/20 21:24
 * E-mail:971060378@qq.com
 * 微信第三方登录返回的个人信息
 */

public class UserInfo extends BaseBean{

    /**
     * headImg : http://wx.qlogo.cn/mmopen/vi_32/gFw8ozHiag7rQWRz2E0uk3Y0MN4YQK3IZbZHxGvCOdqaWEmYS5elHtIxZxAXRS2PqDuIQW5hgxicuV8Na3fTU6jw/0
     * inviteCode : NL1TMH
     * nickName : haha
     * state : 0
     * userId : 1000002
     */

    private String headImg;
    private String inviteCode;
    private String nickName;
    private int state;
    private int userId;

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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
