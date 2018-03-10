package com.iscreate.mobile.utils;

import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

public class Signal {
	/**
	 * start wifi scan
	 * 
	 * @param context
	 *            a context
	 * @return true if success,otherwise false
	 */
	public static boolean startWifiScan(Context context) {
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		int wifiState = wifiManager.getWifiState();
		switch (wifiState) {
		case WifiManager.WIFI_STATE_ENABLED:
		case WifiManager.WIFI_STATE_ENABLING:
			return (wifiManager.startScan());
		}
		return (false);
	}

	/**
	 * sort the list of wifi result s
	 * 
	 * @param wifiSR
	 *            a list of wifi result
	 */
	public static void sortScanResultList(List<ScanResult> wifiSR) {
		if (wifiSR != null) {
			ScanResult SRi;
			ScanResult SRj;
			int count = wifiSR.size();
			int i = 0;
			int j = 0;
			while (i < count) {
				SRi = wifiSR.get(i);
				j = i + 1;
				while (j < count) {
					SRj = wifiSR.get(j);
					if (SRj.level > SRi.level) {
						wifiSR.set(i, SRj);
						wifiSR.set(j, SRi);
						SRi = SRj;
					}
					++j;
				}
				++i;
			}
		}
	}

	/**
	 * convert asu to dBm
	 * 
	 * @param asu
	 *            alone signal unit
	 * @return dBm
	 */
	public static int todBm(int asu) {
		int dBm = -113 + 2 * asu;
		return (dBm);
	}

	/**
	 * convert frequency to channel
	 * 
	 * @param frequency
	 *            the frequency from a wifi
	 * @return channel
	 */
	public static int toChannel(int frequency) {
		if (2484 == frequency) {
			return (14);
		} else {
			return ((frequency - 2407) / 5);
		}
		// int channel = 0;
		// switch (frequency) {
		// case 2412:// 2401/2423
		// channel = 1;
		// break;
		// case 2417:// 2406/2428
		// channel = 2;
		// break;
		// case 2422:// 2411/2433
		// channel = 3;
		// break;
		// case 2427:// 2416/2438
		// channel = 4;
		// break;
		// case 2432:// 2421/2443
		// channel = 5;
		// break;
		// case 2437:// 2426/2448
		// channel = 6;
		// break;
		// case 2442:// 2431/2453
		// channel = 7;
		// break;
		// case 2447:// 2426/2448
		// channel = 8;
		// break;
		// case 2452:// 2441/2463
		// channel = 9;
		// break;
		// case 2457:// 2446/2468
		// channel = 10;
		// break;
		// case 2462:// 2451/2473
		// channel = 11;
		// break;
		// case 2467:// 2456/2478
		// channel = 12;
		// break;
		// case 2472:// 2461/2483
		// channel = 13;
		// break;
		// case 2484:// 2473/2495
		// channel = 14;
		// break;
		// }
		// return (channel);
	}
}
