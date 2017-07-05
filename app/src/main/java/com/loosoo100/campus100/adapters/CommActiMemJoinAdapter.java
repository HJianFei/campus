package com.loosoo100.campus100.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.beans.UserInfo;
import com.loosoo100.campus100.view.CircleView;

/**
 * 
 * @author yang 社团活动报名人员适配器
 */
public class CommActiMemJoinAdapter extends BaseAdapter {

	private List<UserInfo> list;
	private LayoutInflater inflater;
	private Activity activity;

	public CommActiMemJoinAdapter(Context context, List<UserInfo> list) {
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
			convertView = inflater.inflate(R.layout.item_comm_acti_mem_join,
					null);
			viewHolder.tv_name = (TextView) convertView
					.findViewById(R.id.tv_name);
			viewHolder.tv_trueName = (TextView) convertView
					.findViewById(R.id.tv_trueName);
			viewHolder.tv_phone = (TextView) convertView
					.findViewById(R.id.tv_phone);
			viewHolder.iv_sex = (ImageView) convertView
					.findViewById(R.id.iv_sex);
			viewHolder.iv_boss = (ImageView) convertView
					.findViewById(R.id.iv_boss);
			viewHolder.cv_picture = (CircleView) convertView
					.findViewById(R.id.cv_picture);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tv_name.setText(list.get(position).getNickName());
		viewHolder.tv_trueName
				.setText("(" + list.get(position).getName() + ")");
		if (list.get(position).getName().equals("")) {
			viewHolder.tv_trueName.setVisibility(View.GONE);
		} else {
			viewHolder.tv_trueName.setVisibility(View.VISIBLE);
		}
		if (list.get(position).getSex().equals("1")) {
			viewHolder.iv_sex.setImageResource(R.drawable.icon_female_picture);
		} else {
			viewHolder.iv_sex.setImageResource(R.drawable.icon_male_picture);
		}

		if (list.get(position).getOrg() == 1) {
			viewHolder.iv_boss.setVisibility(View.VISIBLE);
		} else {
			viewHolder.iv_boss.setVisibility(View.GONE);
		}

		viewHolder.tv_phone.setText(list.get(position).getPhone());

		if (!list.get(position).getHeadShot().equals("")
				&& !list.get(position).getHeadShot().equals("null")) {
			Glide.with(activity).load(list.get(position).getHeadShot())
					.override(150, 150).into(viewHolder.cv_picture);
		} else {
			Glide.with(activity).load(R.drawable.imgloading).asBitmap()
					.into(viewHolder.cv_picture);
		}

		return convertView;
	}

	class ViewHolder {
		private TextView tv_name, tv_trueName, tv_phone;
		private CircleView cv_picture;
		private ImageView iv_sex, iv_boss;
	}

}
