package com.loosoo100.campus100.beans;

import android.graphics.Bitmap;

/**
 * 
 * @author yang 在线咨询信息
 * 
 */
public class OnlineConsultInfo {
	// 聊天内容
	private String content;
	// 头像
	private Bitmap headShotBitmap;
	// 发送的图片
	private Bitmap contentBitmap;
	// 判断是否自己发送的信息
	private boolean isSelfContent;

	public Bitmap getContentBitmap() {
		return contentBitmap;
	}

	public void setContentBitmap(Bitmap contentBitmap) {
		this.contentBitmap = contentBitmap;
	}

	public boolean isSelfContent() {
		return isSelfContent;
	}

	public void setSelfContent(boolean isSelfContent) {
		this.isSelfContent = isSelfContent;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Bitmap getHeadShotBitmap() {
		return headShotBitmap;
	}

	public void setHeadShotBitmap(Bitmap headShotBitmap) {
		this.headShotBitmap = headShotBitmap;
	}

}
