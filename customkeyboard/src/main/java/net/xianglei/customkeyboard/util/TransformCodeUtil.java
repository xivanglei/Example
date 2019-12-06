package net.xianglei.customkeyboard.util;

import android.inputmethodservice.Keyboard;
import android.util.Log;
import android.util.SparseIntArray;

import net.xianglei.customkeyboard.constants.KeyConst;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Author:xianglei
 * Date: 2019-12-05 14:36
 * Description:转成windows键盘码
 */
public class TransformCodeUtil {

    private SparseIntArray mCodeTransform;

    //初始化转换码 key为输入，value为输出的windows码
    public TransformCodeUtil() {
        mCodeTransform = new SparseIntArray();
        //字母 a - z
        mCodeTransform.put(KeyConst.KEY_a, 65);
        mCodeTransform.put(KeyConst.KEY_b, 66);
        mCodeTransform.put(KeyConst.KEY_c, 67);
        mCodeTransform.put(KeyConst.KEY_d, 68);
        mCodeTransform.put(KeyConst.KEY_e, 69);
        mCodeTransform.put(KeyConst.KEY_f, 70);
        mCodeTransform.put(KeyConst.KEY_g, 71);
        mCodeTransform.put(KeyConst.KEY_h, 72);
        mCodeTransform.put(KeyConst.KEY_i, 73);
        mCodeTransform.put(KeyConst.KEY_j, 74);
        mCodeTransform.put(KeyConst.KEY_k, 75);
        mCodeTransform.put(KeyConst.KEY_l, 76);
        mCodeTransform.put(KeyConst.KEY_m, 77);
        mCodeTransform.put(KeyConst.KEY_n, 78);
        mCodeTransform.put(KeyConst.KEY_o, 79);
        mCodeTransform.put(KeyConst.KEY_p, 80);
        mCodeTransform.put(KeyConst.KEY_q, 81);
        mCodeTransform.put(KeyConst.KEY_r, 82);
        mCodeTransform.put(KeyConst.KEY_s, 83);
        mCodeTransform.put(KeyConst.KEY_t, 84);
        mCodeTransform.put(KeyConst.KEY_u, 85);
        mCodeTransform.put(KeyConst.KEY_v, 86);
        mCodeTransform.put(KeyConst.KEY_w, 87);
        mCodeTransform.put(KeyConst.KEY_x, 88);
        mCodeTransform.put(KeyConst.KEY_y, 89);
        mCodeTransform.put(KeyConst.KEY_z, 90);
        //字母A-Z 这部分不用转，为了避免遗漏也加一下
        mCodeTransform.put(KeyConst.KEY_A, 65);
        mCodeTransform.put(KeyConst.KEY_B, 66);
        mCodeTransform.put(KeyConst.KEY_C, 67);
        mCodeTransform.put(KeyConst.KEY_D, 68);
        mCodeTransform.put(KeyConst.KEY_E, 69);
        mCodeTransform.put(KeyConst.KEY_F, 70);
        mCodeTransform.put(KeyConst.KEY_G, 71);
        mCodeTransform.put(KeyConst.KEY_H, 72);
        mCodeTransform.put(KeyConst.KEY_I, 73);
        mCodeTransform.put(KeyConst.KEY_J, 74);
        mCodeTransform.put(KeyConst.KEY_K, 75);
        mCodeTransform.put(KeyConst.KEY_L, 76);
        mCodeTransform.put(KeyConst.KEY_M, 77);
        mCodeTransform.put(KeyConst.KEY_N, 78);
        mCodeTransform.put(KeyConst.KEY_O, 79);
        mCodeTransform.put(KeyConst.KEY_P, 80);
        mCodeTransform.put(KeyConst.KEY_Q, 81);
        mCodeTransform.put(KeyConst.KEY_R, 82);
        mCodeTransform.put(KeyConst.KEY_S, 83);
        mCodeTransform.put(KeyConst.KEY_T, 84);
        mCodeTransform.put(KeyConst.KEY_U, 85);
        mCodeTransform.put(KeyConst.KEY_V, 86);
        mCodeTransform.put(KeyConst.KEY_W, 87);
        mCodeTransform.put(KeyConst.KEY_X, 88);
        mCodeTransform.put(KeyConst.KEY_Y, 89);
        mCodeTransform.put(KeyConst.KEY_Z, 90);

        //数字 0 - 9
        mCodeTransform.put(KeyConst.KEY_0, 96);
        mCodeTransform.put(KeyConst.KEY_1, 97);
        mCodeTransform.put(KeyConst.KEY_2, 98);
        mCodeTransform.put(KeyConst.KEY_3, 99);
        mCodeTransform.put(KeyConst.KEY_4, 100);
        mCodeTransform.put(KeyConst.KEY_5, 101);
        mCodeTransform.put(KeyConst.KEY_6, 102);
        mCodeTransform.put(KeyConst.KEY_7, 103);
        mCodeTransform.put(KeyConst.KEY_8, 104);
        mCodeTransform.put(KeyConst.KEY_9, 105);

        //符号
        mCodeTransform.put(KeyConst.KEY_AT, 50);
        mCodeTransform.put(KeyConst.KEY_DOT, 190);
        mCodeTransform.put(KeyConst.KEY_GRAVE_ACCENT, 192);
        mCodeTransform.put(KeyConst.KEY_TILDE, 192);
        mCodeTransform.put(KeyConst.KEY_EXCLAMATION_MARK, 49);
        mCodeTransform.put(KeyConst.KEY_CROSSHATCH, 51);
        mCodeTransform.put(KeyConst.KEY_DOLLAR_SIGN, 52);
        mCodeTransform.put(KeyConst.KEY_PERCENT_SIGN, 53);
        mCodeTransform.put(KeyConst.KEY_CIRCUMFLEX, 54);
        mCodeTransform.put(KeyConst.KEY_AND, 55);
        mCodeTransform.put(KeyConst.KEY_ASTERISK, 56);
        mCodeTransform.put(KeyConst.KEY_LEFT_PARENTHESES, 57);
        mCodeTransform.put(KeyConst.KEY_RIGHT_PARENTHESES, 48);
        mCodeTransform.put(KeyConst.KEY_HYPHEN, 189);
        mCodeTransform.put(KeyConst.KEY_EQUAL_SIGN, 187);
        mCodeTransform.put(KeyConst.KEY_UNDERLINE, 189);
        mCodeTransform.put(KeyConst.KEY_PLUS_SIGN, 187);
        mCodeTransform.put(KeyConst.KEY_LEFT_BRACKETS, 219);
        mCodeTransform.put(KeyConst.KEY_RIGHT_BRACKETS, 221);
        mCodeTransform.put(KeyConst.KEY_LEFT_BRACES, 219);
        mCodeTransform.put(KeyConst.KEY_RIGHT_BRACES, 221);
        mCodeTransform.put(KeyConst.KEY_BACKSLASH, 220);
        mCodeTransform.put(KeyConst.KEY_VERTICAL_BAR, 220);
        mCodeTransform.put(KeyConst.KEY_SEMICOLON, 186);
        mCodeTransform.put(KeyConst.KEY_COLON, 186);
        mCodeTransform.put(KeyConst.KEY_SINGLE_QUOTE, 222);
        mCodeTransform.put(KeyConst.KEY_DOUBLE_QUOTE, 222);
        mCodeTransform.put(KeyConst.KEY_LESS_THAN, 188);
        mCodeTransform.put(KeyConst.KEY_GREATER_THAN, 190);
        mCodeTransform.put(KeyConst.KEY_SLASH, 191);
        mCodeTransform.put(KeyConst.KEY_QUESTION_MARK, 191);
        mCodeTransform.put(KeyConst.KEY_COMMA, 188);

        //windows其他按键
        mCodeTransform.put(KeyConst.KEY_SPACE, 32);
        mCodeTransform.put(KeyConst.KEY_ESC, 27);
        mCodeTransform.put(KeyConst.KEY_CTRL_L, 162);
        mCodeTransform.put(KeyConst.KEY_CTRL_R, 163);
        mCodeTransform.put(KeyConst.KEY_SHIFT_L, 160);
        mCodeTransform.put(KeyConst.KEY_SHIFT_R, 161);
        mCodeTransform.put(KeyConst.KEY_ALT_L, 164);
        mCodeTransform.put(KeyConst.KEY_ALT_R, 165);
        mCodeTransform.put(KeyConst.KEY_CAPSLK, 20);
        mCodeTransform.put(KeyConst.KEY_WIN, 91);
        mCodeTransform.put(KeyConst.KEY_HOME, 36);
        mCodeTransform.put(KeyConst.KEY_END, 35);
        mCodeTransform.put(KeyConst.KEY_PRTSC, 44);
        mCodeTransform.put(KeyConst.KEY_SCRLK, 145);
        mCodeTransform.put(KeyConst.KEY_PAUSE, 19);
        mCodeTransform.put(KeyConst.KEY_INS, 45);
        mCodeTransform.put(KeyConst.KEY_DEL, 46);
        mCodeTransform.put(KeyConst.KEY_PGUP, 33);
        mCodeTransform.put(KeyConst.KEY_PUDN, 34);
        mCodeTransform.put(KeyConst.KEY_F1, 112);
        mCodeTransform.put(KeyConst.KEY_F2, 113);
        mCodeTransform.put(KeyConst.KEY_F3, 114);
        mCodeTransform.put(KeyConst.KEY_F4, 115);
        mCodeTransform.put(KeyConst.KEY_F5, 116);
        mCodeTransform.put(KeyConst.KEY_F6, 117);
        mCodeTransform.put(KeyConst.KEY_F7, 118);
        mCodeTransform.put(KeyConst.KEY_F8, 119);
        mCodeTransform.put(KeyConst.KEY_F9, 120);
        mCodeTransform.put(KeyConst.KEY_F10, 121);
        mCodeTransform.put(KeyConst.KEY_F11, 122);
        mCodeTransform.put(KeyConst.KEY_F12, 123);
        mCodeTransform.put(KeyConst.KEY_LINE_FEED, 9);
        mCodeTransform.put(KeyConst.KEY_TAB, 9);
        mCodeTransform.put(Keyboard.KEYCODE_DELETE, 8);
        mCodeTransform.put(Keyboard.KEYCODE_DONE, 13);
        mCodeTransform.put(KeyConst.KEY_ARROW_LEFT, 37);
        mCodeTransform.put(KeyConst.KEY_ARROW_RIGHT, 39);
        mCodeTransform.put(KeyConst.KEY_ARROW_UP, 38);
        mCodeTransform.put(KeyConst.KEY_ARROW_DOWN, 40);
    }

