package com.happy.moment.clip.doll.bean;

/**
 * Created by Devin on 2017/11/28 00:45
 * E-mail:971060378@qq.com
 */

public class AddressBean extends BaseBean {

    /**
     * addressId : 39
     * city : 韶关
     * isDefault : 0
     * phone : 15623457858
     * province : 广东
     * street : 大道中
     * userId : 1000002
     * userName : 哈西
     */

    private int addressId;
    private String city;
    private int isDefault;
    private String phone;
    private String province;
    private String street;
    private int userId;
    private String userName;

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
