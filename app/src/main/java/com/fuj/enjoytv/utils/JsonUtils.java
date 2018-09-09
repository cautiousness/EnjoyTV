package com.fuj.enjoytv.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.toString();
    }


    public static <T> List<T> getObjectList(String jsonString, Class<T> cls) {
        List<T> list = new ArrayList<>();
        try {
            Gson gson = new Gson();
            int start = jsonString.indexOf("[");
            int end = jsonString.lastIndexOf("]");
            jsonString = jsonString.substring(start, end + 1);
            //list = gson.fromJson(jsonString, new TypeToken<List<T>>(){}.getType());
            JsonArray arry = new JsonParser().parse(jsonString).getAsJsonArray();
            for (JsonElement jsonElement : arry) {
                list.add(gson.fromJson(jsonElement, cls));
            }
        } catch (Exception e) {
            LogUtils.e(" [error] " + e.getMessage());
            e.printStackTrace();
        }
        return list;

    }
}
