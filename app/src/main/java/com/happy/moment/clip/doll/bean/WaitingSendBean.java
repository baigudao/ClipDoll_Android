package com.happy.moment.clip.doll.bean;

/**
 * Created by Devin on 2017/11/27 16:49
 * E-mail:971060378@qq.com
 */

public class WaitingSendBean extends BaseBean {

    /**
     * createTime : 2018-01-08 02:41:05
     * exchangeNum : 0
     * exchangeType : 0
     * playId : 315
     * productId : 52
     * state : 0
     * toyId : 100000
     * toyName : 100000
     * toyPicUrl : http://wawa-1255388722.cosgz.myqcloud.com/img/qeh1i3124123.jpg
     * userId : 1000039
     */

    private String createTime;
    private int exchangeNum;
    private int exchangeType;
    private int playId;
    private int productId;
    private int state;
    private String toyId;
    private String toyName;
    private String toyPicUrl;
    private int userId;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getExchangeNum() {
        return exchangeNum;
    }

    public void setExchangeNum(int exchangeNum) {
        this.exchangeNum = exchangeNum;
    }

    public int getExchangeType() {
        return exchangeType;
    }

    public void setExchangeType(int exchangeType) {
        this.exchangeType = exchangeType;
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getToyId() {
        return toyId;
    }

    public void setToyId(String toyId) {
        this.toyId = toyId;
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
