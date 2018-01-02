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
        view.findViewById(R.id.ll_close).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_bar_title)).setText("我的娃娃币");
        view.findViewById(R.id.iv_share).setVisibility(View.GONE);
        TextView tv_cost_record = (TextView) view.findViewById(R.id.tv_cost_record);
        tv_cost_record.setVisibility(View.VISIBLE);
        tv_cost_record.setOnClickListener(this);

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
            case R.id.ll_close:
                goBack();
                break;
            case R.id.tv_cost_record:
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
                    recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3, LinearLayoutManager.VERTICAL, false));
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
            changeBackgroundColor(holder.rl_id, R.color.pure_white_color);
            if (position == 0) {
                PriceParameterBean priceParameterBean = priceParameterBeanArrayList.get(0);
                priceId = priceParameterBean.getPriceId();
                changeBackgroundColor(holder.rl_id, R.color.main_color);
                tv_recharge_num.setText(priceParameterBean.getPrice() + "元");
            }

            final PriceParameterBean priceParameterBean = priceParameterBeanArrayList.get(position);
            if (EmptyUtils.isNotEmpty(priceParameterBean)) {
                final int size = priceParameterBeanArrayList.size();
                //价目表中的内容
                holder.tv_content.setText(priceParameterBean.getRewardExplain());
                holder.tv_id.setText(String.valueOf(priceParameterBean.getLqbNum()));

                holder.rl_id.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0; i < size; i++) {
                            RelativeLayout rl_id = (RelativeLayout) recyclerView.getChildAt(i).findViewById(R.id.rl_id);
                            changeBackgroundColor(rl_id, R.color.pure_white_color);
                        }
                        changeBackgroundColor(holder.rl_id, R.color.main_color);
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
            private TextView tv_id;
            private TextView tv_content;

            ViewHolder(View itemView) {
                super(itemView);
                rl_id = (RelativeLayout) itemView.findViewById(R.id.rl_id);
                int width_size = ScreenUtils.getScreenWidth();
                int size = (width_size - SizeUtils.dp2px(60.0F)) / 3;
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) rl_id.getLayoutParams();
                layoutParams.width = size;
                rl_id.setLayoutParams(layoutParams);

                tv_id = (TextView) itemView.findViewById(R.id.tv_id);
                tv_content = (TextView) itemView.findViewById(R.id.tv_content);
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
