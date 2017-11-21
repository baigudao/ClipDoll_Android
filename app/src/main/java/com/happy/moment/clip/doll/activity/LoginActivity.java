package com.happy.moment.clip.doll.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.happy.moment.clip.doll.R;
import com.happy.moment.clip.doll.util.Constants;
import com.rey.material.widget.CheckBox;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.login.LoginApi;
import cn.sharesdk.login.OnLoginListener;
import cn.sharesdk.login.UserInfo;

/**
 * Created by Devin on 2017/11/13 17:59
 * E-mail:971060378@qq.com
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private LinearLayout ll_weixin_login;
    private IWXAPI api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setStatusBarColor(LoginActivity.this, getResources().getColor(R.color.main_color));
        setContentView(R.layout.activity_login);

        ll_weixin_login = (LinearLayout) findViewById(R.id.ll_weixin_login);
        ll_weixin_login.setOnClickListener(this);
        findViewById(R.id.tv_user_protocol).setOnClickListener(this);
        CheckBox material_check_box = (CheckBox) findViewById(R.id.material_check_box);
        material_check_box.setChecked(true);//默认选择
        ll_weixin_login.setEnabled(true);//默认激活
        material_check_box.setOnCheckedChangeListener(this);

        //注册微信
        regToWX();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_weixin_login:
                //                gotoPager(MainActivity.class, null);
                //                login("Wechat");
                wxLogin();
                break;
            case R.id.tv_user_protocol:
                ToastUtils.showShort("协议");
                break;
            default:
                break;
        }
    }

    private void regToWX() {
        api = WXAPIFactory.createWXAPI(LoginActivity.this, Constants.APP_ID, true);
        api.registerApp(Constants.APP_ID);
    }

    public void wxLogin() {
        if (api != null && api.isWXAppInstalled()) {
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "wechat_sdk_demo_test_neng";
            api.sendReq(req);
            finish();
        } else
            ToastUtils.showShort("用户未安装微信");
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            ToastUtils.showShort("被选中了");
            ll_weixin_login.setEnabled(true);
        } else {
            ToastUtils.showShort("没被选中");
            ll_weixin_login.setEnabled(false);
        }
    }

    /**
     * 演示执行第三方登录/注册的方法
     * 这不是一个完整的示例代码，需要根据您项目的业务需求，改写登录/注册回调函数
     *
     * @param platformName 执行登录/注册的平台名称，如：SinaWeibo.NAME
     */
    private void login(final String platformName) {
        LoginApi api = new LoginApi();
        //设置登陆的平台后执行登陆的方法
        api.setPlatform(platformName);
        api.setOnLoginListener(new OnLoginListener() {
            // 在这个方法填写尝试的代码，返回true表示还不能登录，需要注册
            public boolean onLogin(Platform platform, HashMap<String, Object> res) {
                PlatformDb platDB = platform.getDb();//获取平台数据DB
                //通过DB获取各种数据
                platDB.getToken();
                platDB.getUserGender();
                platDB.getUserIcon();
                platDB.getUserId();
                platDB.getUserName();

                UserInfo userInfo = new UserInfo();
                userInfo.setUserIcon(platDB.getUserIcon());
                userInfo.setUserId(platDB.getUserId());
                userInfo.setUserName(platDB.getUserName());
                userInfo.setUserGender(platDB.getUserGender());
                if (platform.getName().equalsIgnoreCase("Wechat")) {
                    userInfo.setUserId((String) res.get("unionid"));
                }
                userInfo.setPlatform(platform);
                LogUtils.e(userInfo.toString());
                oauthLogin(userInfo);
                return true;
            }
        });
        api.login(this);
    }

    private void oauthLogin(final UserInfo userInfo) {
        String weiboId = "";
        String wxId = "";
        if (userInfo.getPlatform().getName().equalsIgnoreCase("SinaWeibo")) {
            weiboId = userInfo.getUserId();
        } else {
            wxId = userInfo.getUserId();
        }
        //        OkHttpUtils.post()
        //                .url(Constant.getOauthLoginUrl())
        //                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
        //                .addParams(Constant.AVATAR, userInfo.getUserIcon())
        //                .addParams(Constant.NICKNAME, userInfo.getUserName())
        //                .addParams("weibo_open_id", weiboId)
        //                .addParams("wx_open_id", wxId)
        //                .build()
        //                .execute(new StringCallback() {
        //                    @Override
        //                    public void onError(Call call, Exception e, int id) {
        //
        //                    }
        //
        //                    @Override
        //                    public void onResponse(String response, int id) {
        //                        JSONObject jsonObject = null;
        //                        try {
        //                            jsonObject = new JSONObject(response);
        //                            int status = jsonObject.optInt("status");
        //                            JSONObject jsonObjectData = jsonObject.optJSONObject("data");
        //                            if (status == 1) {
        //                                Gson gson = new Gson();
        //                                LoginBean loginBean = gson.fromJson(jsonObjectData.toString(), LoginBean.class);
        //                                //存储个人相关信息
        //                                SPUtils.getInstance().put(Constant.SESSION_ID, loginBean.getSession_id());
        //                                SPUtils.getInstance().put(Constant.ACCOUNT, loginBean.getUser_info().getMobile());
        //                                SPUtils.getInstance().put(Constant.CHAT_TOKEN, loginBean.getUser_info().getChat_token());
        //                                SPUtils.getInstance().put(Constant.USER_ID, loginBean.getUser_info().getUser_id());
        //                                DataManager.getInstance().setUserInfo(loginBean.getUser_info());
        //
        //                                if (EmptyUtils.isNotEmpty(SPUtils.getInstance().getString(Constant.SESSION_ID))) {
        //                                    SPUtils.getInstance().put(Constant.IS_USER_LOGIN, true);
        //                                }
        //
        //                                startActivity(new Intent(RegisterAndLoginActivity.this, MainActivity.class));
        //                                RegisterAndLoginActivity.this.finish();
        //                                connect(SPUtils.getInstance().getString(Constant.CHAT_TOKEN));//建立与融云服务器的连接
        //                                ToastUtils.showShort(R.string.login_successful);
        //                            } else {
        //                                int code = jsonObject.optInt("code");
        //                                if (code == 51008) {
        //                                    DataManager.getInstance().setData1(userInfo);
        //                                    gotoPager(OauthRegisterFragment.class, null);
        //                                    return;
        //                                }
        //                                String msg = jsonObjectData.optString("msg");
        //                                ToastUtils.showShort("请求数据失败,请检查网络:" + code + " - " + msg);
        //                            }
        //                        } catch (JSONException e) {
        //                            e.printStackTrace();
        //                        }
        //                    }
        //                });
    }
}
