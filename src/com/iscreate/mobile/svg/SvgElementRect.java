package com.iscreate.mobile.svg;

import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;

public class SvgElementRect {
	/**
	 * 获取矩形的Path
	 */
	public static Path getPath(final Rect rect) {
		return (getPath(rect.left, rect.top, rect.width(), rect.height()));
	}

	/**
	 * 获取矩形的Path
	 */
	public static Path getPath(final RectF rectf) {
		return (getPath(rectf.left, rectf.top, rectf.width(), rectf.height()));
	}

	/**
	 * 获取矩形的Path
	 */
	public static Path getPath(final float x, final float y, final float w,
			final float h) {
		Path path = new Path();
		path.moveTo(x, y);
		path.lineTo(x + w, y);
		path.lineTo(x + w, y + h);
		path.lineTo(x, y + h);
		path.lineTo(x, y);
		path.close();
		return (path);
	}
}
