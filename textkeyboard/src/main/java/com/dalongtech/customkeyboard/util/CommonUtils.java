package com.dalongtech.customkeyboard.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.Log;

/**
 * Author:xianglei
 * Date: 2020-05-06 14:45
 * Description:
 */
public class CommonUtils {
    public static String getClipboardText(Context context) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data = cm.getPrimaryClip();
        ClipData.Item item = data.getItemAt(0);
        Log.d("祥雷", "getClipboardText: " + item.getText().toString());
        return item.getText().toString();
    }
}
