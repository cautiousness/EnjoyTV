package com.fuj.enjoytv.widget.map;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.fuj.enjoytv.R;

/**
 * 百度地图缩放按钮自定义控件
 * @author dell
 *
 */
public class MapControlButton extends RelativeLayout implements OnClickListener {
	private Button mButtonZoomin;
	private Button mButtonZoomout;
	private MapView mapView;
	private int maxZoomLevel;
	private int minZoomLevel;
	private Context context;

	public MapControlButton(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MapControlButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}

	private void init() {
		View view = LayoutInflater.from(getContext()).inflate(
		        R.layout.custom_zoom_ctrl, null);
		mButtonZoomin = (Button) view.findViewById(R.id.zoomin);
		mButtonZoomout = (Button) view.findViewById(R.id.zoomout);
		mButtonZoomin.setOnClickListener(this);
		mButtonZoomout.setOnClickListener(this);
		addView(view);
	}

	@Override
	public void onClick(View v) {
		if(mapView == null) {
			throw new NullPointerException(
                "you can call setMapView(MapView mapView) at first");
		}
		float zoomLevel = mapView.getMap().getMapStatus().zoom;
		switch(v.getId()) {
		case R.id.zoomin:
			if(zoomLevel < maxZoomLevel) {
				mapView.getMap().setMapStatus(MapStatusUpdateFactory.zoomIn());
				mButtonZoomout.setEnabled(true);
			} else {
				Toast.makeText(context, "已放至最大!", Toast.LENGTH_SHORT).show();
				mButtonZoomin.setEnabled(false);
			}
			break;
		case R.id.zoomout:
			if(zoomLevel > minZoomLevel) {
				mapView.getMap().setMapStatus(MapStatusUpdateFactory.zoomOut());
				mButtonZoomin.setEnabled(true);
			} else {
				Toast.makeText(context, "已经缩至最小!", Toast.LENGTH_SHORT).show();
				mButtonZoomout.setEnabled(false);
			}
			break;
		}
	}

	/**
	 * 与MapView设置关联
	 * 
	 * @param mapView
	 */
	public void setMapView(MapView mapView) {
		this.mapView = mapView;
		maxZoomLevel = (int) mapView.getMap().getMaxZoomLevel(); // 获取最大的缩放级别
		minZoomLevel = (int) mapView.getMap().getMinZoomLevel(); // 获取最大的缩放级别
	}

	/**
	 * 根据MapView的缩放级别更新缩放按钮的状态，当达到最大缩放级别，设置mButtonZoomin
	 * 为不能点击，反之设置mButtonZoomout
	 * 
	 * @param level
	 */
	public void refreshZoomButtonStatus(int level) {
		if(mapView == null) {
			throw new NullPointerException(
			        "you can call setMapView(MapView mapView) at first");
		}

		if(level > minZoomLevel && level < maxZoomLevel) {
			if(!mButtonZoomout.isEnabled()) {
				mButtonZoomout.setEnabled(true);
			}
			if(!mButtonZoomin.isEnabled()) {
				mButtonZoomin.setEnabled(true);
			}
		} else if(level == minZoomLevel) {
			mButtonZoomout.setEnabled(false);
		} else if(level == maxZoomLevel) {
			mButtonZoomin.setEnabled(false);
		}
	}
	
	/**
	 * 获取地图的展示级别
	 * @return
	 */
	public float getCurrentLevel() {
		return mapView.getMap().getMapStatus().zoom;
	}

	public Button getmButtonZoomin() {
		return mButtonZoomin;
	}

	public Button getmButtonZoomout() {
		return mButtonZoomout;
	}
}
