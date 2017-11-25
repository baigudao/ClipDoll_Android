package com.happy.moment.clip.doll.fragment;

import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.happy.moment.clip.doll.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Devin on 2017/11/18 20:58
 * E-mail:971060378@qq.com
 */

public class MyOrderFragment extends BaseFragment {

    private RadioButton btn_wait_send;
    private RadioButton btn_send_over;

    private List<BaseFragment> mBaseFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_order;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.ll_close).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_bar_title)).setText("我的订单");
        view.findViewById(R.id.iv_share).setVisibility(View.GONE);
        TextView tv_cost_record = (TextView) view.findViewById(R.id.tv_cost_record);
        tv_cost_record.setVisibility(View.VISIBLE);
        tv_cost_record.setText("地址管理");
        tv_cost_record.setOnClickListener(this);

        btn_wait_send = (RadioButton) view.findViewById(R.id.btn_wait_send);
        btn_send_over = (RadioButton) view.findViewById(R.id.btn_send_over);
        btn_wait_send.setChecked(true);

        mBaseFragment = new ArrayList<>();
        mBaseFragment.add(new WaitingSendFragment());
        mBaseFragment.add(new SendOverFragment());

        btn_wait_send.setOnClickListener(this);
        btn_send_over.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_close:
                goBack();
                break;
            case R.id.tv_cost_record:
                ToastUtils.showShort("地址管理");
                break;
            default:
                break;
        }
    }
}
