package com.happy.moment.clip.doll.fragment;

import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.happy.moment.clip.doll.R;
import com.happy.moment.clip.doll.bean.PriceListBean;
import com.happy.moment.clip.doll.util.Constants;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Call;

/**
 * Created by Devin on 2017/11/18 20:49
 * E-mail:971060378@qq.com
 */

public class MyGameCoinFragment extends BaseFragment {

    private RelativeLayout rl_100;
    private RelativeLayout rl_200;
    private RelativeLayout rl_500;
    private RelativeLayout rl_1000;
    private RelativeLayout rl_2000;
    private RelativeLayout rl_5000;

    private TextView tv_recharge_num;

    private int priceId;
    private TextView tv_remain_coin;

    private ArrayList<PriceListBean> priceListBeanArrayList;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_game_coin;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.ll_close).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_bar_title)).setText("我的游戏币");
        view.findViewById(R.id.iv_share).setVisibility(View.GONE);
        TextView tv_cost_record = (TextView) view.findViewById(R.id.tv_cost_record);
        tv_cost_record.setVisibility(View.VISIBLE);
        tv_cost_record.setOnClickListener(this);

        tv_remain_coin = (TextView) view.findViewById(R.id.tv_remain_coin);

        view.findViewById(R.id.ll_coin_recharge).setOnClickListener(this);
        tv_recharge_num = (TextView) view.findViewById(R.id.tv_recharge_num);

        rl_100 = (RelativeLayout) view.findViewById(R.id.rl_100);
        rl_100.setOnClickListener(this);
        rl_200 = (RelativeLayout) view.findViewById(R.id.rl_200);
        rl_200.setOnClickListener(this);
        rl_500 = (RelativeLayout) view.findViewById(R.id.rl_500);
        rl_500.setOnClickListener(this);
        rl_1000 = (RelativeLayout) view.findViewById(R.id.rl_1000);
        rl_1000.setOnClickListener(this);
        rl_2000 = (RelativeLayout) view.findViewById(R.id.rl_2000);
        rl_2000.setOnClickListener(this);
        rl_5000 = (RelativeLayout) view.findViewById(R.id.rl_5000);
        rl_5000.setOnClickListener(this);
        //默认选中
        changeBackgroundColor(rl_100, R.color.main_color);
        changeBackgroundColor(rl_100, R.color.main_color);
        changeBackgroundColor(rl_200, R.color.pure_white_color);
        changeBackgroundColor(rl_500, R.color.pure_white_color);
        changeBackgroundColor(rl_1000, R.color.pure_white_color);
        changeBackgroundColor(rl_2000, R.color.pure_white_color);
        changeBackgroundColor(rl_5000, R.color.pure_white_color);
        tv_recharge_num.setText("10元");
        priceId = 1;
    }

    private void changeBackgroundColor(View view, int color) {
        GradientDrawable drawable = (GradientDrawable) view.getBackground();
        drawable.setColor(getResources().getColor(color));
    }

    @Override
    protected void initData() {
        super.initData();
        //获取价目表
        getRechargePriceListData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_close:
                goBack();
                break;
            case R.id.tv_cost_record:
                gotoPager(SpendCoinRecordFragment.class, null);
                break;
            case R.id.ll_coin_recharge:
                recharge();//充值
                break;
            case R.id.rl_100:
                changeBackgroundColor(rl_100, R.color.main_color);
                changeBackgroundColor(rl_200, R.color.pure_white_color);
                changeBackgroundColor(rl_500, R.color.pure_white_color);
                changeBackgroundColor(rl_1000, R.color.pure_white_color);
                changeBackgroundColor(rl_2000, R.color.pure_white_color);
                changeBackgroundColor(rl_5000, R.color.pure_white_color);
                tv_recharge_num.setText("10元");
                priceId = 1;
                break;
            case R.id.rl_200:
                changeBackgroundColor(rl_100, R.color.pure_white_color);
                changeBackgroundColor(rl_200, R.color.main_color);
                changeBackgroundColor(rl_500, R.color.pure_white_color);
                changeBackgroundColor(rl_1000, R.color.pure_white_color);
                changeBackgroundColor(rl_2000, R.color.pure_white_color);
                changeBackgroundColor(rl_5000, R.color.pure_white_color);
                tv_recharge_num.setText("20元");
                priceId = 2;
                break;
            case R.id.rl_500:
                changeBackgroundColor(rl_100, R.color.pure_white_color);
                changeBackgroundColor(rl_200, R.color.pure_white_color);
                changeBackgroundColor(rl_500, R.color.main_color);
                changeBackgroundColor(rl_1000, R.color.pure_white_color);
                changeBackgroundColor(rl_2000, R.color.pure_white_color);
                changeBackgroundColor(rl_5000, R.color.pure_white_color);
                tv_recharge_num.setText("50元");
                priceId = 3;
                break;
            case R.id.rl_1000:
                changeBackgroundColor(rl_100, R.color.pure_white_color);
                changeBackgroundColor(rl_200, R.color.pure_white_color);
                changeBackgroundColor(rl_500, R.color.pure_white_color);
                changeBackgroundColor(rl_1000, R.color.main_color);
                changeBackgroundColor(rl_2000, R.color.pure_white_color);
                changeBackgroundColor(rl_5000, R.color.pure_white_color);
                tv_recharge_num.setText("100元");
                priceId = 4;
                break;
            case R.id.rl_2000:
                changeBackgroundColor(rl_100, R.color.pure_white_color);
                changeBackgroundColor(rl_200, R.color.pure_white_color);
                changeBackgroundColor(rl_500, R.color.pure_white_color);
                changeBackgroundColor(rl_1000, R.color.pure_white_color);
                changeBackgroundColor(rl_2000, R.color.main_color);
                changeBackgroundColor(rl_5000, R.color.pure_white_color);
                tv_recharge_num.setText("300元");
                priceId = 5;
                break;
            case R.id.rl_5000:
                changeBackgroundColor(rl_100, R.color.pure_white_color);
                changeBackgroundColor(rl_200, R.color.pure_white_color);
                changeBackgroundColor(rl_500, R.color.pure_white_color);
                changeBackgroundColor(rl_1000, R.color.pure_white_color);
                changeBackgroundColor(rl_2000, R.color.pure_white_color);
                changeBackgroundColor(rl_5000, R.color.main_color);
                tv_recharge_num.setText("500元");
                priceId = 6;
                break;
            default:
                break;
        }
    }

    private void recharge() {
        OkHttpUtils.post()
                .url(Constants.getRechargeUrl())
                .addParams(Constants.SESSION, SPUtils.getInstance().getString(Constants.SESSION))
                .addParams("priceId", String.valueOf(priceId))
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
                                String remain_coin_string = tv_remain_coin.getText().toString();
                                int remain_coin = Integer.valueOf(remain_coin_string);
                                int recharge_coin = priceListBeanArrayList.get(priceId - 1).getLqbNum();
                                int remain_coin_total = remain_coin + recharge_coin;

                                tv_remain_coin.setText(String.valueOf(remain_coin_total));
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

    private void getRechargePriceListData() {
        OkHttpUtils.get()
                .url(Constants.getRechargePriceList())
                .addParams(Constants.SESSION, SPUtils.getInstance().getString(Constants.SESSION))
                .addParams("platformType", "0")
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
                                int balance = jsonObjectResBody.optInt("balance");
                                tv_remain_coin.setText(String.valueOf(balance));
                                handlerPriceListData(jsonObjectResBody);
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

    private void handlerPriceListData(JSONObject jsonObjectResBody) {
        if (EmptyUtils.isNotEmpty(jsonObjectResBody)) {
            JSONArray jsonArray = jsonObjectResBody.optJSONArray("lqbPriceList");
            if (jsonArray.length() > 0) {
                Gson gson = new Gson();
                priceListBeanArrayList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<PriceListBean>>() {
                }.getType());
            }
        }
    }
}
