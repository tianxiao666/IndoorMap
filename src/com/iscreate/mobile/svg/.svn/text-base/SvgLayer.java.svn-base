package com.iscreate.mobile.svg;

import java.util.HashMap;
import java.util.Locale;

import android.graphics.Canvas;
import android.util.SparseArray;

public class SvgLayer {
	/**
	 * 图层类型id
	 */
	public final static int TYPE_ID_none = -1;
	public final static int TYPE_ID_OUTWALL = 0;
	public final static int TYPE_ID_TOILE = 1;
	public final static int TYPE_ID_ELEVATE = 2;
	public final static int TYPE_ID_STAIR = 3;
	public final static int TYPE_ID_ROUTE = 4;
	public final static int TYPE_ID_BUSSINESS = 5;
	public final static int TYPE_ID_AP = 6;
	public final static int TYPE_ID_POI = 7;
	public final static int TYPE_ID_END = TYPE_ID_POI;
	/**
	 * 图层类型
	 */
	public final static String TYPE_OUTWALL = "OUT_W";
	public final static String TYPE_TOILE = "TOILE";
	public final static String TYPE_ELEVATE = "ELEVA";
	public final static String TYPE_STAIR = "STAIR";
	public final static String TYPE_ROUTE = "ROUTE";
	public final static String TYPE_BUSSINESS = "BUSSI";
	public final static String TYPE_AP = "AP";
	public final static String TYPE_POI = "POI";
	/**
	 * 图层类型名
	 */
	public final static String TYPE_NAME_OUTWALL = "外墙";
	public final static String TYPE_NAME_TOILE = "洗手间";
	public final static String TYPE_NAME_ELEVATE = "电梯";
	public final static String TYPE_NAME_STAIR = "楼梯";
	public final static String TYPE_NAME_ROUTE = "路径";
	public final static String TYPE_NAME_BUSSINESS = "商家";
	public final static String TYPE_NAME_AP = "AP";
	public final static String TYPE_NAME_POI = "POI";

	private static HashMap<String, Integer> typeIdMap = null;
	private static SparseArray<String> typeNameSparseArray = null;

	/**
	 * 获取类型id
	 */
	public static int getTypeId(String type) {
		if (type != null) {
			if (typeIdMap == null) {
				typeIdMap = new HashMap<String, Integer>();
				typeIdMap.put(TYPE_OUTWALL, TYPE_ID_OUTWALL);
				typeIdMap.put(TYPE_TOILE, TYPE_ID_TOILE);
				typeIdMap.put(TYPE_ELEVATE, TYPE_ID_ELEVATE);
				typeIdMap.put(TYPE_STAIR, TYPE_ID_STAIR);
				typeIdMap.put(TYPE_ROUTE, TYPE_ID_ROUTE);
				typeIdMap.put(TYPE_BUSSINESS, TYPE_ID_BUSSINESS);
				typeIdMap.put(TYPE_AP, TYPE_ID_AP);
				typeIdMap.put(TYPE_POI, TYPE_ID_POI);
			}
			Integer svgElemId = typeIdMap.get(type.toUpperCase(Locale.US));
			if (svgElemId != null) {
				return (svgElemId);
			}
		}
		return (TYPE_ID_none);
	}

	/**
	 * 获取类型名
	 */
	public static String getTypeName(int id) {
		if (typeNameSparseArray == null) {
			typeNameSparseArray = new SparseArray<String>();
			typeNameSparseArray.put(TYPE_ID_OUTWALL, TYPE_NAME_OUTWALL);
			typeNameSparseArray.put(TYPE_ID_TOILE, TYPE_NAME_TOILE);
			typeNameSparseArray.put(TYPE_ID_ELEVATE, TYPE_NAME_ELEVATE);
			typeNameSparseArray.put(TYPE_ID_STAIR, TYPE_NAME_STAIR);
			typeNameSparseArray.put(TYPE_ID_ROUTE, TYPE_NAME_ROUTE);
			typeNameSparseArray.put(TYPE_ID_BUSSINESS, TYPE_NAME_BUSSINESS);
			typeNameSparseArray.put(TYPE_ID_AP, TYPE_NAME_AP);
			typeNameSparseArray.put(TYPE_ID_POI, TYPE_NAME_POI);
		}
		return (typeNameSparseArray.get(id));
	}

	private String title = null;
	private int type = TYPE_ID_none;
	private SvgElement[] svgElems = null;
	private int alpha = 0xff;

	public SvgLayer(String title, String type) {
		this.title = title;
		this.type = getTypeId(type);
	}

	public void setSvgElements(SvgElement[] svgElems) {
		this.svgElems = svgElems;
	}

	public String getSvgLayerTitle() {
		return (title);
	}

	public int getSvgLayerType() {
		return (type);
	}

	public SvgElement[] getSvgElements() {
		return (svgElems);
	}

	/**
	 * 画图层元素
	 */
	public void draw(Canvas canvas) {
		if (svgElems != null) {
			int i = 0;
			while (i < svgElems.length) {
				if (svgElems[i] != null) {
					svgElems[i].setAlpha(alpha);
					svgElems[i].draw(canvas);
				}
				++i;
			}
		}
	}

	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}
}