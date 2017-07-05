package com.loosoo100.campus100.activities;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
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
import com.loosoo100.campus100.adapters.CommActiListHomeAdapter;
import com.loosoo100.campus100.beans.CommunityActivityInfo;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;

import static com.loosoo100.campus100.view.spinkit.animation.AnimationUtils.start;

/**
 * 
 * @author yang 我的社团活动activity
 */
public class CommunityMyActivityActivity extends Activity implements
		OnClickListener, OnScrollListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view
	@ViewInject(R.id.rl_back)
	private RelativeLayout rl_back;
	@ViewInject(R.id.listView)
	private ListView listView; // 列表
	@ViewInject(R.id.rl_progress)
	private RelativeLayout rl_progress; // 加载动画

	private int page = 2;

	private CommActiListHomeAdapter adapter;
	private List<CommunityActivityInfo> list;
	private Intent intent = new Intent();

	private boolean loading = true;
	private boolean isPay = false;

	private String cid = "";
	private String usersid = "";

	private List<CommunityActivityInfo> communityActivityInfos;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			rl_progress.setVisibility(View.GONE);
			if (list != null && list.size() > 0) {
				initListView();
				listView.setVisibility(View.VISIBLE);
			} else {
				listView.setVisibility(View.GONE);
			}
			page = 2;
			loading = false;
		};
	};

	/*
	 * 上拉刷新完成后更新数据
	 */
	private Handler handlerRefresh = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (adapter != null) {
				adapter.notifyDataSetChanged();
			}
			page++;
			loading = false;
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community_myactivity);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);

		cid = getIntent().getExtras().getString("cid");
		usersid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.SCHOOL_ID, "");

		rl_progress.setVisibility(View.VISIBLE);
		rl_back.setOnClickListener(this);
		listView.setOnScrollListener(this);

		// 数据后台加载
		new Thread() {
			@Override
			public void run() {
				list = GetData
						.getCommunityActivityInfos(MyConfig.URL_JSON_COMMUNITY_ACTIVITY_MORE
								+ cid + "&usersid=" + usersid);
				if (!isDestroyed()) {
					handler.sendEmptyMessage(0);
				}
			}
		}.start();

	}

	private void initListView() {
		adapter = new CommActiListHomeAdapter(this, list);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (list.get(position).getClassify() == 0) {
					Intent intent = new Intent(
							CommunityMyActivityActivity.this,
							CommActivityDetailTogetherActivity.class);
					intent.putExtra("aid", list.get(position).getId());
					startActivity(intent);
				} else {
					Intent intent = new Intent(
							CommunityMyActivityActivity.this,
							CommActivityDetailFreeActivity.class);
					intent.putExtra("aid", list.get(position).getId());
					startActivity(intent);
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			if (isPay) {
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				this.finish();
			} else {
				this.finish();
			}
			break;

		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (firstVisibleItem + visibleItemCount == totalItemCount
				&& totalItemCount > 0 && !loading) {
			loading = true;
			new Thread() {
				public void run() {
					communityActivityInfos = GetData
							.getCommunityActivityInfos(MyConfig.URL_JSON_COMMUNITY_ACTIVITY_MORE
									+ cid
									+ "&usersid="
									+ usersid
									+ "&page="
									+ page);
					if (communityActivityInfos != null
							&& communityActivityInfos.size() > 0) {
						for (int i = 0; i < communityActivityInfos.size(); i++) {
							list.add(communityActivityInfos.get(i));
						}
						handlerRefresh.sendEmptyMessage(0);
					}
				};
			}.start();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == 0) {
			isPay = true;
			loading = true;
			page = 1;
			new Thread() {
				@Override
				public void run() {
					list = GetData
							.getCommunityActivityInfos(MyConfig.URL_JSON_COMMUNITY_ACTIVITY_MORE
									+ cid + "&usersid=" + usersid);
					loading = false;
					if (!isDestroyed()) {
						handlerRefresh.sendEmptyMessage(0);
					}
				};
			}.start();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isPay) {
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				this.finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
