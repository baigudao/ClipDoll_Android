package com.happy.moment.clip.doll.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
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
import com.happy.moment.clip.doll.bean.AddressBean;
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
 * Created by Devin on 2017/11/27 22:33
 * E-mail:971060378@qq.com
 */

public class AddressManageFragment extends BaseFragment {

    private Button btn_new_add_address;
    private static final int ADDRESS_LIST_DATA_TYPE = 6;
    private RecyclerView recyclerView;

    private LinearLayout ll_no_data;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_address_manage;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.ll_close).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_bar_title)).setText("地址管理");
        view.findViewById(R.id.iv_share).setVisibility(View.GONE);

        btn_new_add_address = (Button) view.findViewById(R.id.btn_new_add_address);
        btn_new_add_address.setOnClickListener(this);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        ll_no_data = (LinearLayout) view.findViewById(R.id.ll_no_data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_close:
                goBack();
                break;
            case R.id.btn_new_add_address:
                DataManager.getInstance().setData1("NEW_ADD_TYPE");
                gotoPager(NewAddAddressFragment.class, null);
                //                ((BaseActivity)mContext).getVisibleFragment().goBack();
                //                gotoPager(TestFragment.class,null);
                break;
            default:
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            LogUtils.e("隐藏了");
        } else {
            LogUtils.e("显示出来了");
        }
        //显示出来时
        //            getDataFromNet();
    }

    @Override
    public void onStart() {
        super.onStart();
        getDataFromNet();
    }

    private void getDataFromNet() {
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
                ll_no_data.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                Gson gson = new Gson();
                ArrayList<AddressBean> addressBeanArrayList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<AddressBean>>() {
                }.getType());
                if (EmptyUtils.isNotEmpty(addressBeanArrayList) && addressBeanArrayList.size() != 0) {
                    BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, addressBeanArrayList, ADDRESS_LIST_DATA_TYPE);
                    recyclerView.setAdapter(baseRecyclerViewAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                }
            } else {
                //没有数据
                ll_no_data.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        }
    }
}
