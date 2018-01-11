package com.happy.moment.clip.doll.fragment;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.happy.moment.clip.doll.R;
import com.happy.moment.clip.doll.activity.AddressManageActivity;

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
    private Fragment fromFragment;
    private int position;

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

        LinearLayout ll_order_explain = (LinearLayout) view.findViewById(R.id.ll_order_explain);
        ll_order_explain.setVisibility(View.GONE);
        ll_order_explain.setOnClickListener(this);

        mBaseFragment = new ArrayList<>();
        mBaseFragment.add(new WaitingSendFragment());
        mBaseFragment.add(new SendOverFragment());

        btn_wait_send = (RadioButton) view.findViewById(R.id.btn_wait_send);
        btn_send_over = (RadioButton) view.findViewById(R.id.btn_send_over);
        btn_wait_send.setChecked(true);
        btn_send_over.setChecked(false);

        ((TextView) view.findViewById(R.id.tv_auto_exchange_num)).setText(SPUtils.getInstance().getString("AUTO_EXCHANGE_TIME"));
        ((TextView) view.findViewById(R.id.tv_over_top_num)).setText(SPUtils.getInstance().getString("HOW_MACH_FREE"));

        btn_wait_send.setOnClickListener(this);
        btn_send_over.setOnClickListener(this);

        position = 0;
        switchFragment(fromFragment, getFragment());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_close:
                goBack();
                break;
            case R.id.tv_cost_record:
                gotoPager(AddressManageActivity.class, null);//暂定方案
                break;
            case R.id.btn_wait_send:
                position = 0;
                btn_wait_send.setChecked(true);
                btn_send_over.setChecked(false);
                switchFragment(fromFragment, getFragment());
                break;
            case R.id.btn_send_over:
                position = 1;
                btn_send_over.setChecked(true);
                btn_wait_send.setChecked(false);
                switchFragment(fromFragment, getFragment());
                break;
            case R.id.ll_order_explain:
                orderExplainDialog();
                break;
            default:
                break;
        }
    }

    private void orderExplainDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialog_Logout);
        View view = View.inflate(mContext, R.layout.dialog_order_explain_view, null);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        //设置对话框的大小
        alertDialog.getWindow().setLayout(SizeUtils.dp2px(340), LinearLayout.LayoutParams.WRAP_CONTENT);
        //监听事件
        view.findViewById(R.id.btn_i_know).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    /**
     * @param from 刚显示的Fragment,马上就要被隐藏了
     * @param to   马上要切换到的Fragment，一会要显示
     */
    private void switchFragment(Fragment from, Fragment to) {
        if (from != to) {
            fromFragment = to;
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            //才切换
            //判断有没有被添加
            if (!to.isAdded()) {
                //to没有被添加
                //from隐藏
                if (from != null) {
                    ft.hide(from);
                }
                //添加to
                ft.add(R.id.rl_container, to).commit();
            } else {
                //to已经被添加
                // from隐藏
                if (from != null) {
                    ft.hide(from);
                }
                //显示to
                ft.show(to).commit();
            }
        }
    }

    private BaseFragment getFragment() {
        return mBaseFragment.get(position);
    }
}
