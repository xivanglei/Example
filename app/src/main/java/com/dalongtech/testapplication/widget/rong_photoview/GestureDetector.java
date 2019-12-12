//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.dalongtech.testapplication.widget.rong_photoview;

import android.view.MotionEvent;

public interface GestureDetector {
    boolean onTouchEvent(MotionEvent var1);

    boolean isScaling();

    boolean isDragging();

    void setOnGestureListener(OnGestureListener var1);
}
