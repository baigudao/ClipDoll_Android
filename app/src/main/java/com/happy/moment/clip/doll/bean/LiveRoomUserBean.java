package com.happy.moment.clip.doll.bean;

/**
 * Created by Devin on 2017/12/8 17:05
 * E-mail:971060378@qq.com
 */

public class LiveRoomUserBean extends BaseBean {

    /**
     * firstLogin : 1
     * headImg : http://wx.qlogo.cn/mmopen/vi_32/qQMH0Sbwhmlcsic4yeKbhPcjiaib3QfVHfvoyNn0icewtYxfwb4vYkmtzI5bH171CXxEQPSAK16K9Hk7VJccHgKldg/0
     * inviteCode : SNGDRT
     * nickName : 测试账号1
     * state : 0
     * userId : 1000009
     */

    private int firstLogin;
    private String headImg;
    private String inviteCode;
    private String nickName;
    private int state;
    private int userId;

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
