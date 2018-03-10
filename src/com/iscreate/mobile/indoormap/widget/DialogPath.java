package com.iscreate.mobile.indoormap.widget;

import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Region;

import com.iscreate.mobile.svg.SvgElement;
import com.iscreate.mobile.utils.utils;

public class DialogPath {
	/**
	 * 对话框位置
	 */
	public static final int DERECTION_LeftTop = 0;
	public static final int DERECTION_RightBottom = 1;
	public static final int DERECTION_LeftBottom = 2;
	public static final int DERECTION_RightTop = 3;
	private final float DialogTitleSize = 20f;
	private final float DialogTextSize = 20f;
	private final float DialogRadius = DialogTextSize / 2;
	private int derectionID = DERECTION_RightTop;
	private float DialogRX = DialogRadius;
	private float DialogRY = DialogRadius;
	/**
	 * 坐标及长宽
	 */
	private float DialogX = 0f;
	private float DialogY = 0f;
	private float DialogW = 270f;
	private float DialogH = 150f;
	/**
	 * 对话框形状path
	 */
	private Path DialogP = null;
	private List<BasicNameValuePair> DialogSMap = null;

	/**
	 * 圆角半径
	 */
	private float DialogArrowW = 10f;
	private float DialogArrowH = 50f;
	private float DialogStrokeWidth = 2f;

	/**
	 * 获取对话框path
	 */
	private Path getDialogPath() {
		Path p = new Path();
		float x = DialogX;
		float y = DialogY;
		float w = DialogW;
		float h = DialogH;
		float rx = DialogRX;
		float ry = DialogRY;
		if ((x + w - rx) < (x + rx)) {
			rx = w / 2;
			DialogRX = rx;
		}
		if ((y + h - ry) < (y + ry)) {
			ry = h / 2;
			DialogRX = ry;
		}
		float nw = w - 2 * rx;
		float nh = h - 2 * ry;
		float dcx = rx / SvgElement.RectPathNum;
		float dcy = ry / SvgElement.RectPathNum;
		float ncx = rx - dcx;
		float ncy = ry - dcy;
		p.moveTo(x, y);
		switch (derectionID) {
		case DERECTION_LeftTop:
		case DERECTION_RightTop: {
			if (derectionID == DERECTION_RightTop) {
				p.rLineTo(DialogArrowW, -DialogArrowH);
				p.rLineTo(-DialogArrowW, 0f);
			} else {
				p.rLineTo(-2 * DialogArrowW, -DialogArrowH);
				p.rLineTo(-(nw - 2 * DialogArrowW), 0f);
			}
			p.rCubicTo(-ncx, 0f, -rx, -dcy, -rx, -ry);
			p.rLineTo(0f, -nh);
			p.rCubicTo(0f, -ncy, dcx, -ry, rx, -ry);
			p.rLineTo(nw, 0f);
			p.rCubicTo(ncx, 0f, rx, dcy, rx, ry);
			p.rLineTo(0f, nh);
			p.rCubicTo(0f, ncy, -dcx, ry, -rx, ry);
			if (derectionID == DERECTION_RightTop) {
				p.rLineTo(-(nw - 2 * DialogArrowW), 0f);
			} else {
				p.rLineTo(-DialogArrowW, 0f);
			}
		}
			break;
		case DERECTION_RightBottom:
		case DERECTION_LeftBottom: {
			if (derectionID == DERECTION_LeftBottom) {
				p.rLineTo(-2 * DialogArrowW, DialogArrowH);
				p.rLineTo(-(nw - 2 * DialogArrowW), 0f);
			} else {
				p.rLineTo(DialogArrowW, DialogArrowH);
				p.rLineTo(-DialogArrowW, 0f);
			}
			p.rCubicTo(-ncx, 0f, -rx, dcy, -rx, ry);
			p.rLineTo(0f, nh);
			p.rCubicTo(0f, ncy, dcx, ry, rx, ry);
			p.rLineTo(nw, 0f);
			p.rCubicTo(ncx, 0f, rx, -dcy, rx, -ry);
			p.rLineTo(0f, -nh);
			p.rCubicTo(0f, -ncy, -dcx, -ry, -rx, -ry);
			if (derectionID == DERECTION_LeftBottom) {
				p.rLineTo(-DialogArrowW, 0f);
			} else {
				p.rLineTo(-(nw - 2 * DialogArrowW), 0f);
			}
		}
			break;
		}
		p.close();
		return (p);
	}

	public void setDerection(int id) {
		derectionID = id;
	}

	public int getDerection() {
		return (derectionID);
	}

	/**
	 * 在调用draw之前调用
	 */
	public void setDialogCoord(float x, float y) {
		DialogX = x;
		DialogY = y;
	}

	public void setDialogText(List<BasicNameValuePair> smap, int w) {
		DialogSMap = smap;
		DialogW = w;
		if (DialogW < DialogTitleSize) {
			DialogW = 270f;
		}
		calcDialogBounds();
	}

	private Paint getTitlePaint() {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStyle(Style.FILL);
		paint.setColor(Color.RED);
		paint.setTextSize(DialogTitleSize);
		return (paint);
	}

	private Paint getTextPaint() {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStyle(Style.FILL);
		paint.setColor(Color.BLUE);
		paint.setTextSize(DialogTextSize);
		return (paint);
	}

	private int paintMeasureText(Paint p, String s, float len) {
		String ss = null;
		float f = 0f;
		int slen = s.length();
		int end = slen;
		while (end > 0) {
			ss = s.substring(0, end);
			f = p.measureText(ss);
			if (f < len) {
				break;
			}
			--end;
		}
		return (end);
	}

