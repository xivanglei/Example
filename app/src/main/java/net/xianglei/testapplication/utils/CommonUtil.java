package net.xianglei.testapplication.utils;

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
            urlConnection.setRequestProperty("Accept-Encoding", "identity");
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
}
