package com.iscreate.mobile.indoormap.widget;

import android.os.Handler;
import android.os.Message;

import com.iscreate.mobile.service.ServiceClientInterface;

public class requestDataThread extends Thread {
	/**
	 * the handler where you want to sent message
	 */
	private Handler handler = null;
	/**
	 * User-defined message code so that the recipient can identify what this
	 * message is about. Each Handler has its own name-space for message codes,
	 * so you do not need to worry about yours conflicting with other handlers.
	 */
	private Integer whatID = null;
	/**
	 * the interface action id in class ServiceClientInterface
	 */
	private Integer actionID = null;
	/**
	 * the parameter value list
	 */
	private String[] params = null;

	public requestDataThread(Handler handler, int whatID, int actionID,
			String[] params) {
		this.handler = handler;
		this.whatID = whatID;
		this.actionID = actionID;
		this.params = params;
	}

	/**
	 * msg.arg1 is the action id, msg.arg2 is a judge for succeed or failed,
	 * msg.obj is the content from the JSON response,and this msg will be sent
	 * to where handler was created
	 */
	@Override
	public void run() {
		Message msg = handler.obtainMessage();
		msg.what = whatID;
		msg.arg1 = actionID;
		try {
			msg.obj = ServiceClientInterface.request(actionID, params);
			msg.arg2 = 1;
		} catch (Exception e) {
			msg.obj = e.getMessage();
			msg.arg2 = 0;
		}
		handler.sendMessage(msg);
	}
}