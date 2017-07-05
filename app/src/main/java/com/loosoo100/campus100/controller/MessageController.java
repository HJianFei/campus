package com.loosoo100.campus100.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loosoo100.campus100.R;
import com.loosoo100.campus100.activities.CommActivityDetailFreeActivity;
import com.loosoo100.campus100.activities.CommActivityDetailTogetherActivity;
import com.loosoo100.campus100.activities.HomeActivity;
import com.loosoo100.campus100.activities.MessageActivity;
import com.loosoo100.campus100.adapters.CommActiListAdapter;
import com.loosoo100.campus100.beans.CommunityActivityInfo;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 消息-消息
 * 
 * @author yang
 * 
 */
public class MessageController extends BaseController implements
		OnClickListener {

	private LayoutInflater inflater;

	private HomeActivity activity;

	private LinearLayout ll_system;
	private EditText et_search;
	private ImageView iv_search;
	private ListView lv_message;
	private ImageView iv_redPoint;
	private TextView tv_time;

	private CommActiListAdapter adapter;
	private List<CommunityActivityInfo> list;
	private String uid = "";
	private boolean isLoading = true;
	private int page = 1;
	private int noReadCount = 0;	//系统消息未读数

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (noReadCount > 0) {
				iv_redPoint.setVisibility(View.VISIBLE);
			} else {
				iv_redPoint.setVisibility(View.GONE);
			}
		};
	};

//	private Handler handlerRefresh = new Handler() {
//		public void handleMessage(android.os.Message msg) {
//			if (adapter != null) {
//				adapter.notifyDataSetChanged();
//			}
//			isLoading = false;
//			page++;
//		};
//	};

	public MessageController(Context context) {
		super(context);

		activity = (HomeActivity) context;
		init();

		uid = activity.getSharedPreferences(UserInfoDB.USERTABLE,
				activity.MODE_PRIVATE).getString(UserInfoDB.USERID, "");

	}

	@Override
	protected View initView(Context context) {
		inflater = LayoutInflater.from(context);
		return inflater.inflate(R.layout.controller_message_message, null);
	}

	public void init() {
		ll_system = (LinearLayout) mRootView.findViewById(R.id.ll_system);
		et_search = (EditText) mRootView.findViewById(R.id.et_search);
		iv_search = (ImageView) mRootView.findViewById(R.id.iv_search);
		lv_message = (ListView) mRootView.findViewById(R.id.lv_message);
		iv_redPoint = (ImageView) mRootView.findViewById(R.id.iv_redPoint);
		tv_time = (TextView) mRootView.findViewById(R.id.tv_time);

		iv_search.setOnClickListener(this);
		ll_system.setOnClickListener(this);
	}

	@Override
	public void initData() {
		// new Thread() {
		// public void run() {
		// list = GetData
		// .getBossHomeActivityInfos(MyConfig.URL_JSON_ACTI_MYCOLLECT_BOSS
		// + cuid);
		// if (!activity.isDestroyed()) {
		// handler.sendEmptyMessage(0);
		// }
		// };
		// }.start();
		super.initData();
	}

//	private void initListView() {
//		adapter = new CommActiListAdapter(activity, list);
//		lv_message.setAdapter(adapter);
//
//		lv_message.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				if (list.get(position).getClassify() == 0) {
//					Intent intent = new Intent(activity,
//							CommActivityDetailTogetherActivity.class);
//					intent.putExtra("aid", list.get(position).getId());
//					activity.startActivity(intent);
//				} else {
//					Intent intent = new Intent(activity,
//							CommActivityDetailFreeActivity.class);
//					intent.putExtra("aid", list.get(position).getId());
//					activity.startActivity(intent);
//				}
//			}
//		});
//
//		lv_message.setOnScrollListener(new OnScrollListener() {
//
//			@Override
//			public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//			}
//
//			@Override
//			public void onScroll(AbsListView view, int firstVisibleItem,
//					int visibleItemCount, int totalItemCount) {
//				if (firstVisibleItem + visibleItemCount == totalItemCount
//						&& totalItemCount > 0 && !isLoading) {
//					isLoading = true;
//					new Thread() {
//						public void run() {
//							List<CommunityActivityInfo> list2 = GetData
//									.getBossHomeActivityInfos(MyConfig.URL_JSON_ACTI_MYCOLLECT_BOSS
//											+ uid + "&page=" + page);
//							if (list2 != null && list2.size() > 0) {
//								for (int i = 0; i < list2.size(); i++) {
//									list.add(list2.get(i));
//								}
//								if (!activity.isDestroyed()) {
//									handlerRefresh.sendEmptyMessage(0);
//								}
//							}
//						};
//					}.start();
//				}
//			}
//		});
//	}

	/**
	 * 更新系统消息未读数
	 */
	public void updateNoReadCount(){
		new Thread() {
			public void run() {
				noReadCount = getNoReadCount(MyConfig.URL_JSON_MESSAGE_NOREAD
						+ uid);
				if (!activity.isDestroyed()) {
					handler.sendEmptyMessage(0);
				}
			};
		}.start();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_search:
			// TODO

			break;

		case R.id.ll_system:
			Intent intent = new Intent(activity,MessageActivity.class);
			activity.startActivity(intent);
			break;

		}
	}

	/**
	 * 获取系统消息没读的条数
	 */
	private int getNoReadCount(String urlString) {
		InputStream is = null;
		int count = 0;
		try {
			is = new URL(urlString).openStream();
			String jsonString = readStream(is);
			JSONObject jsonObject = new JSONObject(jsonString);
			count = jsonObject.optInt("status", 0);
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


}
