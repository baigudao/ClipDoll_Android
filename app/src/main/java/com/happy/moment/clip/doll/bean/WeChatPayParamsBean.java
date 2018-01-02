package com.happy.moment.clip.doll.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Devin on 2017/12/2 15:13
 * E-mail:971060378@qq.com
 */

public class WeChatPayParamsBean extends BaseBean {

    /**
     * appid : wx5419793b4eeff9be
     * noncestr : 1512198629QE7KYLOR482JCATAPTY
     * package : Sign=WXPay
     * partnerid : 1492975272
     * prepayid : wx201712021510299c1244e2d30246461244
     * sign : E2CE2CEC2562E792D398913BC5775BE3
     * timestamp : 1512198629
     */

    private String appid;
    private String noncestr;
    @SerializedName("package")
    private String packageX;
    private String partnerid;
    private String prepayid;
    private String sign;
    private int timestamp;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getPackageX() {
        return packageX;
    }

    public void setPackageX(String packageX) {
        this.packageX = packageX;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }
}
