package com.loosoo100.campus100.beans;

/**
 * 
 * @author yang 海归求助问答信息
 * 
 */
public class OverseasQuestionInfo {
	// id
	private String id;
	// uid
	private String uid;
	// 提问者姓名
	private String name;
	// 提问者头像
	private String headShot;
	// 提问内容
	private String question;
	// 阅读人数
	private int readingCount;
	// 讨论人数
	private int discussCount;
	// 发表时间
	private String time;
	// 性别
	private String sex;

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

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public int getReadingCount() {
		return readingCount;
	}

	public void setReadingCount(int readingCount) {
		this.readingCount = readingCount;
	}

	public int getDiscussCount() {
		return discussCount;
	}

	public void setDiscussCount(int discussCount) {
		this.discussCount = discussCount;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

}
