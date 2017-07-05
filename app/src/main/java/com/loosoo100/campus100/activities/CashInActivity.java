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
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.wxapi.WXPayUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author yang 充值activity
 * 
 */
public class CashInActivity extends Activity implements OnClickListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view
	@ViewInject(R.id.rl_back)
	private View rl_back;
	@ViewInject(R.id.btn_ok)
	private Button btn_ok; // 确认充值

	@ViewInject(R.id.ll_weixin)
	private LinearLayout ll_weixin; // 微信支付

	@ViewInject(R.id.ll_zhifubao)
	private LinearLayout ll_zhifubao; // 支付宝支付

	@ViewInject(R.id.iv_select_weixin)
	private ImageView iv_select_weixin; // 微信支付选择图标

	@ViewInject(R.id.iv_select_zhifubao)
	private ImageView iv_select_zhifubao; // 支付宝支付选择图标

	@ViewInject(R.id.et_money)
	private EditText et_money; // 充值金额

	private int money;
	private String seedOid = ""; // 订单ID

	private String uid = "";
	private boolean isPaying = false;

	private int payType = 0; // 支付方式 0微信支付 1支付宝支付
	private AlipayUtil alipayUtil;

	private Handler handlerPay = new Handler() {

		public void handleMessage(android.os.Message msg) {
			if (payType == 0) {
				WXPayUtil wp = new WXPayUtil(CashInActivity.this, "cash"
						+ seedOid, uid, "loosoo100", "", money);
				wp.sendPay();
			} else if (payType == 1) {
				alipayUtil = new AlipayUtil(CashInActivity.this, "loosoo100",
						"校园100", money + "", uid, seedOid, "cash");
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
		setContentView(R.layout.activity_cashin);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);
		uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.USERID, "");

		initView();

	}

	private void initView() {
		rl_back.setOnClickListener(this);
		btn_ok.setOnClickListener(this);
		ll_weixin.setOnClickListener(this);
		ll_zhifubao.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			this.finish();
			break;

		case R.id.rl_history:
			Intent intent = new Intent(this, CashOutHistoryActivity.class);
			startActivity(intent);
			break;

		case R.id.btn_ok:
			if (et_money.getText().toString().trim().equals("")) {
				ToastUtil.showToast(this,"请输入金额");
				return;
			}
			money = Integer.valueOf(et_money.getText().toString().trim());
			if (money <= 0) {
				ToastUtil.showToast(this,"输入金额错误");
				return;
			}
			new Thread() {
				@Override
				public void run() {
					getOrderId(MyConfig.URL_POST_CASHIN);
				}
			}.start();
			break;

		// 微信支付
		case R.id.ll_weixin:
			payType = 0;
			resetPaySelect();
			iv_select_weixin.setImageResource(R.drawable.icon_select);
			break;

		// 支付宝支付
		case R.id.ll_zhifubao:
			payType = 1;
			resetPaySelect();
			iv_select_zhifubao.setImageResource(R.drawable.icon_select);
			break;

		}
	}

	/**
	 * 还原支付方式选中图标
	 */
	public void resetPaySelect() {
		iv_select_weixin.setImageResource(R.drawable.icon_select_not);
		iv_select_zhifubao.setImageResource(R.drawable.icon_select_not);
	}

	/**
	 * 获取订单ID
	 * 
	 * @param uploadHost
	 */
	private void getOrderId(String uploadHost) {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("money", money + "");
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
							ToastUtil.showToast(CashInActivity.this,"提交失败");
						} else {
							handlerPay.sendEmptyMessage(0);
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.d("error",arg1.toString());
						ToastUtil.showToast(CashInActivity.this,"提交失败");
					}
				});
	}

}
