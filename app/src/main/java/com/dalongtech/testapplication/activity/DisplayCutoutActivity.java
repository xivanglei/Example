package com.dalongtech.testapplication.activity;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.DisplayCutout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.dalongtech.testapplication.R;
import com.dalongtech.testapplication.base.SimpleActivity;
import com.dalongtech.testapplication.utils.LogUtil;

import java.util.List;

import butterknife.OnClick;

public class DisplayCutoutActivity extends SimpleActivity {

    private static final int VIEW_ID = 0xf0a01;
    private DisplayCutout mDisplayCutout;


    @Override
    protected int getLayoutById() {
        return R.layout.activity_display_cutout;
    }

    @Override
    protected void initViewAndData(Bundle savedInstanceState) {
        setStatusBarFullTransparent();
    }

    //检测是否支持
    @OnClick(R.id.btn_check_support)
    public void checkSupport() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            mDisplayCutout = getWindow().getDecorView().getRootWindowInsets().getDisplayCutout();
            LogUtil.d("是否支持: " + (mDisplayCutout != null));
        } else {
            LogUtil.d("android版本低于9.0");
        }
    }

    //安全区域
    @OnClick(R.id.btn_safety_zone)
    public void getSafetyZone() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && mDisplayCutout != null) {
            LogUtil.d("安全区域距离屏幕左边的距离 SafeInsetLeft:" + mDisplayCutout.getSafeInsetLeft());
            LogUtil.d("安全区域距离屏幕右部的距离 SafeInsetRight:" + mDisplayCutout.getSafeInsetRight());
            LogUtil.d( "安全区域距离屏幕顶部的距离 SafeInsetTop:" + mDisplayCutout.getSafeInsetTop());
            LogUtil.d("安全区域距离屏幕底部的距离 SafeInsetBottom:" + mDisplayCutout.getSafeInsetBottom());
        }
    }

    //获取流海屏基本信息
    @OnClick (R.id.btn_get_info)
    public void getBaseInfo() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && mDisplayCutout != null) {
            List<Rect> rects = mDisplayCutout.getBoundingRects();
            if (rects == null || rects.size() == 0) {
                LogUtil.d( "不是刘海屏");
            } else {
                LogUtil.d("刘海屏数量:" + rects.size());
                for (Rect rect : rects) {
                    LogUtil.d("刘海屏区域：" + rect);
                }
            }
        }
    }

    //点击了适配模式允许后，隐藏状态栏内容也不会下去了，而是在原位置
    @OnClick(R.id.btn_adapt_mode)
    public void adaptMode() {
        fitsNotchScreen();
    }

    @OnClick(R.id.btn_show_status)
    public void showStatus() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @OnClick(R.id.btn_hide_status)
    public void hideStatus() {
        //正常的时候这个模式会让状态栏跑出去，刘海区会空出来，内容会往下走。但是如果设置了下面的适配模式，允许扩展到刘海区，内容还会在刘海区，而不会往下走
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @OnClick(R.id.btn_add_window)
    public void addWindowTest() {
        addView(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, getResources().getColor(R.color.red));
    }

    private void addView(int w, int h, int color) {
        ViewGroup vp = removeView();
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(w, h, Gravity.CENTER);
        FrameLayout newView = new FrameLayout(this);
        newView.setBackgroundColor(color);
        newView.setId(VIEW_ID);
        vp.addView(newView, params);
        newView.setOnClickListener(v -> removeView());
    }

    private ViewGroup removeView() {
        ViewGroup vp = (ViewGroup) (scanForActivity(this))//.getWindow().getDecorView();
                .findViewById(Window.ID_ANDROID_CONTENT);

        View old = vp.findViewById(VIEW_ID);
        if (old != null) {
            vp.removeView(old);
        }
        return vp;
    }

    public static Activity scanForActivity(Context context) {
        if (context == null) return null;

        if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextWrapper) {
            return scanForActivity(((ContextWrapper) context).getBaseContext());
        }

        return null;
    }

    /**
     * LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT：仅仅当系统提供的bar完全包含了刘海区时才允许window扩展到刘海区,否则window不会和刘海区重叠；
     * LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES：允许window扩展到刘海区(原文说的是短边的刘海区, 目前有刘海的手机都在短边,所以就不纠结了)；
     * LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER：不允许window扩展到刘海区。
     * 适配刘海屏
     * Fits notch screen.
     */
    private void fitsNotchScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            getWindow().setAttributes(lp);
        }
    }

    /**
     * 全透状态栏，内容会在状态栏位置，状态栏重叠，所以重要位置不能放在顶部，背景重叠没事
     */
    protected void setStatusBarFullTransparent() {
        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //虚拟键盘也透明
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }
}
