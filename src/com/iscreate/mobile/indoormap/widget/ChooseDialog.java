package com.iscreate.mobile.indoormap.widget;

import com.iscreate.mobile.indoormap.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class ChooseDialog extends Dialog{
	
	private TextView collectData;
	private TextView viewData;
	private Context context;
	private ChooseClickInterface chooseClickInterface;
	public void setChooseClickInterface(ChooseClickInterface chooseClickInterface) {
		this.chooseClickInterface = chooseClickInterface;
	}

	public ChooseDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView(context);
	}

	public void initView(Context context){
		View dialogView = LayoutInflater.from(context).inflate(R.layout.choosewindow, null);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		setContentView(dialogView,params);
		collectData = (TextView) dialogView.findViewById(R.id.collect_data);
		viewData = (TextView) dialogView.findViewById(R.id.view_data);
		collectData.setOnClickListener(new ChooseClickListener());
		viewData.setOnClickListener(new ChooseClickListener());
	}
	
	public interface ChooseClickInterface{
		public void collectBtnClick();
		public void viewBtnClick();
	}
	
	public class ChooseClickListener implements android.view.View.OnClickListener{

		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch(id){
				case R.id.collect_data: 
					chooseClickInterface.collectBtnClick();
					break;
				case R.id.view_data:
					chooseClickInterface.viewBtnClick();
					break;
			}
			
		}
		
	}
}
