package com.iscreate.mobile.utils;

import java.util.List;

import android.hardware.SensorManager;

import com.iscreate.mobile.indoormap.widget.AccelerometerEvent;

public class AnalyseWave {
	/**
	 * 
	 * judge this accelerometerEventList is a wave trough or not
	 * 
	 * @param accelerometerEventList
	 *            a list of accelerometer Event
	 * @param position
	 *            the position which you have to judge
	 * @return true if the accelerometerEventList is a wave trough,otherwise
	 *         false
	 */
	public static boolean isWaveTrough(
			List<AccelerometerEvent> accelerometerEventList, int position) {
		if ((accelerometerEventList != null) && (position >= 0)) {
			int accelerometerEventListSize = accelerometerEventList.size();
			if (position < accelerometerEventListSize) {
				int positionPrev = position - 1;
				int positionNext = position + 1;
				AccelerometerEvent accelerometerEventPosition = accelerometerEventList
						.get(position);
				if (positionPrev >= 0) {
					AccelerometerEvent accelerometerEventPrev = accelerometerEventList
							.get(positionPrev);
					if (accelerometerEventPosition.a > accelerometerEventPrev.a) {
						return (false);
					}
				}
				if (positionNext < accelerometerEventListSize) {
					AccelerometerEvent accelerometerEventNext = accelerometerEventList
							.get(positionNext);
					if (accelerometerEventPosition.a > accelerometerEventNext.a) {
						return (false);
					}
				}
				return (true);
			}
		}
		return (false);
	}

	/**
	 * judge this accelerometerEventList is a wave crest or not
	 * 
	 * @param accelerometerEventList
	 *            a list of accelerometer Event
	 * @param position
	 *            the position which you have to judge
	 * @return true if the accelerometerEventList is a wave crest,otherwise
	 *         false
	 */
	public static boolean isWaveCrest(
			List<AccelerometerEvent> accelerometerEventList, int position) {
		if ((accelerometerEventList != null) && (position >= 0)) {
			int accelerometerEventListSize = accelerometerEventList.size();
			if (position < accelerometerEventListSize) {
				int positionPrev = position - 1;
				int positionNext = position + 1;
				AccelerometerEvent accelerometerEventPosition = accelerometerEventList
						.get(position);
				if (positionPrev >= 0) {
					AccelerometerEvent accelerometerEventPrev = accelerometerEventList
							.get(positionPrev);
					if (accelerometerEventPosition.a < accelerometerEventPrev.a) {
						return (false);
					}
				}
				if (positionNext < accelerometerEventListSize) {
					AccelerometerEvent accelerometerEventNext = accelerometerEventList
							.get(positionNext);
					if (accelerometerEventPosition.a < accelerometerEventNext.a) {
						return (false);
					}
				}
				return (true);
			}
		}
		return (false);
	}

	/**
	 * calculate the count of the wave crest in this accelerometerEventList
	 * 
	 * @param accelerometerEventList
	 *            a list of accelerometer Event
	 * @return the count of the wave crest in this accelerometerEventList
	 */
	public static int getWaveCrestCount(
			List<AccelerometerEvent> accelerometerEventList) {
		int i = 0;
		int count = accelerometerEventList.size();
		int WaveCrestCount = 0;
		while (i < count) {
			if (AnalyseWave.isWaveTrough(accelerometerEventList, i)) {
				++WaveCrestCount;
			}
			++i;
		}
		return (WaveCrestCount);
	}

	/**
	 * remove the median value from accelerometerEventList
	 * 
	 * @param accelerometerEventList
	 *            a list of accelerometer Event
	 * @return true if the accelerometerEventList is modified
	 */
	public static boolean adjustAccelerometerEventListByRemoveMedian(
			List<AccelerometerEvent> accelerometerEventList) {
		boolean changed = false;
		boolean isWaveTrough = false;
		boolean isWaveCrest = false;
		int count = accelerometerEventList.size() - 1;
		int i = 1;
		while (i < count) {
			isWaveTrough = AnalyseWave.isWaveTrough(accelerometerEventList, i);
			isWaveCrest = AnalyseWave.isWaveCrest(accelerometerEventList, i);
			if (isWaveTrough == isWaveCrest) {
				accelerometerEventList.remove(i);
				--count;
				changed = true;
			} else {
				++i;
			}
		}
		return (changed);
	}

