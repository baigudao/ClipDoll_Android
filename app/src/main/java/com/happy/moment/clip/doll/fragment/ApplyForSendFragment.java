package com.happy.moment.clip.doll.fragment;

import android.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.happy.moment.clip.doll.R;
import com.happy.moment.clip.doll.activity.AddressManageActivity;
import com.happy.moment.clip.doll.bean.AddressBean;
import com.happy.moment.clip.doll.bean.WaitingSendBean;
import com.happy.moment.clip.doll.bean.WeChatPayParamsBean;
import com.happy.moment.clip.doll.util.Constants;
import com.happy.moment.clip.doll.util.DataManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
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
 * Created by Devin on 2018/1/3 17:52
 * E-mail:971060378@qq.com
 */

public class ApplyForSendFragment extends BaseFragment implements OnRefreshListener, OnLoadmoreListener {

    private SmartRefreshLayout smartRefreshLayout;
    private int mPage;
    private int refresh_or_load;//0或1

    private TextView tv_address_place;

    private RecyclerView recyclerView;
    private LinearLayout ll_no_data;

    private RelativeLayout rl_bottom_view;
    private TextView tv_exchange_num;
    private TextView tv_express_fee;
    private Button btn_exchange_coin;
    private Button btn_make_sure_send;

    private ArrayList<WaitingSendBean> waitingSendBeanArrayList;

    private ApplySendRecyclerViewAdapter applySendRecyclerViewAdapter;

    private ArrayList<String> playIds;//兑换娃娃币
    private ArrayList<String> playIds_;//发货

