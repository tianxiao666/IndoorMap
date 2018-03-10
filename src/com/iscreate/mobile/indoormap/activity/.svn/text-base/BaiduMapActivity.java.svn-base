package com.iscreate.mobile.indoormap.activity;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.crypto.BadPaddingException;

import android.R.integer;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKMapTouchListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.iscreate.mobile.baidu.BDMapApp;
import com.iscreate.mobile.indoormap.R;
import com.iscreate.mobile.indoormap.widget.ChooseDialog;
import com.iscreate.mobile.indoormap.widget.DatePickerDialog;
import com.iscreate.mobile.indoormap.widget.DoSureDialog;
import com.iscreate.mobile.indoormap.widget.requestDataThread;
import com.iscreate.mobile.service.GsonService;
import com.iscreate.mobile.service.ServiceClientInterface;
import com.iscreate.mobile.utils.utils;
import com.iscreate.mobile.widget.LeftSliderLayout;
import com.iscreate.mobile.widget.LeftSliderLayout.OnLeftSliderLayoutStateListener;
import com.iscreate.mobile.widget.LoadMoreListView;
import com.iscreate.mobile.widget.LoadMoreListView.OnLoadMoreDataListener;

public class BaiduMapActivity extends BDMapActivity implements OnClickListener {
	/**
	 * 当前Activity显示的内容
	 * 
	 * @see #setContentView(View)
	 */
	private View contentView = null;
	/**
	 * 百度API中的MapView
	 */
	private MapView baidumap_mv = null;
	/**
	 * 百度API中的MapController，主要用来控制百度地图的相关控制，如移动，缩放等
	 * 
	 * @see #initBaiduMapController()
	 */
	private MapController mMapController = null;
	/**
	 * 百度API中的LocationOverlay，用来显示当前位置
	 */
	private LocationOverlay myLocationOverlay = null;
	/**
	 * 在百度定位服务开启后如果注册了BDLocationListener，
	 * 会在onReceiveLocation方法下收到当前的位置信息BDLocation
	 * ，在收到位置信息BDLocation后isFirstLoc为false，
	 * 
	 * @see BDLocListenner
	 */
	private boolean isFirstLoc = true;
	/**
	 * 缩放所需要的手指数
	 */
	private final int ZoomPointerCount = 2;
	/**
	 * 忽略手指在屏幕上周长变化值
	 */
	private final float ZoomPointerPerimeterChange = 20;
	/**
	 * Touch事件时，手指在屏幕上的周长
	 */
	private Double PointerPerimeter = null;
	/**
	 * 场所覆盖物
	 */
	private IndoorMapOverlay indoorMapOverlay = null;
	/**
	 * 场所列表数据
	 */
	private List<HashMap<String, String>> buildingResList = null;
	/**
	 * 总场所数
	 */
	private int buildingResTotalCount = 0;
	/**
	 * 显示场所列表所用的ListView
	 */
	private LoadMoreListView buildingres_lv = null;
	/**
	 * 显示场所列表所用的适配器
	 */
	private BaseAdapter buildingres_lv_adapter = null;
	/**
	 * 用于UI线程中接收请求数据的消息
	 */
	private final int WHAT_REQUESTDATA = 0;
	/**
	 * 耗时操作，显示进度
	 */
	private ProgressDialog pd = null;
	/**
	 * 显示当前位置
	 */
	private TextView locatione_tv = null;
	/**
	 * 侧滑控件
	 */
	private LeftSliderLayout leftSliderLayout = null;
	/**
	 * 是否正在加载场所列表数据
	 */
	private boolean isreadingdata = false;
	/**
	 * 场所列表每次加载数据的条次
	 */
	private final int PAGESIZE = 20;
	/**
	 * 百度地图MapController是否已经初始化
	 */
	private boolean isBaiduMapControllerInited = false;
	/**
	 * 百度地图是否初始化
	 */
	private boolean isBaiduMapInited = false;
	/**
	 * 百度地图覆盖物是否初始化
	 */
	private boolean isBaiduMapOverlayInited = false;
	/**
	 * a BDLocationListener to register to LocationClient
	 */
	private BDLocationListener mLocListener = null;

	/**
	 * 百度地图覆盖物提示
	 */
	private View baiduOverlayerView = null;
	/**
	 * 取附近场所时的位置信息
	 */
	private LocationData NearbyBuildinglocData = null;

