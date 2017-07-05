package com.loosoo100.campus100.activities;

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
import com.loosoo100.campus100.anyevent.MEventCommActiPay;
import com.loosoo100.campus100.beans.CommunityActivityInfo;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;

import de.greenrobot.event.EventBus;

/**
 * 
 * @author yang 社团我支持的活动activity
 */
public class MyActivityActivity extends Activity implements OnClickListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view
	@ViewInject(R.id.rl_back)
	private RelativeLayout rl_back;
	@ViewInject(R.id.listView)
	private ListView listView; // 列表
	@ViewInject(R.id.rl_progress)
	private RelativeLayout rl_progress; // 加载动画
	private String uid = "";

	private CommActiListAdapter adapter;
	private List<CommunityActivityInfo> list;

	private int page = 1;
	private boolean isLoading = true;

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
		setContentView(R.layout.activity_myactivity);
		ViewUtils.inject(this);

		EventBus.getDefault().register(this);

		MyUtils.setStatusBarHeight(this, iv_empty);

		rl_progress.setVisibility(View.VISIBLE);
		rl_back.setOnClickListener(this);
		uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.USERID, "");

		new Thread() {
			public void run() {
				list = GetData.getActivityInfos(MyConfig.URL_JSON_MYACTIVITY
						+ uid);
				if (!isDestroyed()) {
					handler.sendEmptyMessage(0);
				}
			};
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
					Intent intent = new Intent(MyActivityActivity.this,
							CommActivityDetailTogetherActivity.class);
					intent.putExtra("aid", list.get(position).getId());
					startActivity(intent);
				} else {
					Intent intent = new Intent(MyActivityActivity.this,
							CommActivityDetailFreeActivity.class);
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
						&& totalItemCount > 0 && !isLoading) {
					isLoading = true;
					new Thread() {
						public void run() {
							List<CommunityActivityInfo> list2 = GetData
									.getActivityInfos(MyConfig.URL_JSON_MYACTIVITY
											+ uid + "&p=" + page);
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

	/**
	 * 活动支付更新活动数据
	 * 
	 * @param event
	 */
	public void onEventMainThread(MEventCommActiPay event) {
		if (event.isChange()) {
			new Thread() {
				public void run() {
					list = GetData
							.getActivityInfos(MyConfig.URL_JSON_MYACTIVITY
									+ uid);
					if (!isDestroyed()) {
						handler.sendEmptyMessage(0);
					}
				};
			}.start();
		}
	}

	@Override
	public void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

}
