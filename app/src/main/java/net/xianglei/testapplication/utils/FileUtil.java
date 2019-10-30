package net.xianglei.testapplication.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Author:xianglei
 * Date: 2019/1/9 6:46 PM
 * Description:
 */
public class FileUtil {

    private static final String TAG = "文件";

    /**
     * 根据URL获取文件名
     *
     * @param url
     *            URL
     * @return 文件名
     */
    public static String getFileNameFromUrl(String url) {
        if (url.indexOf("/") != -1)
            return url.substring(url.lastIndexOf("/")).replace("/", "");
        else
            return url;
    }

    /**
     * TODO<根据路径删除指定的目录或文件，无论存在与否>
     *
     * @return boolean
     */
    public static boolean deleteFolder(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        } else {
            if (file.isFile()) {
                // 为文件时调用删除文件方法
                return deleteFile(filePath);
            } else {
                // 为目录时调用删除目录方法
                return deleteDir(filePath);
            }
        }
    }

    /**
     * TODO<创建文件夹>
     *
     * @return File
     */
    public static File createDir(String path) {
        File dir = new File(path);
        if (!isExist(dir)) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * TODO<删除文件夹及文件夹下的文件>
     *
     * @return boolean
     */
    public static boolean deleteDir(String dirPath) {
        boolean flag = false;
        // 如果dirPath不以文件分隔符结尾，自动添加文件分隔符
        if (!dirPath.endsWith(File.separator)) {
            dirPath = dirPath + File.separator;
        }
        File dirFile = new File(dirPath);

        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }

        flag = true;
        File[] files = dirFile.listFiles();
        // 遍历删除文件夹下的所有文件(包括子目录)
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                // 删除子文件
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            } else {
                // 删除子目录
                flag = deleteDir(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag)
            return false;
        // 删除当前空目录
        return dirFile.delete();
    }

    /**
     * TODO<获取指定目录下文件的个数>
     *
     * @return int
     */
    public static int getFileCount(String dirPath) {
        int count = 0;

        // 如果dirPath不以文件分隔符结尾，自动添加文件分隔符
        if (!dirPath.endsWith(File.separator)) {
            dirPath = dirPath + File.separator;
        }
        File dirFile = new File(dirPath);

        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return count;
        }

        // 获取该目录下所有的子项文件(文件、子目录)
        File[] files = dirFile.listFiles();
        // 遍历删除文件夹下的所有文件(包括子目录)
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                // 删除子文件
                count += 1;
            }
        }

        return count;
    }

    /**
     * TODO<创建文件>
     *
     * @return File
     */
    public static File createFile(String path, String fileName) {
        File file = new File(createDir(path), fileName);
        if (!isExist(file)) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                LogUtil.d(TAG + "创建文件出错：" + e.toString());
                e.printStackTrace();
            }
        }
        return file;
    }

    public static File createFile(String path) {
        File file = new File(path);
        if (!isExist(file)) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                LogUtil.d(TAG + "创建文件出错：" + e.toString());
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * TODO<删除指定地址的文件夹>
     *
     * @return void
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && isExist(file))
            return file.delete();
        return false;
    }

    public static boolean deleteFile(File file) {
        if (file.isFile() && isExist(file))
            return file.delete();
        return false;
    }

    /**
     * 复制单个文件
     *
     * @param srcPath
     *            String 原文件路径
     * @param newPath
     *            String 目标路径
     */
    public static void copyFile(String srcPath, String newPath) {
        copyFile(srcPath, newPath, null);
    }

    /**
     * 复制单个文件
     *
     * @param srcPath
     *            String 原文件路径
     * @param newPath
     *            String 目标路径
     */
    public static void copyFile(String srcPath, String newPath, String fileName) {
        int bytesum = 0;
        int byteread = 0;
        File oldfile = new File(srcPath);

        if (isExist(oldfile)) {// 源文件存在
            try {
                InputStream inStream = new FileInputStream(srcPath); // 读入原文件
                FileOutputStream fs = new FileOutputStream(StringUtil.isBlank(fileName) ? newPath : newPath + File.separator + fileName);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; // 字节数 文件大小
                    fs.write(buffer, 0, byteread);
                }
                fs.close();
                inStream.close();
                LogUtil.d(TAG + "拷贝文件成功,文件总大小为：" + bytesum + "字节");
            } catch (IOException e) {
                LogUtil.d(TAG + "拷贝文件出错：" + e.toString());
                e.printStackTrace();
            }
        } else {// 源文件不存在
            LogUtil.d(TAG + "拷贝文件出错：源文件不存在！");
        }
    }

    /**
     * 复制整个文件夹内容
     *
     * @param srcPath
     *            String 原文件路径
     * @param desPath
     *            String 复制后路径
     */
    public static void copyFolder(String srcPath, String desPath) {

        try {
            (new File(desPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
            File a = new File(srcPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (srcPath.endsWith(File.separator)) {
                    temp = new File(srcPath + file[i]);
                } else {
                    temp = new File(srcPath + File.separator + file[i]);
                }

                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(desPath
                            + "/" + (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {// 如果是子文件夹
                    copyFolder(srcPath + "/" + file[i], desPath + "/" + file[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    /**
     * TODO<判断File对象所指的目录或文件是否存在>
     *
     * @return boolean
     */
    public static boolean isExist(File file) {
        return file.exists();
    }

}