	/**
	 * the max value of the wave crest
	 */
	// 5.6~14 5~14.6 4.6~15
	private static final float MAXCRESTVALUE = 15f;
	/**
	 * the min value of the wave trough
	 */
	private static final float MINTROUGHVALUE = 4.6f;

	/**
	 * remove the value which is out of the limit from accelerometerEventList
	 * 
	 * @param accelerometerEventList
	 *            a list of accelerometer Event
	 * @return true if the accelerometerEventList is modified
	 */
	public static boolean adjustAccelerometerEventListByLimitedValue(
			List<AccelerometerEvent> accelerometerEventList) {
		boolean changed = false;
		AccelerometerEvent accelerometerEvent;
		int i = 0;
		int count = accelerometerEventList.size();
		while (i < count) {
			accelerometerEvent = accelerometerEventList.get(i);
			if ((accelerometerEvent.a > MAXCRESTVALUE)
					|| (accelerometerEvent.a < MINTROUGHVALUE)) {
				accelerometerEvent.a = SensorManager.GRAVITY_EARTH;
				changed = true;
			}
			++i;
		}
		return (changed);
	}

	/**
	 * the min value between two adjacent waves
	 */
	// 0.2
	private static final float DIFFVALUE = 0.3f;

	/**
	 * if the difference of crest or trough between two adjacent waves is less
	 * than the min value between two adjacent waves, then remove this two waves
	 * from accelerometerEventList
	 * 
	 * @param accelerometerEventList
	 *            a list of accelerometer Event
	 * @return true if the accelerometerEventList is modified
	 */
	public static boolean adjustAccelerometerEventListByTinyWave(
			List<AccelerometerEvent> accelerometerEventList) {
		boolean changed = false;
		AccelerometerEvent accelerometerEvent0;
		AccelerometerEvent accelerometerEvent1;
		AccelerometerEvent accelerometerEvent2;
		AccelerometerEvent accelerometerEvent3;
		int i = 0;
		int count = accelerometerEventList.size();
		while (i + 3 < count) {
			accelerometerEvent0 = accelerometerEventList.get(i);
			accelerometerEvent1 = accelerometerEventList.get(i + 1);
			accelerometerEvent2 = accelerometerEventList.get(i + 2);
			accelerometerEvent3 = accelerometerEventList.get(i + 3);
			if (((accelerometerEvent0.a < accelerometerEvent1.a)
					&& (accelerometerEvent1.a > accelerometerEvent2.a)
					&& (accelerometerEvent2.a < accelerometerEvent3.a)
					&& (accelerometerEvent0.a <= accelerometerEvent2.a)
					&& (accelerometerEvent1.a <= accelerometerEvent3.a) && (accelerometerEvent1.a < accelerometerEvent2.a
					+ DIFFVALUE))
					|| ((accelerometerEvent0.a > accelerometerEvent1.a)
							&& (accelerometerEvent1.a < accelerometerEvent2.a)
							&& (accelerometerEvent2.a > accelerometerEvent3.a)
							&& (accelerometerEvent0.a >= accelerometerEvent2.a)
							&& (accelerometerEvent1.a >= accelerometerEvent3.a) && (accelerometerEvent2.a < accelerometerEvent1.a
							+ DIFFVALUE))) {
				accelerometerEventList.remove(i + 2);
				--count;
				accelerometerEventList.remove(i + 1);
				--count;
				changed = true;
			} else {
				++i;
			}
		}
		return (changed);
	}

