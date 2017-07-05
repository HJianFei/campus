package com.loosoo100.campus100.activities;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.adapters.CommActiListHomeAdapter;
import com.loosoo100.campus100.anyevent.MEventCommActiPay;
import com.loosoo100.campus100.beans.CommunityActivityInfo;
import com.loosoo100.campus100.beans.CommunityBasicInfo;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyAnimation;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.CircleView;
import com.loosoo100.campus100.view.RoundAngleImageView;
import com.loosoo100.campus100.view.pulltorefresh.OnRefreshListener;
import com.loosoo100.campus100.view.pulltorefresh.PtrLayout;
import com.loosoo100.campus100.view.pulltorefresh.header.DefaultRefreshView;

import de.greenrobot.event.EventBus;

/**
 * 
 * @author yang 社团
 */
public class CommunityActivity extends Activity implements OnClickListener,
		OnTouchListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view
	@ViewInject(R.id.rl_back)
	private RelativeLayout rl_back;
	@ViewInject(R.id.btn_campusName)
	private Button btn_campusName; // 顶部学校
	@ViewInject(R.id.raiv_topImg)
	private RoundAngleImageView raiv_topImg; // 顶部图片
	@ViewInject(R.id.rl_message)
	private RelativeLayout rl_message; // 消息按钮
	@ViewInject(R.id.ll_together)
	private LinearLayout ll_together; // 创业众筹
	@ViewInject(R.id.ll_heart)
	private LinearLayout ll_heart; // 爱心公益
	@ViewInject(R.id.ll_game)
	private LinearLayout ll_game; // 活动赛事
	@ViewInject(R.id.ll_join)
	private LinearLayout ll_join; // 加入社团
	@ViewInject(R.id.iv_create)
	private ImageView iv_create; // 创建社团图标
	@ViewInject(R.id.tv_create)
	private TextView tv_create; // 创建社团文本
	@ViewInject(R.id.ll_create)
	private LinearLayout ll_create; // 发起活动
	@ViewInject(R.id.ll_community01)
	private LinearLayout ll_community01; // 动漫社01
	@ViewInject(R.id.ll_community02)
	private LinearLayout ll_community02; // 动漫社02
	@ViewInject(R.id.ll_community03)
	private LinearLayout ll_community03; // 动漫社03
	@ViewInject(R.id.ll_community04)
	private LinearLayout ll_community04; // 动漫社04
	@ViewInject(R.id.ll_community05)
	private LinearLayout ll_community05; // 动漫社05
	@ViewInject(R.id.ll_community06)
	private LinearLayout ll_community06; // 动漫社06
	@ViewInject(R.id.ll_community07)
	private LinearLayout ll_community07; // 动漫社07
	@ViewInject(R.id.ll_community08)
	private LinearLayout ll_community08; // 动漫社08
	@ViewInject(R.id.ll_community09)
	private LinearLayout ll_community09; // 动漫社09
	@ViewInject(R.id.cv_community01)
	private CircleView cv_community01;
	@ViewInject(R.id.cv_community02)
	private CircleView cv_community02;
	@ViewInject(R.id.cv_community03)
	private CircleView cv_community03;
	@ViewInject(R.id.cv_community04)
	private CircleView cv_community04;
	@ViewInject(R.id.cv_community05)
	private CircleView cv_community05;
	@ViewInject(R.id.cv_community06)
	private CircleView cv_community06;
	@ViewInject(R.id.cv_community07)
	private CircleView cv_community07;
	@ViewInject(R.id.cv_community08)
	private CircleView cv_community08;
	@ViewInject(R.id.cv_community09)
	private CircleView cv_community09;
	@ViewInject(R.id.tv_community01)
	private TextView tv_community01;
	@ViewInject(R.id.tv_community02)
	private TextView tv_community02;
	@ViewInject(R.id.tv_community03)
	private TextView tv_community03;
	@ViewInject(R.id.tv_community04)
	private TextView tv_community04;
	@ViewInject(R.id.tv_community05)
	private TextView tv_community05;
	@ViewInject(R.id.tv_community06)
	private TextView tv_community06;
	@ViewInject(R.id.tv_community07)
	private TextView tv_community07;
	@ViewInject(R.id.tv_community08)
	private TextView tv_community08;
	@ViewInject(R.id.tv_community09)
	private TextView tv_community09;
	@ViewInject(R.id.ll_more)
	private LinearLayout ll_more; // 更多
	@ViewInject(R.id.lv_community)
	private ListView lv_community; // 列表
	@ViewInject(R.id.ll_join_root)
	private LinearLayout ll_join_root; // 判断用户的学校ID和所选择的学校ID是否一致，不一致则隐藏该布局

	@ViewInject(R.id.rl_again)
	private RelativeLayout rl_again; // 暂无数据
	@ViewInject(R.id.rl_progress)
	private RelativeLayout rl_progress; // 加载动画
	@ViewInject(R.id.rl_top)
	private RelativeLayout rl_top; // 顶部视图，焦点

	@ViewInject(R.id.refresh)
	private PtrLayout refresh; // 刷新
	// private DefaultRefreshView footerView;
	private DefaultRefreshView headerView;

	private String imgUrl = "";

	private int page = 2;

	private View view;

	private Intent intent;

	private String sid = ""; // 所选学校ID
	private String usersid = ""; // 用户的学校ID
	private String uid = "";
	private String mysid = ""; // 用户基本信息学校ID
	private int status = 1; // 0代表管理社团，1代表可创建社团

	private boolean isLoading = true;

	private CommActiListHomeAdapter adapter;
	private List<CommunityActivityInfo> communityActivityInfos;
	private List<CommunityBasicInfo> communityBasicInfos;
	private ScaleAnimation scaleAnimation;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			headerView.postDelayed(new Runnable() {
				@Override
				public void run() {
					refresh.onRefreshComplete();
				}
			}, 1000);
			if (status == 0) {
				tv_create.setText("管理社团");
				iv_create.setImageDrawable(getResources().getDrawable(
						R.drawable.icon_community_manage));
			} else {
				tv_create.setText("创建社团");
				iv_create.setImageDrawable(getResources().getDrawable(
						R.drawable.icon_community_create));
			}
			// 如果所选学校ID跟基本信息的学校ID相同就显示创建和加入社团按钮
			if (sid.equals(mysid)) {
				ll_join_root.setVisibility(View.VISIBLE);
			} else {
				ll_join_root.setVisibility(View.GONE);
			}

			if (imgUrl != null && !imgUrl.equals("") && !imgUrl.equals("null")) {
				Glide.with(CommunityActivity.this).load(imgUrl).asBitmap()
						.placeholder(R.drawable.imgloading_big)
						.into(raiv_topImg);
			} else {
				Glide.with(CommunityActivity.this)
						.load(R.drawable.imgloading_big).asBitmap()
						.into(raiv_topImg);
			}
			if (communityBasicInfos != null && communityBasicInfos.size() > 0) {
				initHView();
				if (communityActivityInfos != null
						&& communityActivityInfos.size() > 0) {
					initListView();
					lv_community.setVisibility(View.VISIBLE);
				} else {
					lv_community.setVisibility(View.GONE);
				}
				rl_again.setVisibility(View.GONE);
			} else {
				rl_again.setVisibility(View.VISIBLE);
				lv_community.setVisibility(View.GONE);
			}
			btn_campusName.setClickable(true);
			rl_progress.setVisibility(View.GONE);
			isLoading = false;
			page = 2;
		}
	};

	/**
	 * 刷新活动列表
	 */
	private Handler handlerUpdateListview = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (communityActivityInfos != null
					&& communityActivityInfos.size() > 0) {
				initListView();
			}
		}
	};

	/**
	 * 加载更多后更新数据
	 */
	private Handler handlerRefresh = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (adapter != null) {
				adapter.notifyDataSetChanged();
				MyUtils.setListViewHeight(lv_community, 0);
			}
			isLoading = false;
			page++;
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);
		EventBus.getDefault().register(this);

		intent = new Intent();
		rl_back.setOnClickListener(this);

		uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.USERID, "");
		usersid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.SCHOOL_ID, "");

		rl_progress.setVisibility(View.VISIBLE);

		initView();

		rl_top.setFocusable(true);
		rl_top.setFocusableInTouchMode(true);
		rl_top.requestFocus();

		scaleAnimation = MyAnimation.getScaleAnimation();

		updateCommData(false);
	}

	/**
	 * 初始化列表
	 */
	private void initListView() {
		adapter = new CommActiListHomeAdapter(this, communityActivityInfos);
		lv_community.setAdapter(adapter);
		lv_community.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (communityActivityInfos.get(position).getClassify() == 0) {
					Intent intent = new Intent(CommunityActivity.this,
							CommActivityDetailTogetherActivity.class);
					intent.putExtra("aid", communityActivityInfos.get(position)
							.getId());
					startActivity(intent);
				} else {
					Intent intent = new Intent(CommunityActivity.this,
							CommActivityDetailFreeActivity.class);
					intent.putExtra("aid", communityActivityInfos.get(position)
							.getId());
					startActivity(intent);
				}
			}
		});
		MyUtils.setListViewHeight(lv_community, 0);

		lv_community.setOnScrollListener(new OnScrollListener() {

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
									.getCommunityActivityInfos(MyConfig.URL_JSON_COMMUNITY_HOME
											+ sid
											+ "&uid="
											+ uid
											+ "&usersid="
											+ usersid + "&page=" + page);
							if (list2 != null && list2.size() > 0) {
								for (int i = 0; i < list2.size(); i++) {
									communityActivityInfos.add(list2.get(i));
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

	private void initHView() {
		TextView[] tv_communitys = new TextView[] { tv_community01,
				tv_community02, tv_community03, tv_community04, tv_community05,
				tv_community06, tv_community07, tv_community08, tv_community09 };
		CircleView[] cv_communitys = new CircleView[] { cv_community01,
				cv_community02, cv_community03, cv_community04, cv_community05,
				cv_community06, cv_community07, cv_community08, cv_community09 };
		if (communityBasicInfos == null) {
			return;
		}
		int size = communityBasicInfos.size();
		for (int i = 0; i < size; i++) {
			tv_communitys[i].setText(communityBasicInfos.get(i)
					.getCommunityName());
			if (!communityBasicInfos.get(i).getHeadthumb().equals("")
					&& !communityBasicInfos.get(i).getHeadthumb()
							.equals("null")) {
				Glide.with(this)
						.load(communityBasicInfos.get(i).getHeadthumb())
						.into(cv_communitys[i]);
			}
		}
		if (size < 6) {
			ll_more.setVisibility(View.GONE);
		} else {
			ll_more.setVisibility(View.VISIBLE);
		}
		ll_community01.setVisibility(View.VISIBLE);
		ll_community02.setVisibility(View.VISIBLE);
		ll_community03.setVisibility(View.VISIBLE);
		ll_community04.setVisibility(View.VISIBLE);
		ll_community05.setVisibility(View.VISIBLE);
		ll_community06.setVisibility(View.VISIBLE);
		ll_community07.setVisibility(View.VISIBLE);
		ll_community08.setVisibility(View.VISIBLE);
		ll_community09.setVisibility(View.VISIBLE);
		switch (size) {
		case 0:
			ll_community01.setVisibility(View.GONE);
		case 1:
			ll_community02.setVisibility(View.GONE);
		case 2:
			ll_community03.setVisibility(View.GONE);
		case 3:
			ll_community04.setVisibility(View.GONE);
		case 4:
			ll_community05.setVisibility(View.GONE);
		case 5:
			ll_community06.setVisibility(View.GONE);
		case 6:
			ll_community07.setVisibility(View.GONE);
		case 7:
			ll_community08.setVisibility(View.GONE);
		case 8:
			ll_community09.setVisibility(View.GONE);
			break;
		}
	}

	private void initView() {
		ll_together.setOnClickListener(this);
		ll_together.setOnTouchListener(this);
		ll_heart.setOnClickListener(this);
		ll_heart.setOnTouchListener(this);
		ll_game.setOnClickListener(this);
		ll_game.setOnTouchListener(this);
		ll_join.setOnClickListener(this);
		ll_join.setOnTouchListener(this);
		ll_create.setOnClickListener(this);
		ll_create.setOnTouchListener(this);

		ll_community01.setOnClickListener(this);
		ll_community01.setOnTouchListener(this);
		ll_community02.setOnClickListener(this);
		ll_community02.setOnTouchListener(this);
		ll_community03.setOnClickListener(this);
		ll_community03.setOnTouchListener(this);
		ll_community04.setOnClickListener(this);
		ll_community04.setOnTouchListener(this);
		ll_community05.setOnClickListener(this);
		ll_community05.setOnTouchListener(this);
		ll_community06.setOnClickListener(this);
		ll_community06.setOnTouchListener(this);
		ll_community07.setOnClickListener(this);
		ll_community07.setOnTouchListener(this);
		ll_community08.setOnClickListener(this);
		ll_community08.setOnTouchListener(this);
		ll_community09.setOnClickListener(this);
		ll_community09.setOnTouchListener(this);
		ll_more.setOnClickListener(this);
		ll_more.setOnTouchListener(this);
		rl_message.setOnClickListener(this);
		btn_campusName.setOnClickListener(this);
		// btn_again.setOnClickListener(this);

		int[] colors = new int[] { getResources().getColor(R.color.red_fd3c49) };
		headerView = new DefaultRefreshView(this);
		headerView.setColorSchemeColors(colors);
		headerView.setIsPullDown(true);
		refresh.setHeaderView(headerView);

		refresh.setOnPullDownRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				updateCommData(true);
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			finish();
			break;

		// 选择学校
		case R.id.btn_campusName:
			intent.setClass(this, SearchActivity.class);
			startActivityForResult(intent, MyConfig.SCHOOL_CODE_COMMUNITY);
			break;

		// 消息
		case R.id.rl_message:
			intent.setClass(this, MessageActivity.class);
			startActivity(intent);
			break;

		// 创业众筹
		case R.id.ll_together:
			intent.setClass(this, CommunityTogetherActivity.class);
			startActivity(intent);
			break;

		// 爱心公益
		case R.id.ll_heart:
			intent.setClass(this, CommunityHeartActivity.class);
			startActivity(intent);
			break;

		// 活动赛事
		case R.id.ll_game:
			intent.setClass(this, CommunityMatchActivity.class);
			startActivity(intent);
			break;

		// 加入社团
		case R.id.ll_join:
			intent.setClass(this, CommunityJoinActivity.class);
			startActivity(intent);
			break;

		// 创建社团
		case R.id.ll_create:
			if (status == 1) {
				intent.setClass(this, CommunityCreateActivity.class);
				startActivity(intent);
			} else if (status == 0) {
				intent.setClass(this, MyCommunityActivity.class);
				startActivity(intent);
			}
			break;

		// 社团01
		case R.id.ll_community01:
			intent.setClass(this, CommDetailActivity.class);
			intent.putExtra("id", communityBasicInfos.get(0).getId());
			// intent.putExtra("position", -1);
			startActivity(intent);
			break;

		// 社团02
		case R.id.ll_community02:
			intent.setClass(this, CommDetailActivity.class);
			intent.putExtra("id", communityBasicInfos.get(1).getId());
			// intent.putExtra("position", -1);
			startActivity(intent);
			break;

		// 社团03
		case R.id.ll_community03:
			intent.setClass(this, CommDetailActivity.class);
			intent.putExtra("id", communityBasicInfos.get(2).getId());
			// intent.putExtra("position", -1);
			startActivity(intent);
			break;

		// 社团04
		case R.id.ll_community04:
			intent.setClass(this, CommDetailActivity.class);
			intent.putExtra("id", communityBasicInfos.get(3).getId());
			// intent.putExtra("position", -1);
			startActivity(intent);
			break;

		// 社团05
		case R.id.ll_community05:
			intent.setClass(this, CommDetailActivity.class);
			intent.putExtra("id", communityBasicInfos.get(4).getId());
			// intent.putExtra("position", -1);
			startActivity(intent);
			break;

		// 社团06
		case R.id.ll_community06:
			intent.setClass(this, CommDetailActivity.class);
			intent.putExtra("id", communityBasicInfos.get(5).getId());
			// intent.putExtra("position", -1);
			startActivity(intent);
			break;

		// 社团07
		case R.id.ll_community07:
			intent.setClass(this, CommDetailActivity.class);
			intent.putExtra("id", communityBasicInfos.get(6).getId());
			// intent.putExtra("position", -1);
			startActivity(intent);
			break;

		// 社团08
		case R.id.ll_community08:
			intent.setClass(this, CommDetailActivity.class);
			intent.putExtra("id", communityBasicInfos.get(7).getId());
			// intent.putExtra("position", -1);
			startActivity(intent);
			break;

		// 社团09
		case R.id.ll_community09:
			intent.setClass(this, CommDetailActivity.class);
			intent.putExtra("id", communityBasicInfos.get(8).getId());
			// intent.putExtra("position", -1);
			startActivity(intent);
			break;

		// 更多
		case R.id.ll_more:
			intent.setClass(this, CommunityJoinActivity.class);
			startActivity(intent);
			break;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			/**
			 * 设置动画效果
			 */
			switch (v.getId()) {
			// 创业众筹
			case R.id.ll_together:
				ll_together.startAnimation(scaleAnimation);
				break;

			// 爱心公益
			case R.id.ll_heart:
				ll_heart.startAnimation(scaleAnimation);
				break;

			// 活动赛事
			case R.id.ll_game:
				ll_game.startAnimation(scaleAnimation);
				break;

			// 社团入驻
			case R.id.ll_join:
				ll_join.startAnimation(scaleAnimation);
				break;

			// 创建社团
			case R.id.ll_create:
				ll_create.startAnimation(scaleAnimation);
				break;

			// 社团01
			case R.id.ll_community01:
				ll_community01.startAnimation(scaleAnimation);
				break;

			// 社团02
			case R.id.ll_community02:
				ll_community02.startAnimation(scaleAnimation);
				break;

			// 社团03
			case R.id.ll_community03:
				ll_community03.startAnimation(scaleAnimation);
				break;

			// 社团04
			case R.id.ll_community04:
				ll_community04.startAnimation(scaleAnimation);
				break;

			// 社团05
			case R.id.ll_community05:
				ll_community05.startAnimation(scaleAnimation);
				break;

			// 社团06
			case R.id.ll_community06:
				ll_community06.startAnimation(scaleAnimation);
				break;

			// 社团07
			case R.id.ll_community07:
				ll_community07.startAnimation(scaleAnimation);
				break;

			// 社团08
			case R.id.ll_community08:
				ll_community08.startAnimation(scaleAnimation);
				break;

			// 社团09
			case R.id.ll_community09:
				ll_community09.startAnimation(scaleAnimation);
				break;

			// 更多
			case R.id.ll_more:
				ll_more.startAnimation(scaleAnimation);
				break;
			}
		}
		return false;
	}

	/**
	 * 判断学校是否改变，是的话就改变社团信息
	 */
	private void updateCommData(boolean pullRefresh) {
		if (!pullRefresh) {
			ll_join_root.setVisibility(View.GONE);
		}
		page = 2;
		isLoading = true;
		// 重新获取学校ID
		sid = this
				.getSharedPreferences(UserInfoDB.USERTABLE, this.MODE_PRIVATE)
				.getString(UserInfoDB.SCHOOL_ID_COMM, "");
		mysid = this.getSharedPreferences(UserInfoDB.USERTABLE,
				this.MODE_PRIVATE).getString(UserInfoDB.SCHOOL_ID, "");
		btn_campusName.setText(this.getSharedPreferences(UserInfoDB.USERTABLE,
				this.MODE_PRIVATE).getString(UserInfoDB.SCHOOL_COMM, ""));

		btn_campusName.setClickable(false);
		new Thread() {

			@Override
			public void run() {
				imgUrl = GetData.getCommImg(MyConfig.URL_JSON_COMMUNITY_HOME
						+ sid);
				status = GetData.getCommStatus(MyConfig.URL_JSON_COMMUNITY_HOME
						+ sid + "&uid=" + uid);
				communityActivityInfos = GetData
						.getCommunityActivityInfos(MyConfig.URL_JSON_COMMUNITY_HOME
								+ sid + "&uid=" + uid + "&usersid=" + usersid);
				communityBasicInfos = GetData
						.getCommunityHomeInfos(MyConfig.URL_JSON_COMMUNITY_HOME
								+ sid + "&uid=" + uid);
				if (!isDestroyed()) {
					handler.sendEmptyMessage(0);
				}
			};
		}.start();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK
				&& requestCode == MyConfig.SCHOOL_CODE_COMMUNITY) {
			UserInfoDB.setUserInfo(this, UserInfoDB.SCHOOL_ID_COMM, data
					.getExtras().getString(MyConfig.SCHOOL_SEARCH_ID));
			UserInfoDB.setUserInfo(this, UserInfoDB.SCHOOL_COMM, data
					.getExtras().getString(MyConfig.SCHOOL_SEARCH));
			rl_progress.setVisibility(View.VISIBLE);
			lv_community.setVisibility(View.GONE);
			updateCommData(false);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void onEventMainThread(MEventCommActiPay event) {
		if (event.isChange()) {
			page = 2;
			new Thread() {
				@Override
				public void run() {
					communityActivityInfos = GetData
							.getCommunityActivityInfos(MyConfig.URL_JSON_COMMUNITY_HOME
									+ sid + "&uid=" + uid+"&usersid="+usersid);
					if (!isDestroyed()) {
						handlerUpdateListview.sendEmptyMessage(0);
					}
				};
			}.start();
		}
	}

	@Override
	protected void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}
}
