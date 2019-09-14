package net.xianglei.testapplication.activity;

import android.os.Bundle;
import android.util.Base64;

import net.xianglei.testapplication.R;
import net.xianglei.testapplication.base.SimpleActivity;
import net.xianglei.testapplication.component.jncryptor.AES256JNCryptor;
import net.xianglei.testapplication.component.jncryptor.AESHelper;
import net.xianglei.testapplication.component.jncryptor.CryptorException;
import net.xianglei.testapplication.component.jncryptor.JNCryptor;
import net.xianglei.testapplication.utils.LogUtil;

import butterknife.OnClick;


public class MainActivity extends SimpleActivity {

    private String aa;

    @Override
    protected int getLayoutById() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViewAndData(Bundle savedInstanceState) {

    }

    @OnClick(R.id.btn_test)
    public void test() {
        aa = encode();
    }

    @OnClick(R.id.btn_test2)
    public void test2() {
        decode();
    }

    private String encode() {
        String password = "heychat1223";
        String result = AESHelper.encrypt("{sss}\"\"slkj__kdj", password);
            LogUtil.d(result);
        return result;
    }

    private void decode() {
        String result = AESHelper.decrypt(aa,"heychat1223" );
        LogUtil.d(result);
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






}
