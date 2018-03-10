package com.iscreate.mobile.indoormap.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.SparseBooleanArray;

public class CustumProgressDialog extends ProgressDialog {

	private SparseBooleanArray sba = null;

	public CustumProgressDialog(Context context) {
		super(context);
	}

	public CustumProgressDialog(Context context, int theme) {
		super(context, theme);
	}

	public void show(int id) {
		if (sba == null) {
			sba = new SparseBooleanArray();
		}
		sba.put(id, true);
		show();
	}

	public void dismiss(int id) {
		if (sba != null) {
			sba.delete(id);
		}
		if ((sba == null) || (sba.size() == 0)) {
			dismiss();
		}
	}
}
