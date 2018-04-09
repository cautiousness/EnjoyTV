package com.fuj.enjoytv.utils;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.ref.SoftReference;

/**
 * Created by gang
 */
public class ValidUtils {

    public static String getEditString(EditText editText) {
        SoftReference<EditText> softReference = new SoftReference<>(editText);
        return softReference.get().getEditableText().toString().trim();
    }

    public static boolean isEmptyInput(EditText editText) throws Exception {
        SoftReference<EditText> softReference = new SoftReference<>(editText);
        if(TextUtils.isEmpty(softReference.get().getText())) {
            String msg = softReference.get().getHint().toString();
            throw new NullPointerException(msg);
        }
        return false;
    }

    public static boolean isEmptyInput(TextView textView) throws Exception {
        SoftReference<TextView> softReference = new SoftReference<>(textView);
        if(TextUtils.isEmpty(softReference.get().getText())) {
            String msg = softReference.get().getHint().toString();
            throw new NullPointerException(msg);
        }
        return false;
    }


}
