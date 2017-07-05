package com.loosoo100.campus100.beans;

/**
 * 
 * @author yang 还款信息
 * 
 */
public class RepaymentInfo {
	// 账单时间
	private String time;
	// 金额
	private float money;
	// 期数
	private int periods;
	// 状态
	private String status;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public float getMoney() {
		return money;
	}

	public void setMoney(float money) {
		this.money = money;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getPeriods() {
		return periods;
	}

	public void setPeriods(int periods) {
		this.periods = periods;
	}

}
