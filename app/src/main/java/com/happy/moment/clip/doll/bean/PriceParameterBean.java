package com.happy.moment.clip.doll.bean;

/**
 * Created by Devin on 2017/12/29 01:54
 * E-mail:971060378@qq.com
 */

public class PriceParameterBean extends BaseBean {

    /**
     * firstReward : 950
     * lqbNum : 1000
     * price : 100.00
     * priceId : 4
     * rewardExplain : 首充100元送100
     * rewardNum : 100
     */

    private int firstReward;
    private int lqbNum;
    private String price;
    private int priceId;
    private String rewardExplain;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getPriceId() {
        return priceId;
    }

    public void setPriceId(int priceId) {
        this.priceId = priceId;
    }

    public String getRewardExplain() {
        return rewardExplain;
    }

    public void setRewardExplain(String rewardExplain) {
        this.rewardExplain = rewardExplain;
    }

    public int getRewardNum() {
        return rewardNum;
    }

    public void setRewardNum(int rewardNum) {
        this.rewardNum = rewardNum;
    }
}
