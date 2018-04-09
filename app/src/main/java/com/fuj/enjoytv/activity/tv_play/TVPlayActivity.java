package com.fuj.enjoytv.activity.tv_play;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.fuj.enjoytv.R;
import com.fuj.enjoytv.base.BaseActivity;

/**
 * Created by gang
 */
public class TVPlayActivity extends BaseActivity {
    private TVPlayFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comm);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        fragment = new TVPlayFragment();
        fragment.setArguments(getIntent().getExtras());
        transaction.add(R.id.fragment, fragment);
        transaction.commit();
        new TVPlayPresenter(fragment);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(fragment != null) {
            fragment.onBackPressed();
        }
    }
}
