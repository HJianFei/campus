package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.adapters.PictureManageAdapter;
import com.loosoo100.campus100.anyevent.MEventPicChange;
import com.loosoo100.campus100.anyevent.MEventPicManageChange;
import com.loosoo100.campus100.card.PictureWallInfo;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyAnimation;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.CustomDialog;
import com.loosoo100.campus100.view.spinkit.SpinKitView;
import com.loosoo100.campus100.view.spinkit.style.Wave;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 
 * @author yang 许愿管理activity
 */
public class PictureManageActivity extends Activity implements OnClickListener,
		OnTouchListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view
	@ViewInject(R.id.rl_back)
	private RelativeLayout rl_back; // 返回
	@ViewInject(R.id.lv_manage)
	private ListView lv_manage; // 列表
	@ViewInject(R.id.progress_small)
	private RelativeLayout progress_small; // 点击不同分类的加载进度条
	@ViewInject(R.id.progress)
	private SpinKitView progress; // 点击不同分类的加载进度条
	@ViewInject(R.id.progress_update)
	private RelativeLayout progress_update; // 加载动画
	@ViewInject(R.id.ib_add)
	private ImageButton ib_add; // 添加

	private List<PictureWallInfo> list;
	private PictureManageAdapter adapter;

	private String uid = "";

	private int page = 2;
	private boolean isLoading = true;

	private int mPosition;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (list != null && list.size() > 0) {
				initListView();
				lv_manage.setVisibility(View.VISIBLE);
			} else {
				lv_manage.setVisibility(View.GONE);
			}
			page = 2;
			progress_small.setVisibility(View.GONE);
			isLoading = false;
		};
	};

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
		setContentView(R.layout.activity_picture_manage);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);
		// 更改状态栏字体颜色为黑色
		MyUtils.setMiuiStatusBarDarkMode(this, true);
		EventBus.getDefault().register(this);

		uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.USERID, "");
		// 设置加载动画样式
		Wave wave = new Wave();
		progress.setIndeterminateDrawable(wave);
		progress_small.setVisibility(View.VISIBLE);

		initView();

		new Thread() {
			public void run() {
				list = GetData
						.getPictureWallManageInfos(MyConfig.URL_JSON_PICTURE_WALL_MANAGE
								+ uid);
				if (!isDestroyed()) {
					handler.sendEmptyMessage(0);
				}
			};
		}.start();

	}

	private void initView() {
		rl_back.setOnClickListener(this);
		ib_add.setOnClickListener(this);
		ib_add.setOnTouchListener(this);

		ImageView imageView = new ImageView(this);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, 100);
		imageView.setLayoutParams(params);
		lv_manage.addFooterView(imageView);

		lv_manage.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == list.size()) {
					return;
				}
				mPosition = position;
				Intent intent = new Intent(PictureManageActivity.this,
						PictureManageDetailActivity.class);
				intent.putExtra("pic", list.get(position).getPicture());
				intent.putExtra("dream", list.get(position).getDream());
				intent.putExtra("supportCount", list.get(position)
						.getSupportCount());
				intent.putExtra("opposeCount", list.get(position)
						.getOpposeCount());
				intent.putExtra("time", list.get(position).getTime());
				intent.putExtra("action", list.get(position).getAction());
				intent.putExtra("wid", list.get(position).getId());
				startActivity(intent);
			}
		});

		lv_manage.setOnScrollListener(new OnScrollListener() {

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
						private List<PictureWallInfo> list2;

						public void run() {
							list2 = GetData
									.getPictureWallManageInfos(MyConfig.URL_JSON_PICTURE_WALL_MANAGE
											+ uid + "&page=" + page);
							if (list2 != null && list2.size() > 0) {
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

	private void initListView() {
		adapter = new PictureManageAdapter(this, list);
		lv_manage.setAdapter(adapter);

		lv_manage.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				CustomDialog.Builder builder = new CustomDialog.Builder(
						PictureManageActivity.this);
				builder.setMessage("是否确认删除");
				builder.setPositiveButton("是",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								progress_update.setVisibility(View.VISIBLE);
								new Thread() {
									public void run() {
										doDelete(
												MyConfig.URL_POST_PICTURE_WALL_DELETE
														+ list.get(position)
																.getId(),
												position);
									};
								}.start();
							}
						});
				builder.setNegativeButton("否", null);
				builder.create().show();
				return true;
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			this.finish();
			break;

		case R.id.ib_add:
			Intent intent = new Intent(this, PicturePublishActivity.class);
			startActivity(intent);
			break;
		}
	}

	private void doDelete(String url, final int position) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpRequest.HttpMethod.GET, url,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.d("error",arg1.toString());
						ToastUtil.showToast(PictureManageActivity.this,"删除失败");
						progress_update.setVisibility(View.GONE);
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						LogUtils.d("responseInfo",arg0.result);
						progress_update.setVisibility(View.GONE);
						String result = "";
						try {
							JSONObject jsonObject = new JSONObject(arg0.result);
							result = jsonObject.getString("status");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (result.equals("1")) {
							ToastUtil.showToast(PictureManageActivity.this,"删除成功");
							EventBus.getDefault().post(
									new MEventPicChange(true));
							list.remove(position);
							adapter.notifyDataSetChanged();
						} else {
							ToastUtil.showToast(PictureManageActivity.this,"删除失败");
						}
					}
				});
	}

	public void onEventMainThread(MEventPicManageChange event) {
		if (event.getChange()) {
			list.remove(mPosition);
			adapter.notifyDataSetChanged();
		}
	}

	public void onEventMainThread(MEventPicChange event) {
		if (event.getChange()) {
			new Thread() {
				public void run() {
					list = GetData
							.getPictureWallManageInfos(MyConfig.URL_JSON_PICTURE_WALL_MANAGE
									+ uid);
					if (!isDestroyed()) {
						handler.sendEmptyMessage(0);
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

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN
				&& v.getId() == R.id.ib_add) {
			ib_add.startAnimation(MyAnimation.getScaleAnimationDown());
		}
		return false;
	}

}
