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
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.adapters.GiftListGridviewAdapter;
import com.loosoo100.campus100.anyevent.MEventGiftGoodsList;
import com.loosoo100.campus100.beans.GiftGoodsInfo;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;

import de.greenrobot.event.EventBus;

/**
 * 
 * @author yang 礼物说列表详情activity
 */
public class GiftListDetailActivity extends Activity implements OnClickListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view
	@ViewInject(R.id.rl_back)
	private RelativeLayout rl_back; // 返回按钮
	@ViewInject(R.id.rl_message)
	private RelativeLayout rl_message; // 消息按钮
	// @ViewInject(R.id.iv_message)
	// private ImageView iv_message; // 消息按钮图标
	@ViewInject(R.id.et_search)
	private EditText et_search; // 搜索框
	@ViewInject(R.id.ll_root)
	private LinearLayout ll_root; // 分类布局
	@ViewInject(R.id.gv_gift_list)
	private GridView gv_gift_list; // 表格布局
	@ViewInject(R.id.rl_pb)
	private RelativeLayout rl_pb; // 加载进度条
	@ViewInject(R.id.btn_delete)
	private Button btn_delete; // 输入文本删除

	@ViewInject(R.id.btn_all)
	private Button btn_all; // 综合排序
	@ViewInject(R.id.btn_price)
	private Button btn_price; // 价格排序
	@ViewInject(R.id.btn_sold)
	private Button btn_sold; // 销量排序
	@ViewInject(R.id.btn_new)
	private Button btn_new; // 最新排序
	@ViewInject(R.id.iv_redline)
	private ImageView iv_redline; // 最新排序

	// @ViewInject(R.id.refresh)
	// private PtrLayout refresh; // 刷新

	private int x = 0; // 记录当前红条所在位置
	private int width; // 屏幕宽度

	private String catId = "";
	private boolean isAsc; // 是否升序
	private boolean isChange; // 排序是否改变

	private int page = 2;
	private boolean isLoading = false;
	// private DefaultRefreshView footerView;

	private List<GiftGoodsInfo> giftGoodsInfos;
	private List<GiftGoodsInfo> list2;

	private GiftListGridviewAdapter giftListGridviewAdapter;

	// private GiftListDetailActivity activity;
	private String search; // 搜索内容
	private String search_encoded; // 转码后的搜索内容

	private boolean isSearch = false;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (giftGoodsInfos != null && giftGoodsInfos.size() > 0) {
				initGridView();
				gv_gift_list.setVisibility(View.VISIBLE);
			} else {
				gv_gift_list.setVisibility(View.GONE);
			}
			rl_pb.setVisibility(View.GONE);
		};
	};

	/*
	 * 上拉刷新完成后更新数据
	 */
	private Handler handlerRefresh = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (giftListGridviewAdapter != null) {
				giftListGridviewAdapter.notifyDataSetChanged();
				isLoading = false;
				page++;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gift_list_detail);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);
		// 更改状态栏字体颜色为黑色
		MyUtils.setMiuiStatusBarDarkMode(this, true);

		EventBus.getDefault().register(this);

		WindowManager systemService = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		width = systemService.getDefaultDisplay().getWidth() / 4;

		// activity = GiftListDetailActivity.this;
		catId = getIntent().getExtras().getString("catId");

		initView();
		rl_pb.setVisibility(View.VISIBLE);

		new Thread() {
			@Override
			public void run() {
				giftGoodsInfos = GetData
						.getGiftGoodsListInfos(MyConfig.URL_JSON_GIFT_GOODS_LIST
								+ catId);
				if (!isDestroyed()) {
					handler.sendEmptyMessage(0);
				}
			}
		}.start();
	}

	/**
	 * 初始化gridView
	 */
	private void initGridView() {
		giftListGridviewAdapter = new GiftListGridviewAdapter(this,
				giftGoodsInfos);
		gv_gift_list.setAdapter(giftListGridviewAdapter);

		gv_gift_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(GiftListDetailActivity.this,
						GiftGoodsDetailActivity.class);
				intent.putExtra("giftgoodsId", giftGoodsInfos.get(position)
						.getGiftgoodsId());
				startActivity(intent);
			}
		});

	}

	private void initView() {
		btn_delete.setOnClickListener(this);
		rl_back.setOnClickListener(this);
		rl_message.setOnClickListener(this);
		btn_all.setOnClickListener(this);
		btn_price.setOnClickListener(this);
		btn_sold.setOnClickListener(this);
		btn_new.setOnClickListener(this);

		// 设置红条的宽度和高度
		LayoutParams params = new LayoutParams(width, 10);
		iv_redline.setLayoutParams(params);

		et_search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.toString().trim().length() > 0) {
					// iv_message.setVisibility(View.GONE);
					btn_delete.setVisibility(View.VISIBLE);
				} else {
					// iv_message.setVisibility(View.VISIBLE);
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

		gv_gift_list.setOnScrollListener(new OnScrollListener() {
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
							if (isSearch) {
								list2 = GetData
										.getGiftGoodsListInfos(MyConfig.URL_JSON_GIFT_GOODS_LIST_SEARCH
												+ search_encoded
												+ "&page="
												+ page);
							} else {
								list2 = GetData
										.getGiftGoodsListInfos(MyConfig.URL_JSON_GIFT_GOODS_LIST
												+ catId + "&page=" + page);
							}
							if (list2 != null) {
								for (int i = 0; i < list2.size(); i++) {
									giftGoodsInfos.add(list2.get(i));
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

		case R.id.btn_delete:
			et_search.setText("");
			break;

		// 消息或搜索按钮
		case R.id.rl_message:
			search = et_search.getText().toString().trim();
			if (search.equals("")) {
				// Intent intent = new Intent(this, MessageActivity.class);
				// startActivity(intent);
			} else {
				ll_root.setVisibility(View.GONE);
				rl_pb.setVisibility(View.VISIBLE);
				isSearch = true;
				page = 2;
				isLoading = false;
				// 隐藏软键盘
				MyUtils.hideSoftInput(this, et_search);
				try {
					// url转码
					search_encoded = java.net.URLEncoder
							.encode(search, "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				new Thread() {
					@Override
					public void run() {
						giftGoodsInfos = GetData
								.getGiftGoodsListInfos(MyConfig.URL_JSON_GIFT_GOODS_LIST_SEARCH
										+ search_encoded);
						if (!isDestroyed()) {
							handler.sendEmptyMessage(0);
						}
					}
				}.start();
			}
			break;

		// 综合排序
		case R.id.btn_all:
			isLoading = false;
			isAsc = false;
			if (x != 0) {
				page = 2;
				isChange = true;
				rl_pb.setVisibility(View.VISIBLE);
				new Thread(new Runnable() {
					@Override
					public void run() {
						// 红条滑动不卡顿
						SystemClock.sleep(300);
						giftGoodsInfos = GetData
								.getGiftGoodsListInfos(MyConfig.URL_JSON_GIFT_GOODS_LIST
										+ catId);
						if (giftGoodsInfos != null && giftGoodsInfos.size() > 0) {
							handler.sendEmptyMessage(0);
						}
					}
				}).start();
			}
			// 红色线条平移动画
			iv_redline.startAnimation(translateAnimation(0));
			// 记录当前红色线条在什么位置
			x = 0;
			resetBtnColor();
			btn_all.setTextColor(getResources().getColor(R.color.red_fd3c49));
			break;

		// 价格排序
		case R.id.btn_price:
			rl_pb.setVisibility(View.VISIBLE);
			page = 2;
			isChange = true;
			isLoading = false;
			if (!isAsc) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						// 红条滑动不卡顿
						SystemClock.sleep(300);
						// 价格升序
						giftGoodsInfos = GetData
								.getGiftGoodsListInfos(MyConfig.URL_JSON_GIFT_GOODS_LIST_PRICEASC
										+ catId);
						isAsc = true;
						if (!isDestroyed()) {
							handler.sendEmptyMessage(0);
						}
					}
				}).start();
			} else {
				new Thread(new Runnable() {
					@Override
					public void run() {
						// 价格降序
						giftGoodsInfos = GetData
								.getGiftGoodsListInfos(MyConfig.URL_JSON_GIFT_GOODS_LIST_PRICEDESC
										+ catId);
						isAsc = false;
						if (!isDestroyed()) {
							handler.sendEmptyMessage(0);
						}
					}
				}).start();
			}
			// 红色线条平移动画
			iv_redline.startAnimation(translateAnimation(1));
			// 记录当前红色线条在什么位置
			x = width;
			resetBtnColor();
			btn_price.setTextColor(getResources().getColor(R.color.red_fd3c49));
			break;

		// 销量排序
		case R.id.btn_sold:
			isAsc = false;
			isLoading = false;
			if (x != 2 * width) {
				page = 2;
				isChange = true;
				rl_pb.setVisibility(View.VISIBLE);
				new Thread(new Runnable() {
					@Override
					public void run() {
						// 红条滑动不卡顿
						SystemClock.sleep(300);
						giftGoodsInfos = GetData
								.getGiftGoodsListInfos(MyConfig.URL_JSON_GIFT_GOODS_LIST_SOLDDESC
										+ catId);
						if (!isDestroyed()) {
							handler.sendEmptyMessage(0);
						}
					}
				}).start();
			}
			// 红色线条平移动画
			iv_redline.startAnimation(translateAnimation(2));
			// 记录当前红色线条在什么位置
			x = 2 * width;
			resetBtnColor();
			btn_sold.setTextColor(getResources().getColor(R.color.red_fd3c49));
			break;

		// 最新排序
		case R.id.btn_new:
			isAsc = false;
			isLoading = false;
			if (x != 3 * width) {
				page = 2;
				isChange = true;
				rl_pb.setVisibility(View.VISIBLE);
				new Thread(new Runnable() {
					@Override
					public void run() {
						// 红条滑动不卡顿
						SystemClock.sleep(300);
						giftGoodsInfos = GetData
								.getGiftGoodsListInfos(MyConfig.URL_JSON_GIFT_GOODS_LIST_TIMEASC
										+ catId);
						if (!isDestroyed()) {
							handler.sendEmptyMessage(0);
						}
					}
				}).start();
			}
			// 红色线条平移动画
			iv_redline.startAnimation(translateAnimation(3));
			// 记录当前红色线条在什么位置
			x = 3 * width;
			resetBtnColor();
			btn_new.setTextColor(getResources().getColor(R.color.red_fd3c49));
			break;

		}
	}

	/**
	 * 按钮字体颜色还原初始化
	 */
	private void resetBtnColor() {
		btn_all.setTextColor(Color.BLACK);
		btn_price.setTextColor(Color.BLACK);
		btn_sold.setTextColor(Color.BLACK);
		btn_new.setTextColor(Color.BLACK);
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

	public void onEventMainThread(MEventGiftGoodsList event) {
		if (event.isFinish()) {
			this.finish();
		}
	}

	@Override
	protected void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

}
