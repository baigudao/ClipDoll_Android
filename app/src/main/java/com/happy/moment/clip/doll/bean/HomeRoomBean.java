package com.happy.moment.clip.doll.bean;

import java.util.List;

/**
 * Created by Devin on 2017/11/18 11:59
 * E-mail:971060378@qq.com
 */

public class HomeRoomBean extends BaseBean {

    /**
     * gamePrice : 1
     * gameTime : 30
     * groupId : 2010
     * onlineState : 1
     * product : {"detailPics":["http://wawa-1255388722.cosgz.myqcloud.com/img/qeh1i3124123.jpg","http://wawa-1255388722.cosgz.myqcloud.com/img/qeh1i3124123.jpg","http://wawa-1255388722.cosgz.myqcloud.com/img/qeh1i3124123.jpg"],"productDetails":"http://wawa-1255388722.cosgz.myqcloud.com/img/qeh1i3124123.jpg,http://wawa-1255388722.cosgz.myqcloud.com/img/qeh1i3124123.jpg,http://wawa-1255388722.cosgz.myqcloud.com/img/qeh1i3124123.jpg","productId":11,"state":0,"toyId":"2010","toyName":"2010","toyPicUrl":"http://wawa-1255388722.cosgz.myqcloud.com/img/qeh1i3124123.jpg"}
     * productId : 11
     * roomId : 10000011
     * roomName : 2010
     * roomPicUrl : http://wawa-1255388722.cosgz.myqcloud.com/img/qeh1i3124123.jpg
     * roomState : 0
     * working : 1
     */

    private int gamePrice;
    private int gameTime;
    private String groupId;
    private int onlineState;
    private ProductBean product;
    private int productId;
    private int roomId;
    private String roomName;
    private String roomPicUrl;
    private int roomState;
    private int working;

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

    public int getOnlineState() {
        return onlineState;
    }

    public void setOnlineState(int onlineState) {
        this.onlineState = onlineState;
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

    public int getWorking() {
        return working;
    }

    public void setWorking(int working) {
        this.working = working;
    }

    public static class ProductBean {
        /**
         * detailPics : ["http://wawa-1255388722.cosgz.myqcloud.com/img/qeh1i3124123.jpg","http://wawa-1255388722.cosgz.myqcloud.com/img/qeh1i3124123.jpg","http://wawa-1255388722.cosgz.myqcloud.com/img/qeh1i3124123.jpg"]
         * productDetails : http://wawa-1255388722.cosgz.myqcloud.com/img/qeh1i3124123.jpg,http://wawa-1255388722.cosgz.myqcloud.com/img/qeh1i3124123.jpg,http://wawa-1255388722.cosgz.myqcloud.com/img/qeh1i3124123.jpg
         * productId : 11
         * state : 0
         * toyId : 2010
         * toyName : 2010
         * toyPicUrl : http://wawa-1255388722.cosgz.myqcloud.com/img/qeh1i3124123.jpg
         */

        private String productDetails;
        private int productId;
        private int state;
        private String toyId;
        private String toyName;
        private String toyPicUrl;
        private List<String> detailPics;

        public String getProductDetails() {
            return productDetails;
        }

        public void setProductDetails(String productDetails) {
            this.productDetails = productDetails;
        }

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

        public List<String> getDetailPics() {
            return detailPics;
        }

        public void setDetailPics(List<String> detailPics) {
            this.detailPics = detailPics;
        }
    }
}
