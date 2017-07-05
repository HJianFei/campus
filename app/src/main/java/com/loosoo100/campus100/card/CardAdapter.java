package com.loosoo100.campus100.card;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.activities.CampusContactsFriendActivity;
import com.loosoo100.campus100.activities.CampusContactsPersonalActivity;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.view.CircleView;

/**
 * 照片墙卡片适配器
 */
public class CardAdapter extends BaseAdapter {
	private Context mContext;
	private List<PictureWallInfo> list;
	private Activity activity;
	private String uid = "";
	public ViewHolder viewHolder = null;
	public boolean support = false;

	public ImageView iv_like2, iv_dislike2;
	public TextView tv_like2, tv_dislike2;

	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (support) {
				iv_like2.setImageResource(R.drawable.icon_collect_picture_red);
				tv_like2.setText(list.get(0).getSupportCount() + "");
				ToastUtil.showToast(activity,"赞赞");
			} else {
				iv_dislike2.setImageResource(R.drawable.icon_bad_color_pictre);
				tv_dislike2.setText(list.get(0).getOpposeCount() + "");
				ToastUtil.showToast(activity,"踩踩");
			}
		}
	};

	public CardAdapter(Context mContext, List<PictureWallInfo> list, String uid) {
		this.mContext = mContext;
		this.list = list;
		this.uid = uid;
		activity = (Activity) mContext;
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
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.item_picture_wall, parent,
					false);
			viewHolder = new ViewHolder();
			viewHolder.iv_picture = (ImageView) convertView
					.findViewById(R.id.iv_picture);
			viewHolder.iv_sex = (ImageView) convertView
					.findViewById(R.id.iv_sex);
			viewHolder.cv_headShot = (CircleView) convertView
					.findViewById(R.id.cv_headShot);
			viewHolder.tv_dream = (TextView) convertView
					.findViewById(R.id.tv_dream);
			viewHolder.tv_name = (TextView) convertView
					.findViewById(R.id.tv_name);
			viewHolder.tv_school = (TextView) convertView
					.findViewById(R.id.tv_school);
			viewHolder.tv_like = (TextView) convertView
					.findViewById(R.id.tv_like);
			viewHolder.tv_dislike = (TextView) convertView
					.findViewById(R.id.tv_dislike);
			viewHolder.iv_like = (ImageView) convertView
					.findViewById(R.id.iv_like);
			viewHolder.iv_dislike = (ImageView) convertView
					.findViewById(R.id.iv_dislike);
			viewHolder.rl_like = (RelativeLayout) convertView
					.findViewById(R.id.rl_like);
			viewHolder.rl_dislike = (RelativeLayout) convertView
					.findViewById(R.id.rl_dislike);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.iv_like.setTag(position);
		viewHolder.iv_dislike.setTag(position);
		viewHolder.tv_like.setTag(position);
		viewHolder.tv_dislike.setTag(position);

		if (viewHolder.iv_picture.getDrawable() == null) {
			Glide.with(mContext).load(list.get(position).getPicture())
					.dontAnimate().into(viewHolder.iv_picture);
		}

//		if (viewHolder.cv_headShot.getDrawable() == null) {
			Glide.with(mContext).load(list.get(position).getHeadShot())
					.dontAnimate().into(viewHolder.cv_headShot);
