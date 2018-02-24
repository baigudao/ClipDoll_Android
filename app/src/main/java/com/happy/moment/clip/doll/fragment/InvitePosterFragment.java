package com.happy.moment.clip.doll.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.ClipboardManager;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.happy.moment.clip.doll.R;
import com.happy.moment.clip.doll.util.CommonUtil;
import com.happy.moment.clip.doll.util.Constants;
import com.happy.moment.clip.doll.util.DataManager;
import com.happy.moment.clip.doll.view.SharePlatformPopupWindow;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by Devin on 2018/1/24 19:31
 * E-mail:971060378@qq.com
 */

public class InvitePosterFragment extends BaseFragment {

    private ImageView iv_user_photo;
    private TextView tv_user_name;

    private RelativeLayout rl_poster_share;
    private ImageView iv_QR_code;

    private Bitmap bitmap_;
    private IWXAPI api;

    private String promoteUrl;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_invite_poster;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.ll_close).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_bar_title)).setText("邀请海报");
        view.findViewById(R.id.iv_share).setOnClickListener(this);

        iv_user_photo = (ImageView) view.findViewById(R.id.iv_user_photo);
        tv_user_name = (TextView) view.findViewById(R.id.tv_user_name);

        rl_poster_share = (RelativeLayout) view.findViewById(R.id.rl_poster_share);
        rl_poster_share.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showSharePlatformPopWindow();
                return false;
            }
        });
        iv_QR_code = (ImageView) view.findViewById(R.id.iv_QR_code);
        ImageView iv_gen_poster = (ImageView) view.findViewById(R.id.iv_gen_poster);
        iv_gen_poster.setOnClickListener(this);

        regToWx();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_close:
                goBack();
                break;
            case R.id.iv_gen_poster:
                showDialog();
                break;
            case R.id.iv_share:
                showSharePlatformPopWindow();
                break;
            default:
                break;
        }
    }

    @Override
    protected void initData() {
        super.initData();
        initHeadView();
        initQR();
    }

    private void showDialog() {
        if (EmptyUtils.isEmpty(promoteUrl)) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialog_Logout);
        View view = View.inflate(mContext, R.layout.dialog_poster_view, null);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        //设置对话框的大小
        alertDialog.getWindow().setLayout(SizeUtils.dp2px(350), LinearLayout.LayoutParams.WRAP_CONTENT);
        //监听事件
        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        ImageView iv_dialog_QR = (ImageView) view.findViewById(R.id.iv_dialog_QR);
        final TextView tv_http = (TextView) view.findViewById(R.id.tv_http);
        TextView tv_copy_http = (TextView) view.findViewById(R.id.tv_copy_http);
        if (EmptyUtils.isNotEmpty(promoteUrl)) {
            //生成二维码
            final Bitmap bitmap = encodeAsBitmap(promoteUrl);
            iv_dialog_QR.setImageBitmap(bitmap);
            tv_http.setText(promoteUrl);
            tv_copy_http.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tv_http.getText());
                    ToastUtils.showShort("复制到剪贴板成功");
                }
            });
            iv_dialog_QR.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    CommonUtil.saveImageToGallery(mContext, bitmap);
                    ToastUtils.showShort("保存成功");
                    return false;
                }
            });
        }
    }

    private void initQR() {
        promoteUrl = (String) DataManager.getInstance().getData1();
        DataManager.getInstance().setData1(null);
        if (EmptyUtils.isNotEmpty(promoteUrl)) {
            //生成二维码
            Bitmap bitmap = encodeAsBitmap(promoteUrl);
            iv_QR_code.setImageBitmap(bitmap);
            //分享出去的view
            rl_poster_share.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    LogUtils.e(rl_poster_share.getWidth() + "和" + rl_poster_share.getHeight());
                    rl_poster_share.removeOnLayoutChangeListener(this);
                    bitmap_ = ImageUtils.view2Bitmap(rl_poster_share);
                }
            });
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
                if (EmptyUtils.isNotEmpty(bitmap_)) {
                    weChatShare(1);
                }
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
        sharePlatformPopWindow.getContentView().findViewById(R.id.tv_state1).setVisibility(View.GONE);
        sharePlatformPopWindow.getContentView().findViewById(R.id.tv_state2).setVisibility(View.GONE);
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

    private void initHeadView() {
        String string_head_img = SPUtils.getInstance().getString(Constants.HEADIMG);
        if (EmptyUtils.isNotEmpty(string_head_img)) {
            Glide.with(mContext)
                    .load(string_head_img)
                    .placeholder(R.drawable.avatar)
                    .error(R.drawable.avatar)
                    .into(iv_user_photo);
        }
        tv_user_name.setText(SPUtils.getInstance().getString(Constants.NICKNAME));
    }

    /**
     * 生成二维码
     *
     * @param string 传入字符串
     * @return
     */
    private Bitmap encodeAsBitmap(String string) {
        Bitmap bitmap = null;
        BitMatrix result = null;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            result = multiFormatWriter.encode(string, BarcodeFormat.QR_CODE, SizeUtils.dp2px(150.0F), SizeUtils.dp2px(150.0F));
            // 使用 ZXing Android Embedded 要写的代码
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(result);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException iae) {
            return null;
        }
        return bitmap;
    }
}
