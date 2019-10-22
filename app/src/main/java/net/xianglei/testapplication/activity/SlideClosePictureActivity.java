package net.xianglei.testapplication.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import net.xianglei.testapplication.R;
import net.xianglei.testapplication.base.SimpleActivity;
import net.xianglei.testapplication.utils.GlideUtil;
import net.xianglei.testapplication.widget.MyPhotoView;
import net.xianglei.testapplication.widget.SlideCloseFrameLayout;
import net.xianglei.testapplication.widget.rong_photoview.PhotoView;

import butterknife.BindView;
import butterknife.OnClick;

public class SlideClosePictureActivity extends SimpleActivity {

    @BindView(R.id.sf_layout)
    SlideCloseFrameLayout mCloseFrameLayout;
    @BindView(R.id.pv_photo)
    PhotoView pv_photo;

    public static final String IT_URL = "url";

    @Override
    protected int getLayoutById() {
        return R.layout.activity_slide_close_picture;
    }

    @Override
    protected void initViewAndData(Bundle savedInstanceState) {
        GlideUtil.loadImage(this, getIntent().getStringExtra(IT_URL), pv_photo);
        mCloseFrameLayout.setIntercept(pv_photo.getScale() == 1);
        mCloseFrameLayout.setViewCall(this :: onBackPressed);
        mCloseFrameLayout.setIntercept(false);
        mCloseFrameLayout.setObtainInterruptible(() -> pv_photo.getScale() == 1);
    }

    @OnClick(R.id.btn_get_scale)
    public void getScale() {
        pv_photo.getScale();
    }

    public static void start(Context context, String url, View view) {
        Intent intent = new Intent(context, SlideClosePictureActivity.class);
        intent.putExtra(IT_URL, url);
        //转场动画
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context, view, "shareView");
        context.startActivity(intent, options.toBundle());
    }
}
