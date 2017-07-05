package com.loosoo100.campus100.adapters;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.loosoo100.campus100.R;

public class CampusPublishGridViewAdapter extends BaseAdapter {

	private Context mContext;
	private List<String> list = new ArrayList<String>();
	private Activity activity;

	/**
	 * 获取列表数据
	 * 
	 * @param list
	 */
	public void setList(List<String> list) {
		this.list = list;
		this.notifyDataSetChanged();
	}

	public CampusPublishGridViewAdapter(Context mContext, List<String> list) {
		this.mContext = mContext;
		this.list = list;
		activity = (Activity) mContext;
	}

	@Override
	public int getCount() {
		if (list == null) {
			return 1;
		} else if (list.size() == 9) {
			return 9;
		} else {
			return list.size() + 1;
		}
	}

	@Override
	public Object getItem(int position) {
		if (list != null && list.size() == 9) {
			return list.get(position);
		}

		else if (list == null || position - 1 < 0 || position > list.size()) {
			return null;
		} else {
			return list.get(position - 1);
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_published_grida, null);
			holder = new ViewHolder();
			holder.item_grida_image = (ImageView) convertView
					.findViewById(R.id.item_grida_image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (list != null && list.size() != 0) {
			if (position != (list == null ? 0 : list.size())) {
				Glide.with(activity).load(list.get(position))
						.placeholder(R.drawable.imgloading)
						.into(holder.item_grida_image);
			} else {
				Glide.with(activity).load(R.drawable.icon_addpic_focused)
						.into(holder.item_grida_image);
			}
		}else{
			Glide.with(activity).load(R.drawable.icon_addpic_focused)
					.into(holder.item_grida_image);
		}

		return convertView;
	}

	/**
	 * 判断当前下标是否是最大值
	 *
	 *            当前下标
	 * @return
	 */
	// private boolean isShowAddItem(int position) {
	// int size = list == null ? 0 : list.size();
	// return position == size;
	// }

	class ViewHolder {
		ImageView item_grida_image;
	}

}
