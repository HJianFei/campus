package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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
import com.loosoo100.campus100.beans.AddressInfo;
import com.loosoo100.campus100.beans.PayGiftGoods;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.GoodsInfoInCart;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * @author yang 礼物说我想要支付activity
 */
public class PayIWantActivity extends Activity implements OnClickListener {
    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view

    @ViewInject(R.id.rl_back)
    private RelativeLayout rl_back; // 返回按钮

    @ViewInject(R.id.ll_receiptAddress)
    private LinearLayout ll_receiptAddress; // 收货地址
    @ViewInject(R.id.ll_address)
    private LinearLayout ll_address; // 有收货地址时显示地址

    @ViewInject(R.id.ll_payByMyself)
    private LinearLayout ll_payByMyself; // 自己支付

    @ViewInject(R.id.ll_payByFriend)
    private LinearLayout ll_payByFriend; // 朋友支付

    @ViewInject(R.id.tv_expressFee)
    private TextView tv_expressFee; // 配送费

    @ViewInject(R.id.btn_sure_pay)
    private Button btn_sure_pay; // 确认订单按钮

    @ViewInject(R.id.iv_select_self)
    private ImageView iv_select_self; // 自己支付选择图标

    @ViewInject(R.id.iv_select_friend)
    private ImageView iv_select_friend; // 朋友支付选择图标

    @ViewInject(R.id.ll_redPaper)
    private LinearLayout ll_redPaper; // 红包
    @ViewInject(R.id.et_remark)
    private EditText et_remark; // 备注

    @ViewInject(R.id.tv_goodsName)
    private TextView tv_goodsName; // 商品名
    @ViewInject(R.id.tv_goodsCount)
    private TextView tv_goodsCount; // 商品数量
    @ViewInject(R.id.tv_goodsAttr)
    private TextView tv_goodsAttr; // 商品属性
    @ViewInject(R.id.tv_goodsPrice)
    private TextView tv_goodsPrice; // 商品价格
    @ViewInject(R.id.tv_goodsCountOrder)
    private TextView tv_goodsCountOrder; // 商品数量加减中间的数量
    @ViewInject(R.id.tv_price)
    private TextView tv_price; // 商品总金额
    @ViewInject(R.id.tv_totalMoney)
    private TextView tv_totalMoney; // 加运费后总金额
    @ViewInject(R.id.iv_goodsIcon)
    private ImageView iv_goodsIcon; // 商品图标
    @ViewInject(R.id.ib_add)
    private ImageButton ib_add; // 添加
    @ViewInject(R.id.ib_reduce)
    private ImageButton ib_reduce; // 减少
    @ViewInject(R.id.tv_name)
    private TextView tv_name; // 收货人
    @ViewInject(R.id.tv_tel)
    private TextView tv_tel; // 收货电话
    @ViewInject(R.id.tv_address)
    private TextView tv_address; // 收货地址
    @ViewInject(R.id.progress)
    private RelativeLayout progress; // 加载动画
    @ViewInject(R.id.progress_update)
    private RelativeLayout progress_update; // 点击付款时的加载动画

    private String giftgoodsId = "";
    private String giftgoodsSn = "";
    private String giftgoodsName = "";
    private String giftgoodsImg = "";
    private float shopPrice;
    private int stock;
    private String goodsAttrName = "";
    private String goodsAttrId = "";

    private String orderId = "";
    private String skuId = "";

    private AddressInfo addressInfo;

