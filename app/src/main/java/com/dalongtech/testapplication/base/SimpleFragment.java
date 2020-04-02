package com.dalongtech.testapplication.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dalongtech.testapplication.utils.LogUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Author:xianglei
 * Date: 2018/12/25 8:36 AM
 * Description:fragment的原始基类
 */
public abstract class SimpleFragment extends Fragment {

    protected View mView;
    protected Activity mActivity;
    protected Context mContext;
    private Unbinder mUnBinder;
    private Boolean hasInitData = false;
    private Bundle mSavedInstanceState;

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(getLayoutById(), container, false);
        }
        return mView;
    }

    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnBinder = ButterKnife.bind(this, view);
        LogUtil.d("BaseFragment:      " + getClass().getSimpleName());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSavedInstanceState = savedInstanceState;
        if (!hasInitData) {
            initViewAndData();
            initEvent();
            initRequest();
            hasInitData = true;
        }
    }

    @Override
    public void onAttach(Context context) {
        mActivity = (Activity) context;
        mContext = context;
        super.onAttach(context);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((ViewGroup) mView.getParent()).removeView(mView);
        mUnBinder.unbind();
    }

    protected void initEvent() {
    }

    protected void initRequest(){}

    public abstract int getLayoutById();

    protected abstract void initViewAndData();

    public void startActivity(Class<? extends Activity> clazz) {
        startActivity(new Intent(getActivity(), clazz));
    }

    public Bundle getSavedInstanceState() {
        return mSavedInstanceState;
    }
}
