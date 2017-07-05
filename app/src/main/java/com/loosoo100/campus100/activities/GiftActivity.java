package com.loosoo100.campus100.activities;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.adapters.GiftCategoryAdapter;
import com.loosoo100.campus100.adapters.GiftGridviewAdapter;
import com.loosoo100.campus100.adapters.ViewPagerAdapter;
import com.loosoo100.campus100.beans.CategoryInfo;
import com.loosoo100.campus100.beans.GiftCategoryInfo;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.controller.BaseController;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.MyGridView;
import com.loosoo100.campus100.view.RoundAngleImageView;
import com.loosoo100.campus100.view.spinkit.SpinKitView;
import com.loosoo100.campus100.view.spinkit.style.Wave;
import com.loosoo100.campus100.view.viewpager.autoscroll.AutoScrollViewPager;

/**
 * 
 * @author yang 礼物说activity
 */
public class GiftActivity extends Activity implements OnClickListener {

	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view

	@ViewInject(R.id.rl_back)
	private RelativeLayout rl_back; // 返回按钮

	@ViewInject(R.id.rl_business)
	private RelativeLayout rl_business; // 右上角商家按钮

	@ViewInject(R.id.btn_category)
	private Button btn_category; // 分类标签

	@ViewInject(R.id.btn_hot)
	private Button btn_hot; // 热门推荐标签

	@ViewInject(R.id.ib_exit)
	private ImageButton ib_exit; // 退出按钮

	@ViewInject(R.id.lv_category)
	private ListView lv_gift; // 分类列表

	@ViewInject(R.id.root_category)
	private RelativeLayout root_category; // 分类标签界面

	@ViewInject(R.id.root_hot)
	private LinearLayout root_hot; // 热门推荐标签界面

	@ViewInject(R.id.vp_category)
	private AutoScrollViewPager vp_category; // 轮播图

	@ViewInject(R.id.ll_gridview01)
	private LinearLayout ll_gridview01; // 表格布局
	@ViewInject(R.id.ll_gridview02)
	private LinearLayout ll_gridview02; // 表格布局
	@ViewInject(R.id.ll_gridview03)
	private LinearLayout ll_gridview03; // 表格布局
	@ViewInject(R.id.ll_gridview04)
	private LinearLayout ll_gridview04; // 表格布局
	@ViewInject(R.id.ll_gridview05)
	private LinearLayout ll_gridview05; // 表格布局
	@ViewInject(R.id.ll_gridview06)
	private LinearLayout ll_gridview06; // 表格布局
	@ViewInject(R.id.ll_gridview07)
	private LinearLayout ll_gridview07; // 表格布局
	@ViewInject(R.id.ll_gridview08)
	private LinearLayout ll_gridview08; // 表格布局
	@ViewInject(R.id.ll_gridview09)
	private LinearLayout ll_gridview09; // 表格布局
	@ViewInject(R.id.ll_gridview10)
	private LinearLayout ll_gridview10; // 表格布局
	@ViewInject(R.id.ll_gridview11)
	private LinearLayout ll_gridview11; // 表格布局
	@ViewInject(R.id.ll_gridview12)
	private LinearLayout ll_gridview12; // 表格布局
	@ViewInject(R.id.ll_gridview13)
	private LinearLayout ll_gridview13; // 表格布局
	@ViewInject(R.id.ll_gridview14)
	private LinearLayout ll_gridview14; // 表格布局
	@ViewInject(R.id.ll_gridview15)
	private LinearLayout ll_gridview15; // 表格布局

	@ViewInject(R.id.ll_fragment_business)
	private LinearLayout ll_fragment_business; // 礼物说商家详情页面

	private List<CategoryInfo> categoryFirst; // 一级分类
	private List<CategoryInfo> categorySec01; // 二级分类
	private List<CategoryInfo> categorySec02; // 二级分类
	private List<CategoryInfo> categorySec03; // 二级分类
	private List<CategoryInfo> categorySec04; // 二级分类
	private List<CategoryInfo> categorySec05; // 二级分类
	private List<CategoryInfo> categorySec06; // 二级分类
	private List<CategoryInfo> categorySec07; // 二级分类
	private List<CategoryInfo> categorySec08; // 二级分类
	private List<CategoryInfo> categorySec09; // 二级分类
	private List<CategoryInfo> categorySec10; // 二级分类
	private List<CategoryInfo> categorySec11; // 二级分类
	private List<CategoryInfo> categorySec12; // 二级分类
	private List<CategoryInfo> categorySec13; // 二级分类
	private List<CategoryInfo> categorySec14; // 二级分类
	private List<CategoryInfo> categorySec15; // 二级分类

	private GiftCategoryAdapter giftCategoryBaseAdapter;
	private GiftGridviewAdapter giftGridviewBaseAdapter;

	@ViewInject(R.id.iv_circle01)
	private ImageButton iv_circle01;

	@ViewInject(R.id.iv_circle02)
	private ImageButton iv_circle02;

	@ViewInject(R.id.iv_circle03)
	private ImageButton iv_circle03;

	@ViewInject(R.id.rl_redCircle)
	private RelativeLayout rl_redCircle;

	@ViewInject(R.id.progress_category)
	private RelativeLayout progress_category; // 刚进去时的加载进度条
	@ViewInject(R.id.progress_small)
	private RelativeLayout progress_small; // 点击不同分类的加载进度条
	@ViewInject(R.id.progress)
	private SpinKitView progress; // 点击不同分类的加载进度条

	@ViewInject(R.id.btn_01)
	private Button btn_01;
	@ViewInject(R.id.btn_02)
	private Button btn_02;
	@ViewInject(R.id.btn_03)
	private Button btn_03;
	@ViewInject(R.id.btn_04)
	private Button btn_04;
	@ViewInject(R.id.btn_05)
	private Button btn_05;

	// @ViewInject(R.id.gift_category01)
	// private LinearLayout gift_category01;
	// @ViewInject(R.id.gift_category02)
	// private LinearLayout gift_category02;

