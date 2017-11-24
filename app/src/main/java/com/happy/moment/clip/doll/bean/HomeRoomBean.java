package com.happy.moment.clip.doll.bean;

/**
 * Created by Devin on 2017/11/18 11:59
 * E-mail:971060378@qq.com
 */

public class HomeRoomBean extends BaseBean {

    /**
     * frontPullFlvUrl : http://16145.liveplay.myqcloud.com/live/16145_d95f3c35f184debb1980ed6569e0c673.flv
     * frontPullHlsUrl : http://16145.liveplay.myqcloud.com/live/16145_d95f3c35f184debb1980ed6569e0c673.m3u8
     * frontPullRtmpUrl : rtmp://16145.liveplay.myqcloud.com/live/16145_d95f3c35f184debb1980ed6569e0c673
     * frontPushId : live_1_front_push_1510577069
     * gamePrice : 1
     * gameTime : 30
     * groupId : 348312
     * product : {"productId":1,"state":0,"toyId":"1","toyName":"鹦鹉兄弟","toyPicUrl":"http://cachu-test.b0.upaiyun.com/r4rqic5e4hbjxqanld2e956fi0fyqswf.jpg"}
     * productId : 1
     * roomId : 1
     * roomName : LIVE_ROOM_1001
     * roomPicUrl : http://wawa-1255388722.cosgz.myqcloud.com/img/wawa.jpg
     * roomState : 0
     * sidePullFlvUrl : http://16145.liveplay.myqcloud.com/live/16145_8d6771eec114973f09a9a1a69b2a25a0.flv
     * sidePullHlsUrl : http://1145.liveplay.myqcloud.com/live/16145_8d6771eec114973f09a9a1a69b2a25a0.m3u8
     * sidePullRtmpUrl : rtmp://16145.liveplay.myqcloud.com/live/16145_8d6771eec114973f09a9a1a69b2a25a0
     * sidePushId : live_1_side_push_1510577079
     */

    private String frontPullFlvUrl;
    private String frontPullHlsUrl;
    private String frontPullRtmpUrl;
    private String frontPushId;
    private int gamePrice;
    private int gameTime;
    private String groupId;
    private ProductBean product;
    private int productId;
    private int roomId;
    private String roomName;
    private String roomPicUrl;
    private int roomState;
    private String sidePullFlvUrl;
    private String sidePullHlsUrl;
    private String sidePullRtmpUrl;
    private String sidePushId;

    public String getFrontPullFlvUrl() {
        return frontPullFlvUrl;
    }

    public void setFrontPullFlvUrl(String frontPullFlvUrl) {
        this.frontPullFlvUrl = frontPullFlvUrl;
    }

    public String getFrontPullHlsUrl() {
        return frontPullHlsUrl;
    }

    public void setFrontPullHlsUrl(String frontPullHlsUrl) {
        this.frontPullHlsUrl = frontPullHlsUrl;
    }

    public String getFrontPullRtmpUrl() {
        return frontPullRtmpUrl;
    }

    public void setFrontPullRtmpUrl(String frontPullRtmpUrl) {
        this.frontPullRtmpUrl = frontPullRtmpUrl;
    }

    public String getFrontPushId() {
        return frontPushId;
    }

    public void setFrontPushId(String frontPushId) {
        this.frontPushId = frontPushId;
    }

    public int getGamePrice() {
        return gamePrice;
    }

    public void setGamePrice(int gamePrice) {
        this.gamePrice = gamePrice;
    }

    public int getGameTime() {
        return gameTime;
    }

    public void setGameTime(int gameTime) {
        this.gameTime = gameTime;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public ProductBean getProduct() {
        return product;
    }

    public void setProduct(ProductBean product) {
        this.product = product;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomPicUrl() {
        return roomPicUrl;
    }

    public void setRoomPicUrl(String roomPicUrl) {
        this.roomPicUrl = roomPicUrl;
    }

    public int getRoomState() {
        return roomState;
    }

    public void setRoomState(int roomState) {
        this.roomState = roomState;
    }

    public String getSidePullFlvUrl() {
        return sidePullFlvUrl;
    }

    public void setSidePullFlvUrl(String sidePullFlvUrl) {
        this.sidePullFlvUrl = sidePullFlvUrl;
    }

    public String getSidePullHlsUrl() {
        return sidePullHlsUrl;
    }

    public void setSidePullHlsUrl(String sidePullHlsUrl) {
        this.sidePullHlsUrl = sidePullHlsUrl;
    }

    public String getSidePullRtmpUrl() {
        return sidePullRtmpUrl;
    }

    public void setSidePullRtmpUrl(String sidePullRtmpUrl) {
        this.sidePullRtmpUrl = sidePullRtmpUrl;
    }

    public String getSidePushId() {
        return sidePushId;
    }

    public void setSidePushId(String sidePushId) {
        this.sidePushId = sidePushId;
    }

    public static class ProductBean {
        /**
         * productId : 1
         * state : 0
         * toyId : 1
         * toyName : 鹦鹉兄弟
         * toyPicUrl : http://cachu-test.b0.upaiyun.com/r4rqic5e4hbjxqanld2e956fi0fyqswf.jpg
         */

        private int productId;
        private int state;
        private String toyId;
        private String toyName;
        private String toyPicUrl;

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getToyId() {
            return toyId;
        }

        public void setToyId(String toyId) {
            this.toyId = toyId;
        }

        public String getToyName() {
            return toyName;
        }

        public void setToyName(String toyName) {
            this.toyName = toyName;
        }

        public String getToyPicUrl() {
            return toyPicUrl;
        }

        public void setToyPicUrl(String toyPicUrl) {
            this.toyPicUrl = toyPicUrl;
        }
    }
}
