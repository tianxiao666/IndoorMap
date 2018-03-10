package com.iscreate.mobile.indoormap.activity;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.iscreate.mobile.indoormap.R;
import com.iscreate.mobile.indoormap.widget.DatePickerDialog;
import com.iscreate.mobile.service.ServiceClientInterface;
import com.iscreate.mobile.utils.SignalBean;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ViewSignalDataActivity extends Activity{
	
	private LineChartView chartView;
	private String beginDate;
	private String endDate;
	private Button chooseBtn;
	private TextView beginDateView;
	private TextView endDateView;
	private DatePickerDialog datePickerDialog;
	private Button sureBtn;
	private Spinner spinner;
	private TextView noChartView;
	private RadioGroup radioGroup;
	private RadioButton wifiRadioBtn;
	private RadioButton stationRadioBtn;
	private String BUILDING_ID = null;
	private String FLOOR_ID = null;
	private String DRAW_MAP_ID = null;
	private String x = null;
	private String y = null;
	private String pic_width = null;
	private String pic_height = null;
	private String signal_type = "W";
	private String []params = null;
	private List<SignalBean> signalBeans = new ArrayList<SignalBean>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewsignaldata_act);
		initParams(this.getIntent());
		this.chartView = (LineChartView) findViewById(R.id.chart);
		this.noChartView = (TextView) findViewById(R.id.no_chart_text);
		getPointData(this.params);
		TextView xTextView = (TextView) findViewById(R.id.point_x);
		TextView yTextView = (TextView) findViewById(R.id.point_y);
		initRadio();
		this.sureBtn = (Button) findViewById(R.id.sure_btn);
		xTextView.setText(x);
		yTextView.setText(y);
		initDateShow();
		this.sureBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String []postParams = {BUILDING_ID,FLOOR_ID,DRAW_MAP_ID,
						   x,y,pic_height,pic_width,signal_type,
						   beginDate,endDate};
				params = postParams;
				getPointData(params);
			}
		});
	}
	
	public void initRadio(){
		this.radioGroup = (RadioGroup) findViewById(R.id.radio_group);
		this.wifiRadioBtn = (RadioButton) findViewById(R.id.radio_wifi);
		this.stationRadioBtn = (RadioButton) findViewById(R.id.radio_station);
		this.wifiRadioBtn.setChecked(true);
		this.radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(wifiRadioBtn.getId() == checkedId){
					signal_type = "W";
				}else{
					signal_type = "B";
				}
			}
		});
	}
	
	
	public void initParams(Intent intent){
		Bundle bundle = intent.getBundleExtra("point");
		x = bundle.getString("x");
		y = bundle.getString("y");
		pic_width = bundle.getString("width");
		pic_height = bundle.getString("height");
		BUILDING_ID = bundle.getString("BUILDING_ID");
		FLOOR_ID = bundle.getString("FLOOR_ID");
		DRAW_MAP_ID = bundle.getString("DRAW_MAP_ID");
		Calendar dateCalendar = Calendar.getInstance();
		int year = dateCalendar.get(GregorianCalendar.YEAR);
		int month = dateCalendar.get(GregorianCalendar.MONTH);
		int day = dateCalendar.get(GregorianCalendar.DAY_OF_MONTH);
		beginDate = year + "-" + (month+1) + "-" + day;
		endDate = beginDate;
		String []params = {BUILDING_ID,FLOOR_ID,DRAW_MAP_ID,
						   x,y,pic_height,pic_width,signal_type,
						   beginDate,endDate};
		this.params = params;
	}
	
	public void initSpinner(){
		List<String> list = new ArrayList<String>();
		list.add("wifi");
		list.add("基站");
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
		adapter.setDropDownViewResource(R.layout.my_spinner_layout);
		this.spinner.setAdapter(adapter);
		this.spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				TextView fontView = (TextView) view;
				fontView.setTextColor(getResources().getColor(R.color.gray3));
				fontView.setCompoundDrawables(null, null,getResources().getDrawable(R.drawable.spinner_drop), null);
				ArrayAdapter<String> arrayAdapter = (ArrayAdapter<String>) parent.getAdapter();
				if(arrayAdapter.getItem(position).equals("wifi")){
					signal_type = "W";
				}else{
					signal_type = "B";
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				signal_type = "B";
			}
		});
	}	
	
	/**
	 * 
	 * TODO:reques the signal data
	 * @date:2017-2-24
	 * @autor:lu.sj
	 * @param params
	 * @throws Exception 
	 */
	public void getPointData(String []params){
		String[] keys = {"BUILDING_ID","FLOOR_ID","DRAW_MAP_ID",
						"PLANE_X","PLANE_Y","HEIGHT","WIDTH",
						"SIGNAL_TYPE","START_DATE","END_DATE"};
		try {
			String resonpseJson =  ServiceClientInterface.getSignalData(keys, params);
			Log.e("getPointDataResult", resonpseJson);
			if(resonpseJson.equals("[]")){
				this.chartView.setVisibility(View.GONE);
				this.noChartView.setVisibility(View.VISIBLE);
			}else{
				if(this.noChartView.getVisibility() == View.VISIBLE){
					this.noChartView.setVisibility(View.GONE);
				}
				if(this.chartView.getVisibility() == View.GONE){
					this.chartView.setVisibility(View.VISIBLE);
				}
				if(this.signalBeans != null && this.signalBeans.size() > 0){
					this.signalBeans.clear();
				}
				Gson gson = new Gson();
				JsonArray jsonArray = new JsonParser().parse(resonpseJson).getAsJsonArray();
				for(int i = 0; i < jsonArray.size(); i++){
					SignalBean signalBean = gson.fromJson(jsonArray.get(i), SignalBean.class);
					this.signalBeans.add(signalBean);
				}
				initSignalChart(this.signalBeans,this.signal_type);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}				
	}
	
	public void initSignalChart(List<SignalBean> sBeans,String signalType){
		List<AxisValue> axisValuesX = new ArrayList<AxisValue>();
		List<AxisValue> axisValuesY = new ArrayList<AxisValue>();
		List<PointValue> values = new ArrayList<PointValue>();
		float yMin = 0;
		values.add(new PointValue(0, 0).setLabel(0+""));
		axisValuesY.add(new AxisValue(yMin));
		axisValuesX.add(new AxisValue(0));
		for(int i = 0; i < sBeans.size(); i++){
			String []date = sBeans.get(i).MEA_DATE.split("-");
			int month = Integer.parseInt(date[1]);
			if(month < 10){
				date[1] = date[1].replace("0", "");			
			}
			String xdayStr = date[1]+"."+date[2];
			float level = Float.parseFloat(sBeans.get(i).SIGNAL);
			DecimalFormat decimalFormat = new DecimalFormat(".00");
			float levelF = Float.parseFloat(decimalFormat.format(level));
			values.add(new PointValue(i+1, levelF).setLabel(levelF+""));
			axisValuesY.add(new AxisValue(levelF));
			axisValuesX.add(new AxisValue(i+1).setLabel(xdayStr));
		}
		Line line = new Line(values).setColor(Color.GREEN).setCubic(false);
		line.setStrokeWidth(1);
		List<Line> lines = new ArrayList<Line>();
		lines.add(line);
		line.setHasLabelsOnlyForSelected(true);
		line.setHasLines(true);
		LineChartData data = new LineChartData();
		data.setLines(lines);
		if(signalType.equals("W")){
			data.setAxisXTop(new Axis(axisValuesX).setTextSize(8).setName("日期"));
		}else{
			data.setAxisXBottom(new Axis(axisValuesX).setTextSize(8).setName("日期"));
		}
		data.setAxisYLeft(new Axis(axisValuesY).setTextSize(8).setName("信号强度"));
		this.chartView.setLineChartData(data);
		this.chartView.setZoomEnabled(false);
		this.chartView.setScrollEnabled(false);
	}
	
	public void initDateShow(){
		beginDateView = (TextView) findViewById(R.id.date_begin_tv);
		endDateView = (TextView) findViewById(R.id.date_end_tv);
		chooseBtn = (Button) findViewById(R.id.date_picker_btn);
		final Calendar calendar = Calendar.getInstance();
		final int year = calendar.get(GregorianCalendar.YEAR);
		final int month = calendar.get(GregorianCalendar.MONTH);
		final int day = calendar.get(GregorianCalendar.DAY_OF_MONTH);
		beginDate = year+"-"+(month+1)+"-"+day;
		endDate = beginDate;
		chooseBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				chooseBtn.setClickable(false);
				if(datePickerDialog == null){
					datePickerDialog = new DatePickerDialog(ViewSignalDataActivity.this, new DatePickerDialog.OnDateSetListener() {
						
						@Override
						public void onDateSet(DatePicker startDatePicker, int startYear,
								int startMonth, int startDay, DatePicker endDatePicker,
								int endYear, int endMonth, int endDay) {
								beginDate = startYear+"-"+(startMonth+1)+"-"+startDay;
								endDate = endYear+"-"+(endMonth+1)+"-"+endDay;
								long beginTime = getDateTime(beginDate);
								long endTime = getDateTime(endDate);
								if(beginTime > endTime){
									String tempDate = beginDate;
									beginDate = endDate;
									endDate = tempDate;
									long tempTime = beginTime;
									beginTime = endTime;
									endTime = tempTime;
								}
								if(beginTime != 0 && endTime != 0){
									long dayCount = (endTime - beginTime)/(1000*3600*24);
									Log.d("dayCount", dayCount+"天");
									if( dayCount > 31){
										Toast.makeText(ViewSignalDataActivity.this, "时间间隔不超过31天!", Toast.LENGTH_SHORT).show();
									}else{
										beginDateView.setText(beginDate);
										endDateView.setText(endDate);
									}
								}
						}
					}, year, month, day);
				}
				datePickerDialog.show();
				datePickerDialog.setCanceledOnTouchOutside(true);
				chooseBtn.setClickable(true);
			}
		});
		beginDateView.setText(beginDate);
		endDateView.setText(endDate);
	}
	
	public long getDateTime(String dateStr){
		Calendar calendar = Calendar.getInstance();
		long time = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = sdf.parse(dateStr);
			calendar.setTime(date);
			time = calendar.getTimeInMillis();
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}
		return time;
	}
	
	public void requestPoints(Map<String, Object> paramsMap){
		
	}
}
