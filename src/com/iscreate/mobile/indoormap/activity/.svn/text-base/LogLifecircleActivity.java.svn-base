package com.iscreate.mobile.indoormap.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class LogLifecircleActivity extends Activity {
	/**
	 * Log的tag
	 */
	private String logTag = "indoormap";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(logTag, getClass().getName() + "---onCreate");
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onRestart() {
		Log.i(logTag, getClass().getName() + "---onRestart");
		super.onRestart();
	}

	@Override
	protected void onStart() {
		Log.i(logTag, getClass().getName() + "---onStart");
		super.onStart();
	}

	@Override
	protected void onResume() {
		Log.i(logTag, getClass().getName() + "---onResume");
		super.onResume();
	}

	@Override
	protected void onPause() {
		Log.i(logTag, getClass().getName() + "---onPause");
		super.onPause();
	}

	@Override
	protected void onStop() {
		Log.i(logTag, getClass().getName() + "---onStop");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		Log.i(logTag, getClass().getName() + "---onDestroy");
		super.onDestroy();
	}

	/**
	 * 函数log日记
	 */
	public void logFunction(String log) {
		Log.i(logTag, log);
	}

	/**
	 * 错误log日记
	 */
	public void logError(String log) {
		Log.e(logTag, log);
	}
}
