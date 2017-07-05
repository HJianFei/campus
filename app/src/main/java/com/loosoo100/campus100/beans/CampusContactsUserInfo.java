package com.loosoo100.campus100.beans;

/**
 * 校园圈用户信息
 * 
 * @author yang
 * 
 */
public class CampusContactsUserInfo {
	// 性别
	private String sex;
	// 头像
	private String headShot;
	// 背景图
	private String bg;
	// 被暗恋的人数
	private long crushNum;
	// 名字
	private String name;
	// 学校
	private String school;

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getHeadShot() {
		return headShot;
	}

	public void setHeadShot(String headShot) {
		this.headShot = headShot;
	}

	public long getCrushNum() {
		return crushNum;
	}

	public void setCrushNum(long crushNum) {
		this.crushNum = crushNum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getBg() {
		return bg;
	}

	public void setBg(String bg) {
		this.bg = bg;
	}

}
