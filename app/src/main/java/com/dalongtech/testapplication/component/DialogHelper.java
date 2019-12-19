package com.dalongtech.testapplication.component;

import android.content.Context;
import android.view.View;

import com.dalongtech.testapplication.base.dialog.OnClickCallback;
import com.dalongtech.testapplication.dialog.AlertDialog;

/**
 * Author:xianglei
 * Date: 2018/12/24 3:15 PM
 * Description:
 */
public class DialogHelper {

    private static final String LEFT_BUTTON = "取消";
    private static final String RIGHT_BUTTON = "确定";

    private AlertDialog mDialog;

    private DialogHelper() {
    }
    public static DialogHelper getInstance() {
        return DialogHolder.sInstance;
    }
    private static class DialogHolder {
        private static final DialogHelper sInstance = new DialogHelper();
    }

    public void showSingleDialog(Context context, String content) {
        showSingleDialog(context, content, RIGHT_BUTTON, new OnDefaultCallback());
    }
    public void showSingleDialog(Context context, String content, OnClickCallback onClickCallback) {
        showSingleDialog(context, content, RIGHT_BUTTON, onClickCallback);
    }
    public void showSingleDialog(Context context, String content, String rightButton) {
        showSingleDialog(context, content, rightButton, new OnDefaultCallback());
    }
    public void showSingleDialog(Context context, String content, String rightButton, OnClickCallback onClickCallback) {
        showDialog(context, 0, content, null, false, rightButton, onClickCallback);
    }

    public void showDoubleDialog(Context context, String content, OnClickCallback onClickCallback) {
        showDoubleDialog(context, 0, content, LEFT_BUTTON, RIGHT_BUTTON, onClickCallback);
    }

    public void showDoubleDialog(Context context, String content, String leftButton, String rightButton, OnClickCallback onClickCallback) {
        showDialog(context, 0, content, leftButton, true, rightButton, onClickCallback);
    }

    public void showDoubleDialog(Context context, int layoutId, String content, String leftButton, String rightButton, OnClickCallback onClickCallback) {
        showDialog(context, layoutId, content, leftButton, true, rightButton, onClickCallback);
    }

    public void showDialog(Context context, int layoutId, String content, String leftButton, boolean showLeft, String rightButton, OnClickCallback onClickCallback) {
        mDialog = new AlertDialog(context, layoutId);
        mDialog.setContent(content);
        mDialog.setLeftButton(leftButton);
        mDialog.setLeftButtonVisibility(showLeft);
        mDialog.setRightButton(rightButton);
        mDialog.setOnClickListener(onClickCallback);
        mDialog.show();
    }

    public static class OnDefaultCallback implements OnClickCallback {
        @Override
        public void onLeftClickListener(View v) { }
        @Override
        public void onRightClickListener(View v) { }
    }

    public void setCanceledOnTouch(boolean canceledOnTouch) {
        mDialog.setCanceledOnTouch(canceledOnTouch);
    }

    public void show() {
        if (mDialog != null && !mDialog.isShowing()) {
            mDialog.show();
        }
    }

    public void dismiss() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        mDialog = null;
    }
}
