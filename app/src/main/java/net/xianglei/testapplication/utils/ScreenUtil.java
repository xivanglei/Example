package net.xianglei.testapplication.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Author:xianglei
 * Date: 2018/12/23 12:37 PM
 * Description:屏幕尺寸类操作
 */
public class ScreenUtil {

    private static int screenW;
    private static int screenH;
    private static float screenDensity;
    private static float screenDensityDpi;

    public static float getScreenDensityDpi() {
        if(screenDensityDpi == 0) {
            initScreen();
        }
        return screenDensityDpi;
    }

    public static int getScreenW() {
        if (screenW == 0) {
            initScreen();
        }
        return screenW;
    }

    public static int getScreenH() {
        if (screenH == 0) {
            initScreen();
        }
        return screenH;
    }

    public static float getScreenDensity() {
        if (screenDensity == 0) {
            initScreen();
        }
        return screenDensity;
    }

    public static void initScreen() {
        DisplayMetrics metric = Util.getApp().getResources().getDisplayMetrics();
        screenW = metric.widthPixels;
        screenH = metric.heightPixels;
        if (screenW > screenH) {
            int t = screenH;
            screenH = screenW;
            screenW = t;
        }
        //屏幕分辨率为1024x600，density为1.5，densityDpi为240
        screenDensity = metric.density;         //屏幕密度
        screenDensityDpi = metric.densityDpi;   //每英寸距离具有多少个像素点
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(float dpValue) {
        return (int) (dpValue * getScreenDensity() + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(float pxValue) {
        return (int) (pxValue / getScreenDensity() + 0.5f);
    }

    /**
     * 计算状态栏高度
     */
    public static int getStatusBarHeight() {
        //获取状态栏高度
        int result = 24;
        int resId = Util.getApp().getResources().getIdentifier("status_bar_height", "dimen", "android"); //系统方法，获得状态栏高度的资源ID
        if (resId > 0) {                    //如果获取到了ID
            result = Util.getApp().getResources().getDimensionPixelSize(resId);           //获取到状态栏高度
        } else {
            result = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,    //把 dip（TypedValue.COMPLEX_UNIT_DIP）转化为px单位
                    result, Resources.getSystem().getDisplayMetrics());
        }
        return result;
    }
}
