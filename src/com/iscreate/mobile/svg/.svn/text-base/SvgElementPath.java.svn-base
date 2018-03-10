package com.iscreate.mobile.svg;

import java.util.List;

import android.graphics.Path;

public class SvgElementPath {
	// private void drawArc(Path p, float lastX, float lastY, float x, float y,
	// float rx, float ry, float theta, int largeArc, int sweepArc) {
	// // todo - not implemented yet, may be very hard to do using Android
	// // drawing facilities.
	// // rx, 弧的半长轴长度
	// // ry 弧的半短轴长度
	// // x-axis-rotation 是此段弧所在的x轴与水平方向的夹角，即x轴的逆时针旋转角度，负数代表顺时针转动的角度。
	// // large-arc-flag 为1 表示大角度弧线，0 代表小角度弧线。
	// // sweep-flag 为1代表从起点到终点弧线绕中心顺时针方向，0 代表逆时针方向。
	// // x,y 是弧终端坐标。
	// }

	/**
	 * Parses a single SVG path and returns it as a
	 * <code>android.graphics.Path</code> object. An example path is
	 * <code>M250,150L150,350L350,350Z</code>, which draws a triangle.
	 * 
	 * @param pathString
	 *            the SVG path, see the specification <a
	 *            href="http://www.w3.org/TR/SVG/paths.html">here</a>.
	 */
	/**
	 * This is where the hard-to-parse paths are handled. Uppercase rules are
	 * absolute positions, lowercase are relative. Types of path rules:
	 * <p/>
	 * <ol>
	 * <li>M/m - (x y)+ - Move to (without drawing)
	 * <li>Z/z - (no params) - Close path (back to starting point)
	 * <li>L/l - (x y)+ - Line to
	 * <li>H/h - x+ - Horizontal ine to
	 * <li>V/v - y+ - Vertical line to
	 * <li>C/c - (x1 y1 x2 y2 x y)+ - Cubic bezier to
	 * <li>S/s - (x2 y2 x y)+ - Smooth cubic bezier to (shorthand that assumes
	 * the x2, y2 from previous C/S is the x1, y1 of this bezier)
	 * <li>Q/q - (x1 y1 x y)+ - Quadratic bezier to
	 * <li>T/t - (x y)+ - Smooth quadratic bezier to (assumes previous control
	 * point is "reflection" of last one w.r.t. to current point)
	 * </ol>
	 * <p/>
	 * Numbers are separate by whitespace, comma or nothing at all (!) if they
	 * are self-delimiting, (ie. begin with a - sign)
	 * 
	 * @param d
	 *            the path string from the XML
	 */
	public static Path getPath1(String d) {
		int n = d.length();
		ParserHelper ph = new ParserHelper(d, 0);
		ph.skipWhitespace();
		Path p = new Path();
		float lastX = 0;
		float lastY = 0;
		float lastX1 = 0;
		float lastY1 = 0;
		// float subPathStartX = 0;
		// float subPathStartY = 0;
		char prevCmd = 0;
		while (ph.pos < n) {
			char cmd = d.charAt(ph.pos);
			switch (cmd) {
			case '-':
			case '+':
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				if (prevCmd == 'm' || prevCmd == 'M') {
					cmd = (char) (((int) prevCmd) - 1);
					break;
				} else if (prevCmd == 'c' || prevCmd == 'C') {
					cmd = prevCmd;
					break;
				} else if (prevCmd == 'l' || prevCmd == 'L') {
					cmd = prevCmd;
					break;
				}
			default: {
				ph.advance();
				prevCmd = cmd;
			}
			}

			boolean wasCurve = false;
			switch (cmd) {
			case 'M':
			case 'm': {
				float x = SVGConfig.getScaleValue(ph.nextFloat());
				float y = SVGConfig.getScaleValue(ph.nextFloat());
				if (cmd == 'm') {
					// subPathStartX += x;
					// subPathStartY += y;
					p.rMoveTo(x, y);
					lastX += x;
					lastY += y;
				} else {
					// subPathStartX = x;
					// subPathStartY = y;
					p.moveTo(x, y);
					lastX = x;
					lastY = y;
				}
				break;
			}
			case 'Z':
			case 'z': {
				p.close();
				// p.moveTo(lastX, lastY);// 这句应该是错误的，不应该返回上一坐标
				// lastX = subPathStartX;
				// lastY = subPathStartY;
				lastX1 = lastX;
				lastY1 = lastY;
				wasCurve = true;
				break;
			}
			case 'L':
			case 'l': {
				float x = SVGConfig.getScaleValue(ph.nextFloat());
				float y = SVGConfig.getScaleValue(ph.nextFloat());
				if (cmd == 'l') {
					p.rLineTo(x, y);
					lastX += x;
					lastY += y;
				} else {
					p.lineTo(x, y);
					lastX = x;
					lastY = y;
				}
				break;
			}
			case 'H':
			case 'h': {
				float x = SVGConfig.getScaleValue(ph.nextFloat());
				if (cmd == 'h') {
					p.rLineTo(x, 0);
					lastX += x;
				} else {
					p.lineTo(x, lastY);
					lastX = x;
				}
				break;
			}
			case 'V':
			case 'v': {
				float y = SVGConfig.getScaleValue(ph.nextFloat());
				if (cmd == 'v') {
					p.rLineTo(0, y);
					lastY += y;
				} else {
					p.lineTo(lastX, y);
					lastY = y;
				}
				break;
			}
			case 'C':
			case 'c': {
				wasCurve = true;
				float x1 = SVGConfig.getScaleValue(ph.nextFloat());
				float y1 = SVGConfig.getScaleValue(ph.nextFloat());
				float x2 = SVGConfig.getScaleValue(ph.nextFloat());
				float y2 = SVGConfig.getScaleValue(ph.nextFloat());
				float x = SVGConfig.getScaleValue(ph.nextFloat());
				float y = SVGConfig.getScaleValue(ph.nextFloat());
				if (cmd == 'c') {
					x1 += lastX;
					x2 += lastX;
					x += lastX;
					y1 += lastY;
					y2 += lastY;
					y += lastY;
				}
				p.cubicTo(x1, y1, x2, y2, x, y);
				lastX1 = x2;
				lastY1 = y2;
				lastX = x;
				lastY = y;
				break;
			}
			case 'S':
			case 's': {
				wasCurve = true;
				float x2 = SVGConfig.getScaleValue(ph.nextFloat());
				float y2 = SVGConfig.getScaleValue(ph.nextFloat());
				float x = SVGConfig.getScaleValue(ph.nextFloat());
				float y = SVGConfig.getScaleValue(ph.nextFloat());
				if (cmd == 's') {
					x2 += lastX;
					x += lastX;
					y2 += lastY;
					y += lastY;
				}
				float x1 = 2 * lastX - lastX1;
				float y1 = 2 * lastY - lastY1;
				p.cubicTo(x1, y1, x2, y2, x, y);
				lastX1 = x2;
				lastY1 = y2;
				lastX = x;
				lastY = y;
				break;
			}
			case 'A':
			case 'a': {
				// float rx = SVGConfig.getScaleValue(ph.nextFloat());
				// 指所在椭圆的X半轴大小
				// float ry = SVGConfig.getScaleValue(ph.nextFloat());
				// 指所在椭圆的Y半轴大小
				// float angle = ph.nextFloat();
				// 指椭圆的X轴与水平方向顺时针方向夹角，可以想像成一个水平的椭圆绕中心点顺时针旋转XROTATION的角度。
				// float largeArc = SVGConfig.getScaleValue(ph.nextFloat());
				// 1表示大角度弧线，0为小角度弧线。
				// float sweepArc = SVGConfig.getScaleValue(ph.nextFloat());
				// 只有两个值，确定从起点至终点的方向，1为顺时针，0为逆时针
				ph.nextFloat();
				ph.nextFloat();
				ph.nextFloat();
				ph.nextFloat();
				ph.nextFloat();
				float x = SVGConfig.getScaleValue(ph.nextFloat());
				float y = SVGConfig.getScaleValue(ph.nextFloat());
				// drawArc(p, lastX, lastY, x, y, rx, ry, theta, (int) largeArc,
				// (int) sweepArc);
				// RectF rectF = new RectF(rx, ry, x, y);
				// p.arcTo(rectF, largeArc, sweepArc, false);
				lastX = x;
				lastY = y;
				break;
			}
			}
			if (!wasCurve) {
				lastX1 = lastX;
				lastY1 = lastY;
			}
			ph.skipWhitespace();
		}
		return (p);
	}

