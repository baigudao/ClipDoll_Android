package com.happy.moment.clip.doll.bean;

/**
 * Created by Devin on 2017/11/25 19:30
 * E-mail:971060378@qq.com
 */

public class MessageNotificationBean extends BaseBean {

    /**
     * createTime : 1511254523000
     * messageContent : 新用户注册成功，获赠'100003'游戏币，快去游戏吧
     * messageId : 1
     * messageType : 0
     * readState : 0
     * userId : 1000002
     */

    private long createTime;
    private String messageContent;
    private int messageId;
    private int messageType;
    private int readState;
    private int userId;

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public int getReadState() {
        return readState;
    }

    public void setReadState(int readState) {
        this.readState = readState;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
