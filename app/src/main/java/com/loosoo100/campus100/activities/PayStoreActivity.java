package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.loosoo100.campus100.adapters.OrderListAdapter;
import com.loosoo100.campus100.alipayapi.AlipayUtil;
import com.loosoo100.campus100.anyevent.MEventStoreCart;
import com.loosoo100.campus100.beans.AddressInfo;
import com.loosoo100.campus100.beans.BusinessInfo;
import com.loosoo100.campus100.beans.CartInfo;
import com.loosoo100.campus100.beans.PayStoreGoods;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.GoodsInfoInCart;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.fragments.StoreFragment;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.wxapi.WXPayUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * @author yang 校园超市确认订单activity
 */
public class PayStoreActivity extends Activity implements OnClickListener {
    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view

    @ViewInject(R.id.rl_back)
    private RelativeLayout rl_back; // 返回按钮

    @ViewInject(R.id.ll_receiptAddress)
    private LinearLayout ll_receiptAddress; // 收货地址
    @ViewInject(R.id.ll_address)
    private LinearLayout ll_address; // 有收货地址时显示地址

    @ViewInject(R.id.tv_name)
    private TextView tv_name; // 收货人
    @ViewInject(R.id.tv_tel)
    private TextView tv_tel; // 收货电话
    @ViewInject(R.id.tv_address)
    private TextView tv_address; // 收货地址

    @ViewInject(R.id.ll_sendByBusiness)
    private LinearLayout ll_sendByBusiness; // 商家配送

    @ViewInject(R.id.ll_sendBySelf)
    private LinearLayout ll_sendBySelf; // 到店自取

    @ViewInject(R.id.ll_wallet)
    private LinearLayout ll_wallet; // 钱包支付

    @ViewInject(R.id.ll_weixin)
    private LinearLayout ll_weixin; // 微信支付

    @ViewInject(R.id.ll_zhifubao)
    private LinearLayout ll_zhifubao; // 支付宝支付

    @ViewInject(R.id.ll_redPaper)
    private LinearLayout ll_redPaper; // 红包

    @ViewInject(R.id.tv_totalMoney)
    private TextView tv_totalMoney; // 总价

    @ViewInject(R.id.tv_expressFee)
    private TextView tv_expressFee; // 配送费

    @ViewInject(R.id.et_remark)
    private EditText et_remark; // 备注

    @ViewInject(R.id.btn_sure_pay)
    private Button btn_sure_pay; // 确认订单按钮

    @ViewInject(R.id.iv_select_sendByBusiness)
    private ImageView iv_select_sendByBusiness; // 商家配送选择图标

    @ViewInject(R.id.iv_select_sendBySelf)
    private ImageView iv_select_sendBySelf; // 到店自取选择图标

    @ViewInject(R.id.iv_select_wallet)
    private ImageView iv_select_wallet; // 钱包支付选择图标

    @ViewInject(R.id.iv_select_weixin)
    private ImageView iv_select_weixin; // 微信支付选择图标

    @ViewInject(R.id.iv_select_zhifubao)
    private ImageView iv_select_zhifubao; // 支付宝支付选择图标

    @ViewInject(R.id.lv_order)
    private ListView lv_order; // 订单详情列表

    @ViewInject(R.id.progress)
    private RelativeLayout progress; // 加载动画

    @ViewInject(R.id.progress_update)
    private RelativeLayout progress_update; // 点击付款时的加载动画

    private List<Map<String, Object>> list;

    private final int ADDRESS_CODE = 1;
    private final int PAY_CODE = 2;

    private AddressInfo addressInfo;

    private String uid = "";
    private String userName = "";
    private String addressId = "";
    private String userAddress = "";
    private String userPhone = "";
    private String sid = ""; // 小卖部的学校ID
    private String said = ""; // 收货地址的学校ID
    private String province = "";
    private String city = "";
    private String area = "";
    private String isSelf = "0"; // 0商家配送1到店自取
    private String payType = "0"; // 0钱包支付 1微信支付 2支付宝支付

    private String orderId = "";

