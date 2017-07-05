package com.loosoo100.campus100.beans;

/**
 * 暗恋
 * 
 * @author yang
 * 
 */
public class CampusContactsLoveInfo {
	// ID
	private String id;
	// 请求互换ID
	private String changeId;
	// 性别
	private String sex;
	// 名字
	private String name;
	// 用户ID
	private String uid;
	// 头像
	private String headShot;
	// 学校
	private String school;
	// 真实名字
	private String tureName;
	// 电话
	private String phone;
	// weixin
	private String weixin;
	// 请求类型
	private String requestType; //0为手机，1为微信
	// 暗恋状态
	private String status; // 有内容代表1心，否则半心

	public String getTureName() {
		return tureName;
	}

	public void setTureName(String tureName) {
		this.tureName = tureName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHeadShot() {
		return headShot;
	}

	public void setHeadShot(String headShot) {
		this.headShot = headShot;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getChangeId() {
		return changeId;
	}

	public void setChangeId(String changeId) {
		this.changeId = changeId;
	}

	public String getWeixin() {
		return weixin;
	}

	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}

}