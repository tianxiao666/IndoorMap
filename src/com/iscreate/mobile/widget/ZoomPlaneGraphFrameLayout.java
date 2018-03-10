package com.iscreate.mobile.widget;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public abstract class ZoomPlaneGraphFrameLayout extends FrameLayout {
	/**
	 * this scale will be set if a double click occurred
	 */
	private final float ZOOM_DOUBLE_CLICK_SCALE = 2f;
	/**
	 * the max scale by two finger zoom
	 */
	private final float ZOOM_MAX_SCALE = 20f;
	/**
	 * the min scale by two finger zoom
	 */
	private final float ZOOM_MIN_SCALE = 0.2f;
	/**
	 * the init scale
	 */
	private final float INIT_SCALE = 1f;
	/**
	 * the Matrix of this ImageView
	 */
	private Matrix imgMatrix = null;
	/**
	 * current scale
	 */
	private float currentScale = INIT_SCALE;
	/**
	 * max translate value in x
	 */
	private float maxTranslateX = 0f;
	/**
	 * max translate value in y
	 */
	private float maxTranslateY = 0f;
	/**
	 * max value to translate right
	 */
	private float limitX1 = 0f;
	/**
	 * max value to translate left
	 */
	private float limitX2 = 0f;
	/**
	 * max value to translate down
	 */
	private float limitY1 = 0f;
	/**
	 * max value to translate up
	 */
	private float limitY2 = 0f;
	/**
	 * 平面图像素宽
	 */
	private float PlaneGraphWidth = 0;
	/**
	 * 平面图像素高
	 */
	private float PlaneGraphHeight = 0;
	private TouchEventHandler touchEventHandler = null;

	public abstract void onClickImage(float xi, float yi);

	public abstract void onSetImageMatrix(Matrix matrix);

	public ZoomPlaneGraphFrameLayout(Context context) {
		super(context);
		init();
	}

	public ZoomPlaneGraphFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ZoomPlaneGraphFrameLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		imgMatrix = new Matrix();
		touchEventHandler = new TouchEventHandler() {
			@Override
			public void onClick(float x, float y) {
				float xi = getXOnImage(x);
				float yi = getYOnImage(y);
				if (xi >= 0 && xi <= PlaneGraphWidth && yi >= 0
						&& yi <= PlaneGraphHeight) {
					onClickImage(xi, yi);
				}
			}

			@Override
			public void onDoubleClick(float x, float y) {
				zoom(x, y);
			}

			@Override
			public void onDrag(float offsetx, float offsety) {
				drag(offsetx, offsety);
			}

			@Override
			public void onZoom(float centerx, float centery, float holdx,
					float holdy, float scale) {
				zoom(scale, scale, centerx, centerx, holdx, holdy);
			}
		};
		setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				touchEventHandler.updateTouchEvent(event);
				return (true);
			}
		});
	}

	/**
	 * calculate the translate limit of right,left,top,bottom
	 */
	private void calcLimit() {
		float mapScaleWidth = getPlaneGraphWidth() * currentScale;
		float mapScaleHeight = getPlaneGraphHeight() * currentScale;
		maxTranslateX = getWidth() - mapScaleWidth;
		maxTranslateY = getHeight() - mapScaleHeight;
		limitX1 = maxTranslateX / 2;
		limitY1 = maxTranslateY / 2;
		limitX2 = limitX1;
		limitY2 = limitY1;
		if (maxTranslateX < 0) {
			limitX1 = maxTranslateX;
			limitX2 = 0;
		}
		if (maxTranslateY < 0) {
			limitY1 = maxTranslateY;
			limitY2 = 0;
		}
	}

	/**
	 * init the scale and calculate the translate limit of right,left,top,bottom
	 */
	private void initScaleLimit() {
		currentScale = INIT_SCALE;
		calcLimit();
		imgMatrix.reset();
		imgMatrix.postScale(currentScale, currentScale);
		imgMatrix.postTranslate(maxTranslateX / 2, maxTranslateY / 2);
		onSetImageMatrix(imgMatrix);
	}

	private void drag(float transX, float transY) {
		float[] matrixValues = new float[9];
		imgMatrix.getValues(matrixValues);
		if (transX + matrixValues[Matrix.MTRANS_X] < limitX1) {
			transX = limitX1 - matrixValues[Matrix.MTRANS_X];
		}
		if (transX + matrixValues[Matrix.MTRANS_X] > limitX2) {
			transX = limitX2 - matrixValues[Matrix.MTRANS_X];
		}
		if (transY + matrixValues[Matrix.MTRANS_Y] < limitY1) {
			transY = limitY1 - matrixValues[Matrix.MTRANS_Y];
		}
		if (transY + matrixValues[Matrix.MTRANS_Y] > limitY2) {
			transY = limitY2 - matrixValues[Matrix.MTRANS_Y];
		}
		imgMatrix.postTranslate(transX, transY);
		onSetImageMatrix(imgMatrix);
	}

	/**
	 * 
	 * @param centerX
	 *            a x-coordinate where you want to keep
	 * @param centerY
	 *            a y-coordinate where you want to keep
	 */
	private void zoom(float centerX, float centerY) {
		float[] matrixValues = new float[9];
		imgMatrix.getValues(matrixValues);
		if (matrixValues[Matrix.MSCALE_X] != INIT_SCALE) {
			initScaleLimit();
		} else {
			zoom(ZOOM_DOUBLE_CLICK_SCALE, ZOOM_DOUBLE_CLICK_SCALE, centerX,
					centerY, null, null);
		}
	}

	/**
	 * set to current scale, and keep (centerX,centerY) as center
	 * 
	 * @param scaleX
	 *            the x scale you want to set
	 * @param scaleY
	 *            the y scale you want to set
	 * @param centerX
	 *            a x-coordinate where you want to keep
	 * @param centerY
	 *            a y-coordinate where you want to keep
	 */
	private void zoom(float scaleX, float scaleY, float centerX, float centerY,
			Float holdx, Float holdy) {
		float[] matrixValues = new float[9];
		imgMatrix.getValues(matrixValues);
		float toScaleX = scaleX * matrixValues[Matrix.MSCALE_X];
		float toScaleY = scaleY * matrixValues[Matrix.MSCALE_Y];
		if (toScaleX < ZOOM_MIN_SCALE) {
			scaleX = ZOOM_MIN_SCALE / matrixValues[Matrix.MSCALE_X];
		}
		if (toScaleY < ZOOM_MIN_SCALE) {
			scaleY = ZOOM_MIN_SCALE / matrixValues[Matrix.MSCALE_Y];
		}
		if (toScaleY > ZOOM_MAX_SCALE) {
			scaleX = ZOOM_MAX_SCALE / matrixValues[Matrix.MSCALE_X];
		}
		if (toScaleY > ZOOM_MAX_SCALE) {
			scaleY = ZOOM_MAX_SCALE / matrixValues[Matrix.MSCALE_Y];
		}
		toScaleX = scaleX * matrixValues[Matrix.MSCALE_X];
		toScaleY = scaleY * matrixValues[Matrix.MSCALE_Y];
		currentScale = toScaleX;
		imgMatrix.postScale(scaleX, scaleY);
		float mapScaleWidth = getPlaneGraphWidth() * currentScale;
		float mapScaleHeight = getPlaneGraphHeight() * currentScale;
		maxTranslateX = getWidth() - mapScaleWidth;
		maxTranslateY = getHeight() - mapScaleHeight;
		float transX = 0;
		float transY = 0;
		if (maxTranslateX > 0) {
			transX = maxTranslateX / 2 - matrixValues[Matrix.MTRANS_X] * scaleX;
		} else {
			if (scaleX != 1) {
				if (holdx == null) {
					transX = (getWidth() / 2 - centerX * scaleX);
				} else {
					transX = holdx * (1 - scaleX);
				}
			}
		}
		if (maxTranslateY > 0) {
			transY = maxTranslateY / 2 - matrixValues[Matrix.MTRANS_Y] * scaleY;
		} else {
			if (scaleY != 1) {
				if (holdy == null) {
					transY = (getHeight() / 2 - centerY * scaleY);
				} else {
					transY = holdy * (1 - scaleY);
				}
			}
		}
		limitX1 = maxTranslateX / 2;
		limitY1 = maxTranslateY / 2;
		limitX2 = limitX1;
		limitY2 = limitY1;
		if (maxTranslateX < 0) {
			limitX1 = maxTranslateX;
			limitX2 = 0;
		}
		if (maxTranslateY < 0) {
			limitY1 = maxTranslateY;
			limitY2 = 0;
		}
		if (transX + matrixValues[Matrix.MTRANS_X] * scaleX < limitX1) {
			transX = limitX1 - matrixValues[Matrix.MTRANS_X] * scaleX;
		}
		if (transX + matrixValues[Matrix.MTRANS_X] * scaleX > limitX2) {
			transX = limitX2 - matrixValues[Matrix.MTRANS_X] * scaleX;
		}
		if (transY + matrixValues[Matrix.MTRANS_Y] * scaleY < limitY1) {
			transY = limitY1 - matrixValues[Matrix.MTRANS_Y] * scaleY;
		}
		if (transY + matrixValues[Matrix.MTRANS_Y] * scaleY > limitY2) {
			transY = limitY2 - matrixValues[Matrix.MTRANS_Y] * scaleY;
		}
		imgMatrix.postTranslate(transX, transY);
		onSetImageMatrix(imgMatrix);
	}

	/**
	 * convert x-coordinate from drawable to view
	 * 
	 * @param x
	 *            x-coordinate on your drawable
	 * @return x-coordinate on this view
	 */
	public float getXOnView(float x) {
		float[] matrixValues = new float[9];
		imgMatrix.getValues(matrixValues);
		return (x * matrixValues[Matrix.MSCALE_X] + matrixValues[Matrix.MTRANS_X]);
	}

	/**
	 * convert y-coordinate from drawable to view
	 * 
	 * @param y
	 *            y-coordinate on your drawable
	 * @return y-coordinate on this view
	 */
	public float getYOnView(float y) {
		float[] matrixValues = new float[9];
		imgMatrix.getValues(matrixValues);
		return (y * matrixValues[Matrix.MSCALE_Y] + matrixValues[Matrix.MTRANS_Y]);
	}

	/**
	 * convert a Point from drawable to view
	 * 
	 * @param p
	 *            a Point on this drawable
	 * @return a Point on this view
	 */
	public Point getPointOnView(Point p) {
		int x = (int) getXOnView(p.x);
		int y = (int) getYOnView(p.y);
		return (new Point(x, y));
	}

	/**
	 * convert x-coordinate from view to drawable
	 */
	public float getXOnImage(float x) {
		float[] matrixValues = new float[9];
		imgMatrix.getValues(matrixValues);
		return ((x - matrixValues[Matrix.MTRANS_X]) / matrixValues[Matrix.MSCALE_X]);
	}

	/**
	 * convert y-coordinate from view to drawable
	 */
	public float getYOnImage(float y) {
		float[] matrixValues = new float[9];
		imgMatrix.getValues(matrixValues);
		return ((y - matrixValues[Matrix.MTRANS_Y]) / matrixValues[Matrix.MSCALE_Y]);
	}

	/**
	 * set this view's center coordinate (x,y) on this drawable
	 * 
	 * @param x
	 *            x-coordinate on this drawable to be the center of this view
	 * @param y
	 *            y-coordinate on this drawable to be the center of this view
	 */
	public void setCenter(float x, float y) {
		float[] matrixValues = new float[9];
		imgMatrix.getValues(matrixValues);
		x = (int) getXOnView(x);
		y = (int) getYOnView(y);
		float transX = 0;
		float transY = 0;
		if (limitX1 != limitX2) {
			transX = (getWidth() / 2 - x);
		}
		if (limitY1 != limitY2) {
			transY = (getWidth() / 2 - y);
		}
		if (transX + matrixValues[Matrix.MTRANS_X] < limitX1) {
			transX = limitX1 - matrixValues[Matrix.MTRANS_X];
		}
		if (transX + matrixValues[Matrix.MTRANS_X] > limitX2) {
			transX = limitX2 - matrixValues[Matrix.MTRANS_X];
		}
		if (transY + matrixValues[Matrix.MTRANS_Y] < limitY1) {
			transY = limitY1 - matrixValues[Matrix.MTRANS_Y];
		}
		if (transY + matrixValues[Matrix.MTRANS_Y] > limitY2) {
			transY = limitY2 - matrixValues[Matrix.MTRANS_Y];
		}
		imgMatrix.postTranslate(transX, transY);
		onSetImageMatrix(imgMatrix);
	}

	/**
	 * 设置平面图大小
	 */
	public void setPlaneGraphSize(float w, float h) {
		PlaneGraphWidth = w;
		PlaneGraphHeight = h;
		initScaleLimit();
	}

	/**
	 * 获取平面图的宽
	 */
	public float getPlaneGraphWidth() {
		return (PlaneGraphWidth);
	}

	/**
	 * 获取平面图的高
	 */
	public float getPlaneGraphHeight() {
		return (PlaneGraphHeight);
	}
}
