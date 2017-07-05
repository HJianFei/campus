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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.adapters.CommunityTogetherDetailAdapter;
import com.loosoo100.campus100.beans.GiftTogetherFriendInfo;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;

/**
 * 
 * @author yang 社团活动凑集详情activity
 */
public class CommTogetherDetailActivity extends Activity implements
		OnClickListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view
	@ViewInject(R.id.rl_back)
	private RelativeLayout rl_back;
	@ViewInject(R.id.ll_noData)
	private LinearLayout ll_noData; // 没数据时的显示
	@ViewInject(R.id.listView)
	private ListView listView; // 列表
	@ViewInject(R.id.rl_progress)
	private RelativeLayout rl_progress; // 加载动画

	private int page = 1;

	private boolean isLoading = true;

	private List<GiftTogetherFriendInfo> list;

	private CommunityTogetherDetailAdapter adapter;

	private String aid = "";

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (list != null && list.size() > 0) {
				initListView();
				listView.setVisibility(View.VISIBLE);
				ll_noData.setVisibility(View.GONE);
			} else {
				listView.setVisibility(View.GONE);
				ll_noData.setVisibility(View.VISIBLE);
			}
			page = 2;
			isLoading = false;
			rl_progress.setVisibility(View.GONE);

		}
	};

	private Handler handlerRefresh = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (adapter != null) {
				adapter.notifyDataSetChanged();
				MyUtils.setListViewHeight(listView, 20);
			}
			isLoading = false;
			page++;
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community_together_detail);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);

		aid = getIntent().getExtras().getString("aid");

		rl_progress.setVisibility(View.VISIBLE);

		rl_back.setOnClickListener(this);

		// 数据后台加载
		new Thread() {

			@Override
			public void run() {
				list = GetData
						.getCommTogetherFriendInfos(MyConfig.URL_JSON_COMMUNITY_TOGETHER_DETAIL
								+ aid);
				if (!isDestroyed()) {
					handler.sendEmptyMessage(0);
				}
			}
		}.start();

	}


	private void initListView() {
		adapter = new CommunityTogetherDetailAdapter(this, list);
		listView.setAdapter(adapter);
		MyUtils.setListViewHeight(listView, 20);
		
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
							List<GiftTogetherFriendInfo> list2 = GetData
									.getCommTogetherFriendInfos(MyConfig.URL_JSON_COMMUNITY_TOGETHER_DETAIL
											+ aid + "&page=" + page);
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

}
