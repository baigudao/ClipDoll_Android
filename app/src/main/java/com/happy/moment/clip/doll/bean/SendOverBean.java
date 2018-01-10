package com.happy.moment.clip.doll.bean;

/**
 * Created by Devin on 2018/1/6 14:05
 * E-mail:971060378@qq.com
 */

public class SendOverBean extends BaseBean {

    /**
     * createTime : 2018-01-08 10:51:02
     * num : 1
     * orderId : 133
     * orderTitle : 100001等1个娃娃
     * productId : 53
     * state : 3
     * stateTitle : 已兑换
     * toyPicUrl : http://wawa-1255388722.cosgz.myqcloud.com/img/qeh1i3124123.jpg
     */

    private String createTime;
    private int num;
    private int orderId;
    private String orderTitle;
    private int productId;
    private int state;
    private String stateTitle;
    private String toyPicUrl;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderTitle() {
        return orderTitle;
    }

    public void setOrderTitle(String orderTitle) {
        this.orderTitle = orderTitle;
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

    public String getStateTitle() {
        return stateTitle;
    }

    public void setStateTitle(String stateTitle) {
        this.stateTitle = stateTitle;
    }

    public String getToyPicUrl() {
        return toyPicUrl;
    }

    public void setToyPicUrl(String toyPicUrl) {
        this.toyPicUrl = toyPicUrl;
    }
}
