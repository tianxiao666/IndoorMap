package com.iscreate.mobile.indoormap.activity;

import java.util.HashMap;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.iscreate.mobile.indoormap.R;
import com.iscreate.mobile.service.GsonService;

public class mainActivity extends TabActivity {
	/**
	 * @see TabHost
	 */
	private TabHost tabHost = null;
	/**
	 * @see IndoorMapActivity
	 */
	private TabSpec indoormapActivitynewTabSpec = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.indoormain);
		tabHost = getTabHost();
		tabHost.getTabWidget().setVisibility(View.GONE);
		tabHost.addTab(tabHost.newTabSpec("BaiduMapActivity")
				.setIndicator("BaiduMapActivity")
				.setContent(new Intent(this, BaiduMapActivity.class)));
		indoormapActivitynewTabSpec = tabHost.newTabSpec("IndoorMapActivity")
				.setIndicator("IndoorMapActivity").setContent(R.id.process_pb);
		tabHost.addTab(indoormapActivitynewTabSpec);
		onHandleIntent(getIntent());
	}

	/**
	 * 处理Intent事件
	 */
	private void onHandleIntent(Intent intent) {
		String content = intent.getStringExtra("getlocationcontent");
		HashMap<String, String> contentMap = null;
		if (content != null) {
			try {
				contentMap = GsonService.gsonGetHashMap(content);
			} catch (Exception e) {
			}
		}
		if (contentMap != null && contentMap.size() > 0) {
			Intent it = new Intent(mainActivity.this, IndoorMapActivity.class);
			it.putExtra("BUILDING_ID", contentMap.get("BUILDING_ID"));
			it.putExtra("BUILDING_NAME", contentMap.get("BUILDING_NAME"));
			it.putExtra("FLOOR_ID", contentMap.get("FLOOR_ID"));
			it.putExtra("DRAW_MAP_ID", contentMap.get("DRAW_MAP_ID"));
			it.putExtra("SVGSRC", contentMap.get("SVGSRC"));
			it.putExtra("X", contentMap.get("X"));
			it.putExtra("Y", contentMap.get("Y"));
			it.putExtra("LOCATION", contentMap.get("LOCATION"));
			it.putExtra("GOBACKBD", true);
			it.putExtra("operType", intent.getStringExtra("operType"));
			indoormapActivitynewTabSpec.setContent(it);
			tabHost.setCurrentTabByTag("IndoorMapActivity");
		} else {
			String BUILDING_ID = intent.getStringExtra("BUILDING_ID");
			String BUILDING_NAME = intent.getStringExtra("BUILDING_NAME");
			String RB_LATITUDEL = intent.getStringExtra("RB_LATITUDEL");
			String LT_LATITUDEL = intent.getStringExtra("LT_LATITUDEL");
			String RB_LONGITUDEL = intent.getStringExtra("RB_LONGITUDEL");
			String LT_LONGITUDEL = intent.getStringExtra("LT_LONGITUDEL");
			if (BUILDING_ID != null) {
				Intent it = new Intent(mainActivity.this,
						IndoorMapActivity.class);
				it.putExtra("BUILDING_ID", BUILDING_ID);
				it.putExtra("BUILDING_NAME", BUILDING_NAME);
				it.putExtra("RB_LATITUDEL", RB_LATITUDEL);
				it.putExtra("LT_LATITUDEL", LT_LATITUDEL);
				it.putExtra("RB_LONGITUDEL", RB_LONGITUDEL);
				it.putExtra("LT_LONGITUDEL", LT_LONGITUDEL);
				it.putExtra("GOBACKBD", true);
				it.putExtra("operType", intent.getStringExtra("operType"));
				indoormapActivitynewTabSpec.setContent(it);
				tabHost.setCurrentTabByTag("IndoorMapActivity");
			} else {
				tabHost.setCurrentTabByTag("BaiduMapActivity");
			}
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		onHandleIntent(intent);
		super.onNewIntent(intent);
	}

	/**
	 * 如果显示室内地图就回到场所列表，如果当前为场所列表则提示退出
	 */
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (tabHost.getCurrentTabTag() == "IndoorMapActivity") {
				tabHost.setCurrentTabByTag("BaiduMapActivity");
				return (true);
			}
		}
		return (super.onKeyUp(keyCode, event));
	}
}