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
import com.loosoo100.campus100.activities.CompanySummaryActivity;
import com.loosoo100.campus100.adapters.CommunityBossHomeAdapter;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.zzboss.beans.BossCompanySummaryInfo;

/**
 * 
 * @author yang 最给力企业列表
 */
public class BossCompanyListActivity extends Activity implements
		OnClickListener, OnScrollListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view
	@ViewInject(R.id.rl_back)
	private RelativeLayout rl_back;
	@ViewInject(R.id.listView)
	private ListView listView; // 列表
	@ViewInject(R.id.rl_progress)
	private RelativeLayout rl_progress; // 加载动画

	private int page = 1;

	private boolean isLoading = true;

	private CommunityBossHomeAdapter adapter;
	private List<BossCompanySummaryInfo> list;

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
		setContentView(R.layout.activity_boss_company_list);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);

		rl_progress.setVisibility(View.VISIBLE);
		initView();

		// 数据后台加载
		new Thread(new Runnable() {
			@Override
			public void run() {
				list = GetData
						.getBossHomeBossInfos(MyConfig.URL_JSON_COMPANY_BOSS);
				if (!isDestroyed()) {
					handler.sendEmptyMessage(0);
				}

			}
		}).start();
	}

	private void initListView() {
		adapter = new CommunityBossHomeAdapter(this, list);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(BossCompanyListActivity.this,
						CompanySummaryActivity.class);
				intent.putExtra("cuid", list.get(position).getId());
//				intent.putExtra("commLeader", false);
				startActivity(intent);
			}
		});
	}

	private void initView() {
		listView.setOnScrollListener(this);
		rl_back.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			finish();
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
				&& totalItemCount > 0 && !isLoading) {
			isLoading = true;
			new Thread() {
				public void run() {
					List<BossCompanySummaryInfo> infos = GetData
							.getBossHomeBossInfos(MyConfig.URL_JSON_COMPANY_BOSS
									+ "?page=" + page);
					if (infos != null && infos.size() > 0) {
						for (int i = 0; i < infos.size(); i++) {
							list.add(infos.get(i));
						}
						handlerRefresh.sendEmptyMessage(0);
					}
				};
			}.start();
		}
	}

}
