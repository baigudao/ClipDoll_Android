package com.happy.moment.clip.doll.fragment;

import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.happy.moment.clip.doll.R;

/**
 * Created by Devin on 2017/11/18 20:49
 * E-mail:971060378@qq.com
 */

public class MyGameCoinFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_game_coin;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.ll_close).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_bar_title)).setText("我的游戏币");
        view.findViewById(R.id.iv_share).setVisibility(View.GONE);
        TextView tv_cost_record = (TextView) view.findViewById(R.id.tv_cost_record);
        tv_cost_record.setVisibility(View.VISIBLE);
        tv_cost_record.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_close:
                goBack();
                break;
            case R.id.tv_cost_record:
                ToastUtils.showShort("消费记录");
                break;
            default:
                break;
        }
    }
}
