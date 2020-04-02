package com.dalongtech.testapplication.activity.material;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import com.dalongtech.testapplication.utils.LogUtil;

/**
 * Created by Administrator on 2016/9/26 0026.
 *继承CoordinatorLayout.Behavior<View>
 *
 */

public class FooterBehaviorAppBar extends CoordinatorLayout.Behavior<View> {

    public FooterBehaviorAppBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof AppBarLayout;
    }
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        //得到AppBarLayout一共移动了多少
        float translationY = dependency.getY();
        LogUtil.d(translationY);
        //child就是自己，想怎么移动就怎么移动
        child.setTranslationY(Math.max(0, translationY * 1.5f));
        return true;
    }
}