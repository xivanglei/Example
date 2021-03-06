package com.dalongtech.testapplication.activity;

import android.os.Bundle;
import android.widget.ProgressBar;

import com.dalongtech.testapplication.R;
import com.dalongtech.testapplication.base.SimpleActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class CustomProgressBarActivity extends SimpleActivity {

    @BindView(R.id.pb_score)
    ProgressBar pb_score;

    @Override
    protected int getLayoutById() {
        return R.layout.activity_custom_progress_bar;
    }

    @Override
    protected void initViewAndData(Bundle savedInstanceState) {

    }

    @OnClick(R.id.btn_add_progress)
    public void addProgress() {
        pb_score.setProgress(pb_score.getProgress() + 1);
    }
}
