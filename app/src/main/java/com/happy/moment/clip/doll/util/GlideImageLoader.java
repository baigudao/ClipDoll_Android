package com.happy.moment.clip.doll.util;

import android.content.Context;
import android.widget.ImageView;

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
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setBackgroundColor(context.getResources().getColor(R.color.background_color));
        //Glide 加载图片简单用法
        Glide.with(context)
                .load(path)
                .placeholder(R.drawable.wawa_default1)
                .error(R.drawable.wawa_default1)
                .bitmapTransform(new RoundedCornersTransformation(context, 30, 0, RoundedCornersTransformation.CornerType.ALL))
                .into(imageView);
    }

    //提供createImageView 方法，如果不用可以不重写这个方法，主要是方便自定义ImageView的创建
    @Override
    public ImageView createImageView(Context context) {
        return super.createImageView(context);
    }
}
