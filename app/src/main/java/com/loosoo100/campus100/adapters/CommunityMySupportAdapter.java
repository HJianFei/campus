package com.loosoo100.campus100.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.activities.CommunitySupportActivity;
import com.loosoo100.campus100.beans.CommunityActivityInfo;
import com.loosoo100.campus100.view.RoundAngleImageView;

/**
 * 
 * @author yang 社团我支持的活动适配器
 */
public class CommunityMySupportAdapter extends BaseAdapter {

	private List<CommunityActivityInfo> list;
	private LayoutInflater inflater;
	private Activity activity;

	public CommunityMySupportAdapter(Context context,
			List<CommunityActivityInfo> list) {
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
			convertView = inflater.inflate(R.layout.item_community_mysupport,
					null);
			viewHolder.tv_name = (TextView) convertView
					.findViewById(R.id.tv_name);
			viewHolder.tv_needMoney = (TextView) convertView
					.findViewById(R.id.tv_needMoney);
			viewHolder.tv_raisedMoney = (TextView) convertView
					.findViewById(R.id.tv_raisedMoney);
			viewHolder.tv_supportCount = (TextView) convertView
					.findViewById(R.id.tv_supportCount);
			viewHolder.tv_percent = (TextView) convertView
					.findViewById(R.id.tv_percent);
			viewHolder.tv_date = (TextView) convertView
					.findViewById(R.id.tv_date);
			viewHolder.tv_supportMoney = (TextView) convertView
					.findViewById(R.id.tv_supportMoney);
			viewHolder.progressBar = (ProgressBar) convertView
					.findViewById(R.id.progressBar);
			viewHolder.raiv_picture = (RoundAngleImageView) convertView
					.findViewById(R.id.raiv_picture);
			viewHolder.btn_support = (Button) convertView
					.findViewById(R.id.btn_support);
			viewHolder.btn_overdue = (Button) convertView
					.findViewById(R.id.btn_overdue);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tv_name.setText(list.get(position).getActivityName());
		viewHolder.tv_date.setText(list.get(position).getDate());
		viewHolder.tv_supportMoney.setText("￥"
				+ list.get(position).getMySupportMoney());
		viewHolder.tv_needMoney
				.setText("￥" + list.get(position).getNeedMoney());
		viewHolder.tv_raisedMoney.setText("￥"
				+ list.get(position).getRaisedMoney());
		viewHolder.tv_supportCount.setText(list.get(position).getSupportCount()
				+ "");
		viewHolder.tv_percent.setText((int) (list.get(position)
				.getRaisedMoney() / list.get(position).getNeedMoney() * 100)
				+ "%");
		viewHolder.progressBar.setProgress((int) (list.get(position)
				.getRaisedMoney() / list.get(position).getNeedMoney() * 100));
		Glide.with(activity).load(list.get(position).getActiHeadShot()).asBitmap()
				.into(viewHolder.raiv_picture);

		if (list.get(position).getStatus() == 0) {
			viewHolder.btn_support.setVisibility(View.VISIBLE);
			viewHolder.btn_overdue.setVisibility(View.GONE);
		} else if (list.get(position).getStatus() == 1) {
			viewHolder.btn_support.setVisibility(View.GONE);
			viewHolder.btn_overdue.setVisibility(View.VISIBLE);
		}
		viewHolder.btn_support.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity,
						CommunitySupportActivity.class);
				intent.putExtra("id", list.get(position).getId());
				intent.putExtra("cid", list.get(position).getCommId());
				activity.startActivityForResult(intent, 0);
			}
		});
		return convertView;
	}

	class ViewHolder {
		private TextView tv_name, tv_needMoney, tv_raisedMoney,
				tv_supportCount, tv_percent, tv_date, tv_supportMoney;
		private ProgressBar progressBar;
		private RoundAngleImageView raiv_picture;
		private Button btn_support, btn_overdue;
	}

}
