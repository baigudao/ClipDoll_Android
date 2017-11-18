package com.happy.moment.clip.doll.bean;

/**
 * Created by Devin on 2017/11/18 11:35
 * E-mail:971060378@qq.com
 */

public class BannerBean extends BaseBean {

    /**
     * bannerId : 1
     * createTime : 1510399952000
     * endDate : 1512041534000
     * icon : http://tu.duowan.com/gallery/135895.html
     * lastUpdateTime : 1510973572000
     * location : 0
     * objType : 1
     * objectId : 1
     * parentId : 1
     * pictures : http://tu.duowan.com/gallery/135905.html
     * startDate : 1510399931000
     * state : 0
     * title : 第一个banner
     * url : www.baidu.com
     * weight : 0
     */

    private int bannerId;
    private long createTime;
    private long endDate;
    private String icon;
    private long lastUpdateTime;
    private int location;
    private int objType;
    private String objectId;
    private int parentId;
    private String pictures;
    private long startDate;
    private int state;
    private String title;
    private String url;
    private int weight;

    public int getBannerId() {
        return bannerId;
    }

    public void setBannerId(int bannerId) {
        this.bannerId = bannerId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public int getObjType() {
        return objType;
    }

    public void setObjType(int objType) {
        this.objType = objType;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
