package net.xianglei.testapplication.widget;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import net.xianglei.testapplication.common.callback.SimpleCallback;
import net.xianglei.testapplication.utils.LogUtil;
import net.xianglei.testapplication.utils.ScreenUtil;

/**
 * Author:xianglei
 * Date: 2019-10-19 13:59
 * Description:下滑关闭布局
 */
public class SlideCloseFrameLayout extends FrameLayout implements GestureDetector.OnGestureListener {

    private GestureDetector detector;
    private int screenHeight;//设备屏幕高度
    private float oldX, oldY;//手机放在屏幕的坐标
    private float alphaPercent = 1f;//背景颜色透明度
    private boolean isFinsh = false;//是否执行关闭页面的操作
    private SimpleCallback viewCall = null;
    private boolean isIntercept = true;
    private ObtainInterruptible mObtainInterruptible;
    private int lastInterceptX=0;
    private int lastInterceptY=0;
    private float mDiffX = 0;
    private float mDiffY = 0;

    public SlideCloseFrameLayout(@NonNull Context context) {
        this(context, null);
    }

    public SlideCloseFrameLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideCloseFrameLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SlideCloseFrameLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    public void setIntercept(boolean intercept) {
        isIntercept = intercept;
    }

    public void setObtainInterruptible(ObtainInterruptible obtainInterruptible) {
        mObtainInterruptible = obtainInterruptible;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                oldX = ev.getRawX();
                oldY = ev.getRawY();
                mDiffX = 0;
                mDiffY = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - lastInterceptX;
                int deltaY = y - lastInterceptY;
//                    LogUtil.d("移动了");
                if(deltaY > 0 && Math.abs(deltaY) - Math.abs(deltaX) > 0 && mObtainInterruptible != null && mObtainInterruptible.isInterruptible()) {
                    oldX = ev.getRawX();
                    oldY = ev.getRawY();
                    return true;
                }
                break;
        }
        lastInterceptX = x;
        lastInterceptY = y;
        return isIntercept || super.onInterceptTouchEvent(ev);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (isFinsh) {
                    isFinsh = false;
                    if (viewCall != null) {
                        viewCall.callback();
                    }
                } else {
                    setupUping();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                isFinsh = false;
                mDiffX += event.getRawX() - oldX;
                oldX = event.getRawX();
                mDiffY += event.getRawY() - oldY;
                oldY = event.getRawY();
                setupMoving(mDiffX, mDiffY);
                if (Math.abs(mDiffX) <= Math.abs(mDiffY)) {
                    if (mDiffY > (screenHeight / 6)) {
                        isFinsh = true;
                    }
                }
                return false;
        }
        return true;
    }

    private void initView(Context context) {
        screenHeight = ScreenUtil.getScreenH();
        detector = new GestureDetector(context, this);
    }

    private void setupUping() {
        animate().setDuration(200)
                .scaleX(1)
                .scaleY(1)
                .translationX(0)
                .translationY(0)
                .setUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        if (alphaPercent < animation.getAnimatedFraction()) {
                            ((ViewGroup) getParent()).setBackgroundColor(convertPercentToBlackAlphaColor(animation.getAnimatedFraction()));
                        }
                    }
                })
                .start();
    }

    private void setupMoving(float deltaX, float deltaY) {
        if (Math.abs(mDiffY) < (screenHeight / 4)) {
            float scale = 1 - Math.abs(mDiffY) / screenHeight;
            alphaPercent = 1 - Math.abs(deltaY) / (screenHeight / 2);
            setScaleX(scale);
            setScaleY(scale);
            ((ViewGroup) getParent()).setBackgroundColor(convertPercentToBlackAlphaColor(alphaPercent));
        }
        setTranslationX(deltaX);
        setTranslationY(deltaY);
    }

    //设置背景颜色透明度
    protected int convertPercentToBlackAlphaColor(float percent) {
        percent = Math.min(1, Math.max(0, percent));
        int intAlpha = (int) (percent * 255);
        String stringAlpha = Integer.toHexString(intAlpha).toLowerCase();
        String color = "#" + (stringAlpha.length() < 2 ? "0" : "") + stringAlpha + "000000";
//        LogUtil.d(color);
        return Color.parseColor(color);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        isFinsh = true;
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if(e1 == null || e2 == null) return false;
        float movX = e2.getRawX() - e1.getRawX();
        float movY = e2.getRawY() - e1.getRawY();
        Log.e("ldd------2", movX + "---------" + movY);
        if (Math.abs(movX) > Math.abs(movY)) {
            if (movX < 0) {
                Log.e("ldd------2", "左滑动");
            } else {
                Log.e("ldd------2", "右滑动");
            }
        } else {
            if (movY < 0) {
                Log.e("ldd------2", "上滑动");
            } else {
                isFinsh = true;
                Log.e("ldd------2", "下滑动");
            }
        }
        return true;
    }

    public void setViewCall(SimpleCallback viewCall) {
        this.viewCall = viewCall;
    }

    public interface ObtainInterruptible {
        boolean isInterruptible();
    }

}
