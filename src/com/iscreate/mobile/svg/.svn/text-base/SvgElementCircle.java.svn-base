package com.iscreate.mobile.svg;

import android.graphics.Path;

public class SvgElementCircle {
	/**
	 * 不明
	 */
	public final static float pathNum = 1.81f;

	/**
	 * 获取圆的Path
	 */
	public static Path getPath(final float cx, final float cy, final float r) {
		final float rx = r;
		final float ry = r;
		Path path = new Path();
		path.moveTo(cx - rx, cy);
		path.cubicTo(cx - rx, cy - ry / pathNum, cx - rx / pathNum, cy - ry,
				cx, cy - ry);
		path.cubicTo(cx + rx / pathNum, cy - ry, cx + rx, (cy - ry / pathNum),
				cx + rx, cy);
		path.cubicTo(cx + rx, cy + ry / pathNum, cx + rx / pathNum, cy + ry,
				cx, cy + ry);
		path.cubicTo(cx - rx / pathNum, cy + ry, cx - rx, cy + ry / pathNum, cx
				- rx, cy);
		path.close();
		return (path);
	}
}
