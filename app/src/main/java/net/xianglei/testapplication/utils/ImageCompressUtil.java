package net.xianglei.testapplication.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Author:xianglei
 * Date: 2019-10-29 14:17
 * Description:图片压缩并旋转
 */
public class ImageCompressUtil {

    private static final String JPG = ".jpg";
    private static final String TAG = "图片压缩";
    private static final byte[] JPEG_SIGNATURE = new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF};

    public static File compress(File srcFile, String outPath, int size) throws IOException {
        if(srcFile == null || !srcFile.exists()) return null;
        File cacheFile = getImageCacheFile(outPath, extSuffix(new FileInputStream(srcFile)));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        boolean needCompress = size > 0 && needCompress(size, srcFile.getPath());
        if(needCompress) {
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(srcFile), null, options);
            int srcWidth = options.outWidth;
            int srcHeight = options.outHeight;
            options.inJustDecodeBounds = false;
            options.inSampleSize = computeSize(srcWidth, srcHeight);
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap tagBitmap = BitmapFactory.decodeStream(new FileInputStream(srcFile), null, options);
        int orientation = 0;
        if(isJPG(toByteArray(new FileInputStream(srcFile)))) {
            orientation = getOrientation(toByteArray(new FileInputStream(srcFile)));
        }
        tagBitmap = rotatingImage(tagBitmap, orientation);
        tagBitmap.compress(Bitmap.CompressFormat.JPEG, needCompress ? 60 : 100, stream);
        tagBitmap.recycle();
        try {
            FileOutputStream fos = new FileOutputStream(cacheFile);
            fos.write(stream.toByteArray());
            fos.flush();
            fos.close();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cacheFile;
    }

    public static Bitmap compressScale(Bitmap bitmap, int s) {
        Bitmap result = bitmap;
        float size = (float) s * 8f;
        int beforeSize = getBitmapSize(bitmap);
        if(beforeSize / 1024 > size) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int options = (int) (size / (beforeSize / 1024) * 100);
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
            byte[] bytes = baos.toByteArray();
            result = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
        return result;
    }

    private static File getImageCacheFile(String targetDir, String suffix) {
        String cacheBuilder = targetDir + "/" +
                System.currentTimeMillis() +
                (int) (Math.random() * 1000) +
                (TextUtils.isEmpty(suffix) ? ".jpg" : suffix);

        return new File(cacheBuilder);
    }

    private static String extSuffix(InputStream input) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(input, null, options);
            return options.outMimeType.replace("image/", ".");
        } catch (Exception e) {
            return JPG;
        }
    }

    //减少宽高比例压缩
    public static Bitmap compressMatrix(Bitmap bitmap, long fileLength, int s, int orientation) {
        Bitmap result = bitmap;
        int size = s << 10;
        Matrix matrix = new Matrix();
        float scale = 1f;
        if(fileLength > size) {
            scale = scale / (float) Math.sqrt(fileLength /size);
            matrix.setScale(scale, scale);
        }
        matrix.postRotate(orientation);
        result = Bitmap.createBitmap(result, 0, 0, result.getWidth(), result.getHeight(), matrix, true);
        return result;
    }

    private static Bitmap rotatingImage(Bitmap bitmap, int angle) {
        Matrix matrix = new Matrix();

        matrix.postRotate(angle);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    //得到bitmap的大小
    public static int getBitmapSize(Bitmap bitmap) {
        return bitmap.getRowBytes() * bitmap.getHeight();                //earlier version
    }

    public static boolean needCompress(int leastCompressSize, String path) {
        if (leastCompressSize > 0) {
            File source = new File(path);
            return source.exists() && source.length() > (leastCompressSize << 10);
        }
        return true;
    }

    private static int computeSize(int srcWidth, int srcHeight) {
        srcWidth = srcWidth % 2 == 1 ? srcWidth + 1 : srcWidth;
        srcHeight = srcHeight % 2 == 1 ? srcHeight + 1 : srcHeight;

        int longSide = Math.max(srcWidth, srcHeight);
        int shortSide = Math.min(srcWidth, srcHeight);

        float scale = ((float) shortSide / longSide);
        if (scale <= 1 && scale > 0.5625) {
            if (longSide < 1664) {
                return 1;
            } else if (longSide < 4990) {
                return 2;
            } else if (longSide > 4990 && longSide < 10240) {
                return 4;
            } else {
                return longSide / 1280 == 0 ? 1 : longSide / 1280;
            }
        } else if (scale <= 0.5625 && scale > 0.5) {
            return longSide / 1280 == 0 ? 1 : longSide / 1280;
        } else {
            return (int) Math.ceil(longSide / (1280.0 / scale));
        }
    }

    private static boolean isJPG(byte[] data) {
        if (data == null || data.length < 3) {
            return false;
        }
        byte[] signatureB = new byte[]{data[0], data[1], data[2]};
        return Arrays.equals(JPEG_SIGNATURE, signatureB);
    }

    private static byte[] toByteArray(InputStream is) {
        if (is == null) {
            return new byte[0];
        }

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int read;
        byte[] data = new byte[4096];

        try {
            while ((read = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, read);
            }
        } catch (Exception ignored) {
            return new byte[0];
        } finally {
            try {
                buffer.close();
            } catch (IOException ignored) {
            }
        }

        return buffer.toByteArray();
    }

    private static int getOrientation(byte[] jpeg) {
        if (jpeg == null) {
            return 0;
        }

        int offset = 0;
        int length = 0;

        // ISO/IEC 10918-1:1993(E)
        while (offset + 3 < jpeg.length && (jpeg[offset++] & 0xFF) == 0xFF) {
            int marker = jpeg[offset] & 0xFF;

            // Check if the marker is a padding.
            if (marker == 0xFF) {
                continue;
            }
            offset++;

            // Check if the marker is SOI or TEM.
            if (marker == 0xD8 || marker == 0x01) {
                continue;
            }
            // Check if the marker is EOI or SOS.
            if (marker == 0xD9 || marker == 0xDA) {
                break;
            }

            // Get the length and check if it is reasonable.
            length = pack(jpeg, offset, 2, false);
            if (length < 2 || offset + length > jpeg.length) {
                Log.e(TAG, "Invalid length");
                return 0;
            }

            // Break if the marker is EXIF in APP1.
            if (marker == 0xE1 && length >= 8
                    && pack(jpeg, offset + 2, 4, false) == 0x45786966
                    && pack(jpeg, offset + 6, 2, false) == 0) {
                offset += 8;
                length -= 8;
                break;
            }

            // Skip other markers.
            offset += length;
            length = 0;
        }

        // JEITA CP-3451 Exif Version 2.2
        if (length > 8) {
            // Identify the byte order.
            int tag = pack(jpeg, offset, 4, false);
            if (tag != 0x49492A00 && tag != 0x4D4D002A) {
                Log.e(TAG, "Invalid byte order");
                return 0;
            }
            boolean littleEndian = (tag == 0x49492A00);

            // Get the offset and check if it is reasonable.
            int count = pack(jpeg, offset + 4, 4, littleEndian) + 2;
            if (count < 10 || count > length) {
                Log.e(TAG, "Invalid offset");
                return 0;
            }
            offset += count;
            length -= count;

            // Get the count and go through all the elements.
            count = pack(jpeg, offset - 2, 2, littleEndian);
            while (count-- > 0 && length >= 12) {
                // Get the tag and check if it is orientation.
                tag = pack(jpeg, offset, 2, littleEndian);
                if (tag == 0x0112) {
                    int orientation = pack(jpeg, offset + 8, 2, littleEndian);
                    switch (orientation) {
                        case 1:
                            return 0;
                        case 3:
                            return 180;
                        case 6:
                            return 90;
                        case 8:
                            return 270;
                    }
                    Log.e(TAG, "Unsupported orientation");
                    return 0;
                }
                offset += 12;
                length -= 12;
            }
        }

        Log.e(TAG, "Orientation not found");
        return 0;
    }

    private static int pack(byte[] bytes, int offset, int length, boolean littleEndian) {
        int step = 1;
        if (littleEndian) {
            offset += length - 1;
            step = -1;
        }

        int value = 0;
        while (length-- > 0) {
            value = (value << 8) | (bytes[offset] & 0xFF);
            offset += step;
        }
        return value;
    }

    public static void saveBitmapToSD(Bitmap bt, int quality, String savePath, String paramPath) {
        File file = null;
        if(savePath == null) {
            file = FileUtil.createFile(SDCardUtil.getPrivateCacheDir().getAbsolutePath() + File.separator + System.currentTimeMillis() + ".jpg");
        }  else {
            file = FileUtil.createFile(savePath);
        }
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bt.compress(Bitmap.CompressFormat.JPEG, quality, stream);
        try {
            if(StringUtil.isNotBlank(paramPath)) {
                ExifInterface startEI = new ExifInterface(paramPath);//imgPath为压缩后图片路径
                int degree = startEI.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                ExifInterface endEI = new ExifInterface(savePath);//imgPath为压缩后图片路径
                endEI.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(degree));
                endEI.saveAttributes();
            }
            bt.recycle();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(stream.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
