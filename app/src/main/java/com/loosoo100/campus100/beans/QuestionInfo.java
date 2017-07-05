package com.loosoo100.campus100.beans;

import java.util.List;

/**
 * Created by HJianFei on 2016/10/8.
 */

public class QuestionInfo {

	/**
	 * name : 校园APP功能调查问卷 description :
	 * 本次调查目的是希望了解当前大学生最想要什么，期待校园手机APP具备什么强大功能，进而开发出解决您亟待解决的校园问题
	 * ，丰富校园生活。本次参与调查者，你的真诚的想法，一经采用，后期都会有丰厚的奖品哦！（本次调查只作为开发APP数据使用，绝不外传，请放心填写）
	 * conclusion : 感谢你耐心完成这份问卷，我们将认真对待你的建议，惊喜或许已在寻找你的路上，留意我们给你的消息喔(^_^) detail
	 * : [{"qid":"16","quest_order":"1","quest_type":"0","question":
	 * "请问你最偏爱下载的APP类型是？","options":
	 * "{\"1\":\"实用工具\",\"2\":\"聊天与社交\",\"3\":\"学习与教育\",\"4\":\"时尚与购物\",\"5\":\"理财\"}"
	 * ,"created_at":"0","updated_at":"0"},{"qid":"16","quest_order":"2",
	 * "quest_type":"0","question":"如果有一款APP能够拉赞助，找社团，联谊外校的，是否感兴趣？","options":
	 * "{\"1\":\"很感兴趣\",\"2\":\"一般\",\"3\":\"不感兴趣\"}"
	 * ,"created_at":"0","updated_at"
	 * :"0"},{"qid":"16","quest_order":"3","quest_type"
	 * :"0","question":"如果我们在校园APP上发布城市内或校园周边的兼职/实习信息，你是否会浏览并报名参加？","options":
	 * "{\"1\":\"愿意，解决了找兼职/实习的大问题\",\"2\":\"不确定，兼职信息是否真实有效心里没底\",\"3\":\"不会去找兼职\"}"
	 * ,"created_at":"0","updated_at":"0"},{"qid":"16","quest_order":"4",
	 * "quest_type":"0","question":"拥有创业众筹与社团活动众筹的APP你是否感兴趣？","options":
	 * "{\"1\":\"想立刻下载使用\",\"2\":\"可以考虑\",\"3\":\"没兴趣\"}"
	 * ,"created_at":"0","updated_at"
	 * :"0"},{"qid":"16","quest_order":"5","quest_type"
	 * :"0","question":"可以宅着也能知道校园最新消息，可以在直接与陌生校友聊天，可以相互点评，互换QQ和电话的APP，你是否感兴趣？"
	 * ,"options"
	 * :"{\"1\":\"很感兴趣，增加自己的社交圈\",\"2\":\"一般，已经有用类似的APP\",\"3\":\"没兴趣\"}"
	 * ,"created_at"
	 * :"0","updated_at":"0"},{"qid":"16","quest_order":"6","quest_type"
	 * :"0","question":"一个关于暗恋小心思的APP，你是否感兴趣？","options":
	 * "{\"1\":\"很感兴趣\",\"2\":\"一般\",\"3\":\"没兴趣\"}"
	 * ,"created_at":"0","updated_at"
	 * :"0"},{"qid":"16","quest_order":"7","quest_type"
	 * :"0","question":"一款可以在微信朋友圈发起让朋友凑一凑买礼物的APP你优先考虑什么因素？"
	 * ,"options":"{\"1\":\"实惠程度\",\"2\":\"网购安全程度\",\"3\":\"售后保障\"}"
	 * ,"created_at"
	 * :"0","updated_at":"0"},{"qid":"16","quest_order":"8","quest_type"
	 * :"0","question":"不用知道对方地址也能送礼物的APP你感兴趣吗？","options":
	 * "{\"1\":\"很感兴趣\",\"2\":\"一般\",\"3\":\"没兴趣\"}"
	 * ,"created_at":"0","updated_at"
	 * :"0"},{"qid":"16","quest_order":"9","quest_type"
	 * :"0","question":"请问您选择一款APP通常考虑哪些因素？","options":
	 * "{\"1\":\"应用是否收费\",\"2\":\"应用安全性与隐私保密性\",\"3\":\"被应用名称、图标所吸引\",\"4\":\"应用功能是否实用、吸引\",\"5\":\"应用文件大小\",\"6\":\"周边人推荐度\"}"
	 * ,"created_at":"0","updated_at":"0"},{"qid":"16","quest_order":"10",
	 * "quest_type"
	 * :"1","question":"你希望以后校园APP具备哪些功能或提出你宝贵的意见！（简单描述）","options":""
	 * ,"created_at":"0","updated_at":"0"}]
	 */

	private String name;
	private String description;
	private String conclusion;
	/**
	 * qid : 16 quest_order : 1 quest_type : 0 question : 请问你最偏爱下载的APP类型是？
	 * options : {"1":"实用工具","2":"聊天与社交","3":"学习与教育","4":"时尚与购物","5":"理财"}
	 * created_at : 0 updated_at : 0
	 */

	private List<QuestionDetailBean> detail;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getConclusion() {
		return conclusion;
	}

	public void setConclusion(String conclusion) {
		this.conclusion = conclusion;
	}

	public List<QuestionDetailBean> getDetail() {
		return detail;
	}

	public void setDetail(List<QuestionDetailBean> detail) {
		this.detail = detail;
	}

}
