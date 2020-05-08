package com.dalongtech.testapplication.widget.flexiblelayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.dalongtech.testapplication.R;
import com.dalongtech.testapplication.utils.LogUtil;
import com.dalongtech.testapplication.widget.flexiblelayout.callback.OnPullListener;
import com.dalongtech.testapplication.widget.flexiblelayout.callback.OnReadyPullListener;
import com.dalongtech.testapplication.widget.flexiblelayout.callback.OnRefreshListener;
import com.dalongtech.testapplication.widget.flexiblelayout.util.PullAnimatorUtil;

/**
 * Created by gavin
 * date 2018/6/12
 * 带有下拉放大效果的FrameLayout
 */
public class FlexibleLayout extends FrameLayout implements IFlexible {

    /**
     * 是否允许下拉放大
     */
    private boolean isEnable = true;

    /**
     * 是否允许下拉刷新
     */
    private boolean isRefreshable = false;

    /**
     * 头部高度
     */
    private int mHeaderHeight = 0;

    /**
     * 头部宽度
     */
    private int mHeaderWidth = 0;

    /**
     * topMargin
     */
    private int mTopMargin = 0;

    /**
     * 头部size ready
     */
    private boolean mHeaderSizeReady;

    /**
     * 头部
     */
    private View mHeaderView;

    /**
     * 刷新
     */
    private View mRefreshView;

    /**
     * 刷新View的宽高
     */
    private int mRefreshSize = getScreenWidth() / 15;

    /**
     * 最大头部下拉高度
     */
    private int mMaxPullHeight = getScreenWidth() / 3;

    /**
     * 最大 刷新 下拉高度
     */
    private int mMaxRefreshPullHeight = getScreenWidth() / 3;

    /**
     * true 开始下拽
     */
    private boolean mIsBeingDragged;

    /**
     * 标志：正在刷新
     */
    private boolean mIsRefreshing;

    /**
     * 准备下拉监听
     */
    private OnReadyPullListener mListener;

    /**
     * 刷新监听
     */
    private OnRefreshListener mRefreshListener;

    /**
     * 初始坐标
     */
    private float mInitialY, mInitialX;

    /**
     * 记录当前活动的手指id
     */
    private int mActivePointerId;

    private int mDiffY = 0;

    /**
     * 下拉监听
     */
    private OnPullListener mOnPullListener;

    /**
     * 刷新动画消失监听
     */
    private RefreshAnimatorListener mRefreshAnimatorListener = new RefreshAnimatorListener();

    public FlexibleLayout(@NonNull Context context) {
        this(context, null);
    }

