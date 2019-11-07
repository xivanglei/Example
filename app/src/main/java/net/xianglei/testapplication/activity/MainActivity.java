package net.xianglei.testapplication.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;

import net.xianglei.testapplication.R;
import net.xianglei.testapplication.base.SimpleActivity;
import net.xianglei.testapplication.bean.ContactBean;
import net.xianglei.testapplication.component.MyAlertDialog;
import net.xianglei.testapplication.utils.JsonUtil;
import net.xianglei.testapplication.utils.LogUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.OnClick;


public class MainActivity extends SimpleActivity {

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
        array.put(1, new ContactBean(null));
        array.put(2, new ContactBean("sllss"));
        array.put(3, new ContactBean("slls7"));
        array.put(4, new ContactBean(null));
        array.put(5, new ContactBean(null));
        array.put(6, new ContactBean(null));
        array.put(7, new ContactBean(null));
        array.put(8, new ContactBean(null));
        array.put(9, new ContactBean("slls2"));
        array.put(10, new ContactBean(null));
        array.put(11, new ContactBean("slls3"));
        array.put(12, new ContactBean("sllsa"));
        array.put(13, new ContactBean(null));
        LogUtil.d(JsonUtil.toJson(array));

    }

    @OnClick(R.id.btn_test2)
    public void test2() {
        new MyAlertDialog(this, new String[] {"测试下", "dddddd"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LogUtil.d(which);
            }
        }).initDialog();
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

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.d("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.d("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.d("onStop");
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.d("onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtil.d("onRestart");
    }
}
