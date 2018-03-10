package com.iscreate.mobile.utils;

import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;

import com.iscreate.mobile.service.GsonService;
import com.iscreate.mobile.service.ServiceClientInterface;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Environment;

public class ApkUpdateUtil {
	/**
	 * install an apk file
	 * 
	 * @param activity
	 *            a activity where you want to get result in an override
	 *            onActivityResult
	 * @param file
	 *            the apk file you want to install
	 * @param requestCode
	 *            an request code where will be received your override
	 *            onActivityResult
	 */
	public static void installApkForResult(Activity activity, File file,
			int requestCode) {
		Intent intent = new Intent();
		// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		String type = "application/vnd.android.package-archive";
		intent.setDataAndType(Uri.fromFile(file), type);
		activity.startActivityForResult(intent, requestCode);
	}

	/**
	 * get an download path for a file name
	 * 
	 * @param fname
	 *            the file name not path
	 * @return the file path if success ,otherwise false
	 */
	public static String getDownloadPathFor(String fname) {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			File extsddir = Environment.getExternalStorageDirectory();
			if (extsddir.canWrite()) {
				String downloadpath = Environment.getExternalStorageDirectory()
						+ "/" + "download";
				File downloadpathfile = new File(downloadpath);
				if ((!downloadpathfile.exists())
						|| (!downloadpathfile.isDirectory())) {
					downloadpathfile.mkdirs();
				}
				String fpath = downloadpath + "/" + fname;
				return (fpath);
			}
		}
		return (null);
	}

	/**
	 * request the service to get a new version information
	 * 
	 * @param versionCode
	 *            the version Code of this apk
	 * @param versionName
	 *            the version name of this apk
	 * @return a new version information
	 * @throws Exception
	 */
	public static HashMap<String, String> getNewVersionInfo(int versionCode,
			String versionName) throws Exception {
		int actionID = ServiceClientInterface.ID_ACTION_CheckNewVersion;
		String[] params = { versionName, "" + versionCode };
		String content = ServiceClientInterface.request(actionID, params);
		HashMap<String, String> contentMap = GsonService
				.gsonGetHashMap(content);
		return (contentMap);
	}

	/**
	 * convert a file size in bytes to string
	 * 
	 * @param size
	 *            the size in bytes
	 * @return the size in string
	 */
	public static String fileSizeToStr(int size) {
		Integer filesize = size;
		Double filesizeInFloat = filesize.doubleValue();
		Double sizeInM = (filesizeInFloat / 1024 / 1024);
		String outputSize = "";
		DecimalFormat df = new DecimalFormat("#.00");
		if (sizeInM.doubleValue() > 1d) {
			outputSize = df.format(sizeInM) + "M";
		} else {
			Double sizeInK = (filesizeInFloat / 1024);
			outputSize = df.format(sizeInK) + "K";
		}
		return (outputSize);
	}

	/**
	 * convert the version information to a format string
	 * 
	 * @param apkVersionInfomap
	 *            a version information
	 * @return the the version information string
	 */
	public static String getApkVersionInfoStr(
			HashMap<String, String> apkVersionInfomap) {
		String updateDate = apkVersionInfomap.get("UpdateTime");
		String outputSize = apkVersionInfomap.get("FileSize");
		try {
			outputSize = fileSizeToStr(Integer.valueOf(outputSize));
		} catch (Exception e) {
		}
		String updateInfoStr = "";
		updateInfoStr += "新版本号:" + apkVersionInfomap.get("VersionName") + "\n";
		updateInfoStr += "更新大小:" + outputSize + "\n";
		updateInfoStr += "更新时间:" + updateDate + "\n";
		String updateinfo = apkVersionInfomap.get("UpdateDescription");
		if (updateinfo != null) {
			String[] updateinfolist = updateinfo.split(";");
			if (updateinfolist.length > 1) {
				updateinfo = null;
				for (String s : updateinfolist) {
					s = s.trim();
					if (s.length() > 0) {
						if (updateinfo == null) {
							updateinfo = "\n               " + s;
						} else {
							updateinfo = updateinfo + "\n               " + s;
						}
					}
				}
				if (updateinfo == null) {
					updateinfo = "";
				}
			}
		} else {
			updateinfo = "";
		}
		updateInfoStr += "更新内容:" + updateinfo + "\n";
		updateInfoStr += "您确定要下载更新吗？";
		return (updateInfoStr);
	}

	/**
	 * get this apk package information
	 * 
	 * @param context
	 *            a context
	 * @return PackageInfo if success,otherwise null
	 * 
	 */
	public static PackageInfo getAppPackageInfo(Context context) {
		try {
			return (context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0));
		} catch (Exception e) {
		}
		return (null);
	}

}
