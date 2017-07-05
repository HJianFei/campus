package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.adapters.OverseaAppraiseAdapter;
import com.loosoo100.campus100.beans.OverseasReplyInfo;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.CircleView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * 
 * @author yang 海归问答详情activity
 */
public class OverseasQuestionDetailActivity extends Activity implements
		OnClickListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view
	@ViewInject(R.id.rl_back)
	private RelativeLayout rl_back;
	@ViewInject(R.id.tv_name)
	private TextView tv_name;
	@ViewInject(R.id.tv_question)
	private TextView tv_question;
	@ViewInject(R.id.tv_readingCount)
	private TextView tv_readingCount;
	@ViewInject(R.id.tv_discussCount)
	private TextView tv_discussCount;
	@ViewInject(R.id.cv_headShot)
	private CircleView cv_headShot;
	@ViewInject(R.id.iv_sex)
	private ImageView iv_sex;

	@ViewInject(R.id.tv_loading)
	private TextView tv_loading; // 加载中
	@ViewInject(R.id.progress_update_blackbg)
	private RelativeLayout progress_update_blackbg; // 加载动画
	@ViewInject(R.id.ll_root_appraise)
	private LinearLayout ll_root_appraise;
	@ViewInject(R.id.lv_appraise)
	private ListView lv_appraise;
	@ViewInject(R.id.ll_top)
	private LinearLayout ll_top;

	@ViewInject(R.id.et_content)
	private EditText et_content; // 评论输入框内容
	@ViewInject(R.id.btn_send)
	private Button btn_send; // 评论输入框发送

	private int position = -1; // 记录点击的评论的哪一条 -1代表评论 否则代表回复
	private String respondent = ""; // 回复给谁

	// private List<CampusContactsInfo> list;
	// private List<Map<String, Object>> appraiseList;
	private List<OverseasReplyInfo> appraiseList;
	private OverseaAppraiseAdapter adapter;

	private String id = "";
	private String pid = "";
	private String name = "";
	private String headShot = "";
	private String question = "";
	private int read;
	private int discuss;
	private String uid = "";
	private String muid = "";
	private String sex = "";
	private int disCount;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (appraiseList != null && appraiseList.size() > 0) {
				initListView();
				ll_root_appraise.setVisibility(View.VISIBLE);
			} else {
				ll_root_appraise.setVisibility(View.GONE);
			}
			if (disCount != 0) {
				tv_discussCount.setText(disCount + "");
			}
			tv_loading.setVisibility(View.GONE);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_overseas_question_detail);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);

		id = getIntent().getExtras().getString("id");
		name = getIntent().getExtras().getString("name");
		headShot = getIntent().getExtras().getString("headShot");
		question = getIntent().getExtras().getString("question");
		read = getIntent().getExtras().getInt("read");
		discuss = getIntent().getExtras().getInt("discuss");
		muid = getIntent().getExtras().getString("muid");
		sex = getIntent().getExtras().getString("sex");

		uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.USERID, "");

		initView();

		new Thread() {
			public void run() {
				appraiseList = GetData
						.getOverseasReplyInfos(MyConfig.URL_POST_OVERSEAS_QUESTION_DETAIL
								+ id + "&uid=" + uid);
				if (!isDestroyed()) {
					handler.sendEmptyMessage(0);
				}
			};
		}.start();
	}

	private void initListView() {
		adapter = new OverseaAppraiseAdapter(this, appraiseList);
		lv_appraise.setAdapter(adapter);

		lv_appraise.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int mPosition, long id) {
				respondent = appraiseList.get(mPosition).getName();
				et_content.setHint("回复" + respondent + ":");
				et_content.setText("");
				position = mPosition;
				pid = appraiseList.get(mPosition).getId();
				// 显示软键盘
				MyUtils.showSoftInput(OverseasQuestionDetailActivity.this,
						et_content);
			}
		});
		MyUtils.setListViewHeight(lv_appraise, 0);
	}

	private void initView() {
		rl_back.setOnClickListener(this);
		btn_send.setOnClickListener(this);
		ll_top.setOnClickListener(this);
		cv_headShot.setOnClickListener(this);

		tv_name.setText(name);
		tv_question.setText(question);
		tv_readingCount.setText(read + "");
		tv_discussCount.setText(discuss + "");

		if (sex.equals("1")) {
			iv_sex.setImageDrawable(getResources().getDrawable(
					R.drawable.icon_female_picture));
		} else {
			iv_sex.setImageDrawable(getResources().getDrawable(
					R.drawable.icon_male_picture));
		}

		// 头像
		Glide.with(this).load(headShot).override(200, 200).into(cv_headShot);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			if (disCount != 0) {
				Intent intent = new Intent();
				intent.putExtra("discount", disCount);
				setResult(RESULT_OK, intent);
			}
			this.finish();
			break;

		case R.id.cv_headShot:
			if (uid.equals(muid)) {
				Intent intent = new Intent(this,
						CampusContactsPersonalActivity.class);
				startActivity(intent);
			} else {
				Intent intent = new Intent(this,
						CampusContactsFriendActivity.class);
				// intent.putExtra("uid", uid);
				// intent.putExtra("sex", sex);
				intent.putExtra("muid", muid);
				// intent.putExtra("headShot", headShot);
				// intent.putExtra("name", name);
				startActivity(intent);
			}
			break;

		case R.id.ll_top:
			respondent = "";
			et_content.setHint("");
			et_content.setText("");
			pid = "";
			break;

		// 评论内容发送
		case R.id.btn_send:
			if (!et_content.getText().toString().trim().equals("")) {
				progress_update_blackbg.setVisibility(View.VISIBLE);
				new Thread() {
					public void run() {
						postCampusContacts(MyConfig.URL_POST_OVERSEAS_APPRAISE,
								et_content.getText().toString().trim());
					};
				}.start();

				// 隐藏软键盘
				MyUtils.hideSoftInput(this, et_content);
			} else {
				ToastUtil.showToast(this,"输入内容不能为空");
			}
			break;
		}
	}

	/**
	 * 发表评论
	 * 
	 * @param uploadHost
	 */
	private void postCampusContacts(String uploadHost, String content) {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("uid", uid);
		params.addBodyParameter("qid", id);
		params.addBodyParameter("content", content);
		if (position != -1) {
			params.addBodyParameter("pid", pid);
		}
		httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						LogUtils.d("responseInfo",responseInfo.result);
						progress_update_blackbg.setVisibility(View.GONE);
						respondent = "";
						et_content.setHint("");
						et_content.setText("");
						pid = "";
						ToastUtil.showToast(OverseasQuestionDetailActivity.this,"发表成功");
						new Thread() {

							public void run() {
								appraiseList = GetData
										.getOverseasReplyInfos(MyConfig.URL_POST_OVERSEAS_QUESTION_DETAIL
												+ id + "&uid=" + uid);
								disCount = getDisCount(MyConfig.URL_POST_OVERSEAS_QUESTION_DETAIL
										+ id + "&uid=" + uid);
								if (!isDestroyed()) {
									handler.sendEmptyMessage(0);
								}
							};
						}.start();
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.d("error",arg1.toString());
						progress_update_blackbg.setVisibility(View.GONE);
						ToastUtil.showToast(OverseasQuestionDetailActivity.this,"发表失败");
					}
				});
	}

	/**
	 * 获取讨论人数
	 */
	private int getDisCount(String urlString) {
		InputStream is = null;
		int count = 0;
		try {
			is = new URL(urlString).openStream();
			String jsonString = GetData.readStream(is);
			JSONObject object = new JSONObject(jsonString);
			JSONObject jsonObject = object.getJSONObject("ques");
			count = jsonObject.optInt("hgques_ans", 0);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (disCount != 0) {
				Intent intent = new Intent();
				intent.putExtra("discount", disCount);
				setResult(RESULT_OK, intent);
				this.finish();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
