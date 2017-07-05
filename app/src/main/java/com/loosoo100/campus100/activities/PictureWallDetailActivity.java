package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.anyevent.MEventPicReport;
import com.loosoo100.campus100.card.CardAdapter;
import com.loosoo100.campus100.card.PictureWallInfo;
import com.loosoo100.campus100.card.SwipeFlingAdapterView;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 
 * @author yang 照片墙详情activity
 */
public class PictureWallDetailActivity extends Activity implements
		OnClickListener {
	// @ViewInject(R.id.iv_empty)
	// private ImageView iv_empty; // 空view
	@ViewInject(R.id.iv_empty2)
	private ImageView iv_empty2; // 空view2
	@ViewInject(R.id.ib_close)
	private ImageButton ib_close; // 关闭按钮
	@ViewInject(R.id.btn_type)
	private Button btn_type; // 本校或全国排行按钮
	@ViewInject(R.id.btn_change)
	private Button btn_change; // 换一批或下一页按钮
	@ViewInject(R.id.tv_index)
	private TextView tv_index; // 当前显示照片序号
	@ViewInject(R.id.tv_count)
	private TextView tv_count; // 当前页总照片数量
	@ViewInject(R.id.frame)
	SwipeFlingAdapterView flingContainer;
	@ViewInject(R.id.progress)
	private RelativeLayout progress;
	// @ViewInject(R.id.rl_root)
	// private RelativeLayout rl_root;

	private ImageView iv_like;
	private ImageView iv_dislike;
	private TextView tv_like;
	private TextView tv_dislike;

	private int index = 1;
	private String sid = ""; // 学校ID
	private String uid = ""; // 用户ID
	private int count; // 照片数量

	private CardAdapter cardAdapter;
	private List<PictureWallInfo> list;

	private int page = 1;
	private String type = ""; // 本校照片或全国排行
	private Intent intent;
	private String wid = "";

	private boolean support = false;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			tv_index.setText("1");
			if (list != null && list.size() > 0) {
				initView();
				// flingContainer.setVisibility(View.VISIBLE);
			} else {
				tv_count.setText("0");
				tv_index.setText("0");
				ToastUtil.showToast(PictureWallDetailActivity.this,"没有更多心愿");
				// flingContainer.setVisibility(View.GONE);
			}
			btn_type.setClickable(true);
			btn_change.setClickable(true);
			page++;
			progress.setVisibility(View.GONE);
		};
	};

	private Handler handler2 = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (support) {
				iv_like.setImageDrawable(PictureWallDetailActivity.this
						.getResources().getDrawable(
								R.drawable.icon_collect_picture_red));
				tv_like.setText((list.get(0).getSupportCount() + 1) + "");
				list.get(0).setAction("1");
				list.get(0).setSupportCount(list.get(0).getSupportCount() + 1);
			} else {
				iv_dislike.setImageDrawable(PictureWallDetailActivity.this
						.getResources().getDrawable(
								R.drawable.icon_bad_color_pictre));
				tv_dislike.setText((list.get(0).getOpposeCount() + 1) + "");
				list.get(0).setAction("2");
				list.get(0).setOpposeCount(list.get(0).getOpposeCount() + 1);
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picture_wall_detail);
		ViewUtils.inject(this);

		// MyUtils.setStatusBarHeight(this, iv_empty);
		MyUtils.setStatusBarHeight(this, iv_empty2);

		EventBus.getDefault().register(this);

		// UserInfoDB.setUserInfo(this, UserInfoDB.PICTURE_FIRST, "1");
		intent = getIntent();
		page = getIntent().getExtras().getInt("page");
		type = getIntent().getExtras().getString("type");
		wid = getIntent().getExtras().getString("wid");

		progress.setVisibility(View.VISIBLE);
		sid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.SCHOOL_ID_PICTURE, "");
		uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.USERID, "");

		ib_close.setOnClickListener(this);
		btn_type.setOnClickListener(this);
		btn_change.setOnClickListener(this);

		flingContainer.setVisibility(View.GONE);

		if (type.equals("本校照片")) {
			btn_type.setText("本校照片");
			btn_change.setText("换一批");
			new Thread() {
				public void run() {
					if (!wid.equals("")) {
						list = GetData
								.getPictureWallInfos(MyConfig.URL_JSON_PICTURE_WALL_SCHOOL
										+ sid + "&uid=" + uid + "&wid=" + wid);
					} else {
						list = GetData
								.getPictureWallInfos(MyConfig.URL_JSON_PICTURE_WALL_SCHOOL
										+ sid + "&uid=" + uid);
					}
					if (!isDestroyed()) {
						handler.sendEmptyMessage(0);
					}
				};
			}.start();
		} else {
			btn_type.setText("全国排名");
			btn_change.setText("下一页");
			new Thread() {
				public void run() {
					list = GetData
							.getPictureWallInfos(MyConfig.URL_JSON_PICTURE_WALL_COUNTRY
									+ uid + "&page=" + page);
					if (!isDestroyed()) {
						handler.sendEmptyMessage(0);
					}
				};
			}.start();
		}

	}

	private void initView() {
		cardAdapter = new CardAdapter(this, list, uid);
		count = list.size();
		tv_count.setText(count + "");
		flingContainer.setAdapter(cardAdapter);
		flingContainer.setVisibility(View.VISIBLE);
		flingContainer
				.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
					@Override
					public void removeFirstObjectInAdapter() {
						if (list != null && list.size() > 0) {
							list.remove(0);
							cardAdapter.notifyDataSetChanged();
							if (index == count) {
								index = 1;
								return;
							}
							index++;
							tv_index.setText(index + "");
						}
					}

					@Override
					public void onLeftCardExit(Object dataObject) {
					}

					@Override
					public void onRightCardExit(Object dataObject) {
					}

					@Override
					public void onAdapterAboutToEmpty(int itemsInAdapter) {
						if (itemsInAdapter == 0) {
							if (btn_type.getText().toString().equals("本校照片")) {
								intent.putExtra("page", 1);
								intent.putExtra("type", "本校照片");
								intent.putExtra("wid", "");
								startActivity(intent);
								PictureWallDetailActivity.this.finish();
							} else {
								intent.putExtra("page", page);
								intent.putExtra("type", "全国排名");
								intent.putExtra("wid", "");
								startActivity(intent);
								PictureWallDetailActivity.this.finish();
							}
						}
					}

					@Override
					public void onScroll(float scrollProgressPercent) {

					}

				});

		flingContainer
				.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
					@Override
					public void onItemClicked(int itemPosition,
							Object dataObject) {
						iv_like = (ImageView) flingContainer.getSelectedView()
								.findViewById(R.id.iv_like);
						iv_dislike = (ImageView) flingContainer
								.getSelectedView()
								.findViewById(R.id.iv_dislike);
						tv_like = (TextView) flingContainer.getSelectedView()
								.findViewById(R.id.tv_like);
						tv_dislike = (TextView) flingContainer
								.getSelectedView()
								.findViewById(R.id.tv_dislike);
						Intent intent = new Intent(
								PictureWallDetailActivity.this,
								PictureReportActivity.class);
						intent.putExtra("wid", list.get(0).getId());
						intent.putExtra("sid", list.get(0).getSid());
						intent.putExtra("supportCount", list.get(0)
								.getSupportCount());
						intent.putExtra("opposeCount", list.get(0)
								.getOpposeCount());
						intent.putExtra("action", list.get(0).getAction());
						intent.putExtra("pictureUrl", list.get(0).getPicture());
						intent.putExtra("dream", list.get(0).getDream());
						intent.putExtra("time", list.get(0).getTime());
						startActivity(intent);
					}
				});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 关闭按钮
		case R.id.ib_close:
			this.finish();
			break;

		// 学校或全国按钮
		case R.id.btn_type:
			if (btn_type.getText().toString().equals("本校照片")) {
				finish();
				intent.putExtra("page", 1);
				intent.putExtra("type", "全国排名");
				intent.putExtra("wid", "");
				startActivity(intent);
			} else {
				finish();
				intent.putExtra("page", 1);
				intent.putExtra("type", "本校照片");
				intent.putExtra("wid", "");
				startActivity(intent);
			}
			break;

		// 换一批或下一页按钮
		case R.id.btn_change:
			if (btn_type.getText().toString().equals("本校照片")) {
				finish();
				intent.putExtra("page", 1);
				intent.putExtra("type", "本校照片");
				intent.putExtra("wid", "");
				startActivity(intent);
			} else {
				finish();
				intent.putExtra("page", page);
				intent.putExtra("type", "全国排名");
				intent.putExtra("wid", "");
				startActivity(intent);
			}
			break;
		}
	}

	public void onEventMainThread(MEventPicReport event) {
		support = event.getSupport();
		handler2.sendEmptyMessage(0);
	}

	@Override
	protected void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	// @Override
	// public void removeFirstObjectInAdapter() {
	// if (list != null) {
	// list.remove(0);
	// cardAdapter.notifyDataSetChanged();
	// if (index == count) {
	// index = 1;
	// return;
	// }
	// index++;
	// tv_index.setText(index + "");
	// }
	// }
	//
	// @Override
	// public void onLeftCardExit(Object dataObject) {
	//
	// }
	//
	// @Override
	// public void onRightCardExit(Object dataObject) {
	//
	// }
	//
	// @Override
	// public void onAdapterAboutToEmpty(int itemsInAdapter) {
	// if (itemsInAdapter == 0) {
	// if (btn_type.getText().toString().equals("本校照片")) {
	// intent.putExtra("page", 1);
	// intent.putExtra("type", "本校照片");
	// intent.putExtra("wid", "");
	// startActivity(intent);
	// PictureWallDetailActivity.this.finish();
	// } else {
	// intent.putExtra("page", page);
	// intent.putExtra("type", "全国排名");
	// intent.putExtra("wid", "");
	// startActivity(intent);
	// PictureWallDetailActivity.this.finish();
	// }
	// }
	// }
	//
	// @Override
	// public void onScroll(float scrollProgressPercent) {
	// iv_like = (ImageView) flingContainer.getSelectedView()
	// .findViewById(R.id.iv_like);
	// iv_dislike = (ImageView) flingContainer
	// .getSelectedView()
	// .findViewById(R.id.iv_dislike);
	// tv_like = (TextView) flingContainer.getSelectedView()
	// .findViewById(R.id.tv_like);
	// tv_dislike = (TextView) flingContainer
	// .getSelectedView()
	// .findViewById(R.id.tv_dislike);
	// Intent intent = new Intent(
	// PictureWallDetailActivity.this,
	// PictureReportActivity.class);
	// intent.putExtra("wid", list.get(0).getId());
	// intent.putExtra("sid", list.get(0).getSid());
	// intent.putExtra("supportCount", list.get(0)
	// .getSupportCount());
	// intent.putExtra("opposeCount", list.get(0)
	// .getOpposeCount());
	// intent.putExtra("action", list.get(0).getAction());
	// intent.putExtra("pictureUrl", list.get(0).getPicture());
	// intent.putExtra("dream", list.get(0).getDream());
	// intent.putExtra("time", list.get(0).getTime());
	// startActivity(intent);
	// }

}
