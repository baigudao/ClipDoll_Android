package com.happy.moment.clip.doll.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.happy.moment.clip.doll.R;
import com.happy.moment.clip.doll.util.Constants;
import com.happy.moment.clip.doll.view.SharePlatformPopupWindow;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by Devin on 2017/11/18 21:05
 * E-mail:971060378@qq.com
 */

public class InvitePrizeFragment extends BaseFragment {

    private IWXAPI api;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_invite_prize;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.ll_close).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_bar_title)).setText("邀请奖励");
        view.findViewById(R.id.iv_share).setVisibility(View.GONE);

        view.findViewById(R.id.btn_share_num).setOnClickListener(this);

        String invite_code = SPUtils.getInstance().getString(Constants.INVITECODE);
        if (EmptyUtils.isNotEmpty(invite_code)) {
            ((TextView) view.findViewById(R.id.tv_invite_num1)).setText(invite_code.charAt(0) + "");
            ((TextView) view.findViewById(R.id.tv_invite_num2)).setText(invite_code.charAt(1) + "");
            ((TextView) view.findViewById(R.id.tv_invite_num3)).setText(invite_code.charAt(2) + "");
            ((TextView) view.findViewById(R.id.tv_invite_num4)).setText(invite_code.charAt(3) + "");
            ((TextView) view.findViewById(R.id.tv_invite_num5)).setText(invite_code.charAt(4) + "");
            ((TextView) view.findViewById(R.id.tv_invite_num6)).setText(invite_code.charAt(5) + "");
        }
        regToWx();
    }

    private void regToWx() {
        api = WXAPIFactory.createWXAPI(mContext, Constants.APP_ID, true);
        api.registerApp(Constants.APP_ID);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_close:
                goBack();
                break;
            case R.id.btn_share_num:
                showSharePlatformPopWindow();
                break;
            default:
                break;
        }
    }

    private void showSharePlatformPopWindow() {
        SharePlatformPopupWindow sharePlatformPopWindow = new SharePlatformPopupWindow(mContext, new SharePlatformPopupWindow.SharePlatformListener() {
            @Override
            public void onWeChatClicked() {
                weChatShare(0);
            }

            @Override
            public void onWechatMomentsClicked() {
                weChatShare(1);
            }

            @Override
            public void onSaveLocalClicked() {

            }

            @Override
            public void onCancelBtnClicked() {
            }
        });
        sharePlatformPopWindow.initView();
        sharePlatformPopWindow.showAtLocation(getView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void weChatShare(int flag) {
        //初始化一个wxwebpageobject对象
        WXWebpageObject webpageObject = new WXWebpageObject();
        webpageObject.webpageUrl = "http://wwh5.tuuban.com/invitation/" + SPUtils.getInstance().getInt(Constants.USERID);

        //用wxwebpageobject对象初始化一个wxmediaMessage对象
        WXMediaMessage msg = new WXMediaMessage(webpageObject);
        msg.title = "这是一封邀请函，" + SPUtils.getInstance().getString(Constants.NICKNAME) + "送您免费抓娃娃资格，快来一起挑战吧。";
        msg.description = "手机直播抓娃娃，随时随地想抓就抓";
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        msg.setThumbImage(thumb);

        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;

        //调用api接口发送数据到微信
        api.sendReq(req);

        //另一个房间中的分享：
        //标题：好友渡边孔邀您一起在线直播抓娃娃，共同High起抓娃娃世界。
        //内容：手机直播抓娃娃，随时随地想抓就抓
    }
}
