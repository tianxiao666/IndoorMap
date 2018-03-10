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
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.iscreate.mobile.indoormap.R;
import com.iscreate.mobile.indoormap.widget.AccelerometerEvent;
import com.iscreate.mobile.utils.AnalyseWave;

public class testGyroscopeActivity extends stepDetecterByNetActivity {
	private List<String> loglist = new ArrayList<String>();
	private ListView log_lv = null;
	private CountDownTimer countDownTimer = null;
	private long startTime = 0;
	private final int WHAT_UPDATEACC = 0;
	private List<AccelerometerEvent> accelerometerEventList = null;
	private Timer timer = null;
	private float[] value_TYPE_ACCELEROMETER = null;
	private float[] value_TYPE_ORIENTATION = null;
	private float[] value_TYPE_MAGNETIC_FIELD = null;
	private float pitch = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.testgyroscope);
		final Button stop_btn = (Button) findViewById(R.id.stop_btn);
		final Button shake_btn = (Button) findViewById(R.id.shake_btn);
		final Button move_btn = (Button) findViewById(R.id.move_btn);
		stop_btn.setBackgroundResource(R.drawable.bg_button);
		shake_btn.setBackgroundResource(R.drawable.bg_button);
		move_btn.setBackgroundResource(R.drawable.bg_button);
		stop_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Button btn = (Button) v;
				loglist.clear();
				if (btn.getTag() == null) {
					setButton(stop_btn, true);
					setButton(shake_btn, false);
					setButton(move_btn, false);
					ErrorFileName = "stop.log";
					startTime = System.currentTimeMillis();
					accelerometerEventList.clear();
					resetStep();
					eraseErrorLog();
				} else {
					setButton(stop_btn, false);
					setButton(shake_btn, false);
					setButton(move_btn, false);
					eraseErrorLog();
					if (accelerometerEventList != null) {
						writeErrorLog("\tOriginal combined Accelerating Force\tCombined Accelerating Force - GRAVITY_EARTH\tZ asix Accelerating Force - GRAVITY_EARTH\tZ asix Accelerating Force\tY asix Accelerating Force\tX asix Accelerating Force");
						for (AccelerometerEvent accelerometerEvent : accelerometerEventList) {
							if (ErrorFileName != null) {
								float difft = ((float) (accelerometerEvent.t - startTime)) / 100;
								writeErrorLog(""
										+ DecimalFormatCoord(difft)
										+ "\t"
										+ DecimalFormatCoord(accelerometerEvent.a)
										+ "\t"
										+ DecimalFormatCoord(accelerometerEvent.a
												- SensorManager.GRAVITY_EARTH)
										+ "\t"
										+ DecimalFormatCoord(accelerometerEvent.z
												- SensorManager.GRAVITY_EARTH)
										+ "\t"
										+ DecimalFormatCoord(accelerometerEvent.z)
										+ "\t"
										+ DecimalFormatCoord(accelerometerEvent.y)
										+ "\t"
										+ DecimalFormatCoord(accelerometerEvent.x));
							}
						}
						AnalyseWave
								.FilterAccelerometerEventList(accelerometerEventList);
						ErrorFileName = "stopfilter.log";
						eraseErrorLog();
						writeErrorLog("\tFiltered combined Accelerating Force\tCombined Accelerating Force - GRAVITY_EARTH\tZ asix Accelerating Force - GRAVITY_EARTH\tZ asix Accelerating Force\tY asix Accelerating Force\tX asix Accelerating Force");
						for (AccelerometerEvent accelerometerEvent : accelerometerEventList) {
							if (ErrorFileName != null) {
								float difft = ((float) (accelerometerEvent.t - startTime)) / 100;
								writeErrorLog(""
										+ DecimalFormatCoord(difft)
										+ "\t"
										+ DecimalFormatCoord(accelerometerEvent.a)
										+ "\t"
										+ DecimalFormatCoord(accelerometerEvent.a
												- SensorManager.GRAVITY_EARTH)
										+ "\t"
										+ DecimalFormatCoord(accelerometerEvent.z
												- SensorManager.GRAVITY_EARTH)
										+ "\t"
										+ DecimalFormatCoord(accelerometerEvent.z)
										+ "\t"
										+ DecimalFormatCoord(accelerometerEvent.y)
										+ "\t"
										+ DecimalFormatCoord(accelerometerEvent.x));
							}
						}
					}
					ErrorFileName = null;
				}
			}
		});
		shake_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				loglist.clear();
				Button btn = (Button) v;
				if (btn.getTag() == null) {
					setButton(stop_btn, false);
					setButton(shake_btn, true);
					setButton(move_btn, false);
					ErrorFileName = "shake.log";
					startTime = System.currentTimeMillis();
					eraseErrorLog();
				} else {
					setButton(stop_btn, false);
					setButton(shake_btn, false);
					setButton(move_btn, false);
					ErrorFileName = null;
				}
			}
		});
		move_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				loglist.clear();
				Button btn = (Button) v;
				if (btn.getTag() == null) {
					setButton(stop_btn, false);
					setButton(shake_btn, false);
					setButton(move_btn, true);
					ErrorFileName = "move.log";
					startTime = System.currentTimeMillis();
					eraseErrorLog();
				} else {
					setButton(stop_btn, false);
					setButton(shake_btn, false);
					setButton(move_btn, false);
					ErrorFileName = null;
				}
			}
		});
		log_lv = (ListView) findViewById(R.id.log_lv);
		log_lv.setFastScrollEnabled(true);
		log_lv.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction() & 0x000000ff;
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					if (countDownTimer != null) {
						countDownTimer.cancel();
					}
					break;
				case MotionEvent.ACTION_UP:
					countDownTimer = new CountDownTimer(2000, 2000) {
						@Override
						public void onTick(long millisUntilFinished) {
						}

						@Override
						public void onFinish() {
							countDownTimer = null;
						}
					};
					countDownTimer.start();
					break;
				}
				return false;
			}
		});
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
		log_lv.setVisibility(View.VISIBLE);
		/*
		 * ErrorFileName = "stop.log"; eraseErrorLog(); ErrorFileName =
		 * "shake.log"; eraseErrorLog(); ErrorFileName = "move.log";
		 * eraseErrorLog();
		 */
		ErrorFileName = null;
		startTime = System.currentTimeMillis();
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		switch (event.sensor.getType()) {
		case Sensor.TYPE_ACCELEROMETER:
			value_TYPE_ACCELEROMETER = event.values.clone();
			initTimer();
			analyseAccelerometerEvent(event);
			break;
		case Sensor.TYPE_ORIENTATION:
			value_TYPE_ORIENTATION = event.values.clone();
			analyseOrientationEvent(event);
			break;
		case Sensor.TYPE_MAGNETIC_FIELD:
			value_TYPE_MAGNETIC_FIELD = event.values.clone();
			break;
		}
	}

	private Float AnalyseSensorEvent() {
		if ((value_TYPE_ACCELEROMETER != null)
				&& (value_TYPE_ORIENTATION != null)) {
			float azimuth = value_TYPE_ORIENTATION[SensorManager.DATA_X];
			float pitch = value_TYPE_ORIENTATION[SensorManager.DATA_Y];
			float roll = value_TYPE_ORIENTATION[SensorManager.DATA_Z];
			float[] fy = getHorizontalForceInY(pitch,
					value_TYPE_ACCELEROMETER[1]);
			float[] fz = getHorizontalForceInZ(pitch,
					value_TYPE_ACCELEROMETER[2]);
			float fh = fy[0] + fz[0];
			float fv = fy[1] + fz[1];
			return (fh);
		}
		return (null);
	}

	private Float AnalyseSensorEvent1() {
		if (value_TYPE_ORIENTATION != null) {
			float azimuth = value_TYPE_ORIENTATION[SensorManager.DATA_X];
			float pitch = value_TYPE_ORIENTATION[SensorManager.DATA_Y];
			float roll = value_TYPE_ORIENTATION[SensorManager.DATA_Z];
			final int matrix_size = 16;
			float[] R = new float[matrix_size];
			float[] outR = new float[matrix_size];
			float[] I = new float[matrix_size];
			float[] values = new float[3];
			if ((value_TYPE_ACCELEROMETER != null)
					&& (value_TYPE_MAGNETIC_FIELD != null)) {
				SensorManager.getRotationMatrix(R, I, value_TYPE_ACCELEROMETER,
						value_TYPE_MAGNETIC_FIELD);
				SensorManager.getOrientation(R, values);
				boolean resutl = SensorManager.remapCoordinateSystem(R,
						SensorManager.AXIS_Z, SensorManager.AXIS_MINUS_Y, outR);
				float incl = SensorManager.getInclination(I);
				float x1 = (float) Math.toDegrees(values[0]);
				float y1 = (float) Math.toDegrees(values[1]);
				float z1 = (float) Math.toDegrees(values[2]);
				float i1 = (float) Math.toDegrees(incl);
				float[] fy = getHorizontalForceInY(pitch,
						value_TYPE_ACCELEROMETER[1]);
				float[] fz = getHorizontalForceInZ(pitch,
						value_TYPE_ACCELEROMETER[2]);
				float fh = fy[0] + fz[0];
				float fv = fy[1] + fz[1];
				Log.e("test", "" + roll);
				return (fh);
			}
		}
		return (null);
	}

	private float[] getHorizontalForceInY(float pitch, float forceinY) {
		float[] force = new float[2];
		force[0] = (float) ((forceinY) * Math.cos(Math.toRadians(pitch)));
		force[1] = (float) ((forceinY) * (Math.sin(Math.toRadians(180 + pitch))));
		return (force);
	}

	private float[] getHorizontalForceInZ(float pitch, float forceinZ) {
		float[] force = new float[2];
		pitch = pitch - 90;
		force[0] = (float) ((forceinZ) * Math.cos(Math.toRadians(pitch)));
		force[1] = (float) ((forceinZ) * (Math.sin(Math.toRadians(180 + pitch))));
		return (force);
	}

	private void setButton(Button btn, boolean clicked) {
		if (clicked) {
			btn.setTag("");
			btn.setBackgroundResource(R.drawable.bg_button_focus);
		} else {
			btn.setTag(null);
			btn.setBackgroundResource(R.drawable.bg_button);
		}
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case WHAT_UPDATEACC:
				updateStepDetecter();
				break;
			}
		}
	};

	private void initTimer() {
		if (timer == null) {
			timer = new Timer("UpdateData", false);
			TimerTask task = new TimerTask() {
				@Override
				public void run() {
					Message msg = handler.obtainMessage();
					msg.what = WHAT_UPDATEACC;
					handler.sendMessage(msg);
				}
			};
			timer.schedule(task, 0, stepDetecterByNetActivity.INTERVAL_MS);
		}
	}

	private void analyseAccelerometerEvent(SensorEvent event) {
		AccelerometerEvent accelerometerEvent = new AccelerometerEvent();
		accelerometerEvent.t = System.currentTimeMillis();
		accelerometerEvent.x = event.values[SensorManager.DATA_X];
		accelerometerEvent.y = event.values[SensorManager.DATA_Y];
		accelerometerEvent.z = event.values[SensorManager.DATA_Z];
		accelerometerEvent.a = AnalyseSensorEvent();
		/*
		 * float[] forcey = getPitchForceInY(accelerometerEvent.y); float[]
		 * forcez = getPitchForceInZ(accelerometerEvent.z); if (forcey != null
		 * && forcez != null) { float a = forcey[0] + forcez[0]; float b =
		 * forcey[1] + forcez[1]; accelerometerEvent.a = (float) Math.sqrt(a * a
		 * + b * b); accelerometerEventList.add(accelerometerEvent); }
		 */
		if (accelerometerEventList == null) {
			accelerometerEventList = new ArrayList<AccelerometerEvent>();
		}
		// Log.e("test", "" + accelerometerEvent.a);
		// Log.e("test", "" + accelerometerEvent.x + "  - " +
		// accelerometerEvent.y
		// + " - " + accelerometerEvent.z);
		accelerometerEventList.add(accelerometerEvent);
		// List<AccelerometerEvent> accelerometerEventList1 = new
		// ArrayList<AccelerometerEvent>();
		// accelerometerEventList1.addAll(accelerometerEventList);
		// AnalyseWave.FilterAccelerometerEventList(accelerometerEventList1);
		// updateLog("    波峰: "
		// + AnalyseWave.getWaveCrestCount(accelerometerEventList1)
		// + "    步数: "
		// + AnalyseWave
		// .CountStepsAccelerometerEventList(accelerometerEventList1)
		// + "    WIFI计算步数: " + steps);
	}

	private void analyseOrientationEvent(SensorEvent event) {
		float x = event.values[SensorManager.DATA_X];
		pitch = event.values[SensorManager.DATA_Y];
		float z = event.values[SensorManager.DATA_Z];
		float[] values = new float[3];
		float[] R = new float[9];
		float[] I = new float[16];
		if ((value_TYPE_ACCELEROMETER != null)
				&& (value_TYPE_MAGNETIC_FIELD != null)) {
			SensorManager.getRotationMatrix(R, I, value_TYPE_ACCELEROMETER,
					value_TYPE_MAGNETIC_FIELD);
			SensorManager.getOrientation(R, values);
			float x1 = (float) Math.toDegrees(values[0]);
			float y1 = (float) Math.toDegrees(values[1]);
			float z1 = (float) Math.toDegrees(values[2]);
			// Log.e("test0", "" + x + " - " + pitch + " - " + z);
			// Log.e("test1", "" + x1 + " - " + y1 + " - " + z1);
			float incl = SensorManager.getInclination(I);
			final double rad2deg = (double) (180.0d / Math.PI);
			// Log.d("Compass", "yaw: " + (values[0] * rad2deg) + "  pitch: "
			// + (values[1] * rad2deg) + "  roll: "
			// + (values[2] * rad2deg) + "  incl: " + (incl * rad2deg));
		}
	}

	private String getDerectionString(SensorEvent event) {
		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];
		int derection = (int) (x / 90);
		String derectionstr = "";
		switch (derection) {
		case 0:
			break;
		case 3:
		case 1:
		case 2:
		}
		return (derectionstr);
	}

	private void AccelerometerEventListFliter(
			List<AccelerometerEvent> accelerometerEventList) {
		int mode = 2;
		int i = 0;
		int count = accelerometerEventList.size();
		while (i < count) {
			switch (mode) {
			case 2:
				if (AnalyseWave.isWaveTrough(accelerometerEventList, i)) {
					mode = 1;
				} else {
					if (AnalyseWave.isWaveCrest(accelerometerEventList, i)) {
						mode = 0;
					}
				}
				break;
			case 0:
				if (AnalyseWave.isWaveTrough(accelerometerEventList, i)) {
					mode = 1;
				}
				break;
			case 1:
				if (AnalyseWave.isWaveCrest(accelerometerEventList, i)) {
					mode = 0;
				}
				break;
			}
			++i;
		}
	}

	private int seekWakeDiff(AccelerometerEvent accelerometerEvent,
			List<AccelerometerEvent> accelerometerEventList, int start) {
		while (start < accelerometerEventList.size()) {
			if ((accelerometerEvent.a != accelerometerEventList.get(start).a)
					&& (accelerometerEventList.get(start).a != 0)) {
				return (start);
			}
			++start;
		}
		return (-1);
	}

	private int analyseAccelerometerEventList(
			List<AccelerometerEvent> accelerometerEventList) {
		int WaveCount = 0;
		int mode = 2;
		int position = 0;
		int modestart = 2;
		int i = 0;
		int count = accelerometerEventList.size();
		while (i < count) {
			switch (mode) {
			case 2:
				if (AnalyseWave.isWaveTrough(accelerometerEventList, i)) {
					mode = 1;
					modestart = 0;
					position = i;
				} else {
					if (AnalyseWave.isWaveCrest(accelerometerEventList, i)) {
						mode = 0;
						modestart = 1;
						position = i;
					}
				}
				break;
			case 0:
				if (AnalyseWave.isWaveTrough(accelerometerEventList, i)) {
					if (mode == modestart) {
						if (AnalyseWave.isStepTimeDiff(accelerometerEventList
								.get(i).t
								- accelerometerEventList.get(position).t)) {
							++WaveCount;
						}
					}
					mode = 1;
					position = i;
				}
				break;
			case 1:
				if (AnalyseWave.isWaveCrest(accelerometerEventList, i)) {
					if (mode == modestart) {
						if (AnalyseWave.isStepTimeDiff(accelerometerEventList
								.get(i).t
								- accelerometerEventList.get(position).t)) {
							++WaveCount;
						}
					}
					mode = 0;
					position = i;
				}
				break;
			}
			++i;
		}
		// updateLog("" + WaveCount);
		return (WaveCount);
	}

	private void updateLog(String log) {
		loglist.add(log);
		log_lv.setAdapter(new BaseAdapter() {

			@Override
			public int getCount() {
				return loglist.size();
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				TextView tv = new TextView(testGyroscopeActivity.this);
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
	}

	private static String DecimalFormatCoord(float f) {
		DecimalFormat df = new DecimalFormat("0.00000000000000000000000000000");
		return (df.format(f));
	}

	private static String ErrorFileName = null;

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