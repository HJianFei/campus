package com.loosoo100.campus100.zzboss.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.loosoo100.campus100.adapters.PartnerJoinListAdapter;
import com.loosoo100.campus100.beans.CampusInfo;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.zzboss.beans.BossCompanyInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author yang 提现activity
 */
public class BossCashOutActivity extends Activity implements OnClickListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view
	@ViewInject(R.id.rl_back)
	private View rl_back;
	@ViewInject(R.id.btn_ok)
	private Button btn_ok; // 确定按钮
	@ViewInject(R.id.rl_history)
	private RelativeLayout rl_history; // 提现记录
	@ViewInject(R.id.tv_totalMoney)
	private TextView tv_totalMoney; // 可提现金额
	@ViewInject(R.id.et_money)
	private EditText et_money; // 提现金额
	@ViewInject(R.id.et_alipayAccount)
	private EditText et_alipayAccount; // 支付宝账号
	@ViewInject(R.id.et_trueName)
	private EditText et_trueName; // 真实姓名
	@ViewInject(R.id.et_cardName)
	private EditText et_cardName; // 姓名
	@ViewInject(R.id.et_cardNumber)
	private EditText et_cardNumber; // 卡号
	@ViewInject(R.id.et_cardPhone)
	private EditText et_cardPhone; // 银行预留手机号

	@ViewInject(R.id.ll_zhifubao)
	private LinearLayout ll_zhifubao; // 选中支付宝
	@ViewInject(R.id.ll_zhifubaoInfo)
	private LinearLayout ll_zhifubaoInfo; // 支付宝信息填写
	@ViewInject(R.id.iv_select_zhifubao)
	private ImageView iv_select_zhifubao; // 支付宝选中图标

	@ViewInject(R.id.ll_card)
	private LinearLayout ll_card; // 选中银行卡
	@ViewInject(R.id.ll_cardInfo)
	private LinearLayout ll_cardInfo; // 银行卡信息填写
	@ViewInject(R.id.iv_select_card)
	private ImageView iv_select_card; // 银行卡选中图标

	@ViewInject(R.id.progress_update_whitebg)
	private RelativeLayout progress_update_whitebg; // 加载动画
	@ViewInject(R.id.listview)
	private ListView listview;
	@ViewInject(R.id.rl_listview)
	private RelativeLayout rl_listview;
	@ViewInject(R.id.rl_black)
	private RelativeLayout rl_black;

	@ViewInject(R.id.btn_bank)
	private Button btn_bank; // 下拉选择银行开户行名称

	private double outMoney; // 提现金额
	private double totalMoney; // 提现金额
	private String alipayAccount = ""; // 支付宝账号
	private String trueName = ""; // 真实姓名
	private String cardUserName = ""; // 持卡人姓名
	private String bankName = ""; // 银行名称
	private String bankid = ""; // 银行ID
	private String cardNum = ""; // 卡号
	private String phone = ""; // 银行预留手机号
	private boolean isBank = false;

	private String cuid = "";

	private int decimal_digits = 2; // 小数位数

	// private List<String> list = new ArrayList<String>();
	// private ArrayAdapter<String> spinnerAdapter;

	private BossCompanyInfo companyInfo;
	private List<CampusInfo> bankList = new ArrayList<CampusInfo>();

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// 保存用户余额
			UserInfoDB.setUserInfo(BossCashOutActivity.this, UserInfoDB.MONEY,
					companyInfo.getMoney() + "");
			ToastUtil.showToast(BossCashOutActivity.this,"提现申请已提交成功");
			BossCashOutActivity.this.finish();
		};
	};

	private Handler handler2 = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (bankList != null && bankList.size() > 0) {
				initListView();
				rl_listview.setVisibility(View.VISIBLE);
			}
			progress_update_whitebg.setVisibility(View.GONE);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_boss_cashout);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);
		// 不自动弹出软键盘
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		cuid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.CUSERID, "");
		initView();

	}

	private void initView() {
		rl_back.setOnClickListener(this);
		btn_ok.setOnClickListener(this);
		rl_history.setOnClickListener(this);
		ll_zhifubao.setOnClickListener(this);
		ll_card.setOnClickListener(this);
		btn_bank.setOnClickListener(this);
		rl_black.setOnClickListener(this);

		totalMoney = Double.valueOf(getSharedPreferences(UserInfoDB.USERTABLE,
				MODE_PRIVATE).getString(UserInfoDB.MONEY, "0"));

		tv_totalMoney.setText("￥"
				+ getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
						.getString(UserInfoDB.MONEY, "0"));

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

		case R.id.rl_history:
			Intent intent = new Intent(this, BossCashOutHistoryActivity.class);
			startActivity(intent);
			break;

		// 选中支付宝账号时
		case R.id.ll_zhifubao:
			isBank = false;
			ll_zhifubaoInfo.setVisibility(View.VISIBLE);
			iv_select_zhifubao.setImageDrawable(getResources().getDrawable(
					R.drawable.icon_select));
			ll_cardInfo.setVisibility(View.GONE);
			iv_select_card.setImageDrawable(getResources().getDrawable(
					R.drawable.icon_select_not));
			break;

		// 选中银行卡账号时
		case R.id.ll_card:
			isBank = true;
			ll_cardInfo.setVisibility(View.VISIBLE);
			iv_select_card.setImageDrawable(getResources().getDrawable(
					R.drawable.icon_select));
			ll_zhifubaoInfo.setVisibility(View.GONE);
			iv_select_zhifubao.setImageDrawable(getResources().getDrawable(
					R.drawable.icon_select_not));
			break;

		case R.id.btn_ok:
			if (!et_money.getText().toString().trim().equals("")) {
				outMoney = Double.valueOf(et_money.getText().toString().trim());
			}
			alipayAccount = et_alipayAccount.getText().toString().trim();
			trueName = et_trueName.getText().toString().trim();
			cardUserName = et_cardName.getText().toString().trim();
			cardNum = et_cardNumber.getText().toString().trim();
			phone = et_cardPhone.getText().toString().trim();
			// 如果是提现到支付宝
			if (!isBank) {
				if (outMoney <= 0 || alipayAccount.equals("")
						|| trueName.equals("")) {
					ToastUtil.showToast(BossCashOutActivity.this,"请将信息填写正确");
					return;
				}
				if (outMoney > totalMoney) {
					ToastUtil.showToast(BossCashOutActivity.this,"金额输入错误");
					return;
				}
				progress_update_whitebg.setVisibility(View.VISIBLE);
				new Thread() {
					public void run() {
						postCashoutAlipay(MyConfig.URL_POST_CASHOUT_ALIPAY);
					};
				}.start();
			}
			// 如果提现到银行卡
			else {
				if (outMoney <= 0 || cardUserName.equals("")
						|| cardNum.equals("") || phone.equals("")
						|| bankName.equals("")) {
					ToastUtil.showToast(BossCashOutActivity.this,"请将信息填写正确");
					return;
				}
				if (outMoney > totalMoney) {
					ToastUtil.showToast(BossCashOutActivity.this,"金额输入错误");
					return;
				}
				progress_update_whitebg.setVisibility(View.VISIBLE);
				new Thread() {
					public void run() {
						postCashoutBank(MyConfig.URL_POST_CASHOUT_BANK);
					};
				}.start();
			}
			break;

		case R.id.btn_bank:
			progress_update_whitebg.setVisibility(View.VISIBLE);
			new Thread() {
				public void run() {
					bankList = GetData.getBankInfos(MyConfig.URL_JSON_BANK);
					if (!isDestroyed()) {
						handler2.sendEmptyMessage(0);
					}
				}
			}.start();
			break;

		case R.id.rl_black:
			rl_listview.setVisibility(View.GONE);
			break;
		}
	}

	private void initListView() {
		listview.setAdapter(new PartnerJoinListAdapter(this, bankList));
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				rl_listview.setVisibility(View.GONE);
				bankName = bankList.get(position).getCampusName();
				bankid = bankList.get(position).getCampusID();
				btn_bank.setText(bankName);
			}
		});
	}

	/**
	 * 提交数据,提现至支付宝
	 * 
	 * @param uploadHost
	 */
	private void postCashoutAlipay(String uploadHost) {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("userid", cuid);
		params.addBodyParameter("type", "1");
		params.addBodyParameter("txalipay", alipayAccount);
		params.addBodyParameter("txname", trueName);
		params.addBodyParameter("money", outMoney + "");
		httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						LogUtils.d("responseInfo",responseInfo.result);
						progress_update_whitebg.setVisibility(View.GONE);
						String result = "";
						try {
							JSONObject jsonObject = new JSONObject(
									responseInfo.result);
							result = jsonObject.getString("status");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (result.equals("1")) {
							new Thread() {
								public void run() {
									companyInfo = GetData
											.getBossUserInfo(MyConfig.URL_JSON_COMPANYINFO
													+ cuid);
									if (!isDestroyed()) {
										handler.sendEmptyMessage(0);
									}
								};
							}.start();
						} else if (result.equals("-1")) {
							ToastUtil.showToast(BossCashOutActivity.this,"金额输入错误,请刷新后重新输入");
						} else {
							ToastUtil.showToast(BossCashOutActivity.this,"操作失败");
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.d("error",arg1.toString());
						progress_update_whitebg.setVisibility(View.GONE);
						ToastUtil.showToast(BossCashOutActivity.this,"操作失败");
					}
				});
	}

	/**
	 * 提交数据,提现至银行卡
	 * 
	 * @param uploadHost
	 */
	private void postCashoutBank(String uploadHost) {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("userid", cuid);
		params.addBodyParameter("type", "1");
		params.addBodyParameter("cardname", cardUserName);
		params.addBodyParameter("bankid", bankid);
		params.addBodyParameter("bankNum", cardNum);
		params.addBodyParameter("money", outMoney + "");
		params.addBodyParameter("bankPhone", phone);
		httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						LogUtils.d("responseInfo",responseInfo.result);
						progress_update_whitebg.setVisibility(View.GONE);
						String result = "";
						try {
							JSONObject jsonObject = new JSONObject(
									responseInfo.result);
							result = jsonObject.getString("status");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (result.equals("1")) {
							new Thread() {
								public void run() {
									companyInfo = GetData
											.getBossUserInfo(MyConfig.URL_JSON_COMPANYINFO
													+ cuid);
									if (!isDestroyed()) {
										handler.sendEmptyMessage(0);
									}
								};
							}.start();
						} else if (result.equals("-1")) {
							ToastUtil.showToast(BossCashOutActivity.this,"金额输入错误,请刷新后重新输入");
						} else {
							ToastUtil.showToast(BossCashOutActivity.this,"操作失败");
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.d("error",arg1.toString());
						progress_update_whitebg.setVisibility(View.GONE);
						ToastUtil.showToast(BossCashOutActivity.this,"操作失败");
					}
				});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (rl_listview.getVisibility() == View.VISIBLE) {
				rl_listview.setVisibility(View.GONE);
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
