package com.happy.moment.clip.doll.util;

public class Constants {

    //一般常量
    public static final String APP_ID = "wx8bac6dd603d47d15";//微信开放平台AppID
    public static final String FRAGMENT_NAME = "fragment_name";
    public static final int LIVE_APPID = 1400048722;
    public static final int ACCOUNT_TYPE = 19064;
    public static final String IS_ENTER_GUIDE_VIEW = "is_enter_guide_view";
    public static final String IS_USER_LOGIN = "is_user_login";


    // 存储
    public static final String USERINFO = "userInfo";
    public static final String ACCOUNT = "account";
    public static final String PWD = "password";
    public static final String ROOM = "room";

    // 角色
    public static final String ROLE_MASTER = "LiveMaster";
    public static final String ROLE_GUEST = "Guest";
    public static final String ROLE_LIVEGUEST = "LiveGuest";

    // 直播业务id和appid，可在控制台的 直播管理中查看
    public static final int BIZID = 8525;
    public static final int APPID = 1253488539;     // 直播appid

    // 直播的API鉴权Key，可在控制台的 直播管理 => 接入管理 => 直播码接入 => 接入配置 中查看
    public static final String MIX_API_KEY = "45eeb9fc2e4e6f88b778e0bbd9de3737";
    // 固定地址
    public static final String MIX_SERVER = "http://fcgi.video.qcloud.com";

    public static final int MAX_SIZE = 50;


    //网络常量
    private static final String BASE_URL = "http://192.168.1.108:8080/wawa_api/";
    private static final String homeRoomListUrl = BASE_URL + "live/room/getHomeRoomList/v1";
    private static final String homeBannerUrl = BASE_URL + "home/getBannerList/v1";
    private static final String clipDollRecordUrl = BASE_URL + "playRecord/getPlayRecordList/v1";


    public static String getHomeRoomListUrl() {
        return homeRoomListUrl;
    }

    public static String getHomeBannerUrl() {
        return homeBannerUrl;
    }

    public static String getClipDollRecordUrl() {
        return clipDollRecordUrl;
    }
}
