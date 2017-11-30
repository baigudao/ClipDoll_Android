package com.happy.moment.clip.doll.bean;

/**
 * Created by Devin on 2017/11/28 15:32
 * E-mail:971060378@qq.com
 */

public class PriceListBean extends BaseBean {

    /**
     * firstReward : 500
     * lqbNum : 1000
     * price : 100
     * priceId : 4
     * rewardNum : 100
     */

    private int firstReward;
    private int lqbNum;
    private int price;
    private int priceId;
    private int rewardNum;

    public int getFirstReward() {
        return firstReward;
    }

    public void setFirstReward(int firstReward) {
        this.firstReward = firstReward;
    }

    public int getLqbNum() {
        return lqbNum;
    }

    public void setLqbNum(int lqbNum) {
        this.lqbNum = lqbNum;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPriceId() {
        return priceId;
    }

    public void setPriceId(int priceId) {
        this.priceId = priceId;
    }

    public int getRewardNum() {
        return rewardNum;
    }

    public void setRewardNum(int rewardNum) {
        this.rewardNum = rewardNum;
    }
}
