package net.xianglei.testapplication.activity;

import android.graphics.drawable.ColorDrawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.stomhong.library.KeyboardTouchListener;
import com.stomhong.library.KeyboardUtil;

import net.xianglei.testapplication.R;
import net.xianglei.testapplication.base.SimpleActivity;
import net.xianglei.testapplication.utils.LogUtil;
import net.xianglei.testapplication.utils.ViewUtil;

import butterknife.BindView;

public class CustomKayboardActivity extends SimpleActivity {

    @BindView(R.id.kb_custom_keyboard)
    KeyboardView kb_custom_keyboard;
    @BindView(R.id.et_test)
    EditText et_test;

    @BindView(R.id.root_view)
    LinearLayout root_view;
    @BindView(R.id.sv_main)
    ScrollView sv_main;
    @BindView(R.id.normal_ed)
    EditText normal_ed;
    @BindView(R.id.special_ed)
    EditText special_ed;

    private KeyboardUtil keyboardUtil;


    @Override
    protected int getLayoutById() {
        return R.layout.activity_custom_kayboard;
    }

    @Override
    protected void initViewAndData(Bundle savedInstanceState) {
        initCustomKeyboard();
//        initMoveKeyBoard();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
            if(keyboardUtil.isShow){
                keyboardUtil.hideSystemKeyBoard();
                keyboardUtil.hideAllKeyBoard();
                keyboardUtil.hideKeyboardLayout();
            }else {
                return super.onKeyDown(keyCode, event);
            }

            return false;
        } else
            return super.onKeyDown(keyCode, event);
    }

    private void initMoveKeyBoard() {
        ViewUtil.setGone(true, kb_custom_keyboard, et_test);
        keyboardUtil = new KeyboardUtil(this, root_view, sv_main);
        keyboardUtil.setOtherEdittext(normal_ed);
        // monitor the KeyBarod state
        keyboardUtil.setKeyBoardStateChangeListener(new KeyBoardStateListener());
        // monitor the finish or next Key
        keyboardUtil.setInputOverListener(new inputOverListener());
        special_ed.setOnTouchListener(new KeyboardTouchListener(keyboardUtil, KeyboardUtil.INPUTTYPE_ABC, -1));
    }

    class KeyBoardStateListener implements KeyboardUtil.KeyBoardStateChangeListener {

        @Override
        public void KeyBoardStateChange(int state, EditText editText) {
//            System.out.println("state" + state);
//            System.out.println("editText" + editText.getText().toString());
        }
    }

    class inputOverListener implements KeyboardUtil.InputFinishListener {

        @Override
        public void inputHasOver(int onclickType, EditText editText) {
//            System.out.println("onclickType" + onclickType);
//            System.out.println("editText" + editText.getText().toString());
        }
    }

    private void initCustomKeyboard() {
        final Keyboard pinyin26KB = new Keyboard(this, R.xml.key_board_pinyin_26);// 字母键盘
        final Keyboard numberKB = new Keyboard(this, R.xml.key_board_number); // 数字键盘

        kb_custom_keyboard.setKeyboard(pinyin26KB); // 设置默认显示字符键盘
        kb_custom_keyboard.setOnKeyboardActionListener(new KeyboardView.OnKeyboardActionListener() {

            // 按下 key 时执行
            @Override
            public void onPress(int primaryCode) {
                LogUtil.d("onPress: "+primaryCode);
            }

            // 释放 key 时执行
            @Override
            public void onRelease(int primaryCode) {
                 LogUtil.d("onRelease: "+primaryCode);
            }

            // 点击 key 时执行
            @Override
            public void onKey(int primaryCode, int[] keyCodes) {
                Editable editable = et_test.getText();
                int start = et_test.getSelectionStart();
                switch (primaryCode) {
                    case Keyboard.KEYCODE_SHIFT:// 设置shift状态然后刷新页面
                        pinyin26KB.setShifted(!pinyin26KB.isShifted());
                        kb_custom_keyboard.invalidateAllKeys();
                        break;
                    case Keyboard.KEYCODE_DELETE:// 点击删除键，长按连续删除
                        if (editable != null && editable.length() > 0 && start > 0) {
                            editable.delete(start - 1, start);
                        }
                        break;
                    case -10:// 自定义code，切换到拼音键盘
                        kb_custom_keyboard.setKeyboard(pinyin26KB);
                        break;
                    case -11:// 自定义code，切换到字母键盘
                        kb_custom_keyboard.setKeyboard(numberKB);
                        break;
                    case -12:// 自定义code
                        // 切换到符号键盘，待实现
                        break;
                    default:// 数值code
                        if (primaryCode >= 97 && primaryCode <= 97 + 26) {// 按下字母键
                            editable.insert(start, pinyin26KB.isShifted() ? Character.toString((char) (primaryCode - 32)) : Character.toString((char) (primaryCode)));
                        } else {// 其他code值，转字符在输入框中显示
                            editable.insert(start, Character.toString((char) (primaryCode)));
                        }
                        break;
                }
            }

            // 设置了 keyOutputText 属性后执行。
            @Override
            public void onText(CharSequence text) {
                LogUtil.d("onText: "+text);
            }

            @Override
            public void swipeLeft() {

            }

            @Override
            public void swipeRight() {

            }

            @Override
            public void swipeDown() {

            }

            @Override
            public void swipeUp() {

            }
        });
        kb_custom_keyboard.setPreviewEnabled(false);
    }
}
