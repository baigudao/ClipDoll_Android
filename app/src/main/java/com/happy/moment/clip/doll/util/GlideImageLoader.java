package com.happy.moment.clip.doll.util;

import android.content.Context;
import android.widget.ImageView;

import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.happy.moment.clip.doll.R;
import com.youth.banner.loader.ImageLoader;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by Devin on 2017/11/11 14:51
 * E-mail:971060378@qq.com
 */

public class GlideImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //Glide 加载图片简单用法
        Glide.with(context)
                .load(path)
                .placeholder(R.drawable.wawa_default)
                .bitmapTransform(new RoundedCornersTransformation(context, SizeUtils.dp2px(15), 0, RoundedCornersTransformation.CornerType.ALL))
                .into(imageView);
    }
}
