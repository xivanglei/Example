package com.dalongtech.customkeyboard.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * ScrollView with scroll state change observer
 */
public class ObservableScrollView extends ScrollView {

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
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(mScrollCallback != null) mScrollCallback.onScroll();
    }

    public void setScrollCallback(ScrollCallback scrollCallback) {
        mScrollCallback = scrollCallback;
    }

    public interface ScrollCallback {
        void onScroll();
    }
}