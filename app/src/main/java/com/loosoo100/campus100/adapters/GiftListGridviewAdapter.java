package com.loosoo100.campus100.adapters;

import java.util.List;

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
import com.loosoo100.campus100.activities.GiftListDetailActivity;
import com.loosoo100.campus100.beans.GiftGoodsInfo;

/**
 * 
 * @author yang 商品分类列表详情girdview适配器
 */
public class GiftListGridviewAdapter extends BaseAdapter {

	private List<GiftGoodsInfo> list;
	private LayoutInflater inflater;

	private GiftListDetailActivity activity;

	public GiftListGridviewAdapter(Context context, List<GiftGoodsInfo> list) {
		this.list = list;
		inflater = LayoutInflater.from(context);
		activity = (GiftListDetailActivity) context;
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
			convertView = inflater.inflate(R.layout.item_gift_list_gridview,
					null);
			viewHolder.root_gridview = (LinearLayout) convertView
					.findViewById(R.id.root_gridview);
			viewHolder.ll_collect = (LinearLayout) convertView
					.findViewById(R.id.ll_collect);
			viewHolder.iv_goodsIcon = (ImageView) convertView
					.findViewById(R.id.iv_goodsIcon);
			viewHolder.iv_collect = (ImageView) convertView
					.findViewById(R.id.iv_collect);
			viewHolder.tv_goodsName = (TextView) convertView
					.findViewById(R.id.tv_goodsName);
			viewHolder.tv_goodsPrice = (TextView) convertView
					.findViewById(R.id.tv_goodsPrice);
			viewHolder.tv_collect = (TextView) convertView
					.findViewById(R.id.tv_collect);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tv_goodsName.setText(list.get(position).getGiftgoodsName()
				+ "");
		viewHolder.tv_goodsPrice.setText("￥"
				+ list.get(position).getShopPrice() + "");
		 viewHolder.tv_collect.setText(list.get(position).getCollect()+"");
		Glide.with(activity).load(list.get(position).getGiftgoodsImg()).dontAnimate().placeholder(R.drawable.imgloading) // 设置占位图
				.override(400, 400).into(viewHolder.iv_goodsIcon);

		// if (list.get(position).isCollected()) {
		// viewHolder.iv_collect.setBackground(activity.getResources()
		// .getDrawable(R.drawable.icon_collect_red));
		// } else {
		// viewHolder.iv_collect.setBackground(activity.getResources()
		// .getDrawable(R.drawable.icon_collect));
		// }

		// 点击整个商品列表项
		// viewHolder.root_gridview.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// Intent intent = new Intent(activity,
		// GiftGoodsDetailActivity.class);
		// intent.putExtra("position", position);
		// activity.startActivity(intent);
		// }
		// });

		// // 点击收藏按钮
		// viewHolder.ll_collect.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// if (list.get(position).isCollected()) {
		// list.get(position).setCollection(
		// list.get(position).getCollection() - 1);
		// list.get(position).setCollected(false);
		// } else {
		// list.get(position).setCollection(
		// list.get(position).getCollection() + 1);
		// list.get(position).setCollected(true);
		// }
		// GiftListGridviewAdapter.this.notifyDataSetChanged();
		// }
		// });
		return convertView;
	}

	class ViewHolder {
		private ImageView iv_goodsIcon, iv_collect;
		private TextView tv_goodsName, tv_goodsPrice, tv_collect;
		private LinearLayout ll_collect, root_gridview;
	}

}
