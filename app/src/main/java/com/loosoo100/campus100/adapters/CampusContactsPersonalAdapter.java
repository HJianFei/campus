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
import com.loosoo100.campus100.beans.CampusContactsInfo;

/**
 * 
 * @author yang 校园圈个人发表中心适配器
 */
public class CampusContactsPersonalAdapter extends BaseAdapter {

	private List<CampusContactsInfo> list;
	private Activity activity;
	private LayoutInflater inflater;
	private ViewHolder viewHolder = null;

	public CampusContactsPersonalAdapter(Context context,
			List<CampusContactsInfo> list) {
		this.activity = (Activity) context;
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
			convertView = inflater.inflate(
					R.layout.item_campus_contacts_personal, null);
			// 标题
			viewHolder.tv_title = (TextView) convertView
					.findViewById(R.id.tv_title);
			// 共几张
			viewHolder.tv_pictureCount = (TextView) convertView
					.findViewById(R.id.tv_pictureCount);
			// 发表日期
			viewHolder.tv_day = (TextView) convertView
					.findViewById(R.id.tv_day);
			// 发表月份
			viewHolder.tv_month = (TextView) convertView
					.findViewById(R.id.tv_month);
			// 图片
			viewHolder.imageview1 = (ImageView) convertView
					.findViewById(R.id.imageview1);
			viewHolder.imageview2 = (ImageView) convertView
					.findViewById(R.id.imageview2);
			viewHolder.imageview3 = (ImageView) convertView
					.findViewById(R.id.imageview3);
			viewHolder.imageview4 = (ImageView) convertView
					.findViewById(R.id.imageview4);
			viewHolder.ll_picture = (LinearLayout) convertView
					.findViewById(R.id.ll_picture);
			viewHolder.ll_picture02 = (LinearLayout) convertView
					.findViewById(R.id.ll_picture02);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (list.get(position).getPicListThum().size() == 0) {
			viewHolder.ll_picture.setVisibility(View.GONE);
			viewHolder.tv_title.setBackgroundColor(activity.getResources()
					.getColor(R.color.gray_efefee));
		} else {
			viewHolder.tv_title.setBackgroundColor(activity.getResources()
					.getColor(R.color.none_color));
			viewHolder.ll_picture.setVisibility(View.VISIBLE);
			if (list.get(position).getPicListThum().size() == 1) {
				hideAllPicture();
				viewHolder.ll_picture02.setVisibility(View.GONE);
				viewHolder.imageview1.setVisibility(View.VISIBLE);
				Glide.with(activity)
						.load(list.get(position).getPicListThum().get(0))
						.dontAnimate().placeholder(R.drawable.imgloading)
						.override(320, 320).into(viewHolder.imageview1);
			} else if (list.get(position).getPicListThum().size() == 2) {
				hideAllPicture();
				viewHolder.ll_picture02.setVisibility(View.VISIBLE);
				viewHolder.imageview1.setVisibility(View.VISIBLE);
				Glide.with(activity)
						.load(list.get(position).getPicListThum().get(0))
						.dontAnimate().placeholder(R.drawable.imgloading)
						.override(160, 320).into(viewHolder.imageview1);
				viewHolder.imageview2.setVisibility(View.VISIBLE);
				Glide.with(activity)
						.load(list.get(position).getPicListThum().get(1))
						.dontAnimate().placeholder(R.drawable.imgloading)
						.override(160, 320).into(viewHolder.imageview2);
			} else if (list.get(position).getPicListThum().size() == 3) {
				hideAllPicture();
				viewHolder.ll_picture02.setVisibility(View.VISIBLE);
				viewHolder.imageview1.setVisibility(View.VISIBLE);
				Glide.with(activity)
						.load(list.get(position).getPicListThum().get(0))
						.dontAnimate().placeholder(R.drawable.imgloading)
						.override(160, 320).into(viewHolder.imageview1);
				viewHolder.imageview2.setVisibility(View.VISIBLE);
				Glide.with(activity)
						.load(list.get(position).getPicListThum().get(1))
						.dontAnimate().placeholder(R.drawable.imgloading)
						.override(160, 160).into(viewHolder.imageview2);
				viewHolder.imageview3.setVisibility(View.VISIBLE);
				Glide.with(activity)
						.load(list.get(position).getPicListThum().get(2))
						.dontAnimate().placeholder(R.drawable.imgloading)
						.override(160, 160).into(viewHolder.imageview3);
			} else if (list.get(position).getPicListThum().size() > 3) {
				hideAllPicture();
				viewHolder.ll_picture02.setVisibility(View.VISIBLE);
				viewHolder.imageview1.setVisibility(View.VISIBLE);
				Glide.with(activity)
						.load(list.get(position).getPicListThum().get(0))
						.dontAnimate().placeholder(R.drawable.imgloading)
						.override(160, 160).into(viewHolder.imageview1);
				viewHolder.imageview2.setVisibility(View.VISIBLE);
				Glide.with(activity)
						.load(list.get(position).getPicListThum().get(1))
						.dontAnimate().placeholder(R.drawable.imgloading)
						.override(160, 160).into(viewHolder.imageview2);
				viewHolder.imageview3.setVisibility(View.VISIBLE);
				Glide.with(activity)
						.load(list.get(position).getPicListThum().get(2))
						.dontAnimate().placeholder(R.drawable.imgloading)
						.override(160, 160).into(viewHolder.imageview3);
				viewHolder.imageview4.setVisibility(View.VISIBLE);
				Glide.with(activity)
						.load(list.get(position).getPicListThum().get(3))
						.dontAnimate().placeholder(R.drawable.imgloading)
						.override(160, 160).into(viewHolder.imageview4);
			}
		}

		if (position > 0) {
			if (list.get(position).getMonth()
					.equals(list.get(position - 1).getMonth())
					&& list.get(position).getDay()
							.equals(list.get(position - 1).getDay())) {
				viewHolder.tv_month.setVisibility(View.GONE);
				viewHolder.tv_day.setVisibility(View.GONE);
			} else {
				viewHolder.tv_month.setVisibility(View.VISIBLE);
				viewHolder.tv_day.setVisibility(View.VISIBLE);
			}
		} else {
			viewHolder.tv_month.setVisibility(View.VISIBLE);
			viewHolder.tv_day.setVisibility(View.VISIBLE);
		}
		viewHolder.tv_month.setText("" + list.get(position).getMonth() + "月");
		viewHolder.tv_day.setText("" + list.get(position).getDay());
		viewHolder.tv_title.setText("" + list.get(position).getContent());
		if (list.get(position).getPicListThum().size() > 1) {
			viewHolder.tv_pictureCount.setVisibility(View.VISIBLE);
			viewHolder.tv_pictureCount.setText("共"
					+ list.get(position).getPicListThum().size() + "张");
		} else {
			viewHolder.tv_pictureCount.setVisibility(View.GONE);
		}

		return convertView;
	}

	private class ViewHolder {
		private TextView tv_title, tv_pictureCount, tv_day, tv_month;
		private ImageView imageview1, imageview2, imageview3, imageview4;
		private LinearLayout ll_picture, ll_picture02;
	}

	private void hideAllPicture() {
		viewHolder.imageview1.setVisibility(View.GONE);
		viewHolder.imageview2.setVisibility(View.GONE);
		viewHolder.imageview3.setVisibility(View.GONE);
		viewHolder.imageview4.setVisibility(View.GONE);
	}
}
