package com.loosoo100.campus100.anyevent;

/**
 * 微信支付成功或失败
 * 
 * @author yang
 * 
 */
public class MEventWXPay {
	private boolean success;

	public MEventWXPay(boolean success) {
		this.success = success;
	}

	public boolean isSuccess() {
		return success;
	}
}
