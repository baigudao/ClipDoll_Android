package com.happy.moment.clip.doll.fragment;

import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.happy.moment.clip.doll.R;
import com.happy.moment.clip.doll.util.Constants;

/**
 * Created by Devin on 2017/11/18 21:05
 * E-mail:971060378@qq.com
 */

public class InvitePrizeFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_invite_prize;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.ll_close).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_bar_title)).setText("邀请奖励");
        view.findViewById(R.id.iv_share).setVisibility(View.GONE);

        view.findViewById(R.id.btn_share_num).setOnClickListener(this);

        String invite_code = SPUtils.getInstance().getString(Constants.INVITECODE);
        if (EmptyUtils.isNotEmpty(invite_code)) {
            ((TextView) view.findViewById(R.id.tv_invite_num1)).setText(invite_code.charAt(0) + "");
            ((TextView) view.findViewById(R.id.tv_invite_num2)).setText(invite_code.charAt(1) + "");
            ((TextView) view.findViewById(R.id.tv_invite_num3)).setText(invite_code.charAt(2) + "");
            ((TextView) view.findViewById(R.id.tv_invite_num4)).setText(invite_code.charAt(3) + "");
            ((TextView) view.findViewById(R.id.tv_invite_num5)).setText(invite_code.charAt(4) + "");
            ((TextView) view.findViewById(R.id.tv_invite_num6)).setText(invite_code.charAt(5) + "");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_close:
                goBack();
                break;
            case R.id.btn_share_num:
                ToastUtils.showShort("分享");
                break;
            default:
                break;
        }
    }
}
