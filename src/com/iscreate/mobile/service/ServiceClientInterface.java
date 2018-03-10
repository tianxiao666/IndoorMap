package com.iscreate.mobile.service;

import java.net.URLEncoder;
import java.util.HashMap;

import android.text.StaticLayout;
import android.util.Log;

import com.google.gson.Gson;
import com.iscreate.mobile.config.EnvConfig;

public class ServiceClientInterface {
	/**
	 * set true if you want to use GET request,otherwise false
	 */
	private static boolean requestMethodIsGet = true;
	/**
	 * the interface action name list
	 */
	private static final String[] ServiceClientActionArray = {
			"GetNearbyBuildingList",// 0
			"GetBuildingFloorList",// 1
			"GetBuildingFloorMap", // 2
			"getIndoormapBuildingFloorFastSearchKeyList",// 3
			"GetMatchBuildingList",// 4
			"CheckNewVersion",// 5
			"GetLocation",// 6
			"GetBuildingTypeList",// 7
			"GetFloorTypeList",// 8
			"GetApList",// 9
			"GetPoiList",// 10
			"IdealApCollection"//11 新添该采集点的集合数据
	};
	/**
	 * the interface parameter name list
	 */
	private static final String[][] ServiceClientActionParameter = {
			{ "LONGITUDE", "LATITUDE", "RANG", "INDEX_START", "INDEX_END",
					"BUILD_TYPE" },// 0
			{ "BUILDING_ID" },// 1
			{ "FLOOR_ID", "BUILDING_ID" },// 2
			{ "BuildingID", "FloorID" }, // 3
			{ "LONGITUDE", "LATITUDE", "KEYWORD", "INDEX_START", "INDEX_END" }, // 4
			{ "VersionName", "VersionCode" }, // 5
			{ "LONGITUDE", "LATITUDE", "APLIST", "MoveSteps",
					"MoveStepsMillis", "Derection", "BUILDING_ID", "FLOOR_ID",
					"DRAW_MAP_ID" }, // 6
			null,// 7
			null, // 8
			{ "SVG_ID", "AP_ID", "DRAW_MAP_ID", "FLOOR_ID", "BUILDING_ID" },// 9
			{ "SVG_ID", "POI_ID", "POI_TYPE", "DRAW_MAP_ID", "FLOOR_ID",
					"BUILDING_ID" }, // 10
			{ "BUILDING_ID", "FLOOR_ID", "DRAW_MAP_ID", "LONGITUDE", "LATITUDE",
			"PLANE_X","PLANE_Y","AP_LEVELS","Derection" }//11 新添该采集点的集合数据接口参数
	};
	/**
	 * the interface action id
	 */
	public static final int ID_ACTION_GetNearbyBuildingList = 0;
	public static final int ID_ACTION_GetBuildingFloorList = 1;
	public static final int ID_ACTION_GetBuildingFloorMap = 2;
	public static final int ID_ACTION_getIndoormapBuildingFloorFastSearchKeyList = 3;
	public static final int ID_ACTION_GetMatchBuildingList = 4;
	public static final int ID_ACTION_CheckNewVersion = 5;
	public static final int ID_ACTION_GetLocation = 6;
	public static final int ID_ACTION_GetBuildingTypeList = 7;
	public static final int ID_ACTION_GetFloorTypeList = 8;
	public static final int ID_ACTION_GetApList = 9;
	public static final int ID_ACTION_GetPoiList = 10;
	public static final int ID_ACTION_IdealApCollection = 11;//新添该采集点的集合数据

	/**
	 * get the interface action name via the action id
	 * 
	 * @param actionID
	 *            the interface action id
	 * @return the interface action name
	 * @throws Exception
	 */
	public static final String getServiceClientAction(int actionID)
			throws Exception {
		if (actionID >= ServiceClientActionArray.length) {
			throw (new Exception("请求的ActionID错误！"));
		}
		return (ServiceClientActionArray[actionID]);
	}

