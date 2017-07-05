package com.loosoo100.campus100.anyevent;

/**
 * 支付宝支付成功或失败
 * 
 * @author yang
 * 
 */
public class MEventAliPay {
	private boolean success;

	public MEventAliPay(boolean success) {
		this.success = success;
	}

	public boolean isSuccess() {
		return success;
	}
}
