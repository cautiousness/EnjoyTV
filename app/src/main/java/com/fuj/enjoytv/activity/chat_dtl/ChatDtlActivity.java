package com.fuj.enjoytv.activity.chat_dtl;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;

import com.fuj.enjoytv.R;
import com.fuj.enjoytv.activity.now.KeyboardAssitant;
import com.fuj.enjoytv.base.BaseActivity;

public class ChatDtlActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comm);
        setBackToolbar();
        KeyboardAssitant.assistActivity(this);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        ChatDtlFragment fragment = new ChatDtlFragment();
        fragment.setArguments(getIntent().getExtras());
        transaction.add(R.id.fragment, fragment);
        transaction.commit();
        new ChatDtlPresenter(fragment);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View focusView = getCurrentFocus();
            if (KeyboardAssitant.isShouldHideInput(focusView, ev)) {
                if(KeyboardAssitant.hideInputMethod(getApplication(), focusView)) {
                    return true;
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
