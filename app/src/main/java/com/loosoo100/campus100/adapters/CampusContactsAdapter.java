package com.loosoo100.campus100.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.activities.CampusContactsActivity;
import com.loosoo100.campus100.activities.CampusContactsDetailPersonalActivity;
import com.loosoo100.campus100.activities.CampusContactsFriendActivity;
import com.loosoo100.campus100.activities.CampusContactsImagePreviewActivity;
import com.loosoo100.campus100.activities.CampusContactsPersonalActivity;
import com.loosoo100.campus100.anyevent.MEventCampusContactsAdd;
import com.loosoo100.campus100.beans.CampusContactsInfo;
import com.loosoo100.campus100.beans.CampusContactsReplyInfo;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.utils.MyAnimation;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.CircleView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 
 * @author yang 校园圈适配器
 */
public class CampusContactsAdapter extends BaseAdapter {

	private List<CampusContactsInfo> list;
	private Context context;
	private LayoutInflater inflater;
	private Activity activity;
	private ViewHolder viewHolder;

	private long campusContactsTime;
	private String uid;
	private MyAppraiseAdapter myAppraiseAdapter;
	private int mPosition;
	private int aPosition;
	private int morePosition;
	private List<CampusContactsReplyInfo> appraiseList;

	private Dialog dialog;
	private View viewDialog;

	private String copyText = "";

	public CampusContactsAdapter(Context context,
			List<CampusContactsInfo> list, long campusContactsTime, String uid) {
		this.context = context;
		this.list = list;
		this.campusContactsTime = campusContactsTime;
		this.uid = uid;
		activity = (Activity) context;
		inflater = LayoutInflater.from(context);

		dialog = new Dialog(activity, R.style.MyDialog);
		viewDialog = LayoutInflater.from(activity).inflate(
				R.layout.dialog_copy, null);
		dialog.setContentView(viewDialog);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		dialog.setContentView(viewDialog, params);

	}

	@Override
	public int getCount() {
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_campus_contacts, null);
			viewHolder.btn_name = (Button) convertView
					.findViewById(R.id.btn_name);
			viewHolder.btn_content = (Button) convertView
					.findViewById(R.id.btn_content);
			viewHolder.tv_location = (TextView) convertView
					.findViewById(R.id.tv_location);
			viewHolder.tv_time = (TextView) convertView
					.findViewById(R.id.tv_time);
			viewHolder.tv_likeCount = (TextView) convertView
					.findViewById(R.id.tv_likeCount);
			viewHolder.cv_headShot = (CircleView) convertView
					.findViewById(R.id.cv_headShot);
			// viewHolder.tv_ulcount = (TextView) convertView
			// .findViewById(R.id.tv_ulcount);
			viewHolder.btn_more = (Button) convertView
					.findViewById(R.id.btn_more);
			viewHolder.iv_dialog = (ImageView) convertView
					.findViewById(R.id.iv_dialog);
			viewHolder.gv_picture = (GridView) convertView
					.findViewById(R.id.gv_picture);
			viewHolder.ll_like = (LinearLayout) convertView
					.findViewById(R.id.ll_like);
			viewHolder.ll_appraise = (LinearLayout) convertView
					.findViewById(R.id.ll_appraise);
			viewHolder.ll_root_appraise = (LinearLayout) convertView
					.findViewById(R.id.ll_root_appraise);
			viewHolder.ll_appraise_up = (LinearLayout) convertView
					.findViewById(R.id.ll_appraise_up);
			viewHolder.ll_root_like = (LinearLayout) convertView
					.findViewById(R.id.ll_root_like);
			viewHolder.ll_content = (LinearLayout) convertView
					.findViewById(R.id.ll_content);
			viewHolder.iv_line = (ImageView) convertView
					.findViewById(R.id.iv_line);
			viewHolder.iv_sex = (ImageView) convertView
					.findViewById(R.id.iv_sex);
			viewHolder.lv_appraise = (ListView) convertView
					.findViewById(R.id.lv_appraise);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.btn_more.setTag(position);

