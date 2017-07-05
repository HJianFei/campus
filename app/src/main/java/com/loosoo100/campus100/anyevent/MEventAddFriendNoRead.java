package com.loosoo100.campus100.anyevent;

/**
 * 新的好友是否有未读消息
 * @author yang
 *
 */
public class MEventAddFriendNoRead {
	private boolean change;

	public MEventAddFriendNoRead(boolean change) {
		this.change = change;
	}

	public boolean isChange() {
		return change;
	}
}
