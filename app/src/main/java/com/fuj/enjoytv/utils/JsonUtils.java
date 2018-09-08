package com.fuj.enjoytv.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.SoftReference;

/**
 * Created by gang
 */
public class JsonUtils {
    public static String readJsonFile(Context context, String fileName) {
        SoftReference<Context> mContext = new SoftReference<>(context);
        StringBuilder builder = new StringBuilder();
        int id = mContext.get().getResources().getIdentifier(fileName, "raw", mContext.get().getPackageName());
        InputStream inputStream = mContext.get().getResources().openRawResource(id);
        InputStreamReader inputStreamReader;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String content;
            while ((content = bufferedReader.readLine()) != null) {
                builder.append(content);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
}
