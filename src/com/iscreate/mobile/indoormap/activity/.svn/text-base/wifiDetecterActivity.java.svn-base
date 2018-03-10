package com.iscreate.mobile.indoormap.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.SensorEvent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;

import com.iscreate.mobile.utils.Signal;

public abstract class wifiDetecterActivity extends cellDetecterActivity {
	/**
	 * the default count of wifi scanning
	 */
	private static final int DefaultOnceScanCount = 2;
	/**
	 * the default time gap of each wifi scanning
	 */
	private static final int DefaultOnceScanTimeGap = 500;
	/**
	 * the count of wifi scanning
	 */
	public static int OnceScanCount = DefaultOnceScanCount;
	/**
	 * the time gap of each wifi scanning
	 */
	public static int OnceScanTimeGap = DefaultOnceScanTimeGap;
	/**
	 * true if wifi is scanning now
	 */
	private boolean isscanning = false;
	/**
	 * keep each scan result here
	 */
	private List<ScanResult> wifisScanResult = new ArrayList<ScanResult>();
	/**
	 * keep all scan result here
	 */
	public List<List<ScanResult>> scanresultlist = new ArrayList<List<ScanResult>>();
	/**
	 * receive the scan result and handle the result here
	 */
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent != null) {
				String action = intent.getAction();
				if (action
						.equalsIgnoreCase(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
					WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
					List<ScanResult> wifisScanResult1 = wifiManager
							.getScanResults();
					Signal.sortScanResultList(wifisScanResult1);
					if (isscanning) {
						wifisScanResult.addAll(wifisScanResult1);
						scanresultlist.add(wifisScanResult1);
						int scancount = scanresultlist.size();
						if (scancount >= wifiDetecterActivity.OnceScanCount) {
							isscanning = false;
							OnHandleScanResult();
						} else {
							new Handler().postDelayed(new Runnable() {
								@Override
								public void run() {
									if (!continueWifiScan()) {
										isscanning = false;
										OnHandleScanResult();
									}
								}
							}, wifiDetecterActivity.OnceScanTimeGap);
						}
					}
				}
			}
		}
	};

	/**
	 * override it to handle the scan result
	 */
	public abstract void OnHandleScanResult();

	/**
	 * register the receiver
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		IntentFilter filter = new IntentFilter();
		filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		registerReceiver(broadcastReceiver, filter);
	}

	/**
	 * unregister the receiver
	 */
	@Override
	protected void onDestroy() {
		unregisterReceiver(broadcastReceiver);
		super.onDestroy();
	}

	/**
	 * start the wifi scan
	 */
	public boolean startWifiScan() {
		if (!isscanning && Signal.startWifiScan(this)) {
			isscanning = true;
			wifisScanResult.clear();
			scanresultlist.clear();
			return (true);
		}
		return (false);
	}

	/**
	 * the count of wifi scanning is larger than 1 ,then will call this to
	 * continue the next wifi scan
	 */
	public boolean continueWifiScan() {
		return (isscanning && Signal.startWifiScan(this));
	}

	/**
	 * we do not need this here
	 */
	@Override
	public void onSensorChanged(SensorEvent event) {
	}

	public void clearWifisScanResult() {
		wifisScanResult.clear();
	}

	public List<ScanResult> getWifisScanResult() {
		return (wifisScanResult);
	}
}