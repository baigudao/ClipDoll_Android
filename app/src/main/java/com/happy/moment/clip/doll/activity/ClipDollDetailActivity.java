package com.happy.moment.clip.doll.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.happy.moment.clip.doll.R;
import com.happy.moment.clip.doll.adapter.BaseRecyclerViewAdapter;
import com.happy.moment.clip.doll.bean.LiveRoomLuckyUserBean;
import com.happy.moment.clip.doll.fragment.MyGameCoinFragment;
import com.happy.moment.clip.doll.util.Constants;
import com.happy.moment.clip.doll.view.TransparentDialog;
import com.tencent.av.sdk.AVRoomMulti;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.core.ILiveRoomManager;
import com.tencent.ilivesdk.view.AVRootView;
import com.tencent.ilivesdk.view.VideoListener;
import com.tencent.livesdk.ILVLiveManager;
import com.tencent.livesdk.ILVLiveRoomOption;
import com.tencent.wawasdk.TXWawaCallBack;
import com.tencent.wawasdk.TXWawaPlayer;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;

import static com.tencent.av.sdk.AVView.VIDEO_SRC_TYPE_CAMERA;


public class ClipDollDetailActivity extends BaseActivity implements View.OnClickListener, TXWawaPlayer.PlayerListener, View.OnTouchListener, AVRootView.onSubViewCreatedListener {

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
    private AVRootView arv_root;
    private TXWawaPlayer wawaPlayer;

    private boolean isFront;

    private ImageView iv_product_pic;

    private static final int CLIP_DOLL_RECORD_LIVE_DATA_TYPE = 3;
    private RecyclerView recyclerView_lucky;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setStatusBarColor(ClipDollDetailActivity.this, getResources().getColor(R.color.main_color));
        setContentView(R.layout.activity_clip_doll_detail);

