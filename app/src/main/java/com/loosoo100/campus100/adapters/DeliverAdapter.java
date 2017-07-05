package com.loosoo100.campus100.adapters;

import java.util.List;
import java.util.Map;

import android.app.Activity;
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
 * @author yang 物流详情适配器
 */
public class DeliverAdapter extends BaseAdapter {

	private List<Map<String, Object>> list;
	private LayoutInflater inflater;
	private Activity activity;

	public DeliverAdapter(Context context, List<Map<String, Object>> list) {
		this.list = list;
		inflater = LayoutInflater.from(context);
		activity = (Activity) context;
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
			convertView = inflater.inflate(R.layout.item_deliver, null);
			viewHolder.iv_line_top = (ImageView) convertView
					.findViewById(R.id.iv_line_top);
			viewHolder.iv_line_down = (ImageView) convertView
					.findViewById(R.id.iv_line_down);
			viewHolder.iv_circle_gray = (ImageView) convertView
					.findViewById(R.id.iv_circle_gray);
			viewHolder.iv_circle_red = (ImageView) convertView
					.findViewById(R.id.iv_circle_red);
			viewHolder.tv_content = (TextView) convertView
					.findViewById(R.id.tv_content);
			viewHolder.tv_time = (TextView) convertView
					.findViewById(R.id.tv_time);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tv_content.setText(""
				+ list.get(position).get("context").toString());
		viewHolder.tv_time.setText(""
				+ list.get(position).get("time").toString());
		if (position == 0) {
			viewHolder.iv_line_top.setVisibility(View.GONE);
			viewHolder.iv_circle_red.setVisibility(View.VISIBLE);
			viewHolder.tv_content.setTextColor(activity.getResources()
					.getColor(R.color.red_ff4045));
			viewHolder.tv_time.setTextColor(activity.getResources().getColor(
					R.color.red_ff4045));
		} else {
			viewHolder.iv_line_top.setVisibility(View.VISIBLE);
			viewHolder.iv_circle_red.setVisibility(View.GONE);
			viewHolder.tv_content.setTextColor(activity.getResources()
					.getColor(R.color.gray_dcdcdc));
			viewHolder.tv_time.setTextColor(activity.getResources().getColor(
					R.color.gray_dcdcdc));

		}
		if (position == list.size() - 1) {
			viewHolder.iv_line_down.setVisibility(View.GONE);
		}else {
			viewHolder.iv_line_down.setVisibility(View.VISIBLE);
		}

		return convertView;
	}

	class ViewHolder {
		public TextView tv_content, tv_time;
		public ImageView iv_circle_gray, iv_circle_red, iv_line_top,
				iv_line_down;
	}

}