    //单个码转换，如果找不到会返回-10000
    private int transformSingleCode(int code) {
        if(mCodeTransform.indexOfKey(code) >= 0) {
            return mCodeTransform.valueAt(mCodeTransform.indexOfKey(code));
        } else {
            Log.d(TAG, "transformSingleCode: " + "转换错误！找不到code: " + code + "  对应的windows码");
            return KeyConst.NO_FIND_KEY;
        }
    }

    //需要加shift
    private boolean isNeedShift(int code) {
        return Arrays.asList(
                KeyConst.KEY_TILDE,
                KeyConst.KEY_EXCLAMATION_MARK,
                KeyConst.KEY_AT,
                KeyConst.KEY_CROSSHATCH,
                KeyConst.KEY_DOLLAR_SIGN,
                KeyConst.KEY_PERCENT_SIGN,
                KeyConst.KEY_CIRCUMFLEX,
                KeyConst.KEY_AND,
                KeyConst.KEY_ASTERISK,
                KeyConst.KEY_LEFT_PARENTHESES,
                KeyConst.KEY_RIGHT_PARENTHESES,
                KeyConst.KEY_UNDERLINE,
                KeyConst.KEY_PLUS_SIGN,
                KeyConst.KEY_LEFT_BRACES,
                KeyConst.KEY_RIGHT_BRACES,
                KeyConst.KEY_VERTICAL_BAR,
                KeyConst.KEY_COLON,
                KeyConst.KEY_DOUBLE_QUOTE,
                KeyConst.KEY_LESS_THAN,
                KeyConst.KEY_GREATER_THAN,
                KeyConst.KEY_QUESTION_MARK,
                //这部分是字母A-Z
                KeyConst.KEY_A,
                KeyConst.KEY_B,
                KeyConst.KEY_C,
                KeyConst.KEY_D,
                KeyConst.KEY_E,
                KeyConst.KEY_F,
                KeyConst.KEY_G,
                KeyConst.KEY_H,
                KeyConst.KEY_I,
                KeyConst.KEY_J,
                KeyConst.KEY_K,
                KeyConst.KEY_L,
                KeyConst.KEY_M,
                KeyConst.KEY_N,
                KeyConst.KEY_O,
                KeyConst.KEY_P,
                KeyConst.KEY_Q,
                KeyConst.KEY_R,
                KeyConst.KEY_S,
                KeyConst.KEY_T,
                KeyConst.KEY_U,
                KeyConst.KEY_V,
                KeyConst.KEY_W,
                KeyConst.KEY_X,
                KeyConst.KEY_Y,
                KeyConst.KEY_Z
        ).contains(code);
    }

