package com.loosoo100.campus100.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
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
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.RoundAngleImageView;

/**
 * 
 * @author yang 社团活动列表适配器
 */
public class CommActiListHomeAdapter extends BaseAdapter {

	private List<CommunityActivityInfo> list;
	private LayoutInflater inflater;
	private Activity activity;
	private ViewHolder viewHolder = null;

	Handler handler = new Handler();

	public CommActiListHomeAdapter(Context context,
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

		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_comm_acti_list_home,
					null);
			viewHolder.ll_root = (LinearLayout) convertView
					.findViewById(R.id.ll_root);
			viewHolder.iv_line = (ImageView) convertView
					.findViewById(R.id.iv_line);
			viewHolder.ll_type_money = (LinearLayout) convertView
					.findViewById(R.id.ll_type_money);
			viewHolder.ll_type_together = (LinearLayout) convertView
					.findViewById(R.id.ll_type_together);
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
			viewHolder.tv_location = (TextView) convertView
					.findViewById(R.id.tv_location);
			viewHolder.tv_time = (TextView) convertView
					.findViewById(R.id.tv_time);
			viewHolder.tv_count = (TextView) convertView
					.findViewById(R.id.tv_count);
			viewHolder.tv_money = (TextView) convertView
					.findViewById(R.id.tv_money);
			viewHolder.progressBar = (ProgressBar) convertView
					.findViewById(R.id.progressBar);
			viewHolder.raiv_picture = (RoundAngleImageView) convertView
					.findViewById(R.id.raiv_picture);
			viewHolder.iv_finish = (ImageView) convertView
					.findViewById(R.id.iv_finish);
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

		if (list.get(position).getClassify() == 0) {
			viewHolder.ll_type_together.setVisibility(View.VISIBLE);
			viewHolder.ll_type_money.setVisibility(View.GONE);

			viewHolder.tv_needMoney.setText("￥"
					+ list.get(position).getNeedMoney());
			viewHolder.tv_raisedMoney.setText("￥"
					+ list.get(position).getRaisedMoney());
			viewHolder.tv_supportCount.setText(list.get(position)
					.getSupportCount() + "");
			viewHolder.tv_percent
					.setText((int) (list.get(position).getRaisedMoney()
							/ list.get(position).getNeedMoney() * 100)
							+ "%");
			viewHolder.progressBar
					.setProgress((int) (list.get(position).getRaisedMoney()
							/ list.get(position).getNeedMoney() * 100));

		} else {
			viewHolder.ll_type_together.setVisibility(View.GONE);
			viewHolder.ll_type_money.setVisibility(View.VISIBLE);

			viewHolder.tv_location.setText(list.get(position).getCity() + " "
					+ list.get(position).getSchool());
			viewHolder.tv_time.setText(MyUtils.getSqlDate(Long.valueOf(list
					.get(position).getDate())));
			viewHolder.tv_count.setText(list.get(position).getSupportCount()
					+ "");
			if (Float.valueOf(list.get(position).getFree()) == 0) {
				viewHolder.tv_money.setText("免费");
			} else {
				viewHolder.tv_money.setText("￥" + list.get(position).getFree()
						+ "起");
			}
		}

		viewHolder.tv_name.setText(list.get(position).getActivityName());
		if (list.get(position).getActiHeadShot() != null
				&& !list.get(position).getActiHeadShot().equals("")) {
			// 设置图片
			Glide.with(activity).load(list.get(position).getActiHeadShot())
					.asBitmap().override(200, 200)
					.placeholder(R.drawable.imgloading)
					.into(viewHolder.raiv_picture);
		} else {
			Glide.with(activity).load("").asBitmap()
					.placeholder(R.drawable.imgloading).override(200, 200)
					.into(viewHolder.raiv_picture);
		}
		if (position == list.size() - 1) {
			viewHolder.iv_line.setVisibility(View.GONE);
		} else {
			viewHolder.iv_line.setVisibility(View.VISIBLE);
		}

		return convertView;
	}

	class ViewHolder {
		private TextView tv_name, tv_needMoney, tv_raisedMoney,
				tv_supportCount, tv_percent;
		private ProgressBar progressBar;
		private RoundAngleImageView raiv_picture;
		private ImageView iv_finish;
		private ImageView iv_line;
		private LinearLayout ll_root, ll_type_money, ll_type_together;

		private TextView tv_location, tv_time, tv_count, tv_money;
	}

}
