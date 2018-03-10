package com.iscreate.mobile.indoormap.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;

import com.iscreate.mobile.indoormap.activity.IndoorMapActivity;
import com.iscreate.mobile.indoormap.activity.ViewSignalDataActivity;
import com.iscreate.mobile.indoormap.poi.ApInfo;
import com.iscreate.mobile.indoormap.poi.PoiInfo;
import com.iscreate.mobile.indoormap.poi.SignalDistribution;
import com.iscreate.mobile.service.ServiceClientInterface;
import com.iscreate.mobile.svg.SvgElement;
import com.iscreate.mobile.svg.SvgElementCircle;
import com.iscreate.mobile.svg.SvgLayer;
import com.iscreate.mobile.svg.SvgStruct;
import com.iscreate.mobile.utils.MathUtil;
import com.iscreate.mobile.widget.ElectromagneticWave;
import com.iscreate.mobile.widget.Point2D;
import com.iscreate.mobile.widget.ZoomPlaneGraphFrameLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Region.Op;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class PlaneGraphFrameLayout extends ZoomPlaneGraphFrameLayout {
	/**
	 * 平面图背景色，默认为白色
	 */
	private final int PLANEGRAPH_BACKGROUND_COLOR = Color.WHITE;
	private final static String LAYER_BACKGROUND = "BACKGROUND";
	private final static String LAYER_OVERLAYER = "OVERLAYER";
	private final static String LAYER_IDEALSIGNAL = "IDEALSIGNAL";
	private final static String LAYER_RECKONSIGNAL = "RECKONSIGNAL";
	private final static String LAYER_BSIDEALSIGNAL = "BSIDEALSIGNAL";
	private final static String LAYER_BSRECKONSIGNAL = "BSRECKONSIGNAL";
	public final static int LAYER_ID_BACKGROUND = SvgLayer.TYPE_ID_END + 1;
	public final static int LAYER_ID_OVERLAYER = SvgLayer.TYPE_ID_END + 2;
	public final static int LAYER_ID_IDEALSIGNAL = SvgLayer.TYPE_ID_END + 3;
	public final static int LAYER_ID_RECKONSIGNAL = SvgLayer.TYPE_ID_END + 4;
	public final static int LAYER_ID_BSIDEALSIGNAL = SvgLayer.TYPE_ID_END + 5;
	public final static int LAYER_ID_BSRECKONSIGNAL = SvgLayer.TYPE_ID_END + 6;
	public final static int[] LAYER_ID_LIST = { SvgLayer.TYPE_ID_OUTWALL,//
			SvgLayer.TYPE_ID_TOILE,//
			SvgLayer.TYPE_ID_ELEVATE, //
			SvgLayer.TYPE_ID_STAIR,//
			SvgLayer.TYPE_ID_ROUTE,//
			SvgLayer.TYPE_ID_BUSSINESS,//
			SvgLayer.TYPE_ID_AP,//
			SvgLayer.TYPE_ID_POI //
	};
	private ImageView im_bglayer = null;
	private OverLayerImageView im_overlayer = null;
	private SignalLayerImageView im_idealsignallayer = null;
	private SignalLayerImageView im_reckonsignallayer = null;
	private SignalLayerImageView im_bsidealsignallayer = null;
	private SignalLayerImageView im_bsreckonsignallayer = null;
	private SparseArray<SvgLayerImageView> PlaneGraphLayerList = null;
	private SvgStruct svgstruct = null;
	private Integer HightlightedsvgLayer = null;
	private List<ApInfo> apinfoList = null;
	private List<PoiInfo> poiantinfoList = null;
	private SparseIntArray layervisibilitystatus = null;
	private CustumProgressDialog pd = null;
	private IndoorMapActivity indoorMapActivity;
	private Map<String,Object> paramsMap = new HashMap<String,Object>();
	private String operType = null;
	
	public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}

	public Map<String, Object> getParamsMap() {
		return paramsMap;
	}

	public void setParamsMap(Map<String, Object> paramsMap) {
		this.paramsMap = paramsMap;
	}

	public IndoorMapActivity getIndoorMapActivity() {
		return indoorMapActivity;
	}

	public void setIndoorMapActivity(IndoorMapActivity indoorMapActivity) {
		this.indoorMapActivity = indoorMapActivity;
	}

	public PlaneGraphFrameLayout(Context context) {
		super(context);
		init();
	}

	public PlaneGraphFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PlaneGraphFrameLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		pd = new CustumProgressDialog(getContext());
		pd.setCancelable(false);
		pd.setTitle("请稍后");
		pd.setMessage("正在计算......");
		layervisibilitystatus = new SparseIntArray();
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.MATCH_PARENT);
		im_bglayer = new ImageView(getContext()) {
			@Override
			protected void onDraw(Canvas canvas) {
				drawPlaneGraphBackground(canvas);
				super.onDraw(canvas);
			}
		};
		im_bglayer.setLayoutParams(params);
		im_bglayer.setScaleType(ScaleType.MATRIX);
		im_bglayer.setTag(LAYER_BACKGROUND);
		addView(im_bglayer);
		layervisibilitystatus.put(LAYER_ID_BACKGROUND, View.VISIBLE);
		PlaneGraphLayerList = new SparseArray<SvgLayerImageView>();
		for (int layer : LAYER_ID_LIST) {
			if (SvgLayer.TYPE_ID_OUTWALL != layer) {
				addImageView(layer);
			}
		}
		im_idealsignallayer = new SignalLayerImageView(getContext());
		im_idealsignallayer.setLayoutParams(params);
		im_idealsignallayer.setScaleType(ScaleType.MATRIX);
		im_idealsignallayer.setTag(LAYER_IDEALSIGNAL);
		addView(im_idealsignallayer);
		im_idealsignallayer.setVisibility(View.INVISIBLE);
		layervisibilitystatus.put(LAYER_ID_IDEALSIGNAL, View.INVISIBLE);
		im_reckonsignallayer = new SignalLayerImageView(getContext());
		im_reckonsignallayer.setLayoutParams(params);
		im_reckonsignallayer.setScaleType(ScaleType.MATRIX);
		im_reckonsignallayer.setTag(LAYER_RECKONSIGNAL);
		addView(im_reckonsignallayer);
		im_reckonsignallayer.setVisibility(View.INVISIBLE);
		layervisibilitystatus.put(LAYER_ID_RECKONSIGNAL, View.INVISIBLE);
		im_bsidealsignallayer = new SignalLayerImageView(getContext());
		im_bsidealsignallayer.setLayoutParams(params);
		im_bsidealsignallayer.setScaleType(ScaleType.MATRIX);
		im_bsidealsignallayer.setTag(LAYER_BSIDEALSIGNAL);
		addView(im_bsidealsignallayer);
		im_bsidealsignallayer.setVisibility(View.INVISIBLE);
		layervisibilitystatus.put(LAYER_ID_BSIDEALSIGNAL, View.INVISIBLE);
		im_bsreckonsignallayer = new SignalLayerImageView(getContext());
		im_bsreckonsignallayer.setLayoutParams(params);
		im_bsreckonsignallayer.setScaleType(ScaleType.MATRIX);
		im_bsreckonsignallayer.setTag(LAYER_BSRECKONSIGNAL);
		addView(im_bsreckonsignallayer);
		im_bsreckonsignallayer.setVisibility(View.INVISIBLE);
		layervisibilitystatus.put(LAYER_ID_BSRECKONSIGNAL, View.INVISIBLE);
		addImageView(SvgLayer.TYPE_ID_OUTWALL);
		im_overlayer = new OverLayerImageView(getContext());
		im_overlayer.setLayoutParams(params);
		im_overlayer.setScaleType(ScaleType.MATRIX);
		im_overlayer.setTag(LAYER_OVERLAYER);
		addView(im_overlayer);
		layervisibilitystatus.put(LAYER_ID_OVERLAYER, View.VISIBLE);
	}

	/**
	 * 画平面图背景
	 */
	private void drawPlaneGraphBackground(Canvas canvas) {
		if (hasDrawable()) {
			Paint paint = new Paint();
			paint.setStyle(Style.FILL);
			paint.setColor(PLANEGRAPH_BACKGROUND_COLOR);
			canvas.drawRect(getXOnView(0), getYOnView(0),
					getXOnView(getPlaneGraphWidth()),
					getYOnView(getPlaneGraphHeight()), paint);
		}
	}

	/**
	 * 添加平面图图层
	 */
	private void addImageView(int layer) {
		SvgLayerImageView im = new SvgLayerImageView(getContext());
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.MATCH_PARENT);
		im.setLayoutParams(params);
		im.setScaleType(ScaleType.MATRIX);
		im.setTag(layer);
		addView(im);
		layervisibilitystatus.put(layer, View.VISIBLE);
		PlaneGraphLayerList.put(layer, im);
	}

	/**
	 * 设置平面图Matrix
	 * 
	 * @param matrix
	 */
	private void setImageMatrix(Matrix matrix) {
		im_bglayer.setImageMatrix(matrix);
		im_overlayer.setImageMatrix(matrix);
		for (int layer : LAYER_ID_LIST) {
			PlaneGraphLayerList.get(layer).setImageMatrix(matrix);
		}
		im_idealsignallayer.setImageMatrix(matrix);
		im_reckonsignallayer.setImageMatrix(matrix);
		im_bsidealsignallayer.setImageMatrix(matrix);
		im_bsreckonsignallayer.setImageMatrix(matrix);
	}

	/**
	 * 平面图图层是否有Drawable
	 */
	private boolean hasDrawable() {
		for (int layer : LAYER_ID_LIST) {
			if (PlaneGraphLayerList.get(layer).getDrawable() != null) {
				return (true);
			}
		}
		return (false);
	}

	private Float[] getIdealSignal(float xi, float yi) {
		List<SignalDistribution> apsignaldistributionlist = getApSignalDistributionList();
		if (apsignaldistributionlist != null) {
			ElectromagneticWave emw = new ElectromagneticWave();
			MathUtil mathUtil = new MathUtil();
			SignalDistribution signaldistribution = null;
			double d = 0;
			Float[] array_signal = new Float[apsignaldistributionlist.size()];
			int i = 0;
			while (i < array_signal.length) {
				signaldistribution = apsignaldistributionlist.get(i);
				if (signaldistribution != null) {
					d = mathUtil.getDistanceForTwoPoint(
							signaldistribution.POSITION_X,
							signaldistribution.POSITION_Y, xi, yi)
							* SvgStruct.scale;
					emw.setPower(signaldistribution.POWER);
					emw.setFrequency(signaldistribution.FREQUENCY);
					array_signal[i] = (float) emw.getSignalViaRadius(d);
					if (array_signal[i] <= ElectromagneticWave.SIGNAL_Min) {
						array_signal[i] = null;
					}
				}
				++i;
			}
			return (array_signal);
		}
		return (null);
	}

	private Float[] getReckonSignal(float xi, float yi) {
		List<SignalDistribution> apsignaldistributionlist = getApSignalDistributionList();
		if (apsignaldistributionlist != null) {
			ElectromagneticWave emw = new ElectromagneticWave();
			Float[] array_signal = new Float[apsignaldistributionlist.size()];
			Point2D signal = null;
			SignalDistribution signaldistribution = null;
			int i = 0;
			while (i < array_signal.length) {
				if (apsignaldistributionlist.get(i) != null) {
					signaldistribution = apsignaldistributionlist.get(i);
					emw.setPower(signaldistribution.POWER);
					emw.setFrequency(signaldistribution.FREQUENCY);
					signal = getCalcSingal(signaldistribution.POSITION_X,
							signaldistribution.POSITION_Y, xi, yi, emw);
					array_signal[i] = (float) signal.y;
					if (array_signal[i] <= ElectromagneticWave.SIGNAL_Min) {
						array_signal[i] = null;
					}
				}
				++i;
			}
			return (array_signal);
		}
		return (null);
	}

	private Point2D getBSIdealSignal(float xi, float yi,
			SignalDistribution currentSignalDistribution) {
		if (poiantinfoList != null) {
			ElectromagneticWave emw = new ElectromagneticWave();
			MathUtil mathUtil = new MathUtil();
			if (currentSignalDistribution != null) {
				emw.setPower(currentSignalDistribution.POWER);
				emw.setFrequency(currentSignalDistribution.FREQUENCY);
				double d = mathUtil.getDistanceForTwoPoint(
						currentSignalDistribution.POSITION_X,
						currentSignalDistribution.POSITION_Y, xi, yi)
						* SvgStruct.scale;
				double s = emw.getSignalViaRadius(d);
				return (Point2D.getPoint(d, s));
			} else {
				SignalDistribution signaldistribution = null;
				Point2D maxsignal = null;
				Point2D signal = null;
				int count = poiantinfoList.size();
				int i = 0;
				while (i < count) {
					if (poiantinfoList.get(i) != null) {
						signaldistribution = poiantinfoList.get(i);
						double d = mathUtil.getDistanceForTwoPoint(
								signaldistribution.POSITION_X,
								signaldistribution.POSITION_Y, xi, yi)
								* SvgStruct.scale;
						emw.setPower(signaldistribution.POWER);
						emw.setFrequency(signaldistribution.FREQUENCY);
						double s = emw.getSignalViaRadius(d);
						signal = Point2D.getPoint(d, s);
						if (signal != null) {
							if (maxsignal == null) {
								maxsignal = signal;
							} else {
								if (signal.y > maxsignal.y) {
									maxsignal = signal;
								}
							}
						}
					}
					++i;
				}
				return (maxsignal);
			}
		}
		return (null);
	}

	private Point2D getBSReckonSignal(final float xi, final float yi,
			SignalDistribution currentSignalDistribution) {
		if (poiantinfoList != null) {
			ElectromagneticWave emw = new ElectromagneticWave();
			if (currentSignalDistribution != null) {
				emw.setPower(currentSignalDistribution.POWER);
				emw.setFrequency(currentSignalDistribution.FREQUENCY);
				return (getCalcSingal(currentSignalDistribution.POSITION_X,
						currentSignalDistribution.POSITION_Y, xi, yi, emw));
			} else {
				SignalDistribution signaldistribution = null;
				Point2D maxsignal = null;
				Point2D signal = null;
				int count = poiantinfoList.size();
				int i = 0;
				while (i < count) {
					if (poiantinfoList.get(i) != null) {
						signaldistribution = poiantinfoList.get(i);
						emw.setPower(signaldistribution.POWER);
						emw.setFrequency(signaldistribution.FREQUENCY);
						signal = getCalcSingal(signaldistribution.POSITION_X,
								signaldistribution.POSITION_Y, xi, yi, emw);
						if (signal != null) {
							if (maxsignal == null) {
								maxsignal = signal;
							} else {
								if (signal.y > maxsignal.y) {
									maxsignal = signal;
								}
							}
						}
					}
					++i;
				}
				return (maxsignal);
			}
		}
		return (null);
	}

	private void onClickDialog() {
		im_overlayer.clrDialog();
	}

	/**
	 * 点击在图层上的元素时
	 */
	private void onClickLayer(float xi, float yi, int layertype,
			SvgElement svgElem) {
		switch (layertype) {
		case LAYER_ID_BSRECKONSIGNAL:
			if (!im_bsreckonsignallayer.handleOnClick(xi, yi)) {
				if (!im_overlayer.isInDialog(xi, yi)) {
					List<BasicNameValuePair> slist = new ArrayList<BasicNameValuePair>();
					Point2D signal = getBSReckonSignal(xi, yi,
							im_bsreckonsignallayer
									.getCurrentSignalDistribution());
					if (signal != null) {
						slist.add(new BasicNameValuePair("距离（米）", ""
								+ (float) signal.x));
						slist.add(new BasicNameValuePair("信号（dBm）", ""
								+ (float) signal.y));
						im_overlayer.setDialog(getXOnView(xi), getYOnView(yi), slist);
					} else {
						im_overlayer.clrDialog();
					}
				} else {
					im_overlayer.clrDialog();
				}
			}
			break;
		case LAYER_ID_BSIDEALSIGNAL:
			if (!im_bsidealsignallayer.handleOnClick(xi, yi)) {
				if (!im_overlayer.isInDialog(xi, yi)) {
					List<BasicNameValuePair> slist = new ArrayList<BasicNameValuePair>();
					Point2D signal = getBSIdealSignal(xi, yi,
							im_bsidealsignallayer
									.getCurrentSignalDistribution());
					if (signal != null) {
						slist.add(new BasicNameValuePair("距离（米）", ""
								+ (float) signal.x));
						slist.add(new BasicNameValuePair("信号（dBm）", ""
								+ (float) signal.y));
						im_overlayer.setDialog(getXOnView(xi), getYOnView(yi), slist);
					} else {
						im_overlayer.clrDialog();
					}
				} else {
					im_overlayer.clrDialog();
				}
			}
			break;
		case LAYER_ID_RECKONSIGNAL:
			if (!im_reckonsignallayer.handleOnClick(xi, yi)) {
				if (!im_overlayer.isInDialog(xi, yi)) {
					List<BasicNameValuePair> slist = new ArrayList<BasicNameValuePair>();
					Float[] array_signal = getReckonSignal(xi, yi);
					if (array_signal != null) {
						int i = 0;
						StringBuffer signalLevel = new StringBuffer("");
						while (i < array_signal.length) {
							if (array_signal[i] != null) {
								ApInfo apinfo = apinfoList.get(i);
								slist.add(new BasicNameValuePair(
										apinfo.EQUT_SSID, "" + array_signal[i]));
								if(array_signal[i]!=null){
									signalLevel.append(","+apinfo.EQUT_SSID+":"+array_signal[i]);
								}
							}
							++i;
						}
						if(signalLevel.length()>0){
							//估算AP数据保存
							this.indoorMapActivity.startThreadToReckonSignalData(ServiceClientInterface.ID_ACTION_IdealApCollection,xi,yi,signalLevel.substring(1));
						}
						
					}
					if (slist.size() > 0) {
						im_overlayer.setDialog(getXOnView(xi), getYOnView(yi), slist);
					} else {
						im_overlayer.clrDialog();
					}
				} else {
					im_overlayer.clrDialog();
				}
			}
			break;
		case LAYER_ID_IDEALSIGNAL:
			if (!im_idealsignallayer.handleOnClick(xi, yi)) {
				if (!im_overlayer.isInDialog(xi, yi)) {
					List<BasicNameValuePair> slist = new ArrayList<BasicNameValuePair>();
					Float[] array_signal = getIdealSignal(xi, yi);
					if (array_signal != null) {
						int i = 0;
						StringBuffer signalLevel = new StringBuffer("");
						while (i < array_signal.length) {
							if (array_signal[i] != null) {
								ApInfo apinfo = apinfoList.get(i);
								slist.add(new BasicNameValuePair(
										apinfo.EQUT_SSID, "" + array_signal[i]));
								if(array_signal[i]!=null){
									signalLevel.append(","+apinfo.EQUT_SSID+":"+array_signal[i]);
								}
							}
							++i;
						}
						if(signalLevel.length()>0){
							//估算AP数据保存
							this.indoorMapActivity.startThreadToReckonSignalData(ServiceClientInterface.ID_ACTION_IdealApCollection,xi,yi,signalLevel.substring(1));
						}
					}
					if (slist.size() > 0) {
						im_overlayer.setDialog(getXOnView(xi), getYOnView(yi), slist);
					} else {
						im_overlayer.clrDialog();
					}
				} else {
					im_overlayer.clrDialog();
				}
			}
			break;
		default: {
			SvgElement HightlightedsvgElem = null;
			if (HightlightedsvgLayer != null) {
				HightlightedsvgElem = PlaneGraphLayerList.get(
						HightlightedsvgLayer).getHightLight();
			}
			if (HightlightedsvgElem != svgElem) {
				if (HightlightedsvgElem != null) {
					if (HightlightedsvgLayer != layertype) {
						PlaneGraphLayerList.get(HightlightedsvgLayer)
								.clrHightLight();
					}
				}
				HightlightedsvgLayer = layertype;
				PlaneGraphLayerList.get(HightlightedsvgLayer).setHightLight(
						svgElem);
			}
			List<BasicNameValuePair> slist = new ArrayList<BasicNameValuePair>();
			slist.add(new BasicNameValuePair("名称", "取不到哦"));
			slist.add(new BasicNameValuePair("SvgId", svgElem.getAttr().get(
					"id")));
			slist.add(new BasicNameValuePair("所属图层", SvgLayer
					.getTypeName(layertype)));
			if (slist.size() > 0) {
				im_overlayer.setDialog(getXOnView(xi), getYOnView(yi), slist);
			}
		}
		}
	}

	/**
	 * 
	 * @return x为距离（米），y为信号强度（dBm）
	 */
	private Point2D getCalcSingal(float poix, float poiy, float x, float y,
			ElectromagneticWave emw) {
		if (svgstruct != null) {
			MathUtil mathUtil = new MathUtil();
			List<RectF> plist = svgstruct.getWallInfoForTwoPoint(poix, poiy, x,
					y);
			Point2D currentsignal = new Point2D();
			currentsignal.x = 0;// 当前信号距离（ 米)
			currentsignal.y = emw.getPower();// 当前信号强度
			float w = 0;
			if (plist != null) {
				for (RectF rectf : plist) {
					w = (float) mathUtil.getDistanceForTwoPoint(rectf.left,
							rectf.top, rectf.right, rectf.bottom);
					if (w > 0) {
						double d1 = mathUtil.getDistanceForTwoPoint(rectf.left,
								rectf.top, poix, poiy) * SvgStruct.scale;
						double s1 = emw.getSignalViaRadius(currentsignal.x,
								currentsignal.y, d1);
						if (Math.abs((d1 + (w * SvgStruct.scale))
								- (mathUtil.getDistanceForTwoPoint(rectf.right,
										rectf.bottom, poix, poiy) * SvgStruct.scale)) > 0.1) {
						}
						double afterloss = s1 - (w / 2) * emw.getWallLoss();
						currentsignal.x = d1 + (w * SvgStruct.scale);
						currentsignal.y = afterloss;
					}
				}
			}
			double d = mathUtil.getDistanceForTwoPoint(poix, poiy, x, y)
					* SvgStruct.scale;
			if (currentsignal.x < d) {
				currentsignal.y = emw.getSignalViaRadius(currentsignal.x,
						currentsignal.y, d);
				currentsignal.x = d;
			}
			return (currentsignal);
		}
		return (null);
	}

	private Point2D[] getCalcSignalPoints(double apx, double apy, double pd,
			ElectromagneticWave emw) {
		if (svgstruct != null) {
			if (pd > emw.getPower()) {
				pd = emw.getPower();
			}
			final double r = emw.getRadiusViaSignal(0, emw.getPower(), pd)
					/ SvgStruct.scale;
			MathUtil mathUtil = new MathUtil();
			Point2D currentsignal = new Point2D();
			// final double stepAngrad = Math.toRadians(1);
			final double stepAngrad = 4 / r;
			final double startAngrad = Math.toRadians(0);
			final double endAngrad = Math.toRadians(360) + stepAngrad; // +stepAngrad后计算包括endAngrad
			Point2D[] signalpoints = new Point2D[(int) Math
					.ceil((endAngrad - startAngrad) / stepAngrad)];
			double angrad = 0;
			int i = 0;
			while (i < signalpoints.length) {
				angrad = startAngrad + (i * stepAngrad);
				currentsignal.x = 0;// 当前信号距离（ 米)
				currentsignal.y = emw.getPower();// 当前信号强度
				Point2D ppp = mathUtil.getCirclePoint(apx, apy, r, angrad);
				double rx = ppp.x;
				double ry = ppp.y;
				List<RectF> plist = svgstruct.getWallInfoForTwoPoint(
						(float) apx, (float) apy, (float) rx, (float) ry);
				float wallthickness = 0;// 墙壁厚度（px）
				if (plist != null) {
					for (RectF rectf : plist) {
						wallthickness = (float) mathUtil
								.getDistanceForTwoPoint(rectf.left, rectf.top,
										rectf.right, rectf.bottom);
						if (wallthickness > 0) {
							/**
							 * left,top为靠近POI的墙边
							 */
							double d1 = mathUtil.getDistanceForTwoPoint(
									rectf.left, rectf.top, apx, apy)
									* SvgStruct.scale;
							double s1 = emw.getSignalViaRadius(currentsignal.x,
									currentsignal.y, d1);
							if (s1 > pd) {// 没过墙时信号大于要求的
								double afterloss = s1
										- ((wallthickness / 2) * emw
												.getWallLoss());
								if (afterloss > pd) {
									currentsignal.x = d1
											+ (wallthickness * SvgStruct.scale);
									currentsignal.y = afterloss;
								} else {
									if (afterloss < pd) {
										double wt = (((double) wallthickness * SvgStruct.scale) / (1 + ((pd - afterloss) / (s1 - pd))));
										currentsignal.x = d1 + wt;
										currentsignal.y = pd;
									} else {
										currentsignal.x = d1
												+ (wallthickness * SvgStruct.scale);
										currentsignal.y = afterloss;
									}
									break;
								}
							} else {
								if (s1 < pd) {
									currentsignal.x = emw.getRadiusViaSignal(
											currentsignal.x, currentsignal.y,
											pd);
									currentsignal.y = pd;
								} else {
									currentsignal.x = d1;
									currentsignal.y = s1;
								}
								break;
							}
						}
					}
				}
				double rr = emw.getRadiusViaSignal(currentsignal.x,
						currentsignal.y, pd) / SvgStruct.scale;
				signalpoints[i] = mathUtil.getCirclePoint(apx, apy, rr, angrad);
				++i;
			}
			return (signalpoints);
		}
		return (null);
	}

	private Path getCalcSignalPath(double apx, double apy, double pd,
			ElectromagneticWave emw) {
		Point2D[] signalpoints = getCalcSignalPoints(apx, apy, pd, emw);
		if ((signalpoints != null) && (signalpoints.length > 0)) {
			Path path = new Path();
			path.moveTo((float) apx, (float) apy);
			int i = 0;
			while (i < signalpoints.length) {
				path.lineTo((float) signalpoints[i].x,
						(float) signalpoints[i].y);
				++i;
			}
			path.close();
			return (path);
		}
		return (null);
	}

	private Path getIdealSignalPath(float apx, float apy, float signal,
			ElectromagneticWave emw) {
		float apr = (float) (emw.getRadiusViaSignal(signal) / SvgStruct.scale);
		return (SvgElementCircle.getPath(apx, apy, apr));
	}

	private List<SignalDistribution> getApSignalDistributionList() {
		if (apinfoList != null) {
			List<SignalDistribution> signaldistributionlist = new ArrayList<SignalDistribution>();
			MathUtil mathUtil = new MathUtil();
			ApInfo apinfo = null;
			SignalDistribution signaldistribution = null;
			double d = 0d;
			int i = 0;
			int j = 0;
			int count = apinfoList.size();
			while (i < count) {
				apinfo = apinfoList.get(i);
				if (apinfo != null) {
					j = 0;
					int countj = signaldistributionlist.size();
					while (j < countj) {
						signaldistribution = signaldistributionlist.get(j);
						d = mathUtil.getDistanceForTwoPoint(apinfo.POSITION_X,
								apinfo.POSITION_Y,
								signaldistribution.POSITION_X,
								signaldistribution.POSITION_Y);
						if (d * SvgStruct.scale < 0.5) {
							break;
						}
						++j;
					}
					if (j == countj) {
						signaldistributionlist.add(apinfo);
					}
				}
				++i;
			}
			return (signaldistributionlist);
		}
		return (null);
	}

	/**
	 * 设置平面图Matrix事件
	 */
	@Override
	public void onSetImageMatrix(Matrix matrix) {
		setImageMatrix(matrix);
	}

	/**
	 * 点击在平面图上
	 */
	@Override
	public void onClickImage(float xi, float yi) {
		//im_overlayer.handleOnClick(getXOnView(xi), getYOnView(yi));
		Log.d("operType", operType);
		this.paramsMap.put("x", xi);
		this.paramsMap.put("y", yi);
		if(operType.equals("1")){
			this.indoorMapActivity.postPointSignal(this.paramsMap);
			Toast.makeText(getContext(), "点击位置:"+"X:"+xi+",Y:"+yi,Toast.LENGTH_SHORT).show();
		}else{
			this.indoorMapActivity.gotoViewSignalDataActivity(paramsMap);
		}
		if (im_bsreckonsignallayer.getVisibility() == View.VISIBLE) {
			onClickLayer(xi, yi, LAYER_ID_BSRECKONSIGNAL, null);
		} else {
			if (im_bsidealsignallayer.getVisibility() == View.VISIBLE) {
				onClickLayer(xi, yi, LAYER_ID_BSIDEALSIGNAL, null);
			} else {
				if (im_reckonsignallayer.getVisibility() == View.VISIBLE) {
					onClickLayer(xi, yi, LAYER_ID_RECKONSIGNAL, null);
				} else {
					if (im_idealsignallayer.getVisibility() == View.VISIBLE) {
						onClickLayer(xi, yi, LAYER_ID_IDEALSIGNAL, null);
					} else {
						Integer onClickedLayertype = null;
						boolean onClickedDialog = false;
						if (svgstruct != null) {
							int j = LAYER_ID_LIST.length - 1;
							/**
							 * 不要外墙?
							 */
							while ((j > 0) && (onClickedLayertype == null)) {
								int layertype = LAYER_ID_LIST[j];
								ImageView iv_layer = PlaneGraphLayerList
										.get(layertype);
								if ((iv_layer.getDrawable() != null)
										&& (iv_layer.getVisibility() == View.VISIBLE)) {
									if (!im_overlayer.isInDialog(xi, yi)) {
										SvgLayer[] svglayers = svgstruct
												.getSvgLayers();
										SvgLayer svglayer = null;
										int i = 0;
										while (i < svglayers.length) {
											if (svglayers[i].getSvgLayerType() == layertype) {
												svglayer = svglayers[i];
											}
											++i;
										}
										if (svglayer != null) {
											SvgElement[] svgElems = svglayer
													.getSvgElements();
											i = svgElems.length - 1;
											while (i >= 0) {
												if (svgElems[i].getTypeId() == SvgElement.TYPE_ID_path) {
													Region r = svgElems[i]
															.getRegion();
													if ((r != null)
															&& r.contains(
																	(int) xi,
																	(int) yi)) {
														onClickLayer(xi, yi,
																layertype,
																svgElems[i]);
														onClickedLayertype = layertype;
														break;
													}
												}
												--i;
											}
										}
									} else {
										onClickedDialog = true;
										onClickDialog();
									}
								}
								--j;
							}
						}
						if ((onClickedLayertype == null) && !onClickedDialog) {
							if (HightlightedsvgLayer != null) {
								PlaneGraphLayerList.get(HightlightedsvgLayer)
										.clrHightLight();
								HightlightedsvgLayer = null;
								im_overlayer.clrDialog();
							}
						}
					}
				}
			}
		}
	}

	
	
	public boolean clrDialog() {
		if (im_overlayer.isShowingDialog()) {
			im_overlayer.clrDialog();
			return (true);
		}
		return (false);
	}

	/**
	 * 设置定位坐标
	 */
	public void setLocation(Float x, Float y) {
		if ((x != null) && (y != null)) {
			setCenter(x, y);
			im_overlayer.setLocation(getXOnView(x),getYOnView(y));
		}
	}

	/**
	 * 清除定位坐标
	 */
	public void clrLocation() {
		im_overlayer.clrLocation();
	}

	private final int WHAT_GENSIGNALDISTRIBUTION_WIFI_IDEAL = 10;
	private final int WHAT_GENSIGNALDISTRIBUTION_WIFI_RECKON = 11;
	private final int WHAT_GENSIGNALDISTRIBUTION_ANTS_IDEAL = 12;
	private final int WHAT_GENSIGNALDISTRIBUTION_ANTS_RECKON = 13;
	private genSignalDistributionThread genSignalDistributionThreadWifiIdeal = null;
	private genSignalDistributionThread genSignalDistributionThreadWifiReckon = null;
	private genSignalDistributionThread genSignalDistributionThreadAntsIdeal = null;
	private genSignalDistributionThread genSignalDistributionThreadAntsReckon = null;

	private Handler handler = new Handler(Looper.getMainLooper()) {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case WHAT_GENSIGNALDISTRIBUTION_WIFI_IDEAL:
				if ((msg.arg2 == 1) && (msg.obj != null)) {
					im_idealsignallayer
							.setSignalDistribution(
									svgstruct.getCeilWidth(),
									svgstruct.getCeilHeight(),
									(HashMap<String, List<SignalDistribution>>) msg.obj,
									true, false);
				} else {
					setLayerVisibility(LAYER_ID_IDEALSIGNAL, View.INVISIBLE);
				}
				genSignalDistributionThreadWifiIdeal = null;
				pd.dismiss(WHAT_GENSIGNALDISTRIBUTION_WIFI_IDEAL);
				break;
			case WHAT_GENSIGNALDISTRIBUTION_WIFI_RECKON:
				if ((msg.arg2 == 1) && (msg.obj != null)) {
					im_reckonsignallayer
							.setSignalDistribution(
									svgstruct.getCeilWidth(),
									svgstruct.getCeilHeight(),
									(HashMap<String, List<SignalDistribution>>) msg.obj,
									false, false);
				} else {
					setLayerVisibility(LAYER_ID_RECKONSIGNAL, View.INVISIBLE);
				}
				genSignalDistributionThreadWifiReckon = null;
				pd.dismiss(WHAT_GENSIGNALDISTRIBUTION_WIFI_RECKON);
				break;
			case WHAT_GENSIGNALDISTRIBUTION_ANTS_IDEAL:
				if ((msg.arg2 == 1) && (msg.obj != null)) {
					im_bsidealsignallayer
							.setSignalDistribution(
									svgstruct.getCeilWidth(),
									svgstruct.getCeilHeight(),
									(HashMap<String, List<SignalDistribution>>) msg.obj,
									true, true);
				} else {
					setLayerVisibility(LAYER_ID_BSIDEALSIGNAL, View.INVISIBLE);
				}
				genSignalDistributionThreadAntsIdeal = null;
				pd.dismiss(WHAT_GENSIGNALDISTRIBUTION_ANTS_IDEAL);
				break;
			case WHAT_GENSIGNALDISTRIBUTION_ANTS_RECKON:
				if ((msg.arg2 == 1) && (msg.obj != null)) {
					im_bsreckonsignallayer
							.setSignalDistribution(
									svgstruct.getCeilWidth(),
									svgstruct.getCeilHeight(),
									(HashMap<String, List<SignalDistribution>>) msg.obj,
									false, true);
				} else {
					setLayerVisibility(LAYER_ID_BSRECKONSIGNAL, View.INVISIBLE);
				}
				genSignalDistributionThreadAntsReckon = null;
				pd.dismiss(WHAT_GENSIGNALDISTRIBUTION_ANTS_RECKON);
				break;
			}
		}
	};

	private void genSignalDistribution(int whatID,
			List<SignalDistribution> signaldistributionlist) {
		if (signaldistributionlist != null) {
			int signaldistributionlistsize = signaldistributionlist.size();
			if (signaldistributionlistsize > 0) {
				Rect svgrect = new Rect(0, 0, svgstruct.getCeilWidth(),
						svgstruct.getCeilHeight());
				ElectromagneticWave emw = new ElectromagneticWave();
				SignalDistribution signaldistribution = null;
				Path path = null;
				int i = 0;
				while (i < signaldistributionlistsize) {
					Region weekRegion = new Region(svgrect);
					SparseArray<Path> signalleveldistribution = new SparseArray<Path>();
					signaldistribution = signaldistributionlist.get(i);
					int signalstep = ElectromagneticWave.STEP_Strong;
					int signal = ElectromagneticWave.SIGNAL_Strong;
					while (signal > ElectromagneticWave.SIGNAL_Weak) {
						if (signal < ElectromagneticWave.SIGNAL_Moderate) {
							signalstep = ElectromagneticWave.STEP_Middle;
						}
						emw.setPower(signaldistribution.POWER);
						emw.setFrequency(signaldistribution.FREQUENCY);
						switch (whatID) {
						case WHAT_GENSIGNALDISTRIBUTION_WIFI_IDEAL:
						case WHAT_GENSIGNALDISTRIBUTION_ANTS_IDEAL: {
							path = getIdealSignalPath(
									signaldistribution.POSITION_X,
									signaldistribution.POSITION_Y, signal, emw);
						}
							break;
						case WHAT_GENSIGNALDISTRIBUTION_WIFI_RECKON:
						case WHAT_GENSIGNALDISTRIBUTION_ANTS_RECKON: {
							path = getCalcSignalPath(
									signaldistribution.POSITION_X,
									signaldistribution.POSITION_Y, signal, emw);
						}
							break;
						}
						switch (whatID) {
						case WHAT_GENSIGNALDISTRIBUTION_ANTS_IDEAL:
						case WHAT_GENSIGNALDISTRIBUTION_ANTS_RECKON: {
							Region pathRegion = new Region();
							pathRegion.setPath(path, weekRegion);
							weekRegion.op(pathRegion, Op.DIFFERENCE);
							path = pathRegion.getBoundaryPath();
						}
							break;
						}
						signalleveldistribution.put(signal, path);
						signal = signal - signalstep;
					}
					switch (whatID) {
					case WHAT_GENSIGNALDISTRIBUTION_ANTS_IDEAL:
					case WHAT_GENSIGNALDISTRIBUTION_ANTS_RECKON: {
						signalleveldistribution.put(
								ElectromagneticWave.SIGNAL_Weak,
								weekRegion.getBoundaryPath());
					}
						break;
					}
					switch (whatID) {
					case WHAT_GENSIGNALDISTRIBUTION_WIFI_IDEAL:
					case WHAT_GENSIGNALDISTRIBUTION_ANTS_IDEAL: {
						signaldistribution.IdealSignalDistribution = signalleveldistribution;
					}
						break;
					case WHAT_GENSIGNALDISTRIBUTION_WIFI_RECKON:
					case WHAT_GENSIGNALDISTRIBUTION_ANTS_RECKON: {
						signaldistribution.ReckonSignalDistribution = signalleveldistribution;
					}
						break;
					}
					++i;
				}
			}
		}
	}

	private HashMap<String, List<SignalDistribution>> genSignalDistribution(
			int whatID) {
		if (svgstruct != null) {
			HashMap<String, List<SignalDistribution>> signaldistributionlistmap = null;
			switch (whatID) {
			case WHAT_GENSIGNALDISTRIBUTION_WIFI_IDEAL:
			case WHAT_GENSIGNALDISTRIBUTION_WIFI_RECKON: {
				List<SignalDistribution> signaldistributionlist = getApSignalDistributionList();
				if (signaldistributionlist != null) {
					signaldistributionlistmap = new HashMap<String, List<SignalDistribution>>();
					signaldistributionlistmap.put("ap", signaldistributionlist);
				}
			}
				break;
			case WHAT_GENSIGNALDISTRIBUTION_ANTS_IDEAL:
			case WHAT_GENSIGNALDISTRIBUTION_ANTS_RECKON: {
				if (poiantinfoList != null) {
					signaldistributionlistmap = splitToSignalDistributionListMap(poiantinfoList);
				}
			}
				break;
			}
			if (signaldistributionlistmap != null) {
				Iterator<String> iterator = signaldistributionlistmap.keySet()
						.iterator();
				while (iterator.hasNext()) {
					genSignalDistribution(whatID,
							signaldistributionlistmap.get(iterator.next()));
				}
				return (signaldistributionlistmap);
			}
		}
		return (null);
	}

	private HashMap<String, List<SignalDistribution>> splitToSignalDistributionListMap(
			List<PoiInfo> poiantinfoList) {
		if (poiantinfoList != null) {
			HashMap<String, List<SignalDistribution>> laccidmap = new HashMap<String, List<SignalDistribution>>();
			List<SignalDistribution> list = null;
			PoiInfo poiInfo = null;
			String laccid = "poi";
			int i = 0;
			int count = poiantinfoList.size();
			while (i < count) {
				poiInfo = poiantinfoList.get(i);
				// laccid = ("" + poiInfo.ANT_LAC) + poiInfo.ANT_CID;
				list = laccidmap.get(laccid);
				if (list == null) {
					list = new ArrayList<SignalDistribution>();
					laccidmap.put(laccid, list);
				}
				list.add(poiInfo);
				++i;
			}
			return (laccidmap);
		}
		return (null);
	}

	private class genSignalDistributionThread extends Thread {
		private Integer whatID = null;

		public genSignalDistributionThread(int whatID) {
			this.whatID = whatID;
		}

		@Override
		public void run() {
			Message msg = handler.obtainMessage();
			msg.what = whatID;
			try {
				msg.obj = genSignalDistribution(whatID);
				msg.arg2 = 1;
			} catch (Exception e) {
				msg.obj = e.getMessage();
				msg.arg2 = 0;
			}
			handler.sendMessage(msg);
		}
	}

	public void setApInfoList(List<ApInfo> apinfolist) {
		apinfoList = apinfolist;
		if (apinfoList != null) {
			if ((im_idealsignallayer.getVisibility() == View.VISIBLE)
					&& (im_idealsignallayer.getDrawable() == null)
					&& (genSignalDistributionThreadWifiIdeal == null)) {
				pd.show(WHAT_GENSIGNALDISTRIBUTION_WIFI_IDEAL);
				genSignalDistributionThreadWifiIdeal = new genSignalDistributionThread(
						WHAT_GENSIGNALDISTRIBUTION_WIFI_IDEAL);
				genSignalDistributionThreadWifiIdeal.start();
			}
			if ((im_reckonsignallayer.getVisibility() == View.VISIBLE)
					&& (im_reckonsignallayer.getDrawable() == null)
					&& (genSignalDistributionThreadWifiReckon == null)) {
				pd.show(WHAT_GENSIGNALDISTRIBUTION_WIFI_RECKON);
				genSignalDistributionThreadWifiReckon = new genSignalDistributionThread(
						WHAT_GENSIGNALDISTRIBUTION_WIFI_RECKON);
				genSignalDistributionThreadWifiReckon.start();
			}
		}
	}

	public void setPoiAntList(List<PoiInfo> poiantinfoList) {
		this.poiantinfoList = poiantinfoList;
		if (poiantinfoList != null) {
			if ((im_bsidealsignallayer.getVisibility() == View.VISIBLE)
					&& (im_bsidealsignallayer.getDrawable() == null)
					&& (genSignalDistributionThreadAntsIdeal == null)) {
				pd.show(WHAT_GENSIGNALDISTRIBUTION_ANTS_IDEAL);
				genSignalDistributionThreadAntsIdeal = new genSignalDistributionThread(
						WHAT_GENSIGNALDISTRIBUTION_ANTS_IDEAL);
				genSignalDistributionThreadAntsIdeal.start();
			}
			if ((im_bsreckonsignallayer.getVisibility() == View.VISIBLE)
					&& (im_bsreckonsignallayer.getDrawable() == null)
					&& (genSignalDistributionThreadAntsReckon == null)) {
				pd.show(WHAT_GENSIGNALDISTRIBUTION_ANTS_RECKON);
				genSignalDistributionThreadAntsReckon = new genSignalDistributionThread(
						WHAT_GENSIGNALDISTRIBUTION_ANTS_RECKON);
				genSignalDistributionThreadAntsReckon.start();
			}
		}
	}

	public void clrApList() {
		if (genSignalDistributionThreadWifiIdeal != null) {
			genSignalDistributionThreadWifiIdeal.stop();
		}
		if (genSignalDistributionThreadWifiReckon != null) {
			genSignalDistributionThreadWifiReckon.stop();
		}
		apinfoList = null;
		im_idealsignallayer.clrSignalDistribution();
		setLayerVisibility(LAYER_ID_IDEALSIGNAL, View.INVISIBLE);
		im_idealsignallayer.setImageDrawable(null);
		im_reckonsignallayer.clrSignalDistribution();
		setLayerVisibility(LAYER_ID_RECKONSIGNAL, View.INVISIBLE);
		im_reckonsignallayer.setImageDrawable(null);
	}

	public boolean hasApList() {
		return (apinfoList != null);
	}

	public void clrPoiAntList() {
		if (genSignalDistributionThreadAntsIdeal != null) {
			genSignalDistributionThreadAntsIdeal.stop();
		}
		if (genSignalDistributionThreadAntsReckon != null) {
			genSignalDistributionThreadAntsReckon.stop();
		}
		im_bsidealsignallayer.clrSignalDistribution();
		setLayerVisibility(LAYER_ID_BSIDEALSIGNAL, View.INVISIBLE);
		im_bsidealsignallayer.setImageDrawable(null);
		im_bsreckonsignallayer.clrSignalDistribution();
		setLayerVisibility(LAYER_ID_BSRECKONSIGNAL, View.INVISIBLE);
		im_bsreckonsignallayer.setImageDrawable(null);
		poiantinfoList = null;
	}

	public boolean hasPoiAntList() {
		return (poiantinfoList != null);
	}

	/**
	 * 设置指定图层的是否可见
	 */
	public void setLayerVisibility(int layer, int visibility) {
		switch (layer) {
		case LAYER_ID_BACKGROUND:
			im_bglayer.setVisibility(visibility);
			break;
		case LAYER_ID_OVERLAYER:
			im_overlayer.setVisibility(visibility);
			break;
		case LAYER_ID_IDEALSIGNAL:
			im_idealsignallayer.setVisibility(visibility);
			im_overlayer.clrDialog();
			if ((visibility == View.VISIBLE)
					&& (im_idealsignallayer.getDrawable() == null)) {
				setApInfoList(apinfoList);
			}
			break;
		case LAYER_ID_RECKONSIGNAL:
			im_reckonsignallayer.setVisibility(visibility);
			im_overlayer.clrDialog();
			if ((visibility == View.VISIBLE)
					&& (im_reckonsignallayer.getDrawable() == null)) {
				setApInfoList(apinfoList);
			}
			break;
		case LAYER_ID_BSIDEALSIGNAL:
			im_bsidealsignallayer.setVisibility(visibility);
			im_overlayer.clrDialog();
			if ((visibility == View.VISIBLE)
					&& (im_bsidealsignallayer.getDrawable() == null)) {
				setPoiAntList(poiantinfoList);
			}
			break;
		case LAYER_ID_BSRECKONSIGNAL:
			im_bsreckonsignallayer.setVisibility(visibility);
			im_overlayer.clrDialog();
			if ((visibility == View.VISIBLE)
					&& (im_bsreckonsignallayer.getDrawable() == null)) {
				setPoiAntList(poiantinfoList);
			}
			break;
		default:
			PlaneGraphLayerList.get(layer).setVisibility(visibility);
		}
		layervisibilitystatus.put(layer, visibility);
	}

	public void clrPlaneGraph() {
		im_overlayer.clrLocation();
		im_overlayer.clrDialog();
		for (int layer : LAYER_ID_LIST) {
			PlaneGraphLayerList.get(layer).clrSvgLayer();
			// setLayerVisibility(layer, View.VISIBLE);
		}
		clrApList();
		clrPoiAntList();
		this.svgstruct = null;
	}

	public void setPlaneGraph(SvgStruct svgstruct) {
		clrPlaneGraph();
		this.svgstruct = svgstruct;
		for (int layer : LAYER_ID_LIST) {
			PlaneGraphLayerList.get(layer).setSvgLayer(
					svgstruct.getCeilWidth(), svgstruct.getCeilHeight(),
					svgstruct.getSvgLayer(layer));
		}
		setPlaneGraphSize(svgstruct.getCeilWidth(), svgstruct.getCeilHeight());
	}

	public SvgStruct getSvgStruct() {
		return (svgstruct);
	}

	public SparseIntArray getLayerVisibilityStatus() {
		return (layervisibilitystatus);
	}

	// private ApInfo getApInfo(String mac) {
	// if ((mac != null) && (apinfoList != null)) {
	// ApInfo apinfo = null;
	// int i = 0;
	// while (i < apinfoList.size()) {
	// apinfo = apinfoList.get(i);
	// if (mac.equals(apinfo.MAC_BSSID)) {
	// return (apinfo);
	// }
	// ++i;
	// }
	// }
	// return (null);
	// }
	//
	// private Path getApSignalPath(String mac, float signal) {
	// ApInfo apinfo = getApInfo(mac);
	// if (apinfo != null) {
	// Rect svgclip = new Rect(0, 0,
	// (int) Math.ceil(svgstruct.getWidth()),
	// (int) Math.ceil(svgstruct.getHeight()));
	// ElectromagneticWave emw = new ElectromagneticWave();
	// emw.setFrequency(apinfo.FREQUENCY);
	// emw.setPower(apinfo.POWER);
	// Region r1 = null;
	// {
	// Point2D[] signalpoints1 = getCalcSignalPoints(
	// apinfo.POSITION_X, apinfo.POSITION_Y, signal - 15, emw);
	// Path path = new Path();
	// if ((signalpoints1 != null) && (signalpoints1.length > 0)) {
	// path.moveTo((float) signalpoints1[0].x,
	// (float) signalpoints1[0].y);
	// int i = 1;
	// while (i < signalpoints1.length) {
	// path.lineTo((float) signalpoints1[i].x,
	// (float) signalpoints1[i].y);
	// ++i;
	// }
	// }
	// path.close();
	// r1 = utils.getPathRegion(path, svgclip);
	// }
	// Region r2 = null;
	// {
	// Point2D[] signalpoints2 = getCalcSignalPoints(
	// apinfo.POSITION_X, apinfo.POSITION_Y, signal + 15, emw);
	// Path path = new Path();
	// if ((signalpoints2 != null) && (signalpoints2.length > 0)) {
	// path.moveTo((float) signalpoints2[0].x,
	// (float) signalpoints2[0].y);
	// int i = 1;
	// while (i < signalpoints2.length) {
	// path.lineTo((float) signalpoints2[i].x,
	// (float) signalpoints2[i].y);
	// ++i;
	// }
	// }
	// path.close();
	// r2 = utils.getPathRegion(path, svgclip);
	// }
	// if ((r1 != null) && !r1.isEmpty()) {
	// if ((r2 != null) && !r2.isEmpty()) {
	// try {
	// r1.op(r2, Op.DIFFERENCE);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// if (!r1.isEmpty()) {
	// Path p = r1.getBoundaryPath();
	// return (p);
	// }
	// }
	// }
	// }
	// return (null);
	// }
	//
	// public void location(HashMap<String, Float> macsignal) {
	// if (macsignal != null) {
	// Rect svgclip = new Rect(0, 0,
	// (int) Math.ceil(svgstruct.getWidth()),
	// (int) Math.ceil(svgstruct.getHeight()));
	// Iterator<String> iterator = macsignal.keySet().iterator();
	// List<List<Path>> appathlist = new ArrayList<List<Path>>();
	// List<Path> strokepathList = new ArrayList<Path>();
	// List<Path> fillpathList = new ArrayList<Path>();
	// Region region1 = null;
	// while (iterator.hasNext()) {
	// String mac = iterator.next();
	// if (macsignal.get(mac) != null) {
	// if (macsignal.get(mac) < -90) {
	// break;
	// }
	// Path path = getApSignalPath(mac, macsignal.get(mac));
	// if (path != null) {
	// strokepathList.add(path);
	// Region region = utils.getPathRegion(path, svgclip);
	// if (region1 == null) {
	// region1 = region;
	// } else {
	// Rect rect = new Rect(0, 0,
	// (int) svgstruct.getWidth(),
	// (int) svgstruct.getHeight());
	// Region r3 = new Region(rect);
	// r3.op(region, Op.INTERSECT);
	// region1 = r3;
	// }
	// }
	// }
	// }
	// if (region1 != null) {
	// Region pass = new Region(0, 0, (int) svgstruct.getWidth(),
	// (int) svgstruct.getHeight());
	// Region r3 = new Region(region1);
	// r3.op(pass, Op.INTERSECT);
	// region1 = r3;
	// fillpathList.add(region1.getBoundaryPath());
	// RegionIterator regionIterator = new RegionIterator(region1);
	// Rect r = new Rect();
	// Rect max = null;
	// while (regionIterator.next(r)) {
	// if (max == null) {
	// max = new Rect(r);
	// } else {
	// if ((r.height() * r.width()) > (max.height() * max
	// .width())) {
	// max.set(r);
	// }
	// }
	// }
	// if (max != null) {
	// fillpathList.add(SvgElementRect.getPath(max));
	// im_overlayer.setLocation((float) max.centerX(),
	// (float) max.centerY());
	// }
	// }
	// appathlist.add(strokepathList);
	// // im_overlayer.setPath(strokepathList, fillpathList);
	// }
	// }
}
