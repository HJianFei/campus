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
import com.loosoo100.campus100.card.PictureWallInfo;

/**
 * 
 * @author yang 许愿管理适配器
 */
public class PictureManageAdapter extends BaseAdapter {

	public List<PictureWallInfo> list;
	private Activity activity;
	private LayoutInflater inflater;

	public PictureManageAdapter(Context context, List<PictureWallInfo> list) {
		this.activity = (Activity) context;
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
			convertView = inflater.inflate(R.layout.item_picture_manage, null);
			// 图标
			viewHolder.iv_picture = (ImageView) convertView
					.findViewById(R.id.iv_picture);
			// 日
			viewHolder.tv_day = (TextView) convertView
					.findViewById(R.id.tv_day);
			// 月
			viewHolder.tv_month = (TextView) convertView
					.findViewById(R.id.tv_month);
			// 标题
			viewHolder.tv_title = (TextView) convertView
					.findViewById(R.id.tv_title);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Glide.with(activity).load(list.get(position).getPicture())
				.override(200, 200).placeholder(R.drawable.imgloading)
				.dontAnimate().into(viewHolder.iv_picture);
		viewHolder.tv_day.setText("" + list.get(position).getDay());
		viewHolder.tv_month.setText("" + list.get(position).getMonth() + "月");
		viewHolder.tv_title.setText("" + list.get(position).getDream());

		return convertView;
	}

	private class ViewHolder {
		private ImageView iv_picture;
		private TextView tv_day, tv_month, tv_title;
	}

}
