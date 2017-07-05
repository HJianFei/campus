package com.loosoo100.campus100.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.beans.CommunityActivityInfo;
import com.loosoo100.campus100.view.RoundAngleImageView;

/**
 * 
 * @author yang 社团详情适配器(最多显示3个)
 */
public class CommunityDetailActivityAdapter extends BaseAdapter {

	private List<CommunityActivityInfo> list;
	private LayoutInflater inflater;
	private Activity activity;

	public CommunityDetailActivityAdapter(Context context,
			List<CommunityActivityInfo> list) {
		this.list = list;
		activity = (Activity) context;
		inflater = LayoutInflater.from(context);

	}

	@Override
	public int getCount() {
		if (list.size() > 3) {
			return 3;
		}
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
			convertView = inflater.inflate(R.layout.item_community_activity,
					null);
			viewHolder.ll_root = (LinearLayout) convertView
					.findViewById(R.id.ll_root);
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
			viewHolder.progressBar = (ProgressBar) convertView
					.findViewById(R.id.progressBar);
			viewHolder.raiv_picture = (RoundAngleImageView) convertView
					.findViewById(R.id.raiv_picture);
			viewHolder.iv_finish = (ImageView) convertView
					.findViewById(R.id.iv_finish);
			viewHolder.iv_line = (ImageView) convertView
					.findViewById(R.id.iv_line);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (list.get(position).getStatus() == 0) {
			viewHolder.iv_finish.setVisibility(View.GONE);
			viewHolder.ll_root.setBackgroundDrawable(activity.getResources()
					.getDrawable(R.drawable.selector_ll));
		} else {
			viewHolder.iv_finish.setVisibility(View.VISIBLE);
			viewHolder.ll_root.setBackgroundColor(activity.getResources()
					.getColor(R.color.gray_f8f8f8));
		}
		if (position == 2) {
			viewHolder.iv_line.setVisibility(View.GONE);
		} else if (position == list.size() - 1) {
			viewHolder.iv_line.setVisibility(View.GONE);
		} else {
			viewHolder.iv_line.setVisibility(View.VISIBLE);
		}
		viewHolder.tv_name.setText(list.get(position).getActivityName());
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
		if (list.get(position).getActiHeadShot() != null
				&& !list.get(position).getActiHeadShot().equals("")) {
			Glide.with(activity).load(list.get(position).getActiHeadShot())
					.asBitmap().override(200, 200)
					.into(viewHolder.raiv_picture);
		} else {
			Glide.with(activity).load("").asBitmap()
					.placeholder(R.drawable.imgloading).override(200, 200)
					.into(viewHolder.raiv_picture);
		}
		return convertView;
	}

	class ViewHolder {
		private TextView tv_name, tv_needMoney, tv_raisedMoney,
				tv_supportCount, tv_percent;
		private ProgressBar progressBar;
		private ImageView iv_finish, iv_line;
		private RoundAngleImageView raiv_picture;
		private LinearLayout ll_root;
	}

}
