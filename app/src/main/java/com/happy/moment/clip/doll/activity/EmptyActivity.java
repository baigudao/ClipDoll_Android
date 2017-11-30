package com.happy.moment.clip.doll.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.LogUtils;
import com.happy.moment.clip.doll.BaseApplication;
import com.happy.moment.clip.doll.R;
import com.happy.moment.clip.doll.fragment.BaseFragment;
import com.happy.moment.clip.doll.util.Constants;


public class EmptyActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setStatusBarColor(EmptyActivity.this, getResources().getColor(R.color.main_color));
        setContentView(R.layout.activity_empty);
        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        String fragmentName = intent.getStringExtra(Constants.FRAGMENT_NAME);
        BaseFragment fragment = (BaseFragment) Fragment.instantiate(this, fragmentName);

        LogUtils.e(fragment.getClass().getSimpleName());
        Bundle bundle = intent.getExtras();
        fragment.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment currentFragment = getVisibleFragment();
        if (currentFragment != null) {
            ft.hide(currentFragment);
            LogUtils.e(currentFragment.getClass().getSimpleName());
        }else {
            LogUtils.e("currentFragment为空");
        }
        ft.add(R.id.container, fragment, fragmentName);
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
        BaseApplication.setCurFragment(fragment);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getVisibleFragment() != null) {
                getVisibleFragment().goBack();
            } else {
                finish();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
