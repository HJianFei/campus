package com.loosoo100.campus100.anyevent;


/**
 * 社团活动是否有支付
 * 
 * @author yang
 * 
 */
public class MEventCommNeedPay {
	private boolean change;

	public MEventCommNeedPay(boolean change) {
		this.change = change;
	}

	public boolean isChange() {
		return change;
	}

}
