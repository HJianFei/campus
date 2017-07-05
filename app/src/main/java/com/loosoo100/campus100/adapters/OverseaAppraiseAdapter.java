package com.loosoo100.campus100.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.activities.CampusContactsFriendActivity;
import com.loosoo100.campus100.activities.CampusContactsPersonalActivity;
import com.loosoo100.campus100.beans.OverseasReplyInfo;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.view.CircleView;

/**
 * @author yang 海归问答评论适配器
 */
public class OverseaAppraiseAdapter extends BaseAdapter {
	private List<OverseasReplyInfo> list;
	private LayoutInflater inflater;
	private Activity activity;
	private String uid = "";

	public OverseaAppraiseAdapter(Context context, List<OverseasReplyInfo> list) {
		activity = (Activity) context;
		this.list = list;

		inflater = LayoutInflater.from(context);
		uid = activity.getSharedPreferences(UserInfoDB.USERTABLE,
				activity.MODE_PRIVATE).getString(UserInfoDB.USERID, "");
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
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater
					.inflate(R.layout.item_oversea_appraise, null);
			viewHolder.tv_name = (TextView) convertView
					.findViewById(R.id.tv_name);
			viewHolder.tv_respond = (TextView) convertView
					.findViewById(R.id.tv_respond);
			viewHolder.tv_name_respondent = (TextView) convertView
					.findViewById(R.id.tv_name_respondent);
			viewHolder.tv_content_appraise = (TextView) convertView
					.findViewById(R.id.tv_content_appraise);
			viewHolder.cv_headShot_appraise = (CircleView) convertView
					.findViewById(R.id.cv_headShot_appraise);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// 评论的人的头像
		if (list.get(position).getHeadShot() != null
				&& !list.get(position).getHeadShot().equals("")) {
			Glide.with(activity).load(list.get(position).getHeadShot())
					.override(90, 90).into(viewHolder.cv_headShot_appraise);
		} else {
			Glide.with(activity).load("").placeholder(R.drawable.imgloading)
					.override(90, 90).into(viewHolder.cv_headShot_appraise);
		}
		// 评论的人
		viewHolder.tv_name.setText(list.get(position).getName());
		// 评论内容
		viewHolder.tv_content_appraise.setText(list.get(position).getContent());
		if (!list.get(position).getPname().equals("")) {
			// 是否属于回复
			viewHolder.tv_respond.setText("回复");
			// 回复给谁
			viewHolder.tv_name_respondent
					.setText(list.get(position).getPname());
		}

		viewHolder.cv_headShot_appraise
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (uid.equals(list.get(position).getUid())) {
							Intent intent = new Intent(activity,
									CampusContactsPersonalActivity.class);
							activity.startActivity(intent);
						} else {
							Intent intent = new Intent(activity,
									CampusContactsFriendActivity.class);
//							intent.putExtra("uid", uid);
//							intent.putExtra("sex", list.get(position).getSex());
							intent.putExtra("muid", list.get(position).getUid());
//							intent.putExtra("headShot", list.get(position)
//									.getHeadShot());
//							intent.putExtra("name", list.get(position).getName());
							activity.startActivity(intent);
						}
					}
				});
		return convertView;
	}

	class ViewHolder {
		public TextView tv_name, tv_respond, tv_name_respondent,
				tv_content_appraise;
		public CircleView cv_headShot_appraise;
	}

}
