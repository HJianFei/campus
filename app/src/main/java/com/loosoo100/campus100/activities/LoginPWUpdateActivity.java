package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.loosoo100.campus100.anyevent.MEventBasicFinish;
import com.loosoo100.campus100.anyevent.MEventHome;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.MD5;
import com.loosoo100.campus100.utils.MyUtils;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;
import de.greenrobot.event.EventBus;

/**
 * 
 * @author yang 登陆密码修改activity
 */
public class LoginPWUpdateActivity extends Activity implements OnClickListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view
	@ViewInject(R.id.rl_back)
	private View rl_back;
	@ViewInject(R.id.tv_phone)
	private TextView tv_phone;
	@ViewInject(R.id.et_phoneCode)
	private EditText et_phoneCode; // 验证码
	@ViewInject(R.id.et_password)
	private EditText et_password; // 密码
	@ViewInject(R.id.et_passwordSure)
	private EditText et_passwordSure; // 确认密码
	@ViewInject(R.id.btn_sendSMS)
	private Button btn_sendSMS; // 发送验证码
	@ViewInject(R.id.btn_ok)
	private Button btn_ok; // 确定按钮
	private int i = 60;

	private String phone = "";
	private String message = "";
	private String code = ""; // 从后台获取的验证码
	private String password = "";
	private String passwordSure = "";

	private String result = ""; // 判断是否修改成功 1成功0失败
	private String org = "";

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (i > 0) {
				btn_sendSMS.setText(i + "s重新获取");
				btn_sendSMS.setClickable(false);
				btn_sendSMS.setBackgroundColor(getResources().getColor(
						R.color.gray_767779));
			} else {
				btn_sendSMS.setText("获取验证码");
				btn_sendSMS.setClickable(true);
				btn_sendSMS.setBackgroundColor(getResources().getColor(
						R.color.red_fd3c49));
			}
		};
	};

	private Handler handlerReg = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (result.equals("1")) {
				ToastUtil.showToast(LoginPWUpdateActivity.this,"密码修改成功");
				// 停止推送
				JPushInterface.setAlias(LoginPWUpdateActivity.this, "", null);
				// 清除本地用户
				UserInfoDB.clearUserInfo(LoginPWUpdateActivity.this);
				EventBus.getDefault().post(new MEventHome(true));
				EventBus.getDefault().post(new MEventBasicFinish(true));
				Intent intent = new Intent(LoginPWUpdateActivity.this,
						LoginActivity.class);
				startActivity(intent);
				finish();
			} else {
				ToastUtil.showToast(LoginPWUpdateActivity.this,"操作失败");
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_pw_update);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);
		
		org = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.ORG, "");

		initView();

	}

	private void initView() {
		rl_back.setOnClickListener(this);
		btn_sendSMS.setOnClickListener(this);
		btn_ok.setOnClickListener(this);

		phone = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.PHONE, "");
		tv_phone.setText(phone);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			this.finish();
			break;

		case R.id.btn_sendSMS:
			i = 60;
			btn_sendSMS.setText("60s重新获取");
			btn_sendSMS.setClickable(false);
			btn_sendSMS.setBackgroundColor(getResources().getColor(
					R.color.gray_767779));
			new Thread() {
				public void run() {
					getMessage(MyConfig.URL_POST_MESSAGE + "?phone=" + phone);
					while (i > 0) {
						SystemClock.sleep(1000);
						i--;
						handler.sendEmptyMessage(0);
					}
				};
			}.start();
			break;

		case R.id.btn_ok:
			message = et_phoneCode.getText().toString().trim();
			password = et_password.getText().toString().trim();
			passwordSure = et_passwordSure.getText().toString().trim();
			if (phone.equals("") || message.equals("") || password.equals("")
					|| passwordSure.equals("")) {
				ToastUtil.showToast(LoginPWUpdateActivity.this,"请将信息填写完整！");
				return;
			} else if (!password.equals(passwordSure)) {
				ToastUtil.showToast(LoginPWUpdateActivity.this,"两次密码输入不致！");
				return;
			} else if (!code.equals(MD5.md5("b0O" + message + "l1i"))) {
				ToastUtil.showToast(LoginPWUpdateActivity.this,"验证码输入错误");
			} else if (password.length() < 6 || password.length() > 16) {
				ToastUtil.showToast(LoginPWUpdateActivity.this,"请设置6~16位密码");
			} else {
				new Thread() {
					public void run() {
						postDataXutils(MyConfig.URL_POST_REGISTER);
					};
				}.start();
			}
			break;
		}
	}

	/**
	 * 获取验证码
	 * 
	 * @param url
	 */
	private void getMessage(String url) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpRequest.HttpMethod.GET, url,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.d("error",arg1.toString());
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						LogUtils.d("responseInfo",arg0.result);
						try {
							JSONObject jsonObject = new JSONObject(arg0.result);
							code = jsonObject.getString("code");
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}

	/**
	 * 提交数据
	 * 
	 * @param uploadHost
	 */
	private void postDataXutils(String uploadHost) {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("phone", phone);
		params.addBodyParameter("loginPwd", password);
		params.addBodyParameter("status", "1");
		if (org.equals("1")) {
			params.addBodyParameter("org", "1");
		}
		httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						LogUtils.d("responseInfo",responseInfo.result);
						try {
							JSONObject jsonObject = new JSONObject(
									responseInfo.result);
							result = jsonObject.getString("status");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (!isDestroyed()) {
							handlerReg.sendEmptyMessage(0);
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.d("error",arg1.toString());
					}
				});
	}

}
