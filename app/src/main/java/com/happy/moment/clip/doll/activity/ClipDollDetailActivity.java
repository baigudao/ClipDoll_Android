package com.happy.moment.clip.doll.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.happy.moment.clip.doll.R;
import com.happy.moment.clip.doll.view.TransparentDialog;
import com.tencent.av.sdk.AVRoomMulti;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.ILiveConstants;
import com.tencent.ilivesdk.core.ILiveLoginManager;
import com.tencent.ilivesdk.core.ILiveRoomManager;
import com.tencent.livesdk.ILVLiveManager;
import com.tencent.livesdk.ILVLiveRoomOption;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;

public class ClipDollDetailActivity extends BaseActivity implements View.OnClickListener {

    private EditText etRoom;
    private TextView tvMsg;
    private ScrollView svScroll;
    private TXCloudVideoView txvv_play_view;

    private boolean isCameraOn = true;
    private boolean isMicOn = true;
    private boolean isFlashOn = false;

    private String strMsg = "";

    private boolean login;

    private RelativeLayout rl_start_clip_and_recharge;
    private RelativeLayout rl_operation;

    private LinearLayout ll_start_clip_doll;

    private TextView tv_cost_coin_num;
    private TextView tv_coin_num;
    private TextView tv_start_clip_doll;

    private ImageButton action_btn_left;
    private ImageButton action_btn_bottom;
    private ImageButton action_btn_top;
    private ImageButton action_btn_right;
    private ImageButton action_start_clip;

    private Timer mTimer;
    private int mTotalTime;
    private TimerTask mTask;
    private TextView tv_timer;

    private int time;
    private TXLivePlayer mTxlpPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setStatusBarColor(ClipDollDetailActivity.this, getResources().getColor(R.color.main_color));
        setContentView(R.layout.activity_clip_doll_detail);

        initView();

