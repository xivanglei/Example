package net.xianglei.testapplication.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Author:xianglei
 * Date: 2018/12/23 11:16 AM
 * Description:关闭滑动，主要应用于滑动冲突
 */
public class CustomLinearLayoutManager extends LinearLayoutManager {
    private boolean isScrollEnabled = true;

    public CustomLinearLayoutManager(Context context) {
        super(context);
    }

    public CustomLinearLayoutManager(Context context, boolean flag) {
        this(context);
        setScrollEnabled(isScrollEnabled);
    }
    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically();
    }
}
