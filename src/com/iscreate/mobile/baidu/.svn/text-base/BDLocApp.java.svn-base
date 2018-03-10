package com.iscreate.mobile.baidu;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.LocationData;

public class BDLocApp extends Application {
	/**
	 * baidu api key in meta
	 */
	protected static final String MetaBaiduApiKey = "com.baidu.lbsapi.API_KEY";
	/**
	 * a LocationClient for this application
	 */
	private LocationClient mLocClient = null;
	/**
	 * a BDLocationListener to register to LocationClient
	 */
	private BDLocListenner mLocListener = null;
	/**
	 * current location
	 */
	private BDLocation mLoc = null;
	/**
	 * current location data
	 */
	private LocationData mLocData = null;
	/**
	 * baidu api key
	 */
	private String BaiduApiKey = null;

	/**
	 * create a LocationClient
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		mLocData = new LocationData();
		mLocClient = new LocationClient(this);
		// mLocClient.setAccessKey(BaiduKey);
		// mLocClient.setAK(BaiduKey);
		mLocListener = new BDLocListenner();
		mLocClient.registerLocationListener(mLocListener);
		setBDLocOption();
		mLocClient.start();
		requestBDLocation();
	}

	/**
	 * stop LocationClient if it is running
	 */
	@Override
	public void onTerminate() {
		mLocClient.unRegisterLocationListener(mLocListener);
		if (mLocClient.isStarted()) {
			mLocClient.stop();
		}
		super.onTerminate();
	}

	/**
	 * set the option for LocationClient
	 */
	private void setBDLocOption() {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setCoorType("bd09ll");
		option.setScanSpan(5000);
		option.setAddrType("all");
		option.setPriority(LocationClientOption.NetWorkFirst);
		mLocClient.setLocOption(option);
	}

	/**
	 * listen the location
	 */
	private class BDLocListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location != null) {
				mLoc = location;
				mLocData.longitude = location.getLongitude();
				mLocData.latitude = location.getLatitude();
				mLocData.accuracy = location.getRadius();
				mLocData.direction = location.getDerect();
				mLocData.speed = location.getSpeed();
				mLocData.satellitesNum = location.getSatelliteNumber();
			}
		}

		@Override
		public void onReceivePoi(BDLocation location) {
		}
	}

	/**
	 * get current location information
	 */
	public BDLocation getBDLocation() {
		return (mLoc);
	}

	/**
	 * get current location data
	 */
	public LocationData getBDLocationData() {
		return (mLocData);
	}

	/**
	 * register baidu a BDLocationListener to listen the location changing
	 */
	public void registerBDLocationListener(BDLocationListener listener) {
		mLocClient.registerLocationListener(listener);
	}

	/**
	 * unregister the BDLocationListener
	 */
	public void unRegisterBDLocationListener(BDLocationListener listener) {
		mLocClient.unRegisterLocationListener(listener);
	}

	/**
	 * if want to have more information in a new BDLocationListener,then can
	 * reset the option
	 * 
	 * @param option
	 *            a option for LocationClient
	 */
	public void setBDLocOption(LocationClientOption option) {
		mLocClient.setLocOption(option);
	}

	/**
	 * request location
	 */
	public int requestBDLocation() {
		return (mLocClient.requestLocation());
	}

	/**
	 * get baidu api key
	 */
	public String getBaiduApiKey() {
		if (BaiduApiKey == null) {
			try {
				ApplicationInfo appInfo = getPackageManager()
						.getApplicationInfo(getPackageName(),
								PackageManager.GET_META_DATA);
				BaiduApiKey = appInfo.metaData.getString(MetaBaiduApiKey);
			} catch (Exception e) {
			}
		}
		return (BaiduApiKey);
	}
}