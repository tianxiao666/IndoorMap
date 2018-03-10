package com.iscreate.mobile.widget;

public class ElectromagneticWaveFreeSpaceLoss {
	public final double n = 2;
	public final double N = n * 10;
	public final double S = 32.45;
	public final double base = 10;

	/**
	 * 换底
	 */
	public double log(double d) {
		return (Math.log10(d) / Math.log10(base));
	}

	public double pow(double d) {
		return (Math.pow(base, d));
	}

	/**
	 * 自由空间路径损耗
	 */
	public double getRadiusLoss(double f, double s) {
		return (pow((s - S) / N) / f);
	}

	/**
	 * 自由空间功率损耗
	 */
	public double getSignalLoss(double f, double r) {
		return (S + (N * log(f * r)));
	}
}
