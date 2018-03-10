package com.iscreate.mobile.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AbsListView;
import android.widget.ListView;

public class LoadMoreListView extends ListView {
	/**
	 * scroll up will load data
	 */
	public static final int LOADMOREMODE_SCROLLUP = 0;
	/**
	 * scroll down will load data
	 */
	public static final int LOADMOREMODE_SCROLLDOWN = 1;
	/**
	 * scroll up/down will load data
	 */
	public static final int LOADMOREMODE_SCROLLUPANDDOWN = 2;
	/**
	 * the moving sensitivity
	 */
	private final int SENSITIVITY_MOVING = 3;
	/**
	 * whether it is scrolling down
	 */
	private boolean isScrollDown = false;
	/**
	 * whether it is scrolling up
	 */
	private boolean isScrollUp = false;
	/**
	 * the coordinate of the down touch event
	 */
	private Float downY = null;
	/**
	 * the default mode is LOADMOREMODE_SCROLLDOWN
	 */
	private int LoadMoreMode = LOADMOREMODE_SCROLLDOWN;
	/**
	 * a listener when we need load new data
	 */
	private OnLoadMoreDataListener onLoadMoreDataListener = null;

	public LoadMoreListView(Context context) {
		super(context);
		init();
	}

	public LoadMoreListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public LoadMoreListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	/**
	 * init the ListView
	 */
	private void init() {
		setFastScrollEnabled(true);
		setOnScrollListener(onScrollListener);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int action = ev.getAction() & 0x000000ff;
		switch (action) {
		/**
		 * a down event means a new start
		 */
		case MotionEvent.ACTION_DOWN:
			downY = ev.getY();
			isScrollUp = false;
			isScrollDown = false;
			break;
		/**
		 * judging the direction of the scrolling
		 */
		case MotionEvent.ACTION_MOVE:
			if (downY != null) {
				float y = ev.getY();
				if (y + SENSITIVITY_MOVING < downY) {
					isScrollDown = true;
				}
				if (downY + SENSITIVITY_MOVING < y) {
					isScrollUp = true;
				}
			}
			break;
		/**
		 * a up event means it ended
		 */
		case MotionEvent.ACTION_UP:
			downY = null;
			break;
		/**
		 * a cancel event means it ended
		 */
		case MotionEvent.ACTION_CANCEL:
			isScrollUp = false;
			isScrollDown = false;
			downY = null;
			break;
		}
		return (super.onTouchEvent(ev));
	}

	/**
	 * listen to the scrolling
	 */
	private OnScrollListener onScrollListener = new OnScrollListener() {
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			switch (scrollState) {
			/**
			 * judge the position of the first and last view when the scrolling
			 * is idle,if it is the last or the first view,then call
			 * onLoadMoreDataListener
			 */
			case OnScrollListener.SCROLL_STATE_IDLE:
				switch (LoadMoreMode) {
				case LOADMOREMODE_SCROLLDOWN:
					isScrollUp = false;
					break;
				case LOADMOREMODE_SCROLLUP:
					isScrollDown = false;
					break;
				case LOADMOREMODE_SCROLLUPANDDOWN:
					break;
				}
				int totalItemCount = view.getCount();
				if (isScrollDown) {
					if (view.getLastVisiblePosition() + 1 == totalItemCount) {
						if (onLoadMoreDataListener != null) {
							onLoadMoreDataListener.OnLoadMore(
									LOADMOREMODE_SCROLLDOWN, totalItemCount);
						}
					}
				}
				if (isScrollUp) {
					if (view.getFirstVisiblePosition() == 0) {
						if (onLoadMoreDataListener != null) {
							onLoadMoreDataListener.OnLoadMore(
									LOADMOREMODE_SCROLLUP, totalItemCount);
						}
					}
				}
				break;
			case OnScrollListener.SCROLL_STATE_FLING:
				break;
			case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
				break;
			default:
				break;
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
		}
	};

	/**
	 * set the mode
	 * 
	 * @param mode
	 *            LOADMOREMODE_SCROLLUP/ LOADMOREMODE_SCROLLDOWN/
	 *            LOADMOREMODE_SCROLLUPANDDOWN
	 */
	public void setLoadMoreMode(int mode) {
		LoadMoreMode = mode;
	}

	/**
	 * 
	 * a interface for load more data
	 */
	public interface OnLoadMoreDataListener {
		public void OnLoadMore(int mode, int totalItemCount);
	}

	/**
	 * a listener for load more data
	 * 
	 * @param listener
	 *            OnLoadMoreDataListener
	 */
	public void setOnLoadMoreDataListener(OnLoadMoreDataListener listener) {
		onLoadMoreDataListener = listener;
	}
}