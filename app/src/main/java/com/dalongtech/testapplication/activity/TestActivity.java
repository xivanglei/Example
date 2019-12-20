package com.dalongtech.testapplication.activity;

import android.os.Bundle;
import android.widget.CheckBox;

import com.dalongtech.testapplication.R;
import com.dalongtech.testapplication.base.SimpleActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class TestActivity extends SimpleActivity {

    @BindView(R.id.cb_test)
    CheckBox cb_test;
    @Override
    protected int getLayoutById() {
        return R.layout.activity_test;
    }

    @Override
    protected void initViewAndData(Bundle savedInstanceState) {

    }

    @OnClick(R.id.fl_test)
    public void testCb() {
        cb_test.setChecked(!cb_test.isChecked());
    }
}
