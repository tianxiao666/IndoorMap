package com.iscreate.mobile.indoormap.activity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.os.Bundle;

import com.iscreate.mobile.utils.StepDetecter;

public class stepDetecterByNetActivity extends stepDetecterActivity {
	/**
	 * a detecter for detect steps
	 */
	private StepDetecter stepDetecter = null;
	/**
	 * the steps that the detecter detected
	 */
	private long steps = 0;
	public static final long INTERVAL_MS = 1000 / 30;

	/**
	 * 使用StepDetecter计算步数，其它为使用波峰计算步数
	 */
	public final int STEP_MODE_NET = 0;
	/**
	 * 默认为使用使用波峰计算步数
	 */
	private int stepMode = 1;

	/**
	 * detect the sensor on this device
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		stepDetecter = new StepDetecter() {
			@Override
			public void onStep() {
				++steps;
			}
		};
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		switch (event.sensor.getType()) {
		case Sensor.TYPE_ACCELEROMETER:
			stepDetecter.setLastAcc(event.values);
			break;
		}
	}

	public void setStepMode(int mode) {
		stepMode = mode;
	}

	/**
	 * call this to judge a new step
	 */
	public void updateStepDetecter() {
		stepDetecter.updateData();
	}

	/**
	 * reset the step to 0
	 */
	@Override
	public void resetStep() {
		if (stepMode == 0) {
			steps = 0;
		} else {
			super.resetStep();
		}
	}

	/**
	 * get the steps
	 */
	@Override
	public long getSteps() {
		if (stepMode == 0) {
			return (steps);
		} else {
			return (super.getSteps());
		}
	}
}