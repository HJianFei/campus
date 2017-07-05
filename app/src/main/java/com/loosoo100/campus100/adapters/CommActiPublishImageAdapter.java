package com.loosoo100.campus100.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.loosoo100.campus100.R;

public class CommActiPublishImageAdapter extends BaseAdapter {

	private Context mContext;
	private List<String> list;
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

	public CommActiPublishImageAdapter(Context mContext, List<String> list) {
		this.mContext = mContext;
		this.list = list;
		activity = (Activity) mContext;
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
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_published_comm_acti, null);
			holder = new ViewHolder();
			holder.iv_image = (ImageView) convertView
					.findViewById(R.id.iv_image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (list.size() > 0) {
			Glide.with(activity).load(list.get(position))
					.placeholder(R.drawable.imgloading).dontAnimate()
					.into(holder.iv_image);
		}

		return convertView;
	}

	class ViewHolder {
		ImageView iv_image;
	}

}
