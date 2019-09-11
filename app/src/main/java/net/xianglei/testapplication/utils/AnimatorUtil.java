package net.xianglei.testapplication.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

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
}
