package com.happy.moment.clip.doll.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.happy.moment.clip.doll.R;
import com.happy.moment.clip.doll.adapter.BaseRecyclerViewAdapter;
import com.happy.moment.clip.doll.bean.EarningUserBean;
import com.happy.moment.clip.doll.bean.MyIncomeBean;
import com.happy.moment.clip.doll.util.Constants;
import com.happy.moment.clip.doll.util.DataManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Call;

/**
 * Created by Devin on 2017/12/23 19:35
 * E-mail:971060378@qq.com
 */

public class MyIncomeDetailFragment extends BaseFragment {

    private TextView tv_take_cash;
    private TextView tv_has_clearing;
    private TextView tv_total_income;
    private TextView tv_yesterday_income_num;
    private TextView tv_yesterday_user_num;
    private TextView tv_seven_new_income_num;
    private TextView tv_seven_new_user_num;
    private TextView tv_thirtieth_new_income_num;
    private TextView tv_thirtieth_new_user_num;
    private TextView tv_has_invite_num;
    private TextView tv_has_earning;

    private RecyclerView recyclerView_user;

    private MyIncomeBean myIncomeBean;

    private static final int EARNING_USER_DATA_TYPE = 10;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_income_detail;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.ll_close).setVisibility(View.GONE);
        ((TextView) view.findViewById(R.id.tv_bar_title)).setText("我的收益");
        view.findViewById(R.id.iv_share).setVisibility(View.GONE);

        view.findViewById(R.id.iv_go_to_9).setOnClickListener(this);
        tv_take_cash = (TextView) view.findViewById(R.id.tv_take_cash);

        Button btn_take_cash = (Button) view.findViewById(R.id.btn_take_cash);
        btn_take_cash.setOnClickListener(this);
        Button btn_lizhuan = (Button) view.findViewById(R.id.btn_lizhuan);
        btn_lizhuan.setOnClickListener(this);

        tv_has_clearing = (TextView) view.findViewById(R.id.tv_has_clearing);//已结算
        tv_total_income = (TextView) view.findViewById(R.id.tv_total_income);//总收益

        tv_yesterday_income_num = (TextView) view.findViewById(R.id.tv_yesterday_income_num);
        tv_yesterday_user_num = (TextView) view.findViewById(R.id.tv_yesterday_user_num);

        tv_seven_new_income_num = (TextView) view.findViewById(R.id.tv_seven_new_income_num);
        tv_seven_new_user_num = (TextView) view.findViewById(R.id.tv_seven_new_user_num);
        tv_thirtieth_new_income_num = (TextView) view.findViewById(R.id.tv_thirtieth_new_income_num);
        tv_thirtieth_new_user_num = (TextView) view.findViewById(R.id.tv_thirtieth_new_user_num);
        tv_has_invite_num = (TextView) view.findViewById(R.id.tv_has_invite_num);
        tv_has_earning = (TextView) view.findViewById(R.id.tv_has_earning);

        Button btn_get_poster = (Button) view.findViewById(R.id.btn_get_poster);
        btn_get_poster.setOnClickListener(this);

        recyclerView_user = (RecyclerView) view.findViewById(R.id.recyclerView_user);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_take_cash:
                DataManager.getInstance().setData1(myIncomeBean);
                gotoPager(TakeCashFragment.class, null);
                break;
            case R.id.btn_get_poster:
                if (EmptyUtils.isNotEmpty(myIncomeBean)) {
                    String promoteUrl = myIncomeBean.getPromoteUrl();
                    if (EmptyUtils.isNotEmpty(promoteUrl)) {
                        DataManager.getInstance().setData1(promoteUrl);
                        gotoPager(PosterFragment.class, null);
                    }
                }
                break;
            case R.id.btn_lizhuan:
                if (EmptyUtils.isNotEmpty(myIncomeBean)) {
                    String promoteUrl = myIncomeBean.getPromoteUrl();
                    if (EmptyUtils.isNotEmpty(promoteUrl)) {
                        DataManager.getInstance().setData1(promoteUrl);
                        gotoPager(InvitePosterFragment.class, null);
                    }
                }
                break;
            case R.id.iv_go_to_9:
                if (EmptyUtils.isNotEmpty(myIncomeBean)) {
                    String promoteUrl = myIncomeBean.getPromoteUrl();
                    if (EmptyUtils.isNotEmpty(promoteUrl)) {
                        DataManager.getInstance().setData1(promoteUrl);
                        gotoPager(InvitePosterFragment.class, null);
                    }
                }
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
                .url(Constants.getMyIncomeUrl())
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
                            final String req = jsonObjectResHead.optString("req");
                            JSONObject jsonObjectResBody = jsonObject.optJSONObject("resBody");
                            if (code == 1) {
                                handlerDataForMyIncome(jsonObjectResBody);
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

    private void handlerDataForMyIncome(JSONObject jsonObjectResBody) {
        if (EmptyUtils.isNotEmpty(jsonObjectResBody)) {
            Gson gson = new Gson();
            myIncomeBean = gson.fromJson(jsonObjectResBody.toString(), MyIncomeBean.class);
            if (EmptyUtils.isNotEmpty(myIncomeBean)) {
                tv_take_cash.setText(String.valueOf(myIncomeBean.getBalance()));
                tv_has_clearing.setText("已结算：" + myIncomeBean.getSettledMoney() + "元");
                tv_total_income.setText(myIncomeBean.getTotalEarnings());
                tv_yesterday_income_num.setText("昨日收益：" + myIncomeBean.getYesterdayEarnings() + "元");
                tv_yesterday_user_num.setText(String.valueOf(myIncomeBean.getYesterdayUserCount()));

                tv_seven_new_income_num.setText(myIncomeBean.getWeekEarnings());
                tv_seven_new_user_num.setText(String.valueOf(myIncomeBean.getWeekUserCount()));
                tv_thirtieth_new_income_num.setText(myIncomeBean.getMonthEarnings());
                tv_thirtieth_new_user_num.setText(String.valueOf(myIncomeBean.getMonthUserCount()));

                if (EmptyUtils.isNotEmpty(myIncomeBean.getEarningsCountVo())) {
                    tv_has_invite_num.setText(String.valueOf(myIncomeBean.getEarningsCountVo().getFriendNum()));
                    tv_has_earning.setText("¥" + String.valueOf(myIncomeBean.getEarningsCountVo().getEarningsNum()) + "元");

                    ArrayList<EarningUserBean> earningUserBeanArrayList = (ArrayList<EarningUserBean>) myIncomeBean.getEarningsCountVo().getUserEarningsVos();
                    if (EmptyUtils.isNotEmpty(earningUserBeanArrayList) && earningUserBeanArrayList.size() > 0) {
                        BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, earningUserBeanArrayList, EARNING_USER_DATA_TYPE);
                        recyclerView_user.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                        recyclerView_user.setAdapter(baseRecyclerViewAdapter);
                    }
                }
            }
        }
    }
}
