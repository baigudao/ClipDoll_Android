/*
 * 官网地站:http://www.mob.com
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 *
 * Copyright (c) 2013年 mob.com. All rights reserved.
 */

package com.happy.moment.clip.doll.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.happy.moment.clip.doll.R;
import com.happy.moment.clip.doll.activity.BaseActivity;
import com.happy.moment.clip.doll.activity.MainActivity;
import com.happy.moment.clip.doll.bean.UserInfo;
import com.happy.moment.clip.doll.util.Constants;
import com.happy.moment.clip.doll.util.DataManager;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * 微信客户端回调activity示例
 */
public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private IWXAPI mWeixinAPI;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeixinAPI = WXAPIFactory.createWXAPI(this, Constants.APP_ID, true);
        mWeixinAPI.handleIntent(this.getIntent(), this);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mWeixinAPI.handleIntent(intent, this);//必须调用此句话
    }

    //微信发送的请求将回调到onReq方法
    @Override
    public void onReq(BaseReq req) {
        LogUtils.e("onReq");
    }

    //发送到微信请求的响应结果
    @Override
    public void onResp(BaseResp resp) {
        showLoadingDialog("正在登录中...", null, false);
        int result = 0;
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                LogUtils.e("ERR_OK");
                //发送成功
                SendAuth.Resp sendResp = (SendAuth.Resp) resp;
                if (sendResp != null) {
                    String code = sendResp.code;
                    getDataFromNet(code);
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                LogUtils.e("ERR_USER_CANCEL");
                //发送取消
                result = R.string.cancel;
                hideLoadingDialog();
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                LogUtils.e("ERR_AUTH_DENIED");
                //发送被拒绝
                result = R.string.reject;
                hideLoadingDialog();
                finish();
                break;
            default:
                //发送返回
                finish();
                hideLoadingDialog();
                break;
        }
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    }

    private void getDataFromNet(String code) {
        //网络请求，根据自己的请求方式
        OkHttpUtils.post()
                .url(Constants.getUserLoginUrl())
                .addParams(Constants.SESSION, getJsonData("xxx", 100).toString())
                .addParams(Constants.LOGINTYPE, String.valueOf(1))
                .addParams(Constants.PLATFORM, "Android")
                .addParams(Constants.WECHATCODE, code)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e(e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.e(response);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            JSONObject jsonObjectResHead = jsonObject.optJSONObject("resHead");
                            int code = jsonObjectResHead.optInt("code");
                            String msg = jsonObjectResHead.optString("msg");
                            String req = jsonObjectResHead.optString("req");
                            JSONObject jsonObjectResBody = jsonObject.optJSONObject("resBody");
                            if (code == 1) {
                                String ticket = jsonObjectResBody.optString("ticket");
                                int userId = jsonObjectResBody.optInt("userId");
                                JSONObject jsonObjectTicketAndUserId = getJsonData(ticket, userId);
                                //存储ticket和userId
                                SPUtils.getInstance().put(Constants.SESSION, jsonObjectTicketAndUserId.toString());
                                JSONObject jsonObjectUserInfo = jsonObjectResBody.optJSONObject("userInfo");
                                if (EmptyUtils.isNotEmpty(jsonObjectUserInfo)) {
                                    Gson gson = new Gson();
                                    UserInfo userInfo = gson.fromJson(jsonObjectUserInfo.toString(), UserInfo.class);
                                    //存储第三方微信登录返回的用户信息
                                    SPUtils.getInstance().put(Constants.HEADIMG, userInfo.getHeadImg());
                                    SPUtils.getInstance().put(Constants.INVITECODE, userInfo.getInviteCode());
                                    SPUtils.getInstance().put(Constants.NICKNAME, userInfo.getNickName());
                                    SPUtils.getInstance().put(Constants.USERID, userInfo.getUserId());
                                    DataManager.getInstance().setUserInfo(userInfo);

                                    //存储登录状态
                                    if (EmptyUtils.isNotEmpty(SPUtils.getInstance().getString(Constants.SESSION))) {
                                        SPUtils.getInstance().put(Constants.IS_USER_LOGIN, true);
                                    }
                                    hideLoadingDialog();
                                    gotoPager(MainActivity.class, null);
                                    finish();
                                    ToastUtils.showShort(R.string.login_successful);
                                }

                            } else {
                                LogUtils.e("请求数据失败：" + msg + "-" + code + "-" + req);
                                ToastUtils.showShort("请求数据失败,请检查网络并重试！");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public JSONObject getJsonData(String ticket, int userId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ticket", ticket);
            jsonObject.put("userId", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * 获取openid accessToken值用于后期操作
     *
     * @param code 请求码
     */
    private void getAccess_token(final String code) {
        String path = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                + Constants.APP_ID
                + "&secret="
                + Constants.APP_SECRET
                + "&code="
                + code
                + "&grant_type=authorization_code";
    }
}
