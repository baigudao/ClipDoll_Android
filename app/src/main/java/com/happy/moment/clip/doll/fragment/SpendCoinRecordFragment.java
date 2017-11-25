package com.happy.moment.clip.doll.fragment;

import android.view.View;
import android.widget.TextView;

import com.happy.moment.clip.doll.R;

/**
 * Created by Devin on 2017/11/25 15:59
 * E-mail:971060378@qq.com
 */

public class SpendCoinRecordFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_spend_coin_record;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.ll_close).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_bar_title)).setText("消费记录");
        view.findViewById(R.id.iv_share).setVisibility(View.GONE);
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
