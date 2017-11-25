package com.happy.moment.clip.doll.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.happy.moment.clip.doll.R;
import com.happy.moment.clip.doll.adapter.BaseRecyclerViewAdapter;
import com.happy.moment.clip.doll.bean.MessageNotificationBean;
import com.happy.moment.clip.doll.util.Constants;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Call;

/**
 * Created by Devin on 2017/11/16 17:53
 * E-mail:971060378@qq.com
 */

public class NotificationCenterFragment extends BaseFragment {

    private static final int NOTIFICATION_CENTER_DATA_TYPE = 16;
    private RecyclerView recyclerView_notification;
    private LinearLayout ll_no_data;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_notification_center;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.ll_close).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_bar_title)).setText("通知中心");
        view.findViewById(R.id.iv_share).setVisibility(View.GONE);

        recyclerView_notification = (RecyclerView) view.findViewById(R.id.recyclerView_notification);
        ll_no_data = (LinearLayout) view.findViewById(R.id.ll_no_data);
    }

    @Override
    protected void initData() {
        super.initData();
        getDataFromNet();
    }

    private void getDataFromNet() {
        OkHttpUtils.post()
                .url(Constants.getMyNotifyUrl())
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
                                handlerMessageDataForNotification(jsonObjectResBody);
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

    private void handlerMessageDataForNotification(JSONObject jsonObjectResBody) {
        if (EmptyUtils.isNotEmpty(jsonObjectResBody)) {
            JSONArray jsonArray = jsonObjectResBody.optJSONArray("messageList");
            if (EmptyUtils.isNotEmpty(jsonArray)) {
                recyclerView_notification.setVisibility(View.VISIBLE);
                ll_no_data.setVisibility(View.GONE);
                Gson gson = new Gson();
                ArrayList<MessageNotificationBean> messageNotificationBeanArrayList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<MessageNotificationBean>>() {
                }.getType());
                if (EmptyUtils.isNotEmpty(messageNotificationBeanArrayList) && messageNotificationBeanArrayList.size() != 0) {
                    BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, messageNotificationBeanArrayList, NOTIFICATION_CENTER_DATA_TYPE);
                    recyclerView_notification.setAdapter(baseRecyclerViewAdapter);
                    recyclerView_notification.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                }
            } else {
                //没有任何数据
                ll_no_data.setVisibility(View.VISIBLE);
                recyclerView_notification.setVisibility(View.GONE);
            }
        }
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
}
