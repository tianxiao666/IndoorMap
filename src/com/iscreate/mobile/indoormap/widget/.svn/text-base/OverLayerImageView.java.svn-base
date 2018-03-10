package com.iscreate.mobile.indoormap.widget;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.iscreate.mobile.indoormap.R;
import com.iscreate.mobile.utils.utils;

public class OverLayerImageView extends CustomImageView {
	/**
	 * the x-coordinate of the current location
	 */
	private Float Location_X = null;
	/**
	 * the y-coordinate of the current location
	 */
	private Float Location_Y = null;
	/**
	 * the bitmap for current location icon
	 */
	private Bitmap Location_BM = null;
	/**
	 * 对话框在平面图上的坐标
	 */
	private Float DialogX = null;
	private Float DialogY = null;
	private DialogPath dialogPath = null;
	private List<Clicked> clickedlist = null;
	private Timer timer = null;
	private final int stageCount = 7;

	public OverLayerImageView(Context context) {
		super(context);
	}

	public OverLayerImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public OverLayerImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * clear the location coordinate if a new drawable is setting
	 */
	@Override
	public void setImageDrawable(Drawable drawable) {
		super.setImageDrawable(drawable);
		Location_X = null;
		Location_Y = null;
	}

	/**
	 * draw current location while onDraw occurred
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		DrawLocation(canvas);
		DrawDialog(canvas);
		DrawClickList(canvas);
	}

	/**
	 * set current location to (x,y)
	 * 
	 * @param x
	 *            the x-coordinate of the current location to be set
	 * @param y
	 *            the y-coordinate of the current location to be set
	 */
	public void setLocation(Float x, Float y) {
		Location_X = x;
		Location_Y = y;
		if ((Location_X != null) && (Location_Y != null)) {
			invalidate();
		}
	}

	public void clrLocation() {
		Location_X = null;
		Location_Y = null;
		invalidate();
	}

	private void calcDialogDerection() {
		if (isShowingDialog()) {
			int d = DialogPath.DERECTION_RightTop;
			float cx = getWidth() / 2;
			float cy = getHeight() / 2;
			float vx = getXOnView(DialogX);
			float vy = getYOnView(DialogY);
			if (vx < cx) {
				if (vy < cy) {
					// 左上
					d = DialogPath.DERECTION_RightBottom;
				} else {
					// 左下
					d = DialogPath.DERECTION_RightTop;
				}
			} else {
				if (vy < cy) {
					// 右上
					d = DialogPath.DERECTION_LeftBottom;
				} else {
					// 右下
					d = DialogPath.DERECTION_LeftTop;
				}
			}
			dialogPath.setDerection(d);
		}
	}

	/**
	 * @param xi
	 *            平面图上的x坐标
	 * @param yi
	 *            平面图上的y坐标
	 */
	public void setDialog(float xi, float yi, List<BasicNameValuePair> smap) {
		DialogX = xi;
		DialogY = yi;
		dialogPath = new DialogPath();
		dialogPath.setDialogText(smap, getWidth() / 2);
		calcDialogDerection();
		invalidate();
	}

	public void clrDialog() {
		DialogX = null;
		DialogY = null;
		dialogPath = null;
		invalidate();
	}

	public boolean isShowingDialog() {
		return ((DialogX != null) && (DialogY != null) && (dialogPath != null));
	}

	public boolean isInDialog(float xi, float yi) {
		if (isShowingDialog()) {
			return (dialogPath.isInDialog(getXOnView(xi), getYOnView(yi)));
		}
		return (false);
	}

	/**
	 * draw a cross
	 * 
	 * @param canvas
	 *            the canvas on which the background will be drawn
	 * @param x
	 *            the x-coordinate where to draw the cross
	 * @param y
	 *            the y-coordinate where to draw the cross
	 * @param radius
	 *            the radius from the externally tangent circle of this cross
	 */
	private void DrawCross(Canvas canvas, float x, float y, float radius) {
		Paint paint = new Paint();
		paint.setStrokeWidth(4);
		paint.setStyle(Style.FILL);
		paint.setColor(Color.BLACK);
		canvas.drawLine(x - radius, y, x + radius, y, paint);
		canvas.drawLine(x, y - radius, x, y + radius, paint);
	}

