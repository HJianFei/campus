package com.loosoo100.campus100.beans;

/**
 * 
 * @author yang 调查问卷列表信息
 * 
 */
public class QuestionListInfo {
	//ID
	private String qid;
	// 标题
	private String title;
	// 状态
	private int status;
	// 概要
	private String content;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getQid() {
		return qid;
	}

	public void setQid(String qid) {
		this.qid = qid;
	}

}
