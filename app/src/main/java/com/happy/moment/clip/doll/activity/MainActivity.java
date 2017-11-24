package com.happy.moment.clip.doll.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.happy.moment.clip.doll.R;
import com.happy.moment.clip.doll.adapter.BaseRecyclerViewAdapter;
import com.happy.moment.clip.doll.bean.BannerBean;
import com.happy.moment.clip.doll.bean.HomeRoomBean;
import com.happy.moment.clip.doll.fragment.NotificationCenterFragment;
import com.happy.moment.clip.doll.fragment.UserCenterFragment;
import com.happy.moment.clip.doll.util.Constants;
import com.happy.moment.clip.doll.util.DataManager;
import com.happy.moment.clip.doll.util.GlideImageLoader;
import com.happy.moment.clip.doll.view.SharePlatformPopupWindow;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.core.ILiveLoginManager;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;
import okhttp3.Call;

import static com.blankj.utilcode.util.SnackbarUtils.getView;

public class MainActivity extends BaseActivity implements View.OnClickListener, OnRefreshListener, OnLoadmoreListener, BaseRecyclerViewAdapter.OnItemClickListener {

    private String share_img;
    private String share_title;
    private String share_txt;
    private String share_url;

    private Banner banner;

    private static final int HOME_ROOM_LIST_DATA_TYPE = 1;

    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView recyclerView;
    private int refresh_or_load;//0或1
    private boolean bLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setStatusBarColor(MainActivity.this, getResources().getColor(R.color.main_color));
        setContentView(R.layout.activity_main);

        initView();
        initData();
    }

    private void initView() {
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

        bLogin = false;

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);

        banner = (Banner) findViewById(R.id.banner);
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_user_photo:
                gotoPager(UserCenterFragment.class, null);
                break;
            case R.id.iv_exchange:
            case R.id.tv_exchange:
                getHomeRoomListData();
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

    private void initData() {
        getHomeBannerData();
        getHomeRoomListData();
        if (!bLogin) {
            loginTXLive();
        }
    }

    private void getHomeBannerData() {
        OkHttpUtils.get()
                .url(Constants.getHomeBannerUrl())
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
                            String req = jsonObjectResHead.optString("req");
                            JSONObject jsonObjectResBody = jsonObject.optJSONObject("resBody");
                            if (code == 1) {
                                handlerBannerData(jsonObjectResBody);
                                smartRefreshLayout.finishRefresh();
                                smartRefreshLayout.finishLoadmore();
                            } else {
                                LogUtils.e("请求数据失败：" + msg + "-" + code + "-" + req);
                                ToastUtils.showShort("请求数据失败,请检查网络并重试！");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void getHomeRoomListData() {
        OkHttpUtils.get()
                .url(Constants.getHomeRoomListUrl())
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
                            String req = jsonObjectResHead.optString("req");
                            JSONObject jsonObjectResBody = jsonObject.optJSONObject("resBody");
                            if (code == 1) {
                                handlerHomeRoomListData(jsonObjectResBody);
                                smartRefreshLayout.finishRefresh();
                                smartRefreshLayout.finishLoadmore();
                            } else {
                                LogUtils.e("请求数据失败：" + msg + "-" + code + "-" + req);
                                ToastUtils.showShort("请求数据失败,请检查网络并重试！");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void handlerHomeRoomListData(JSONObject jsonObjectResBody) {
        if (EmptyUtils.isNotEmpty(jsonObjectResBody)) {
            JSONArray homeRoomList = jsonObjectResBody.optJSONArray("liveRoomList");
            if (EmptyUtils.isNotEmpty(homeRoomList)) {
                Gson gson = new Gson();
                ArrayList<HomeRoomBean> homeRoomBeanArrayList = gson.fromJson(homeRoomList.toString(), new TypeToken<ArrayList<HomeRoomBean>>() {
                }.getType());
                if (EmptyUtils.isNotEmpty(homeRoomBeanArrayList) && homeRoomBeanArrayList.size() != 0) {
                    BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(MainActivity.this, homeRoomBeanArrayList, HOME_ROOM_LIST_DATA_TYPE);
                    recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2, LinearLayoutManager.VERTICAL, false));
                    recyclerView.setAdapter(baseRecyclerViewAdapter);
                    baseRecyclerViewAdapter.setOnItemClickListener(this);
                }
            }
        }
    }

    private void handlerBannerData(JSONObject jsonObjectResBody) {
        if (EmptyUtils.isNotEmpty(jsonObjectResBody)) {
            JSONArray bannerList = jsonObjectResBody.optJSONArray("bannerList");
            if (EmptyUtils.isNotEmpty(bannerList)) {
                Gson gson = new Gson();
                ArrayList<BannerBean> bannerArrayList = gson.fromJson(bannerList.toString(), new TypeToken<ArrayList<BannerBean>>() {
                }.getType());

                if (EmptyUtils.isNotEmpty(bannerArrayList) && bannerArrayList.size() != 0) {
                    ArrayList<String> images = new ArrayList<>();
                    for (int i = 0; i < bannerArrayList.size(); i++) {
                        images.add(bannerArrayList.get(i).getPictures());
                    }
                    //设置图片集合
                    banner.setImages(images);
                    //banner设置方法全部调用完毕时最后调用
                    banner.start();
                }
            }
        }
    }

    @Override
    public void onItemClick(Object data, int position) {
        if (data.getClass().getSimpleName().equals("HomeRoomBean")) {
            HomeRoomBean homeRoomBean = (HomeRoomBean) data;
            if (EmptyUtils.isNotEmpty(homeRoomBean)) {
                DataManager.getInstance().setData1(homeRoomBean);
                gotoPager(ClipDollDetailActivity.class, null);
            }
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
        initData();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        initData();
    }

    private void loginTXLive() {
        String sig = SPUtils.getInstance().getString(Constants.TLSSIGN);
        int user_id = SPUtils.getInstance().getInt(Constants.USERID);
        if (EmptyUtils.isNotEmpty(sig) && EmptyUtils.isNotEmpty(user_id)) {
            ILiveLoginManager.getInstance().iLiveLogin(String.valueOf(user_id), sig, new ILiveCallBack() {
                @Override
                public void onSuccess(Object data) {
                    bLogin = true;
                    LogUtils.e("登录成功");
                }

                @Override
                public void onError(String module, int errCode, String errMsg) {
                    LogUtils.e(module + errMsg);
                }
            });
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
}
