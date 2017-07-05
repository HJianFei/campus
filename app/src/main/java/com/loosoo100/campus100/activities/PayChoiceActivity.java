package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.alipayapi.AlipayUtil;
import com.loosoo100.campus100.anyevent.MEventPayChoiceIsSelect;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.wxapi.WXPayUtil;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * 
 * @author yang 支付选择activity
 */
public class PayChoiceActivity extends Activity implements OnClickListener {
	/**
	 * 弹出的支付选择界面
	 */
	@ViewInject(R.id.rl_black)
	private RelativeLayout rl_black; // 暗部分布局

	@ViewInject(R.id.ll_wallet)
	private LinearLayout ll_wallet; // 钱包支付

	@ViewInject(R.id.ll_weixin)
	private LinearLayout ll_weixin; // 微信支付

	@ViewInject(R.id.ll_zhifubao)
	private LinearLayout ll_zhifubao; // 支付宝支付

	@ViewInject(R.id.btn_recharge)
	private Button btn_recharge; // 充值按钮

	@ViewInject(R.id.rl_back_pay)
	private RelativeLayout rl_back_pay; // 返回按钮
	@ViewInject(R.id.rl_close_pay)
	private RelativeLayout rl_close_pay; // 关闭按钮

	@ViewInject(R.id.tv_moneyNotEnough)
	private TextView tv_moneyNotEnough; // 余额不足文本

	@ViewInject(R.id.root_payChoice)
	private LinearLayout root_payChoice; // 整个选择支付弹出框界面

	private float money;
	private float totalMoney;
	private String oid = "";
	private String cid = "";
	private String uid = "";
	private String type = "";

	private String seedOid = "";

	private boolean isPaying = false;

	private AlipayUtil alipayUtil;
	private int payWay; // 0代表微信支付 1代表支付宝支付

	private Handler handlerTogetherPay = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (payWay == 0) {
				WXPayUtil wp = new WXPayUtil(PayChoiceActivity.this, type
						+ seedOid, uid, "loosoo100", "", money);
				wp.sendPay();
			} else if (payWay == 1) {
				alipayUtil = new AlipayUtil(PayChoiceActivity.this,
						"loosoo100", "校园100", money + "", uid, seedOid, type);
				alipayUtil.pay();
				isPaying = true;
				new Thread() {
					public void run() {
						while (isPaying) {
							if (alipayUtil.isPay == 1 || alipayUtil.isPay == -1) {
								isPaying = false;
								finish();
							}
						}
					};
				}.start();
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_choice);
		ViewUtils.inject(this);

		money = getIntent().getExtras().getFloat("money");
		oid = getIntent().getExtras().getString("orderId");
		cid = getIntent().getExtras().getString("cid");
		type = getIntent().getExtras().getString("type");

