package com.loosoo100.campus100.zzboss.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.beans.CommunityMemberInfo;
import com.loosoo100.campus100.view.CircleView;

/**
 * 
 * @author yang 社团成员适配器
 */
public class BossCommMemberAdapter extends BaseAdapter {

	private List<CommunityMemberInfo> list;
	private LayoutInflater inflater;

	private Activity activity;

	public BossCommMemberAdapter(Context context, List<CommunityMemberInfo> list) {
		this.list = list;
		activity = (Activity) context;
		inflater = LayoutInflater.from(context);

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
					.inflate(R.layout.item_boss_comm_member, null);
			viewHolder.tv_nickName = (TextView) convertView
					.findViewById(R.id.tv_nickName);
			viewHolder.cv_headShot = (CircleView) convertView
					.findViewById(R.id.cv_headShot);
			viewHolder.iv_sex = (ImageView) convertView
					.findViewById(R.id.iv_sex);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tv_nickName.setText(list.get(position).getNickName());
		if (!list.get(position).getHeadShotString().equals("")) {
			Glide.with(activity).load(list.get(position).getHeadShotString())
					.override(200, 200).into(viewHolder.cv_headShot);
		} else {
			Glide.with(activity).load(R.drawable.imgloading).asBitmap()
					.into(viewHolder.cv_headShot);
		}
		if (list.get(position).getSex() == 0) {
			viewHolder.iv_sex.setImageDrawable(activity.getResources()
					.getDrawable(R.drawable.icon_male_picture));
		} else {
			viewHolder.iv_sex.setImageDrawable(activity.getResources()
					.getDrawable(R.drawable.icon_female_picture));
		}

		return convertView;
	}

	class ViewHolder {
		private TextView tv_nickName;
		private CircleView cv_headShot;
		private ImageView iv_sex;
	}

}