	/**
	 * get the interface parameter
	 * 
	 * @param actionID
	 *            the interface action id
	 * @param params
	 *            the interface parameter value list
	 * @param isUrl
	 *            if it is a paramer for url then true, otherwise false
	 * @return a string of the interface parameter
	 */
	private static final String getServiceClientActionParameter(int actionID,
			String[] params, boolean isUrl) {
		if ((params != null)
				&& (actionID < ServiceClientActionParameter.length)) {
			if (isUrl) {
				return (getUrlParameter(ServiceClientActionParameter[actionID],
						params));
			} else {
				return (getJsonStrParameter(
						ServiceClientActionParameter[actionID], params));
			}
		}
		return (null);
	}

	/**
	 * get the interface parameter of a JSON string
	 * 
	 * @param keyArray
	 *            the parameter name list
	 * @param valueArray
	 *            the parameter value list
	 * @return the interface parameter of a JSON string
	 */
	private static String getJsonStrParameter(String[] keyArray,
			String[] valueArray) {
		if ((keyArray != null) && (valueArray != null)) {
			int count = (keyArray.length < valueArray.length) ? keyArray.length
					: valueArray.length;
			if (count > 0) {
				HashMap<String, String> paramsMap = new HashMap<String, String>();
				int i = 0;
				while (i < count) {
					if (valueArray[i] != null) {
						paramsMap.put(keyArray[i], valueArray[i]);
					}
					++i;
				}
				String JsonStr = (new Gson()).toJson(paramsMap);
				return (JsonStr);
			}
		}
		return (null);
	}

	/**
	 * get the interface parameter of a URL string (example :
	 * actionID=1&actionName=getLoction)
	 * 
	 * @param keyArray
	 *            the parameter name list
	 * @param valueArray
	 *            the parameter value list
	 * @return the interface parameter of a URL string
	 */
	private static String getUrlParameter(String[] keyArray, String[] valueArray) {
		if ((keyArray != null) && (valueArray != null)) {
			int count = (keyArray.length < valueArray.length) ? keyArray.length
					: valueArray.length;
			if (count > 0) {
				String params = keyArray[0] + "=" + valueArray[0];
				int i = 1;
				while (i < count) {
					if (valueArray[i] != null) {
						params = params + "&" + keyArray[i] + "="
								+ valueArray[i];
					}
					++i;
				}
				return (params);
			}
		}
		return (null);
	}

	/**
	 * POST requset
	 * 
	 * @param actionID
	 *            the interface action id
	 * @param params
	 *            the parameter value list
	 * @return the content from the JSON response
	 * @throws Exception
	 */
	public static String post(int actionID, String[] params) throws Exception {
		String jsonArrayObj = getServiceClientActionParameter(actionID, params,
				false);
		jsonArrayObj = (jsonArrayObj == null) ? "" : URLEncoder
				.encode(jsonArrayObj);
		String requestUrl = EnvConfig.getUrl()
				+ getServiceClientAction(actionID);
		String responseJsonStr = HttpService.post(requestUrl,
				EnvConfig.getCookie(), jsonArrayObj);
		return (handleResponse(responseJsonStr));
	}

	/**
	 * 
	 * @param actionID
	 *            the interface action id
	 * @param params
	 *            the parameter value list
	 * @return the content from the JSON response
	 * @throws Exception
	 */
	public static String get(int actionID, String[] params) throws Exception {
		String jsonArrayObj = getServiceClientActionParameter(actionID, params,
				false);
		Log.d("jsonArrayObj", jsonArrayObj);
		jsonArrayObj = (jsonArrayObj == null) ? "" : URLEncoder
				.encode(jsonArrayObj,"UTF-8");
		String requestUrl = EnvConfig.getUrl()
				+ getServiceClientAction(actionID) + "?jsonArrayObj="
				+ jsonArrayObj;
		/*String requestUrl = EnvConfig.getUrl()
				+ getServiceClientAction(actionID) + "&jsonArrayObj="
				+ jsonArrayObj;*/
		Log.d("requestUrl=", requestUrl);
		String responseJsonStr = HttpService.get(requestUrl,
				EnvConfig.getCookie());
		Log.e("responseJsonStr", responseJsonStr);
		return (handleResponse(responseJsonStr));
	}

