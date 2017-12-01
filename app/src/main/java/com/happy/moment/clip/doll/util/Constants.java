package com.happy.moment.clip.doll.util;

public class Constants {

    //一般常量
    public static final String APP_ID = "wx5419793b4eeff9be";//微信开放平台AppID
    public static final String APP_SECRET = "d44b28417caa330d12128338ecfea664";//微信开放平台AppID
    public static final String FRAGMENT_NAME = "fragment_name";
    public static final int LIVE_APPID = 1400048722;//  1400051630
    public static final int ACCOUNT_TYPE = 19064;//  19613
    public static final String IS_ENTER_GUIDE_VIEW = "is_enter_guide_view";
    public static final String IS_USER_LOGIN = "is_user_login";
    public static final String LOGINTYPE = "loginType";
    public static final String PLATFORM = "platform";
    public static final String WECHATCODE = "wechatCode";
    public static final String SESSION = "session";
    public static final String HEADIMG = "headImg";
    public static final String INVITECODE = "inviteCode";
    public static final String NICKNAME = "nickName";
    public static final String USERID = "userId";
    public static final String DEVICE_ID = "device_id";
    public static final String MY_USER_INFO = "my_user_info";
    public static final String TLSSIGN = "tlsSign";
    public static final String OPINION = "opinion";
    public static final String GROUPID = "groupId";
    public static final String PAGENUM = "pageNum";
    public static final String PAGESIZE = "pageSize";
    public static final String RESULT = "result";
    public static final String FROMINVITECODE = "fromInviteCode";
    public static final String FROMUSERID = "fromUserId";
    public static final int PAGE_SIZE = 10;


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
    private static final String BASE_URL = "http://119.29.119.179:8090/wawa_api/";//192.168.1.108:8080（蔡）//119.29.119.179:8090//192.168.1.112:8080（盾哥）
    private static final String homeRoomListUrl = BASE_URL + "live/room/getHomeRoomList/v1";
    private static final String homeBannerUrl = BASE_URL + "home/getBannerList/v1";
    private static final String clipDollRecordUrl = BASE_URL + "playRecord/getPlayRecordList/v1";
    private static final String userLoginUrl = BASE_URL + "user/account/login/v1";
    private static final String liveSigUrl = BASE_URL + "user/getTlsSign/v1";
    private static final String userFeedBack = BASE_URL + "feedback/saveFeedback/v1";
    private static final String userInfo = BASE_URL + "user/getUserInfo/v1";
    private static final String userBalance = BASE_URL + "user/getUserBalance/v1";//得到余额
    private static final String applyBeginGame = BASE_URL + "live/room/applyBeginGame/v1";
    private static final String gameOverUrl = BASE_URL + "live/room/gameover/v1";
    private static final String coinCostRecordUrl = BASE_URL + "user/getCoinRecords/v1";//游戏币消费记录
    private static final String myNotifyUrl = BASE_URL + "message/getMyNotify/v1";//我的通知
    private static final String verifyInviteUrl = BASE_URL + "inviteAward/verifyInviteCode/v1";//邀请有奖
    private static final String logoutUrl = BASE_URL + "user/account/logout/v1";//登录注销

    private static final String rechargePriceList = BASE_URL + "recharge/getLqbPriceByPlatformType/v1";//获取价目表
    private static final String rechargeUrl = BASE_URL + "recharge/rechargeLqb/v1";//充值

    private static final String waitingSendUrl = BASE_URL + "order/unsentProducts/v1";//待发货
    private static final String sendOverUrl = BASE_URL + "order/sentedProducts/v1";//待发货

    private static final String userAddressSaveUrl = BASE_URL + "user/address/save/v1";//保存地址
    private static final String userAddressListUrl = BASE_URL + "user/address/list/v1";//地址列表
    private static final String userAddressDeleteUrl = BASE_URL + "user/address/delete/v1";//删除地址

    private static final String checkVersionUrl = BASE_URL + "version/checkAppVersionByVersion/v1";//获取版本信息

    public static String getHomeRoomListUrl() {
        return homeRoomListUrl;
    }

    public static String getHomeBannerUrl() {
        return homeBannerUrl;
    }

    public static String getClipDollRecordUrl() {
        return clipDollRecordUrl;
    }

    public static String getUserLoginUrl() {
        return userLoginUrl;
    }

    public static String getLiveSigUrl() {
        return liveSigUrl;
    }

    public static String getUserFeedBack() {
        return userFeedBack;
    }

    public static String getUserInfo() {
        return userInfo;
    }

    public static String getUserBalance() {
        return userBalance;
    }

    public static String getApplyBeginGame() {
        return applyBeginGame;
    }

    public static String getGameOverUrl() {
        return gameOverUrl;
    }

    public static String getCoinCostRecordUrl() {
        return coinCostRecordUrl;
    }

    public static String getMyNotifyUrl() {
        return myNotifyUrl;
    }

    public static String getVerifyInviteUrl() {
        return verifyInviteUrl;
    }

    public static String getLogoutUrl() {
        return logoutUrl;
    }

    public static String getRechargePriceList() {
        return rechargePriceList;
    }

    public static String getRechargeUrl() {
        return rechargeUrl;
    }

    public static String getWaitingSendUrl() {
        return waitingSendUrl;
    }

    public static String getSendOverUrl() {
        return sendOverUrl;
    }

    public static String getUserAddressSaveUrl() {
        return userAddressSaveUrl;
    }

    public static String getUserAddressListUrl() {
        return userAddressListUrl;
    }

    public static String getUserAddressDeleteUrl() {
        return userAddressDeleteUrl;
    }

    public static String getCheckVersionUrl() {
        return checkVersionUrl;
    }
}
