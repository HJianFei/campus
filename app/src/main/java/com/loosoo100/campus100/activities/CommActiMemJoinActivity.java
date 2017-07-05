package com.loosoo100.campus100.activities;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.adapters.CommActiMemJoinAdapter;
import com.loosoo100.campus100.beans.UserInfo;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;

/**
 * 
 * @author yang 活动报名人员activity
 */
public class CommActiMemJoinActivity extends Activity implements
		OnClickListener {
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

	private String aid = ""; // 活动ID

	private CommActiMemJoinAdapter adapter;
	private List<UserInfo> list;

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
		setContentView(R.layout.activity_comm_acti_mem_join);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);

		rl_progress.setVisibility(View.VISIBLE);
		rl_back.setOnClickListener(this);

		aid = getIntent().getExtras().getString("aid");

		new Thread() {
			@Override
			public void run() {
				list = GetData
						.getCommMemJoinInfos(MyConfig.URL_JSON_ACTI_MEM_JOIN
								+ aid + "&page=" + page);
				if (!isDestroyed()) {
					handler.sendEmptyMessage(0);
				}

			}
		}.start();
	}

	private void initListView() {
		adapter = new CommActiMemJoinAdapter(this, list);
		listView.setAdapter(adapter);

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
							List<UserInfo> userInfos = GetData
									.getCommMemJoinInfos(MyConfig.URL_JSON_ACTI_MEM_JOIN
											+ aid + "&page=" + page);
							if (userInfos != null && userInfos.size() > 0) {
								for (int i = 0; i < userInfos.size(); i++) {
									list.add(userInfos.get(i));
								}
								handlerRefresh.sendEmptyMessage(0);
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

}
