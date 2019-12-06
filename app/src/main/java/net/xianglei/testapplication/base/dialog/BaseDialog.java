package net.xianglei.testapplication.base.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import net.xianglei.testapplication.R;
import net.xianglei.testapplication.utils.ScreenUtil;

import butterknife.ButterKnife;

/**
 * Author:xianglei
 * Date: 2018/12/24 2:07 PM
 * Description:基本对话框
 */
public abstract class BaseDialog extends Dialog {

    public static final int GRAVITY_CENTER = 0x01;
    public static final int GRAVITY_BOTTOM = 0x02;
    public static final int GRAVITY_TOP = 0x03;
    private int mShowGravity = GRAVITY_CENTER;
    private boolean mIsCanceledOnTouch = true;
    protected OnClickCallback mOnClickCallback;
    protected TextView tv_submit;
    protected TextView tv_cancel;
    private View mView;
    private int mMarginLR = 25;

    protected Context mContext;

    public BaseDialog(@NonNull Context context) {
        this(context, 0);
    }

    public BaseDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = View.inflate(mContext, getLayoutById(), null);
        tv_submit = (TextView) mView.findViewById(R.id.tv_submit);
        tv_cancel = (TextView) mView.findViewById(R.id.tv_cancel);
        setContentView(mView);
        ButterKnife.bind(this, mView);
        initViewAndData(savedInstanceState);
        initEvent();
        setCanceledOnTouchOutside(mIsCanceledOnTouch);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void setGravity(int showGravity) {
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.width = ScreenUtil.getScreenW() - ScreenUtil.dp2px(mMarginLR * 2);
        if(showGravity == GRAVITY_CENTER) {
            attributes.gravity = Gravity.CENTER;
        } else if(showGravity == GRAVITY_BOTTOM) {
            attributes.gravity = Gravity.BOTTOM;
        } else {
            attributes.gravity = Gravity.TOP;
        }
        getWindow().setAttributes(attributes);
    }

    public void setHeight(int height) {
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.height = height;
        getWindow().setAttributes(attributes);
    }

    public void setShowGravity(int showGravity) {
        mShowGravity = showGravity;
    }

    public void setMarginLR(int marginLR) {
        mMarginLR = marginLR;
    }

    public void setCanceledOnTouch(boolean canceledOnTouch) {
        mIsCanceledOnTouch = canceledOnTouch;
        setCanceledOnTouchOutside(mIsCanceledOnTouch);
    }

    @Override
    public void show() {
        super.show();
        setGravity(mShowGravity);
    }

    public void show(int showGravity) {
        setShowGravity(showGravity);
        this.show();
    }

    protected abstract int getLayoutById();
    protected abstract void initViewAndData(Bundle savedInstanceState);
    protected void initEvent(){
        if(tv_submit != null) tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnClickCallback != null) {
                    mOnClickCallback.onRightClickListener(v);
                }
            }
        });
        if(tv_cancel != null) tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnClickCallback != null) {
                    mOnClickCallback.onLeftClickListener(v);
                }
                dismiss();
            }
        });
    }

    public void setOnClickListener(OnClickCallback onClickCallback) {
        mOnClickCallback = onClickCallback;
    }

    public View getView() {
        return mView;
    }
}
