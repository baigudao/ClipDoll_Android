package com.happy.moment.clip.doll.bean;

/**
 * Created by Devin on 2017/11/24 16:54
 * E-mail:971060378@qq.com
 */

public class LiveRoomLuckyUserBean extends BaseBean {

    /**
     * createTime : 2017-12-09 14:13:36
     * gameId : 8ddb3cb9df54cdae6c524890b1047568
     * groupId : 1103
     * lastUpdateTime : 2017-12-09 14:13:52
     * playId : 10002160
     * productId : 5
     * recordTimeDesc : 3小时前
     * result : 1
     * roomId : 10003
     * user : {"firstLogin":1,"headImg":"http://wx.qlogo.cn/mmopen/vi_32/DYAIOgq83epM1USJXN2WicBH83uia3tsUVzWbPOKQTCdXaH9SSQGvxSR4PKJt9LAzSFp8Xia1ib7JwgRcYP5uzyfng/0","inviteCode":"O3U74K","lastLoginTime":1512804410000,"nickName":"coon","state":0,"userId":1000019}
     * userId : 1000019
     */

    private String createTime;
    private String gameId;
    private String groupId;
    private String lastUpdateTime;
    private int playId;
    private int productId;
    private String recordTimeDesc;
    private int result;
    private int roomId;
    private UserBean user;
    private int userId;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public int getPlayId() {
        return playId;
    }

    public void setPlayId(int playId) {
        this.playId = playId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getRecordTimeDesc() {
        return recordTimeDesc;
    }

    public void setRecordTimeDesc(String recordTimeDesc) {
        this.recordTimeDesc = recordTimeDesc;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public static class UserBean {
        /**
         * firstLogin : 1
         * headImg : http://wx.qlogo.cn/mmopen/vi_32/DYAIOgq83epM1USJXN2WicBH83uia3tsUVzWbPOKQTCdXaH9SSQGvxSR4PKJt9LAzSFp8Xia1ib7JwgRcYP5uzyfng/0
         * inviteCode : O3U74K
         * lastLoginTime : 1512804410000
         * nickName : coon
         * state : 0
         * userId : 1000019
         */

        private int firstLogin;
        private String headImg;
        private String inviteCode;
        private long lastLoginTime;
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
}
