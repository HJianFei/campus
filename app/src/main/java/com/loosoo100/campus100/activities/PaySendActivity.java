package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.anyevent.MEventGiftGoodsDetails;
import com.loosoo100.campus100.anyevent.MEventGiftGoodsList;
import com.loosoo100.campus100.beans.PayGiftGoods;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.GoodsInfoInCart;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.MyUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * @author yang 礼物说送给TA支付activity
 */
public class PaySendActivity extends Activity implements OnClickListener {
    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view

    @ViewInject(R.id.rl_back)
    private RelativeLayout rl_back; // 返回按钮
    @ViewInject(R.id.ll_goods)
    private LinearLayout ll_goods; // 商品详情布局

    @ViewInject(R.id.tv_expressFee)
    private TextView tv_expressFee; // 配送费

    @ViewInject(R.id.btn_sure_pay)
    private Button btn_sure_pay; // 确认订单按钮

    @ViewInject(R.id.tv_goodsName)
    private TextView tv_goodsName; // 商品名
    @ViewInject(R.id.tv_goodsCount)
    private TextView tv_goodsCount; // 商品数量
    @ViewInject(R.id.tv_goodsAttr)
    private TextView tv_goodsAttr; // 商品属性
    @ViewInject(R.id.tv_goodsCountOrder)
    private TextView tv_goodsCountOrder; // 商品加减中间的数量
    @ViewInject(R.id.tv_goodsPrice)
    private TextView tv_goodsPrice; // 商品价格
    @ViewInject(R.id.tv_totalMoney)
    private TextView tv_totalMoney; // 加运费后总金额
    @ViewInject(R.id.iv_goodsIcon)
    private ImageView iv_goodsIcon; // 商品图标
    @ViewInject(R.id.progress_update)
    private RelativeLayout progress_update; // 点击付款时的加载动画

    @ViewInject(R.id.ib_add)
    private ImageButton ib_add; // 添加
    @ViewInject(R.id.ib_reduce)
    private ImageButton ib_reduce; // 减少

    private String giftgoodsId = "";
    private String giftgoodsSn = "";
    private String giftgoodsName = "";
    private String giftgoodsImg = "";
    private float shopPrice;
    private int stock;
    private String goodsAttrName = "";
    private String goodsAttrId = "";
    private String skuId = "";

    private float deliverMoney = 0; // 配送费
    private String orderId = "";

