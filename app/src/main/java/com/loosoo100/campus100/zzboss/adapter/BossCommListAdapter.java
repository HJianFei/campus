package com.loosoo100.campus100.zzboss.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.beans.CommunityBasicInfo;
import com.loosoo100.campus100.view.CircleView;

/**
 * 
 * @author yang 社团列表适配器
 */
public class BossCommListAdapter extends BaseAdapter {

	private List<CommunityBasicInfo> list;
	private LayoutInflater inflater;
	private Activity activity;

	public BossCommListAdapter(Context context, List<CommunityBasicInfo> list) {
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
			convertView = inflater.inflate(R.layout.item_boss_comm_list, null);
			viewHolder.tv_commName = (TextView) convertView
					.findViewById(R.id.tv_commName);
			viewHolder.tv_city = (TextView) convertView.findViewById(R.id.tv_city);
			viewHolder.tv_school = (TextView) convertView
					.findViewById(R.id.tv_school);
			viewHolder.tv_id = (TextView) convertView
					.findViewById(R.id.tv_id);
			viewHolder.tv_name = (TextView) convertView
					.findViewById(R.id.tv_name);
			viewHolder.cv_picture = (CircleView) convertView
					.findViewById(R.id.cv_picture);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tv_commName.setText(list.get(position)
				.getCommunityName());
		viewHolder.tv_id.setText("" + list.get(position).getId());
		viewHolder.tv_name.setText(list.get(position).getName());
		viewHolder.tv_school.setText(list.get(position).getSchool());
		viewHolder.tv_city.setText(list.get(position).getCity());

		// 加载图片
		Glide.with(activity).load(list.get(position).getHeadthumb())
				.into(viewHolder.cv_picture);

		return convertView;
	}

	class ViewHolder {
		private TextView tv_commName, tv_city, tv_school, tv_id, tv_name;
		private CircleView cv_picture;
	}

}