    private int addressId;
    private IWXAPI api;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_apply_for_send;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.ll_close).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_bar_title)).setText("申请发货");
        view.findViewById(R.id.iv_share).setVisibility(View.GONE);

        view.findViewById(R.id.rl_address).setOnClickListener(this);
        tv_address_place = (TextView) view.findViewById(R.id.tv_address_place);//请选择收货地址

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        ll_no_data = (LinearLayout) view.findViewById(R.id.ll_no_data);

        ((TextView) view.findViewById(R.id.tv_apply_send_text)).setText(SPUtils.getInstance().getString("HOW_MACH_FREE"));

        tv_exchange_num = (TextView) view.findViewById(R.id.tv_exchange_num);

        rl_bottom_view = (RelativeLayout) view.findViewById(R.id.rl_bottom_view);
        tv_express_fee = (TextView) view.findViewById(R.id.tv_express_fee);
        btn_exchange_coin = (Button) view.findViewById(R.id.btn_exchange_coin);
        btn_exchange_coin.setOnClickListener(this);
        btn_make_sure_send = (Button) view.findViewById(R.id.btn_make_sure_send);
        btn_make_sure_send.setOnClickListener(this);

        smartRefreshLayout = (SmartRefreshLayout) view.findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setOnRefreshListener(this);
        smartRefreshLayout.setOnLoadmoreListener(this);
        refresh_or_load = 0;
        mPage = 1;

        addressId = -1;

        //注册微信支付
        regToWx();
    }

    private String getExchangeNumText() {
        return tv_exchange_num.getText().toString().trim();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_close:
                goBack();
                break;
            case R.id.rl_address:
                DataManager.getInstance().setData1("APPLY_SEND");
                gotoPager(AddressManageActivity.class, null);
                break;
            case R.id.btn_exchange_coin:
                exchangeCoin();
                break;
            case R.id.btn_make_sure_send:
                applySend();
                break;
            default:
                break;
        }
    }

    @Override
    protected void initData() {
        super.initData();
        //获取默认地址，如果有的话
        getDefaultAddress();
        getDataFromNet();
        initBottomView();
    }

    @Override
    public void onResume() {
        super.onResume();
        String apply_send_result = (String) DataManager.getInstance().getData1();
        DataManager.getInstance().setData1(null);
        if (EmptyUtils.isNotEmpty(apply_send_result) && apply_send_result.equals("APPLY_SEND_RESULT")) {
            addressId = (int) DataManager.getInstance().getData2();
            DataManager.getInstance().setData2(null);
            tv_address_place.setText((String) DataManager.getInstance().getData3());
            DataManager.getInstance().setData3(null);
        }
    }

    //兑换娃娃币
    private void exchangeCoin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialog_Logout);
        View view = View.inflate(mContext, R.layout.dialog_exchange_coin_view, null);
        ((TextView) view.findViewById(R.id.tv)).setText("你确定要兑换" + getExchangeNumText() + "娃娃币吗");
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
                        .url(Constants.getExchangeProductUrl())
                        .addParams(Constants.SESSION, SPUtils.getInstance().getString(Constants.SESSION))
                        .addParams("playIds", playIds.toString().substring(1, playIds.toString().lastIndexOf("]")))
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
                                            ToastUtils.showShort("兑换成功！");
                                            //恢复默认
                                            initBottomView();
                                            getDataFromNet();
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
        });
    }

    //申请发货
    private void applySend() {
        if (addressId == -1) {
            ToastUtils.showShort("请选择发货地址！");
            return;
        }
        OkHttpUtils.post()
                .url(Constants.getApplySendUrl())
                .addParams(Constants.SESSION, SPUtils.getInstance().getString(Constants.SESSION))
                .addParams(Constants.ADDRESSID, String.valueOf(addressId))
                .addParams("playIds", playIds_.toString().substring(1, playIds_.toString().lastIndexOf("]")))
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
                                        int business = jsonObjectResData.optInt("business");//0.充值 1.申请代理 2.邮费支付
                                        int isPay = jsonObjectResData.optInt("isPay");//0.需要 1.不需要
                                        String orderSn = jsonObjectResData.optString("orderSn");
                                        if (isPay == 0) {
                                            payExpressFee(business, orderSn);
                                        }
                                        if (isPay == 1) {
                                            getDataFromNet();
                                            initBottomView();
                                        }
                                    }
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

    private void payExpressFee(int business, String orderSn) {
        OkHttpUtils.post()
                .url(Constants.getWeChatUnifyPayUrl())
                .addParams(Constants.SESSION, SPUtils.getInstance().getString(Constants.SESSION))
                .addParams("business", String.valueOf(business))
                .addParams("orderSn", orderSn)
                .addParams("sdkType", String.valueOf(0))
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
                                    handlerWeChatPayData(jsonObjectResBody);
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

    //初始化操作
    private void initBottomView() {
        tv_express_fee.setText("0");
        tv_exchange_num.setText("0");
        btn_exchange_coin.setEnabled(false);
        btn_make_sure_send.setEnabled(false);
    }

    private void getDefaultAddress() {
        OkHttpUtils.get()
                .url(Constants.getUserAddressListUrl())
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
                                handlerDataForAddressList(jsonObjectResBody);
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

    private void handlerDataForAddressList(JSONObject jsonObjectResBody) {
        if (EmptyUtils.isNotEmpty(jsonObjectResBody)) {
            JSONArray jsonArray = jsonObjectResBody.optJSONArray("addressList");
            if (jsonArray.length() > 0) {
                Gson gson = new Gson();
                ArrayList<AddressBean> addressBeanArrayList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<AddressBean>>() {
                }.getType());
                if (EmptyUtils.isNotEmpty(addressBeanArrayList) && addressBeanArrayList.size() != 0) {
                    for (AddressBean address : addressBeanArrayList) {
                        if (address.getIsDefault() == 1) {
                            tv_address_place.setText(address.getProvince() + "省" + address.getCity() + "市" + address.getStreet() + "\n" +
                                    address.getUserName() + "  " + address.getPhone());
                            addressId = address.getAddressId();
                        }
                    }
                }
            }
        }
    }

    private void getDataFromNet() {
        OkHttpUtils.get()
                .url(Constants.getUnsentProductsUrl())
                .addParams(Constants.SESSION, SPUtils.getInstance().getString(Constants.SESSION))
                .addParams(Constants.PAGESIZE, "10")
                .addParams(Constants.PAGENUM, String.valueOf(mPage))
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
                                switch (refresh_or_load) {
                                    case 0:
                                        //刷新
                                        smartRefreshLayout.finishRefresh();
                                        handlerDataForApplySend(jsonObjectResBody);
                                        break;
                                    case 1:
                                        handlerMoreDataForApplySend(jsonObjectResBody);
                                        smartRefreshLayout.finishLoadmore();
                                        break;
                                    default:
                                        break;
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

    private void handlerMoreDataForApplySend(JSONObject jsonObjectResBody) {
        if (EmptyUtils.isNotEmpty(jsonObjectResBody)) {
            JSONArray jsonArray = jsonObjectResBody.optJSONArray("pageData");
            if (jsonArray.length() > 0) {
                Gson gson = new Gson();
                ArrayList<WaitingSendBean> waitingSendBeanArrayList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<WaitingSendBean>>() {
                }.getType());
                if (EmptyUtils.isNotEmpty(waitingSendBeanArrayList) && waitingSendBeanArrayList.size() != 0) {
                    applySendRecyclerViewAdapter.addDatas(waitingSendBeanArrayList);
                }
            } else {
                ToastUtils.showShort("没有更多了");
            }
        }
    }

    private void handlerDataForApplySend(JSONObject jsonObjectResBody) {
        if (EmptyUtils.isNotEmpty(jsonObjectResBody)) {
            JSONArray jsonArray = jsonObjectResBody.optJSONArray("pageData");
            if (jsonArray.length() > 0) {
                ll_no_data.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                rl_bottom_view.setVisibility(View.VISIBLE);
                Gson gson = new Gson();
                waitingSendBeanArrayList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<WaitingSendBean>>() {
                }.getType());

                if (EmptyUtils.isNotEmpty(waitingSendBeanArrayList)) {
                    applySendRecyclerViewAdapter = new ApplySendRecyclerViewAdapter();
                    recyclerView.setAdapter(applySendRecyclerViewAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                }
            } else {
                //没有数据
                ll_no_data.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                rl_bottom_view.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        mPage = 1;
        refresh_or_load = 0;
        getDataFromNet();
        initBottomView();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        ++mPage;
        refresh_or_load = 1;
        initBottomView();
        getDataFromNet();
    }

    private class ApplySendRecyclerViewAdapter extends RecyclerView.Adapter<ApplySendRecyclerViewAdapter.ViewHolder> {

        private ArrayList<String> stateString;

        ApplySendRecyclerViewAdapter() {
            stateString = new ArrayList<>();
            int size = waitingSendBeanArrayList.size();
            for (int i = 0; i < size; i++) {
                stateString.add("false");
            }
            playIds = new ArrayList<>();
            playIds_ = new ArrayList<>();
        }

        /**
         * 添加数据集合
         *
         * @param dataList
         */
        void addDatas(ArrayList<WaitingSendBean> dataList) {
            if (waitingSendBeanArrayList == null) {
                waitingSendBeanArrayList = new ArrayList<>();
            }
            waitingSendBeanArrayList.addAll(dataList);
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = View.inflate(mContext, R.layout.item_view_exchange_coin, null);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            if (EmptyUtils.isNotEmpty(waitingSendBeanArrayList)) {
                final WaitingSendBean waitingSendBean = waitingSendBeanArrayList.get(position);
                if (EmptyUtils.isNotEmpty(waitingSendBean)) {
                    Glide.with(mContext)
                            .load(waitingSendBean.getToyPicUrl())
                            .placeholder(R.drawable.avatar)
                            .error(R.drawable.avatar)
                            .into(holder.iv_user_photo);
                    holder.tv_clip_doll_name.setText(waitingSendBean.getToyName());

                    final int exchangeType = waitingSendBean.getExchangeType();
                    switch (exchangeType) {
                        //0.不可以兑换 1.可以兑换
                        case 0:
                            holder.tv_clip_doll_num.setText("当前商品不支持兑换");
                            break;
                        case 1:
                            holder.tv_clip_doll_num.setText("可兑换" + waitingSendBean.getExchangeNum() + "娃娃币");
                            break;
                        default:
                            break;
                    }

                    holder.rl_item_send.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (stateString.get(position)) {
                                case "false":
                                    holder.iv_address_choose_no.setVisibility(View.INVISIBLE);
                                    holder.iv_address_choose_yes.setVisibility(View.VISIBLE);
                                    stateString.set(position, "true");

                                    //重置快递费的显示
                                    resetExpressFee(Integer.valueOf(SPUtils.getInstance().getString("HOW_MACH_FREE")));

                                    btn_make_sure_send.setEnabled(true);
                                    playIds_.add(String.valueOf(waitingSendBean.getPlayId()));

                                    if (exchangeType == 1) { //0.不可以兑换 1.可以兑换
                                        int num = Integer.valueOf(getExchangeNumText());
                                        int total = num + waitingSendBean.getExchangeNum();
                                        tv_exchange_num.setText(String.valueOf(total));

                                        if (total > 0) {
                                            btn_exchange_coin.setEnabled(true);
                                        }

                                        playIds.add(String.valueOf(waitingSendBean.getPlayId()));
                                    }
                                    break;
                                case "true":
                                    holder.iv_address_choose_yes.setVisibility(View.INVISIBLE);
                                    holder.iv_address_choose_no.setVisibility(View.VISIBLE);
                                    stateString.set(position, "false");

                                    //重置快递费的显示
                                    resetExpressFee(Integer.valueOf(SPUtils.getInstance().getString("HOW_MACH_FREE")));

                                    if (!stateString.contains("true")) {
                                        btn_make_sure_send.setEnabled(false);
                                    }
                                    playIds_.remove(String.valueOf(waitingSendBean.getPlayId()));

                                    if (exchangeType == 1) {//0.不可以兑换 1.可以兑换
                                        int num1 = Integer.valueOf(getExchangeNumText());
                                        int total1 = num1 - waitingSendBean.getExchangeNum();
                                        tv_exchange_num.setText(String.valueOf(total1));

                                        if (total1 <= 0) {
                                            btn_exchange_coin.setEnabled(false);
                                        }

                                        playIds.remove(String.valueOf(waitingSendBean.getPlayId()));
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
                }
            }
        }

        /**
         * @param num 不满的数量。当不满2件时，就传2。
         */
        private void resetExpressFee(int num) {
            int count = 0;
            for (String str : stateString) {
                if (str.equals("true")) {
                    count++;
                }
            }
            tv_express_fee.setText("0");
            if (count > 0 && count < num) {
                tv_express_fee.setText(SPUtils.getInstance().getString("EXPRESS_FEE"));
            }
        }

        @Override
        public int getItemCount() {
            return waitingSendBeanArrayList == null ? 0 : waitingSendBeanArrayList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private ImageView iv_address_choose_no;
            private ImageView iv_address_choose_yes;

            private ImageView iv_user_photo;
            private TextView tv_clip_doll_name;
            private TextView tv_clip_doll_num;

            private RelativeLayout rl_item_send;

            ViewHolder(View itemView) {
                super(itemView);
                rl_item_send = (RelativeLayout) itemView.findViewById(R.id.rl_item_send);

                iv_user_photo = (ImageView) itemView.findViewById(R.id.iv_user_photo);

                tv_clip_doll_name = (TextView) itemView.findViewById(R.id.tv_clip_doll_name);
                tv_clip_doll_num = (TextView) itemView.findViewById(R.id.tv_clip_doll_num);

                iv_address_choose_no = (ImageView) itemView.findViewById(R.id.iv_address_choose_no);//设置默认地址
                iv_address_choose_yes = (ImageView) itemView.findViewById(R.id.iv_address_choose_yes);//设置默认地址
            }
        }
    }

    private void regToWx() {
        api = WXAPIFactory.createWXAPI(mContext, Constants.APP_ID, false);//false：表示checkSignature
        api.registerApp(Constants.APP_ID);
    }
}
