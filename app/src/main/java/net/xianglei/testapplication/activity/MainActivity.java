package net.xianglei.testapplication.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import net.xianglei.testapplication.R;
import net.xianglei.testapplication.base.SimpleActivity;
import net.xianglei.testapplication.component.AddressListUtil;
import net.xianglei.testapplication.test.aaa;
import net.xianglei.testapplication.test.ccc;
import net.xianglei.testapplication.utils.JsonUtil;
import net.xianglei.testapplication.utils.LogUtil;

import java.lang.reflect.Method;

import butterknife.OnClick;


public class MainActivity extends SimpleActivity {

    @Override
    protected int getLayoutById() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViewAndData(Bundle savedInstanceState) {

    }

    @OnClick(R.id.btn_test)
    public void test() {

        new ccc().zzz();
    }

    @OnClick(R.id.btn_get_address_list)
    public void getAddressLit() {
        if(ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            String addressList = JsonUtil.toJson(AddressListUtil.getContacts(this));
            LogUtil.d(addressList);
        }
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






}
