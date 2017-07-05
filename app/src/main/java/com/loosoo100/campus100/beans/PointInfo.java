package com.loosoo100.campus100.beans;

/**
 * 
 * @author yang 积分信息
 * 
 */
public class PointInfo {
	// 类型
	private String type;
	// 积分
	private int point;
	// 时间
	private String time;
	// 改变的积分
	private int pointChange;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getPointChange() {
		return pointChange;
	}

	public void setPointChange(int pointChange) {
		this.pointChange = pointChange;
	}

}
