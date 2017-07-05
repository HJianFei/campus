package com.loosoo100.campus100.anyevent;

/**
 * 小卖部订单移除
 * 
 * @author yang
 * 
 */
public class MEventStoreRemove {
	private int position;

	public MEventStoreRemove(int position) {
		this.position = position;
	}

	public int getPosition() {
		return position;
	}
}
