package com.iscreate.mobile.indoormap.widget;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Picture;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;

import com.iscreate.mobile.indoormap.poi.SignalDistribution;
import com.iscreate.mobile.svg.SvgElementCircle;
import com.iscreate.mobile.utils.MathUtil;
import com.iscreate.mobile.widget.ElectromagneticWave;
import com.iscreate.mobile.widget.Point2D;

public class SignalLayerImageView extends CustomImageView {
	private final float POI_Radius = 3f;
	/**
	 * svg长和宽
	 */
	private Integer width = null;
	private Integer height = null;
	private HashMap<String, List<SignalDistribution>> signalDistributionListMap = null;
	private boolean isDrawIdeal = true;
	private String currentPosKey = null;
	private Integer currentPos = null;
	private boolean bcombin = false;

	public SignalLayerImageView(Context context) {
		super(context);
	}

	public SignalLayerImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SignalLayerImageView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	private int getStrengthColor(int level) {
		// int T = 0xff000000;
		// int R = 0;
		// int G = 0xff;
		// if (level >= ElectromagneticWave.SIGNAL_Strong) {
		// R = 0;
		// G = 0xff;
		// } else {
		// if (level <= ElectromagneticWave.SIGNAL_Weak) {
		// R = 0xff;
		// G = 0;
		// } else {
		// double each = ((double) 0xff)
		// / (ElectromagneticWave.SIGNAL_Strong -
		// ElectromagneticWave.SIGNAL_Weak);
		// int opacity = (int) ((ElectromagneticWave.SIGNAL_Strong - level) *
		// each);
		// R = R + opacity;
		// G = G - opacity;
		// if (R > 0xff) {
		// R = 0xff;
		// }
		// if (G < 0) {
		// G = 0;
		// }
		// }
		// }
		// return (T | ((R << 16) | (G << 8)));
		if (level >= ElectromagneticWave.SIGNAL_Strong) {
			return (Color.GREEN);
		} else {
			if (level >= -60) {
				return (Color.BLUE);
			} else {
				if (level >= ElectromagneticWave.SIGNAL_Moderate) {
					return (Color.CYAN);
				} else {
					if (level >= -75) {
						return (0xFFFFE97F);
					} else {
						if (level >= -80) {
							return (Color.MAGENTA);
						} else {
							if (level > ElectromagneticWave.SIGNAL_Weak) {
								return (Color.YELLOW);
							} else {
								return (Color.RED);
							}
						}
					}
				}
			}
		}
	}

