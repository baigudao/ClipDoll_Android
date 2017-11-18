package com.happy.moment.clip.doll.view;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.widget.EditText;

import com.happy.moment.clip.doll.R;

/**
 * Created by Devin on 2017/11/13 15:13
 * E-mail:971060378@qq.com
 */

public class DemoEditText extends EditText {

    public DemoEditText(Context context) {
        super(context);
        init();
    }

    public DemoEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DemoEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    // 定制Demo中的编辑框
    private void init() {
        getBackground().setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_IN);
    }
}
