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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.adapters.MoneyAdapter;
import com.loosoo100.campus100.beans.ConsumeInfo;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.spinkit.SpinKitView;
import com.loosoo100.campus100.view.spinkit.style.Wave;

/**
 * 
 * @author yang 余额activity
 */
public class MoneyActivity extends Activity implements OnClickListener {

	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view

	@ViewInject(R.id.rl_back)
	private View rl_back;

	@ViewInject(R.id.tv_money)
	private TextView tv_money; // 当前余额

	@ViewInject(R.id.btn_week)
	private Button btn_week; // 最近一周
	@ViewInject(R.id.btn_month)
	private Button btn_month; // 最近一月
	@ViewInject(R.id.btn_threeMonth)
	private Button btn_threeMonth; // 最近三月
	@ViewInject(R.id.rl_cashin)
	private RelativeLayout rl_cashin; // 充值
	@ViewInject(R.id.rl_cashout)
	private RelativeLayout rl_cashout; // 提现
	@ViewInject(R.id.ll_explain)
	private LinearLayout ll_explain; //余额说明
	@ViewInject(R.id.progress_small)
	private RelativeLayout progress_small; // 点击不同分类的加载进度条
	@ViewInject(R.id.progress)
	private SpinKitView progress; // 点击不同分类的加载进度条

	@ViewInject(R.id.lv_money)
	private ListView lv_money;

	private MoneyAdapter adapter;
	private List<ConsumeInfo> list;

	private String uid = "";
	private int page = 2;
	private int action = 1;

	private boolean isLoading = true;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (list != null && list.size() > 0) {
				initListView();
				lv_money.setVisibility(View.VISIBLE);
			} else {
				lv_money.setVisibility(View.GONE);
			}
			isLoading = false;
			page=2;
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
		setContentView(R.layout.activity_money);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);
		// 设置加载动画样式
		Wave wave = new Wave();
		progress.setIndeterminateDrawable(wave);
		progress_small.setVisibility(View.VISIBLE);

		uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.USERID, "");

		initView();

		new Thread() {
			public void run() {
				list = GetData
						.getConsumeInfos(MyConfig.URL_JSON_CONSUME_HISTORY
								+ action + "&uid=" + uid + "&page=1");
				if (!isDestroyed()) {
					handler.sendEmptyMessage(0);
				}
			};
		}.start();

	}

	private void initListView() {
		adapter = new MoneyAdapter(this, list);
		lv_money.setAdapter(adapter);
	}

	private void initView() {
		rl_back.setOnClickListener(this);
		btn_week.setOnClickListener(this);
		btn_month.setOnClickListener(this);
		btn_threeMonth.setOnClickListener(this);
		rl_cashout.setOnClickListener(this);
		rl_cashin.setOnClickListener(this);
		ll_explain.setOnClickListener(this);

		// 当前余额
//		tv_money.setText(getSharedPreferences(UserInfoDB.USERTABLE,
//				MODE_PRIVATE).getString(UserInfoDB.MONEY, "0"));

		lv_money.setOnScrollListener(new OnScrollListener() {

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
							List<ConsumeInfo> list2 = GetData
									.getConsumeInfos(MyConfig.URL_JSON_CONSUME_HISTORY
											+ action
											+ "&uid="
											+ uid
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
			Intent moneyIntent = new Intent(this, MoneyInstructionActivity.class);
			startActivity(moneyIntent);
			break;

		case R.id.rl_cashout:
			Intent outIntent = new Intent(this, CashOutActivity.class);
			startActivity(outIntent);
			break;

		case R.id.rl_cashin:
			Intent inIntent = new Intent(this, CashInActivity.class);
			startActivity(inIntent);
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
							.getConsumeInfos(MyConfig.URL_JSON_CONSUME_HISTORY
									+ action + "&uid=" + uid + "&page=1");
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
							.getConsumeInfos(MyConfig.URL_JSON_CONSUME_HISTORY
									+ action + "&uid=" + uid + "&page=1");
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
							.getConsumeInfos(MyConfig.URL_JSON_CONSUME_HISTORY
									+ action + "&uid=" + uid + "&page=1");
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

	@Override
	protected void onResume() {
		// 当前余额
		tv_money.setText(getSharedPreferences(UserInfoDB.USERTABLE,
				MODE_PRIVATE).getString(UserInfoDB.MONEY, "0"));
		super.onResume();
	}

}
