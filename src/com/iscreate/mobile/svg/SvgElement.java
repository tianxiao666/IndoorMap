package com.iscreate.mobile.svg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Shader;
import android.util.Log;

import com.iscreate.mobile.utils.utils;
import com.iscreate.mobile.widget.Point2D;

public class SvgElement {
	/**
	 * 不明
	 */
	public final static float pathNum = 1.81f;
	public final static float RectPathNum = 4f - pathNum;
	/**
	 * SVG 元素ID
	 */
	public final static int TYPE_ID_none = -1;
	public final static int TYPE_ID_rect = 0;
	public final static int TYPE_ID_circle = 1;
	public final static int TYPE_ID_ellipse = 2;
	public final static int TYPE_ID_line = 3;
	public final static int TYPE_ID_polygon = 4;
	public final static int TYPE_ID_polyline = 5;
	public final static int TYPE_ID_path = 6;
	public final static int TYPE_ID_text = 7;
	public final static int TYPE_ID_image = 8;
	/**
	 * SVG 元素
	 */
	public final static String TYPE_rect = "rect";
	public final static String TYPE_circle = "circle";
	public final static String TYPE_ellipse = "ellipse";
	public final static String TYPE_line = "line";
	public final static String TYPE_polygon = "polygon";
	public final static String TYPE_polyline = "polyline";
	public final static String TYPE_path = "path";
	public final static String TYPE_text = "text";
	public final static String TYPE_image = "image";
	/**
	 * 保存此map
	 */
	private static HashMap<String, Integer> typeIdMap = null;

	public static int getTypeId(String type) {
		if (type != null) {
			if (typeIdMap == null) {
				typeIdMap = new HashMap<String, Integer>();
				typeIdMap.put(TYPE_rect, TYPE_ID_rect);
				typeIdMap.put(TYPE_circle, TYPE_ID_circle);
				typeIdMap.put(TYPE_ellipse, TYPE_ID_ellipse);
				typeIdMap.put(TYPE_line, TYPE_ID_line);
				typeIdMap.put(TYPE_polygon, TYPE_ID_polygon);
				typeIdMap.put(TYPE_polyline, TYPE_ID_polyline);
				typeIdMap.put(TYPE_path, TYPE_ID_path);
				typeIdMap.put(TYPE_text, TYPE_ID_text);
				typeIdMap.put(TYPE_image, TYPE_ID_image);
			}
			Integer svgElemId = typeIdMap.get(type.toLowerCase(Locale.US));
			if (svgElemId != null) {
				return (svgElemId);
			}
		}
		return (TYPE_ID_none);
	}

	private static SVGUtil svgutil = new SVGUtil();
	/**
	 * 当前SVG元素ID
	 */
	private int type = TYPE_ID_none;
	/**
	 * 当前SVG元素的属性
	 */
	private HashMap<String, String> attr = null;
	/**
	 * 当前SVG元素的值，只有text才有，一般为空
	 */
	private String value = null;

	private Integer searchColor = null;
	private Integer replaceColor = null;
	private boolean whiteMode = false;
	private boolean highlighted = false;
	private HashMap<String, Shader> shaderMap = null;
	private Path path = null;
	private Region region = null;
	private Paint paint_fill = null;
	private Paint paint_storke = null;
	private Paint paint_fill_highlight = null;
	private Paint paint_storke_highlight = null;
	/**
	 * 是否显示当前元素
	 */
	private boolean display = true;
	/**
	 * 透明度
	 */
	private int alpha = 0xff;
	/**
	 * 不透明度，>=0且<=1
	 */
	private float fill_opacity = 1;
	private float stroke_opacity = 1;

