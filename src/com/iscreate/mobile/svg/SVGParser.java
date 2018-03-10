package com.iscreate.mobile.svg;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.PictureDrawable;

/*

 Licensed to the Apache Software Foundation (ASF) under one or more
 contributor license agreements.  See the NOTICE file distributed with
 this work for additional information regarding copyright ownership.
 The ASF licenses this file to You under the Apache License, Version 2.0
 (the "License"); you may not use this file except in compliance with
 the License.  You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 */

/**
 * Entry point for parsing SVG files for Android. Use one of the various static
 * methods for parsing SVGs by resource, asset or input stream. Optionally, a
 * single color can be searched and replaced in the SVG while parsing. You can
 * also parse an svg path directly.
 * 
 * @author Larva Labs, LLC
 * @see #getSVGFromResource(android.content.res.Resources, int)
 * @see #getSVGFromAsset(android.content.res.AssetManager, String)
 * @see #getSVGFromString(String)
 * @see #getSVGFromInputStream(java.io.InputStream)
 * @see #parsePath(String)
 */
public class SVGParser {
	/**
	 * Parse SVG data from an input stream.
	 * 
	 * @param svgData
	 *            the input stream, with SVG XML data in UTF-8 character
	 *            encoding.
	 * @return the parsed SVG.
	 * @throws Exception
	 *             if there is an error while parsing.
	 */
	public static SVG getSVGFromInputStream(InputStream svgData)
			throws Exception {
		return SVGParser.parse(svgData, 0, 0, false);
	}

	/**
	 * Parse SVG data from a string.
	 * 
	 * @param svgData
	 *            the string containing SVG XML data.
	 * @return the parsed SVG.
	 * @throws Exception
	 *             if there is an error while parsing.
	 */
	public static SVG getSVGFromString(String svgData) throws Exception {
		return SVGParser.parse(new ByteArrayInputStream(svgData.getBytes()), 0,
				0, false);
	}

	/**
	 * convert SVG data to a PictureDrawable
	 * 
	 * @param svgData
	 *            the string containing SVG XML data.
	 * @return converted PictureDrawable
	 * @throws Exception
	 */
	public static PictureDrawable getPictureDrawableFromString(String svgData)
			throws Exception {
		SVG svg = SVGParser.getSVGFromString(svgData);
		return (svg.createPictureDrawable());
	}

	/**
	 * convert SVG data to a SvgStruct
	 * 
	 * @param svgData
	 *            the string containing SVG XML data.
	 * @return converted PictureDrawable
	 * @throws Exception
	 */
	public static SvgStruct getSvgStructFromString(String svgData)
			throws Exception {
		SVG svg = SVGParser.getSVGFromString(svgData);
		return (svg.getSvgStruct());
	}

	/**
	 * Parse SVG data from an Android application resource.
	 * 
	 * @param resources
	 *            the Android context resources.
	 * @param resId
	 *            the ID of the raw resource SVG.
	 * @return the parsed SVG.
	 * @throws Exception
	 *             if there is an error while parsing.
	 */
	public static SVG getSVGFromResource(Resources resources, int resId)
			throws Exception {
		return SVGParser.parse(resources.openRawResource(resId), 0, 0, false);
	}

	/**
	 * Parse SVG data from an Android application asset.
	 * 
	 * @param assetMngr
	 *            the Android asset manager.
	 * @param svgPath
	 *            the path to the SVG file in the application's assets.
	 * @return the parsed SVG.
	 * @throws Exception
	 *             if there is an error while parsing.
	 * @throws IOException
	 *             if there was a problem reading the file.
	 */
	public static SVG getSVGFromAsset(AssetManager assetMngr, String svgPath)
			throws Exception {
		InputStream inputStream = assetMngr.open(svgPath);
		SVG svg = getSVGFromInputStream(inputStream);
		inputStream.close();
		return svg;
	}

