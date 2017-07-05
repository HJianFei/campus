package com.loosoo100.campus100.beans;

/**
 * 
 * @author yang
 * 提现信息
 *
 */
public class CashoutInfo {
	//状态
	private String status;
	//金额
	private String money;
	//时间
	private String time;
	//提现至的账户名称
	private String account;
	//提现至的账户
	private String num;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
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
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
}
