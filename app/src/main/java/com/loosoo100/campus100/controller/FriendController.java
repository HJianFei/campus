package com.loosoo100.campus100.controller;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.activities.CampusContactsFriendActivity;
import com.loosoo100.campus100.activities.HomeActivity;
import com.loosoo100.campus100.adapters.CampusContactsMyFriendAdapter;
import com.loosoo100.campus100.anyevent.MEventCampusContactsMyFriend;
import com.loosoo100.campus100.beans.CampusContactsLoveInfo;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.view.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 消息-社团
 * 
 * @author yang
 * 
 */
public class FriendController extends BaseController {

	private LayoutInflater inflater;

	private HomeActivity activity;

	private ListView listView;
	private RelativeLayout progress; // 加载动画

	private boolean isLoading = true;
	private int page = 1;
	private String uid = ""; // 用户ID

	private CampusContactsMyFriendAdapter adapter;

	private List<CampusContactsLoveInfo> list;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (list != null && list.size() > 0) {
				initListView();
				listView.setVisibility(View.VISIBLE);
			} else {
				listView.setVisibility(View.GONE);
			}
			page = 2;
			isLoading = false;
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
			}
			page++;
			isLoading = false;
		};
	};

	public FriendController(Context context) {
		super(context);

		activity = (HomeActivity) context;

		init();

		uid = activity.getSharedPreferences(UserInfoDB.USERTABLE,
				activity.MODE_PRIVATE).getString(UserInfoDB.USERID, "");

	}

	@Override
	protected View initView(Context context) {
		inflater = LayoutInflater.from(context);
		return inflater.inflate(R.layout.controller_message_friend, null);
	}

	public void init() {
		listView = (ListView) mRootView.findViewById(R.id.listView);
		progress = (RelativeLayout) mRootView.findViewById(R.id.progress);
	}

	@Override
	public void initData() {
		if (list == null || list.size() == 0) {
			progress.setVisibility(View.VISIBLE);
			new Thread() {
				@Override
				public void run() {
					list = GetData
							.getCampusContactsFriendInfos(MyConfig.URL_JSON_CAMPUS_FRIEND
									+ uid);
					if (!activity.isDestroyed()) {
						handler.sendEmptyMessage(0);
					}
				}
			}.start();
		}
		super.initData();
	}

	private void initListView() {
		adapter = new CampusContactsMyFriendAdapter(activity, list);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(activity,
						CampusContactsFriendActivity.class);
				intent.putExtra("muid", list.get(position).getUid());
				activity.startActivity(intent);
			}
		});

		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				CustomDialog.Builder builderSure = new CustomDialog.Builder(
						activity);
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
							ToastUtil.showToast(activity,"删除成功");
						} else {
							ToastUtil.showToast(activity,"操作失败");
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.d("error",arg1.toString());
						ToastUtil.showToast(activity,"操作失败");
					}
				});
	}
}
