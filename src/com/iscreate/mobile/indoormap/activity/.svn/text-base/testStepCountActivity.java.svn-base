package com.iscreate.mobile.indoormap.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.iscreate.mobile.indoormap.R;
import com.iscreate.mobile.indoormap.widget.AccelerometerEvent;
import com.iscreate.mobile.widget.PopupNormalWindow;

public class testStepCountActivity extends stepDetecterByNetActivity {
	private List<String> loglist = new ArrayList<String>();
	private ListView log_lv = null;
	private View contentView = null;
	private static String ErrorFileName = null;
	private long steps = 0;
	private final int WHAT_UPDATESTEP = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView = LayoutInflater.from(this)
				.inflate(R.layout.teststep, null);
		setContentView(contentView);
		((ToggleButton) findViewById(R.id.tb_write))
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							View contentView = LayoutInflater.from(
									testStepCountActivity.this).inflate(
									R.layout.popupenterfilename, null);
							final PopupNormalWindow popupWin = new PopupNormalWindow(
									contentView,
									LinearLayout.LayoutParams.MATCH_PARENT,
									LinearLayout.LayoutParams.MATCH_PARENT);
							final EditText et_filename = (EditText) contentView
									.findViewById(R.id.et_filename);
							et_filename
									.setOnKeyListener(new View.OnKeyListener() {
										@Override
										public boolean onKey(View v,
												int keyCode, KeyEvent event) {
											switch (keyCode) {
											case KeyEvent.KEYCODE_BACK:
												popupWin.dismiss();
												return (true);
											}
											return (false);
										}
									});
							contentView.findViewById(R.id.bt_confirm)
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											ErrorFileName = et_filename
													.getText().toString();
											if (ErrorFileName != null
													&& ErrorFileName.length() > 0) {
												popupWin.dismiss();
												resetStep();
											} else {
												Toast.makeText(
														testStepCountActivity.this,
														"请重新输入！",
														Toast.LENGTH_SHORT)
														.show();
											}
										}
									});
							contentView.findViewById(R.id.bt_cancel)
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											popupWin.dismiss();
											ErrorFileName = null;
										}
									});
							popupWin.showAtLocation(contentView,
									Gravity.CENTER, 0, 0);
						} else {
							if (ErrorFileName != null) {
								List<AccelerometerEvent> accelerometerEventList = getAccelerometerEventList();
								if (accelerometerEventList != null) {
									String filename = ErrorFileName;
									ErrorFileName = filename + ".txt";
									List<AccelerometerEvent> accelerometerEventList1 = new ArrayList<AccelerometerEvent>();
									accelerometerEventList1
											.addAll(accelerometerEventList);
									eraseErrorLog();
									writeErrorLog("\tC\tY\tZ");
									long startTime = 0;
									if (accelerometerEventList1.size() > 0) {
										startTime = accelerometerEventList1
												.get(0).t;
									}
									for (AccelerometerEvent accelerometerEvent : accelerometerEventList1) {
										float difft = ((float) (accelerometerEvent.t - startTime)) / 10;
										writeErrorLog(""
												+ DecimalFormatCoord(difft)
												+ "\t"
												+ DecimalFormatCoord(accelerometerEvent.a)
												+ "\t"
												+ DecimalFormatCoord(accelerometerEvent.y)
												+ "\t"
												+ DecimalFormatCoord(accelerometerEvent.z));
									}
									ErrorFileName = filename + "_filter.txt";
									eraseErrorLog();
									writeErrorLog("\tCF\tYF\tZF");
									accelerometerEventList1 = filterEvent(accelerometerEventList1);
									if (accelerometerEventList1.size() > 0) {
										startTime = accelerometerEventList1
												.get(0).t;
									}
									for (AccelerometerEvent accelerometerEvent : accelerometerEventList1) {
										float difft = ((float) (accelerometerEvent.t - startTime)) / 10;
										writeErrorLog(""
												+ DecimalFormatCoord(difft)
												+ "\t"
												+ DecimalFormatCoord(accelerometerEvent.a)
												+ "\t"
												+ DecimalFormatCoord(accelerometerEvent.y)
												+ "\t"
												+ DecimalFormatCoord(accelerometerEvent.z));
									}
								}
								ErrorFileName = null;
							}
						}
					}
				});
		log_lv = (ListView) findViewById(R.id.log_lv);
		log_lv.setFastScrollEnabled(true);
		Button function_btn = (Button) findViewById(R.id.btn_fun);
		function_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (log_lv.getVisibility() == View.VISIBLE) {
					log_lv.setVisibility(View.GONE);
				} else {
					log_lv.setVisibility(View.VISIBLE);
				}
			}
		});
		log_lv.setAdapter(new BaseAdapter() {

			@Override
			public int getCount() {
				return ((loglist == null) ? 0 : loglist.size());
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				TextView tv = new TextView(testStepCountActivity.this);
				tv.setTextColor(Color.RED);
				tv.setText(loglist.get(position));
				return tv;
			}

			@Override
			public Object getItem(int position) {
				return null;
			}

			@Override
			public long getItemId(int position) {
				return 0;
			}
		});
		log_lv.setVisibility(View.VISIBLE);
		Timer timer = new Timer("UpdateLog", false);
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				Message msg = handler.obtainMessage();
				msg.what = WHAT_UPDATESTEP;
				msg.obj = null;
				long stepsnow = getSteps();
				if (stepsnow > steps) {
					steps = stepsnow;
					msg.obj = "" + steps;
				}
				handler.sendMessage(msg);
			}
		};
		timer.schedule(task, 0, 3000);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case WHAT_UPDATESTEP:
				if (msg.obj != null) {
					updateLog((String) msg.obj);
				}
				break;
			}
		}
	};

	private List<AccelerometerEvent> filterEvent(
			List<AccelerometerEvent> accelerometerList) {
		List<AccelerometerEvent> accelerometerListnew = new ArrayList<AccelerometerEvent>();
		int i = 0;
		int count = accelerometerList.size();
		AccelerometerEvent accelerometerEventi;
		AccelerometerEvent newaccelerometerEventlast = null;
		long stime = 0;
		while (i < count) {
			accelerometerEventi = accelerometerList.get(i);
			if (accelerometerListnew.size() == 0) {
				newaccelerometerEventlast = accelerometerEventi;
				stime = newaccelerometerEventlast.t;
				accelerometerListnew.add(newaccelerometerEventlast);
				accelerometerList.remove(i);
				--count;
			} else {
				long needtime = stime
						+ ((newaccelerometerEventlast.t - stime) / 10 + 1) * 10;
				if (accelerometerEventi.t <= needtime) {
					newaccelerometerEventlast = accelerometerEventi;
					accelerometerListnew.add(newaccelerometerEventlast);
					accelerometerList.remove(i);
					--count;
				} else {
					if (accelerometerEventi.t > needtime) {
						AccelerometerEvent accelerometerEventtemp = new AccelerometerEvent();
						accelerometerEventtemp.t = needtime;
						accelerometerEventtemp.a = getRightValue(
								newaccelerometerEventlast.t - stime,
								newaccelerometerEventlast.a,
								accelerometerEventi.t - stime,
								accelerometerEventi.a, needtime - stime);
						accelerometerEventtemp.x = getRightValue(
								newaccelerometerEventlast.t - stime,
								newaccelerometerEventlast.x,
								accelerometerEventi.t - stime,
								accelerometerEventi.x, needtime - stime);
						accelerometerEventtemp.y = getRightValue(
								newaccelerometerEventlast.t - stime,
								newaccelerometerEventlast.y,
								accelerometerEventi.t - stime,
								accelerometerEventi.y, needtime - stime);
						accelerometerEventtemp.z = getRightValue(
								newaccelerometerEventlast.t - stime,
								newaccelerometerEventlast.z,
								accelerometerEventi.t - stime,
								accelerometerEventi.z, needtime - stime);
						newaccelerometerEventlast = accelerometerEventtemp;
						accelerometerListnew.add(newaccelerometerEventlast);
					} else {
						++i;
					}
				}
			}
		}
		i = 0;
		count = accelerometerListnew.size();
		while (i < count) {
			accelerometerEventi = accelerometerListnew.get(i);
			if ((accelerometerEventi.t - stime) % 10 != 0) {
				accelerometerListnew.remove(i);
				--count;
			} else {
				++i;
			}
		}
		return (accelerometerListnew);
	}

	private float getRightValue(long x0, float y0, long x1, float y1, long x) {
		if (y0 != y1) {
			double a = (double) (y1 - y0) / (double) (x1 - x0);
			double b = (((double) y0 * (double) x1) - ((double) y1 * (double) x0))
					/ (double) (x1 - x0);
			float y = (float) (a * (double) x + b);
			return (y);
		}
		return (y0);
	}

	private void updateLog(String log) {
		loglist.add(log);
		((BaseAdapter) log_lv.getAdapter()).notifyDataSetChanged();
	}

	private static String DecimalFormatCoord(float f) {
		DecimalFormat df = new DecimalFormat("0.00000000000000000000000000000");
		String s = df.format(f);
		if (s.indexOf("E") != -1) {
			s = "" + f;
		}
		return (s);
	}

	private static boolean eraseErrorLog() {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			File extsddir = Environment.getExternalStorageDirectory();
			if (extsddir.canWrite()) {
				String photofolderpath = Environment
						.getExternalStorageDirectory() + "/iscreate";
				File pathFile = new File(photofolderpath);
				if ((!pathFile.exists()) || (!pathFile.isDirectory())) {
					pathFile.mkdirs();
				}
				String photoPath = photofolderpath + "/" + ErrorFileName;
				File file = new File(photoPath);
				FileOutputStream fos = null;
				try {
					if (file.exists()) {
						fos = new FileOutputStream(file, false);
					}
				} catch (Exception e) {
				}
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
					}
				}
			}
		}
		return (false);
	}

	private static boolean writeErrorLog(String error) {
		if (error == null) {
			error = "\n";
		} else {
			error = error + "\n";
		}
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			File extsddir = Environment.getExternalStorageDirectory();
			if (extsddir.canWrite()) {
				String photofolderpath = Environment
						.getExternalStorageDirectory() + "/iscreate";
				File pathFile = new File(photofolderpath);
				if ((!pathFile.exists()) || (!pathFile.isDirectory())) {
					pathFile.mkdirs();
				}
				String photoPath = photofolderpath + "/" + ErrorFileName;
				File file = new File(photoPath);
				FileOutputStream fos = null;
				try {
					if (file.exists()) {
						fos = new FileOutputStream(file, true);
					} else {
						fos = new FileOutputStream(file);
					}
				} catch (Exception e) {
				}
				if (fos != null) {
					try {
						fos.write(error.getBytes());
						return (true);
					} catch (Exception e) {
					} finally {
						try {
							fos.close();
						} catch (Exception e) {
						}
					}
				}
			}
		}
		return (false);
	}
}
