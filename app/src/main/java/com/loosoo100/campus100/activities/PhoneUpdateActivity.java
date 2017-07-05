//package com.loosoo100.campus100.activities;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.SystemClock;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.lidroid.xutils.HttpUtils;
//import com.lidroid.xutils.ViewUtils;
//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.RequestParams;
//import com.lidroid.xutils.http.ResponseInfo;
//import com.lidroid.xutils.http.callback.RequestCallBack;
//import com.lidroid.xutils.http.client.HttpRequest;
//import com.lidroid.xutils.view.annotation.ViewInject;
//import com.loosoo100.campus100.R;
//import com.loosoo100.campus100.config.MyConfig;
//import com.loosoo100.campus100.db.UserInfoDB;
//import com.loosoo100.campus100.utils.MD5;
//import com.loosoo100.campus100.utils.MyUtils;
//
///**
// * 暂时封闭不让改
// * @author yang 手机绑定修改activity
// */
//public class PhoneUpdateActivity extends Activity implements OnClickListener {
//	@ViewInject(R.id.iv_empty)
//	private ImageView iv_empty; // 空view
//	@ViewInject(R.id.rl_back)
//	private View rl_back;
//	@ViewInject(R.id.tv_phone)
//	private TextView tv_phone; // 原手机号
//	@ViewInject(R.id.et_newPhone)
//	private EditText et_newPhone;
//	@ViewInject(R.id.et_phoneCode_old)
//	private EditText et_phoneCode_old; // 验证码旧
//	@ViewInject(R.id.et_phoneCode_new)
//	private EditText et_phoneCode_new; // 验证码新
//	@ViewInject(R.id.btn_sendSMS_old)
//	private Button btn_sendSMS_old; // 发送验证码旧
//	@ViewInject(R.id.btn_sendSMS_new)
//	private Button btn_sendSMS_new; // 发送验证码新
//	@ViewInject(R.id.btn_save)
//	private Button btn_save; // 保存按钮
//
//	private String oldPhone = ""; // 旧手机号
//	private String newPhone = ""; // 新手机号
//	private String newPhone2 = ""; // 新手机号,防止新手机号跟接收验证码的手机号不一致
//	private String oldCode = ""; // 旧手机的验证码
//	private String newCode = ""; // 新手机的验证码
//	private String oldMessage = ""; // 旧手机的验证码
//	private String newMessage = ""; // 新手机的验证码
//
//	private String uid = ""; // 用户ID
//	private String cuid = ""; // 公司ID
//
//	private int i = 60;
//	private int j = 60;
//
//	private String result = ""; // 判断是否修改成功 1成功0失败
//
//	private String org = "";
//
//	private Handler handlerSMS01 = new Handler() {
//		public void handleMessage(android.os.Message msg) {
//			if (i > 0) {
//				btn_sendSMS_old.setText(i + "s重新获取");
//				btn_sendSMS_old.setClickable(false);
//				btn_sendSMS_old.setBackgroundColor(getResources().getColor(
//						R.color.gray_767779));
//			} else {
//				btn_sendSMS_old.setText("获取验证码");
//				btn_sendSMS_old.setClickable(true);
//				btn_sendSMS_old.setBackgroundColor(getResources().getColor(
//						R.color.red_fd3c49));
//			}
//		};
//	};
//
//	private Handler handlerSMS02 = new Handler() {
//		public void handleMessage(android.os.Message msg) {
//			if (j > 0) {
//				btn_sendSMS_new.setText(j + "s重新获取");
//				btn_sendSMS_new.setClickable(false);
//				btn_sendSMS_new.setBackgroundColor(getResources().getColor(
//						R.color.gray_767779));
//			} else {
//				btn_sendSMS_new.setText("获取验证码");
//				btn_sendSMS_new.setClickable(true);
//				btn_sendSMS_new.setBackgroundColor(getResources().getColor(
//						R.color.red_fd3c49));
//			}
//		};
//	};
//
//	/*
//	 * 修改密码是否成功
//	 */
//	private Handler handlerReg = new Handler() {
//		public void handleMessage(android.os.Message msg) {
//			if (result.equals("1")) {
//				Toast.makeText(PhoneUpdateActivity.this, "手机号修改成功", 0).show();
//				UserInfoDB.setUserInfo(PhoneUpdateActivity.this,
//						UserInfoDB.PHONE, newPhone);
//				PhoneUpdateActivity.this.finish();
//			} else if (result.equals("2")) {
//				Toast.makeText(PhoneUpdateActivity.this, "该手机号已经被注册", 0).show();
//			} else {
//				Toast.makeText(PhoneUpdateActivity.this, "操作失败", 0).show();
//			}
//		};
//	};
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_phone_update);
//		ViewUtils.inject(this);
//
//		MyUtils.setStatusBarHeight(this, iv_empty);
//
//		uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
//				.getString(UserInfoDB.USERID, "");
//		cuid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
//				.getString(UserInfoDB.CUSERID, "");
//		org = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
//				.getString(UserInfoDB.ORG, "");
//		initView();
//
//	}
//
//	private void initView() {
//		rl_back.setOnClickListener(this);
//		btn_sendSMS_old.setOnClickListener(this);
//		btn_sendSMS_new.setOnClickListener(this);
//		btn_save.setOnClickListener(this);
//
//		oldPhone = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
//				.getString(UserInfoDB.PHONE, "");
//		tv_phone.setText(oldPhone);
//
//	}
//
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.rl_back:
//			this.finish();
//			break;
//
//		case R.id.btn_sendSMS_old:
//			i = 60;
//			btn_sendSMS_old.setText("60s重新获取");
//			btn_sendSMS_old.setClickable(false);
//			btn_sendSMS_old.setBackgroundColor(getResources().getColor(
//					R.color.gray_767779));
//			new Thread() {
//				public void run() {
//					getMessageOld(MyConfig.URL_POST_MESSAGE + "?phone="
//							+ oldPhone);
//					while (i > 0) {
//						SystemClock.sleep(1000);
//						i--;
//						handlerSMS01.sendEmptyMessage(0);
//					}
//				};
//			}.start();
//			break;
//
//		case R.id.btn_sendSMS_new:
//			newPhone = et_newPhone.getText().toString().trim();
//			if (newPhone.equals("")) {
//				Toast.makeText(this, "请输入新手机号码", 0).show();
//				return;
//			} else if (newPhone.length() != 11) {
//				Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
//				return;
//			}
//			j = 60;
//			btn_sendSMS_new.setText("60s重新获取");
//			btn_sendSMS_new.setClickable(false);
//			btn_sendSMS_new.setBackgroundColor(getResources().getColor(
//					R.color.gray_767779));
//			new Thread() {
//				public void run() {
//					getMessageNew(MyConfig.URL_POST_MESSAGE + "?phone="
//							+ newPhone);
//					while (j > 0) {
//						SystemClock.sleep(1000);
//						j--;
//						handlerSMS02.sendEmptyMessage(0);
//					}
//				};
//			}.start();
//			break;
//
//		case R.id.btn_save:
//			oldMessage = et_phoneCode_old.getText().toString().trim();
//			newMessage = et_phoneCode_new.getText().toString().trim();
//			newPhone2 = et_newPhone.getText().toString().trim();
//			if (oldMessage.equals("") || newMessage.equals("")
//					|| newPhone2.equals("")) {
//				Toast.makeText(this, "请将信息填写完整", 0).show();
//				return;
//			} else if (!oldCode.equals(MD5.md5("b0O" + oldMessage + "l1i"))
//					|| !newCode.equals((MD5.md5("b0O" + newMessage + "l1i")))
//					|| !newPhone2.equals(newPhone)) {
//				Toast.makeText(this, "验证码输入错误！", 0).show();
//				return;
//			} else {
//				new Thread() {
//					public void run() {
//						postDataXutils(MyConfig.URL_POST_REGISTER);
//					};
//				}.start();
//			}
//			break;
//
//		}
//	}
//
//	/**
//	 * 获取旧手机号验证码
//	 * 
//	 * @param url
//	 */
//	private void getMessageOld(String url) {
//		HttpUtils httpUtils = new HttpUtils();
//		httpUtils.send(HttpRequest.HttpMethod.GET, url,
//				new RequestCallBack<String>() {
//					@Override
//					public void onFailure(HttpException arg0, String arg1) {
//						System.out.println("获取验证码失败" + arg1.toString());
//					}
//
//					@Override
//					public void onSuccess(ResponseInfo<String> arg0) {
//						System.out.println("arg0.result--" + arg0.result);
//						try {
//							JSONObject jsonObject = new JSONObject(arg0.result);
//							oldCode = jsonObject.getString("code");
//						} catch (JSONException e) {
//							e.printStackTrace();
//						}
//					}
//				});
//	}
//
//	/**
//	 * 获取新手机号验证码
//	 * 
//	 * @param url
//	 */
//	private void getMessageNew(String url) {
//		HttpUtils httpUtils = new HttpUtils();
//		httpUtils.send(HttpRequest.HttpMethod.GET, url,
//				new RequestCallBack<String>() {
//					@Override
//					public void onFailure(HttpException arg0, String arg1) {
//						System.out.println("获取验证码失败" + arg1.toString());
//					}
//
//					@Override
//					public void onSuccess(ResponseInfo<String> arg0) {
//						System.out.println("arg0.result--" + arg0.result);
//						try {
//							JSONObject jsonObject = new JSONObject(arg0.result);
//							newCode = jsonObject.getString("code");
//						} catch (JSONException e) {
//							e.printStackTrace();
//						}
//					}
//				});
//	}
//
//	/**
//	 * 提交数据
//	 * 
//	 * @param uploadHost
//	 */
//	private void postDataXutils(String uploadHost) {
//		HttpUtils httpUtils = new HttpUtils();
//		RequestParams params = new RequestParams();
//		if (org.equals("0")) {
//			params.addBodyParameter("uid", uid);
//			params.addBodyParameter("phones", newPhone);
//			params.addBodyParameter("status", "3");
//		} else if (org.equals("1")) {
//			params.addBodyParameter("org", "1");
//			params.addBodyParameter("uid", cuid);
//			params.addBodyParameter("phones", newPhone);
//			params.addBodyParameter("status", "3");
//		}
//		httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
//				new RequestCallBack<String>() {
//					@Override
//					public void onSuccess(ResponseInfo<String> responseInfo) {
//						System.out.println("responseInfo.result**"
//								+ responseInfo.result);
//						try {
//							JSONObject jsonObject = new JSONObject(
//									responseInfo.result);
//							result = jsonObject.getString("status");
//						} catch (JSONException e) {
//							e.printStackTrace();
//						}
//						if (!isDestroyed()) {
//							handlerReg.sendEmptyMessage(0);
//						}
//					}
//
//					@Override
//					public void onFailure(HttpException arg0, String arg1) {
//						System.out.println("上传数据失败");
//						Toast.makeText(PhoneUpdateActivity.this, "操作失败", 0)
//								.show();
//					}
//				});
//	}
//
//}
