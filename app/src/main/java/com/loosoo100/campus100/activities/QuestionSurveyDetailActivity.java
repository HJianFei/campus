package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import com.loosoo100.campus100.adapters.QuestionOptionsAdapter;
import com.loosoo100.campus100.beans.QuestionDetailBean;
import com.loosoo100.campus100.beans.QuestionInfo;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 问卷调查详情页
 * 
 * @author yang
 */
public class QuestionSurveyDetailActivity extends Activity implements
		OnClickListener {

	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view
	@ViewInject(R.id.rl_back)
	private RelativeLayout rl_back;
	@ViewInject(R.id.iv_bg)
	private ImageView iv_bg;
	@ViewInject(R.id.progressBar)
	private ProgressBar progressBar;
	@ViewInject(R.id.tv_index)
	private TextView tv_index;
	@ViewInject(R.id.tv_question)
	private TextView tv_question;
	@ViewInject(R.id.btn_previous)
	private Button btn_previous;
	@ViewInject(R.id.btn_next)
	private Button btn_next;
	@ViewInject(R.id.btn_ok)
	private Button btn_ok;
	@ViewInject(R.id.listView)
	private ListView listView;
	@ViewInject(R.id.et_content)
	private EditText et_content;
	@ViewInject(R.id.progress)
	private RelativeLayout progress;
	@ViewInject(R.id.progress_update_whitebg)
	private RelativeLayout progress_update_whitebg;
	@ViewInject(R.id.ll_details)
	private LinearLayout ll_details;

	@ViewInject(R.id.ll_description)
	private LinearLayout ll_description;
	@ViewInject(R.id.tv_description)
	private TextView tv_description;
	@ViewInject(R.id.btn_join)
	private Button btn_join;

	@ViewInject(R.id.ll_conclusion)
	private LinearLayout ll_conclusion;
	@ViewInject(R.id.tv_conclusion)
	private TextView tv_conclusion;
	@ViewInject(R.id.btn_finish)
	private Button btn_finish;

	private String uid = "";

	private QuestionInfo questionInfo;
	private List<QuestionDetailBean> details;
	private List<String> options;
	private QuestionOptionsAdapter adapter;

	private int index = 0;
	private String qid = "";

	private int[] choose;
	private String[] answers;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			initView();
			progress.setVisibility(View.GONE);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question_survey_detail);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);

		uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.USERID, "");

		qid = getIntent().getExtras().getString("qid");

		progress.setVisibility(View.VISIBLE);

		rl_back.setOnClickListener(this);
		btn_previous.setOnClickListener(this);
		btn_next.setOnClickListener(this);
		btn_ok.setOnClickListener(this);
		btn_join.setOnClickListener(this);
		btn_finish.setOnClickListener(this);
		iv_bg.setImageResource(R.drawable.questionsurvey);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				setBtnStatus(true);
				choose[index] = position;
				adapter.mPosition = position;
				adapter.notifyDataSetChanged();
				MyUtils.setListViewHeight(listView, 20);
			}
		});

		new Thread() {
			public void run() {
				postIsSubmit(MyConfig.URL_JSON_QUESTION_IS_SUBMIT);
				questionInfo = GetData
						.getQuestionInfo(MyConfig.URL_JSON_QUESTION_DETAIL
								+ qid);
				if (questionInfo != null && !isDestroyed()) {
					handler.sendEmptyMessage(0);
				}
			};
		}.start();

		et_content.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				answers[index] = et_content.getText().toString();
				if (et_content.getText().toString().equals("")) {
					setBtnStatus(false);
				} else {
					setBtnStatus(true);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	protected void initView() {
		tv_description.setText(questionInfo.getDescription());
		tv_conclusion.setText(questionInfo.getConclusion());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			this.finish();
			break;

		case R.id.btn_join:
			details = questionInfo.getDetail();
			if (details == null || details.size() == 0) {
				return;
			}
			ll_description.setVisibility(View.GONE);
			choose = new int[details.size()];
			answers = new String[details.size()];
			for (int i = 0; i < choose.length; i++) {
				choose[i] = -1;
				answers[i] = "";
			}
			update();
			break;

		case R.id.btn_previous:
			index--;
			update();
			break;

		case R.id.btn_next:
			index++;
			update();
			break;

		case R.id.btn_ok:
			progress_update_whitebg.setVisibility(View.VISIBLE);
			new Thread() {
				public void run() {
					postData(MyConfig.URL_POST_QUESTION_SUBMIT);
				};
			}.start();
			break;

		case R.id.btn_finish:
			finish();
			break;
		}
	}

	private void update() {
		if (index == 0) {
			btn_previous.setVisibility(View.GONE);
		} else {
			btn_previous.setVisibility(View.VISIBLE);
		}
		if (index == details.size() - 1) {
			btn_next.setVisibility(View.GONE);
			btn_ok.setVisibility(View.VISIBLE);
		} else {
			btn_next.setVisibility(View.VISIBLE);
			btn_ok.setVisibility(View.GONE);
		}
		progressBar
				.setProgress((int) ((index + 1) * 1.0f / details.size() * 100));
		tv_index.setText((index + 1) + "/" + details.size());
		//段首空格
		tv_question.setText("   "+details.get(index).getQuestion());
		if (details.get(index).getQuest_type().equals("0")) {
			if (choose[index] == -1) {
				setBtnStatus(false);
			} else {
				setBtnStatus(true);
			}
			et_content.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);

			options = details.get(index).getOptions();
			adapter = new QuestionOptionsAdapter(this, options);
			adapter.mPosition = choose[index];
			listView.setAdapter(adapter);
			MyUtils.setListViewHeight(listView, 20);
		} else {
			if (answers[index].equals("")) {
				setBtnStatus(false);
			} else {
				setBtnStatus(true);
			}
			et_content.setText(answers[index]);
			et_content.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
		}
	}

	private void setBtnStatus(boolean show) {
		if (show) {
			btn_next.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.selector_btn_red));
			btn_ok.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.selector_btn_red));
			btn_next.setClickable(true);
			btn_ok.setClickable(true);
		} else {
			btn_next.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.shape_gray_404040));
			btn_ok.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.shape_gray_404040));
			btn_next.setClickable(false);
			btn_ok.setClickable(false);
		}
	}

	/**
	 * 提交数据
	 */
	private void postData(String uploadHost) {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("qid", qid);
		params.addBodyParameter("uid", uid);
		for (int i = 0; i < details.size(); i++) {
			if (details.get(i).getQuest_type().equals("0")) {
				params.addBodyParameter(i + "", "0:"
						+ details.get(i).getQuest_order() + ":"
						+ (choose[i] + 1) + "");
			} else {
				params.addBodyParameter(i + "", details.get(i).getQuest_type()
						+ ":" + details.get(i).getQuest_order() + ":"
						+ answers[i] + "");
			}
		}
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
							ll_conclusion.setVisibility(View.VISIBLE);
						} else {
							ToastUtil.showToast(QuestionSurveyDetailActivity.this,"提交失败");
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.d("error",arg1.toString());
						progress_update_whitebg.setVisibility(View.GONE);
						ToastUtil.showToast(QuestionSurveyDetailActivity.this,"提交失败");
					}
				});
	}

	/**
	 * 判断用户是否提交过
	 */
	private void postIsSubmit(String uploadHost) {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("qid", qid);
		params.addBodyParameter("uid", uid);
		httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						LogUtils.d("responseInfo",responseInfo.result);
						String result = "";
						try {
							JSONObject jsonObject = new JSONObject(
									responseInfo.result);
							result = jsonObject.getString("hasSubmitted");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (result.equals("1")) {
							progress.setVisibility(View.GONE);
							btn_finish.setText("您已参加过");
							ll_conclusion.setVisibility(View.VISIBLE);
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.d("error",arg1.toString());
						ToastUtil.showToast(QuestionSurveyDetailActivity.this,"提交失败");
					}
				});
	}

}
