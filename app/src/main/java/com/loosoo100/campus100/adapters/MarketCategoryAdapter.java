package com.loosoo100.campus100.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loosoo100.campus100.R;
import com.loosoo100.campus100.activities.HomeActivity;
import com.loosoo100.campus100.beans.CategoryInfo;

/**
 * 
 * @author yang 商品分类适配器
 */
public class MarketCategoryAdapter extends BaseAdapter {

	private List<CategoryInfo> list;
	private LayoutInflater inflater;

	private HomeActivity activity;

	public MarketCategoryAdapter(Context context, List<CategoryInfo> list) {
		this.list = list;
		inflater = LayoutInflater.from(context);
		activity = (HomeActivity) context;
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
			convertView = inflater.inflate(R.layout.item_category, null);
			viewHolder.category = (TextView) convertView
					.findViewById(R.id.tv_category);
			viewHolder.iv_redline_category = (ImageView) convertView
					.findViewById(R.id.iv_redline_category);
			viewHolder.rl_root_bg = (RelativeLayout) convertView
					.findViewById(R.id.rl_root_bg);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.category.setText(list.get(position).getCategoryName());
		if (list.get(position).isShowRedline()) {
			viewHolder.iv_redline_category.setBackgroundColor(activity
					.getResources().getColor(R.color.red_fd3c49));
			viewHolder.rl_root_bg.setBackgroundColor(activity.getResources()
					.getColor(R.color.white));
		} else {
			viewHolder.iv_redline_category.setBackgroundColor(activity
					.getResources().getColor(R.color.gray_d0d0d0));
			viewHolder.rl_root_bg.setBackgroundColor(activity.getResources()
					.getColor(R.color.gray_f8f8f8));
		}
		return convertView;
	}

	class ViewHolder {
		private TextView category;
		private ImageView iv_redline_category;
		private RelativeLayout rl_root_bg;
	}

}
