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
import com.loosoo100.campus100.beans.MyCollectInfo;

/**
 * 
 * @author yang 我的收藏列表适配器
 */
public class MyCollectAdapter extends BaseAdapter {

	private List<MyCollectInfo> list;
	private LayoutInflater inflater;
	private Activity activity;

	public MyCollectAdapter(Context context, List<MyCollectInfo> list) {
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
			convertView = inflater.inflate(R.layout.item_mycollect, null);
			viewHolder.iv_icon = (ImageView) convertView
					.findViewById(R.id.iv_icon);
			viewHolder.tv_name = (TextView) convertView
					.findViewById(R.id.tv_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Glide.with(activity).load(list.get(position).getPicUrl())
				.override(180, 180).placeholder(R.drawable.imgloading)
				.into(viewHolder.iv_icon);
		viewHolder.tv_name.setText(list.get(position).getName());

		return convertView;
	}

	private class ViewHolder {
		private ImageView iv_icon;
		private TextView tv_name;
	}

}
