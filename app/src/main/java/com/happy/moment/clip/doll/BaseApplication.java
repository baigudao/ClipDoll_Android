package com.happy.moment.clip.doll;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.happy.moment.clip.doll.fragment.BaseFragment;
import com.happy.moment.clip.doll.util.Constants;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.ilivesdk.ILiveSDK;
import com.tencent.livesdk.ILVLiveConfig;
import com.tencent.livesdk.ILVLiveManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.game.UMGameAgent;
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
        MultiDex.install(this);
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

        //友盟统计
        UMGameAgent.setDebugMode(true);//设置输出运行时日志
        UMGameAgent.init(this);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_GAME);
        MobclickAgent.setSessionContinueMillis(10000);
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
                layout.setPrimaryColorsId(R.color.new_second_background_color, R.color.second_text_color);//全局设置主题颜色 背景色和字体颜色
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