	/**
	 * draw current location (x,y)
	 * 
	 * @param canvas
	 *            the canvas on which the background will be drawn
	 * @param x
	 *            the x-coordinate where to draw current location
	 * @param y
	 *            the y-coordinate where to draw current location
	 */
	private void DrawLocationPic(Canvas canvas, float x, float y) {
		if (Location_BM == null) {
			Drawable drawable = getContext().getResources().getDrawable(
					R.drawable.icon_locations);
			Location_BM = utils.drawableToBitmap(drawable);
		}
		if (Location_BM != null) {
			Matrix matrix = new Matrix();
			matrix.postTranslate(x - Location_BM.getWidth() / 2, y
					- Location_BM.getHeight() / 2);
			canvas.drawBitmap(Location_BM, matrix, null);
		}
		DrawCross(canvas, getXOnView(Location_X), getYOnView(Location_Y), 10);
	}

	/**
	 * draw current location
	 * 
	 * @param canvas
	 *            the canvas on which the background will be drawn
	 */
	private void DrawLocation(Canvas canvas) {
		if (Location_X != null && Location_Y != null) {
			DrawLocationPic(canvas, getXOnView(Location_X),
					getYOnView(Location_Y));
		}
	}

	private void DrawDialog(Canvas canvas) {
		if (isShowingDialog()) {
			dialogPath.setDialogCoord(getXOnView(DialogX), getYOnView(DialogY));
			dialogPath.draw(canvas);
		}
	}

	private class Clicked {
		public int stage = 0;
		public float x = 0;
		public float y = 0;
	}

	private void DrawClick(Canvas canvas, float x, float y, int stage) {
		final int color = Color.MAGENTA;
		Paint paint = new Paint();
		paint.setStyle(Style.FILL);
		paint.setAntiAlias(true);
		int color0 = 0;
		int color1 = 0;
		int color2 = 0;
		int color3 = 0;
		switch (stage) {
		case 0:
		case 6:
			color0 = color & 0x00ffffff;
			color1 = color & 0x00ffffff;
			color2 = color & 0x00ffffff;
			color3 = color & 0x3Effffff;
			break;
		case 1:
		case 5:
			color0 = color & 0x00ffffff;
			color1 = color & 0x00ffffff;
			color2 = color & 0x3Effffff;
			color3 = color & 0x7Cffffff;
			break;
		case 2:
		case 4:
			color0 = color & 0x00ffffff;
			color1 = color & 0x3Effffff;
			color2 = color & 0x7Cffffff;
			color3 = color & 0xBAffffff;
			break;
		case 3:
			color0 = color & 0x3Effffff;
			color1 = color & 0x7Cffffff;
			color2 = color & 0xBAffffff;
			color3 = color & 0xF8ffffff;
			break;
		}
		paint.setColor(color0);
		canvas.drawCircle(getXOnView(x), getYOnView(y), 12, paint);
		paint.setColor(color1);
		canvas.drawCircle(getXOnView(x), getYOnView(y), 9, paint);
		paint.setColor(color2);
		canvas.drawCircle(getXOnView(x), getYOnView(y), 6, paint);
		paint.setColor(color3);
		canvas.drawCircle(getXOnView(x), getYOnView(y), 3, paint);
	}

	private void DrawClickList(Canvas canvas) {
		if ((clickedlist != null) && (clickedlist.size() > 0)) {
			Clicked clicked = null;
			int i = 0;
			int count = clickedlist.size();
			while (i < count) {
				clicked = clickedlist.get(i);
				DrawClick(canvas, clicked.x, clicked.y, clicked.stage);
				++i;
			}
		} else {
			if (timer != null) {
				timer.cancel();
				timer.purge();
				timer = null;
			}
		}
	}

	public void handleOnClick(float xi, float yi) {
		if (clickedlist == null) {
			clickedlist = new ArrayList<Clicked>();
		}
		Clicked clicked = new Clicked();
		clicked.x = xi;
		clicked.y = yi;
		clicked.stage = stageCount;
		clickedlist.add(clicked);
		if (timer == null) {
			timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					if (clickedlist != null) {
						Clicked clicked = null;
						int i = 0;
						int count = clickedlist.size();
						while (i < count) {
							clicked = clickedlist.get(i);
							if (clicked.stage > 0) {
								--clicked.stage;
								++i;
							} else {
								clickedlist.remove(i);
								--count;
							}
						}
					}
					postInvalidate();
				}
			}, 0, 300);
		}
	}
}