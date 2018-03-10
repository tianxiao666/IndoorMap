package com.iscreate.mobile.indoormap.activity;

import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.LocationData;
import com.iscreate.mobile.baidu.BDMapApp;
import com.iscreate.mobile.indoormap.R;
import com.iscreate.mobile.service.GsonService;
import com.iscreate.mobile.service.ServiceClientInterface;

public class SearchActivity extends LogLifecircleActivity {

	public static final String searchKey = "searchKey";
	private final String[] SearchKeyList = { "入口", "出口", "无障碍出入口", "紧急出口",
			"扶梯", "电梯", "步行梯", "货梯", "洗手间", "女洗手间", "男洗手间", "无障碍洗手间", "母婴室",
			"餐饮", "购物", "商户", "娱乐", "电话", "VIP室", "服务台", "停车场", "客服中心", "收银台",
			"询问", "休息处", "行李提取", "行李寄存", "票务服务", "医疗急救", "自动检票机", "售票处",
			"自动提款机", "登机口", "值机岛", "超规行李托运", "邮局", "边检", "安检处", "行李查询", "吸烟处" };
	private final int WHAT_LOADSEARCHRESULT = 0;
	private GridView fastsearchkey_gv = null;
	private EditText searchkey_et = null;
	private List<HashMap<String, String>> buildingResList = null;
	private ListView buildingres_lv = null;
	private int SearchResultTotalCount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		initComponent();
		initComponentControl();
	}

	/**
	 * 初始化控件
	 */
	private void initComponent() {
		searchkey_et = (EditText) findViewById(R.id.searchkey_et);
		fastsearchkey_gv = (GridView) findViewById(R.id.fastsearchkey_gv);
		buildingres_lv = (ListView) findViewById(R.id.buildingres_lv);
	}

	/**
	 * 初始化组件控制
	 */
	private void initComponentControl() {
		fastsearchkey_gv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				searchkey_et.setText(SearchKeyList[position]);
				new GetMatchBuildingListThread().start();
			}
		});
		findViewById(R.id.search_btn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new GetMatchBuildingListThread().start();
			}
		});
		buildingres_lv.setVisibility(View.GONE);
		buildingres_lv.setAdapter(new BaseAdapter() {
			@Override
			public int getCount() {
				return ((buildingResList == null) ? 0 : buildingResList.size());
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View v = LayoutInflater.from(SearchActivity.this).inflate(
						R.layout.buildingitem, null);
				ImageView iv_building_icon = (ImageView) v
						.findViewById(R.id.iv_building_icon);
				TextView tv_building_name = (TextView) v
						.findViewById(R.id.tv_building_name);
				TextView tv_building_distance = (TextView) v
						.findViewById(R.id.tv_building_distance);
				TextView tv_building_note = (TextView) v
						.findViewById(R.id.tv_building_note);
				tv_building_name.setText(buildingResList.get(position).get(
						"BUILDING_NAME"));
				tv_building_distance.setText(buildingResList.get(position).get(
						"DISTANCE"));
				tv_building_note.setText(buildingResList.get(position).get(
						"NOTE"));
				String BUILD_TYPE = buildingResList.get(position).get(
						"BUILD_TYPE");
				Integer iconResId = getBuildingIconResId(BUILD_TYPE);
				if (iconResId != null) {
					iv_building_icon.setBackgroundResource(iconResId);
				}
				return (v);
			}

			@Override
			public Object getItem(int position) {
				return null;
			}

			@Override
			public long getItemId(int position) {
				return 0;
			}
		});
		buildingres_lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				HashMap<String, String> map = buildingResList.get(position);
				gotoIndoorMap(map.get("BUILDING_ID"), map.get("BUILDING_NAME"));
			}
		});
		fastsearchkey_gv.setAdapter(new BaseAdapter() {
			@Override
			public int getCount() {
				return (SearchKeyList.length);
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View v = LayoutInflater.from(SearchActivity.this).inflate(
						R.layout.imagetext, null);
				TextView fastsearchkey_tv = (TextView) v
						.findViewById(R.id.fastsearchkey_tv);
				fastsearchkey_tv.setText(SearchKeyList[position]);
				return v;
			}

			@Override
			public Object getItem(int position) {
				return null;
			}

			@Override
			public long getItemId(int position) {
				return 0;
			}
		});
		searchkey_et.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					new GetMatchBuildingListThread().start();
					return (true);
				}
				return (false);
			}
		});
	}

	/**
	 * get the Building type icon id via building type
	 * 
	 * @param BUILD_TYPE
	 *            the building type
	 * @return get the Building type icon id
	 */
	private Integer getBuildingIconResId(String BUILD_TYPE) {
		HashMap<String, Integer> buildingIconMap = new HashMap<String, Integer>();
		buildingIconMap.put("LARGE", R.drawable.building_type_large);
		buildingIconMap.put("TRAFF", R.drawable.building_type_traff);
		buildingIconMap.put("OFFIC", R.drawable.building_type_office);
		buildingIconMap.put("MALL_", R.drawable.building_type_mall_);
		return (buildingIconMap.get(BUILD_TYPE));
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case WHAT_LOADSEARCHRESULT:
				if (msg.arg2 == 1) {
					if (msg.obj != null) {
						buildingResList = (List<HashMap<String, String>>) msg.obj;
						((BaseAdapter) buildingres_lv.getAdapter())
								.notifyDataSetChanged();
						buildingres_lv.setVisibility(View.VISIBLE);
						fastsearchkey_gv.setVisibility(View.GONE);
					}
					Toast.makeText(SearchActivity.this,
							"查找到" + SearchResultTotalCount + "条记录！",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(SearchActivity.this, (String) msg.obj,
							Toast.LENGTH_SHORT).show();
				}
				break;
			}
		}
	};

	/**
	 * 开启一个线程搜索
	 */
	private class GetMatchBuildingListThread extends Thread {
		@Override
		public void run() {
			Message msg = handler.obtainMessage();
			msg.what = WHAT_LOADSEARCHRESULT;
			try {
				msg.obj = GetMatchBuildingList();
				msg.arg2 = 1;
			} catch (Exception e) {
				msg.obj = e.getMessage();
				msg.arg2 = 0;
			}
			handler.sendMessage(msg);
		}
	}

	/**
	 * 搜索
	 */
	private List<HashMap<String, String>> GetMatchBuildingList()
			throws Exception {
		String keywords = searchkey_et.getText().toString();
		if ((keywords != null) && (keywords.length() > 0)) {
			int actionID = ServiceClientInterface.ID_ACTION_GetMatchBuildingList;
			LocationData locData = BDMapApp.getInstance().getBDLocationData();
			String parmas[] = { "" + locData.longitude,//
					"" + locData.latitude,//
					keywords,//
					"0",//
					"-1"//
			};
			String content = ServiceClientInterface.request(actionID, parmas);
			HashMap<String, String> contentMap = GsonService
					.gsonGetHashMap(content);
			if (contentMap.size() > 0) {
				SearchResultTotalCount = Integer.valueOf(contentMap
						.get("TOTALCOUNT"));
				String MatchBuildingListStr = contentMap
						.get("MatchBuildingList");
				List<HashMap<String, String>> MatchBuildingList = GsonService
						.gsonGetListHashMap(MatchBuildingListStr);
				return (MatchBuildingList);
			} else {
				SearchResultTotalCount = 0;
				return (null);
			}
		} else {
			throw (new Exception("请输入关键字！"));
		}
	}

	/**
	 * 如果当前显示查看结果，先退出
	 */
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (fastsearchkey_gv.getVisibility() != View.VISIBLE) {
				buildingres_lv.setVisibility(View.GONE);
				fastsearchkey_gv.setVisibility(View.VISIBLE);
				return (true);
			}
		}
		return (super.onKeyUp(keyCode, event));
	}

	/**
	 * 进入查看平面图
	 */
	private void gotoIndoorMap(String BUILDING_ID, String BUILDING_NAME) {
		Intent intent = new Intent(SearchActivity.this, IndoorMapActivity.class);
		intent.putExtra("BUILDING_ID", BUILDING_ID);
		intent.putExtra("BUILDING_NAME", BUILDING_NAME);
		startActivity(intent);
	}
}