	public static Path getPath(String d) {
		return (getPath(d, SVGConfig.getScale()));
	}

	public static Path getPath(String d, float scale) {
		List<SvgElementPathD> SvgElementPathDList = new SvgElementPathDParser(d)
				.parse();
		if (SvgElementPathDList != null) {
			Path path = new Path();
			Float x = null;
			Float y = null;
			float last_x = 0;
			float last_y = 0;
			float last_m_x = 0;
			float last_m_y = 0;
			SvgElementPathD current_d = null;
			boolean isUpper = false;
			int current_dvlen = 0;
			int i = 0;
			int count = SvgElementPathDList.size();
			while (i < count) {
				current_d = SvgElementPathDList.get(i);
				if (current_d != null) {
					x = null;
					y = null;
					isUpper = (current_d.d >= 'A') && (current_d.d <= 'Z');
					current_dvlen = (current_d.v == null) ? 0 : (current_d.v
							.size());
					switch (current_d.d) {
					case SvgElementPathD.D_M:
					case SvgElementPathD.D_m: {
						if (current_dvlen > 1) {
							x = scale * current_d.v.get(0);
							y = scale * current_d.v.get(1);
							if (current_d.d == SvgElementPathD.D_m) {
								path.rMoveTo(x, y);
							} else {
								path.moveTo(x, y);
							}
						}
					}
						break;
					case SvgElementPathD.D_L:
					case SvgElementPathD.D_l: {
						if (current_dvlen > 1) {
							x = scale * current_d.v.get(0);
							y = scale * current_d.v.get(1);
							if (current_d.d == SvgElementPathD.D_l) {
								path.rLineTo(x, y);
							} else {
								path.lineTo(x, y);
							}
						}
					}
						break;
					case SvgElementPathD.D_H:
					case SvgElementPathD.D_h: {
						if (current_dvlen > 0) {
							x = scale * current_d.v.get(0);
							if (current_d.d == SvgElementPathD.D_h) {
								path.rLineTo(x, last_y);
							} else {
								path.lineTo(x, last_y);
							}
						}
					}
						break;
					case SvgElementPathD.D_V:
					case SvgElementPathD.D_v: {
						if (current_dvlen > 0) {
							y = scale * current_d.v.get(0);
							if (current_d.d == SvgElementPathD.D_v) {
								path.rLineTo(last_x, y);
							} else {
								path.lineTo(last_x, y);
							}
						}
					}
						break;
					case SvgElementPathD.D_C:
					case SvgElementPathD.D_c: {
						if (current_dvlen > 5) {
							float x1 = scale * current_d.v.get(0);
							float y1 = scale * current_d.v.get(1);
							float x2 = scale * current_d.v.get(2);
							float y2 = scale * current_d.v.get(3);
							x = scale * current_d.v.get(4);
							y = scale * current_d.v.get(5);
							if (current_d.d == SvgElementPathD.D_c) {
								path.rCubicTo(x1, y1, x2, y2, x, y);
							} else {
								path.cubicTo(x1, y1, x2, y2, x, y);
							}
						}
					}
						break;
					case SvgElementPathD.D_S:
					case SvgElementPathD.D_s: {
						if (current_dvlen > 3) {
							float x2 = scale * current_d.v.get(0);
							float y2 = scale * current_d.v.get(1);
							x = scale * current_d.v.get(2);
							y = scale * current_d.v.get(3);
							if (current_d.d == SvgElementPathD.D_s) {
								path.rCubicTo(0, 0, x2, y2, x, y);
							} else {
								path.cubicTo(last_x, last_y, x2, y2, x, y);
							}
						}
					}
						break;
					case SvgElementPathD.D_Q:
					case SvgElementPathD.D_q: {
						if (current_dvlen > 3) {
							float x1 = scale * current_d.v.get(0);
							float y1 = scale * current_d.v.get(1);
							x = scale * current_d.v.get(2);
							y = scale * current_d.v.get(3);
							if (current_d.d == SvgElementPathD.D_q) {
								path.rQuadTo(x1, y1, x, y);
							} else {
								path.quadTo(x1, y1, x, y);
							}
						}
					}
						break;
					case SvgElementPathD.D_T:
					case SvgElementPathD.D_t: {
						if (current_dvlen > 1) {
							x = scale * current_d.v.get(0);
							y = scale * current_d.v.get(1);
							if (current_d.d == SvgElementPathD.D_t) {
								path.rQuadTo(0, 0, x, y);
							} else {
								path.quadTo(last_x, last_y, x, y);
							}
						}
					}
						break;
					case SvgElementPathD.D_A://
					case SvgElementPathD.D_a: {
						if (current_dvlen > 6) {
							x = scale * current_d.v.get(5);
							y = scale * current_d.v.get(6);
							if (current_d.d == SvgElementPathD.D_a) {
							}
						}
					}
						break;
					case SvgElementPathD.D_Z:
					case SvgElementPathD.D_z: {
						path.close();
						path.moveTo(last_m_x, last_m_y);
						last_x = last_m_x;
						last_y = last_m_y;
					}
						break;
					}
					if (x != null) {
						last_x = isUpper ? (x) : (last_x + x);
					}
					if (y != null) {
						last_y = isUpper ? (y) : (last_y + y);
					}
					if ((current_d.d == SvgElementPathD.D_M)
							|| (current_d.d == SvgElementPathD.D_m)) {
						last_m_x = last_x;
						last_m_y = last_y;
					}
				}
				++i;
			}
			return (path);
		}
		return (null);
	}
}
