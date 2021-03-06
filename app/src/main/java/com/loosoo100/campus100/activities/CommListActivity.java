package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.beans.CommunityBasicInfo;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.zzboss.adapter.BossCommListAdapter;

import java.util.List;

/**
 * 
 * @author yang 最活跃社团列表
 */
public class CommListActivity extends Activity implements OnClickListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view
	@ViewInject(R.id.rl_back)
	private RelativeLayout rl_back;
//	@ViewInject(R.id.rl_search)
//	private RelativeLayout rl_search;
//	@ViewInject(R.id.btn_title)
//	private Button btn_title;
	// @ViewInject(R.id.lv_school)
	// private ListView lv_school; // 下拉学校列表
	@ViewInject(R.id.listView)
	private ListView listView; // 列表
	@ViewInject(R.id.rl_progress)
	private RelativeLayout rl_progress; // 加载动画
	@ViewInject(R.id.progress_update_whitebg)
	private RelativeLayout progress_update_whitebg; // 加载动画

	private int page = 1;
	private String school = "";
	private String sid = "0";

	private boolean isLoading = true;

	private BossCommListAdapter adapter;
	private List<CommunityBasicInfo> list;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (list != null && list.size() > 0) {
				initListView();
				listView.setVisibility(View.VISIBLE);
			} else {
				listView.setVisibility(View.GONE);
			}
			page = 2;
			isLoading = false;
			rl_progress.setVisibility(View.GONE);
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
			}
			page++;
			isLoading = false;
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comm_list);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);

		rl_progress.setVisibility(View.VISIBLE);
		initView();

		// 数据后台加载
		new Thread() {
			@Override
			public void run() {
				list = GetData
						.getBossCommListInfos(MyConfig.URL_JSON_COMM_BOSS);
				if (!isDestroyed()) {
					handler.sendEmptyMessage(0);
				}

			}
		}.start();
	}

	private void initListView() {
		adapter = new BossCommListAdapter(this, list);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(CommListActivity.this,
						CommDetailActivity.class);
				intent.putExtra("id", list.get(position).getId());
				startActivity(intent);
			}
		});

	}

	private void initView() {
		rl_back.setOnClickListener(this);
//		rl_search.setOnClickListener(this);
//		btn_title.setOnClickListener(this);
//		btn_title.setClickable(false);

		listView.setOnScrollListener(new OnScrollListener() {

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
						public void run() {
							List<CommunityBasicInfo> communityBasicInfos = GetData
									.getBossCommListInfos(MyConfig.URL_JSON_COMM_BOSS
											+ "?page=" + page + "&sid=" + sid);
							if (communityBasicInfos != null
									&& communityBasicInfos.size() > 0) {
								for (int i = 0; i < communityBasicInfos.size(); i++) {
									list.add(communityBasicInfos.get(i));
								}
								handlerRefresh.sendEmptyMessage(0);
							}
						}
					}.start();
				}
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			finish();
			break;

//		case R.id.rl_search:
//			Intent intent = new Intent(this, SearchActivity.class);
//			startActivityForResult(intent, 1);
//			break;

//		case R.id.btn_title:
			// if (lv_school.getVisibility() == View.VISIBLE) {
			// lv_school.setVisibility(View.GONE);
			// lv_school.startAnimation(MyAnimation.getScaleAnimationToTopBus());
			// } else {
			// lv_school.setVisibility(View.VISIBLE);
			// lv_school.startAnimation(MyAnimation
			// .getScaleAnimationToBottomBus());
			// }
//			break;
		}
	}

//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (resultCode == RESULT_OK && requestCode == 1) {
//			if (list != null) {
//				list.clear();
//			}
//			school = data.getExtras().getString(MyConfig.SCHOOL_SEARCH);
//			sid = data.getExtras().getString(MyConfig.SCHOOL_SEARCH_ID);
//			btn_title.setText(school);
//			btn_title.setClickable(true);
//			progress_update_whitebg.setVisibility(View.VISIBLE);
//			// 数据后台加载
//			new Thread() {
//				@Override
//				public void run() {
//					list = GetData
//							.getBossCommListInfos(MyConfig.URL_JSON_COMM_BOSS
//									+ "?sid=" + sid);
//					if (!isDestroyed()) {
//						handler.sendEmptyMessage(0);
//					}
//				}
//			}.start();
//		}
//		super.onActivityResult(requestCode, resultCode, data);
//	}

}
