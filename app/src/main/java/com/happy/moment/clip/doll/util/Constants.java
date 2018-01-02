package com.happy.moment.clip.doll.util;

import android.os.Environment;

public class Constants {

    //一般常量
    public static final String APP_ID = "wx5419793b4eeff9be";//微信开放平台AppID
    public static final String FRAGMENT_NAME = "fragment_name";
    public static final int LIVE_APPID = 1400048722;
    public static final int ACCOUNT_TYPE = 19064;
    //    public static final int LIVE_APPID = 1400055042;//测试环境
    //    public static final int ACCOUNT_TYPE = 20348;//测试环境
    public static final String IS_ENTER_GUIDE_VIEW = "is_enter_guide_view";
    public static final String IS_USER_LOGIN = "is_user_login";
    public static final String LOGINTYPE = "loginType";
    public static final String PLATFORM = "platform";
    public static final String PLATFORMTYPE = "platformType";
    public static final String ACCOUNTTYPE = "accountType";
    public static final String PWD = "pwd";
    public static final String VERIFYCODE = "verifyCode";
    public static final String VERIFLYCODE = "veriflyCode";
    public static final String WECHATCODE = "wechatCode";
    public static final String SDKTYPE = "sdkType";
    public static final String REALNAME = "realName";
    public static final String SESSION = "session";
    public static final String HEADIMG = "headImg";
    public static final String INVITECODE = "inviteCode";
    public static final String NICKNAME = "nickName";
    public static final String USERID = "userId";
    public static final String DEVICE_ID = "device_id";
    public static final String PHONE = "phone";
    public static final String MY_USER_INFO = "my_user_info";
    public static final String TLSSIGN = "tlsSign";
    public static final String OPINION = "opinion";
    public static final String GROUPID = "groupId";
    public static final String ROOMID = "roomId";
    public static final String CONTENT = "content";
    public static final String GAMEID = "gameId";
    public static final String PRICEID = "priceId";
    public static final String ADDRESSID = "addressId";
    public static final String OPER = "oper";
    public static final String LEFT = "left";
    public static final String RIGHT = "right";
    public static final String DOWN = "down";
    public static final String UP = "up";
    public static final String CATCH = "catch";
    public static final String PAGENUM = "pageNum";
    public static final String PAGESIZE = "pageSize";
    public static final String RESULT = "result";
    public static final String TYPE = "type";
    public static final String WSURL = "wsUrl";
    public static final String PLAYID = "playId";
    public static final String MESSAGEID = "messageId";
    public static final String NOTIFYNUM = "notifyNum";
    public static final String FIRSTLOGIN = "firstLogin";
    public static final String FROMINVITECODE = "fromInviteCode";
    public static final String IS_PLAY_BACKGROUND_SOUND = "is_play_background_sound";
    public static final String IS_PLAY_BACKGROUND_MUSIC = "is_play_background_music";
    public static final int PAGE_SIZE = 10;
    public static final String ROLE_MASTER = "LiveMaster";
    public static final String ROLE_GUEST = "Guest";
    public static final String ROLE_LIVEGUEST = "LiveGuest";
    public static final String JOIN_ROOM_FAIL = "加入房间失败";
    public static final String CACHE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();


    //网络常量
    private static final String BASE_URL = "https://api.52z.cn/wawa_api/";//119.29.119.179:8090  正式环境
    //    private static final String BASE_URL = "http://wwapi.tuuban.com/wawa_api/";//测试环境
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
    private static final String changeNotifyStateUrl = BASE_URL + "message/changeNotifyState/v1";//修改消息的状态为已读
    private static final String notifyCountUrl = BASE_URL + " message/getNotifyCount/v1";//获取消息数量

    private static final String verifyInviteUrl = BASE_URL + "inviteAward/verifyInviteCode/v1";//邀请有奖
    private static final String inviteCodeUrl = BASE_URL + "inviteAward/getInviteCodeByUserId/v1";//获取用户兑换过的邀请码

    private static final String logoutUrl = BASE_URL + "user/account/logout/v1";//登录注销

    private static final String rechargePriceList = BASE_URL + "recharge/getLqbPriceByPlatformType/v1";//获取价目表
    private static final String rechargeUrl = BASE_URL + "recharge/rechargeLqb/v1";//虚拟充值
    private static final String isFirstRechargeUrl = BASE_URL + "recharge/isFirstRecharge/v1";//是否首充
    private static final String weChatPayUrl = BASE_URL + "recharge/wechatPay/v1";//微信充值

