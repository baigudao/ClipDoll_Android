package com.happy.moment.clip.doll.bean;

/**
 * Created by Devin on 2017/11/24 14:33
 * E-mail:971060378@qq.com
 */

public class ClipDollRecordBean extends BaseBean {

    /**
     * createTime : 2017-11-24 14:20:47
     * groupId : 348312
     * lastUpdateTime : 2017-11-24 14:21:19
     * playId : 10000189
     * productId : 1
     * recordTimeDesc : 7分钟前
     * result : true
     * roomId : 1
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
