package com.happy.moment.clip.doll.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.happy.moment.clip.doll.R;
import com.happy.moment.clip.doll.adapter.BaseRecyclerViewAdapter;
import com.happy.moment.clip.doll.bean.ClipDollRecordBean;
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

/**
 * Created by Devin on 2017/11/17 10:10
 * E-mail:971060378@qq.com
 */

public class ClipDollRecordFragment extends BaseFragment implements OnRefreshListener, OnLoadmoreListener {

    private static final int CLIP_DOLL_RECORD_DATA_TYPE = 2;
    private RecyclerView recyclerView_clip_doll_record;
    private SmartRefreshLayout smartRefreshLayout;
    private int page;
    private BaseRecyclerViewAdapter baseRecyclerViewAdapter;
    private int refresh_or_load;//0或1

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_clip_doll_record;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.ll_close).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_bar_title)).setText("抓取记录");
        view.findViewById(R.id.iv_share).setVisibility(View.GONE);

        recyclerView_clip_doll_record = (RecyclerView) view.findViewById(R.id.recyclerView_clip_doll_record);
        page = 1;

        smartRefreshLayout = (SmartRefreshLayout) view.findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setEnableRefresh(true);
        smartRefreshLayout.setEnableLoadmore(true);
        smartRefreshLayout.setOnRefreshListener(this);
        smartRefreshLayout.setOnLoadmoreListener(this);
        refresh_or_load = 0;
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
        getDataFromNet();
    }

    private void getDataFromNet() {
        OkHttpUtils.post()
                .url(Constants.getClipDollRecordUrl())
                .addParams(Constants.PAGENUM, String.valueOf(page))
                .addParams(Constants.PAGESIZE, String.valueOf(Constants.PAGE_SIZE))//每页的数据数量
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
                                switch (refresh_or_load) {
                                    case 0:
                                        //刷新
                                        handlerDataForClipDollRecord(jsonObjectResBody);
                                        smartRefreshLayout.finishRefresh();
                                        break;
                                    case 1:
                                        handlerMoreDataForClipDollRecord(jsonObjectResBody);
                                        smartRefreshLayout.finishLoadmore();
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                LogUtils.e("请求数据失败：" + msg + "-" + code + "-" + req);
                                ToastUtils.showShort("请求数据失败:"+msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void handlerMoreDataForClipDollRecord(JSONObject jsonObjectResBody) {
        if (EmptyUtils.isNotEmpty(jsonObjectResBody)) {
            JSONArray jsonArrayForClipDoll = jsonObjectResBody.optJSONArray("pageData");
            if (EmptyUtils.isNotEmpty(jsonArrayForClipDoll)) {
                Gson gson = new Gson();
                ArrayList<ClipDollRecordBean> clipDollRecordBeanArrayList = gson.fromJson(jsonArrayForClipDoll.toString(), new TypeToken<ArrayList<ClipDollRecordBean>>() {
                }.getType());
                if (EmptyUtils.isNotEmpty(clipDollRecordBeanArrayList) && clipDollRecordBeanArrayList.size() != 0) {
                    baseRecyclerViewAdapter.addDatas(clipDollRecordBeanArrayList);
                } else {
                    ToastUtils.showShort("没有更多数据了");
                }
            }
        }
    }

    private void handlerDataForClipDollRecord(JSONObject jsonObjectResBody) {
        if (EmptyUtils.isNotEmpty(jsonObjectResBody)) {
            JSONArray jsonArrayForClipDoll = jsonObjectResBody.optJSONArray("pageData");
            if (EmptyUtils.isNotEmpty(jsonArrayForClipDoll)) {
                Gson gson = new Gson();
                ArrayList<ClipDollRecordBean> clipDollRecordBeanArrayList = gson.fromJson(jsonArrayForClipDoll.toString(), new TypeToken<ArrayList<ClipDollRecordBean>>() {
                }.getType());
                if (EmptyUtils.isNotEmpty(clipDollRecordBeanArrayList) && clipDollRecordBeanArrayList.size() != 0) {
                    baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, clipDollRecordBeanArrayList, CLIP_DOLL_RECORD_DATA_TYPE);
                    recyclerView_clip_doll_record.setAdapter(baseRecyclerViewAdapter);
                    recyclerView_clip_doll_record.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                }
            }
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        page = 1;
        refresh_or_load = 0;
        getDataFromNet();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        ++page;
        refresh_or_load = 1;
        getDataFromNet();
    }
}
