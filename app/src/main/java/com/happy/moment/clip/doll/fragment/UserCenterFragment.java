package com.happy.moment.clip.doll.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.happy.moment.clip.doll.R;

/**
 * Created by Devin on 2017/11/16 19:20
 * E-mail:971060378@qq.com
 */

public class UserCenterFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_center;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.ll_close).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_bar_title)).setText("");
        ImageView iv_share = (ImageView) view.findViewById(R.id.iv_share);
        iv_share.setImageResource(R.drawable.news);
        iv_share.setOnClickListener(this);

        view.findViewById(R.id.rl_game_coin).setOnClickListener(this);
        view.findViewById(R.id.rl_clip_doll_record).setOnClickListener(this);
        view.findViewById(R.id.rl_order).setOnClickListener(this);
        view.findViewById(R.id.rl_prize).setOnClickListener(this);
        view.findViewById(R.id.rl_invite_num).setOnClickListener(this);
        view.findViewById(R.id.rl_about_us).setOnClickListener(this);
        view.findViewById(R.id.rl_feed_back).setOnClickListener(this);
        view.findViewById(R.id.rl_score_prize).setOnClickListener(this);
        view.findViewById(R.id.rl_check_update).setOnClickListener(this);

        view.findViewById(R.id.btn_exit_login).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_close:
                goBack();
                break;
            case R.id.iv_share:
                gotoPager(NotificationCenterFragment.class, null);
                break;
            case R.id.rl_game_coin:
                gotoPager(MyGameCoinFragment.class, null);
                break;
            case R.id.rl_clip_doll_record:
                gotoPager(ClipDollRecordFragment.class, null);
                break;
            case R.id.rl_order:
                gotoPager(MyOrderFragment.class, null);
                break;
            case R.id.rl_prize:
                gotoPager(InvitePrizeFragment.class, null);
                break;
            case R.id.rl_invite_num:
                gotoPager(InviteNumExchangeFragment.class, null);
                break;
            case R.id.rl_about_us:
                gotoPager(AboutUsFragment.class, null);
                break;
            case R.id.rl_feed_back:
                gotoPager(FeedBackFragment.class, null);
                break;
            case R.id.rl_score_prize:
                ToastUtils.showShort("评分有奖");
                break;
            case R.id.rl_check_update:
                ToastUtils.showShort("检查更新");
                break;
            case R.id.btn_exit_login:
                ToastUtils.showShort("退出登录");
                break;
            default:
                break;
        }
    }
}
