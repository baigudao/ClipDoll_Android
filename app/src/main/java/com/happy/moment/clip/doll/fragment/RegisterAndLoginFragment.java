package com.happy.moment.clip.doll.fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.happy.moment.clip.doll.R;
import com.happy.moment.clip.doll.activity.MainActivity;
import com.happy.moment.clip.doll.bean.UserInfo;
import com.happy.moment.clip.doll.util.Constants;
import com.happy.moment.clip.doll.util.DataManager;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by Devin on 2017/12/22 10:52
 * E-mail:971060378@qq.com
 */

public class RegisterAndLoginFragment extends BaseFragment {

    private EditText et_account_login;
    private EditText et_password_login;
    private Button btn_login;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register_and_login;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.ll_close).setOnClickListener(this);
        view.findViewById(R.id.iv_share).setVisibility(View.GONE);
        TextView tv_cost_record = (TextView) view.findViewById(R.id.tv_cost_record);
        tv_cost_record.setVisibility(View.VISIBLE);
        tv_cost_record.setText("注册");
        tv_cost_record.setOnClickListener(this);

        et_account_login = (EditText) view.findViewById(R.id.et_account_login);
        et_account_login.addTextChangedListener(new TextChangedListener());
        et_password_login = (EditText) view.findViewById(R.id.et_password_login);
        et_password_login.addTextChangedListener(new TextChangedListener());
        btn_login = (Button) view.findViewById(R.id.btn_login);//登录按钮
        btn_login.setOnClickListener(this);
    }

    public String getAccount() {
        return et_account_login.getText().toString().trim();
    }

    public String getPassword() {
        return et_password_login.getText().toString().trim();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_close:
                goBack();
                break;
            case R.id.tv_cost_record:
                gotoPager(RegisterFragment.class, null);
                break;
            case R.id.btn_login:
                login();
                break;
            default:
                break;
        }
    }

    private void login() {
        final String account = getAccount();
        final String password = getPassword();
        OkHttpUtils.post()
                .url(Constants.getUserLoginUrl())
                .addParams(Constants.SESSION, SPUtils.getInstance().getString(Constants.SESSION))
                .addParams(Constants.LOGINTYPE, String.valueOf(0))
                .addParams(Constants.PHONE, account)
                .addParams(Constants.PLATFORM, "Android")
                .addParams(Constants.PWD, EncryptUtils.encryptMD5ToString(password))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

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
                            final String req = jsonObjectResHead.optString("req");
                            JSONObject jsonObjectResBody = jsonObject.optJSONObject("resBody");
                            if (code == 1) {
                                String ticket = jsonObjectResBody.optString("ticket");
                                int userId = jsonObjectResBody.optInt("userId");
                                JSONObject jsonObjectTicketAndUserId = getJsonData(ticket, userId);
                                //存储ticket和userId
                                SPUtils.getInstance().put(Constants.SESSION, jsonObjectTicketAndUserId.toString());
                                String tlsSign = jsonObjectResBody.optString("tlsSign");
                                //存储sig
                                SPUtils.getInstance().put(Constants.TLSSIGN, tlsSign);
                                JSONObject jsonObjectUserInfo = jsonObjectResBody.optJSONObject("userInfo");
                                if (EmptyUtils.isNotEmpty(jsonObjectUserInfo)) {
                                    Gson gson = new Gson();
                                    UserInfo userInfo = gson.fromJson(jsonObjectUserInfo.toString(), UserInfo.class);
                                    //存储第三方微信登录返回的用户信息
                                    SPUtils.getInstance().put(Constants.FIRSTLOGIN, userInfo.getFirstLogin());
                                    SPUtils.getInstance().put(Constants.HEADIMG, userInfo.getHeadImg());
                                    SPUtils.getInstance().put(Constants.INVITECODE, userInfo.getInviteCode());
                                    SPUtils.getInstance().put(Constants.NICKNAME, userInfo.getNickName());
                                    SPUtils.getInstance().put(Constants.USERID, userInfo.getUserId());
                                    SPUtils.getInstance().put(Constants.ROLE,userInfo.getRole());
                                    DataManager.getInstance().setUserInfo(userInfo);

                                    //配置信鸽推送
                                    configXGPush();

                                    //存储登录状态
                                    if (EmptyUtils.isNotEmpty(SPUtils.getInstance().getString(Constants.SESSION))) {
                                        SPUtils.getInstance().put(Constants.IS_USER_LOGIN, true);
                                    }
                                    gotoPager(MainActivity.class, null);
                                    getActivity().finish();
                                    ToastUtils.showShort(R.string.login_successful);
                                    //隐藏软键盘
                                    KeyboardUtils.hideSoftInput(getActivity());
                                }
                            } else {
                                LogUtils.e("请求数据失败：" + msg + "-" + code + "-" + req);
                                ToastUtils.showShort("请求数据失败" + msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    private class TextChangedListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            boolean user = et_account_login.getText().length() > 0;
            boolean pwd = et_password_login.getText().length() > 0;
            if (user & pwd) {
                btn_login.setEnabled(true);
            } else {
                btn_login.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
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

    private void configXGPush() {
        XGPushConfig.enableDebug(mContext, true);
        XGPushManager.setTag(mContext, "XINGE");
        XGPushManager.registerPush(mContext, String.valueOf(SPUtils.getInstance().getInt(Constants.USERID)), new XGIOperateCallback() {
            @Override
            public void onSuccess(Object o, int i) {
                LogUtils.e("注册成功，设备token为：" + o);
            }

            @Override
            public void onFail(Object o, int i, String s) {
                LogUtils.e("注册失败，错误码：" + i + ",错误信息：" + s);
            }
        });
    }
}
