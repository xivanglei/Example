package com.dalongtech.testapplication.activity;

import android.os.Bundle;
import android.widget.Button;

import com.dalongtech.customkeyboard.listener.KeyListener;
import com.dalongtech.customkeyboard.widget.DLKeyboardView;
import com.dalongtech.testapplication.R;
import com.dalongtech.testapplication.base.SimpleActivity;
import com.dalongtech.testapplication.utils.LogUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class CustomKayboardActivity extends SimpleActivity {

    @BindView(R.id.btn_show_keyboard)
    Button btn_show_keyboard;
    @BindView(R.id.btn_hide_keyboard)
    Button btn_hide_keyboard;
    @BindView(R.id.kb_custom_keyboard)
    DLKeyboardView kb_custom_keyboard;

    @Override
    protected int getLayoutById() {
        return R.layout.activity_custom_kayboard;
    }

    @Override
    protected void initViewAndData(Bundle savedInstanceState) {
        kb_custom_keyboard.setAutoClickBlankHide(true);
        kb_custom_keyboard.setListener(new KeyListener() {
            @Override
            public void onPress(int code) {
                LogUtil.d(code);
            }

            @Override
            public void onRelease(int code) {
                LogUtil.d(code);
            }
        });
    }

    private void showCustomKeyboard() {
        kb_custom_keyboard.showKeyboard();
    }

    @OnClick(R.id.btn_show_keyboard)
    public void showKeyboard() {
        showCustomKeyboard();
    }
    @OnClick(R.id.btn_hide_keyboard)
    public void hideKeyboard() {
        kb_custom_keyboard.hideKeyboard();
    }

}
