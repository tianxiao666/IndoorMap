package com.iscreate.mobile.indoormap.widget;

import com.iscreate.mobile.indoormap.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class DoSureDialog extends Dialog{
	
	private Button doSureButton = null;
	private Button doCancelButton = null;
	private CheckBox noTipBox = null;
	private TextView titleView = null;
	private boolean noTipSelect = false;
	
	public boolean isNoTipSelect() {
		
		return noTipSelect;
	}

	public void setNoTipSelect(boolean noTipSelect) {
		this.noTipSelect = noTipSelect;
	}
	
	public DoSureDialog(Context context, String title) {
		super(context, R.style.ChooseDialog);
		initView(context,title);
	}

	public void initView(Context context,String title){
		View view = LayoutInflater.from(context).inflate(R.layout.dosuredialog, null);
		titleView = (TextView) view.findViewById(R.id.dialog_title);
		doSureButton = (Button) view.findViewById(R.id.sure);
		doCancelButton = (Button) view.findViewById(R.id.cancel);
		titleView.setText(title);
		noTipBox = (CheckBox) view.findViewById(R.id.show_dialog);
		noTipBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					setNoTipSelect(true);
				}else{
					setNoTipSelect(false);
				}
			}
		});
		LinearLayout.LayoutParams params = new LayoutParams(400,300);
		super.setContentView(view,params);
	}
	
	public void setDoSureBtnClickListener(android.view.View.OnClickListener listener){
		doSureButton.setOnClickListener(listener);
	}
	
	public void setDoCancelBtnClickListener(android.view.View.OnClickListener listener){
		doCancelButton.setOnClickListener(listener);
	}
}
