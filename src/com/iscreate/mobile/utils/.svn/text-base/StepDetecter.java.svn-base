package com.iscreate.mobile.utils;

public abstract class StepDetecter {
	/**
	 * the time between two adjacent steps
	 */
	private final int step_timeout_ms = 666;
	/**
	 * unknow
	 */
	private final double peak = 0.5f;
	/**
	 * the size of a array for acc data
	 */
	private final int vhSize = 6;
	/**
	 * the last acc
	 */
	private float[] lastAcc = new float[] { 0f, 0f, 0f };
	/**
	 * a array for acc data
	 */
	private double[] values_history = new double[vhSize];
	/**
	 * a index pointer to values_history
	 */
	private int vhPointer = 0;
	/**
	 * the time when last step occurred
	 */
	private long last_step_ts = 0;

	/**
	 * call this when a new step occurred
	 */
	public abstract void onStep();

	/**
	 * set lastAcc
	 * 
	 * @param acc
	 *            the src acc
	 */
	public void setLastAcc(float[] acc) {
		System.arraycopy(acc, 0, lastAcc, 0, 3);
	}

	/**
	 * judge a new step, call onStep if true
	 */
	public void updateData() {
		long now_ms = System.currentTimeMillis();
		double lOld_z = lastAcc[2];
		addData(lOld_z);
		if ((now_ms - last_step_ts) > step_timeout_ms && checkForStep(peak)) {
			last_step_ts = now_ms;
			onStep();
		}
	}

	/**
	 * add acc
	 * 
	 * @param value
	 *            acc
	 */
	private void addData(double value) {
		values_history[vhPointer % vhSize] = value;
		vhPointer++;
		vhPointer = vhPointer % vhSize;
	}

	/**
	 * judge a new step by peak
	 * 
	 * @param peakSize
	 *            peak
	 * @return true if a new steps occurred
	 */
	private boolean checkForStep(double peakSize) {
		int last = (vhPointer - 1 + vhSize) % vhSize;
		int loop = 0;
		for (int t = 1; t < vhSize; t++) {
			loop = (vhPointer - 1 + vhSize - t + vhSize) % vhSize;
			if ((values_history[loop] - values_history[last] > peakSize)) {
				return (true);
			}
		}
		return (false);
	}
}