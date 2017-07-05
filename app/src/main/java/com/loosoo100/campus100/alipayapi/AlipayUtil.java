package com.loosoo100.campus100.alipayapi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.loosoo100.campus100.chat.utils.ToastUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class AlipayUtil {

    // 商户PID
    private static final String PARTNER = "2088121427982190";
    // 商户收款账号
    private static final String SELLER = "loosoo100@163.com";
    // 商户私钥，pkcs8格式
    private static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKqFSrp0ygnGQxtRFDSg09NSRTXzTp4UsndtJ/h9IKcu1hE4xPHQ/5Tkrh4whBPcprQjjRcPa8o8Fs+23cDic0E6qB0BrWecwl9ExGpgRbpIxxYQV2m46f6iEBw+RUulxRCYQiFFFqw74uaeCGV8Db6+kxN8fhyDaSZjTOjFE/XHAgMBAAECgYBr10Zua0QHMHrqQLmdmwS8BbC9sPVMZlyDyOXZchD/ilBZreRn5jxtBr7TWu18vjQzgrRNHDSYPq5SiRzvSoeGde6lN7StU97aPVFZhikctJ+VO6iI0SZGLir4pACK5IV9nNLciNdVRKHaTqUyQTHNCzSw1GM9Ztmkjiwul5YKwQJBANNyg3o+6aiBRU1sMx+8wp3b2Roob0NRWaQGh0lW8pB19ACF8oyd8iqyJAyBSmqfmrLEcVeWeOZ3q7LV76hbVKcCQQDOczEE+IziuvO1HzzubqZivnV88fgxy6rDwrgmprgSe+WpoCtpayf/ccf1IL3/ShF19yOZJ0vLTFDB/UDGK9nhAkAYRNJ2W3YyEvRDWKHdiKrFt5AJgo6SqWmie+VXM5WPoxooXTdjkFVVNTESBlzmM/9reUSRCwJBYYbhddCWADWNAkEAoD2gqq8RC1r4lnjsnlScKMCUCKr3bg8bOF8G+FknPpGULRj5GRXnMcpq3cTQNDqVWu68Xr+1gNJ3TFr5z9dxgQJAHXJsTNy233i4I2NiCpQSmJjfBed6Y8D0gq5WDVZdRTh/fwE6Hl4qWGPpZrOkazePEyOdaxa9uWRhU1uYjil6ZQ==";
    // 支付宝支付回调接口
    private static final String ALIPAY_NOTIFY_URL = "http://www.campus100.cn/App/index.php/Home/ApiwxNative2/notifyalipay";
    // 支付宝公钥
    // private static final String RSA_PUBLIC =
    // "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
    private static final int SDK_PAY_FLAG = 1;

    private Activity activity;

    private String money = ""; // 金额
    private String body = ""; // 描述
    private String subject = ""; // 商品名
    private String uid = ""; // 用户ID
    private String oid = ""; // 订单ID
    private String type = ""; // 支付类型 小卖部 ： mark 礼物盒子：gift 活动：acti 借贷：disa

    public int isPay = 0;// 1支付成功 -1支付失败

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        isPay = 1;
                        ToastUtil.showToast(activity,"支付成功");
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            ToastUtil.showToast(activity,"支付结果确认中");

                        } else {
                            isPay = -1;
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            ToastUtil.showToast(activity,"支付失败");

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    public AlipayUtil(Activity activity, String subject, String body,
                      String money, String uid, String oid, String type) {
        this.activity = activity;
        this.subject = subject;
        this.body = body;
        this.money = money;
        this.uid = uid;
        this.oid = oid;
        this.type = type;
    }

    public void pay() {
        if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE)
                || TextUtils.isEmpty(SELLER)) {
            new AlertDialog.Builder(activity)
                    .setTitle("警告")
                    .setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialoginterface, int i) {
                                    //
                                    // finish();
                                }
                            }).show();
            return;
        }
        String orderInfo = getOrderInfo(subject, body, money);

        /**
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
         */
        String sign = sign(orderInfo);
        if (sign == null) {
        }
        try {
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(activity);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * create the order info. 创建订单信息
     */
    private String getOrderInfo(String subject, String body, String price) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        long time = System.currentTimeMillis();
        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + uid + "A" + time + "B" + type
                + oid + "\"";
        // orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + ALIPAY_NOTIFY_URL + "\"";
        // orderInfo += "&notify_url=" + "\"" +
        // "http://notify.msp.hk/notify.htm"
        // + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     */
    private String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
                Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    private String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }
}
