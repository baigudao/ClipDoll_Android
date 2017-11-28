package com.happy.moment.clip.doll.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.happy.moment.clip.doll.R;
import com.happy.moment.clip.doll.activity.EmptyActivity;
import com.happy.moment.clip.doll.bean.AddressBean;
import com.happy.moment.clip.doll.bean.ClipDollRecordBean;
import com.happy.moment.clip.doll.bean.HomeRoomBean;
import com.happy.moment.clip.doll.bean.LiveRoomLuckyUserBean;
import com.happy.moment.clip.doll.bean.MessageNotificationBean;
import com.happy.moment.clip.doll.bean.SpendCoinRecordBean;
import com.happy.moment.clip.doll.bean.WaitingSendBean;
import com.happy.moment.clip.doll.fragment.NewAddAddressFragment;
import com.happy.moment.clip.doll.util.Constants;
import com.happy.moment.clip.doll.util.DataManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Devin on 2017/11/17 18:22
 * E-mail:971060378@qq.com
 */

public class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewAdapter.ViewHolder> {

    private static final int HOME_ROOM_LIST_DATA_TYPE = 1;
    private static final int CLIP_DOLL_RECORD_DATA_TYPE = 2;
    private static final int CLIP_DOLL_RECORD_LIVE_DATA_TYPE = 3;
    private static final int SPEND_COIN_RECORD_DATA_TYPE = 4;
    private static final int WAITING_SEND_DATA_TYPE = 5;
    private static final int ADDRESS_LIST_DATA_TYPE = 6;
    private static final int NOTIFICATION_CENTER_DATA_TYPE = 16;

    private Context mContext;
    private int dataType;
    private List<T> mList;

    public BaseRecyclerViewAdapter(Context context, List<T> tList, int dataType) {
        this.mContext = context;
        this.mList = tList;
        this.dataType = dataType;
    }

    /**
     * 添加数据集合
     *
     * @param dataList
     */
    public void addDatas(List<T> dataList) {
        if (mList == null) {
            mList = new ArrayList<>();
        }
        mList.addAll(dataList);
        notifyDataSetChanged();
    }

