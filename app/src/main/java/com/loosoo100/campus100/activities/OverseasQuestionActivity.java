package com.loosoo100.campus100.activities;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.adapters.OverseasQuestionAdapter;
import com.loosoo100.campus100.anyevent.MEventOverseasQuestion;
import com.loosoo100.campus100.beans.OverseasQuestionInfo;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyAnimation;
import com.loosoo100.campus100.utils.MyUtils;

import de.greenrobot.event.EventBus;

/**
 * 
 * @author yang 海归问答activity
 */
public class OverseasQuestionActivity extends Activity implements
		OnClickListener, OnTouchListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view

	@ViewInject(R.id.rl_back)
	private RelativeLayout rl_back; // 返回
	@ViewInject(R.id.rl_myask)
	private RelativeLayout rl_myask; // 我的提问
	@ViewInject(R.id.ib_add)
	private ImageButton ib_add; // 发起提问
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
	private int mPosition;

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
		setContentView(R.layout.activity_overseas_question);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);
		EventBus.getDefault().register(this);

		progress.setVisibility(View.VISIBLE);
		initView();

		new Thread() {
			public void run() {
				list = GetData
						.getOverseasQuestionInfos(MyConfig.URL_JSON_OVERSEAS_QUESTION);
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
				mPosition = position;
				Intent intent = new Intent(OverseasQuestionActivity.this,
						OverseasQuestionDetailActivity.class);
				list.get(position).setReadingCount(
						list.get(position).getReadingCount() + 1);
				intent.putExtra("id", list.get(position).getId());
				intent.putExtra("name", list.get(position).getName());
				intent.putExtra("headShot", list.get(position).getHeadShot());
				intent.putExtra("question", list.get(position).getQuestion());
				intent.putExtra("read", list.get(position).getReadingCount());
				intent.putExtra("discuss", list.get(position).getDiscussCount());
				intent.putExtra("muid", list.get(position).getUid());
				intent.putExtra("sex", list.get(position).getSex());
				startActivityForResult(intent, 3);
			}
		});
	}

	private void initView() {
		rl_back.setOnClickListener(this);
		rl_myask.setOnClickListener(this);
		ib_add.setOnClickListener(this);
		ib_add.setOnTouchListener(this);

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
											+ "?page=" + page);
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

		case R.id.rl_myask:
			Intent intent = new Intent(this, OverseasMyQuestionActivity.class);
			startActivityForResult(intent, 1);
			break;

		case R.id.ib_add:
			Intent intent2 = new Intent(this, OverseasAskActivity.class);
			startActivityForResult(intent2, 2);
			break;

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == 2) {
			progress_update_whitebg.setVisibility(View.VISIBLE);
			isLoading = true;
			new Thread() {
				public void run() {
					list = GetData
							.getOverseasQuestionInfos(MyConfig.URL_JSON_OVERSEAS_QUESTION);
					if (!isDestroyed()) {
						handler.sendEmptyMessage(0);
					}
				};
			}.start();
		} else if (resultCode == RESULT_OK && requestCode == 3) {
			list.get(mPosition).setDiscussCount(
					data.getExtras().getInt("discount"));
			adapter.notifyDataSetChanged();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN
				&& v.getId() == R.id.ib_add) {
			ib_add.startAnimation(MyAnimation.getScaleAnimationDown());
		}
		return false;
	}

	public void onEventMainThread(MEventOverseasQuestion event) {
		if (event.isChange()) {
			new Thread() {
				public void run() {
					list = GetData
							.getOverseasQuestionInfos(MyConfig.URL_JSON_OVERSEAS_QUESTION);
					if (!isDestroyed()) {
						handler.sendEmptyMessage(0);
					}
				};
			}.start();
		}
	}

	@Override
	protected void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}
}
