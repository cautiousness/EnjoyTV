package com.fuj.enjoytv.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fuj.enjoytv.R;

import java.lang.ref.SoftReference;

/**
 * Created by gang on 2016/8/2
 */
public class ToastUtils {
    private static String oldMsg; //之前显示的内容
    private static SoftReference<Toast> sToast; //Toast对象
    private static SoftReference<Toast> sToast1; //Toast对象
    private static long oneTime = 0; //第一次时间
    private static long twoTime = 0; //第二次时间

    /**
     * 显示Toast
     * @param context 上下文环境
     * @param message 消息
     */
    public static void showToast(Context context, String message){
        if(sToast == null || sToast.get() == null){
            Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            sToast = new SoftReference<>(toast);
            sToast.get().show() ;
            oneTime = System.currentTimeMillis() ;
        } else {
            twoTime = System.currentTimeMillis() ;
            if(message.equals(oldMsg)){
                if(twoTime - oneTime > Toast.LENGTH_SHORT){
                    sToast.get().show() ;
                }
            } else {
                oldMsg = message ;
                sToast.get().setText(message) ;
                sToast.get().show() ;
            }
        }
        oneTime = twoTime ;
    }

    public static void showYESorNO(Context context, int resId, String message) {
        View view = View.inflate(context, resId, null);
        TextView text = (TextView) view.findViewById(R.id.textToast);
        text.setText(message); // 设置显示文字
        if(sToast1 == null || sToast1.get() == null){
            Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            sToast1 = new SoftReference<>(toast);
            commToast(view, sToast1.get());
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if(message.equals(oldMsg)){
                if(twoTime - oneTime > Toast.LENGTH_SHORT){
                    commToast(view, sToast1.get());
                }
            } else {
                oldMsg = message ;
                commToast(view, sToast1.get());
            }
        }
        oneTime = twoTime ;
    }

    private static void commToast(View view, Toast toast) {
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }

    public static void cancelToast() {
        if (sToast != null) {
            sToast.get().cancel();
        }
    }
}
