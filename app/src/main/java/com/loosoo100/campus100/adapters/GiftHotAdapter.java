package com.loosoo100.campus100.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loosoo100.campus100.R;
import com.loosoo100.campus100.beans.GoodsInfo;

/**
 * 
 * @author yang 热门推荐适配器
 */
public class GiftHotAdapter extends BaseAdapter {

	public static List<GoodsInfo> list;
	private Context context;
	private LayoutInflater inflater;

	public GiftHotAdapter(Context context, List<GoodsInfo> list) {
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
			convertView = inflater.inflate(R.layout.item_gift_hot, null);

			viewHolder.iv_collect = (ImageView) convertView
					.findViewById(R.id.iv_collect);
			viewHolder.tv_collect = (TextView) convertView
					.findViewById(R.id.tv_collect);
			viewHolder.tv_recommend = (TextView) convertView
					.findViewById(R.id.tv_recommend);
			viewHolder.iv_recommendIcon = (ImageView) convertView
					.findViewById(R.id.iv_recommendIcon);

			viewHolder.ll_collect = (LinearLayout) convertView
					.findViewById(R.id.ll_collect);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tv_collect.setText("" + list.get(position).getCollection());
		viewHolder.tv_recommend.setText("" + list.get(position).getGoodsName());
		viewHolder.iv_recommendIcon.setImageBitmap(list.get(position)
				.getGoodsIcon());

		if (list.get(position).isCollected()) {
			viewHolder.iv_collect.setBackground(context.getResources()
					.getDrawable(R.drawable.icon_collect_red));
		} else {
			viewHolder.iv_collect.setBackground(context.getResources()
					.getDrawable(R.drawable.icon_collect));
		}
		viewHolder.ll_collect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (list.get(position).isCollected()) {
					list.get(position).setCollection(
							list.get(position).getCollection() - 1);
					list.get(position).setCollected(false);
				} else {
					list.get(position).setCollection(
							list.get(position).getCollection() + 1);
					list.get(position).setCollected(true);
				}
				GiftHotAdapter.this.notifyDataSetChanged();
			}
		});

		return convertView;
	}

	class ViewHolder {
		public TextView tv_collect, tv_recommend;
		public ImageView iv_recommendIcon;
		public LinearLayout ll_collect;
		public ImageView iv_collect;
	}

}