    @Override
    public BaseRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder;
        switch (dataType) {
            case NOTIFICATION_CENTER_DATA_TYPE:
                viewHolder = new ViewHolder(View.inflate(mContext, R.layout.item_view_notification_center, null));
                break;
            case HOME_ROOM_LIST_DATA_TYPE:
                viewHolder = new ViewHolder(View.inflate(mContext, R.layout.item_view_home_room, null));
                break;
            case CLIP_DOLL_RECORD_DATA_TYPE:
                viewHolder = new ViewHolder(View.inflate(mContext, R.layout.item_view_clip_doll_record, null));
                break;
            case CLIP_DOLL_RECORD_LIVE_DATA_TYPE:
                viewHolder = new ViewHolder(View.inflate(mContext, R.layout.item_view_live_clip_doll_record, null));
                break;
            case SPEND_COIN_RECORD_DATA_TYPE:
                viewHolder = new ViewHolder(View.inflate(mContext, R.layout.item_view_spend_coin_record, null));
                break;
            case WAITING_SEND_DATA_TYPE:
                viewHolder = new ViewHolder(View.inflate(mContext, R.layout.item_view_waiting_send, null));
                break;
            case ADDRESS_LIST_DATA_TYPE:
                viewHolder = new ViewHolder(View.inflate(mContext, R.layout.item_view_address, null));
                break;
            default:
                viewHolder = null;
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final BaseRecyclerViewAdapter.ViewHolder holder, final int position) {
        switch (dataType) {
            case HOME_ROOM_LIST_DATA_TYPE:
                ArrayList<HomeRoomBean> homeRoomBeanArrayList = (ArrayList<HomeRoomBean>) mList;
                if (EmptyUtils.isNotEmpty(homeRoomBeanArrayList)) {
                    HomeRoomBean homeRoomBean = homeRoomBeanArrayList.get(position);
                    if (EmptyUtils.isNotEmpty(homeRoomBean)) {
                        Glide.with(mContext)
                                .load(homeRoomBean.getRoomPicUrl())
                                .placeholder(R.drawable.wawa_default)
                                .error(R.drawable.wawa_default)
                                .into(holder.iv_item1);
                        int roomState = homeRoomBean.getRoomState();
                        switch (roomState) {
                            case 0:
                                holder.tv_item1.setVisibility(View.GONE);
                                holder.tv_item2.setVisibility(View.VISIBLE);
                                break;
                            case 1:
                                holder.tv_item1.setVisibility(View.VISIBLE);
                                holder.tv_item2.setVisibility(View.GONE);
                                break;
                            default:
                                break;
                        }
                        holder.tv_item3.setText(homeRoomBean.getRoomName());
                        holder.tv_item4.setText(homeRoomBean.getGamePrice() + "币/次");
                    }
                }
                break;
            case CLIP_DOLL_RECORD_DATA_TYPE:
                ArrayList<ClipDollRecordBean> clipDollRecordBeanArrayList = (ArrayList<ClipDollRecordBean>) mList;
                if (EmptyUtils.isNotEmpty(clipDollRecordBeanArrayList)) {
                    ClipDollRecordBean clipDollRecordBean = clipDollRecordBeanArrayList.get(position);
                    if (EmptyUtils.isNotEmpty(clipDollRecordBean)) {
                        Glide.with(mContext)
                                .load(SPUtils.getInstance().getString(Constants.HEADIMG))
                                .placeholder(R.drawable.avatar)
                                .error(R.drawable.avatar)
                                .into(holder.iv_item1);
                        holder.tv_item1.setText(SPUtils.getInstance().getString(Constants.NICKNAME));
                        holder.tv_item2.setText(clipDollRecordBean.getCreateTime());
                        boolean result = clipDollRecordBean.isResult();
                        if (result) {
                            //抓取成功
                            holder.tv_item3.setText("抓取成功");
                            holder.tv_item3.setTextColor(mContext.getResources().getColor(R.color.seventh_text_color));
                        } else {
                            holder.tv_item3.setText("抓取失败");
                            holder.tv_item3.setTextColor(mContext.getResources().getColor(R.color.fifth_text_color));
                        }
                    }
                }
                break;
            case CLIP_DOLL_RECORD_LIVE_DATA_TYPE:
                ArrayList<LiveRoomLuckyUserBean> liveRoomLuckyUserBeanArrayList = (ArrayList<LiveRoomLuckyUserBean>) mList;
                if (EmptyUtils.isNotEmpty(liveRoomLuckyUserBeanArrayList)) {
                    LiveRoomLuckyUserBean liveRoomLuckyUserBean = liveRoomLuckyUserBeanArrayList.get(position);
                    if (EmptyUtils.isNotEmpty(liveRoomLuckyUserBean)) {
                        if (EmptyUtils.isNotEmpty(liveRoomLuckyUserBean.getUser())) {
                            Glide.with(mContext)
                                    .load(liveRoomLuckyUserBean.getUser().getHeadImg())
                                    .placeholder(R.drawable.avatar)
                                    .error(R.drawable.avatar)
                                    .into(holder.iv_item1);
                            holder.tv_item1.setText(liveRoomLuckyUserBean.getUser().getNickName());
                        }
                        holder.tv_item2.setText(liveRoomLuckyUserBean.getRecordTimeDesc());
                    }
                }
                break;
            case NOTIFICATION_CENTER_DATA_TYPE:
                ArrayList<MessageNotificationBean> messageNotificationBeanArrayList = (ArrayList<MessageNotificationBean>) mList;
                if (EmptyUtils.isNotEmpty(messageNotificationBeanArrayList)) {
                    MessageNotificationBean messageNotificationBean = messageNotificationBeanArrayList.get(position);
                    if (EmptyUtils.isNotEmpty(messageNotificationBean)) {
                        Glide.with(mContext)
                                .load(SPUtils.getInstance().getString(Constants.HEADIMG))
                                .placeholder(R.drawable.avatar)
                                .error(R.drawable.avatar)
                                .into(holder.iv_item1);
                        holder.tv_item1.setText(messageNotificationBean.getMessageContent());
                        holder.tv_item2.setText(TimeUtils.millis2String(messageNotificationBean.getCreateTime(), new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")));
                    }
                }
                break;
            case SPEND_COIN_RECORD_DATA_TYPE:
                final ArrayList<SpendCoinRecordBean> spendCoinRecordBeanArrayList = (ArrayList<SpendCoinRecordBean>) mList;
                if (EmptyUtils.isNotEmpty(spendCoinRecordBeanArrayList)) {
                    SpendCoinRecordBean spendCoinRecordBean = spendCoinRecordBeanArrayList.get(position);
                    if (EmptyUtils.isNotEmpty(spendCoinRecordBean)) {
                        int currentType = spendCoinRecordBean.getCurrentType();
                        switch (currentType) {
                            case 0://游戏消费
                                holder.tv_item1.setText("游戏消费");
                                break;
                            case 1:
                                holder.tv_item1.setText("微信充值");
                                break;
                            case 2:
                                holder.tv_item1.setText("充值奖励");
                                break;
                            case 3:
                                holder.tv_item1.setText("首充奖励");
                                break;
                            case 4:
                                holder.tv_item1.setText("活动奖励");
                                break;
                            case 5:
                                holder.tv_item1.setText("虚拟充值");
                                break;
                            case 8:
                                holder.tv_item1.setText("邀请奖励");
                                break;
                            default:
                                break;
                        }
                        holder.tv_item2.setText(spendCoinRecordBean.getDetails());
                        holder.tv_item3.setText(spendCoinRecordBean.getCreateTime());
                        int expendOrIncome = spendCoinRecordBean.getExpendOrIncome();
                        if (expendOrIncome == 0) {//支出
                            holder.tv_item4.setText(spendCoinRecordBean.getLqbAmount() + "");
                            holder.tv_item4.setTextColor(mContext.getResources().getColor(R.color.fifth_text_color));
                        } else if (expendOrIncome == 1) {
                            holder.tv_item4.setTextColor(mContext.getResources().getColor(R.color.seventh_text_color));
                            holder.tv_item4.setText("+" + spendCoinRecordBean.getLqbAmount());
                        }
                    }
                }
                break;
            case WAITING_SEND_DATA_TYPE:
                ArrayList<WaitingSendBean> waitingSendBeanArrayList = (ArrayList<WaitingSendBean>) mList;
                if (EmptyUtils.isNotEmpty(waitingSendBeanArrayList)) {
                    WaitingSendBean waitingSendBean = waitingSendBeanArrayList.get(position);
                    if (EmptyUtils.isNotEmpty(waitingSendBean)) {
                        Glide.with(mContext)
                                .load(waitingSendBean.getToyPicUrl())
                                .placeholder(R.drawable.avatar)
                                .error(R.drawable.avatar)
                                .into(holder.iv_item1);
                        holder.tv_item1.setText(waitingSendBean.getToyName());
                        holder.tv_item2.setText(waitingSendBean.getNum() + "个");
                    }
                }
                break;
            case ADDRESS_LIST_DATA_TYPE:
                final ArrayList<AddressBean> addressBeanArrayList = (ArrayList<AddressBean>) mList;
                if (EmptyUtils.isNotEmpty(addressBeanArrayList)) {
                    final AddressBean addressBean = addressBeanArrayList.get(position);
                    if (EmptyUtils.isNotEmpty(addressBean)) {
                        holder.tv_item1.setText(addressBean.getUserName());
                        holder.tv_item2.setText(addressBean.getPhone());
                        holder.tv_item3.setText(addressBean.getProvince() + "省" + addressBean.getCity() + "市" + addressBean.getStreet());

                        //渲染
                        int isDefault = addressBean.getIsDefault();
                        if (isDefault == 0) {
                            holder.iv_item1.setVisibility(View.VISIBLE);
                            holder.iv_item2.setVisibility(View.INVISIBLE);
                        } else if (isDefault == 1) {
                            holder.iv_item1.setVisibility(View.INVISIBLE);
                            holder.iv_item2.setVisibility(View.VISIBLE);
                        }

                        //设为默认地址
                        holder.iv_item1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                holder.iv_item1.setVisibility(View.INVISIBLE);
                                holder.iv_item2.setVisibility(View.VISIBLE);
                                //请求网络
                                OkHttpUtils.post()
                                        .url(Constants.getUserAddressSaveUrl())
                                        .addParams(Constants.SESSION, SPUtils.getInstance().getString(Constants.SESSION))
                                        .addParams("addressId", String.valueOf(addressBean.getAddressId()))
                                        .addParams("isDefault", String.valueOf(1))
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
                                                        if (success == 1) {
                                                            LogUtils.e("设为默认地址成功");
                                                        }
                                                    } else {
                                                        LogUtils.e("请求数据失败：" + msg + "-" + code + "-" + req);
                                                        ToastUtils.showShort("请求数据失败" + msg);
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                            }
                        });
                        holder.iv_item2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                holder.iv_item1.setVisibility(View.VISIBLE);
                                holder.iv_item2.setVisibility(View.INVISIBLE);
                                //请求网络
                                OkHttpUtils.post()
                                        .url(Constants.getUserAddressSaveUrl())
                                        .addParams(Constants.SESSION, SPUtils.getInstance().getString(Constants.SESSION))
                                        .addParams("addressId", String.valueOf(addressBean.getAddressId()))
                                        .addParams("isDefault", String.valueOf(0))
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
                                                        if (success == 1) {
                                                            LogUtils.e("取消默认地址成功");
                                                        }
                                                    } else {
                                                        LogUtils.e("请求数据失败：" + msg + "-" + code + "-" + req);
                                                        ToastUtils.showShort("请求数据失败" + msg);
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                            }
                        });

