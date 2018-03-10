package com.iscreate.mobile.indoormap.activity;

import java.util.ArrayList;
import java.util.List;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Toast;

import com.iscreate.mobile.indoormap.widget.AccelerometerEvent;
import com.iscreate.mobile.utils.AnalyseWave;

public abstract class stepDetecterActivity extends LogLifecircleActivity {
	/**
	 * SensorManager lets you access the device's sensors.
	 */
	private SensorManager sensorManager = null;
	/**
	 * whether your device has a accelerometer sensor
	 */
	private boolean hasAccelerometer = false;
	/**
	 * whether your device has a magnetic sensor
	 */
	private boolean hasMagneticField = false;
	/**
	 * whether your device has a orientation sensor
	 */
	private boolean hasOrientation = false;
	/**
	 * the direction of the device
	 */
	private float direction = 0;
	/**
	 * the steps that the detecter detected
	 */
	private long steps = 0;
	/**
	 * 存储加速度列表
	 */
	private List<AccelerometerEvent> accelerometerEventList = null;

	public abstract void onSensorChanged(SensorEvent event);

	/**
	 * the sensor event listener, calculate the steps via SensorEvent
	 */
	private SensorEventListener sensorEventListener = new SensorEventListener() {
		@Override
		public void onSensorChanged(SensorEvent event) {
			switch (event.sensor.getType()) {
			case Sensor.TYPE_ACCELEROMETER:
				appendAccelerometerEvent(event);
				break;
			case Sensor.TYPE_ORIENTATION:
				direction = event.values[SensorManager.DATA_X];
				break;
			}
			stepDetecterActivity.this.onSensorChanged(event);
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
	};

	/**
	 * detect the sensor on this device
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		List<Sensor> sensorlist = sensorManager.getSensorList(Sensor.TYPE_ALL);
		for (Sensor sensor : sensorlist) {
			if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				hasAccelerometer = true;
			}
			if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
				hasMagneticField = true;
			}
			if (sensor.getType() == Sensor.TYPE_ORIENTATION) {
				hasOrientation = true;
			}
			if (hasAccelerometer && hasMagneticField && hasOrientation) {
				break;
			}
		}
		if (!hasAccelerometer) {
			Toast.makeText(this, "该设备没有加速力传感器,无法计算移动步数!", Toast.LENGTH_SHORT)
					.show();
		}
		if (!hasMagneticField) {
			Toast.makeText(this, "该设备没有磁力传感器!", Toast.LENGTH_SHORT).show();
		}
		if (!hasOrientation) {
			Toast.makeText(this, "该设备没有方向传感器,无法计算方向!", Toast.LENGTH_SHORT)
					.show();
		}
	}

	/**
	 * register the sensor listener
	 */
	@Override
	protected void onResume() {
		super.onResume();
		if (hasAccelerometer) {
			sensorManager.registerListener(sensorEventListener,
					sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
					SensorManager.SENSOR_DELAY_NORMAL);
		}
		if (hasMagneticField) {
			sensorManager.registerListener(sensorEventListener,
					sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
					SensorManager.SENSOR_DELAY_NORMAL);
		}
		if (hasOrientation) {
			sensorManager.registerListener(sensorEventListener,
					sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
					SensorManager.SENSOR_DELAY_NORMAL);
		}
	}

	/**
	 * unregister the sensor listener
	 */
	@Override
	protected void onPause() {
		sensorManager.unregisterListener(sensorEventListener);
		super.onPause();
	}

	/**
	 * 保存加速力
	 * 
	 * @param event
	 *            TYPE_ACCELEROMETER SensorEvent
	 */
	private void appendAccelerometerEvent(SensorEvent event) {
		AccelerometerEvent accelerometerEvent = new AccelerometerEvent();
		accelerometerEvent.t = System.currentTimeMillis();
		accelerometerEvent.x = event.values[SensorManager.DATA_X];
		accelerometerEvent.y = event.values[SensorManager.DATA_Y];
		accelerometerEvent.z = event.values[SensorManager.DATA_Z];
		accelerometerEvent.a = (float) Math.sqrt(accelerometerEvent.x
				* accelerometerEvent.x + accelerometerEvent.y
				* accelerometerEvent.y + accelerometerEvent.z
				* accelerometerEvent.z);
		if (accelerometerEventList == null) {
			accelerometerEventList = new ArrayList<AccelerometerEvent>();
		}
		accelerometerEventList.add(accelerometerEvent);
		if (accelerometerEventList.size() > 5000) {
			steps = getSteps();
			accelerometerEventList.clear();
		}
	}

	public List<AccelerometerEvent> getAccelerometerEventList() {
		return (accelerometerEventList);
	}

	/**
	 * get the steps
	 */
	public long getSteps() {
		if (accelerometerEventList == null) {
			accelerometerEventList = new ArrayList<AccelerometerEvent>();
		}
		List<AccelerometerEvent> accelerometerEventListTemp = new ArrayList<AccelerometerEvent>();
		accelerometerEventListTemp.addAll(accelerometerEventList);
		AnalyseWave.FilterAccelerometerEventList(accelerometerEventListTemp);
		// AnalyseWave.getWaveCrestCount(accelerometerEventListTemp);
		return (steps + AnalyseWave
				.CountStepsAccelerometerEventList(accelerometerEventListTemp));
	}

	/**
	 * get the device direction
	 */
	public float getDirection() {
		return (direction);
	}

	/**
	 * reset
	 */
	public void resetStep() {
		steps = 0;
		if (accelerometerEventList != null) {
			accelerometerEventList.clear();
		}
	}
}