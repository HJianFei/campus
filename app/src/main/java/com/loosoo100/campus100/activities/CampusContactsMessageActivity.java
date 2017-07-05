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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.adapters.CampusContactsMessageAdapter;
import com.loosoo100.campus100.beans.CampusContactsMessageInfo;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;

/**
 * 
 * @author yang 校园圈消息activity
 */
public class CampusContactsMessageActivity extends Activity implements
		OnClickListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view
	@ViewInject(R.id.rl_back)
	private RelativeLayout rl_back; // 返回
	@ViewInject(R.id.lv_message)
	private ListView lv_message;
	@ViewInject(R.id.btn_more)
	private Button btn_more; // 查看更多消息
	@ViewInject(R.id.tv_nodata)
	private TextView tv_nodata;
	@ViewInject(R.id.progress_custom)
	private RelativeLayout progress_custom;
	@ViewInject(R.id.progress_update_whitebg)
	private RelativeLayout progress_update_whitebg;
	@ViewInject(R.id.iv_lineup)
	private ImageView iv_lineup;
	@ViewInject(R.id.iv_linedown)
	private ImageView iv_linedown;

	private CampusContactsMessageAdapter adapter;
	private List<CampusContactsMessageInfo> list;
	private boolean isLoading = true;

	private int page;
	private String uid = "";
	private long campusContactsTime;

	private int status = 1; // status为1时表示没读的内容，status为0时表示已读的内容

	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			if (list != null && list.size() > 0) {
				initListView();
				lv_message.setVisibility(View.VISIBLE);
				tv_nodata.setVisibility(View.GONE);
				iv_linedown.setVisibility(View.GONE);
			} else {
				lv_message.setVisibility(View.GONE);
				tv_nodata.setVisibility(View.VISIBLE);
				iv_lineup.setVisibility(View.GONE);
			}
			page = 2;
			isLoading = false;
			progress_custom.setVisibility(View.GONE);
		};
	};

	private Handler handler2 = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (adapter != null) {
				adapter.notifyDataSetChanged();
				MyUtils.setListViewHeight(lv_message, 10);
			}
			isLoading = false;
			page++;
		};
	};

	private Handler handler3 = new Handler() {
		public void handleMessage(android.os.Message msg) {
			btn_more.setClickable(true);
			btn_more.setVisibility(View.GONE);
			if (list != null && list.size() > 0) {
				initListView();
				lv_message.setVisibility(View.VISIBLE);
				tv_nodata.setVisibility(View.GONE);
			} else {
				lv_message.setVisibility(View.GONE);
				tv_nodata.setText("暂无消息");
				tv_nodata.setVisibility(View.VISIBLE);
			}
			page = 2;
			isLoading = false;
			progress_custom.setVisibility(View.GONE);
			progress_update_whitebg.setVisibility(View.GONE);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_campus_contacts_message);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);
		// 更改状态栏字体颜色为黑色
		MyUtils.setMiuiStatusBarDarkMode(this, true);

		uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.USERID, "");

		rl_back.setOnClickListener(this);
		btn_more.setOnClickListener(this);

		progress_custom.setVisibility(View.VISIBLE);

		new Thread() {

			public void run() {
				list = GetData
						.getCampusContactsMessageInfos(MyConfig.URL_JSON_CAMPUS_MESSAGE
								+ uid + "&status=" + status);
				campusContactsTime = GetData
						.getCampusContactsMessageTime(MyConfig.URL_JSON_CAMPUS_MESSAGE
								+ uid);
				if (!isDestroyed()) {
					handler.sendEmptyMessage(0);
				}
			};
		}.start();

	}

	private void initListView() {
		adapter = new CampusContactsMessageAdapter(this, list,
				campusContactsTime);
		lv_message.setAdapter(adapter);
		MyUtils.setListViewHeight(lv_message, 10);

		lv_message.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.setClass(CampusContactsMessageActivity.this,
						CampusContactsDetailPersonalActivity.class);
				intent.putExtra("mid", list.get(position).getMid());
//				intent.putExtra("index", -1);
				startActivity(intent);
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
							List<CampusContactsMessageInfo> list2 = GetData
									.getCampusContactsMessageInfos(MyConfig.URL_JSON_CAMPUS_MESSAGE
											+ uid
											+ "&status="
											+ status
											+ "&page=" + page);
							if (list2 != null && list2.size() > 0) {
								for (int i = 0; i < list2.size(); i++) {
									list.add(list2.get(i));
								}
								handler2.sendEmptyMessage(0);
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

		case R.id.btn_more:
			status = 0;
			isLoading = true;
			btn_more.setClickable(false);
			progress_update_whitebg.setVisibility(View.VISIBLE);
			new Thread() {
				public void run() {
					// status为1时表示没读的内容，status为0时表示已读的内容
					list = GetData
							.getCampusContactsMessageInfos(MyConfig.URL_JSON_CAMPUS_MESSAGE
									+ uid + "&status=" + status);
					campusContactsTime = GetData
							.getCampusContactsMessageTime(MyConfig.URL_JSON_CAMPUS_MESSAGE
									+ uid);
					if (!isDestroyed()) {
						handler3.sendEmptyMessage(0);
					}
				};
			}.start();
			break;

		}
	}

}
