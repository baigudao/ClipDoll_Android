package com.happy.moment.clip.doll.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.happy.moment.clip.doll.R;
import com.happy.moment.clip.doll.fragment.RegisterAndLoginFragment;
import com.happy.moment.clip.doll.fragment.UserProtocolFragment;
import com.happy.moment.clip.doll.util.Constants;
import com.rey.material.widget.CheckBox;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

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
        BarUtils.hideNavBar(LoginActivity.this);
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

        Button btn_register_login = (Button) findViewById(R.id.btn_register_login);
        btn_register_login.setVisibility(View.GONE);
        btn_register_login.setOnClickListener(this);//手机注册登录
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_weixin_login:
                wxLogin();
                break;
            case R.id.tv_user_protocol:
                gotoPager(UserProtocolFragment.class, null);
                break;
            case R.id.btn_register_login:
                gotoPager(RegisterAndLoginFragment.class, null);
                finish();
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
            req.state = "clip_doll_weixin_login";
            api.sendReq(req);
            finish();
        } else
            ToastUtils.showShort("用户未安装微信");
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            ll_weixin_login.setEnabled(true);
        } else {
            ll_weixin_login.setEnabled(false);
        }
    }
}
