package com.loosoo100.campus100.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.loosoo100.campus100.R;
import com.loosoo100.campus100.activities.AddressAddActivity;
import com.loosoo100.campus100.beans.BusOrderInfo;
import com.loosoo100.campus100.chat.utils.ToastUtil;

/**
 * 
 * @author yang 校园包车订单适配器
 */
public class BusOrderAdapter extends BaseAdapter {

	private List<BusOrderInfo> list;
	private Context context;
	private LayoutInflater inflater;
	
	public static boolean isShowGarbage=false;

	public BusOrderAdapter(Context context, List<BusOrderInfo> list) {
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
			convertView = inflater.inflate(R.layout.item_bus_order,
					null);
			viewHolder.tv_destination = (TextView) convertView
					.findViewById(R.id.tv_destination);
			viewHolder.tv_startingPoint = (TextView) convertView
					.findViewById(R.id.tv_startingPoint);
			viewHolder.tv_time = (TextView) convertView
					.findViewById(R.id.tv_time);
			viewHolder.tv_date = (TextView) convertView
					.findViewById(R.id.tv_date);
			viewHolder.tv_price = (TextView) convertView
					.findViewById(R.id.tv_price);
			viewHolder.tv_orderDate = (TextView) convertView
					.findViewById(R.id.tv_orderDate);
			viewHolder.ib_garbage = (ImageButton) convertView
					.findViewById(R.id.ib_garbage);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tv_startingPoint.setText(list.get(position).getStartingPoint());
		viewHolder.tv_destination.setText(list.get(position)
				.getDestination());
		viewHolder.tv_time.setText(list.get(position).getTime());
		viewHolder.tv_date.setText(list.get(position).getDate());
		viewHolder.tv_orderDate.setText("下单时间："+list.get(position).getOrderTime());
		viewHolder.tv_price.setText("￥" + list.get(position).getPrice());

		if (isShowGarbage) {
			viewHolder.ib_garbage.setVisibility(View.VISIBLE);
		}else {
			viewHolder.ib_garbage.setVisibility(View.GONE);
		}
		viewHolder.ib_garbage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				list.remove(position);
				ToastUtil.showToast(context,"订单删除成功");
				BusOrderAdapter.this.notifyDataSetChanged();
			}
		});
		return convertView;
	}

	private class ViewHolder {
		private TextView tv_destination, tv_startingPoint, tv_time, tv_date,tv_price,tv_orderDate;
		private ImageButton ib_garbage;
	}

}