    public FlexibleLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlexibleLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mIsRefreshing = false;
        mHeaderSizeReady = false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        LogUtil.d("onInterceptTouchEvent");
        if (isEnable && isHeaderReady() && isReady()) {
            switch (ev.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    mActivePointerId = ev.getPointerId(0);
                    LogUtil.d(mActivePointerId);
                    mInitialX = ev.getX(0);
                    mInitialY = ev.getY(0);
                    mIsBeingDragged = false;
                    mDiffY = 0;
                    break;
                case MotionEvent.ACTION_MOVE:
//                    log("onInterceptTouchEvent MOVE");
                    final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                    float diffY = ev.getY(pointerIndex) - mInitialY;
                    float diffX = ev.getX(pointerIndex) - mInitialX;
                    if (diffY > 0 && diffY / Math.abs(diffX) > 2) {
                        mIsBeingDragged = true;
//                        log("onInterceptTouchEvent return true");
                        return true;
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    break;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isEnable && isHeaderReady() && isReady()) {
            switch (ev.getActionMasked()) {
                case MotionEvent.ACTION_POINTER_DOWN:
//                    LogUtil.d("其他手指按下了");
                    mActivePointerId = ev.getPointerId(ev.getActionIndex());
//                    LogUtil.d(mActivePointerId);
                    mInitialX = ev.getX(ev.getActionIndex());
                    mInitialY = ev.getY(ev.getActionIndex());
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mIsBeingDragged) {
                        final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                        mDiffY += (ev.getY(pointerIndex) - mInitialY);
                        mInitialY = ev.getY(pointerIndex);
                        changeHeader((int) mDiffY);
                        changeRefreshView(getRefreshViewOffsetY(mDiffY));
                        if (mOnPullListener != null) {
                            mOnPullListener.onPull((int) mDiffY);
                        }
//                        log("onTouchEvent return true");
                        //return true;
                    }
                    break;
                case MotionEvent.ACTION_POINTER_UP:
//                    LogUtil.d("其他手指抬起了");
                    if(mActivePointerId == ev.getPointerId(ev.getActionIndex())) {
                        final int newPointerIndex = ev.getActionIndex() == 0 ? 1 : 0;
                        mActivePointerId = ev.getPointerId(newPointerIndex);
                        mInitialX = ev.getX(newPointerIndex);
                        mInitialY = ev.getY(newPointerIndex);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
//                    LogUtil.d("抬起了");
                    if (mIsBeingDragged) {
                        mIsBeingDragged = false;
                        resetHeader();
                        if (mOnPullListener != null) {
                            mOnPullListener.onRelease();
                        }
                        //刷新操作
//                        float diffY = ev.getY() - mInitialY;
                        changeRefreshViewOnActionUp(getRefreshViewOffsetY(mDiffY));
                        return true;
                    }
                    break;
            }
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean isReady() {
        return mListener != null && mListener.isReady();
    }

    @Override
    public boolean isHeaderReady() {
        return mHeaderView != null && mHeaderSizeReady;
    }

    @Override
    public void changeHeader(int offsetY) {
        PullAnimatorUtil.pullAnimator(mHeaderView, mHeaderHeight, mHeaderWidth, offsetY, mMaxPullHeight, mTopMargin);
    }

    @Override
    public void resetHeader() {
        LogUtil.d("触发了动画");
        PullAnimatorUtil.resetAnimator(mHeaderView, mHeaderHeight, mHeaderWidth, mTopMargin);
    }

    @Override
    public void changeRefreshView(int offsetY) {
        if (!isRefreshable || mRefreshView == null || isRefreshing()) {
            return;
        }
        PullAnimatorUtil.pullRefreshAnimator(mRefreshView, offsetY, mRefreshSize, mMaxRefreshPullHeight);
    }

    @Override
    public void changeRefreshViewOnActionUp(int offsetY) {
        if (!isRefreshable || mRefreshView == null || isRefreshing()) {
            return;
        }
        mIsRefreshing = true;
        if (offsetY > mMaxRefreshPullHeight) {
            PullAnimatorUtil.onRefreshing(mRefreshView);
            if (mRefreshListener != null) {
                mRefreshListener.onRefreshing();
            }
        } else {
            PullAnimatorUtil.resetRefreshView(mRefreshView, mRefreshSize, mRefreshAnimatorListener);
        }
    }

    @Override
    public void onRefreshComplete() {
        if (!isRefreshable || mRefreshView == null) {
            return;
        }
        PullAnimatorUtil.resetRefreshView(mRefreshView, mRefreshSize, mRefreshAnimatorListener);
    }


    @Override
    public boolean isRefreshing() {
        return mIsRefreshing;
    }

    /**
     * 是否允许下拉放大
     *
     * @param isEnable
     * @return
     */
    public FlexibleLayout setEnable(boolean isEnable) {
        this.isEnable = isEnable;
        return this;
    }

    /**
     * 是否允许下拉刷新
     *
     * @param isEnable
     * @return
     */
    public FlexibleLayout setRefreshable(boolean isEnable) {
        this.isRefreshable = isEnable;
        return this;
    }

    /**
     * 设置头部
     *
     * @param header
     * @return
     */
    public FlexibleLayout setHeader(View header) {
        mHeaderView = header;
        mHeaderView.post(new Runnable() {
            @Override
            public void run() {
                mHeaderHeight = mHeaderView.getHeight();
                mHeaderWidth = mHeaderView.getWidth();
                if(mHeaderView.getLayoutParams() instanceof FrameLayout.LayoutParams) {
                    mTopMargin = ((LayoutParams)mHeaderView.getLayoutParams()).topMargin;
                }
                mHeaderSizeReady = true;
            }
        });
        return this;
    }

    /**
     * Header最大下拉高度
     *
     * @param height
     * @return
     */
    public FlexibleLayout setMaxPullHeight(int height) {
        mMaxPullHeight = height;
        return this;
    }

    /**
     * 刷新控件 最大下拉高度
     *
     * @param height
     * @return
     */
    public FlexibleLayout setMaxRefreshPullHeight(int height) {
        mMaxRefreshPullHeight = height;
        return this;
    }

    /**
     * 设置刷新View的尺寸（正方形）
     *
     * @param size
     * @return
     */
    public FlexibleLayout setRefreshSize(int size) {
        mRefreshSize = size;
        return this;
    }

    public int getRefreshViewOffsetY(int offsetY) {
        return (int) Math.min(mHeaderHeight * 0.7, offsetY);
    }

    /**
     * 设置刷新View
     *
     * @param refreshView
     * @param listener
     * @return
     */
    public FlexibleLayout setRefreshView(View refreshView, OnRefreshListener listener) {
        if (mRefreshView != null) {
            removeView(mRefreshView);
        }
        mRefreshView = refreshView;
        mRefreshListener = listener;
        FrameLayout.LayoutParams layoutParams = new LayoutParams(mRefreshSize, mRefreshSize);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        mRefreshView.setLayoutParams(layoutParams);
        mRefreshView.setTranslationY(-mRefreshSize);
        addView(mRefreshView);
        return this;
    }

    /**
     * 设置默认的刷新头
     *
     * @param listener
     * @return
     */
    public FlexibleLayout setRefreshView(OnRefreshListener listener) {
        ImageView refreshView = new ImageView(getContext());
        refreshView.setImageResource(R.mipmap.flexible_loading);
        return setRefreshView(refreshView, listener);
    }

    /**
     * 监听 是否可以下拉放大
     *
     * @param listener
     * @return
     */
    public FlexibleLayout setReadyListener(OnReadyPullListener listener) {
        mListener = listener;
        return this;
    }

    /**
     * 设置下拉监听
     *
     * @param onPullListener
     * @return
     */
    public FlexibleLayout setOnPullListener(OnPullListener onPullListener) {
        mOnPullListener = onPullListener;
        return this;
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    private int getScreenWidth() {
        WindowManager mWindowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        if (mWindowManager != null) {
            mWindowManager.getDefaultDisplay().getMetrics(metrics);
            return metrics.widthPixels;
        } else {
            return 300;
        }
    }

    private void log(String str) {
        LogUtil.d(str);
    }

    class RefreshAnimatorListener extends AnimatorListenerAdapter {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            mIsRefreshing = false;
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            super.onAnimationCancel(animation);
            mIsRefreshing = false;
        }
    }
}
