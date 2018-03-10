package com.iscreate.mobile.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;

public class DownloadApkAsyncTask extends AsyncTask<String, Integer, String> {
	/**
	 * a type for update the upper range of a progress bar
	 */
	public static final int UPDATE_MAX = 0;
	/**
	 * a type for update process for a progress bar
	 */
	public static final int UPDATE_PROGRESS = 1;
	/**
	 * stop this thread
	 */
	private boolean stop = false;

	/**
	 * download a file
	 */
	@Override
	protected String doInBackground(String... params) {
		String f = ApkUpdateUtil.getDownloadPathFor("IndoorMap.apk");
		if (f != null) {
			if ((params != null) && (params.length > 0)) {
				String urlstr = params[0];
				if (urlstr != null) {
					String cookie = null;
					Integer totalsize = null;
					if (params.length > 1) {
						try {
							totalsize = Integer.parseInt(params[1]);
						} catch (Exception e) {
						}
					}
					if (params.length > 2) {
						cookie = params[2];
					}
					try {
						URL url = new URL(urlstr);
						HttpURLConnection httpurlc = (HttpURLConnection) url
								.openConnection();
						httpurlc.setRequestProperty("cookie", cookie);
						InputStream is = httpurlc.getInputStream();
						if (totalsize == null) {
							totalsize = is.available();
						}
						int max = totalsize / 1024;
						publishProgress(UPDATE_MAX, max);
						File file = new File(f);
						file.createNewFile();
						int process = 0;
						FileOutputStream fos = new FileOutputStream(file);
						byte[] temp = new byte[1024];
						int readbytes = 0;
						while ((readbytes = is.read(temp)) > 0) {
							if (stop) {
								f = null;
								break;
							} else {
								fos.write(temp, 0, readbytes);
								++process;
								publishProgress(UPDATE_PROGRESS, process);
							}
						}
						fos.close();
						is.close();
						httpurlc.disconnect();
						return (f);
					} catch (Exception e) {
					}
				}
			}
		}
		return (null);
	}

	public void stop() {
		stop = true;
		cancel(true);
	}
}