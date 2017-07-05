package com.loosoo100.campus100.beans;

import android.graphics.Bitmap;

/**
 * 
 * @author yang 社团成员信息
 * 
 */
public class CommunityMemberInfo {
	// 头像
	private Bitmap headShot;
	// 姓名
	private String name;
	// 用户ID
	private String userId;
	// 昵称
	private String nickName;
	// 性别(0男 1女)
	private int sex;
	// weixin
	private String weixin;
	// 头像地址
	private String headShotString;

	public Bitmap getHeadShot() {
		return headShot;
	}

	public void setHeadShot(Bitmap headShot) {
		this.headShot = headShot;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getHeadShotString() {
		return headShotString;
	}

	public void setHeadShotString(String headShotString) {
		this.headShotString = headShotString;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getWeixin() {
		return weixin;
	}

	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}

}
