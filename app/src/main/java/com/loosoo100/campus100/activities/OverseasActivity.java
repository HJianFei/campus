package com.loosoo100.campus100.activities;

import java.io.UnsupportedEncodingException;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.adapters.OverseasGridviewAdapter;
import com.loosoo100.campus100.anyevent.MEventOverseas;
import com.loosoo100.campus100.beans.OverseasInfo;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;

import de.greenrobot.event.EventBus;

/**
 * 
 * @author yang 海归activity
 */
public class OverseasActivity extends Activity implements OnClickListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view

	@ViewInject(R.id.rl_back)
	private RelativeLayout rl_back; // 返回
	@ViewInject(R.id.rl_help)
	private RelativeLayout rl_help; // 求助
	@ViewInject(R.id.et_search)
	private EditText et_search; // 搜索内容
	@ViewInject(R.id.tv_help)
	private TextView tv_help; // 帮助
	@ViewInject(R.id.btn_delete)
	private Button btn_delete; // 输入文本删除

	@ViewInject(R.id.btn_all)
	private Button btn_all; // 全部
	@ViewInject(R.id.btn01)
	private Button btn01; // 南美洲
	@ViewInject(R.id.btn02)
	private Button btn02; // 北美洲
	@ViewInject(R.id.btn03)
	private Button btn03; // 欧洲
	@ViewInject(R.id.btn04)
	private Button btn04; // 亚洲
	@ViewInject(R.id.iv_redline)
	private ImageView iv_redline; // 红条
	@ViewInject(R.id.gv_school)
	private GridView gv_school; // 学校列表
	@ViewInject(R.id.progress)
	private RelativeLayout progress; // 加载动画

	private int x = 0; // 记录当前红条所在位置
	private int width; // 屏幕宽度的1/5

	private List<OverseasInfo> list;
	private OverseasGridviewAdapter adapter;

	private boolean isLoading = true;
	private int page = 1;
	private String uid = "";
	private String name = "";
	private String name_encoded = ""; // 转码后的搜索内容
	private int type = 0;
	private int mPosition;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (list != null && list.size() > 0) {
				initGridView();
				gv_school.setVisibility(View.VISIBLE);
			} else {
				gv_school.setVisibility(View.GONE);
			}
			isLoading = false;
			page = 2;
			progress.setVisibility(View.GONE);
		};
	};

	/*
	 * 加载更多后更新数据
	 */
	private Handler handlerRefresh = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (adapter != null) {
				adapter.notifyDataSetChanged();
				isLoading = false;
				page++;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_overseas);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);
		EventBus.getDefault().register(this);

		uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.USERID, "");

		WindowManager systemService = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		width = systemService.getDefaultDisplay().getWidth() / 5;

		progress.setVisibility(View.VISIBLE);
		initView();

		new Thread() {
			public void run() {
				list = GetData.getOverseasInfos(MyConfig.URL_JSON_OVERSEAS_LIST
						+ uid + "&id=" + type);
				if (!isDestroyed()) {
					handler.sendEmptyMessage(0);
				}
			};
		}.start();
	}

	private void initView() {
		btn_delete.setOnClickListener(this);
		rl_back.setOnClickListener(this);
		rl_help.setOnClickListener(this);
		btn_all.setOnClickListener(this);
		btn01.setOnClickListener(this);
		btn02.setOnClickListener(this);
		btn03.setOnClickListener(this);
		btn04.setOnClickListener(this);
		// 设置红条的宽度和高度
		LayoutParams params = new LayoutParams(width, 10);
		iv_redline.setLayoutParams(params);

		et_search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.toString().trim().length() > 0) {
					tv_help.setVisibility(View.GONE);
					btn_delete.setVisibility(View.VISIBLE);
				} else {
					tv_help.setVisibility(View.VISIBLE);
					btn_delete.setVisibility(View.GONE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		gv_school.setOnScrollListener(new OnScrollListener() {
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
						private List<OverseasInfo> list2;

						public void run() {
							list2 = GetData
									.getOverseasInfos(MyConfig.URL_JSON_OVERSEAS_LIST
											+ uid
											+ "&id="
											+ type
											+ "&name="
											+ name_encoded + "&page=" + page);
							if (list2 != null) {
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

	/**
	 * 初始化gridView
	 */
	private void initGridView() {
		adapter = new OverseasGridviewAdapter(this, list, uid);
		gv_school.setAdapter(adapter);

		gv_school.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mPosition = position;
				Intent intent = new Intent(OverseasActivity.this,
						OverseasDetailActivity.class);
				intent.putExtra("sid", list.get(position).getSchoolID());
				startActivity(intent);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			this.finish();
			break;
			
		case R.id.btn_delete:
			et_search.setText("");
			break;

		// 求助
		case R.id.rl_help:
			name = et_search.getText().toString().trim();
			if (name.equals("")) {
				Intent intent = new Intent(this, OverseasQuestionActivity.class);
				startActivity(intent);
			} else {
				progress.setVisibility(View.VISIBLE);
				isLoading = true;
				// 隐藏软键盘
				MyUtils.hideSoftInput(this, et_search);
				try {
					// url转码
					name_encoded = java.net.URLEncoder.encode(name, "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				new Thread() {
					@Override
					public void run() {
						list = GetData
								.getOverseasInfos(MyConfig.URL_JSON_OVERSEAS_LIST
										+ uid
										+ "&id="
										+ type
										+ "&name="
										+ name_encoded);
						if (!isDestroyed()) {
							handler.sendEmptyMessage(0);
						}
					}
				}.start();
			}
			break;

		// 全部
		case R.id.btn_all:
			name_encoded = "";
			// 红色线条平移动画
			iv_redline.startAnimation(translateAnimation(0));
			// 记录当前红色线条在什么位置
			x = 0;
			resetBtnColor();
			btn_all.setTextColor(getResources().getColor(R.color.red_fd3c49));
			if (type == 0) {
				return;
			}
			progress.setVisibility(View.VISIBLE);
			type = 0;
			new Thread() {
				public void run() {
					// 红条滑动不卡顿
					SystemClock.sleep(300);
					list = GetData
							.getOverseasInfos(MyConfig.URL_JSON_OVERSEAS_LIST
									+ uid + "&id=" + type);
					if (!isDestroyed()) {
						handler.sendEmptyMessage(0);
					}
				};
			}.start();
			break;

		// 大洋洲
		case R.id.btn01:
			name_encoded = "";
			// 红色线条平移动画
			iv_redline.startAnimation(translateAnimation(1));
			// 记录当前红色线条在什么位置
			x = width;
			resetBtnColor();
			btn01.setTextColor(getResources().getColor(R.color.red_fd3c49));
			if (type == 1) {
				return;
			}
			progress.setVisibility(View.VISIBLE);
			type = 1;
			new Thread() {
				public void run() {
					// 红条滑动不卡顿
					SystemClock.sleep(300);
					list = GetData
							.getOverseasInfos(MyConfig.URL_JSON_OVERSEAS_LIST
									+ uid + "&id=" + type);
					if (!isDestroyed()) {
						handler.sendEmptyMessage(0);
					}
				};
			}.start();
			break;

		// 美洲
		case R.id.btn02:
			name_encoded = "";
			// 红色线条平移动画
			iv_redline.startAnimation(translateAnimation(2));
			// 记录当前红色线条在什么位置
			x = 2 * width;
			resetBtnColor();
			btn02.setTextColor(getResources().getColor(R.color.red_fd3c49));
			if (type == 2) {
				return;
			}
			progress.setVisibility(View.VISIBLE);
			type = 2;
			new Thread() {
				public void run() {
					// 红条滑动不卡顿
					SystemClock.sleep(300);
					list = GetData
							.getOverseasInfos(MyConfig.URL_JSON_OVERSEAS_LIST
									+ uid + "&id=" + type);
					if (!isDestroyed()) {
						handler.sendEmptyMessage(0);
					}
				};
			}.start();
			break;

		// 欧洲
		case R.id.btn03:
			name_encoded = "";
			// 红色线条平移动画
			iv_redline.startAnimation(translateAnimation(3));
			// 记录当前红色线条在什么位置
			x = 3 * width;
			resetBtnColor();
			btn03.setTextColor(getResources().getColor(R.color.red_fd3c49));
			if (type == 3) {
				return;
			}
			progress.setVisibility(View.VISIBLE);
			type = 3;
			new Thread() {
				public void run() {
					// 红条滑动不卡顿
					SystemClock.sleep(300);
					list = GetData
							.getOverseasInfos(MyConfig.URL_JSON_OVERSEAS_LIST
									+ uid + "&id=" + type);
					if (!isDestroyed()) {
						handler.sendEmptyMessage(0);
					}
				};
			}.start();
			break;

		// 亚洲
		case R.id.btn04:
			name_encoded = "";
			// 红色线条平移动画
			iv_redline.startAnimation(translateAnimation(4));
			// 记录当前红色线条在什么位置
			x = 4 * width;
			resetBtnColor();
			btn04.setTextColor(getResources().getColor(R.color.red_fd3c49));
			if (type == 4) {
				return;
			}
			progress.setVisibility(View.VISIBLE);
			type = 4;
			new Thread() {
				public void run() {
					// 红条滑动不卡顿
					SystemClock.sleep(300);
					list = GetData
							.getOverseasInfos(MyConfig.URL_JSON_OVERSEAS_LIST
									+ uid + "&id=" + type);
					if (!isDestroyed()) {
						handler.sendEmptyMessage(0);
					}
				};
			}.start();
			break;

		}
	}

	/**
	 * 按钮字体颜色还原初始化
	 */
	private void resetBtnColor() {
		btn_all.setTextColor(Color.BLACK);
		btn01.setTextColor(Color.BLACK);
		btn02.setTextColor(Color.BLACK);
		btn03.setTextColor(Color.BLACK);
		btn04.setTextColor(Color.BLACK);
	}

	/**
	 * 设置平移动画
	 * 
	 * @return
	 */
	private TranslateAnimation translateAnimation(int index) {
		TranslateAnimation translateAnimation = new TranslateAnimation(x, index
				* width, 0, 0);
		translateAnimation.setDuration(300);
		translateAnimation.setFillBefore(true);
		translateAnimation.setFillAfter(true);
		return translateAnimation;
	}

	public void onEventMainThread(MEventOverseas event) {
		if (event.getOollect()) {
			list.get(mPosition).setStatus(1);
			adapter.notifyDataSetChanged();
		} else {
			list.get(mPosition).setStatus(0);
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

}