    private String uid = "";
    private String userName = "";
    private String addressId = "";
    private String userAddress = "";
    private String userPhone = "";
    private String province = "";
    private String city = "";
    private String area = "";
    private String pid = "";
    private String cid = "";
    private String aid = "";
    private String needType = "0"; // 0自己付款 2朋友帮付
    private String payType = "0"; // 0钱包支付 1微信支付2支付宝支付
    private float deliverMoney = 0; // 配送费
    private float money;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (addressInfo != null) {
                userName = addressInfo.getUserName();
                addressId = addressInfo.getAddressId();
                userPhone = addressInfo.getUserPhone();
                tv_name.setText(userName);
                tv_tel.setText(userPhone);
                province = addressInfo.getProvince();
                city = addressInfo.getCity();
                area = addressInfo.getArea();
                pid = addressInfo.getPid();
                cid = addressInfo.getCid();
                aid = addressInfo.getAid();
                if (addressInfo.getSchool().equals("")
                        || addressInfo.getSchool().equals("null")) {
                    userAddress = province + city + area + " "
                            + addressInfo.getAddress();
                } else {
                    userAddress = addressInfo.getSchool() + " "
                            + addressInfo.getAddress();
                }
                tv_address.setText(userAddress);
            } else {
                ll_address.setVisibility(View.GONE);
            }
            initView();
            progress.setVisibility(View.GONE);
        }

        ;
    };

    private Handler handler2 = new Handler() {
        public void handleMessage(android.os.Message msg) {
            // 提交订单后销毁礼物说列表详情和商品详情页
            EventBus.getDefault().post(new MEventGiftGoodsDetails(true));
            EventBus.getDefault().post(new MEventGiftGoodsList(true));
            // if (GiftListDetailActivity.activity != null) {
            // GiftListDetailActivity.activity.finish();
            // }
            if (needType.equals("0")) {
                Intent payIntent = new Intent(PayIWantActivity.this,
                        PayActivity.class);
//                payIntent.putExtra(
//                        "money",
//                        Float.valueOf(MyUtils.decimalFormat.format(shopPrice
//                                * GoodsInfoInCart.goodsCount)));
                payIntent.putExtra(
                        "money",money);
                payIntent.putExtra("orderId", orderId);
                payIntent.putExtra("cid", "0");
                payIntent.putExtra("type", "gift");
                startActivityForResult(payIntent, 2);
            } else if (needType.equals("2")) {
                // 跳转到凑一凑填写页面
                Intent friendIntent = new Intent(PayIWantActivity.this,
                        GiftTogetherActivity.class);
                friendIntent.putExtra("oid", orderId);
                friendIntent.putExtra("giftgoodsId", giftgoodsId);
                friendIntent.putExtra("giftgoodsName", giftgoodsName);
                friendIntent.putExtra("giftgoodsImg", giftgoodsImg);
                friendIntent.putExtra("shopPrice", shopPrice);
                friendIntent.putExtra("count", GoodsInfoInCart.goodsCount);
                friendIntent.putExtra("type", 3);
                startActivity(friendIntent);
                PayIWantActivity.this.finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_gift_iwant);
        ViewUtils.inject(this);

        MyUtils.setStatusBarHeight(this, iv_empty);
        progress.setVisibility(View.VISIBLE);

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

        new Thread() {
            public void run() {
                List<AddressInfo> addressInfos = GetData
                        .getAddressInfos(MyConfig.URL_JSON_ADDRESS + uid);
                if (addressInfos != null && addressInfos.size() > 0) {
                    addressInfo = addressInfos.get(0);
                }
                if (!isDestroyed()) {
                    handler.sendEmptyMessage(0);
                }
            }

            ;
        }.start();

    }

    private void initView() {
        rl_back.setOnClickListener(this);
        ll_receiptAddress.setOnClickListener(this);
        ll_payByMyself.setOnClickListener(this);
        ll_payByFriend.setOnClickListener(this);
        btn_sure_pay.setOnClickListener(this);
        ib_add.setOnClickListener(this);
        ib_reduce.setOnClickListener(this);
        ll_redPaper.setOnClickListener(this);

        Glide.with(this).load(giftgoodsImg).placeholder(R.drawable.imgloading) // 设置占位图
                .override(200,200).into(iv_goodsIcon);

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

            // 添加按钮
            case R.id.ib_add:
                if (GoodsInfoInCart.goodsCount < stock) {
                    GoodsInfoInCart.goodsCount++;
                    updateCountAndPrice();
                } else {
                    ToastUtil.showToast(this, "已达最大库存,不能继续添加");
                }
                break;

            // 减少按钮
            case R.id.ib_reduce:
                if (GoodsInfoInCart.goodsCount > 1) {
                    GoodsInfoInCart.goodsCount--;
                    updateCountAndPrice();
                }
                break;

            // 收货地址
            case R.id.ll_receiptAddress:
                Intent intent = new Intent(PayIWantActivity.this,
                        AddressActivity.class);
                intent.putExtra("type", 1);
                startActivityForResult(intent, 1);
                break;

            // 自己支付
            case R.id.ll_payByMyself:
                needType = "0";
                resetPaySelect();
                iv_select_self.setImageResource(R.drawable.icon_select);
                break;

            // 凑一凑
            case R.id.ll_payByFriend:
                needType = "2";
                resetPaySelect();
                iv_select_friend.setImageResource(R.drawable.icon_select);
                break;

            // 红包
            case R.id.ll_redPaper:
                Intent intentRedPaper = new Intent(PayIWantActivity.this,
                        RedPaperActivity.class);
                startActivity(intentRedPaper);
                break;

            // 确认订单按钮
            case R.id.btn_sure_pay:
                // 判断是否选择了收货地址
                if (ll_address.getVisibility() == View.GONE) {
                    ToastUtil.showToast(PayIWantActivity.this, "请选择收货地址");
                    return;
                }
                if (province.equals("") || city.equals("") || area.equals("")
                        || province.equals("null") || city.equals("null")
                        || area.equals("null")) {
                    ToastUtil.showToast(PayIWantActivity.this, "请选择礼物盒子的收货地址");
                    return;
                }
               money= Float.valueOf(MyUtils.decimalFormat.format(shopPrice
                       * GoodsInfoInCart.goodsCount+deliverMoney));
                progress_update.setVisibility(View.VISIBLE);
                new Thread() {
                    public void run() {
                        postAllXUtils(MyConfig.URL_POST_GIFT_PAY);
                    }

                    ;
                }.start();
                break;

        }
    }

    /**
     * 还原支付方式选中图标
     */
    public void resetPaySelect() {
        iv_select_self.setImageResource(R.drawable.icon_select_not);
        iv_select_friend.setImageResource(R.drawable.icon_select_not);
    }

    public void updateCountAndPrice() {
        tv_goodsCountOrder.setText("" + GoodsInfoInCart.goodsCount);
        tv_goodsCount.setText("数量x" + GoodsInfoInCart.goodsCount);
        tv_price.setText("￥" + shopPrice * GoodsInfoInCart.goodsCount);
        money=shopPrice
                * GoodsInfoInCart.goodsCount+deliverMoney;
        tv_totalMoney.setText("￥"
                + MyUtils.decimalFormat.format(money));

        // shopPrice=Float.valueOf(MyUtils.decimalFormat.format(shopPrice *
        // GoodsInfoInCart.goodsCount));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    ll_address.setVisibility(View.VISIBLE);
                    userName = data.getExtras().getString("name");
                    addressId = data.getExtras().getString("addressId");
                    userPhone = data.getExtras().getString("phone");
                    province = data.getExtras().getString("province");
                    city = data.getExtras().getString("city");
                    area = data.getExtras().getString("area");
                    pid = data.getExtras().getString("pid");
                    cid = data.getExtras().getString("cid");
                    aid = data.getExtras().getString("aid");
                    tv_name.setText(userName);
                    tv_tel.setText(userPhone);
                    if (data.getExtras().getString("school").equals("")
                            || data.getExtras().getString("school").equals("null")) {
                        if (province.equals(city)) {
                            userAddress = city + area + " "
                                    + data.getExtras().getString("address");
                        } else {
                            userAddress = province + city + area + " "
                                    + data.getExtras().getString("address");
                        }
                    } else {
                        userAddress = data.getExtras().getString("school") + " "
                                + data.getExtras().getString("address");
                    }
                    tv_address.setText(userAddress);
                    break;
                case 2:
                    payType = "0";
                    if (!orderId.equals("")) {
                        progress_update.setVisibility(View.VISIBLE);
                        new Thread() {
                            public void run() {
                                postOrderIdXUtils(MyConfig.URL_POST_GIFT_PAY_SUCCESS);
                            }

                            ;
                        }.start();
                    }
                    break;
            }
        }
        if (resultCode == RESULT_CANCELED && requestCode == 2) {
            // 跳转到已收到的详情页面
            Intent intent = new Intent(PayIWantActivity.this,
                    GiftOrderDetailReceiveActivity.class);
            intent.putExtra("index", 0);
            intent.putExtra("type", 3);
            intent.putExtra("oid", orderId);
            startActivity(intent);
            PayIWantActivity.this.finish();
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
//        params.addBodyParameter(
//                "totalMoney",
//                MyUtils.decimalFormat.format(shopPrice
//                        * GoodsInfoInCart.goodsCount));
        params.addBodyParameter(
                "totalMoney",Float.valueOf(MyUtils.decimalFormat.format(shopPrice
                        * GoodsInfoInCart.goodsCount))+"");
        params.addBodyParameter("remarks", et_remark.getText().toString());
        params.addBodyParameter("userId", uid);
        params.addBodyParameter("userName", userName);
        params.addBodyParameter("addressId", addressId + "");
        params.addBodyParameter("areaId1", pid);
        params.addBodyParameter("areaId2", cid);
        params.addBodyParameter("areaId3", aid);
        params.addBodyParameter("userAddress", userAddress);
        params.addBodyParameter("needType", needType);
        params.addBodyParameter("deliverMoney", deliverMoney + "");
        params.addBodyParameter("userPhone", userPhone);
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
                            ToastUtil.showToast(PayIWantActivity.this, "库存不足");
                            return;
                        }
                        if (result.equals("-4")) {
                            ToastUtil.showToast(PayIWantActivity.this, "请先设置支付密码");
                            Intent intent = new Intent(PayIWantActivity.this,
                                    PayPWUpdateActivity.class);
                            startActivity(intent);
                            return;
                        }
                        if (result.equals("0")) {
                            ToastUtil.showToast(PayIWantActivity.this, "下单失败");
                            return;
                        }
                        orderId = result;
                        if (!isDestroyed()) {
                            handler2.sendEmptyMessage(0);
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        ToastUtil.showToast(PayIWantActivity.this, "提交订单失败");
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
        params.addBodyParameter("userId", uid);
        params.addBodyParameter("payType", payType);
        params.addBodyParameter("isPay", "1");
//        params.addBodyParameter("needPay", MyUtils.decimalFormat.format(shopPrice
        params.addBodyParameter("needPay", money+"");
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
                        if (result.equals("1")) {
                            ToastUtil.showToast(PayIWantActivity.this, "支付成功");
                        }else{
                            ToastUtil.showToast(PayIWantActivity.this, "支付失败");
                        }
                        Intent intent = new Intent(PayIWantActivity.this,
                                GiftOrderDetailReceiveActivity.class);
                        intent.putExtra("index", 0);
                        intent.putExtra("type", 3);
                        intent.putExtra("oid", orderId);
                        startActivity(intent);
                        PayIWantActivity.this.finish();
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        progress_update.setVisibility(View.GONE);
                        ToastUtil.showToast(PayIWantActivity.this, "支付失败");
                        Intent intent = new Intent(PayIWantActivity.this,
                                GiftOrderDetailReceiveActivity.class);
                        intent.putExtra("index", 0);
                        intent.putExtra("type", 3);
                        intent.putExtra("oid", orderId);
                        startActivity(intent);
                        PayIWantActivity.this.finish();
                    }
                });
    }
}
