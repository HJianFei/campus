package com.loosoo100.campus100.activities;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.adapters.CommunityMemberCheckedAdapter;
import com.loosoo100.campus100.anyevent.MEventComCheck;
import com.loosoo100.campus100.beans.CommunityMemberInfo;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;

import de.greenrobot.event.EventBus;

/**
 * 
 * @author yang 社团成员审核activity
 */
public class CommunityMemberCheckedActivity extends Activity implements
		OnClickListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view
	@ViewInject(R.id.rl_back)
	private RelativeLayout rl_back; // 返回
	@ViewInject(R.id.listView)
	private ListView listView; // 列表
	@ViewInject(R.id.progress)
	private RelativeLayout progress; // 加载动画
	@ViewInject(R.id.progress_update)
	private RelativeLayout progress_update; // 加载动画

	private String cid = "";
	private String uid = "";
	private String shopid = "";
	private List<CommunityMemberInfo> auditingMemberInfos;

	private CommunityMemberCheckedAdapter adapter;

	private boolean isChange = false;
	private boolean isLoading = true;
	private int page = 1;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (auditingMemberInfos != null && auditingMemberInfos.size() > 0) {
				initListView();
				listView.setVisibility(View.VISIBLE);
			} else {
				listView.setVisibility(View.GONE);
			}
			page=2;
			isLoading=false;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community_member_checked);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);
		EventBus.getDefault().register(this);

		cid = getIntent().getExtras().getString("cid");
		uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.USERID, "");
		shopid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.SCHOOL_ID, "");

		isChange = false;

		rl_back.setOnClickListener(this);
		progress.setVisibility(View.VISIBLE);

		new Thread() {
			public void run() {
				auditingMemberInfos = GetData
						.getCommunityAuditingMemberInfos(MyConfig.URL_JSON_COMMUNITY_AUDITING_MEMBER
								+ cid + "&shopid=" + shopid);
				if (!isDestroyed()) {
					handler.sendEmptyMessage(0);
				}
			};
		}.start();

	}

	private void initListView() {
		adapter = new CommunityMemberCheckedAdapter(this, auditingMemberInfos,
				cid, shopid);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (uid.equals(auditingMemberInfos.get(position).getUserId())) {
					Intent intent = new Intent(
							CommunityMemberCheckedActivity.this,
							CampusContactsPersonalActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(
							CommunityMemberCheckedActivity.this,
							CampusContactsFriendActivity.class);
					intent.putExtra("muid", auditingMemberInfos.get(position)
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
									.getCommunityAuditingMemberInfos(MyConfig.URL_JSON_COMMUNITY_AUDITING_MEMBER
											+ cid
											+ "&shopid="
											+ shopid
											+ "&page=" + page);
							if (list2 != null && list2.size() > 0) {
								for (int i = 0; i < list2.size(); i++) {
									auditingMemberInfos.add(list2.get(i));
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

	public void onEventMainThread(MEventComCheck event) {
		if (event.getPosition() == -1) {
			progress_update.setVisibility(View.VISIBLE);
		} else if (event.getPosition() == -2) {
			progress_update.setVisibility(View.GONE);
		} else {
			auditingMemberInfos.remove(event.getPosition());
			adapter.notifyDataSetChanged();
			if (auditingMemberInfos.size() == 0) {
				listView.setVisibility(View.GONE);
			}
			isChange = true;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			if (isChange) {
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				this.finish();
			} else {
				this.finish();
			}
			break;

		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isChange) {
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				this.finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}
}
