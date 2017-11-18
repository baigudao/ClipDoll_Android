package com.happy.moment.clip.doll.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.happy.moment.clip.doll.R;
import com.rey.material.widget.CheckBox;

/**
 * Created by Devin on 2017/11/13 17:59
 * E-mail:971060378@qq.com
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private LinearLayout ll_weixin_login;

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_weixin_login:
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                break;
            case R.id.tv_user_protocol:
                ToastUtils.showShort("协议");
                break;
            default:
                break;
        }
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
}
