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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.beans.CommunityMemberInfo;
import com.loosoo100.campus100.view.CircleView;

/**
 * 
 * @author yang 社团详情girdview适配器
 */
public class CommunityDetialMemberAdapter extends BaseAdapter {

	private List<CommunityMemberInfo> list;
	private LayoutInflater inflater;
	private Activity activity;

	private boolean showTrueName = false;

	public CommunityDetialMemberAdapter(Context context,
			List<CommunityMemberInfo> list, boolean showTrueName) {
		this.list = list;
		inflater = LayoutInflater.from(context);
		activity = (Activity) context;
		this.showTrueName = showTrueName;
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
			convertView = inflater.inflate(
					R.layout.item_community_detail_member, null);
			viewHolder.cv_headShot = (CircleView) convertView
					.findViewById(R.id.cv_headShot);
			viewHolder.iv_sex = (ImageView) convertView
					.findViewById(R.id.iv_sex);
			viewHolder.tv_nickName = (TextView) convertView
					.findViewById(R.id.tv_nickName);
			viewHolder.tv_name = (TextView) convertView
					.findViewById(R.id.tv_name);
//			viewHolder.ll_qq = (LinearLayout) convertView
//					.findViewById(R.id.ll_qq);
//			viewHolder.tv_qq = (TextView) convertView.findViewById(R.id.tv_qq);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (showTrueName) {
//			viewHolder.ll_qq.setVisibility(View.VISIBLE);
			viewHolder.tv_name.setVisibility(View.VISIBLE);
//			viewHolder.tv_qq.setVisibility(View.VISIBLE);
			viewHolder.tv_name
					.setText("(" + list.get(position).getName() + ")");
//			viewHolder.tv_qq.setText(list.get(position).getQq());
		} else {
//			viewHolder.ll_qq.setVisibility(View.GONE);
			viewHolder.tv_name.setVisibility(View.GONE);
//			viewHolder.tv_qq.setVisibility(View.GONE);
		}
		viewHolder.tv_nickName.setText(list.get(position).getNickName());
		if (list.get(position).getHeadShotString() != null
				&& !list.get(position).getHeadShotString().equals("null")
				&& !list.get(position).getHeadShotString().equals("")) {
			Glide.with(activity).load(list.get(position).getHeadShotString())
					.override(120, 120).into(viewHolder.cv_headShot);
		} else {
			Glide.with(activity).load("").placeholder(R.drawable.imgloading)
					.override(120, 120).into(viewHolder.cv_headShot);
		}
		if (list.get(position).getSex() == 0) {
			viewHolder.iv_sex.setImageDrawable(activity.getResources()
					.getDrawable(R.drawable.icon_male_picture));
		} else {
			viewHolder.iv_sex.setImageDrawable(activity.getResources()
					.getDrawable(R.drawable.icon_female_picture));
		}

		return convertView;
	}

	class ViewHolder {
		private CircleView cv_headShot;
		private TextView tv_nickName, tv_name;
		private ImageView iv_sex;
//		private LinearLayout ll_qq;
	}

}
