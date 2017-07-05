package com.loosoo100.campus100.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loosoo100.campus100.R;
import com.loosoo100.campus100.beans.BusRunsInfo;
import com.loosoo100.campus100.utils.MyAnimation;

/**
 * 
 * @author yang 校园包车班次列表适配器
 */
public class BusNumberOfRunsAdapter extends BaseAdapter {

	private List<BusRunsInfo> list;
	private Context context;
	private LayoutInflater inflater;
	private ViewHolder viewHolder = null;
	
	private int index;

	public BusNumberOfRunsAdapter(Context context, List<BusRunsInfo> list) {
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
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_bus_number_of_runs,
					null);
			viewHolder.tv_time = (TextView) convertView
					.findViewById(R.id.tv_time);
			viewHolder.tv_startingPoint = (TextView) convertView
					.findViewById(R.id.tv_startingPoint);
			viewHolder.tv_destination = (TextView) convertView
					.findViewById(R.id.tv_destination);
			viewHolder.tv_price = (TextView) convertView
					.findViewById(R.id.tv_price);
			viewHolder.rl_pullDown = (RelativeLayout) convertView
					.findViewById(R.id.rl_pullDown);
			viewHolder.rl_bus_byway = (RelativeLayout) convertView
					.findViewById(R.id.rl_bus_byway);
			viewHolder.iv_pullDown = (ImageView) convertView
					.findViewById(R.id.iv_pullDown);
			viewHolder.tv_startByway = (TextView) convertView
					.findViewById(R.id.tv_startByway);
			viewHolder.tv_endByway = (TextView) convertView
					.findViewById(R.id.tv_endByway);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tv_time.setText(list.get(position).getTime());
		viewHolder.tv_startingPoint
				.setText(list.get(position).getStartSchool());
		viewHolder.tv_startByway.setText(list.get(position).getStartSchool());
		viewHolder.tv_destination.setText(list.get(position).getEndSchool());
		viewHolder.tv_endByway.setText(list.get(position).getEndSchool());
		viewHolder.tv_price.setText("￥" + list.get(position).getPrice());

		viewHolder.rl_bus_byway.setTag(position);

		//设置是否显示途经内容
		if (list.get(position).isShowByway()) {
			viewHolder.rl_bus_byway.setVisibility(View.VISIBLE);
			if (Integer.valueOf(viewHolder.rl_bus_byway.getTag().toString()) == index) {
				viewHolder.rl_bus_byway.startAnimation(MyAnimation
						.getScaleAnimationToTopBus());
			}
			//图标旋转180度
			viewHolder.iv_pullDown.setRotation(180);
		} else {
			viewHolder.rl_bus_byway.setVisibility(View.GONE);
			viewHolder.iv_pullDown.setRotation(0);
		}

		viewHolder.rl_pullDown.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (list.get(position).isShowByway()) {
					list.get(position).setShowByway(false);
				} else {
					list.get(position).setShowByway(true);
				}
				index = position;
				BusNumberOfRunsAdapter.this.notifyDataSetChanged();
			}
		});

		return convertView;
	}

	private class ViewHolder {
		private TextView tv_time, tv_startingPoint, tv_destination, tv_price;
		private TextView tv_startByway, tv_endByway;
		private RelativeLayout rl_pullDown;
		private RelativeLayout rl_bus_byway;
		private ImageView iv_pullDown;
	}

}
