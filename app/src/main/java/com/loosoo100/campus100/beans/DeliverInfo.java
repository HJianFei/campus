package com.loosoo100.campus100.beans;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author yang 物流信息
 * 
 */
public class DeliverInfo {
	// 快递单号
	private String num;
	// 快递公司
	private String company;
	// 状态详情列表
	private List<Map<String, Object>> list;

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public List<Map<String, Object>> getList() {
		return list;
	}

	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}

}
