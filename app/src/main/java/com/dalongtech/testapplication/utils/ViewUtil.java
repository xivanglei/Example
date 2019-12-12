package com.dalongtech.testapplication.utils;

import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Author:xianglei
 * Date: 2019/1/10 10:41 AM
 * Description:
 */
public class ViewUtil {

    public static void toggleGone(View view) {
        view.setVisibility(View.VISIBLE == view.getVisibility() ? View.GONE : View.VISIBLE);
    }

    public static void setGone(boolean isGone, View... views) {
        for(View view : views) {
            view.setVisibility(isGone ? View.GONE : View.VISIBLE);
        }
    }

    public static void setAlpha(boolean isGone, View... views) {
        for(View view : views) {
            view.setAlpha(isGone ? 0 : 1);
        }
    }

    public static void setEnabled(boolean enabled, View... views) {
        for(View view : views) {
            view.setEnabled(enabled);
        }
    }

    public static void setInvisible(boolean isInvisible, View... views) {
        for(View view : views) {
            view.setVisibility(isInvisible ? View.INVISIBLE : View.VISIBLE);
        }
    }

    public static String getText(View view) {
        TextView textView = null;
        if(view instanceof TextView) textView = (TextView) view;
        if(textView != null && textView.getText() != null && StringUtil.isNotBlank(textView.getText().toString()))
            return textView.getText().toString();
        else
            return "";
    }

    public static void addTextChangedListener(TextChangedListener listener, EditText... editTexts) {
        for(EditText editText : editTexts) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    listener.onTextChanged(s, start, before, count, editText);
                }
                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    public interface TextChangedListener {
        void onTextChanged(CharSequence s, int start, int before, int count, EditText editText);
    }

    public void setBgColor(View view, int color, float corner) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(corner);
        drawable.setColor(color);
        view.setBackground(drawable);
    }

    //对比不显示的用户名尺寸，如果超过应显示的用户名宽度，就按比例重设字大小
    public static void checkNickNameWidth(TextView textView) {
        textView.post(() -> {
            float textWidth = getTextWH(textView.getText().toString(), textView.getTextSize())[0] + 20;
            if(textWidth > textView.getWidth()) {
                float ratio = (float) textView.getWidth() / (textWidth * 1.05f);
                textView.setTextSize(ScreenUtil.px2sp(textView.getTextSize() * ratio));
            }
        });
    }

    public static float[] getTextWH(String text, float size) {
        Rect rect = new Rect();
        Paint paint=new Paint();
        paint.setTextSize(size);
        paint.getTextBounds(text, 0, text.length(), rect);
        return new float[]{rect.width(), rect.height()};
    }

}
