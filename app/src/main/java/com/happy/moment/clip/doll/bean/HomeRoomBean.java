package com.happy.moment.clip.doll.bean;

/**
 * Created by Devin on 2017/11/18 11:59
 * E-mail:971060378@qq.com
 */

public class HomeRoomBean extends BaseBean {

    /**
     * frontPulFlvUrl : http://16145.liveplay.myqcloud.com/live/16145_d95f3c35f184debb1980ed6569e0c673.flv
     * frontPullHlsUrl : http://16145.liveplay.myqcloud.com/live/16145_d95f3c35f184debb1980ed6569e0c673.m3u8
     * frontPullRtmpUrl : rtmp://16145.liveplay.myqcloud.com/live/16145_d95f3c35f184debb1980ed6569e0c673
     * gamePrice : 1
     * gameTime : 1
     * groupId : 348312
     * lastPlayTime : 0
     * product : {"productId":1,"state":0,"toyId":"1","toyName":"鹦鹉兄弟","toyPicUrl":"http://cachu-test.b0.upaiyun.com/r4rqic5e4hbjxqanld2e956fi0fyqswf.jpg"}
     * productId : 1
     * roomId : 1
     * roomName : LIVE_ROOM_1001
     * roomPicUrl : http://wawa-1255388722.cosgz.myqcloud.com/img/tea2.png
     * roomState : 1
     * sidePulFlvUrl : http://16145.liveplay.myqcloud.com/live/16145_8d6771eec114973f09a9a1a69b2a25a0.flv
     * sidePullHlsUrl : http://16145.liveplay.myqcloud.com/live/16145_8d6771eec114973f09a9a1a69b2a25a0.m3u8
     * sidePullRtmpUrl : rtmp://16145.liveplay.myqcloud.com/live/16145_8d6771eec114973f09a9a1a69b2a25a0
     * sort : 0
     */

    private String frontPulFlvUrl;
    private String frontPullHlsUrl;
    private String frontPullRtmpUrl;
    private int gamePrice;
    private int gameTime;
    private String groupId;
    private int lastPlayTime;
    private ProductBean product;
    private int productId;
    private int roomId;
    private String roomName;
    private String roomPicUrl;
    private int roomState;
    private String sidePulFlvUrl;
    private String sidePullHlsUrl;
    private String sidePullRtmpUrl;
    private int sort;

    public String getFrontPulFlvUrl() {
        return frontPulFlvUrl;
    }

    public void setFrontPulFlvUrl(String frontPulFlvUrl) {
        this.frontPulFlvUrl = frontPulFlvUrl;
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

    public int getLastPlayTime() {
        return lastPlayTime;
    }

    public void setLastPlayTime(int lastPlayTime) {
        this.lastPlayTime = lastPlayTime;
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

    public String getSidePulFlvUrl() {
        return sidePulFlvUrl;
    }

    public void setSidePulFlvUrl(String sidePulFlvUrl) {
        this.sidePulFlvUrl = sidePulFlvUrl;
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

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
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
