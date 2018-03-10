package com.iscreate.mobile.indoormap.widget;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CustomImageView extends ImageView {
	public CustomImageView(Context context) {
		super(context);
	}

	public CustomImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * convert x-coordinate from drawable to view
	 * 
	 * @param x
	 *            x-coordinate on your drawable
	 * @return x-coordinate on this view
	 */
	public float getXOnView(float x) {
		Matrix matrix = getImageMatrix();
		float[] matrixValues = new float[9];
		matrix.getValues(matrixValues);
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
		Matrix matrix = getImageMatrix();
		float[] matrixValues = new float[9];
		matrix.getValues(matrixValues);
		return (y * matrixValues[Matrix.MSCALE_Y] + matrixValues[Matrix.MTRANS_Y]);
	}

	/**
	 * convert a length r from drawable to view
	 * 
	 * @param r
	 *            r on your drawable
	 * @return r on this view
	 */
	public float getROnView(float r) {
		Matrix matrix = getImageMatrix();
		float[] matrixValues = new float[9];
		matrix.getValues(matrixValues);
		return (r
				* (matrixValues[Matrix.MSCALE_X] + matrixValues[Matrix.MSCALE_Y]) / 2);
	}
}
