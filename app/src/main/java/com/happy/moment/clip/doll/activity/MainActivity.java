package com.happy.moment.clip.doll.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.SPUtils;
import com.happy.moment.clip.doll.R;
import com.happy.moment.clip.doll.fragment.BaseFragment;
import com.happy.moment.clip.doll.fragment.EarningFragment;
import com.happy.moment.clip.doll.fragment.HomeFragment;
import com.happy.moment.clip.doll.fragment.MyGameCoinFragment;
import com.happy.moment.clip.doll.fragment.MyIncomeDetailFragment;
import com.happy.moment.clip.doll.fragment.MyOrderFragment;
import com.happy.moment.clip.doll.fragment.UserCenterFragment;
import com.happy.moment.clip.doll.util.Constants;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private ArrayList<BaseFragment> mBaseFragment;
    private int position;
    private Fragment fromFragment;

    private LinearLayout ll_home;
    private LinearLayout ll_earning;
    private LinearLayout ll_recharge;
    private LinearLayout ll_order;
    private LinearLayout ll_center;

    private ImageView iv_home;
    private ImageView iv_earning;
    private ImageView iv_recharge;
    private ImageView iv_order;
    private ImageView iv_center;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化View
        initView();
        //初始化Fragment
        initFragment();
        //设置RadioGroup的监听
        setListener();
    }

    private void initView() {
        BarUtils.setStatusBarColor(MainActivity.this, getResources().getColor(R.color.new_background_color));
        BarUtils.hideNavBar(MainActivity.this);
        setContentView(R.layout.activity_main);

        ll_home = (LinearLayout) findViewById(R.id.ll_home);
        ll_earning = (LinearLayout) findViewById(R.id.ll_earning);
        ll_recharge = (LinearLayout) findViewById(R.id.ll_recharge);
        ll_order = (LinearLayout) findViewById(R.id.ll_order);
        ll_center = (LinearLayout) findViewById(R.id.ll_center);

        iv_home = (ImageView) findViewById(R.id.iv_home);
        iv_earning = (ImageView) findViewById(R.id.iv_earning);
        iv_recharge = (ImageView) findViewById(R.id.iv_recharge);
        iv_order = (ImageView) findViewById(R.id.iv_order);
        iv_center = (ImageView) findViewById(R.id.iv_center);
    }

    private void initFragment() {
        mBaseFragment = new ArrayList<>();
        mBaseFragment.add(new HomeFragment());//主Fragment
        int role = SPUtils.getInstance().getInt(Constants.ROLE);
        switch (role) {//0.普通用户,1.分销商
            case 0:
                mBaseFragment.add(new EarningFragment());//赚钱Fragment
                break;
            case 1:
                mBaseFragment.add(new MyIncomeDetailFragment());//我的收益Fragment
                break;
            default:
                mBaseFragment.add(new EarningFragment());//赚钱Fragment
                break;
        }
        mBaseFragment.add(new MyGameCoinFragment());//充值Fragment
        mBaseFragment.add(new MyOrderFragment());//订单Fragment
        mBaseFragment.add(new UserCenterFragment());//我的中心Fragment
    }

    private void setListener() {
        ll_home.setOnClickListener(this);
        ll_earning.setOnClickListener(this);
        ll_recharge.setOnClickListener(this);
        ll_order.setOnClickListener(this);
        ll_center.setOnClickListener(this);

        //默认
        position = 0;
        setCheck(0);
        switchFragment(fromFragment, getFragment());
    }

    private void setCheck(int position) {
        switch (position) {
            case 0:
                resetTab();
                iv_home.setImageResource(R.drawable.tab_home_click);
                break;
            case 1:
                resetTab();
                iv_earning.setImageResource(R.drawable.tab_zhuanqian_click);
                break;
            case 2:
                resetTab();
                iv_recharge.setImageResource(R.drawable.tab_chongzhi_click);
                break;
            case 3:
                resetTab();
                iv_order.setImageResource(R.drawable.tab_dingdan_click);
                break;
            case 4:
                resetTab();
                iv_center.setImageResource(R.drawable.tab_wode_click);
                break;
            default:
                break;
        }
    }

    private void resetTab() {
        iv_home.setImageResource(R.drawable.tab_home_normal);
        iv_earning.setImageResource(R.drawable.tab_zhuanqian_normal);
        iv_recharge.setImageResource(R.drawable.tab_chongzhi_normal);
        iv_order.setImageResource(R.drawable.tab_dingdan_normal);
        iv_center.setImageResource(R.drawable.tab_wode_normal);
    }

    private void switchFragment(Fragment from, Fragment to) {
        if (from != to) {
            fromFragment = to;
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            //才切换
            //判断有没有被添加
            if (!to.isAdded()) {
                //to没有被添加
                //from隐藏
                if (from != null) {
                    ft.hide(from);
                }
                //添加to
                ft.add(R.id.fl_content, to).commit();
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

    /**
     * 跳到首页
     */
    public void goToMyHomeFragment (){
        position = 0;
        setCheck(0);
        switchFragment(fromFragment, getFragment());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_home:
                position = 0;
                break;
            case R.id.ll_earning:
                position = 1;
                break;
            case R.id.ll_recharge:
                position = 2;
                break;
            case R.id.ll_order:
                position = 3;
                break;
            case R.id.ll_center:
                position = 4;
                break;
            default:
                break;
        }
        setCheck(position);
        //根据位置得到对应的Fragment
        BaseFragment toFragment = getFragment();
        //切换Fragment
        switchFragment(fromFragment, toFragment);
    }

    private long startTime = 0;

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - startTime) > 2000) {
            Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            startTime = currentTime;
        } else {
            finish();
        }
    }
}
