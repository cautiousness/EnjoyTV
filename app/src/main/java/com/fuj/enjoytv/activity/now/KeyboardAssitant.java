package com.fuj.enjoytv.activity.now;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.lang.ref.SoftReference;

/**
 * Created by gang
 */
public class KeyboardAssitant {
    private static SoftReference<Activity> mActivity;

    public static void assistActivity (Activity activity) {
        mActivity = new SoftReference<>(activity);
        new KeyboardAssitant();
    }

    private View mChildOfContent;
    private int usableHeightPrevious;
    private FrameLayout.LayoutParams frameLayoutParams;

    private KeyboardAssitant() {
        FrameLayout content = mActivity.get().findViewById(android.R.id.content);
        mChildOfContent = content.getChildAt(0);
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                possiblyResizeChildOfContent();
            }
        });
        frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent.getLayoutParams();
    }

    private void possiblyResizeChildOfContent() {
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        int usableHeightNow = r.bottom;
        if (usableHeightNow != usableHeightPrevious) {
            int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
            if (heightDifference > (usableHeightSansKeyboard / 4)) {
                frameLayoutParams.height = usableHeightSansKeyboard - heightDifference; // keyboard probably just became visible
            } else {
                frameLayoutParams.height = usableHeightSansKeyboard;  // keyboard probably just became hidden
            }
            mChildOfContent.requestLayout();
            usableHeightPrevious = usableHeightNow;
        }
    }

    public static boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            ViewGroup parent = ((ViewGroup) v.getParent());
            parent.getLocationInWindow(leftTop);
            int left = leftTop[0], top = leftTop[1], bottom = top + parent.getHeight(), right = left + parent.getWidth();
            return !(event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

    public static Boolean hideInputMethod(Context context, View v) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm != null && imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}
