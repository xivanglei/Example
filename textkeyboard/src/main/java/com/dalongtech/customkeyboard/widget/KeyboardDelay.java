package com.dalongtech.customkeyboard.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Author:xianglei
 * Date: 2019-12-10 18:03
 * Description:
 */
public class KeyboardDelay extends Keyboard {

    public static final String TAG = "滑动";
    public static final int DOWN = 10;
    public static final int UP = 20;

    private ObservableScrollView mScrollView;

    private boolean mIsScroll = false;
    private boolean mDownIsExecute = false;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == DOWN) {
                Log.d(TAG, "handleMessage: " + mScrollView.getScrollY());
                if(mIsScroll) {
                    mDownIsExecute = false;
                } else {
                    Log.d(TAG, "handleMessage: down execute");
                    mDownIsExecute = true;
                    setPressed(true);
                    if(mListener != null) mListener.onPress(KeyboardDelay.this, code);
                }
            } else if(msg.what == UP) {
                if(!mDownIsExecute) {
                    Log.d(TAG, "handleMessage: 按下未执行");
                    return;
                }
                mDownIsExecute = false;
                Log.d(TAG, "handleMessage: up execute");
                if(mListener != null) mListener.onRelease(code);
                setPressed(false);
            }
        }
    };

    public KeyboardDelay(Context context) {
        this(context, null);
    }

    public KeyboardDelay(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KeyboardDelay(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(isCustomClick()) return super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "onTouchEvent: " + mScrollView.getScrollY());
                mIsScroll = false;
                if(mScrollView != null) {
                    mScrollView.setScrollCallback(mScrollCallback);
                }
//                Log.d(TAG, "onTouchEvent: down" + code);
                sendMessage(DOWN);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
//                Log.d(TAG, "onTouchEvent: " + "up");
                sendMessage(UP);
                break;
        }
        return true;
    }

    private void sendMessage(int what) {
        Message message = Message.obtain();
        message.what = what;
        mHandler.sendMessageDelayed(message, 100);
    }

    private ObservableScrollView.ScrollCallback mScrollCallback = new ObservableScrollView.ScrollCallback() {
        @Override
        public void onScroll() {
            mIsScroll = true;
            mHandler.removeMessages(DOWN);
//            Log.d(TAG, "onScroll: " + getCode());
        }
    };

    public void setScrollView(ObservableScrollView scrollView) {
        mScrollView = scrollView;
    }
}
