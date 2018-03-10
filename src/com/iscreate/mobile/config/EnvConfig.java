package com.iscreate.mobile.config;

import java.util.HashMap;

import android.R.integer;
import android.content.pm.PackageInfo;
import android.util.Log;

import com.iscreate.mobile.baidu.BDMapApp;

public class EnvConfig {
//	private static final String RunEnvUrl_Prod = "http://192.168.6.41/indooradmin/api.php?r=";// 生产环境
//	private static final String RunEnvUrl_Prod = "http://192.168.6.206:8381/api/mt-interface-data/";// 生产环境
	private static final String RunEnvUrl_Prod = "http://192.168.6.40:8280/api/mt-interface-data/";// 生产环境
	private static final String RunEnvUrl_Uat = "http://192.168.6.251:8843/indata/trunk/indooradmin/api.php?r=";// UAT环境
	private static final String RunEnvUrl_Test = "http://192.168.6.251:8838/indooradmin/api.php?r=";// 测试环境
	private static final String RunEnvUrl_Debug = "http://192.168.6.251:8873/indoor/indoor/trunk/indooradmin/api.php?r=";// 调试环境
	private static final String RunEnvTag_Prod = "p";
	private static final String RunEnvTag_Uat = "u";
	private static final String RunEnvTag_Test = "t";
	private static final String RunEnvTag_Debug = "d";
//	private static final String RunEnvUrl_Oper = "http://192.168.6.41/indooradmin/api.php?r=";
//	private static final String RunEnvUrl_Oper = "http://192.168.6.206:8381/api/mt-interface-data/";
	private static final String RunEnvUrl_Oper = "http://192.168.6.40:8280/api/mt-interface-data/";
	/**
	 * 保存与服务端交互的URL
	 */
	private static String RunEnvUrl = null;
	/**
	 * a cookie ,but here we dont need it
	 */
	private static String cookie = null;

	/**
	 * 获取一个关于运行环境Tag和URL的表
	 */
	private static HashMap<String, String> getRunEnvMap() {
		HashMap<String, String> hmap = new HashMap<String, String>();
		hmap.put(RunEnvTag_Prod, RunEnvUrl_Prod);
		hmap.put(RunEnvTag_Uat, RunEnvUrl_Uat);
		hmap.put(RunEnvTag_Test, RunEnvUrl_Test);
		hmap.put(RunEnvTag_Debug, RunEnvUrl_Debug);
		return (hmap);
	}

	/**
	 * 获取运行环境的tag
	 */
	private static String getRunEnvTag() {
		try {
			PackageInfo packageInfo = BDMapApp.getInstance()
					.getPackageManager()
					.getPackageInfo(BDMapApp.getInstance().getPackageName(), 0);
			String versionName = packageInfo.versionName;
			Log.d("versionName", versionName);
			return (versionName.substring(versionName.length() - 1,
					versionName.length()));
		} catch (Exception e) {
			Log.e("indoormap", "EnvConfig.getRunEnvId:" + e.getMessage());
		}
		return (RunEnvTag_Prod);
	}

	/**
	 * 与服务端交互的URL
	 */
	public static String getUrl() {
		if (RunEnvUrl == null) {
			HashMap<String, String> RunEnvMap = getRunEnvMap();
			String RunEnvTag = getRunEnvTag();
			RunEnvUrl = RunEnvMap.get(RunEnvTag);
		}
		return (RunEnvUrl);
	}

	/**
	 * cookie
	 */
	public static String getCookie() {
		return (cookie);
	}
	
	/**
	 * TASK #3800 室内数据Android端功能增强
	 * TODO:get the url to operate
	 * @date:2017-2-23
	 * @autor:lu.sj
	 * @param code
	 * @return
	 */
	public static String getOperUrl(int code){
		String requestUrl = null;
		switch (code) {
		case 1:
			requestUrl = RunEnvUrl_Oper+"MtSignalCollection";
			break;
		case 2:
			requestUrl = RunEnvUrl_Oper+"GetMtSignalList";
			break;
		default:
			break;
		}
		return requestUrl;
	}
}