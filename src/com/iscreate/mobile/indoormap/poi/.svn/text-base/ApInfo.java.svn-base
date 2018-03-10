package com.iscreate.mobile.indoormap.poi;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iscreate.mobile.widget.Point2D;

public class ApInfo extends SignalDistribution {
	public String EQUT_SSID = null;
	public String MAC_BSSID = null;

	public static List<ApInfo> toListObject(String jsonstr) throws Exception {
		try {
			return (new Gson().fromJson(jsonstr, new TypeToken<List<ApInfo>>() {
			}.getType()));
		} catch (Exception e) {
			throw (new Exception("不是List<ApInfo>结构的Json字符串,Gson解析出错!"));
		}
	}

	public static List<Point2D> getCoordList(List<ApInfo> apinfolist) {
		if (apinfolist != null) {
			int i = 0;
			int count = apinfolist.size();
			List<Point2D> lst = new ArrayList<Point2D>();
			ApInfo apinfo = null;
			while (i < count) {
				apinfo = apinfolist.get(i);
				if (apinfo != null) {
					lst.add(Point2D.getPoint(apinfo.POSITION_X,
							apinfo.POSITION_Y));
				}
				++i;
			}
			return (lst);
		}
		return (null);
	}
}