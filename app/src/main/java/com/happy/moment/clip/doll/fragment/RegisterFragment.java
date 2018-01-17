package com.happy.moment.clip.doll.fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.happy.moment.clip.doll.R;
import com.happy.moment.clip.doll.util.Constants;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;

/**
 * Created by Devin on 2017/12/22 11:07
 * E-mail:971060378@qq.com
 */

public class RegisterFragment extends BaseFragment {

    private Timer mTimer;
    private int mTotalTime;
    private TimerTask mTask;

    private Button get_verification_code;
    private EditText nickname_register;
    private EditText phone_register;
    private EditText input_verification_code_register;
    private EditText input_password_register;
    private Button btn_private_register;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register;
    }

    private String getNickName() {
        return nickname_register.getText().toString().trim();
    }

    private String getMobile() {
        return phone_register.getText().toString().trim();
    }

    private String getVerificationCode() {
        return input_verification_code_register.getText().toString().trim();
    }

    private String getNewPassword() {
        return input_password_register.getText().toString().trim();
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.ll_close).setOnClickListener(this);//初始化返回图标
        view.findViewById(R.id.iv_share).setVisibility(View.GONE);

        nickname_register = (EditText) view.findViewById(R.id.nickname_register);//初始化昵称
        nickname_register.addTextChangedListener(new MyTextChangedListener());
        phone_register = (EditText) view.findViewById(R.id.phone_register);//初始化手机号
        phone_register.addTextChangedListener(new MyTextChangedListener());
        get_verification_code = (Button) view.findViewById(R.id.get_verification_code);//初始化验证码按钮
        get_verification_code.setOnClickListener(this);
        input_verification_code_register = (EditText) view.findViewById(R.id.input_verification_code_register);//初始化输入验证码
        input_verification_code_register.addTextChangedListener(new MyTextChangedListener());
        input_password_register = (EditText) view.findViewById(R.id.input_password_register);//初始化密码
        input_password_register.addTextChangedListener(new MyTextChangedListener());
        btn_private_register = (Button) view.findViewById(R.id.btn_private_register);//初始化注册按钮
        btn_private_register.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_close://返回图标
                goBack();
                break;
            case R.id.get_verification_code://获取验证码按钮
                if (mTotalTime > 0) {
                    return;
                }
                final String mobile = getMobile();
                if (!RegexUtils.isMobileExact(mobile)) {
                    Toast.makeText(getActivity(), "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(mobile)) {
                    Toast.makeText(getActivity(), getString(R.string.please_input_phone_or_name), Toast.LENGTH_SHORT).show();
                    return;
                }
                mTotalTime = 60;
                mTimer = new Timer();
                initTimerTask();
                mTimer.schedule(mTask, 1000, 1000);
                OkHttpUtils.get()
                        .url(Constants.getVerificationCodeUrl())
                        .addParams(Constants.SESSION, SPUtils.getInstance().getString(Constants.SESSION))
                        .addParams(Constants.PHONE, mobile)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public void onResponse(String response, int id) {
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response);
                                    JSONObject jsonObjectResHead = jsonObject.optJSONObject("resHead");
                                    int code = jsonObjectResHead.optInt("code");
                                    String msg = jsonObjectResHead.optString("msg");
                                    final String req = jsonObjectResHead.optString("req");
                                    JSONObject jsonObjectResBody = jsonObject.optJSONObject("resBody");
                                    if (code == 1) {
                                        int success = jsonObjectResBody.optInt("success");
                                        String alertMsg = jsonObjectResBody.optString("alertMsg");
                                    } else {
                                        LogUtils.e("请求数据失败：" + msg + "-" + code + "-" + req);
                                        ToastUtils.showShort("请求数据失败" + msg);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                break;
            case R.id.btn_private_register://注册按钮
                final String name = getNickName();
                final String phone = getMobile();
                final String verCode = getVerificationCode();
                final String password = getNewPassword();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(verCode) || TextUtils.isEmpty(password)) {
                    Toast.makeText(getActivity(), getString(R.string.please_input_all_message), Toast.LENGTH_SHORT).show();
                    return;
                }
                OkHttpUtils.post()
                        .url(Constants.getUserRegisterUrl())
                        .addParams(Constants.SESSION, SPUtils.getInstance().getString(Constants.SESSION))
                        .addParams(Constants.ACCOUNTTYPE, String.valueOf(0))
                        .addParams(Constants.NICKNAME, name)
                        .addParams(Constants.PHONE, phone)
                        .addParams(Constants.VERIFYCODE, verCode)
                        .addParams(Constants.PWD, EncryptUtils.encryptMD5ToString(password))
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public void onResponse(String response, int id) {
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response);
                                    JSONObject jsonObjectResHead = jsonObject.optJSONObject("resHead");
                                    int code = jsonObjectResHead.optInt("code");
                                    String msg = jsonObjectResHead.optString("msg");
                                    final String req = jsonObjectResHead.optString("req");
                                    JSONObject jsonObjectResBody = jsonObject.optJSONObject("resBody");
                                    if (code == 1) {
                                        int success = jsonObjectResBody.optInt("success");
                                        String alertMsg = jsonObjectResBody.optString("alertMsg");
                                        switch (success) {
                                            case 0:
                                                ToastUtils.showShort(alertMsg);
                                                break;
                                            case 1:
                                                ToastUtils.showShort("注册成功");
                                                goBack();
                                                break;
                                            default:
                                                break;
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
                break;
            default:
                break;
        }
    }

    /**
     * 初始化时间任务器
     */
    private void initTimerTask() {
        mTask = new TimerTask() {
            @Override
            public void run() {
                if (getView() == null) {
                    mTimer.cancel();
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        --mTotalTime;
                        if (getView() == null) {
                            mTimer.cancel();
                            return;
                        }
                        get_verification_code.setText(String.valueOf(mTotalTime));
                        get_verification_code.setTextSize(15);
                        if (mTotalTime <= 0) {
                            mTimer.cancel();
                            get_verification_code.setText(getString(R.string.free_get));//免费获取
                            get_verification_code.setTextSize(12);
                            get_verification_code.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_enable_selector));
                        } else {
                            get_verification_code.setBackgroundDrawable(getResources().getDrawable(R.drawable.my_verification_code_shape));//倒计时时候的背景
                        }
                    }
                });
            }
        };
    }

    private class MyTextChangedListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            boolean nickName = nickname_register.getText().length() > 0;
            boolean phone = phone_register.getText().length() > 0;
            boolean verificationCode = input_verification_code_register.getText().length() > 0;
            boolean password = input_password_register.getText().length() > 0;
            if (phone) {
                get_verification_code.setEnabled(true);
            } else {
                get_verification_code.setEnabled(false);
            }
            if (nickName & phone & verificationCode & password) {
                btn_private_register.setEnabled(true);
            } else {
                btn_private_register.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }
}
