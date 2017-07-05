package com.loosoo100.campus100.activities;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.adapters.CampusContactsMyFriendAdapter;
import com.loosoo100.campus100.anyevent.MEventCampusContactsAllDel;
import com.loosoo100.campus100.anyevent.MEventCampusContactsMyFriend;
import com.loosoo100.campus100.anyevent.MEventCampusNoReadFriend;
import com.loosoo100.campus100.beans.CampusContactsLoveInfo;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.CustomDialog;

import de.greenrobot.event.EventBus;

/**
 * 
 * @author yang 校园圈我的友友activity
 */
public class CampusContactsMyFriendActivity extends Activity implements
		OnClickListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view
	@ViewInject(R.id.rl_back)
	private RelativeLayout rl_back;
	@ViewInject(R.id.btn_request)
	private Button btn_request;
	@ViewInject(R.id.listView)
	private ListView listView; // 列表
	// @ViewInject(R.id.rsmLv)
	// private RefreshSwipeMenuListView rsmLv; // 列表
	@ViewInject(R.id.rl_progress)
	private RelativeLayout rl_progress; // 加载动画
	@ViewInject(R.id.iv_circle)
	private ImageView iv_circle; // 引导红点

	private int page = 1;

	private boolean isLoading = true;

	private String uid = ""; // 用户ID

	private CampusContactsMyFriendAdapter adapter;

	private List<CampusContactsLoveInfo> list;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (list != null && list.size() > 0) {
				initListView();
				// initListView02();
				listView.setVisibility(View.VISIBLE);
				// rsmLv.setVisibility(View.VISIBLE);
			} else {
				listView.setVisibility(View.GONE);
				// rsmLv.setVisibility(View.GONE);
			}
			page = 2;
			isLoading = false;
			rl_progress.setVisibility(View.GONE);
		};
	};

	/*
	 * 加载更多后更新数据
	 */
	private Handler handlerRefresh = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (adapter != null) {
				adapter.notifyDataSetChanged();
			}
			page++;
			isLoading = false;
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_campus_contacts_myfriend);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);
		MyUtils.setMiuiStatusBarDarkMode(this, true);
		EventBus.getDefault().register(this);

		UserInfoDB.setUserInfo(this, UserInfoDB.CAMPUS_NOREAD_FRIEND, "0");

		uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.USERID, "");

		rl_progress.setVisibility(View.VISIBLE);

		initView();

		// 数据后台加载
		new Thread() {
			@Override
			public void run() {
				if (!isDestroyed()) {
					list = GetData
							.getCampusContactsFriendInfos(MyConfig.URL_JSON_CAMPUS_FRIEND
									+ uid);
					if (!isDestroyed()) {
						handler.sendEmptyMessage(0);
					}
				}
			}
		}.start();
	}

	private void initListView() {
		adapter = new CampusContactsMyFriendAdapter(this, list);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(CampusContactsMyFriendActivity.this,
						CampusContactsFriendActivity.class);
				intent.putExtra("muid", list.get(position).getUid());
				startActivity(intent);
			}
		});

		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				CustomDialog.Builder builderSure = new CustomDialog.Builder(
						CampusContactsMyFriendActivity.this);
				builderSure.setMessage("是否确认断绝关系");
				builderSure.setPositiveButton("是",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								new Thread() {
									public void run() {
										postDel(MyConfig.URL_POST_CAMPUS_FRIEND_DEL,
												position);
									};
								}.start();
							}
						});
				builderSure.setNegativeButton("否", null);
				builderSure.create().show();
				return true;
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
							List<CampusContactsLoveInfo> list2 = GetData
									.getCampusContactsFriendInfos(MyConfig.URL_JSON_CAMPUS_FRIEND
											+ uid + "&p=" + page);
							if (list2 != null && list2.size() > 0) {
								for (int i = 0; i < list2.size(); i++) {
									list.add(list2.get(i));
								}
								handlerRefresh.sendEmptyMessage(0);
							}
						};
					}.start();
				}
			}
		});

	}

	private void initView() {
		rl_back.setOnClickListener(this);
		btn_request.setOnClickListener(this);
	}

	// private void initListView02() {
	// adapter = new CampusContactsMyFriendAdapter(this, list);
	// rsmLv.setAdapter(adapter);
	// // rsmLv.setListViewMode(RefreshSwipeMenuListView.HEADER);
	// // rsmLv.setOnRefreshListener(this);
	// rsmLv.setPullLoadEnable(false);
	// rsmLv.setPullRefreshEnable(false);
	//
	// SwipeMenuCreator creator = new SwipeMenuCreator() {
	// @Override
	// public void create(SwipeMenu menu) {
	// // 创建删除选项
	// SwipeMenuItem argeeItem = new SwipeMenuItem(
	// getApplicationContext());
	// argeeItem.setBackground(new ColorDrawable(getResources()
	// .getColor(R.color.red_fd3c49)));
	// argeeItem.setWidth(dp2px(80, getApplicationContext()));
	// argeeItem.setTitle("删除");
	// argeeItem.setTitleSize(16);
	// argeeItem.setTitleColor(Color.WHITE);
	// menu.addMenuItem(argeeItem);
	// }
	// };
	//
	// rsmLv.setMenuCreator(creator);
	//
	// rsmLv.setOnItemClickListener(new OnItemClickListener() {
	//
	// @Override
	// public void onItemClick(AdapterView<?> parent, View view,
	// int position, long id) {
	// Intent intent = new Intent(CampusContactsMyFriendActivity.this,
	// CampusContactsFriendActivity.class);
	// intent.putExtra("muid", list.get(position-1).getUid());
	// startActivity(intent);
	// }
	// });
	//
	// rsmLv.setOnMenuItemClickListener(new
	// RefreshSwipeMenuListView.OnMenuItemClickListener() {
	// @Override
	// public void onMenuItemClick(final int position, SwipeMenu menu,
	// int index) {
	// switch (index) {
	// case 0:
	// CustomDialog.Builder builderSure = new CustomDialog.Builder(
	// CampusContactsMyFriendActivity.this);
	// builderSure.setMessage("是否确认断绝关系");
	// builderSure.setPositiveButton("是",
	// new DialogInterface.OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog,
	// int which) {
	// new Thread() {
	// public void run() {
	// postDel(MyConfig.URL_POST_CAMPUS_FRIEND_DEL,
	// position);
	// };
	// }.start();
	// }
	// });
	// builderSure.setNegativeButton("否", null);
	// builderSure.create().show();
	// break;
	//
	// }
	// }
	// });
	// }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			this.finish();
			break;

		case R.id.btn_request:
			Intent intent = new Intent(this,
					CampusContactsMyFriendRequestActivity.class);
			startActivity(intent);
			break;

		}
	}

	// @Override
	// public void onScrollStateChanged(AbsListView view, int scrollState) {
	//
	// }
	//
	// @Override
	// public void onScroll(AbsListView view, int firstVisibleItem,
	// int visibleItemCount, int totalItemCount) {
	// if (firstVisibleItem + visibleItemCount == totalItemCount
	// && totalItemCount > 0 && !isLoading) {
	// isLoading = true;
	// new Thread() {
	// public void run() {
	// List<CampusContactsLoveInfo> list2 = GetData
	// .getcCampusContactsLoveInfos(MyConfig.URL_JSON_CAMPUS_FRIEND
	// + uid + "&p=" + page);
	// if (list2 != null && list2.size() > 0) {
	// for (int i = 0; i < list2.size(); i++) {
	// list.add(list2.get(i));
	// }
	// handlerRefresh.sendEmptyMessage(0);
	// }
	// };
	// }.start();
	// }
	// }

	public void onEventMainThread(MEventCampusContactsMyFriend event) {
		if (event.getChange()) {
			new Thread() {
				@Override
				public void run() {
					if (!isDestroyed()) {
						list = GetData
								.getCampusContactsFriendInfos(MyConfig.URL_JSON_CAMPUS_FRIEND
										+ uid);
						if (!isDestroyed()) {
							handler.sendEmptyMessage(0);
						}
					}
				}
			}.start();
		}
	}

	/**
	 * 红点是否显示
	 * 
	 * @param event
	 */
	public void onEventMainThread(MEventCampusNoReadFriend event) {
		if (event.isChange()) {
			if (!getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
					.getString(UserInfoDB.CAMPUS_NOREAD_FRIEND_DETAIL, "0")
					.equals("0")) {
				iv_circle.setVisibility(View.VISIBLE);
			} else {
				iv_circle.setVisibility(View.GONE);
			}
		}
	}

	public void onEventMainThread(MEventCampusContactsAllDel event) {
		if (event.isChange()) {
			this.finish();
		}
	}

	@Override
	protected void onResume() {
		if (!getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.CAMPUS_NOREAD_FRIEND_DETAIL, "0").equals(
						"0")) {
			iv_circle.setVisibility(View.VISIBLE);
		} else {
			iv_circle.setVisibility(View.GONE);
		}
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	/**
	 * 删除友友
	 * 
	 * @param uploadHost
	 */
	private void postDel(String uploadHost, final int position) {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("uid", uid);
		params.addBodyParameter("touid", list.get(position).getUid());
		httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						LogUtils.d("responseInfo",responseInfo.result);
						String result = "";
						try {
							JSONObject jsonObject = new JSONObject(
									responseInfo.result);
							result = jsonObject.getString("status");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (result.equals("1")) {
							list.remove(position);
							adapter.notifyDataSetChanged();
							if (list.size() == 0) {
								listView.setVisibility(View.GONE);
							}
							EventBus.getDefault().post(
									new MEventCampusContactsMyFriend(true));
							ToastUtil.showToast(CampusContactsMyFriendActivity.this,"删除成功");
						} else {
							ToastUtil.showToast(CampusContactsMyFriendActivity.this,"操作失败");
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.d("error",arg1.toString());
						ToastUtil.showToast(CampusContactsMyFriendActivity.this,"操作失败");
					}
				});
	}

	public int dp2px(int dp, Context context) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				context.getResources().getDisplayMetrics());
	}
}
