package com.iscreate.mobile.baidu;

import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;

public class BDMapApp extends BDLocApp {
	/**
	 * BMapManager create times
	 */
	private int BMapManagerCreateCount = 0;
	/**
	 * a instance for this app
	 */
	private static BDMapApp mInstance = null;
	/**
	 * true if init baidu key successfully
	 */
	private boolean m_bKeyRight = true;
	/**
	 * a BMapManager
	 */
	private BMapManager mBMapManager = null;

	@Override
	public void onCreate() {
		super.onCreate();
		BMapManagerCreateCount = 0;
		mInstance = this;
	}

	/**
	 * stop LocationClient if it is running
	 */
	@Override
	public void onTerminate() {
		BMapManagerCreateCount = 0;
		destroyBMapManager();
		super.onTerminate();
	}

	/**
	 * get BaiduApplication instance
	 * 
	 * @return mInstance
	 */
	public static BDMapApp getInstance() {
		return mInstance;
	}

	/**
	 * get the mBMapManager in BaiduApplication
	 * 
	 * @return mBMapManager
	 */
	public BMapManager getBMapManager() {
		return (mBMapManager);
	}

	/**
	 * create a BMapManager
	 * 
	 * @param context
	 *            Application
	 */
	public static void createBMapManager() {
		BDMapApp app = BDMapApp.getInstance();
		if (app.mBMapManager == null) {
			app.mBMapManager = new BMapManager(app);
		}
		++app.BMapManagerCreateCount;
		if (!app.mBMapManager.init(app.getBaiduApiKey(),
				new BaiduMKGeneralListener())) {
			Toast.makeText(BDMapApp.getInstance().getApplicationContext(),
					"BMapManager 初始化错误!", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * destory BMapManager if BMapManagerCreateCount <= 0
	 */
	public static void destroyBMapManager() {
		BDMapApp app = BDMapApp.getInstance();
		--app.BMapManagerCreateCount;
		if (app.BMapManagerCreateCount <= 0) {
			if (app.mBMapManager != null) {
				app.mBMapManager.stop();
				app.mBMapManager.destroy();
				app.mBMapManager = null;
			}
		}
	}

	/**
	 * @return true if init baidu key successfully
	 */
	public boolean isBaiduKeyRight() {
		return (m_bKeyRight);
	}

	/**
	 * init baidu key listener
	 */
	private static class BaiduMKGeneralListener implements MKGeneralListener {

		@Override
		public void onGetNetworkState(int iError) {
			if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
				Toast.makeText(BDMapApp.getInstance().getApplicationContext(),
						"您的网络出错啦！", Toast.LENGTH_LONG).show();
			} else if (iError == MKEvent.ERROR_NETWORK_DATA) {
				Toast.makeText(BDMapApp.getInstance().getApplicationContext(),
						"输入正确的检索条件！", Toast.LENGTH_LONG).show();
			}
		}

		@Override
		public void onGetPermissionState(int iError) {
			if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
				Toast.makeText(BDMapApp.getInstance().getApplicationContext(),
						"百度地图授权Key错误！", Toast.LENGTH_LONG).show();
				BDMapApp.getInstance().m_bKeyRight = false;
			}
		}
	}
}