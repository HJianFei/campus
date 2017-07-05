package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.adapters.CampusContactsAdapter;
import com.loosoo100.campus100.anyevent.MEventCampusContactsAdd;
import com.loosoo100.campus100.anyevent.MEventCampusContactsAllDel;
import com.loosoo100.campus100.anyevent.MEventCampusContactsAppraiseChange;
import com.loosoo100.campus100.anyevent.MEventCampusContactsChange;
import com.loosoo100.campus100.beans.CampusContactsInfo;
import com.loosoo100.campus100.beans.CampusContactsReplyInfo;
import com.loosoo100.campus100.beans.CampusContactsUserInfo;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.listener.OnDoubleClickListener;
import com.loosoo100.campus100.listener.OnDoubleClickListener.DoubleClickCallback;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyAnimation;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.CircleView;
import com.loosoo100.campus100.view.scrollablelayout.ScrollableHelper.ScrollableContainer;
import com.loosoo100.campus100.view.scrollablelayout.ScrollableLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 
 * @author yang 校园圈activity
 */
public class CampusContactsActivity extends Activity implements
		OnClickListener, OnTouchListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view
	@ViewInject(R.id.rl_back)
	private RelativeLayout rl_back; // 返回
	@ViewInject(R.id.rl_topbar)
	private RelativeLayout rl_topbar; // 顶部栏
	@ViewInject(R.id.tv_school)
	private TextView tv_school; // 所属学校
	@ViewInject(R.id.tv_ulcount)
	private TextView tv_ulcount; // 被暗恋的人数
	@ViewInject(R.id.ll_love)
	private LinearLayout ll_love; // 全国或全校
	@ViewInject(R.id.ll_top)
	private LinearLayout ll_top; // 全国或全校
	@ViewInject(R.id.tv_top01)
	private TextView tv_top01; // 全国或全校
	@ViewInject(R.id.tv_top02)
	private TextView tv_top02; // 全国或全校
	@ViewInject(R.id.rl_message)
	private RelativeLayout rl_message; // 消息
	@ViewInject(R.id.tv_count)
	private TextView tv_count; // 消息条数
	@ViewInject(R.id.cv_headShot)
	private CircleView cv_headShot; // 头像
	@ViewInject(R.id.iv_sex)
	private ImageView iv_sex; // 性别图标
	@ViewInject(R.id.btn_all_top02)
	private Button btn_all_top02; // 全部
	@ViewInject(R.id.btn_feeling_top02)
	private Button btn_feeling_top02; // 心情
	@ViewInject(R.id.btn_trading_top02)
	private Button btn_trading_top02; // 买卖
	@ViewInject(R.id.btn_gogo_top02)
	private Button btn_gogo_top02; // GOGO
	@ViewInject(R.id.btn_lostAndFound_top02)
	private Button btn_lostAndFound_top02; // 失物招领
	@ViewInject(R.id.iv_redline_top02)
	private ImageView iv_redline_top02; // 红条

	@ViewInject(R.id.scrollableLayout)
	private ScrollableLayout scrollableLayout;
	@ViewInject(R.id.lv_campus)
	private ListView lv_campus; // 校园圈列表
	@ViewInject(R.id.ib_add)
	private ImageButton ib_add; // 发表

	@ViewInject(R.id.ll_appraise_edit)
	private LinearLayout ll_appraise_edit; // 评论输入框
	@ViewInject(R.id.et_content)
	public static EditText et_content; // 评论输入框内容
	@ViewInject(R.id.btn_send)
	private Button btn_send; // 评论输入框发送

	@ViewInject(R.id.progress_update_whitebg)
	private RelativeLayout progress_update_whitebg; // 加载框
	@ViewInject(R.id.tv_noData)
	private TextView tv_noData; // 加载框
	// @ViewInject(R.id.iv_bg)
	// private ImageView iv_bg; // 背景图

	private List<CampusContactsInfo> list;
	private CampusContactsAdapter adapter;

	private int x = 0; // 记录当前红条所在位置
	private int width; // 屏幕宽度的1/5

	public static int position = -1; // 记录点击的评论的哪一条
	public static int appraisePosition = -1; // 记录点击的回复的哪一条

	private String sid = "";
	private String school = "";
	private int type = 0; // 0全部 1心情 2买卖 3GOGO 4失物招领
	private String uid = "";
	private String username = "";
	public static String pname = "";
	private String status = "3"; // 1本校 3全国
	private int page = 1;
	private String content = ""; // 评论内容

	private String repayId = ""; // 评论ID
	public static String pid = "0"; // 回复ID

	private long campusContactsTime = 0;

	private boolean isLoading = true;

	private int noReadCount = 0;
	private boolean loading = false;

	public static int mPosition = -1; // 评论框收回
	protected CampusContactsUserInfo userInfo;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (userInfo != null) {
				ll_love.setVisibility(View.VISIBLE);
				tv_ulcount.setText(userInfo.getCrushNum() + "");
			}
			if (list != null && list.size() > 0) {
				UserInfoDB.setUserInfo(CampusContactsActivity.this,
						UserInfoDB.CAMPUS_TIME, list.get(0).getTime() + "");
				initListView();
				lv_campus.setVisibility(View.VISIBLE);
				tv_noData.setVisibility(View.GONE);
			} else {
				lv_campus.setVisibility(View.GONE);
				tv_noData.setVisibility(View.VISIBLE);
			}
			page++;
			isLoading = false;
			// 数据加载完成后隐藏加载框
			progress_update_whitebg.setVisibility(View.GONE);
			ll_top.setClickable(true);
		};
	};

	private Handler handlerRefresh = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (list != null && list.size() > 0) {
				if (Long.valueOf(getSharedPreferences(UserInfoDB.USERTABLE,
						MODE_PRIVATE).getString(UserInfoDB.CAMPUS_TIME, "0")) < list
						.get(0).getTime()) {
					UserInfoDB.setUserInfo(CampusContactsActivity.this,
							UserInfoDB.CAMPUS_TIME, list.get(0).getTime() + "");
				}
				adapter = new CampusContactsAdapter(
						CampusContactsActivity.this, list, campusContactsTime,
						uid);
				lv_campus.setAdapter(adapter);
				lv_campus.setVisibility(View.VISIBLE);
				tv_noData.setVisibility(View.GONE);
			} else {
				lv_campus.setVisibility(View.GONE);
				lv_campus.setAdapter(null);
				tv_noData.setVisibility(View.VISIBLE);
			}
			progress_update_whitebg.setVisibility(View.GONE);
			isLoading = false;
			ll_top.setClickable(true);
		};
	};

	private Handler handler2 = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (adapter != null) {
				adapter.notifyDataSetChanged();
			}
			page++;
			isLoading = false;
		};
	};

	private Handler handlerAppraise = new Handler() {
		public void handleMessage(android.os.Message msg) {
			et_content.setText("");
			ll_appraise_edit.setVisibility(View.GONE);
			ib_add.setVisibility(View.VISIBLE);
			List<CampusContactsReplyInfo> appraiseList = list.get(position)
					.getAppraiseList();
			CampusContactsReplyInfo replyInfo = new CampusContactsReplyInfo();
			replyInfo.setName(username);
			replyInfo.setContent(content);
			replyInfo.setPname(pname);
			replyInfo.setId(repayId);
			replyInfo.setPid(pid);
			replyInfo.setUid(uid);
			if (appraiseList == null) {
				appraiseList = new ArrayList<CampusContactsReplyInfo>();
			}
			if (appraisePosition == -1) {
				appraiseList.add(replyInfo);
			} else {
				appraiseList.add(appraisePosition + 1, replyInfo);
			}
			appraisePosition = -1;
			pname = "";
			list.get(position).setAppraiseList(appraiseList);
			adapter.notifyDataSetChanged();
		};
	};

	private Handler handlerMessage = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (noReadCount > 0) {
				tv_count.setText(noReadCount + "");
				tv_count.setVisibility(View.VISIBLE);
			} else {
				tv_count.setVisibility(View.GONE);
			}
			loading = false;
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_campus_contacts);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);
		// 更改状态栏字体颜色为黑色
		MyUtils.setMiuiStatusBarDarkMode(this, true);
		EventBus.getDefault().register(this);

		WindowManager systemService = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		width = systemService.getDefaultDisplay().getWidth() / 5;

		uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.USERID, "");
		sid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.SCHOOL_ID, "");
		school = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.SCHOOL, "");
		username = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.NICK_NAME, "");

		// 设置用户头像
		File file = new File(MyConfig.IMAGGE_URI + uid + ".png");
		if (file.exists()) {
			Bitmap bitmap = BitmapFactory.decodeFile(MyConfig.IMAGGE_URI + uid
					+ ".png");
			cv_headShot.setImageBitmap(bitmap);
		}

		/*
		 * 下滑时当lv_campus滑动到顶部时头部view才显示出来
		 */
		scrollableLayout.getHelper().setCurrentScrollableContainer(
				new ScrollableContainer() {
					@Override
					public View getScrollableView() {
						return lv_campus;
					}
				});

		position = -1;
		appraisePosition = -1;
		pname = "";
		pid = "0";
		// isChange = false;
		mPosition = -1;

		rl_back.setOnClickListener(this);
		ll_top.setOnClickListener(this);
		ll_top.setClickable(false);
		rl_message.setOnClickListener(this);
		ib_add.setOnClickListener(this);
		ib_add.setOnTouchListener(this);
		lv_campus.setOnTouchListener(this);
		// 双击监听
		rl_topbar.setOnTouchListener(new OnDoubleClickListener(
				new DoubleClickCallback() {
					@Override
					public void onDoubleClick() {
						lv_campus.setSelection(0);
						scrollableLayout.scrollTo(0, 0);
						ll_top.setClickable(false);
						isLoading = true;
						mUpdate();
					}
				}));

		initView();
		// 刚进入页面时显示加载框
		progress_update_whitebg.setVisibility(View.VISIBLE);
		tv_noData.setVisibility(View.GONE);

		new Thread() {
			public void run() {
				userInfo = GetData
						.getCampusContactsUserInfo(MyConfig.URL_JSON_CAMPUS
								+ sid + "&type=" + type + "&uid=" + uid
								+ "&status=" + status + "&page=" + page);
				list = GetData.getCampusContactsInfos(MyConfig.URL_JSON_CAMPUS
						+ sid + "&type=" + type + "&uid=" + uid + "&status="
						+ status + "&page=" + page);
				campusContactsTime = GetData
						.getCampusContactsTime(MyConfig.URL_JSON_CAMPUS + sid
								+ "&type=" + type + "&uid=" + uid + "&status="
								+ status + "&page=" + page);
				if (!isDestroyed()) {
					handler.sendEmptyMessage(0);
				}
			};
		}.start();

	}

	/**
	 * 初始化列表
	 */
	private void initListView() {
		adapter = new CampusContactsAdapter(this, list, campusContactsTime, uid);
		lv_campus.setAdapter(adapter);
	}

	private void initView() {
		btn_send.setOnClickListener(this);
		tv_school.setText(school);
		cv_headShot.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CampusContactsActivity.this,
						CampusContactsPersonalActivity.class);
				startActivity(intent);
			}
		});
		String headShot = getSharedPreferences(UserInfoDB.USERTABLE,
				MODE_PRIVATE).getString(UserInfoDB.HEADSHOT, "");
		if (!headShot.equals("")) {
			// 设置头像和性别图标
			Glide.with(this).load(headShot).into(cv_headShot);
		}
		if (getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE).getString(
				UserInfoDB.SEX, "").equals("1")) {
			iv_sex.setImageDrawable(getResources().getDrawable(
					R.drawable.icon_female_picture));
		} else {
			iv_sex.setImageDrawable(getResources().getDrawable(
					R.drawable.icon_male_picture));
		}

		btn_all_top02.setOnClickListener(this);
		btn_feeling_top02.setOnClickListener(this);
		btn_trading_top02.setOnClickListener(this);
		btn_gogo_top02.setOnClickListener(this);
		btn_lostAndFound_top02.setOnClickListener(this);

		// 设置红条的宽度和高度
		LayoutParams params = new LayoutParams(width, 10);
		iv_redline_top02.setLayoutParams(params);

		// 监听列表滑动，滑动时隐藏评论输入框
		lv_campus.setOnScrollListener(new OnScrollListener() {
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
						List<CampusContactsInfo> list2 = null;

						public void run() {
							list2 = GetData
									.getCampusContactsInfos(MyConfig.URL_JSON_CAMPUS
											+ sid
											+ "&type="
											+ type
											+ "&uid="
											+ uid
											+ "&status="
											+ status
											+ "&page=" + page);
							if (list2 != null && list2.size() > 0) {
								for (int i = 0; i < list2.size(); i++) {
									list.add(list2.get(i));
								}
								if (!isDestroyed()) {
									handler2.sendEmptyMessage(0);
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

		case R.id.ll_top:
			if (status.equals("3")) {
				status = "1";
				tv_top01.setText("本校");
				tv_top02.setText("全国");
				ll_top.setClickable(false);
				// 红色线条平移动画
				iv_redline_top02.startAnimation(translateAnimation(0));
				// 记录当前红色线条在什么位置
				x = 0;
				resetBtnColor();
				btn_all_top02.setTextColor(getResources().getColor(
						R.color.red_fd3c49));
				type = 0;
				isLoading = true;
				mUpdate();
			} else {
				status = "3";
				tv_top01.setText("全国");
				tv_top02.setText("本校");
				ll_top.setClickable(false);
				// 红色线条平移动画
				iv_redline_top02.startAnimation(translateAnimation(0));
				// 记录当前红色线条在什么位置
				x = 0;
				resetBtnColor();
				btn_all_top02.setTextColor(getResources().getColor(
						R.color.red_fd3c49));
				type = 0;
				isLoading = true;
				mUpdate();
			}
			break;

		// 消息
		case R.id.rl_message:
			Intent messageIntent = new Intent(this,
					CampusContactsMessageActivity.class);
			startActivity(messageIntent);
			break;

		// 发表
		case R.id.ib_add:
			Intent publishIntent = new Intent(this,
					CampusContactsPublishActivity.class);
			startActivityForResult(publishIntent, 1);
			break;

		// 头像
		case R.id.cv_headShot:
			Intent intent = new Intent(this,
					CampusContactsPersonalActivity.class);
			startActivity(intent);
			break;

		// 全部
		case R.id.btn_all_top02:
			ll_top.setClickable(false);
			// 红色线条平移动画
			iv_redline_top02.startAnimation(translateAnimation(0));
			// 记录当前红色线条在什么位置
			x = 0;
			resetBtnColor();
			btn_all_top02.setTextColor(getResources().getColor(
					R.color.red_fd3c49));
			type = 0;
			isLoading = true;
			mUpdate();
			break;

		// 心情
		case R.id.btn_feeling_top02:
			ll_top.setClickable(false);
			// 红色线条平移动画
			iv_redline_top02.startAnimation(translateAnimation(1));
			// 记录当前红色线条在什么位置
			x = width;
			resetBtnColor();
			btn_feeling_top02.setTextColor(getResources().getColor(
					R.color.red_fd3c49));
			type = 1;
			isLoading = true;
			mUpdate();
			break;

		// 交易
		case R.id.btn_trading_top02:
			ll_top.setClickable(false);
			// 红色线条平移动画
			iv_redline_top02.startAnimation(translateAnimation(2));
			// 记录当前红色线条在什么位置
			x = 2 * width;
			resetBtnColor();
			btn_trading_top02.setTextColor(getResources().getColor(
					R.color.red_fd3c49));
			type = 2;
			isLoading = true;
			mUpdate();
			break;

		// GOGO
		case R.id.btn_gogo_top02:
			ll_top.setClickable(false);
			// 红色线条平移动画
			iv_redline_top02.startAnimation(translateAnimation(3));
			// 记录当前红色线条在什么位置
			x = 3 * width;
			resetBtnColor();
			btn_gogo_top02.setTextColor(getResources().getColor(
					R.color.red_fd3c49));
			type = 3;
			isLoading = true;
			mUpdate();
			break;

		// 失物招领
		case R.id.btn_lostAndFound_top02:
			ll_top.setClickable(false);
			// 红色线条平移动画
			iv_redline_top02.startAnimation(translateAnimation(4));
			// 记录当前红色线条在什么位置
			x = 4 * width;
			resetBtnColor();
			btn_lostAndFound_top02.setTextColor(getResources().getColor(
					R.color.red_fd3c49));
			type = 4;
			isLoading = true;
			mUpdate();
			break;

		// 评论内容发送
		case R.id.btn_send:
			content = et_content.getText().toString().trim();
			if (content.equals("")) {
				ToastUtil.showToast(CampusContactsActivity.this,"请输入评论内容");
				return;
			}

			// 隐藏软键盘
			MyUtils.hideSoftInput(this, et_content);
			progress_update_whitebg.setVisibility(View.VISIBLE);
			new Thread() {
				public void run() {
					postCampusContacts(MyConfig.URL_POST_CAMPUS_APPRAISE,
							content);
				};
			}.start();
			break;
		}
	}

	/**
	 * 按钮字体颜色还原初始化
	 */
	private void resetBtnColor() {
		btn_all_top02.setTextColor(Color.BLACK);
		btn_feeling_top02.setTextColor(Color.BLACK);
		btn_trading_top02.setTextColor(Color.BLACK);
		btn_gogo_top02.setTextColor(Color.BLACK);
		btn_lostAndFound_top02.setTextColor(Color.BLACK);
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

	private void mUpdate() {
		mPosition = -1;
		page = 2;
		// if (progress_update_whitebg.getVisibility() == View.GONE) {
		progress_update_whitebg.setVisibility(View.VISIBLE);
		// }
		tv_noData.setVisibility(View.GONE);
		new Thread() {
			public void run() {
				// 停留0.3秒让红线滑动不会卡顿
				SystemClock.sleep(300);
				list = GetData.getCampusContactsInfos(MyConfig.URL_JSON_CAMPUS
						+ sid + "&type=" + type + "&uid=" + uid + "&status="
						+ status + "&page=" + 1);
				campusContactsTime = GetData
						.getCampusContactsTime(MyConfig.URL_JSON_CAMPUS + sid
								+ "&type=" + type + "&uid=" + uid + "&status="
								+ status + "&page=" + 1);
				if (!isDestroyed()) {
					handlerRefresh.sendEmptyMessage(0);
				}
			};
		}.start();

		if (!loading) {
			loading = true;
			new Thread() {
				public void run() {
					noReadCount = getNoReadCount(MyConfig.URL_JSON_CAMPUS_NOREAD_COUNT
							+ uid);
					if (!isDestroyed()) {
						handlerMessage.sendEmptyMessage(0);
					}
				};
			}.start();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == 1) {
			mUpdate();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 获取校园圈没读的条数
	 */
	private int getNoReadCount(String urlString) {
		InputStream is = null;
		int count = 0;
		try {
			is = new URL(urlString).openStream();
			String jsonString = readStream(is);
			JSONObject jsonObject = new JSONObject(jsonString);
			count = jsonObject.optInt("nums", 0);
		} catch (JSONException e) {
			e.printStackTrace();
			return 0;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return 0;
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		} finally {
			try {
				if (is != null) {
					is.close();
					is = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return count;
	}

	private String readStream(InputStream is) {
		InputStreamReader isr = null;
		BufferedReader br = null;
		String result = "";
		try {
			String line = "";
			isr = new InputStreamReader(is, "utf-8");
			br = new BufferedReader(isr);
			while ((line = br.readLine()) != null) {
				result += line;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (br != null) {
					br.close();
				}
				if (isr != null) {
					isr.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 发表评论
	 * 
	 * @param uploadHost
	 */
	private void postCampusContacts(String uploadHost, String content) {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("uid", uid);
		params.addBodyParameter("mid", list.get(position).getId());
		params.addBodyParameter("content", content);
		if (appraisePosition == -1) {
			params.addBodyParameter("pid", "0");
			params.addBodyParameter("puid", "0");
			pid = "0";
			pname = "";
		} else {
			pid = list.get(position).getAppraiseList().get(appraisePosition)
					.getId();
			pname = list.get(position).getAppraiseList().get(appraisePosition)
					.getName();
			params.addBodyParameter("pid", pid);
			params.addBodyParameter("puid", list.get(position)
					.getAppraiseList().get(appraisePosition).getUid());
		}
		params.addBodyParameter("username", username);
		httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						LogUtils.d("responseInfo",responseInfo.result);
						progress_update_whitebg.setVisibility(View.GONE);
						String result = "";
						try {
							JSONObject jsonObject = new JSONObject(
									responseInfo.result);
							result = jsonObject.getString("status");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (!result.equals("0") && !result.equals("-1")) {
							repayId = result;
							if (!isDestroyed()) {
								handlerAppraise.sendEmptyMessage(0);
							}
						} else {
							ToastUtil.showToast(CampusContactsActivity.this,"评论失败");
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.d("arg1",arg1.toString());
						progress_update_whitebg.setVisibility(View.GONE);
						ToastUtil.showToast(CampusContactsActivity.this,"评论失败");
					}
				});
	}

	// private void saveUserBg(Bitmap bitmap) throws IOException {
	// File file = new File(MyConfig.FILE_URI);
	// if (!file.exists()) {
	// file.mkdirs();
	// }
	// File dir = new File(MyConfig.IMAGGE_URI + uid + "timelinebg.png");
	// if (!dir.exists()) {
	// dir.createNewFile();
	// }
	// FileOutputStream iStream = new FileOutputStream(dir);
	// bitmap.compress(CompressFormat.PNG, 100, iStream);
	// iStream.close();
	// iStream = null;
	// file = null;
	// dir = null;
	// }

//	public void onEventMainThread(MEventCampusContactsMessage event) {
//		if (event.getFlag()) {
//			if (!loading) {
//				loading = true;
//				new Thread() {
//					public void run() {
//						noReadCount = getNoReadCount(MyConfig.URL_JSON_CAMPUS_NOREAD_COUNT
//								+ uid);
//						if (!isDestroyed()) {
//							handlerMessage.sendEmptyMessage(0);
//						}
//					};
//				}.start();
//			}
//		}
//	}

	public void onEventMainThread(MEventCampusContactsAdd event) {
		if (!event.isShow()) {
			ib_add.setVisibility(View.GONE);
			ll_appraise_edit.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 校园圈内容更新(我有新发布或者屏蔽某用户)
	 * 
	 * @param event
	 */
	public void onEventMainThread(MEventCampusContactsChange event) {
		if (event.isChange()) {
			mUpdate();
		}
	}

	/**
	 * 评论内容更新
	 * 
	 * @param event
	 */
	public void onEventMainThread(MEventCampusContactsAppraiseChange event) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getId().equals(event.getMid())) {
				list.get(i).setAppraiseList(event.getAppraiseList());
				adapter.notifyDataSetChanged();
			}
		}
	}

	/**
	 * 我的圈圈删除后更新
	 * 
	 * @param event
	 */
	public void onEventMainThread(MEventCampusContactsAllDel event) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getId().equals(event.getDelCid())) {
				list.remove(i);
				adapter.notifyDataSetChanged();
				break;
			}
		}
	}

	@Override
	protected void onResume() {
		if (mPosition != -1) {
			list.get(mPosition).setShowAppraise(false);
			adapter.notifyDataSetChanged();
			mPosition = -1;
		}
		if (!loading) {
			loading = true;
			new Thread() {
				public void run() {
					noReadCount = getNoReadCount(MyConfig.URL_JSON_CAMPUS_NOREAD_COUNT
							+ uid);
					if (!isDestroyed()) {
						handlerMessage.sendEmptyMessage(0);
					}
				};
			}.start();
		}
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		et_content = null;
		pname = null;
		pid = null;
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN
				&& v.getId() == R.id.ib_add) {
			ib_add.startAnimation(MyAnimation.getScaleAnimationDown());
		}
		if (mPosition != -1) {
			list.get(mPosition).setShowAppraise(false);
			adapter.notifyDataSetChanged();
			mPosition = -1;
		}
		if (ll_appraise_edit.getVisibility() == View.VISIBLE) {
			ll_appraise_edit.setVisibility(View.GONE);
			ib_add.setVisibility(View.VISIBLE);
			// 隐藏软键盘
			MyUtils.hideSoftInput(CampusContactsActivity.this, et_content);
		}
		return false;
	}

}
