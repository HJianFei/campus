package com.loosoo100.campus100.anyevent;

/**
 * 校园圈
 * 
 * @author yang
 * 
 */
public class MEventCampusContactsAllDel {
	private boolean change;
	// 删除的圈圈ID
	private String delCid;

	public MEventCampusContactsAllDel(boolean change, String delCid) {
		this.change = change;
		this.delCid = delCid;
	}

	public boolean isChange() {
		return change;
	}

	public String getDelCid() {
		return delCid;
	}
}
