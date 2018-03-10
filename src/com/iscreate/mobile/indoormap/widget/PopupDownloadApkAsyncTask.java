package com.iscreate.mobile.indoormap.widget;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.iscreate.mobile.indoormap.R;
import com.iscreate.mobile.utils.DownloadApkAsyncTask;
import com.iscreate.mobile.widget.PopupNormalWindow;

public abstract class PopupDownloadApkAsyncTask extends DownloadApkAsyncTask {
	private PopupNormalWindow popupWin = null;
	private View parent = null;
	private ProgressBar process_bar = null;
	private TextView tv_message = null;
	private CustomAlertDialog cad = null;

	public abstract void onDownloadFinished(String path);

	public PopupDownloadApkAsyncTask(View parent) {
		this.parent = parent;
		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		View contentView = LayoutInflater.from(parent.getContext()).inflate(
				R.layout.popupdownload, null);
		process_bar = (ProgressBar) contentView.findViewById(R.id.process_bar);
		tv_message = (TextView) contentView.findViewById(R.id.tv_message);
		popupWin = new PopupNormalWindow(contentView,
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT) {
			@Override
			public boolean onDismiss() {
				cad = new CustomAlertDialog(parent.getContext(), "下载",
						"确定中止下载吗？") {
					@Override
					public void onConfirm() {
						stop();
					}

					@Override
					public void onCancel() {
					}
				};
				cad.show();
				return (false);
			}
		};

	}

	/**
	 * 设置popupWin.getContentView()的背景色
	 * 
	 * @param color
	 *            颜色
	 */
	public void setBackgroundColor(int color) {
		popupWin.getContentView().setBackgroundColor(color);
	}

	/**
	 * 设置显示文字
	 */
	public void setMessage(String msg) {
		tv_message.setText(msg);
		tv_message.setVisibility(View.VISIBLE);
	}

	/**
	 * 启动线程及弹框
	 */
	public void start(String... params) {
		popupWin.showAtLocation(parent, Gravity.CENTER, 0, 0);
		execute(params);
	}

	@Override
	protected void onPreExecute() {
		process_bar.setMax(100);
		process_bar.setProgress(0);
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(String apkpath) {
		onDownloadFinished(apkpath);
		if (cad != null) {
			cad.dismiss();
		}
		popupWin.dismiss();
		super.onPostExecute(apkpath);
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		if (cad != null) {
			cad.dismiss();
		}
		popupWin.dismiss();
		onDownloadFinished(null);
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		switch (values[0]) {
		case UPDATE_MAX:
			process_bar.setMax(values[1]);
			break;
		case UPDATE_PROGRESS:
			process_bar.setProgress(values[1]);
			break;
		}
		super.onProgressUpdate(values);
	}
}