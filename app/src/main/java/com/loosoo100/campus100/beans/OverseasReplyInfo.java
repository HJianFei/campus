package com.loosoo100.campus100.beans;

/**
 * 海归评论
 * 
 * @author yang
 * 
 */
public class OverseasReplyInfo {
	// 评论ID
	private String id;
	// 回复(谁)ID
	private String pid;
	// 用户ID
	private String uid;
	// 用户头像
	private String headShot;
	// 昵称
	private String name;
	// 回复(谁)昵称
	private String pname;
	// 内容
	private String content;
	// 性别
	private String sex;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getHeadShot() {
		return headShot;
	}

	public void setHeadShot(String headShot) {
		this.headShot = headShot;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

}
