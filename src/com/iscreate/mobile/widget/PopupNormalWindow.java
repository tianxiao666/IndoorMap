package com.iscreate.mobile.widget;

import android.content.Context;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.PopupWindow;

public class PopupNormalWindow extends PopupWindow {
	private boolean KEYCODE_BACK_OnTouch_Down = false;
	private boolean KEYCODE_BACK_OnKey_Down = false;

	public PopupNormalWindow(Context context) {
		super(context);
	}

	/**
	 * set
	 * 
	 * @param v
	 *            the view to pop up
	 * @param w
	 *            the width of this view
	 * @param h
	 *            the height of this view
	 */
	public PopupNormalWindow(View v, int w, int h) {
		super(v, w, h);
		if (v != null) {
			v.setFocusableInTouchMode(true);
			v.setOnTouchListener(popupViewOnTouch);
			v.setOnKeyListener(popupViewOnKey);
			setFocusable(true);
		}
	}

	/**
	 * a listener to key event ,if pressed KEYCODE_BACK then dismiss
	 */
	private OnKeyListener popupViewOnKey = new OnKeyListener() {
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				int action = event.getAction();
				switch (action) {
				case KeyEvent.ACTION_DOWN:
					KEYCODE_BACK_OnKey_Down = true;
					break;
				case KeyEvent.ACTION_UP:
					if (KEYCODE_BACK_OnKey_Down) {
						if (onDismiss()) {
							dismiss();
						}
						return (true);
					}
				}
			}
			return (false);
		}
	};

	/**
	 * a listener to touch event,if the touch is out of this view then dismiss
	 */
	private OnTouchListener popupViewOnTouch = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			int action = event.getAction();
			switch (action) {
			case KeyEvent.ACTION_DOWN:
				KEYCODE_BACK_OnTouch_Down = isOutOfView(getContentView(),
						event.getX(), event.getY());
				break;
			case KeyEvent.ACTION_UP:
				if (KEYCODE_BACK_OnTouch_Down) {
					if (isOutOfView(getContentView(), event.getX(),
							event.getY())) {
						if (onDismiss()) {
							dismiss();
						}
						return (true);
					}
				}
			}
			return false;
		}
	};

	/**
	 * judge x,y is out of v or not
	 * 
	 * @param v
	 *            the popped up view
	 * @param x
	 *            x
	 * @param y
	 *            y
	 * @return true if (x,y) is out of v
	 */
	private boolean isOutOfView(View v, float x, float y) {
		return (v != null)
				&& ((x < 0) || (y < 0) || (x >= v.getWidth()) || (y >= v
						.getHeight()));
	}

	/**
	 * @return true if continue dismiss,otherwise false
	 */
	public boolean onDismiss() {
		return (true);
	}
}