	private void drawSignalDistribution(Canvas canvas, String key, int color) {
		if (signalDistributionListMap != null) {
			List<SignalDistribution> signalDistributionList = signalDistributionListMap
					.get(key);
			if (signalDistributionList != null) {
				int count = signalDistributionList.size();
				if (count > 0) {
					SignalDistribution signaldistribution = signalDistributionList
							.get(0);
					SparseArray<Path> signalleveldistribution = isDrawIdeal ? signaldistribution.IdealSignalDistribution
							: signaldistribution.ReckonSignalDistribution;
					if (signalleveldistribution != null) {
						int levelcount = signalleveldistribution.size();
						final int basecolor = 0x02;
						Paint paint = new Paint();
						paint.setAntiAlias(true);
						paint.setStrokeWidth(1);
						paint.setStyle(Style.FILL_AND_STROKE);
						float stepcolor = (0xff - basecolor) / levelcount;
						if (stepcolor > (basecolor / 2)) {
							stepcolor = (basecolor / 2);
						}
						int i = 0;
						while (i < levelcount) {
							if (bcombin) {
								Path path = null;
								Path pathtmp = null;
								int j = 0;
								while (j < count) {
									if (((currentPosKey == null) || (currentPos == null))
											|| ((currentPosKey == key) && (currentPos == j))) {
										signaldistribution = signalDistributionList
												.get(j);
										if (signaldistribution != null) {
											signalleveldistribution = isDrawIdeal ? signaldistribution.IdealSignalDistribution
													: signaldistribution.ReckonSignalDistribution;
											if (signalleveldistribution != null) {
												pathtmp = signalleveldistribution
														.valueAt(i);
												if ((pathtmp != null)
														&& (!pathtmp.isEmpty())) {
													if (path == null) {
														path = pathtmp;
													} else {
														path.addPath(pathtmp);
													}
												}
											}
										}
									}
									++j;
								}
								if (path != null) {
									color = getStrengthColor(signalleveldistribution
											.keyAt(i));
									paint.setColor(color);
									canvas.drawPath(path, paint);
								}
							} else {
								Path path = null;
								int j = 0;
								while (j < count) {
									if (((currentPosKey == null) || (currentPos == null))
											|| ((currentPosKey == key) && (currentPos == j))) {
										signaldistribution = signalDistributionList
												.get(j);
										if (signaldistribution != null) {
											signalleveldistribution = isDrawIdeal ? signaldistribution.IdealSignalDistribution
													: signaldistribution.ReckonSignalDistribution;
											if (signalleveldistribution != null) {
												path = signalleveldistribution
														.valueAt(i);
												if (path != null) {
													switch (j % 7) {
													case 0:
														color = 0x00FF0000;// 红
														break;
													case 1:
														color = 0x0000FF00;// 绿
														break;
													case 2:
														color = 0x000000FF;// 蓝
														break;
													case 3:
														color = 0x00FF6100;// 橙
														break;
													case 4:
														color = 0x00A020F0;// 紫
														break;
													case 5:
														color = 0x00FFFF00;// 黄
														break;
													case 6:
														color = 0x00082E54;// 靛
														break;
													}
													int t = basecolor
															+ (int) (stepcolor * (i + 1));
													if (t > 0xff) {
														t = 0xff;
													}
													paint.setColor((color & 0x00ffffff)
															| (t << 24));
													canvas.drawPath(path, paint);
												}
											}
										}
									}
									++j;
								}
							}
							++i;
						}
					}
				}
			}
		}
	}

	private void drawSignalDistributionListMap(Canvas canvas) {
		if (signalDistributionListMap != null) {
			Set<String> iteratorset = signalDistributionListMap.keySet();
			int count = iteratorset.size();
			if (count > 0) {
				Iterator<String> iterator = iteratorset.iterator();
				while (iterator.hasNext()) {
					--count;
					int color = Color.RED;
					switch (count % 7) {
					case 0:
						color = 0xFFFF0000;// 红
						break;
					case 1:
						color = 0xFF00FF00;// 绿
						break;
					case 2:
						color = 0xFF0000FF;// 蓝
						break;
					case 3:
						color = 0xFFFF6100;// 橙
						break;
					case 4:
						color = 0xFFA020F0;// 紫
						break;
					case 5:
						color = 0xFFFFFF00;// 黄
						break;
					case 6:
						color = 0xFF082E54;// 靛
						break;
					}
					drawSignalDistribution(canvas, iterator.next(), color);
				}
			}
		}
	}

	private Path getPoiPath(float cx, float cy, float cr) {
		MathUtil mathutil = new MathUtil();
		Path path = SvgElementCircle.getPath(cx, cy, cr);
		Point2D p1 = mathutil.getCirclePoint(cx, cy, cr,
				Math.toRadians(90 - 45));
		Point2D p2 = mathutil.getCirclePoint(cx, cy, cr,
				Math.toRadians(90 + 45));
		Point2D p3 = mathutil.getCirclePoint(cx, cy, cr,
				Math.toRadians(270 - 45));
		Point2D p4 = mathutil.getCirclePoint(cx, cy, cr,
				Math.toRadians(270 + 45));
		path.moveTo((float) p1.x, (float) p1.y);
		path.lineTo((float) p3.x, (float) p3.y);
		path.moveTo((float) p2.x, (float) p2.y);
		path.lineTo((float) p4.x, (float) p4.y);
		return (path);
	}

