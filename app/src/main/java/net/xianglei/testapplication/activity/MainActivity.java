package net.xianglei.testapplication.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.xianglei.testapplication.R;
import net.xianglei.testapplication.base.SimpleActivity;
import net.xianglei.testapplication.component.jncryptor.AES256JNCryptor;
import net.xianglei.testapplication.component.jncryptor.AESHelper;
import net.xianglei.testapplication.component.jncryptor.CryptorException;
import net.xianglei.testapplication.component.jncryptor.JNCryptor;
import net.xianglei.testapplication.utils.CommonUtil;
import net.xianglei.testapplication.utils.GlideUtil;
import net.xianglei.testapplication.utils.ImageCompressUtil;
import net.xianglei.testapplication.utils.LogUtil;
import net.xianglei.testapplication.utils.SDCardUtil;
import net.xianglei.testapplication.utils.StringUtil;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.net.URLConnection;

import butterknife.BindView;
import butterknife.OnClick;
import top.zibin.luban.Luban;


public class MainActivity extends SimpleActivity {

    private String mImageUrl = "https://rongcloud-file.cn.ronghub.com/application_octet-stream__RC-2019-10-21_8228_1571652938959.mp4?attname=40dfc95cc9fb4ba497b445d10441782f.mp4&e=2147483647&token=CddrKW5AbOMQaDRwc3ReDNvo3-sL_SO1fSUBKV3H:YV8sUH1aAnYaVrh-7iJx0iB31N0=";
    private String srcFile = "/storage/emulated/0/DCIM/Camera/IMG_20191004_150413.jpg";

    @Override
    protected int getLayoutById() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViewAndData(Bundle savedInstanceState) {

    }

    @OnClick(R.id.btn_test)
    public void test() {
    }

    @OnClick(R.id.btn_test2)
    public void test2() {
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
        SlideClosePictureActivity.start(this, mImageUrl, v);
    }

    @OnClick(R.id.btn_badge)
    public void startShortcutBadgeActivity() {
        startActivity(ShortcutBadgeActivity.class);
    }





}
