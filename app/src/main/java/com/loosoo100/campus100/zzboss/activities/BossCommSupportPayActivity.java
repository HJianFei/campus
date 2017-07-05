package com.loosoo100.campus100.zzboss.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.activities.PayPWUpdateActivity;
import com.loosoo100.campus100.anyevent.MEventCommActiPay;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.MyUtils;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * 
 * @author yang 活动打赏ta支付activity
 */
public class BossCommSupportPayActivity extends Activity implements
		OnClickListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view
	@ViewInject(R.id.rl_back)
	private View rl_back;
	@ViewInject(R.id.btn_ok)
	private Button btn_ok; // 确认付款

	@ViewInject(R.id.et_money)
	private EditText et_money; // 支付的金额
	@ViewInject(R.id.progress_update_blackbg)
	private RelativeLayout progress_update_blackbg; // 加载动画

	private float payMoney = 0;
	private String aid = ""; // 活动ID
	private String cid = ""; // 社团ID
	private String cuid = "";
	private String tip = "1";
	private String classifyid = "";

	private int decimal_digits = 2; // 小数位数

	private Handler handlerPay = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Intent intent = new Intent(BossCommSupportPayActivity.this,
					BossPayActivity.class);
			intent.putExtra("money", payMoney);
			intent.putExtra("aid", aid);
			intent.putExtra("cid", cid);
			intent.putExtra("tip", tip);
			startActivityForResult(intent, 1);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_boss_comm_reward_pay);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);

		cuid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.CUSERID, "");
		aid = getIntent().getExtras().getString("aid");
		cid = getIntent().getExtras().getString("cid");
		classifyid = getIntent().getExtras().getString("classifyid");
		tip = getIntent().getExtras().getString("tip");

		initView();

	}

	private void initView() {
		rl_back.setOnClickListener(this);
		btn_ok.setOnClickListener(this);

		/**
		 * 设置小数位数控制
		 */
		InputFilter lengthfilter = new InputFilter() {
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				// 删除等特殊字符，直接返回
				if ("".equals(source.toString())) {
					return null;
				}
				String dValue = dest.toString();
				String[] splitArray = dValue.split("\\.");
				if (splitArray.length > 1) {
					String dotValue = splitArray[1];
					int diff = dotValue.length() + 1 - decimal_digits;
					if (diff > 0) {
						return source.subSequence(start, end - diff);
					}
				}
				return null;
			}
		};

		// 控制输入框的小数位和长度,这里长度暂时设置为10
		et_money.setFilters(new InputFilter[] { lengthfilter,
				new InputFilter.LengthFilter(20) });
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			this.finish();
			break;

		case R.id.btn_ok:
			// 没有输入金额时不执行下一步
			String money = et_money.getText().toString().trim();
			if (money.equals("")) {
				ToastUtil.showToast(this,"请输入金额");
				return;
			}
			payMoney = Float.valueOf(money);
			if (payMoney <= 0) {
				ToastUtil.showToast(this,"金额不能为0");
				return;
			}
			progress_update_blackbg.setVisibility(View.VISIBLE);
			new Thread() {
				public void run() {
					isHavePayPW(MyConfig.URL_POST_ISSETTING_PAYPW);
				};
			}.start();

			break;
		}
	}

	/**
	 * 提交数据,提交支付金额
	 */
	private void postData(String uploadHost) {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("aid", aid);
		params.addBodyParameter("cid", cid);
		params.addBodyParameter("classifyid", classifyid);
		params.addBodyParameter("money", payMoney + "");
		params.addBodyParameter("uid", cuid);
		params.addBodyParameter("tip", tip);
		httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						LogUtils.d("responseInfo",responseInfo.result);
						progress_update_blackbg.setVisibility(View.GONE);
						String result = "";
						try {
							JSONObject jsonObject = new JSONObject(
									responseInfo.result);
							result = jsonObject.getString("status");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (result.equals("0")) {
							EventBus.getDefault().post(
									new MEventCommActiPay(true));
							BossCommSupportPayActivity.this.finish();
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.d("error",arg1.toString());
						ToastUtil.showToast(BossCommSupportPayActivity.this,"支付失败");
						progress_update_blackbg.setVisibility(View.GONE);
					}
				});
	}

	/**
	 * 判断是否设置过支付密码
	 * 
	 * @param uploadHost
	 */
	private void isHavePayPW(String uploadHost) {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("userId", cuid);
		params.addBodyParameter("type", "1");
		httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						LogUtils.d("responseInfo",responseInfo.result);
						progress_update_blackbg.setVisibility(View.GONE);
						String result = "";
						try {
							JSONObject jsonObject = new JSONObject(
									responseInfo.result);
							result = jsonObject.getString("status");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (result.equals("-4")) {
							ToastUtil.showToast(BossCommSupportPayActivity.this,"请先设置支付密码");
							Intent intent = new Intent(
									BossCommSupportPayActivity.this,
									PayPWUpdateActivity.class);
							startActivity(intent);
							return;
						}
						if (!isDestroyed()) {
							handlerPay.sendEmptyMessage(0);
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.d("error",arg1.toString());
						progress_update_blackbg.setVisibility(View.GONE);
					}
				});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == 1) {
			new Thread() {
				public void run() {
					postData(MyConfig.URL_POST_COMM_ACTI_SUPPORT_BOSS);
				};
			}.start();
		} else if (resultCode == RESULT_CANCELED && requestCode == 1) {
			progress_update_blackbg.setVisibility(View.GONE);
			EventBus.getDefault().post(new MEventCommActiPay(true));
			BossCommSupportPayActivity.this.finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
