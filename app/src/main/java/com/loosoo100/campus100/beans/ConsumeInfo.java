package com.loosoo100.campus100.beans;

/**
 * 
 * @author yang
 * 消费信息
 *
 */
public class ConsumeInfo {
	//类型
	private String type;
	//余额
	private String money;
	//时间
	private String time;
	//改变的金额
	private String moneyChange;
	
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getMoneyChange() {
		return moneyChange;
	}
	public void setMoneyChange(String moneyChange) {
		this.moneyChange = moneyChange;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	
}
