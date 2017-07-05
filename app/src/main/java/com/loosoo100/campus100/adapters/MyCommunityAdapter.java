package com.loosoo100.campus100.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.loosoo100.campus100.R;
import com.loosoo100.campus100.beans.CommunityBasicInfo;

/**
 * 
 * @author yang 我的社团顶部列表适配器
 */
public class MyCommunityAdapter extends BaseAdapter {

	private List<CommunityBasicInfo> list;
	private LayoutInflater inflater;

	public MyCommunityAdapter(Context context, List<CommunityBasicInfo> list) {
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
			convertView = inflater.inflate(R.layout.item_mycommunity, null);
			viewHolder.tv_comm = (TextView) convertView
					.findViewById(R.id.tv_comm);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tv_comm.setText(list.get(position).getCommunityName());
		return convertView;
	}

	class ViewHolder {
		private TextView tv_comm;
	}

}
