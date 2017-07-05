package com.loosoo100.campus100.activities;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import com.loosoo100.campus100.adapters.CampusContactsDetailAppraiseAdapter;
import com.loosoo100.campus100.anyevent.MEventCampusContactsAllDel;
import com.loosoo100.campus100.anyevent.MEventCampusContactsAppraiseChange;
import com.loosoo100.campus100.beans.CampusContactsInfo;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyAnimation;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.CircleView;

import de.greenrobot.event.EventBus;

/**
 * 
 * @author yang 校园圈详情朋友的activity
 * 
 */
public class CampusContactsDetailFriendActivity extends Activity implements
		OnClickListener, OnTouchListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view
	@ViewInject(R.id.rl_back)
	private RelativeLayout rl_back;
	@ViewInject(R.id.tv_top)
	private TextView tv_top;
	@ViewInject(R.id.tv_name)
	private TextView tv_name; // 姓名
	@ViewInject(R.id.tv_location)
	private TextView tv_location; // 地址
	@ViewInject(R.id.tv_time)
	private TextView tv_time; // 时间
	@ViewInject(R.id.tv_content)
	private TextView tv_content; // 发表的内容
	@ViewInject(R.id.cv_headShot)
	private CircleView cv_headShot; // 头像
	@ViewInject(R.id.iv_sex)
	private ImageView iv_sex; // 性别
	@ViewInject(R.id.iv_dialog)
	private ImageView iv_dialog; // 评论的按钮
	@ViewInject(R.id.gv_picture)
	private GridView gv_picture; // 发表的图片的表格
	@ViewInject(R.id.tv_likeCount)
	private TextView tv_likeCount; // 点赞的人数
	@ViewInject(R.id.ll_root_like)
	private LinearLayout ll_root_like; // 点赞的布局
	@ViewInject(R.id.ll_like)
	private LinearLayout ll_like; // 点赞的按钮
	@ViewInject(R.id.ll_appraise)
	private LinearLayout ll_appraise; // 评论布局
	@ViewInject(R.id.ll_root_appraise)
	private LinearLayout ll_root_appraise; // 评论布局
	@ViewInject(R.id.ll_appraise_up)
	private LinearLayout ll_appraise_up; // 评论布局上
	@ViewInject(R.id.ll_content)
	private LinearLayout ll_content; // 发表的内容布局
	@ViewInject(R.id.iv_line)
	private ImageView iv_line;
	@ViewInject(R.id.ll_appraise_down)
	private LinearLayout ll_appraise_down;
	@ViewInject(R.id.scrollView)
	private ScrollView scrollView;

	@ViewInject(R.id.ll_appraise_edit)
	public static LinearLayout ll_appraise_edit; // 评论输入框
	@ViewInject(R.id.et_content)
	private EditText et_content; // 评论输入框内容
	@ViewInject(R.id.btn_send)
	private Button btn_send; // 评论输入框发送
	@ViewInject(R.id.progress)
	private RelativeLayout progress; // 加载动画
	@ViewInject(R.id.progress_update_blackbg)
	private RelativeLayout progress_update_blackbg; // 加载动画
	@ViewInject(R.id.lv_appraise)
	private ListView lv_appraise;
	@ViewInject(R.id.rl_more)
	private RelativeLayout rl_more; // 更多
	@ViewInject(R.id.rl_popupwindow)
	private RelativeLayout rl_popupwindow; // 弹出菜单项
	@ViewInject(R.id.rl_report)
	private RelativeLayout rl_report; // 举报
	@ViewInject(R.id.dialog_report)
	private RelativeLayout dialog_report; // 举报界面
	@ViewInject(R.id.btn01)
	private Button btn01;
	@ViewInject(R.id.btn02)
	private Button btn02;
	@ViewInject(R.id.btn03)
	private Button btn03;
	@ViewInject(R.id.btn04)
	private Button btn04;
	@ViewInject(R.id.btn05)
	private Button btn05;

	private Dialog dialog;
	private EditText editText;

	public static int position; // 记录点击的评论的哪一条

	private CampusContactsInfo campusContactsInfo = null;

	private CampusContactsDetailAppraiseAdapter adapter;

	private String mid = "";
	private String content = "";
	private String uid = "";
	private String pname = "";
	private String pid = "";
	private String sid = "";
	private String username = "";
	private int appraisePosition;

	private int mPosition = 0;

	private Dialog dialogCopy;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (campusContactsInfo != null) {
				initView();
				if (campusContactsInfo.getAppraiseList() != null
						&& campusContactsInfo.getAppraiseList().size() > 0) {
					initListView();
				}
				progress.setVisibility(View.GONE);
			} else {
				progress.setVisibility(View.VISIBLE);
			}
		};
	};

	private Handler handlerAppraise = new Handler() {
		public void handleMessage(android.os.Message msg) {
			et_content.setText("");
			ll_appraise_edit.setVisibility(View.GONE);
			appraisePosition = -1;
			pname = "";
			if (campusContactsInfo.getAppraiseList() == null
					|| campusContactsInfo.getAppraiseList().size() == 0) {
				ll_appraise_down.setVisibility(View.GONE);
			} else {
				initListView();
				EventBus.getDefault().post(
						new MEventCampusContactsAppraiseChange(mid,
								campusContactsInfo.getAppraiseList()));
				ll_appraise_down.setVisibility(View.VISIBLE);
				ll_root_appraise.setVisibility(View.VISIBLE);
			}
			progress_update_blackbg.setVisibility(View.GONE);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_campus_contacts_detail_friend);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);
		// 更改状态栏字体颜色为黑色
		MyUtils.setMiuiStatusBarDarkMode(this, true);
		EventBus.getDefault().register(this);

		mid = getIntent().getExtras().getString("mid");
		username = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.NICK_NAME, "");
		uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.USERID, "");

		progress.setVisibility(View.VISIBLE);
		rl_back.setOnClickListener(this);
		new Thread() {
			public void run() {
				campusContactsInfo = GetData
						.getCampusContactsInfo(MyConfig.URL_JSON_CAMPUS_DETAILS
								+ mid + "&uid=" + uid);
				if (!isDestroyed()) {
					handler.sendEmptyMessage(0);
				}
			};
		}.start();

		dialogCopy = new Dialog(this, R.style.MyDialog);
		View viewDialog = LayoutInflater.from(this).inflate(
				R.layout.dialog_copy, null);
		dialogCopy.setContentView(viewDialog);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		dialogCopy.setContentView(viewDialog, params);
		viewDialog.findViewById(R.id.btn_copy).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						android.text.ClipboardManager clipboardManager = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
						if (campusContactsInfo != null) {
							clipboardManager.setText(campusContactsInfo
									.getContent());
							ToastUtil.showToast(CampusContactsDetailFriendActivity.this,"已复制");
						}
						dialogCopy.dismiss();
					}
				});
	}

	private void initListView() {
		// appraiseList = campusContactsInfo.getAppraiseList();
		adapter = new CampusContactsDetailAppraiseAdapter(this,
				campusContactsInfo.getAppraiseList());
		lv_appraise.setAdapter(adapter);
		// 设置listview的高度
		MyUtils.setListViewHeight(lv_appraise, 15);

	}

	private void initView() {
		btn01.setOnClickListener(this);
		btn02.setOnClickListener(this);
		btn03.setOnClickListener(this);
		btn04.setOnClickListener(this);
		btn05.setOnClickListener(this);
		rl_more.setOnClickListener(this);
		rl_report.setOnClickListener(this);
		iv_dialog.setOnClickListener(this);
		ll_like.setOnClickListener(this);
		ll_appraise.setOnClickListener(this);
		cv_headShot.setOnClickListener(this);
		btn_send.setOnClickListener(this);
		scrollView.setOnTouchListener(this);
		// 昵称
		tv_name.setText(campusContactsInfo.getName());
		tv_top.setText(campusContactsInfo.getName() + "的话题");
		// 内容
		if (campusContactsInfo.getContent().equals("")) {
			tv_content.setVisibility(View.GONE);
		} else {
			tv_content.setText(campusContactsInfo.getContent());
			tv_content.setVisibility(View.VISIBLE);
		}
		// 长按复制内容
		tv_content.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				dialogCopy.show();
				return true;
			}
		});

		if (campusContactsInfo.getSex().equals("1")) {
			iv_sex.setImageDrawable(getResources().getDrawable(
					R.drawable.icon_female_picture));
		} else {
			iv_sex.setImageDrawable(getResources().getDrawable(
					R.drawable.icon_male_picture));
		}
		// 地址
		tv_location.setText(campusContactsInfo.getSchool());
		// 学校ID
		sid = campusContactsInfo.getSid();
		// 时间
		tv_time.setText(MyUtils.getSqlDateMH(campusContactsInfo.getTime()));
		// 头像
		Glide.with(this).load(campusContactsInfo.getHeadShot())
				.override(200, 200).into(cv_headShot);
		// 发表图片
		gv_picture.setAdapter(new MyPictureAdapter(this, campusContactsInfo
				.getPicListThum()));
		// 点击进入图片预览界面
		gv_picture.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int mIndex, long id) {
				Intent intent = new Intent(
						CampusContactsDetailFriendActivity.this,
						CampusContactsImagePreviewActivity.class);
				intent.putExtra("index", mIndex);
				intent.putExtra("button", true);
				intent.putStringArrayListExtra("urlList",
						campusContactsInfo.getPicList());
				startActivity(intent);
			}
		});
		gv_picture.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (ll_appraise_up.getVisibility() == View.VISIBLE) {
					ll_appraise_up.setVisibility(View.GONE);
					ll_appraise_up.startAnimation(MyAnimation
							.getScaleAnimationToRight());
				}
				return false;
			}
		});

		MyUtils.setGridViewHeight(gv_picture, 20, 3);

		// 如果没人点赞则隐藏点赞区
		if (campusContactsInfo.getLikeCount() == 0) {
			ll_root_like.setVisibility(View.GONE);
			iv_line.setVisibility(View.GONE);
			// 如果没人评论则隐藏评论区
			if (campusContactsInfo.getAppraiseList() == null
					|| campusContactsInfo.getAppraiseList().size() == 0) {
				ll_appraise_down.setVisibility(View.GONE);
				ll_root_appraise.setVisibility(View.GONE);
			} else {
				ll_root_appraise.setVisibility(View.VISIBLE);
				ll_appraise_down.setVisibility(View.VISIBLE);
			}
			// 否则显示整个评论和点赞区
		} else if (campusContactsInfo.getLikeCount() > 0) {
			ll_root_like.setVisibility(View.VISIBLE);
			ll_root_appraise.setVisibility(View.VISIBLE);
			// 如果没人评论则隐藏评论区
			if (campusContactsInfo.getAppraiseList() == null
					|| campusContactsInfo.getAppraiseList().size() == 0) {
				iv_line.setVisibility(View.GONE);
				ll_appraise_down.setVisibility(View.GONE);
			} else {
				iv_line.setVisibility(View.VISIBLE);
				ll_appraise_down.setVisibility(View.VISIBLE);
			}
		}
		if (campusContactsInfo.getLikeCount() > 0) {
			// 点赞的人
			tv_likeCount.setText(campusContactsInfo.getLikeCount() + "人觉得很赞");
		}

		lv_appraise.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int mPosition, long id) {
				pname = campusContactsInfo.getAppraiseList().get(mPosition)
						.getName();
				ll_appraise_edit.setVisibility(View.VISIBLE);
				appraisePosition = mPosition;
				et_content.setHint("回复"
						+ campusContactsInfo.getAppraiseList().get(mPosition)
								.getName() + ":");
				et_content.setText("");
				// 显示软键盘
				MyUtils.showSoftInput(CampusContactsDetailFriendActivity.this,
						et_content);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			this.finish();
			break;

		case R.id.rl_more:
			if (rl_popupwindow.getVisibility() == View.GONE) {
				rl_popupwindow.setVisibility(View.VISIBLE);
			} else {
				rl_popupwindow.setVisibility(View.GONE);
			}
			break;

		case R.id.rl_report:
			rl_popupwindow.setVisibility(View.GONE);
			dialog_report.setVisibility(View.VISIBLE);
			break;

		case R.id.btn01:
			dialog_report.setVisibility(View.GONE);
			content = "头像、资料虚假";
			progress_update_blackbg.setVisibility(View.VISIBLE);
			new Thread() {
				public void run() {
					doPostReport(MyConfig.URL_POST_REPORT);
				};
			}.start();
			break;

		case R.id.btn02:
			dialog_report.setVisibility(View.GONE);
			content = "广告骚扰";
			progress_update_blackbg.setVisibility(View.VISIBLE);
			new Thread() {
				public void run() {
					doPostReport(MyConfig.URL_POST_REPORT);
				};
			}.start();
			break;

		case R.id.btn03:
			dialog_report.setVisibility(View.GONE);
			content = "色情低俗";
			progress_update_blackbg.setVisibility(View.VISIBLE);
			new Thread() {
				public void run() {
					doPostReport(MyConfig.URL_POST_REPORT);
				};
			}.start();
			break;

		case R.id.btn04:
			dialog_report.setVisibility(View.GONE);
			content = "不文明语言";
			progress_update_blackbg.setVisibility(View.VISIBLE);
			new Thread() {
				public void run() {
					doPostReport(MyConfig.URL_POST_REPORT);
				};
			}.start();
			break;

		case R.id.btn05:
			dialog_report.setVisibility(View.GONE);
			dialog = new Dialog(this, R.style.MyDialog);
			LayoutInflater inflater = LayoutInflater.from(this);
			View view = inflater.inflate(R.layout.dialog_report_other, null);
			dialog.setContentView(view);
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			dialog.setContentView(view, params);
			editText = (EditText) view.findViewById(R.id.et_reason);
			Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
			Button btn_ok = (Button) view.findViewById(R.id.btn_ok);

			btn_cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			btn_ok.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					content = editText.getText().toString().trim();
					if (content.equals("")) {
						ToastUtil.showToast(CampusContactsDetailFriendActivity.this,"请输入举报内容");
						return;
					}
					dialog.dismiss();
					progress_update_blackbg.setVisibility(View.VISIBLE);
					new Thread() {
						public void run() {
							doPostReport(MyConfig.URL_POST_REPORT);
						};
					}.start();
				}
			});
			dialog.show();
			// 显示软键盘
			MyUtils.showSoftInput(this, editText);
			break;

		// 评论对话框
		case R.id.iv_dialog:
			appraisePosition = -1;
			if (ll_appraise_up.getVisibility() == View.VISIBLE) {
				ll_appraise_up.setVisibility(View.GONE);
				ll_appraise_up.startAnimation(MyAnimation
						.getScaleAnimationToRight());
			} else {
				ll_appraise_up.setVisibility(View.VISIBLE);
				ll_appraise_up.startAnimation(MyAnimation
						.getScaleAnimationToLeft());
			}
			break;

		// 点赞
		case R.id.ll_like:
			if (campusContactsInfo.getIsLiked() == 0) {
				new Thread() {
					public void run() {
						postLike(MyConfig.URL_POST_CAMPUS_LIKE + "?uid=" + uid
								+ "&mid=" + campusContactsInfo.getId());
					};
				}.start();
			} else {
				ToastUtil.showToast(CampusContactsDetailFriendActivity.this,"您已赞过了");
			}
			ll_appraise_up.setVisibility(View.GONE);
			ll_appraise_up.startAnimation(MyAnimation
					.getScaleAnimationToRight());
			break;

		// 评论
		case R.id.ll_appraise:
			ll_appraise_edit.setVisibility(View.VISIBLE);
			ll_appraise_up.setVisibility(View.GONE);
			pname = "";
			et_content.setHint("");
			et_content.setText("");
			ll_appraise_up.startAnimation(MyAnimation
					.getScaleAnimationToRight());
			// 显示软键盘
			MyUtils.showSoftInput(CampusContactsDetailFriendActivity.this,
					et_content);
			break;

		// 评论内容发送
		case R.id.btn_send:
			content = et_content.getText().toString().trim();
			if (content.equals("")) {
				ToastUtil.showToast(CampusContactsDetailFriendActivity.this,"请输入评论内容");
				return;
			}
			progress_update_blackbg.setVisibility(View.VISIBLE);
			// 隐藏软键盘
			MyUtils.hideSoftInput(this, et_content);
			new Thread() {
				public void run() {
					postCampusContacts(MyConfig.URL_POST_CAMPUS_APPRAISE,
							content);
				};
			}.start();
			break;

		// 头像
		case R.id.cv_headShot:
			if (uid.equals(campusContactsInfo.getUserId())) {
				Intent intent = new Intent(this,
						CampusContactsPersonalActivity.class);
				startActivity(intent);
			} else {
				Intent intent = new Intent(this,
						CampusContactsFriendActivity.class);
				// intent.putExtra("uid", uid);
				// intent.putExtra("sex", campusContactsInfo.getSex());
				intent.putExtra("muid", campusContactsInfo.getUserId());
				// intent.putExtra("headShot",
				// campusContactsInfo.getHeadShot());
				// intent.putExtra("name", campusContactsInfo.getName());
				startActivity(intent);
			}
			break;
		}

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
		params.addBodyParameter("mid", campusContactsInfo.getId());
		params.addBodyParameter("content", content);
		if (appraisePosition == -1) {
			params.addBodyParameter("pid", "0");
			params.addBodyParameter("puid", "0");
			pid = "0";
			pname = "";
		} else {
			pid = campusContactsInfo.getAppraiseList().get(appraisePosition)
					.getId();
			pname = campusContactsInfo.getAppraiseList().get(appraisePosition)
					.getName();
			params.addBodyParameter("pid", pid);
			params.addBodyParameter("puid", campusContactsInfo
					.getAppraiseList().get(appraisePosition).getUid());
		}
		params.addBodyParameter("username", username);
		httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						LogUtils.d("responseInfo",responseInfo.result);
						progress_update_blackbg.setVisibility(View.GONE);
						String result = "";
						try {
							JSONObject jsonObject = new JSONObject(
									responseInfo.result);
							result = jsonObject.getString("status");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (!result.equals("0") && !result.equals("-1")) {
							new Thread() {
								public void run() {
									campusContactsInfo = GetData
											.getCampusContactsInfo(MyConfig.URL_JSON_CAMPUS_DETAILS
													+ mid + "&uid=" + uid);
									if (!isDestroyed()) {
										handlerAppraise.sendEmptyMessage(0);
									}
								};
							}.start();
						} else {
							ToastUtil.showToast(CampusContactsDetailFriendActivity.this,"评论失败");
						}

					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.d("arg1",arg1.toString());
						progress_update_blackbg.setVisibility(View.GONE);
						ToastUtil.showToast(CampusContactsDetailFriendActivity.this,"评论失败");
					}
				});
	}

	/**
	 * 赞
	 * 
	 * @param uploadHost
	 */
	private void postLike(String uploadHost) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpRequest.HttpMethod.GET, uploadHost,
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
						if (!result.equals("0")) {
							campusContactsInfo.setIsLiked(1);
							tv_likeCount.setText((campusContactsInfo
									.getLikeCount() + 1) + "人觉得很赞");
							ll_root_like.setVisibility(View.VISIBLE);
							ll_root_appraise.setVisibility(View.VISIBLE);
							if (campusContactsInfo.getAppraiseList() == null
									|| campusContactsInfo.getAppraiseList()
											.size() == 0) {
								iv_line.setVisibility(View.GONE);
							} else {
								iv_line.setVisibility(View.VISIBLE);
							}
						} else {
							ToastUtil.showToast(CampusContactsDetailFriendActivity.this,"操作失败");
						}

					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.d("arg1",arg1.toString());
						ToastUtil.showToast(CampusContactsDetailFriendActivity.this,"操作失败");
					}
				});
	}

	/**
	 * 发表的图片适配器
	 * 
	 * @author yang
	 * 
	 */
	class MyPictureAdapter extends BaseAdapter {
		private Activity activity;
		private List<String> list;
		private LayoutInflater inflater;

		public MyPictureAdapter(Context context, List<String> list) {
			activity = (Activity) context;
			this.list = list;

			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			if (list.size() > 9) {
				return 9;
			}
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = inflater.inflate(
						R.layout.item_contacts_gv_picture, null);
				viewHolder.iv_picture = (ImageView) convertView
						.findViewById(R.id.iv_picture);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			if (list.get(position) != null && !list.get(position).equals("")) {
				Glide.with(activity).load(list.get(position))
						.placeholder(R.drawable.imgloading).override(320, 320)
						.into(viewHolder.iv_picture);
			}

			return convertView;
		}

		class ViewHolder {
			public ImageView iv_picture;
		}

	}

	/**
	 * 点赞的头像适配器
	 * 
	 * @author yang
	 * 
	 */
	class MyHeadShotAdapter extends BaseAdapter {
		private Context context;
		private List<Bitmap> list;
		private LayoutInflater inflater;

		public MyHeadShotAdapter(Context context, List<Bitmap> list) {
			this.context = context;
			this.list = list;

			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// 最多显示14个头像
			if (list.size() > 14) {
				return 14;
			}
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = inflater.inflate(
						R.layout.item_contacts_gv_headshot, null);
				viewHolder.cv_headShot_like = (CircleView) convertView
						.findViewById(R.id.cv_headShot_like);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			if (list.size() > 0) {
				viewHolder.cv_headShot_like.setImageBitmap(list.get(position));
			}

			return convertView;
		}

		class ViewHolder {
			public CircleView cv_headShot_like;
		}

	}

	public void onEventMainThread(MEventCampusContactsAllDel event) {
		if (event.isChange()) {
			this.finish();
		}
	}

	@Override
	protected void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (ll_appraise_up.getVisibility() == View.VISIBLE) {
			ll_appraise_up.setVisibility(View.GONE);
			ll_appraise_up.startAnimation(MyAnimation
					.getScaleAnimationToRight());
		}
		if (ll_appraise_edit.getVisibility() == View.VISIBLE) {
			ll_appraise_edit.setVisibility(View.GONE);
			// 隐藏软键盘
			MyUtils.hideSoftInput(CampusContactsDetailFriendActivity.this,
					et_content);
		}
		return false;
	}

	/**
	 * 举报
	 * 
	 * @param uploadHost
	 */
	private void doPostReport(String uploadHost) {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("type", "2");
		params.addBodyParameter("rid", mid);
		params.addBodyParameter("sid", sid);
		params.addBodyParameter("uid", uid);
		params.addBodyParameter("content", content);
		httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						LogUtils.d("responseInfo",responseInfo.result);
						progress_update_blackbg.setVisibility(View.GONE);
						String result = "";
						try {
							JSONObject jsonObject = new JSONObject(
									responseInfo.result);
							result = jsonObject.getString("status");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (result.equals("0")) {
							ToastUtil.showToast(CampusContactsDetailFriendActivity.this,"操作失败");
						} else if (result.equals("-1")) {
							ToastUtil.showToast(CampusContactsDetailFriendActivity.this,"您已举报过了");
						} else {
							ToastUtil.showToast(CampusContactsDetailFriendActivity.this,"举报成功");
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.d("arg1",arg1.toString());
						progress_update_blackbg.setVisibility(View.GONE);
						ToastUtil.showToast(CampusContactsDetailFriendActivity.this,"操作失败");
					}
				});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (ll_appraise_edit.getVisibility() == View.VISIBLE) {
				ll_appraise_edit.setVisibility(View.GONE);
				return true;
			}
			if (dialog_report.getVisibility() == View.VISIBLE) {
				dialog_report.setVisibility(View.GONE);
				return true;
			}
			if (rl_popupwindow.getVisibility() == View.VISIBLE) {
				rl_popupwindow.setVisibility(View.GONE);
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
