package com.happy.moment.clip.doll.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.happy.moment.clip.doll.R;
import com.happy.moment.clip.doll.adapter.BaseRecyclerViewAdapter;
import com.happy.moment.clip.doll.bean.HomeRoomBean;
import com.happy.moment.clip.doll.util.DataManager;

import java.util.ArrayList;

/**
 * Created by Devin on 2018/1/22 10:38
 * E-mail:971060378@qq.com
 */

public class ProductDetailFragment extends BaseFragment {

    private RecyclerView recyclerView_introduce;
    private static final int PRODUCT_INTRODUCE_DATA_TYPE = 7;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_product_detail;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.ll_close).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_bar_title)).setText("宝贝介绍");
        view.findViewById(R.id.iv_share).setVisibility(View.GONE);

        recyclerView_introduce = (RecyclerView) view.findViewById(R.id.recyclerView_introduce);
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
        HomeRoomBean homeRoomBean = (HomeRoomBean) DataManager.getInstance().getData1();
        DataManager.getInstance().setData1(null);
        //渲染宝贝图片
        if (EmptyUtils.isNotEmpty(homeRoomBean)) {
            ArrayList<String> stringArrayList = (ArrayList<String>) homeRoomBean.getProduct().getDetailPics();
            if (stringArrayList.size() != 0) {
                BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, stringArrayList, PRODUCT_INTRODUCE_DATA_TYPE);
                recyclerView_introduce.setAdapter(baseRecyclerViewAdapter);
                recyclerView_introduce.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            }
        }
    }
}
