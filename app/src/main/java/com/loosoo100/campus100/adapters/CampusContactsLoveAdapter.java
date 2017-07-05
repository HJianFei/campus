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
 * @author yang 社团入驻适配器
 */
public class CampusContactsLoveAdapter extends BaseAdapter {

	private List<CampusContactsLoveInfo> list;
	private LayoutInflater inflater;
	private Activity activity;

	public CampusContactsLoveAdapter(Context context,
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
			convertView = inflater.inflate(R.layout.item_campus_contacts_love,
					null);
			viewHolder.tv_name = (TextView) convertView
					.findViewById(R.id.tv_name);
			viewHolder.tv_school = (TextView) convertView
					.findViewById(R.id.tv_school);
			viewHolder.iv_love = (ImageView) convertView
					.findViewById(R.id.iv_love);
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

		if (list.get(position).getStatus().equals("")) {
			viewHolder.iv_love.setImageDrawable(activity.getResources()
					.getDrawable(R.drawable.campus_heartbeat_half));
		} else {
			viewHolder.iv_love.setImageDrawable(activity.getResources()
					.getDrawable(R.drawable.campus_heartbeat));
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
		private TextView tv_name, tv_school;
		private ImageView iv_love, iv_sex;
		private CircleView cv_picture;
	}

}
