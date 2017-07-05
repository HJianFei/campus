package com.loosoo100.campus100.beans;

/**
 * 
 * @author yang
 * 借款记录信息
 *
 */
public class BorrowHistoryInfo {
	//时间
	private String time;
	//金额
	private float money;
	//还款方式
	private String way;
	//状态
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
	public String getWay() {
		return way;
	}
	public void setWay(String way) {
		this.way = way;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