	/**
	 * 更新当前位置及第一次定位移动到当前位置
	 */
	private class BDLocListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location != null) {
				// logFunction(getClass().getName() + ".onReceiveLocation");
				LocationData locData = new LocationData();
				locData.longitude = location.getLongitude();
				locData.latitude = location.getLatitude();
				locData.accuracy = location.getRadius();
				locData.direction = location.getDerect();
				locData.speed = location.getSpeed();
				locData.satellitesNum = location.getSatelliteNumber();
				if (myLocationOverlay != null) {
					myLocationOverlay.setData(locData);
					baidumap_mv.refresh();
				}
				if (isFirstLoc && isBaiduMapControllerInited) {
					animateToCurrentLocation();
				}
				isFirstLoc = false;
				locatione_tv.setText(location.getAddrStr());
				updateDistance(locData);
			}
		}

		@Override
		public void onReceivePoi(BDLocation location) {

		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView = LayoutInflater.from(this).inflate(R.layout.baidu, null);
		setContentView(contentView);
		initComponent();
		setBDMapView(baidumap_mv);
		initComponentControl();
		/**
		 * 注册mLocListener
		 */
		BDMapApp.getInstance().registerBDLocationListener(mLocListener);
	}

	/**
	 * 初始化控件
	 */
	private void initComponent() {
		logFunction("initComponent");
		locatione_tv = (TextView) findViewById(R.id.locatione_tv);
		leftSliderLayout = (LeftSliderLayout) findViewById(R.id.main_slider_layout);
		baidumap_mv = (MapView) findViewById(R.id.baidumap_mv);
		buildingres_lv = (LoadMoreListView) findViewById(R.id.buildingres_lv);
		findViewById(R.id.btn_listmode).setOnClickListener(this);
		findViewById(R.id.search_btn).setOnClickListener(this);
		findViewById(R.id.location_btn).setOnClickListener(this);
		findViewById(R.id.more_btn).setOnClickListener(this);
		findViewById(R.id.about_btn).setOnClickListener(this);
		findViewById(R.id.LayerSelection_btn).setVisibility(View.GONE);
		findViewById(R.id.WifiSignalTestMenu_btn).setOnClickListener(this);
		findViewById(R.id.StepTestMenu_btn).setOnClickListener(this);
		findViewById(R.id.exit_btn).setOnClickListener(this);
		pd = new ProgressDialog(BaiduMapActivity.this);
		mLocListener = new BDLocListenner();
	}

	/**
	 * 初始化组件控制
	 */
	private void initComponentControl() {
		logFunction("initComponentControl");
		buildingres_lv.setVisibility(View.VISIBLE);
		buildingres_lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				final ChooseDialog chooseDialog = new ChooseDialog(BaiduMapActivity.this,R.style.ChooseDialog);
				chooseDialog.show();
				chooseDialog.setCanceledOnTouchOutside(true);
				chooseDialog.setChooseClickInterface(new ChooseDialog.ChooseClickInterface() {
					
					@Override
					public void viewBtnClick() {
						chooseDialog.dismiss();
						gotoIndoorMap(buildingResList.get(position),"2");
					}
					
					@Override
					public void collectBtnClick() {
						chooseDialog.dismiss();
						gotoIndoorMap(buildingResList.get(position),"1");
					}
				});
			}
		});
		buildingres_lv.setVisibility(View.VISIBLE);
		buildingres_lv.setOnLoadMoreDataListener(new OnLoadMoreDataListener() {
			@Override
			public void OnLoadMore(int mode, int totalItemCount) {
				if (isreadingdata) {
				} else {
					if ((buildingResList == null)
							|| (buildingResList.size() < buildingResTotalCount)) {
						isreadingdata = true;
						startThreadToGetNearbyBuildingList();
					}
				}
			}
		});
		// buildingres_lv_adapter = new SimpleAdapter(this, buildingResList,
		// R.layout.buildingitem, new String[] { "BUILDING_NAME" },
		// new int[] { R.id.tv_building_name });
		buildingres_lv_adapter = new BaseAdapter() {
			@Override
			public int getCount() {
				return ((buildingResList == null) ? 0 : buildingResList.size());
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View v = LayoutInflater.from(BaiduMapActivity.this).inflate(
						R.layout.buildingitem, null);
				ImageView iv_building_icon = (ImageView) v
						.findViewById(R.id.iv_building_icon);
				TextView tv_building_name = (TextView) v
						.findViewById(R.id.tv_building_name);
				TextView tv_building_distance = (TextView) v
						.findViewById(R.id.tv_building_distance);
				TextView tv_building_note = (TextView) v
						.findViewById(R.id.tv_building_note);
				HashMap<String, String> buildingInfo = buildingResList
						.get(position);
				tv_building_name.setText(buildingInfo.get("BUILDING_NAME"));
				tv_building_distance.setText(buildingInfo.get("DISTANCE"));
				tv_building_note.setText(buildingInfo.get("NOTE"));
				String BUILD_TYPE = buildingInfo.get("BUILD_TYPE");
				Integer iconResId = getBuildingIconResId(BUILD_TYPE);
				if (iconResId != null) {
					iv_building_icon.setBackgroundResource(iconResId);
				} else {
					logError("buildingres_lv_adapter.getView.iconResId为空！"
							+ BUILD_TYPE);
				}
				return (v);
			}

			@Override
			public long getItemId(int position) {
				return 0;
			}

			@Override
			public Object getItem(int position) {
				return null;
			}
		};
		buildingres_lv.setAdapter(buildingres_lv_adapter);
		baidumap_mv.setVisibility(View.GONE);
		pd.setTitle("请稍后");
		pd.setMessage("正在获取附近场所信息......");
		NearbyBuildinglocData = BDMapApp.getInstance().getBDLocationData();
		startThreadToGetNearbyBuildingList();
		leftSliderLayout.enableSlide(true);
		leftSliderLayout
				.setOnLeftSliderLayoutListener(new OnLeftSliderLayoutStateListener() {

					@Override
					public void OnLeftSliderLayoutStateChanged(boolean bIsOpen) {

					}

					@Override
					public boolean OnLeftSliderLayoutInterceptTouch(
							MotionEvent ev) {
						return (buildingres_lv.getVisibility() == View.VISIBLE);
					}
				});
	}

	/**
	 * 注销mLocListener
	 */
	@Override
	protected void onDestroy() {
		clearOverlay();
		BDMapApp.getInstance().unRegisterBDLocationListener(mLocListener);
		super.onDestroy();
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

	private void gotoSearch() {
		logFunction("gotoSearch");
		Intent intent = new Intent(BaiduMapActivity.this, SearchActivity.class);
		startActivity(intent);
	}

	/**
	 * 新建一个Handler接受Message
	 */
	private Handler handler = new Handler(Looper.getMainLooper()) {
		@Override
		public void handleMessage(Message msg) {
			logFunction("handler.handleMessage");
			String ErrorMessage = null;
			switch (msg.what) {
			case WHAT_REQUESTDATA:
				if (msg.arg2 == 1) {
					try {
						handleContent(msg.arg1, (String) msg.obj);
					} catch (Exception e) {
						ErrorMessage = e.getMessage();
					}
				} else {
					ErrorMessage = (String) msg.obj;
				}
				if (ErrorMessage != null) {
					handleError(msg.arg1);
				}
				break;
			}
			if (ErrorMessage != null) {
				Toast.makeText(BaiduMapActivity.this, ErrorMessage,
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	/**
	 * start a thread to get the current location
	 */
	private void startThreadToGetLocation() {
		logFunction("startThreadToGetLocation");
		String WifiListJsonStr = testWifiActivity
				.getWifiListJsonStr(scanresultlist);
		if (WifiListJsonStr == null) {
			WifiListJsonStr = "";
		}
		LocationData locData = BDMapApp.getInstance().getBDLocationData();
		String[] params = {//
		"" + locData.longitude,//
				"" + locData.latitude,//
				WifiListJsonStr //
		};
		new requestDataThread(handler,//
				WHAT_REQUESTDATA,//
				ServiceClientInterface.ID_ACTION_GetLocation, //
				params//
		).start();
	}

	/**
	 * 计算距离
	 */
	private List<HashMap<String, String>> calcDistance(
			List<HashMap<String, String>> buildingResList) {
		if (buildingResList != null) {
			LocationData locData = BDMapApp.getInstance().getBDLocationData();
			if ((locData.latitude != 0) || (locData.longitude != 0)) {
				float[] results = new float[1];
				HashMap<String, String> buildingInfo = null;
				int i = 0;
				int count = buildingResList.size();
				while (i < count) {
					buildingInfo = buildingResList.get(i);
					Location.distanceBetween(
							getBuildingInfoLatitude(buildingInfo),
							getBuildingInfoLongitude(buildingInfo),
							locData.latitude, locData.longitude, results);
					buildingInfo
							.put("DISTANCE",
									""
											+ ((float) Math
													.round(results[0] * 100) / 100000));
					buildingResList.set(i, buildingInfo);
					++i;
				}
			}
		}
		return (buildingResList);
	}

	/**
	 * 更新场所距离
	 */
	private void updateDistance(LocationData locData) {
		if (buildingResList != null) {
			if ((locData.latitude != 0) || (locData.longitude != 0)) {
				buildingResList = calcDistance(buildingResList);
				buildingres_lv_adapter.notifyDataSetChanged();
				if (buildingres_lv.getVisibility() != View.VISIBLE) {
					if (baiduOverlayerView != null) {
						Integer id = (Integer) baiduOverlayerView.getTag();
						if (id != null) {
							TextView tv_building_distance = (TextView) baiduOverlayerView
									.findViewById(R.id.tv_building_distance);
							HashMap<String, String> buildingInfo = buildingResList
									.get(id);
							tv_building_distance.setText(buildingInfo
									.get("DISTANCE"));
						}
					}
				}
			}
		}
	}

	/**
	 * start a thread to get the nearby buildings
	 */
	private void startThreadToGetNearbyBuildingList() {
		logFunction("startThreadToGetNearbyBuildingList");
		int index = (buildingResList == null) ? 0
				: ((buildingResList.size() / PAGESIZE) + 1);
		String params[] = { //
		"" + NearbyBuildinglocData.longitude,//
				"" + NearbyBuildinglocData.latitude,//
				"500",//
				"" + index,//
				"" + (index + PAGESIZE) //
		};
		new requestDataThread(handler,//
				WHAT_REQUESTDATA,//
				ServiceClientInterface.ID_ACTION_GetNearbyBuildingList, //
				params//
		).start();
	}

	/**
	 * 处理服务器上传下来的content
	 * 
	 * @param actionID
	 *            the interface action id in class ServiceClientInterface
	 * @param content
	 *            the content from the JSON response
	 * @return true if succeed
	 * @throws Exception
	 */
	private void handleContent(int actionID, String content) throws Exception {
		logFunction("handleContent");
		switch (actionID) {
		case ServiceClientInterface.ID_ACTION_GetNearbyBuildingList: {
			HashMap<String, String> contentMap = GsonService
					.gsonGetHashMap(content);
			if (contentMap.size() == 2) {
				try {
					buildingResTotalCount = Integer.valueOf(contentMap
							.get("TOTALCOUNT"));
				} catch (Exception e) {
					buildingResTotalCount = 0;
					buildingResList = null;
					logError("获取附近场所总数错误！");
					throw (new Exception("获取附近场所总数错误！"));
				}
				String IndoormapBuildingListStr = contentMap
						.get("NearbyBuildingList");
				List<HashMap<String, String>> IndoormapBuildingList = GsonService
						.gsonGetListHashMap(IndoormapBuildingListStr);
				if (IndoormapBuildingList != null) {
					IndoormapBuildingList = calcDistance(IndoormapBuildingList);
					if (buildingResList == null) {
						buildingResList = IndoormapBuildingList;
					} else {
						buildingResList.addAll(IndoormapBuildingList);
					}
					isBaiduMapOverlayInited = false;
					buildingres_lv_adapter.notifyDataSetChanged();
				}
			} else {
				buildingResTotalCount = 0;
				buildingResList = null;
				logError("附近没有场所！");
				throw (new Exception("附近没有场所！"));
			}
			isreadingdata = false;
			if (pd.isShowing()) {
				pd.dismiss();
			}
		}
			break;
		case ServiceClientInterface.ID_ACTION_GetLocation: {
			// HashMap<String, String> contentMap = GsonService
			// .gsonGetHashMap(content);
			// gotoIndoorMap(contentMap);
			pd.dismiss();
		}
			break;
		}
	}

	/**
	 * 服务器取content出错或处理数据出错时的处理
	 * 
	 * @param actionID
	 *            the interface action id in class ServiceClientInterface
	 */
	private void handleError(int actionID) {
		logFunction("handleError");
		switch (actionID) {
		case ServiceClientInterface.ID_ACTION_GetNearbyBuildingList:
			isreadingdata = false;
			pd.dismiss();
			break;
		case ServiceClientInterface.ID_ACTION_GetLocation:
			pd.setMessage("正在获取附近场所信息......");
			NearbyBuildinglocData = BDMapApp.getInstance().getBDLocationData();
			startThreadToGetNearbyBuildingList();
			break;
		}
	}

	/**
	 * 初始化百度地图 MapController
	 */
	private void initBaiduMapController() {
		logFunction("initBaiduMapController");
		/**
		 * 显示内置缩放控件
		 */
		baidumap_mv.setBuiltInZoomControls(true);
		/**
		 * 监听百度地图事件
		 */
		baidumap_mv.regMapTouchListner(new MKMapTouchListener() {
			@Override
			public void onMapClick(GeoPoint point) {
				if (baiduOverlayerView != null) {
					baidumap_mv.removeView(baiduOverlayerView);
					baiduOverlayerView = null;
				}
			}

			@Override
			public void onMapDoubleClick(GeoPoint point) {
			}

			@Override
			public void onMapLongClick(GeoPoint point) {
				popupOverlayer(point, null);
			}
		});
		/**
		 * 获取地图控制器
		 */
		mMapController = baidumap_mv.getController();
		/**
		 * 设置地图是否响应点击事件 .
		 */
		mMapController.enableClick(true);
		/**
		 * 设置地图缩放级别
		 */
		mMapController.setZoom(14);
	}

	/**
	 * 初始化百度地图
	 */
	private void initBaiduMap() {
		logFunction("initBaiduMap");
		/**
		 * 如果没有初始化要初始化
		 */
		if (!isBaiduMapControllerInited) {
			initBaiduMapController();
			isBaiduMapControllerInited = true;
		}
		/**
		 * 添加当前位置
		 */
		myLocationOverlay = new LocationOverlay(baidumap_mv);
		List<Overlay> overlaylist = baidumap_mv.getOverlays();
		if (overlaylist != null) {
			overlaylist.add(myLocationOverlay);
		}
		myLocationOverlay.enableCompass();
		baidumap_mv.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				boolean consumed = false;
				int action = event.getActionMasked();
				int PointerCount = event.getPointerCount();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					if (baidumap_mv.getZoomLevel() == baidumap_mv
							.getMaxZoomLevel()) {
						PointerPerimeter = -1d;
					} else {
						PointerPerimeter = null;
					}
					break;
				case MotionEvent.ACTION_UP:
					PointerPerimeter = null;
					break;
				case MotionEvent.ACTION_MOVE:
					if (PointerPerimeter != null) {
						if (PointerCount >= ZoomPointerCount) {
							if (PointerPerimeter == -1f) {
								PointerPerimeter = utils
										.getEventPointerPerimeter(event, false);
							}
							double nowPointerPerimeter = utils
									.getEventPointerPerimeter(event, false);
							if ((nowPointerPerimeter - PointerPerimeter) > ZoomPointerPerimeterChange
									* PointerCount) {
								if (baidumap_mv.getZoomLevel() == baidumap_mv
										.getMaxZoomLevel()) {
									consumed = handleMapViewMaxZoom(baidumap_mv);
									PointerPerimeter = null;
								} else {
									PointerPerimeter = nowPointerPerimeter;
								}
							}
						}
					}
					break;
				case MotionEvent.ACTION_POINTER_DOWN:
					if (PointerPerimeter != null) {
						PointerPerimeter = utils.getEventPointerPerimeter(
								event, false);
					}
					break;
				case MotionEvent.ACTION_POINTER_UP:
					if (PointerPerimeter != null) {
						if (PointerCount <= ZoomPointerCount) {
							PointerPerimeter = -1d;
						} else {
							PointerPerimeter = utils.getEventPointerPerimeter(
									event, false);
						}
					}
					break;
				}
				return (consumed);
			}
		});
	}

	/**
	 * 计算场所的经度
	 */
	private double getBuildingInfoLongitude(HashMap<String, String> buildingInfo) {
		if (buildingInfo != null) {
			try {
				double rb = Double.parseDouble(buildingInfo
						.get("RB_LONGITUDEL"));
				double lt = Double.parseDouble(buildingInfo
						.get("LT_LONGITUDEL"));
				return ((rb + lt) / 2);
			} catch (Exception e) {
				logError("解析" + buildingInfo.get("BUILDING_NAME") + "经度失败！"
						+ e.getMessage());
			}
		}
		return (113.345548d);

	}

	/**
	 * 计算场所的纬度
	 */
	private double getBuildingInfoLatitude(HashMap<String, String> buildingInfo) {
		if (buildingInfo != null) {
			try {
				double rb = Double
						.parseDouble(buildingInfo.get("RB_LATITUDEL"));
				double lt = Double
						.parseDouble(buildingInfo.get("LT_LATITUDEL"));
				return ((rb + lt) / 2);
			} catch (Exception e) {
				logError("解析" + buildingInfo.get("BUILDING_NAME") + "纬度失败！"
						+ e.getMessage());
			}
		}
		return (23.130808d);
	}

	/**
	 * 初始化覆盖场所
	 */
	private void initOverlay() {
		logFunction("initOverlay");
		if (buildingResList != null) {
			if (indoorMapOverlay != null) {
				indoorMapOverlay.removeAll();
			}
			indoorMapOverlay = new IndoorMapOverlay(getResources().getDrawable(
					R.drawable.icon_indoormap), baidumap_mv);
			HashMap<String, String> buildingInfo = null;
			int count = buildingResList.size();
			int i = 0;
			while (i < count) {
				buildingInfo = buildingResList.get(i);
				try {
					int log = (int) (getBuildingInfoLongitude(buildingInfo) * 1E6);
					int lan = (int) (getBuildingInfoLatitude(buildingInfo) * 1E6);
					GeoPoint gp = new GeoPoint(lan, log);
					OverlayItem oi = new OverlayItem(gp,
							buildingInfo.get("BUILD_TYPE"),
							buildingInfo.get("BUILD_TYPE"));
					Drawable drawable = getResources().getDrawable(
							R.drawable.icon_building);
					// drawable = utils.zoomDrawable(drawable, 150, 150);
					oi.setMarker(drawable);
					indoorMapOverlay.addItem(oi);
				} catch (Exception e) {
					logError("生成覆盖物错误！" + e.getMessage());
				}
				++i;
			}
			List<Overlay> overlaylist = baidumap_mv.getOverlays();
			if (overlaylist != null) {
				overlaylist.add(indoorMapOverlay);
			}
		}
	}

	/**
	 * 清除场所覆盖物
	 */
	private void clearOverlay() {
		logFunction("clearOverlay");
		if (indoorMapOverlay != null) {
			indoorMapOverlay.removeAll();
		}
		// baidumap_mv.refresh();
	}

	/**
	 * 显示百度地图更新位置，更新覆盖物
	 */
	private void showBaiduMap() {
		logFunction("showBaiduMap");
		if (!isBaiduMapInited) {
			initBaiduMap();
			isBaiduMapInited = true;
		}
		/**
		 * 如果有定位数据称动并显示当前位置
		 */
		if (!isFirstLoc) {
			LocationData locData = BDMapApp.getInstance().getBDLocationData();
			myLocationOverlay.setData(locData);
			animateToCurrentLocation();
		}
		/**
		 * 更新地图上的场所
		 */
		if (!isBaiduMapOverlayInited) {
			clearOverlay();
			initOverlay();
			isBaiduMapOverlayInited = true;
			baidumap_mv.refresh();
		}
	}

	private void gotoIndoorMap(HashMap<String, String> buildingInfo,String operType) {
		logFunction("gotoIndoorMap");
		Intent intent = new Intent(BaiduMapActivity.this, mainActivity.class);
		intent.putExtra("BUILDING_ID", buildingInfo.get("BUILDING_ID"));
		intent.putExtra("BUILDING_NAME", buildingInfo.get("BUILDING_NAME"));
		intent.putExtra("RB_LATITUDEL", buildingInfo.get("RB_LATITUDEL"));
		intent.putExtra("LT_LATITUDEL", buildingInfo.get("LT_LATITUDEL"));
		intent.putExtra("RB_LONGITUDEL", buildingInfo.get("RB_LONGITUDEL"));
		intent.putExtra("LT_LONGITUDEL", buildingInfo.get("LT_LONGITUDEL"));
		intent.putExtra("operType", operType);
		startActivity(intent);
	}

	/**
	 * 处理百度地图放到最大后的情况，暂缺
	 */
	private boolean handleMapViewMaxZoom(MapView mv) {
		logFunction("handleMapViewMaxZoom");
		// GeoPoint centerPoint = baidumap_mv.getMapCenter();
		// int tbSpan = baidumap_mv.getLatitudeSpan();
		// int lrSpan = baidumap_mv.getLongitudeSpan();
		// GeoPoint ltPoint = new GeoPoint(centerPoint.getLatitudeE6() - tbSpan
		// / 2, centerPoint.getLongitudeE6() - lrSpan / 2);
		// GeoPoint rbPoint = new GeoPoint(centerPoint.getLatitudeE6() + tbSpan
		// / 2, centerPoint.getLongitudeE6() + lrSpan / 2);
		// ArrayList<OverlayItem> oi = indoorMapOverlay.getAllItem();
		// int i = 0;
		// while (i < oi.size()) {
		// Drawable drawable = indoorMapOverlay.getItem(i).getMarker();
		// drawable = utils.zoomDrawable(drawable, 2);
		// indoorMapOverlay.getItem(i).setMarker(drawable);
		// ++i;
		// }
		// baidumap_mv.refresh();
		return (true);
	}

	/**
	 * 在场所覆盖物上点击进入相应的室内地图
	 */
	private class IndoorMapOverlay extends ItemizedOverlay<OverlayItem> {
		public IndoorMapOverlay(Drawable defaultMarker, MapView mapView) {
			super(defaultMarker, mapView);
		}

		@Override
		public boolean onTap(int id) {
			logFunction(getClass().getName() + ".onTap");
			// HashMap<String, String> buildingInfo = buildingResList.get(id);
			// gotoIndoorMap(buildingInfo.get("BUILDING_ID"),
			// buildingInfo.get("BUILDING_NAME"));
			OverlayItem item = getItem(id);
			GeoPoint gp = item.getPoint();
			popupOverlayer(gp, id);
			return (true);
		}

		@Override
		public boolean onTap(GeoPoint pt, MapView mv) {
			return (false);
		}
	}

	/**
	 * 百度地图上显示场所信息
	 * 
	 * @param gp
	 *            GeoPoint
	 * @param id
	 *            OverlayItem index
	 */
	private void popupOverlayer(GeoPoint gp, Integer id) {
		logFunction("popupOverlayer");
		MapView.LayoutParams mvParam = new MapView.LayoutParams(
				MapView.LayoutParams.WRAP_CONTENT,
				MapView.LayoutParams.WRAP_CONTENT, gp, 0, 0,
				MapView.LayoutParams.BOTTOM_CENTER);
		if (baiduOverlayerView != null) {
			baidumap_mv.removeView(baiduOverlayerView);
			baiduOverlayerView = null;
		}
		if (id != null) {
			HashMap<String, String> buildingInfo = buildingResList.get(id);
			View v = LayoutInflater.from(BaiduMapActivity.this).inflate(
					R.layout.buildingitem, null);
			v.setBackgroundResource(R.drawable.icon_popupoverlayer);
			ImageView iv_building_icon = (ImageView) v
					.findViewById(R.id.iv_building_icon);
			TextView tv_building_name = (TextView) v
					.findViewById(R.id.tv_building_name);
			TextView tv_building_distance = (TextView) v
					.findViewById(R.id.tv_building_distance);
			TextView tv_building_note = (TextView) v
					.findViewById(R.id.tv_building_note);
			LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tv_building_note
					.getLayoutParams();
			if (params == null) {
				params = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
			}
			params.width = 400;
			tv_building_note.setLayoutParams(params);
			tv_building_name.setText(buildingInfo.get("BUILDING_NAME"));
			tv_building_distance.setText(buildingInfo.get("DISTANCE"));
			tv_building_note.setText(buildingInfo.get("NOTE"));
			String BUILD_TYPE = buildingInfo.get("BUILD_TYPE");
			Integer iconResId = getBuildingIconResId(BUILD_TYPE);
			if (iconResId != null) {
				iv_building_icon.setBackgroundResource(iconResId);
			}
			v.setTag(id);
			v.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					HashMap<String, String> buildingInfo = buildingResList
							.get((Integer) v.getTag());
					gotoIndoorMap(buildingInfo,"2");
				}
			});
			baiduOverlayerView = v;
		} else {
			View v = LayoutInflater.from(BaiduMapActivity.this).inflate(
					R.layout.currentlocation, null);
			TextView tv_currentlocation = (TextView) v
					.findViewById(R.id.currentlocation);
			String LogLat = "(" + gp.getLongitudeE6() / 1e6 + ","
					+ gp.getLatitudeE6() / 1e6 + ")";
			tv_currentlocation.setText("经纬度" + LogLat);
			if (baiduOverlayerView != null) {
				baidumap_mv.removeView(baiduOverlayerView);
				baiduOverlayerView = null;
			}
			baiduOverlayerView = v;
		}
		baidumap_mv.addView(baiduOverlayerView, mvParam);
	}

	/**
	 * 点击当前位置处理情况
	 */
	private class LocationOverlay extends MyLocationOverlay {

		public LocationOverlay(MapView mapView) {
			super(mapView);
		}

		@Override
		protected boolean dispatchTap() {
			logFunction(getClass().getName() + ".dispatchTap");
			LocationData locData = BDMapApp.getInstance().getBDLocationData();
			GeoPoint gp = new GeoPoint((int) (locData.latitude * 1e6),
					(int) (locData.longitude * 1e6));
			popupOverlayer(gp, null);
			return (true);
		}
	}

	/**
	 * 百度地图移动到当前位置
	 */
	private void animateToCurrentLocation() {
		logFunction("animateToCurrentLocation");
		if (mMapController != null) {
			LocationData locData = BDMapApp.getInstance().getBDLocationData();
			GeoPoint gp = new GeoPoint((int) (locData.latitude * 1e6),
					(int) (locData.longitude * 1e6));
			mMapController.animateTo(gp);
		} else {
			logError("mMapController为空！");
		}
	}

	/**
	 * scanresultlist is ready to use
	 */
	@Override
	public void OnHandleScanResult() {
		logFunction("OnHandleScanResult");
		pd.setMessage("正在定位......");
		pd.setCancelable(false);
		pd.show();
		startThreadToGetLocation();
	}

	/**
	 * KEYCODE_BACK 处理
	 */
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		logFunction("onKeyUp");
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			/**
			 * 如果左侧菜单没有关闭，先关闭
			 */
			if (leftSliderLayout.isOpen()) {
				leftSliderLayout.close();
			} else {
				/**
				 * 弹出退出提示菜单
				 */
				utils.exitAppAlertDialog(this);
			}
			return (true);
		}
		return (super.onKeyUp(keyCode, event));
	}

	/**
	 * 点击地图、列表模式切换开关
	 */
	private void OnClickMapSwitch(View v) {
		logFunction("OnClickMapSwitch");
		Button btn = (Button) v;
		if (buildingres_lv.getVisibility() == View.VISIBLE) {
			btn.setBackgroundResource(R.drawable.icon_listmode);
			baidumap_mv.setVisibility(View.VISIBLE);
			buildingres_lv.setVisibility(View.GONE);
			showBaiduMap();
		} else {
			btn.setBackgroundResource(R.drawable.icon_locationmode);
			buildingres_lv.setVisibility(View.VISIBLE);
			baidumap_mv.setVisibility(View.GONE);
		}
	}

	/**
	 * 步数测试
	 */
	private void gotoStepTest() {
		logFunction("gotoStepTest");
		Intent intent = new Intent(BaiduMapActivity.this,
				testStepCountActivity.class);
		startActivity(intent);
	}

	/**
	 * wifi测试
	 */
	private void gotoWifiTest() {
		logFunction("gotoWifiTest");
		Intent intent = new Intent(BaiduMapActivity.this,
				testWifiActivity.class);
		startActivity(intent);
	}

	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		logFunction("onClick");
		int id = v.getId();
		switch (id) {
		case R.id.btn_listmode: {
			OnClickMapSwitch(v);
		}
			break;
		case R.id.search_btn: {
			gotoSearch();
		}
			break;
		case R.id.more_btn: {
			if (leftSliderLayout.isOpen()) {
				leftSliderLayout.close();
			} else {
				leftSliderLayout.open();
			}
		}
			break;
		case R.id.about_btn: {
			Toast.makeText(this, "关于", Toast.LENGTH_SHORT).show();
		}
			break;
		case R.id.WifiSignalTestMenu_btn: {
			gotoWifiTest();
		}
			break;
		case R.id.StepTestMenu_btn: {
			gotoStepTest();
		}
			break;
		case R.id.exit_btn: {
			utils.exitApp(BaiduMapActivity.this);
		}
			break;
		case R.id.location_btn: {
			// mMapController.setRotation(0);
			// mMapController.setOverlooking(0);
			// mMapController.setZoom(14);
			BDMapApp.getInstance().requestBDLocation();
			if (!isFirstLoc && isBaiduMapControllerInited) {
				animateToCurrentLocation();
			}
		}
			break;
		}
	}
}