	private void calcDialogBounds() {
		if (DialogSMap != null) {
			Paint painttitle = getTitlePaint();
			Paint painttext = getTextPaint();
			float textW = DialogW - DialogRX * 2;
			int linecount = 0;
			float lastlinelen = textW;
			String title = null;
			String text = null;
			int i = 0;
			int DialogSMaplen = DialogSMap.size();
			while (i < DialogSMaplen) {
				String key = DialogSMap.get(i).getName();
				title = key + "：";
				while (title.length() > 0) {
					++linecount;
					lastlinelen = painttitle.measureText(title);
					if (lastlinelen >= textW) {
						int end = paintMeasureText(painttitle, title, textW);
						title = title.substring(end);
					} else {
						title = "";
					}
				}
				float textlen = 0f;
				text = DialogSMap.get(i).getValue();
				if (text != null) {
					if ((textW - lastlinelen) > painttext.getTextSize()) {
						textlen = painttext.measureText(text);
						if (textlen >= (textW - lastlinelen)) {
							int end = paintMeasureText(painttext, text,
									(textW - lastlinelen));
							text = text.substring(end);
						} else {
							text = "";
						}
					}
					while (text.length() > 0) {
						++linecount;
						textlen = painttext.measureText(text);
						if (textlen >= textW) {
							int end = paintMeasureText(painttext, text, textW);
							text = text.substring(end);
						} else {
							text = "";
						}
					}
				}
				++i;
			}
			DialogH = (DialogTextSize * linecount) + (DialogRY * 2);
		}
	}

	/**
	 * 
	 * @param x
	 *            View上的x坐标
	 * @param y
	 *            View上的y坐标
	 */
	public boolean isInDialog(float x, float y) {
		if (DialogP != null) {
			Region r = utils.getPathRegion(DialogP);
			return (r.contains((int) x, (int) y));
		}
		return (false);
	}

	private void drawText(Canvas canvas) {
		float x = 0f;
		float y = 0f;
		switch (derectionID) {
		case DERECTION_LeftTop:
			x = DialogX - DialogW + DialogRX + DialogRX;// 0
			y = DialogY - DialogArrowH - DialogH + DialogRY + DialogTextSize;// 1
			break;
		case DERECTION_RightBottom:
			x = DialogX;// 2
			y = DialogY + DialogArrowH + DialogRY + DialogTextSize;// 3
			break;
		case DERECTION_LeftBottom:
			x = DialogX - DialogW + DialogRX + DialogRX;// 0
			y = DialogY + DialogArrowH + DialogRY + DialogTextSize;// 3
			break;
		case DERECTION_RightTop:
			x = DialogX;// 2
			y = DialogY - DialogArrowH - DialogH + DialogRY + DialogTextSize;// 1
			break;
		}
		Paint painttitle = getTitlePaint();
		Paint painttext = getTextPaint();
		float textW = DialogW - DialogRX * 2;
		int linecount = 0;
		float lastlinelen = textW;
		String title = null;
		String text = null;
		int i = 0;
		int DialogSMaplen = DialogSMap.size();
		while (i < DialogSMaplen) {
			String key = DialogSMap.get(i).getName();
			title = key + "：";
			while (title.length() > 0) {
				++linecount;
				lastlinelen = painttitle.measureText(title);
				if (lastlinelen >= textW) {
					int end = paintMeasureText(painttitle, title, textW);
					canvas.drawText(title.substring(0, end), x, y
							+ ((linecount - 1) * painttitle.getTextSize()),
							painttitle);
					title = title.substring(end);
				} else {
					canvas.drawText(title, x,
							y + ((linecount - 1) * painttitle.getTextSize()),
							painttitle);
					title = "";
				}
			}
			float textlen = 0f;
			text = DialogSMap.get(i).getValue();
			if (text != null) {
				if ((textW - lastlinelen) > painttext.getTextSize()) {
					textlen = painttext.measureText(text);
					if (textlen >= (textW - lastlinelen)) {
						int end = paintMeasureText(painttext, text,
								(textW - lastlinelen));
						canvas.drawText(
								text.substring(0, end),
								x + lastlinelen,
								y + ((linecount - 1) * painttext.getTextSize()),
								painttext);
						text = text.substring(end);
					} else {
						canvas.drawText(text, x + lastlinelen, y
								+ ((linecount - 1) * painttext.getTextSize()),
								painttext);
						text = "";
					}
				}
				while (text.length() > 0) {
					++linecount;
					textlen = painttext.measureText(text);
					if (textlen >= textW) {
						int end = paintMeasureText(painttext, text, textW);
						canvas.drawText(text.substring(0, end), x, y
								+ ((linecount - 1) * painttext.getTextSize()),
								painttext);
						text = text.substring(end);
					} else {
						canvas.drawText(text, x, y
								+ ((linecount - 1) * painttext.getTextSize()),
								painttext);
						text = "";
					}
				}
			}
			++i;
		}
	}

	/**
	 * 画对话框
	 */
	public void draw(Canvas canvas) {
		DialogP = getDialogPath();
		// DialogPath.toggleInverseFillType();
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStrokeWidth(DialogStrokeWidth);
		paint.setStyle(Style.STROKE);
		paint.setColor(Color.BLACK);
		canvas.drawPath(DialogP, paint);
		paint.setStyle(Style.FILL);
		paint.setColor(Color.WHITE);
		canvas.drawPath(DialogP, paint);
		drawText(canvas);
	}
}
