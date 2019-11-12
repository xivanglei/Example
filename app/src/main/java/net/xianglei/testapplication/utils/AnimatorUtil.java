package net.xianglei.testapplication.utils;

import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Author:xianglei
 * Date: 2019/1/7 10:04 AM
 * Description:动画
 */
public class AnimatorUtil {

    public static void setAlphaIn(View view, int duration) {
        if(view == null) return;
        view.setAlpha(0);
        ObjectAnimator.ofFloat(view, "alpha", 0, 1).setDuration(duration).start();
    }

    public static void setAlphaOut(View view, int duration) {
        if(view == null) return;
        view.setAlpha(1);
        ObjectAnimator.ofFloat(view, "alpha", 1, 0).setDuration(duration).start();
    }

    public static void alternateView(int duration, View... views) {
        for(View view : views) {
            if(view.getAlpha() < 0.5) {
                setAlphaIn(view, duration);
            } else {
                setAlphaOut(view, duration);
            }
        }

    }

    public static AnimatorSet upAlphaIn(View view, int duration, float width) {
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(view,"translationY",width, 0);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(view,"alpha",0,1);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(duration);
        set.play(objectAnimator1).with(objectAnimator2);
        set.start();
        return set;
    }

    public static ObjectAnimator translationY(View view, int duration, float from, float to) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view,"translationY",from, to);
        objectAnimator.start();
        return objectAnimator;
    }



    public static void xScroll(final View view, final int duration, int from, int to) {
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(view,"translationX", from, to);
        objectAnimator1.setDuration(duration);
        objectAnimator1.start();
    }

    /**
     * 模拟入场动画
     */
    public static void showBigImageTransformAnim(View view, float radio, float translationX, float translationY, boolean isIn, AnimatorListenerAdapter listenerAdapter) {
        float[] values = isIn ? new float[]{0f, 1f} : new float[]{1f, 0f};

        final ValueAnimator animator = ValueAnimator.ofFloat(values).setDuration(150);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                view.setAlpha(1f);
                view.setScaleX(radio + (1 - radio) * value);
                view.setScaleY(radio + (1 - radio) * value);
                view.setTranslationX(-translationX * (1f - value));
                view.setTranslationY(-translationY * (1f - value));
            }
        });
        if(listenerAdapter != null) animator.addListener(listenerAdapter);
        animator.start();
    }
}