                        holder.linearLayout1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DataManager.getInstance().setData1("EDIT_TYPE");
                                DataManager.getInstance().setData2(addressBean);
                                ((EmptyActivity) mContext).gotoPager(NewAddAddressFragment.class, null);
                            }
                        });
                        holder.linearLayout2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                OkHttpUtils.post()
                                        .url(Constants.getUserAddressDeleteUrl())
                                        .addParams(Constants.SESSION, SPUtils.getInstance().getString(Constants.SESSION))
                                        .addParams("addressId", String.valueOf(addressBean.getAddressId()))
                                        .build()
                                        .execute(new StringCallback() {
                                            @Override
                                            public void onError(Call call, Exception e, int id) {
                                                LogUtils.e(e.toString());
                                            }

                                            @Override
                                            public void onResponse(String response, int id) {
                                                LogUtils.e(response);
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
                                                        if (success == 1) {
                                                            addressBeanArrayList.remove(position);
                                                            notifyDataSetChanged();
                                                        } else {
                                                            ToastUtils.showShort("删除失败");
                                                        }
                                                    } else {
                                                        LogUtils.e("请求数据失败：" + msg + "-" + code + "-" + req);
                                                        ToastUtils.showShort("请求数据失败" + msg);
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                            }
                        });
                    }
                }
                break;
            default:
                break;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private View itemView;

        private ImageView iv_item1;
        private ImageView iv_item2;
        private ImageView iv_item3;
        private ImageView iv_item4;
        private ImageView iv_item5;
        private ImageView iv_item6;
        private ImageView iv_item7;
        private ImageView iv_item8;
        private ImageView iv_item9;
        private ImageView iv_item10;
        private ImageView iv_item11;
        private ImageView iv_item12;
        private ImageView iv_item13;
        private ImageView iv_item14;

        private TextView tv_item1;
        private TextView tv_item2;
        private TextView tv_item3;
        private TextView tv_item4;
        private TextView tv_item5;
        private TextView tv_item6;
        private TextView tv_item7;
        private TextView tv_item8;

        private RecyclerView recyclerView;

        private LinearLayout linearLayout1;
        private LinearLayout linearLayout2;
        private LinearLayout linearLayout3;

        private RelativeLayout relativeLayout;

        private CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            switch (dataType) {
                case HOME_ROOM_LIST_DATA_TYPE:
                    iv_item1 = (ImageView) itemView.findViewById(R.id.iv_room);

                    tv_item1 = (TextView) itemView.findViewById(R.id.tv_state_playing);//游戏中
                    tv_item2 = (TextView) itemView.findViewById(R.id.tv_state_free);//空闲中
                    tv_item3 = (TextView) itemView.findViewById(R.id.tv_room_name);
                    tv_item4 = (TextView) itemView.findViewById(R.id.tv_cost_coin_num);

                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mOnItemClickListener != null) {
                                int position = getLayoutPosition();
                                mOnItemClickListener.onItemClick(mList.get(position), position);
                            }
                        }
                    });
                    break;
                case CLIP_DOLL_RECORD_DATA_TYPE:
                    iv_item1 = (ImageView) itemView.findViewById(R.id.iv_user_photo);

                    tv_item1 = (TextView) itemView.findViewById(R.id.tv_clip_doll_name);
                    tv_item2 = (TextView) itemView.findViewById(R.id.tv_clip_doll_time);
                    tv_item3 = (TextView) itemView.findViewById(R.id.tv_clip_doll_state);

                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mOnItemClickListener != null) {
                                int position = getLayoutPosition();
                                mOnItemClickListener.onItemClick(mList.get(position), position);
                            }
                        }
                    });
                    break;
                case CLIP_DOLL_RECORD_LIVE_DATA_TYPE:
                    iv_item1 = (ImageView) itemView.findViewById(R.id.iv_user_photo);

                    tv_item1 = (TextView) itemView.findViewById(R.id.tv_user_name);
                    tv_item2 = (TextView) itemView.findViewById(R.id.tv_clip_doll_time);

                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mOnItemClickListener != null) {
                                int position = getLayoutPosition();
                                mOnItemClickListener.onItemClick(mList.get(position), position);
                            }
                        }
                    });
                    break;
                case NOTIFICATION_CENTER_DATA_TYPE:
                    iv_item1 = (ImageView) itemView.findViewById(R.id.iv_user_photo);

                    tv_item1 = (TextView) itemView.findViewById(R.id.tv_notification_content);
                    tv_item2 = (TextView) itemView.findViewById(R.id.tv_notification_time);

                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mOnItemClickListener != null) {
                                int position = getLayoutPosition();
                                mOnItemClickListener.onItemClick(mList.get(position), position);
                            }
                        }
                    });
                    break;
                case SPEND_COIN_RECORD_DATA_TYPE:
                    tv_item1 = (TextView) itemView.findViewById(R.id.tv_recharge_name);//微信充值
                    tv_item2 = (TextView) itemView.findViewById(R.id.tv_recharge_coin);//充值游戏币：200币
                    tv_item3 = (TextView) itemView.findViewById(R.id.tv_recharge_time);//时间
                    tv_item4 = (TextView) itemView.findViewById(R.id.tv_recharge_coin_num);//充值多少

                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mOnItemClickListener != null) {
                                int position = getLayoutPosition();
                                mOnItemClickListener.onItemClick(mList.get(position), position);
                            }
                        }
                    });
                    break;
                case WAITING_SEND_DATA_TYPE:
                    iv_item1 = (ImageView) itemView.findViewById(R.id.iv_user_photo);

                    tv_item1 = (TextView) itemView.findViewById(R.id.tv_clip_doll_name);
                    tv_item2 = (TextView) itemView.findViewById(R.id.tv_clip_doll_num);

                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mOnItemClickListener != null) {
                                int position = getLayoutPosition();
                                mOnItemClickListener.onItemClick(mList.get(position), position);
                            }
                        }
                    });
                    break;
                case ADDRESS_LIST_DATA_TYPE:
                    tv_item1 = (TextView) itemView.findViewById(R.id.tv_address_name);//姓名
                    tv_item2 = (TextView) itemView.findViewById(R.id.tv_address_phone);//电话
                    tv_item3 = (TextView) itemView.findViewById(R.id.tv_address_place);//地址

                    iv_item1 = (ImageView) itemView.findViewById(R.id.iv_address_choose_no);//设置默认地址
                    iv_item2 = (ImageView) itemView.findViewById(R.id.iv_address_choose_yes);//设置默认地址

                    linearLayout1 = (LinearLayout) itemView.findViewById(R.id.ll_address_edit);
                    linearLayout2 = (LinearLayout) itemView.findViewById(R.id.ll_address_delete);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }


    /**
     * 接口定义三步曲
     */
    public interface OnItemClickListener {
        void onItemClick(Object data, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    /**
     * 接口定义三步曲,点击某个item中的局部view的回调
     */
    public interface OnItemViewClickListener {
        void onItemViewClick(Object data, int position);
    }

    private OnItemViewClickListener mOnItemViewClickListener;

    public void setOnItemViewClickListener(OnItemViewClickListener mOnItemViewClickListener) {
        this.mOnItemViewClickListener = mOnItemViewClickListener;
    }
}
