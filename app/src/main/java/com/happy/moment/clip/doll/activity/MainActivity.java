package com.happy.moment.clip.doll.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.happy.moment.clip.doll.R;
import com.happy.moment.clip.doll.fragment.NotificationCenterFragment;
import com.happy.moment.clip.doll.fragment.UserCenterFragment;
import com.happy.moment.clip.doll.util.GlideImageLoader;
import com.happy.moment.clip.doll.view.SharePlatformPopupWindow;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;

import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

import static com.blankj.utilcode.util.SnackbarUtils.getView;

public class MainActivity extends BaseActivity implements View.OnClickListener, OnRefreshListener, OnLoadmoreListener {

    private String share_img;
    private String share_title;
    private String share_txt;
    private String share_url;

    private Banner banner;

    private SmartRefreshLayout smartRefreshLayout;
    private int refresh_or_load;//0或1

    private ArrayList<String> images = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setStatusBarColor(MainActivity.this, getResources().getColor(R.color.main_color));
        setContentView(R.layout.activity_main);

        findViewById(R.id.ll_close).setVisibility(View.GONE);
        ImageView iv_user_photo = (ImageView) findViewById(R.id.iv_user_photo);
        iv_user_photo.setVisibility(View.VISIBLE);
        iv_user_photo.setOnClickListener(this);
        ImageView iv_share = (ImageView) findViewById(R.id.iv_share);
        iv_share.setImageResource(R.drawable.news);
        iv_share.setOnClickListener(this);

        findViewById(R.id.tv_exchange).setOnClickListener(this);
        findViewById(R.id.iv_exchange).setOnClickListener(this);

        smartRefreshLayout = (SmartRefreshLayout) findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setEnableRefresh(true);
        smartRefreshLayout.setEnableLoadmore(true);
        smartRefreshLayout.setOnRefreshListener(this);
        smartRefreshLayout.setOnLoadmoreListener(this);
        refresh_or_load = 0;

        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510393528928&di=270563025bcd05daecd6185670305488&imgtype=0&src=http%3A%2F%2Fimg1.3lian.com%2F2015%2Fa1%2F84%2Fd%2F95.jpg");
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510393529139&di=6001f9a8d8b29abe83502a23f242eca1&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F29381f30e924b899deb0d7ea64061d950b7bf650.jpg");
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510393529133&di=b41f298f9623a34d1ebc2f1d790fc649&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dshijue1%252C0%252C0%252C294%252C40%2Fsign%3Dc55331232c9759ee5e5d6888da922963%2F3c6d55fbb2fb4316a08b2f542aa4462309f7d30c.jpg");

        banner = (Banner) findViewById(R.id.banner);
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(images);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //设置banner的点击事件
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                ToastUtils.showShort("你点击了第" + position + "个banner");
            }
        });
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_user_photo:
                gotoPager(UserCenterFragment.class, null);
                break;
            case R.id.iv_exchange:
            case R.id.tv_exchange:
                startActivity(new Intent(MainActivity.this, ClipDollDetailActivity.class));

                //分享
                //            showSharePlatformPopWindow();
                break;
            case R.id.iv_share:
                gotoPager(NotificationCenterFragment.class, null);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //开始轮播
        banner.startAutoPlay();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //结束轮播
        banner.stopAutoPlay();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private long startTime = 0;

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - startTime) > 2000) {
            Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            startTime = currentTime;
        } else {
            finish();
        }
    }

    private void showSharePlatformPopWindow() {
        SharePlatformPopupWindow sharePlatformPopWindow = new SharePlatformPopupWindow(MainActivity.this, new SharePlatformPopupWindow.SharePlatformListener() {
            @Override
            public void onSinaWeiboClicked() {
                showShare(MainActivity.this, "SinaWeibo", true);
            }

            @Override
            public void onWeChatClicked() {
                showShare(MainActivity.this, "Wechat", true);
            }

            @Override
            public void onWechatMomentsClicked() {
                showShare(MainActivity.this, "WechatMoments", true);
            }

            @Override
            public void onCancelBtnClicked() {

            }
        });
        sharePlatformPopWindow.initView();
        sharePlatformPopWindow.showAtLocation(getView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 演示调用ShareSDK执行分享
     *
     * @param context
     * @param platformToShare 指定直接分享平台名称（一旦设置了平台名称，则九宫格将不会显示）
     * @param showContentEdit 是否显示编辑页
     */
    private void showShare(Context context, String platformToShare, boolean showContentEdit) {

        OnekeyShare oks = new OnekeyShare();
        oks.setSilent(!showContentEdit);
        if (platformToShare != null) {
            oks.setPlatform(platformToShare);
        }
        //ShareSDK快捷分享提供两个界面第一个是九宫格 CLASSIC  第二个是SKYBLUE
        oks.setTheme(OnekeyShareTheme.CLASSIC);
        // 令编辑页面显示为Dialog模式
        //        oks.setDialogMode();
        // 在自动授权时可以禁用SSO方式
        oks.disableSSOWhenAuthorize();

        oks.setTitle(share_title);
        if (platformToShare.equalsIgnoreCase("SinaWeibo")) {
            oks.setText(share_txt + "\n" + share_url);
        } else {
            oks.setText(share_img);
            oks.setImageUrl(share_img);
            oks.setUrl(share_url);
        }

        // 启动分享
        oks.show(context);
    }
}
