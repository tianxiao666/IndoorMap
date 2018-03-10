package com.iscreate.mobile.svg;

import java.util.HashMap;

public class Properties {
	StyleSet styles = null;
	HashMap<String, String> atts;

	public Properties(HashMap<String, String> atts) {
		this.atts = atts;
		String styleAttr = SVGUtil.getStringAttr("style", atts);
		if (styleAttr != null) {
			styles = new StyleSet(styleAttr);
		}
	}

	public String getAttr(String name) {
		String v = null;
		if (styles != null) {
			v = styles.getStyle(name);
		}
		if (v == null) {
			v = SVGUtil.getStringAttr(name, atts);
		}
		return v;
	}

	public String getString(String name) {
		return getAttr(name);
	}

	public Integer getHex(String name) {
		String v = getAttr(name);
		if (v == null || !v.startsWith("#")) {
			return null;
		} else {
			try {
				return Integer.parseInt(v.substring(1), 16);
			} catch (NumberFormatException nfe) {
				// todo - parse word-based color here
				return null;
			}
		}
	}

	// public Float getFloat(String name, float defaultValue) {
	// Float v = getFloat(name);
	// if (v == null) {
	// return defaultValue;
	// } else {
	// return v;
	// }
	// }

	public Float getFloat(String name) {
		String v = getAttr(name);
		if (v == null) {
			return null;
		} else {
			try {
				return Float.parseFloat(v);
			} catch (NumberFormatException nfe) {
				return null;
			}
		}
	}
}