        //        UserInfo.getInstance().getCache(getApplicationContext());
        OkHttpUtils.post()
                .url("http://119.29.119.179:8090/wawa_api/user/getTlsSign/v1")
                .addParams("state", "1")
                .addParams("data", getJsonData().toString())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e(e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            int code = jsonObject.optInt("code");
                            JSONObject jsonObjectData = jsonObject.optJSONObject("data");
                            if (code == 1) {
                                String tlsSign = jsonObjectData.optString("tlsSign");
                                LogUtils.e(tlsSign);
                                if (EmptyUtils.isNotEmpty(tlsSign)) {
                                    login(tlsSign);
                                }
                            } else {
                                String msg = jsonObject.optString("msg");
                                ToastUtils.showShort("请求数据失败,请检查网络:" + code + " - " + msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        //进入界面后10s开始抓取娃娃
        time = 5;
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ClipDollDetailActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        --time;
                        if (time <= 0) {
                            LogUtils.e("hhahahah");
                            startClipDoll();
                            timer.cancel();
                        }
                    }
                });
            }
        }, 1000, 1000);


        //        etRoom = (EditText) findViewById(R.id.et_room);
        //        //        etRoom.setText(""+UserInfo.getInstance().getRoom());
        //        tvMsg = (TextView) findViewById(R.id.tv_msg);
        //        svScroll = (ScrollView) findViewById(R.id.sv_scroll);
        //
        //                ILVLiveManager.getInstance().setAvVideoView(arvRoot);
        //        MessageObservable.getInstance().addObserver(this);
        //        StatusObservable.getInstance().addObserver(this);
        //
        //        arvRoot.setAutoOrientation(false);
        //        // 打开摄像头预览
        //        arvRoot.setSubCreatedListener(new AVRootView.onSubViewCreatedListener() {
        //            @Override
        //            public void onSubViewCreated() {
        //                ILiveRoomManager.getInstance().enableCamera(ILiveConstants.FRONT_CAMERA, true);
        //            }
        //        });
    }

    /**
     * 轮到自己抓娃娃了，显示开始抓取按钮
     */
    private void startClipDoll() {
        ll_start_clip_doll.setEnabled(true);
        tv_start_clip_doll.setTextColor(getResources().getColor(R.color.seventh_text_color));
        tv_cost_coin_num.setTextColor(getResources().getColor(R.color.seventh_text_color));
    }

    private void initView() {
        //一屏显示
        int height = ScreenUtils.getScreenHeight() - BarUtils.getStatusBarHeight() - 50;
        RelativeLayout rl_fill_screen = (RelativeLayout) findViewById(R.id.rl_fill_screen);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) rl_fill_screen.getLayoutParams();
        layoutParams.height = height;
        rl_fill_screen.setLayoutParams(layoutParams);

        findViewById(R.id.ll_close).setOnClickListener(this);
        findViewById(R.id.iv_share).setOnClickListener(this);
        findViewById(R.id.iv_live_room_camera).setOnClickListener(this);
        tv_timer = (TextView) findViewById(R.id.tv_timer);
        txvv_play_view = (TXCloudVideoView) findViewById(R.id.txvv_play_view);

        mTxlpPlayer = new TXLivePlayer(this);

        mTxlpPlayer.setPlayerView(txvv_play_view);
        mTxlpPlayer.setConfig(new TXLivePlayConfig());
        mTxlpPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
        mTxlpPlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_LANDSCAPE);

        rl_start_clip_and_recharge = (RelativeLayout) findViewById(R.id.rl_start_clip_and_recharge);
        rl_start_clip_and_recharge.setVisibility(View.VISIBLE);
        rl_operation = (RelativeLayout) findViewById(R.id.rl_operation);
        rl_operation.setVisibility(View.GONE);

        tv_cost_coin_num = (TextView) findViewById(R.id.tv_cost_coin_num);
        tv_coin_num = (TextView) findViewById(R.id.tv_coin_num);
        tv_start_clip_doll = (TextView) findViewById(R.id.tv_start_clip_doll);

        ll_start_clip_doll = (LinearLayout) findViewById(R.id.ll_start_clip_doll);
        ll_start_clip_doll.setEnabled(false);
        tv_start_clip_doll.setTextColor(getResources().getColor(R.color.pure_white_color));
        tv_cost_coin_num.setTextColor(getResources().getColor(R.color.pure_white_color));
        ll_start_clip_doll.setOnClickListener(this);
        findViewById(R.id.ll_coin_recharge).setOnClickListener(this);

        action_btn_left = (ImageButton) findViewById(R.id.action_btn_left);
        action_btn_left.setEnabled(true);
        action_btn_left.setOnClickListener(this);
        action_btn_bottom = (ImageButton) findViewById(R.id.action_btn_bottom);
        action_btn_bottom.setEnabled(true);
        action_btn_bottom.setOnClickListener(this);
        action_btn_top = (ImageButton) findViewById(R.id.action_btn_top);
        action_btn_top.setEnabled(true);
        action_btn_top.setOnClickListener(this);
        action_btn_right = (ImageButton) findViewById(R.id.action_btn_right);
        action_btn_right.setEnabled(true);
        action_btn_right.setOnClickListener(this);
        action_start_clip = (ImageButton) findViewById(R.id.action_start_clip);
        action_start_clip.setEnabled(true);
        action_start_clip.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_close:
                goBack();
                break;
            case R.id.iv_live_room_camera:
                ToastUtils.showShort("切换相机");
                break;
            case R.id.ll_start_clip_doll:
                ToastUtils.showShort("开始抓取");
                //显示操作按钮，隐藏开始抓取和充值按钮
                rl_operation.setVisibility(View.VISIBLE);
                rl_start_clip_and_recharge.setVisibility(View.INVISIBLE);
                //显示倒计时
                mTotalTime = 30;
                mTimer = new Timer();
                initTimerTask();
                mTimer.schedule(mTask, 1000, 1000);
                break;
            case R.id.ll_coin_recharge:
                ToastUtils.showShort("充值");
                break;
            case R.id.action_btn_left:
                ToastUtils.showShort("左");
                break;
            case R.id.action_btn_bottom:
                ToastUtils.showShort("下");
                break;
            case R.id.action_btn_top:
                ToastUtils.showShort("上");
                break;
            case R.id.action_btn_right:
                ToastUtils.showShort("右");
                break;
            case R.id.action_start_clip:
                ToastUtils.showShort("下抓");
                break;
            case R.id.iv_share:
                ToastUtils.showShort("分享");
                break;
            default:
                break;
        }
    }

    public JSONObject getJsonData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("state", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private void login(String tlsSign) {
        LogUtils.e("进入登录");
        ILiveLoginManager.getInstance().iLiveLogin("f631a6c7c84511e790f2246e96754b22", tlsSign, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                login = true;
                Toast.makeText(ClipDollDetailActivity.this, "login success !", Toast.LENGTH_SHORT).show();
                createRoom();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                Toast.makeText(ClipDollDetailActivity.this, module + "|login fail " + errCode + " " + errMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createRoom() {
        if (login) {
            //加入房间配置项
            ILVLiveRoomOption memberOption = new ILVLiveRoomOption("live_1_front_push_1510577069")
                    .autoCamera(false) //是否自动打开摄像头
                    .controlRole("Guest") //角色设置
                    .authBits(AVRoomMulti.AUTH_BITS_JOIN_ROOM | AVRoomMulti.AUTH_BITS_RECV_AUDIO | AVRoomMulti.AUTH_BITS_RECV_CAMERA_VIDEO | AVRoomMulti.AUTH_BITS_RECV_SCREEN_VIDEO) //权限设置
                    .videoRecvMode(AVRoomMulti.VIDEO_RECV_MODE_SEMI_AUTO_RECV_CAMERA_VIDEO) //是否开始半自动接收
                    .autoMic(false);//是否自动打开mic
            //加入房间
            ILVLiveManager.getInstance().joinRoom(348312, memberOption, new ILiveCallBack() {
                @Override
                public void onSuccess(Object data) {
                    //                bEnterRoom = true;
                    LogUtils.e(data);
                    Toast.makeText(ClipDollDetailActivity.this, "join room  ok ", Toast.LENGTH_SHORT).show();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTxlpPlayer.startPlay("rtmp://16145.liveplay.myqcloud.com/live/16145_8d6771eec114973f09a9a1a69b2a25a0", TXLivePlayer.PLAY_TYPE_LIVE_RTMP);
                        }
                    });
                    //                logoutBtn.setVisibility(View.INVISIBLE);
                    //                backBtn.setVisibility(View.VISIBLE);
                }

                @Override
                public void onError(String module, int errCode, String errMsg) {
                    //                Toast.makeText(LiveActivity.this, module + "|join fail " + errMsg + " " + errMsg, Toast.LENGTH_SHORT).show();
                    LogUtils.e("module:" + module + "\n" + "code:" + errCode + "\n" + "msg:" + errMsg);
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        ILVLiveManager.getInstance().onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ILVLiveManager.getInstance().onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ILiveConstants.NONE_CAMERA != ILiveRoomManager.getInstance().getActiveCameraId()) {
            ILiveRoomManager.getInstance().enableCamera(ILiveRoomManager.getInstance().getActiveCameraId(), false);
        }
        //        MessageObservable.getInstance().deleteObserver(this);
        //        StatusObservable.getInstance().deleteObserver(this);
        ILVLiveManager.getInstance().onDestory();
    }
    //
    //    @Override
    //    public void onClick(View view) {
    //        switch (view.getId()) {
    //            case R.id.tv_create:
    //                createRoom();
    //                break;
    //            case R.id.iv_camera:
    //                isCameraOn = !isCameraOn;
    //                ILiveRoomManager.getInstance().enableCamera(ILiveRoomManager.getInstance().getCurCameraId(),
    //                        isCameraOn);
    //                ((ImageView) findViewById(R.id.iv_camera)).setImageResource(
    //                        isCameraOn ? R.mipmap.ic_camera_on : R.mipmap.ic_camera_off);
    //                break;
    //            case R.id.iv_switch:
    //                Log.v(TAG, "switch->cur: " + ILiveRoomManager.getInstance().getActiveCameraId() + "/" + ILiveRoomManager.getInstance().getCurCameraId());
    //                if (ILiveConstants.NONE_CAMERA != ILiveRoomManager.getInstance().getActiveCameraId()) {
    //                    ILiveRoomManager.getInstance().switchCamera(1 - ILiveRoomManager.getInstance().getActiveCameraId());
    //                } else {
    //                    ILiveRoomManager.getInstance().switchCamera(ILiveConstants.FRONT_CAMERA);
    //                }
    //                break;
    //            case R.id.iv_flash:
    //                toggleFlash();
    //                break;
    //            case R.id.iv_mic:
    //                isMicOn = !isMicOn;
    //                ILiveRoomManager.getInstance().enableMic(isMicOn);
    //                ((ImageView) findViewById(R.id.iv_mic)).setImageResource(
    //                        isMicOn ? R.mipmap.ic_mic_on : R.mipmap.ic_mic_off);
    //                break;
    //            case R.id.iv_return:
    //                finish();
    //                break;
    //        }
    //    }
    //
    //    @Override
    //    public void onNewTextMsg(ILVText text, String SenderId, TIMUserProfile userProfile) {
    //        addMessage(SenderId, DemoFunc.getLimitString(text.getText(), Constants.MAX_SIZE));
    //    }
    //
    //    @Override
    //    public void onNewCustomMsg(ILVCustomCmd cmd, String id, TIMUserProfile userProfile) {
    //        switch (cmd.getCmd()) {
    //            case ILVLiveConstants.ILVLIVE_CMD_LINKROOM_REQ:     // 跨房邀请
    //                linkRoomReq(id);
    //                break;
    //        }
    //    }
    //
    //    @Override
    //    public void onNewOtherMsg(TIMMessage message) {
    //
    //    }
    //
    //    @Override
    //    public void onForceOffline(int error, String message) {
    //        finish();
    //    }
    //
    //    private Context getContenxt() {
    //        return ClipDollDetailActivity.this;
    //    }
    //
    //    // 添加消息
    //    private void addMessage(String sender, String msg) {
    //        strMsg += "[" + sender + "]  " + msg + "\n";
    //        tvMsg.setText(strMsg);
    //        svScroll.fullScroll(View.FOCUS_DOWN);
    //    }
    //
    //    private void joinRoom() {
    //        final int roomId = DemoFunc.getIntValue(etRoom.getText().toString(), -1);
    //        if (-1 == roomId) {
    //            DlgMgr.showMsg(getContenxt(), getString(R.string.str_tip_num_error));
    //            return;
    //        }
    //        ILVLiveRoomOption option = new ILVLiveRoomOption("")
    //                .autoCamera(ILiveConstants.NONE_CAMERA != ILiveRoomManager.getInstance().getActiveCameraId())
    //                .videoMode(ILiveConstants.VIDEOMODE_NORMAL)
    //                .controlRole(Constants.ROLE_LIVEGUEST)
    //                .autoFocus(true);
    //        ILVLiveManager.getInstance().joinRoom(roomId, option, new ILiveCallBack() {
    //            @Override
    //            public void onSuccess(Object data) {
    //                afterCreate();
    //            }
    //
    //            @Override
    //            public void onError(String module, int errCode, String errMsg) {
    //                DlgMgr.showMsg(getContenxt(), "create failed:" + module + "|" + errCode + "|" + errMsg);
    //            }
    //        });
    //    }
    //
    //    private void showChoiceDlg() {
    //        AlertDialog.Builder builder = new AlertDialog.Builder(this)
    //                .setTitle("提示")
    //                .setMessage("房间已存在，是否加入房间？")
    //                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
    //                    @Override
    //                    public void onClick(DialogInterface dialogInterface, int i) {
    //                        dialogInterface.dismiss();
    //                    }
    //                })
    //                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
    //                    @Override
    //                    public void onClick(DialogInterface dialogInterface, int i) {
    //                        joinRoom();
    //                        dialogInterface.dismiss();
    //                    }
    //                });
    //        DlgMgr.showAlertDlg(this, builder);
    //    }
    //
    //    // 加入房间
    //    private void createRoom() {
    //        final int roomId = DemoFunc.getIntValue(etRoom.getText().toString(), -1);
    //        if (-1 == roomId) {
    //            DlgMgr.showMsg(getContenxt(), getString(R.string.str_tip_num_error));
    //            return;
    //        }
    //        ILVLiveRoomOption option = new ILVLiveRoomOption(ILiveLoginManager.getInstance().getMyUserId())
    //                .autoCamera(ILiveConstants.NONE_CAMERA != ILiveRoomManager.getInstance().getActiveCameraId())
    //                .videoMode(ILiveConstants.VIDEOMODE_NORMAL)
    //                .controlRole(Constants.ROLE_MASTER)
    //                .autoFocus(true);
    //        ILVLiveManager.getInstance().createRoom(roomId,
    //                option, new ILiveCallBack() {
    //                    @Override
    //                    public void onSuccess(Object data) {
    //                        afterCreate();
    //                    }
    //
    //                    @Override
    //                    public void onError(String module, int errCode, String errMsg) {
    //                        if (module.equals(ILiveConstants.Module_IMSDK) && 10021 == errCode) {
    //                            // 被占用，改加入
    //                            showChoiceDlg();
    //                        } else {
    //                            DlgMgr.showMsg(getContenxt(), "create failed:" + module + "|" + errCode + "|" + errMsg);
    //                        }
    //                    }
    //                });
    //    }
    //
    //    private void afterCreate() {
    //        UserInfo.getInstance().setRoom(ILiveRoomManager.getInstance().getRoomId());
    //        UserInfo.getInstance().writeToCache(this);
    //        etRoom.setEnabled(false);
    //        findViewById(R.id.tv_create).setVisibility(View.INVISIBLE);
    //        findViewById(R.id.iv_camera).setVisibility(View.VISIBLE);
    //        findViewById(R.id.iv_flash).setVisibility(View.VISIBLE);
    //        findViewById(R.id.iv_mic).setVisibility(View.VISIBLE);
    //    }
    //
    //    private void toggleFlash() {
    //        if (ILiveConstants.BACK_CAMERA != ILiveRoomManager.getInstance().getActiveCameraId()) {
    //            return;
    //        }
    //        AVVideoCtrl videoCtrl = ILiveSDK.getInstance().getAvVideoCtrl();
    //        if (null == videoCtrl) {
    //            return;
    //        }
    //
    //        final Object cam = videoCtrl.getCamera();
    //        if ((cam == null) || (!(cam instanceof Camera))) {
    //            return;
    //        }
    //        final Camera.Parameters camParam = ((Camera) cam).getParameters();
    //        if (null == camParam) {
    //            return;
    //        }
    //
    //        Object camHandler = videoCtrl.getCameraHandler();
    //        if ((camHandler == null) || (!(camHandler instanceof Handler))) {
    //            return;
    //        }
    //
    //        //对摄像头的操作放在摄像头线程
    //        if (isFlashOn == false) {
    //            ((Handler) camHandler).post(new Runnable() {
    //                public void run() {
    //                    try {
    //                        camParam.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
    //                        ((Camera) cam).setParameters(camParam);
    //                        isFlashOn = true;
    //                    } catch (RuntimeException e) {
    //                        Log.d(TAG, "setParameters->RuntimeException");
    //                    }
    //                }
    //            });
    //        } else {
    //            ((Handler) camHandler).post(new Runnable() {
    //                public void run() {
    //                    try {
    //                        camParam.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
    //                        ((Camera) cam).setParameters(camParam);
    //                        isFlashOn = false;
    //                    } catch (RuntimeException e) {
    //                        Log.d(TAG, "setParameters->RuntimeException");
    //                    }
    //                }
    //            });
    //        }
    //    }
    //
    //    // 拒绝跨房连麦
    //    private void refuseLink(String id) {
    //        ILVLiveManager.getInstance().refuseLinkRoom(id, null);
    //    }
    //
    //    // 同意跨房连麦
    //    private void acceptLink(String id) {
    //        ILVLiveManager.getInstance().acceptLinkRoom(id, null);
    //    }
    //
    //    private void linkRoomReq(final String id) {
    //        AlertDialog.Builder builder = new AlertDialog.Builder(this);
    //        builder.setTitle(R.string.live_title_link);
    //        builder.setMessage("[" + id + "]" + getString(R.string.link_req_tips));
    //        builder.setNegativeButton(R.string.str_btn_refuse, new DialogInterface.OnClickListener() {
    //            @Override
    //            public void onClick(DialogInterface dialogInterface, int i) {
    //                refuseLink(id);
    //            }
    //        });
    //        builder.setPositiveButton(R.string.str_btn_agree, new DialogInterface.OnClickListener() {
    //            @Override
    //            public void onClick(DialogInterface dialogInterface, int i) {
    //                acceptLink(id);
    //            }
    //        });
    //        DlgMgr.showAlertDlg(this, builder);
    //    }

    private void initTimerTask() {
        mTask = new TimerTask() {
            @Override
            public void run() {
                ClipDollDetailActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        --mTotalTime;
                        tv_timer.setText(String.valueOf(mTotalTime) + "s");
                        if (mTotalTime <= 9) {
                            tv_timer.setTextSize(40);
                        }
                        if (mTotalTime <= 0) {
                            mTimer.cancel();
                            //不激活操作按钮
                            action_start_clip.setEnabled(false);
                            action_btn_left.setEnabled(false);
                            action_btn_top.setEnabled(false);
                            action_btn_right.setEnabled(false);
                            action_btn_bottom.setEnabled(false);
                            //弹出结果对话框
                            showResultDialog();
                        }
                    }
                });
            }
        };
    }

    private void showResultDialog() {
        //        View view = View.inflate(ClipDollDetailActivity.this, R.layout.dialog_clip_doll_result_yes, null);
        //        AlertDialog.Builder builder = new AlertDialog.Builder(ClipDollDetailActivity.this, R.style.dialog);
        //        builder.setView(view);
        //        builder.create().show();

        TransparentDialog transparentDialog = new TransparentDialog(this, R.style.dialog);//创建Dialog并设置样式主题
        transparentDialog.setCanceledOnTouchOutside(true);//设置点击Dialog外部任意区域关闭Dialog
        transparentDialog.show();
    }
}
