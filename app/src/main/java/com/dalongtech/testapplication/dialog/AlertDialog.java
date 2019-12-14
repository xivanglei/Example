package com.dalongtech.testapplication.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.dalongtech.testapplication.R;
import com.dalongtech.testapplication.base.dialog.BaseDialog;
import com.dalongtech.testapplication.utils.StringUtil;

import butterknife.BindView;

/**
 * Author:xianglei
 * Date: 2018/12/24 1:54 PM
 * Description:基本弹窗
 */
public class AlertDialog extends BaseDialog {

    @BindView(R.id.tv_hint)
    TextView tv_hint;

    private int mLayoutId = R.layout.base_dialog;
    private String mContent;
    private String mRightButton;
    private String mLeftButton;
    private boolean mIsShowLeft = true;

    public AlertDialog(@NonNull Context context) {
        this(context, 0);
    }

    public AlertDialog(@NonNull Context context, int layoutId) {
        super(context, R.style.dialog);
        if(layoutId != 0) mLayoutId = layoutId;
    }

    public void setRightButton(String rightButton) {
        mRightButton = rightButton;
        if(tv_submit != null) tv_submit.setText(mRightButton);
    }

    public void setLeftButton(String leftButton) {
        mLeftButton = leftButton;
        if(tv_cancel != null) tv_cancel.setText(mLeftButton);
    }

    public void setLeftButtonVisibility(boolean isShow) {
        mIsShowLeft = isShow;
        if(tv_cancel != null) tv_cancel.setVisibility(mIsShowLeft ? View.VISIBLE : View.GONE);
    }

    @Override
    protected int getLayoutById() {
        return mLayoutId;
    }

    @Override
    protected void initViewAndData(Bundle savedInstanceState) {
        if(StringUtil.isNotBlank(mLeftButton)) tv_cancel.setText(mLeftButton);
        if(StringUtil.isNotBlank(mRightButton)) tv_submit.setText(mRightButton);
        if(StringUtil.isNotBlank(mContent)) tv_hint.setText(mContent);
        tv_cancel.setVisibility(mIsShowLeft ? View.VISIBLE : View.GONE);
        setCanceledOnTouch(true);
    }

    public void setContent(String content) {
        mContent = content;
        if(tv_hint != null) tv_hint.setText(mContent);
    }


}
