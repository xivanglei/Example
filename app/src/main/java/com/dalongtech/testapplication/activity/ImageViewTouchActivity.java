package com.dalongtech.testapplication.activity;

import android.os.Bundle;
import android.widget.ImageView;

import com.dalongtech.testapplication.R;
import com.dalongtech.testapplication.base.SimpleActivity;
import com.dalongtech.testapplication.utils.LogUtil;
import com.dalongtech.testapplication.utils.ShotUtil;
import com.dalongtech.testapplication.widget.rong_photoview.PhotoView;

import butterknife.BindView;
import butterknife.OnClick;

public class ImageViewTouchActivity extends SimpleActivity {

    @BindView(R.id.image_view)
    PhotoView image_view;
    @BindView(R.id.iv_shot)
    ImageView iv_shot;

    @Override
    protected int getLayoutById() {
        return R.layout.activity_image_view_touch;
    }

    @Override
    protected void initViewAndData(Bundle savedInstanceState) {
    }

    @OnClick(R.id.btn_shot)
    public void shot() {
        iv_shot.setImageBitmap(ShotUtil.getViewBitmap(image_view));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.d("停止了");
    }
}
