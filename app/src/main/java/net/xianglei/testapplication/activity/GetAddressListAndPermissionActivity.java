package net.xianglei.testapplication.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.xianglei.testapplication.R;
import net.xianglei.testapplication.base.SimpleActivity;
import net.xianglei.testapplication.component.AddressListUtil;
import net.xianglei.testapplication.utils.CommonUtil;
import net.xianglei.testapplication.utils.JsonUtil;
import net.xianglei.testapplication.utils.LogUtil;

import butterknife.OnClick;

public class GetAddressListAndPermissionActivity extends SimpleActivity {

    private static final int PERMISSIONS = 11;

    @Override
    protected int getLayoutById() {
        return R.layout.activity_get_address_list_and_permission;
    }

    @Override
    protected void initViewAndData(Bundle savedInstanceState) {
        CommonUtil.clickBlankHideKeyboard(this);
    }

    @OnClick(R.id.btn_get_address_list)
    public void getAddressLitIfHasPermissions() {
        if(ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
           getAddressList();
        } else {
            ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS);
        }
    }

    private void getAddressList() {
        String addressList = JsonUtil.toJson(AddressListUtil.getContacts(this));
        LogUtil.d(addressList);
    }

    private boolean isGranted(int[] grantResults) {
        for(int grantResult : grantResults) {
            if(grantResult != PackageManager.PERMISSION_GRANTED) return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS:
                if(isGranted(grantResults)) {
                    getAddressList();
                } else {
                    //是否始终拒绝 false 表示曾经选中过不再提醒，这是可以弹出弹框让别人去设置了
                    boolean test = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS);
                    LogUtil.d(test);
                }
                break;

        }
    }
}
