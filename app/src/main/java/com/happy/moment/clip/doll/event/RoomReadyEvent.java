package com.happy.moment.clip.doll.event;

/**
 * Created by SETA_WORK on 2017/4/26.
 */

public class RoomReadyEvent {
    public int price = -1;
    public int account = -1;
//    public int created_at = 60;

    public RoomReadyEvent(int price, int account){
        this.price = price;
        this.account = account;
//        this.created_at = created_at;
    }
}
