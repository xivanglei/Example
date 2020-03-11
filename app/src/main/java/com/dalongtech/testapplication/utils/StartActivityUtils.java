//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.dalongtech.testapplication.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class StartActivityUtils {
    public StartActivityUtils() {
    }

    public static void startActivity(Context context, String url) {
        if (StringUtil.isNotBlank(url)) {
            Intent action;
            if (url.contains(":")) {
                action = new Intent("android.intent.action.VIEW");
                StringBuilder builder = new StringBuilder();
                builder.append(url);
                action.setData(Uri.parse(builder.toString()));
                context.startActivity(action);
            } else {
                action = new Intent();
                action.setClassName(context.getPackageName(), url);
                context.startActivity(action);
            }

        }
    }
}
