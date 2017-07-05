package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
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
import com.loosoo100.campus100.beans.MyCreditInfo;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;

/**
 * 
 * @author yang 我的信用activity
 */
public class MyCreditActivity extends Activity implements OnClickListener {

	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view

	@ViewInject(R.id.rl_back)
	private RelativeLayout rl_back;
	@ViewInject(R.id.rl_question)
	private RelativeLayout rl_question;

	@ViewInject(R.id.btn_apply)
	private Button btn_apply; // 我要申请

	@ViewInject(R.id.btn_repayment)
	private Button btn_repayment; // 还款按钮

	@ViewInject(R.id.ll_history_borrow)
	private LinearLayout ll_history_borrow; // 借款记录

	@ViewInject(R.id.ll_repayment)
	private LinearLayout ll_repayment; // 下月还款

	@ViewInject(R.id.tv_nextMonth)
	private TextView tv_nextMonth; // 下月还款金额

	@ViewInject(R.id.tv_currentMonth)
	private TextView tv_currentMonth; // 本月还款金额

	@ViewInject(R.id.iv_moon)
	private ImageView iv_moon;

	@ViewInject(R.id.tv_totalMoney)
	private TextView tv_totalMoney; // 总信用额度

	@ViewInject(R.id.tv_surplusMoney)
	private TextView tv_surplusMoney; // 剩余信用额度

	@ViewInject(R.id.progress)
	private RelativeLayout progress;// 加载动画
	@ViewInject(R.id.progress_update_blackbg)
	private RelativeLayout progress_update_blackbg;// 加载动画

	private RotateAnimation rotate; // 旋转动画

	private AlphaAnimation alphaAnimation; // 透明度动画

	private MyCreditInfo myCreditInfo = null;

	private String uid = "";

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (myCreditInfo.getRepaymentThis() > 0) {
				btn_repayment.setVisibility(View.VISIBLE);
			} else {
				btn_repayment.setVisibility(View.GONE);
			}
			if (myCreditInfo.getMoney() == 0) {
				tv_surplusMoney.setText("￥0");
				btn_apply.setText("已申请");
				btn_apply.setClickable(false);
				iv_moon.startAnimation(rotate);
				tv_totalMoney.startAnimation(alphaAnimation);
				tv_surplusMoney.startAnimation(alphaAnimation);
				tv_currentMonth.setText("￥" + myCreditInfo.getRepaymentThis());
				tv_nextMonth.setText("￥" + myCreditInfo.getRepaymentNext());
			} else if (myCreditInfo.getMoney() == 1000) {
				tv_surplusMoney.setText("￥1000");
				btn_apply.setText("我要申请");
				btn_apply.setClickable(true);
				iv_moon.startAnimation(rotate);
				tv_totalMoney.startAnimation(alphaAnimation);
				tv_surplusMoney.startAnimation(alphaAnimation);
				tv_currentMonth.setText("￥" + myCreditInfo.getRepaymentThis());
				tv_nextMonth.setText("￥" + myCreditInfo.getRepaymentNext());
			} else if (myCreditInfo.getMoney() == 100) {
				tv_surplusMoney.setText("￥1000");
				btn_apply.setText("审核中");
				btn_apply.setClickable(false);
				iv_moon.startAnimation(rotate);
				tv_totalMoney.startAnimation(alphaAnimation);
				tv_surplusMoney.startAnimation(alphaAnimation);
				tv_currentMonth.setText("￥" + myCreditInfo.getRepaymentThis());
				tv_nextMonth.setText("￥" + myCreditInfo.getRepaymentNext());
			}
			progress.setVisibility(View.GONE);
			progress_update_blackbg.setVisibility(View.GONE);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mycredit);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);
		uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.USERID, "");

		progress.setVisibility(View.VISIBLE);
		initView();
		new Thread() {
			public void run() {
				myCreditInfo = GetData.getMyCreditInfo(MyConfig.URL_JSON_CREDIT
						+ uid);
				if (myCreditInfo != null && !isDestroyed()) {
					handler.sendEmptyMessage(0);
				}
			};
		}.start();

	}

	private void initView() {
		rl_back.setOnClickListener(this);
		rl_question.setOnClickListener(this);
		btn_apply.setOnClickListener(this);
		ll_history_borrow.setOnClickListener(this);
		ll_repayment.setOnClickListener(this);
		btn_repayment.setOnClickListener(this);

		rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		// 设置动画时间
		rotate.setDuration(3000);
		// 设置动画结束后控件停留在结束的状态
		rotate.setFillAfter(true);

		alphaAnimation = new AlphaAnimation(0, 1);
		alphaAnimation.setDuration(3000);
		alphaAnimation.setFillAfter(false);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			this.finish();
			break;

		case R.id.rl_question:
			Intent intent = new Intent(this, CreditQuestionActivity.class);
			startActivity(intent);
			break;

		case R.id.btn_apply:
			Intent applyIntent = new Intent(this, MyCreditApplyActivity.class);
			startActivityForResult(applyIntent, 3);
			break;

		// 还款按钮
		case R.id.btn_repayment:
			progress_update_blackbg.setVisibility(View.VISIBLE);
			Intent repayIntent = new Intent(this, PayActivity.class);
			repayIntent.putExtra("money",
					myCreditInfo.getRepaymentThis() * 1.0f);
			repayIntent.putExtra("orderId", myCreditInfo.getRepaymentThis()
					+ "");
			repayIntent.putExtra("cid", "0");
			repayIntent.putExtra("type", "disa");
			startActivityForResult(repayIntent, 1);
			break;

		// 借贷记录
		case R.id.ll_history_borrow:
			Intent historyIntent = new Intent(this, BorrowHistoryActivity.class);
			startActivity(historyIntent);
			break;

		// 还款账单
		case R.id.ll_repayment:
			Intent repaymentIntent = new Intent(this,
					RepaymentBillActivity.class);
			startActivity(repaymentIntent);
			break;
		}
	}

	@Override
	protected void onPause() {
		// 取消动画
		rotate.cancel();
		super.onPause();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == 1) {
			progress_update_blackbg.setVisibility(View.VISIBLE);
			new Thread() {
				public void run() {
					postDataXutils(MyConfig.URL_POST_CREDIT_REPAYMENT);
					myCreditInfo = GetData
							.getMyCreditInfo(MyConfig.URL_JSON_CREDIT + uid);
					if (myCreditInfo != null && !isDestroyed()) {
						handler.sendEmptyMessage(0);
					}
				};
			}.start();
		} else if (resultCode == RESULT_OK && requestCode == 3) {
			btn_apply.setText("审核中");
			btn_apply.setClickable(false);
		} else {
			new Thread() {
				public void run() {
					myCreditInfo = GetData
							.getMyCreditInfo(MyConfig.URL_JSON_CREDIT + uid);
					if (myCreditInfo != null && !isDestroyed()) {
						handler.sendEmptyMessage(0);
					}
				};
			}.start();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void postDataXutils(String uploadHost) {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("uid", uid);
		params.addBodyParameter("money", myCreditInfo.getRepaymentThis() + "");
		httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						LogUtils.d("responseInfo",responseInfo.result);
						btn_repayment.setVisibility(View.GONE);
						tv_currentMonth.setText("￥0");
						progress_update_blackbg.setVisibility(View.GONE);
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						ToastUtil.showToast(MyCreditActivity.this,"提交失败");
						LogUtils.d("error",arg1.toString());
						progress_update_blackbg.setVisibility(View.GONE);
					}
				});
	}

}