	/**
	 * Parse SVG data from an input stream, replacing a single color with
	 * another color.
	 * 
	 * @param svgData
	 *            the input stream, with SVG XML data in UTF-8 character
	 *            encoding.
	 * @param searchColor
	 *            the color in the SVG to replace.
	 * @param replaceColor
	 *            the color with which to replace the search color.
	 * @return the parsed SVG.
	 * @throws Exception
	 *             if there is an error while parsing.
	 */
	public static SVG getSVGFromInputStream(InputStream svgData,
			int searchColor, int replaceColor) throws Exception {
		return SVGParser.parse(svgData, searchColor, replaceColor, false);
	}

	/**
	 * Parse SVG data from a string.
	 * 
	 * @param svgData
	 *            the string containing SVG XML data.
	 * @param searchColor
	 *            the color in the SVG to replace.
	 * @param replaceColor
	 *            the color with which to replace the search color.
	 * @return the parsed SVG.
	 * @throws Exception
	 *             if there is an error while parsing.
	 */
	public static SVG getSVGFromString(String svgData, int searchColor,
			int replaceColor) throws Exception {
		return SVGParser.parse(new ByteArrayInputStream(svgData.getBytes()),
				searchColor, replaceColor, false);
	}

	/**
	 * Parse SVG data from an Android application resource.
	 * 
	 * @param resources
	 *            the Android context
	 * @param resId
	 *            the ID of the raw resource SVG.
	 * @param searchColor
	 *            the color in the SVG to replace.
	 * @param replaceColor
	 *            the color with which to replace the search color.
	 * @return the parsed SVG.
	 * @throws Exception
	 *             if there is an error while parsing.
	 */
	public static SVG getSVGFromResource(Resources resources, int resId,
			int searchColor, int replaceColor) throws Exception {
		return SVGParser.parse(resources.openRawResource(resId), searchColor,
				replaceColor, false);
	}

	/**
	 * Parse SVG data from an Android application asset.
	 * 
	 * @param assetMngr
	 *            the Android asset manager.
	 * @param svgPath
	 *            the path to the SVG file in the application's assets.
	 * @param searchColor
	 *            the color in the SVG to replace.
	 * @param replaceColor
	 *            the color with which to replace the search color.
	 * @return the parsed SVG.
	 * @throws Exception
	 *             if there is an error while parsing.
	 * @throws IOException
	 *             if there was a problem reading the file.
	 */
	public static SVG getSVGFromAsset(AssetManager assetMngr, String svgPath,
			int searchColor, int replaceColor) throws Exception, IOException {
		InputStream inputStream = assetMngr.open(svgPath);
		SVG svg = getSVGFromInputStream(inputStream, searchColor, replaceColor);
		inputStream.close();
		return svg;
	}

	private static SVG parse(InputStream in, Integer searchColor,
			Integer replaceColor, boolean whiteMode) throws Exception {
		// long start = System.currentTimeMillis();
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();
		SVGHandler handler = new SVGHandler();
		handler.setColorSwap(searchColor, replaceColor);
		handler.setWhiteMode(whiteMode);
		xr.setContentHandler(handler);
		xr.parse(new InputSource(in));
		// Util.debug("Parsing complete in " + (System.currentTimeMillis() -
		// start) + " millis.");
		SvgStruct svg = handler.getSvgStruct();
		SVG result = new SVG(svg.getPicture(), handler.bounds);
		result.setSvgStruct(svg);
		// Skip bounds if it was an empty pic
		// if (!Float.isInfinite(handler.limits.top)) {
		// result.setLimits(handler.limits);
		// }
		return result;
	}

	// private static Integer getHexAttr(String name, Attributes attributes) {
	// String v = getStringAttr(name, attributes);
	// // Util.debug("Hex parsing '" + name + "=" + v + "'");
	// if (v == null) {
	// return null;
	// } else {
	// try {
	// return Integer.parseInt(v.substring(1), 16);
	// } catch (NumberFormatException nfe) {
	// // todo - parse word-based color here
	// return null;
	// }
	// }
	// }
}
