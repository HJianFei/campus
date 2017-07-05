package com.loosoo100.campus100.zzboss.activities;

import java.util.List;

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
import com.loosoo100.campus100.adapters.CommActiListAdapter;
import com.loosoo100.campus100.beans.CommunityActivityInfo;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;

/**
 * 
 * @author yang 社团活动更多activity
 */
public class BossCommActiMoreActivity extends Activity implements
		OnClickListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view
	@ViewInject(R.id.rl_back)
	private RelativeLayout rl_back;
	@ViewInject(R.id.listView)
	private ListView listView; // 列表
	@ViewInject(R.id.rl_progress)
	private RelativeLayout rl_progress; // 加载动画

	private int page = 2;

	private CommActiListAdapter adapter;
	private List<CommunityActivityInfo> list;
	private List<CommunityActivityInfo> list2;

	private boolean loading = true; // 是否正在加载数据

	private String id = "";

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (list != null && list.size() > 0) {
				initListView();
				listView.setVisibility(View.VISIBLE);
			} else {
				listView.setVisibility(View.GONE);
			}
			loading = false;
			rl_progress.setVisibility(View.GONE);
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
			loading = false;
			page++;
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_boss_comm_acti_more);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);

		id = getIntent().getExtras().getString("id");

		rl_progress.setVisibility(View.VISIBLE);
		rl_back.setOnClickListener(this);

		// 数据后台加载
		new Thread() {
			@Override
			public void run() {
				list = GetData
						.getMoreActivityInfos(MyConfig.URL_JSON_COMMUNITY_ACTIVITY_MORE
								+ id);
				if (!isDestroyed()) {
					handler.sendEmptyMessage(0);
				}

			}
		}.start();

	}

	private void initListView() {
		adapter = new CommActiListAdapter(this, list);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (list.get(position).getClassify() == 0) {
					Intent intent = new Intent(BossCommActiMoreActivity.this,
							BossCommActivityDetailTogetherActivity.class);
					intent.putExtra("aid", list.get(position).getId());
					startActivity(intent);
				} else {
					Intent intent = new Intent(BossCommActiMoreActivity.this,
							BossCommActivityDetailFreeActivity.class);
					intent.putExtra("aid", list.get(position).getId());
					startActivity(intent);
				}
			}
		});

		listView.setOnScrollListener(new OnScrollListener() {
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
							list2 = GetData
									.getMoreActivityInfos(MyConfig.URL_JSON_COMMUNITY_ACTIVITY_MORE
											+ id + "&page=" + page);
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == 0) {
			page = 2;
			loading = true;
			new Thread() {
				@Override
				public void run() {
					list = GetData
							.getMoreActivityInfos(MyConfig.URL_JSON_COMMUNITY_ACTIVITY_MORE
									+ id);
					if (!isDestroyed()) {
						handlerRefresh.sendEmptyMessage(0);
					}
				};
			}.start();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
