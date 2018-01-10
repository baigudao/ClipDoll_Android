package com.happy.moment.clip.doll.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.happy.moment.clip.doll.R;
import com.happy.moment.clip.doll.adapter.BaseRecyclerViewAdapter;
import com.happy.moment.clip.doll.bean.OrderDetailBean;
import com.happy.moment.clip.doll.util.Constants;
import com.happy.moment.clip.doll.util.DataManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Call;

/**
 * Created by Devin on 2018/1/4 14:26
 * E-mail:971060378@qq.com
 */

public class OrderDetailFragment extends BaseFragment {

    private RecyclerView recyclerView;

    private TextView tv_order_no;
    private TextView tv_order_state;
    private TextView tv_order_type;
    private TextView tv_express_fee;
    private TextView tv_express_fee_title;
    private TextView tv_address_place;

    private TextView tv_create_time;

    private TextView tv_logistics;

    private RelativeLayout rl_address;

    private static final int ORDER_DETAIL_PRODUCT_INFO_DATA_TYPE = 9;
    private int orderId;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_order_detail;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.ll_close).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_bar_title)).setText("订单详情");
        view.findViewById(R.id.iv_share).setVisibility(View.GONE);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        tv_order_no = (TextView) view.findViewById(R.id.tv_order_no);//订单编号
        tv_order_state = (TextView) view.findViewById(R.id.tv_order_state);//订单状态
        tv_order_type = (TextView) view.findViewById(R.id.tv_order_type);//订单类型
        tv_express_fee = (TextView) view.findViewById(R.id.tv_express_fee);//快递费
        tv_express_fee_title = (TextView) view.findViewById(R.id.tv_express_fee_title);//快递费
        tv_create_time = (TextView) view.findViewById(R.id.tv_create_time);//创建时间
        tv_address_place = (TextView) view.findViewById(R.id.tv_address_place);//收货地址
        tv_logistics = (TextView) view.findViewById(R.id.tv_logistics);//物流信息

        rl_address = (RelativeLayout) view.findViewById(R.id.rl_address);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_close:
                goBack();
                break;
            default:
                break;
        }
    }

    @Override
    protected void initData() {
        super.initData();
        orderId = (int) DataManager.getInstance().getData1();
        DataManager.getInstance().setData1(null);
        if (EmptyUtils.isNotEmpty(orderId)) {
            getDataFromNet();
        }
    }

    private void getDataFromNet() {
        OkHttpUtils.get()
                .url(Constants.getOrderDetailUrl())
                .addParams(Constants.SESSION, SPUtils.getInstance().getString(Constants.SESSION))
                .addParams("orderId", String.valueOf(orderId))
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
                                    handlerDataForOrderDetail(jsonObjectResData);
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

    private void handlerDataForOrderDetail(JSONObject jsonObjectResData) {
        if (EmptyUtils.isNotEmpty(jsonObjectResData)) {
            Gson gson = new Gson();
            OrderDetailBean orderDetailBean = gson.fromJson(jsonObjectResData.toString(), OrderDetailBean.class);
            if (EmptyUtils.isNotEmpty(orderDetailBean)) {
                int orderType = orderDetailBean.getOrderType();
                switch (orderType) {
                    //0.物流订单,1.兑换游戏币
                    case 0:
                        tv_order_type.setText("物流订单");
                        tv_express_fee_title.setText("快递费");
                        tv_express_fee.setText(orderDetailBean.getPostageMoney());
                        rl_address.setVisibility(View.VISIBLE);
                        tv_address_place.setText(orderDetailBean.getAddress() + "  " + orderDetailBean.getConsignee() + "  " + orderDetailBean.getPhone());
                        if (EmptyUtils.isEmpty(orderDetailBean.getLogistics())) {
                            tv_logistics.setText("暂无物流信息");
                        } else {
                            tv_logistics.setText(orderDetailBean.getLogistics() + "  " + orderDetailBean.getLogisticsSn());
                        }
                        break;
                    case 1:
                        tv_order_type.setText("兑换游戏币");
                        tv_express_fee_title.setText("娃娃币");
                        tv_express_fee.setText(String.valueOf(orderDetailBean.getTotalExchangeLqb()));
                        rl_address.setVisibility(View.GONE);
                        tv_logistics.setText("暂无物流信息");
                        break;
                    default:
                        break;
                }

                tv_order_no.setText(orderDetailBean.getOrderSn());

                tv_order_state.setText(orderDetailBean.getStateTitle());

                tv_create_time.setText(orderDetailBean.getCreateTime());

                ArrayList<OrderDetailBean.ProductListBean> orderDetailProductInfoBeanArrayList = (ArrayList<OrderDetailBean.ProductListBean>) orderDetailBean.getProductList();
                BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, orderDetailProductInfoBeanArrayList, ORDER_DETAIL_PRODUCT_INFO_DATA_TYPE);
                recyclerView.setAdapter(baseRecyclerViewAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            }
        }
    }
}
