package com.dalongtech.customkeyboard.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dalongtech.customkeyboard.DLKeyboard;
import com.dalongtech.customkeyboard.R;
import com.dalongtech.customkeyboard.constants.KeyConst;
import com.dalongtech.customkeyboard.listener.KeyListener;
import com.dalongtech.customkeyboard.util.AnimatorUtil;
import com.dalongtech.customkeyboard.util.TransformCodeUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author:xianglei
 * Date: 2019-12-14 18:38
 * Description:
 */
public class DLKeyboardView extends FrameLayout implements Keyboard.OnKeyActionListener, View.OnClickListener {

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

    private boolean mAutoClickBlankHide = true;

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
    private TextView mPreviewText;
    private int[] mPreviewLocation;
    private boolean mCtrlLIsDown;
    private boolean mCtrlRIsDown;
    private boolean mAltLIsDown;
    private boolean mAltRIsDown;

    private int[] mClickableViews = new int[] {
            R.id.kb_shift, R.id.kb_cancel, R.id.kb_symbol, R.id.kb_win, R.id.kb_symbol_next, R.id.kb_symbol_return, R.id.kb_symbol_2_return, R.id.kb_symbol_2_previous, R.id.kb_win_next, R.id.kb_win_return, R.id.kb_win_2_return, R.id.kb_win_2_previous, R.id.kb_ctrl_l, R.id.kb_ctrl_r, R.id.kb_alt_l, R.id.kb_alt_r
    };

    private View mKeyboardBase;
    private View mKeyboardSymbol;
    private View mKeyboardSymbol2;
    private View mKeyboardWin;
    private View mKeyboardWin2;

    private Keyboard mKbCtrlL;
    private Keyboard mKbCtrlR;
    private Keyboard mKbAltL;
    private Keyboard mKbAltR;
    //主键盘上的大小写切换，不算普通的shift按键
    private Keyboard mBaseShiftView;

    public DLKeyboardView(@NonNull Context context) {
        this(context, null);
    }

