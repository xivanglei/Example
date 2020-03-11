package com.dalongtech.testapplication.constants;

import android.os.Environment;

import java.io.File;

/**
 * Author:xianglei
 * Date: 2020-02-24 18:02
 * Description:
 */
public interface ConstPath {
    String ALBUM_PATH = Environment.getExternalStorageDirectory()           //相册，用户保存图片与视频存在这里
            + File.separator + Environment.DIRECTORY_DCIM
            + File.separator + "Camera" + File.separator + "xianglei" + File.separator;
}
