package com.loosoo100.campus100.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.loosoo100.campus100.R;
import com.loosoo100.campus100.beans.CashoutInfo;

/**
 * 
 * @author yang 提现记录适配器
 */
public class CashoutHistoryAdapter extends BaseAdapter {

	public List<CashoutInfo> list;
	private Context context;
	private LayoutInflater inflater;

	public CashoutHistoryAdapter(Context context, List<CashoutInfo> list) {
		this.context = context;
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
			convertView = inflater.inflate(R.layout.item_cashout_history, null);
			viewHolder.tv_status = (TextView) convertView
					.findViewById(R.id.tv_status);
			viewHolder.tv_money = (TextView) convertView
					.findViewById(R.id.tv_money);
			viewHolder.tv_time = (TextView) convertView
					.findViewById(R.id.tv_time);
			viewHolder.tv_account = (TextView) convertView
					.findViewById(R.id.tv_account);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (list.get(position).getStatus().equals("2")) {
			viewHolder.tv_status.setText("提现成功");
		} else {
			viewHolder.tv_status.setText("提现处理中");
		}
		viewHolder.tv_money.setText("￥" + list.get(position).getMoney());
		viewHolder.tv_time.setText("" + list.get(position).getTime());
		String num = list.get(position).getNum();
		if (num.length() > 4) {
			num = num.substring(num.length() - 4);
		}
		viewHolder.tv_account.setText("提现至" + list.get(position).getAccount()
				+ "(尾号" + num + ")");

		return convertView;
	}

	private class ViewHolder {
		private TextView tv_status, tv_money, tv_time, tv_account;
	}

}
