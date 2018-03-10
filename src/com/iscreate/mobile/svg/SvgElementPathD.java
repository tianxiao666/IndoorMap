package com.iscreate.mobile.svg;

import java.util.ArrayList;
import java.util.List;

public class SvgElementPathD {
	public static final char D_M = 'M';
	public static final char D_m = 'm';
	public static final char D_L = 'L';
	public static final char D_l = 'l';
	public static final char D_H = 'H';
	public static final char D_h = 'h';
	public static final char D_V = 'V';
	public static final char D_v = 'v';
	public static final char D_C = 'C';
	public static final char D_c = 'c';
	public static final char D_S = 'S';
	public static final char D_s = 's';
	public static final char D_Q = 'Q';
	public static final char D_q = 'q';
	public static final char D_T = 'T';
	public static final char D_t = 't';
	public static final char D_A = 'A';
	public static final char D_a = 'a';
	public static final char D_Z = 'Z';
	public static final char D_z = 'z';

	public static List<Character> SvgElementPathDList = null;

	public static boolean isD(Character ch) {
		if (ch != null) {
			if (SvgElementPathDList == null) {
				SvgElementPathDList = new ArrayList<Character>();
				SvgElementPathDList.add(D_M);
				SvgElementPathDList.add(D_L);
				SvgElementPathDList.add(D_H);
				SvgElementPathDList.add(D_V);
				SvgElementPathDList.add(D_C);
				SvgElementPathDList.add(D_S);
				SvgElementPathDList.add(D_Q);
				SvgElementPathDList.add(D_T);
				SvgElementPathDList.add(D_A);
				SvgElementPathDList.add(D_Z);
				SvgElementPathDList.add(D_m);
				SvgElementPathDList.add(D_l);
				SvgElementPathDList.add(D_h);
				SvgElementPathDList.add(D_v);
				SvgElementPathDList.add(D_c);
				SvgElementPathDList.add(D_s);
				SvgElementPathDList.add(D_q);
				SvgElementPathDList.add(D_t);
				SvgElementPathDList.add(D_a);
				SvgElementPathDList.add(D_z);
			}
			return (SvgElementPathDList.contains(ch));
		}
		return (false);
	}

	public Character d = null;
	public List<Float> v = null;
}