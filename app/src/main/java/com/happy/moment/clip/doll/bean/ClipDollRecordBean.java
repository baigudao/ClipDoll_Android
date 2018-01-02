package com.happy.moment.clip.doll.bean;

/**
 * Created by Devin on 2017/11/24 14:33
 * E-mail:971060378@qq.com
 */

public class ClipDollRecordBean extends BaseBean {

    /**
     * createTime : 2017-12-14 19:43:10
     * gameId : fdb04b8eea8b35210eb5e687b62a1f32
     * groupId : 2019
     * lastUpdateTime : 2017-12-14 19:43:48
     * playId : 10000606
     * productId : 20
     * recordTimeDesc : 8分钟前
     * result : 0
     * roomId : 10000020
     * toyName : 2019
     * toyPicUrl : http://wawa-1255388722.cosgz.myqcloud.com/img/qeh1i3124123.jpg
     * userId : 1000030
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
