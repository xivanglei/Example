package net.xianglei.testapplication.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Author:xianglei
 * Date: 2018/12/21 4:51 PM
 * Description:
 */
public class SDCardUtil {

    // 判断SD卡是否被挂载
    public static boolean isSDCardMounted() {
        // return Environment.getExternalStorageState().equals("mounted");
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    // 获取SD卡的完整空间大小，返回MB
    public static long getSDCardSize() {
        if (isSDCardMounted()) {
            StatFs fs = new StatFs(getBaseDir());
            long count = fs.getBlockCountLong();
            long size = fs.getBlockSizeLong();
            return count * size / 1024 / 1024;
        }
        return 0;
    }

    // 获取SD卡的剩余空间大小
    public static long getFreeSize() {
        if (isSDCardMounted()) {
            StatFs fs = new StatFs(getBaseDir());
            long count = fs.getFreeBlocksLong();
            long size = fs.getBlockSizeLong();
            return count * size / 1024 / 1024;
        }
        return 0;
    }

    // 获取SD卡的可用空间大小
    public static long getAvailableSize() {
        if (isSDCardMounted()) {
            StatFs fs = new StatFs(getBaseDir());
            long count = fs.getAvailableBlocksLong();
            long size = fs.getBlockSizeLong();
            return count * size / 1024 / 1024;
        }
        return 0;
    }

    // 往SD卡的公有目录下保存文件
    public static boolean saveFileToPublicDir(byte[] data, String type,
                                                    String fileName) {
        BufferedOutputStream bos = null;
        if (isSDCardMounted()) {
            File file = Environment.getExternalStoragePublicDirectory(type);
            try {
                bos = new BufferedOutputStream(new FileOutputStream(new File(
                        file, fileName)));
                bos.write(data);
                bos.flush();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    bos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    // 往SD卡的自定义目录下保存文件
    public static boolean saveFileToCustomDir(byte[] data, String dir,
                                                    String fileName) {
        BufferedOutputStream bos = null;
        if (isSDCardMounted()) {
            File file = new File(getBaseDir() + File.separator + dir);
            if (!file.exists()) {
                file.mkdirs();// 递归创建自定义目录
            }
            try {
                bos = new BufferedOutputStream(new FileOutputStream(new File(
                        file, fileName)));
                bos.write(data);
                bos.flush();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    bos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    // 往SD卡的私有Files目录下保存文件
    public static boolean saveFileToPrivateFilesDir(byte[] data,
                                                          String type, String fileName) {
        BufferedOutputStream bos = null;
        if (isSDCardMounted()) {
            File file = Util.getApp().getExternalFilesDir(type);
            try {
                bos = new BufferedOutputStream(new FileOutputStream(new File(
                        file, fileName)));
                bos.write(data);
                bos.flush();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    bos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    // 往SD卡的私有Cache目录下保存文件
    public static boolean saveFileToPrivateCacheDir(byte[] data,
                                                          String fileName) {
        BufferedOutputStream bos = null;
        if (isSDCardMounted()) {
            File file = Util.getApp().getExternalCacheDir();
            try {
                bos = new BufferedOutputStream(new FileOutputStream(new File(
                        file, fileName)));
                bos.write(data);
                bos.flush();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    bos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    // 保存bitmap图片到SDCard的私有Cache目录，必须保存png或者jpg格式
    public static boolean saveBitmapToPrivateCacheDir(Bitmap bitmap,
                                                            String fileName) {
        if (isSDCardMounted()) {
            BufferedOutputStream bos = null;
            // 获取私有的Cache缓存目录
            File file = Util.getApp().getExternalCacheDir();

            try {
                bos = new BufferedOutputStream(new FileOutputStream(new File(
                        file, fileName)));
                if (fileName != null
                        && (fileName.contains(".png") || fileName
                        .contains(".PNG"))) {
                    //压缩图片，100表示不压缩，差不多是纯复制
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                } else {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                }
                bos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    // 从SD卡获取文件,输出字节数组
    public static byte[] loadFile(String fileDir) {
        BufferedInputStream bis = null;
        //字节数组输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            bis = new BufferedInputStream(
                    //文件输入流，准备从文件获取数据
                    new FileInputStream(new File(fileDir)));
            byte[] buffer = new byte[8 * 1024];
            int c = 0;
            while ((c = bis.read(buffer)) != -1) {
                baos.write(buffer, 0, c);
                //刷新缓冲区，向其下属流对象输出数据
                baos.flush();
            }
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                baos.close();
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    // 从SDCard中寻找指定目录下的文件，返回Bitmap
    public static Bitmap loadBitmap(String filePath) {
        byte[] data = loadFile(filePath);
        if (data != null) {
            Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
            if (bm != null) {
                return bm;
            }
        }
        return null;
    }

    // 获取SD卡的根目录: /storage/emulated/0
    public static String getBaseDir() {
        if (isSDCardMounted()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return null;
    }

    // 获取SD卡根目录的路径:/storage/emulated/0/ddd（ddd就是准备在根目录下创建的）
    public static File getPublicDir(String type) {
        return Environment.getExternalStoragePublicDirectory(type);
    }

    // 获取SD卡私有Cache目录的路径 : /storage/emulated/0/Android/data/xl.gcs.com/cache(根目录进去android进去data进去包名进去cache文件夹)
    public static File getPrivateCacheDir() {
        return Util.getApp().getExternalCacheDir();
    }

    // 获取SD卡私有Files目录的路径: /storage/emulated/0/Android/data/xl.gcs.com/files/abc(abc指的是手动输入的文件夹名)
    public static File getPrivateFilesDir(String type) {
        return Util.getApp().getExternalFilesDir(type);
    }

    //获取/data/user/0/xl.gcs.com/files/参数type，这个目录是必须要root才能看到，而这里不需要权限也能存，用来做缓存目录比较好
    public static File getHideFilesDir(String type) {
        return new File(Util.getApp().getFilesDir(), type);
    }

    //获取/data/user/0/xl.gcs.com/Cache/参数type，这个目录是必须要root才能看到，而这里不需要权限也能存，用来做缓存目录比较好
    public static File getHideCacheDir(String type) {
        return new File(Util.getApp().getCacheDir(),type);
    }

    //查询文件是否存在
    public static boolean isFileExist(String filePath) {
        File file = new File(filePath);
        //isFile()表示不当当这个路径存在，而且还必须是个标准文件，不能是目录，否则返回false
        return file.isFile();
    }

    // 从sdcard中删除文件
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            try {
                file.delete();
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }
}
