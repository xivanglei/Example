package net.xianglei.customkeyboard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import net.xianglei.customkeyboard.constants.KeyConst;
import net.xianglei.customkeyboard.listener.KBOnTouchListener;
import net.xianglei.customkeyboard.listener.KeyListener;
import net.xianglei.customkeyboard.util.AnimatorUtil;
import net.xianglei.customkeyboard.util.TransformCodeUtil;
import net.xianglei.customkeyboard.widget.DLKeyBoardView;

import java.util.Arrays;
import java.util.List;

/**
 * Author:xianglei
 * Date: 2019-12-02 19:03
 * Description:自定义软键盘单例，退出时需要掉release释放资源
 */
public class DLKeyboard implements KeyboardView.OnKeyboardActionListener {

    private static final String TAG = "DLKeyboard";
    private static volatile DLKeyboard instance;

    private static final int INPUT_TYPE_BASE = 1001;
    private static final int INPUT_TYPE_SYMBOL = 1002;
    private static final int INPUT_TYPE_WIN = 1003;
    private static final int INPUT_TYPE_SYMBOL_2 = 1004;
    private static final int INPUT_TYPE_WIN_2 = 1005;

    public static final int STATUS_OPEN = 1;
    public static final int STATUS_CLOSE = 0;

    private Activity mActivity;
    //自定义软键盘View
    private DLKeyBoardView mDLKeyBoardView;
    private View mRootView;
    //记录键盘打开状态
    private int mOpenStatus = STATUS_CLOSE;
    //键盘类型
    private int mInputType;
    //转换windows码工具
    private TransformCodeUtil mCodeUtil;
    //监听
    private KeyListener mListener;

    private DLKeyboard(Activity activity) {
        mActivity = activity;
        if(mCodeUtil == null) mCodeUtil = new TransformCodeUtil();
    }

    //单例，静态强引用activity,页面销毁必须调用release释放，否则内存泄漏
    public static DLKeyboard getInstance(Activity activity) {
        if(instance == null) {
            synchronized (DLKeyboard.class) {
                if(instance == null) {
                    instance = new DLKeyboard(activity);
                } else {
                    instance.checkNewActivity(activity);
                }
            }
        } else {
            instance.checkNewActivity(activity);
        }
        return instance;
    }

    //设置监听，showKeyboard前必须设置，否则报错
    public DLKeyboard setListener(KeyListener listener) {
        mListener = listener;
        return this;
    }

    //打开软键盘,不需要判断打开状态，可以直接调用
    public void showKeyboard() {
        if(mListener == null)
            throw new IllegalStateException("Listening must be set");
        if(mOpenStatus == STATUS_OPEN && mRootView != null) return;
        mOpenStatus = STATUS_OPEN;
        if(mActivity == null) return;
        if(mDLKeyBoardView == null) {
            mRootView = View.inflate(mActivity, R.layout.dl_view_keyboard, null);
            ((FrameLayout) mActivity.getWindow().getDecorView()).addView(mRootView);
            mDLKeyBoardView = mRootView.findViewById(R.id.kb_custom_keyboard);
            mDLKeyBoardView.setOnTouchListener(new KBOnTouchListener());
            mDLKeyBoardView.setOnKeyboardActionListener(this);
            setInputType(INPUT_TYPE_BASE);
        }
        AnimatorUtil.yScroll(mRootView, 250, dp2px(280), 0, new DecelerateInterpolator());
    }

