package com.iscreate.mobile.widget;

import android.os.AsyncTask;

/**
 * 只能在UI线程中用
 */
public abstract class DelayRun {
	private boolean canceled = false;
	private AsyncTask<Long, Void, Void> task = null;
	private long delaytime = 0;
	private Object[] runobj = null;

	/**
	 * @param o
	 *            传入的参数
	 */
	abstract public void run(Object[] o);

	public DelayRun(long t, Object... runparmas) {
		delaytime = t;
		runobj = new Object[runparmas.length];
		int i = 0;
		while (i < runobj.length) {
			runobj[i] = runparmas[i];
			++i;
		}
		init();
	}

	private void init() {
		task = new AsyncTask<Long, Void, Void>() {
			@Override
			protected Void doInBackground(Long... params) {
				try {
					Thread.sleep(params[0]);
				} catch (InterruptedException e) {
				}
				return (null);
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				if (!canceled) {
					run(runobj);
				}
			}
		};
	}

	protected void start() {
		task.execute((Long) delaytime);
	}

	protected void cancel() {
		task.cancel(true);
		canceled = true;
	}
}
