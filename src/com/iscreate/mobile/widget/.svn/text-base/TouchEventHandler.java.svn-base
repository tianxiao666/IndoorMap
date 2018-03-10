package com.iscreate.mobile.widget;

import android.view.MotionEvent;

import com.iscreate.mobile.utils.utils;

public abstract class TouchEventHandler {
	/**
	 * the time of double click
	 */
	private final int DOUBLE_CLICK_TIME_GAP = 350;
	/**
	 * the distance of double click
	 */
	private final float DOUBLE_CLICK_DISTANCE_GAP = 40f;
	/**
	 * the distance between down and up event
	 */
	private final float CLICK_DISTANCE_GAP = 5f;
	/**
	 * the sense difference between the perimeter of all pointers ,if larger
	 * than this value will call zoom
	 */
	private final float ZOOM_POINTER_PERIMETER_DISTANCE_GAP = 10;
	/**
	 * two finger
	 */
	private final int ZOOM_POINTER_COUNT = 2;

	/**
	 * drag start x-coordinate
	 */
	private Float dragStartX = null;
	/**
	 * drag start y-coordinate
	 */
	private Float dragStartY = null;

	/**
	 * the pointer perimeter of all pointers
	 */
	private double PointerPerimeter = -1;
	/**
	 * the center x-coordinate during zoom
	 */
	private Float zoomHoldX = null;
	/**
	 * the center y-coordinate during zoom
	 */
	private Float zoomHoldY = null;
	private float dragTrans = 0;
	private boolean zoomed = false;
	/**
	 * last click event
	 */
	private Point3D upeventfordoubleclick = null;
	private DelayRun delayRun = null;

	public abstract void onClick(float x, float y);

	public abstract void onDoubleClick(float x, float y);

	public abstract void onDrag(float offsetx, float offsety);

	public abstract void onZoom(float centerx, float centery, float holdx,
			float holdy, float scale);

	public void updateTouchEvent(MotionEvent event) {
		int action = event.getActionMasked();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			onActionDown(event);
			break;
		case MotionEvent.ACTION_UP:
			onActionUp(event);
			break;
		case MotionEvent.ACTION_MOVE:
			onActionMove(event);
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			onActionPointerDown(event);
			break;
		case MotionEvent.ACTION_POINTER_UP:
			onActionPointerUp(event);
			break;
		}
	}

	/**
	 * mark the start event x,y
	 * 
	 * @param event
	 *            touch event while ACTION_DOWN
	 */
	private void onActionDown(MotionEvent event) {
		zoomed = false;
		dragTrans = 0;
		PointerPerimeter = -1;
		dragStartX = event.getX();
		dragStartY = event.getY();
		if (delayRun != null) {
			delayRun.cancel();
		}
	}

	/**
	 * clear the variables
	 * 
	 * @param event
	 *            touch event while ACTION_UP
	 */
	private void onActionUp(MotionEvent event) {
		if (dragStartY != null && dragStartX != null) {
			boolean doubleclicked = false;
			long thisClickTime = event.getEventTime();
			if ((upeventfordoubleclick != null)
					&& ((thisClickTime - upeventfordoubleclick.z) < DOUBLE_CLICK_TIME_GAP)
					&& (Math.sqrt(Math.pow(
							dragStartY - upeventfordoubleclick.y, 2)
							+ Math.pow(dragStartX - upeventfordoubleclick.x, 2)) < DOUBLE_CLICK_DISTANCE_GAP)) {
				onDoubleClick(event.getX(), event.getY());
				doubleclicked = true;
				upeventfordoubleclick = null;
			} else {
				if ((dragTrans < CLICK_DISTANCE_GAP) && !zoomed) {
					if (upeventfordoubleclick == null) {
						upeventfordoubleclick = new Point3D();
					}
					upeventfordoubleclick.z = thisClickTime;
					upeventfordoubleclick.y = dragStartY;
					upeventfordoubleclick.x = dragStartX;
				}
			}
			if (delayRun != null) {
				delayRun.cancel();
			}
			if (!doubleclicked) {
				delayRun = new DelayRun(DOUBLE_CLICK_TIME_GAP, dragTrans,
						dragStartX, dragStartY) {
					@Override
					public void run(Object[] o) {
						if (o.length == 3) {
							try {
								float dragTrans = (Float) o[0];
								float dragStartX = (Float) o[1];
								float dragStartY = (Float) o[2];
								if (dragTrans < CLICK_DISTANCE_GAP && !zoomed) {
									onClick(dragStartX, dragStartY);
									upeventfordoubleclick = null;
								}
							} catch (Exception e) {
							}
						}
						delayRun = null;
					}
				};
				delayRun.start();
			}
		}
		PointerPerimeter = -1;
		dragStartX = null;
		dragStartY = null;
		zoomHoldX = null;
		zoomHoldY = null;
		dragTrans = 0;
	}

	/**
	 * calculate the translate value or zoom scale
	 * 
	 * @param event
	 *            touch event while ACTION_MOVE
	 */
	private void onActionMove(MotionEvent event) {
		int PointerCount = event.getPointerCount();
		if (PointerCount < ZOOM_POINTER_COUNT) {
			zoomHoldX = null;
			zoomHoldY = null;
			if ((dragStartX == null) || (dragStartY == null)) {
				dragStartX = event.getX();
				dragStartY = event.getY();
			}
			float moveX = event.getX();
			float moveY = event.getY();
			float transX = 0;
			float transY = 0;
			if (dragStartX != null) {
				transX = moveX - dragStartX;
			}
			if (dragStartY != null) {
				transY = moveY - dragStartY;
			}
			float trans = (float) Math.sqrt(Math.pow(transX, 2)
					+ Math.pow(transY, 2));
			if (trans >= CLICK_DISTANCE_GAP) {
				dragStartX = moveX;
				dragStartY = moveY;
				onDrag(transX, transY);
				if (dragStartX != null && dragStartX != null) {
					dragTrans = dragTrans + trans;
				}
			}
		} else {
			double nowPointersRound = utils.getEventPointerPerimeter(event,
					false);
			if (Math.abs(PointerPerimeter - nowPointersRound) > ZOOM_POINTER_PERIMETER_DISTANCE_GAP
					* PointerCount) {
				float[] pointerCenter = utils.getEventPointerCenter(event);
				if (zoomHoldX == null) {
					zoomHoldX = pointerCenter[0];
					zoomHoldY = pointerCenter[1];
				}
				float scale = (float) (nowPointersRound / PointerPerimeter);
				zoomed = true;
				onZoom(pointerCenter[0], pointerCenter[1], zoomHoldX,
						zoomHoldY, scale);
				PointerPerimeter = nowPointersRound;
			}
		}
	}

	/**
	 * recalculate the pointer perimeters
	 * 
	 * @param event
	 *            touch event while ACTION_POINTER_DOWN
	 */
	private void onActionPointerDown(MotionEvent event) {
		PointerPerimeter = utils.getEventPointerPerimeter(event, false);
	}

	/**
	 * clear the variables if pointer count is equal to
	 * ZOOM_POINTER_COUNT,otherwise recalculate the pointer perimeters
	 * 
	 * @param event
	 *            touch event while ACTION_POINTER_UP
	 */
	private void onActionPointerUp(MotionEvent event) {
		int PointerCount = event.getPointerCount();
		if (PointerCount == ZOOM_POINTER_COUNT) {
			PointerPerimeter = -1;
			dragStartX = null;
			dragStartY = null;
			zoomHoldX = null;
			zoomHoldY = null;
		} else {
			PointerPerimeter = utils.getEventPointerPerimeter(event, true);
		}
	}
}