package com.happy.moment.clip.doll.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.happy.moment.clip.doll.R;
import com.happy.moment.clip.doll.util.Constants;
import com.rey.material.app.Dialog;
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

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_center;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.ll_close).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_bar_title)).setText("");
        ImageView iv_share = (ImageView) view.findViewById(R.id.iv_share);
        iv_share.setImageResource(R.drawable.news);
        iv_share.setOnClickListener(this);

        iv_user_photo = (ImageView) view.findViewById(R.id.iv_user_photo);
        tv_user_name = (TextView) view.findViewById(R.id.tv_user_name);
        tv_id_num = (TextView) view.findViewById(R.id.tv_id_num);

        view.findViewById(R.id.rl_game_coin).setOnClickListener(this);
        view.findViewById(R.id.rl_clip_doll_record).setOnClickListener(this);
        view.findViewById(R.id.rl_order).setOnClickListener(this);
        view.findViewById(R.id.rl_prize).setOnClickListener(this);
        view.findViewById(R.id.rl_invite_num).setOnClickListener(this);
        view.findViewById(R.id.rl_about_us).setOnClickListener(this);
        view.findViewById(R.id.rl_feed_back).setOnClickListener(this);
        view.findViewById(R.id.rl_score_prize).setOnClickListener(this);
        view.findViewById(R.id.rl_check_update).setOnClickListener(this);

        music_btn_toggle_on = (ImageView) view.findViewById(R.id.music_btn_toggle_on);
        music_btn_toggle_on.setOnClickListener(this);
        music_btn_toggle_off = (ImageView) view.findViewById(R.id.music_btn_toggle_off);
        music_btn_toggle_off.setOnClickListener(this);
        sound_btn_toggle_on = (ImageView) view.findViewById(R.id.sound_btn_toggle_on);
        sound_btn_toggle_on.setOnClickListener(this);
        sound_btn_toggle_off = (ImageView) view.findViewById(R.id.sound_btn_toggle_off);
        sound_btn_toggle_off.setOnClickListener(this);
        //默认显示开的状态
        showImageView(music_btn_toggle_on, music_btn_toggle_off);
        showImageView(sound_btn_toggle_on, sound_btn_toggle_off);

        view.findViewById(R.id.btn_exit_login).setOnClickListener(this);
    }

    private void showImageView(ImageView imageView1, ImageView imageView2) {
        imageView1.setVisibility(View.VISIBLE);
        imageView2.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_close:
                goBack();
                break;
            case R.id.iv_share:
                gotoPager(NotificationCenterFragment.class, null);
                break;
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
            case R.id.rl_about_us:
                gotoPager(AboutUsFragment.class, null);
                break;
            case R.id.rl_feed_back:
                gotoPager(FeedBackFragment.class, null);
                break;
            case R.id.rl_score_prize:
                ToastUtils.showShort("评分有奖");
                break;
            case R.id.rl_check_update:
                checkUpdate();
                break;
            case R.id.btn_exit_login:
                final Dialog dialog = new Dialog(mContext);
                dialog.setTitle("你确定要退出应用吗？");
                dialog.negativeAction("取消").positiveAction("确定");

                dialog.negativeActionClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                dialog.positiveActionClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
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
                                        LogUtils.e(response);
                                        JSONObject jsonObject = null;
                                        try {
                                            jsonObject = new JSONObject(response);
                                            JSONObject jsonObjectResHead = jsonObject.optJSONObject("resHead");
                                            int code = jsonObjectResHead.optInt("code");
                                            String msg = jsonObjectResHead.optString("msg");
                                            String req = jsonObjectResHead.optString("req");
                                            JSONObject jsonObjectResBody = jsonObject.optJSONObject("resBody");
                                            if (code == 1) {
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
                dialog.show();
                break;
            case R.id.music_btn_toggle_on:
                showImageView(music_btn_toggle_off, music_btn_toggle_on);
                //                ToastUtils.showShort("关闭音乐");
                break;
            case R.id.music_btn_toggle_off:
                showImageView(music_btn_toggle_on, music_btn_toggle_off);
                //                ToastUtils.showShort("打开音乐");
                break;
            case R.id.sound_btn_toggle_on:
                showImageView(sound_btn_toggle_off, sound_btn_toggle_on);
                //                ToastUtils.showShort("关闭音效");
                break;
            case R.id.sound_btn_toggle_off:
                showImageView(sound_btn_toggle_on, sound_btn_toggle_off);
                //                ToastUtils.showShort("打开音效");
                break;
            default:
                break;
        }
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
        Glide.with(mContext)
                .load(SPUtils.getInstance().getString(Constants.HEADIMG))
                .placeholder(R.drawable.avatar)
                .error(R.drawable.avatar)
                .into(iv_user_photo);
        LogUtils.e(SPUtils.getInstance().getString(Constants.HEADIMG));
        tv_user_name.setText(SPUtils.getInstance().getString(Constants.NICKNAME));
        tv_id_num.setText("ID:" + SPUtils.getInstance().getInt(Constants.USERID));
    }
}
