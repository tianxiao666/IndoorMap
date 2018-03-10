package com.iscreate.mobile.indoormap.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.util.AttributeSet;

import com.iscreate.mobile.svg.SvgElement;
import com.iscreate.mobile.svg.SvgLayer;

public class SvgLayerImageView extends CustomImageView {
	/**
	 * svg长和宽
	 */
	private Integer width = null;
	private Integer height = null;
	/**
	 * 图层数据
	 */
	private SvgLayer svglayer = null;
	private SvgElement svgElement = null;

	public SvgLayerImageView(Context context) {
		super(context);
	}

	public SvgLayerImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SvgLayerImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void update() {
		Drawable drawable = null;
		if ((svglayer != null) && (width != null) && (height != null)) {
			Picture picture = new Picture();
			Canvas canvas = picture.beginRecording(width, height);
			svglayer.draw(canvas);
			picture.endRecording();
			drawable = new PictureDrawable(picture);
		}
		this.setImageDrawable(drawable);
	}

	public void setSvgLayer(int width, int height, SvgLayer svglayer) {
		this.width = width;
		this.height = height;
		this.svglayer = svglayer;
		update();
	}

	public void clrSvgLayer() {
		this.width = null;
		this.height = null;
		this.svglayer = null;
		update();
	}

	public SvgLayer getSvgLayer() {
		return (svglayer);
	}

	public void setHightLight(SvgElement svgElem) {
		if (svgElement != null) {
			svgElement.setHighlight(false);
		}
		svgElement = svgElem;
		if (svgElement != null) {
			svgElement.setHighlight(true);
		}
		update();
	}

	public SvgElement getHightLight() {
		return (svgElement);
	}

	public void clrHightLight() {
		setHightLight(null);
	}
}