		totalMoney = Float.valueOf(getSharedPreferences(UserInfoDB.USERTABLE,
				MODE_PRIVATE).getString(UserInfoDB.MONEY, "0"));
		uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.USERID, "");

		initView();

	}

	private void initView() {
		ll_wallet.setOnClickListener(this);
		ll_weixin.setOnClickListener(this);
		ll_zhifubao.setOnClickListener(this);
		rl_back_pay.setOnClickListener(this);
		rl_close_pay.setOnClickListener(this);
		btn_recharge.setOnClickListener(this);
		rl_black.setOnClickListener(this);

		if (money > totalMoney) {
			tv_moneyNotEnough.setVisibility(View.VISIBLE);
		} else {
			tv_moneyNotEnough.setVisibility(View.GONE);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 钱包支付
		case R.id.ll_wallet:
			seedOid = "0";
//			Intent payIntent1 = new Intent(PayChoiceActivity.this,
//					PayActivity.class);
//			payIntent1.putExtra("money", money);
//			payIntent1.putExtra("orderId", oid);
//			payIntent1.putExtra("type", type);
//			startActivity(payIntent1);
			finish();
			break;

		// 微信支付
		case R.id.ll_weixin:
			EventBus.getDefault().post(new MEventPayChoiceIsSelect(true));
			payWay = 0;
			seedOid = "0";
			if (type.equals("gcou")) {
				new Thread() {
					@Override
					public void run() {
						getTogetherOrderId(MyConfig.URL_POST_GIFT_ORDER_TOGETHER_PAY_OTHER);
					}
				}.start();
			} else if (type.equals("acti")) {
				new Thread() {
					@Override
					public void run() {
						getActiOrderId(MyConfig.URL_POST_COMMUNITY_ACTIVITY_SUPPORT_OTHER);
					}
				}.start();
			} else {
				WXPayUtil wp = new WXPayUtil(this, type + oid, uid,
						"loosoo100", "", money);
				wp.sendPay();
			}
			break;

		// 支付宝支付
		case R.id.ll_zhifubao:
			EventBus.getDefault().post(new MEventPayChoiceIsSelect(true));
			payWay = 1;
			seedOid = "0";
			if (type.equals("gcou")) {
				new Thread() {
					@Override
					public void run() {
						getTogetherOrderId(MyConfig.URL_POST_GIFT_ORDER_TOGETHER_PAY_OTHER);
					}
				}.start();
			} else if (type.equals("acti")) {
				new Thread() {
					@Override
					public void run() {
						getActiOrderId(MyConfig.URL_POST_COMMUNITY_ACTIVITY_SUPPORT_OTHER);
					}
				}.start();
			} else {
				alipayUtil = new AlipayUtil(this, "loosoo100", "校园100", money
						+ "", uid, oid, type);
				alipayUtil.pay();
				isPaying = true;
				new Thread() {
					public void run() {
						while (isPaying) {
							if (alipayUtil.isPay == 1 || alipayUtil.isPay == -1) {
								isPaying = false;
								finish();
							}
						}
					};
				}.start();
			}

			break;

		// 充值按钮
		case R.id.btn_recharge:
			Intent rechargeIntent = new Intent(PayChoiceActivity.this,
					CashInActivity.class);
			startActivity(rechargeIntent);
			break;

		// 支付返回按钮
		case R.id.rl_back_pay:
//			Intent payIntent2 = new Intent(PayChoiceActivity.this,
//					PayActivity.class);
//			payIntent2.putExtra("money", money);
//			payIntent2.putExtra("orderId", oid);
//			payIntent2.putExtra("type", type);
//			startActivity(payIntent2);
			finish();
			break;

		// 支付关闭按钮
		case R.id.rl_close_pay:
			// Intent payIntent2 = new Intent(PayChoiceActivity.this,
			// PayActivity.class);
			// payIntent2.putExtra("money", money);
			// payIntent2.putExtra("orderId", oid);
			// payIntent2.putExtra("type", type);
			// startActivity(payIntent2);
			finish();
			break;
		}
	}

	/**
	 * 提交数据,获取凑一凑支付子订单ID
	 */
	private void getTogetherOrderId(String uploadHost) {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("id", oid);
		params.addBodyParameter("money", money + "");
		params.addBodyParameter("uid", uid);
		httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						LogUtils.d("responseInfo",responseInfo.result);
						try {
							JSONObject jsonObject = new JSONObject(
									responseInfo.result);
							seedOid = jsonObject.getString("status");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (seedOid.equals("0")) {
							ToastUtil.showToast(PayChoiceActivity.this,"提交失败");
						} else {
							handlerTogetherPay.sendEmptyMessage(0);
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.d("error",arg1.toString());
						ToastUtil.showToast(PayChoiceActivity.this,"提交失败");
					}
				});
	}

	/**
	 * 提交数据,获取活动支付子订单ID
	 */
	private void getActiOrderId(String uploadHost) {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("aid", oid);
		params.addBodyParameter("cid", cid);
		params.addBodyParameter("money", money + "");
		params.addBodyParameter("uid", uid);
		httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						LogUtils.d("responseInfo",responseInfo.result);
						try {
							JSONObject jsonObject = new JSONObject(
									responseInfo.result);
							seedOid = jsonObject.getString("status");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (seedOid.equals("0")) {
							ToastUtil.showToast(PayChoiceActivity.this,"提交失败");
						} else {
							handlerTogetherPay.sendEmptyMessage(0);
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.d("error",arg1.toString());
						ToastUtil.showToast(PayChoiceActivity.this,"提交失败");
					}
				});
	}

	@Override
	protected void onRestart() {
		finish();
		super.onRestart();
	}

}
