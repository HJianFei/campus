package com.loosoo100.campus100.adapters;

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
import com.loosoo100.campus100.beans.CampusContactsLoveInfo;
import com.loosoo100.campus100.view.CircleView;

/**
 * 
 * @author yang 校园圈我的友友适配器
 */
public class CampusContactsMyFriendAdapter extends BaseAdapter {

	private List<CampusContactsLoveInfo> list;
	private LayoutInflater inflater;
	private Activity activity;

	public CampusContactsMyFriendAdapter(Context context,
			List<CampusContactsLoveInfo> list) {
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
			convertView = inflater.inflate(
					R.layout.item_campus_contacts_myfriend, null);
			viewHolder.tv_name = (TextView) convertView
					.findViewById(R.id.tv_name);
			viewHolder.tv_school = (TextView) convertView
					.findViewById(R.id.tv_school);
			viewHolder.tv_trueName = (TextView) convertView
					.findViewById(R.id.tv_trueName);
			viewHolder.tv_phone = (TextView) convertView
					.findViewById(R.id.tv_phone);
			viewHolder.tv_weixin = (TextView) convertView
					.findViewById(R.id.tv_weixin);
			viewHolder.iv_sex = (ImageView) convertView
					.findViewById(R.id.iv_sex);
			viewHolder.cv_picture = (CircleView) convertView
					.findViewById(R.id.cv_picture);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tv_name.setText(list.get(position).getName());
		viewHolder.tv_school.setText(list.get(position).getSchool());
		viewHolder.tv_trueName.setText("(" + list.get(position).getTureName()
				+ ")");
		if (list.get(position).getTureName().equals("")) {
			viewHolder.tv_trueName.setVisibility(View.GONE);
		}else {
			viewHolder.tv_trueName.setVisibility(View.VISIBLE);
		}

		if (list.get(position).getPhone().equals("")) {
			viewHolder.tv_phone.setText("未互换");
		} else {
			viewHolder.tv_phone.setText(list.get(position).getPhone());
		}

		if (list.get(position).getWeixin().equals("")) {
			viewHolder.tv_weixin.setText("未互换");
		} else {
			viewHolder.tv_weixin.setText(list.get(position).getWeixin());
		}

		if (list.get(position).getSex().equals("1")) {
			viewHolder.iv_sex.setImageDrawable(activity.getResources()
					.getDrawable(R.drawable.icon_female_picture));
		} else {
			viewHolder.iv_sex.setImageDrawable(activity.getResources()
					.getDrawable(R.drawable.icon_male_picture));
		}

		if (!list.get(position).getHeadShot().equals("")
				&& !list.get(position).getHeadShot().equals("null")) {
			Glide.with(activity).load(list.get(position).getHeadShot())
					.into(viewHolder.cv_picture);
		} else {
			Glide.with(activity).load(R.drawable.imgloading).asBitmap()
					.into(viewHolder.cv_picture);
		}

		return convertView;
	}

	class ViewHolder {
		private TextView tv_name, tv_school, tv_trueName, tv_phone, tv_weixin;
		private ImageView iv_sex;
		private CircleView cv_picture;
	}

}
