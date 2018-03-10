package com.iscreate.mobile.svg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.Log;

public class SVGHandler extends DefaultHandler {
	private SVGUtil svgutil = new SVGUtil();
	// private Picture picture;
	// private Canvas canvas;
	// Scratch rect (so we aren't constantly making new ones)
	private Integer searchColor = null;
	private Integer replaceColor = null;
	private boolean whiteMode = false;
	private HashMap<String, Shader> shaderMap = new HashMap<String, Shader>();
	private HashMap<String, Gradient> gradientRefMap = new HashMap<String, Gradient>();
	private Gradient gradient = null;
	private String value = null;
	private boolean hidden = false;
	private int hiddenLevel = 0;
	private boolean boundsMode = false;
	public RectF bounds = null;
	private SvgStruct svgstruct = null;
	private List<SvgLayer> svglayers = null;
	private String svglayertitle = null;
	private String svglayertype = null;
	private List<SvgElement> svgelems = null;
	private SvgElement svgElem = null;

	// public RectF limits = new RectF(Float.POSITIVE_INFINITY,
	// Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY,
	// Float.NEGATIVE_INFINITY);

	public void setColorSwap(Integer searchColor, Integer replaceColor) {
		this.searchColor = searchColor;
		this.replaceColor = replaceColor;
	}

	public void setWhiteMode(boolean whiteMode) {
		this.whiteMode = whiteMode;
	}

	public SvgStruct getSvgStruct() {
		return (svgstruct);
	}

	@Override
	public void startDocument() throws SAXException {
		// Set up prior to parsing a doc
	}

	@Override
	public void endDocument() throws SAXException {
		// Clean up after parsing a doc
	}

	private Gradient doGradient(boolean isLinear, HashMap<String, String> atts) {
		Gradient gradient = new Gradient();
		gradient.id = SVGUtil.getStringAttr("id", atts);
		gradient.isLinear = isLinear;
		if (isLinear) {
			gradient.x1 = SVGConfig.getScaleValue(svgutil.getFloatAttr("x1",
					atts, 0f));
			gradient.x2 = SVGConfig.getScaleValue(svgutil.getFloatAttr("x2",
					atts, 0f));
			gradient.y1 = SVGConfig.getScaleValue(svgutil.getFloatAttr("y1",
					atts, 0f));
			gradient.y2 = SVGConfig.getScaleValue(svgutil.getFloatAttr("y2",
					atts, 0f));
		} else {
			gradient.x = SVGConfig.getScaleValue(svgutil.getFloatAttr("cx",
					atts, 0f));
			gradient.y = SVGConfig.getScaleValue(svgutil.getFloatAttr("cy",
					atts, 0f));
			gradient.radius = SVGConfig.getScaleValue(svgutil.getFloatAttr("r",
					atts, 0f));
		}
		String transform = SVGUtil.getStringAttr("gradientTransform", atts);
		if (transform != null) {
			gradient.matrix = svgutil.parseTransform(transform);
		}
		String xlink = SVGUtil.getStringAttr("href", atts);
		if (xlink != null) {
			if (xlink.startsWith("#")) {
				xlink = xlink.substring(1);
			}
			gradient.xlink = xlink;
		}
		return gradient;
	}

	// private void doLimits(float x, float y) {
	// if (x < limits.left) {
	// limits.left = x;
	// }
	// if (x > limits.right) {
	// limits.right = x;
	// }
	// if (y < limits.top) {
	// limits.top = y;
	// }
	// if (y > limits.bottom) {
	// limits.bottom = y;
	// }
	// }
	//
	// private void doLimits(float x, float y, float width, float height) {
	// doLimits(x, y);
	// doLimits(x + width, y + height);
	// }
	//
	// private void doLimits(Path path) {
	// path.computeBounds(rect, false);
	// doLimits(rect.left, rect.top);
	// doLimits(rect.right, rect.bottom);
	// }

