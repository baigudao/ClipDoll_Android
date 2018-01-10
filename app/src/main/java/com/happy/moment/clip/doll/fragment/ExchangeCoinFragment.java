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
import com.happy.moment.clip.doll.bean.WaitingSendBean;
import com.happy.moment.clip.doll.util.Constants;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Call;

import static com.happy.moment.clip.doll.R.id.tv_exchange_day_num;

/**
 * Created by Devin on 2018/1/3 17:49
 * E-mail:971060378@qq.com
 */

public class ExchangeCoinFragment extends BaseFragment implements OnRefreshListener, OnLoadmoreListener {

    private SmartRefreshLayout smartRefreshLayout;
    private int mPage;
    private int refresh_or_load;//0或1

    private RecyclerView recyclerView;
    private LinearLayout ll_no_data;

    private RelativeLayout rl_bottom_view;
    private ArrayList<WaitingSendBean> waitingSendBeanArrayList;

    private TextView tv_exchange_num;
    private Button btn_make_sure_exchange;

    private ExchangeCoinRecyclerViewAdapter exchangeCoinRecyclerViewAdapter;

    private ArrayList<String> playIds;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_exchange_coin;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.ll_close).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_bar_title)).setText("兑换娃娃币");
        view.findViewById(R.id.iv_share).setVisibility(View.GONE);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        ll_no_data = (LinearLayout) view.findViewById(R.id.ll_no_data);

        ((TextView) view.findViewById(tv_exchange_day_num)).setText(SPUtils.getInstance().getString("AUTO_EXCHANGE_TIME"));

        rl_bottom_view = (RelativeLayout) view.findViewById(R.id.rl_bottom_view);

        tv_exchange_num = (TextView) view.findViewById(R.id.tv_exchange_num);
        btn_make_sure_exchange = (Button) view.findViewById(R.id.btn_make_sure_exchange);
        btn_make_sure_exchange.setOnClickListener(this);

        smartRefreshLayout = (SmartRefreshLayout) view.findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setOnRefreshListener(this);
        smartRefreshLayout.setOnLoadmoreListener(this);
        refresh_or_load = 0;
        mPage = 1;
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
            case R.id.btn_make_sure_exchange:
                showDialog();
                break;
            default:
                break;
        }
    }

    @Override
    protected void initData() {
        super.initData();
        getDataFromNet();
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
                                        handlerDataForWaitingSend(jsonObjectResBody);
                                        break;
                                    case 1:
                                        handlerMoreDataForWaitingSend(jsonObjectResBody);
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

    private void handlerMoreDataForWaitingSend(JSONObject jsonObjectResBody) {
        if (EmptyUtils.isNotEmpty(jsonObjectResBody)) {
            JSONArray jsonArray = jsonObjectResBody.optJSONArray("pageData");
            if (jsonArray.length() > 0) {
                Gson gson = new Gson();
                ArrayList<WaitingSendBean> waitingSendBeanArrayList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<WaitingSendBean>>() {
                }.getType());
                if (EmptyUtils.isNotEmpty(waitingSendBeanArrayList) && waitingSendBeanArrayList.size() != 0) {
                    exchangeCoinRecyclerViewAdapter.addDatas(waitingSendBeanArrayList);
                }
            } else {
                ToastUtils.showShort("没有更多了");
            }
        }
    }

    private void handlerDataForWaitingSend(JSONObject jsonObjectResBody) {
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
                    exchangeCoinRecyclerViewAdapter = new ExchangeCoinRecyclerViewAdapter();
                    recyclerView.setAdapter(exchangeCoinRecyclerViewAdapter);
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
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        ++mPage;
        refresh_or_load = 1;
        getDataFromNet();
    }

    private class ExchangeCoinRecyclerViewAdapter extends RecyclerView.Adapter<ExchangeCoinRecyclerViewAdapter.ViewHolder> {

        private ArrayList<String> stateString;

        ExchangeCoinRecyclerViewAdapter() {
            stateString = new ArrayList<>();
            int size = waitingSendBeanArrayList.size();
            for (int i = 0; i < size; i++) {
                stateString.add("false");
            }
            playIds = new ArrayList<>();
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
                        case 0://0.不可以兑换 1.可以兑换
                            holder.tv_clip_doll_num.setText("当前商品不支持兑换");
                            holder.rl_item_send.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ToastUtils.showShort("当前商品不支持兑换！");
                                }
                            });
                            break;
                        case 1:
                            holder.tv_clip_doll_num.setText("可兑换" + waitingSendBean.getExchangeNum() + "娃娃币");

                            holder.rl_item_send.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    switch (stateString.get(position)) {
                                        case "false":
                                            holder.iv_address_choose_no.setVisibility(View.INVISIBLE);
                                            holder.iv_address_choose_yes.setVisibility(View.VISIBLE);
                                            stateString.set(position, "true");

                                            int num = Integer.valueOf(getExchangeNumText());
                                            int total = num + waitingSendBean.getExchangeNum();
                                            tv_exchange_num.setText(String.valueOf(total));

                                            if (total > 0) {
                                                btn_make_sure_exchange.setEnabled(true);
                                            }

                                            playIds.add(String.valueOf(waitingSendBean.getPlayId()));
                                            break;
                                        case "true":
                                            holder.iv_address_choose_yes.setVisibility(View.INVISIBLE);
                                            holder.iv_address_choose_no.setVisibility(View.VISIBLE);
                                            stateString.set(position, "false");

                                            int num1 = Integer.valueOf(getExchangeNumText());
                                            int total1 = num1 - waitingSendBean.getExchangeNum();
                                            tv_exchange_num.setText(String.valueOf(total1));

                                            if (total1 <= 0) {
                                                btn_make_sure_exchange.setEnabled(false);
                                            }

                                            playIds.remove(String.valueOf(waitingSendBean.getPlayId()));
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            });
                            break;
                        default:
                            break;
                    }
                }
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

    private void showDialog() {
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
                                            tv_exchange_num.setText("0");
                                            btn_make_sure_exchange.setEnabled(false);
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
}
