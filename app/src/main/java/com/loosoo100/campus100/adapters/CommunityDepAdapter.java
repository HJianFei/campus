package com.loosoo100.campus100.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.loosoo100.campus100.R;
import com.loosoo100.campus100.activities.CommunityCreateActivity;
import com.loosoo100.campus100.beans.CommunityDepInfo;

/**
 * 
 * @author yang 社团架构girdview适配器
 */
public class CommunityDepAdapter extends BaseAdapter {

	private List<CommunityDepInfo> list;
	private LayoutInflater inflater;
	private ViewHolder viewHolder = null;
	private CommunityCreateActivity activity;

	public CommunityDepAdapter(Context context, List<CommunityDepInfo> list) {
		this.list = list;
		activity = (CommunityCreateActivity) context;
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
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_community_dep, null);
			viewHolder.tv_dep = (TextView) convertView
					.findViewById(R.id.tv_dep);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tv_dep.setText(list.get(position).getText());
		if (list.get(position).isChecked()) {
			viewHolder.tv_dep.setBackgroundDrawable(activity.getResources()
					.getDrawable(R.drawable.shape_red_stroke_small));
			viewHolder.tv_dep.setTextColor(activity.getResources().getColor(
					R.color.red_fd3c49));
		} else {
			viewHolder.tv_dep.setBackgroundDrawable(activity.getResources()
					.getDrawable(R.drawable.shape_gray_stroke));
			viewHolder.tv_dep.setTextColor(activity.getResources().getColor(
					R.color.gray_b3b3b3));
		}
		return convertView;
	}

	class ViewHolder {
		private TextView tv_dep;
	}

}