	/**
	 * handle the entity string from the response to the request,here it is a
	 * JSON string which the JSON structure is
	 * {success:0,message:error,content:the content}
	 * 
	 * @param responseJsonStr
	 *            Entity：the entity string from the response to the request.
	 * @return the content from the JSON response
	 * @throws Exception
	 */
	public static String handleResponse(String responseJsonStr)
			throws Exception {
		HashMap<String, String> responseHashMap = GsonService
				.gsonGetHashMap(responseJsonStr);
		if (responseHashMap != null) {
			if ("1".equals(responseHashMap.get("success"))) {
				String content = responseHashMap.get("content");
				if (content != null) {
					return (content);
				} else {
					throw (new Exception("数据为空！"));
				}
			} else {
				String msg = responseHashMap.get("message");
				if (msg == null) {
					msg = "错误信为空！";
				}
				throw (new Exception(msg));
			}
		} else {
			throw (new Exception("返回数剧格式错误！"));
		}
	}

	public static String handleResponseStr(String responseJsonStr) {
		HashMap<String, String> responseHashMap;
		String status = "failed";
		try {
			responseHashMap = GsonService
					.gsonGetHashMap(responseJsonStr);
			if (responseHashMap != null) {
				if ("1".equals(responseHashMap.get("success"))) {
					String content = responseHashMap.get("content");
					if (content != null) {
						return (content);
					} 
				} else {
					return status;
				}
			} else {
				return status;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return status;
	}
	
	/**
	 * 
	 * @param actionID
	 *            the interface action id
	 * @param params
	 *            the parameter value list
	 * @return the content from the JSON response
	 * @throws Exception
	 */
	public static String request(int actionID, String[] params)
			throws Exception {
		if (requestMethodIsGet) {
			return (get(actionID, params));
		} else {
			return (post(actionID, params));
		}
	}
	
	public static String postSignal(String[] keys,String[] params) throws Exception{
		if(params.length > 0){
			String jsonArrayObj=getJsonStrParameter(keys, params);
			Log.e("jsonArrayObj", jsonArrayObj);
			jsonArrayObj = (jsonArrayObj == null) ? "" :URLEncoder.encode(jsonArrayObj);
			String jsonStr = HttpService.post(EnvConfig.getOperUrl(1), EnvConfig.getCookie(), jsonArrayObj);
			Log.e("resultJson", jsonStr);
			return (handleResponse(jsonStr));
		}else{
			return null;
		}
	}
	
	public static String getSignal(String[] keys,String[] params) throws Exception{
		if(params.length > 0){
			String jsonArrayObj=getJsonStrParameter(keys, params);
			jsonArrayObj = (jsonArrayObj == null) ? "" :URLEncoder.encode(jsonArrayObj);
			/*String request = EnvConfig.getOperUrl(1) + "&jsonArrayObj="
					+ jsonArrayObj;*/
			String request = EnvConfig.getOperUrl(1) + "?jsonArrayObj="
					+ jsonArrayObj;
			String jsonStr = HttpService.get(request, EnvConfig.getCookie());
			Log.e("insertDataResult", jsonStr);
			return (handleResponseStr(jsonStr));
		}else{
			return null;
		}
	}
	
	public static String getSignalData(String []keys,String []params) throws Exception{
		if(params.length > 0){
			String jsonArrayObj = getJsonStrParameter(keys, params);
			jsonArrayObj = (jsonArrayObj == null) ? "" :URLEncoder.encode(jsonArrayObj);
//			String requstUrl = EnvConfig.getOperUrl(2) + "&jsonArrayObj=" + jsonArrayObj;
			String requstUrl = EnvConfig.getOperUrl(2) + "?jsonArrayObj=" + jsonArrayObj;
			String jsonStr = HttpService.get(requstUrl, EnvConfig.getCookie());
			Log.d("getDataResult={}", jsonStr);
			Log.e("错误日志输出={}", jsonStr);
			return (handleResponse(jsonStr));
		}else{
			return null;
		}
	}
}