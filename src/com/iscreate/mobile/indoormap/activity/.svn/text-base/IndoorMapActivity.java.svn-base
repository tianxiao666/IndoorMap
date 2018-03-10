package com.iscreate.mobile.indoormap.activity;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.Xml;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.LocationData;
import com.iscreate.mobile.baidu.BDMapApp;
import com.iscreate.mobile.indoormap.R;
import com.iscreate.mobile.indoormap.poi.ApInfo;
import com.iscreate.mobile.indoormap.poi.PoiInfo;
import com.iscreate.mobile.indoormap.widget.DoSureDialog;
import com.iscreate.mobile.indoormap.widget.PlaneGraphFrameLayout;
import com.iscreate.mobile.indoormap.widget.requestDataThread;
import com.iscreate.mobile.service.GsonService;
import com.iscreate.mobile.service.ServiceClientInterface;
import com.iscreate.mobile.svg.SVG;
import com.iscreate.mobile.svg.SVGParser;
import com.iscreate.mobile.svg.SvgLayer;
import com.iscreate.mobile.svg.SvgStruct;
import com.iscreate.mobile.utils.utils;
import com.iscreate.mobile.widget.BaseStationInfo;
import com.iscreate.mobile.widget.LeftSliderLayout;
import com.iscreate.mobile.widget.LeftSliderLayout.OnLeftSliderLayoutStateListener;
import com.iscreate.mobile.widget.PopupNormalWindow;

