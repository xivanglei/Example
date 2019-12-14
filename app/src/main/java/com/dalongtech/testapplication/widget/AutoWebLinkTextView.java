package com.dalongtech.testapplication.widget;

import android.content.Context;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.AttributeSet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author:xianglei
 * Date: 2019-10-29 11:06
 * Description:
 */
public class AutoWebLinkTextView extends android.support.v7.widget.AppCompatTextView {

    public AutoWebLinkTextView(Context context) {
        super(context);
    }

    public AutoWebLinkTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoWebLinkTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private final String regex = "(https|http):\\/\\/[-a-zA-Z0-9+&@#\\/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#\\/%=~_|]";

    @Override
    public void setText(CharSequence text, BufferType type) {
        String str = text.toString();

        SpannableString spannableString = new SpannableString(str);
        Matcher urlMatcher = Pattern.compile(regex).matcher(str);
        while (urlMatcher.find()) {
            String url = urlMatcher.group();
            int start = urlMatcher.start();
            int end = urlMatcher.end();
            spannableString.setSpan(new URLSpan(url), start, end, 0);
        }

        super.setText(spannableString, type);
        super.setMovementMethod(new LinkMovementMethod());
    }
}
