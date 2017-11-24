package com.happy.moment.clip.doll.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.happy.moment.clip.doll.R;
import com.happy.moment.clip.doll.adapter.BaseRecyclerViewAdapter;
import com.happy.moment.clip.doll.util.Constants;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;

import okhttp3.Call;

/**
 * Created by Devin on 2017/11/16 17:53
 * E-mail:971060378@qq.com
 */

public class NotificationCenterFragment extends BaseFragment {

    private static final int NOTIFICATION_CENTER_DATA_TYPE = 16;
    private RecyclerView recyclerView_notification;


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
    }

    @Override
    protected void initData() {
        super.initData();
        getDataFromNet();
    }

    private void getDataFromNet() {
        ArrayList<String> strings = new ArrayList<>();
        strings.add("hh");
        strings.add("xx");
        strings.add("33");
        BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, strings, NOTIFICATION_CENTER_DATA_TYPE);
        recyclerView_notification.setAdapter(baseRecyclerViewAdapter);
        recyclerView_notification.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

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
                        LogUtils.e(response);
                    }
                });
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
