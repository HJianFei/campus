package com.loosoo100.campus100.zzboss.activities;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.activities.PointInstructionActivity;
import com.loosoo100.campus100.activities.YetOpenActivity;
import com.loosoo100.campus100.adapters.PointAdapter;
import com.loosoo100.campus100.beans.PointInfo;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.spinkit.SpinKitView;
import com.loosoo100.campus100.view.spinkit.style.Wave;

/**
 * 
 * @author yang 积分activity
 */
public class BossPointActivity extends Activity implements OnClickListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view

	@ViewInject(R.id.rl_back)
	private View rl_back;

	@ViewInject(R.id.tv_point)
	private TextView tv_point; // 积分

	@ViewInject(R.id.btn_week)
	private Button btn_week; // 最近一周
	@ViewInject(R.id.btn_month)
	private Button btn_month; // 最近一月
	@ViewInject(R.id.btn_threeMonth)
	private Button btn_threeMonth; // 最近三月

	@ViewInject(R.id.ll_explain)
	private LinearLayout ll_explain; // 积分说明
	@ViewInject(R.id.btn_pointExchange)
	private Button btn_pointExchange; // 积分兑换
	@ViewInject(R.id.lv_point)
	private ListView lv_point; // 积分列表

	@ViewInject(R.id.progress_small)
	private RelativeLayout progress_small; // 点击不同分类的加载进度条
	@ViewInject(R.id.progress)
	private SpinKitView progress; // 点击不同分类的加载进度条

	private PointAdapter adapter;
	private List<PointInfo> list;

	private String cuid = "";
	private int page = 2;
	private int action = 1;

	private boolean isLoading = true;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (list != null && list.size() > 0) {
				initListView();
				lv_point.setVisibility(View.VISIBLE);
			} else {
				lv_point.setVisibility(View.GONE);
			}
			page=2;
			isLoading = false;
			progress_small.setVisibility(View.GONE);
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
		setContentView(R.layout.activity_point);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);
		// 设置加载动画样式
		Wave wave = new Wave();
		progress.setIndeterminateDrawable(wave);
		progress_small.setVisibility(View.VISIBLE);

		cuid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.CUSERID, "");

		initView();

		new Thread() {
			public void run() {
				list = GetData.getBossPointInfos(MyConfig.URL_JSON_POINT_HISTORY_BOSS
						+ action + "&uid=" + cuid + "&page=1");
				if (!isDestroyed()) {
					handler.sendEmptyMessage(0);
				}
			};
		}.start();

	}

	private void initListView() {
		adapter = new PointAdapter(this, list);
		lv_point.setAdapter(adapter);
	}

	private void initView() {
		rl_back.setOnClickListener(this);
		btn_week.setOnClickListener(this);
		btn_month.setOnClickListener(this);
		btn_threeMonth.setOnClickListener(this);
		ll_explain.setOnClickListener(this);
		btn_pointExchange.setOnClickListener(this);
		// 当前积分
		tv_point.setText(getSharedPreferences(UserInfoDB.USERTABLE,
				MODE_PRIVATE).getString(UserInfoDB.POINT, "0"));

		lv_point.setOnScrollListener(new OnScrollListener() {

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
						List<PointInfo> list2 = null;

						public void run() {
							list2 = GetData
									.getBossPointInfos(MyConfig.URL_JSON_POINT_HISTORY_BOSS
											+ action
											+ "&uid="
											+ cuid
											+ "&page="
											+ page);
							if (list2 != null&&list2.size()>0) {
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
			
		case R.id.ll_explain:
			Intent pointIntent = new Intent(this, PointInstructionActivity.class);
			startActivity(pointIntent);
			break;
			
		case R.id.btn_pointExchange:
			Intent exchangeIntent = new Intent(this, YetOpenActivity.class);
			startActivity(exchangeIntent);
			break;

		case R.id.btn_week:
			if (action == 1) {
				return;
			}
			progress_small.setVisibility(View.VISIBLE);
			action = 1;
			page = 2;
			resetBGcolor();
			btn_week.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.shape_red_left_gray));
			btn_week.setTextColor(getResources().getColor(R.color.white));
			new Thread() {
				public void run() {
					list = GetData
							.getBossPointInfos(MyConfig.URL_JSON_POINT_HISTORY_BOSS
									+ action + "&uid=" + cuid + "&page=1");
					if (!isDestroyed()) {
						handler.sendEmptyMessage(0);
					}
				};
			}.start();
			break;

		case R.id.btn_month:
			if (action == 2) {
				return;
			}
			progress_small.setVisibility(View.VISIBLE);
			action = 2;
			page = 2;
			resetBGcolor();
			btn_month.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.shape_red_middle_gray));
			btn_month.setTextColor(getResources().getColor(R.color.white));
			new Thread() {
				public void run() {
					list = GetData
							.getBossPointInfos(MyConfig.URL_JSON_POINT_HISTORY_BOSS
									+ action + "&uid=" + cuid + "&page=1");
					if (!isDestroyed()) {
						handler.sendEmptyMessage(0);
					}
				};
			}.start();
			break;

		case R.id.btn_threeMonth:
			if (action == 3) {
				return;
			}
			progress_small.setVisibility(View.VISIBLE);
			action = 3;
			page = 2;
			resetBGcolor();
			btn_threeMonth.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.shape_red_right_gray));
			btn_threeMonth.setTextColor(getResources().getColor(R.color.white));
			new Thread() {
				public void run() {
					list = GetData
							.getBossPointInfos(MyConfig.URL_JSON_POINT_HISTORY_BOSS
									+ action + "&uid=" + cuid + "&page=1");
					if (!isDestroyed()) {
						handler.sendEmptyMessage(0);
					}
				};
			}.start();
			break;

		}
	}

	/**
	 * 还原三个按钮背景色
	 */
	public void resetBGcolor() {
		btn_week.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.shape_white_left_gray));
		btn_week.setTextColor(getResources().getColor(R.color.black));
		btn_month.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.shape_white_middle_gray));
		btn_month.setTextColor(getResources().getColor(R.color.black));
		btn_threeMonth.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.shape_white_right_gray));
		btn_threeMonth.setTextColor(getResources().getColor(R.color.black));
	}

}