    public DLKeyboardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DLKeyboardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mCodeUtil = new TransformCodeUtil();
        LayoutInflater.from(context).inflate(R.layout.dl_view_keyboard, this, true);
        mPreviewText = findViewById(R.id.tv_preview);
        setVisibilityToView(mPreviewText, false);
        initEvent();
        initCustomEvent();
        initContainerView();
        setInputType(INPUT_TYPE_BASE);
        findViewById(R.id.view_hide_keyboard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAutoClickBlankHide) hideKeyboard();
            }
        });
        setInputType(INPUT_TYPE_BASE);
        setVisibilityToView(this, false);
    }

    public void showKeyboard() {
        if(mOpenStatus == STATUS_OPEN && getTranslationY() < dp2px(280)) return;
        mOpenStatus = STATUS_OPEN;
        setVisibilityToView(this, true);
        AnimatorUtil.yScroll(this, 250, dp2px(280), 0, new DecelerateInterpolator());
    }

    //隐藏软键盘,不需要判断打开状态，可以直接调用
    public void hideKeyboard() {
        if(mOpenStatus == STATUS_CLOSE) return;
        resetKeyStatus();
        mOpenStatus = STATUS_CLOSE;
        AnimatorUtil.yScroll(this, 250,  0, dp2px(280), new DecelerateInterpolator());
    }

    public void setAutoClickBlankHide(boolean autoClickBlankHide) {
        mAutoClickBlankHide = autoClickBlankHide;
    }

    private void resetKeyStatus() {
        if(mCtrlLIsDown) onClick(mKbCtrlL);
        if(mCtrlRIsDown) onClick(mKbCtrlR);
        if(mAltLIsDown) onClick(mKbAltL);
        if(mAltRIsDown) onClick(mKbAltR);
        if(mShiftIsOpen) changeShiftStatus();
    }

    private void initEvent() {
        if(mAlphaKeys == null) {
            mAlphaKeys = new ArrayList<>();
        } else {
            mAlphaKeys.clear();
        }
        FrameLayout rootContainer = findViewById(R.id.fl_key_root_container);
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
            findViewById(id).setOnClickListener(this);
        }
        mKbCtrlL = findViewById(R.id.kb_ctrl_l);
        mKbCtrlR = findViewById(R.id.kb_ctrl_r);
        mKbAltL = findViewById(R.id.kb_alt_l);
        mKbAltR = findViewById(R.id.kb_alt_r);
        mBaseShiftView = findViewById(R.id.kb_shift);
    }

    private void initContainerView() {
        mKeyboardBase = findViewById(R.id.ll_keyboard_base);
        mKeyboardSymbol = findViewById(R.id.ll_keyboard_symbol);
        mKeyboardSymbol2 = findViewById(R.id.ll_keyboard_symbol_2);
        mKeyboardWin = findViewById(R.id.ll_keyboard_win);
        mKeyboardWin2 = findViewById(R.id.ll_keyboard_win_2);
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
                changeShiftStatus();
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
            case KeyConst.KEY_CTRL_L:
            case KeyConst.KEY_CTRL_R:
            case KeyConst.KEY_ALT_L:
            case KeyConst.KEY_ALT_R:
                changeHotKeyStatus((Keyboard) v);
                if(mShiftIsOpen) changeShiftStatus();
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

    private void changeShiftStatus() {
        mShiftIsOpen = !mShiftIsOpen;
        mBaseShiftView.setText(mShiftIsOpen ? "小写" : "大写");
        changeCapitalAlphabet();
    }

    private void changeHotKeyStatus(Keyboard key) {
        int code = key.getCode();
        switch (key.getCode()) {
            case KeyConst.KEY_CTRL_L:
                mCtrlLIsDown = !mCtrlLIsDown;
                changeNormalCancelBg(key, mCtrlLIsDown);
                callback(mCodeUtil.transformCode(code, mCtrlLIsDown), mCtrlLIsDown);
                break;
            case KeyConst.KEY_CTRL_R:
                mCtrlRIsDown = !mCtrlRIsDown;
                changeNormalCancelBg(key, mCtrlRIsDown);
                callback(mCodeUtil.transformCode(code, mCtrlRIsDown), mCtrlRIsDown);
                break;
            case KeyConst.KEY_ALT_L:
                mAltLIsDown = !mAltLIsDown;
                changeNormalCancelBg(key, mAltLIsDown);
                callback(mCodeUtil.transformCode(code, mAltLIsDown), mAltLIsDown);
                break;
            case KeyConst.KEY_ALT_R:
                mAltRIsDown = !mAltRIsDown;
                changeNormalCancelBg(key, mAltRIsDown);
                callback(mCodeUtil.transformCode(code, mAltRIsDown), mAltRIsDown);
                break;
        }
    }

    //改变Ctrl 与 Alt 正常状态与取消状态的背景
    private void changeNormalCancelBg(Keyboard keyboard, boolean isDown) {
        keyboard.setBackgroundResource(isDown ? R.drawable.dl_key_bg_cancel : R.drawable.dl_key_bg);
    }


    private int dp2px(float dpValue) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5F);
    }

    @Override
    public void onPress(Keyboard key, int primaryCode) {
        if(mCodeUtil.isNeedShift(primaryCode)) {
            if(mCtrlLIsDown) changeHotKeyStatus(mKbCtrlL);
            if(mCtrlRIsDown) changeHotKeyStatus(mKbCtrlR);
            if(mAltLIsDown) changeHotKeyStatus(mKbAltL);
            if(mAltRIsDown) changeHotKeyStatus(mKbAltR);
        }
//        Log.d(TAG, "onPress: " + primaryCode);
        showPreviewIfNeed(key, primaryCode);
        callback(mCodeUtil.transformCode(primaryCode, true), true);
    }

    @Override
    public void onRelease(int primaryCode) {
        setVisibilityToView(mPreviewText, false);
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
//        Log.d(TAG, "onRelease: " + primaryCode);
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
            mPreviewText.setText(key.getText().toString());
            showPreview(key);
        }
    }

    private void showPreview(Keyboard key) {
        int[] keyLocation = new int[2];
        if(mPreviewLocation == null) {
            mPreviewLocation = new int[2];
            mPreviewText.getLocationOnScreen(mPreviewLocation);
        }
        key.getLocationOnScreen(keyLocation);
        int x = keyLocation[0] + key.getWidth() / 2 - dp2px(80) / 2 - mPreviewLocation[0];
        mPreviewText.setTranslationX(x);
        mPreviewText.setTranslationY(keyLocation[1] - dp2px(120));
        setVisibilityToView(mPreviewText, true);
    }

    //返回 STATUS_OPEN 或 STATUS_CLOSE
    public int getOpenStatus() {
        return mOpenStatus;
    }

    public void setListener(KeyListener listener) {
        mListener = listener;
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
                if(mListener != null) mListener.onPress(i);
            } else {
                if(mListener != null) mListener.onRelease(i);
            }
        }
    }

    private void setVisibilityToView(View v, boolean isShow) {
        v.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

}