//		}

		viewHolder.tv_dream.setText(list.get(position).getDream());
		viewHolder.tv_name.setText(list.get(position).getName());
		viewHolder.tv_school.setText(list.get(position).getSchool());
		viewHolder.tv_like.setText(list.get(position).getSupportCount() + "");
		viewHolder.tv_dislike.setText(list.get(position).getOpposeCount() + "");

		// 性别
		if (list.get(position).getUserSex().equals("1")) {
			viewHolder.iv_sex.setImageDrawable(activity.getResources()
					.getDrawable(R.drawable.icon_female_picture));
		} else {
			viewHolder.iv_sex.setImageDrawable(activity.getResources()
					.getDrawable(R.drawable.icon_male_picture));
		}
		// 判断本人赞或踩或没有赞踩过
		if (list.get(position).getAction().equals("0")) {
			viewHolder.iv_like.setImageDrawable(activity.getResources()
					.getDrawable(R.drawable.icon_collect_picture));
			viewHolder.iv_dislike.setImageDrawable(activity.getResources()
					.getDrawable(R.drawable.icon_bad_pictre));
		} else if (list.get(position).getAction().equals("1")) {
			viewHolder.iv_like.setImageDrawable(activity.getResources()
					.getDrawable(R.drawable.icon_collect_picture_red));
			viewHolder.iv_dislike.setImageDrawable(activity.getResources()
					.getDrawable(R.drawable.icon_bad_pictre));
		} else if (list.get(position).getAction().equals("2")) {
			viewHolder.iv_like.setImageDrawable(activity.getResources()
					.getDrawable(R.drawable.icon_collect_picture));
			viewHolder.iv_dislike.setImageDrawable(activity.getResources()
					.getDrawable(R.drawable.icon_bad_color_pictre));
		}

		viewHolder.cv_headShot.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (uid.equals(list.get(position).getUid())) {
					Intent intent = new Intent(activity,
							CampusContactsPersonalActivity.class);
					activity.startActivity(intent);
				} else {
					Intent intent = new Intent(activity,
							CampusContactsFriendActivity.class);
//					intent.putExtra("uid", uid);
//					intent.putExtra("sex", list.get(position).getUserSex() + "");
					intent.putExtra("muid", list.get(position).getUid());
//					intent.putExtra("headShot", list.get(position)
//							.getHeadShot());
//					intent.putExtra("name", list.get(position).getName());
					activity.startActivity(intent);
				}
			}
		});

		viewHolder.rl_like.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				iv_like2 = (ImageView) v.findViewById(R.id.iv_like);
				tv_like2 = (TextView) v.findViewById(R.id.tv_like);
				if (list.get(position).getAction().equals("0")) {
					new Thread() {
						@Override
						public void run() {
							doPostSupport(MyConfig.URL_POST_PICTURE_WALL_ATTITUDE);
						}
					}.start();
				}
			}
		});

		viewHolder.rl_dislike.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				iv_dislike2 = (ImageView) v.findViewById(R.id.iv_dislike);
				tv_dislike2 = (TextView) v.findViewById(R.id.tv_dislike);
				if (list.get(position).getAction().equals("0")) {
					new Thread() {
						@Override
						public void run() {
							doPostOppose(MyConfig.URL_POST_PICTURE_WALL_ATTITUDE);
						}
					}.start();
				}
			}
		});
		return convertView;
	}

	public class ViewHolder {
		public TextView tv_dream, tv_name, tv_school, tv_like, tv_dislike;
		public ImageView iv_picture, iv_sex, iv_like, iv_dislike;
		public CircleView cv_headShot;
		public RelativeLayout rl_like, rl_dislike;
	}

	private void doPostSupport(String uploadHost) {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("uid", uid);
		params.addBodyParameter("wid", list.get(0).getId());
		params.addBodyParameter("action", "support");
		httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						LogUtils.d("responseInfo",responseInfo.result);
						list.get(0).setAction("1");
						list.get(0).setSupportCount(
								list.get(0).getSupportCount() + 1);
						support = true;
						handler.sendEmptyMessage(0);
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.d("error",arg1.toString());
						ToastUtil.showToast(activity,"操作失败");
					}
				});
	}

	private void doPostOppose(String uploadHost) {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("uid", uid);
		params.addBodyParameter("wid", list.get(0).getId());
		params.addBodyParameter("action", "oppose");
		httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						LogUtils.d("responseInfo",responseInfo.result);
						list.get(0).setAction("2");
						list.get(0).setOpposeCount(
								list.get(0).getOpposeCount() + 1);
						support = false;
						handler.sendEmptyMessage(0);
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.d("error",arg1.toString());
						ToastUtil.showToast(activity,"操作失败");
					}
				});
	}

}
