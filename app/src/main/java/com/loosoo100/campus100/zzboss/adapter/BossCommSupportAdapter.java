package com.loosoo100.campus100.zzboss.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.view.RoundAngleImageView;
import com.loosoo100.campus100.zzboss.beans.BossCommSupportInfo;

/**
 * 
 * @author yang 赞助社团适配器
 */
public class BossCommSupportAdapter extends BaseAdapter {

	private List<BossCommSupportInfo> list;
	private LayoutInflater inflater;
	private Activity activity;

	public BossCommSupportAdapter(Context context,
			List<BossCommSupportInfo> list) {
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
			convertView = inflater.inflate(R.layout.item_boss_comm_support,
					null);
			viewHolder.tv_name = (TextView) convertView
					.findViewById(R.id.tv_name);
			viewHolder.tv_offer = (TextView) convertView
					.findViewById(R.id.tv_offer);
			viewHolder.tv_price = (TextView) convertView
					.findViewById(R.id.tv_price);
			viewHolder.tv_count = (TextView) convertView
					.findViewById(R.id.tv_count);
			viewHolder.tv_location = (TextView) convertView
					.findViewById(R.id.tv_location);
			viewHolder.tv_time = (TextView) convertView
					.findViewById(R.id.tv_time);
			viewHolder.tv_unit = (TextView) convertView
					.findViewById(R.id.tv_unit);
			viewHolder.tv_remainder = (TextView) convertView
					.findViewById(R.id.tv_remainder);
			viewHolder.raiv_picture = (RoundAngleImageView) convertView
					.findViewById(R.id.raiv_picture);
			viewHolder.iv_finish = (ImageView) convertView
					.findViewById(R.id.iv_finish);
			viewHolder.ll_root = (LinearLayout) convertView
					.findViewById(R.id.ll_root);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tv_name.setText(list.get(position).getDemandName());
		viewHolder.tv_offer.setText(list.get(position).getOffer());
		viewHolder.tv_count
				.setText((int) (list.get(position).getNeedMoney() - list.get(
						position).getRaiseMoney())
						+ "");
		viewHolder.tv_location.setText(list.get(position).getSchool());
		viewHolder.tv_time.setText(list.get(position).getOverTime());

		if (list.get(position).getStatus() == 0) {
			viewHolder.iv_finish.setVisibility(View.GONE);
			viewHolder.ll_root.setBackgroundDrawable(activity.getResources()
					.getDrawable(R.drawable.selector_ll));
		} else {
			viewHolder.iv_finish.setVisibility(View.VISIBLE);
			viewHolder.ll_root.setBackgroundColor(activity.getResources()
					.getColor(R.color.gray_f8f8f8));
		}

		if (list.get(position).getClassify() == 0) {
			viewHolder.tv_unit.setVisibility(View.GONE);
			viewHolder.tv_remainder.setVisibility(View.GONE);
			viewHolder.tv_count.setVisibility(View.GONE);
			viewHolder.tv_price
					.setText("￥" + list.get(position).getNeedMoney());
		} else {
			viewHolder.tv_unit.setVisibility(View.VISIBLE);
			viewHolder.tv_remainder.setVisibility(View.VISIBLE);
			viewHolder.tv_count.setVisibility(View.VISIBLE);
			viewHolder.tv_price
					.setText("￥" + list.get(position).getFree() + "");
		}

		if (list.get(position).getHeadthumb() != null
				&& !list.get(position).getHeadthumb().equals("")) {
			// 设置图片
			Glide.with(activity).load(list.get(position).getHeadthumb())
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
		private TextView tv_name, tv_offer, tv_price, tv_count, tv_location,
				tv_time;
		private TextView tv_unit, tv_remainder;
		private RoundAngleImageView raiv_picture;
		private ImageView iv_finish;
		private LinearLayout ll_root;
	}

}
