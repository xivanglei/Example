package net.xianglei.testapplication.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.xianglei.testapplication.R;
import net.xianglei.testapplication.base.SimpleActivity;
import net.xianglei.testapplication.widget.SlideCloseFrameLayout;

import butterknife.BindView;

public class SlideClosePictureActivity extends SimpleActivity {

    @BindView(R.id.sf_layout)
    SlideCloseFrameLayout mCloseFrameLayout;

    @Override
    protected int getLayoutById() {
        return R.layout.activity_slide_close_picture;
    }

    @Override
    protected void initViewAndData(Bundle savedInstanceState) {
        mCloseFrameLayout.setIntercept(false);
        mCloseFrameLayout.setViewCall(this :: onBackPressed);
    }
}
