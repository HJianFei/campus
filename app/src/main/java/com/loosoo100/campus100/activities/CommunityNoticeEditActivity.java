package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.loosoo100.campus100.utils.MyUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author yang 社团简介编辑activity
 */
public class CommunityNoticeEditActivity extends Activity implements
		OnClickListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view
	@ViewInject(R.id.rl_back)
	private RelativeLayout rl_back; // 返回
	@ViewInject(R.id.rl_save)
	private RelativeLayout rl_save; // 保存
	@ViewInject(R.id.et_summary)
	private EditText et_summary; // 内容

	private String notice = "";
	private String result = "";

	private String cid="";

	// 保存成功
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (result.equals("1")) {
				ToastUtil.showToast(CommunityNoticeEditActivity.this,"保存成功");
				Intent intent = new Intent();
				intent.putExtra("notice", et_summary.getText().toString());
				setResult(RESULT_OK, intent);
				CommunityNoticeEditActivity.this.finish();
			} else if (result.equals("0")) {
				ToastUtil.showToast(CommunityNoticeEditActivity.this,"保存失败");
			}
		};
	};

	// 保存失败
	private Handler handler2 = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ToastUtil.showToast(CommunityNoticeEditActivity.this,"保存失败");
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community_notice_edit);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);

		notice = getIntent().getExtras().getString("notice");
		cid = getIntent().getExtras().getString("cid");
		initView();

	}

	private void initView() {
		rl_back.setOnClickListener(this);
		rl_save.setOnClickListener(this);

		et_summary.setText(notice);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			this.finish();
			break;

		case R.id.rl_save:
			if (et_summary.getText().toString().trim().equals("")) {
				ToastUtil.showToast(CommunityNoticeEditActivity.this,"输入内容不能为空");
			} else {
				new Thread() {
					public void run() {
						postData(MyConfig.URL_POST_COMMUNITY_NOTICE);
					};
				}.start();
			}
			break;
		}
	}

	/**
	 * 提交数据
	 */
	private void postData(String uploadHost) {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("notice", et_summary.getText().toString());
		params.addBodyParameter("cid", cid+"");
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
						handler.sendEmptyMessage(0);
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.d("error",arg1.toString());
						handler2.sendEmptyMessage(0);
					}
				});
	}
}
