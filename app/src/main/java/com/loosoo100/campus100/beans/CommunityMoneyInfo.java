package com.loosoo100.campus100.beans;

/**
 * 
 * @author yang 社团资金记录信息
 * 
 */
public class CommunityMoneyInfo {
	// 时间
	private String time;
	// 金额
	private float money;
	// 变动金额
	private float moneyChange;
	// 类型
	private String type;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public float getMoneyChange() {
		return moneyChange;
	}

	public void setMoneyChange(float moneyChange) {
		this.moneyChange = moneyChange;
	}

}
