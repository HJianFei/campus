package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.loosoo100.campus100.adapters.OverseasQuestionAdapter;
import com.loosoo100.campus100.anyevent.MEventOverseasQuestion;
import com.loosoo100.campus100.beans.OverseasQuestionInfo;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 
 * @author yang 海归我的问答activity
 */
public class OverseasMyQuestionActivity extends Activity implements
		OnClickListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view

	@ViewInject(R.id.rl_back)
	private RelativeLayout rl_back; // 返回
	@ViewInject(R.id.lv_question)
	private ListView lv_question; // 列表
	@ViewInject(R.id.progress)
	private RelativeLayout progress; // 加载动画
	@ViewInject(R.id.progress_update_whitebg)
	private RelativeLayout progress_update_whitebg; // 加载动画

	private List<OverseasQuestionInfo> list;
	private OverseasQuestionAdapter adapter;

	private boolean isLoading = true;
	private int page = 1;

	private String uid = "";
	private String sex = "";

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (list != null && list.size() > 0) {
				initListView();
				lv_question.setVisibility(View.VISIBLE);
			} else {
				lv_question.setVisibility(View.GONE);
			}
			page = 2;
			isLoading = false;
			progress.setVisibility(View.GONE);
			progress_update_whitebg.setVisibility(View.GONE);
		};
	};

	/*
	 * 加载更多后更新数据
	 */
	private Handler handlerRefresh = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (adapter != null) {
				adapter.notifyDataSetChanged();
				isLoading = false;
				page++;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_overseas_myquestion);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);
		uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.USERID, "");
		sex = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.SEX, "1");

		progress.setVisibility(View.VISIBLE);
		initView();

		new Thread() {
			public void run() {
				list = GetData
						.getOverseasQuestionInfos(MyConfig.URL_JSON_OVERSEAS_QUESTION
								+ "?uid=" + uid);
				if (!isDestroyed()) {
					handler.sendEmptyMessage(0);
				}
			};
		}.start();
	}

	private void initListView() {
		adapter = new OverseasQuestionAdapter(this, list);
		lv_question.setAdapter(adapter);
		lv_question.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(OverseasMyQuestionActivity.this,
						OverseasQuestionDetailActivity.class);
				intent.putExtra("id", list.get(position).getId());
				intent.putExtra("name", list.get(position).getName());
				intent.putExtra("headShot", list.get(position).getHeadShot());
				intent.putExtra("question", list.get(position).getQuestion());
				intent.putExtra("read", list.get(position).getReadingCount());
				intent.putExtra("discuss", list.get(position).getDiscussCount());
				intent.putExtra("sex", sex);
				startActivity(intent);
			}
		});

		lv_question.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				CustomDialog.Builder builderSure = new CustomDialog.Builder(
						OverseasMyQuestionActivity.this);
				builderSure.setMessage("是否确认删除");
				builderSure.setPositiveButton("是",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								progress_update_whitebg
										.setVisibility(View.VISIBLE);
								new Thread() {
									public void run() {
										postDel(MyConfig.URL_POST_OVERSEAS_QUESTION_DEL,
												position);
									};
								}.start();
							}
						});
				builderSure.setNegativeButton("否", null);
				builderSure.create().show();
				return true;
			}
		});
	}

	private void initView() {
		rl_back.setOnClickListener(this);

		lv_question.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (firstVisibleItem + visibleItemCount == totalItemCount
						&& totalItemCount > 0 && !isLoading) {
					isLoading = true;
					new Thread() {
						private List<OverseasQuestionInfo> list2;

						public void run() {
							list2 = GetData
									.getOverseasQuestionInfos(MyConfig.URL_JSON_OVERSEAS_QUESTION
											+ "?uid=" + uid + "&page=" + page);
							if (list2 != null && list2.size() > 0) {
								for (int i = 0; i < list2.size(); i++) {
									list.add(list2.get(i));
								}
								if (!isDestroyed()) {
									handlerRefresh.sendEmptyMessage(0);
								}
							}
						};
					}.start();
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			this.finish();
			break;

		}
	}

	private void postDel(String uploadHost, final int position) {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("id", list.get(position).getId());
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
							ToastUtil.showToast(OverseasMyQuestionActivity.this,"删除成功");
							EventBus.getDefault().post(
									new MEventOverseasQuestion(true));
							list.remove(position);
							adapter.notifyDataSetChanged();
						} else {
							ToastUtil.showToast(OverseasMyQuestionActivity.this,"操作失败");
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.d("error",arg1.toString());
						progress_update_whitebg.setVisibility(View.GONE);
						ToastUtil.showToast(OverseasMyQuestionActivity.this,"操作失败");
					}
				});
	}
}
