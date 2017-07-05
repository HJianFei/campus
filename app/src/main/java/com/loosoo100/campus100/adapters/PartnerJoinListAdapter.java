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
 * @author yang 校园拍档申请列表适配器
 */
public class PartnerJoinListAdapter extends BaseAdapter {

	private List<CampusInfo> list;
	private LayoutInflater inflater;

	public PartnerJoinListAdapter(Context context, List<CampusInfo> list) {
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
			convertView = inflater.inflate(R.layout.item_partner_join, null);
			viewHolder.textview = (TextView) convertView
					.findViewById(R.id.textview);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.textview.setText(list.get(position).getCampusName());
		return convertView;
	}

	class ViewHolder {
		private TextView textview;
	}

}