		if (list.get(position).getSex().equals("1")) {
			viewHolder.iv_sex.setImageDrawable(activity.getResources()
					.getDrawable(R.drawable.icon_female_picture));
		} else {
			viewHolder.iv_sex.setImageDrawable(activity.getResources()
					.getDrawable(R.drawable.icon_male_picture));
		}

		// 昵称
		viewHolder.btn_name.setText(list.get(position).getName());
		viewHolder.btn_name.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (uid.equals(list.get(position).getUserId())) {
					Intent intent = new Intent(activity,
							CampusContactsPersonalActivity.class);
					activity.startActivity(intent);
				} else {
					Intent intent = new Intent(activity,
							CampusContactsFriendActivity.class);
					// intent.putExtra("uid", uid);
					// intent.putExtra("sex", list.get(position).getSex());
					intent.putExtra("muid", list.get(position).getUserId());
					// intent.putExtra("headShot", list.get(position)
					// .getHeadShot());
					// intent.putExtra("name", list.get(position).getName());
					activity.startActivity(intent);
				}
			}
		});
		// 内容
		viewHolder.btn_content.setText(list.get(position).getContent());
		viewHolder.btn_content.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity,
						CampusContactsDetailPersonalActivity.class);
				intent.putExtra("mid", list.get(position).getId());
				// intent.putExtra("index", -1);
				activity.startActivity(intent);
			}
		});

		viewDialog.findViewById(R.id.btn_copy).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						android.text.ClipboardManager clipboardManager = (android.text.ClipboardManager) context
								.getSystemService(Context.CLIPBOARD_SERVICE);
						clipboardManager.setText(copyText);
						ToastUtil.showToast(activity,"已复制");
						dialog.dismiss();
					}
				});

		/*
		 * 长按复制内容
		 */
		viewHolder.btn_content
				.setOnLongClickListener(new OnLongClickListener() {

					@Override
					public boolean onLongClick(View v) {
						copyText = list.get(position).getContent();
						dialog.show();
						return true;
					}
				});

		if (list.get(position).getContent().equals("")) {
			viewHolder.ll_content.setVisibility(View.GONE);
		} else {
			viewHolder.ll_content.setVisibility(View.VISIBLE);
		}

		// 地址
		viewHolder.tv_location.setText(list.get(position).getSchool());
		// 时间
		if (campusContactsTime - list.get(position).getTime() < 60) {
			viewHolder.tv_time.setText("刚刚");
		} else if (campusContactsTime - list.get(position).getTime() < 180) {
			viewHolder.tv_time.setText("1分钟前");
		} else if (campusContactsTime - list.get(position).getTime() < 300) {
			viewHolder.tv_time.setText("3分钟前");
		} else if (campusContactsTime - list.get(position).getTime() < 600) {
			viewHolder.tv_time.setText("5分钟前");
		} else if (campusContactsTime - list.get(position).getTime() < 900) {
			viewHolder.tv_time.setText("10分钟前");
		} else if (campusContactsTime - list.get(position).getTime() < 1800) {
			viewHolder.tv_time.setText("15分钟前");
		} else if (campusContactsTime - list.get(position).getTime() < 2700) {
			viewHolder.tv_time.setText("30分钟前");
		} else if (campusContactsTime - list.get(position).getTime() < 3600) {
			viewHolder.tv_time.setText("45分钟前");
		} else if (campusContactsTime - list.get(position).getTime() < 7200) {
			viewHolder.tv_time.setText("1小时前");
		} else if (campusContactsTime - list.get(position).getTime() < 10800) {
			viewHolder.tv_time.setText("2小时前");
		} else if (campusContactsTime - list.get(position).getTime() < 14400) {
			viewHolder.tv_time.setText("3小时前");
		} else if (campusContactsTime - list.get(position).getTime() < 172800) {
			if (MyUtils.getSqlDateM(campusContactsTime).equals(
					MyUtils.getSqlDateM(list.get(position).getTime()))) {
				viewHolder.tv_time.setText("今天"
						+ MyUtils.getSqlDateH(list.get(position).getTime()));
			} else if (MyUtils.getSqlDateM(campusContactsTime - 86400).equals(
					MyUtils.getSqlDateM(list.get(position).getTime()))) {
				viewHolder.tv_time.setText("昨天"
						+ MyUtils.getSqlDateH(list.get(position).getTime()));
			} else {
				viewHolder.tv_time.setText(MyUtils.getSqlDateM(list.get(
						position).getTime()));
			}
		} else {
			viewHolder.tv_time.setText(MyUtils.getSqlDateM(list.get(position)
					.getTime()));
		}

		// 头像
		if (list.get(position).getHeadShot() != null
				&& !list.get(position).getHeadShot().equals("")) {
			Glide.with(activity).load(list.get(position).getHeadShot())
					.override(200, 200).into(viewHolder.cv_headShot);
		} else {
			Glide.with(activity).load("").placeholder(R.drawable.imgloading)
					.override(200, 200).into(viewHolder.cv_headShot);
		}
		viewHolder.cv_headShot.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (uid.equals(list.get(position).getUserId())) {
					Intent intent = new Intent(activity,
							CampusContactsPersonalActivity.class);
					activity.startActivity(intent);
				} else {
					Intent intent = new Intent(activity,
							CampusContactsFriendActivity.class);
					// intent.putExtra("uid", uid);
					// intent.putExtra("sex", list.get(position).getSex());
					intent.putExtra("muid", list.get(position).getUserId());
					// intent.putExtra("headShot", list.get(position)
					// .getHeadShot());
					// intent.putExtra("name", list.get(position).getName());
					activity.startActivity(intent);
				}
			}
		});

		// 对话框按钮
		viewHolder.iv_dialog.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CampusContactsActivity.mPosition = position;
				aPosition = position;
				for (int i = 0; i < list.size(); i++) {
					if (i != position) {
						list.get(i).setShowAppraise(false);
					}
				}
				if (list.get(position).isShowAppraise()) {
					list.get(position).setShowAppraise(false);
				} else {
					list.get(position).setShowAppraise(true);
				}
				CampusContactsAdapter.this.notifyDataSetChanged();
			}
		});

		viewHolder.ll_appraise_up.setTag(position);
		// 点击对话框按钮弹出框
		if (list.get(position).isShowAppraise()) {
			viewHolder.ll_appraise_up.setVisibility(View.VISIBLE);
			if (Integer.valueOf(viewHolder.ll_appraise_up.getTag().toString()) == position) {
				viewHolder.ll_appraise_up.startAnimation(MyAnimation
						.getScaleAnimationToLeft());
			}
		} else {
			if (Integer.valueOf(viewHolder.ll_appraise_up.getTag().toString()) == position
					&& viewHolder.ll_appraise_up.getVisibility() == View.VISIBLE) {
				viewHolder.ll_appraise_up.startAnimation(MyAnimation
						.getScaleAnimationToRight());
			}
			viewHolder.ll_appraise_up.setVisibility(View.GONE);
		}

		if (list.get(position).getPicListThum() != null
				&& list.get(position).getPicListThum().size() > 0
				&& !list.get(position).getPicListThum().get(0).equals("")) {
			viewHolder.gv_picture.setAdapter(new MyPictureAdapter(context, list
					.get(position).getPicListThum()));
			viewHolder.gv_picture.setVisibility(View.VISIBLE);
		} else {
			viewHolder.gv_picture.setVisibility(View.GONE);
		}
		// 点击进入图片预览界面
		viewHolder.gv_picture.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int index, long id) {
				Intent intent = new Intent(activity,
						CampusContactsImagePreviewActivity.class);
				intent.putExtra("index", index);
				intent.putExtra("button", true);
				intent.putStringArrayListExtra("urlList", list.get(position)
						.getPicList());
				activity.startActivity(intent);
			}
		});

		viewHolder.gv_picture.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (list.get(aPosition).isShowAppraise()) {
					list.get(aPosition).setShowAppraise(false);
					CampusContactsAdapter.this.notifyDataSetChanged();
				}
				return false;
			}
		});

		// 设置gridView的高度
		MyUtils.setGridViewHeight(viewHolder.gv_picture, 3);

		// 点赞
		viewHolder.ll_like.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CampusContactsActivity.mPosition = -1;
				mPosition = position;
				if (list.get(position).getIsLiked() == 0) {
					new Thread() {
						public void run() {
							postLike(MyConfig.URL_POST_CAMPUS_LIKE + "?uid="
									+ uid + "&mid="
									+ list.get(position).getId());
						};
					}.start();
				} else {
					ToastUtil.showToast(activity,"您已赞过了");
				}
				list.get(position).setShowAppraise(false);
				CampusContactsAdapter.this.notifyDataSetChanged();
			}
		});
		if (list.get(position).getLikeCount() > 0) {
			// 点赞的人
			viewHolder.tv_likeCount.setText(list.get(position).getLikeCount()
					+ "人觉得很赞");
		}

		viewHolder.lv_appraise.setTag(position);
		// 评论
		viewHolder.ll_appraise.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CampusContactsActivity.mPosition = -1;
				CampusContactsActivity.et_content.setHint("");
				CampusContactsActivity.et_content.setText("");
				CampusContactsActivity.pname = "";
				CampusContactsActivity.pid = "0";
				CampusContactsActivity.appraisePosition = -1;
				CampusContactsActivity.position = position;
				list.get(position).setShowAppraise(false);
				CampusContactsAdapter.this.notifyDataSetChanged();
				// 显示软键盘
				MyUtils.showSoftInput(context,
						CampusContactsActivity.et_content);
				EventBus.getDefault().post(new MEventCampusContactsAdd(false));
			}
		});

		if (list.get(position).getAppraiseList() != null
				&& list.get(position).getAppraiseList().size() > 0) {
			appraiseList = list.get(position).getAppraiseList();
			myAppraiseAdapter = new MyAppraiseAdapter(appraiseList, position);
			viewHolder.lv_appraise.setAdapter(myAppraiseAdapter);
			viewHolder.lv_appraise.setVisibility(View.VISIBLE);
			if (list.get(position).getAppraiseList().size() > 5) {
				viewHolder.btn_more.setVisibility(View.VISIBLE);
			} else {
				viewHolder.btn_more.setVisibility(View.GONE);
			}
		} else {
			viewHolder.lv_appraise.setVisibility(View.GONE);
			viewHolder.btn_more.setVisibility(View.GONE);
		}
		// 点击某条评论进行回复
		viewHolder.lv_appraise
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int mPosition, long id) {
						CampusContactsActivity.pname = list.get(position)
								.getAppraiseList().get(mPosition).getName();
						CampusContactsActivity.position = position;
						CampusContactsActivity.appraisePosition = mPosition;
						CampusContactsActivity.et_content.setHint("回复"
								+ list.get(position).getAppraiseList()
										.get(mPosition).getName() + ":");
						CampusContactsActivity.et_content.setText("");
						// 显示软键盘
						MyUtils.showSoftInput(context,
								CampusContactsActivity.et_content);
						EventBus.getDefault().post(
								new MEventCampusContactsAdd(false));
					}
				});

		viewHolder.btn_more.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (morePosition == position && list.get(position).isShowAll()) {
					list.get(position).setShowAll(false);
				} else {
					list.get(position).setShowAll(true);
				}
				morePosition = position;
				CampusContactsAdapter.this.notifyDataSetChanged();
			}
		});

		// 如果没人点赞则隐藏点赞区
		if (list.get(position).getLikeCount() == 0) {
			viewHolder.ll_root_like.setVisibility(View.GONE);
			viewHolder.iv_line.setVisibility(View.GONE);
			// 如果没人评论则隐藏评论区
			if (list.get(position).getAppraiseList() == null
					|| list.get(position).getAppraiseList().size() == 0) {
				viewHolder.ll_root_appraise.setVisibility(View.GONE);
			} else {
				viewHolder.ll_root_appraise.setVisibility(View.VISIBLE);
			}
			// 否则显示整个评论和点赞区
		} else if (list.get(position).getLikeCount() > 0) {
			viewHolder.ll_root_like.setVisibility(View.VISIBLE);
			viewHolder.ll_root_appraise.setVisibility(View.VISIBLE);
			// 如果没人评论则隐藏评论区
			if (list.get(position).getAppraiseList() == null
					|| list.get(position).getAppraiseList().size() == 0) {
				viewHolder.iv_line.setVisibility(View.GONE);
			} else {
				viewHolder.iv_line.setVisibility(View.VISIBLE);
			}
		}

		MyUtils.setListViewHeight(viewHolder.lv_appraise, 0);
		return convertView;
	}

	class ViewHolder {
		private TextView tv_location, tv_time, tv_likeCount;
		// private TextView tv_ulcount;
		private CircleView cv_headShot;
		private Button btn_name;
		private Button btn_content;
		private Button btn_more;
		private ImageView iv_dialog;
		private GridView gv_picture;
		private LinearLayout ll_like, ll_appraise, ll_root_appraise,
				ll_appraise_up, ll_root_like, ll_content;
		private ListView lv_appraise;
		private ImageView iv_line, iv_sex;
	}

	/**
	 * 发表的图片适配器
	 * 
	 * @author yang
	 * 
	 */
	class MyPictureAdapter extends BaseAdapter {
		private List<String> list;
		private LayoutInflater inflater;
		private ViewHolder viewHolder;

		public MyPictureAdapter(Context context, List<String> list) {
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
			if (list.get(position) != null) {
				// 加载网络图片
				Glide.with(activity).load(list.get(position))
						.placeholder(R.drawable.imgloading).override(300, 300)
						.into(viewHolder.iv_picture);
			}

			return convertView;
		}

		class ViewHolder {
			public ImageView iv_picture;
		}

	}

	/**
	 * 评论适配器
	 * 
	 * @author yang
	 * 
	 */
	class MyAppraiseAdapter extends BaseAdapter {
		private List<CampusContactsReplyInfo> list2;
		private LayoutInflater inflater;
		private int length1;
		private int length2;
		private String text;
		private int tag;

		public MyAppraiseAdapter(List<CampusContactsReplyInfo> list2, int tag) {
			this.list2 = list2;
			this.tag = tag;
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			if (list2.size() > 5 && list.get(morePosition).isShowAll()
					&& morePosition == tag) {
				viewHolder.btn_more.setText("收起评论");
				return list2.size();
			} else if (list2.size() > 5) {
				viewHolder.btn_more.setText("显示全部");
				return 5;
			} else {
				viewHolder.btn_more.setText("显示全部");
				return list2.size();
			}
		}

		@Override
		public Object getItem(int position) {
			return list2.get(position);
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
						R.layout.item_campus_contacts_appraise, null);
				viewHolder.tv_content_appraise = (TextView) convertView
						.findViewById(R.id.tv_content_appraise);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			// 判断是否属于回复
			if (!list2.get(position).getPid().equals("0")) {
				length1 = list2.get(position).getName().length();
				length2 = list2.get(position).getPname().length();
				text = list2.get(position).getName() + "回复"
						+ list2.get(position).getPname() + "："
						+ list2.get(position).getContent();
				// String text2=MyUtils.ToDBC(text);
				SpannableStringBuilder style = new SpannableStringBuilder(text);
				style.setSpan(new ForegroundColorSpan(Color.RED), 0, length1,
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				style.setSpan(new ForegroundColorSpan(Color.RED), length1 + 2,
						length1 + length2 + 2,
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				viewHolder.tv_content_appraise.setText(style);
			} else {
				length1 = list2.get(position).getName().length();
				text = list2.get(position).getName() + "："
						+ list2.get(position).getContent();
				// String text2=MyUtils.ToDBC(text);
				SpannableStringBuilder style = new SpannableStringBuilder(text);
				style.setSpan(new ForegroundColorSpan(Color.RED), 0, length1,
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				viewHolder.tv_content_appraise.setText(style);
			}

			return convertView;
		}

		class ViewHolder {
			public TextView tv_content_appraise;
		}

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
							list.get(mPosition).setIsLiked(1);
							list.get(mPosition).setLikeCount(
									list.get(mPosition).getLikeCount() + 1);
							CampusContactsAdapter.this.notifyDataSetChanged();
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
