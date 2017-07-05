package com.loosoo100.campus100.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.view.RoundAngleImageView;
import com.loosoo100.campus100.zzboss.beans.BossCompanySummaryInfo;

/**
 * 
 * @author yang 首页企业适配器
 */
public class CommunityBossHomeAdapter extends BaseAdapter {

	private List<BossCompanySummaryInfo> list;
	private LayoutInflater inflater;
	private Activity activity;

	public CommunityBossHomeAdapter(Context context,
			List<BossCompanySummaryInfo> list) {
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
			convertView = inflater.inflate(R.layout.item_community_boss_home,
					null);
			viewHolder.tv_name = (TextView) convertView
					.findViewById(R.id.tv_name);
			viewHolder.tv_count = (TextView) convertView
					.findViewById(R.id.tv_count);
			viewHolder.tv_money = (TextView) convertView
					.findViewById(R.id.tv_money);
			viewHolder.raiv_picture = (RoundAngleImageView) convertView
					.findViewById(R.id.raiv_picture);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tv_name.setText(list.get(position).getCompany());
		viewHolder.tv_count.setText(list.get(position).getCount()+"");
		viewHolder.tv_money.setText(list.get(position).getMoney()+"");
		if (list.get(position).getLogo() != null
				&& !list.get(position).getLogo().equals("")) {
			// 设置图片
			Glide.with(activity).load(list.get(position).getLogo())
					.asBitmap().override(200, 200)
					.placeholder(R.drawable.imgloading)
					.into(viewHolder.raiv_picture);
		} else {
			Glide.with(activity).load("").asBitmap()
					.placeholder(R.drawable.imgloading).override(200, 200)
					.into(viewHolder.raiv_picture);
		}
		return convertView;
	}

	class ViewHolder {
		private TextView tv_name, tv_count, tv_money;
		private RoundAngleImageView raiv_picture;
	}

}
