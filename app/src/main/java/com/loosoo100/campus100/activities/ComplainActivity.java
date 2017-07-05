package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.os.Bundle;
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
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.MyUtils;

/**
 * 
 * @author yang 投诉activity
 */
public class ComplainActivity extends Activity implements OnClickListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view
	@ViewInject(R.id.rl_back)
	private View rl_back;
	@ViewInject(R.id.et_complain)
	private EditText et_complain; // 反馈内容
	@ViewInject(R.id.et_phone)
	private EditText et_phone; // 手机号
	@ViewInject(R.id.btn_ok)
	private Button btn_ok; // 确定按钮
	@ViewInject(R.id.progress_update_blackbg)
	private RelativeLayout progress_update_blackbg;

	private String orderId = "";
	private String shopId = "";
	private String userName = "";
	private String content = "";
	private String phone = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_complain);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);

		orderId = getIntent().getExtras().getString("orderId");
		shopId = getIntent().getExtras().getString("shopId");
		userName = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.NICK_NAME, "");

		initView();

	}

	private void initView() {
		rl_back.setOnClickListener(this);
		btn_ok.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			this.finish();
			break;

		case R.id.btn_ok:
			content = et_complain.getText().toString().trim();
			phone = et_phone.getText().toString().trim();
			if (content.equals("") || phone.equals("")) {
				ToastUtil.showToast(this,"请将信息填写完整");
				return;
			}
			progress_update_blackbg.setVisibility(View.VISIBLE);
			new Thread() {
				public void run() {
					postData(MyConfig.URL_POST_GOODS_ORDER_COMPLAIN);
				};
			}.start();
			break;

		}
	}

	/**
	 * 提交数据
	 * 
	 * @param uploadHost
	 */
	private void postData(String uploadHost) {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("userName", userName);
		params.addBodyParameter("shopId", shopId);
		params.addBodyParameter("orderId", orderId);
		params.addBodyParameter("content", content);
		params.addBodyParameter("userPhone", phone);
		httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						LogUtils.d("responseInfo",responseInfo.result);
						progress_update_blackbg.setVisibility(View.GONE);
						ToastUtil.showToast(ComplainActivity.this,"提交成功");
						ComplainActivity.this.finish();
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.d("error",arg1.toString());
						progress_update_blackbg.setVisibility(View.GONE);
						ToastUtil.showToast(ComplainActivity.this,"提交失败");
					}
				});
	}

}
