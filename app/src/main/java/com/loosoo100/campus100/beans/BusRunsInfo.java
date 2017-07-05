package com.loosoo100.campus100.beans;

import java.util.List;

/**
 * 
 * @author yang 校园包车班次信息
 * 
 */
public class BusRunsInfo {
	// 发车时间
	private String time;
	// 出发地
	private String startSchool;
	// 目的地
	private String endSchool;
	// 价格
	private float price;
	// 是否显示途经内容
	private boolean isShowByway;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getStartSchool() {
		return startSchool;
	}

	public void setStartSchool(String startSchool) {
		this.startSchool = startSchool;
	}

	public String getEndSchool() {
		return endSchool;
	}

	public void setEndSchool(String endSchool) {
		this.endSchool = endSchool;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}

	public boolean isShowByway() {
		return isShowByway;
	}

	public void setShowByway(boolean isShowByway) {
		this.isShowByway = isShowByway;
	}

	// 途经地点
	private List<String> list;
}
