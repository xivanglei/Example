package net.xianglei.customkeyboard;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import net.xianglei.customkeyboard.constants.KeyConst;
import net.xianglei.customkeyboard.listener.KeyListener;
import net.xianglei.customkeyboard.popwindow.KeyboardPreviewPop;
import net.xianglei.customkeyboard.util.AnimatorUtil;
import net.xianglei.customkeyboard.util.TransformCodeUtil;
import net.xianglei.customkeyboard.widget.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author:xianglei
 * Date: 2019-12-02 19:03
 * Description:自定义软键盘单例，退出时需要掉release释放资源
 */
public class DLKeyboard implements Keyboard.OnKeyActionListener, View.OnClickListener {

    private static final String TAG = "DLKeyboard";
    private static volatile DLKeyboard instance;

    private static final int INPUT_TYPE_BASE = 1001;
    private static final int INPUT_TYPE_SYMBOL = 1002;
    private static final int INPUT_TYPE_WIN = 1003;
    private static final int INPUT_TYPE_SYMBOL_2 = 1004;
    private static final int INPUT_TYPE_WIN_2 = 1005;

    public static final int STATUS_OPEN = 1;
    public static final int STATUS_CLOSE = 0;

    private List<Keyboard> mAlphaKeys;

    private Activity mActivity;
    private View mRootView;
    //记录键盘打开状态
    private int mOpenStatus = STATUS_CLOSE;
    //键盘类型
    private int mInputType;
    //转换windows码工具
    private TransformCodeUtil mCodeUtil;
    //监听
    private KeyListener mListener;
    // 大小写key 记录shift状态
    private boolean mShiftIsOpen;
    //预览框
    private KeyboardPreviewPop mPreviewPop;

    private int[] mClickableViews = new int[] {
            R.id.kb_shift, R.id.kb_cancel, R.id.kb_symbol, R.id.kb_win, R.id.kb_symbol_next, R.id.kb_symbol_return, R.id.kb_symbol_2_return, R.id.kb_symbol_2_previous, R.id.kb_win_next, R.id.kb_win_return, R.id.kb_win_2_return, R.id.kb_win_2_previous
    };

    private View mKeyboardBase;
    private View mKeyboardSymbol;
    private View mKeyboardSymbol2;
    private View mKeyboardWin;
    private View mKeyboardWin2;

    private DLKeyboard(Activity activity) {
        mActivity = activity;
        mPreviewPop = new KeyboardPreviewPop(mActivity);
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
        if(mOpenStatus == STATUS_OPEN) return;
        mOpenStatus = STATUS_OPEN;
        if(mActivity == null) return;
        if(mRootView == null) {
            mRootView = View.inflate(mActivity, R.layout.dl_view_keyboard_2, null);
            ((FrameLayout) mActivity.getWindow().getDecorView()).addView(mRootView);
            initEvent(mRootView);
            initCustomEvent();
            initContainerView();
            setInputType(INPUT_TYPE_BASE);
        }
        AnimatorUtil.yScroll(mRootView, 250, dp2px(280), 0, new DecelerateInterpolator());
    }

    //隐藏软键盘,不需要判断打开状态，可以直接调用
    public void hideKeyboard() {
        if(mOpenStatus == STATUS_CLOSE) return;
        mOpenStatus = STATUS_CLOSE;
        if(mRootView == null) return;
        AnimatorUtil.yScroll(mRootView, 250,  0, dp2px(280), new DecelerateInterpolator());
    }

    private void initEvent(View view) {
        if(mAlphaKeys == null) {
            mAlphaKeys = new ArrayList<>();
        } else {
            mAlphaKeys.clear();
        }
        FrameLayout rootContainer = view.findViewById(R.id.fl_key_root_container);
        for(int i = 0; i < rootContainer.getChildCount(); i++) {
            LinearLayout singleKeyContainer = (LinearLayout) rootContainer.getChildAt(i);
            for(int j = 0; j < singleKeyContainer.getChildCount(); j++) {
                LinearLayout rowContainer = (LinearLayout) singleKeyContainer.getChildAt(j);
                for(int k = 0; k < rowContainer.getChildCount(); k++) {
                    View v = rowContainer.getChildAt(k);
                    if(v instanceof Keyboard) {
                        Keyboard key = (Keyboard) v;
                        key.setListener(this);
                        if(key.getCode() >= KeyConst.KEY_a && key.getCode() <= KeyConst.KEY_z) {
                            mAlphaKeys.add(key);
                        }
                    }
                }
            }
        }
    }

