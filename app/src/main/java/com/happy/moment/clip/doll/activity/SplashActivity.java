package com.happy.moment.clip.doll.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.SPUtils;
import com.happy.moment.clip.doll.R;
import com.happy.moment.clip.doll.util.Constants;

import java.util.ArrayList;

/**
 * Created by Devin on 2017/11/18 17:39
 * E-mail:971060378@qq.com
 */

public class SplashActivity extends BaseActivity {

    private static final String[] APP_NEED_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,//定位
            Manifest.permission.RECORD_AUDIO, //录音
            Manifest.permission.CALL_PHONE,//打电话
            Manifest.permission.WRITE_EXTERNAL_STORAGE, //读写
            Manifest.permission.CAMERA};//照相

    private static final int EXTERNAL_STORAGE_REQ_CODE = 10;//权限请求码

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setStatusBarAlpha(SplashActivity.this);
        setContentView(R.layout.activity_splash);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //SDK版本大于等于23，也就是Android 6.0
            requestPermission();//请求权限 Xiaomi6.0.1
        } else {
            //SDK版本小于23的走这
            afterRequestPermission();//请求权限之后 Meizu5.1
        }
    }

    private void requestPermission() {
        ArrayList<String> unCheckPermissions = null;
        String[] appNeedPermission = APP_NEED_PERMISSIONS;
        for (String permission : appNeedPermission) {
            if (ContextCompat.checkSelfPermission(SplashActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
                if (unCheckPermissions == null) {
                    unCheckPermissions = new ArrayList<>();
                }
                unCheckPermissions.add(permission);
            }
        }
        if (unCheckPermissions != null && !unCheckPermissions.isEmpty()) {
            String[] array = new String[unCheckPermissions.size()];
            ActivityCompat.requestPermissions(this, unCheckPermissions.toArray(array), EXTERNAL_STORAGE_REQ_CODE);
        } else {
            afterRequestPermission();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case EXTERNAL_STORAGE_REQ_CODE: {
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            finish();
                            return;
                        }
                    }
                    afterRequestPermission();
                } else {
                    afterRequestPermission();
                }
            }
            default:
                break;
        }
    }

    private void afterRequestPermission() {
        //        String device_identifier = SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER);
        //        if (EmptyUtils.isEmpty(device_identifier)) {
        //            String device_id = CommonUtil.getDeviceId(this);//设备id
        //            String device_resolution = ScreenUtils.getScreenWidth() + "*" + ScreenUtils.getScreenHeight();//设备分辨率
        //            String device_sys_version = Constant.DEVICE_ANDROID + Build.VERSION.SDK_INT;//版本号
        //            String device_type = String.valueOf(Constant.DEVICE_TYPE_ANDROID);//设备类型Apple/Android
        //
        //            OkHttpUtils.post()
        //                    .url(Constant.getRegisterApp())
        //                    .addParams(Constant.DEVICE_ID, device_id)
        //                    .addParams(Constant.DEVICE_RESOLUTION, device_resolution)
        //                    .addParams(Constant.DEVICE_SYS_VERSION, device_sys_version)
        //                    .addParams(Constant.DEVICE_TYPE, device_type)
        //                    .build()
        //                    .execute(new StringCallback() {
        //                        @Override
        //                        public void onError(Call call, Exception e, int id) {
        //
        //                        }
        //
        //                        @Override
        //                        public void onResponse(String response, int id) {
        //                            JSONObject jsonObject = null;
        //                            try {
        //                                jsonObject = new JSONObject(response);
        //                                int status = jsonObject.optInt("status");
        //                                JSONObject jsonObjectData = jsonObject.optJSONObject("data");
        //                                if (status == 1) {
        //                                    //app注册，返回设备唯一标识，并保存
        //                                    String device_identifier = jsonObjectData.optString(Constant.DEVICE_IDENTIFIER);
        //                                    SPUtils.getInstance().put(Constant.DEVICE_IDENTIFIER, device_identifier);
        //                                } else {
        //                                    String code = jsonObject.optString("code");
        //                                    String msg = jsonObjectData.optString("msg");
        //                                    ToastUtils.showShort("请求数据失败,请检查网络:" + code + " - " + msg);
        //                                }
        //                            } catch (JSONException e) {
        //                                e.printStackTrace();
        //                            }
        //                        }
        //                    });
        //        }

        boolean isEnterGuideView = SPUtils.getInstance().getBoolean(Constants.IS_ENTER_GUIDE_VIEW);
        boolean is_user_login = SPUtils.getInstance().getBoolean(Constants.IS_USER_LOGIN);
        if (isEnterGuideView) {
            //如果进入了引导页面
            if (is_user_login) {
                //如果用户登录过，就直接进入主页面
                gotoPager(MainActivity.class, null);
            } else {
                //如果用户没有登录过，就进入登录注册页面
                gotoPager(LoginActivity.class, null);
            }
        } else {
            //如果没有进入引导页面，就进入引导页面
            gotoPager(GuideActivity.class, null);
        }
        finish();
    }
}
