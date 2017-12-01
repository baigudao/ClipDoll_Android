package com.happy.moment.clip.doll.fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.happy.moment.clip.doll.R;
import com.happy.moment.clip.doll.util.Constants;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by Devin on 2017/11/18 21:11
 * E-mail:971060378@qq.com
 */

public class InviteNumExchangeFragment extends BaseFragment {

    private Button btn_exchange;

    private EditText et_invite_num1;
    private EditText et_invite_num2;
    private EditText et_invite_num3;
    private EditText et_invite_num4;
    private EditText et_invite_num5;
    private EditText et_invite_num6;

    private int flag;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_invite_num_exchange;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.ll_close).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_bar_title)).setText("邀请码兑换");
        view.findViewById(R.id.iv_share).setVisibility(View.GONE);

        btn_exchange = (Button) view.findViewById(R.id.btn_exchange);
        btn_exchange.setOnClickListener(this);

        flag = 1;//1：输入；0：删除

        et_invite_num1 = (EditText) view.findViewById(R.id.et_invite_num1);
        et_invite_num1.addTextChangedListener(watcher);

        et_invite_num2 = (EditText) view.findViewById(R.id.et_invite_num2);
        handlerEditTextStatus(false, et_invite_num2);
        et_invite_num2.setOnClickListener(this);
        et_invite_num2.addTextChangedListener(watcher);


        et_invite_num3 = (EditText) view.findViewById(R.id.et_invite_num3);
        handlerEditTextStatus(false, et_invite_num3);
        et_invite_num3.setOnClickListener(this);
        et_invite_num3.addTextChangedListener(watcher);

        et_invite_num4 = (EditText) view.findViewById(R.id.et_invite_num4);
        handlerEditTextStatus(false, et_invite_num4);
        et_invite_num4.setOnClickListener(this);
        et_invite_num4.addTextChangedListener(watcher);

        et_invite_num5 = (EditText) view.findViewById(R.id.et_invite_num5);
        handlerEditTextStatus(false, et_invite_num5);
        et_invite_num5.setOnClickListener(this);
        et_invite_num5.addTextChangedListener(watcher);

        et_invite_num6 = (EditText) view.findViewById(R.id.et_invite_num6);
        handlerEditTextStatus(false, et_invite_num6);
        et_invite_num6.setOnClickListener(this);
        et_invite_num6.addTextChangedListener(watcher);

        //需求
        //1，开始输入前，不管点击哪个输入框，光标都显示在第一个
        //2，依次向下输入，中间可删除前面的输入
        //3，输入完成后，不管点击哪个输入框，光标都显示在最后，并且依次删除
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_close:
                goBack();
                break;
            case R.id.btn_exchange:
                exchangeInviteNum();
                break;
            case R.id.et_invite_num2:
            case R.id.et_invite_num3:
            case R.id.et_invite_num4:
            case R.id.et_invite_num5:
            case R.id.et_invite_num6:
                handlerEditTextStatus(true, et_invite_num1);
                break;
            default:
                break;
        }
    }

    private void exchangeInviteNum() {
        String invite_code = et_invite_num1.getText().toString().trim() +
                et_invite_num2.getText().toString().trim() +
                et_invite_num3.getText().toString().trim() +
                et_invite_num4.getText().toString().trim() +
                et_invite_num5.getText().toString().trim() +
                et_invite_num6.getText().toString().trim();
        OkHttpUtils.post()
                .url(Constants.getVerifyInviteUrl())
                .addParams(Constants.SESSION, SPUtils.getInstance().getString(Constants.SESSION))
                .addParams(Constants.FROMINVITECODE, "dr78by")
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

    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            switch (flag) {
                case 1:
                    if (et_invite_num1.getText().toString().length() == 1) {
                        handlerEditTextStatus(false, et_invite_num1);
                        handlerEditTextStatus(false, et_invite_num3);
                        handlerEditTextStatus(false, et_invite_num4);
                        handlerEditTextStatus(false, et_invite_num5);
                        handlerEditTextStatus(false, et_invite_num6);
                        et_invite_num2.setOnClickListener(null);
                        et_invite_num3.setOnClickListener(null);
                        et_invite_num4.setOnClickListener(null);
                        et_invite_num5.setOnClickListener(null);
                        et_invite_num6.setOnClickListener(null);
                        handlerEditTextStatus(true, et_invite_num2);
                    }
                    if (et_invite_num2.getText().toString().length() == 1) {
                        handlerEditTextStatus(false, et_invite_num1);
                        handlerEditTextStatus(false, et_invite_num2);
                        handlerEditTextStatus(false, et_invite_num4);
                        handlerEditTextStatus(false, et_invite_num5);
                        handlerEditTextStatus(false, et_invite_num6);
                        et_invite_num2.setOnClickListener(null);
                        et_invite_num3.setOnClickListener(null);
                        et_invite_num4.setOnClickListener(null);
                        et_invite_num5.setOnClickListener(null);
                        et_invite_num6.setOnClickListener(null);
                        handlerEditTextStatus(true, et_invite_num3);
                    }
                    if (et_invite_num3.getText().toString().length() == 1) {
                        handlerEditTextStatus(false, et_invite_num1);
                        handlerEditTextStatus(false, et_invite_num2);
                        handlerEditTextStatus(false, et_invite_num3);
                        handlerEditTextStatus(false, et_invite_num5);
                        handlerEditTextStatus(false, et_invite_num6);
                        et_invite_num2.setOnClickListener(null);
                        et_invite_num3.setOnClickListener(null);
                        et_invite_num4.setOnClickListener(null);
                        et_invite_num5.setOnClickListener(null);
                        et_invite_num6.setOnClickListener(null);
                        handlerEditTextStatus(true, et_invite_num4);
                    }
                    if (et_invite_num4.getText().toString().length() == 1) {
                        handlerEditTextStatus(false, et_invite_num1);
                        handlerEditTextStatus(false, et_invite_num2);
                        handlerEditTextStatus(false, et_invite_num3);
                        handlerEditTextStatus(false, et_invite_num4);
                        et_invite_num2.setOnClickListener(null);
                        et_invite_num3.setOnClickListener(null);
                        et_invite_num4.setOnClickListener(null);
                        et_invite_num5.setOnClickListener(null);
                        et_invite_num6.setOnClickListener(null);
                        handlerEditTextStatus(true, et_invite_num5);
                    }
                    if (et_invite_num5.getText().toString().length() == 1) {
                        handlerEditTextStatus(false, et_invite_num1);
                        handlerEditTextStatus(false, et_invite_num2);
                        handlerEditTextStatus(false, et_invite_num3);
                        handlerEditTextStatus(false, et_invite_num4);
                        handlerEditTextStatus(false, et_invite_num5);
                        et_invite_num2.setOnClickListener(null);
                        et_invite_num3.setOnClickListener(null);
                        et_invite_num4.setOnClickListener(null);
                        et_invite_num5.setOnClickListener(null);
                        et_invite_num6.setOnClickListener(null);
                        handlerEditTextStatus(true, et_invite_num6);
                        flag = 0;
                    }
                    break;
                case 0:
                    if (et_invite_num6.getText().toString().length() == 0) {
                        handlerEditTextStatus(false, et_invite_num1);
                        handlerEditTextStatus(false, et_invite_num2);
                        handlerEditTextStatus(false, et_invite_num3);
                        handlerEditTextStatus(false, et_invite_num4);
                        handlerEditTextStatus(false, et_invite_num6);
                        handlerEditTextStatus(true, et_invite_num5);
                    }
                    if (et_invite_num5.getText().toString().length() == 0) {
                        handlerEditTextStatus(false, et_invite_num1);
                        handlerEditTextStatus(false, et_invite_num2);
                        handlerEditTextStatus(false, et_invite_num3);
                        handlerEditTextStatus(false, et_invite_num5);
                        handlerEditTextStatus(false, et_invite_num6);
                        handlerEditTextStatus(true, et_invite_num4);
                    }
                    if (et_invite_num4.getText().toString().length() == 0) {
                        handlerEditTextStatus(false, et_invite_num1);
                        handlerEditTextStatus(false, et_invite_num2);
                        handlerEditTextStatus(false, et_invite_num4);
                        handlerEditTextStatus(false, et_invite_num5);
                        handlerEditTextStatus(false, et_invite_num6);
                        handlerEditTextStatus(true, et_invite_num3);
                    }
                    if (et_invite_num3.getText().toString().length() == 0) {
                        handlerEditTextStatus(false, et_invite_num1);
                        handlerEditTextStatus(false, et_invite_num3);
                        handlerEditTextStatus(false, et_invite_num4);
                        handlerEditTextStatus(false, et_invite_num5);
                        handlerEditTextStatus(false, et_invite_num6);
                        handlerEditTextStatus(true, et_invite_num2);
                    }
                    if (et_invite_num2.getText().toString().length() == 0) {
                        handlerEditTextStatus(false, et_invite_num2);
                        handlerEditTextStatus(false, et_invite_num3);
                        handlerEditTextStatus(false, et_invite_num4);
                        handlerEditTextStatus(false, et_invite_num5);
                        handlerEditTextStatus(false, et_invite_num6);
                        et_invite_num2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                handlerEditTextStatus(true, et_invite_num1);
                            }
                        });
                        et_invite_num3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                handlerEditTextStatus(true, et_invite_num1);
                            }
                        });
                        et_invite_num4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                handlerEditTextStatus(true, et_invite_num1);
                            }
                        });
                        et_invite_num5.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                handlerEditTextStatus(true, et_invite_num1);
                            }
                        });
                        et_invite_num6.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                handlerEditTextStatus(true, et_invite_num1);
                            }
                        });
                        handlerEditTextStatus(true, et_invite_num1);
                        flag = 1;
                    }
                    break;
                default:
                    break;
            }

            //自动改变状态
            boolean invite1 = et_invite_num1.getText().length() > 0;
            boolean invite2 = et_invite_num2.getText().length() > 0;
            boolean invite3 = et_invite_num3.getText().length() > 0;
            boolean invite4 = et_invite_num4.getText().length() > 0;
            boolean invite5 = et_invite_num5.getText().length() > 0;
            boolean invite6 = et_invite_num6.getText().length() > 0;
            if (invite1 && invite2 && invite3 && invite4 && invite5 && invite6) {
                btn_exchange.setEnabled(true);
                KeyboardUtils.hideSoftInput(getActivity());
            } else {
                btn_exchange.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void handlerEditTextStatus(boolean isFocusable, EditText editText) {
        editText.setFocusableInTouchMode(isFocusable);
        editText.setFocusable(isFocusable);
        if (isFocusable) {
            editText.requestFocus();
            KeyboardUtils.showSoftInput(getActivity());
        } else {
            editText.clearFocus();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        KeyboardUtils.hideSoftInput(getActivity());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        KeyboardUtils.hideSoftInput(getActivity());
    }

    @Override
    public void goBack() {
        super.goBack();
        KeyboardUtils.hideSoftInput(getActivity());
    }
}
