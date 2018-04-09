package com.fuj.enjoytv.activity.video;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.fuj.enjoytv.R;
import com.fuj.enjoytv.base.BaseActivity;

/**
 * Created by gang
 */
public class VideoActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comm);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        VideoFragment fragment = new VideoFragment();
        fragment.setArguments(getIntent().getExtras());
        transaction.add(R.id.fragment, fragment);
        transaction.commit();
        new VideoPresenter(fragment);
    }
}