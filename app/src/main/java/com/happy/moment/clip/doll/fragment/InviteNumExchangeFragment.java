package com.happy.moment.clip.doll.fragment;

import android.view.View;
import android.widget.TextView;

import com.happy.moment.clip.doll.R;

/**
 * Created by Devin on 2017/11/18 21:11
 * E-mail:971060378@qq.com
 */

public class InviteNumExchangeFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_invite_num_exchange;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.ll_close).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_bar_title)).setText("邀请码兑换");
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
