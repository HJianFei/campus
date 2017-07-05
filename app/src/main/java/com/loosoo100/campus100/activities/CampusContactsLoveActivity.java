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
import com.loosoo100.campus100.adapters.CampusContactsLoveAdapter;
import com.loosoo100.campus100.anyevent.MEventCampusContactsAllDel;
import com.loosoo100.campus100.anyevent.MEventCampusContactsLove;
import com.loosoo100.campus100.anyevent.MEventCampusNoRead;
import com.loosoo100.campus100.beans.CampusContactsLoveInfo;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;

import de.greenrobot.event.EventBus;

/**
 * 
 * @author yang 暗恋的人activity
 */
public class CampusContactsLoveActivity extends Activity implements
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

	private String sid = ""; // 学校ID
	private String uid = ""; // 用户ID

	private CampusContactsLoveAdapter adapter;

	private List<CampusContactsLoveInfo> list;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (list != null && list.size() > 0) {
				// UserInfoDB.setUserInfo(CampusContactsLoveActivity.this,
				// UserInfoDB.CAMPUS_NOREAD_LOVE, "0");
				// EventBus.getDefault().post(new MEventCampusNoRead(true));
				initListView();
				listView.setVisibility(View.VISIBLE);
			} else {
				listView.setVisibility(View.GONE);
			}
			new Thread() {
				public void run() {
					int count = GetData
							.getCampusContactsNoRead(MyConfig.URL_JSON_CAMPUS_NOREAD
									+ uid);
					UserInfoDB.setUserInfo(CampusContactsLoveActivity.this,
							UserInfoDB.CAMPUS_NOREAD_LOVE, count + "");
//					UserInfoDB.setUserInfo(CampusContactsLoveActivity.this,
//							UserInfoDB.CAMPUS_NOREAD_HOME_LOVE, count + "");
					EventBus.getDefault().post(new MEventCampusNoRead(true));
				};
			}.start();
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
		setContentView(R.layout.activity_campus_contacts_love);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);
		MyUtils.setMiuiStatusBarDarkMode(this, true);
		EventBus.getDefault().register(this);

		uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.USERID, "");

		rl_progress.setVisibility(View.VISIBLE);

		initView();

		// 数据后台加载
		new Thread() {

			@Override
			public void run() {
				if (!isDestroyed()) {
					list = GetData
							.getcCampusContactsLoveInfos(MyConfig.URL_JSON_CAMPUS_LOVE
									+ uid);
					if (!isDestroyed()) {
						handler.sendEmptyMessage(0);
					}
				}

			}
		}.start();
	}

	private void initListView() {
		adapter = new CampusContactsLoveAdapter(this, list);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(CampusContactsLoveActivity.this,
						CampusContactsFriendActivity.class);
				intent.putExtra("muid", list.get(position).getUid());
				startActivity(intent);
			}
		});

	}

	private void initView() {
		rl_back.setOnClickListener(this);
		listView.setOnScrollListener(this);

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
					List<CampusContactsLoveInfo> list2 = GetData
							.getcCampusContactsLoveInfos(MyConfig.URL_JSON_CAMPUS_LOVE
									+ uid + "&page=" + page);
					if (list2 != null && list2.size() > 0) {
						for (int i = 0; i < list2.size(); i++) {
							list.add(list2.get(i));
						}
						handlerRefresh.sendEmptyMessage(0);
					}
				};
			}.start();
		}
	}

	public void onEventMainThread(MEventCampusContactsLove event) {
		if (event.isChange()) {
			new Thread() {
				@Override
				public void run() {
					if (!isDestroyed()) {
						list = GetData
								.getcCampusContactsLoveInfos(MyConfig.URL_JSON_CAMPUS_LOVE
										+ uid);
						if (!isDestroyed()) {
							handler.sendEmptyMessage(0);
						}
					}

				}
			}.start();
		}
	}

	public void onEventMainThread(MEventCampusContactsAllDel event) {
		if (event.isChange()) {
			this.finish();
		}
	}

	@Override
	protected void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

}
