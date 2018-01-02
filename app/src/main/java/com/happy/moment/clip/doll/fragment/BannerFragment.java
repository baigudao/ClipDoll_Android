package com.happy.moment.clip.doll.fragment;

import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.happy.moment.clip.doll.R;
import com.happy.moment.clip.doll.util.DataManager;

/**
 * Created by Devin on 2017/12/6 17:25
 * E-mail:971060378@qq.com
 */

public class BannerFragment extends BaseFragment {

    private ProgressBar progress_bar;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_banner;
    }

    @Override
    protected void initView(View view) {
        String url = (String) DataManager.getInstance().getData1();
        DataManager.getInstance().setData1(null);
        view.findViewById(R.id.ll_close).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_bar_title)).setText("");
        view.findViewById(R.id.iv_share).setVisibility(View.GONE);

        progress_bar = (ProgressBar) view.findViewById(R.id.progress_bar);

        WebView wb_banner = (WebView) view.findViewById(R.id.wb_banner);
        if (!TextUtils.isEmpty(url)) {
            wb_banner.getSettings().setJavaScriptEnabled(true);
            wb_banner.getSettings().setSupportZoom(true);
            wb_banner.getSettings().setUseWideViewPort(true);
            wb_banner.getSettings().setLoadWithOverviewMode(true);
            wb_banner.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

            if (Build.VERSION.SDK_INT < 19) {
                if (Build.VERSION.SDK_INT > 8) {
                    wb_banner.getSettings().setPluginState(WebSettings.PluginState.ON);
                }
            }

            wb_banner.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });

            wb_banner.setWebChromeClient(new WebChromeClient() {
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
            wb_banner.loadUrl(url);
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
