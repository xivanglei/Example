package net.xianglei.testapplication.activity;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import net.xianglei.testapplication.R;
import net.xianglei.testapplication.base.SimpleActivity;
import net.xianglei.testapplication.component.jncryptor.AES256JNCryptor;
import net.xianglei.testapplication.component.jncryptor.AESHelper;
import net.xianglei.testapplication.component.jncryptor.CryptorException;
import net.xianglei.testapplication.component.jncryptor.JNCryptor;
import net.xianglei.testapplication.utils.LogUtil;

import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends SimpleActivity {


    @BindView(R.id.et_text)
    EditText et_text;
    @BindView(R.id.tv_link)
    TextView tv_link;

    @Override
    protected int getLayoutById() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViewAndData(Bundle savedInstanceState) {
    }

    @OnClick(R.id.btn_test)
    public void test() {
        tv_link.setText(et_text.getText());
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






}
