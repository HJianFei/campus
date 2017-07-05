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
import com.loosoo100.campus100.beans.CampusContactsMessageInfo;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.CircleView;

/**
 * 
 * @author yang 校园圈消息列表适配器
 */
public class CampusContactsMessageAdapter extends BaseAdapter {

	private List<CampusContactsMessageInfo> list;
	private LayoutInflater inflater;
	private Activity activity;
	private long campusContactsTime;

	public CampusContactsMessageAdapter(Context context,
			List<CampusContactsMessageInfo> list, long campusContactsTime) {
		this.list = list;
		this.campusContactsTime = campusContactsTime;
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
			convertView = inflater.inflate(
					R.layout.item_campus_contacts_message, null);
			viewHolder.iv_line = (ImageView) convertView
					.findViewById(R.id.iv_line);
			viewHolder.iv_like = (ImageView) convertView
					.findViewById(R.id.iv_like);
			viewHolder.cv_headshot = (CircleView) convertView
					.findViewById(R.id.cv_headshot);
			viewHolder.tv_name = (TextView) convertView
					.findViewById(R.id.tv_name);
			viewHolder.tv_content = (TextView) convertView
					.findViewById(R.id.tv_content);
			viewHolder.tv_time = (TextView) convertView
					.findViewById(R.id.tv_time);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (position == list.size() - 1) {
			viewHolder.iv_line.setVisibility(View.GONE);
		} else {
			viewHolder.iv_line.setVisibility(View.VISIBLE);
		}
		if (list.get(position).getPicUrl() != null
				&& !list.get(position).getPicUrl().equals("")
				&& !activity.isDestroyed()) {
			Glide.with(activity).load(list.get(position).getPicUrl())
					.override(150, 150).into(viewHolder.cv_headshot);
		} else {
			if (!activity.isDestroyed()) {
				Glide.with(activity).load("")
						.placeholder(R.drawable.imgloading).override(150, 150)
						.into(viewHolder.cv_headshot);
			}
		}
		viewHolder.tv_name.setText(list.get(position).getName());
		// 设置时间
		if (MyUtils.getSqlDateM(campusContactsTime).equals(
				MyUtils.getSqlDateM(list.get(position).getTime()))) {
			viewHolder.tv_time.setText("今天");
		} else if (MyUtils.getSqlDateM(campusContactsTime - 86400).equals(
				MyUtils.getSqlDateM(list.get(position).getTime()))) {
			viewHolder.tv_time.setText("昨天");
		} else {
			viewHolder.tv_time.setText(MyUtils.getSqlDateMD(list.get(position)
					.getTime()));
		}
		if (list.get(position).getType().equals("1")) {
			viewHolder.iv_like.setVisibility(View.VISIBLE);
			viewHolder.tv_content.setText("赞");
		} else {
			viewHolder.iv_like.setVisibility(View.GONE);
			viewHolder.tv_content.setText(list.get(position).getContent());
		}

		return convertView;
	}

	private class ViewHolder {
		private CircleView cv_headshot;
		private TextView tv_name, tv_content, tv_time;
		private ImageView iv_like, iv_line;
	}

}
