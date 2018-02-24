package com.happy.moment.clip.doll.fragment;

import android.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.happy.moment.clip.doll.R;
import com.happy.moment.clip.doll.activity.LoginActivity;
import com.happy.moment.clip.doll.activity.MyIncomeActivity;
import com.happy.moment.clip.doll.util.Constants;
import com.happy.moment.clip.doll.util.DataManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by Devin on 2017/11/16 19:20
 * E-mail:971060378@qq.com
 */

public class UserCenterFragment extends BaseFragment {

    private ImageView music_btn_toggle_on;
    private ImageView music_btn_toggle_off;
    private ImageView sound_btn_toggle_on;
    private ImageView sound_btn_toggle_off;

    private ImageView iv_user_photo;
    private TextView tv_user_name;
    private TextView tv_id_num;

    private View view_red_point;
    private int role;

    private TextView tv_my_income;
    private TextView tv_remain_coin;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_center;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.ll_close).setVisibility(View.GONE);
        ((TextView) view.findViewById(R.id.tv_bar_title)).setText("");
        view.findViewById(R.id.rl_notification).setOnClickListener(this);
        view_red_point = view.findViewById(R.id.view_red_point);

        iv_user_photo = (ImageView) view.findViewById(R.id.iv_user_photo);
        tv_user_name = (TextView) view.findViewById(R.id.tv_user_name);
        tv_id_num = (TextView) view.findViewById(R.id.tv_id_num);

        Button btn_recharge = (Button) view.findViewById(R.id.btn_recharge);
        btn_recharge.setOnClickListener(this);
        tv_my_income = (TextView) view.findViewById(R.id.tv_my_income);
        tv_remain_coin = (TextView) view.findViewById(R.id.tv_remain_coin);

        view.findViewById(R.id.rl_game_coin).setOnClickListener(this);
        view.findViewById(R.id.rl_clip_doll_record).setOnClickListener(this);
        view.findViewById(R.id.rl_order).setOnClickListener(this);
        view.findViewById(R.id.rl_prize).setOnClickListener(this);
        view.findViewById(R.id.rl_invite_num).setOnClickListener(this);
        view.findViewById(R.id.rl_about_us).setOnClickListener(this);
        view.findViewById(R.id.rl_feed_back).setOnClickListener(this);
        view.findViewById(R.id.rl_score_prize).setOnClickListener(this);
        view.findViewById(R.id.rl_check_update).setOnClickListener(this);
        view.findViewById(R.id.rl_my_income).setOnClickListener(this);
        view.findViewById(R.id.rl_become_cooperative_partner).setOnClickListener(this);

        music_btn_toggle_on = (ImageView) view.findViewById(R.id.music_btn_toggle_on);
        music_btn_toggle_on.setOnClickListener(this);
        music_btn_toggle_off = (ImageView) view.findViewById(R.id.music_btn_toggle_off);
        music_btn_toggle_off.setOnClickListener(this);
        sound_btn_toggle_on = (ImageView) view.findViewById(R.id.sound_btn_toggle_on);
        sound_btn_toggle_on.setOnClickListener(this);
        sound_btn_toggle_off = (ImageView) view.findViewById(R.id.sound_btn_toggle_off);
        sound_btn_toggle_off.setOnClickListener(this);

        //背景音乐
        if (!SPUtils.getInstance().getBoolean(Constants.IS_PLAY_BACKGROUND_MUSIC)) {
            showImageView(music_btn_toggle_on, music_btn_toggle_off);
        } else {
            showImageView(music_btn_toggle_off, music_btn_toggle_on);
        }
        //背景音效
        if (!SPUtils.getInstance().getBoolean(Constants.IS_PLAY_BACKGROUND_SOUND)) {
            showImageView(sound_btn_toggle_on, sound_btn_toggle_off);
        } else {
            showImageView(sound_btn_toggle_off, sound_btn_toggle_on);
        }

        view.findViewById(R.id.btn_exit_login).setOnClickListener(this);
    }

    private void showImageView(ImageView imageView1, ImageView imageView2) {
        imageView1.setVisibility(View.VISIBLE);
        imageView2.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_notification:
                gotoPager(NotificationCenterFragment.class, null);
                break;
            case R.id.btn_recharge:
            case R.id.rl_game_coin:
                gotoPager(MyGameCoinFragment.class, null);
                break;
            case R.id.rl_clip_doll_record:
                gotoPager(ClipDollRecordFragment.class, null);
                break;
            case R.id.rl_order:
                gotoPager(MyOrderFragment.class, null);
                break;
            case R.id.rl_prize:
                gotoPager(InvitePrizeFragment.class, null);
                break;
            case R.id.rl_invite_num:
                gotoPager(InviteNumExchangeFragment.class, null);
                break;
            case R.id.rl_my_income://代理分销
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
            case R.id.rl_become_cooperative_partner:
                gotoPager(BecomeCooperativePartnerFragment.class, null);
                break;
            case R.id.rl_about_us:
                gotoPager(AboutUsFragment.class, null);
                break;
            case R.id.rl_feed_back:
                gotoPager(FeedBackFragment.class, null);
                break;
            case R.id.rl_score_prize:
                gotoPager(ScorePrizeFragment.class, null);
                break;
            case R.id.rl_check_update:
                checkUpdate();
                break;
            case R.id.btn_exit_login:
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialog_Logout);
                View view = View.inflate(mContext, R.layout.dialog_logout_view, null);
                builder.setView(view);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                //设置对话框的大小
                alertDialog.getWindow().setLayout(SizeUtils.dp2px(350), LinearLayout.LayoutParams.WRAP_CONTENT);
                //监听事件
                view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                view.findViewById(R.id.btn_make_sure).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        OkHttpUtils.post()
                                .url(Constants.getLogoutUrl())
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
                                                //清空ticket和userId
                                                SPUtils.getInstance().put(Constants.SESSION, "");
                                                //清空sig
                                                SPUtils.getInstance().put(Constants.TLSSIGN, "");
                                                //清空第三方微信登录返回的用户信息
                                                SPUtils.getInstance().put(Constants.HEADIMG, "");
                                                SPUtils.getInstance().put(Constants.INVITECODE, "");
                                                SPUtils.getInstance().put(Constants.NICKNAME, "");
                                                SPUtils.getInstance().put(Constants.USERID, 0);
                                                DataManager.getInstance().setUserInfo(null);
                                                //清空登录状态
                                                SPUtils.getInstance().put(Constants.IS_USER_LOGIN, false);

                                                ActivityUtils.finishAllActivities();
                                                gotoPager(LoginActivity.class, null);
                                                ToastUtils.showShort("退出登录成功！");
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
                });
                break;
            case R.id.music_btn_toggle_on:
                showImageView(music_btn_toggle_off, music_btn_toggle_on);
                SPUtils.getInstance().put(Constants.IS_PLAY_BACKGROUND_MUSIC, true);
                break;
            case R.id.music_btn_toggle_off:
                showImageView(music_btn_toggle_on, music_btn_toggle_off);
                SPUtils.getInstance().put(Constants.IS_PLAY_BACKGROUND_MUSIC, false);
                break;
            case R.id.sound_btn_toggle_on:
                showImageView(sound_btn_toggle_off, sound_btn_toggle_on);
                SPUtils.getInstance().put(Constants.IS_PLAY_BACKGROUND_SOUND, true);
                break;
            case R.id.sound_btn_toggle_off:
                showImageView(sound_btn_toggle_on, sound_btn_toggle_off);
                SPUtils.getInstance().put(Constants.IS_PLAY_BACKGROUND_SOUND, false);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //控制首页通知图标上的红点的显示隐藏
        showRedPoint();
        //拉取最新的个人信息
        getUserInfoFromNet();
        //得到剩余娃娃币的数量
        getUserBalanceFromNet();
    }

    private void getUserBalanceFromNet() {
        OkHttpUtils.post()
                .url(Constants.getUserBalance())
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
                                String balance = jsonObjectResBody.optString("balance");
                                if (EmptyUtils.isNotEmpty(balance)) {
                                    tv_remain_coin.setText(balance + "币");
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

    private void getUserInfoFromNet() {
        OkHttpUtils.get()
                .url(Constants.getUserInfo())
                .addParams(Constants.SESSION, SPUtils.getInstance().getString(Constants.SESSION))
                .addParams(Constants.USERID, String.valueOf(SPUtils.getInstance().getInt(Constants.USERID)))
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
                                JSONObject jsonObjectUserInfo = jsonObjectResBody.optJSONObject("userInfo");
                                if (EmptyUtils.isNotEmpty(jsonObjectUserInfo)) {
                                    role = jsonObjectUserInfo.optInt("role");
                                    switch (role) {//0.普通用户,1.分销商
                                        case 0:
                                            tv_my_income.setText("我要赚钱");
                                            break;
                                        case 1:
                                            tv_my_income.setText("我的收益");
                                            break;
                                        default:
                                            break;
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

    private void showRedPoint() {
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
    }

    private void checkUpdate() {
        OkHttpUtils.get()
                .url(Constants.getCheckVersionUrl())
                .addParams(Constants.SESSION, SPUtils.getInstance().getString(Constants.SESSION))
                .addParams("platform", String.valueOf(0))
                .addParams("version", AppUtils.getAppVersionName())
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
                                    ToastUtils.showShort("已经是最新版本");
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
    protected void initData() {
        super.initData();
        initHeadView();
    }

    private void initHeadView() {
        String string_head_img = SPUtils.getInstance().getString(Constants.HEADIMG);
        if (EmptyUtils.isNotEmpty(string_head_img)) {
            Glide.with(mContext)
                    .load(string_head_img)
                    .placeholder(R.drawable.avatar)
                    .error(R.drawable.avatar)
                    .into(iv_user_photo);
        }
        tv_user_name.setText(SPUtils.getInstance().getString(Constants.NICKNAME));
        tv_id_num.setText("ID:" + SPUtils.getInstance().getInt(Constants.USERID));
    }
}
