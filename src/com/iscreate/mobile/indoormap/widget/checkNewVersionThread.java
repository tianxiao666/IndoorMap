package com.iscreate.mobile.indoormap.widget;

import java.util.HashMap;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Handler;
import android.os.Message;

import com.iscreate.mobile.utils.ApkUpdateUtil;

public class checkNewVersionThread extends Thread {
	/**
	 * the application or activity
	 */
	private Context context = null;
	/**
	 * use this handler to sent message back
	 */
	private Handler handler = null;
	/**
	 * handler will receive this id in message
	 */
	private Integer whatId = null;

	public checkNewVersionThread(Context context, Handler handler, int whatId) {
		this.context = context;
		this.handler = handler;
		this.whatId = whatId;
	}

	/**
	 * sent the message back to handler
	 */
	@Override
	public void run() {
		Message msg = handler.obtainMessage();
		msg.what = whatId;
		try {
			msg.obj = getNewVersionInfoMap();
			msg.arg2 = 1;
		} catch (Exception e) {
			msg.arg2 = 0;
		}
		handler.sendMessage(msg);
	}

	/**
	 * get the new version information from server
	 * 
	 * @return the new version information map if has new version and not error
	 *         occurred
	 */
	private HashMap<String, String> getNewVersionInfoMap() throws Exception {
		PackageInfo packageInfo = ApkUpdateUtil.getAppPackageInfo(context);
		if (packageInfo != null) {
			HashMap<String, String> versionInfomap = null;
			versionInfomap = ApkUpdateUtil.getNewVersionInfo(
					packageInfo.versionCode, packageInfo.versionName);
			if (versionInfomap != null) {
				String urlstr = versionInfomap.get("url");
				if ((urlstr != null) && (urlstr.length() > 0)) {
					return (versionInfomap);
				}
			}
		}
		return (null);
	}
}
