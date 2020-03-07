package com.dalongtech.testapplication.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Author:xianglei
 * Date: 2019-08-05 11:22
 * Description:
 */
public abstract class SimpleActivity extends AppCompatActivity {

    protected TextView mProgressText;
    protected FrameLayout mTextProgressContainer;
    protected AppCompatActivity mContext;
    private Unbinder mUnBinder;
    protected Bundle mSavedInstanceState;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mSavedInstanceState = savedInstanceState;
        if(!isOnCreateContinue()) return;
        beforeContentView();
        if(getLayoutById() != 0) setContentView(getLayoutById());
        bindId();
        onViewCreated();
        initViewAndData(savedInstanceState);
        initEvent();
    }


    /**
     * @return 是否继续执行剩余的onCreate方法，可在这里加入判断
     */
    protected boolean isOnCreateContinue() {
        return true;
    }

    //引入视图之前
    protected void beforeContentView(){ }
    //视图初始化完成
    protected void onViewCreated() { }

    protected void bindId() {
        mUnBinder = ButterKnife.bind(this);
    }

    protected abstract int getLayoutById();

    protected abstract void initViewAndData(Bundle savedInstanceState);

    protected void startActivity(Class<? extends AppCompatActivity> activity) {
        startActivity(new Intent(this, activity));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnBinder.unbind();
    }

    protected void initEvent() {
    }


}
