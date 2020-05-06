package com.dalongtech.testapplication.activity;

import android.os.Bundle;
import android.widget.Button;

import com.dalongtech.customkeyboard.listener.KeyListener;
import com.dalongtech.customkeyboard.widget.DLKeyboardScrollView;
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
    DLKeyboardScrollView kb_custom_keyboard;

    @Override
    protected int getLayoutById() {
        return R.layout.activity_custom_kayboard;
    }

    @Override
    protected void initViewAndData(Bundle savedInstanceState) {
        kb_custom_keyboard.setAutoClickBlankHide(false);
        kb_custom_keyboard.setListener(new KeyListener() {
            @Override
            public void onPress(int code) {
                LogUtil.d(code);
            }

            @Override
            public void onRelease(int code) {
                LogUtil.d(code);
            }

            @Override
            public void onHide(int hideType) {
                LogUtil.d(hideType);
            }

            @Override
            public void onKeyClickEvent(String eventCode) {
                LogUtil.d(eventCode);
            }

            @Override
            public void onClickPaste() {
                LogUtil.d("clickPaste");
            }

            @Override
            public void onClickAccountAssist() {
                LogUtil.d("onClickAccountAssist");
            }
        });
    }

    @OnClick(R.id.btn_show_keyboard)
    public void showKeyboard() {
        kb_custom_keyboard.showKeyboard();
    }


    @OnClick(R.id.btn_hide_keyboard)
    public void hideKeyboard() {
        kb_custom_keyboard.hideKeyboard();
    }

}
