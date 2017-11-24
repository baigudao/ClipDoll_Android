package com.happy.moment.clip.doll.bean;

/**
 * Created by Devin on 2017/11/24 16:54
 * E-mail:971060378@qq.com
 */

public class LiveRoomLuckyUserBean extends BaseBean {

    /**
     * createTime : 2017-11-24 14:20:47
     * groupId : 348312
     * lastUpdateTime : 2017-11-24 14:21:19
     * playId : 10000189
     * productId : 1
     * recordTimeDesc : 2小时前
     * result : true
     * roomId : 1
     * user : {"headImg":"http://wx.qlogo.cn/mmopen/vi_32/gFw8ozHiag7rQWRz2E0uk3Y0MN4YQK3IZbZHxGvCOdqaWEmYS5elHtIxZxAXRS2PqDuIQW5hgxicuV8Na3fTU6jw/0","inviteCode":"NL1TMH","nickName":"李怀龙","state":0,"userId":1000002}
     * userId : 1000002
     */

    private String createTime;
    private String groupId;
    private String lastUpdateTime;
    private int playId;
    private int productId;
    private String recordTimeDesc;
    private boolean result;
    private int roomId;
    private UserBean user;
    private int userId;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
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
         * headImg : http://wx.qlogo.cn/mmopen/vi_32/gFw8ozHiag7rQWRz2E0uk3Y0MN4YQK3IZbZHxGvCOdqaWEmYS5elHtIxZxAXRS2PqDuIQW5hgxicuV8Na3fTU6jw/0
         * inviteCode : NL1TMH
         * nickName : 李怀龙
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
}
