package com.fuj.enjoytv.activity.simulation_loc;

import android.os.Handler;
import android.os.Message;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.fuj.enjoytv.R;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by gang
 */
public class SimulationLocPresenter implements ISimulationLocContact.Presenter {
    private final static int MESSAGE_REFRESH = 1;
    private float mCurZoom = 15;

    private LatLng mSelfLatLng;
    private LocationClient mLocClient;
    private MarkerOptions mSelfMarker;
    private MyLocationListener myListener;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private RefreshHandler mHandler;
    private ISimulationLocContact.View mView;

    public SimulationLocPresenter(ISimulationLocContact.View view) {
        this.mView = view;
        mView.setPresenter(this);
        mHandler = new RefreshHandler(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void setLocArg() {
        setLocationListener();
        startLocate();
    }

    @Override
    public void setCurZoom(float zoom) {
        mCurZoom = zoom;
    }

    @Override
    public void initLocClient(LocationClient mLocClient) {
        this.mLocClient = mLocClient;
        myListener = new MyLocationListener();
        mSelfMarker = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_loc_self));
        mView.addMarker(mSelfMarker.position(new LatLng(0.0, 0.0)));
    }

    private void setLocationListener() {
        if (myListener == null) {
            myListener = new MyLocationListener();
        } else {
            mLocClient.registerLocationListener(myListener);
        }
    }

    private void startLocate() {
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(10000);
        option.setAddrType("all");
        mLocClient.setLocOption(option);
        startLocateThread();
    }

    private void startLocateThread() {
        mLocClient.start();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                while (mSelfLatLng == null) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                mHandler.sendEmptyMessage(MESSAGE_REFRESH);
                mLocClient.stop();
            }
        });
    }

    @Override
    public void reLocate() {
        mSelfLatLng = null;
        startLocateThread();
    }

    @Override
    public void refreshSelfMarker() {
        if(mSelfLatLng == null) {
            return;
        }

        mSelfMarker.position(mSelfLatLng);
    }

    @Override
    public void onDestroy() {
        if(!executor.isShutdown()) {
            executor.shutdownNow();
        }
        mLocClient.stop();
        mLocClient.unRegisterLocationListener(myListener);
        myListener = null;
    }

    private class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location){
            if(location == null)
                return;
            mSelfLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        }
    }

    private static class RefreshHandler extends Handler {
        private WeakReference<SimulationLocPresenter> mPresenter;

        public RefreshHandler(SimulationLocPresenter presenter) {
            mPresenter = new WeakReference<>(presenter);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_REFRESH:
                    float mCurZoom = mPresenter.get().mCurZoom;
                    ISimulationLocContact.View mView = mPresenter.get().mView;
                    MarkerOptions mSelfMarker = mPresenter.get().mSelfMarker;
                    LatLng mSelfLatLng = mPresenter.get().mSelfLatLng;
                    mView.clearMarkers();
                    mView.addMarker(mSelfMarker.position(mSelfLatLng));
                    mView.hook(mSelfLatLng);
                    mView.setMapCenterAndZoom(MapStatusUpdateFactory.newLatLngZoom(mSelfLatLng, mCurZoom));
                    break;
            }
        }
    }
}
