package net.xianglei.testapplication.utils;

/**
 * Author:xianglei
 * Date: 2019-08-09 12:59
 * Description:资源文件获取工具
 */
public class ResUtil {
    public static String getS(int strId, Object... format) {
        try {
            return Util.getApp().getResources().getString(strId, format);
        } catch (Exception e) {
            return null;
        }
    }

    public static int getC(int colorId) {
        try {
            return Util.getApp().getResources().getColor(colorId);
        } catch (Exception e) {
            return 0;
        }
    }
}
