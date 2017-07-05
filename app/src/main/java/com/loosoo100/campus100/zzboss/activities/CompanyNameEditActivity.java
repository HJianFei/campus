package com.loosoo100.campus100.zzboss.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author yang 公司名称更改
 */
public class CompanyNameEditActivity extends Activity implements
		OnClickListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view
	@ViewInject(R.id.rl_back)
	private View rl_back;
	@ViewInject(R.id.et_name)
	private EditText et_name;
	@ViewInject(R.id.btn_save)
	private Button btn_save;

	private String cuid = ""; // 公司ID
	private String name = "";
	private String result = "";

	// 保存成功
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (result.equals("1")) {
				// 本地保存
				UserInfoDB.setUserInfo(CompanyNameEditActivity.this,
						UserInfoDB.COMPANY, name);
				ToastUtil.showToast(CompanyNameEditActivity.this,"修改成功");
				finish();
			} else {
				ToastUtil.showToast(CompanyNameEditActivity.this,"修改失败");
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_boss_company_name_edit);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);

		cuid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.CUSERID, "");

		rl_back.setOnClickListener(this);
		btn_save.setOnClickListener(this);

		String company = getSharedPreferences(UserInfoDB.USERTABLE,
				MODE_PRIVATE).getString(UserInfoDB.COMPANY, "");
		et_name.setText(company);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			this.finish();
			break;

		// 保存用户昵称
		case R.id.btn_save:
			name = et_name.getText().toString().trim();
			if (name.equals("")) {
				ToastUtil.showToast(CompanyNameEditActivity.this,"请输入正确的公司名称");
				return;
			}
			new Thread() {
				public void run() {
					postDataBoss(MyConfig.URL_POST_COMPANY_EDIT_BOSS);
				};
			}.start();
			break;

		}
	}

	/**
	 * 提交数据
	 */
	private void postDataBoss(String uploadHost) {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("cuid", cuid);
		params.addBodyParameter("companyName", name);
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
							handler.sendEmptyMessage(0);
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.d("error",arg1.toString());
						ToastUtil.showToast(CompanyNameEditActivity.this,"修改失败");
					}
				});
	}

}
