package com.fuj.enjoytv.activity.simulation_loc;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.fuj.enjoytv.R;
import com.fuj.enjoytv.base.BaseActivity;
import com.fuj.enjoytv.utils.LogUtils;

/**
 * Created by gang
 */
public class SimulationLocActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comm);
        setBackToolbar();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        SimulationLocFragment fragment = new SimulationLocFragment();
        fragment.setArguments(getIntent().getExtras());
        transaction.add(R.id.fragment, fragment);
        transaction.commit();
        new SimulationLocPresenter(fragment);
    }
}