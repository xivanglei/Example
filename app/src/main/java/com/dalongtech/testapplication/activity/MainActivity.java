package com.dalongtech.testapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;

import com.dalongtech.testapplication.R;
import com.dalongtech.testapplication.base.SimpleActivity;
import com.dalongtech.testapplication.bean.ContactBean;
import com.dalongtech.testapplication.component.transformanim.ActivityAnimationHelper;
import com.dalongtech.testapplication.utils.StartActivityUtils;
import com.dalongtech.testapplication.widget.RoundedImageView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends SimpleActivity {

    @BindView(R.id.iv_test_transform_animator)
    RoundedImageView iv_test_transform_animator;

    boolean isTest = false;

    private String mImageUrl = "https://rongcloud-file.cn.ronghub.com/application_octet-stream__RC-2019-10-21_8228_1571652938959.mp4?attname=40dfc95cc9fb4ba497b445d10441782f.mp4&e=2147483647&token=CddrKW5AbOMQaDRwc3ReDNvo3-sL_SO1fSUBKV3H:YV8sUH1aAnYaVrh-7iJx0iB31N0=";
    private String srcFile = "/storage/emulated/0/DCIM/Camera/IMG_20191004_150413.jpg";

    SparseArray<ContactBean> array = new SparseArray<>();
    Map mMap = new HashMap();

    @Override
    protected int getLayoutById() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViewAndData(Bundle savedInstanceState) {
        if(isTest) startActivity(TestActivity.class);
        startDisplayCutoutActivity();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @OnClick(R.id.btn_test)
    public void test() {
        StartActivityUtils.startActivity(this, "com.dalongtech.testapplication.activity.TestActivity");
    }

    @OnClick(R.id.btn_test_page)
    public void enterTestPage() {
        startActivity(TestActivity.class);
    }

    @OnClick(R.id.btn_get_address_list)
    public void getAddressLit() {
        startActivity(GetAddressListAndPermissionActivity.class);
    }
    @OnClick(R.id.btn_scroll_conflict)
    public void startScrollConflictActivity() {
        startActivity(ScrollConflictActivity.class);
    }
    @OnClick(R.id.btn_imageview_touch)
    public void startImageViewTouchPage() {
        startActivity(ImageViewTouchActivity.class);
    }
    @OnClick(R.id.btn_recycler_divide)
    public void startRecyclerDividePage() {
        startActivity(RecyclerDivideActivity.class);
    }
    @OnClick(R.id.btn_flexible_layout)
    public void startFlexibleLayoutActivity() {
        startActivity(FlexibleLayoutActivity.class);
    }
    @OnClick(R.id.btn_small_pinned_head)
    public void startSmallPinnedHeadActivity() {
        startActivity(SmallPinnedHeaderActivity.class);
    }
    @OnClick(R.id.btn_bt_recycler)
    public void startBTRecyclerActivity() {
        startActivity(BTRecyclerActivity.class);
    }

    @OnClick(R.id.btn_custom_progress)
    public void startCustomProgressActivity() {
        startActivity(CustomProgressBarActivity.class);
    }

    @OnClick(R.id.btn_slide_close_layout)
    public void startSlideCloseActivity(View v) {
        ActivityAnimationHelper.startActivity(this, new Intent(this, SlideClosePictureActivity.class), iv_test_transform_animator);
//        SlideClosePictureActivity.start(this, mImageUrl, v);
    }

    @OnClick(R.id.btn_badge)
    public void startShortcutBadgeActivity() {
        startActivity(ShortcutBadgeActivity.class);
    }

    @OnClick(R.id.btn_big_image)
    public void startBigImageActivity(View v) {
        BigImageActivity.start(this, iv_test_transform_animator, mImageUrl);
    }

    @OnClick(R.id.btn_voice_play)
    public void startVoicePlayActivity() {
        startActivity(VoicePlayActivity.class);
    }

    @OnClick(R.id.btn_custom_keyboard)
    public void startCustomKeyboard() {
        startActivity(CustomKayboardActivity.class);
    }

    @OnClick(R.id.btn_analysis)
    public void startAnalysisDemo() {
        startActivity(AnalysisDemoActivity.class);
    }

    @OnClick(R.id.btn_action_queue)
    public void startActionQueueActivity() {
        startActivity(ActionQueueActivity.class);
    }

    @OnClick(R.id.btn_service_not_wait)
    public void startServiceNotWait() {
        Intent start = new Intent(this, NotWaitService.class);
        startService(start);
    }
    @OnClick(R.id.btn_qr_code)
    public void strToQRCode() {
        startActivity(QRCodeActivity.class);
    }

    @OnClick(R.id.btn_reflect)
    public void startReflectActivity() {
        startActivity(ReflectDemoActivity.class);
    }

    @OnClick(R.id.btn_display_cutout)
    public void startDisplayCutoutActivity() {
        startActivity(DisplayCutoutActivity.class);
    }
}
