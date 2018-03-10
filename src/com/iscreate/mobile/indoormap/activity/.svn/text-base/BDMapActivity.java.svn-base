package com.iscreate.mobile.indoormap.activity;

import android.os.Bundle;

import com.baidu.mapapi.map.MapView;
import com.iscreate.mobile.baidu.BDMapApp;

public abstract class BDMapActivity extends wifiDetecterActivity {
	/**
	 * MapView from baidu
	 */
	private MapView baidumap_mv = null;

	/**
	 * create a BMapManager
	 * 
	 * @param context
	 *            Application
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BDMapApp.createBMapManager();
	}

	/**
	 * set a MapView for this Activity
	 */
	public void setBDMapView(MapView mv) {
		baidumap_mv = mv;
	}

	/**
	 * destory BMapManager
	 * 
	 * @param context
	 *            Application
	 */
	@Override
	protected void onDestroy() {
		/**
		 * MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
		 */
		if (baidumap_mv != null) {
			baidumap_mv.destroy();
		}
		BDMapApp.destroyBMapManager();
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		/**
		 * MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
		 */
		if (baidumap_mv != null) {
			baidumap_mv.onResume();
		}
		super.onResume();
	}

	@Override
	protected void onPause() {
		/**
		 * MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
		 */
		if (baidumap_mv != null) {
			baidumap_mv.onPause();
		}
		super.onPause();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		/**
		 * save the state for this MapView
		 */
		if (baidumap_mv != null) {
			baidumap_mv.onSaveInstanceState(outState);
		}

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		/**
		 * restore MapView
		 */
		if (baidumap_mv != null) {
			baidumap_mv.onRestoreInstanceState(savedInstanceState);
		}
	}
}