package com.happy.moment.clip.doll.bean;

/**
 * Created by Devin on 2018/2/6 11:35
 * E-mail:971060378@qq.com
 */

public class TaskBean extends BaseBean {

    /**
     * icon : http://wawa-1255579256.cosgz.myqcloud.com/img/dayTask_recharge%403x.png
     * isFulfil : 0
     * taskKey : EVERYDAY_RECHARGE
     * taskName : 每日充值
     * txt : 奖励:娃娃币 +5
     */

    private String icon;
    private int isFulfil;
    private String taskKey;
    private String taskName;
    private String txt;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getIsFulfil() {
        return isFulfil;
    }

    public void setIsFulfil(int isFulfil) {
        this.isFulfil = isFulfil;
    }

    public String getTaskKey() {
        return taskKey;
    }

    public void setTaskKey(String taskKey) {
        this.taskKey = taskKey;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }
}
