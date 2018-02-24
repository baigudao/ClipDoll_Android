package com.happy.moment.clip.doll.bean;

import java.util.List;

/**
 * Created by Devin on 2017/12/28 15:03
 * E-mail:971060378@qq.com
 */

public class MyIncomeBean extends BaseBean {

    /**
     * awardRate : 0.3
     * balance : 0
     * channelId : 0
     * earningsCountVo : {"earningsNum":30.45,"friendNum":3,"userEarningsVos":[{"earnings":10.15,"type":1,"userImgUrl":"http://wawa-1255388722.cosgz.myqcloud.com/img/1516710005201luzle.jpg","userName":"luzle"},{"earnings":10.15,"type":1,"userImgUrl":"http://wawa-1255388722.cosgz.myqcloud.com/img/1516710005302LVDX%E6%99%A8%E5%AE%87.jpg","userName":"LVDX晨宇"},{"earnings":10.15,"type":1,"userImgUrl":"http://wawa-1255388722.cosgz.myqcloud.com/img/1516710005424lxb%E5%B0%8F%E5%BC%BA.jpg","userName":"lxb小强"}]}
     * earningsTotal : 30.45
     * monthEarnings : 0.00
     * monthUserCount : 0
     * promoteUrl : http://wwh5.tuuban.com/?channelId=0&agenterId=1000039
     * settledMoney : 0.00
     * totalEarnings : 0.00
     * totalUserNum : 0
     * userId : 1000039
     * weekEarnings : 0.00
     * weekUserCount : 0
     * withdrawQrCode : http://wawa-1255388722.cosgz.myqcloud.com/img/qr_code.png
     * withdrawWechartAccount : wozhaodun
     * yesterdayEarnings : 0.00
     * yesterdayUserCount : 0
     */

    private double awardRate;
    private double balance;
    private int channelId;
    private EarningsCountVoBean earningsCountVo;
    private double earningsTotal;
    private String monthEarnings;
    private int monthUserCount;
    private String promoteUrl;
    private String settledMoney;
    private String totalEarnings;
    private int totalUserNum;
    private int userId;
    private String weekEarnings;
    private int weekUserCount;
    private String withdrawQrCode;
    private String withdrawWechartAccount;
    private String yesterdayEarnings;
    private int yesterdayUserCount;

    public double getAwardRate() {
        return awardRate;
    }

    public void setAwardRate(double awardRate) {
        this.awardRate = awardRate;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public EarningsCountVoBean getEarningsCountVo() {
        return earningsCountVo;
    }

    public void setEarningsCountVo(EarningsCountVoBean earningsCountVo) {
        this.earningsCountVo = earningsCountVo;
    }

    public double getEarningsTotal() {
        return earningsTotal;
    }

    public void setEarningsTotal(double earningsTotal) {
        this.earningsTotal = earningsTotal;
    }

    public String getMonthEarnings() {
        return monthEarnings;
    }

    public void setMonthEarnings(String monthEarnings) {
        this.monthEarnings = monthEarnings;
    }

    public int getMonthUserCount() {
        return monthUserCount;
    }

    public void setMonthUserCount(int monthUserCount) {
        this.monthUserCount = monthUserCount;
    }

    public String getPromoteUrl() {
        return promoteUrl;
    }

    public void setPromoteUrl(String promoteUrl) {
        this.promoteUrl = promoteUrl;
    }

    public String getSettledMoney() {
        return settledMoney;
    }

    public void setSettledMoney(String settledMoney) {
        this.settledMoney = settledMoney;
    }

    public String getTotalEarnings() {
        return totalEarnings;
    }

    public void setTotalEarnings(String totalEarnings) {
        this.totalEarnings = totalEarnings;
    }

    public int getTotalUserNum() {
        return totalUserNum;
    }

    public void setTotalUserNum(int totalUserNum) {
        this.totalUserNum = totalUserNum;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getWeekEarnings() {
        return weekEarnings;
    }

    public void setWeekEarnings(String weekEarnings) {
        this.weekEarnings = weekEarnings;
    }

    public int getWeekUserCount() {
        return weekUserCount;
    }

    public void setWeekUserCount(int weekUserCount) {
        this.weekUserCount = weekUserCount;
    }

    public String getWithdrawQrCode() {
        return withdrawQrCode;
    }

    public void setWithdrawQrCode(String withdrawQrCode) {
        this.withdrawQrCode = withdrawQrCode;
    }

    public String getWithdrawWechartAccount() {
        return withdrawWechartAccount;
    }

    public void setWithdrawWechartAccount(String withdrawWechartAccount) {
        this.withdrawWechartAccount = withdrawWechartAccount;
    }

    public String getYesterdayEarnings() {
        return yesterdayEarnings;
    }

    public void setYesterdayEarnings(String yesterdayEarnings) {
        this.yesterdayEarnings = yesterdayEarnings;
    }

    public int getYesterdayUserCount() {
        return yesterdayUserCount;
    }

    public void setYesterdayUserCount(int yesterdayUserCount) {
        this.yesterdayUserCount = yesterdayUserCount;
    }

    public static class EarningsCountVoBean {
        /**
         * earningsNum : 30.45
         * friendNum : 3
         * userEarningsVos : [{"earnings":10.15,"type":1,"userImgUrl":"http://wawa-1255388722.cosgz.myqcloud.com/img/1516710005201luzle.jpg","userName":"luzle"},{"earnings":10.15,"type":1,"userImgUrl":"http://wawa-1255388722.cosgz.myqcloud.com/img/1516710005302LVDX%E6%99%A8%E5%AE%87.jpg","userName":"LVDX晨宇"},{"earnings":10.15,"type":1,"userImgUrl":"http://wawa-1255388722.cosgz.myqcloud.com/img/1516710005424lxb%E5%B0%8F%E5%BC%BA.jpg","userName":"lxb小强"}]
         */

        private double earningsNum;
        private int friendNum;
        private List<EarningUserBean> userEarningsVos;

        public double getEarningsNum() {
            return earningsNum;
        }

        public void setEarningsNum(double earningsNum) {
            this.earningsNum = earningsNum;
        }

        public int getFriendNum() {
            return friendNum;
        }

        public void setFriendNum(int friendNum) {
            this.friendNum = friendNum;
        }

        public List<EarningUserBean> getUserEarningsVos() {
            return userEarningsVos;
        }

        public void setUserEarningsVos(List<EarningUserBean> userEarningsVos) {
            this.userEarningsVos = userEarningsVos;
        }
    }
}