	/**
	 * if the difference of crest or trough between current wave and the next
	 * wave wave or the previous wave is less than the min value between two
	 * adjacent waves, then remove this current wave from accelerometerEventList
	 * 
	 * @param accelerometerEventList
	 *            a list of accelerometer Event
	 * @return true if the accelerometerEventList is modified
	 */
	public static boolean adjustAccelerometerEventListBySmallWave(
			List<AccelerometerEvent> accelerometerEventList) {
		boolean changed = false;
		AccelerometerEvent accelerometerEvent0;
		AccelerometerEvent accelerometerEvent1;
		AccelerometerEvent accelerometerEvent2;
		int i = 1;
		int count = accelerometerEventList.size() - 1;
		while (i < count) {
			accelerometerEvent0 = accelerometerEventList.get(i - 1);
			accelerometerEvent1 = accelerometerEventList.get(i);
			accelerometerEvent2 = accelerometerEventList.get(i + 1);
			if (((accelerometerEvent0.a < accelerometerEvent1.a)
					&& (accelerometerEvent2.a < accelerometerEvent1.a)
					&& (accelerometerEvent1.a < accelerometerEvent0.a
							+ DIFFVALUE) && (accelerometerEvent1.a < accelerometerEvent2.a
					+ DIFFVALUE))
					|| ((accelerometerEvent0.a > accelerometerEvent1.a)
							&& (accelerometerEvent2.a > accelerometerEvent1.a)
							&& (accelerometerEvent0.a < accelerometerEvent1.a
									+ DIFFVALUE) && (accelerometerEvent2.a < accelerometerEvent1.a
							+ DIFFVALUE))) {
				// accelerometerEvent1.a = (accelerometerEvent0.a +
				// accelerometerEvent2.a) / 2;
				// accelerometerEventList.remove(i + 1);
				// --count;
				// accelerometerEventList.remove(i - 1);
				// --count;
				accelerometerEventList.remove(i);
				--count;
				changed = true;
			} else {
				++i;
			}
		}
		return (changed);
	}

	/**
	 * adjust the first or the last wave's a value
	 * 
	 * @param accelerometerEventList
	 *            a list of accelerometer Event
	 * @return true if the accelerometerEventList is modified
	 */
	public static boolean adjustAccelerometerEventListByStrip(
			List<AccelerometerEvent> accelerometerEventList) {
		boolean changed = false;
		AccelerometerEvent accelerometerEvent0;
		AccelerometerEvent accelerometerEvent1;
		AccelerometerEvent accelerometerEvent2;
		int count = accelerometerEventList.size();
		if (count >= 3) {
			accelerometerEvent0 = accelerometerEventList.get(0);
			accelerometerEvent1 = accelerometerEventList.get(1);
			accelerometerEvent2 = accelerometerEventList.get(2);
			if (((accelerometerEvent0.a > accelerometerEvent1.a)
					&& (accelerometerEvent2.a > accelerometerEvent1.a)
					&& (accelerometerEvent0.a <= accelerometerEvent1.a
							+ DIFFVALUE) && (accelerometerEvent2.a > accelerometerEvent0.a
					+ 2 * DIFFVALUE))
					|| ((accelerometerEvent0.a < accelerometerEvent1.a)
							&& (accelerometerEvent2.a < accelerometerEvent1.a)
							&& (accelerometerEvent1.a <= accelerometerEvent0.a
									+ DIFFVALUE) && (accelerometerEvent0.a > accelerometerEvent2.a
							+ 2 * DIFFVALUE))) {
				AccelerometerEvent accelerometerEvent = new AccelerometerEvent();
				accelerometerEvent.a = accelerometerEvent1.a;
				accelerometerEvent.t = accelerometerEvent0.t;
				accelerometerEvent.x = accelerometerEvent0.x;
				accelerometerEvent.y = accelerometerEvent0.y;
				accelerometerEvent.z = accelerometerEvent0.z;
				accelerometerEventList.set(0, accelerometerEvent);
				changed = true;
			}
		}
		if (count > 3) {
			accelerometerEvent0 = accelerometerEventList.get(count - 1);
			accelerometerEvent1 = accelerometerEventList.get(count - 2);
			accelerometerEvent2 = accelerometerEventList.get(count - 3);
			if (((accelerometerEvent0.a > accelerometerEvent1.a)
					&& (accelerometerEvent2.a > accelerometerEvent1.a)
					&& (accelerometerEvent0.a <= accelerometerEvent1.a
							+ DIFFVALUE) && (accelerometerEvent2.a > accelerometerEvent0.a
					+ 2 * DIFFVALUE))
					|| ((accelerometerEvent0.a < accelerometerEvent1.a)
							&& (accelerometerEvent2.a < accelerometerEvent1.a)
							&& (accelerometerEvent1.a <= accelerometerEvent0.a
									+ DIFFVALUE) && (accelerometerEvent0.a > accelerometerEvent2.a
							+ 2 * DIFFVALUE))) {
				AccelerometerEvent accelerometerEvent = new AccelerometerEvent();
				accelerometerEvent.a = accelerometerEvent1.a;
				accelerometerEvent.t = accelerometerEvent0.t;
				accelerometerEvent.x = accelerometerEvent0.x;
				accelerometerEvent.y = accelerometerEvent0.y;
				accelerometerEvent.z = accelerometerEvent0.z;
				accelerometerEventList.set(count - 1, accelerometerEvent);
				changed = true;
			}
		}
		return (changed);
	}

