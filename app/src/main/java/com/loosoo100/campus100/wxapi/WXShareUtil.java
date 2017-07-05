package com.loosoo100.campus100.wxapi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.loosoo100.campus100.R;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.utils.MyUtils;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 
 * @author yang 微信分享工具
 * 
 */
public class WXShareUtil {

	private IWXAPI api;
	private Context context;

	public WXShareUtil(Context context) {
		this.context = context;
		if (api == null) {
			api = WXAPIFactory.createWXAPI(context, MyConfig.WEIXIN_APP_ID);
			api.registerApp(MyConfig.WEIXIN_APP_ID);
		}
	}

	/**
	 * 分享到微信好友
	 * 
	 * @param title
	 *            标题
	 * @param description
	 *            描述
	 * @param url
	 *            链接地址
	 */
	public void shareMessageToSession(String title, String description,
			String url, Bitmap bitmap) {
		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = url;

		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = title;
		msg.description = description;

		if (bitmap != null) {
			msg.thumbData = Util.bmpToByteArray(bitmap, true);
		} else {
			Bitmap thump = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.app_logo);
			thump = MyUtils.ResizeBitmap(thump, 100);
			msg.thumbData = Util.bmpToByteArray(thump, true);
		}

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = "loosoo";
		req.message = msg;
		req.scene = SendMessageToWX.Req.WXSceneSession;

		api.sendReq(req);

	}

	/**
	 * 分享到微信朋友圈
	 * 
	 * @param title
	 *            标题
	 * @param description
	 *            描述
	 * @param url
	 *            链接地址
	 */
	public void shareMessageToTimeLine(String title, String description,
			String url, Bitmap bitmap) {
		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = url;

		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = title;
		msg.description = description;

		if (bitmap != null) {
			msg.thumbData = Util.bmpToByteArray(bitmap, true);
		} else {
			Bitmap thump = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.app_logo);
			thump = MyUtils.ResizeBitmap(thump, 100);
			msg.thumbData = Util.bmpToByteArray(thump, true);
		}

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = "loosoo";
		req.message = msg;
		req.scene = SendMessageToWX.Req.WXSceneTimeline;

		api.sendReq(req);

	}

}
