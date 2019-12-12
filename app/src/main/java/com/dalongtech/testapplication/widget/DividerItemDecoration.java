package com.dalongtech.testapplication.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dalongtech.testapplication.utils.LogUtil;
import com.dalongtech.testapplication.utils.ScreenUtil;

/**
 * Created by xianglei on 2018/4/9.
 */

public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private static final int[] ATTRS = new int[] {android.R.attr.listDivider};      //系统自带的分割线
    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;
    private Drawable mDivider;                                                      //画图样式
    private int mOrientation;
    private int mLineWidth;
    private int mMarginLeft;
    private int mMarginRight;

    public DividerItemDecoration(Context context, int orientation) {
        this(context, orientation, Color.parseColor("#f9f9f9"));
    }

    public DividerItemDecoration(Context context, int orientation, int color) {
        this(context, orientation, color, ScreenUtil.dp2px(1));
    }

    public DividerItemDecoration(Context context, int orientation, int color, int lineWidth) {
        mDivider = new ColorDrawable(color);                                           //获取样式
        mLineWidth = lineWidth;
        //设置先的透明度 0-255
        setOrientation(orientation);
    }

    public DividerItemDecoration(Context context, int orientation, int color, int lineWidth, int marginLeft, int marginRight) {
        mDivider = new ColorDrawable(color);                                           //获取样式
        mLineWidth = lineWidth;
        mMarginLeft = marginLeft;
        marginRight = mMarginRight;
        //设置先的透明度 0-255
        setOrientation(orientation);
    }

    public void setOrientation(int orientation) {
        if(orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }

    @Override
    public void onDraw(Canvas c,RecyclerView parent) {
        LogUtil.d("画几次");
        if(mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    private void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();
        final int childCount = parent.getChildCount();
        for(int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int left = child.getRight() + params.rightMargin;
            int right = left + mLineWidth;               //默认分割线的高度，高度跟宽度应该是一样的
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        for(int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mLineWidth ;
            mDivider.setBounds(left , top, right, bottom);   //setBounds(int left, int top, int right, int bottom),这个四参数指的是drawable将在被绘制在canvas的哪个矩形区域内
            mDivider.draw(c);
        }
    }

    //偏移位置，如果加了线而不偏移，就没有位置给线显示了，如果偏移过多，就会有空白背景流出来。通常只偏移需要的尺寸就行了
    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        if(mOrientation == VERTICAL_LIST) {
            outRect.set(0, 0, 0, mLineWidth);
        } else {
            outRect.set(0, 0, mLineWidth, 0);
        }
    }
}
