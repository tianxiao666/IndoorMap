package com.iscreate.mobile.indoormap.poi;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iscreate.mobile.widget.Point2D;

public class PoiInfo extends SignalDistribution {
	public int ANT_LAC = 0;
	public int ANT_CID = 0;

	public static List<PoiInfo> toListObject(String jsonstr) throws Exception {
		try {
			return (new Gson().fromJson(jsonstr,
					new TypeToken<List<PoiInfo>>() {
					}.getType()));
		} catch (Exception e) {
			throw (new Exception("不是List<PoiInfo>结构的Json字符串,Gson解析出错!"));
		}
	}

	public static List<Point2D> getCoordList(List<PoiInfo> poiinfolist) {
		if (poiinfolist != null) {
			int i = 0;
			int count = poiinfolist.size();
			List<Point2D> lst = new ArrayList<Point2D>();
			PoiInfo poiinfo = null;
			while (i < count) {
				poiinfo = poiinfolist.get(i);
				if (poiinfo != null) {
					lst.add(Point2D.getPoint(poiinfo.POSITION_X,
							poiinfo.POSITION_Y));
				}
				++i;
			}
			return (lst);
		}
		return (null);
	}
}