	private Button[] buttons;

	private int width; // 屏幕宽度的1/5
	private int x; // 记录当前位置

	@ViewInject(R.id.vp_hot)
	private ViewPager vp_hot;

	private int index = 2; // 1是热门推荐，2是分类
	private List<View> views = new ArrayList<View>();
	private List<BaseController> controllers = new ArrayList<BaseController>();

	private MyHandler mHandler;
	/*
	 * 某一类目的所有数据
	 */
	private List<List<GiftCategoryInfo>> giftCategoryInfos01;
	private List<List<GiftCategoryInfo>> giftCategoryInfos02;
	private List<List<GiftCategoryInfo>> giftCategoryInfos03;
	private List<List<GiftCategoryInfo>> giftCategoryInfos04;
	private List<List<GiftCategoryInfo>> giftCategoryInfos05;
	private List<List<GiftCategoryInfo>> giftCategoryInfos06;
	private List<List<GiftCategoryInfo>> giftCategoryInfos07;
	private List<List<GiftCategoryInfo>> giftCategoryInfos08;
	private List<List<GiftCategoryInfo>> giftCategoryInfos09;
	private List<List<GiftCategoryInfo>> giftCategoryInfos10;
	private List<List<GiftCategoryInfo>> giftCategoryInfos11;
	private List<List<GiftCategoryInfo>> giftCategoryInfos12;
	private List<List<GiftCategoryInfo>> giftCategoryInfos13;
	private List<List<GiftCategoryInfo>> giftCategoryInfos14;
	private List<List<GiftCategoryInfo>> giftCategoryInfos15;

	static class MyHandler extends Handler {
		private WeakReference<GiftActivity> mActivity;

		MyHandler(GiftActivity activity) {
			mActivity = new WeakReference<GiftActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			GiftActivity activity = mActivity.get();
			switch (msg.what) {
			case 1:
				if (activity.categoryFirst != null
						&& activity.categoryFirst.size() > 0) {
					// 初始化listView
					activity.initListView();
					// 初始化分类的viewPager
					activity.initCategoryViewPager();
					if (activity.giftCategoryInfos01 != null) {
						// 动态添加gridView
						activity.initMyGridView(1);
					}
					// 初始化热门推荐的viewpager
					// activity.initGiftViewPager();
					activity.showWhichPage();
					activity.btn_hot.setClickable(true);
					// 隐藏进度条
					activity.progress_category.setVisibility(View.GONE);
					// 显示第一个类目的数据
					activity.ll_gridview01.setVisibility(View.VISIBLE);
				}
				break;

			case 2:
				if (activity.giftCategoryInfos02 != null) {
					// 动态添加gridView
					activity.initMyGridView(2);
				}
				break;

			case 3:
				if (activity.giftCategoryInfos03 != null) {
					// 动态添加gridView
					activity.initMyGridView(3);
				}
				break;

			case 4:
				if (activity.giftCategoryInfos04 != null) {
					// 动态添加gridView
					activity.initMyGridView(4);
				}
				break;

			case 5:
				if (activity.giftCategoryInfos05 != null) {
					// 动态添加gridView
					activity.initMyGridView(5);
				}
				break;

			case 6:
				if (activity.giftCategoryInfos06 != null) {
					// 动态添加gridView
					activity.initMyGridView(6);
				}
				break;

			case 7:
				if (activity.giftCategoryInfos07 != null) {
					// 动态添加gridView
					activity.initMyGridView(7);
				}
				break;

			case 8:
				if (activity.giftCategoryInfos08 != null) {
					// 动态添加gridView
					activity.initMyGridView(8);
				}
				break;

			case 9:
				if (activity.giftCategoryInfos09 != null) {
					// 动态添加gridView
					activity.initMyGridView(9);
				}
				break;

			case 10:
				if (activity.giftCategoryInfos10 != null) {
					// 动态添加gridView
					activity.initMyGridView(10);
				}
				break;

			case 11:
				if (activity.giftCategoryInfos11 != null) {
					// 动态添加gridView
					activity.initMyGridView(11);
				}
				break;

			case 12:
				if (activity.giftCategoryInfos12 != null) {
					// 动态添加gridView
					activity.initMyGridView(12);
				}
				break;

			case 13:
				if (activity.giftCategoryInfos13 != null) {
					// 动态添加gridView
					activity.initMyGridView(13);
				}
				break;

			case 14:
				if (activity.giftCategoryInfos14 != null) {
					// 动态添加gridView
					activity.initMyGridView(14);
				}
				break;

			case 15:
				if (activity.giftCategoryInfos15 != null) {
					// 动态添加gridView
					activity.initMyGridView(15);
				}
				break;

			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gift);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);
		// 更改状态栏字体颜色为黑色
		MyUtils.setMiuiStatusBarDarkMode(this, true);

		mHandler = new MyHandler(this);

		buttons = new Button[] { btn_01, btn_02, btn_03, btn_04, btn_05 };

		// 设置加载动画样式
		Wave wave = new Wave();
		progress.setIndeterminateDrawable(wave);

		hideView();

		initView();
		width = MyUtils.getWidth(this) / 5;

		new Thread() {
			@Override
			public void run() {
				do {
					// 获取所有的一级类目
					categoryFirst = GetData.getGiftCategoryFirst(
							GiftActivity.this, MyConfig.URL_JSON_GIFT_CATEGORY);
					// 获取所有的二级类目
					categorySec01 = GetData.getGiftCategorySec(
							GiftActivity.this, MyConfig.URL_JSON_GIFT_CATEGORY,
							0);
					// 获取第一个类目的数据
					giftCategoryInfos01 = GetData.getGiftCategoryInfos(
							GiftActivity.this, MyConfig.URL_JSON_GIFT_CATEGORY,
							0);
					// 获取列表图片
					int[] images = new int[] { R.drawable.homepage01,
							R.drawable.homepage02, R.drawable.homepage03 };
					for (int i = 0; i < images.length; i++) {
						// 轮播圆角图片
						RoundAngleImageView imageView = new RoundAngleImageView(
								GiftActivity.this);
						imageView.setRoundPx(50);
						imageView.setScaleType(ScaleType.CENTER_CROP);
//						imageView.setImageResource(images[i % 3]);
						imageView.setImageBitmap(GetData.getBitMap(
								GiftActivity.this, images[i % 3]));
						views.add(imageView);
					}
					if (!isDestroyed()) {
						Message message = Message.obtain();
						message.what = 1;
						mHandler.sendMessage(message);
					}
				} while (categoryFirst == null);
			}
		}.start();

	}

