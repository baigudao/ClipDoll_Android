package com.happy.moment.clip.doll.fragment;

import android.app.AlertDialog;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.happy.moment.clip.doll.R;
import com.happy.moment.clip.doll.activity.MyIncomeActivity;
import com.happy.moment.clip.doll.adapter.BaseRecyclerViewAdapter;
import com.happy.moment.clip.doll.bean.AllCommonParamBean;
import com.happy.moment.clip.doll.bean.BannerBean;
import com.happy.moment.clip.doll.bean.TagsBean;
import com.happy.moment.clip.doll.bean.TaskBean;
import com.happy.moment.clip.doll.bean.UserSignProductBean;
import com.happy.moment.clip.doll.util.Constants;
import com.happy.moment.clip.doll.util.DataManager;
import com.happy.moment.clip.doll.util.GlideImageLoader;
import com.happy.moment.clip.doll.view.UserSignPopupWindow;
import com.happy.moment.clip.doll.view.UserSignSuccessPopupWindow;
import com.happy.moment.clip.doll.view.UserTaskPopupWindow;
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
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;

/**
 * Created by Devin on 2018/2/2 16:35
 * E-mail:971060378@qq.com
 */

public class HomeFragment extends BaseFragment implements OnRefreshListener, OnLoadmoreListener {

    private Banner banner;

    private ArrayList<BaseFragment> mBaseFragment;
    private int position;
    private Fragment fromFragment;

    private static final int TASK_DATA_TYPE = 12;
    private static final int TAGS_DATA_TYPE = 13;

    private View view_red_point;

    private SmartRefreshLayout smartRefreshLayout;
    private boolean bLogin;

    private ArrayList<BannerBean> bannerArrayList;
    private ArrayList<UserSignProductBean> userSignProductArrayList;

    private ImageView iv_title_background;
    private ImageView iv_sign_in;

    private boolean isChangeLamp;
    private Timer titleBarTimer;
    private TimerTask titleBarTimerTask;

    private RecyclerView recyclerView_tags;

    private int tagSize;

    private ArrayList<TagsBean> tagsBeanArrayList;

