package com.happy.moment.clip.doll.fragment;

import android.content.Context;
import android.text.ClipboardManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.happy.moment.clip.doll.R;

/**
 * Created by Devin on 2017/12/23 16:49
 * E-mail:971060378@qq.com
 */

public class BecomeCooperativePartnerFragment extends BaseFragment {

    private ClipboardManager cm;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_become_cooperative_partner;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.ll_close).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_bar_title)).setText("成为合作伙伴");
        view.findViewById(R.id.iv_share).setVisibility(View.GONE);

        int size = ScreenUtils.getScreenWidth();
        ImageView iv_cooperative_partner = (ImageView) view.findViewById(R.id.iv_cooperative_partner);
        LinearLayout.LayoutParams cooperative_partner = (LinearLayout.LayoutParams) iv_cooperative_partner.getLayoutParams();
        cooperative_partner.height = size;
        iv_cooperative_partner.setLayoutParams(cooperative_partner);

        ((TextView)view.findViewById(R.id.tv_weixin_num)).setText(SPUtils.getInstance().getString("CONTACT_WAY"));
        view.findViewById(R.id.btn_copy).setOnClickListener(this);

        cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_close:
                goBack();
                break;
            case R.id.btn_copy:
                cm.setText(SPUtils.getInstance().getString("CONTACT_WAY"));
                ToastUtils.showShort("复制到剪贴板成功");
                break;
            default:
                break;
        }
    }
}