    private static final String waitingSendUrl = BASE_URL + "order/unsentProducts/v1";//待发货
    private static final String sendOverUrl = BASE_URL + "order/sentedProducts/v1";//待发货

    private static final String userAddressSaveUrl = BASE_URL + "user/address/save/v1";//保存地址
    private static final String userAddressListUrl = BASE_URL + "user/address/list/v1";//地址列表
    private static final String userAddressDeleteUrl = BASE_URL + "user/address/delete/v1";//删除地址

    private static final String checkVersionUrl = BASE_URL + "version/checkAppVersionByVersion/v1";//获取版本信息
    private static final String userProtocolUrl = "http://wwh5.tuuban.com/privacy";//用户协议

    private static final String applyBeginGameUrl = BASE_URL + "live/room/applyBeginGameFromMDD/v1";//申请开始游戏(非腾讯)
    private static final String applyBeginGameUrlTX = BASE_URL + "live/room/applyBeginGame/v1";//申请开始游戏(腾讯的)
    private static final String playGameUrl = BASE_URL + "live/room/playGeme/v1";//游戏操控(非腾讯)
    private static final String gameResultUrl = BASE_URL + "live/room/getMyGameResult/v1";//游戏结果(非腾讯)
    private static final String liveRoomStateUrl = BASE_URL + "live/room/getLiveRoomState/v1";//房间状态
    private static final String roomStateUrl = BASE_URL + "live/room/getRoomStateById/v1";//获取直播间状态
    private static final String addUserForLiveRoomUrl = BASE_URL + "live/room/addUserForLiveRoom/v1";//用户加入直播间 计数
    private static final String removeUserForLiveRoomUrl = BASE_URL + "live/room/removeUserForLiveRoom/v1";//用户退出直播间 计数
    private static final String liveRoomUserUrl = BASE_URL + "live/room/getLiveRoomUser/v1";//获取直播间观众数和玩家 计数

    private static final String feedBackPostServerUrl = BASE_URL + "terminal/fault/save/v1";// 上报故障

    private static final String findPwdVerificationCodeUrl = BASE_URL + "user/account/findPwdGetVerifyCode/v1";//根据注册的手机号码发送验证码
    private static final String verificationCodeUrl = BASE_URL + "user/account/getVerifyCode/v1";//获取验证码
    private static final String userRegisterUrl = BASE_URL + "user/account/register/v1";//手机注册

    private static final String openAgentUrl = BASE_URL + "channel/agenter/apply/v1";//开启代理权
    private static final String myIncomeUrl = BASE_URL + "user/agenter/info/v1";//分销商-我的收益

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

    public static String getIsFirstRechargeUrl() {
        return isFirstRechargeUrl;
    }

    public static String getWeChatPayUrl() {
        return weChatPayUrl;
    }

    public static String getApplyBeginGameUrl() {
        return applyBeginGameUrl;
    }

    public static String getApplyBeginGameUrlTX() {
        return applyBeginGameUrlTX;
    }

    public static String getUserProtocolUrl() {
        return userProtocolUrl;
    }

    public static String getPlayGameUrl() {
        return playGameUrl;
    }

    public static String getGameResultUrl() {
        return gameResultUrl;
    }

    public static String getLiveRoomStateUrl() {
        return liveRoomStateUrl;
    }

    public static String getInviteCodeUrl() {
        return inviteCodeUrl;
    }

    public static String getRoomStateUrl() {
        return roomStateUrl;
    }

    public static String getAddUserForLiveRoomUrl() {
        return addUserForLiveRoomUrl;
    }

    public static String getRemoveUserForLiveRoomUrl() {
        return removeUserForLiveRoomUrl;
    }

    public static String getLiveRoomUserUrl() {
        return liveRoomUserUrl;
    }

    public static String getChangeNotifyStateUrl() {
        return changeNotifyStateUrl;
    }

    public static String getFeedBackPostServerUrl() {
        return feedBackPostServerUrl;
    }

    public static String getNotifyCountUrl() {
        return notifyCountUrl;
    }

    public static String getVerificationCodeUrl() {
        return verificationCodeUrl;
    }

    public static String getFindPwdVerificationCodeUrl() {
        return findPwdVerificationCodeUrl;
    }

    public static String getUserRegisterUrl() {
        return userRegisterUrl;
    }

    public static String getOpenAgentUrl() {
        return openAgentUrl;
    }

    public static String getMyIncomeUrl() {
        return myIncomeUrl;
    }
}
