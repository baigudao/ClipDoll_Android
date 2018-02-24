package com.happy.moment.clip.doll.bean;

/**
 * Created by Devin on 2018/1/24 15:59
 * E-mail:971060378@qq.com
 */

public class EarningUserBean extends BaseBean {

    /**
     * earnings : 10.15
     * type : 1
     * userImgUrl : http://wawa-1255388722.cosgz.myqcloud.com/img/1516710005580lyan888.jpg
     * userName : lyan888
     */

    private double earnings;
    private int type;
    private String userImgUrl;
    private String userName;

    public double getEarnings() {
        return earnings;
    }

    public void setEarnings(double earnings) {
        this.earnings = earnings;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUserImgUrl() {
        return userImgUrl;
    }

    public void setUserImgUrl(String userImgUrl) {
        this.userImgUrl = userImgUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
