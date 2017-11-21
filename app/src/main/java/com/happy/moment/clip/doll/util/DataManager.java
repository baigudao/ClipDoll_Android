package com.happy.moment.clip.doll.util;


import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.happy.moment.clip.doll.bean.UserInfo;

public class DataManager {

    private static DataManager mDataManager;

    private static final Object mObject = new Object();

    private Object mData, mData1, mData2, mData3, mData4, mData5, mData6, mData7, mData8, mData9;

    private DataManager() {
    }

    public static DataManager getInstance() {
        if (mDataManager == null) {
            synchronized (mObject) {
                if (mDataManager == null) {
                    mDataManager = new DataManager();
                }
            }
        }
        return mDataManager;
    }

    public void setObject(Object data) {
        mData = data;
    }

    public Object getObject() {
        return mData;
    }

    public void setData1(Object data1) {
        mData1 = data1;
    }

    public Object getData1() {
        return mData1;
    }

    public void setData2(Object data2) {
        mData2 = data2;
    }

    public Object getData2() {
        return mData2;
    }

    public void setData3(Object data3) {
        mData3 = data3;
    }

    public Object getData3() {
        return mData3;
    }

    public void setData4(Object data4) {
        mData4 = data4;
    }

    public Object getData4() {
        return mData4;
    }

    public void setData5(Object data5) {
        mData5 = data5;
    }

    public Object getData5() {
        return mData5;
    }

    public Object getData6() {
        return mData6;
    }

    public void setData6(Object mData6) {
        this.mData6 = mData6;
    }

    public Object getData7() {
        return mData7;
    }

    public void setData7(Object mData7) {
        this.mData7 = mData7;
    }

    public Object getData8() {
        return mData8;
    }

    public void setData8(Object mData8) {
        this.mData8 = mData8;
    }

    public Object getData9() {
        return mData9;
    }

    public void setData9(Object mData9) {
        this.mData9 = mData9;
    }

    /**
     * 保存当前用户信息
     *
     * @param userInfo
     */
    public void setUserInfo(UserInfo userInfo) {
        SPUtils.getInstance().put(Constants.MY_USER_INFO, userInfo == null ? "" : CommonUtil.toJson(userInfo));
    }

    /**
     * 获取当前用户信息
     */
    public UserInfo getUserInfo() {
        String userInfoString = SPUtils.getInstance().getString(Constants.MY_USER_INFO);
        if (EmptyUtils.isEmpty(userInfoString)) {
            return null;
        }
        UserInfo userInfo = CommonUtil.fromJson(UserInfo.class, userInfoString);
        return userInfo;
    }
}
