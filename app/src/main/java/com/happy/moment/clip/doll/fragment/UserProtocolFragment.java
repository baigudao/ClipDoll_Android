package com.happy.moment.clip.doll.fragment;

import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.happy.moment.clip.doll.R;
import com.happy.moment.clip.doll.util.Constants;

/**
 * Created by Devin on 2017/12/4 11:49
 * E-mail:971060378@qq.com
 */

public class UserProtocolFragment extends BaseFragment {

    private ProgressBar progress_bar;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_protocol;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.ll_close).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_bar_title)).setText("协议和条款");
        view.findViewById(R.id.iv_share).setVisibility(View.GONE);

        progress_bar = (ProgressBar) view.findViewById(R.id.progress_bar);

        WebView wb_user_protocol = (WebView) view.findViewById(R.id.wb_user_protocol);
        String user_protocol_url = Constants.getUserProtocolUrl();
        if (!TextUtils.isEmpty(user_protocol_url)) {
            wb_user_protocol.getSettings().setJavaScriptEnabled(true);
            wb_user_protocol.getSettings().setSupportZoom(true);
            wb_user_protocol.getSettings().setUseWideViewPort(true);
            wb_user_protocol.getSettings().setLoadWithOverviewMode(true);
            wb_user_protocol.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

            if (Build.VERSION.SDK_INT < 19) {
                if (Build.VERSION.SDK_INT > 8) {
                    wb_user_protocol.getSettings().setPluginState(WebSettings.PluginState.ON);
                }
            }

            wb_user_protocol.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    super.onProgressChanged(view, newProgress);
                    if (newProgress == 100) {
                        progress_bar.setVisibility(View.GONE);
                        return;
                    }
                    progress_bar.incrementProgressBy(newProgress);
                }
            });
            wb_user_protocol.loadUrl(user_protocol_url);
        }
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
