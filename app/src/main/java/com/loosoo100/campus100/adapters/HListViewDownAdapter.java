package com.loosoo100.campus100.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.loosoo100.campus100.R;
import com.loosoo100.campus100.beans.GoodsInfo;
import com.loosoo100.campus100.utils.MyUtils;

/**
 * 
 * @author yang gallery适配器
 */
public class HListViewDownAdapter extends BaseAdapter {

	private List<GoodsInfo> list;
	private LayoutInflater inflater;

	public HListViewDownAdapter(Context context, List<GoodsInfo> list) {
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
			convertView = inflater.inflate(R.layout.item_gallery_down, null);
			viewHolder.iv_gallery = (ImageView) convertView
					.findViewById(R.id.iv_gallery);
			viewHolder.tv_name = (TextView) convertView
					.findViewById(R.id.tv_name);
			viewHolder.tv_price = (TextView) convertView
					.findViewById(R.id.tv_price);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// viewHolder.iv_gallery.setImageBitmap(list.get(position).getCategoryIcon());
		viewHolder.iv_gallery.setScaleType(ScaleType.CENTER_CROP);
		// 图标设置圆角
		viewHolder.iv_gallery.setImageBitmap(MyUtils.getRoundedCornerBitmap(
				list.get(position).getGoodsIcon(), 30));
		viewHolder.tv_name.setText("" + list.get(position).getGoodsName());
		viewHolder.tv_price.setText("￥" + list.get(position).getCurrentPrice());

		return convertView;
	}

	class ViewHolder {
		private ImageView iv_gallery;
		private TextView tv_name, tv_price;
	}

}
