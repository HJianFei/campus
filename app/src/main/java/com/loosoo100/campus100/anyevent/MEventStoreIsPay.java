package com.loosoo100.campus100.anyevent;

/**
 * 小卖部订单付款
 * 
 * @author yang
 * 
 */
public class MEventStoreIsPay {
	private int position;

	public MEventStoreIsPay(int position) {
		this.position = position;
	}

	public int getPosition() {
		return position;
	}
}
