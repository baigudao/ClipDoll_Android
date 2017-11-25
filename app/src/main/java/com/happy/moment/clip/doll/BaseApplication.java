package com.happy.moment.clip.doll;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.happy.moment.clip.doll.fragment.BaseFragment;
import com.happy.moment.clip.doll.util.Constants;
import com.mob.MobSDK;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.ilivesdk.ILiveSDK;
import com.tencent.livesdk.ILVLiveConfig;
import com.tencent.livesdk.ILVLiveManager;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class BaseApplication extends Application {

    private static Context appContext;
    private static BaseFragment curFragment;

    public static Context getAppContext() {
        return appContext;
    }

    public static void setAppContext(Context appContext) {
        BaseApplication.appContext = appContext;
    }

    public static void setCurFragment(BaseFragment fragment) {
        curFragment = fragment;
    }

    public static BaseFragment getCurFragment() {
        return curFragment;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        BaseApplication.setAppContext(getApplicationContext());

        //AndroidUtilCode库的初始化
        Utils.init(this);

        //配置OKHttp
        configureOkHttp();

        //动态设置内存缓存size
        Glide.get(this).setMemoryCategory(MemoryCategory.HIGH);

        //iLiveSDK初始化
        ILiveSDK.getInstance().initSdk(getApplicationContext(), Constants.LIVE_APPID, Constants.ACCOUNT_TYPE);
        //初始化直播场景
        ILVLiveConfig liveConfig = new ILVLiveConfig();
        ILVLiveManager.getInstance().init(liveConfig);

        //初始社会化组件
        MobSDK.init(this, "224bdd661dffa", "5ec73fe78abc2d0c5ab2ae9da6567745");

        //信鸽推送
        XGPushConfig.enableDebug(this, true);
        XGPushManager.registerPush(this, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object data, int flag) {
                //token在设备卸载重装的时候有可能会变
                Log.e("TPush", "注册成功，设备token为：" + data);
            }

            @Override
            public void onFail(Object data, int errCode, String msg) {
                Log.e("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
            }
        });
        XGPushManager.registerPush(getApplicationContext(), "XINGE");
        XGPushManager.setTag(this, "XINGE");
    }

    private void configureOkHttp() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }

    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setHeaderHeight(60);
                layout.setPrimaryColorsId(R.color.background_color, R.color.main_color);//全局设置主题颜色 背景色和字体颜色
                return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                layout.setFooterHeight(60);
                return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });
    }
}
