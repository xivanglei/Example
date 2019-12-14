package com.dalongtech.testapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;

import com.dalongtech.testapplication.R;
import com.dalongtech.testapplication.base.SimpleActivity;
import com.dalongtech.testapplication.bean.ContactBean;
import com.dalongtech.testapplication.component.transformanim.ActivityAnimationHelper;
import com.dalongtech.testapplication.widget.RoundedImageView;
import com.xianglei.analysis.ANSAutoPageTracker;
import com.xianglei.analysis.AnalysisAgent;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends SimpleActivity implements ANSAutoPageTracker {

    @BindView(R.id.iv_test)
    RoundedImageView iv_test;

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
    }

    @OnClick(R.id.btn_test)
    public void test() {
        AnalysisAgent.track(this, "pppp");
    }

    @OnClick(R.id.btn_test2)
    public void test2() {
        Map<String, Object> map = new HashMap<>();
        map.put("super1", 11111);
        map.put("super2", 22222);
        AnalysisAgent.registerSuperProperties(this, map);
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
        ActivityAnimationHelper.startActivity(this, new Intent(this, SlideClosePictureActivity.class), iv_test);
//        SlideClosePictureActivity.start(this, mImageUrl, v);
    }

    @OnClick(R.id.btn_badge)
    public void startShortcutBadgeActivity() {
        startActivity(ShortcutBadgeActivity.class);
    }

    @OnClick(R.id.iv_test)
    public void startBigImageActivity(View v) {
        BigImageActivity.start(this, v, mImageUrl);
    }

    @OnClick(R.id.btn_voice_play)
    public void startVoicePlayActivity() {
        startActivity(VoicePlayActivity.class);
    }

    @OnClick(R.id.btn_custom_keyboard)
    public void startCustomKeyboard() {
        startActivity(CustomKayboardActivity.class);
    }

    @Override
    public Map<String, Object> registerPageProperties() {
        Map<String, Object> map = new HashMap<>();
        map.put("3393939", 98989898);
        return map;
    }

    @Override
    public String registerPageUrl() {
        return "我是主页";
    }
}
