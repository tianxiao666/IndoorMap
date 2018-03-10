package com.iscreate.mobile.indoormap.activity;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

import com.iscreate.mobile.utils.Signal;
import com.iscreate.mobile.widget.BaseStationInfo;

public abstract class cellDetecterActivity extends stepDetecterByNetActivity {
	/**
	 * TelephonyManager
	 */
	private TelephonyManager telephonyManager = null;
	/**
	 * 手机卡信息
	 */
	private BaseStationInfo baseStationInfo = null;

	/**
	 * a listener to the phone state
	 */
	private class MyPhoneStateListener extends PhoneStateListener {
		@Override
		public void onSignalStrengthsChanged(SignalStrength signalStrength) {
			if (baseStationInfo != null) {
				if (signalStrength.isGsm()) {
					// GSM 卡
					// signalStrength.getGsmSignalStrength(); 2G 信号强度
					baseStationInfo.BSSS = Signal.todBm(signalStrength
							.getGsmSignalStrength());
				} else {
					// CDMA 卡
					// signalStrength.getCdmaDbm(); 联通3G 信号强度
					// signalStrength.getEvdoDbm(); 电信3G 信号强度
					baseStationInfo.BSSS = signalStrength.getCdmaDbm();
				}
			}
			super.onSignalStrengthsChanged(signalStrength);
		}

		@Override
		public void onCellLocationChanged(CellLocation location) {
			if (baseStationInfo != null) {
				if (location instanceof GsmCellLocation) {
					// GSM 卡
					GsmCellLocation gsmCellLocation = (GsmCellLocation) location;
					baseStationInfo.LAC = gsmCellLocation.getLac();
					baseStationInfo.CID = gsmCellLocation.getCid();
				} else {
					if (location instanceof CdmaCellLocation) {
						// CDMA 卡
						CdmaCellLocation cdmaCellLocation = (CdmaCellLocation) location;
						baseStationInfo.LAC = cdmaCellLocation.getNetworkId();
						baseStationInfo.CID = cdmaCellLocation
								.getBaseStationId();
					}
				}
			}
			super.onCellLocationChanged(location);
		}
	}

	/**
	 * if there is a sim card ,then listen to the signal strength
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		if (telephonyManager.getSimState() != TelephonyManager.SIM_STATE_ABSENT) {
			MyPhoneStateListener myPhoneStateListener = new MyPhoneStateListener();
			telephonyManager.listen(myPhoneStateListener,
					PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
			telephonyManager.listen(myPhoneStateListener,
					PhoneStateListener.LISTEN_CELL_LOCATION);
			baseStationInfo = new BaseStationInfo();
			String operator = telephonyManager.getSimOperator();
			if ((operator != null) && (operator.length() >= 3)) {
				String MCC = operator.substring(0, 3);
				String MNC = operator.substring(3);
				try {
					baseStationInfo.MCC = Integer.valueOf(MCC);
				} catch (Exception e) {
				}
				try {
					baseStationInfo.MNC = Integer.valueOf(MNC);
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * 获取基站信息
	 */
	public BaseStationInfo getBaseStationInfo() {
		return (baseStationInfo);
	}

	/**
	 * get NeighboringCellInfo,this one is not working
	 */
	public List<NeighboringCellInfo> getNeighboringCellInfo() {
		return (telephonyManager.getNeighboringCellInfo());
	}
}
