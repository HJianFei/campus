package com.loosoo100.campus100.zzboss.activities;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
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

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.activities.CommSearchActivity;
import com.loosoo100.campus100.beans.CampusInfo;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyAnimation;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.zzboss.adapter.BossCommSupportAdapter;
import com.loosoo100.campus100.zzboss.adapter.BossCommTimeSearchAdapter;
import com.loosoo100.campus100.zzboss.adapter.BossCommTypeSearchAdapter;
import com.loosoo100.campus100.zzboss.beans.BossCommSupportInfo;

/**
 * 
 * @author yang 赞助社团activity
 */
public class BossCommSupportActivity extends Activity implements
		OnClickListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view
	@ViewInject(R.id.rl_search_back)
	private RelativeLayout rl_search_back; // 返回
	@ViewInject(R.id.rl_search)
	private RelativeLayout rl_search; // 搜索按钮
	@ViewInject(R.id.btn_type)
	private Button btn_type;
	@ViewInject(R.id.btn_time)
	private Button btn_time;
	@ViewInject(R.id.listview)
	private ListView listview;
	@ViewInject(R.id.lv_type)
	private ListView lv_type;
	@ViewInject(R.id.lv_time)
	private ListView lv_time;
	@ViewInject(R.id.progress)
	private RelativeLayout progress;
	@ViewInject(R.id.progress_update_whitebg)
	private RelativeLayout progress_update_whitebg;

	private List<BossCommSupportInfo> list;
	private List<BossCommSupportInfo> list2;

	private List<CampusInfo> listType = new ArrayList<>();
	private List<String> listTime = new ArrayList<>();
	private BossCommTypeSearchAdapter adapterType;
	private BossCommTimeSearchAdapter adapterTime;
	private BossCommSupportAdapter commAdapter;

	private String cartId = "0";
	private String timeId = "0";
	private String cityId = "0";
	private String content = "";
	private String content_encoded = "";

	private int page = 1;
	private boolean isLoading = true;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			initTimeListView();
			if (listType != null && listType.size() > 0) {
				CampusInfo campusInfo = new CampusInfo();
				campusInfo.setCampusID("0");
				campusInfo.setCampusName("全类型");
				listType.add(0, campusInfo);
				initTypeListView();
			}
			if (list != null && list.size() > 0) {
				initNeedListView();
				listview.setVisibility(View.VISIBLE);
			} else {
				listview.setVisibility(View.GONE);
			}
			page = 2;
			isLoading = false;
			progress.setVisibility(View.GONE);
		};
	};

	private Handler handler2 = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (list != null && list.size() > 0) {
				initNeedListView();
				listview.setVisibility(View.VISIBLE);
			} else {
				listview.setVisibility(View.GONE);
			}
			page = 2;
			isLoading = false;
			progress.setVisibility(View.GONE);
			progress_update_whitebg.setVisibility(View.GONE);
		};
	};

	private Handler handlerRefresh = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (list2 != null && list2.size() > 0) {
				for (int i = 0; i < list2.size(); i++) {
					list.add(list2.get(i));
				}
				list2.clear();
				if (commAdapter != null) {
					commAdapter.notifyDataSetChanged();
				}
				page++;
				isLoading = false;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_boss_comm_support);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);

		listTime.add("全时段");
		listTime.add("当天");
		listTime.add("本周");
		listTime.add("上周");
		listTime.add("本月");

		initView();

		progress.setVisibility(View.VISIBLE);

		new Thread() {
			@Override
			public void run() {
				listType = GetData
						.getBossActiTypeInfos(MyConfig.URL_JSON_NEED_BOSS);
				list = GetData.getBossNeedsInfos(MyConfig.URL_JSON_NEED_BOSS
						+ "?cartid=" + cartId + "&time=" + timeId + "&cityid="
						+ cityId + "&name=" + content_encoded);
				if (!isDestroyed()) {
					handler.sendEmptyMessage(0);
				}
			}
		}.start();
	}

	private void initTypeListView() {
		adapterType = new BossCommTypeSearchAdapter(this, listType);
		lv_type.setAdapter(adapterType);
		lv_type.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				cartId = listType.get(position).getCampusID();
				btn_type.setText(listType.get(position).getCampusName());
				lv_type.setVisibility(View.GONE);

				progress_update_whitebg.setVisibility(View.VISIBLE);
				new Thread() {
					public void run() {
						list = GetData
								.getBossNeedsInfos(MyConfig.URL_JSON_NEED_BOSS
										+ "?cartid=" + cartId + "&time="
										+ timeId + "&cityid=" + cityId
										+ "&name=" + content_encoded);
						if (!isDestroyed()) {
							handler2.sendEmptyMessage(0);
						}
					};
				}.start();
			}
		});

	}

	private void initTimeListView() {
		adapterTime = new BossCommTimeSearchAdapter(this, listTime);
		lv_time.setAdapter(adapterTime);
		lv_time.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				timeId = position + "";
				btn_time.setText(listTime.get(position));
				lv_time.setVisibility(View.GONE);

				progress_update_whitebg.setVisibility(View.VISIBLE);
				new Thread() {
					public void run() {
						list = GetData
								.getBossNeedsInfos(MyConfig.URL_JSON_NEED_BOSS
										+ "?cartid=" + cartId + "&time="
										+ timeId + "&cityid=" + cityId
										+ "&name=" + content_encoded);
						if (!isDestroyed()) {
							handler2.sendEmptyMessage(0);
						}
					};
				}.start();
			}
		});

	}

	private void initNeedListView() {
		commAdapter = new BossCommSupportAdapter(this, list);
		listview.setAdapter(commAdapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (list.get(position).getClassify() == 0) {
					Intent intent = new Intent(BossCommSupportActivity.this,
							BossCommDemandMoneyDetailActivity.class);
					intent.putExtra("did", list.get(position).getId());
					startActivity(intent);
				} else {
					Intent intent = new Intent(BossCommSupportActivity.this,
							BossCommDemandBoothDetailActivity.class);
					intent.putExtra("did", list.get(position).getId());
					startActivity(intent);
				}
			}
		});

		listview.setOnScrollListener(new OnScrollListener() {
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
							list2 = GetData
									.getBossNeedsInfos(MyConfig.URL_JSON_NEED_BOSS
											+ "?cartid="
											+ cartId
											+ "&time="
											+ timeId
											+ "&cityid="
											+ cityId
											+ "&name="
											+ content_encoded
											+ "&page=" + page);
							if (!isDestroyed()) {
								handlerRefresh.sendEmptyMessage(0);
							}
						};
					}.start();
				}
			}
		});
	}

	private void initView() {
		rl_search_back.setOnClickListener(this);
		rl_search.setOnClickListener(this);
		btn_type.setOnClickListener(this);
		btn_time.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_search_back:
			finish();
			break;

		// 搜索按钮
		case R.id.rl_search:
			Intent intentSearch = new Intent(this, CommSearchActivity.class);
			startActivityForResult(intentSearch, 1);
			break;

		// 全类型
		case R.id.btn_type:
			lv_time.setVisibility(View.GONE);
			if (lv_type.getVisibility() == View.VISIBLE) {
				lv_type.setVisibility(View.GONE);
				lv_type.startAnimation(MyAnimation.getScaleAnimationToTopBus());
			} else {
				lv_type.setVisibility(View.VISIBLE);
				lv_type.startAnimation(MyAnimation
						.getScaleAnimationToBottomBus());
			}
			break;

		// 全时段
		case R.id.btn_time:
			lv_type.setVisibility(View.GONE);
			if (lv_time.getVisibility() == View.VISIBLE) {
				lv_time.setVisibility(View.GONE);
				lv_time.startAnimation(MyAnimation.getScaleAnimationToTopBus());
			} else {
				lv_time.setVisibility(View.VISIBLE);
				lv_time.startAnimation(MyAnimation
						.getScaleAnimationToBottomBus());
			}
			break;

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == 1) {
			cityId = data.getExtras().getString("cityId");
			content = data.getExtras().getString("content");
			try {
				content_encoded = java.net.URLEncoder.encode(content, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			progress_update_whitebg.setVisibility(View.VISIBLE);
			new Thread() {
				public void run() {
					list = GetData
							.getBossNeedsInfos(MyConfig.URL_JSON_NEED_BOSS
									+ "?cartid=" + cartId + "&time=" + timeId
									+ "&cityid=" + cityId + "&name="
									+ content_encoded);
					if (!isDestroyed()) {
						handler2.sendEmptyMessage(0);
					}
				};
			}.start();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (lv_type.getVisibility() == View.VISIBLE) {
				lv_type.setVisibility(View.GONE);
				lv_type.startAnimation(MyAnimation.getScaleAnimationToTopBus());
				return true;
			}
			if (lv_time.getVisibility() == View.VISIBLE) {
				lv_time.setVisibility(View.GONE);
				lv_time.startAnimation(MyAnimation.getScaleAnimationToTopBus());
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onPause() {
		if (lv_type.getVisibility() == View.VISIBLE) {
			lv_type.setVisibility(View.GONE);
		}
		if (lv_time.getVisibility() == View.VISIBLE) {
			lv_time.setVisibility(View.GONE);
		}
		super.onPause();
	}
}
