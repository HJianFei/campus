package com.loosoo100.campus100.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.loosoo100.campus100.R;
import com.loosoo100.campus100.beans.CampusInfo;

/**
 * 
 * @author yang 搜索学校适配器
 */
public class SearchAdapter extends BaseAdapter {

	private List<CampusInfo> list;
	private LayoutInflater inflater;

	public SearchAdapter(Context context, List<CampusInfo> list) {
		this.list = list;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_search_campus, null);
//			viewHolder.campusIcon = (ImageView) convertView
//					.findViewById(R.id.iv_search_campusIcon);
			viewHolder.campusName = (TextView) convertView
					.findViewById(R.id.tv_search_campusName);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
//		viewHolder.campusIcon.setTag(position);
		viewHolder.campusName.setText(list.get(position).getCampusName());
//		if (!list.get(position).getSchool_picthumb().equals("")) {
//			Glide.with(activity).load(list.get(position).getSchool_picthumb())
//					.placeholder(R.drawable.imgloading).override(72, 72)
//					.into(viewHolder.campusIcon);
//		}
		return convertView;
	}

	class ViewHolder {
		private TextView campusName;
//		private ImageView campusIcon;
	}

}