    //是否是组合键，如果是，就需要交给transCombinationCode处理
    private boolean isCombinationCode(int code) {
        return code == KeyConst.KEY_LANGUAGE;
    }

    //添加组合键code
    private List<Integer> transCombinationCode(int code, boolean isDown) {
        List<Integer> result = new ArrayList<>();
        switch (code) {
            case KeyConst.KEY_LANGUAGE:
                if(isDown) {
                    result.add(transformSingleCode(KeyConst.KEY_CTRL_L));
                    result.add(transformSingleCode(KeyConst.KEY_SPACE));
                } else {
                    result.add(transformSingleCode(KeyConst.KEY_SPACE));
                    result.add(transformSingleCode(KeyConst.KEY_CTRL_L));
                }
                break;
        }
        return result;
    }

    //这部分不需要转换
    private boolean isDoNotTransform(int code) {
        return Arrays.asList(
                Keyboard.KEYCODE_SHIFT,
                KeyConst.KEY_BACK,
                Keyboard.KEYCODE_CANCEL,
                KeyConst.KEY_SYMBOL,
                KeyConst.KEY_FUNCTION_WIN,
                KeyConst.KEY_PREVIOUS_PAGE,
                KeyConst.KEY_NEXT_PAGE,
                KeyConst.KEY_EMPTY).contains(code);
    }

    //转换码发送
    public List<Integer> transformCode(int code, boolean isDown) {
        if(isCombinationCode(code)) return transCombinationCode(code, isDown);
        List<Integer> result = new ArrayList<>();
        if(isDoNotTransform(code)) return result;
        if(isNeedShift(code)) {
            if(isDown) {
                result.add(transformSingleCode(KeyConst.KEY_SHIFT_L));
                result.add(transformSingleCode(code));
            } else {
                result.add(transformSingleCode(code));
                result.add(transformSingleCode(KeyConst.KEY_SHIFT_L));
            }
        } else {
            result.add(transformSingleCode(code));
        }
        return result;
    }

    //a-z如果按住了shift就需要转换成大写再转windows码
    public int changeCapitalAlphabetIfNeed(int code, boolean isShift) {
        int result = code;
        if (isShift && code >= KeyConst.KEY_a && code <= KeyConst.KEY_z) {
            result = code - 32;
        }
        return result;
    }
}
