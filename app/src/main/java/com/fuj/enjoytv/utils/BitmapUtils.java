package com.fuj.enjoytv.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapUtils {

    public static String converPath(String path, int width, int high, String savePath) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, newOpts);// 打开空图片获取分辨率
        newOpts.inSampleSize = calculateInSampleSize(newOpts, width, high);// 设置缩放倍数
        newOpts.inJustDecodeBounds = false;
        try {
            Bitmap bitmap1 = BitmapFactory.decodeFile(path, newOpts);
            bitmap1.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        } catch (OutOfMemoryError e) {
            newOpts.inSampleSize = newOpts.inSampleSize + 2;
            Bitmap bitmap1 = BitmapFactory.decodeFile(path, newOpts);
            bitmap1.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        }
        Bitmap bitmap = null;
        int options = 90;
        while (baos.toByteArray().length / 1024 > 100) {
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.toByteArray().length);
            } else {
                baos.reset();
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
            options -= 5;
        }
        FileOutputStream out = null;
        File file = null;
        try {
            file = new File(savePath);
            if (!file.getParentFile().exists())
                file.getParentFile().mkdirs();
            if (!file.exists())
                file.createNewFile();
            out = new FileOutputStream(file);
            out.write(baos.toByteArray());
            out.flush();
            out.close();
        } catch (IOException e) {
            try {
                if(out != null) {
                    out.close();
                    out = null;
                }
                baos.reset();
                baos = null;
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }
}
