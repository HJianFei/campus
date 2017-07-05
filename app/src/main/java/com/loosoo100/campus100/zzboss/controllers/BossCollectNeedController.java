package com.loosoo100.campus100.zzboss.controllers;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.loosoo100.campus100.R;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.controller.BaseController;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.zzboss.activities.BossCommDemandBoothDetailActivity;
import com.loosoo100.campus100.zzboss.activities.BossCommDemandMoneyDetailActivity;
import com.loosoo100.campus100.zzboss.activities.BossMyCollectActivity;
import com.loosoo100.campus100.zzboss.adapter.BossCommSupportAdapter;
import com.loosoo100.campus100.zzboss.beans.BossCommSupportInfo;

/**
 * 我的收藏-需求
 * 
 * @author yang
 * 
 */
public class BossCollectNeedController extends BaseController {

	private LayoutInflater inflater;

	private BossMyCollectActivity activity;

	private ListView listView;
	private RelativeLayout progress;

	private BossCommSupportAdapter adapter;
	private List<BossCommSupportInfo> list;
	private String cuid = "";
	private boolean isLoading = true;
	private int page = 1;
	private boolean first = true;

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
			progress.setVisibility(View.GONE);
		};
	};

	private Handler handlerRefresh = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (adapter != null) {
				adapter.notifyDataSetChanged();
			}
			isLoading = false;
			page++;
		};
	};

	public BossCollectNeedController(Context context) {
		super(context);

		activity = (BossMyCollectActivity) context;

	}

	@Override
	protected View initView(Context context) {
		inflater = LayoutInflater.from(context);
		return inflater.inflate(R.layout.controller_boss_collect_need, null);
	}

	@Override
	public void initData() {
		if (!first) {
			return;
		}
		first = false;
		listView = (ListView) mRootView.findViewById(R.id.listView);
		progress = (RelativeLayout) mRootView.findViewById(R.id.progress);
		cuid = activity.getSharedPreferences(UserInfoDB.USERTABLE,
				activity.MODE_PRIVATE).getString(UserInfoDB.CUSERID, "");

		progress.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				list = GetData
						.getBossCollectNeedInfos(MyConfig.URL_JSON_ACTI_MYCOLLECT_BOSS
								+ cuid + "&cart=1");
				if (!activity.isDestroyed()) {
					handler.sendEmptyMessage(0);
				}
			};
		}.start();
		super.initData();
	}

	private void initListView() {
		adapter = new BossCommSupportAdapter(activity, list);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (list.get(position).getClassify() == 0) {
					Intent intent = new Intent(activity,
							BossCommDemandMoneyDetailActivity.class);
					intent.putExtra("did", list.get(position).getId());
					activity.startActivity(intent);
				} else {
					Intent intent = new Intent(activity,
							BossCommDemandBoothDetailActivity.class);
					intent.putExtra("did", list.get(position).getId());
					activity.startActivity(intent);
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
							List<BossCommSupportInfo> list2 = GetData
									.getBossCollectNeedInfos(MyConfig.URL_JSON_ACTI_MYCOLLECT_BOSS
											+ cuid
											+ "&cart=1"
											+ "&page="
											+ page);
							if (list2 != null && list2.size() > 0) {
								for (int i = 0; i < list2.size(); i++) {
									list.add(list2.get(i));
								}
								if (!activity.isDestroyed()) {
									handlerRefresh.sendEmptyMessage(0);
								}
							}
						};
					}.start();
				}
			}
		});
	}

	public void updateData() {
		new Thread() {
			public void run() {
				list = GetData
						.getBossCollectNeedInfos(MyConfig.URL_JSON_ACTI_MYCOLLECT_BOSS
								+ cuid + "&cart=1");
				if (!activity.isDestroyed()) {
					handler.sendEmptyMessage(0);
				}
			};
		}.start();
	}
}
