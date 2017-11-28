package com.happy.moment.clip.doll.bean;

/**
 * Created by Devin on 2017/11/27 16:49
 * E-mail:971060378@qq.com
 */

public class WaitingSendBean extends BaseBean {

    /**
     * num : 1
     * productId : 2
     * state : 0
     * toyId : 160
     * toyName : wawa1
     * toyPicUrl : http://wawa-1255388722.cosgz.myqcloud.com/img/d71a5054jw8euqdybnb1ij20xc1e0tht.jpg
     * userId : 1000002
     */

    private int num;
    private int productId;
    private int state;
    private String toyId;
    private String toyName;
    private String toyPicUrl;
    private int userId;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
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
