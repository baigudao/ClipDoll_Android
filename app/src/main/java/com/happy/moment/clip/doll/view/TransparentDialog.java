package com.happy.moment.clip.doll.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.happy.moment.clip.doll.R;

/**
 * Created by Devin on 2017/11/20 14:34
 * E-mail:971060378@qq.com
 */

public class TransparentDialog extends Dialog {

    public TransparentDialog(@NonNull Context context) {
        super(context);
    }

    public TransparentDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_clip_doll_result_yes);
    }
}
