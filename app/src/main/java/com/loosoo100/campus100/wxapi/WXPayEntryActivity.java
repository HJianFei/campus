package com.loosoo100.campus100.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.loosoo100.campus100.anyevent.MEventWXPay;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import de.greenrobot.event.EventBus;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;

    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;

    public int isPay = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.entry);

        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, MyConfig.WEIXIN_APP_ID, false);

        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        api.handleIntent(intent, this);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
        switch (req.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                finish();
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                finish();
                break;
        }
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == 0) {
                isPay = 1;
                ToastUtil.showToast(this, "支付成功");
                EventBus.getDefault().post(new MEventWXPay(true));
                finish();
            } else {
                isPay = 0;
                ToastUtil.showToast(this, "支付失败");
                EventBus.getDefault().post(new MEventWXPay(false));
                finish();
            }
        }
    }

    // private void goToShowMsg(ShowMessageFromWX.Req showReq) {
    // WXMediaMessage wxMsg = showReq.message;
    // WXAppExtendObject obj = (WXAppExtendObject) wxMsg.mediaObject;
    //
    // StringBuffer msg = new StringBuffer(); // 组织一个待显示的消息内容
    // msg.append("description: ");
    // msg.append(wxMsg.description);
    // msg.append("\n");
    // msg.append("extInfo: ");
    // msg.append(obj.extInfo);
    // msg.append("\n");
    // msg.append("filePath: ");
    // msg.append(obj.filePath);
    //
    // finish();
    // }
}