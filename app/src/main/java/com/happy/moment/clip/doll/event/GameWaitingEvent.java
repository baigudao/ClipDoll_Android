package com.happy.moment.clip.doll.event;

/**
 * Created by Devin on 2017/11/22 11:33
 * E-mail:971060378@qq.com
 */

public class GameWaitingEvent {
    public boolean waiting = false;
    public int num;

    public GameWaitingEvent(boolean waiting, int num) {
        this.waiting = waiting;
        this.num = num;
    }
}
