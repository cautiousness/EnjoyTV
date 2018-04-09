package com.fuj.enjoytv.activity.now;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;

import com.fuj.enjoytv.R;
import com.fuj.enjoytv.base.BaseActivity;

/**
 * Created by gang
 */
public class NowPlayActivity extends BaseActivity {
    private NowPlayFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comm);
        KeyboardAssitant.assistActivity(this);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        fragment = new NowPlayFragment();
        fragment.setArguments(getIntent().getExtras());
        transaction.add(R.id.fragment, fragment);
        transaction.commit();
        new NowPlayPresenter(fragment);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(fragment != null) {
            fragment.onBackPressed();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View focusView = getCurrentFocus();
            if (KeyboardAssitant.isShouldHideInput(focusView, ev)) {
                if(KeyboardAssitant.hideInputMethod(this, focusView)) {
                    return true;
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
