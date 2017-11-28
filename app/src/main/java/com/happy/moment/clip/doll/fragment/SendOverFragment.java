package com.happy.moment.clip.doll.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.happy.moment.clip.doll.R;
import com.happy.moment.clip.doll.activity.MainActivity;
import com.happy.moment.clip.doll.util.Constants;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by Devin on 2017/11/25 18:10
 * E-mail:971060378@qq.com
 */

public class SendOverFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private LinearLayout ll_no_data;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_send_over;
    }

    @Override
    protected void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        ll_no_data = (LinearLayout) view.findViewById(R.id.ll_no_data);

        view.findViewById(R.id.btn_go_clip_doll).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_go_clip_doll) {
            gotoPager(MainActivity.class, null);
        }
    }

    @Override
    protected void initData() {
        super.initData();
        getDataFromNet();
    }

    private void getDataFromNet() {
        OkHttpUtils.get()
                .url(Constants.getSendOverUrl())
                .addParams(Constants.SESSION, SPUtils.getInstance().getString(Constants.SESSION))
                .addParams(Constants.PAGENUM, "1")
                .addParams(Constants.PAGESIZE, "10")
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
                                handlerDataForSendOver(jsonObjectResBody);
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

    private void handlerDataForSendOver(JSONObject jsonObjectResBody) {
        if (EmptyUtils.isNotEmpty(jsonObjectResBody)) {
            JSONArray jsonArray = jsonObjectResBody.optJSONArray("pageData");
            if (jsonArray.length() > 0) {
                ll_no_data.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

            } else {
                ll_no_data.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        }
    }
}
