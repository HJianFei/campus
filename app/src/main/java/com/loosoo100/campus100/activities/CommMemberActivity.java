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
import com.loosoo100.campus100.adapters.CommMemberAdapter;
import com.loosoo100.campus100.beans.CommunityMemberInfo;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;

/**
 * 
 * @author yang 社团成员
 */
public class CommMemberActivity extends Activity implements OnClickListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view
	@ViewInject(R.id.rl_back)
	private RelativeLayout rl_back; // 返回
	@ViewInject(R.id.listView)
	private ListView listView; // 列表
	@ViewInject(R.id.progress)
	private RelativeLayout progress; // 加载动画

	private String cid = "";
	private String uid = "";
	private boolean isLoading = true;
	private boolean showName = false;	//成员列表是否显示真实姓名
	private int page = 1;

	private CommMemberAdapter adapter;
	private List<CommunityMemberInfo> memberList;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (memberList != null && memberList.size() > 0) {
				initListView();
				listView.setVisibility(View.VISIBLE);
			} else {
				listView.setVisibility(View.GONE);
			}
			page = 2;
			isLoading = false;
			progress.setVisibility(View.GONE);
		}
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_boss_comm_member);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);

		cid = getIntent().getExtras().getString("cid");
		showName = getIntent().getExtras().getBoolean("showName");
		uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.USERID, "");

		initView();

		progress.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				memberList = GetData
						.getBossCommMemberInfos(MyConfig.URL_JSON_COMM_MEMBER_BOSS
								+ cid);
				if (!isDestroyed()) {
					handler.sendEmptyMessage(0);
				}
			};
		}.start();

	}

	private void initListView() {
		adapter = new CommMemberAdapter(this, memberList,showName);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (uid.equals(memberList.get(position).getUserId())) {
					Intent intent = new Intent(CommMemberActivity.this,
							CampusContactsPersonalActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(CommMemberActivity.this,
							CampusContactsFriendActivity.class);
					intent.putExtra("muid", memberList.get(position)
							.getUserId());
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
							List<CommunityMemberInfo> list2 = GetData
									.getBossCommMemberInfos(MyConfig.URL_JSON_COMM_MEMBER_BOSS
											+ cid + "&page=" + page);
							if (list2 != null && list2.size() > 0) {
								for (int i = 0; i < list2.size(); i++) {
									memberList.add(list2.get(i));
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

	private void initView() {
		rl_back.setOnClickListener(this);
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
