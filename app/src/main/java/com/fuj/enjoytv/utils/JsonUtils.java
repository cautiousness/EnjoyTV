package com.fuj.enjoytv.utils;

import android.content.Context;

import com.fuj.enjoytv.model.TVDetResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.SoftReference;
import java.util.List;
import java.util.Map;

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

    public static TVDetResult getResult(String json) {
        Map<String, Object> objectMap = new Gson().fromJson(json, new TypeToken<Map<String, Object>>() {}.getType());
        TVDetResult TVDetResult = new TVDetResult();
        for (Map.Entry<String, Object> entry : objectMap.entrySet()) {
            if("result".equals(entry.getKey())) {
                TVDetResult.result = (String) entry.getValue();
            }

            if ("datas".equals(entry.getKey())) {
                TVDetResult.datas = (List) entry.getValue();
            }
        }
        return TVDetResult;
    }
}
