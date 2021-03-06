package com.happy.moment.clip.doll.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happy.moment.clip.doll.BaseApplication;
import com.happy.moment.clip.doll.activity.BaseActivity;

import acplibrary.ACProgressBaseDialog;
import acplibrary.ACProgressConstant;
import acplibrary.ACProgressFlower;

public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    private ACProgressBaseDialog mDlgLoading;
    protected Context mContext;

    /**
     * 当该Fragment被创建的时候调用
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    /**
     * 当创建视图时被调用
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), null);
    }

    protected abstract int getLayoutId();

    /**
     * 视图创建好之后调用
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    protected abstract void initView(View view);

    /**
     * 当界面被创建时调用
     * 通常用来加载数据
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    /**
     * 当孩子需要初始化数据，或者联网请求绑定数据，展示数据的 等等可以重写该方法
     */
    protected void initData() {
    }

    /**
     * 跳转到新的界面
     *
     * @param pagerClass
     * @param bundle
     */
    public void gotoPager(final Class<?> pagerClass, final Bundle bundle) {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) mContext).gotoPager(pagerClass, bundle);
        }
    }

    /**
     * 跳转到新的界面
     *
     * @param pagerClass
     * @param bundle
     */
    public void gotoPager(final Class<?> pagerClass, final Bundle bundle, boolean isGoTwo) {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) mContext).gotoPager(pagerClass, bundle, isGoTwo);
        }
    }

    /**
     * 返回，如果stack中还有Fragment的话，则返回stack中的fragment，否则 finish当前的Activity
     */
    public void goBack() {
        ((BaseActivity) mContext).goBack();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!isHidden()) {
            //显示
            if (BaseApplication.getCurFragment() == null || !BaseApplication.getCurFragment().getClass().getName().equals(getClass().getName())) {
                BaseApplication.setCurFragment(this);
            }
        }
    }

    @Override
    public void onResume() {
        if (BaseApplication.getCurFragment() == null || !BaseApplication.getCurFragment().getClass().getName().equals(getClass().getName())) {
            BaseApplication.setCurFragment(this);
        }
        super.onResume();
    }

    /**
     * 显示Loading 页面， listener可为null
     *
     * @param strTitle
     * @param listener
     * @param isCancelByUser:用户是否可点击屏幕，或者Back键关掉对话框
     */
    public void showLoadingDialog(String strTitle, final DialogInterface.OnCancelListener listener, boolean isCancelByUser) {
        if (mDlgLoading == null) {
            mDlgLoading = new ACProgressFlower.Builder(getActivity())
                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                    .themeColor(Color.WHITE)  // loading花瓣颜色
                    .text(strTitle)
                    .fadeColor(Color.DKGRAY).build(); // loading花瓣颜色
        }
        if (listener != null) {
            mDlgLoading.setOnCancelListener(listener);
        }
        if (isCancelByUser) {
            mDlgLoading.setCanceledOnTouchOutside(true);
            mDlgLoading.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    return false;
                }
            });
        } else {
            mDlgLoading.setCanceledOnTouchOutside(false);
            //防止用户点击Back键，关掉此对话框
            mDlgLoading.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK)
                        return true;
                    return false;
                }
            });
        }
        mDlgLoading.setMessage(strTitle);
        mDlgLoading.show();
    }

    /**
     * 关闭loading的页面
     */
    public void hideLoadingDialog() {
        if (mDlgLoading != null) {
            mDlgLoading.dismiss();
        }
    }
}
