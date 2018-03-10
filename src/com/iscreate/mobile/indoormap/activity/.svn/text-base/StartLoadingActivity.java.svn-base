package com.iscreate.mobile.indoormap.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.LocationData;
import com.iscreate.mobile.baidu.BDMapApp;
import com.iscreate.mobile.indoormap.R;
import com.iscreate.mobile.indoormap.widget.CustomAlertDialog;
import com.iscreate.mobile.indoormap.widget.PopupDownloadApkAsyncTask;
import com.iscreate.mobile.indoormap.widget.checkNewVersionThread;
import com.iscreate.mobile.service.ServiceClientInterface;
import com.iscreate.mobile.utils.ApkUpdateUtil;

public class StartLoadingActivity extends wifiDetecterActivity {
	/**
	 * 开始动画
	 */
	private AnimationDrawable animationDrawable = null;
	/**
	 * 设置网络的请求code
	 */
	private final int REQUESTCODE_SETNETWORK = 0;
	/**
	 * 安装apk的请求code
	 */
	private final int REQUESTCODE_INSTALLAPK = 1;
	/**
	 * 用于UI线程中接收检查新版本的消息
	 */
	private final int WHAT_CHECKNEWVERSION = 0;
	/**
	 * 用于UI线程中接收定位的消息
	 */
	private final int WHAT_GETLOCATION = 1;
	/**
	 * 当前Activity显示的内容
	 * 
	 * @see #setContentView(View)
	 */
	private View contentView = null;
	/**
	 * 显示动画的ImageView
	 */
	private ImageView process_im = null;
	/**
	 * 显示进度提示
	 */
	private TextView tips_tv = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
		contentView = LayoutInflater.from(StartLoadingActivity.this).inflate(
				R.layout.start, null);
		setContentView(contentView);
		process_im = (ImageView) findViewById(R.id.process_im);
		tips_tv = (TextView) findViewById(R.id.tips_tv);
		process_im.setBackgroundResource(R.anim.start_animation);
		animationDrawable = (AnimationDrawable) process_im.getBackground();
		checkNetworkState();
	}

	/**
	 * 当前Activity得到焦点时候就开始动画
	 */
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if (hasFocus) {
			if (animationDrawable != null) {
				animationDrawable.start();
			}
		}
		super.onWindowFocusChanged(hasFocus);
	}

	/**
	 * 开启线程检测新版本
	 */
	private void checkNewVersion() {
		tips_tv.setText("正在检测新版本");
		// /LoginActivity.initAppEnv(this);
		new checkNewVersionThread(this, handler, WHAT_CHECKNEWVERSION).start();
	}

	/**
	 * 接收其它线程中发送过来的消息
	 */
	private Handler handler = new Handler(Looper.getMainLooper()) {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case WHAT_CHECKNEWVERSION:
				if ((msg.arg2 == 1) && (msg.obj != null)) {
					confirmDownloadDialog((HashMap<String, String>) msg.obj);
				} else {
					startLocation();
				}
				break;
			case WHAT_GETLOCATION:
				if (msg.arg2 == 1) {
					startLoginActivity((String) msg.obj);
				} else {
					startLoginActivity(null);
				}
				break;
			default:
				startLocation();
				break;
			}
		}
	};

	/**
	 * 确认更新apk
	 */
	private void confirmDownloadDialog(
			final HashMap<String, String> apkVersionInfomap) {
		String updateMessage = "";
		try {
			updateMessage = ApkUpdateUtil
					.getApkVersionInfoStr(apkVersionInfomap);
		} catch (Exception e) {
		}
		new CustomAlertDialog(this, "检测到新版本", updateMessage) {
			@Override
			public void onConfirm() {
				InstallApkFromURL(apkVersionInfomap.get("url"),
						apkVersionInfomap.get("FileSize"));
			}

			@Override
			public void onCancel() {
				startLocation();
			}
		}.show();
	}

	/**
	 * 通过url下载apk并 安装apk
	 * 
	 * @param params
	 *            the apk URL,filesize,cookie
	 */
	private void InstallApkFromURL(String... params) {
		tips_tv.setText("正在下载");
		PopupDownloadApkAsyncTask popup = new PopupDownloadApkAsyncTask(
				contentView) {
			@Override
			public void onDownloadFinished(String path) {
				if (path != null) {
					tips_tv.setText("正在安装");
					ApkUpdateUtil.installApkForResult(
							StartLoadingActivity.this, new File(path),
							REQUESTCODE_INSTALLAPK);
				} else {
					tips_tv.setText("下载失败");
					startLocation();
				}
			}
		};
		popup.setBackgroundColor(Color.TRANSPARENT);
		popup.start(params);
	}

	private void startLocation() {
		if (!startWifiScan()) {
			startLoginActivity(null);
		} else {
			tips_tv.setText("正在扫描wifi信息");
		}
	}

	/**
	 * @param getlocationcontent
	 *            定位信息
	 */
	private void startLoginActivity(String getlocationcontent) {
		Intent intent = new Intent(StartLoadingActivity.this,
				mainActivity.class);
		if (getlocationcontent != null) {
			intent.putExtra("getlocationcontent", getlocationcontent);
		}
		startActivity(intent);
		if (animationDrawable != null) {
			if (animationDrawable.isRunning()) {
				animationDrawable.stop();
			}
			animationDrawable = null;
		}
		StartLoadingActivity.this.finish();
	}

	/**
	 * 检测网络
	 */
	private void checkNetworkState() {
		tips_tv.setText("正在检测网络");
		try {
			if (isNetworkAvailable()) {
				ConnectivityManager connectManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo mobileNetworkInfo = connectManager
						.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
				State mobileState = null;
				if (mobileNetworkInfo != null) {
					mobileState = mobileNetworkInfo.getState();
				}
				if ((mobileState == State.CONNECTED)
						|| (mobileState == State.CONNECTING)) {
					checkNewVersion();
				} else {
					State wifi = connectManager.getNetworkInfo(
							ConnectivityManager.TYPE_WIFI).getState();
					if (wifi == State.CONNECTED || wifi == State.CONNECTING) {
						checkNewVersion();
					} else {
						checkNewVersion();
					}
				}
			} else {
				new CustomAlertDialog(this, "提示", "网络不可用或未开启,是否进行网络设置?",
						"设置网络", "退出程序") {
					@Override
					public void onConfirm() {
						String actionSetNetwork = (android.os.Build.VERSION.SDK_INT > 10) ? Settings.ACTION_SETTINGS
								: Settings.ACTION_WIRELESS_SETTINGS;
						Intent intent = new Intent(actionSetNetwork);
						// intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
						startActivityForResult(intent, REQUESTCODE_SETNETWORK);
					}

					@Override
					public void onCancel() {
						finish();
					}
				}.show();
			}
		} catch (Exception e) {
			finish();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUESTCODE_SETNETWORK:
			if (isNetworkConntectedOrConnecting(this)) {
				checkNewVersion();
			} else {
				ConnectivityManager connectManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo wifiNetworkInfo = connectManager
						.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				NetworkInfo mobileNetworkInfo = connectManager
						.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
				if (wifiNetworkInfo.isAvailable()
						|| mobileNetworkInfo.isAvailable()) {
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							if (isNetworkConntectedOrConnecting(StartLoadingActivity.this)) {
								checkNewVersion();
							} else {
								Toast.makeText(StartLoadingActivity.this,
										"网络不可用或未开启,开启网络后重新运行!",
										Toast.LENGTH_SHORT).show();
								finish();
							}
						}
					}, 3000);
				} else {
					Toast.makeText(StartLoadingActivity.this,
							"网络不可用或未开启,开启网络后重新运行!", Toast.LENGTH_SHORT).show();
					finish();
				}
			}
			break;
		case REQUESTCODE_INSTALLAPK:
			Toast.makeText(StartLoadingActivity.this, "安装取消或失败!",
					Toast.LENGTH_SHORT).show();
			startLocation();
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 检测是否有可用网络
	 * 
	 * @return
	 */
	private boolean isNetworkAvailable() {
		Context context = StartLoadingActivity.this;
		ConnectivityManager connect = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connect != null) {
			NetworkInfo[] info = connect.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return (true);
					}
				}
			}
		}
		return (false);
	}

	/**
	 * 检测是否正在连接网络或已连接
	 */
	public boolean isNetworkConntectedOrConnecting(Context context) {
		ConnectivityManager connect = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connect != null) {
			NetworkInfo[] info = connect.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if ((info[i].getState() == NetworkInfo.State.CONNECTED)
							|| (info[i].getState() == NetworkInfo.State.CONNECTING)) {
						return (true);
					}
				}
			}
		}
		return (false);
	}

	@Override
	public void OnHandleScanResult() {
		tips_tv.setText("正在获取位置信息");
		new GetLocationThread().start();
	}

	private class GetLocationThread extends Thread {
		@Override
		public void run() {
			Message msg = handler.obtainMessage();
			msg.what = WHAT_GETLOCATION;
			try {
				msg.obj = GetLocation();
				msg.arg2 = 1;
			} catch (Exception e) {
				msg.obj = "没有定位到场所:" + e.getMessage();
				msg.arg2 = 0;
			}
			handler.sendMessage(msg);
		}
	}

	/**
	 * 定位当前位置
	 */
	private String GetLocation() throws Exception {
		List<List<ScanResult>> WifiScanResult = scanresultlist;
		List<ScanResult> wifisSRALL = new ArrayList<ScanResult>();
		for (List<ScanResult> wifisSR : WifiScanResult) {
			if (wifisSR != null) {
				wifisSRALL.addAll(wifisSR);
			}
		}
		String WifiListJsonStr = testWifiActivity
				.getWifiListJsonStr(WifiScanResult);
		if (WifiListJsonStr == null) {
			WifiListJsonStr = "";
		}
		LocationData locData = BDMapApp.getInstance().getBDLocationData();
		String[] params = {//
		"" + locData.longitude,//
				"" + locData.latitude,//
				WifiListJsonStr //
		};
		int actionID = ServiceClientInterface.ID_ACTION_GetLocation;
		String content = ServiceClientInterface.request(actionID, params);
		return (content);
	}
}