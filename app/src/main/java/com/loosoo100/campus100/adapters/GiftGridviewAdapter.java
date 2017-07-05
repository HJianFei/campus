package com.loosoo100.campus100.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.activities.GiftActivity;
import com.loosoo100.campus100.beans.GiftCategoryInfo;

/**
 * 
 * @author yang 商品分类girdview适配器
 */
public class GiftGridviewAdapter extends BaseAdapter {

	private List<GiftCategoryInfo> list;
	private LayoutInflater inflater;

	private GiftActivity activity;

	public GiftGridviewAdapter(Context context, List<GiftCategoryInfo> list) {
		this.list = list;
		inflater = LayoutInflater.from(context);
		activity = (GiftActivity) context;
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
			convertView = inflater.inflate(R.layout.item_gift_gridview, null);
			viewHolder.iv_categoryIcon = (ImageView) convertView
					.findViewById(R.id.iv_categoryIcon);
			viewHolder.tv_categoryName = (TextView) convertView
					.findViewById(R.id.tv_categoryName);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// 设置类目名
		viewHolder.tv_categoryName.setText(list.get(position)
				.getCategoryThird());
		// 设置图片
		Glide.with(activity).load(list.get(position).getPicUrl())
				.placeholder(R.drawable.imgloading).override(200, 200)
				.into(viewHolder.iv_categoryIcon);
		// viewHolder.root_gridview.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// Intent intent = new Intent(activity,
		// GiftListDetailActivity.class);
		// activity.startActivity(intent);
		// }
		// });
		return convertView;
	}

	class ViewHolder {
		private TextView tv_categoryName;
		private ImageView iv_categoryIcon;
	}

}
