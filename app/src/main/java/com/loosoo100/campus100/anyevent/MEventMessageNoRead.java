package com.loosoo100.campus100.anyevent;

/**
 * 系统消息是否有未读消息
 * @author yang
 *
 */
public class MEventMessageNoRead {
	private boolean change;

	public MEventMessageNoRead(boolean change) {
		this.change = change;
	}

	public boolean isChange() {
		return change;
	}
}
