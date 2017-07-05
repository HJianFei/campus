package com.loosoo100.campus100.controller;

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
import com.loosoo100.campus100.activities.GiftGoodsDetailActivity;
import com.loosoo100.campus100.activities.MyCollectActivity;
import com.loosoo100.campus100.adapters.MyCollectAdapter;
import com.loosoo100.campus100.beans.MyCollectInfo;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;

/**
 * 我的收藏-礼物盒子
 * 
 * @author yang
 * 
 */
public class CollectGiftController extends BaseController {

	private LayoutInflater inflater;

	private MyCollectActivity activity;

	private ListView listView;
	private RelativeLayout progress;

	private MyCollectAdapter adapter;
	private List<MyCollectInfo> list;
	private String uid = "";
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

	public CollectGiftController(Context context) {
		super(context);

		activity = (MyCollectActivity) context;

	}

	@Override
	public void initData() {
		if (!first) {
			return;
		}
		first = false;
		listView = (ListView) mRootView.findViewById(R.id.listView);
		progress = (RelativeLayout) mRootView.findViewById(R.id.progress);

		uid = activity.getSharedPreferences(UserInfoDB.USERTABLE,
				activity.MODE_PRIVATE).getString(UserInfoDB.USERID, "");

		progress.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				list = GetData
						.getMyCollectInfos(MyConfig.URL_JSON_MYCOLLECT_GIFT
								+ uid);
				if (!activity.isDestroyed()) {
					handler.sendEmptyMessage(0);
				}
			};
		}.start();
		super.initData();
	}

	@Override
	protected View initView(Context context) {
		inflater = LayoutInflater.from(context);
		return inflater.inflate(R.layout.controller_collect_gift, null);
	}

	private void initListView() {
		adapter = new MyCollectAdapter(activity, list);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(activity,
						GiftGoodsDetailActivity.class);
				intent.putExtra("giftgoodsId", list.get(position).getId());
				activity.startActivity(intent);
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
							List<MyCollectInfo> list2 = GetData
									.getMyCollectInfos(MyConfig.URL_JSON_MYCOLLECT_GIFT
											+ uid + "&page=" + page);
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
						.getMyCollectInfos(MyConfig.URL_JSON_MYCOLLECT_GIFT
								+ uid);
				if (!activity.isDestroyed()) {
					handler.sendEmptyMessage(0);
				}
			};
		}.start();
	}

}