public class IndoorMapActivity extends wifiDetecterActivity implements
		OnClickListener {
	/**
	 * 当前Activity显示的内容
	 * 
	 * @see #setContentView(View)
	 */
	private View contentView = null;
	/**
	 * 自定义控件，用于显示平面图
	 */
	private PlaneGraphFrameLayout fl_PlaneGraph = null;
	/**
	 * 用于UI线程中接收请求数据的消息
	 */
	private final int WHAT_REQUESTDATA = 0;
	/**
	 * 用于UI线程中接收更新步数的消息
	 */
	private final int WHAT_UPDATEACC = 2;
	/**
	 * 用于UI线程中接收SVG源代码转找成平面图的消息
	 */
	private final int WHAT_SVGSRCTODRAWABLE = 3;
	/**
	 * 耗时操作，显示进度
	 */
	private ProgressDialog pd = null;
	/**
	 * 显示当前位置
	 */
	private TextView locatione_tv = null;
	/**
	 * 显示楼层列表按钮
	 */
	private Button floors_btn = null;
	/**
	 * 楼层列表数据
	 */
	private List<HashMap<String, String>> IndoormapBuildingFloorList = null;
	/**
	 * 定时器，周期性的更新步数
	 */
	private Timer timer = null;
	/**
	 * 上次定位时间
	 */
	private Long lastsubmittime = null;
	/**
	 * 场所ID
	 */
	private String BUILDING_ID = null;
	/**
	 * 场所名
	 */
	private String BUILDING_NAME = null;
	/**
	 * 左上右下经纬度
	 */
	private double RB_LATITUDEL = 0;
	private double LT_LATITUDEL = 0;
	private double RB_LONGITUDEL = 0;
	private double LT_LONGITUDEL = 0;
	/**
	 * 楼层ID
	 */
	private String FLOOR_ID = null;
	/**
	 * 平面图ID
	 */
	private String DRAW_MAP_ID = null;
	/**
	 * 平面图源代码
	 */
	private String svgsrc = null;
	/**
	 * 当前位置的x坐标
	 */
	private Float location_x = null;
	/**
	 * 当前位置的y坐标
	 */
	private Float location_y = null;
	/**
	 * 当前位置
	 */
	// private String LOCATION = null;
	/**
	 * 标题
	 */
	private TextView title_tv = null;
	/**
	 * 显示楼层列表
	 */
	private PopupNormalWindow popupWin_Floor = null;
	/**
	 * 显示图层列表
	 */
	private PopupNormalWindow popupWin_Layer = null;
	// /**
	// * 图层列表数据
	// */
	// private HashMap<String, String> LayerTypeMap = null;
	/**
	 * 是否可回到mainActivity
	 * 
	 * @see mainActivity
	 */
	private boolean GOBACKBD = false;
	/**
	 * 侧滑控件
	 */
	private LeftSliderLayout leftSliderLayout = null;
	
	private List<ScanResult> scanWifiResults = new ArrayList<ScanResult>();
	private List<ScanResult> wifiResults = new ArrayList<ScanResult>();
	public void setWifiResults(List<ScanResult> wifiResults) {
		this.wifiResults = wifiResults;
	}

	private BaseStationInfo baseStationInfo = null;
	private String operType = null;
	private Map<String, Object> paramsMap = new HashMap<String, Object>();
	private String pic_height = null;
	private String pic_width = null;
	private ProgressDialog pdDialog;
	private final int WHAT_START_SAVEDATA = 4;
	private final int WHAT_END_SAVEDATA = 5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView = LayoutInflater.from(IndoorMapActivity.this).inflate(
				R.layout.indoor, null);
		setContentView(contentView);
		initComponent();
		initComponentControl();
		onHandleIntent(getIntent());
	}

	/**
	 * 初始化控件
	 */
	private void initComponent() {
		logFunction("initComponent");
		leftSliderLayout = (LeftSliderLayout) findViewById(R.id.main_slider_layout);
		findViewById(R.id.search_btn).setOnClickListener(this);
		findViewById(R.id.location_btn).setOnClickListener(this);
		findViewById(R.id.more_btn).setOnClickListener(this);
		findViewById(R.id.about_btn).setOnClickListener(this);
		findViewById(R.id.LayerSelection_btn).setOnClickListener(this);
		findViewById(R.id.WifiSignalTestMenu_btn).setOnClickListener(this);
		findViewById(R.id.StepTestMenu_btn).setOnClickListener(this);
		findViewById(R.id.btn_setting).setOnClickListener(this);
		findViewById(R.id.exit_btn).setOnClickListener(this);
		title_tv = (TextView) findViewById(R.id.title);
		fl_PlaneGraph = (PlaneGraphFrameLayout) findViewById(R.id.fl_PlaneGraph);
		fl_PlaneGraph.setIndoorMapActivity(IndoorMapActivity.this);
		floors_btn = (Button) findViewById(R.id.floors_btn);
		locatione_tv = (TextView) findViewById(R.id.locatione_tv);
		pd = new ProgressDialog(IndoorMapActivity.this);
		pdDialog = new ProgressDialog(IndoorMapActivity.this);
	}

	/**
	 * 初始化组件控制
	 */
	private void initComponentControl() {
		logFunction("initComponentControl");
		leftSliderLayout.enableSlide(true);
		leftSliderLayout
				.setOnLeftSliderLayoutListener(new OnLeftSliderLayoutStateListener() {
					@Override
					public void OnLeftSliderLayoutStateChanged(boolean bIsOpen) {

					}

					@Override
					public boolean OnLeftSliderLayoutInterceptTouch(
							MotionEvent ev) {
						return (false);
					}
				});
		floors_btn.setOnClickListener(this);
		pd.setTitle("请稍后");
		pd.setCancelable(false);
	}

	/**
	 * 处理Intent
	 * 
	 * @param intent
	 */
	private void onHandleIntent(Intent intent) {
		logFunction("onHandleIntent");
		BUILDING_ID = intent.getStringExtra("BUILDING_ID");
		BUILDING_NAME = intent.getStringExtra("BUILDING_NAME");
		FLOOR_ID = intent.getStringExtra("FLOOR_ID");
		DRAW_MAP_ID = intent.getStringExtra("DRAW_MAP_ID");
		svgsrc = intent.getStringExtra("SVGSRC");
		operType = intent.getStringExtra("operType");
		/**
		 * 已有定位
		 */
		if (svgsrc != null) {
			try {
				location_x = Float.parseFloat(intent.getStringExtra("X"));
				location_y = Float.parseFloat(intent.getStringExtra("Y"));
			} catch (Exception e) {
				logError("定位坐标解析错误！" + e.getMessage());
				location_x = null;
				location_y = null;
			}
			/**
			 * 没有定位
			 */
		} else {
			try {
				RB_LATITUDEL = Double.parseDouble(intent
						.getStringExtra("RB_LATITUDEL"));
				LT_LATITUDEL = Double.parseDouble(intent
						.getStringExtra("LT_LATITUDEL"));
				RB_LONGITUDEL = Double.parseDouble(intent
						.getStringExtra("RB_LONGITUDEL"));
				LT_LONGITUDEL = Double.parseDouble(intent
						.getStringExtra("LT_LONGITUDEL"));
			} catch (Exception e) {
				logError("左上右下经纬度获取失败！" + e.getMessage());
			}
		}
		GOBACKBD = intent.getBooleanExtra("GOBACKBD", false);
		// LOCATION = intent.getStringExtra("LOCATION");
		title_tv.setText(BUILDING_NAME);
		if (BUILDING_ID != null) {
			floors_btn.setClickable(false);
			pd.setMessage("正在获取信息");
			pd.show();
			// LayerTypeMap = null;
			popupWin_Layer = null;
			startThreadToGetBuildingFloorList();
		}		
		fl_PlaneGraph.setOperType(operType);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		fl_PlaneGraph.clrPlaneGraph();
		onHandleIntent(intent);
		super.onNewIntent(intent);
	}

	private IndoorMapActivity getThis() {
		return (IndoorMapActivity.this);
	}

	@Override
	protected void onDestroy() {
		if (timer != null) {
			timer.cancel();
			timer.purge();
		}
		super.onDestroy();
	}

	private void initTimer() {
		if (timer == null) {
			timer = new Timer("UpdateData", false);
			TimerTask task = new TimerTask() {
				@Override
				public void run() {
					Message msg = handler.obtainMessage();
					msg.what = WHAT_UPDATEACC;
					handler.sendMessage(msg);
				}
			};
			timer.schedule(task, 0, stepDetecterByNetActivity.INTERVAL_MS);
		}
	}

	/**
	 * 新建一个Handler接受Message
	 */
	private Handler handler = new Handler(Looper.getMainLooper()) {
		@Override
		public void handleMessage(Message msg) {
			// logFunction("handler.handleMessage");
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
			case WHAT_UPDATEACC:
				/**
				 * 更新加速力计算步数
				 */
				updateStepDetecter();
				break;
			case WHAT_SVGSRCTODRAWABLE:
				if (msg.arg2 == 1) {
					/**
					 * SVG图片转换成功
					 */
					SVG svg = (SVG) msg.obj;
					SvgStruct svgstruct = svg.getSvgStruct();
					fl_PlaneGraph.setPlaneGraph(svgstruct);
					fl_PlaneGraph.setLocation(location_x, location_y);
					paramsMap.put("BUILDING_ID", BUILDING_ID);
					paramsMap.put("FLOOR_ID", FLOOR_ID);
					paramsMap.put("DRAW_MAP_ID", DRAW_MAP_ID);
					paramsMap.put("location_x", location_x);
					paramsMap.put("location_y", location_y);
					fl_PlaneGraph.setParamsMap(paramsMap);
				} else {
					fl_PlaneGraph.clrPlaneGraph();
					ErrorMessage = (String) msg.obj;
				}
				pd.dismiss();
				break;
			case WHAT_START_SAVEDATA:
				pdDialog.setMessage("正在保存数据...");
				pdDialog.show();
				break;
			case WHAT_END_SAVEDATA:
				pdDialog.dismiss();
				break;
			}
			if (ErrorMessage != null) {
				logError(ErrorMessage);
				Toast.makeText(IndoorMapActivity.this, ErrorMessage,
						Toast.LENGTH_SHORT).show();
			}
		}
	};

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
		logFunction("handleContent:"
				+ ServiceClientInterface.getServiceClientAction(actionID));
		switch (actionID) {
		case ServiceClientInterface.ID_ACTION_GetBuildingFloorList: {
			floors_btn.setClickable(true);
			HashMap<String, String> contentMap = GsonService
					.gsonGetHashMap(content);
			String BuildingFloorListStr = contentMap.get("BuildingFloorList");
			IndoormapBuildingFloorList = GsonService
					.gsonGetListHashMap(BuildingFloorListStr);
			Log.e("IndoormapBuildingFloorList", IndoormapBuildingFloorList+"");
			if (((IndoormapBuildingFloorList == null) || (IndoormapBuildingFloorList
					.size() == 0))) {
				throw (new Exception("没有楼层！"));
			} else {
				initPopupWinFloor();
				if (BUILDING_ID != null) {
					if (FLOOR_ID != null && FLOOR_ID != "" && svgsrc != null
							&& DRAW_MAP_ID != null) {
						locatione_tv.setText(getLocationText());
						pd.setMessage("正在生成图片.....");
						
						new SvgToDrawableThread().start();
					} else {
						FLOOR_ID = null;
						DRAW_MAP_ID = null;
						svgsrc = null;
						location_x = null;
						location_y = null;
						// LOCATION = null;
						// LayerTypeMap = null;
						popupWin_Layer = null;
						LocationData locData = BDMapApp.getInstance()
								.getBDLocationData();
						if (locData.latitude >= RB_LATITUDEL
								&& locData.latitude <= LT_LATITUDEL
								&& locData.longitude <= RB_LONGITUDEL
								&& locData.longitude >= LT_LONGITUDEL) {
							if (startWifiScan()) {
								pd.setMessage("正在扫描......");
								pd.show();
							} else {
								logError("启动wifi扫描失败!");
								Toast.makeText(IndoorMapActivity.this,
										"启动wifi扫描失败!", Toast.LENGTH_SHORT)
										.show();
								fl_PlaneGraph.clrLocation();
								fl_PlaneGraph.clrApList();
								location_x = null;
								location_y = null;
								locatione_tv.setText(getLocationText());
								startThreadToGetBuildingFloorMap();
							}
						} else {
							pd.setMessage("正在获取平面图信息......");
							fl_PlaneGraph.clrLocation();
							fl_PlaneGraph.clrApList();
							location_x = null;
							location_y = null;
							locatione_tv.setText(getLocationText());
							startThreadToGetBuildingFloorMap();
						}
					}
				} else {
					if (startWifiScan()) {
						pd.setMessage("正在扫描......");
					} else {
						Toast.makeText(IndoorMapActivity.this, "启动wifi扫描失败!",
								Toast.LENGTH_SHORT).show();
						pd.dismiss();
					}
				}
			}
		}
			break;
		case ServiceClientInterface.ID_ACTION_GetLocation: {
			HashMap<String, String> contentMap = GsonService
					.gsonGetHashMap(content);
			svgsrc = contentMap.get("SVGSRC");
			if (svgsrc == "") {
				svgsrc = null;
			}
			FLOOR_ID = contentMap.get("FLOOR_ID");
			DRAW_MAP_ID = contentMap.get("DRAW_MAP_ID");
			try {
				location_x = Float.parseFloat(contentMap.get("X"));
				location_y = Float.parseFloat(contentMap.get("Y"));
			} catch (Exception e) {
				logError("定位坐标解析错误！" + e.getMessage());
				location_x = null;
				location_y = null;
				fl_PlaneGraph.clrLocation();
			}
			if ((location_x == null) || (location_y == null)) {
				Toast.makeText(IndoorMapActivity.this, "定位失败！",
						Toast.LENGTH_SHORT).show();
				logError("定位失败！");
			}
			// LOCATION = contentMap.get("LOCATION");
			locatione_tv.setText(getLocationText());
			if (svgsrc != null) {
				String LayerListStr = contentMap.get("LayerList");
				if (LayerListStr != null) {
					SparseArray<String> layerstatus = null;
					try {
						HashMap<String, String> layermap = GsonService
								.gsonGetHashMap(LayerListStr);
						Iterator<String> iterator = layermap.keySet()
								.iterator();
						layerstatus = new SparseArray<String>();
						String layer = null;
						while (iterator.hasNext()) {
							layer = iterator.next();
							layerstatus.put(SvgLayer.getTypeId(layer), "");
						}
					} catch (Exception e) {
						logError("图层列表为空！");
					}
					SparseIntArray layervisibilitystatus = fl_PlaneGraph
							.getLayerVisibilityStatus();
					int layerid = 0;
					int count = layervisibilitystatus.size();
					int i = 0;
					while (i < count) {
						layerid = layervisibilitystatus.keyAt(i);
						if (((layerstatus == null) || (layerstatus.get(layerid) == null))
								&& (SvgLayer.getTypeName(layerid) != null)) {
							fl_PlaneGraph
									.setLayerVisibility(layerid, View.GONE);
						} else {
							fl_PlaneGraph.setLayerVisibility(layerid,
									View.VISIBLE);
						}
						++i;
					}
				}
				pd.setMessage("正在生成图片.....");
				new SvgToDrawableThread().start();
			} else {
				fl_PlaneGraph.setLocation(location_x, location_y);
				pd.dismiss();
			}
		}
			break;
		case ServiceClientInterface.ID_ACTION_GetBuildingFloorMap: {
			HashMap<String, String> contentMap = GsonService
					.gsonGetHashMap(content);
			String error = null;
			svgsrc = contentMap.get("SVGSRC");
			Log.e("SVGSRC", svgsrc+"");
			FLOOR_ID = contentMap.get("FLOOR_ID");
			DRAW_MAP_ID = contentMap.get("DRAW_MAP_ID");
			locatione_tv.setText(getLocationText());
			if (svgsrc == null) {
				logError("SVG源码为空！");
				error = "SVG源码为空！";
			} else {
				getPicHeightAndWidth(svgsrc);
				String LayerListStr = contentMap.get("LayerList");
				if (LayerListStr != null) {
					SparseArray<String> layerstatus = null;
					try {
						HashMap<String, String> layermap = GsonService
								.gsonGetHashMap(LayerListStr);
						Iterator<String> iterator = layermap.keySet()
								.iterator();
						layerstatus = new SparseArray<String>();
						String layer = null;
						while (iterator.hasNext()) {
							layer = iterator.next();
							layerstatus.put(SvgLayer.getTypeId(layer), "");
						}
					} catch (Exception e) {
						logError("图层列表为空！");
					}
					SparseIntArray layervisibilitystatus = fl_PlaneGraph
							.getLayerVisibilityStatus();
					int layerid = 0;
					int count = layervisibilitystatus.size();
					int i = 0;
					while (i < count) {
						layerid = layervisibilitystatus.keyAt(i);
						if (((layerstatus == null) || (layerstatus.get(layerid) == null))
								&& (SvgLayer.getTypeName(layerid) != null)) {
							fl_PlaneGraph
									.setLayerVisibility(layerid, View.GONE);
						} else {
							fl_PlaneGraph.setLayerVisibility(layerid,
									View.VISIBLE);
						}
						++i;
					}
				}
				pd.setMessage("正在生成图片.....");
				// pd.show();
				new SvgToDrawableThread().start();
			}
			if (error != null) {
				fl_PlaneGraph.clrPlaneGraph();
				pd.dismiss();
				Toast.makeText(IndoorMapActivity.this, error,
						Toast.LENGTH_SHORT).show();
			}
		}
			break;
		case ServiceClientInterface.ID_ACTION_GetApList: {
			List<ApInfo> apinfolist = ApInfo.toListObject(content);
			if ((apinfolist != null) && (fl_PlaneGraph != null)) {
				fl_PlaneGraph.setApInfoList(apinfolist);
			}
		}
			break;
		case ServiceClientInterface.ID_ACTION_GetPoiList: {
			List<PoiInfo> poiantinfolist = PoiInfo.toListObject(content);
			if ((poiantinfolist != null) && (fl_PlaneGraph != null)) {
				fl_PlaneGraph.setPoiAntList(poiantinfolist);
			}
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
		case ServiceClientInterface.ID_ACTION_GetBuildingFloorList:
			floors_btn.setClickable(true);
			// BUILDING_ID =null;
			// BUILDING_NAME =null;
			FLOOR_ID = null;
			DRAW_MAP_ID = null;
			svgsrc = null;
			location_x = null;
			location_y = null;
			// LOCATION = null;
			pd.dismiss();
			break;
		case ServiceClientInterface.ID_ACTION_GetLocation:
			locatione_tv.setText("定位错误");
			fl_PlaneGraph.clrLocation();
			location_x = null;
			location_y = null;
			pd.dismiss();
			break;
		case ServiceClientInterface.ID_ACTION_GetBuildingFloorMap:
			fl_PlaneGraph.clrPlaneGraph();
			pd.dismiss();
			break;
		case ServiceClientInterface.ID_ACTION_GetApList:
			fl_PlaneGraph.clrApList();
			break;
		case ServiceClientInterface.ID_ACTION_GetPoiList:
			fl_PlaneGraph.clrPoiAntList();
			break;
		}
	}

	/**
	 * start a thread to request data from server
	 */
	private void startThreadToRequestData(int actionID) {
		logFunction("startThreadToRequestData");
		new requestDataThread(handler,//
				WHAT_REQUESTDATA,//
				actionID, //
				getParamsForRequestData(actionID)//
		).start();
	}

	/**
	 * start a thread to get the current location
	 */
	private void startThreadToGetLocation() {
		startThreadToRequestData(ServiceClientInterface.ID_ACTION_GetLocation);
	}

	/**
	 * start a thread to get the Floors
	 */
	private void startThreadToGetBuildingFloorList() {
		startThreadToRequestData(ServiceClientInterface.ID_ACTION_GetBuildingFloorList);
	}

	/**
	 * start a thread to get the Floor map
	 */
	private void startThreadToGetBuildingFloorMap() {
		startThreadToRequestData(ServiceClientInterface.ID_ACTION_GetBuildingFloorMap);
	}

	/**
	 * start a thread to get the AP list
	 */
	private void startThreadToGetApList() {
		startThreadToRequestData(ServiceClientInterface.ID_ACTION_GetApList);
	}

	/**
	 * start a thread to get the POI list
	 */
	private void startThreadToGetPoiAntList() {
		startThreadToRequestData(ServiceClientInterface.ID_ACTION_GetPoiList);
	}

	/**
	 * start a thread to save ReckonSignal data to server
	 */
	public void startThreadToReckonSignalData(int actionID,float x,float y,String signalLevels) {
		logFunction("startThreadToSaveData");
		new requestDataThread(handler,//
				WHAT_REQUESTDATA,//
				actionID, //
				getParamsForSaveReckonSignalData(actionID,x,y,signalLevels)//
		).start();
	}
	/**
	 * get save ReckonSignal data params
	 */
	private String[] getParamsForSaveReckonSignalData(int actionID,float x,float y,String signalLevels) {
		try {
			logFunction("getParamsForPostData:"
					+ ServiceClientInterface.getServiceClientAction(actionID));
		} catch (Exception e) {
			logError("getParamsForPostData:" + e.getMessage());
		}
		//新添该采集点的集合数据
		LocationData locData = BDMapApp.getInstance().getBDLocationData();
		String[] params = {
					BUILDING_ID,
					FLOOR_ID, 
					DRAW_MAP_ID, 
					"" + locData.longitude,
					"" + locData.latitude,
					""+	x,
					""+	y,
					""+signalLevels,
					"" + getDirection()
				};
		return (params);
	}
	
	/**
	 * get the parameters for each action
	 * 
	 * @param actionID
	 *            the interface action id
	 */
	private String[] getParamsForRequestData(int actionID) {
		try {
			logFunction("getParamsForRequestData:"
					+ ServiceClientInterface.getServiceClientAction(actionID));
		} catch (Exception e) {
			logError("getParamsForRequestData:" + e.getMessage());
		}
		switch (actionID) {
			case ServiceClientInterface.ID_ACTION_GetBuildingFloorList: {
				String params[] = { BUILDING_ID };
				return (params);
			}
			case ServiceClientInterface.ID_ACTION_GetLocation: {
				long nowtime = System.currentTimeMillis();
				long timegap = 0;
				long stepsintimegap = 0;
				if (lastsubmittime != null) {
					timegap = nowtime - lastsubmittime;
					stepsintimegap = getSteps();
				}
				String WifiListJsonStr = testWifiActivity
						.getWifiListJsonStr(scanresultlist);
				if (WifiListJsonStr == null) {
					WifiListJsonStr = "";
					logError("WifiListJsonStr为空！");
				}
				LocationData locData = BDMapApp.getInstance().getBDLocationData();
				String[] params = {//
				"" + locData.longitude,//
						"" + locData.latitude,//
						WifiListJsonStr,//
						"" + stepsintimegap,//
						"" + timegap,//
						"" + getDirection(), //
						BUILDING_ID,//
						FLOOR_ID, //
						DRAW_MAP_ID //
				};
				if (lastsubmittime == null) {
					initTimer();
					lastsubmittime = System.currentTimeMillis();
				} else {
					resetStep();
					lastsubmittime = nowtime;
				}
				return (params);
			}
			case ServiceClientInterface.ID_ACTION_GetBuildingFloorMap: {
				if (FLOOR_ID == null) {
					FLOOR_ID = "";
				}
				if (BUILDING_ID == null) {
					BUILDING_ID = "";
					logError("BUILDING_ID为空！");
				}
				String params[] = { FLOOR_ID, BUILDING_ID };
				return (params);
			}
			case ServiceClientInterface.ID_ACTION_GetApList: {
				String params[] = { null, null, DRAW_MAP_ID };
				return (params);
			}
			case ServiceClientInterface.ID_ACTION_GetPoiList: {
				String params[] = { null, null, "ANTS", DRAW_MAP_ID };
				return (params);
			}
		}
		return (null);
	}

	/**
	 * 初始化楼层下拉菜单
	 */
	private void initPopupWinFloor() {
		logFunction("initPopupWinFloor");
		ListView popupView = (ListView) LayoutInflater.from(
				IndoorMapActivity.this).inflate(R.layout.floorsmenu, null);
		popupView.setAdapter(new BaseAdapter() {
			@Override
			public int getCount() {
				return ((IndoormapBuildingFloorList == null) ? 0
						: IndoormapBuildingFloorList.size());
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				HashMap<String, String> map = IndoormapBuildingFloorList
						.get(position);
				TextView tv = new TextView(IndoorMapActivity.this);
				tv.setText(map.get("FLOOR_NAME"));
				tv.setTag(map.get("FLOOR_ID"));
				tv.setBackgroundResource(R.drawable.bg_floorsmenuitem);
				tv.setGravity(Gravity.CENTER);
				tv.setTextColor(Color.WHITE);
				return (tv);
			}

			@Override
			public long getItemId(int position) {
				return (0);
			}

			@Override
			public Object getItem(int position) {
				return (null);
			}
		});
		popupWin_Floor = new PopupNormalWindow(popupView, 350,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		popupView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> groupview, View view,
					int position, long arg3) {
				String floorId = (String) view.getTag();
				if ((floorId != null) && !floorId.equals(FLOOR_ID)) {
					FLOOR_ID = floorId;
					getThis().pd.setMessage("正在获取平面图信息......");
					getThis().pd.show();
					// LayerTypeMap = null;
					popupWin_Layer = null;
					fl_PlaneGraph.clrLocation();
					fl_PlaneGraph.clrApList();
					location_x = null;
					location_y = null;
					locatione_tv.setText(getLocationText());
					startThreadToGetBuildingFloorMap();
				}
				popupWin_Floor.dismiss();
			}
		});
		popupWin_Floor.setAnimationStyle(R.style.popupttb);
	}

	/**
	 * convert svg source to PictureDrawable
	 * 
	 * @param svgsrc
	 *            the svg source string to convert
	 * @return PictureDrawable
	 * @throws Exception
	 */
	private SVG SvgToDrawable(String svgsrc) throws Exception {
		logFunction("SvgToDrawable");
		if (svgsrc != null) {
			return (SVGParser.getSVGFromString(svgsrc));
		} else {
			throw (new Exception("SVG源代码为空！"));
		}
	}

	/**
	 * 开启一个线程将svg源代码生成Drawable
	 */
	private class SvgToDrawableThread extends Thread {
		@Override
		public void run() {
			Message msg = handler.obtainMessage();
			msg.what = WHAT_SVGSRCTODRAWABLE;
			try {
				msg.obj = SvgToDrawable(svgsrc);
				msg.arg2 = 1;
			} catch (Exception e) {
				logError("SVG代码转换Drawable错误:" + e.getMessage() + "\nsvgsrc:\n"
						+ svgsrc);
				msg.obj = "SVG代码转换Drawable错误:" + e.getMessage();
				msg.arg2 = 0;
			}
			handler.sendMessage(msg);
		}
	}

	/**
	 * 进入查找
	 */
	private void gotoSearch() {
		logFunction("gotoSearch");
		Intent intent = new Intent(IndoorMapActivity.this, SearchActivity.class);
		startActivityForResult(intent, 1);
	}

	/**
	 * 在wifi扫描完后定位
	 */
	@Override
	public void OnHandleScanResult() {
		logFunction("OnHandleScanResult");
		pd.setMessage("正在提交......");
		if (DRAW_MAP_ID == null) {
			fl_PlaneGraph.clrLocation();
			fl_PlaneGraph.clrApList();
			location_x = null;
			location_y = null;
		}
		startThreadToGetLocation();
		// if (pd.isShowing()) {
		// pd.dismiss();
		// }
		// if (fl_PlaneGraph != null) {
		// HashMap<String, Float> macsignal = new HashMap<String, Float>();
		// // macsignal.put("74:25:8A:59:BD:D0", null);
		// // macsignal.put("74:25:8A:59:BD:B1", null);
		// // macsignal.put("74:25:8A:44:9D:D0", null);
		// int count = scanresultlist.size();
		// int i = 0;
		// while (i < count) {
		// int j = 0;
		// int countj = scanresultlist.get(i).size();
		// while (j < countj) {
		// ScanResult s = scanresultlist.get(i).get(j);
		// if (s != null && s.BSSID != null
		// // && macsignal.containsKey(s.BSSID.toUpperCase())
		// ) {
		// s.BSSID = s.BSSID.toUpperCase();
		// if (macsignal.get(s.BSSID) != null) {
		// macsignal.put(s.BSSID,
		// (macsignal.get(s.BSSID) + s.level) / 2);
		// } else {
		// macsignal.put(s.BSSID, (float) s.level);
		// }
		// }
		// ++j;
		// }
		// ++i;
		// }
		// // macsignal.put("74:25:8A:59:BD:D0", (float) -60);
		// // fl_PlaneGraph.location(macsignal);
		// }
		scanWifiResults = getWifisScanResult();
		baseStationInfo = getBaseStationInfo();
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			/**
			 * 如果左侧菜单没有关闭，先关闭
			 */
			if (leftSliderLayout.isOpen()) {
				leftSliderLayout.close();
			} else {
				if (fl_PlaneGraph != null && fl_PlaneGraph.clrDialog()) {
				} else {
					if (GOBACKBD) {
						if (fl_PlaneGraph != null) {
							fl_PlaneGraph.clrPlaneGraph();
						}
						Intent intent = new Intent(this, mainActivity.class);
						startActivity(intent);
					} else {
						return (super.onKeyUp(keyCode, event));
					}
				}
			}
			return (true);
		}
		return (super.onKeyUp(keyCode, event));
	}

	/**
	 * 步数测试
	 */
	private void gotoStepTest() {
		logFunction("gotoStepTest");
		Intent intent = new Intent(this, testStepCountActivity.class);
		startActivity(intent);
	}

	/**
	 * wifi测试
	 */
	private void gotoWifiTest() {
		logFunction("gotoWifiTest");
		Intent intent = new Intent(this, testWifiActivity.class);
		startActivity(intent);
	}

	private SparseIntArray getLayerResIdMap() {
		logFunction("getLayerResIdMap");
		SparseIntArray layerResIdMap = new SparseIntArray();
		layerResIdMap.put(SvgLayer.TYPE_ID_OUTWALL, R.id.cb_layer_OUT_W);
		layerResIdMap.put(SvgLayer.TYPE_ID_TOILE, R.id.cb_layer_TOILE);
		layerResIdMap.put(SvgLayer.TYPE_ID_ELEVATE, R.id.cb_layer_ELEVA);
		layerResIdMap.put(SvgLayer.TYPE_ID_STAIR, R.id.cb_layer_STAIR);
		layerResIdMap.put(SvgLayer.TYPE_ID_ROUTE, R.id.cb_layer_ROUTE);
		layerResIdMap.put(SvgLayer.TYPE_ID_BUSSINESS, R.id.cb_layer_BUSSI);
		layerResIdMap.put(SvgLayer.TYPE_ID_AP, R.id.cb_layer_AP);
		layerResIdMap.put(SvgLayer.TYPE_ID_POI, R.id.cb_layer_POI);
		layerResIdMap.put(PlaneGraphFrameLayout.LAYER_ID_IDEALSIGNAL,
				R.id.cb_layer_IdealSignal);
		layerResIdMap.put(PlaneGraphFrameLayout.LAYER_ID_RECKONSIGNAL,
				R.id.cb_layer_ReckonSignal);
		layerResIdMap.put(PlaneGraphFrameLayout.LAYER_ID_BSIDEALSIGNAL,
				R.id.cb_layer_BaseStationIdealSignal);
		layerResIdMap.put(PlaneGraphFrameLayout.LAYER_ID_BSRECKONSIGNAL,
				R.id.cb_layer_BaseStationReckonSignal);
		return (layerResIdMap);
	}

	/**
	 * 保存图层信息，哪个有选中，哪个没有选中
	 */
	private void saveLayers() {
		logFunction("saveLayers");
		SparseIntArray layerResIdMap = getLayerResIdMap();
		CompoundButton cb = null;
		View v = popupWin_Layer.getContentView();
		SparseIntArray layervisibilitystatus = fl_PlaneGraph
				.getLayerVisibilityStatus();
		int layerid = 0;
		int i = 0;
		int count = layervisibilitystatus.size();
		while (i < count) {
			layerid = layervisibilitystatus.keyAt(i);
			cb = (CompoundButton) v.findViewById(layerResIdMap.get(layerid));
			if (cb != null) {
				if (cb.getVisibility() == View.VISIBLE) {
					fl_PlaneGraph.setLayerVisibility(layerid,
							cb.isChecked() ? View.VISIBLE : View.INVISIBLE);
					if ((PlaneGraphFrameLayout.LAYER_ID_IDEALSIGNAL == layerid)
							|| (PlaneGraphFrameLayout.LAYER_ID_RECKONSIGNAL == layerid)) {
						if ((!fl_PlaneGraph.hasApList())
								&& (layervisibilitystatus.valueAt(i) == View.VISIBLE)) {
							startThreadToGetApList();
						}
					}
					if ((PlaneGraphFrameLayout.LAYER_ID_BSIDEALSIGNAL == layerid)
							|| (PlaneGraphFrameLayout.LAYER_ID_BSRECKONSIGNAL == layerid)) {
						if ((!fl_PlaneGraph.hasPoiAntList())
								&& (layervisibilitystatus.valueAt(i) == View.VISIBLE)) {
							startThreadToGetPoiAntList();
						}
					}
				} else {
					fl_PlaneGraph.setLayerVisibility(layerid, View.GONE);
				}
			}
			++i;
		}
	}

	/**
	 * 显示有的图层，没有的不显示，哪个应该选中，哪个不应该选中
	 */
	private void showLayers() {
		logFunction("showLayers");
		SparseIntArray layerResIdMap = getLayerResIdMap();
		CompoundButton cb = null;
		View v = popupWin_Layer.getContentView();
		SparseIntArray layervisibilitystatus = fl_PlaneGraph
				.getLayerVisibilityStatus();
		int layerid = 0;
		int visibility = 0;
		int i = 0;
		int count = layervisibilitystatus.size();
		while (i < count) {
			layerid = layervisibilitystatus.keyAt(i);
			cb = (CompoundButton) v.findViewById(layerResIdMap.get(layerid));
			if (cb != null) {
				visibility = layervisibilitystatus.valueAt(i);
				cb.setVisibility((visibility == View.GONE) ? View.GONE
						: View.VISIBLE);
				cb.setChecked(visibility == View.VISIBLE);
			}
			++i;
		}
		final CompoundButton cb_layer_IdealSignal = (CompoundButton) v
				.findViewById(R.id.cb_layer_IdealSignal);
		final CompoundButton cb_layer_ReckonSignal = (CompoundButton) v
				.findViewById(R.id.cb_layer_ReckonSignal);
		final CompoundButton cb_layer_BaseStationIdealSignal = (CompoundButton) v
				.findViewById(R.id.cb_layer_BaseStationIdealSignal);
		final CompoundButton cb_layer_BaseStationReckonSignal = (CompoundButton) v
				.findViewById(R.id.cb_layer_BaseStationReckonSignal);
		cb_layer_IdealSignal
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							cb_layer_ReckonSignal.setChecked(false);
							cb_layer_BaseStationIdealSignal.setChecked(false);
							cb_layer_BaseStationReckonSignal.setChecked(false);
						}
					}
				});
		cb_layer_ReckonSignal
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							cb_layer_IdealSignal.setChecked(false);
							cb_layer_BaseStationIdealSignal.setChecked(false);
							cb_layer_BaseStationReckonSignal.setChecked(false);
						}
					}
				});
		cb_layer_BaseStationIdealSignal
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							cb_layer_IdealSignal.setChecked(false);
							cb_layer_ReckonSignal.setChecked(false);
							cb_layer_BaseStationReckonSignal.setChecked(false);
						}
					}
				});
		cb_layer_BaseStationReckonSignal
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							cb_layer_IdealSignal.setChecked(false);
							cb_layer_ReckonSignal.setChecked(false);
							cb_layer_BaseStationIdealSignal.setChecked(false);
						}
					}
				});
	}

	/**
	 * 选择图层
	 */
	private void popupLayers() {
		logFunction("popupLayers");
		if (popupWin_Layer == null) {
			View popupContentView = LayoutInflater.from(this).inflate(
					R.layout.popuplayers, null);
			popupWin_Layer = new PopupNormalWindow(popupContentView,
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT);
			popupContentView.findViewById(R.id.bt_confirm).setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							saveLayers();
							if (popupWin_Layer != null) {
								popupWin_Layer.dismiss();
							} else {
								logError("popupWin_Layer为空！");
							}
						}
					});
			popupContentView.findViewById(R.id.bt_cancel).setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							if (popupWin_Layer != null) {
								popupWin_Layer.dismiss();
							} else {
								logError("popupWin_Layer为空！");
							}
						}
					});
		}
		try {
			showLayers();
		} catch (Exception e) {
			logFunction("popupLayers." + e.getMessage());
		}
		popupWin_Layer.showAtLocation(contentView, Gravity.CENTER, 0, 0);
	}

	private String getLocationText() {
		String location = getFloorName(FLOOR_ID);
		if ((location != null) && (location_x != null) && (location_y != null)) {
			location = location + "(" + location_x + "," + location_y + ")";
		}
		return (location);
	}

	/**
	 * 获取楼层名称
	 */
	private String getFloorName(String floorId) {
		if ((IndoormapBuildingFloorList != null) && (floorId != null)
				&& (floorId.length() > 0)) {
			HashMap<String, String> floorInfo = null;
			int count = IndoormapBuildingFloorList.size();
			int i = 0;
			while (i < count) {
				floorInfo = IndoormapBuildingFloorList.get(i);
				if (floorInfo != null) {
					if (floorId.equals(floorInfo.get("FLOOR_ID"))) {
						return (floorInfo.get("FLOOR_NAME"));
					}
				}
				++i;
			}
		}
		return (null);
	}

	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		logFunction("onClick");
		int id = v.getId();
		switch (id) {
		case R.id.LayerSelection_btn:
			popupLayers();
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
		case R.id.btn_setting: {
			Toast.makeText(this, "设置", Toast.LENGTH_SHORT).show();
		}
			break;
		case R.id.exit_btn: {
			utils.exitApp(this);
		}
			break;
		case R.id.location_btn: {
			if (startWifiScan()) {
				pd.setMessage("正在扫描......");
				pd.show();
			} else {
				logError("启动wifi扫描失败!");
				Toast.makeText(IndoorMapActivity.this, "启动wifi扫描失败!",
						Toast.LENGTH_SHORT).show();
			}
		}
			break;
		case R.id.floors_btn: {
			if (popupWin_Floor != null) {
				if (popupWin_Floor.isShowing()) {
					popupWin_Floor.dismiss();
				} else {
					popupWin_Floor.showAsDropDown(floors_btn, 0, 0);
				}
			} else {
				logError("popupWin_Floor为空！");
			}
		}
		}
	}
	
	/**
	 * send point data to save
	 * TASK #3800 室内数据Android端功能增强
	 */
	public void postPointSignal(final Map<String, Object> paramMap){
		final SharedPreferences sharedPreferences = getSharedPreferences("setting",Context.MODE_PRIVATE);
		boolean isNoTipCheck = sharedPreferences.getBoolean("isNoTip", false);
		Log.d("isNoTip", isNoTipCheck+"");
		if(!isNoTipCheck){
			final DoSureDialog doSureDialog = new DoSureDialog(this,"确认发送此坐标数据");
			doSureDialog.show();
			doSureDialog.setDoSureBtnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					doSureDialog.dismiss();
					if(doSureDialog.isNoTipSelect()){
						Editor editor = sharedPreferences.edit();
						editor.putBoolean("isNoTip", true);
						editor.commit();
						Toast.makeText(IndoorMapActivity.this, "对话框不再提示！", Toast.LENGTH_SHORT).show();
						getBestSignalAndSendData(paramMap);
					}else{
						getBestSignalAndSendData(paramMap);
					}
				}
			});
			doSureDialog.setDoCancelBtnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					doSureDialog.dismiss();
				}
			});
		}else {
			getBestSignalAndSendData(paramMap);
		}
	}
	
	/**
	 * 
	 * TODO:get the best signal data to send
	 * @date:2017-2-22
	 * @autor:lu.sj
	 * @return
	 */
	public void getBestSignalAndSendData(final Map<String, Object> paramMap){
		logFunction("getBestSignalAndSendData");
		if(startWifiScan()){
			logFunction("startWifiScan");
			Message msg1 = handler.obtainMessage();
			msg1.what = WHAT_START_SAVEDATA;
			handler.sendMessage(msg1);
		}else{
			Toast.makeText(IndoorMapActivity.this, "获取信号信息出错！", Toast.LENGTH_SHORT).show();
		}
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				
				Message msg2 = handler.obtainMessage();
				msg2.what = WHAT_END_SAVEDATA;
				Log.d("scanWifiResults", scanWifiResults+"");
				Log.d("baseStationInfo", baseStationInfo+"");
				int signalLevel = -200;
				String signalName = "";
				String signalType = "";
				String LONGITUDE = location_x+"";
				String LATITUDE = location_y+"";
				String []keys = {"BUILDING_ID","FLOOR_ID","DRAW_MAP_ID",
						 "LONGITUDE","LATITUDE","PLANE_X",
						 "PLANE_Y","SIGNAL","SIGNAL_TYPE","DEVICE_ID"};
				if(scanWifiResults != null && scanWifiResults.size() > 0){
					setWifiResults(scanWifiResults);
					for(ScanResult scanResult : scanWifiResults){
						if(scanResult.level > signalLevel){
							signalLevel = scanResult.level;
							signalName = scanResult.SSID;
						}
					}
					signalType = "W";
					String []params = {paramMap.get("BUILDING_ID")+"",
									   paramMap.get("FLOOR_ID")+"",
									   paramMap.get("DRAW_MAP_ID")+"",
									   LONGITUDE,
									   LATITUDE,
									   paramMap.get("x")+"",
									   paramMap.get("y")+"",
									   signalLevel+"",signalType,
									   signalName};
					try {
						String resultStr = ServiceClientInterface.getSignal(keys, params);
						Log.e("jsonData1", resultStr);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else{
					Log.e("wifiResults", wifiResults+"");
					if(wifiResults != null && wifiResults.size() > 0){
						for(ScanResult scanResult : wifiResults){
							if(scanResult.level > signalLevel){
								signalLevel = scanResult.level;
								signalName = scanResult.SSID;
							}
						}
						signalType = "W";
						String []params = {paramMap.get("BUILDING_ID")+"",
										   paramMap.get("FLOOR_ID")+"",
										   paramMap.get("DRAW_MAP_ID")+"",
										   LONGITUDE,
										   LATITUDE,
										   paramMap.get("x")+"",
										   paramMap.get("y")+"",
										   signalLevel+"",signalType,
										   signalName};
						try {
							String resultStr = ServiceClientInterface.getSignal(keys, params);
							Log.e("jsonData1", resultStr);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				if(baseStationInfo != null){
					signalLevel = baseStationInfo.BSSS;
					Log.d("CID", baseStationInfo.CID+"");
					if ((baseStationInfo.MCC != null)
							&& (baseStationInfo.MCC == 460)) {
						String MCC = "中国";
						String MNC = "";
						Log.d("MNC",baseStationInfo.MNC+"");
						if (baseStationInfo.MNC != null) {
							switch (baseStationInfo.MNC) {
							case 0:
							case 2:
							case 7:
								MNC = "移动";
								break;
							case 1:
								MNC = "联通";
								break;
							case 3:
								MNC = "电信";
								break;
							}
						}
						signalName = MCC + MNC;
					} else {
						signalName = "未知";
					}
					signalType = "B";
					String []params = {paramMap.get("BUILDING_ID")+"",
									   paramMap.get("FLOOR_ID")+"",
									   paramMap.get("DRAW_MAP_ID")+"",
									   LONGITUDE,
									   LATITUDE,
									   paramMap.get("x")+"",
									   paramMap.get("y")+"",
									   signalLevel+"",signalType,
									   signalName};
					try {
						String resultStr = ServiceClientInterface.getSignal(keys, params);
						Log.e("jsonData2", resultStr);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				handler.sendMessage(msg2);
			}
		}, 4000);
	}
	
	/**
	 * 
	 * TODO:read the width and height of regional picture from xml String
	 * @date:2017-2-24	
	 * @autor:lu.sj
	 * @param xmlStr
	 * @throws IOException
	 */
	private void getPicHeightAndWidth(String xmlStr) throws IOException{
		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(new StringReader(xmlStr));
			int eventType = parser.getEventType();
			while(eventType != parser.END_DOCUMENT){
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					
					break;
				case XmlPullParser.START_TAG:
					if(parser.getName().equalsIgnoreCase("svg")){
						int count = parser.getAttributeCount();
						for(int i = 0 ; i < count ; i++){
							String key = parser.getAttributeName(i);
							if(key.equalsIgnoreCase("width")){
								this.pic_width = parser.getAttributeValue(i);
							}else if(key.equalsIgnoreCase("height")){
								this.pic_height = parser.getAttributeValue(i);
							}
						}
					}
					break;
				case XmlPullParser.END_DOCUMENT:
					break;
				}
				eventType = parser.next();
			}
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * TODO:TASK #3800 室内数据Android端功能增强
	 * @date:2017-2-22
	 * @autor:lu.sj
	 * @param x
	 * @param y
	 */
	public void gotoViewSignalDataActivity(Map<String, Object> paramsMap){
		Log.d("gotoViewSignalDataActivity", "gotoViewSignalDataActivity");
		String BUILDING_ID = paramsMap.get("BUILDING_ID")+"";
		String FLOOR_ID = paramsMap.get("FLOOR_ID")+"";
		String DRAW_MAP_ID = paramsMap.get("DRAW_MAP_ID")+"";
		String x = paramsMap.get("x")+"";
		String y = paramsMap.get("y")+"";
		Intent intent = new Intent(IndoorMapActivity.this,ViewSignalDataActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("BUILDING_ID", BUILDING_ID);
		bundle.putString("FLOOR_ID", FLOOR_ID);
		bundle.putString("DRAW_MAP_ID", DRAW_MAP_ID);
		bundle.putString("x", x);
		bundle.putString("y", y);
		bundle.putString("width", this.pic_width);
		bundle.putString("height", this.pic_height);
		intent.putExtra("point", bundle);
		this.startActivity(intent);
	}
}