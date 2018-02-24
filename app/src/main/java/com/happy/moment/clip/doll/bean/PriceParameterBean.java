package com.happy.moment.clip.doll.bean;

/**
 * Created by Devin on 2017/12/29 01:54
 * E-mail:971060378@qq.com
 */

public class PriceParameterBean extends BaseBean {

    /**
     * firstReward : 300
     * lqbNum : 1200
     * price : 100.00
     * priceId : 4
     * rewardExplain : 充100元送200币
     * rewardNum : 200
     * tagText : 首充再送100币
     */

    private int firstReward;
    private int lqbNum;
    private String price;
    private int priceId;
    private String rewardExplain;
    private int rewardNum;
    private String tagText;

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

    public String getTagText() {
        return tagText;
    }

    public void setTagText(String tagText) {
        this.tagText = tagText;
    }
}
