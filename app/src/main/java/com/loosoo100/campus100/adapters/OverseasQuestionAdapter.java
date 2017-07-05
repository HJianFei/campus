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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.ImageVideoBitmapDecoder;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.activities.CampusContactsFriendActivity;
import com.loosoo100.campus100.activities.CampusContactsPersonalActivity;
import com.loosoo100.campus100.beans.OverseasQuestionInfo;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.view.CircleView;

/**
 * 
 * @author yang 海归求助问答适配器
 */
public class OverseasQuestionAdapter extends BaseAdapter {

	private List<OverseasQuestionInfo> list;
	private LayoutInflater inflater;
	private Activity activity;

	private String uid = "";

	public OverseasQuestionAdapter(Context context,
			List<OverseasQuestionInfo> list) {
		this.list = list;
		activity = (Activity) context;
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
			convertView = inflater.inflate(R.layout.item_overseas_question,
					null);
			viewHolder.tv_name = (TextView) convertView
					.findViewById(R.id.tv_name);
			viewHolder.tv_question = (TextView) convertView
					.findViewById(R.id.tv_question);
			viewHolder.tv_readingCount = (TextView) convertView
					.findViewById(R.id.tv_readingCount);
			viewHolder.tv_discussCount = (TextView) convertView
					.findViewById(R.id.tv_discussCount);
			viewHolder.cv_headShot = (CircleView) convertView
					.findViewById(R.id.cv_headShot);
			viewHolder.iv_sex = (ImageView) convertView
					.findViewById(R.id.iv_sex);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tv_name.setText(list.get(position).getName());
		viewHolder.tv_question.setText(list.get(position).getQuestion());
		viewHolder.tv_readingCount.setText(list.get(position).getReadingCount()
				+ "");
		viewHolder.tv_discussCount.setText(list.get(position).getDiscussCount()
				+ "");
		if (!list.get(position).getHeadShot().equals("")) {
			Glide.with(activity).load(list.get(position).getHeadShot())
					.override(200, 200).into(viewHolder.cv_headShot);
		} else {
			Glide.with(activity).load("").placeholder(R.drawable.imgloading)
					.override(200, 200).into(viewHolder.cv_headShot);
		}

		if (list.get(position).getSex().equals("1")) {
			viewHolder.iv_sex.setImageDrawable(activity.getResources()
					.getDrawable(R.drawable.icon_female_picture));
		} else {
			viewHolder.iv_sex.setImageDrawable(activity.getResources()
					.getDrawable(R.drawable.icon_male_picture));
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
//					intent.putExtra("sex", list.get(position).getSex());
					intent.putExtra("muid", list.get(position).getUid());
//					intent.putExtra("headShot", list.get(position)
//							.getHeadShot());
//					intent.putExtra("name", list.get(position).getName());
					activity.startActivity(intent);
				}
			}
		});

		return convertView;
	}

	class ViewHolder {
		private TextView tv_name, tv_question, tv_readingCount,
				tv_discussCount;
		private CircleView cv_headShot;
		private ImageView iv_sex;
	}

}
