package com.dalongtech.customkeyboard.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.dalongtech.customkeyboard.R;
import com.dalongtech.customkeyboard.constants.KeyConst;

/**
 * Author:xianglei
 * Date: 2019-12-14 18:05
 * Description:
 */
public class DLKeyboardScrollView extends DLKeyboardView {

    private ObservableScrollView sv_symbol;
    private ObservableScrollView sv_win;

    public DLKeyboardScrollView(@NonNull Context context) {
        this(context, null);
    }

    public DLKeyboardScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DLKeyboardScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dl_view_keyboard_scroll;
    }

    @Override
    protected void bindView() {
        super.bindView();
        sv_symbol = findViewById(R.id.sv_symbol);
        sv_win = findViewById(R.id.sv_win);
    }

    @Override
    protected void initKeyEvent() {
        FrameLayout rootContainer = findViewById(R.id.fl_key_root_container);
        for(int i = 0; i < rootContainer.getChildCount(); i++) {
            LinearLayout singleKeyContainer = (LinearLayout) rootContainer.getChildAt(i);
            for(int j = 0; j < singleKeyContainer.getChildCount(); j++) {
                if(singleKeyContainer.getChildAt(j) instanceof LinearLayout) {
                    addKeyAndListener((LinearLayout) singleKeyContainer.getChildAt(j));
                } else if(singleKeyContainer.getChildAt(j) instanceof ObservableScrollView) {
                    ObservableScrollView scrollView = (ObservableScrollView) singleKeyContainer.getChildAt(j);
                    LinearLayout linearLayout = (LinearLayout) scrollView.getChildAt(0);
                    for(int k = 0; k < linearLayout.getChildCount(); k++) {
                        addKeyAndListener((LinearLayout) linearLayout.getChildAt(k), scrollView);
                    }
                }
            }
        }
    }

    private void addKeyAndListener(LinearLayout keyContainer) {
        addKeyAndListener(keyContainer, null);
    }

    private void addKeyAndListener(LinearLayout keyContainer, ObservableScrollView scrollView) {
        for(int k = 0; k < keyContainer.getChildCount(); k++) {
            View v = keyContainer.getChildAt(k);
            if(v instanceof Keyboard) {
                Keyboard key = (Keyboard) v;
                key.setListener(this);
                if(scrollView != null && key instanceof KeyboardDelay) {
                    ((KeyboardDelay)key).setScrollView(scrollView);
                }
                if(key.getCode() >= KeyConst.KEY_a && key.getCode() <= KeyConst.KEY_z) {
                    mAlphaKeys.add(key);
                }
            }
        }
    }

    @Override
    protected void handlerPageTurning(int code) {
        switch (code) {
            case KeyConst.KEY_PREVIOUS_PAGE:
                if(mInputType == INPUT_TYPE_SYMBOL) {
                    sv_symbol.smoothScrollTo(0, 0);
                } else if(mInputType == INPUT_TYPE_WIN) {
                    sv_win.smoothScrollTo(0, 0);
                }
                break;
            case KeyConst.KEY_NEXT_PAGE:
                if(mInputType == INPUT_TYPE_SYMBOL) {
                    sv_symbol.smoothScrollTo(0, dp2px(50));
                } else if(mInputType == INPUT_TYPE_WIN) {
                    sv_win.smoothScrollTo(0, dp2px(50));
                }
                break;
        }
    }
}
