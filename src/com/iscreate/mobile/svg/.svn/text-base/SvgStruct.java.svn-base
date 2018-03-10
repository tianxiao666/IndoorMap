package com.iscreate.mobile.svg;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Region.Op;
import android.graphics.RegionIterator;
import android.graphics.drawable.PictureDrawable;

import com.iscreate.mobile.utils.MathUtil;
import com.iscreate.mobile.utils.utils;
import com.iscreate.mobile.widget.Point2D;

public class SvgStruct {
	public static final float scale = 0.15f;
	private float width = 0;
	private float height = 0;
	private SvgLayer[] svgLayers = null;

	public SvgStruct(float width, float height) {
		this.width = width;
		this.height = height;
	}

	public void setSvgLayers(SvgLayer[] svgLayers) {
		this.svgLayers = svgLayers;
	}

	public float getWidth() {
		return (width);
	}

	public float getHeight() {
		return (height);
	}

	public int getCeilWidth() {
		return ((int) Math.ceil(width));
	}

	public int getCeilHeight() {
		return ((int) Math.ceil(height));
	}

	public SvgLayer[] getSvgLayers() {
		return (svgLayers);
	}

	public SvgLayer getSvgLayer(int layertype) {
		if (svgLayers != null) {
			int i = 0;
			while (i < svgLayers.length) {
				if ((svgLayers[i] != null)
						&& (svgLayers[i].getSvgLayerType() == layertype)) {
					return (svgLayers[i]);
				}
				++i;
			}
		}
		return (null);
	}

	/**
	 * 画图层
	 */
	public void draw(Canvas canvas, int layertype) {
		if (svgLayers != null) {
			SvgLayer svglayer = getSvgLayer(layertype);
			svglayer.draw(canvas);
		}
	}

	/**
	 * 获取图层Picture
	 */
	public Picture getPicture(int layertype) {
		Picture picture = new Picture();
		Canvas canvas = picture.beginRecording(getCeilWidth(), getCeilHeight());
		draw(canvas, layertype);
		picture.endRecording();
		return (picture);
	}

	/**
	 * 获取图层PictureDrawable
	 */
	public PictureDrawable getPictureDrawable(int layertype) {
		return (new PictureDrawable(getPicture(layertype)));
	}

	/**
	 * 画平面图
	 */
	public void draw(Canvas canvas) {
		if (svgLayers != null) {
			int i = 0;
			while (i < svgLayers.length) {
				if (svgLayers[i] != null) {
					svgLayers[i].draw(canvas);
				}
				++i;
			}
		}
	}

	/**
	 * 获取平面图Picture
	 */
	public Picture getPicture() {
		Picture picture = new Picture();
		Canvas canvas = picture.beginRecording(getCeilWidth(), getCeilHeight());
		draw(canvas);
		picture.endRecording();
		return (picture);
	}

	/**
	 * 获取平面图PictureDrawable
	 */
	public PictureDrawable getPictureDrawable() {
		return (new PictureDrawable(getPicture()));
	}

	/**
	 * 获取Region和svg图层元素相交的区域
	 */
	public Region getIntersectRegion(final Region ro, SvgElement[] svgElems) {
		if ((ro != null) && (!ro.isEmpty()) && (svgElems != null)) {
			Region cr = null;
			Region nr = new Region();
			SvgElement svgElem = null;
			int i = 0;
			while (i < svgElems.length) {
				cr = new Region(ro);
				svgElem = svgElems[i];
				if (svgElem.getTypeId() == SvgElement.TYPE_ID_path) {
					Region r = svgElem.getRegion();
					if ((r != null) && cr.op(r, Op.INTERSECT)) {
						nr.op(cr, Op.UNION);
					}
				}
				++i;
			}
			if (!nr.isEmpty()) {
				return (nr);
			}
		}
		return (null);
	}