	/**
	 * 初始化热门推荐的viewpager
	 */
	// private void initGiftViewPager() {
	// // 初始化controllers
	// SendFriendController sendFriendController = new SendFriendController(
	// GiftActivity.this);
	// SendEnemyController sendEnemyController = new SendEnemyController(
	// GiftActivity.this);
	// SendGirlFriendController sendGirlFriendController = new
	// SendGirlFriendController(
	// GiftActivity.this);
	// SendOlderController sendOlderController = new SendOlderController(
	// GiftActivity.this);
	// SendCloseFriendController sendCloseFriendController = new
	// SendCloseFriendController(
	// GiftActivity.this);
	// controllers.add(sendFriendController);
	// controllers.add(sendEnemyController);
	// controllers.add(sendGirlFriendController);
	// controllers.add(sendOlderController);
	// controllers.add(sendCloseFriendController);
	// vp_hot.setAdapter(new MyViewPagerAdapter(controllers));
	// vp_hot.setOnPageChangeListener(new OnPageChangeListener() {
	//
	// @Override
	// public void onPageSelected(int arg0) {
	// setCurrentViewPager(arg0);
	// }
	//
	// @Override
	// public void onPageScrolled(int arg0, float arg1, int arg2) {
	// }
	//
	// @Override
	// public void onPageScrollStateChanged(int arg0) {
	// }
	// });
	// }

