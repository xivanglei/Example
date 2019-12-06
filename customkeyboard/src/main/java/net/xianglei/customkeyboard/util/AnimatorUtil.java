package net.xianglei.customkeyboard.util;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.view.View;

/**
 * Author:xianglei
 * Date: 2019-12-03 14:46
 * Description:动画帮助类
 */
public class AnimatorUtil {

    public static void yScroll(final View view, final int duration, int from, int to) {
        yScroll(view, duration, from, to, null);
    }

    public static void yScroll(final View view, final int duration, int from, int to, TimeInterpolator interpolator) {
        yScroll(view, duration, from, to, interpolator, null);
    }

    public static void yScroll(final View view, final int duration, int from, int to, TimeInterpolator interpolator, Animator.AnimatorListener animatorListener) {
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(view,"translationY", from, to);
        objectAnimator1.setDuration(duration);
        if(interpolator != null) objectAnimator1.setInterpolator(interpolator);
        if(animatorListener != null) objectAnimator1.addListener(animatorListener);
        objectAnimator1.start();
    }
}
