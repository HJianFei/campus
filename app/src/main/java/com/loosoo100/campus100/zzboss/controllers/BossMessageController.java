package com.loosoo100.campus100.zzboss.controllers;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.loosoo100.campus100.R;
import com.loosoo100.campus100.activities.CommActivityDetailFreeActivity;
import com.loosoo100.campus100.activities.CommActivityDetailTogetherActivity;
import com.loosoo100.campus100.adapters.CommActiListAdapter;
import com.loosoo100.campus100.beans.CommunityActivityInfo;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.controller.BaseController;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.zzboss.activities.BossHomeActivity;

import java.util.List;

/**
 * 消息-消息
 * 
 * @author yang
 * 
 */
public class BossMessageController extends BaseController implements
		OnClickListener {

	private LayoutInflater inflater;

	private BossHomeActivity activity;

	private EditText et_search;
	private ImageView iv_search;
	private ListView lv_message;

	private CommActiListAdapter adapter;
	private List<CommunityActivityInfo> list;
	private String cuid = "";
	private boolean isLoading = true;
	private int page = 1;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (list != null && list.size() > 0) {
				initListView();
				lv_message.setVisibility(View.VISIBLE);
			} else {
				lv_message.setVisibility(View.GONE);
			}
			page = 2;
			isLoading = false;
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

	public BossMessageController(Context context) {
		super(context);

		activity = (BossHomeActivity) context;
		init();

		cuid = activity.getSharedPreferences(UserInfoDB.USERTABLE,
				activity.MODE_PRIVATE).getString(UserInfoDB.CUSERID, "");

//		new Thread() {
//			public void run() {
//				list = GetData
//						.getBossHomeActivityInfos(MyConfig.URL_JSON_ACTI_MYCOLLECT_BOSS
//								+ cuid);
//				if (!activity.isDestroyed()) {
//					handler.sendEmptyMessage(0);
//				}
//			};
//		}.start();
	}

	@Override
	protected View initView(Context context) {
		inflater = LayoutInflater.from(context);
		return inflater.inflate(R.layout.controller_boss_message_message, null);
	}

	public void init() {
		et_search = (EditText) mRootView.findViewById(R.id.et_search);
		iv_search = (ImageView) mRootView.findViewById(R.id.iv_search);
		lv_message = (ListView) mRootView.findViewById(R.id.lv_message);

		iv_search.setOnClickListener(this);
	}

	private void initListView() {
		adapter = new CommActiListAdapter(activity, list);
		lv_message.setAdapter(adapter);

		lv_message.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (list.get(position).getClassify()==0) {
					Intent intent = new Intent(activity,
							CommActivityDetailTogetherActivity.class);
					intent.putExtra("aid", list.get(position).getId());
					activity.startActivity(intent);
				} else {
					Intent intent = new Intent(activity,
							CommActivityDetailFreeActivity.class);
					intent.putExtra("aid", list.get(position).getId());
					activity.startActivity(intent);
				}
			}
		});

		lv_message.setOnScrollListener(new OnScrollListener() {

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
									.getBossHomeActivityInfos(MyConfig.URL_JSON_ACTI_MYCOLLECT_BOSS
											+ cuid + "&page=" + page);
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_search:
			// TODO
			break;

		}
	}

}
