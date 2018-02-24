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
import com.happy.moment.clip.doll.bean.EarningUserBean;
import com.happy.moment.clip.doll.bean.WeChatPayParamsBean;
import com.happy.moment.clip.doll.util.Constants;
import com.happy.moment.clip.doll.util.DataManager;
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
 * Created by Devin on 2018/2/2 16:36
 * E-mail:971060378@qq.com
 */

public class EarningFragment extends BaseFragment {

    private TextView tv_total;
    private RecyclerView recyclerView_user;
    private RecyclerView recyclerView_incoming;

    private IWXAPI api;

    private TextView tv_my_invite_num;
    private TextView tv_my_earning_num;
    private TextView tv_system_friend_num;

    private static final int EARNING_USER_DATA_TYPE = 10;
    private static final int EARNING_INCOMING_DATA_TYPE = 11;

    private String promoteUrl;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_earning;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.ll_close).setVisibility(View.GONE);
        ((TextView) view.findViewById(R.id.tv_bar_title)).setText("代理赚钱");
        view.findViewById(R.id.iv_share).setVisibility(View.GONE);

        view.findViewById(R.id.iv_go_to_9).setOnClickListener(this);

        tv_total = (TextView) view.findViewById(R.id.tv_total);

        view.findViewById(R.id.ll_xianshikaitong).setOnClickListener(this);
        view.findViewById(R.id.ll_lizhuan).setOnClickListener(this);

        tv_my_invite_num = (TextView) view.findViewById(R.id.tv_my_invite_num);
        tv_my_earning_num = (TextView) view.findViewById(R.id.tv_my_earning_num);
        tv_system_friend_num = (TextView) view.findViewById(R.id.tv_system_friend_num);
        view.findViewById(R.id.tv_open_permission).setOnClickListener(this);

        recyclerView_user = (RecyclerView) view.findViewById(R.id.recyclerView_user);
        recyclerView_incoming = (RecyclerView) view.findViewById(R.id.recyclerView_incoming);

        //注册微信支付
        regToWx();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_xianshikaitong:
                openPermission();
                break;
            case R.id.ll_lizhuan:
                if (EmptyUtils.isNotEmpty(promoteUrl)) {
                    DataManager.getInstance().setData1(promoteUrl);
                    gotoPager(InvitePosterFragment.class, null);
                }
                break;
            case R.id.tv_open_permission:
                openPermission();
                break;
            case R.id.iv_go_to_9:
                if (EmptyUtils.isNotEmpty(promoteUrl)) {
                    DataManager.getInstance().setData1(promoteUrl);
                    gotoPager(InvitePosterFragment.class, null);
                }
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

    private void openPermission() {
        OkHttpUtils.post()
                .url(Constants.getOpenAgentUrl())
                .addParams(Constants.SESSION, SPUtils.getInstance().getString(Constants.SESSION))
                .addParams(Constants.SDKTYPE, String.valueOf(0))
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
                                handlerWeChatPayData(jsonObjectResBody);
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

    private void regToWx() {
        api = WXAPIFactory.createWXAPI(getActivity(), Constants.APP_ID, false);//false：表示checkSignature
        api.registerApp(Constants.APP_ID);
    }

    private void getDataFromNet() {
        OkHttpUtils.get()
                .url(Constants.getEarningsListUrl())
                .addParams(Constants.SESSION, SPUtils.getInstance().getString(Constants.SESSION))
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
                            final String req = jsonObjectResHead.optString("req");
                            JSONObject jsonObjectResBody = jsonObject.optJSONObject("resBody");
                            if (code == 1) {
                                handlerData(jsonObjectResBody);
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

    private void handlerData(JSONObject jsonObjectResBody) {
        if (EmptyUtils.isNotEmpty(jsonObjectResBody)) {
            JSONObject jsonObjectSynthesizeVo = jsonObjectResBody.optJSONObject("synthesizeVo");
            if (EmptyUtils.isNotEmpty(jsonObjectSynthesizeVo)) {
                double systemEarningsNum = jsonObjectSynthesizeVo.optDouble("systemEarningsNum");
                tv_total.setText(String.valueOf(systemEarningsNum));
                int friendNum = jsonObjectSynthesizeVo.optInt("friendNum");
                tv_my_invite_num.setText(String.valueOf(friendNum));
                double earningsNum = jsonObjectSynthesizeVo.optDouble("earningsNum");
                if (String.valueOf(earningsNum).equals("NaN")) {
                    tv_my_earning_num.setText("¥00.00元");//¥00.00元
                } else {
                    tv_my_earning_num.setText("¥" + String.valueOf(earningsNum) + "元");//¥00.00元
                }
                int systemFriendNum = jsonObjectSynthesizeVo.optInt("systemFriendNum");
                tv_system_friend_num.setText(String.valueOf(systemFriendNum));
                promoteUrl = jsonObjectSynthesizeVo.optString("promoteUrl");//url

                JSONArray jsonArrayUserEarningsVos = jsonObjectSynthesizeVo.optJSONArray("userEarningsVos");//4个
                if (EmptyUtils.isNotEmpty(jsonArrayUserEarningsVos) && jsonArrayUserEarningsVos.length() > 0) {
                    Gson gson = new Gson();
                    ArrayList<EarningUserBean> earningUserBeanArrayList = gson.fromJson(jsonArrayUserEarningsVos.toString(), new TypeToken<ArrayList<EarningUserBean>>() {
                    }.getType());
                    if (EmptyUtils.isNotEmpty(earningUserBeanArrayList) && earningUserBeanArrayList.size() > 0) {
                        BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(getActivity(), earningUserBeanArrayList, EARNING_USER_DATA_TYPE);
                        recyclerView_user.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                        recyclerView_user.setAdapter(baseRecyclerViewAdapter);
                    }
                }

                JSONArray jsonArrayEarningsVos = jsonObjectSynthesizeVo.optJSONArray("earningsVos");
                if (EmptyUtils.isNotEmpty(jsonArrayEarningsVos) && jsonArrayEarningsVos.length() > 0) {
                    Gson gson = new Gson();
                    ArrayList<EarningUserBean> earningUserBeanArrayList = gson.fromJson(jsonArrayEarningsVos.toString(), new TypeToken<ArrayList<EarningUserBean>>() {
                    }.getType());
                    if (EmptyUtils.isNotEmpty(earningUserBeanArrayList) && earningUserBeanArrayList.size() > 0) {
                        int size = earningUserBeanArrayList.size();
                        if (size < 6) {
                            BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(getActivity(), earningUserBeanArrayList, EARNING_INCOMING_DATA_TYPE);
                            recyclerView_incoming.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                            recyclerView_incoming.setAdapter(baseRecyclerViewAdapter);
                        } else {
                            ArrayList<EarningUserBean> earningUserBeanArrayListCopy = new ArrayList<>();
                            for (int i = 0; i < 5; i++) {
                                earningUserBeanArrayListCopy.add(earningUserBeanArrayList.get(i));
                            }
                            BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(getActivity(), earningUserBeanArrayListCopy, EARNING_INCOMING_DATA_TYPE);
                            recyclerView_incoming.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                            recyclerView_incoming.setAdapter(baseRecyclerViewAdapter);
                        }
                    }
                }
            }
        }
    }
}
