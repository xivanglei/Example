package com.dalongtech.testapplication.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.dalongtech.testapplication.utils.LogUtil;

/**
 * Author:xianglei
 * Date: 2019-07-25 08:57
 * Description:
 */
public class MyScrollView extends ScrollView {

    private boolean mIsNeedScroll = false;
    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr, 0);
    }

    public void setNeedScroll(boolean isNeedScroll) {
        mIsNeedScroll = isNeedScroll;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        LogUtil.d("总事件" + ev.getAction());
        if(mIsNeedScroll) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (getParent() != null) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                    break;
            }
        }

        return super.onTouchEvent(ev);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        if(clampedY) {
            if(getParent() != null) {
                getParent().requestDisallowInterceptTouchEvent(false);
            }
        }
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
    }
}
