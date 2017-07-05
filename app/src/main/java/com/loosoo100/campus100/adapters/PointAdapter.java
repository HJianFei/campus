package com.loosoo100.campus100.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.loosoo100.campus100.R;
import com.loosoo100.campus100.beans.PointInfo;

/**
 * 
 * @author yang 积分列表适配器
 */
public class PointAdapter extends BaseAdapter {

	private List<PointInfo> list;
	private Activity activity;
	private LayoutInflater inflater;

	public PointAdapter(Context context, List<PointInfo> list) {
		activity = (Activity) context;
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
			convertView = inflater.inflate(R.layout.item_point_history, null);
			viewHolder.tv_type = (TextView) convertView
					.findViewById(R.id.tv_type);
			viewHolder.tv_time = (TextView) convertView
					.findViewById(R.id.tv_time);
			viewHolder.tv_point = (TextView) convertView
					.findViewById(R.id.tv_point);
			viewHolder.tv_pointChange = (TextView) convertView
					.findViewById(R.id.tv_pointChange);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tv_type.setText(list.get(position).getType());
		viewHolder.tv_time.setText(list.get(position).getTime());
		if (list.get(position).getPointChange() >= 0) {
			viewHolder.tv_pointChange.setText("+"
					+ list.get(position).getPointChange() + "(积分)");
			viewHolder.tv_pointChange.setTextColor(activity.getResources()
					.getColor(R.color.green_388d78));
		} else {
			viewHolder.tv_pointChange.setText(list.get(position)
					.getPointChange() + "(积分)");
			viewHolder.tv_pointChange.setTextColor(activity.getResources()
					.getColor(R.color.red_fd3c49));
		}
		viewHolder.tv_point.setText("" + list.get(position).getPoint());

		return convertView;
	}

	private class ViewHolder {
		private TextView tv_type, tv_time, tv_point, tv_pointChange;
	}

}
