package com.iscreate.mobile.widget;

public class ElectromagneticWave extends ElectromagneticWaveFreeSpaceLoss {
	public static final int SIGNAL_Min = -113;
	public static final int SIGNAL_Strong = -50; // 大于等于-50dbm
	public static final int SIGNAL_Moderate = -70;
	public static final int SIGNAL_Weak = -86;// 小于等于-86dbm
	public static final int STEP_Strong = 2;
	public static final int STEP_Middle = 1;
	public static final float WALL_LOSS_N = 12;
	// 天线增益:2dbm
	private final float AntennaGain = 2;
	// 多径损耗:-10dbm
	private final float MultipathLoss = -10;
	// 天花板损耗:-13dbm
	private final float CeilingLoss = -13;
	// 其他损耗:-2dbm
	private final float OtherLoss = -2;
	/**
	 * 信号圆点的半径及信号强度
	 */
	private float Power = 12;
	private float WallLoss = WALL_LOSS_N;
	/**
	 * 频率
	 */
	private float Frequency = 2.462f;
	private double Radius_Translate = 0;
	private double Signal_Translate = AntennaGain + MultipathLoss + CeilingLoss
			+ OtherLoss;
	private Float Radius_T = null;
	private Float Signal_T = null;

	/**
	 * 自由空间状态下，获取信号圆半径（米）
	 */
	private double getRadiusUnderFreeSpace(double s, double f) {
		return (getRadiusLoss(f, Power - s));
	}

	/**
	 * 自由空间状态下，获取信号强度
	 */
	private double getSignalUnderFreeSpace(double r, double f) {
		return (Power - getSignalLoss(f, r));
	}

	/**
	 * 平移函数图形进行校正
	 */
	private Point2D getTranslateRS(double r0, double s0, double r1, double s1,
			double f) {
		if (s0 != s1) {
			double a = pow((s0 - s1) / N);
			double rt = ((a * r0 - r1) / (1 - a));
			double st = s0 - getSignalUnderFreeSpace(r0 + rt, f);
			return (Point2D.getPoint(rt, st));
		}
		return (null);
	}

	/**
	 * 校正后，获取信号圆半径（米）
	 */
	private double getAdjustRadiusUnderFreeSpace(double s, double f) {
		return (getRadiusUnderFreeSpace(s - Signal_Translate, f) - Radius_Translate);
	}

	/**
	 * 校正后，获取信号强度
	 */
	private double getAdjustSignalUnderFreeSpace(double r, double f) {
		return (getSignalUnderFreeSpace(r + Radius_Translate, f) + Signal_Translate);
	}

	/**
	 * 获取在信号强度从s0变成s1后信号圆半径（米）的变化值
	 */
	private double getIdealRadiusChange(double s0, double s1) {
		double d0 = getAdjustRadiusUnderFreeSpace(s0, Frequency);
		if (d0 < 0) {
			d0 = 0;
		}
		double d1 = getAdjustRadiusUnderFreeSpace(s1, Frequency);
		if (d1 < 0) {
			d1 = 0;
		}
		return (d1 - d0);
	}

	/**
	 * 获取在信号圆半径（米）从d0变成d1后信号强度的变化值
	 */
	public double getIdealSignalChange(double d0, double d1) {
		double s0 = getAdjustSignalUnderFreeSpace(d0, Frequency);
		double s1 = getAdjustSignalUnderFreeSpace(d1, Frequency);
		return (s1 - s0);
	}

	/**
	 * 通过已知信号强度、信号圆半径（米）获取当前信号强度下的信号圆半径（米）
	 */
	public double getRadiusViaSignal(double d0, double s0, double s1) {
		double d1 = d0 + getIdealRadiusChange(s0, s1);
		return (d1);
	}

	/**
	 * 通过已知信号强度、信号圆半径（米）获取当前信号圆半径（米）下的信号强度
	 */
	public double getSignalViaRadius(double d0, double s0, double d1) {
		double dd = d1 - d0;
		d0 = getAdjustRadiusUnderFreeSpace(s0, Frequency);
		d1 = d0 + dd;
		double s1 = getAdjustSignalUnderFreeSpace(d1, Frequency);
		return (s1);
	}

	/**
	 * 获取信号圆半径（米）
	 */
	public double getRadiusViaSignal(double s) {
		return (getRadiusViaSignal(0, Power, s));
	}

	/**
	 * 获取信号强度
	 */
	public double getSignalViaRadius(double d) {
		return (getSignalViaRadius(0, Power, d));
	}

	/**
	 * 天线输出功率(dBm)
	 */
	public void setPower(float power) {
		Power = power;
		setTranslate();
	}

	public float getPower() {
		return (Power);
	}

	/**
	 * 墙壁损耗(dBm)
	 */
	public void setWallLoss(float loss) {
		WallLoss = loss;
	}

	public float getWallLoss() {
		return (WallLoss);
	}

	/**
	 * 频率(dBm)
	 */
	public void setFrequency(float f) {
		if (f != 0) {
			Frequency = f / 1000;
		} else {
			Frequency = 2.462f;
		}
		setTranslate();
	}

	public float getFrequency() {
		return (Frequency * 1000);
	}

	public void setTranslate() {
		Radius_Translate = 0;
		Radius_Translate = getAdjustRadiusUnderFreeSpace(Power, Frequency);
		if ((Radius_T != null) && (Signal_T != null)) {
			Point2D TranslateRS = getTranslateRS(0, Power, Radius_T, Signal_T,
					Frequency);
			if (TranslateRS != null) {
				// Radius_Translate = TranslateRS.x;
				// Signal_Translate = TranslateRS.y;
			}
		}
	}

	public void setTranslate(float r, float s) {
		Radius_T = r;
		Signal_T = s;
		setTranslate();
	}
}