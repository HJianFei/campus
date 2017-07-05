package com.loosoo100.campus100.beans;

/**
 * 
 * @author yang 我的信用信息
 * 
 */
public class MyCreditInfo {
	// 剩余信用额度
	private int money;
	// 本月应还
	private int repaymentThis;
	// 下月应还
	private int repaymentNext;

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getRepaymentThis() {
		return repaymentThis;
	}

	public void setRepaymentThis(int repaymentThis) {
		this.repaymentThis = repaymentThis;
	}

	public int getRepaymentNext() {
		return repaymentNext;
	}

	public void setRepaymentNext(int repaymentNext) {
		this.repaymentNext = repaymentNext;
	}

}
