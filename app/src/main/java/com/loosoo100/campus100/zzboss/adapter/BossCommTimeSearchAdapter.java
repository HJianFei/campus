package com.loosoo100.campus100.zzboss.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.loosoo100.campus100.R;

/**
 * 
 * @author yang 企业版搜索活动适配器
 */
public class BossCommTimeSearchAdapter extends BaseAdapter {

	private List<String> list;
	private LayoutInflater inflater;

	public BossCommTimeSearchAdapter(Context context, List<String> list) {
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
			convertView = inflater.inflate(
					R.layout.item_boss_comm_time_search, null);
			viewHolder.tv_name = (TextView) convertView
					.findViewById(R.id.tv_name);
			viewHolder.iv_bottom = (ImageView) convertView
					.findViewById(R.id.iv_bottom);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tv_name.setText(list.get(position));
		if (position == list.size() - 1) {
			viewHolder.iv_bottom.setVisibility(View.VISIBLE);
		} else {
			viewHolder.iv_bottom.setVisibility(View.GONE);
		}
		return convertView;
	}

	class ViewHolder {
		private TextView tv_name;
		private ImageView iv_bottom;
	}

}
