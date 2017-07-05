package com.loosoo100.campus100.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.loosoo100.campus100.R;
import com.loosoo100.campus100.beans.CommunityMoneyInfo;

/**
 * 
 * @author yang 社团资金列表适配器
 */
public class CommunityMoneyAdapter extends BaseAdapter {

	private List<CommunityMoneyInfo> list;
	private LayoutInflater inflater;
	private Activity activity;

	public CommunityMoneyAdapter(Context context, List<CommunityMoneyInfo> list) {
		this.list = list;
		activity = (Activity) context;
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
			convertView = inflater.inflate(R.layout.item_community_money, null);
			viewHolder.tv_money = (TextView) convertView
					.findViewById(R.id.tv_money);
			viewHolder.tv_time = (TextView) convertView
					.findViewById(R.id.tv_time);
			viewHolder.tv_moneyChange = (TextView) convertView
					.findViewById(R.id.tv_moneyChange);
			viewHolder.tv_type = (TextView) convertView
					.findViewById(R.id.tv_type);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tv_type.setText(list.get(position).getType());
		viewHolder.tv_money.setText("" + list.get(position).getMoney());
		viewHolder.tv_time.setText(list.get(position).getTime());
		if (list.get(position).getMoneyChange() > 0) {
			viewHolder.tv_moneyChange.setTextColor(activity.getResources()
					.getColor(R.color.green_388d78));
			viewHolder.tv_moneyChange.setText("+"
					+ list.get(position).getMoneyChange());
		} else {
			viewHolder.tv_moneyChange.setTextColor(activity.getResources()
					.getColor(R.color.red_fd3c49));
			viewHolder.tv_moneyChange.setText(""
					+ list.get(position).getMoneyChange());
		}

		return convertView;
	}

	private class ViewHolder {
		private TextView tv_type, tv_money, tv_time, tv_moneyChange;
	}

}
