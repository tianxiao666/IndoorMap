package com.iscreate.mobile.svg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import org.xml.sax.Attributes;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Typeface;

import com.iscreate.mobile.widget.Point2D;

public class SVGUtil {
	public static String getStringAttr(String name,
			HashMap<String, String> attributes) {
		return ((attributes == null) ? null : attributes.get(name));
	}

	public Float getFloatAttr(String name, HashMap<String, String> attributes,
			Float defaultValue) {
		String v = getStringAttr(name, attributes);
		if (v == null) {
			return defaultValue;
		} else {
			if (v.endsWith("px")) {
				v = v.substring(0, v.length() - 2);
			}
			// Log.d(TAG, "Float parsing '" + name + "=" + v + "'");
			return Float.parseFloat(v);
		}
	}

	public Float getFloatAttr(String name, HashMap<String, String> attributes) {
		return getFloatAttr(name, attributes, null);
	}

	public ArrayList<Float> parseNumbers(String s) {
		// Util.debug("Parsing numbers from: '" + s + "'");
		ArrayList<Float> numbers = new ArrayList<Float>();
		if (s != null) {
			int n = s.length();
			int p = 0;
			boolean skipChar = false;
			for (int i = 1; i < n; i++) {
				if (skipChar) {
					skipChar = false;
					continue;
				}
				char c = s.charAt(i);
				switch (c) {
				// This ends the parsing, as we are on the next element
				case 'M':
				case 'm':
				case 'Z':
				case 'z':
				case 'L':
				case 'l':
				case 'H':
				case 'h':
				case 'V':
				case 'v':
				case 'C':
				case 'c':
				case 'S':
				case 's':
				case 'Q':
				case 'q':
				case 'T':
				case 't':
				case 'a':
				case 'A':
				case ')': {
					String str = s.substring(p, i);
					if (str.trim().length() > 0) {
						// Util.debug("  Last: " + str);
						Float f = Float.parseFloat(str);
						numbers.add(f);
					}
					p = i;
					return (numbers);
				}
				case '\n':
				case '\t':
				case ' ':
				case ',':
				case '-': {
					String str = s.substring(p, i);
					// Just keep moving if multiple whitespace
					if (str.trim().length() > 0) {
						// Util.debug("  Next: " + str);
						Float f = Float.parseFloat(str);
						numbers.add(f);
						if (c == '-') {
							p = i;
						} else {
							p = i + 1;
							skipChar = true;
						}
					} else {
						p++;
					}
					break;
				}
				}
			}
			String last = s.substring(p);
			if (last.length() > 0) {
				// Util.debug("  Last: " + last);
				try {
					numbers.add(Float.parseFloat(last));
				} catch (NumberFormatException nfe) {
					// Just white-space, forget it
				}
				p = s.length();
			}
		}
		return (numbers);
	}

	public ArrayList<Float> getNumberParseAttr(String name,
			HashMap<String, String> attributes) {
		return parseNumbers(attributes.get(name));
	}

