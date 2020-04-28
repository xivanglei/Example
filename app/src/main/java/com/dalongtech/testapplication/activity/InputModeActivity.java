package com.dalongtech.testapplication.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.dalongtech.testapplication.R;
import com.dalongtech.testapplication.base.SimpleActivity;
import com.dalongtech.testapplication.component.AndroidBug5497Workaround;
import com.dalongtech.testapplication.utils.ScreenUtil;

import butterknife.BindView;

public class InputModeActivity extends SimpleActivity {

    @BindView(R.id.view_status)
    View view_status;

    @Override
    protected int getLayoutById() {
        return R.layout.activity_input_mode;
    }

    @Override
    protected void beforeContentView() {
        //这里是全屏，会导致软键盘遮挡，下面是解决方案
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @Override
    protected void initViewAndData(Bundle savedInstanceState) {
        view_status.getLayoutParams().height = ScreenUtil.getStatusBarHeight();
        //这里解决因为SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN导致adjustResize失效
        AndroidBug5497Workaround.assistActivity(mContext);
    }
}