	/**
	 * 获取平面图两点之间的墙壁区域
	 */
	public Region getWallRegionForTwoPoint(float x0, float y0, float x1,
			float y1) {
		if (!((x0 == x1) && (y0 == y1))) {
			SvgLayer svglayer = getSvgLayer(SvgLayer.TYPE_ID_OUTWALL);
			if (svglayer != null) {
				SvgElement[] svgElems = svglayer.getSvgElements();
				if (svgElems != null) {
					final float c = 1.5f;
					Path path = new Path();
					float x2, x3, x4, x5, y2, y3, y4, y5;
					if (x1 == x0) {
						x2 = x0 - c;
						y2 = y0;
						x3 = x0 + c;
						y3 = y0;
						x4 = x1 + c;
						y4 = y1;
						x5 = x1 - c;
						y5 = y1;
					} else {
						if (y1 == y0) {
							x2 = x0;
							y2 = y0 - c;
							x3 = x0;
							y3 = y0 + c;
							x4 = x1;
							y4 = y1 + c;
							x5 = x1;
							y5 = y1 - c;
						} else {
							float k = (y1 - y0) / (x1 - x0);
							if (k > 0) {
								x2 = x0 - c;
								y2 = y0 + c;
								x3 = x0 + c;
								y3 = y0 - c;
								x4 = x1 + c;
								y4 = y1 - c;
								x5 = x1 - c;
								y5 = y1 + c;
							} else {
								x2 = x0 - c;
								y2 = y0 - c;
								x3 = x0 + c;
								y3 = y0 + c;
								x4 = x1 + c;
								y4 = y1 + c;
								x5 = x1 - c;
								y5 = y1 - c;
							}
						}
					}
					path.moveTo(x2, y2);
					path.lineTo(x3, y3);
					path.lineTo(x4, y4);
					path.lineTo(x5, y5);
					path.close();
					Region r = utils.getPathRegion(path);
					if ((r != null) && (!r.isEmpty())) {
						r = getIntersectRegion(r, svgElems);
						if ((r != null) && (!r.isEmpty())) {
							return (r);
						}
					}
				}
			}
		}
		return (null);
	}

	/**
	 * 获取平面图两点之间的墙壁信息
	 */
	public List<RectF> getWallInfoForTwoPoint(float x0, float y0, float x1,
			float y1) {
		Region wr = getWallRegionForTwoPoint(x0, y0, x1, y1);
		if (wr != null) {
			List<RectF> rList = new ArrayList<RectF>();
			MathUtil mathUtil = new MathUtil();
			RegionIterator riterator = new RegionIterator(wr);
			Rect r = new Rect();
			while (riterator.next(r)) {
				Point2D[] intersections = mathUtil
						.getIntersectionForRectAndLine(r.left, r.top, r.right,
								r.bottom, x0, y0, x1, y1);
				if ((intersections != null) && (intersections.length == 2)) {
					if (// 降序
					((x0 > x1) && (intersections[0].x < intersections[1].x))
							|| // 升序
							((x0 < x1) && (intersections[0].x > intersections[1].x))
							|| // 降序
							((x0 == x1) && (y0 > y1) && (intersections[0].y < intersections[1].y))
							|| // 升序
							((x0 == x1) && (y0 < y1) && (intersections[0].y > intersections[1].y))) {
						Point2D tmp = intersections[1];
						intersections[1] = intersections[0];
						intersections[0] = tmp;
					}
					rList.add(new RectF((float) intersections[0].x,
							(float) intersections[0].y,
							(float) intersections[1].x,
							(float) intersections[1].y));
				}
			}
			int count = rList.size();
			if (count > 0) {
				RectF rectfi = null;
				RectF rectfj = null;
				int i = 0;
				int j = 0;
				while (i < count) {
					rectfi = rList.get(i);
					j = i + 1;
					while (j < count) {
						rectfj = rList.get(j);
						if (// 降序
						((x0 > x1) && (rectfi.right < rectfj.right))
								|| // 升序
								((x0 < x1) && (rectfi.left > rectfj.left))
								|| // 升序
								((x0 == x1) && (y0 > y1) && (rectfi.bottom < rectfj.bottom))
								|| // 升序
								((x0 == x1) && (y0 < y1) && (rectfi.top > rectfj.top))) {
							rList.set(i, rectfj);
							rList.set(j, rectfi);
							rectfi = rList.get(i);
							rectfj = rList.get(j);
						}
						++j;
					}
					++i;
				}
				return (rList);
			}
		}
		return (null);
	}
}