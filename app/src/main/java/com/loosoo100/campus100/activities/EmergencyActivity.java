package com.loosoo100.campus100.activities;

import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.MyUtils;

/**
 * 
 * @author yang 江湖救急activity
 */
public class EmergencyActivity extends Activity implements OnClickListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view

	@ViewInject(R.id.rl_back)
	private RelativeLayout rl_back;
	@ViewInject(R.id.rl_question)
	private RelativeLayout rl_question;
	@ViewInject(R.id.progress)
	private RelativeLayout progress;// 加载动画

	@ViewInject(R.id.btn_apply)
	private Button btn_apply;

	@ViewInject(R.id.iv_moon)
	private ImageView iv_moon;

	@ViewInject(R.id.tv_totalMoney)
	private TextView tv_totalMoney; // 总信用额度

	@ViewInject(R.id.tv_surplusMoney)
	private TextView tv_surplusMoney; // 剩余信用额度

	private RotateAnimation rotate; // 旋转动画
	private AlphaAnimation alphaAnimation; // 透明度动画

	private String result = "";
	private String uid = "";

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (result.equals("0")) {
				tv_surplusMoney.setText("￥0");
				btn_apply.setText("已申请");
				btn_apply.setClickable(false);
				iv_moon.startAnimation(rotate);
				tv_totalMoney.startAnimation(alphaAnimation);
				tv_surplusMoney.startAnimation(alphaAnimation);
				progress.setVisibility(View.GONE);
			} else if (result.equals("1000")) {
				tv_surplusMoney.setText("￥1000");
				btn_apply.setText("我要申请");
				btn_apply.setClickable(true);
				iv_moon.startAnimation(rotate);
				tv_totalMoney.startAnimation(alphaAnimation);
				tv_surplusMoney.startAnimation(alphaAnimation);
				progress.setVisibility(View.GONE);
			} else if (result.equals("100")) {
				tv_surplusMoney.setText("￥1000");
				btn_apply.setText("审核中");
				btn_apply.setClickable(false);
				iv_moon.startAnimation(rotate);
				tv_totalMoney.startAnimation(alphaAnimation);
				tv_surplusMoney.startAnimation(alphaAnimation);
				progress.setVisibility(View.GONE);
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_emergency);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);

		progress.setVisibility(View.VISIBLE);
		initView();
		uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.USERID, "");

		new Thread() {
			public void run() {
				getData(MyConfig.URL_GET_CREDIT_LIMIT + uid);
			};
		}.start();
	}

	private void initView() {
		rl_back.setOnClickListener(this);
		rl_question.setOnClickListener(this);
		btn_apply.setOnClickListener(this);

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
			startActivityForResult(applyIntent, 1);
			break;

		}
	}

	@Override
	protected void onPause() {
		// 取消动画
		rotate.cancel();
		super.onPause();
	}

	private void getData(String url) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpRequest.HttpMethod.GET, url,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.d("error",arg1.toString());
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						LogUtils.d("responseInfo",responseInfo.result);
						try {
							JSONObject jsonObject = new JSONObject(responseInfo.result);
							result = jsonObject.getString("status");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						handler.sendEmptyMessage(0);
					}
				});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == 1) {
			btn_apply.setText("审核中");
			btn_apply.setClickable(false);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
