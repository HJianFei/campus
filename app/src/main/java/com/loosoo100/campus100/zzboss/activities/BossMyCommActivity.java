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
import com.loosoo100.campus100.beans.CommunityBasicInfo;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.zzboss.adapter.BossCommInterestAdapter;

/**
 * 
 * @author yang 我赞助过的社团
 */
public class BossMyCommActivity extends Activity implements OnClickListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view
	@ViewInject(R.id.rl_back)
	private RelativeLayout rl_back;
	@ViewInject(R.id.listView)
	private ListView listView; // 列表
	@ViewInject(R.id.rl_progress)
	private RelativeLayout rl_progress; // 加载动画

	private String cuid = "";
	private boolean isLoading = true;
	private int page = 1;

	private BossCommInterestAdapter adapter;
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
		setContentView(R.layout.activity_boss_mycomm);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);

		rl_progress.setVisibility(View.VISIBLE);
		cuid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.CUSERID, "");

		initView();

		// 数据后台加载
		new Thread() {
			@Override
			public void run() {
				list = GetData
						.getBossMyCommListInfos(MyConfig.URL_JSON_COMM_SUPPORT_BOSS
								+ cuid);
				if (!isDestroyed()) {
					handler.sendEmptyMessage(0);
				}
			}
		}.start();
	}

	private void initView() {
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

	private void initListView() {
		adapter = new BossCommInterestAdapter(this, list);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(BossMyCommActivity.this,
						BossCommDetailActivity.class);
				intent.putExtra("id", list.get(position).getId());
				startActivity(intent);
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
						&& totalItemCount > 0 && !isLoading) {
					isLoading = true;
					new Thread() {
						public void run() {
							List<CommunityBasicInfo> communityBasicInfos = GetData
									.getBossMyCommListInfos(MyConfig.URL_JSON_COMM_SUPPORT_BOSS
											+ cuid + "&page=" + page);
							if (communityBasicInfos != null
									&& communityBasicInfos.size() > 0) {
								for (int i = 0; i < communityBasicInfos.size(); i++) {
									list.add(communityBasicInfos.get(i));
								}
								handlerRefresh.sendEmptyMessage(0);
							}
						};
					}.start();
				}
			}
		});
	}

}
