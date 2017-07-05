package com.loosoo100.campus100.anyevent;

import java.util.List;

import com.loosoo100.campus100.beans.CampusContactsReplyInfo;

/**
 * 校园圈评论是否有改变
 * 
 * @author yang
 * 
 */
public class MEventCampusContactsAppraiseChange {
	private String mid;
	private List<CampusContactsReplyInfo> appraiseList;

	public MEventCampusContactsAppraiseChange(String mid,
			List<CampusContactsReplyInfo> appraiseList) {
		this.mid = mid;
		this.appraiseList = appraiseList;
	}

	public String getMid() {
		return mid;
	}

	public List<CampusContactsReplyInfo> getAppraiseList() {
		return appraiseList;
	}
}
