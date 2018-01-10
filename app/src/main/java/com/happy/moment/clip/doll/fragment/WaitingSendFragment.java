package com.happy.moment.clip.doll.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.happy.moment.clip.doll.R;
import com.happy.moment.clip.doll.activity.MainActivity;
import com.happy.moment.clip.doll.adapter.BaseRecyclerViewAdapter;
import com.happy.moment.clip.doll.bean.WaitingSendBean;
import com.happy.moment.clip.doll.util.Constants;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Call;

/**
 * Created by Devin on 2017/11/25 18:07
 * E-mail:971060378@qq.com
 */

public class WaitingSendFragment extends BaseFragment implements OnRefreshListener, OnLoadmoreListener {

    private RecyclerView recyclerView;
    private LinearLayout ll_no_data;

    private LinearLayout ll_bottom_view;

    private SmartRefreshLayout smartRefreshLayout;
    private int mPage;
    private int refresh_or_load;//0或1

    private static final int WAITING_SEND_DATA_TYPE = 5;
    private BaseRecyclerViewAdapter baseRecyclerViewAdapter;

    static {
        ClassicsFooter.REFRESH_FOOTER_PULLUP = "";
        ClassicsFooter.REFRESH_FOOTER_RELEASE = "";
        ClassicsFooter.REFRESH_FOOTER_REFRESHING = "";
        ClassicsFooter.REFRESH_FOOTER_LOADING = "";
        ClassicsFooter.REFRESH_FOOTER_FINISH = "";
        ClassicsFooter.REFRESH_FOOTER_FAILED = "";
        ClassicsFooter.REFRESH_FOOTER_ALLLOADED = "";
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_waiting_send;
    }

    @Override
    protected void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        ll_no_data = (LinearLayout) view.findViewById(R.id.ll_no_data);

        ll_bottom_view = (LinearLayout) view.findViewById(R.id.ll_bottom_view);

        Button btn_exchange = (Button) view.findViewById(R.id.btn_exchange);
        btn_exchange.setOnClickListener(this);
        Button btn_application = (Button) view.findViewById(R.id.btn_application);
        btn_application.setOnClickListener(this);

        smartRefreshLayout = (SmartRefreshLayout) view.findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setOnRefreshListener(this);
        smartRefreshLayout.setOnLoadmoreListener(this);
        refresh_or_load = 0;
        view.findViewById(R.id.btn_go_clip_doll).setOnClickListener(this);

        mPage = 1;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_go_clip_doll:
                gotoPager(MainActivity.class, null);
                break;
            case R.id.btn_exchange:
                gotoPager(ExchangeCoinFragment.class, null);//兑换娃娃币
                break;
            case R.id.btn_application:
                gotoPager(ApplyForSendFragment.class, null);//申请发货
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
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
                    baseRecyclerViewAdapter.addDatas(waitingSendBeanArrayList);
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
                ll_bottom_view.setVisibility(View.VISIBLE);
                Gson gson = new Gson();
                ArrayList<WaitingSendBean> waitingSendBeanArrayList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<WaitingSendBean>>() {
                }.getType());

                //                //测试数据
                //                WaitingSendBean waitingSendBean = waitingSendBeanArrayList.get(0);
                //                for (int i = 0; i < 10; i++) {
                //                    waitingSendBeanArrayList.add(waitingSendBean);
                //                }

                if (EmptyUtils.isNotEmpty(waitingSendBeanArrayList)) {
                    baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, waitingSendBeanArrayList, WAITING_SEND_DATA_TYPE);
                    recyclerView.setAdapter(baseRecyclerViewAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                }
            } else {
                //没有数据
                ll_no_data.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                ll_bottom_view.setVisibility(View.GONE);
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
}
