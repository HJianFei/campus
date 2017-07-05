package com.loosoo100.campus100.adapters;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.loosoo100.campus100.R;

/**
 * 
 * @author yang 确认订单详情适配器
 */
public class OrderListAdapter extends BaseAdapter {

	private List<Map<String, Object>> list;
	private LayoutInflater inflater;

	public OrderListAdapter(Context context, List<Map<String, Object>> list) {
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
			convertView = inflater.inflate(R.layout.item_orderlist, null);
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
		viewHolder.goodsName.setText("" + list.get(position).get("name"));
		viewHolder.goodsPrice.setText("￥" + list.get(position).get("price"));
		viewHolder.goodsCount.setText("" + list.get(position).get("count"));

		return convertView;
	}

	class ViewHolder {
		private TextView goodsName, goodsPrice, goodsCount;
	}


}