	/**
	 * judge t is between 50 and 1500 or not,
	 * 
	 * @param t
	 *            a time value
	 * @return true if t is between 50 and 1500
	 */
	public static boolean isStepTimeDiff(float t) {
		return (t > 50 && t < 1500);
	}

	/**
	 * judge a is between MINTROUGHVALUE and 10.5 or not
	 * 
	 * @param a
	 *            accelerometer event 's accelerometer value
	 * @return true if a is a trough value
	 */
	public static boolean isStepTroughValue(float a) {
		return (a > MINTROUGHVALUE && a < 10.5);
	}

	/**
	 * judge a is between 9.1 and MAXCRESTVALUE or not
	 * 
	 * @param a
	 *            accelerometer event 's accelerometer value
	 * @return true if a is a crest value
	 */
	public static boolean isStepCrestValue(float a) {
		return (a > 9.1 && a < MAXCRESTVALUE);
	}

	/**
	 * filter the accelerometerEventList
	 * 
	 * @param accelerometerEventList
	 *            a list of accelerometer Event
	 */
	public static void FilterAccelerometerEventList(
			List<AccelerometerEvent> accelerometerEventList) {
		AnalyseWave
				.adjustAccelerometerEventListByLimitedValue(accelerometerEventList);
		boolean changed = false;
		do {
			changed = AnalyseWave
					.adjustAccelerometerEventListByRemoveMedian(accelerometerEventList);
			if (!changed) {
				changed = AnalyseWave
						.adjustAccelerometerEventListByTinyWave(accelerometerEventList);
			}
			if (!changed) {
				changed = AnalyseWave
						.adjustAccelerometerEventListBySmallWave(accelerometerEventList);
			}
			if (!changed) {
				changed = AnalyseWave
						.adjustAccelerometerEventListByStrip(accelerometerEventList);
			}
		} while (changed);
	}

	/**
	 * calculate the count of the wave's crest or trough in
	 * accelerometerEventList
	 * 
	 * @param accelerometerEventList
	 *            a list of accelerometer Event
	 * @return the count of the steps in accelerometerEventList
	 */
	public static int CountStepsAccelerometerEventList(
			List<AccelerometerEvent> accelerometerEventList) {
		int stepcount = 0;
		boolean isWaveCrest0 = false;
		boolean isWaveCrest1 = false;
		boolean isWaveCrest2 = false;
		boolean isWaveCrest3 = false;
		AccelerometerEvent accelerometerEvent0;
		AccelerometerEvent accelerometerEvent1;
		AccelerometerEvent accelerometerEvent2;
		int i = 0;
		int count = accelerometerEventList.size();
		while (i + 2 < count) {
			isWaveCrest0 = AnalyseWave.isWaveCrest(accelerometerEventList, i);
			isWaveCrest1 = AnalyseWave.isWaveCrest(accelerometerEventList,
					i + 1);
			isWaveCrest2 = AnalyseWave.isWaveCrest(accelerometerEventList,
					i + 2);
			isWaveCrest3 = AnalyseWave.isWaveCrest(accelerometerEventList,
					i + 3);
			if ((isWaveCrest0 == isWaveCrest2)
					&& (isWaveCrest1 == isWaveCrest3)
					&& (isWaveCrest0 != isWaveCrest1)) {
				accelerometerEvent0 = accelerometerEventList.get(i);
				accelerometerEvent1 = accelerometerEventList.get(i + 1);
				accelerometerEvent2 = accelerometerEventList.get(i + 2);
				if ((isWaveCrest1 ? (isStepTroughValue(accelerometerEvent0.a)
						&& isStepCrestValue(accelerometerEvent1.a) && isStepTroughValue(accelerometerEvent2.a))
						: (isStepCrestValue(accelerometerEvent0.a)
								&& isStepTroughValue(accelerometerEvent1.a) && isStepCrestValue(accelerometerEvent2.a)))
						&& isStepTimeDiff(accelerometerEvent2.t
								- accelerometerEvent0.t)) {
					if ((((accelerometerEvent0.a + accelerometerEvent2.a) - (accelerometerEvent1.a * 2)) / 2) > DIFFVALUE) {
						++stepcount;
						++i;
					}
				}
			}
			++i;
		}
		return (stepcount);
	}
}