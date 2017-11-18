package com.happy.moment.clip.doll.fragment;

import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.EmptyUtils;
import com.happy.moment.clip.doll.R;

/**
 * Created by Devin on 2017/11/18 20:02
 * E-mail:971060378@qq.com
 */

public class AboutUsFragment extends BaseFragment {

    private TextView tv_version;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_about_us;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.ll_close).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_bar_title)).setText("关于我们");
        view.findViewById(R.id.iv_share).setVisibility(View.GONE);

        tv_version = (TextView) view.findViewById(R.id.tv_version);
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

    @Override
    protected void initData() {
        super.initData();
        String version = AppUtils.getAppVersionName();
        if (EmptyUtils.isNotEmpty(version)) {
            tv_version.setText("v" + version + "版本");
        }
    }
}