    private JSONObject jsonObjectResBody;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.ll_close).setVisibility(View.GONE);
        ImageView iv_user_photo = (ImageView) view.findViewById(R.id.iv_user_photo);
        iv_user_photo.setVisibility(View.GONE);
        iv_user_photo.setOnClickListener(this);
        view.findViewById(R.id.rl_notification).setOnClickListener(this);
        view_red_point = view.findViewById(R.id.view_red_point);

        iv_title_background = (ImageView) view.findViewById(R.id.iv_title_background);

        iv_sign_in = (ImageView) view.findViewById(R.id.iv_sign_in);
        iv_sign_in.setOnClickListener(this);
        view.findViewById(R.id.iv_dazhuanpan).setOnClickListener(this);

        view.findViewById(R.id.iv_meirirenwu).setOnClickListener(this);
        view.findViewById(R.id.iv_dailizhuanqian).setOnClickListener(this);
        view.findViewById(R.id.iv_meizhoubangdan).setOnClickListener(this);

        recyclerView_tags = (RecyclerView) view.findViewById(R.id.recyclerView_tags);

        smartRefreshLayout = (SmartRefreshLayout) view.findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);
        smartRefreshLayout.setEnableRefresh(true);
        smartRefreshLayout.setEnableLoadmore(true);
        smartRefreshLayout.setOnRefreshListener(this);
        smartRefreshLayout.setOnLoadmoreListener(this);

        bLogin = false;
        isChangeLamp = false;

        banner = (Banner) view.findViewById(R.id.banner);
        //动态的设置banner的长宽比
        int width = ScreenUtils.getScreenWidth() - SizeUtils.dp2px(30);
        ViewGroup.LayoutParams layoutParams = banner.getLayoutParams();
        layoutParams.height = (width * 3) / 7;
        banner.setLayoutParams(layoutParams);
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(4000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //设置banner的点击事件
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if (bannerArrayList.size() != 0) {
                    BannerBean bannerBean = bannerArrayList.get(position);
                    if (EmptyUtils.isNotEmpty(bannerBean)) {
                        String url = bannerBean.getUrl();
                        if (EmptyUtils.isNotEmpty(url)) {
                            DataManager.getInstance().setData1(url);
                            gotoPager(BannerFragment.class, null);
                        }
                    }
                }
            }
        });
    }

    private void initFragment() {
        mBaseFragment = new ArrayList<>();
        for (int i = 0; i < tagSize; i++) {
            mBaseFragment.add(new ContainerFragment());
        }
        setListener();
    }

    private void setListener() {
        //默认
        position = 0;
        DataManager.getInstance().setData1(EmptyUtils.isNotEmpty(tagsBeanArrayList) ? tagsBeanArrayList.get(0).getTagName() : "");
        switchFragment(fromFragment, getFragment());
    }

    private void switchFragment(Fragment from, Fragment to) {
        if (from != to) {
            fromFragment = to;
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            //才切换
            //判断有没有被添加
            if (!to.isAdded()) {
                //to没有被添加
                //from隐藏
                if (from != null) {
                    ft.hide(from);
                }
                //添加to
                ft.add(R.id.rl_container_home, to).commit();
            } else {
                //to已经被添加
                // from隐藏
                if (from != null) {
                    ft.hide(from);
                }
                //显示to
                ft.show(to).commit();
            }
        }
    }

    private BaseFragment getFragment() {
        return mBaseFragment.get(position);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_user_photo:
                gotoPager(UserCenterFragment.class, null);
                break;
            case R.id.rl_notification:
                gotoPager(NotificationCenterFragment.class, null);
                break;
            case R.id.iv_sign_in:
                //每日签到
                showUserSignPopupWindow();
                break;
            case R.id.iv_dazhuanpan:
                gotoPager(TurnTableFragment.class, null);//大转盘
                break;
            case R.id.iv_meizhoubangdan:
                gotoPager(RankingListFragment.class, null);//榜单
                break;
            case R.id.iv_meirirenwu:
                //每日任务
                showUserTaskView();
                break;
            case R.id.iv_dailizhuanqian:
                int role = SPUtils.getInstance().getInt(Constants.ROLE);
                switch (role) {//0.普通用户,1.分销商
                    case 0:
                        gotoPager(MyIncomeActivity.class, null);
                        break;
                    case 1:
                        gotoPager(MyIncomeDetailFragment.class, null);
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void initData() {
        super.initData();
        getHomeBannerData();
        getHomeRoomListData();
        getLiveTagData();
        getUserSignProductListData();
        if (!bLogin) {
            loginTXLive();
        }
    }

    /**
     * 首页标签
     */
    private void getLiveTagData() {
        OkHttpUtils.get()
                .url(Constants.getLiveTagUrl())
                .addParams(Constants.SESSION, SPUtils.getInstance().getString(Constants.SESSION))
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
                                JSONArray jsonArrayTags = jsonObjectResBody.optJSONArray("tags");
                                if (EmptyUtils.isNotEmpty(jsonArrayTags) && jsonArrayTags.length() > 0) {
                                    Gson gson = new Gson();
                                    tagsBeanArrayList = gson.fromJson(jsonArrayTags.toString(), new TypeToken<ArrayList<TagsBean>>() {
                                    }.getType());
                                    if (EmptyUtils.isNotEmpty(tagsBeanArrayList) && tagsBeanArrayList.size() > 0) {
                                        tagSize = tagsBeanArrayList.size();
                                        //初始化标签区
                                        initFragment();

                                        BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, tagsBeanArrayList, TAGS_DATA_TYPE);
                                        recyclerView_tags.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                                        recyclerView_tags.setAdapter(baseRecyclerViewAdapter);

                                        baseRecyclerViewAdapter.setOnItemViewRBClickListener(new BaseRecyclerViewAdapter.OnItemViewRBClickListener() {
                                            @Override
                                            public void onItemViewRBClick(Object data, int mPosition, final RadioButton radioButton) {
                                                if (data.getClass().getSimpleName().equals("TagsBean")) {
                                                    TagsBean tagsBean = (TagsBean) data;
                                                    if (EmptyUtils.isNotEmpty(tagsBean)) {
                                                        position = mPosition;
                                                        DataManager.getInstance().setData1(tagsBean.getTagName());
                                                        switchFragment(fromFragment, getFragment());

                                                        for (int i = 0; i < tagSize; i++) {
                                                            TagsBean tags = tagsBeanArrayList.get(i);
                                                            View view = recyclerView_tags.getChildAt(i);
                                                            final RadioButton rb = (RadioButton) view.findViewById(R.id.rb_home);

                                                            Glide.with(mContext)
                                                                    .load(tags.getIcon())
                                                                    .placeholder(R.drawable.income_avatar)
                                                                    .error(R.drawable.income_avatar)
                                                                    .into(new SimpleTarget<GlideDrawable>() {
                                                                        @Override
                                                                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                                                                rb.setBackground(resource);
                                                                            }
                                                                        }
                                                                    });
                                                        }

                                                        Glide.with(mContext)
                                                                .load(tagsBean.getIconCk())
                                                                .placeholder(R.drawable.income_avatar)
                                                                .error(R.drawable.income_avatar)
                                                                .into(new SimpleTarget<GlideDrawable>() {
                                                                    @Override
                                                                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                                                            radioButton.setBackground(resource);
                                                                        }
                                                                    }
                                                                });

                                                    }
                                                }
                                            }
                                        });
                                    }
                                } else {
                                    //初始化标签区
                                    tagSize = 1;
                                    initFragment();
                                }
                            } else {
                                LogUtils.e("请求数据失败：" + msg + "-" + code + "-" + req);
                                ToastUtils.showShort("请求数据失败:" + msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 首页房间列表所有数据
     */
    private void getHomeRoomListData() {
        OkHttpUtils.get()
                .url(Constants.getHomeRoomListUrl())
                .addParams(Constants.SESSION, SPUtils.getInstance().getString(Constants.SESSION))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e(e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //                        LogUtils.e(response);
                    }
                });
    }

    private void getUserSignProductListData() {
        //获取签到奖品列表
        OkHttpUtils.get()
                .url(Constants.getUserSignProductListUrl())
                .addParams(Constants.SESSION, SPUtils.getInstance().getString(Constants.SESSION))
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
                            JSONArray jsonObjectResBody = jsonObject.optJSONArray("resBody");
                            if (code == 1) {
                                if (EmptyUtils.isNotEmpty(jsonObjectResBody)) {
                                    Gson gson = new Gson();
                                    ArrayList<UserSignProductBean> userSignProductBeanArrayList = gson.fromJson(jsonObjectResBody.toString(), new TypeToken<ArrayList<UserSignProductBean>>() {
                                    }.getType());
                                    if (EmptyUtils.isNotEmpty(userSignProductBeanArrayList) && userSignProductBeanArrayList.size() > 0) {
                                        userSignProductArrayList = userSignProductBeanArrayList;
                                    }
                                }
                            } else {
                                LogUtils.e("请求数据失败：" + msg + "-" + code + "-" + req);
                                ToastUtils.showShort("请求数据失败:" + msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        //获取所有公共配置
        OkHttpUtils.get()
                .url(Constants.getAllCommonParam())
                .addParams(Constants.SESSION, SPUtils.getInstance().getString(Constants.SESSION))
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
                                int success = jsonObjectResBody.optInt("success");
                                if (success == 1) {
                                    JSONObject jsonObjectResData = jsonObjectResBody.optJSONObject("resData");
                                    if (EmptyUtils.isNotEmpty(jsonObjectResData)) {
                                        AllCommonParamBean allCommonParamBean = new Gson().fromJson(jsonObjectResData.toString(), AllCommonParamBean.class);
                                        if (EmptyUtils.isNotEmpty(allCommonParamBean)) {
                                            //处理数据
                                            SPUtils.getInstance().put("AGENTER_APPLY_TEXT", allCommonParamBean.getAgenterApplyText());//开启分销的价格文案
                                            SPUtils.getInstance().put("AUTO_EXCHANGE_TIME", allCommonParamBean.getAutoExchangeTime());//系统自动兑换娃娃币的时间
                                            SPUtils.getInstance().put("CONTACT_WAY", allCommonParamBean.getContactway());//联系客服微信
                                            SPUtils.getInstance().put("EXCHANGE_AWARDS", allCommonParamBean.getExchangeAwards());//兑换奖励
                                            SPUtils.getInstance().put("EXPRESS_FEE", allCommonParamBean.getExpressFee());//快递费
                                            SPUtils.getInstance().put("EXPRESS_FEE_PARAM", allCommonParamBean.getExpressFeeParam());//无用
                                            SPUtils.getInstance().put("HOW_MACH_FREE", allCommonParamBean.getHowmachFree());//几件包邮
                                            SPUtils.getInstance().put("INVITING_AWARDS", allCommonParamBean.getInvitingAwards());//邀请奖励
                                            SPUtils.getInstance().put("MAX_REWARD", allCommonParamBean.getMaxReward());//最多兑换币额
                                            SPUtils.getInstance().put("NEW_REWARD", allCommonParamBean.getNewReward());//新人奖励

                                            //是否首次登录
                                            isFirstLogin();
                                        }
                                    }
                                }
                            } else {
                                LogUtils.e("请求数据失败：" + msg + "-" + code + "-" + req);
                                ToastUtils.showShort("请求数据失败:" + msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        //控制首页通知图标上的红点的显示隐藏
        OkHttpUtils.post()
                .url(Constants.getNotifyCountUrl())
                .addParams(Constants.SESSION, SPUtils.getInstance().getString(Constants.SESSION))
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
                                int notifyNum = jsonObjectResBody.optInt("notifyNum");
                                if (notifyNum > 0) {
                                    view_red_point.setVisibility(View.VISIBLE);
                                } else {
                                    view_red_point.setVisibility(View.GONE);
                                }
                            } else {
                                LogUtils.e("请求数据失败：" + msg + "-" + code + "-" + req);
                                ToastUtils.showShort("请求数据失败:" + msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        //titleBar上的跑马灯
        titleBarTimer = new Timer();
        titleBarTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (getActivity().isFinishing()) {
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isChangeLamp) {
                            iv_title_background.setImageResource(R.drawable.home_nav_lamps);
                            isChangeLamp = false;
                        } else {
                            iv_title_background.setImageResource(R.drawable.home_nav_lamp);
                            isChangeLamp = true;
                        }
                    }
                });
            }
        };
        titleBarTimer.schedule(titleBarTimerTask, 0, 600);

        //获取当前用户签到信息
        getUserSignInfoData();
    }

    private void isFirstLogin() {
        //是否首次登录
        int firstLogin = SPUtils.getInstance().getInt(Constants.FIRSTLOGIN);
        //0.是(跳注册奖励弹窗) 1.否
        if (firstLogin == 0) {
            SPUtils.getInstance().put(Constants.FIRSTLOGIN, 1);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialog_Logout);
            View view_ = View.inflate(getActivity(), R.layout.new_user_login_dialog, null);
            ((TextView) view_.findViewById(R.id.tv_num)).setText(SPUtils.getInstance().getString("NEW_REWARD"));
            ((TextView) view_.findViewById(R.id.tv_center_num)).setText(SPUtils.getInstance().getString("NEW_REWARD"));
            builder.setView(view_);
            final AlertDialog alertDialog = builder.create();
            alertDialog.show();
            //设置对话框的大小
            alertDialog.getWindow().setLayout(SizeUtils.dp2px(340), LinearLayout.LayoutParams.WRAP_CONTENT);
            //监听事件
            view_.findViewById(R.id.iv_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            view_.findViewById(R.id.btn_go_to_invite).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    gotoPager(InvitePrizeFragment.class, null);
                }
            });
            view_.findViewById(R.id.btn_start_game).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
        }
    }

    private void getUserSignInfoData() {
        OkHttpUtils.get()
                .url(Constants.getUserSignInfoUrl())
                .addParams(Constants.SESSION, SPUtils.getInstance().getString(Constants.SESSION))
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
                            jsonObjectResBody = jsonObject.optJSONObject("resBody");
                            if (code == 1) {
                                if (EmptyUtils.isNotEmpty(jsonObjectResBody)) {
                                    int signCount = jsonObjectResBody.optInt("signCount");//当前用户连续签到次数 (最高记录为7,顺序为1,2,3,4,5,6,7)
                                    int signHistory = jsonObjectResBody.optInt("signHistory");//用户累计签到次数   总签到数
                                    int todaySign = jsonObjectResBody.optInt("todaySign");//当天是否签到(1:是,2:否)
                                    switch (todaySign) {
                                        case 1:
                                            iv_sign_in.setVisibility(View.GONE);
                                            break;
                                        case 2:
                                            iv_sign_in.setVisibility(View.VISIBLE);
                                            break;
                                        default:
                                            iv_sign_in.setVisibility(View.VISIBLE);
                                            break;
                                    }
                                    LogUtils.e("signCount==" + signCount);
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

    /**
     * 每日签到
     */
    private void showUserSignPopupWindow() {
        if (EmptyUtils.isEmpty(jsonObjectResBody)) {
            return;
        }
        int signCount = jsonObjectResBody.optInt("signCount");//当前用户连续签到次数 (最高记录为7,顺序为1,2,3,4,5,6,7)
        UserSignPopupWindow userSignPopupWindow = new UserSignPopupWindow(getActivity(), new UserSignPopupWindow.UserSignPopupWindowListener() {
            @Override
            public void onCancelClicked() {

            }

            @Override
            public void onSignClicked() {
                OkHttpUtils.post()
                        .url(Constants.getUserSignUrl())
                        .addParams(Constants.SESSION, SPUtils.getInstance().getString(Constants.SESSION))
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
                                    if (code == 1) {//15000
                                        ShowSignInSuccessPopupWindow();
                                        iv_sign_in.setVisibility(View.GONE);
                                    } else {
                                        LogUtils.e("请求数据失败：" + msg + "-" + code + "-" + req);
                                        ToastUtils.showShort("" + msg);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }
        });
        userSignPopupWindow.initView();
        userSignPopupWindow.showAtLocation(getView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

        TextView tv_han_sign_day_num = (TextView) userSignPopupWindow.getContentView().findViewById(R.id.tv_han_sign_day_num);
        tv_han_sign_day_num.setText(String.valueOf(signCount));
        ImageView iv_sign_in_1 = (ImageView) userSignPopupWindow.getContentView().findViewById(R.id.iv_sign_in_1);
        TextView tv_coin_1 = (TextView) userSignPopupWindow.getContentView().findViewById(R.id.tv_coin_1);
        ImageView iv_sign_in_2 = (ImageView) userSignPopupWindow.getContentView().findViewById(R.id.iv_sign_in_2);
        TextView tv_coin_2 = (TextView) userSignPopupWindow.getContentView().findViewById(R.id.tv_coin_2);
        ImageView iv_sign_in_3 = (ImageView) userSignPopupWindow.getContentView().findViewById(R.id.iv_sign_in_3);
        TextView tv_coin_3 = (TextView) userSignPopupWindow.getContentView().findViewById(R.id.tv_coin_3);
        ImageView iv_sign_in_4 = (ImageView) userSignPopupWindow.getContentView().findViewById(R.id.iv_sign_in_4);
        TextView tv_coin_4 = (TextView) userSignPopupWindow.getContentView().findViewById(R.id.tv_coin_4);
        ImageView iv_sign_in_5 = (ImageView) userSignPopupWindow.getContentView().findViewById(R.id.iv_sign_in_5);
        TextView tv_coin_5 = (TextView) userSignPopupWindow.getContentView().findViewById(R.id.tv_coin_5);
        ImageView iv_sign_in_6 = (ImageView) userSignPopupWindow.getContentView().findViewById(R.id.iv_sign_in_6);
        TextView tv_coin_6 = (TextView) userSignPopupWindow.getContentView().findViewById(R.id.tv_coin_6);
        ImageView iv_sign_in_7 = (ImageView) userSignPopupWindow.getContentView().findViewById(R.id.iv_sign_in_7);
        TextView tv_coin_7 = (TextView) userSignPopupWindow.getContentView().findViewById(R.id.tv_coin_7);
        if (EmptyUtils.isNotEmpty(userSignProductArrayList) && userSignProductArrayList.size() > 0) {
            //奖品类型(1: 娃娃币,2:商品)
            //第1天
            UserSignProductBean userSignProductBean1 = userSignProductArrayList.get(0);
            if (EmptyUtils.isNotEmpty(userSignProductBean1)) {
                int signType = userSignProductBean1.getSignType();
                switch (signType) {
                    case 1:
                        iv_sign_in_1.setImageResource(R.drawable.sign_weikaiqi);
                        tv_coin_1.setText(userSignProductBean1.getRemark());
                        break;
                    case 2:
                        iv_sign_in_1.setImageResource(R.drawable.sign_dalibao);
                        break;
                }
            }
            //第2天
            UserSignProductBean userSignProductBean2 = userSignProductArrayList.get(1);
            if (EmptyUtils.isNotEmpty(userSignProductBean2)) {
                int signType = userSignProductBean2.getSignType();
                switch (signType) {
                    case 1:
                        iv_sign_in_2.setImageResource(R.drawable.sign_weikaiqi);
                        tv_coin_2.setText(userSignProductBean2.getRemark());
                        break;
                    case 2:
                        iv_sign_in_2.setImageResource(R.drawable.sign_dalibao);
                        break;
                }
            }
            //第3天
            UserSignProductBean userSignProductBean3 = userSignProductArrayList.get(2);
            if (EmptyUtils.isNotEmpty(userSignProductBean3)) {
                int signType = userSignProductBean3.getSignType();
                switch (signType) {
                    case 1:
                        iv_sign_in_3.setImageResource(R.drawable.sign_weikaiqi);
                        tv_coin_3.setText(userSignProductBean3.getRemark());
                        break;
                    case 2:
                        iv_sign_in_3.setImageResource(R.drawable.sign_dalibao);
                        break;
                }
            }
            //第4天
            UserSignProductBean userSignProductBean4 = userSignProductArrayList.get(3);
            if (EmptyUtils.isNotEmpty(userSignProductBean4)) {
                int signType = userSignProductBean4.getSignType();
                switch (signType) {
                    case 1:
                        iv_sign_in_4.setImageResource(R.drawable.sign_weikaiqi);
                        tv_coin_4.setText(userSignProductBean4.getRemark());
                        break;
                    case 2:
                        iv_sign_in_4.setImageResource(R.drawable.sign_dalibao);
                        break;
                }
            }
            //第5天
            UserSignProductBean userSignProductBean5 = userSignProductArrayList.get(4);
            if (EmptyUtils.isNotEmpty(userSignProductBean5)) {
                int signType = userSignProductBean5.getSignType();
                switch (signType) {
                    case 1:
                        iv_sign_in_5.setImageResource(R.drawable.sign_weikaiqi);
                        tv_coin_5.setText(userSignProductBean5.getRemark());
                        break;
                    case 2:
                        iv_sign_in_5.setImageResource(R.drawable.sign_dalibao);
                        break;
                }
            }
            //第6天
            UserSignProductBean userSignProductBean6 = userSignProductArrayList.get(5);
            if (EmptyUtils.isNotEmpty(userSignProductBean6)) {
                int signType = userSignProductBean6.getSignType();
                switch (signType) {
                    case 1:
                        iv_sign_in_6.setImageResource(R.drawable.sign_weikaiqi);
                        tv_coin_6.setText(userSignProductBean6.getRemark());
                        break;
                    case 2:
                        iv_sign_in_6.setImageResource(R.drawable.sign_dalibao);
                        break;
                }
            }
            //第7天
            UserSignProductBean userSignProductBean7 = userSignProductArrayList.get(6);
            if (EmptyUtils.isNotEmpty(userSignProductBean7)) {
                int signType = userSignProductBean7.getSignType();
                switch (signType) {
                    case 1:
                        iv_sign_in_7.setImageResource(R.drawable.sign_weikaiqi);
                        tv_coin_7.setText(userSignProductBean7.getRemark());
                        break;
                    case 2:
                        iv_sign_in_7.setImageResource(R.drawable.sign_dalibao);
                        break;
                }
            }
        }

        switch (signCount) {
            case 0:
                iv_sign_in_1.setImageResource(R.drawable.sign_dailingqu);
                break;
            case 1:
                iv_sign_in_1.setImageResource(R.drawable.sign_lingqu);
                iv_sign_in_2.setImageResource(R.drawable.sign_dailingqu);
                break;
            case 2:
                iv_sign_in_1.setImageResource(R.drawable.sign_lingqu);
                iv_sign_in_2.setImageResource(R.drawable.sign_lingqu);
                iv_sign_in_3.setImageResource(R.drawable.sign_dailingqu);
                break;
            case 3:
                iv_sign_in_1.setImageResource(R.drawable.sign_lingqu);
                iv_sign_in_2.setImageResource(R.drawable.sign_lingqu);
                iv_sign_in_3.setImageResource(R.drawable.sign_lingqu);
                iv_sign_in_4.setImageResource(R.drawable.sign_dailingqu);
                break;
            case 4:
                iv_sign_in_1.setImageResource(R.drawable.sign_lingqu);
                iv_sign_in_2.setImageResource(R.drawable.sign_lingqu);
                iv_sign_in_3.setImageResource(R.drawable.sign_lingqu);
                iv_sign_in_4.setImageResource(R.drawable.sign_lingqu);
                iv_sign_in_5.setImageResource(R.drawable.sign_dailingqu);
                break;
            case 5:
                iv_sign_in_1.setImageResource(R.drawable.sign_lingqu);
                iv_sign_in_2.setImageResource(R.drawable.sign_lingqu);
                iv_sign_in_3.setImageResource(R.drawable.sign_lingqu);
                iv_sign_in_4.setImageResource(R.drawable.sign_lingqu);
                iv_sign_in_5.setImageResource(R.drawable.sign_lingqu);
                iv_sign_in_6.setImageResource(R.drawable.sign_dailingqu);
                break;
            case 6:
                iv_sign_in_1.setImageResource(R.drawable.sign_lingqu);
                iv_sign_in_2.setImageResource(R.drawable.sign_lingqu);
                iv_sign_in_3.setImageResource(R.drawable.sign_lingqu);
                iv_sign_in_4.setImageResource(R.drawable.sign_lingqu);
                iv_sign_in_5.setImageResource(R.drawable.sign_lingqu);
                iv_sign_in_6.setImageResource(R.drawable.sign_lingqu);
                iv_sign_in_7.setImageResource(R.drawable.sign_dailingqu);
                break;
            case 7:
                iv_sign_in_1.setImageResource(R.drawable.sign_lingqu);
                iv_sign_in_2.setImageResource(R.drawable.sign_lingqu);
                iv_sign_in_3.setImageResource(R.drawable.sign_lingqu);
                iv_sign_in_4.setImageResource(R.drawable.sign_lingqu);
                iv_sign_in_5.setImageResource(R.drawable.sign_lingqu);
                iv_sign_in_6.setImageResource(R.drawable.sign_lingqu);
                iv_sign_in_7.setImageResource(R.drawable.sign_lingqu);
                break;
        }
    }

    private void ShowSignInSuccessPopupWindow() {
        UserSignSuccessPopupWindow userSignSuccessPopupWindow = new UserSignSuccessPopupWindow(getActivity(), new UserSignSuccessPopupWindow.UserSignSuccessPopupWindowListener() {

            @Override
            public void onSignClicked() {

            }
        });
        userSignSuccessPopupWindow.initView();
        userSignSuccessPopupWindow.showAtLocation(getView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 每日任务
     */
    private void showUserTaskView() {
        OkHttpUtils.get()
                .url(Constants.getUserTaskInfoUrl())
                .addParams(Constants.SESSION, SPUtils.getInstance().getString(Constants.SESSION))
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
                                if (EmptyUtils.isNotEmpty(jsonObjectResBody)) {
                                    JSONArray jsonArrayTaskList = jsonObjectResBody.optJSONArray("taskList");
                                    if (EmptyUtils.isNotEmpty(jsonArrayTaskList) && jsonArrayTaskList.length() > 0) {
                                        Gson gson = new Gson();
                                        ArrayList<TaskBean> taskBeanArrayList = gson.fromJson(jsonArrayTaskList.toString(), new TypeToken<ArrayList<TaskBean>>() {
                                        }.getType());
                                        if (EmptyUtils.isNotEmpty(taskBeanArrayList) && taskBeanArrayList.size() > 0) {
                                            showTaskPopupWindow(taskBeanArrayList);
                                        }
                                    }
                                }
                            } else {
                                LogUtils.e("请求数据失败：" + msg + "-" + code + "-" + req);
                                ToastUtils.showShort("请求数据失败:" + msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void showTaskPopupWindow(ArrayList<TaskBean> taskBeanArrayList) {
        final UserTaskPopupWindow userTaskPopupWindow = new UserTaskPopupWindow(getActivity(), new UserTaskPopupWindow.UserTaskPopupWindowListener() {
            @Override
            public void onCancelClicked() {

            }
        });
        userTaskPopupWindow.initView();
        userTaskPopupWindow.showAtLocation(getView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

        RecyclerView recyclerView = (RecyclerView) userTaskPopupWindow.getContentView().findViewById(R.id.recyclerView);
        if (EmptyUtils.isNotEmpty(taskBeanArrayList) && taskBeanArrayList.size() > 0) {
            BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, taskBeanArrayList, TASK_DATA_TYPE);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(baseRecyclerViewAdapter);
            baseRecyclerViewAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Object data, int position) {
                    TaskBean taskBean = (TaskBean) data;
                    if (EmptyUtils.isNotEmpty(taskBean)) {
                        String taskKey = taskBean.getTaskKey();
                        switch (taskKey) {
                            case "EVERYDAY_RECHARGE"://每日充值
                                gotoPager(MyGameCoinFragment.class, null);
                                userTaskPopupWindow.dismiss();
                                break;
                            case "EVERYDAY_INVITE_USER"://每日邀请
                                gotoPager(InvitePrizeFragment.class, null);
                                userTaskPopupWindow.dismiss();
                                break;
                            case "EVERYDAY_PLAY_WIN"://每日抓中
                                userTaskPopupWindow.dismiss();
                                break;
                            case "EVERYDAY_SGIN"://每日签到
                                showUserSignPopupWindow();
                                userTaskPopupWindow.dismiss();
                                break;
                        }
                    }
                }
            });
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
                                ToastUtils.showShort("请求数据失败:" + msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void handlerBannerData(JSONObject jsonObjectResBody) {
        if (EmptyUtils.isNotEmpty(jsonObjectResBody)) {
            JSONArray bannerList = jsonObjectResBody.optJSONArray("bannerList");
            if (EmptyUtils.isNotEmpty(bannerList)) {
                Gson gson = new Gson();
                bannerArrayList = gson.fromJson(bannerList.toString(), new TypeToken<ArrayList<BannerBean>>() {
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
    public void onStart() {
        super.onStart();
        //开始轮播
        banner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        //结束轮播
        banner.stopAutoPlay();

        if (EmptyUtils.isNotEmpty(titleBarTimer)) {
            titleBarTimer.cancel();
            titleBarTimer = null;
        }
        if (EmptyUtils.isNotEmpty(titleBarTimerTask)) {
            titleBarTimerTask.cancel();
            titleBarTimerTask = null;
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        initData();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        //此时没有重新加载数据，只是做了返回nestedScrollView顶部的操作
        smartRefreshLayout.finishLoadmore();
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
                    LogUtils.e("module：" + module + "，errCode：" + errCode + "，errMsg" + errMsg);
                }
            });
        }
    }
}
