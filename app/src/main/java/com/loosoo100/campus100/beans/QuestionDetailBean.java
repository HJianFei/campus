package com.loosoo100.campus100.beans;

import java.util.List;

public class QuestionDetailBean {
	private String qid;
	private String quest_order;
	private String quest_type;
	private String question;
	private List<String> options;
	private String created_at;
	private String updated_at;

	public String getQid() {
		return qid;
	}

	public void setQid(String qid) {
		this.qid = qid;
	}

	public String getQuest_order() {
		return quest_order;
	}

	public void setQuest_order(String quest_order) {
		this.quest_order = quest_order;
	}

	public String getQuest_type() {
		return quest_type;
	}

	public void setQuest_type(String quest_type) {
		this.quest_type = quest_type;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public List<String> getOptions() {
		return options;
	}

	public void setOptions(List<String> options) {
		this.options = options;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}
}
