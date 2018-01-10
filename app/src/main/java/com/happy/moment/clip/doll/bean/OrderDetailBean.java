package com.happy.moment.clip.doll.bean;

import java.util.List;

/**
 * Created by Devin on 2018/1/6 14:34
 * E-mail:971060378@qq.com
 */

public class OrderDetailBean extends BaseBean {

    /**
     * address : 广东深圳东莞大道51号楼上
     * consignee : 小菜
     * createTime : 2018-01-08 18:37:54
     * logistics : 顺丰快递
     * logisticsSn : 1234562252
     * orderId : 233
     * orderSn : PDO201801081837541230
     * orderType : 0
     * phone : 13525456885
     * postageMoney : 0.00
     * productList : [{"num":1,"productId":52,"toyName":"100000","toyPicUrl":"http://wawa-1255388722.cosgz.myqcloud.com/img/qeh1i3124123.jpg"},{"num":1,"productId":53,"toyName":"100001","toyPicUrl":"http://wawa-1255388722.cosgz.myqcloud.com/img/qeh1i3124123.jpg"}]
     * state : 1
     * stateTitle : 已收货
     * totalExchangeLqb : 0
     */

    private String address;
    private String consignee;
    private String createTime;
    private String logistics;
    private String logisticsSn;
    private int orderId;
    private String orderSn;
    private int orderType;
    private String phone;
    private String postageMoney;
    private int state;
    private String stateTitle;
    private int totalExchangeLqb;
    private List<ProductListBean> productList;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLogistics() {
        return logistics;
    }

    public void setLogistics(String logistics) {
        this.logistics = logistics;
    }

    public String getLogisticsSn() {
        return logisticsSn;
    }

    public void setLogisticsSn(String logisticsSn) {
        this.logisticsSn = logisticsSn;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPostageMoney() {
        return postageMoney;
    }

    public void setPostageMoney(String postageMoney) {
        this.postageMoney = postageMoney;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateTitle() {
        return stateTitle;
    }

    public void setStateTitle(String stateTitle) {
        this.stateTitle = stateTitle;
    }

    public int getTotalExchangeLqb() {
        return totalExchangeLqb;
    }

    public void setTotalExchangeLqb(int totalExchangeLqb) {
        this.totalExchangeLqb = totalExchangeLqb;
    }

    public List<ProductListBean> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductListBean> productList) {
        this.productList = productList;
    }

    public static class ProductListBean {
        /**
         * num : 1
         * productId : 52
         * toyName : 100000
         * toyPicUrl : http://wawa-1255388722.cosgz.myqcloud.com/img/qeh1i3124123.jpg
         */

        private int num;
        private int productId;
        private String toyName;
        private String toyPicUrl;

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
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