	private void drawPoiPosition(Canvas canvas) {
		if (signalDistributionListMap != null) {
			Paint paint = new Paint();
			paint.setAntiAlias(true);
			paint.setStrokeWidth(1);
			Iterator<String> iterator = signalDistributionListMap.keySet()
					.iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				List<SignalDistribution> signalDistributionList = signalDistributionListMap
						.get(key);
				if (signalDistributionList != null) {
					int count = signalDistributionList.size();
					if (count > 0) {
						Path poipath = null;
						SignalDistribution signaldistribution = null;
						int i = 0;
						while (i < count) {
							signaldistribution = signalDistributionList.get(i);
							poipath = getPoiPath(signaldistribution.POSITION_X,
									signaldistribution.POSITION_Y, POI_Radius);
							if (((currentPosKey != null) && (currentPosKey == key))
									&& ((currentPos != null) && (currentPos == i))) {
								paint.setColor(Color.BLACK);
								paint.setStyle(Style.FILL);
								canvas.drawPath(poipath, paint);
								paint.setColor(Color.RED);
								paint.setStyle(Style.STROKE);
								canvas.drawPath(poipath, paint);
							} else {
								paint.setColor(Color.RED);
								paint.setStyle(Style.FILL);
								canvas.drawPath(poipath, paint);
								paint.setColor(Color.BLACK);
								paint.setStyle(Style.STROKE);
								canvas.drawPath(poipath, paint);
							}
							++i;
						}
					}
				}
			}
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

	public void update() {
		Drawable drawable = null;
		if ((width != null) && (height != null)) {
			Picture picture = new Picture();
			Canvas canvas = picture.beginRecording(width, height);
			drawSignalDistributionListMap(canvas);
			drawPoiPosition(canvas);
			picture.endRecording();
			drawable = new PictureDrawable(picture);
		}
		this.setImageDrawable(drawable);
	}

	public void setSignalDistribution(
			int width,
			int height,
			HashMap<String, List<SignalDistribution>> signalDistributionListMap,
			boolean isDrawIdeal, boolean bcombin) {
		this.width = width;
		this.height = height;
		this.signalDistributionListMap = signalDistributionListMap;
		this.isDrawIdeal = isDrawIdeal;
		this.bcombin = bcombin;
		update();
	}

	public void clrSignalDistribution() {
		this.width = null;
		this.height = null;
		this.signalDistributionListMap = null;
		update();
	}

	public boolean handleOnClick(float xi, float yi) {
		if ((signalDistributionListMap != null)
				&& (signalDistributionListMap.size() > 0)
				&& (getVisibility() == View.VISIBLE)) {
			float scale = 0;
			{
				Matrix matrix = getImageMatrix();
				float[] matrixValues = new float[9];
				matrix.getValues(matrixValues);
				scale = (matrixValues[Matrix.MSCALE_X] + matrixValues[Matrix.MSCALE_Y]) / 2;
			}
			final float mindis = (scale > 2) ? POI_Radius
					: ((scale > 1) ? (float) (POI_Radius * 1.5)
							: (POI_Radius * 3));
			final MathUtil mathutil = new MathUtil();
			String poskey = null;
			Integer pos = null;
			Double min = null;
			Iterator<String> iterator = signalDistributionListMap.keySet()
					.iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				List<SignalDistribution> signalDistributionList = signalDistributionListMap
						.get(key);
				if (signalDistributionList != null) {
					int count = signalDistributionList.size();
					if (count > 0) {
						SignalDistribution signaldistribution = null;
						double distance = 0;
						int i = 0;
						while (i < count) {
							signaldistribution = signalDistributionList.get(i);
							distance = mathutil.getDistanceForTwoPoint(
									signaldistribution.POSITION_X,
									signaldistribution.POSITION_Y, xi, yi);
							if (distance < mindis) {
								if (min == null) {
									min = distance;
									pos = i;
									poskey = key;
								} else {
									if (distance < min) {
										min = distance;
										pos = i;
										poskey = key;
									}
								}
							}
							++i;
						}
					}
				}
			}
			if (pos != null) {
				if (currentPos == pos) {
					currentPos = null;
					currentPosKey = null;
				} else {
					currentPos = pos;
					currentPosKey = poskey;
				}
				update();
				return (true);
			}
		}
		return (false);
	}

	public SignalDistribution getCurrentSignalDistribution() {
		if ((signalDistributionListMap != null) && (currentPosKey != null)) {
			List<SignalDistribution> signalDistributionList = signalDistributionListMap
					.get(currentPosKey);
			if ((signalDistributionList != null)
					&& (currentPos < signalDistributionList.size())) {
				return (signalDistributionList.get(currentPos));
			}
		}
		return (null);
	}
}