	@Override
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		svgElem = null;
		value = "";
		HashMap<String, String> svgElemAttr = svgutil.AttributesToHashMap(atts);
		// Reset paint opacity
		// Ignore everything but rectangles in bounds mode
		if (boundsMode) {
			if (localName.equals("rect")) {
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
				Float height = SVGConfig.getScaleValue(svgutil.getFloatAttr(
						"height", svgElemAttr));
				bounds = new RectF(x, y, x + width, y + height);
			}
			return;
		}
		if (localName.equals("svg")) {
			int width = (int) Math.ceil(SVGConfig.getScaleValue(svgutil
					.getFloatAttr("width", svgElemAttr)));
			int height = (int) Math.ceil(SVGConfig.getScaleValue(svgutil
					.getFloatAttr("height", svgElemAttr)));
			// canvas = picture.beginRecording(width, height);
			// paint.setStyle(Paint.Style.FILL);
			// paint.setColor(Color.WHITE);
			// canvas.drawRect(0, 0, width, height, paint);
			// paint.setStyle(Paint.Style.STROKE);
			// paint.setStrokeWidth(2);
			// paint.setColor(Color.RED);
			// canvas.drawRect(0, 0, width, height, paint);
			svgstruct = new SvgStruct(width, height);
			svglayers = new ArrayList<SvgLayer>();
		} else if (localName.equals("defs")) {
			// Ignore
		} else if (localName.equals("linearGradient")) {
			gradient = doGradient(true, svgElemAttr);
		} else if (localName.equals("radialGradient")) {
			gradient = doGradient(false, svgElemAttr);
		} else if (localName.equals("stop")) {
			if (gradient != null) {
				float offset = SVGConfig.getScaleValue(svgutil.getFloatAttr(
						"offset", svgElemAttr));
				String styles = SVGUtil.getStringAttr("style", svgElemAttr);
				StyleSet styleSet = new StyleSet(styles);
				String colorStyle = styleSet.getStyle("stop-color");
				int color = Color.BLACK;
				if (colorStyle != null) {
					if (colorStyle.startsWith("#")) {
						color = Integer.parseInt(colorStyle.substring(1), 16);
					} else {
						color = Integer.parseInt(colorStyle, 16);
					}
				}
				String opacityStyle = styleSet.getStyle("stop-opacity");
				if (opacityStyle != null) {
					float alpha = Float.parseFloat(opacityStyle);
					int alphaInt = Math.round(255 * alpha);
					color |= (alphaInt << 24);
				} else {
					color |= 0xFF000000;
				}
				gradient.positions.add(offset);
				gradient.colors.add(color);
			}
		} else if (localName.equals("g")) {
			// Check to see if this is the "bounds" layer
			svglayertitle = null;
			svglayertype = null;
			svgelems = new ArrayList<SvgElement>();
			if ("bounds".equalsIgnoreCase(SVGUtil.getStringAttr("id",
					svgElemAttr))) {
				boundsMode = true;
			}
			if (hidden) {
				hiddenLevel++;
				// Util.debug("Hidden up: " + hiddenLevel);
			}
			// Go in to hidden mode if display is "none"
			if ("none".equals(SVGUtil.getStringAttr("display", svgElemAttr))) {
				if (!hidden) {
					hidden = true;
					hiddenLevel = 1;
					// Util.debug("Hidden up: " + hiddenLevel);
				}
			}
		} else {
			if (!hidden) {
				int svgElemId = SvgElement.getTypeId(localName);
				if (svgElemId != SvgElement.TYPE_ID_none) {
					svgElem = new SvgElement(svgElemId);
					svgElem.setAttr(svgElemAttr);
					svgElem.setColorSwap(searchColor, replaceColor);
					svgElem.setWhiteMode(whiteMode);
					svgElem.setShaderMap(shaderMap);
					svgElem.convertToPath();
				} else {
					Log.d("SVGHandler", "unrecognized SVG tag: " + localName);
				}
			}
		}
	}

	@Override
	public void characters(char ch[], int start, int length) {
		if (value != null) {
			value = value + String.valueOf(ch, start, length);
		}
	}

	@Override
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		if (localName.equals("svg")) {
			if ((svgstruct != null) && (svglayers != null)) {
				SvgLayer[] svgLayers1 = new SvgLayer[svglayers.size()];
				int i = 0;
				while (i < svgLayers1.length) {
					svgLayers1[i] = svglayers.get(i);
					++i;
				}
				svgstruct.setSvgLayers(svgLayers1);
			}
			// picture.endRecording();
		} else if (localName.equals("linearGradient")) {
			if (gradient.id != null) {
				if (gradient.xlink != null) {
					Gradient parent = gradientRefMap.get(gradient.xlink);
					if (parent != null) {
						gradient = parent.createChild(gradient);
					}
				}
				int[] colors = new int[gradient.colors.size()];
				for (int i = 0; i < colors.length; i++) {
					colors[i] = gradient.colors.get(i);
				}
				float[] positions = new float[gradient.positions.size()];
				for (int i = 0; i < positions.length; i++) {
					positions[i] = gradient.positions.get(i);
				}
				if (colors.length == 0) {
					Log.d("BAD", "BAD");
				}
				LinearGradient g = new LinearGradient(gradient.x1, gradient.y1,
						gradient.x2, gradient.y2, colors, positions,
						Shader.TileMode.CLAMP);
				if (gradient.matrix != null) {
					g.setLocalMatrix(gradient.matrix);
				}
				shaderMap.put(gradient.id, g);
				gradientRefMap.put(gradient.id, gradient);
			}
		} else if (localName.equals("radialGradient")) {
			if (gradient.id != null) {
				if (gradient.xlink != null) {
					Gradient parent = gradientRefMap.get(gradient.xlink);
					if (parent != null) {
						gradient = parent.createChild(gradient);
					}
				}
				int[] colors = new int[gradient.colors.size()];
				for (int i = 0; i < colors.length; i++) {
					colors[i] = gradient.colors.get(i);
				}
				float[] positions = new float[gradient.positions.size()];
				for (int i = 0; i < positions.length; i++) {
					positions[i] = gradient.positions.get(i);
				}
				if (gradient.xlink != null) {
					Gradient parent = gradientRefMap.get(gradient.xlink);
					if (parent != null) {
						gradient = parent.createChild(gradient);
					}
				}
				RadialGradient g = new RadialGradient(gradient.x, gradient.y,
						gradient.radius, colors, positions,
						Shader.TileMode.CLAMP);
				if (gradient.matrix != null) {
					g.setLocalMatrix(gradient.matrix);
				}
				shaderMap.put(gradient.id, g);
				gradientRefMap.put(gradient.id, gradient);
			}
		} else if (localName.equals("g")) {
			if (svglayers != null) {
				SvgLayer svglayer = new SvgLayer(svglayertitle, svglayertype);
				if (svgelems != null) {
					SvgElement[] svgelems1 = new SvgElement[svgelems.size()];
					int i = 0;
					while (i < svgelems1.length) {
						svgelems1[i] = svgelems.get(i);
						++i;
					}
					svglayer.setSvgElements(svgelems1);
				}
				svglayers.add(svglayer);
			}
			if (boundsMode) {
				boundsMode = false;
			}
			// Break out of hidden mode
			if (hidden) {
				hiddenLevel--;
				// Util.debug("Hidden down: " + hiddenLevel);
				if (hiddenLevel == 0) {
					hidden = false;
				}
			}
			// Clear gradient map
			shaderMap.clear();
		}
		if ((value != null) && (value.length() <= 0)) {
			value = null;
		}
		if (localName.equals("desc")) {
			svglayertype = value;
		} else {
			if (localName.equals("title")) {
				svglayertitle = value;
			}
		}
		if ((svgelems != null) && (svgElem != null)) {
			svgElem.setValue(value);
			svgelems.add(svgElem);
			// svgElem.draw(canvas);
		}
		svgElem = null;
	}
}
