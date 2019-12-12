package com.dalongtech.testapplication.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import com.dalongtech.testapplication.R;


/**
 * Author:xianglei
 * Date: 2018/12/21 4:25 PM
 * Description:
 */
public class GlideUtil {

    public static final int TYPE_CENTER_CROP = 10001;
    public static final int TYPE_FIX_CENTER = 10002;
    public static final int TYPE_CENTER_INSIDE = 10003;

    private static final int PLACEHOLDER = R.mipmap.ic_launcher;
    private static final int ERROR = R.mipmap.ic_launcher;

    /*
     *加载图片(默认)
     */
    public static void loadImage(Context context, Object url, ImageView imageView) {
        loadImage(context, url, imageView, PLACEHOLDER);
    }

    public static void loadImage(Context context, Object url, ImageView imageView, int placeHolder) {
        RequestOptions options = new RequestOptions()
                .placeholder(placeHolder) //占位图
                .error(placeHolder)       //错误图
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(url).apply(options).into(imageView);
    }

    public static void loadImage(Context context, Object url, ImageView imageView, Drawable placeHolder) {
        RequestOptions options = new RequestOptions()
                .placeholder(placeHolder) //占位图
                .error(placeHolder)       //错误图
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(url).apply(options).into(imageView);
    }


    /*
     *加载圆形图片
     */
    public static void loadCircleImage(Context context, Object url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .circleCrop()//设置圆形
                .placeholder(PLACEHOLDER)
                .error(ERROR)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(url).apply(options).into(imageView);
    }

    public static void loadCircleImage(Context context, Object url, ImageView imageView, int resourceId) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .circleCrop()//设置圆形
                .placeholder(resourceId)
                .error(resourceId)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(url).apply(options).into(imageView);
    }








    private static int dip2px(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private static void setScaleType(RequestOptions options, int scaleType) {
        switch (scaleType) {
            case TYPE_CENTER_CROP:
                options.centerCrop();
                break;
            case TYPE_CENTER_INSIDE:
                options.centerInside();
                break;
            case TYPE_FIX_CENTER:
                options.fitCenter();
                break;
        }
    }

}
