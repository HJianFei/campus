package com.loosoo100.campus100.anyevent;

/**
 * 小卖部购物车是否清空
 * 
 * @author yang
 * 
 */
public class MEventStoreCart {
	private boolean change;

	public MEventStoreCart(boolean change) {
		this.change = change;
	}

	public boolean getChange() {
		return change;
	}
}
