package com.loosoo100.campus100.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.loosoo100.campus100.R;
import com.loosoo100.campus100.beans.RepaymentInfo;

/**
 * 
 * @author yang 还款账单适配器
 */
public class RepaymentAdapter extends BaseAdapter {

	private List<RepaymentInfo> list;
	private LayoutInflater inflater;

	public RepaymentAdapter(Context context, List<RepaymentInfo> list) {
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
			convertView = inflater.inflate(R.layout.item_repayment_bill, null);
			viewHolder.tv_orderNumber = (TextView) convertView
					.findViewById(R.id.tv_orderNumber);
			viewHolder.tv_time = (TextView) convertView
					.findViewById(R.id.tv_time);
			viewHolder.tv_money = (TextView) convertView
					.findViewById(R.id.tv_money);
			viewHolder.tv_status = (TextView) convertView
					.findViewById(R.id.tv_status);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tv_orderNumber.setText("" + list.get(position).getPeriods());
		viewHolder.tv_time.setText("" + list.get(position).getTime());
		viewHolder.tv_money.setText("￥" + list.get(position).getMoney());
		viewHolder.tv_status.setText("" + list.get(position).getStatus());
		if (list.get(position).getStatus().toString().equals("未还")) {
			viewHolder.tv_status.setAlpha(0.6f);
		} else {
			viewHolder.tv_status.setAlpha(1);
		}

		return convertView;
	}

	class ViewHolder {
		public TextView tv_orderNumber, tv_time, tv_money, tv_status;
	}

}