    private void initCustomEvent() {
        for(int id : mClickableViews) {
            mRootView.findViewById(id).setOnClickListener(this);
        }
    }

    private void initContainerView() {
        mKeyboardBase = mRootView.findViewById(R.id.ll_keyboard_base);
        mKeyboardSymbol = mRootView.findViewById(R.id.ll_keyboard_symbol);
        mKeyboardSymbol2 = mRootView.findViewById(R.id.ll_keyboard_symbol_2);
        mKeyboardWin = mRootView.findViewById(R.id.ll_keyboard_win);
        mKeyboardWin2 = mRootView.findViewById(R.id.ll_keyboard_win_2);
    }

    @Override
    public void onClick(View v) {
        int code = ((Keyboard)v).getCode();
        switch (code) {
            case android.inputmethodservice.Keyboard.KEYCODE_CANCEL:
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
            case android.inputmethodservice.Keyboard.KEYCODE_SHIFT:
                mShiftIsOpen = !mShiftIsOpen;
                ((Keyboard)v).setText(mShiftIsOpen ? "小写" : "大写");
                changeCapitalAlphabet();
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

    private void setInputType(int inputType) {
        mInputType = inputType;
        setVisibilityToView(mKeyboardBase, inputType == INPUT_TYPE_BASE);
        setVisibilityToView(mKeyboardSymbol, inputType == INPUT_TYPE_SYMBOL);
        setVisibilityToView(mKeyboardSymbol2, inputType == INPUT_TYPE_SYMBOL_2);
        setVisibilityToView(mKeyboardWin, inputType == INPUT_TYPE_WIN);
        setVisibilityToView(mKeyboardWin2, inputType == INPUT_TYPE_WIN_2);
    }

    private void changeCapitalAlphabet() {
        for(Keyboard keyboard : mAlphaKeys) {
            keyboard.setCode(keyboard.getCode() + (mShiftIsOpen ? -32 : 32));
            keyboard.setText(new String(new byte[] {(byte)keyboard.getCode()}));
        }
    }

    //释放内存，必须调用，否则引起内存泄漏
    public void release() {
        if(mRootView != null) {
            ((FrameLayout) mActivity.getWindow().getDecorView()).removeView(mRootView);
            mRootView = null;
        }
        mActivity = null;
        Log.d(TAG, "release: ");
    }

    //是否是新的activity,如果是就需要把老的activity释放，再通过这activity添加View
    private void checkNewActivity(Activity activity) {
        Log.d(TAG, "checkNewActivity: 开始");
        if(mActivity == activity) return;
        release();
        mActivity = activity;
        mPreviewPop = new KeyboardPreviewPop(mActivity);
        Log.d(TAG, "checkNewActivity: 结束");
    }


    private int dp2px(float dpValue) {
        float scale = mActivity.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5F);
    }

    @Override
    public void onPress(Keyboard key, int primaryCode) {
        Log.d(TAG, "onPress: " + primaryCode);
        showPreviewIfNeed(key, primaryCode);
        callback(mCodeUtil.transformCode(primaryCode, true), true);
    }

    @Override
    public void onRelease(int primaryCode) {
        mPreviewPop.dismiss();
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
            callback(mCodeUtil.transformCode(primaryCode, false), false);
        }
        Log.d(TAG, "onRelease: " + primaryCode);
    }

    //设置是否预览
    private void showPreviewIfNeed(Keyboard key, int code) {
        if(mInputType == INPUT_TYPE_WIN || mInputType == INPUT_TYPE_WIN_2) return;
        boolean isNeedShow = !Arrays.asList(
                android.inputmethodservice.Keyboard.KEYCODE_SHIFT,
                android.inputmethodservice.Keyboard.KEYCODE_DELETE,
                KeyConst.KEY_SPACE,
                KeyConst.KEY_LANGUAGE,
                KeyConst.KEY_SYMBOL,
                KeyConst.KEY_LINE_FEED,
                android.inputmethodservice.Keyboard.KEYCODE_CANCEL,
                android.inputmethodservice.Keyboard.KEYCODE_DONE,
                KeyConst.KEY_BACK,
                KeyConst.KEY_FUNCTION_WIN,
                KeyConst.KEY_PREVIOUS_PAGE,
                KeyConst.KEY_NEXT_PAGE).contains(code);
        if(isNeedShow) {
            mPreviewPop.setText(key.getText().toString());
            mPreviewPop.showPopupWindow(key);
        }
//        Log.d(TAG, "showPreviewIfNeed: ");

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

    private void setVisibilityToView(View v, boolean isShow) {
        v.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }
}
