package com.happy.moment.clip.doll.fragment;

import android.content.Context;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
 * Created by Devin on 2017/11/17 21:38
 * E-mail:971060378@qq.com
 */

public class FeedBackFragment extends BaseFragment implements View.OnFocusChangeListener {

    private EditText et_put_feed_back;
    private TextView tv_word_num;
    private Button btn_commit;

    private static final int MAX_NUM = 500;
    private ClipboardManager cm;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_feed_back;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.ll_close).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_bar_title)).setText("意见反馈");
        view.findViewById(R.id.iv_share).setVisibility(View.GONE);

        et_put_feed_back = (EditText) view.findViewById(R.id.et_put_feed_back);
        et_put_feed_back.addTextChangedListener(watcher);
        et_put_feed_back.setOnFocusChangeListener(this);
        tv_word_num = (TextView) view.findViewById(R.id.tv_word_num);

        RelativeLayout rl_input_word = (RelativeLayout) view.findViewById(R.id.rl_input_word);
        rl_input_word.setOnClickListener(this);

        btn_commit = (Button) view.findViewById(R.id.btn_commit);
        btn_commit.setOnClickListener(this);
        btn_commit.setEnabled(false);
        view.findViewById(R.id.rl_copy1).setOnClickListener(this);
        view.findViewById(R.id.rl_copy2).setOnClickListener(this);

        cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);

        KeyboardUtils.clickBlankArea2HideSoftInput();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_close:
                goBack();
                break;
            case R.id.btn_commit:
                commitContent();
                break;
            case R.id.rl_copy1:
                // 从API11开始android推荐使用android.content.ClipboardManager
                // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
                // 将文本内容放到系统剪贴板里。
                cm.setText("woaizhuawa");
                ToastUtils.showShort("复制到剪贴板成功");
                break;
            case R.id.rl_copy2:
                // 从API11开始android推荐使用android.content.ClipboardManager
                // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
                // 将文本内容放到系统剪贴板里。
                cm.setText("我爱抓娃娃正版包邮");
                ToastUtils.showShort("复制到剪贴板成功");
                break;
            case R.id.rl_input_word:
                et_put_feed_back.setFocusable(true);
                et_put_feed_back.setFocusableInTouchMode(true);
                et_put_feed_back.requestFocus();
                KeyboardUtils.showSoftInput(getActivity());
                break;
            default:
                break;
        }
    }

    private void commitContent() {
        showLoadingDialog(null, null, false);
        OkHttpUtils.post()
                .url(Constants.getUserFeedBack())
                .addParams(Constants.SESSION, SPUtils.getInstance().getString(Constants.SESSION))
                .addParams(Constants.OPINION, et_put_feed_back.getText().toString())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e(e.toString());
                        hideLoadingDialog();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        hideLoadingDialog();
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            JSONObject jsonObjectResHead = jsonObject.optJSONObject("resHead");
                            int code = jsonObjectResHead.optInt("code");
                            String msg = jsonObjectResHead.optString("msg");
                            String req = jsonObjectResHead.optString("req");
                            JSONObject jsonObjectResBody = jsonObject.optJSONObject("resBody");
                            if (code == 1) {
                                ToastUtils.showShort("提交成功！");
                                et_put_feed_back.setText("");
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
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //只要编辑框内容有变化就会调用该方法，s为编辑框变化后的内容
            LogUtils.e("onTextChanged" + s.toString());
            boolean hasWord = et_put_feed_back.getText().length() > 0;
            if (hasWord) {
                btn_commit.setEnabled(true);
            } else {
                btn_commit.setEnabled(false);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //编辑框内容变化之前会调用该方法，s为编辑框内容变化之前的内容
            LogUtils.e("beforeTextChanged" + s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {
            //编辑框内容变化之后会调用该方法，s为编辑框内容变化后的内容
            LogUtils.e("afterTextChanged" + s.toString());
            if (s.length() > MAX_NUM) {
                ToastUtils.showShort("字数已经超过限制");
                s.delete(MAX_NUM, s.length());
                return;
            }
            int num = s.length();
            tv_word_num.setText(String.valueOf(num));
        }
    };

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            LogUtils.e("已经获取到了焦点");
        } else {
            LogUtils.e("已经失去焦点");
        }
    }

    @Override
    public void goBack() {
        super.goBack();
        KeyboardUtils.hideSoftInput(getActivity());
    }
}
