package com.loosoo100.campus100.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.loosoo100.campus100.R;

/**
 * 
 * @author yang 社团架构girdview适配器
 */
public class CommunityDepGridviewAdapter extends BaseAdapter {

	private List<String> list;
	private LayoutInflater inflater;

	public CommunityDepGridviewAdapter(Context context, List<String> list) {
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater
					.inflate(R.layout.item_community_dep_gv, null);
			viewHolder.tv_dep = (TextView) convertView
					.findViewById(R.id.tv_dep);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tv_dep.setText(list.get(position));
		return convertView;
	}

	class ViewHolder {
		private TextView tv_dep;
	}

}