	public Matrix parseTransform(String s) {
		if (s != null) {
			if (s.startsWith("matrix(")) {
				ArrayList<Float> numbers = parseNumbers(s.substring("matrix("
						.length()));
				if (numbers.size() == 6) {
					Matrix matrix = new Matrix();
					matrix.setValues(new float[] {
							// Row 1
							numbers.get(0), numbers.get(2),
							SVGConfig.getScaleValue(numbers.get(4)),
							// Row 2
							numbers.get(1), numbers.get(3),
							SVGConfig.getScaleValue(numbers.get(5)),
							// Row 3
							0, 0, 1 });
					return matrix;
				}
			} else if (s.startsWith("translate(")) {
				ArrayList<Float> numbers = parseNumbers(s
						.substring("translate(".length()));
				if (numbers.size() > 0) {
					float tx = SVGConfig.getScaleValue(numbers.get(0));
					float ty = 0;
					if (numbers.size() > 1) {
						ty = SVGConfig.getScaleValue(numbers.get(1));
					}
					Matrix matrix = new Matrix();
					matrix.postTranslate(tx, ty);
					return matrix;
				}
			} else if (s.startsWith("scale(")) {
				ArrayList<Float> numbers = parseNumbers(s.substring("scale("
						.length()));
				if (numbers.size() > 0) {
					float sx = numbers.get(0);
					float sy = 0;
					if (numbers.size() > 1) {
						sy = numbers.get(1);
					}
					Matrix matrix = new Matrix();
					matrix.postScale(sx, sy);
					return matrix;
				}
			} else if (s.startsWith("skewX(")) {
				ArrayList<Float> numbers = parseNumbers(s.substring("skewX("
						.length()));
				if (numbers.size() > 0) {
					float angle = numbers.get(0);
					Matrix matrix = new Matrix();
					matrix.postSkew((float) Math.tan(angle), 0);
					return matrix;
				}
			} else if (s.startsWith("skewY(")) {
				ArrayList<Float> numbers = parseNumbers(s.substring("skewY("
						.length()));
				if (numbers.size() > 0) {
					float angle = numbers.get(0);
					Matrix matrix = new Matrix();
					matrix.postSkew(0, (float) Math.tan(angle));
					return matrix;
				}
			} else if (s.startsWith("rotate(")) {
				ArrayList<Float> numbers = parseNumbers(s.substring("rotate("
						.length()));
				if (numbers.size() > 0) {
					float angle = numbers.get(0);
					float cx = 0;
					float cy = 0;
					if (numbers.size() > 2) {
						cx = SVGConfig.getScaleValue(numbers.get(1));
						cy = SVGConfig.getScaleValue(numbers.get(2));
					}
					Matrix matrix = new Matrix();
					// matrix.postTranslate(cx, cy);
					matrix.postRotate(angle, cx, cy);
					// matrix.postTranslate(-cx, -cy);
					return matrix;
				}
			}
		}
		return null;
	}

	public Integer pushTransform(Canvas canv, HashMap<String, String> atts) {
		final String transform = SVGUtil.getStringAttr("transform", atts);
		if (transform != null) {
			final Matrix matrix = parseTransform(transform);
			int saveCount = canv.save();
			canv.concat(matrix);
			return (saveCount);
		}
		return (null);
	}

	public void popTransform(Canvas canv, Integer saveCount) {
		if (saveCount != null) {
			canv.restore();
		}
	}

	public HashMap<String, String> AttributesToHashMap(Attributes attr) {
		if (attr != null) {
			HashMap<String, String> hmap = new HashMap<String, String>();
			int count = attr.getLength();
			int i = 0;
			while (i < count) {
				hmap.put(attr.getLocalName(i), attr.getValue(i));
				++i;
			}
			return (hmap);
		}
		return (null);
	}

	private boolean doFont(Paint paint, Properties atts) {
		String fontfamily = atts.getString("font-family");
		String fontstyle = atts.getString("font-style");
		String fontweight = atts.getString("font-weight");
		String textanchor = atts.getString("text-anchor");
		String fontsize = atts.getString("font-size");
		if ((fontfamily != null) || (fontstyle != null) || (fontweight != null)
				|| (textanchor != null) || (fontsize != null)) {
			Typeface tf = null;
			if (fontfamily != null) {
				HashMap<String, Typeface> tfMap = new HashMap<String, Typeface>();
				tfMap.put("serif", Typeface.SERIF);
				tfMap.put("sans-serif", Typeface.SANS_SERIF);
				tfMap.put("cursive", Typeface.SERIF);
				tfMap.put("fantasy", Typeface.SERIF);
				tfMap.put("monospace", Typeface.MONOSPACE);
				tf = tfMap.get(fontfamily.toLowerCase(Locale.US));
			}
			if (tf == null) {
				tf = Typeface.SERIF;
			}
			int font = Typeface.NORMAL;
			if (fontstyle != null) {
				if ("italic".equals(fontstyle)) {
					font = Typeface.ITALIC;
				}
			}
			if (fontweight != null) {
				if ("bold".equals(fontweight)) {
					font = (font == Typeface.ITALIC) ? Typeface.BOLD_ITALIC
							: Typeface.BOLD;
				}
			}
			if ((textanchor != null) && textanchor.equals("middle")) {
				paint.setTextAlign(Paint.Align.CENTER);
			} else {
				paint.setTextAlign(Paint.Align.LEFT);
			}
			paint.setTypeface(Typeface.create(tf, font));
			return (true);
		}
		return (false);
	}

	private void doColor(Paint paint, Integer color, boolean fillMode,
			Integer searchColor, Integer replaceColor, boolean highlighted) {
		int c = (0xFFFFFF & color) | 0xFF000000;
		if (searchColor != null && searchColor.intValue() == c) {
			c = replaceColor;
		}
		if (highlighted) {
			c = fillMode ? 0xffff00ff : 0xff00ffff;
		}
		paint.setColor(c);
	}

