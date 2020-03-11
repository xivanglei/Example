package com.dalongtech.testapplication.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import com.dalongtech.testapplication.constants.ConstPath;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Author:xianglei
 * Date: 2020-02-24 18:01
 * Description:
 */
public class ImageUtil {

    /**
     * 保存到相册
     * @param context
     * @param bmp
     * @return
     */
    public static String saveImageToAlbum(Context context, Bitmap bmp) {
        // 首先保存图片
        return saveImageDir(context,bmp, ConstPath.ALBUM_PATH, true);
    }

    //保存文件到指定路径
    public static String saveImageDir(Context context, Bitmap bmp, String storePath, boolean isPng) {


        String fileName = System.currentTimeMillis() + (isPng ? ".png" : ".jpeg");
        File file = FileUtil.createFile(storePath, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(isPng ? Bitmap.CompressFormat.PNG : Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));

            if (isSuccess) {
                return file.getAbsolutePath();
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
