package com.dalongtech.customkeyboard.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * ScrollView with scroll state change observer
 */
public class ObservableScrollView extends ScrollView {

    private static final String TAG = "滑动";
    private ScrollCallback mScrollCallback;

    public ObservableScrollView(Context context) {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObservableScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_MOVE){
            Log.d(TAG, "onInterceptTouchEvent: move");
            if (mScrollCallback != null) mScrollCallback.onScroll();
        }
        return super.onInterceptTouchEvent(ev);
    }

    public void setScrollCallback(ScrollCallback scrollCallback) {
        mScrollCallback = scrollCallback;
    }

    public interface ScrollCallback {
        void onScroll();
    }
}