	public Point2D getOpacity(Properties props) {
		if (props == null) {
			return (Point2D.getPoint(1, 1));
		} else {
			Float opacity = props.getFloat("opacity");
			Float fill_opacity = props.getFloat("fill-opacity");
			Float stroke_opacity = props.getFloat("stroke-opacity");
			if (opacity == null) {
				opacity = 1f;
			} else {
				if (opacity > 1) {
					opacity = 1f;
				} else {
					if (opacity < 0) {
						opacity = 0f;
					}
				}
			}
			if (stroke_opacity == null) {
				stroke_opacity = 1f;
			} else {
				if (stroke_opacity > 1) {
					stroke_opacity = 1f;
				} else {
					if (stroke_opacity < 0) {
						stroke_opacity = 0f;
					}
				}
			}
			if (fill_opacity == null) {
				fill_opacity = 1f;
			} else {
				if (fill_opacity > 1) {
					fill_opacity = 1f;
				} else {
					if (fill_opacity < 0) {
						fill_opacity = 0f;
					}
				}
			}
			return (Point2D.getPoint(opacity * stroke_opacity, opacity
					* fill_opacity));
		}
	}

	public boolean doFill(Paint paint, Properties atts,
			HashMap<String, Shader> gradients, boolean whiteMode,
			Integer searchColor, Integer replaceColor, boolean highlighted) {
		doFont(paint, atts);
		if (whiteMode) {
			paint.setStyle(Paint.Style.FILL);
			int fillcolor = 0xFFFFFFFF;
			if (highlighted) {
				fillcolor = 0xffff00ff;
			}
			paint.setColor(fillcolor);
			return true;
		}
		String fillString = atts.getString("fill");
		if (fillString != null && fillString.startsWith("url(#")) {
			// It's a gradient fill, look it up in our map
			String id = fillString.substring("url(#".length(),
					fillString.length() - 1);
			Shader shader = gradients.get(id);
			if (shader != null) {
				// Util.debug("Found shader!");
				paint.setShader(shader);
				paint.setStyle(Paint.Style.FILL);
				return true;
			} else {
				// Util.debug("Didn't find shader!");
				return false;
			}
		} else {
			paint.setShader(null);
			Integer color = atts.getHex("fill");
			if (color != null) {
				doColor(paint, color, true, searchColor, replaceColor,
						highlighted);
				paint.setStyle(Paint.Style.FILL);
				return true;
			} else if (atts.getString("fill") == null
					&& atts.getString("stroke") == null) {
				// Default is black fill
				paint.setStyle(Paint.Style.FILL);
				int fillcolor = 0xFF000000;
				if (highlighted) {
					fillcolor = 0xffff00ff;
				}
				paint.setColor(fillcolor);
				return true;
			}
		}
		return false;
	}

	public boolean doStroke(Paint paint, Properties atts, boolean whiteMode,
			Integer searchColor, Integer replaceColor, boolean highlighted) {
		if (whiteMode) {
			// Never stroke in white mode
			return false;
		}
		Integer color = atts.getHex("stroke");
		if (color != null) {
			doColor(paint, color, false, searchColor, replaceColor, highlighted);
			// Check for other stroke attributes
			Float width = SVGConfig
					.getScaleValue(atts.getFloat("stroke-width"));
			// Set defaults
			if (width == null) {
				width = SVGConfig.getScaleValue(1f);
			}
			paint.setStrokeWidth(width);
			String linecap = atts.getString("stroke-linecap");
			if ("round".equals(linecap)) {
				paint.setStrokeCap(Paint.Cap.ROUND);
			} else if ("square".equals(linecap)) {
				paint.setStrokeCap(Paint.Cap.SQUARE);
			} else if ("butt".equals(linecap)) {
				paint.setStrokeCap(Paint.Cap.BUTT);
			}
			String linejoin = atts.getString("stroke-linejoin");
			if ("miter".equals(linejoin)) {
				paint.setStrokeJoin(Paint.Join.MITER);
			} else if ("round".equals(linejoin)) {
				paint.setStrokeJoin(Paint.Join.ROUND);
			} else if ("bevel".equals(linejoin)) {
				paint.setStrokeJoin(Paint.Join.BEVEL);
			}
			doFont(paint, atts);
			paint.setStyle(Paint.Style.STROKE);
			return true;
		}
		return false;
	}
}