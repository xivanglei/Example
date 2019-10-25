package net.xianglei.testapplication.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.xianglei.testapplication.R;
import net.xianglei.testapplication.base.SimpleActivity;

public class TestFragmentActivity extends SimpleActivity {

    @Override
    protected int getLayoutById() {
        return R.layout.activity_test_fragment;
    }

    @Override
    protected void initViewAndData(Bundle savedInstanceState) {

    }
}
