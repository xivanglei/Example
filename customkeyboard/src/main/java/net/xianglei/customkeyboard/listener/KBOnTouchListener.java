package net.xianglei.customkeyboard.listener;

import android.view.MotionEvent;
import android.view.View;

/**
 * Author:xianglei
 * Date: 2019-12-03 14:44
 * Description:
 */
public class KBOnTouchListener implements View.OnTouchListener {

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            return true;
        }
        return false;
    }
}