	// 动态添加gridview
	private void initMyGridView(int index) {
		switch (index) {
		case 1:
			for (int i = 0; i < giftCategoryInfos01.size(); i++) {
				final List<GiftCategoryInfo> list = giftCategoryInfos01.get(i);
				giftGridviewBaseAdapter = new GiftGridviewAdapter(this, list);
				TextView textView = addTextView(categorySec01.get(i)
						.getCategoryName());
				MyGridView gridView = addGridView();
				gridView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent(GiftActivity.this,
								GiftListDetailActivity.class);
						intent.putExtra("catId", list.get(position).getId());
						startActivity(intent);
					}
				});
				gridView.setAdapter(giftGridviewBaseAdapter);
				MyUtils.setGridViewHeight(gridView, 40);
				ll_gridview01.addView(textView);
				ll_gridview01.addView(gridView);
			}
			break;
		case 2:
			for (int i = 0; i < giftCategoryInfos02.size(); i++) {
				final List<GiftCategoryInfo> list = giftCategoryInfos02.get(i);
				giftGridviewBaseAdapter = new GiftGridviewAdapter(this, list);
				TextView textView = addTextView(categorySec02.get(i)
						.getCategoryName());
				MyGridView gridView = addGridView();
				gridView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent(GiftActivity.this,
								GiftListDetailActivity.class);
						intent.putExtra("catId", list.get(position).getId());
						startActivity(intent);
					}
				});
				gridView.setAdapter(giftGridviewBaseAdapter);
				MyUtils.setGridViewHeight(gridView, 40);
				ll_gridview02.addView(textView);
				ll_gridview02.addView(gridView);
			}
			break;
		case 3:
			for (int i = 0; i < giftCategoryInfos03.size(); i++) {
				final List<GiftCategoryInfo> list = giftCategoryInfos03.get(i);
				giftGridviewBaseAdapter = new GiftGridviewAdapter(this, list);
				TextView textView = addTextView(categorySec03.get(i)
						.getCategoryName());
				MyGridView gridView = addGridView();
				gridView.setAdapter(giftGridviewBaseAdapter);
				gridView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent(GiftActivity.this,
								GiftListDetailActivity.class);
						intent.putExtra("catId", list.get(position).getId());
						startActivity(intent);
					}
				});
				MyUtils.setGridViewHeight(gridView, 40);
				ll_gridview03.addView(textView);
				ll_gridview03.addView(gridView);
			}
			break;
		case 4:
			for (int i = 0; i < giftCategoryInfos04.size(); i++) {
				final List<GiftCategoryInfo> list = giftCategoryInfos04.get(i);
				giftGridviewBaseAdapter = new GiftGridviewAdapter(this, list);
				TextView textView = addTextView(categorySec04.get(i)
						.getCategoryName());
				MyGridView gridView = addGridView();
				gridView.setAdapter(giftGridviewBaseAdapter);
				gridView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent(GiftActivity.this,
								GiftListDetailActivity.class);
						intent.putExtra("catId", list.get(position).getId());
						startActivity(intent);
					}
				});
				MyUtils.setGridViewHeight(gridView, 40);
				ll_gridview04.addView(textView);
				ll_gridview04.addView(gridView);
			}
			break;
		case 5:
			for (int i = 0; i < giftCategoryInfos05.size(); i++) {
				final List<GiftCategoryInfo> list = giftCategoryInfos05.get(i);
				giftGridviewBaseAdapter = new GiftGridviewAdapter(this, list);
				TextView textView = addTextView(categorySec05.get(i)
						.getCategoryName());
				MyGridView gridView = addGridView();
				gridView.setAdapter(giftGridviewBaseAdapter);
				gridView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent(GiftActivity.this,
								GiftListDetailActivity.class);
						intent.putExtra("catId", list.get(position).getId());
						startActivity(intent);
					}
				});
				MyUtils.setGridViewHeight(gridView, 40);
				ll_gridview05.addView(textView);
				ll_gridview05.addView(gridView);
			}
			break;
		case 6:
			for (int i = 0; i < giftCategoryInfos06.size(); i++) {
				final List<GiftCategoryInfo> list = giftCategoryInfos06.get(i);
				giftGridviewBaseAdapter = new GiftGridviewAdapter(this, list);
				TextView textView = addTextView(categorySec06.get(i)
						.getCategoryName());
				MyGridView gridView = addGridView();
				gridView.setAdapter(giftGridviewBaseAdapter);
				gridView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent(GiftActivity.this,
								GiftListDetailActivity.class);
						intent.putExtra("catId", list.get(position).getId());
						startActivity(intent);
					}
				});
				MyUtils.setGridViewHeight(gridView, 40);
				ll_gridview06.addView(textView);
				ll_gridview06.addView(gridView);
			}
			break;
		case 7:
			for (int i = 0; i < giftCategoryInfos07.size(); i++) {
				final List<GiftCategoryInfo> list = giftCategoryInfos07.get(i);
				giftGridviewBaseAdapter = new GiftGridviewAdapter(this, list);
				TextView textView = addTextView(categorySec07.get(i)
						.getCategoryName());
				MyGridView gridView = addGridView();
				gridView.setAdapter(giftGridviewBaseAdapter);
				gridView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent(GiftActivity.this,
								GiftListDetailActivity.class);
						intent.putExtra("catId", list.get(position).getId());
						startActivity(intent);
					}
				});
				MyUtils.setGridViewHeight(gridView, 40);
				ll_gridview07.addView(textView);
				ll_gridview07.addView(gridView);
			}
			break;
		case 8:
			for (int i = 0; i < giftCategoryInfos08.size(); i++) {
				final List<GiftCategoryInfo> list = giftCategoryInfos08.get(i);
				giftGridviewBaseAdapter = new GiftGridviewAdapter(this, list);
				TextView textView = addTextView(categorySec08.get(i)
						.getCategoryName());
				MyGridView gridView = addGridView();
				gridView.setAdapter(giftGridviewBaseAdapter);
				gridView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent(GiftActivity.this,
								GiftListDetailActivity.class);
						intent.putExtra("catId", list.get(position).getId());
						startActivity(intent);
					}
				});
				MyUtils.setGridViewHeight(gridView, 40);
				ll_gridview08.addView(textView);
				ll_gridview08.addView(gridView);
			}
			break;
		case 9:
			for (int i = 0; i < giftCategoryInfos09.size(); i++) {
				final List<GiftCategoryInfo> list = giftCategoryInfos09.get(i);
				giftGridviewBaseAdapter = new GiftGridviewAdapter(this, list);
				TextView textView = addTextView(categorySec09.get(i)
						.getCategoryName());
				MyGridView gridView = addGridView();
				gridView.setAdapter(giftGridviewBaseAdapter);
				gridView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent(GiftActivity.this,
								GiftListDetailActivity.class);
						intent.putExtra("catId", list.get(position).getId());
						startActivity(intent);
					}
				});
				MyUtils.setGridViewHeight(gridView, 40);
				ll_gridview09.addView(textView);
				ll_gridview09.addView(gridView);
			}
			break;
		case 10:
			for (int i = 0; i < giftCategoryInfos10.size(); i++) {
				final List<GiftCategoryInfo> list = giftCategoryInfos10.get(i);
				giftGridviewBaseAdapter = new GiftGridviewAdapter(this, list);
				TextView textView = addTextView(categorySec10.get(i)
						.getCategoryName());
				MyGridView gridView = addGridView();
				gridView.setAdapter(giftGridviewBaseAdapter);
				gridView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent(GiftActivity.this,
								GiftListDetailActivity.class);
						intent.putExtra("catId", list.get(position).getId());
						startActivity(intent);
					}
				});
				MyUtils.setGridViewHeight(gridView, 40);
				ll_gridview10.addView(textView);
				ll_gridview10.addView(gridView);
			}
			break;
		case 11:
			for (int i = 0; i < giftCategoryInfos11.size(); i++) {
				final List<GiftCategoryInfo> list = giftCategoryInfos11.get(i);
				giftGridviewBaseAdapter = new GiftGridviewAdapter(this, list);
				TextView textView = addTextView(categorySec11.get(i)
						.getCategoryName());
				MyGridView gridView = addGridView();
				gridView.setAdapter(giftGridviewBaseAdapter);
				gridView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent(GiftActivity.this,
								GiftListDetailActivity.class);
						intent.putExtra("catId", list.get(position).getId());
						startActivity(intent);
					}
				});
				MyUtils.setGridViewHeight(gridView, 40);
				ll_gridview11.addView(textView);
				ll_gridview11.addView(gridView);
			}
			break;
		case 12:
			for (int i = 0; i < giftCategoryInfos12.size(); i++) {
				final List<GiftCategoryInfo> list = giftCategoryInfos12.get(i);
				giftGridviewBaseAdapter = new GiftGridviewAdapter(this, list);
				TextView textView = addTextView(categorySec12.get(i)
						.getCategoryName());
				MyGridView gridView = addGridView();
				gridView.setAdapter(giftGridviewBaseAdapter);
				gridView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent(GiftActivity.this,
								GiftListDetailActivity.class);
						intent.putExtra("catId", list.get(position).getId());
						startActivity(intent);
					}
				});
				MyUtils.setGridViewHeight(gridView, 40);
				ll_gridview12.addView(textView);
				ll_gridview12.addView(gridView);
			}
			break;
		case 13:
			for (int i = 0; i < giftCategoryInfos13.size(); i++) {
				final List<GiftCategoryInfo> list = giftCategoryInfos13.get(i);
				giftGridviewBaseAdapter = new GiftGridviewAdapter(this, list);
				TextView textView = addTextView(categorySec13.get(i)
						.getCategoryName());
				MyGridView gridView = addGridView();
				gridView.setAdapter(giftGridviewBaseAdapter);
				gridView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent(GiftActivity.this,
								GiftListDetailActivity.class);
						intent.putExtra("catId", list.get(position).getId());
						startActivity(intent);
					}
				});
				MyUtils.setGridViewHeight(gridView, 40);
				ll_gridview13.addView(textView);
				ll_gridview13.addView(gridView);
			}
			break;
		case 14:
			for (int i = 0; i < giftCategoryInfos14.size(); i++) {
				final List<GiftCategoryInfo> list = giftCategoryInfos14.get(i);
				giftGridviewBaseAdapter = new GiftGridviewAdapter(this, list);
				TextView textView = addTextView(categorySec14.get(i)
						.getCategoryName());
				MyGridView gridView = addGridView();
				gridView.setAdapter(giftGridviewBaseAdapter);
				gridView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent(GiftActivity.this,
								GiftListDetailActivity.class);
						intent.putExtra("catId", list.get(position).getId());
						startActivity(intent);
					}
				});
				MyUtils.setGridViewHeight(gridView, 40);
				ll_gridview14.addView(textView);
				ll_gridview14.addView(gridView);
			}
			break;
		case 15:
			for (int i = 0; i < giftCategoryInfos15.size(); i++) {
				final List<GiftCategoryInfo> list = giftCategoryInfos15.get(i);
				giftGridviewBaseAdapter = new GiftGridviewAdapter(this, list);
				TextView textView = addTextView(categorySec15.get(i)
						.getCategoryName());
				MyGridView gridView = addGridView();
				gridView.setAdapter(giftGridviewBaseAdapter);
				gridView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent(GiftActivity.this,
								GiftListDetailActivity.class);
						intent.putExtra("catId", list.get(position).getId());
						startActivity(intent);
					}
				});
				MyUtils.setGridViewHeight(gridView, 40);
				ll_gridview15.addView(textView);
				ll_gridview15.addView(gridView);
			}
			break;
		}
		progress_small.setVisibility(View.GONE);

	};

	/**
	 * 初始化listView
	 */
	private void initListView() {
		giftCategoryBaseAdapter = new GiftCategoryAdapter(this, categoryFirst);
		lv_gift.setAdapter(giftCategoryBaseAdapter);
		MyUtils.setListViewHeight(lv_gift, 0);
		showRedline(0);
		// 商品分类监听事件
		lv_gift.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 红条显示位置
				showRedline(position);
				// 更新适配器
				giftCategoryBaseAdapter.notifyDataSetChanged();
				hideView();
				// switch (position) {
				// case 0:
				// ll_gridview01.setVisibility(View.VISIBLE);
				// break;
				// case 1:
				// ll_gridview02.setVisibility(View.VISIBLE);
				// break;
				// }
				switch (position) {
				case 0:
					// 如果商品数据没加载就开启线程获取数据
					if (giftCategoryInfos01 == null) {
						progress_small.setVisibility(View.VISIBLE);
						new Thread() {
							@Override
							public void run() {
								giftCategoryInfos01 = GetData
										.getGiftCategoryInfos(
												GiftActivity.this,
												MyConfig.URL_JSON_GIFT_CATEGORY,
												0);
								if (!isDestroyed()) {
									// handler.sendEmptyMessage(0);
									Message message = Message.obtain();
									message.what = 1;
									mHandler.sendMessage(message);
								}
							}
						}.start();
					} else {
						progress_small.setVisibility(View.GONE);
					}
					ll_gridview01.setVisibility(View.VISIBLE);
					break;
				case 1:
					// 如果商品数据没加载就开启线程获取数据
					if (giftCategoryInfos02 == null) {
						progress_small.setVisibility(View.VISIBLE);
						new Thread() {
							@Override
							public void run() {
								giftCategoryInfos02 = GetData
										.getGiftCategoryInfos(
												GiftActivity.this,
												MyConfig.URL_JSON_GIFT_CATEGORY,
												1);
								categorySec02 = GetData.getGiftCategorySec(
										GiftActivity.this,
										MyConfig.URL_JSON_GIFT_CATEGORY, 1);
								if (!isDestroyed()) {
									// handler02.sendEmptyMessage(0);
									Message message = Message.obtain();
									message.what = 2;
									mHandler.sendMessage(message);
								}
							}
						}.start();
					} else {
						progress_small.setVisibility(View.GONE);
					}
					// 如果商品数据已经加载过了则显示
					ll_gridview02.setVisibility(View.VISIBLE);
					break;
				case 2:
					// 如果商品数据没加载就开启线程获取数据
					if (giftCategoryInfos03 == null) {
						progress_small.setVisibility(View.VISIBLE);
						new Thread() {
							@Override
							public void run() {
								giftCategoryInfos03 = GetData
										.getGiftCategoryInfos(
												GiftActivity.this,
												MyConfig.URL_JSON_GIFT_CATEGORY,
												2);
								categorySec03 = GetData.getGiftCategorySec(
										GiftActivity.this,
										MyConfig.URL_JSON_GIFT_CATEGORY, 2);
								if (!isDestroyed()) {
									// handler03.sendEmptyMessage(0);
									Message message = Message.obtain();
									message.what = 3;
									mHandler.sendMessage(message);
								}
							}
						}.start();
					} else {
						progress_small.setVisibility(View.GONE);
					}
					// 如果商品数据已经加载过了则显示
					ll_gridview03.setVisibility(View.VISIBLE);
					break;
				case 3:
					// 如果商品数据没加载就开启线程获取数据
					if (giftCategoryInfos04 == null) {
						progress_small.setVisibility(View.VISIBLE);
						new Thread() {
							@Override
							public void run() {
								giftCategoryInfos04 = GetData
										.getGiftCategoryInfos(
												GiftActivity.this,
												MyConfig.URL_JSON_GIFT_CATEGORY,
												3);
								categorySec04 = GetData.getGiftCategorySec(
										GiftActivity.this,
										MyConfig.URL_JSON_GIFT_CATEGORY, 3);
								if (!isDestroyed()) {
									// handler04.sendEmptyMessage(0);
									Message message = Message.obtain();
									message.what = 4;
									mHandler.sendMessage(message);
								}
							}
						}.start();
					} else {
						progress_small.setVisibility(View.GONE);
					}
					// 如果商品数据已经加载过了则显示
					ll_gridview04.setVisibility(View.VISIBLE);
					break;
				case 4:
					// 如果商品数据没加载就开启线程获取数据
					if (giftCategoryInfos05 == null) {
						progress_small.setVisibility(View.VISIBLE);
						new Thread() {
							@Override
							public void run() {
								giftCategoryInfos05 = GetData
										.getGiftCategoryInfos(
												GiftActivity.this,
												MyConfig.URL_JSON_GIFT_CATEGORY,
												4);
								categorySec05 = GetData.getGiftCategorySec(
										GiftActivity.this,
										MyConfig.URL_JSON_GIFT_CATEGORY, 4);
								if (!isDestroyed()) {
									// handler05.sendEmptyMessage(0);
									Message message = Message.obtain();
									message.what = 5;
									mHandler.sendMessage(message);
								}
							}
						}.start();
					} else {
						progress_small.setVisibility(View.GONE);
					}
					// 如果商品数据已经加载过了则显示
					ll_gridview05.setVisibility(View.VISIBLE);
					break;
				case 5:
					// 如果商品数据没加载就开启线程获取数据
					if (giftCategoryInfos06 == null) {
						progress_small.setVisibility(View.VISIBLE);
						new Thread() {
							@Override
							public void run() {
								giftCategoryInfos06 = GetData
										.getGiftCategoryInfos(
												GiftActivity.this,
												MyConfig.URL_JSON_GIFT_CATEGORY,
												5);
								categorySec06 = GetData.getGiftCategorySec(
										GiftActivity.this,
										MyConfig.URL_JSON_GIFT_CATEGORY, 5);
								if (!isDestroyed()) {
									// handler06.sendEmptyMessage(0);
									Message message = Message.obtain();
									message.what = 6;
									mHandler.sendMessage(message);
								}
							}
						}.start();
					} else {
						progress_small.setVisibility(View.GONE);
					}
					// 如果商品数据已经加载过了则显示
					ll_gridview06.setVisibility(View.VISIBLE);
					break;
				case 6:
					// 如果商品数据没加载就开启线程获取数据
					if (giftCategoryInfos07 == null) {
						progress_small.setVisibility(View.VISIBLE);
						new Thread() {
							@Override
							public void run() {
								giftCategoryInfos07 = GetData
										.getGiftCategoryInfos(
												GiftActivity.this,
												MyConfig.URL_JSON_GIFT_CATEGORY,
												6);
								categorySec07 = GetData.getGiftCategorySec(
										GiftActivity.this,
										MyConfig.URL_JSON_GIFT_CATEGORY, 6);
								if (!isDestroyed()) {
									// handler07.sendEmptyMessage(0);
									Message message = Message.obtain();
									message.what = 7;
									mHandler.sendMessage(message);
								}
							}
						}.start();
					} else {
						progress_small.setVisibility(View.GONE);
					}
					// 如果商品数据已经加载过了则显示
					ll_gridview07.setVisibility(View.VISIBLE);
					break;
				case 7:
					// 如果商品数据没加载就开启线程获取数据
					if (giftCategoryInfos08 == null) {
						progress_small.setVisibility(View.VISIBLE);
						new Thread() {
							@Override
							public void run() {
								giftCategoryInfos08 = GetData
										.getGiftCategoryInfos(
												GiftActivity.this,
												MyConfig.URL_JSON_GIFT_CATEGORY,
												7);
								categorySec08 = GetData.getGiftCategorySec(
										GiftActivity.this,
										MyConfig.URL_JSON_GIFT_CATEGORY, 7);
								if (!isDestroyed()) {
									// handler08.sendEmptyMessage(0);
									Message message = Message.obtain();
									message.what = 8;
									mHandler.sendMessage(message);
								}
							}
						}.start();
					} else {
						progress_small.setVisibility(View.GONE);
					}
					// 如果商品数据已经加载过了则显示
					ll_gridview08.setVisibility(View.VISIBLE);
					break;
				case 8:
					// 如果商品数据没加载就开启线程获取数据
					if (giftCategoryInfos09 == null) {
						progress_small.setVisibility(View.VISIBLE);
						new Thread() {
							@Override
							public void run() {
								giftCategoryInfos09 = GetData
										.getGiftCategoryInfos(
												GiftActivity.this,
												MyConfig.URL_JSON_GIFT_CATEGORY,
												8);
								categorySec09 = GetData.getGiftCategorySec(
										GiftActivity.this,
										MyConfig.URL_JSON_GIFT_CATEGORY, 8);
								if (!isDestroyed()) {
									// handler09.sendEmptyMessage(0);
									Message message = Message.obtain();
									message.what = 9;
									mHandler.sendMessage(message);
								}
							}
						}.start();
					} else {
						progress_small.setVisibility(View.GONE);
					}
					// 如果商品数据已经加载过了则显示
					ll_gridview09.setVisibility(View.VISIBLE);
					break;
				case 9:
					// 如果商品数据没加载就开启线程获取数据
					if (giftCategoryInfos10 == null) {
						progress_small.setVisibility(View.VISIBLE);
						new Thread() {
							@Override
							public void run() {
								giftCategoryInfos10 = GetData
										.getGiftCategoryInfos(
												GiftActivity.this,
												MyConfig.URL_JSON_GIFT_CATEGORY,
												9);
								categorySec10 = GetData.getGiftCategorySec(
										GiftActivity.this,
										MyConfig.URL_JSON_GIFT_CATEGORY, 9);
								if (!isDestroyed()) {
									// handler10.sendEmptyMessage(0);
									Message message = Message.obtain();
									message.what = 10;
									mHandler.sendMessage(message);
								}
							}
						}.start();
					} else {
						progress_small.setVisibility(View.GONE);
					}
					// 如果商品数据已经加载过了则显示
					ll_gridview10.setVisibility(View.VISIBLE);
					break;
				case 10:
					// 如果商品数据没加载就开启线程获取数据
					if (giftCategoryInfos11 == null) {
						progress_small.setVisibility(View.VISIBLE);
						new Thread() {
							@Override
							public void run() {
								giftCategoryInfos11 = GetData
										.getGiftCategoryInfos(
												GiftActivity.this,
												MyConfig.URL_JSON_GIFT_CATEGORY,
												10);
								categorySec11 = GetData.getGiftCategorySec(
										GiftActivity.this,
										MyConfig.URL_JSON_GIFT_CATEGORY, 10);
								if (!isDestroyed()) {
									// handler11.sendEmptyMessage(0);
									Message message = Message.obtain();
									message.what = 11;
									mHandler.sendMessage(message);
								}
							}
						}.start();
					} else {
						progress_small.setVisibility(View.GONE);
					}
					// 如果商品数据已经加载过了则显示
					ll_gridview11.setVisibility(View.VISIBLE);
					break;
				case 11:
					// 如果商品数据没加载就开启线程获取数据
					if (giftCategoryInfos12 == null) {
						progress_small.setVisibility(View.VISIBLE);
						new Thread() {
							@Override
							public void run() {
								giftCategoryInfos12 = GetData
										.getGiftCategoryInfos(
												GiftActivity.this,
												MyConfig.URL_JSON_GIFT_CATEGORY,
												11);
								categorySec12 = GetData.getGiftCategorySec(
										GiftActivity.this,
										MyConfig.URL_JSON_GIFT_CATEGORY, 11);
								if (!isDestroyed()) {
									// handler12.sendEmptyMessage(0);
									Message message = Message.obtain();
									message.what = 12;
									mHandler.sendMessage(message);
								}
							}
						}.start();
					} else {
						progress_small.setVisibility(View.GONE);
					}
					// 如果商品数据已经加载过了则显示
					ll_gridview12.setVisibility(View.VISIBLE);
					break;
				case 12:
					// 如果商品数据没加载就开启线程获取数据
					if (giftCategoryInfos13 == null) {
						progress_small.setVisibility(View.VISIBLE);
						new Thread() {
							@Override
							public void run() {
								giftCategoryInfos13 = GetData
										.getGiftCategoryInfos(
												GiftActivity.this,
												MyConfig.URL_JSON_GIFT_CATEGORY,
												12);
								categorySec13 = GetData.getGiftCategorySec(
										GiftActivity.this,
										MyConfig.URL_JSON_GIFT_CATEGORY, 12);
								if (!isDestroyed()) {
									// handler13.sendEmptyMessage(0);
									Message message = Message.obtain();
									message.what = 13;
									mHandler.sendMessage(message);
								}
							}
						}.start();
					} else {
						progress_small.setVisibility(View.GONE);
					}
					// 如果商品数据已经加载过了则显示
					ll_gridview13.setVisibility(View.VISIBLE);
					break;
				case 13:
					// 如果商品数据没加载就开启线程获取数据
					if (giftCategoryInfos14 == null) {
						progress_small.setVisibility(View.VISIBLE);
						new Thread() {
							@Override
							public void run() {
								giftCategoryInfos14 = GetData
										.getGiftCategoryInfos(
												GiftActivity.this,
												MyConfig.URL_JSON_GIFT_CATEGORY,
												13);
								categorySec14 = GetData.getGiftCategorySec(
										GiftActivity.this,
										MyConfig.URL_JSON_GIFT_CATEGORY, 13);
								if (!isDestroyed()) {
									// handler14.sendEmptyMessage(0);
									Message message = Message.obtain();
									message.what = 14;
									mHandler.sendMessage(message);
								}
							}
						}.start();
					} else {
						progress_small.setVisibility(View.GONE);
					}
					// 如果商品数据已经加载过了则显示
					ll_gridview14.setVisibility(View.VISIBLE);
					break;
				case 14:
					// 如果商品数据没加载就开启线程获取数据
					if (giftCategoryInfos15 == null) {
						progress_small.setVisibility(View.VISIBLE);
						new Thread() {
							@Override
							public void run() {
								giftCategoryInfos15 = GetData
										.getGiftCategoryInfos(
												GiftActivity.this,
												MyConfig.URL_JSON_GIFT_CATEGORY,
												14);
								categorySec15 = GetData.getGiftCategorySec(
										GiftActivity.this,
										MyConfig.URL_JSON_GIFT_CATEGORY, 14);
								if (!isDestroyed()) {
									// handler15.sendEmptyMessage(0);
									Message message = Message.obtain();
									message.what = 15;
									mHandler.sendMessage(message);
								}
							}
						}.start();
					} else {
						progress_small.setVisibility(View.GONE);
					}
					// 如果商品数据已经加载过了则显示
					ll_gridview15.setVisibility(View.VISIBLE);
					break;
				}
			}
		});
	}

	/**
	 * 初始化viewPager
	 */
	private void initCategoryViewPager() {
		// 设置轮播时间
		vp_category.setInterval(3000);
		// 设置过渡时间
		vp_category.setAutoScrollDurationFactor(10);
		// 设置最后一张跳到第一张时是否有动画
		vp_category.setBorderAnimation(false);
		vp_category.setAdapter(new ViewPagerAdapter(views));
		// 开始自动轮播
		vp_category.startAutoScroll();

		vp_category.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				switch (arg0 % 3) {
				case 0:
					resetCircleImageColor();
					iv_circle01.setImageResource(R.drawable.circle_point_color);
					break;

				case 1:
					resetCircleImageColor();
					iv_circle02.setImageResource(R.drawable.circle_point_color);
					break;

				case 2:
					resetCircleImageColor();
					iv_circle03.setImageResource(R.drawable.circle_point_color);
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

	}

	private void initView() {
		// 显示加载条
		progress_category.setVisibility(View.VISIBLE);
		rl_back.setOnClickListener(this);
		rl_business.setOnClickListener(this);
		btn_category.setOnClickListener(this);
		btn_hot.setOnClickListener(this);

		ib_exit.setOnClickListener(this);

		btn_01.setOnClickListener(this);
		btn_02.setOnClickListener(this);
		btn_03.setOnClickListener(this);
		btn_04.setOnClickListener(this);
		btn_05.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			this.finish();
			break;

		case R.id.rl_business:
			ll_fragment_business.setVisibility(View.VISIBLE);
			break;

		// 热门标签按钮
		case R.id.btn_hot:
			index = 1;
			resetCategoryAndHot();
			btn_hot.setBackground(getResources().getDrawable(
					R.drawable.shape_red_lefttwo));
			btn_hot.setTextColor(getResources().getColor(R.color.white));
			showWhichPage();
			break;

		// 分类按钮
		case R.id.btn_category:
			index = 2;
			resetCategoryAndHot();
			btn_category.setBackground(getResources().getDrawable(
					R.drawable.shape_red_righttwo));
			btn_category.setTextColor(getResources().getColor(R.color.white));
			showWhichPage();
			break;

		case R.id.ib_exit:
			ll_fragment_business.setVisibility(View.GONE);
			break;

		case R.id.btn_01:
			vp_hot.setCurrentItem(0);
			break;

		case R.id.btn_02:
			vp_hot.setCurrentItem(1);
			break;

		case R.id.btn_03:
			vp_hot.setCurrentItem(2);
			break;

		case R.id.btn_04:
			vp_hot.setCurrentItem(3);
			break;

		case R.id.btn_05:
			vp_hot.setCurrentItem(4);
			break;

		}
	}

	/**
	 * 还原顶部标签背景颜色
	 */
	private void resetCategoryAndHot() {
		btn_category.setBackground(getResources().getDrawable(
				R.drawable.shape_white_righttwo));
		btn_hot.setBackground(getResources().getDrawable(
				R.drawable.shape_white_lefttwo));
		btn_category.setTextColor(getResources().getColor(R.color.red_fd3c49));
		btn_hot.setTextColor(getResources().getColor(R.color.red_fd3c49));
	}

	/**
	 * 显示分类或热门推荐页面
	 */
	private void showWhichPage() {
		if (index == 1) {
			root_category.setVisibility(View.GONE);
			root_hot.setVisibility(View.VISIBLE);
		} else if (index == 2) {
			root_category.setVisibility(View.VISIBLE);
			root_hot.setVisibility(View.GONE);
		}
	}

	/**
	 * 显示商品分类的红条
	 */
	private void showRedline(int position) {
		for (int i = 0; i < categoryFirst.size(); i++) {
			if (i == position) {
				categoryFirst.get(i).setShowRedline(true);
			} else {
				categoryFirst.get(i).setShowRedline(false);
			}
		}
	}

	/**
	 * 添加gridView
	 */
	private MyGridView addGridView() {
		MyGridView gridView = new MyGridView(this);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		gridView.setHorizontalSpacing(45);
		gridView.setVerticalSpacing(40);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gridView.setLeft(48);
		gridView.setRight(48);
		gridView.setNumColumns(3);
		gridView.setLayoutParams(params);
		return gridView;
	}

	/**
	 * 添加TextView
	 */
	private TextView addTextView(String text) {
		TextView textView = new TextView(this);
		textView.setTextColor(getResources().getColor(R.color.gray_3e3e3e));
		textView.setText(text);
		textView.setPadding(0, 50, 0, 50);
		textView.setTextSize(14);
		// textView.setTextColor(Color.BLACK);
		return textView;
	}

	/**
	 * 设置轮播小圆点的初始化颜色
	 */
	private void resetCircleImageColor() {
		iv_circle01.setImageResource(R.drawable.circle_point);
		iv_circle02.setImageResource(R.drawable.circle_point);
		iv_circle03.setImageResource(R.drawable.circle_point);
	}

	/**
	 * 监听返回键
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (ll_fragment_business.getVisibility() == View.VISIBLE) {
				ll_fragment_business.setVisibility(View.GONE);
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 设置平移动画
	 * 
	 * @return
	 */
	// private TranslateAnimation translateAnimation(int index) {
	// TranslateAnimation translateAnimation = new TranslateAnimation(x, index
	// * width, 0, 0);
	// translateAnimation.setDuration(300);
	// translateAnimation.setFillBefore(true);
	// translateAnimation.setFillAfter(true);
	// return translateAnimation;
	// }

	/**
	 * 按钮字体颜色还原初始化
	 */
	// private void resetBtnColor() {
	// btn_01.setTextColor(Color.BLACK);
	// btn_02.setTextColor(Color.BLACK);
	// btn_03.setTextColor(Color.BLACK);
	// btn_04.setTextColor(Color.BLACK);
	// btn_05.setTextColor(Color.BLACK);
	// }

	/**
	 * 设置当前viewpager显示哪个页面
	 *
	 */
	// private void setCurrentViewPager(int index) {
	// resetBtnColor();
	// buttons[index]
	// .setTextColor(getResources().getColor(R.color.red_fd3c49));
	// rl_redCircle.startAnimation(translateAnimation(index));
	// x = index * width;
	// }

	private void hideView() {
		ll_gridview01.setVisibility(View.GONE);
		ll_gridview02.setVisibility(View.GONE);
		ll_gridview03.setVisibility(View.GONE);
		ll_gridview04.setVisibility(View.GONE);
		ll_gridview05.setVisibility(View.GONE);
		ll_gridview06.setVisibility(View.GONE);
		ll_gridview07.setVisibility(View.GONE);
		ll_gridview08.setVisibility(View.GONE);
		ll_gridview09.setVisibility(View.GONE);
		ll_gridview10.setVisibility(View.GONE);
		ll_gridview11.setVisibility(View.GONE);
		ll_gridview12.setVisibility(View.GONE);
		ll_gridview13.setVisibility(View.GONE);
		ll_gridview14.setVisibility(View.GONE);
		ll_gridview15.setVisibility(View.GONE);
	}

	@Override
	protected void onDestroy() {
		mHandler.removeCallbacksAndMessages(null);
		super.onDestroy();
	}

}