    private String uid = "";
    private String userName = "";

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            // 提交订单后销毁礼物说列表详情和商品详情页
            EventBus.getDefault().post(new MEventGiftGoodsDetails(true));
            EventBus.getDefault().post(new MEventGiftGoodsList(true));
            Intent payIntent = new Intent(PaySendActivity.this,
                    PayActivity.class);
            payIntent.putExtra(
                    "money",
                    Float.valueOf(MyUtils.decimalFormat.format(shopPrice
                            * GoodsInfoInCart.goodsCount+deliverMoney)));
            payIntent.putExtra("orderId", orderId);
            payIntent.putExtra("cid", "0");
            payIntent.putExtra("type", "gift");
            startActivityForResult(payIntent, 1);
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_gift_send);
        ViewUtils.inject(this);

        MyUtils.setStatusBarHeight(this, iv_empty);

        uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.USERID, "");

        giftgoodsId = getIntent().getExtras().getString("giftgoodsId");
        giftgoodsSn = getIntent().getExtras().getString("giftgoodsSn");
        giftgoodsName = getIntent().getExtras().getString("giftgoodsName");
        giftgoodsImg = getIntent().getExtras().getString("giftgoodsImg");
        shopPrice = getIntent().getExtras().getFloat("shopPrice");
        deliverMoney = getIntent().getExtras().getFloat("deliverMoney");
        stock = getIntent().getExtras().getInt("stock");
        goodsAttrId = getIntent().getExtras().getString("goodsAttrId");
        skuId = getIntent().getExtras().getString("skuId");
        goodsAttrName = getIntent().getExtras().getString("goodsAttrName");

        initView();

    }

    private void initView() {
        rl_back.setOnClickListener(this);
        btn_sure_pay.setOnClickListener(this);
        ll_goods.setOnClickListener(this);
        ib_add.setOnClickListener(this);
        ib_reduce.setOnClickListener(this);

        Glide.with(this).load(giftgoodsImg).placeholder(R.drawable.imgloading) // 设置占位图
                .into(iv_goodsIcon);
        tv_goodsName.setText(giftgoodsName);
        tv_goodsPrice.setText("￥" + shopPrice);
        tv_expressFee.setText("￥" + deliverMoney);
        tv_goodsAttr.setText(goodsAttrName);
        updateCountAndPrice();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 返回按钮
            case R.id.rl_back:
                this.finish();
                break;

            // 确认订单按钮
            case R.id.btn_sure_pay:
                progress_update.setVisibility(View.VISIBLE);
                new Thread() {
                    public void run() {
                        postAllXUtils(MyConfig.URL_POST_GIFT_PAY);
                    }

                    ;
                }.start();
                break;

            // 添加按钮
            case R.id.ib_add:
                if (GoodsInfoInCart.goodsCount < stock) {
                    GoodsInfoInCart.goodsCount++;
                    updateCountAndPrice();
                } else {
                    ToastUtil.showToast(PaySendActivity.this, "已达最大库存,不能继续添加");
                }
                break;

            // 减少按钮
            case R.id.ib_reduce:
                if (GoodsInfoInCart.goodsCount > 1) {
                    GoodsInfoInCart.goodsCount--;
                    updateCountAndPrice();
                }
                break;

        }
    }

    public void updateCountAndPrice() {
        tv_goodsCountOrder.setText("" + GoodsInfoInCart.goodsCount);
        tv_goodsCount.setText("数量x" + GoodsInfoInCart.goodsCount);
        tv_totalMoney.setText("￥"
                + MyUtils.decimalFormat.format(shopPrice
                * GoodsInfoInCart.goodsCount+deliverMoney));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1) {
            progress_update.setVisibility(View.VISIBLE);
            new Thread() {
                public void run() {
                    if (!orderId.equals("")) {
                        postOrderIdXUtils(MyConfig.URL_POST_GIFT_PAY_SUCCESS);
                    }
                }

                ;
            }.start();
        }
        if (resultCode == RESULT_CANCELED && requestCode == 1) {
            // 跳转到送出的礼物的详情页面
            Intent intent = new Intent(PaySendActivity.this,
                    GiftOrderDetailSendActivity.class);
            intent.putExtra("index", 0);
            intent.putExtra("type", 3);
            intent.putExtra("oid", orderId);
            startActivity(intent);
            PaySendActivity.this.finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void postAllXUtils(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        Gson gson = new Gson();
        RequestParams params = new RequestParams();

        List<PayGiftGoods> goodsList = new ArrayList<PayGiftGoods>();
        PayGiftGoods goods = new PayGiftGoods();
        goods.setGoodsAttrId(goodsAttrId);
        goods.setGoodsAttrName(goodsAttrName);
        goods.setGoodsId(giftgoodsId);
        goods.setGiftgoodsSn(giftgoodsSn);
        goods.setGoodsName(giftgoodsName);
        goods.setGoodsNums(GoodsInfoInCart.goodsCount);
        goods.setGoodsPrice(shopPrice);
        goods.setSkuId(skuId);
        goods.setGoodsThums(giftgoodsImg);
        goodsList.add(goods);

        NameValuePair info = new BasicNameValuePair("Allgoods",
                gson.toJson(goodsList));
        params.addBodyParameter(
                "totalMoney",
                MyUtils.decimalFormat.format(shopPrice
                        * GoodsInfoInCart.goodsCount));
        params.addBodyParameter("userId", uid + "");
        params.addBodyParameter("needType", "1");
        params.addBodyParameter("deliverMoney", deliverMoney + "");
        params.addBodyParameter("userName", userName);
        params.addBodyParameter(info);

        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo", responseInfo.result);
                        progress_update.setVisibility(View.GONE);
                        String result = "";
                        try {
                            JSONObject jsonObject = new JSONObject(
                                    responseInfo.result);
                            result = jsonObject.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result.equals("-1")) {
                            ToastUtil.showToast(PaySendActivity.this, "库存不足");
                            return;
                        }
                        if (result.equals("-4")) {
                            ToastUtil.showToast(PaySendActivity.this, "请先设置支付密码");
                            Intent intent = new Intent(PaySendActivity.this,
                                    PayPWUpdateActivity.class);
                            startActivity(intent);
                            return;
                        }
                        if (result.equals("0")) {
                            ToastUtil.showToast(PaySendActivity.this, "下单失败");
                            return;
                        }
                        orderId = result;
                        if (!isDestroyed()) {
                            handler.sendEmptyMessage(0);
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        ToastUtil.showToast(PaySendActivity.this, "提交订单失败");
                        progress_update.setVisibility(View.GONE);
                    }
                });
    }

    /**
     * 付款成功后提交订单ID
     *
     * @param uploadHost
     */
    private void postOrderIdXUtils(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("orderId", orderId);
        params.addBodyParameter("isPay", "1");
        params.addBodyParameter("userId", uid);
//		params.addBodyParameter("payType", payType);
        params.addBodyParameter("needPay", MyUtils.decimalFormat.format(shopPrice
                * GoodsInfoInCart.goodsCount));
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo", responseInfo.result);
                        progress_update.setVisibility(View.GONE);
                        // 跳转到送出的礼物的待送出的详情页面
                        String result = "";
                        try {
                            JSONObject jsonObject = new JSONObject(
                                    responseInfo.result);
                            result = jsonObject.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result.equals("1")) {
                            ToastUtil.showToast(PaySendActivity.this, "支付成功");
                            Intent intent = new Intent(PaySendActivity.this,
                                    GiftSendActivity.class);
                            intent.putExtra("index", 0);
                            intent.putExtra("type", 3);
                            intent.putExtra("oid", orderId);
                            startActivity(intent);
                            PaySendActivity.this.finish();
                        } else {
                            ToastUtil.showToast(PaySendActivity.this, "支付失败");
                            Intent intent = new Intent(PaySendActivity.this,
                                    GiftOrderDetailSendActivity.class);
                            intent.putExtra("index", 0);
                            intent.putExtra("type", 3);
                            intent.putExtra("oid", orderId);
                            startActivity(intent);
                            PaySendActivity.this.finish();
                        }

                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        ToastUtil.showToast(PaySendActivity.this, "支付失败");
                        progress_update.setVisibility(View.GONE);
                        Intent intent = new Intent(PaySendActivity.this,
                                GiftOrderDetailSendActivity.class);
                        intent.putExtra("index", 0);
                        intent.putExtra("type", 3);
                        intent.putExtra("oid", orderId);
                        startActivity(intent);
                        PaySendActivity.this.finish();
                    }
                });
    }

}
