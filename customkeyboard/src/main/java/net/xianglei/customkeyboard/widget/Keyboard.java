package net.xianglei.customkeyboard.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import net.xianglei.customkeyboard.R;
import net.xianglei.customkeyboard.listener.KeyListener;

/**
 * Author:xianglei
 * Date: 2019-12-10 18:03
 * Description:
 */
public class Keyboard extends android.support.v7.widget.AppCompatTextView {

    private static final String TAG = "Keyboard";
    private int code;
    //监听
    private KeyListener mListener;

    public Keyboard(Context context) {
        this(context, null);
    }

    public Keyboard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Keyboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DLKeyboard, defStyleAttr, 0);
        this.code = a.getInt(R.styleable.DLKeyboard_dl_keyboard_code, 0);
        if(code == 0 && getText().toString().length() == 1) code = getText().toString().getBytes()[0];
        a.recycle();
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(code == android.inputmethodservice.Keyboard.KEYCODE_SHIFT || code == android.inputmethodservice.Keyboard.KEYCODE_CANCEL)
            return super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setPressed(true);
                Log.d(TAG, "onTouchEvent: down" + code);
                if(mListener != null) mListener.onPress(code);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "onTouchEvent: up" + code);
                if(mListener != null) mListener.onRelease(code);
                setPressed(false);
                break;
        }
        return true;
    }

    public void setListener(KeyListener listener) {
        mListener = listener;
    }
}
