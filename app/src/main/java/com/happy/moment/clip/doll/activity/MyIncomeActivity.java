package com.happy.moment.clip.doll.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.happy.moment.clip.doll.R;
import com.happy.moment.clip.doll.bean.WeChatPayParamsBean;
import com.happy.moment.clip.doll.util.Constants;
import com.happy.moment.clip.doll.util.DataManager;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;

/**
 * Created by Devin on 2017/12/29 19:48
 * E-mail:971060378@qq.com
 */

public class MyIncomeActivity extends BaseActivity implements View.OnClickListener {

    private Timer mTimer;
    private int mTotalTime;
    private TimerTask mTask;

    private TextView tv_income_name;
    private ImageView iv_income_photo;

    private EditText et_put_real_name;
    private EditText et_put_real_phone;
    private EditText et_put_verification_code;

    private Button btn_get_verification_code;

    private Button btn_open_permission;

    private IWXAPI api;
    private View view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_my_income, null);
        setContentView(view);

        findViewById(R.id.ll_close).setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_bar_title)).setText("我要赚钱");
        findViewById(R.id.iv_share).setVisibility(View.GONE);

        tv_income_name = (TextView) findViewById(R.id.tv_income_name);
        iv_income_photo = (ImageView) findViewById(R.id.iv_income_photo);

        et_put_real_name = (EditText) findViewById(R.id.et_put_real_name);
        et_put_real_name.addTextChangedListener(new MyTextChangedListener());
        et_put_real_phone = (EditText) findViewById(R.id.et_put_real_phone);
        et_put_real_phone.addTextChangedListener(new MyTextChangedListener());
        et_put_verification_code = (EditText) findViewById(R.id.et_put_verification_code);
        et_put_verification_code.addTextChangedListener(new MyTextChangedListener());

        btn_get_verification_code = (Button) findViewById(R.id.btn_get_verification_code);
        btn_get_verification_code.setOnClickListener(this);

        btn_open_permission = (Button) findViewById(R.id.btn_open_permission);
        btn_open_permission.setOnClickListener(this);

        //注册微信支付
        regToWx();

        initData();
    }

    private String getRealName() {
        return et_put_real_name.getText().toString().trim();
    }

    private String getRealPhone() {
        return et_put_real_phone.getText().toString().trim();
    }

    private String getVerificationCode() {
        return et_put_verification_code.getText().toString().trim();
    }

    @Override
    public void onResume() {
        super.onResume();
        String recharge_success = (String) DataManager.getInstance().getData1();
        DataManager.getInstance().setData1(null);
        if (EmptyUtils.isNotEmpty(recharge_success) && recharge_success.equals("RECHARGE_SUCCESS")) {
            finish();
        }
    }

    private void initData() {
        //申请人图像和名字
        String string_head_img = SPUtils.getInstance().getString(Constants.HEADIMG);
        if (EmptyUtils.isNotEmpty(string_head_img)) {
            Glide.with(this)
                    .load(string_head_img)
                    .placeholder(R.drawable.avatar)
                    .error(R.drawable.avatar)
                    .into(iv_income_photo);
        }
        tv_income_name.setText(SPUtils.getInstance().getString(Constants.NICKNAME));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_close:
                goBack();
                break;
            case R.id.btn_get_verification_code:
                if (mTotalTime > 0) {
                    return;
                }
                final String mobile = getRealPhone();
                if (!RegexUtils.isMobileExact(mobile)) {
                    Toast.makeText(this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
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
            case R.id.btn_open_permission:
                openPermission();
                break;
            default:
                break;
        }
    }

    private void openPermission() {
        String mobile = getRealPhone();
        String realName = getRealName();
        String verificationCode = getVerificationCode();
        if (!RegexUtils.isMobileExact(mobile)) {
            Toast.makeText(this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
            return;
        }
        OkHttpUtils.post()
                .url(Constants.getOpenAgentUrl())
                .addParams(Constants.SESSION, SPUtils.getInstance().getString(Constants.SESSION))
                .addParams(Constants.SDKTYPE, String.valueOf(0))
                .addParams(Constants.PHONE, mobile)
                .addParams(Constants.REALNAME, realName)
                .addParams(Constants.VERIFLYCODE, verificationCode)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e(e.toString());
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
                                handlerWeChatPayData(jsonObjectResBody);
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

    private void handlerWeChatPayData(JSONObject jsonObjectResBody) {
        if (EmptyUtils.isNotEmpty(jsonObjectResBody)) {
            JSONObject jsonObject = jsonObjectResBody.optJSONObject("wxSdkParams");
            if (EmptyUtils.isNotEmpty(jsonObject)) {
                Gson gson = new Gson();
                WeChatPayParamsBean weChatPayParamsBean = gson.fromJson(jsonObject.toString(), WeChatPayParamsBean.class);
                if (EmptyUtils.isNotEmpty(weChatPayParamsBean)) {
                    PayReq request = new PayReq();
                    request.appId = weChatPayParamsBean.getAppid();
                    request.partnerId = weChatPayParamsBean.getPartnerid();
                    request.prepayId = weChatPayParamsBean.getPrepayid();
                    request.packageValue = weChatPayParamsBean.getPackageX();
                    request.nonceStr = weChatPayParamsBean.getNoncestr();
                    request.timeStamp = String.valueOf(weChatPayParamsBean.getTimestamp());
                    request.sign = weChatPayParamsBean.getSign();
                    //发起微信支付
                    api.sendReq(request);
                }
            }
        }
    }

    /**
     * 初始化时间任务器
     */
    private void initTimerTask() {
        mTask = new TimerTask() {
            @Override
            public void run() {
                if (view == null) {
                    mTimer.cancel();
                    return;
                }
                MyIncomeActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        --mTotalTime;
                        if (view == null) {
                            mTimer.cancel();
                            return;
                        }
                        btn_get_verification_code.setText(String.valueOf(mTotalTime));
                        btn_get_verification_code.setTextSize(16);
                        if (mTotalTime <= 0) {
                            mTimer.cancel();
                            btn_get_verification_code.setText(getString(R.string.free_get));//免费获取
                            btn_get_verification_code.setTextSize(14);
                            btn_get_verification_code.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_enable_selector));
                        } else {
                            btn_get_verification_code.setBackgroundDrawable(getResources().getDrawable(R.drawable.my_verification_code_shape));//倒计时时候的背景
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
            boolean realName = getRealName().length() > 0;
            boolean phone = getRealPhone().length() > 0;
            boolean verificationCode = getVerificationCode().length() > 0;
            if (phone) {
                btn_get_verification_code.setEnabled(true);
            } else {
                btn_get_verification_code.setEnabled(false);
            }
            if (realName && phone && verificationCode) {
                btn_open_permission.setEnabled(true);
            } else {
                btn_open_permission.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    private void regToWx() {
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, false);//false：表示checkSignature
        api.registerApp(Constants.APP_ID);
    }
}
