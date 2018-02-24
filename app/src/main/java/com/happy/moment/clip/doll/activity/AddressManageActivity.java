package com.happy.moment.clip.doll.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.happy.moment.clip.doll.R;
import com.happy.moment.clip.doll.bean.AddressBean;
import com.happy.moment.clip.doll.util.Constants;
import com.happy.moment.clip.doll.util.DataManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Call;

/**
 * Created by Devin on 2017/11/29 16:31
 * E-mail:971060378@qq.com
 */

public class AddressManageActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView recyclerView;

    private LinearLayout ll_no_data;

    private static final int REQUEST_CODE = 22;
    private ArrayList<AddressBean> addressBeanArrayList;
    private String apply_send;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setStatusBarColor(AddressManageActivity.this, getResources().getColor(R.color.new_background_color));
        BarUtils.hideNavBar(AddressManageActivity.this);
        setContentView(R.layout.activity_address_manage);

        initView();
        initData();
    }

    private void initData() {
        apply_send = (String) DataManager.getInstance().getData1();
        DataManager.getInstance().setData1(null);
        getDataFromNet();
    }

    private void initView() {
        findViewById(R.id.ll_close).setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_bar_title)).setText("地址管理");
        findViewById(R.id.iv_share).setVisibility(View.GONE);

        Button btn_new_add_address = (Button) findViewById(R.id.btn_new_add_address);
        btn_new_add_address.setOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        ll_no_data = (LinearLayout) findViewById(R.id.ll_no_data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_close:
                goBack();
                break;
            case R.id.btn_new_add_address:
                DataManager.getInstance().setData1("NEW_ADD_TYPE");
                startActivityForResult(new Intent(AddressManageActivity.this, NewAddAddressActivity.class), REQUEST_CODE);
                break;
            default:
                break;
        }
    }

    private void getDataFromNet() {
        OkHttpUtils.get()
                .url(Constants.getUserAddressListUrl())
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
                                handlerDataForAddressList(jsonObjectResBody);
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

    private void handlerDataForAddressList(JSONObject jsonObjectResBody) {
        if (EmptyUtils.isNotEmpty(jsonObjectResBody)) {
            JSONArray jsonArray = jsonObjectResBody.optJSONArray("addressList");
            if (jsonArray.length() > 0) {
                ll_no_data.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                Gson gson = new Gson();
                addressBeanArrayList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<AddressBean>>() {
                }.getType());
                if (EmptyUtils.isNotEmpty(addressBeanArrayList) && addressBeanArrayList.size() != 0) {
                    AddressManageRecyclerViewAdapter addressManageRecyclerViewAdapter = new AddressManageRecyclerViewAdapter();
                    recyclerView.setAdapter(addressManageRecyclerViewAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(AddressManageActivity.this, LinearLayoutManager.VERTICAL, false));
                }
            } else {
                //没有数据
                ll_no_data.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            getDataFromNet();
        }
    }

    private class AddressManageRecyclerViewAdapter extends RecyclerView.Adapter<AddressManageRecyclerViewAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = View.inflate(AddressManageActivity.this, R.layout.item_view_address, null);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final AddressBean addressBean = addressBeanArrayList.get(position);
            if (EmptyUtils.isNotEmpty(addressBean)) {
                holder.tv_address_name.setText(addressBean.getUserName());
                holder.tv_address_phone.setText(addressBean.getPhone());
                holder.tv_address_place.setText(addressBean.getProvince() + "省" + addressBean.getCity() + "市" + addressBean.getStreet());

                //渲染
                int isDefault = addressBean.getIsDefault();
                if (isDefault == 0) {
                    holder.iv_address_choose_no.setVisibility(View.VISIBLE);
                    holder.iv_address_choose_yes.setVisibility(View.INVISIBLE);
                } else if (isDefault == 1) {
                    holder.iv_address_choose_no.setVisibility(View.INVISIBLE);
                    holder.iv_address_choose_yes.setVisibility(View.VISIBLE);
                }

                //设为默认地址
                holder.iv_address_choose_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
                                                    for (int i = 0; i < getItemCount(); i++) {
                                                        recyclerView.getChildAt(i).findViewById(R.id.iv_address_choose_no).setVisibility(View.VISIBLE);
                                                        recyclerView.getChildAt(i).findViewById(R.id.iv_address_choose_yes).setVisibility(View.INVISIBLE);
                                                    }
                                                    recyclerView.getChildAt(position).findViewById(R.id.iv_address_choose_yes).setVisibility(View.VISIBLE);
                                                    recyclerView.getChildAt(position).findViewById(R.id.iv_address_choose_no).setVisibility(View.INVISIBLE);
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
                holder.iv_address_choose_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
                                                    for (int i = 0; i < getItemCount(); i++) {
                                                        recyclerView.getChildAt(i).findViewById(R.id.iv_address_choose_no).setVisibility(View.VISIBLE);
                                                        recyclerView.getChildAt(i).findViewById(R.id.iv_address_choose_yes).setVisibility(View.INVISIBLE);
                                                    }
                                                    recyclerView.getChildAt(position).findViewById(R.id.iv_address_choose_yes).setVisibility(View.INVISIBLE);
                                                    recyclerView.getChildAt(position).findViewById(R.id.iv_address_choose_no).setVisibility(View.VISIBLE);
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

                holder.ll_address_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DataManager.getInstance().setData1("EDIT_TYPE");
                        DataManager.getInstance().setData2(addressBean);
                        startActivityForResult(new Intent(AddressManageActivity.this, NewAddAddressActivity.class), REQUEST_CODE);
                    }
                });

                //删除
                holder.ll_address_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddressManageActivity.this, R.style.AlertDialog_Logout);
                        View view = View.inflate(AddressManageActivity.this, R.layout.dialog_delete_address_view, null);
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
                        view.findViewById(R.id.btn_make_sure).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                OkHttpUtils.post()
                                        .url(Constants.getUserAddressDeleteUrl())
                                        .addParams(Constants.SESSION, SPUtils.getInstance().getString(Constants.SESSION))
                                        .addParams(Constants.ADDRESSID, String.valueOf(addressBean.getAddressId()))
                                        .build()
                                        .execute(new StringCallback() {
                                            @Override
                                            public void onError(Call call, Exception e, int id) {
                                                LogUtils.e(e.toString());
                                                alertDialog.dismiss();
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
                                                            alertDialog.dismiss();
                                                            addressBeanArrayList.remove(position);
                                                            notifyDataSetChanged();
                                                            if (addressBeanArrayList.size() == 0) {
                                                                getDataFromNet();
                                                            }
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
                });

                //在选地址的条件下
                if (EmptyUtils.isNotEmpty(apply_send) && apply_send.equals("APPLY_SEND")) {
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DataManager.getInstance().setData1("APPLY_SEND_RESULT");
                            DataManager.getInstance().setData2(addressBean.getAddressId());
                            DataManager.getInstance().setData3((addressBean.getProvince() + "省" + addressBean.getCity() + "市" + addressBean.getStreet() +
                                    "\n" + addressBean.getUserName() + "  " + addressBean.getPhone()));
                            goBack();
                        }
                    });
                }
            }
        }

        @Override
        public int getItemCount() {
            return addressBeanArrayList == null ? 0 : addressBeanArrayList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private TextView tv_address_name;
            private TextView tv_address_phone;
            private TextView tv_address_place;

            private ImageView iv_address_choose_no;
            private ImageView iv_address_choose_yes;

            private LinearLayout ll_address_edit;
            private LinearLayout ll_address_delete;

            ViewHolder(View itemView) {
                super(itemView);
                tv_address_name = (TextView) itemView.findViewById(R.id.tv_address_name);//姓名
                tv_address_phone = (TextView) itemView.findViewById(R.id.tv_address_phone);//电话
                tv_address_place = (TextView) itemView.findViewById(R.id.tv_address_place);//地址

                iv_address_choose_no = (ImageView) itemView.findViewById(R.id.iv_address_choose_no);//设置默认地址
                iv_address_choose_yes = (ImageView) itemView.findViewById(R.id.iv_address_choose_yes);//设置默认地址

                ll_address_edit = (LinearLayout) itemView.findViewById(R.id.ll_address_edit);
                ll_address_delete = (LinearLayout) itemView.findViewById(R.id.ll_address_delete);
            }
        }
    }
}
