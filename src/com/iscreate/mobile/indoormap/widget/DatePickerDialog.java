package com.iscreate.mobile.indoormap.widget;

import com.iscreate.mobile.indoormap.R;

import android.R.integer;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.content.DialogInterface.OnClickListener;

public class DatePickerDialog extends AlertDialog implements OnClickListener,OnDateChangedListener{
	private static final String BEGIN_YEAR = "begin_year";
	private static final String BEGIN_MONTH = "begin_month";
	private static final String BEGIN_DAY = "begin_day";
	private static final String END_YEAR = "end_year";
	private static final String END_MONTH = "end_month";
	private static final String END_DAY = "end_day";
	private DatePicker datePickerBegin;
	private DatePicker datePickerEnd;
	private OnDateSetListener mCallBack;
	public DatePickerDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth){
		this(context, 0, callBack, year, monthOfYear, dayOfMonth);
	}
	
	public DatePickerDialog(Context context, int theme, OnDateSetListener callBack, int year, int monthOfYear,
			           int dayOfMonth) {
		super(context, theme);
		this.mCallBack = callBack;
		Context themeContext = getContext();
		setButton(BUTTON_POSITIVE,"确 定", (OnClickListener) this);
		setButton(BUTTON_NEGATIVE,"取 消",(OnClickListener) this);
		setIcon(0);
		LayoutInflater inflater = (LayoutInflater) themeContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.datapickerdialog, null);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setView(view,0,0,0,0);
		this.datePickerBegin = (DatePicker) view.findViewById(R.id.datePickerbegin);
		this.datePickerEnd = (DatePicker) view.findViewById(R.id.datePickerEnd);
		this.datePickerBegin.init(year, monthOfYear, dayOfMonth, this);
		this.datePickerEnd.init(year, monthOfYear, dayOfMonth, this);
	}
	
	public interface OnDateSetListener{
		void onDateSet(DatePicker startDatePicker, int startYear, int startMonth, int startDay,
				   DatePicker endDatePicker, int endYear, int endMonth, int endDay);
	}
	
	@Override
	public void onDateChanged(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		if(view.getId() == R.id.datePickerbegin){
			this.datePickerBegin.init(year, monthOfYear, dayOfMonth, this);
		}
		if(view.getId() == R.id.datePickerEnd){
			this.datePickerEnd.init(year, monthOfYear, dayOfMonth, this);
		}
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		if(which == BUTTON_POSITIVE){
			tryNotifyDateSet();
		}
	}

	private void tryNotifyDateSet(){
		if(this.mCallBack != null){
			this.datePickerBegin.clearFocus();
			this.datePickerEnd.clearFocus();
			this.mCallBack.onDateSet(this.datePickerBegin, this.datePickerBegin.getYear(),
					this.datePickerBegin.getMonth(), this.datePickerBegin.getDayOfMonth(),
					this.datePickerEnd, this.datePickerEnd.getYear(), this.datePickerEnd.getMonth(), 
					this.datePickerEnd.getDayOfMonth());
		}
	}

	@Override
	public Bundle onSaveInstanceState() {
		Bundle state = super.onSaveInstanceState();
		state.putInt(BEGIN_YEAR, this.datePickerBegin.getYear());
		state.putInt(BEGIN_MONTH, this.datePickerBegin.getMonth());
		state.putInt(BEGIN_DAY, this.datePickerBegin.getDayOfMonth());
		state.putInt(END_YEAR, this.datePickerEnd.getYear());
		state.putInt(END_MONTH, this.datePickerEnd.getMonth());
		state.putInt(END_DAY, this.datePickerEnd.getDayOfMonth());
		return state;
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		int beginYear = savedInstanceState.getInt(BEGIN_YEAR);
		int beginMonth = savedInstanceState.getInt(BEGIN_MONTH);
		int beginDay = savedInstanceState.getInt(BEGIN_DAY);
		this.datePickerBegin.init(beginYear, beginMonth, beginDay, this);
		int endYear = savedInstanceState.getInt(END_YEAR);
		int endMonth = savedInstanceState.getInt(END_MONTH);
		int endDay = savedInstanceState.getInt(END_DAY);
		this.datePickerEnd.init(endYear, endMonth, endDay, this);
	}

	public void updateBeginDate(int year,int month,int day){
		this.datePickerBegin.updateDate(year, month, day);
	}
	
	public void updateEndDate(int year,int month,int day){
		this.datePickerEnd.updateDate(year, month, day);
	}
}
