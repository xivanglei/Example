package net.xianglei.testapplication.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.xianglei.testapplication.R;
import net.xianglei.testapplication.base.SimpleActivity;

public class FlexibleLayoutActivity extends SimpleActivity {

    @Override
    protected int getLayoutById() {
        return R.layout.activity_flexible_layout;
    }

    @Override
    protected void initViewAndData(Bundle savedInstanceState) {

    }
}