	public SvgElement(int id) {
		this.type = id;
		paint_fill = new Paint();
		paint_fill.setAntiAlias(true);
		// paint_fill.setAlpha(alpha);
		paint_storke = new Paint();
		paint_storke.setAntiAlias(true);
		// paint_storke.setAlpha(alpha);
		paint_fill_highlight = new Paint();
		paint_fill_highlight.setAntiAlias(true);
		// paint_fill_highlight.setAlpha(alpha);
		paint_storke_highlight = new Paint();
		paint_storke_highlight.setAntiAlias(true);
		// paint_storke_highlight.setAlpha(alpha);
	}

	public int getTypeId() {
		return (type);
	}

	/**
	 * 设置value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	private void initPaint() {
		if (attr != null) {
			Properties props = new Properties(attr);
			if ((paint_fill != null)
					&& svgutil.doFill(paint_fill, props, shaderMap, whiteMode,
							searchColor, replaceColor, false)) {
			} else {
				paint_fill = null;
			}
			if ((paint_storke != null)
					&& svgutil.doStroke(paint_storke, props, whiteMode,
							searchColor, replaceColor, false)) {
			} else {
				paint_storke = null;
			}
			if ((paint_fill_highlight != null)
					&& svgutil.doFill(paint_fill_highlight, props, shaderMap,
							whiteMode, searchColor, replaceColor, true)) {
			} else {
				paint_fill_highlight = null;
			}
			if ((paint_storke_highlight != null)
					&& svgutil.doStroke(paint_storke_highlight, props,
							whiteMode, searchColor, replaceColor, true)) {
			} else {
				paint_storke_highlight = null;
			}
		}
	}

	/**
	 * 设置value
	 */
	public void setAttr(HashMap<String, String> attr) {
		this.attr = attr;
		if (attr != null) {
			Properties props = new Properties(attr);
			if ("none".equals(props.getString("display"))) {
				display = false;
			} else {
				display = true;
			}
			Point2D opacity = svgutil.getOpacity(props);
			this.stroke_opacity = (float) opacity.x;
			this.fill_opacity = (float) opacity.y;
		}
		initPaint();
	}

	public HashMap<String, String> getAttr() {
		return (attr);
	}

	public void setColorSwap(Integer searchColor, Integer replaceColor) {
		this.searchColor = searchColor;
		this.replaceColor = replaceColor;
		initPaint();
	}

	public void setWhiteMode(boolean whiteMode) {
		this.whiteMode = whiteMode;
		initPaint();
	}

	public void setHighlight(boolean b) {
		this.highlighted = b;
	}

	public void setShaderMap(HashMap<String, Shader> shaderMap) {
		this.shaderMap = shaderMap;
	}

