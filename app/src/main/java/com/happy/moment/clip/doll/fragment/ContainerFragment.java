package com.happy.moment.clip.doll.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.happy.moment.clip.doll.R;
import com.happy.moment.clip.doll.activity.ClipDollDetailActivity;
import com.happy.moment.clip.doll.adapter.BaseRecyclerViewAdapter;
import com.happy.moment.clip.doll.bean.HomeRoomBean;
import com.happy.moment.clip.doll.util.Constants;
import com.happy.moment.clip.doll.util.DataManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Call;

/**
 * Created by Devin on 2018/2/8 21:07
 * E-mail:971060378@qq.com
 */

public class ContainerFragment extends BaseFragment implements BaseRecyclerViewAdapter.OnItemClickListener {

    private static final int HOME_ROOM_LIST_DATA_TYPE = 1;
    private RecyclerView recyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_container;
    }

    @Override
    protected void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void initData() {
        super.initData();
        String tagName = (String) DataManager.getInstance().getData1();
        DataManager.getInstance().setData1(null);
        getHomeRoomListData(tagName);
    }

    private void getHomeRoomListData(String tags) {
        OkHttpUtils.get()
                .url(Constants.getHomeRoomListUrl())
                .addParams(Constants.SESSION, SPUtils.getInstance().getString(Constants.SESSION))
                .addParams(Constants.TAGS, tags)
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
                            final String req = jsonObjectResHead.optString("req");
                            JSONObject jsonObjectResBody = jsonObject.optJSONObject("resBody");
                            if (code == 1) {
                                handlerHomeRoomListData(jsonObjectResBody);
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

    private void handlerHomeRoomListData(JSONObject jsonObjectResBody) {
        if (EmptyUtils.isNotEmpty(jsonObjectResBody)) {
            JSONArray homeRoomList = jsonObjectResBody.optJSONArray("liveRoomList");
            if (EmptyUtils.isNotEmpty(homeRoomList)) {
                Gson gson = new Gson();
                ArrayList<HomeRoomBean> homeRoomBeanArrayList = gson.fromJson(homeRoomList.toString(), new TypeToken<ArrayList<HomeRoomBean>>() {
                }.getType());
                if (EmptyUtils.isNotEmpty(homeRoomBeanArrayList) && homeRoomBeanArrayList.size() != 0) {
                    BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(getActivity(), homeRoomBeanArrayList, HOME_ROOM_LIST_DATA_TYPE);
                    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false));
                    recyclerView.setAdapter(baseRecyclerViewAdapter);
                    baseRecyclerViewAdapter.setOnItemClickListener(this);
                }
            }
        }
    }

    @Override
    public void onItemClick(Object data, int position) {
        if (data.getClass().getSimpleName().equals("HomeRoomBean")) {
            HomeRoomBean homeRoomBean = (HomeRoomBean) data;
            if (EmptyUtils.isNotEmpty(homeRoomBean)) {
                DataManager.getInstance().setData1(homeRoomBean);
                gotoPager(ClipDollDetailActivity.class, null);
            }
        }
    }
}