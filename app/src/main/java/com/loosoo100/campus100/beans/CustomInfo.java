package com.loosoo100.campus100.beans;

/**
 * 
 * @author yang 客服中心信息
 * 
 */
public class CustomInfo {
	// 问题
	private String question;
	// 回答
	private String answer;
	// id
	private String id;
	// 类型
	private String type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

}
