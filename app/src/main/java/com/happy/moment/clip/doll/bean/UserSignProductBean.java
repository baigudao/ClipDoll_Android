package com.happy.moment.clip.doll.bean;

/**
 * Created by Devin on 2018/2/5 16:37
 * E-mail:971060378@qq.com
 */

public class UserSignProductBean extends BaseBean {

    /**
     * coinNum : 10
     * id : 1
     * remark :
     * signFate : 1
     * signType : 1
     */

    private int coinNum;
    private int id;
    private String remark;
    private int signFate;
    private int signType;

    public int getCoinNum() {
        return coinNum;
    }

    public void setCoinNum(int coinNum) {
        this.coinNum = coinNum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getSignFate() {
        return signFate;
    }

    public void setSignFate(int signFate) {
        this.signFate = signFate;
    }

    public int getSignType() {
        return signType;
    }

    public void setSignType(int signType) {
        this.signType = signType;
    }
}