    //隐藏软键盘,不需要判断打开状态，可以直接调用
    public void hideKeyboard() {
        if(mOpenStatus == STATUS_CLOSE) return;
        mOpenStatus = STATUS_CLOSE;
        if(mRootView == null) return;
        AnimatorUtil.yScroll(mRootView, 250,  0, dp2px(280), new DecelerateInterpolator(), new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                ((FrameLayout) mActivity.getWindow().getDecorView()).removeView(mRootView);
                mRootView = null;
                mDLKeyBoardView = null;
            }
        });
    }

    //释放内存，必须调用，否则引起内存泄漏
    public void release() {
        if(mRootView != null) {
            ((FrameLayout) mActivity.getWindow().getDecorView()).removeView(mRootView);
            mRootView = null;
        }
        mDLKeyBoardView = null;
        mActivity = null;
        Log.d(TAG, "release: ");
    }

    //是否是新的activity,如果是就需要把老的activity释放，再通过这activity添加View
    private void checkNewActivity(Activity activity) {
        Log.d(TAG, "checkNewActivity: 开始");
        if(mActivity == activity) return;
        release();
        mActivity = activity;
        Log.d(TAG, "checkNewActivity: 结束");
    }

    //设置键盘类型
    private void setInputType(int inputType) {
        mInputType = inputType;
        Keyboard keyboard = null;
        if (inputType == INPUT_TYPE_BASE) {
            keyboard = new Keyboard(mActivity, R.xml.dl_key_board_base);
        } else if(inputType == INPUT_TYPE_SYMBOL) {
            keyboard = new Keyboard(mActivity, R.xml.dl_key_board_symbol);
        } else if(inputType == INPUT_TYPE_WIN) {
            keyboard = new Keyboard(mActivity, R.xml.dl_key_board_win);
        } else if(inputType == INPUT_TYPE_SYMBOL_2) {
            keyboard = new Keyboard(mActivity, R.xml.dl_key_board_symbol_2);
        } else if(inputType == INPUT_TYPE_WIN_2) {
            keyboard = new Keyboard(mActivity, R.xml.dl_key_board_win_2);
        }
        mDLKeyBoardView.setKeyboard(keyboard);
    }

    private int dp2px(float dpValue) {
        float scale = mActivity.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5F);
    }

    @Override
    public void onPress(int primaryCode) {
        Log.d(TAG, "onPress: " + primaryCode);
        setPreviewEnabled(primaryCode);
        int code = mCodeUtil.changeCapitalAlphabetIfNeed(primaryCode, mDLKeyBoardView.getKeyboard().isShifted());
        callback(mCodeUtil.transformCode(code, true), true);
    }

    @Override
    public void onRelease(int primaryCode) {
        if(primaryCode == KeyConst.KEY_WWW) {
            clickKey(KeyConst.KEY_w);
            clickKey(KeyConst.KEY_w);
            clickKey(KeyConst.KEY_w);
            clickKey(KeyConst.KEY_DOT);
        } else if(primaryCode == KeyConst.KEY_COM) {
            clickKey(KeyConst.KEY_DOT);
            clickKey(KeyConst.KEY_c);
            clickKey(KeyConst.KEY_o);
            clickKey(KeyConst.KEY_m);
        } else {
            int code = mCodeUtil.changeCapitalAlphabetIfNeed(primaryCode, mDLKeyBoardView.getKeyboard().isShifted());
            callback(mCodeUtil.transformCode(code, false), false);
        }
        Log.d(TAG, "onRelease: " + primaryCode);
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        Log.d(TAG, "onKey: " + primaryCode);
        switch (primaryCode) {
            case Keyboard.KEYCODE_CANCEL:
                hideKeyboard();
                break;
            case KeyConst.KEY_FUNCTION_WIN:
                setInputType(INPUT_TYPE_WIN);
                break;
            case KeyConst.KEY_SYMBOL:
                setInputType(INPUT_TYPE_SYMBOL);
                break;
            case KeyConst.KEY_BACK:
                setInputType(INPUT_TYPE_BASE);
                break;
            case Keyboard.KEYCODE_SHIFT:
                Keyboard keyboard = mDLKeyBoardView.getKeyboard();
                boolean isShift = !keyboard.isShifted();
                keyboard.setShifted(isShift);
                keyboard.getKeys().get(keyboard.getShiftKeyIndex()).label = isShift ? "小写" : "大写";
                mDLKeyBoardView.invalidateAllKeys();
                break;
            case KeyConst.KEY_PREVIOUS_PAGE:
                if(mInputType == INPUT_TYPE_SYMBOL_2) {
                    setInputType(INPUT_TYPE_SYMBOL);
                } else if(mInputType == INPUT_TYPE_WIN_2) {
                    setInputType(INPUT_TYPE_WIN);
                }
                break;
            case KeyConst.KEY_NEXT_PAGE:
                if(mInputType == INPUT_TYPE_SYMBOL) {
                    setInputType(INPUT_TYPE_SYMBOL_2);
                } else if(mInputType == INPUT_TYPE_WIN) {
                    setInputType(INPUT_TYPE_WIN_2);
                }
                break;
        }
    }

    @Override
    public void onText(CharSequence text) {
        Log.d(TAG, "onText: " + text);
    }

    @Override
    public void swipeLeft() {
        Log.d(TAG, "swipeLeft: ");
    }

    @Override
    public void swipeRight() {
        Log.d(TAG, "swipeRight: ");
    }

    @Override
    public void swipeDown() {
        Log.d(TAG, "swipeDown: ");
    }

    @Override
    public void swipeUp() {
        Log.d(TAG, "swipeUp: ");
    }

    //设置是否预览
    private void setPreviewEnabled(int code) {
        if(mInputType == INPUT_TYPE_WIN || mInputType == INPUT_TYPE_WIN_2) {
            mDLKeyBoardView.setPreviewEnabled(false);
        } else {
            boolean isNeedShow = !Arrays.asList(
                    Keyboard.KEYCODE_SHIFT,
                    Keyboard.KEYCODE_DELETE,
                    KeyConst.KEY_SPACE,
                    KeyConst.KEY_LANGUAGE,
                    KeyConst.KEY_SYMBOL,
                    KeyConst.KEY_LINE_FEED,
                    Keyboard.KEYCODE_CANCEL,
                    Keyboard.KEYCODE_DONE,
                    KeyConst.KEY_BACK,
                    KeyConst.KEY_FUNCTION_WIN,
                    KeyConst.KEY_PREVIOUS_PAGE,
                    KeyConst.KEY_NEXT_PAGE,
                    KeyConst.KEY_ARROW_LEFT,
                    KeyConst.KEY_ARROW_RIGHT,
                    KeyConst.KEY_ARROW_UP,
                    KeyConst.KEY_ARROW_DOWN).contains(code);
            mDLKeyBoardView.setPreviewEnabled(isNeedShow);
        }
    }

    //返回 STATUS_OPEN 或 STATUS_CLOSE
    public int getOpenStatus() {
        return mOpenStatus;
    }

    //自动点击按键
    private void clickKey(int code) {
        callback(mCodeUtil.transformCode(code, true), true);
        callback(mCodeUtil.transformCode(code, false), false);
    }

    //返回
    private void callback(List<Integer> list, boolean isDown) {
        for(int i : list) {
            if(i == KeyConst.NO_FIND_KEY) continue;
            if(isDown) {
                mListener.onPress(i);
            } else {
                mListener.onRelease(i);
            }

        }
    }
}
