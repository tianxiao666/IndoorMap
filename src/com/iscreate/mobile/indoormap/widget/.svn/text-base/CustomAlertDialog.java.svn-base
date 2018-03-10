package com.iscreate.mobile.indoormap.widget;

import com.iscreate.mobile.indoormap.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;

public abstract class CustomAlertDialog {
	private AlertDialog alertDialog = null;

	/**
	 * override this to listen to click confirm
	 */
	public abstract void onConfirm();

	/**
	 * override this to listen to click cancel
	 */
	public abstract void onCancel();

	protected CustomAlertDialog(Context context, String title, String message) {
		init(context, title, message, "确定", "取消");
	}

	protected CustomAlertDialog(Context context, String title, String message,
			String positive, String negative) {
		init(context, title, message, positive, negative);
	}

	/**
	 * init the dialog with title and message,then show this dialog
	 * 
	 * @param title
	 *            the dialog title
	 * @param message
	 *            the dialog message
	 */
	private void init(Context context, String title, String message,
			String positive, String negative) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setIcon(R.drawable.iscreate);
		builder.setCancelable(false);
		builder.setMessage(message);
		builder.setPositiveButton(positive,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						onConfirm();
						dialog.dismiss();
					}
				});
		builder.setNegativeButton(negative,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						onCancel();
					}
				});
		alertDialog = builder.create();
		alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					if (event.getAction() == KeyEvent.ACTION_UP) {
						dialog.dismiss();
						onCancel();
						return (true);
					}
				}
				return (false);
			}
		});
	}

	/**
	 * show AlertDialog
	 */
	public void show() {
		alertDialog.show();
	}

	/**
	 * dismiss AlertDialog
	 */
	public void dismiss() {
		alertDialog.dismiss();
	}
}
