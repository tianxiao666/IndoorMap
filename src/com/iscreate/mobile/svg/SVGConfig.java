package com.iscreate.mobile.svg;

public class SVGConfig {
	/**
	 * 缩放比例
	 */
	private static float scale = 1f;

	/**
	 * 设置缩放比例
	 */
	public static void setScale(float z) {
		scale = z;
	}

	/**
	 * 获取缩放后的值
	 */
	public static Float getScaleValue(Float f) {
		if (f != null) {
			return (scale * f);
		}
		return (null);
	}

	public static float getScale() {
		return (scale);
	}
}