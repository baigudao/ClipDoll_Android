package com.happy.moment.clip.doll.fragment;

import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.happy.moment.clip.doll.R;
import com.happy.moment.clip.doll.bean.PriceParameterBean;
import com.happy.moment.clip.doll.bean.WeChatPayParamsBean;
import com.happy.moment.clip.doll.util.Constants;
import com.happy.moment.clip.doll.util.DataManager;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
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

    private TextView tv_recharge_num;

    private int priceId;
    private TextView tv_remain_coin;

    private IWXAPI api;

    private RecyclerView recyclerView;
    private ArrayList<PriceParameterBean> priceParameterBeanArrayList;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_game_coin;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.ll_close).setVisibility(View.GONE);
        ((TextView) view.findViewById(R.id.tv_bar_title)).setText("我的娃娃币");
        view.findViewById(R.id.iv_share).setVisibility(View.GONE);
        view.findViewById(R.id.btn_cost_record).setOnClickListener(this);

        tv_remain_coin = (TextView) view.findViewById(R.id.tv_remain_coin);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        view.findViewById(R.id.ll_coin_recharge).setOnClickListener(this);
        tv_recharge_num = (TextView) view.findViewById(R.id.tv_recharge_num);

        priceId = 0;
        //注册微信支付
        regToWx();
    }

    @Override
    public void onResume() {
        super.onResume();
        //获取价目表
        getRechargePriceList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cost_record:
                gotoPager(SpendCoinRecordFragment.class, null);
                break;
            case R.id.ll_coin_recharge:
                if (priceId != 0) {
                    recharge();//充值
                }
                break;
            default:
                break;
        }
    }

    /**
     * 充值
     */
    private void recharge() {
        OkHttpUtils.post()
                .url(Constants.getWeChatPayUrl())
                .addParams(Constants.SESSION, SPUtils.getInstance().getString(Constants.SESSION))
                .addParams(Constants.PRICEID, String.valueOf(priceId))
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
                                handlerWeChatPayData(jsonObjectResBody);
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

    private void handlerWeChatPayData(JSONObject jsonObjectResBody) {
        if (EmptyUtils.isNotEmpty(jsonObjectResBody)) {
            JSONObject jsonObject = jsonObjectResBody.optJSONObject("wxSdkParams");
            if (EmptyUtils.isNotEmpty(jsonObject)) {
                Gson gson = new Gson();
                WeChatPayParamsBean weChatPayParamsBean = gson.fromJson(jsonObject.toString(), WeChatPayParamsBean.class);
                if (EmptyUtils.isNotEmpty(weChatPayParamsBean)) {
                    PayReq request = new PayReq();
                    request.appId = weChatPayParamsBean.getAppid();
                    request.partnerId = weChatPayParamsBean.getPartnerid();
                    request.prepayId = weChatPayParamsBean.getPrepayid();
                    request.packageValue = weChatPayParamsBean.getPackageX();
                    request.nonceStr = weChatPayParamsBean.getNoncestr();
                    request.timeStamp = String.valueOf(weChatPayParamsBean.getTimestamp());
                    request.sign = weChatPayParamsBean.getSign();

                    //友盟充值统计
                    int pay_money = 0;
                    int get_wawa_coin = 0;
                    switch (priceId) {
                        case 1:
                            get_wawa_coin = 100;
                            pay_money = 10;
                            break;
                        case 2:
                            get_wawa_coin = 330;
                            pay_money = 30;
                            break;
                        case 3:
                            get_wawa_coin = 565;
                            pay_money = 50;
                            break;
                        case 4:
                            get_wawa_coin = 1500;
                            pay_money = 100;
                            break;
                        case 5:
                            get_wawa_coin = 2400;
                            pay_money = 200;
                            break;
                        case 6:
                            get_wawa_coin = 6000;
                            pay_money = 500;
                            break;
                        case 13:
                            get_wawa_coin = 1010;
                            pay_money = 10;
                            break;
                        default:
                            break;
                    }
                    DataManager.getInstance().setData1(pay_money);
                    DataManager.getInstance().setData2(get_wawa_coin);

                    //发起微信支付
                    api.sendReq(request);
                }
            }
        }
    }

    /**
     * 获取价目表
     */
    private void getRechargePriceList() {
        OkHttpUtils.post()
                .url(Constants.getRechargePriceList())
                .addParams(Constants.SESSION, SPUtils.getInstance().getString(Constants.SESSION))
                .addParams(Constants.PLATFORMTYPE, String.valueOf(0))
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
                                //处理价目表的数据
                                handlerDataForPriceList(jsonObjectResBody);
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

    private void handlerDataForPriceList(JSONObject jsonObjectResBody) {
        if (EmptyUtils.isNotEmpty(jsonObjectResBody)) {
            JSONArray jsonArray = jsonObjectResBody.optJSONArray("lqbPriceList");
            if (EmptyUtils.isNotEmpty(jsonArray)) {
                Gson gson = new Gson();
                priceParameterBeanArrayList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<PriceParameterBean>>() {
                }.getType());
                if (priceParameterBeanArrayList.size() != 0) {
                    MyGameCoinRecyclerViewAdapter myGameCoinRecyclerViewAdapter = new MyGameCoinRecyclerViewAdapter();
                    recyclerView.setAdapter(myGameCoinRecyclerViewAdapter);
                    recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2, LinearLayoutManager.VERTICAL, false));
                }
            }
        }
    }

    private class MyGameCoinRecyclerViewAdapter extends RecyclerView.Adapter<MyGameCoinRecyclerViewAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = View.inflate(mContext, R.layout.item_view_price, null);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            //默认选中第一个价目
            changeBackgroundColor(holder.rl_content, R.color.pure_white_color);
            holder.tv_chong.setTextColor(getResources().getColor(R.color.new_background_color));
            holder.tv_recharge_coin.setTextColor(getResources().getColor(R.color.new_background_color));
            holder.tv_recharge_give_coin_num.setTextColor(getResources().getColor(R.color.new_background_color));
            if (position == 0) {
                PriceParameterBean priceParameterBean = priceParameterBeanArrayList.get(0);
                priceId = priceParameterBean.getPriceId();
                changeBackgroundColor(holder.rl_content, R.color.new_ninth_background_color);
                holder.tv_chong.setTextColor(getResources().getColor(R.color.pure_white_color));
                holder.tv_recharge_coin.setTextColor(getResources().getColor(R.color.pure_white_color));
                holder.tv_recharge_give_coin_num.setTextColor(getResources().getColor(R.color.pure_white_color));
                tv_recharge_num.setText(priceParameterBean.getPrice() + "元");
            }

            final PriceParameterBean priceParameterBean = priceParameterBeanArrayList.get(position);
            if (EmptyUtils.isNotEmpty(priceParameterBean)) {
                final int size = priceParameterBeanArrayList.size();
                //价目表中的内容
                holder.tv_recharge_coin.setText(String.valueOf(priceParameterBean.getLqbNum()) + "币");
                holder.tv_recharge_give_coin_num.setText("送" + String.valueOf(priceParameterBean.getRewardNum()) + "币");
                holder.tv_recharge_money_num.setText("¥" + priceParameterBean.getPrice() + "元");

                String tagText = priceParameterBean.getTagText();
                if (EmptyUtils.isNotEmpty(tagText)) {
                    holder.tv_first_recharge_give_coin_num.setVisibility(View.VISIBLE);
                    holder.tv_first_recharge_give_coin_num.setText(priceParameterBean.getTagText());
                }

                holder.rl_id.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0; i < size; i++) {
                            View view = recyclerView.getChildAt(i);
                            RelativeLayout rl_content = (RelativeLayout) view.findViewById(R.id.rl_content);
                            TextView tv_chong = (TextView) view.findViewById(R.id.tv_chong);
                            TextView tv_recharge_coin = (TextView) view.findViewById(R.id.tv_recharge_coin);
                            TextView tv_recharge_give_coin_num = (TextView) view.findViewById(R.id.tv_recharge_give_coin_num);
                            changeBackgroundColor(rl_content, R.color.pure_white_color);
                            tv_chong.setTextColor(getResources().getColor(R.color.new_background_color));
                            tv_recharge_coin.setTextColor(getResources().getColor(R.color.new_background_color));
                            tv_recharge_give_coin_num.setTextColor(getResources().getColor(R.color.new_background_color));
                        }
                        changeBackgroundColor(holder.rl_content, R.color.new_ninth_background_color);
                        holder.tv_chong.setTextColor(getResources().getColor(R.color.pure_white_color));
                        holder.tv_recharge_coin.setTextColor(getResources().getColor(R.color.pure_white_color));
                        holder.tv_recharge_give_coin_num.setTextColor(getResources().getColor(R.color.pure_white_color));
                        //设置点击的priceId
                        priceId = priceParameterBean.getPriceId();
                        tv_recharge_num.setText(priceParameterBean.getPrice() + "元");
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return priceParameterBeanArrayList == null ? 0 : priceParameterBeanArrayList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private RelativeLayout rl_id;
            private RelativeLayout rl_content;
            private TextView tv_chong;
            private TextView tv_recharge_coin;
            private TextView tv_recharge_give_coin_num;
            private TextView tv_recharge_money_num;
            private TextView tv_first_recharge_give_coin_num;

            ViewHolder(View itemView) {
                super(itemView);
                rl_id = (RelativeLayout) itemView.findViewById(R.id.rl_id);
                int width_size = ScreenUtils.getScreenWidth();
                int size = (width_size - SizeUtils.dp2px(80.0F)) / 2;
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) rl_id.getLayoutParams();
                layoutParams.width = size;
                rl_id.setLayoutParams(layoutParams);

                rl_content = (RelativeLayout) itemView.findViewById(R.id.rl_content);
                tv_chong = (TextView) itemView.findViewById(R.id.tv_chong);
                tv_recharge_coin = (TextView) itemView.findViewById(R.id.tv_recharge_coin);
                tv_recharge_give_coin_num = (TextView) itemView.findViewById(R.id.tv_recharge_give_coin_num);

                tv_recharge_money_num = (TextView) itemView.findViewById(R.id.tv_recharge_money_num);

                tv_first_recharge_give_coin_num = (TextView) itemView.findViewById(R.id.tv_first_recharge_give_coin_num);
                tv_first_recharge_give_coin_num.setVisibility(View.GONE);
            }
        }
    }

    private void changeBackgroundColor(View view, int color) {
        GradientDrawable drawable = (GradientDrawable) view.getBackground();
        drawable.setColor(getResources().getColor(color));
    }

    private void regToWx() {
        api = WXAPIFactory.createWXAPI(getActivity(), Constants.APP_ID, false);//false：表示checkSignature
        api.registerApp(Constants.APP_ID);
    }
}