	/**
	 * 设置透明度
	 */
	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}

	public void convertToPath() {
		if (attr != null) {
			int svgElemId = type;
			HashMap<String, String> svgElemAttr = attr;
			switch (svgElemId) {
			case SvgElement.TYPE_ID_rect: {
				Float x = svgutil.getFloatAttr("x", svgElemAttr, 0f);
				Float y = svgutil.getFloatAttr("y", svgElemAttr, 0f);
				Float w = svgutil.getFloatAttr("width", svgElemAttr, 0f);
				Float h = svgutil.getFloatAttr("height", svgElemAttr, 0f);
				Float rx = SVGConfig.getScaleValue(svgutil.getFloatAttr("rx",
						svgElemAttr, null));
				Float ry = SVGConfig.getScaleValue(svgutil.getFloatAttr("ry",
						svgElemAttr, null));
				String d = null;
				if ((rx != null) && (ry != null)) {
					if ((x + w - rx) < (x + rx)) {
						rx = w / 2;
					}
					if ((y + h - ry) < (y + ry)) {
						ry = h / 2;
					}
					d = "M"
							+ x
							+ ","
							+ (y + ry)
							+ //
							"C" + x + ","
							+ (y + ry / RectPathNum)
							+ " "
							+ (x + rx / RectPathNum)
							+ ","
							+ y
							+ " "
							+ (x + rx)
							+ ","
							+ y
							+ //
							"L"
							+ (x + w - rx)
							+ ","
							+ y
							+ //
							"C" + (x + w - rx / RectPathNum) + "," + y + " "
							+ (x + w) + "," + (y + ry / RectPathNum)
							+ " "
							+ (x + w)
							+ ","
							+ (y + ry)
							+ //
							"L"
							+ (x + w)
							+ ","
							+ (y + h - ry)
							+ //
							"C" + (x + w) + "," + (y + h - ry / RectPathNum)
							+ " " + (x + w - rx / RectPathNum) + "," + (y + h)
							+ " " + (x + w - rx) + ","
							+ (y + h)
							+ //
							"L" + (x + rx) + ","
							+ (y + h)
							+ //
							"C" + (x + rx / RectPathNum) + "," + (y + h) + " "
							+ x + "," + (y + h - ry / RectPathNum) + " " + x
							+ "," + (y + h - ry) + //
							"L" + x + "," + (y + ry) + //
							"Z";
				} else {
					d = "M" + x + "," + y + //
							"L" + (x + w) + "," + y + //
							"L" + (x + w) + "," + (y + h) + //
							"L" + x + "," + (y + h) + //
							"L" + x + "," + y + //
							"Z";
				}
				svgElemAttr.put("d", d);
				svgElemAttr.remove("x");
				svgElemAttr.remove("y");
				svgElemAttr.remove("width");
				svgElemAttr.remove("height");
				svgElemAttr.remove("rx");
				svgElemAttr.remove("ry");
				type = SvgElement.TYPE_ID_path;
			}
				break;
			case SvgElement.TYPE_ID_line: {
				Float x1 = svgutil.getFloatAttr("x1", svgElemAttr);
				Float x2 = svgutil.getFloatAttr("x2", svgElemAttr);
				Float y1 = svgutil.getFloatAttr("y1", svgElemAttr);
				Float y2 = svgutil.getFloatAttr("y2", svgElemAttr);
				String d = "M" + x1 + "," + y1 + " L" + x2 + "," + y2;
				svgElemAttr.put("d", d);
				svgElemAttr.remove("x1");
				svgElemAttr.remove("x2");
				svgElemAttr.remove("y1");
				svgElemAttr.remove("y1");
				type = SvgElement.TYPE_ID_path;
			}
				break;
			case SvgElement.TYPE_ID_circle: {
				Float cx = svgutil.getFloatAttr("cx", svgElemAttr);
				Float cy = svgutil.getFloatAttr("cy", svgElemAttr);
				Float rx = svgutil.getFloatAttr("r", svgElemAttr);
				Float ry = rx;
				String d = "M"
						+ (cx - rx)
						+ ","
						+ (cy)
						+ //
						"C" + (cx - rx) + "," + (cy - ry / pathNum) + " "
						+ (cx - rx / pathNum)
						+ ","
						+ (cy - ry)
						+ " "
						+ (cx)
						+ ","
						+ (cy - ry)
						+ //
						"C" + (cx + rx / pathNum) + "," + (cy - ry) + " "
						+ (cx + rx) + "," + (cy - ry / pathNum)
						+ " "
						+ (cx + rx)
						+ ","
						+ (cy)
						+ //
						"C" + (cx + rx) + "," + (cy + ry / pathNum) + " "
						+ (cx + rx / pathNum) + "," + (cy + ry) + " " + (cx)
						+ ","
						+ (cy + ry)
						+ //
						"C" + (cx - rx / pathNum) + "," + (cy + ry) + " "
						+ (cx - rx) + "," + (cy + ry / pathNum) + " "
						+ (cx - rx) + "," + (cy) + //
						"Z";
				svgElemAttr.put("d", d);
				svgElemAttr.remove("cx");
				svgElemAttr.remove("cy");
				svgElemAttr.remove("r");
				type = SvgElement.TYPE_ID_path;
			}
				break;
			case SvgElement.TYPE_ID_ellipse: {
				Float cx = svgutil.getFloatAttr("cx", svgElemAttr);
				Float cy = svgutil.getFloatAttr("cy", svgElemAttr);
				Float rx = svgutil.getFloatAttr("rx", svgElemAttr);
				Float ry = svgutil.getFloatAttr("ry", svgElemAttr);
				String d = "M"
						+ (cx - rx)
						+ ","
						+ (cy)
						+ //
						"C" + (cx - rx) + "," + (cy - ry / pathNum) + " "
						+ (cx - rx / pathNum)
						+ ","
						+ (cy - ry)
						+ " "
						+ (cx)
						+ ","
						+ (cy - ry)
						+ //
						"C" + (cx + rx / pathNum) + "," + (cy - ry) + " "
						+ (cx + rx) + "," + (cy - ry / pathNum)
						+ " "
						+ (cx + rx)
						+ ","
						+ (cy)
						+ //
						"C" + (cx + rx) + "," + (cy + ry / pathNum) + " "
						+ (cx + rx / pathNum) + "," + (cy + ry) + " " + (cx)
						+ ","
						+ (cy + ry)
						+ //
						"C" + (cx - rx / pathNum) + "," + (cy + ry) + " "
						+ (cx - rx) + "," + (cy + ry / pathNum) + " "
						+ (cx - rx) + "," + (cy) + //
						"Z";
				svgElemAttr.put("d", d);
				svgElemAttr.remove("cx");
				svgElemAttr.remove("cy");
				svgElemAttr.remove("rx");
				svgElemAttr.remove("ry");
				type = SvgElement.TYPE_ID_path;
			}
				break;
			case SvgElement.TYPE_ID_polygon:
			case SvgElement.TYPE_ID_polyline: {
				ArrayList<Float> numbers = svgutil.getNumberParseAttr("points",
						svgElemAttr);
				svgElemAttr.remove("points");
				if (numbers != null) {
					ArrayList<Float> points = numbers;
					if (points.size() > 1) {
						String d = "M" + points.get(0) + "," + points.get(1);
						for (int i = 2; i < points.size(); i += 2) {
							float x = points.get(i);
							float y = points.get(i + 1);
							d = d + " L" + x + "," + y;
						}
						if (SvgElement.TYPE_ID_polygon == svgElemId) {
							d = d + " Z";
						}
						svgElemAttr.put("d", d);
						type = SvgElement.TYPE_ID_path;
					}
				}
			}
				break;
			case SvgElement.TYPE_ID_path: {
			}
				break;
			case SvgElement.TYPE_ID_text: {
			}
				break;
			}
		} else {
			Log.e("SvgElement", "属性为空！");
		}
	}

	public Region getRegion() {
		if ((path != null) && (region == null)) {
			region = utils.getPathRegion(path);
		}
		return (region);
	}

	public void draw(Canvas canvas) {
		if (display && (attr != null)) {
			if ((highlighted && ((paint_fill_highlight != null) || (paint_storke_highlight != null)))
					|| ((!highlighted) && ((paint_fill != null) || (paint_storke != null)))) {
				int svgElemId = type;
				HashMap<String, String> svgElemAttr = attr;
				String svgElemValue = value;
				Integer saveCount = null;
				if (svgElemId != SvgElement.TYPE_ID_path) {
					saveCount = svgutil.pushTransform(canvas, svgElemAttr);
				}
				if (paint_fill_highlight != null) {
					paint_fill_highlight.setAlpha((int) (alpha * fill_opacity));
				}
				if (paint_storke_highlight != null) {
					paint_storke_highlight
							.setAlpha((int) (alpha * stroke_opacity));
				}
				if (paint_fill != null) {
					paint_fill.setAlpha((int) (alpha * fill_opacity));
				}
				if (paint_storke != null) {
					paint_storke.setAlpha((int) (alpha * stroke_opacity));
				}
				switch (svgElemId) {
				case SvgElement.TYPE_ID_rect: {
					Float x = SVGConfig.getScaleValue(svgutil.getFloatAttr("x",
							svgElemAttr));
					if (x == null) {
						x = 0f;
					}
					Float y = SVGConfig.getScaleValue(svgutil.getFloatAttr("y",
							svgElemAttr));
					if (y == null) {
						y = 0f;
					}
					Float width = SVGConfig.getScaleValue(svgutil.getFloatAttr(
							"width", svgElemAttr));
					Float height = SVGConfig.getScaleValue(svgutil
							.getFloatAttr("height", svgElemAttr));
					if (highlighted) {
						if (paint_fill_highlight != null) {
							canvas.drawRect(x, y, x + width, y + height,
									paint_fill_highlight);
						}
						if (paint_storke_highlight != null) {
							canvas.drawRect(x, y, x + width, y + height,
									paint_storke_highlight);
						}
					} else {
						if (paint_fill != null) {
							canvas.drawRect(x, y, x + width, y + height,
									paint_fill);
						}
						if (paint_storke != null) {
							canvas.drawRect(x, y, x + width, y + height,
									paint_storke);
						}
					}
				}
					break;
				case SvgElement.TYPE_ID_line: {
					Float x1 = SVGConfig.getScaleValue(svgutil.getFloatAttr(
							"x1", svgElemAttr));
					Float x2 = SVGConfig.getScaleValue(svgutil.getFloatAttr(
							"x2", svgElemAttr));
					Float y1 = SVGConfig.getScaleValue(svgutil.getFloatAttr(
							"y1", svgElemAttr));
					Float y2 = SVGConfig.getScaleValue(svgutil.getFloatAttr(
							"y2", svgElemAttr));
					if (highlighted) {
						if (paint_storke_highlight != null) {
							canvas.drawLine(x1, y1, x2, y2,
									paint_storke_highlight);
						}
					} else {
						if (paint_storke != null) {
							canvas.drawLine(x1, y1, x2, y2, paint_storke);
						}
					}
				}
					break;
				case SvgElement.TYPE_ID_circle: {
					Float centerX = SVGConfig.getScaleValue(svgutil
							.getFloatAttr("cx", svgElemAttr));
					Float centerY = SVGConfig.getScaleValue(svgutil
							.getFloatAttr("cy", svgElemAttr));
					Float radius = SVGConfig.getScaleValue(svgutil
							.getFloatAttr("r", svgElemAttr));
					if (centerX != null && centerY != null && radius != null) {
						if (highlighted) {
							if (paint_fill_highlight != null) {
								canvas.drawCircle(centerX, centerY, radius,
										paint_fill_highlight);
							}
							if (paint_storke_highlight != null) {
								canvas.drawCircle(centerX, centerY, radius,
										paint_storke_highlight);
							}
						} else {
							if (paint_fill != null) {
								canvas.drawCircle(centerX, centerY, radius,
										paint_fill);
							}
							if (paint_storke != null) {
								canvas.drawCircle(centerX, centerY, radius,
										paint_storke);
							}
						}
					}
				}
					break;
				case SvgElement.TYPE_ID_ellipse: {
					Float centerX = SVGConfig.getScaleValue(svgutil
							.getFloatAttr("cx", svgElemAttr));
					Float centerY = SVGConfig.getScaleValue(svgutil
							.getFloatAttr("cy", svgElemAttr));
					Float radiusX = SVGConfig.getScaleValue(svgutil
							.getFloatAttr("rx", svgElemAttr));
					Float radiusY = SVGConfig.getScaleValue(svgutil
							.getFloatAttr("ry", svgElemAttr));
					if (centerX != null && centerY != null && radiusX != null
							&& radiusY != null) {
						RectF rect = new RectF();
						rect.set(centerX - radiusX, centerY - radiusY, centerX
								+ radiusX, centerY + radiusY);
						if (highlighted) {
							if (paint_fill_highlight != null) {
								canvas.drawOval(rect, paint_fill_highlight);
							}
							if (paint_storke_highlight != null) {
								canvas.drawOval(rect, paint_storke_highlight);
							}
						} else {
							if (paint_fill != null) {
								canvas.drawOval(rect, paint_fill);
							}
							if (paint_storke != null) {
								canvas.drawOval(rect, paint_storke);
							}
						}
					}
				}
					break;
				case SvgElement.TYPE_ID_polygon:
				case SvgElement.TYPE_ID_polyline: {
					ArrayList<Float> numbers = svgutil.getNumberParseAttr(
							"points", svgElemAttr);
					if (numbers != null) {
						Path p = new Path();
						ArrayList<Float> points = numbers;
						if (points.size() > 1) {
							p.moveTo(SVGConfig.getScaleValue(points.get(0)),
									SVGConfig.getScaleValue(points.get(1)));
							for (int i = 2; i < points.size(); i += 2) {
								float x = SVGConfig
										.getScaleValue(points.get(i));
								float y = SVGConfig.getScaleValue(points
										.get(i + 1));
								p.lineTo(x, y);
							}
							// Don't close a polyline
							if (SvgElement.TYPE_ID_polygon == svgElemId) {
								p.close();
							}
							if (highlighted) {
								if (paint_fill_highlight != null) {
									canvas.drawPath(p, paint_fill_highlight);
								}
								if (paint_storke_highlight != null) {
									canvas.drawPath(p, paint_storke_highlight);
								}
							} else {
								if (paint_fill != null) {
									canvas.drawPath(p, paint_fill);
								}
								if (paint_storke != null) {
									canvas.drawPath(p, paint_storke);
								}
							}
						}
					}
				}
					break;
				case SvgElement.TYPE_ID_path: {
					if (path == null) {
						path = SvgElementPath.getPath(SVGUtil.getStringAttr(
								"d", svgElemAttr));
						Matrix matrix = svgutil.parseTransform(SVGUtil
								.getStringAttr("transform", attr));
						if (matrix != null) {
							path.transform(matrix);
						}
					}
					if (highlighted) {
						if (paint_fill_highlight != null) {
							canvas.drawPath(path, paint_fill_highlight);
						}
						if (paint_storke_highlight != null) {
							canvas.drawPath(path, paint_storke_highlight);
						}
					} else {
						if (paint_fill != null) {
							canvas.drawPath(path, paint_fill);
						}
						if (paint_storke != null) {
							canvas.drawPath(path, paint_storke);
						}
					}
				}
					break;
				case SvgElement.TYPE_ID_text: {
					Float text_x = SVGConfig.getScaleValue(svgutil
							.getFloatAttr("x", svgElemAttr));
					Float text_y = SVGConfig.getScaleValue(svgutil
							.getFloatAttr("y", svgElemAttr));
					Float text_s = SVGConfig.getScaleValue(svgutil
							.getFloatAttr("font-size", svgElemAttr));
					if ((text_x != null) && (text_y != null)
							&& (svgElemValue != null)
							&& (svgElemValue.length() > 0)) {
						if (text_s != null) {
							if (highlighted) {
								if (paint_fill_highlight != null) {
									paint_fill_highlight.setTextSize(text_s);
									canvas.drawText(svgElemValue, text_x,
											text_y, paint_fill_highlight);
								}
								if (paint_storke_highlight != null) {
									paint_storke_highlight.setTextSize(text_s);
									canvas.drawText(svgElemValue, text_x,
											text_y, paint_storke_highlight);
								}
							} else {
								if (paint_fill != null) {
									paint_fill.setTextSize(text_s);
									canvas.drawText(svgElemValue, text_x,
											text_y, paint_fill);
								}
								if (paint_storke != null) {
									paint_storke.setTextSize(text_s);
									canvas.drawText(svgElemValue, text_x,
											text_y, paint_storke);
								}
							}
						}
					}
				}
					break;
				}
				svgutil.popTransform(canvas, saveCount);
			} else {
				Log.e("SvgElement", "没有可用的Paint！");
			}
		} else {
			Log.e("SvgElement", "属性为空！");
		}
	}
}