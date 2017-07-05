package com.loosoo100.campus100.anyevent;

/**
 * 小卖部订单详情状态改变
 * 
 * @author yang
 * 
 */
public class MEventStoreStatus {
	private int position;

	public MEventStoreStatus(int position) {
		this.position = position;
	}

	public int getPosition() {
		return position;
	}
}
