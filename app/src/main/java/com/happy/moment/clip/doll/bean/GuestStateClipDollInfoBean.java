package com.happy.moment.clip.doll.bean;

/**
 * Created by Devin on 2017/12/10 23:56
 * E-mail:971060378@qq.com
 */

public class GuestStateClipDollInfoBean extends BaseBean {

    /**
     * createTime : 2017-12-07 16:02:19
     * gameId : a907f931fdfb51ec45456f972e901bf3
     * groupId : 1103
     * lastUpdateTime : 2017-12-07 16:02:39
     * playId : 10001062
     * productId : 5
     * recordTimeDesc : 3天前
     * result : 1
     * roomId : 10003
     * toyName : 小小新
     * toyPicUrl : http://wawa-1255388722.cosgz.myqcloud.com/img/qeh1i3124123.jpg
     * user : {"firstLogin":1,"headImg":"http://wx.qlogo.cn/mmopen/vi_32/DYAIOgq83ervJwqTR2No7qAm8RnrhaSOGlR9icsqB39hHqBDu6kVadUiaCudxbt1RiawImboYprjNRdibUlz3hFhOQ/0","inviteCode":"E8JHN8","lastLoginTime":1512885819000,"nickName":"站长","state":0,"userId":1000018}
     * userId : 1000018
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
    private String toyName;
    private String toyPicUrl;
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

    public String getToyName() {
        return toyName;
    }

    public void setToyName(String toyName) {
        this.toyName = toyName;
    }

    public String getToyPicUrl() {
        return toyPicUrl;
    }

    public void setToyPicUrl(String toyPicUrl) {
        this.toyPicUrl = toyPicUrl;
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
         * headImg : http://wx.qlogo.cn/mmopen/vi_32/DYAIOgq83ervJwqTR2No7qAm8RnrhaSOGlR9icsqB39hHqBDu6kVadUiaCudxbt1RiawImboYprjNRdibUlz3hFhOQ/0
         * inviteCode : E8JHN8
         * lastLoginTime : 1512885819000
         * nickName : 站长
         * state : 0
         * userId : 1000018
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
