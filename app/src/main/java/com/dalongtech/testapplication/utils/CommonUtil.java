package com.dalongtech.testapplication.utils;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import java.net.URL;
import java.net.URLConnection;

/**
 * Author:xianglei
 * Date: 2019-10-22 08:56
 * Description:
 */
public class CommonUtil {
    public static long getHttpFileLength(String strUrl) {
        try {
            URL url = new URL(strUrl);
            URLConnection urlConnection = url.openConnection();
            //处理下载读取长度为-1 问题
            urlConnection.setRequestProperty("Accept-Encoding", "IDENTITIES");
            urlConnection.connect();
            long fileSize = urlConnection.getContentLength();
            LogUtil.d(fileSize);
            return urlConnection.getContentLength();
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
        return 0;
    }

    public static void clickBlankHideKeyboard(Activity activity) {
        FrameLayout layout = ((FrameLayout) activity.getWindow().getDecorView());
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                KeyboardUtil.hideKeyboard(layout);
                return false;
            }
        });
    }
}
