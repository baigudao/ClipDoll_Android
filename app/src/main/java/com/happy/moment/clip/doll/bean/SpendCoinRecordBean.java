package com.happy.moment.clip.doll.bean;

/**
 * Created by Devin on 2017/11/27 14:27
 * E-mail:971060378@qq.com
 */

public class SpendCoinRecordBean extends BaseBean {

    /**
     * balance : 4919
     * createTime : 2017-11-24 11:45:23
     * currentId : e66bfe39d0c911e790f2246e96754b22
     * currentType : 0
     * details : 抓取娃娃'鹦鹉兄弟',消费游戏币 : 1币
     * expendOrIncome : 0
     * isDelete : 0
     * lastUpdateTime : 2017-11-24 11:45:23
     * lqbAmount : -1
     * relationId : 10000170
     * state : 1
     * userId : 1000002
     */

    private int balance;
    private String createTime;
    private String currentId;
    private int currentType;
    private String details;
    private int expendOrIncome;
    private int isDelete;
    private String lastUpdateTime;
    private int lqbAmount;
    private int relationId;
    private int state;
    private int userId;

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCurrentId() {
        return currentId;
    }

    public void setCurrentId(String currentId) {
        this.currentId = currentId;
    }

    public int getCurrentType() {
        return currentType;
    }

    public void setCurrentType(int currentType) {
        this.currentType = currentType;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getExpendOrIncome() {
        return expendOrIncome;
    }

    public void setExpendOrIncome(int expendOrIncome) {
        this.expendOrIncome = expendOrIncome;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public int getLqbAmount() {
        return lqbAmount;
    }

    public void setLqbAmount(int lqbAmount) {
        this.lqbAmount = lqbAmount;
    }

    public int getRelationId() {
        return relationId;
    }

    public void setRelationId(int relationId) {
        this.relationId = relationId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
