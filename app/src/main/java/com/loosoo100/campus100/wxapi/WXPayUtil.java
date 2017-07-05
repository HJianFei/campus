package com.loosoo100.campus100.wxapi;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.utils.MD5;
import com.loosoo100.campus100.utils.MyHttpUtils;
import com.loosoo100.campus100.utils.MyUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class WXPayUtil {
    /**
     * 微信支付回调接口
     */
    private static final String WXPAY_NOTIFY_URL = "http://www.campus100.cn/"
            + "App/index.php/Home/ApiwxNative2/notifyapp";
    /**
     * 获取预支付订单号
     */
    private static final String WXPAY_GETPREPAYORDER = "http://www.campus100.cn/"
            + "App/index.php/Home/ApisharePay/getPrePayOrder";

    private Context ctx;
    private PayReq req;
    private IWXAPI msgApi = null;
    private Map<String, String> resultunifiedorder;
    private String productName, productDetail, orderId;
    private String userId;
    private float price;
    private ProgressDialog dialog;

    public WXPayUtil(Context ctx, String orderId, String userId,
                     String productName, String productDetail, float price) {
        this.ctx = ctx;
        this.productName = productName;
        this.productDetail = productDetail;
        this.orderId = orderId;
        this.userId = userId;
        this.price = Float.valueOf(MyUtils.decimalFormat.format(price));
        req = new PayReq();
        msgApi = WXAPIFactory.createWXAPI(ctx, MyConfig.WEIXIN_APP_ID);
        msgApi.registerApp(MyConfig.WEIXIN_APP_ID);
    }

    public void sendPay() {
//        GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
//        getPrepayId.execute();
        dialog = ProgressDialog.show(ctx, "提示", "正在获取预支付订单...");
        new Thread() {
            @Override
            public void run() {
                getPrePayOrder(WXPAY_GETPREPAYORDER);
            }
        }.start();
    }


    private class GetPrepayIdTask extends
            AsyncTask<Void, Void, Void> {

        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(ctx, "提示", "正在获取预支付订单...");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (dialog != null) {
                dialog.dismiss();
            }

            if (resultunifiedorder != null && resultunifiedorder.containsKey("prepay_id")
                    && resultunifiedorder.get("prepay_id") != null
                    ) {
                sendPayReq();
            } else {
                ToastUtil.showToast(ctx, "获取预支付订单失败");
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Void doInBackground(Void... params) {
            getPrePayOrder(WXPAY_GETPREPAYORDER);
            return null;
        }

    }


    private void sendPayReq() {
//        msgApi = WXAPIFactory.createWXAPI(ctx, req.appId);
//        msgApi.registerApp(req.appId);
        boolean b = msgApi.registerApp(MyConfig.WEIXIN_APP_ID);
        b = msgApi.sendReq(req);
//        msgApi.sendReq(req);
    }

    /**
     * 获取预支付订单号
     *
     * @param uploadHost
     */
    private void getPrePayOrder(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("total_fee", (int) (price * 100) + "");
        params.addBodyParameter("uid", userId);
        params.addBodyParameter("oid", orderId);
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo", responseInfo.result);
                        String replace = responseInfo.result.toString().trim().replace("package", "packageX");
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                            Gson gson = new Gson();
                            WXBean wxBean = gson.fromJson(replace, WXBean.class);
                            if (wxBean.getPrepayid()!= null && !wxBean.getPrepayid().equals("null") && !wxBean.getPrepayid().equals("")) {
                                req.appId = wxBean.getAppid();
                                req.prepayId = wxBean.getPrepayid();
                                req.partnerId = wxBean.getPartnerid();
                                req.timeStamp = wxBean.getTimestamp();
                                req.packageValue = wxBean.getPackageX();
                                req.nonceStr =wxBean.getNoncestr();
                                req.sign = wxBean.getSign();
                                sendPayReq();
                            } else {
                                ToastUtil.showToast(ctx, "获取预支付订单失败");
                            }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        ToastUtil.showToast(ctx, "获取预支付订单失败");
                    }
                });
    }
}
