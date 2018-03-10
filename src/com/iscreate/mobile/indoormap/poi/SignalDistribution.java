package com.iscreate.mobile.indoormap.poi;

import android.graphics.Path;
import android.util.SparseArray;

public class SignalDistribution {
	public float POSITION_X = 0f;
	public float POSITION_Y = 0f;
	public float FREQUENCY = 2462f;
	public float POWER = 18f;
	public SparseArray<Path> IdealSignalDistribution = null;
	public SparseArray<Path> ReckonSignalDistribution = null;
}