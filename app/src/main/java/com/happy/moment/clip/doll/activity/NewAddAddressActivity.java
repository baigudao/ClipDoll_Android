package com.happy.moment.clip.doll.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.happy.moment.clip.doll.R;
import com.happy.moment.clip.doll.bean.AddressBean;
import com.happy.moment.clip.doll.util.Constants;
import com.happy.moment.clip.doll.util.DataManager;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.citywheel.CityPickerView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by Devin on 2017/11/29 16:56
 * E-mail:971060378@qq.com
 */

public class NewAddAddressActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_province;
    private TextView tv_city;

    private EditText et_input_name;
    private EditText et_input_phone;
    private EditText et_input_address_detail;

    private Button btn_save;

    private String type;
    private AddressBean addressBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_add_address);

        initView();
        initData();
    }

    private void initView() {
        findViewById(R.id.ll_close).setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_bar_title)).setText("新增地址");
        findViewById(R.id.iv_share).setVisibility(View.GONE);

        tv_province = (TextView) findViewById(R.id.tv_province);
        tv_province.setOnClickListener(this);
        tv_province.addTextChangedListener(textWatcher);
        tv_city = (TextView) findViewById(R.id.tv_city);
        tv_city.setOnClickListener(this);
        tv_city.addTextChangedListener(textWatcher);

        btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);

        et_input_name = (EditText) findViewById(R.id.et_input_name);
        et_input_name.addTextChangedListener(textWatcher);
        et_input_phone = (EditText) findViewById(R.id.et_input_phone);
        et_input_phone.addTextChangedListener(textWatcher);
        et_input_address_detail = (EditText) findViewById(R.id.et_input_address_detail);
        et_input_address_detail.addTextChangedListener(textWatcher);
    }

    private String getName() {
        return et_input_name.getText().toString().trim();
    }

    private String getPhone() {
        return et_input_phone.getText().toString().trim();
    }

    private String getAddress() {
        return et_input_address_detail.getText().toString().trim();
    }

    private void initData() {
        type = (String) DataManager.getInstance().getData1();
        DataManager.getInstance().setData1(null);
        if (type.equals("NEW_ADD_TYPE")) {
            LogUtils.e("新增地址");
        } else if (type.equals("EDIT_TYPE")) {
            addressBean = (AddressBean) DataManager.getInstance().getData2();
            DataManager.getInstance().setData2(null);
            if (EmptyUtils.isNotEmpty(addressBean)) {
                et_input_name.setText(addressBean.getUserName());
                et_input_phone.setText(addressBean.getPhone());
                et_input_address_detail.setText(addressBean.getStreet());

                tv_province.setText(addressBean.getProvince());
                tv_city.setText(addressBean.getCity());
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_close:
                goBack();
                break;
            case R.id.tv_city:
            case R.id.tv_province:
                selectCity();
                break;
            case R.id.btn_save:
                if (type.equals("NEW_ADD_TYPE")) {
                    saveAddress();
                } else if (type.equals("EDIT_TYPE")) {
                    editAddress();
                }
                break;
            default:
                break;
        }
    }

    private void editAddress() {
        if (!RegexUtils.isMobileExact(getPhone())) {
            ToastUtils.showShort("请输入正确的手机号码");
            return;
        }
        OkHttpUtils.post()
                .url(Constants.getUserAddressSaveUrl())
                .addParams(Constants.SESSION, SPUtils.getInstance().getString(Constants.SESSION))
                .addParams("addressId", String.valueOf(addressBean.getAddressId()))
                .addParams("userName", getName())
                .addParams("phone", getPhone())
                .addParams("province", tv_province.getText().toString())
                .addParams("city", tv_city.getText().toString())
                .addParams("street", getAddress())
                .addParams("isDefault", String.valueOf(addressBean.getIsDefault()))
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
                                    setResult(RESULT_OK);
                                    finish();
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

    private void saveAddress() {
        if (!RegexUtils.isMobileExact(getPhone())) {
            ToastUtils.showShort("请输入正确的手机号码");
            return;
        }
        OkHttpUtils.post()
                .url(Constants.getUserAddressSaveUrl())
                .addParams(Constants.SESSION, SPUtils.getInstance().getString(Constants.SESSION))
                .addParams("userName", getName())
                .addParams("phone", getPhone())
                .addParams("province", tv_province.getText().toString())
                .addParams("city", tv_city.getText().toString())
                .addParams("street", getAddress())
                .addParams("isDefault", String.valueOf(0))// 0.非默认 ; 1.设为默认地址
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
                                    setResult(RESULT_OK);
                                    finish();
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

    private void selectCity() {
        //详细属性设置，如果不需要自定义样式的话，可以直接使用默认的，去掉下面的属性设置，直接build()即可。
        CityConfig cityConfig = new CityConfig.Builder(this)
                .title("选择省市")
                .titleBackgroundColor("#E9E9E9")
                .textSize(18)
                .titleTextColor("#585858")
                //                        .textColor("#ff5d5d")
                .confirTextColor("#333333")//确定的颜色
                .cancelTextColor("#979797")//取消的颜色
                .province("广东")
                .city("深圳")
                .district("南山区")
                .visibleItemsCount(8)
                .provinceCyclic(true)
                .cityCyclic(true)
                .districtCyclic(true)
                .showBackground(true)
                .itemPadding(5)
                .setCityInfoType(CityConfig.CityInfoType.BASE)
                .setCityWheelType(CityConfig.WheelType.PRO_CITY)
                .build();

        CityPickerView cityPicker = new CityPickerView(cityConfig);
        cityPicker.show();
        cityPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
            @Override
            public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                super.onSelected(province, city, district);
                tv_province.setText(province.getName());
                tv_city.setText(city.getName());
            }

            @Override
            public void onCancel() {
                super.onCancel();
            }
        });
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            boolean name = et_input_name.getText().toString().length() > 0;
            boolean phone = et_input_phone.getText().toString().length() > 0;
            boolean address_detail = et_input_address_detail.getText().toString().length() > 0;

            if (name && phone && address_detail) {
                btn_save.setEnabled(true);
            } else {
                btn_save.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
