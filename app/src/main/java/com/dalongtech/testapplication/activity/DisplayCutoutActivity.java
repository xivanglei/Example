package com.dalongtech.testapplication.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.DisplayCutout;

import com.dalongtech.testapplication.R;
import com.dalongtech.testapplication.base.SimpleActivity;
import com.dalongtech.testapplication.utils.LogUtil;

import butterknife.OnClick;

public class DisplayCutoutActivity extends SimpleActivity {

    @Override
    protected int getLayoutById() {
        return R.layout.activity_display_cutout;
    }

    @Override
    protected void initViewAndData(Bundle savedInstanceState) {

    }

    //检测是否支持
    @OnClick(R.id.btn_check_support)
    public void checkSupport() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            DisplayCutout cutout = getWindow().getDecorView().getRootWindowInsets().getDisplayCutout();
            LogUtil.d("是否支持: " + (cutout != null));
        } else {
            LogUtil.d("android版本低于9.0");
        }
    }

    //获取流海屏基本信息
    @OnClick (R.id.btn_get_info)
    public void getBaseInfo() {

    }

    //适配模式
    @OnClick(R.id.btn_adapt_mode)
    public void adaptMode() {

    }
}
