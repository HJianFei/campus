package com.loosoo100.campus100.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.loosoo100.campus100.R;
import com.loosoo100.campus100.beans.BorrowHistoryInfo;

/**
 * 
 * @author yang 借款记录适配器
 */
public class BorrowHistoryAdapter extends BaseAdapter {

	public static List<BorrowHistoryInfo> list;
	private LayoutInflater inflater;

	public BorrowHistoryAdapter(Context context, List<BorrowHistoryInfo> list) {
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
			convertView = inflater.inflate(R.layout.item_borrow_history, null);
			viewHolder.tv_time = (TextView) convertView
					.findViewById(R.id.tv_time);
			viewHolder.tv_money = (TextView) convertView
					.findViewById(R.id.tv_money);
			viewHolder.tv_way = (TextView) convertView
					.findViewById(R.id.tv_way);
			viewHolder.tv_status = (TextView) convertView
					.findViewById(R.id.tv_status);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tv_time.setText("" + list.get(position).getTime());
		viewHolder.tv_money.setText("￥" + list.get(position).getMoney());
		viewHolder.tv_way.setText("" + list.get(position).getWay());
		viewHolder.tv_status.setText("" + list.get(position).getStatus());

		return convertView;
	}

	class ViewHolder {
		public TextView tv_time, tv_money, tv_way,tv_status;
	}


}