        initView();
    }

    private void initView() {
        //屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //一屏显示
        int height = ScreenUtils.getScreenHeight() - BarUtils.getStatusBarHeight() - 50;
        RelativeLayout rl_fill_screen = (RelativeLayout) findViewById(R.id.rl_fill_screen);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) rl_fill_screen.getLayoutParams();
        layoutParams.height = height;
        rl_fill_screen.setLayoutParams(layoutParams);

        //头部视图
        findViewById(R.id.ll_close).setOnClickListener(this);
        findViewById(R.id.iv_share).setOnClickListener(this);

        //主要视图
        arv_root = (AVRootView) findViewById(R.id.arv_root);
        ILVLiveManager.getInstance().setAvVideoView(arv_root);
        arv_root.setAutoOrientation(false);
        isFront = true;
        //有画面之后的回调
        arv_root.setSubCreatedListener(this);

        //操作相关视图
        rl_start_clip_and_recharge = (RelativeLayout) findViewById(R.id.rl_start_clip_and_recharge);
        rl_operation = (RelativeLayout) findViewById(R.id.rl_operation);
        tv_cost_coin_num = (TextView) findViewById(R.id.tv_cost_coin_num);
        tv_coin_num = (TextView) findViewById(R.id.tv_coin_num);
        tv_start_clip_doll = (TextView) findViewById(R.id.tv_start_clip_doll);
        ll_start_clip_doll = (LinearLayout) findViewById(R.id.ll_start_clip_doll);
        ll_start_clip_doll.setOnClickListener(this);
        findViewById(R.id.ll_coin_recharge).setOnClickListener(this);
        tv_timer = (TextView) findViewById(R.id.tv_timer);
        findViewById(R.id.iv_live_room_camera).setOnClickListener(this);

        //上下左右下抓操作按钮视图
        action_btn_left = (ImageButton) findViewById(R.id.action_btn_left);
        action_btn_left.setOnTouchListener(this);
        action_btn_bottom = (ImageButton) findViewById(R.id.action_btn_bottom);
        action_btn_bottom.setOnTouchListener(this);
        action_btn_top = (ImageButton) findViewById(R.id.action_btn_top);
        action_btn_top.setOnTouchListener(this);
        action_btn_right = (ImageButton) findViewById(R.id.action_btn_right);
        action_btn_right.setOnTouchListener(this);
        action_start_clip = (ImageButton) findViewById(R.id.action_start_clip);
        action_start_clip.setOnClickListener(this);

        //底部视图的初始化
        recyclerView_lucky = (RecyclerView) findViewById(R.id.recyclerView_lucky);
        iv_product_pic = (ImageView) findViewById(R.id.iv_product_pic);

        //默认显示
        beforeStartGameView();
    }

    @Override
    protected void onStart() {
        super.onStart();

        joinRoom();

        //得到余额
        getBalanceCoin();
        //得到幸运儿
        getLuckyUsers();

        //渲染宝贝图片
        Glide.with(ClipDollDetailActivity.this)
                .load(SPUtils.getInstance().getString("toyPic"))
                .placeholder(R.drawable.wawa_default)
                .error(R.drawable.wawa_default)
                .into(iv_product_pic);
    }

    private void getBalanceCoin() {
        OkHttpUtils.post()
                .url(Constants.getUserBalance())
                .addParams(Constants.SESSION, SPUtils.getInstance().getString(Constants.SESSION))
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
                            JSONObject jsonObjectResHead = jsonObject.optJSONObject("resHead");
                            int code = jsonObjectResHead.optInt("code");
                            String msg = jsonObjectResHead.optString("msg");
                            String req = jsonObjectResHead.optString("req");
                            JSONObject jsonObjectResBody = jsonObject.optJSONObject("resBody");
                            if (code == 1) {
                                int balance = jsonObjectResBody.optInt("balance");
                                tv_coin_num.setText(String.valueOf(balance));
                            } else {
                                LogUtils.e("请求数据失败：" + msg + "-" + code + "-" + req);
                                ToastUtils.showShort("请求数据失败:" + msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void getLuckyUsers() {
        //获取抓取记录，幸运儿
        OkHttpUtils.post()
                .url(Constants.getClipDollRecordUrl())
                .addParams(Constants.GROUPID, SPUtils.getInstance().getString("groupId"))
                .addParams(Constants.PAGENUM, "1")
                .addParams(Constants.PAGESIZE, "10")
                .addParams(Constants.RESULT, "1")
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
                            JSONObject jsonObjectResHead = jsonObject.optJSONObject("resHead");
                            int code = jsonObjectResHead.optInt("code");
                            String msg = jsonObjectResHead.optString("msg");
                            String req = jsonObjectResHead.optString("req");
                            JSONObject jsonObjectResBody = jsonObject.optJSONObject("resBody");
                            if (code == 1) {
                                handlerDataForLuckyUsers(jsonObjectResBody);
                            } else {
                                LogUtils.e("请求数据失败：" + msg + "-" + code + "-" + req);
                                ToastUtils.showShort("请求数据失败:" + msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void handlerDataForLuckyUsers(JSONObject jsonObjectResBody) {
        if (EmptyUtils.isNotEmpty(jsonObjectResBody)) {
            JSONArray jsonArrayForLuckyUsers = jsonObjectResBody.optJSONArray("pageData");
            if (EmptyUtils.isNotEmpty(jsonArrayForLuckyUsers)) {
                Gson gson = new Gson();
                ArrayList<LiveRoomLuckyUserBean> liveRoomLuckyUserBeanArrayList = gson.fromJson(jsonArrayForLuckyUsers.toString()
                        , new TypeToken<ArrayList<LiveRoomLuckyUserBean>>() {
                        }.getType());
                if (EmptyUtils.isNotEmpty(liveRoomLuckyUserBeanArrayList) && liveRoomLuckyUserBeanArrayList.size() != 0) {
                    BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(ClipDollDetailActivity.this, liveRoomLuckyUserBeanArrayList, CLIP_DOLL_RECORD_LIVE_DATA_TYPE);
                    recyclerView_lucky.setAdapter(baseRecyclerViewAdapter);
                    recyclerView_lucky.setLayoutManager(new LinearLayoutManager(ClipDollDetailActivity.this, LinearLayoutManager.VERTICAL, false));
                }
            }
        }
    }

    private void joinRoom() {
        //加入房间配置项
        ILVLiveRoomOption memberOption = new ILVLiveRoomOption(SPUtils.getInstance().getString("frontPushId"))
                .autoCamera(false) //是否自动打开摄像头
                .controlRole("Guest") //角色设置
                .authBits(AVRoomMulti.AUTH_BITS_JOIN_ROOM | AVRoomMulti.AUTH_BITS_RECV_AUDIO |
                        AVRoomMulti.AUTH_BITS_RECV_CAMERA_VIDEO | AVRoomMulti.AUTH_BITS_RECV_SCREEN_VIDEO) //权限设置
                .videoRecvMode(AVRoomMulti.VIDEO_RECV_MODE_SEMI_AUTO_RECV_CAMERA_VIDEO) //是否开始半自动接收
                .autoMic(false);//是否自动打开mic

        //加入房间
        ILVLiveManager.getInstance().joinRoom(Integer.parseInt(SPUtils.getInstance().getString("groupId")), memberOption, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                //加入房间成功
                ILiveRoomManager.getInstance().enableSpeaker(false);
                ILiveRoomManager.getInstance().enableMic(false);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                //加入房间失败
                LogUtils.e("加入房间失败" + module + errMsg + errCode);
                if (errCode == 1003) {

                } else {
                    ToastUtils.showShort("加入房间失败");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_close:
                goBack();
                break;
            case R.id.iv_live_room_camera:
                //这里切换摄像头
                if (isFront) {
                    arv_root.swapVideoView(0, 1);// 与大屏交换
                    isFront = false;
                } else {
                    arv_root.swapVideoView(1, 0);// 与大屏交换
                    isFront = true;
                }
                break;
            case R.id.ll_start_clip_doll:
                //请求开始游戏接口
                requestBeginGame();
                break;
            case R.id.ll_coin_recharge:
                gotoPager(MyGameCoinFragment.class, null);
                break;
            case R.id.action_start_clip:
                //下抓
                wawaPlayer.controlClaw(TXWawaPlayer.CTL_CATCH);
                startClipDollView();
                break;
            case R.id.iv_share:
                //gameOver();
                break;
            default:
                break;
        }
    }

    private void requestBeginGame() {
        OkHttpUtils.post()
                .url(Constants.getApplyBeginGame())
                .addParams(Constants.SESSION, SPUtils.getInstance().getString(Constants.SESSION))
                .addParams(Constants.GROUPID, SPUtils.getInstance().getString("groupId"))
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
                            JSONObject jsonObjectResHead = jsonObject.optJSONObject("resHead");
                            int code = jsonObjectResHead.optInt("code");
                            String msg = jsonObjectResHead.optString("msg");
                            String req = jsonObjectResHead.optString("req");
                            JSONObject jsonObjectResBody = jsonObject.optJSONObject("resBody");
                            if (code == 1) {
                                int success = jsonObjectResBody.optInt("success");
                                switch (success) {
                                    case 0:
                                        String alertMsg = jsonObjectResBody.optString("alertMsg");
                                        ToastUtils.showShort(alertMsg);
                                        break;
                                    case 1:
                                        final String wsUrl = jsonObjectResBody.optString("wsUrl");
                                        final int playId = jsonObjectResBody.optInt("playId");
                                        wawaPlayer = new TXWawaPlayer(ClipDollDetailActivity.this);
                                        //playId: 唯一标识一次游戏;wsUrl: 需要从业务后台服务器获取
                                        wawaPlayer.startQueue(String.valueOf(playId), wsUrl, new TXWawaCallBack() {
                                            @Override
                                            public void onSuccess(Object o) {
                                                LogUtils.e("success");
                                            }

                                            @Override
                                            public void onError(int i, String s) {
                                                LogUtils.e("error");
                                            }
                                        });
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                LogUtils.e("请求数据失败：" + msg + "-" + code + "-" + req);
                                ToastUtils.showShort("请求数据失败:" + msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //=====================================================================下面固定================
    private void beforeStartGameView() {
        rl_start_clip_and_recharge.setVisibility(View.VISIBLE);
        rl_operation.setVisibility(View.GONE);
        ll_start_clip_doll.setEnabled(true);
        tv_start_clip_doll.setTextColor(getResources().getColor(R.color.seventh_text_color));
        tv_cost_coin_num.setTextColor(getResources().getColor(R.color.seventh_text_color));
    }

    private void startGameShowView() {
        //显示操作按钮，隐藏开始抓取和充值按钮
        rl_start_clip_and_recharge.setVisibility(View.GONE);
        rl_operation.setVisibility(View.VISIBLE);
        action_btn_left.setEnabled(true);
        action_btn_bottom.setEnabled(true);
        action_btn_top.setEnabled(true);
        action_btn_right.setEnabled(true);
        action_start_clip.setEnabled(true);

        //显示倒计时
        mTotalTime = 30;
        mTimer = new Timer();
        initTimerTask();
        mTimer.schedule(mTask, 1000, 1000);

        tv_timer.setVisibility(View.VISIBLE);
    }

    /**
     * 不激活开始抓取按钮
     */
    private void showStartClipAndRecharge() {
        ll_start_clip_doll.setEnabled(false);
        tv_start_clip_doll.setTextColor(getResources().getColor(R.color.pure_white_color));
        tv_cost_coin_num.setTextColor(getResources().getColor(R.color.pure_white_color));
    }

    /**
     * 下抓
     */
    private void startClipDollView() {
        tv_timer.setVisibility(View.GONE);
        mTimer.cancel();
        //不激活操作按钮
        action_start_clip.setEnabled(false);
        action_btn_left.setEnabled(false);
        action_btn_top.setEnabled(false);
        action_btn_right.setEnabled(false);
        action_btn_bottom.setEnabled(false);
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

    @Override
    public void OnWait(int i) {
        LogUtils.e("OnWait");
    }

    @Override
    public void OnReady() {
        LogUtils.e("OnReady");
        wawaPlayer.startGame(new TXWawaCallBack<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                startGameShowView();
            }

            @Override
            public void onError(int i, String s) {
                LogUtils.e("开始游戏出错" + s);
            }
        });
    }

    @Override
    public void OnState(int i) {
        LogUtils.e("OnState");
    }

    @Override
    public void OnResult(boolean b) {
        if (b) {
            ToastUtils.showShort("恭喜你，抓到了娃娃");
            beforeStartGameView();
        } else {
            ToastUtils.showShort("很遗憾，没有抓到");
            beforeStartGameView();
        }
        wawaPlayer.quitGame();
    }

    @Override
    public void OnTime(int i) {
        LogUtils.e("OnTime");
    }

    @Override
    public void OnClose(int i, int i1, String s) {
        LogUtils.e("OnClose");
    }

    @Override
    public void OnControlRTT(int i) {
        LogUtils.e("OnControlRTT");
    }

    @Override
    public void onSubViewCreated() {
        if (arv_root.getViewByIndex(0) != null) { //主摄像头画面
            arv_root.getViewByIndex(0).setRotate(true);
            arv_root.getViewByIndex(0).setRotation(270);
        }
        if (arv_root.getViewByIndex(1) != null) { //副摄像头画面
            arv_root.getViewByIndex(1).setPosHeight(0);
            arv_root.getViewByIndex(1).setPosWidth(0);
            arv_root.getViewByIndex(1).setRotate(true);
            arv_root.getViewByIndex(1).setPosTop(350);
            arv_root.getViewByIndex(1).setRotation(270);
        }

        //统计:主摄像头
        arv_root.getViewByIndex(0).setVideoListener(new VideoListener() {
            @Override
            public void onFirstFrameRecved(int width, int height, int angle, String identifier) {
                LogUtils.e("onFirstFrameRecved");
            }

            @Override
            public void onHasVideo(String identifier, int srcType) {
                LogUtils.e("onHasVideo");
            }

            @Override
            public void onNoVideo(String identifier, int srcType) {
                LogUtils.e(identifier);
            }
        });

        //统计:副摄像头
        arv_root.getViewByIndex(1).setVideoListener(new VideoListener() {
            @Override
            public void onFirstFrameRecved(int width, int height, int angle, String identifier) {
                LogUtils.e("onFirstFrameRecved");
            }

            @Override
            public void onHasVideo(String identifier, int srcType) {
                LogUtils.e("onHasVideo");
            }

            @Override
            public void onNoVideo(String identifier, int srcType) {
                LogUtils.e(identifier);
            }
        });

        arv_root.bindIdAndView(0, VIDEO_SRC_TYPE_CAMERA, SPUtils.getInstance().getString("frontPushId"));
        arv_root.bindIdAndView(1, VIDEO_SRC_TYPE_CAMERA, SPUtils.getInstance().getString("sidePushId"));
    }

    //Activity的回调
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
        ILVLiveManager.getInstance().onDestory();
    }

    private void gameOver() {
        OkHttpUtils.post()
                .url(Constants.getGameOverUrl())
                .addParams(Constants.SESSION, SPUtils.getInstance().getString(Constants.SESSION))
                .addParams(Constants.GROUPID, SPUtils.getInstance().getString("groupId"))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e(e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.e(response);
                        ToastUtils.showShort("游戏结束");
                    }
                });
    }

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
                            startClipDollView();
                        }
                    }
                });
            }
        };
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (isFront) {
            switch (v.getId()) {
                case R.id.action_btn_left:
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        wawaPlayer.controlClaw(TXWawaPlayer.CTL_LEFT_START);
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        wawaPlayer.controlClaw(TXWawaPlayer.CTL_LEFT_END);
                    }
                    break;
                case R.id.action_btn_right:
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        wawaPlayer.controlClaw(TXWawaPlayer.CTL_RIGHT_START);
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        wawaPlayer.controlClaw(TXWawaPlayer.CTL_RIGHT_END);
                    }
                    break;
                case R.id.action_btn_top:
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        wawaPlayer.controlClaw(TXWawaPlayer.CTL_FORWARD_START);
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        wawaPlayer.controlClaw(TXWawaPlayer.CTL_FORWARD_END);
                    }
                    break;
                case R.id.action_btn_bottom:
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        wawaPlayer.controlClaw(TXWawaPlayer.CTL_BACKWARD_START);
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        wawaPlayer.controlClaw(TXWawaPlayer.CTL_BACKWARD_END);
                    }
                    break;
                default:
                    break;
            }
        } else {
            switch (v.getId()) {
                case R.id.action_btn_left:
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        wawaPlayer.controlClaw(TXWawaPlayer.CTL_BACKWARD_START);
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        wawaPlayer.controlClaw(TXWawaPlayer.CTL_BACKWARD_END);
                    }
                    break;
                case R.id.action_btn_right:
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        wawaPlayer.controlClaw(TXWawaPlayer.CTL_FORWARD_START);
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        wawaPlayer.controlClaw(TXWawaPlayer.CTL_FORWARD_END);
                    }
                    break;
                case R.id.action_btn_top:
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        wawaPlayer.controlClaw(TXWawaPlayer.CTL_LEFT_START);
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        wawaPlayer.controlClaw(TXWawaPlayer.CTL_LEFT_END);
                    }
                    break;
                case R.id.action_btn_bottom:
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        wawaPlayer.controlClaw(TXWawaPlayer.CTL_RIGHT_START);
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        wawaPlayer.controlClaw(TXWawaPlayer.CTL_RIGHT_END);
                    }
                    break;
                default:
                    break;
            }
        }
        return false;
    }
}
