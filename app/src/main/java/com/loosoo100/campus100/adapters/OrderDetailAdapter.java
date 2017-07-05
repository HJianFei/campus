package com.loosoo100.campus100.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.loosoo100.campus100.R;
import com.loosoo100.campus100.beans.GoodsInfo;

/**
 * 
 * @author yang 小卖部订单详情适配器
 */
public class OrderDetailAdapter extends BaseAdapter {

	private List<GoodsInfo> list;
	private LayoutInflater inflater;

	public OrderDetailAdapter(Context context, List<GoodsInfo> list) {
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_orderdetail, null);
			viewHolder.goodsName = (TextView) convertView
					.findViewById(R.id.tv_goodsNameOrder);
			viewHolder.goodsPrice = (TextView) convertView
					.findViewById(R.id.tv_goodsPriceOrder);
			viewHolder.goodsCount = (TextView) convertView
					.findViewById(R.id.tv_goodsCountOrder);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.goodsName.setText(list.get(position).getGoodsName());
		viewHolder.goodsPrice.setText("￥"
				+ list.get(position).getCurrentPrice());
		viewHolder.goodsCount.setText("" + list.get(position).getCount());

		return convertView;
	}

	class ViewHolder {
		private TextView goodsName, goodsPrice, goodsCount;
	}

}
