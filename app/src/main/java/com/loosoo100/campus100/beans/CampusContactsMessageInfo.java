package com.loosoo100.campus100.beans;

/**
 * 校园圈消息
 * 
 * @author yang
 * 
 */
public class CampusContactsMessageInfo {
	// ID
	private String id;
	// 圈圈ID
	private String mid;
	// 图标
	private String picUrl;
	// 名字
	private String name;
	// 内容
	private String content;
	// 时间
	private long time;
	//1是赞  2是评论
	private String type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
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

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
