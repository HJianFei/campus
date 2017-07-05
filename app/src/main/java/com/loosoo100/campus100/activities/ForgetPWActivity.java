package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
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
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MD5;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author yang 忘记密码activity
 * 
 */
public class ForgetPWActivity extends Activity implements OnClickListener {
	@ViewInject(R.id.iv_login_bg)
	private ImageView iv_login_bg;
	@ViewInject(R.id.et_phone)
	private EditText et_phone;
	@ViewInject(R.id.et_message)
	private EditText et_message;
	@ViewInject(R.id.et_password)
	private EditText et_password;
	@ViewInject(R.id.et_password_sure)
	private EditText et_password_sure;
	@ViewInject(R.id.btn_message)
	private Button btn_message;
	@ViewInject(R.id.btn_ok)
	private Button btn_ok;
	@ViewInject(R.id.progress_update)
	private RelativeLayout progress_update; // 加载动画
	private int i = 60; // 时间
	private String phone1 = "";
	private String phone2 = "";
	private String loginPwd = "";
	private String loginPwdSure = "";

	private String message = "";
	private String code = "";// 获取验证码
	private String result = "0"; // 点击确定修改密码返回码
	private String isexists = ""; // 判断账号是否存在 1存在-1不存在
	
	private boolean boss;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (i > 0) {
				btn_message.setText(i + "s");
				btn_message.setClickable(false);
			} else {
				btn_message.setText("获取验证码");
				btn_message.setClickable(true);
			}
		};
	};

	private Handler handlerReg = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (result.equals("1")) {
				ToastUtil.showToast(ForgetPWActivity.this,"密码修改成功");
				ForgetPWActivity.this.finish();
			} else {
				ToastUtil.showToast(ForgetPWActivity.this,"操作失败");
			}
		};
	};

	private Handler handlerAcc = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (isexists.equals("-1")) {
				ToastUtil.showToast(ForgetPWActivity.this,"账号不存在");
			} else if (isexists.equals("1")) {
				i = 60;
				btn_message.setText("60s");
				btn_message.setClickable(false);
				new Thread(new Runnable() {
					@Override
					public void run() {
						getMessage(MyConfig.URL_POST_MESSAGE + "?phone="
								+ phone1);
						while (i > 0) {
							SystemClock.sleep(1000);
							i--;
							handler.sendEmptyMessage(0);
						}
					}
				}).start();
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgetpw);
		ViewUtils.inject(this);
		// 全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		boss=getIntent().getExtras().getBoolean("boss");
		iv_login_bg
				.setImageBitmap(GetData.getBitMap(this, R.drawable.login_bg));
		initView();

	}

	private void initView() {
		btn_message.setOnClickListener(this);
		btn_ok.setOnClickListener(this);

		et_phone.setNextFocusDownId(R.id.et_message);
		et_message.setNextFocusDownId(R.id.et_password);
		et_password.setNextFocusDownId(R.id.et_password_sure);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_message:
			phone1 = et_phone.getText().toString().trim();
			if (phone1.equals("")) {
				ToastUtil.showToast(ForgetPWActivity.this,"请输入手机号");
				return;
			}
			if (phone1.length() != 11) {
				ToastUtil.showToast(ForgetPWActivity.this,"请输入正确的手机号");
				return;
			}
			progress_update.setVisibility(View.VISIBLE);
			new Thread(new Runnable() {
				@Override
				public void run() {
					if (boss) {
						getAccount(MyConfig.URL_POST_ACCOUNT_ISEXISTS + phone1+"&org=1");
					}else {
						getAccount(MyConfig.URL_POST_ACCOUNT_ISEXISTS + phone1);
					}
				}
			}).start();
			break;

		case R.id.btn_ok:
			phone2 = et_phone.getText().toString().trim();
			message = et_message.getText().toString().trim();
			loginPwd = et_password.getText().toString().trim();
			loginPwdSure = et_password_sure.getText().toString().trim();

			if (phone2.equals("") || message.equals("") || loginPwd.equals("")
					|| loginPwdSure.equals("")) {
				ToastUtil.showToast(ForgetPWActivity.this,"请将信息填写完整");
				return;
			} else if (!loginPwd.equals(loginPwdSure)) {
				ToastUtil.showToast(ForgetPWActivity.this,"两次密码输入不致");
				return;
			} else if (!code.equals(MD5.md5("b0O" + message + "l1i"))
					|| !phone1.equals(phone2)) {
				ToastUtil.showToast(ForgetPWActivity.this,"验证码输入错误");
				return;
			} else if (loginPwd.length() < 6 || loginPwd.length() > 16) {
				ToastUtil.showToast(ForgetPWActivity.this,"请设置6~16位密码");
				return;
			} else {
				progress_update.setVisibility(View.VISIBLE);
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
		params.addBodyParameter("phone", phone2);
		params.addBodyParameter("loginPwd", loginPwd);
		params.addBodyParameter("status", "1");
		if (boss) {
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
						progress_update.setVisibility(View.GONE);
						if (!isDestroyed()) {
							handlerReg.sendEmptyMessage(0);
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.d("error",arg1.toString());
						progress_update.setVisibility(View.GONE);
					}
				});
	}

	/**
	 * 判断手机号是否存在
	 *
	 */
	private void getAccount(String url) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpRequest.HttpMethod.GET, url,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.d("error",arg1.toString());
						ToastUtil.showToast(ForgetPWActivity.this,"操作失败");
						progress_update.setVisibility(View.GONE);
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						LogUtils.d("responseInfo",arg0.result);
						progress_update.setVisibility(View.GONE);
						try {
							JSONObject jsonObject = new JSONObject(arg0.result);
							isexists = jsonObject.getString("status");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (!isDestroyed()) {
							handlerAcc.sendEmptyMessage(0);
						}
					}
				});
	}

}
