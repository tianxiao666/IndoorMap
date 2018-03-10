package com.iscreate.mobile.svg;

import java.util.ArrayList;
import java.util.List;

public class SvgElementPathDParser {
	private String d = null;
	private List<SvgElementPathD> SvgElementPathDList = null;

	public SvgElementPathDParser(String d) {
		this.d = d + " ";
	}

	public List<SvgElementPathD> parse() {
		if (d != null) {
			SvgElementPathDList = new ArrayList<SvgElementPathD>();
			SvgElementPathD svgElementPathD = null;
			String v = null;
			Character ch = null;
			int dlen = d.length();
			int i = 0;
			while (i < dlen) {
				ch = d.charAt(i);
				if (SvgElementPathD.isD(ch) || Character.isWhitespace(ch)
						|| (ch == ',')) {
					if ((svgElementPathD != null) && (v != null)
							&& (v.length() > 0)) {
						try {
							svgElementPathD.v.add(Float.parseFloat(v));
						} catch (Exception e) {
						}
						v = "";
					}
					if (SvgElementPathD.isD(ch)) {
						svgElementPathD = new SvgElementPathD();
						svgElementPathD.d = ch;
						svgElementPathD.v = new ArrayList<Float>();
						v = "";
						SvgElementPathDList.add(svgElementPathD);
					}
				} else {
					if (v != null) {
						v = v + ch.toString();
					}
				}
				++i;
			}
		}
		return (SvgElementPathDList);
	}

	public List<SvgElementPathD> getSvgElementPathDList() {
		return (SvgElementPathDList);
	}
}