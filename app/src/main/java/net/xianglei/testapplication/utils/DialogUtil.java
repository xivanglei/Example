package net.xianglei.testapplication.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Author:xianglei
 * Date: 2019-11-28 16:37
 * Description:
 */
public class DialogUtil {
    public static void showSingleDialog(Context context, String title, String msg, String positive,
                                        DialogInterface.OnClickListener listener) {
        createSingleDialog(context, title, msg, positive, listener).show();
    }

    public static void showDoubleDialog(Context context, String title, String msg, String positive,
                                        String negative, DialogInterface.OnClickListener listener) {

        createDoubleDialog(context, title, msg, positive, negative, listener).show();
    }

    public static AlertDialog createSingleDialog(Context context, String title, String msg, String positive,
                                                 DialogInterface.OnClickListener listener) {

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(positive, listener)
                .create();
        return dialog;
    }

    public static AlertDialog createDoubleDialog(Context context, String title, String msg, String positive,
                                                 String negative, DialogInterface.OnClickListener listener) {

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(positive, listener)
                .setNegativeButton(negative, listener)
                .create();
        return dialog;
    }
}