    private boolean isPaying = false; // 判断是否支付宝付款中
    private boolean paypwSetting = false; // 判断是否跳到设置支付密码界面
    private boolean goReceiver = false; // 判断是否跳到收货地址界面
    private boolean goRedPaper = false; // 判断是否跳到红包界面

    private AlipayUtil alipayUtil;

    private float money;    //支付金额

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (addressInfo != null) {
                userName = addressInfo.getUserName();
                addressId = addressInfo.getAddressId();
                userPhone = addressInfo.getUserPhone();
                said = addressInfo.getSid();
                province = addressInfo.getProvince();
                city = addressInfo.getCity();
                area = addressInfo.getArea();
                tv_name.setText(userName);
                tv_tel.setText(userPhone);
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
            initListView();
            progress.setVisibility(View.GONE);
        }

        ;
    };

    private Handler handler2 = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (payType.equals("0")) {
                Intent payIntent = new Intent(PayStoreActivity.this,
                        PayActivity.class);
                payIntent.putExtra("money", money);
                payIntent.putExtra("orderId", orderId);
                payIntent.putExtra("cid", "0");
                payIntent.putExtra("type", "mark");
                startActivityForResult(payIntent, PAY_CODE);
            } else if (payType.equals("1")) {
                WXPayUtil wp = new WXPayUtil(PayStoreActivity.this, "mark"
                        + orderId, uid, "loosoo", "", money);
                wp.sendPay();
            } else if (payType.equals("2")) {
                alipayUtil = new AlipayUtil(PayStoreActivity.this,
                        "校园100小卖部订单", "校园100小卖部订单",
                        (CartInfo.goodsPricetotal + BusinessInfo.expressFee)
                                + "", uid, orderId, "mark");
                alipayUtil.pay();
                isPaying = true;
            }
            new Thread() {
                public void run() {
                    while (isPaying) {
                        if (alipayUtil.isPay == -1 || alipayUtil.isPay == 1) {
                            isPaying = false;
                            Intent intent = new Intent(PayStoreActivity.this,
                                    StoreOrderDetailActivity.class);
                            intent.putExtra("oid", orderId);
                            intent.putExtra("type", 1);
                            intent.putExtra("index", 0);
                            startActivity(intent);
                            PayStoreActivity.this.finish();
                        }
                    }
                }

                ;
            }.start();
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_store);
        ViewUtils.inject(this);

        MyUtils.setStatusBarHeight(this, iv_empty);
        progress.setVisibility(View.VISIBLE);
        uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.USERID, "");
        sid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.SCHOOL_ID_STORE, "");

        isPaying = false;

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

    private void initListView() {
        list = StoreFragment.cartList;
        OrderListAdapter adapter = new OrderListAdapter(this, list);
        lv_order.setAdapter(adapter);
        // 设置lv_order的高度
        MyUtils.setListViewHeight(lv_order, 0);
    }

    private void initView() {
        rl_back.setOnClickListener(this);
        ll_receiptAddress.setOnClickListener(this);
        ll_sendByBusiness.setOnClickListener(this);
        ll_sendBySelf.setOnClickListener(this);
        ll_wallet.setOnClickListener(this);
        ll_weixin.setOnClickListener(this);
        ll_zhifubao.setOnClickListener(this);
        ll_redPaper.setOnClickListener(this);
        btn_sure_pay.setOnClickListener(this);

        setTotalPrice();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 返回按钮
            case R.id.rl_back:
                this.finish();
                break;

            // 收货地址
            case R.id.ll_receiptAddress:
                goReceiver = true;
                Intent intent = new Intent(PayStoreActivity.this,
                        AddressActivity.class);
                intent.putExtra("type", 1);
                startActivityForResult(intent, ADDRESS_CODE);
                break;

            // 商家配送
            case R.id.ll_sendByBusiness:
                isSelf = "0";
                resetSendSelect();
                iv_select_sendByBusiness.setImageResource(R.drawable.icon_select);
                break;

            // 到店自取
            case R.id.ll_sendBySelf:
                isSelf = "1";
                resetSendSelect();
                iv_select_sendBySelf.setImageResource(R.drawable.icon_select);
                break;

            // 钱包支付
            case R.id.ll_wallet:
                payType = "0";
                resetPaySelect();
                iv_select_wallet.setImageResource(R.drawable.icon_select);
                break;

            // 微信支付
            case R.id.ll_weixin:
                payType = "1";
                resetPaySelect();
                iv_select_weixin.setImageResource(R.drawable.icon_select);
                break;

            // 支付宝支付
            case R.id.ll_zhifubao:
                payType = "2";
                resetPaySelect();
                iv_select_zhifubao.setImageResource(R.drawable.icon_select);
                break;

            // 红包
            case R.id.ll_redPaper:
                goRedPaper = true;
                Intent intentRedPaper = new Intent(PayStoreActivity.this,
                        RedPaperActivity.class);
                startActivity(intentRedPaper);
                break;

            // 确认订单按钮
            case R.id.btn_sure_pay:
                paypwSetting = false;
                goReceiver = false;
                goRedPaper = false;
                // 判断是否选择了收货地址
                if (ll_address.getVisibility() == View.GONE) {
                    ToastUtil.showToast(PayStoreActivity.this, "请选择收货地址");
                    return;
                }
                if (said.equals("") || said.equals("null") || !said.equals(sid)) {
                    ToastUtil.showToast(PayStoreActivity.this, "收货学校与小卖部学校不一致");
                    return;
                }
                money = CartInfo.goodsPricetotal
                        + BusinessInfo.expressFee;
                // 选择了钱包支付或微信支付
                // if (payType.equals("0") || payType.equals("1")) {
                progress_update.setVisibility(View.VISIBLE);
                new Thread() {
                    public void run() {
                        postAllXUtils(MyConfig.URL_POST_GOODS_PAY);
                    }

                    ;
                }.start();

                break;

        }
    }

    /**
     * 还原配送方式选中图标
     */
    public void resetSendSelect() {
        iv_select_sendByBusiness.setImageResource(R.drawable.icon_select_not);
        iv_select_sendBySelf.setImageResource(R.drawable.icon_select_not);
    }

    /**
     * 还原支付方式选中图标
     */
    public void resetPaySelect() {
        iv_select_wallet.setImageResource(R.drawable.icon_select_not);
        iv_select_weixin.setImageResource(R.drawable.icon_select_not);
        iv_select_zhifubao.setImageResource(R.drawable.icon_select_not);
    }

    public void setTotalPrice() {
        CartInfo.goodsPricetotal = Float.valueOf(MyUtils.decimalFormat
                .format(CartInfo.goodsPricetotal));
        BusinessInfo.expressFee = Float.valueOf(MyUtils.decimalFormat
                .format(BusinessInfo.expressFee));
        tv_totalMoney.setText("￥" + CartInfo.goodsPricetotal);
        tv_expressFee.setText("￥" + BusinessInfo.expressFee);
    }

    /**
     * 提交订单数据到后台
     *
     * @param uploadHost
     */
    private void postAllXUtils(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        PayStoreGoods allgoods = null;

        List<PayStoreGoods> goodsList = new ArrayList<PayStoreGoods>();
        Gson gson = new Gson();
        for (int i = 0; i < list.size(); i++) {
            allgoods = new PayStoreGoods();
            allgoods.setGoodsId(list.get(i).get("goodsID").toString());
            allgoods.setGoodsNums(Integer.valueOf(list.get(i).get("count")
                    .toString()));
            allgoods.setGoodsPrice(Float.valueOf(list.get(i).get("price")
                    .toString()));
            allgoods.setGoodsThums(list.get(i).get("goodsThums").toString());
            allgoods.setGoodsName(list.get(i).get("name").toString());
            goodsList.add(allgoods);
        }
        NameValuePair info = new BasicNameValuePair("Allgoods",
                gson.toJson(goodsList));
        params.addBodyParameter("totalMoney", CartInfo.goodsPricetotal + "");
        params.addBodyParameter("remarks", et_remark.getText().toString());
        params.addBodyParameter("isSelf", isSelf);
        params.addBodyParameter("deliverMoney", BusinessInfo.expressFee + "");
        params.addBodyParameter("userId", uid);
        params.addBodyParameter("goodsNum", CartInfo.goodsCounttotal + "");
        params.addBodyParameter("userName", userName);
        params.addBodyParameter("addressId", addressId + "");
        params.addBodyParameter("userAddress", userAddress);
        params.addBodyParameter("userPhone", userPhone);
        params.addBodyParameter("shopId", BusinessInfo.shopID);
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
                        if (result.equals("-2")) {
                            ToastUtil.showToast(PayStoreActivity.this, "商家休息中");
                            return;
                        }
                        if (result.equals("-1")) {
                            ToastUtil.showToast(PayStoreActivity.this, "库存不足");
                            return;
                        }
                        if (result.equals("-4")) {
                            ToastUtil.showToast(PayStoreActivity.this, "请先设置支付密码");
                            paypwSetting = true;
                            Intent intent = new Intent(PayStoreActivity.this,
                                    PayPWUpdateActivity.class);
                            startActivity(intent);
                            return;
                        }
                        if (result.equals("0")) {
                            ToastUtil.showToast(PayStoreActivity.this, "下单失败");
                            return;
                        }
                        orderId = result;
                        // 通知小卖部清空购物车信息
//						StoreFragment.isChange = true;
                        EventBus.getDefault().post(new MEventStoreCart(true));
                        if (!isDestroyed()) {
                            handler2.sendEmptyMessage(0);
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        ToastUtil.showToast(PayStoreActivity.this, "提交订单失败");
                        progress_update.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == ADDRESS_CODE) {
                ll_address.setVisibility(View.VISIBLE);
                userName = data.getExtras().getString("name");
                addressId = data.getExtras().getString("addressId");
                userPhone = data.getExtras().getString("phone");
                said = data.getExtras().getString("sid");
                province = data.getExtras().getString("province");
                city = data.getExtras().getString("city");
                area = data.getExtras().getString("area");
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
            } else if (requestCode == PAY_CODE) {
                progress_update.setVisibility(View.VISIBLE);
                new Thread() {
                    public void run() {
                        if (!orderId.equals("")) {
                            postOrderIdXUtils(MyConfig.URL_POST_GOODS_PAY_SUCCESS);
                        }
                    }

                    ;
                }.start();
            }
        }
        if (resultCode == RESULT_CANCELED) {
            if (requestCode == PAY_CODE) {
                Intent intent = new Intent(PayStoreActivity.this,
                        StoreOrderDetailActivity.class);
                intent.putExtra("oid", orderId);
                intent.putExtra("type", 1);
                intent.putExtra("index", 0);
                startActivity(intent);
                PayStoreActivity.this.finish();
            }
        }
    }

    /**
     * 订单提交后付款成功post订单号和付款状态
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
        params.addBodyParameter("needPay", money + "");
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
                            ToastUtil.showToast(PayStoreActivity.this, "支付成功");

                        } else {
                            ToastUtil.showToast(PayStoreActivity.this, "支付失败");
                        }
                        Intent intent = new Intent(PayStoreActivity.this,
                                StoreOrderDetailActivity.class);
                        intent.putExtra("oid", orderId);
                        intent.putExtra("type", 1);
                        intent.putExtra("index", 0);
                        startActivity(intent);
                        PayStoreActivity.this.finish();
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        ToastUtil.showToast(PayStoreActivity.this, "支付失败");
                        progress_update.setVisibility(View.GONE);
                        Intent intent = new Intent(PayStoreActivity.this,
                                StoreOrderDetailActivity.class);
                        intent.putExtra("oid", orderId);
                        intent.putExtra("type", 1);
                        intent.putExtra("index", 0);
                        startActivity(intent);
                        PayStoreActivity.this.finish();
                    }
                });
    }

    @Override
    protected void onRestart() {
        if (!paypwSetting && !goReceiver && !goRedPaper) {
            Intent intent = new Intent(PayStoreActivity.this,
                    StoreOrderDetailActivity.class);
            intent.putExtra("oid", orderId);
            intent.putExtra("type", 1);
            intent.putExtra("index", 0);
            startActivity(intent);
            PayStoreActivity.this.finish();
        }
        super.onRestart();
    }

}
