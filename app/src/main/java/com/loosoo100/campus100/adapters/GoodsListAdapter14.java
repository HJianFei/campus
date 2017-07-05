package com.loosoo100.campus100.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.activities.HomeActivity;
import com.loosoo100.campus100.beans.GoodsInfo;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.fragments.StoreFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author yang 商品列表适配器
 */
public class GoodsListAdapter14 extends BaseAdapter {

	public static List<GoodsInfo> list;
	private LayoutInflater inflater;
	private ViewHolder viewHolder = null;
	private HomeActivity activity;

	public GoodsListAdapter14(Context context, List<GoodsInfo> list) {
		this.list = list;

		activity = (HomeActivity) context;

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
			convertView = inflater.inflate(R.layout.item_goods_list, null);
			// 商品名
			viewHolder.tv_goodsName = (TextView) convertView
					.findViewById(R.id.tv_goodsNameOrder);
			// 现价
			viewHolder.tv_currentPrice = (TextView) convertView
					.findViewById(R.id.tv_currentPrice);
			// 原价
			viewHolder.tv_originalPrice = (TextView) convertView
					.findViewById(R.id.tv_originalPrice);
			// 商品图标
			viewHolder.iv_goods = (ImageView) convertView
					.findViewById(R.id.iv_goods);
			// 添加商品数量
			viewHolder.tv_goodsCount = (TextView) convertView
					.findViewById(R.id.tv_goodsCountOrder);
			// 添加按钮
			viewHolder.rl_add = (RelativeLayout) convertView
					.findViewById(R.id.rl_add);
			// 减少按钮
			viewHolder.rl_reduce = (RelativeLayout) convertView
					.findViewById(R.id.rl_reduce);
			// 减少按钮的布局
			viewHolder.rl_reduce_root = (RelativeLayout) convertView
					.findViewById(R.id.rl_reduce_root);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tv_goodsName.setText(list.get(position).getGoodsName());
		viewHolder.tv_currentPrice.setText("￥ "
				+ list.get(position).getCurrentPrice());
		viewHolder.tv_originalPrice.setText("￥ "
				+ list.get(position).getOriginalPrice() + "/"
				+ list.get(position).getUnit());
		// 设置删除线
		viewHolder.tv_originalPrice.getPaint().setFlags(
				Paint.STRIKE_THRU_TEXT_FLAG);
		// 加载网络图片
		Glide.with(activity).load(list.get(position).getGoodsIconUrl())
				.override(180, 180).placeholder(R.drawable.imgloading)
				.into(viewHolder.iv_goods);
		viewHolder.tv_goodsCount.setText(list.get(position).getCount() + "");
		if (list.get(position).isAdding()) {
			viewHolder.rl_reduce_root.setVisibility(View.VISIBLE);
		} else {
			viewHolder.rl_reduce_root.setVisibility(View.GONE);
		}

		// 单独添加按钮监听
		viewHolder.rl_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (list.get(position).getStock() > list.get(position)
						.getCount()) {
					// 设置商品是否是添加状态
					list.get(position).setAdding(true);
					list.get(position).setCount(
							list.get(position).getCount() + 1);
					// 更新适配器
					GoodsListAdapter14.this.notifyDataSetChanged();
					StoreFragment.updateView(list.get(position)
							.getCurrentPrice());

					updateCartList(position, true);
				} else {
					ToastUtil.showToast(activity,"已达最大库存,不能继续添加");
				}
			}
		});

		// 减少按钮监听
		viewHolder.rl_reduce.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				list.get(position).setCount(list.get(position).getCount() - 1);
				GoodsListAdapter14.this.notifyDataSetChanged();
				if (list.get(position).getCount() == 0) {
					list.get(position).setAdding(false);
				}
				StoreFragment.updateView(-list.get(position).getCurrentPrice());

				updateCartList(position, false);
			}
		});

		return convertView;
	}

	private class ViewHolder {
		private ImageView iv_goods;
		private TextView tv_goodsName, tv_currentPrice, tv_originalPrice,
				tv_goodsCount;
		private RelativeLayout rl_reduce_root;
		private RelativeLayout rl_reduce, rl_add;
	}

	/**
	 * 更新购物车商品列表
	 */
	public static void updateCartList(int position, boolean add) {
		if (add) {
			for (int i = 0; i < StoreFragment.cartList.size(); i++) {
				if (list.get(position).getGoodsID()
						.equals(StoreFragment.cartList.get(i).get("goodsID"))) {
					StoreFragment.cartList.get(i).put("name",
							list.get(position).getGoodsName());
					StoreFragment.cartList.get(i).put("goodsID",
							list.get(position).getGoodsID());
					StoreFragment.cartList.get(i).put("catID",
							list.get(position).getCatID());
					StoreFragment.cartList.get(i).put("count",
							list.get(position).getCount());
					StoreFragment.cartList.get(i).put("price",
							list.get(position).getCurrentPrice());
					StoreFragment.cartList.get(i).put("stock",
							list.get(position).getStock());
					StoreFragment.cartList.get(i).put("goodsThums",
							list.get(position).getGoodsIconUrl());
					return;
				}
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", list.get(position).getGoodsName());
			map.put("goodsID", list.get(position).getGoodsID());
			map.put("catID", list.get(position).getCatID());
			map.put("position", position);
			map.put("count", list.get(position).getCount());
			map.put("price", list.get(position).getCurrentPrice());
			map.put("stock", list.get(position).getStock());
			map.put("goodsThums", list.get(position).getGoodsIconUrl());
			StoreFragment.cartList.add(map);
		} else {
			for (int i = 0; i < StoreFragment.cartList.size(); i++) {
				if (list.get(position).getGoodsID()
						.equals(StoreFragment.cartList.get(i).get("goodsID"))) {
					if (StoreFragment.cartList.get(i).get("count").toString()
							.equals("1")) {
						StoreFragment.cartList.remove(i);
					} else {
						StoreFragment.cartList.get(i).put("name",
								list.get(position).getGoodsName());
						StoreFragment.cartList.get(i).put("count",
								list.get(position).getCount());
						StoreFragment.cartList.get(i).put("goodsID",
								list.get(position).getGoodsID());
						StoreFragment.cartList.get(i).put("catID",
								list.get(position).getCatID());
						StoreFragment.cartList.get(i).put("price",
								list.get(position).getCurrentPrice());
						StoreFragment.cartList.get(i).put("stock",
								list.get(position).getStock());
						StoreFragment.cartList.get(i).put("goodsThums",
								list.get(position).getGoodsIconUrl());
					}
				}
			}
		}
		// 如果购物车数量为0则隐藏购物车
		if (StoreFragment.cartList.size() == 0) {
			StoreFragment.showCartView(-1);
		}
		// 如果购物车数量为1则显示购物车
		if (StoreFragment.cartList.size() == 1
				&& StoreFragment.rl_cart_root_pay.getVisibility() == View.GONE) {
			StoreFragment.showCartView(1);
		}

	}

	public static void updateAllView(int position, int count) {
		list.get(position).setCount(list.get(position).getCount() + count);
		if (list.get(position).getCount() == 0) {
			list.get(position).setAdding(false);
		}
	}

}
