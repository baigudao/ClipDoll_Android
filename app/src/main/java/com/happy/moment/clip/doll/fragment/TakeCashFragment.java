package com.happy.moment.clip.doll.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.ClipboardManager;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.happy.moment.clip.doll.R;
import com.happy.moment.clip.doll.bean.MyIncomeBean;
import com.happy.moment.clip.doll.util.CommonUtil;
import com.happy.moment.clip.doll.util.Constants;
import com.happy.moment.clip.doll.util.DataManager;
import com.happy.moment.clip.doll.view.SharePlatformPopupWindow;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by Devin on 2017/12/25 18:15
 * E-mail:971060378@qq.com
 */

public class TakeCashFragment extends BaseFragment {

    private ClipboardManager cm;

    private TextView tv_weixin_num;

    private IWXAPI api;
    private LinearLayout ll_QR_code_share;
    private ImageView iv_QR_code;

    private Bitmap bitmap_;

    private MyIncomeBean myIncomeBean;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_take_cash;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.ll_close).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_bar_title)).setText("提现操作");
        view.findViewById(R.id.iv_share).setVisibility(View.GONE);

        view.findViewById(R.id.btn_copy).setOnClickListener(this);

        cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);

        tv_weixin_num = (TextView) view.findViewById(R.id.tv_weixin_num);

        //要分享出去和保存的view
        ll_QR_code_share = (LinearLayout) view.findViewById(R.id.ll_QR_code_share);
        iv_QR_code = (ImageView) view.findViewById(R.id.iv_QR_code);

        regToWx();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_close:
                goBack();
                break;
            case R.id.btn_copy:
                if (EmptyUtils.isNotEmpty(myIncomeBean)) {
                    String withdrawWechartAccount = myIncomeBean.getWithdrawWechartAccount();
                    cm.setText(withdrawWechartAccount);
                    ToastUtils.showShort("复制到剪贴板成功");
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void initData() {
        super.initData();
        myIncomeBean = (MyIncomeBean) DataManager.getInstance().getData1();
        DataManager.getInstance().setData1(null);
        if (EmptyUtils.isNotEmpty(myIncomeBean)) {
            String withdrawQrCode = myIncomeBean.getWithdrawQrCode();
            String withdrawWechartAccount = myIncomeBean.getWithdrawWechartAccount();
            if (EmptyUtils.isNotEmpty(withdrawQrCode)) {
                //加载并显示二维码
                Glide.with(mContext)
                        .load(withdrawQrCode)
                        .placeholder(R.drawable.order_icon_image)
                        .error(R.drawable.order_icon_image)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                LogUtils.e(e.toString());
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(final GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                //加载完成之后才允许点击
                                iv_QR_code.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        showSharePlatformPopWindow();
                                    }
                                });
                                ll_QR_code_share.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                                    @Override
                                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                                        ll_QR_code_share.removeOnLayoutChangeListener(this);
                                        iv_QR_code.setImageBitmap(ImageUtils.drawable2Bitmap(resource));
                                        bitmap_ = ImageUtils.view2Bitmap(ll_QR_code_share);
                                    }
                                });
                                return false;
                            }
                        })
                        .into(iv_QR_code);
            }
            if (EmptyUtils.isNotEmpty(withdrawWechartAccount)) {
                tv_weixin_num.setText(withdrawWechartAccount);
            }
        }
    }

    private void showSharePlatformPopWindow() {
        SharePlatformPopupWindow sharePlatformPopWindow = new SharePlatformPopupWindow(mContext, new SharePlatformPopupWindow.SharePlatformListener() {
            @Override
            public void onWeChatClicked() {
                if (EmptyUtils.isNotEmpty(bitmap_)) {
                    weChatShare(0);
                }
            }

            @Override
            public void onWechatMomentsClicked() {
                //                weChatShare(1);
            }

            @Override
            public void onSaveLocalClicked() {
                //保存图片
                if (EmptyUtils.isNotEmpty(bitmap_)) {
                    CommonUtil.saveImageToGallery(mContext, bitmap_);
                    //                ImageUtils.save(bitmap_, Constants.CACHE_PATH + "/poster_share_img.jpg", Bitmap.CompressFormat.JPEG);
                    ToastUtils.showShort("保存成功");
                }
            }

            @Override
            public void onCancelBtnClicked() {
            }
        });
        sharePlatformPopWindow.initView();
        sharePlatformPopWindow.showAtLocation(getView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

        ((TextView) sharePlatformPopWindow.getContentView().findViewById(R.id.titleText)).setText("分享或保存");
        sharePlatformPopWindow.getContentView().findViewById(R.id.tv_title_desc).setVisibility(View.GONE);
        sharePlatformPopWindow.getContentView().findViewById(R.id.llWechatMoments).setVisibility(View.GONE);
        sharePlatformPopWindow.getContentView().findViewById(R.id.tv_state1).setVisibility(View.GONE);
        sharePlatformPopWindow.getContentView().findViewById(R.id.ll_save).setVisibility(View.VISIBLE);
    }

    private void weChatShare(int flag) {
        //初始化WXImageObject和WXMediaMessage对象
        WXImageObject imgObj = new WXImageObject(bitmap_);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        //设置缩略图
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        msg.setThumbImage(thumb);

        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;

        //调用api接口发送数据到微信
        api.sendReq(req);
    }

    private void regToWx() {
        api = WXAPIFactory.createWXAPI(mContext, Constants.APP_ID, true);
        api.registerApp(Constants.APP_ID);
    }
}
