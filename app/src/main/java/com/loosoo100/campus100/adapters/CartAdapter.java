package com.loosoo100.campus100.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.loosoo100.campus100.R;
import com.loosoo100.campus100.activities.HomeActivity;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.fragments.StoreFragment;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author yang 购物车适配器
 */
public class CartAdapter extends BaseAdapter {

	private List<Map<String, Object>> list;
	private LayoutInflater inflater;
	private HomeActivity activity;

	public CartAdapter(Context context, List<Map<String, Object>> list) {
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
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_cart, null);
			viewHolder.goodsName = (TextView) convertView
					.findViewById(R.id.tv_goodsName_cart);
			viewHolder.goodsPrice = (TextView) convertView
					.findViewById(R.id.tv_goodsPrice_cart);
			viewHolder.goodsCount = (TextView) convertView
					.findViewById(R.id.tv_goodsCount_cart);
			viewHolder.ib_add = (ImageButton) convertView
					.findViewById(R.id.ib_add_cart);
			viewHolder.ib_reduce = (ImageButton) convertView
					.findViewById(R.id.ib_reduce_cart);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.goodsName.setText("" + list.get(position).get("name"));
		viewHolder.goodsPrice.setText("￥" + list.get(position).get("price"));
		viewHolder.goodsCount.setText("" + list.get(position).get("count"));

		viewHolder.ib_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 如果购物车的商品所属类目跟listview的类目一样，则更新该listview的数据
				for (int i = 0; i < StoreFragment.goodsCategoryData.size(); i++) {
					if (list.get(position)
							.get("catID")
							.equals(StoreFragment.goodsCategoryData.get(i)
									.getCatID())) {
						updateAdapterAdd(position, i);
						break;
					}
				}
			}
		});

		viewHolder.ib_reduce.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 如果购物车的商品所属类目跟listview的类目一样，则更新该listview的数据
				for (int i = 0; i < StoreFragment.goodsCategoryData.size(); i++) {
					if (list.get(position)
							.get("catID")
							.equals(StoreFragment.goodsCategoryData.get(i)
									.getCatID())) {
						updateAdapterReduce(position, i);
						break;
					}
				}
			}
		});
		return convertView;
	}

	class ViewHolder {
		public TextView goodsName, goodsPrice, goodsCount;
		public ImageButton ib_add, ib_reduce;
	}

	/**
	 * 购物车减少按钮更新列表内容
	 * 
	 * @param position
	 *            点击购物车列表的位置
	 * @param index
	 *            点击的商品属于哪个listview
	 */
	private void updateAdapterReduce(int position, int index) {
		switch (index) {
		case 0:
			GoodsListAdapter01.updateCartList(
					Integer.valueOf(list.get(position).get("position")
							.toString()), true);
			// 更新购物车价格
			StoreFragment.updateView(-Float.valueOf(list.get(position)
					.get("price").toString()));
			// 更新商品列表添加的数量
			GoodsListAdapter01.updateAllView(
					Integer.valueOf(list.get(position).get("position")
							.toString()), -1);
			StoreFragment.goodsAdapter01.notifyDataSetChanged();
			break;
		case 1:
			GoodsListAdapter02.updateCartList(
					Integer.valueOf(list.get(position).get("position")
							.toString()), true);
			// 更新购物车价格
			StoreFragment.updateView(-Float.valueOf(list.get(position)
					.get("price").toString()));
			// 更新商品列表添加的数量
			GoodsListAdapter02.updateAllView(
					Integer.valueOf(list.get(position).get("position")
							.toString()), -1);
			StoreFragment.goodsAdapter02.notifyDataSetChanged();
			break;
		case 2:
			GoodsListAdapter03.updateCartList(
					Integer.valueOf(list.get(position).get("position")
							.toString()), true);
			// 更新购物车价格
			StoreFragment.updateView(-Float.valueOf(list.get(position)
					.get("price").toString()));
			// 更新商品列表添加的数量
			GoodsListAdapter03.updateAllView(
					Integer.valueOf(list.get(position).get("position")
							.toString()), -1);
			StoreFragment.goodsAdapter03.notifyDataSetChanged();
			break;
		case 3:
			GoodsListAdapter04.updateCartList(
					Integer.valueOf(list.get(position).get("position")
							.toString()), true);
			// 更新购物车价格
			StoreFragment.updateView(-Float.valueOf(list.get(position)
					.get("price").toString()));
			// 更新商品列表添加的数量
			GoodsListAdapter04.updateAllView(
					Integer.valueOf(list.get(position).get("position")
							.toString()), -1);
			StoreFragment.goodsAdapter04.notifyDataSetChanged();
			break;
		case 4:
			GoodsListAdapter05.updateCartList(
					Integer.valueOf(list.get(position).get("position")
							.toString()), true);
			// 更新购物车价格
			StoreFragment.updateView(-Float.valueOf(list.get(position)
					.get("price").toString()));
			// 更新商品列表添加的数量
			GoodsListAdapter05.updateAllView(
					Integer.valueOf(list.get(position).get("position")
							.toString()), -1);
			StoreFragment.goodsAdapter05.notifyDataSetChanged();
			break;
		case 5:
			GoodsListAdapter06.updateCartList(
					Integer.valueOf(list.get(position).get("position")
							.toString()), true);
			// 更新购物车价格
			StoreFragment.updateView(-Float.valueOf(list.get(position)
					.get("price").toString()));
			// 更新商品列表添加的数量
			GoodsListAdapter06.updateAllView(
					Integer.valueOf(list.get(position).get("position")
							.toString()), -1);
			StoreFragment.goodsAdapter06.notifyDataSetChanged();
			break;
		case 6:
			GoodsListAdapter07.updateCartList(
					Integer.valueOf(list.get(position).get("position")
							.toString()), true);
			// 更新购物车价格
			StoreFragment.updateView(-Float.valueOf(list.get(position)
					.get("price").toString()));
			// 更新商品列表添加的数量
			GoodsListAdapter07.updateAllView(
					Integer.valueOf(list.get(position).get("position")
							.toString()), -1);
			StoreFragment.goodsAdapter07.notifyDataSetChanged();
			break;
		case 7:
			GoodsListAdapter08.updateCartList(
					Integer.valueOf(list.get(position).get("position")
							.toString()), true);
			// 更新购物车价格
			StoreFragment.updateView(-Float.valueOf(list.get(position)
					.get("price").toString()));
			// 更新商品列表添加的数量
			GoodsListAdapter08.updateAllView(
					Integer.valueOf(list.get(position).get("position")
							.toString()), -1);
			StoreFragment.goodsAdapter08.notifyDataSetChanged();
			break;
		case 8:
			GoodsListAdapter09.updateCartList(
					Integer.valueOf(list.get(position).get("position")
							.toString()), true);
			// 更新购物车价格
			StoreFragment.updateView(-Float.valueOf(list.get(position)
					.get("price").toString()));
			// 更新商品列表添加的数量
			GoodsListAdapter09.updateAllView(
					Integer.valueOf(list.get(position).get("position")
							.toString()), -1);
			StoreFragment.goodsAdapter09.notifyDataSetChanged();
			break;
		case 9:
			GoodsListAdapter10.updateCartList(
					Integer.valueOf(list.get(position).get("position")
							.toString()), true);
			// 更新购物车价格
			StoreFragment.updateView(-Float.valueOf(list.get(position)
					.get("price").toString()));
			// 更新商品列表添加的数量
			GoodsListAdapter10.updateAllView(
					Integer.valueOf(list.get(position).get("position")
							.toString()), -1);
			StoreFragment.goodsAdapter10.notifyDataSetChanged();
			break;
		case 10:
			GoodsListAdapter11.updateCartList(
					Integer.valueOf(list.get(position).get("position")
							.toString()), true);
			// 更新购物车价格
			StoreFragment.updateView(-Float.valueOf(list.get(position)
					.get("price").toString()));
			// 更新商品列表添加的数量
			GoodsListAdapter11.updateAllView(
					Integer.valueOf(list.get(position).get("position")
							.toString()), -1);
			StoreFragment.goodsAdapter11.notifyDataSetChanged();
			break;
		case 11:
			GoodsListAdapter12.updateCartList(
					Integer.valueOf(list.get(position).get("position")
							.toString()), true);
			// 更新购物车价格
			StoreFragment.updateView(-Float.valueOf(list.get(position)
					.get("price").toString()));
			// 更新商品列表添加的数量
			GoodsListAdapter12.updateAllView(
					Integer.valueOf(list.get(position).get("position")
							.toString()), -1);
			StoreFragment.goodsAdapter12.notifyDataSetChanged();
			break;
		case 12:
			GoodsListAdapter13.updateCartList(
					Integer.valueOf(list.get(position).get("position")
							.toString()), true);
			// 更新购物车价格
			StoreFragment.updateView(-Float.valueOf(list.get(position)
					.get("price").toString()));
			// 更新商品列表添加的数量
			GoodsListAdapter13.updateAllView(
					Integer.valueOf(list.get(position).get("position")
							.toString()), -1);
			StoreFragment.goodsAdapter13.notifyDataSetChanged();
			break;
		case 13:
			GoodsListAdapter14.updateCartList(
					Integer.valueOf(list.get(position).get("position")
							.toString()), true);
			// 更新购物车价格
			StoreFragment.updateView(-Float.valueOf(list.get(position)
					.get("price").toString()));
			// 更新商品列表添加的数量
			GoodsListAdapter14.updateAllView(
					Integer.valueOf(list.get(position).get("position")
							.toString()), -1);
			StoreFragment.goodsAdapter14.notifyDataSetChanged();
			break;
		case 14:
			GoodsListAdapter15.updateCartList(
					Integer.valueOf(list.get(position).get("position")
							.toString()), true);
			// 更新购物车价格
			StoreFragment.updateView(-Float.valueOf(list.get(position)
					.get("price").toString()));
			// 更新商品列表添加的数量
			GoodsListAdapter15.updateAllView(
					Integer.valueOf(list.get(position).get("position")
							.toString()), -1);
			StoreFragment.goodsAdapter15.notifyDataSetChanged();
			break;

		}
		// 更新购物车列表的商品数量
		list.get(position)
				.put("count",
						Integer.valueOf(list.get(position).get("count")
								.toString()) - 1);
		if (Integer.valueOf(list.get(position).get("count").toString()) == 0) {
			list.remove(position);
		}
		if (list.size() == 0) {
			StoreFragment.rl_cart_page.setVisibility(View.GONE);
			// 隐藏购物车
			StoreFragment.showCartView(-1);
		}
		CartAdapter.this.notifyDataSetChanged();
	}

	/**
	 * 购物车添加按钮更新列表内容
	 * 
	 * @param position
	 *            点击购物车列表的位置
	 * @param index
	 *            点击的商品属于哪个listview
	 */
	private void updateAdapterAdd(int position, int index) {
		switch (index) {
		case 0:
			if (list.get(position).get("stock").toString()
					.equals(list.get(position).get("count").toString())) {
				ToastUtil.showToast(activity,"已达最大库存,不能继续添加");
				return;
			}
			GoodsListAdapter01.updateCartList(
					Integer.valueOf(list.get(position).get("position")
							.toString()), true);
			// 更新购物车价格
			StoreFragment.updateView(Float.valueOf(list.get(position)
					.get("price").toString()));
			// 更新商品列表添加的数量
			GoodsListAdapter01.updateAllView(
					Integer.valueOf(list.get(position).get("position")
							.toString()), 1);
			StoreFragment.goodsAdapter01.notifyDataSetChanged();
			// 更新购物车列表的商品数量
			list.get(position)
					.put("count",
							Integer.valueOf(list.get(position).get("count")
									.toString()) + 1);
			CartAdapter.this.notifyDataSetChanged();
			break;
		case 1:
			if (list.get(position).get("stock").toString()
					.equals(list.get(position).get("count").toString())) {
				ToastUtil.showToast(activity,"已达最大库存,不能继续添加");
				return;
			}
			GoodsListAdapter02.updateCartList(
					Integer.valueOf(list.get(position).get("position")
							.toString()), true);
			// 更新购物车价格
			StoreFragment.updateView(Float.valueOf(list.get(position)
					.get("price").toString()));
			// 更新商品列表添加的数量
			GoodsListAdapter02.updateAllView(
					Integer.valueOf(list.get(position).get("position")
							.toString()), 1);
			StoreFragment.goodsAdapter02.notifyDataSetChanged();
			// 更新购物车列表的商品数量
			list.get(position)
					.put("count",
							Integer.valueOf(list.get(position).get("count")
									.toString()) + 1);
			CartAdapter.this.notifyDataSetChanged();
			break;
		case 2:
			if (list.get(position).get("stock").toString()
					.equals(list.get(position).get("count").toString())) {
				ToastUtil.showToast(activity,"已达最大库存,不能继续添加");
				return;
			}
			GoodsListAdapter03.updateCartList(
					Integer.valueOf(list.get(position).get("position")
							.toString()), true);
			// 更新购物车价格
			StoreFragment.updateView(Float.valueOf(list.get(position)
					.get("price").toString()));
			// 更新商品列表添加的数量
			GoodsListAdapter03.updateAllView(
					Integer.valueOf(list.get(position).get("position")
							.toString()), 1);
			StoreFragment.goodsAdapter03.notifyDataSetChanged();
			// 更新购物车列表的商品数量
			list.get(position)
					.put("count",
							Integer.valueOf(list.get(position).get("count")
									.toString()) + 1);
			CartAdapter.this.notifyDataSetChanged();
			break;
		case 3:
			if (list.get(position).get("stock").toString()
					.equals(list.get(position).get("count").toString())) {
				ToastUtil.showToast(activity,"已达最大库存,不能继续添加");
				return;
			}
			GoodsListAdapter04.updateCartList(
					Integer.valueOf(list.get(position).get("position")
							.toString()), true);
			// 更新购物车价格
			StoreFragment.updateView(Float.valueOf(list.get(position)
					.get("price").toString()));
			// 更新商品列表添加的数量
			GoodsListAdapter04.updateAllView(
					Integer.valueOf(list.get(position).get("position")
							.toString()), 1);
			StoreFragment.goodsAdapter04.notifyDataSetChanged();
			// 更新购物车列表的商品数量
			list.get(position)
					.put("count",
							Integer.valueOf(list.get(position).get("count")
									.toString()) + 1);
			CartAdapter.this.notifyDataSetChanged();
			break;
		case 4:
			if (list.get(position).get("stock").toString()
					.equals(list.get(position).get("count").toString())) {
				ToastUtil.showToast(activity,"已达最大库存,不能继续添加");
				return;
			}
			GoodsListAdapter05.updateCartList(
					Integer.valueOf(list.get(position).get("position")
							.toString()), true);
			// 更新购物车价格
			StoreFragment.updateView(Float.valueOf(list.get(position)
					.get("price").toString()));
			// 更新商品列表添加的数量
			GoodsListAdapter05.updateAllView(
					Integer.valueOf(list.get(position).get("position")
							.toString()), 1);
			StoreFragment.goodsAdapter05.notifyDataSetChanged();
			// 更新购物车列表的商品数量
			list.get(position)
					.put("count",
							Integer.valueOf(list.get(position).get("count")
									.toString()) + 1);
			CartAdapter.this.notifyDataSetChanged();
			break;
		case 5:
			if (list.get(position).get("stock").toString()
					.equals(list.get(position).get("count").toString())) {
				ToastUtil.showToast(activity,"已达最大库存,不能继续添加");
				return;
			}
			GoodsListAdapter06.updateCartList(
					Integer.valueOf(list.get(position).get("position")
							.toString()), true);
			// 更新购物车价格
			StoreFragment.updateView(Float.valueOf(list.get(position)
					.get("price").toString()));
			// 更新商品列表添加的数量
			GoodsListAdapter06.updateAllView(
					Integer.valueOf(list.get(position).get("position")
							.toString()), 1);
			StoreFragment.goodsAdapter06.notifyDataSetChanged();
			// 更新购物车列表的商品数量
			list.get(position)
					.put("count",
							Integer.valueOf(list.get(position).get("count")
									.toString()) + 1);
			CartAdapter.this.notifyDataSetChanged();
			break;
		case 6:
			if (list.get(position).get("stock").toString()
					.equals(list.get(position).get("count").toString())) {
				ToastUtil.showToast(activity,"已达最大库存,不能继续添加");
				return;
			}
			GoodsListAdapter07.updateCartList(
					Integer.valueOf(list.get(position).get("position")
							.toString()), true);
			// 更新购物车价格
			StoreFragment.updateView(Float.valueOf(list.get(position)
					.get("price").toString()));
			// 更新商品列表添加的数量
			GoodsListAdapter07.updateAllView(
					Integer.valueOf(list.get(position).get("position")
							.toString()), 1);
			StoreFragment.goodsAdapter07.notifyDataSetChanged();
			// 更新购物车列表的商品数量
			list.get(position)
					.put("count",
							Integer.valueOf(list.get(position).get("count")
									.toString()) + 1);
			CartAdapter.this.notifyDataSetChanged();
			break;
		case 7:
			if (list.get(position).get("stock").toString()
					.equals(list.get(position).get("count").toString())) {
				ToastUtil.showToast(activity,"已达最大库存,不能继续添加");
				return;
			}
			GoodsListAdapter08.updateCartList(
					Integer.valueOf(list.get(position).get("position")
							.toString()), true);
			// 更新购物车价格
			StoreFragment.updateView(Float.valueOf(list.get(position)
					.get("price").toString()));
			// 更新商品列表添加的数量
			GoodsListAdapter08.updateAllView(
					Integer.valueOf(list.get(position).get("position")
							.toString()), 1);
			StoreFragment.goodsAdapter08.notifyDataSetChanged();
			// 更新购物车列表的商品数量
			list.get(position)
					.put("count",
							Integer.valueOf(list.get(position).get("count")
									.toString()) + 1);
			CartAdapter.this.notifyDataSetChanged();
			break;
		case 8:
			if (list.get(position).get("stock").toString()
					.equals(list.get(position).get("count").toString())) {
				ToastUtil.showToast(activity,"已达最大库存,不能继续添加");
				return;
			}
			GoodsListAdapter09.updateCartList(
					Integer.valueOf(list.get(position).get("position")
							.toString()), true);
			// 更新购物车价格
			StoreFragment.updateView(Float.valueOf(list.get(position)
					.get("price").toString()));
			// 更新商品列表添加的数量
			GoodsListAdapter09.updateAllView(
					Integer.valueOf(list.get(position).get("position")
							.toString()), 1);
			StoreFragment.goodsAdapter09.notifyDataSetChanged();
			// 更新购物车列表的商品数量
			list.get(position)
					.put("count",
							Integer.valueOf(list.get(position).get("count")
									.toString()) + 1);
			CartAdapter.this.notifyDataSetChanged();
			break;
		case 9:
			if (list.get(position).get("stock").toString()
					.equals(list.get(position).get("count").toString())) {
				ToastUtil.showToast(activity,"已达最大库存,不能继续添加");
				return;
			}
			GoodsListAdapter10.updateCartList(
					Integer.valueOf(list.get(position).get("position")
							.toString()), true);
			// 更新购物车价格
			StoreFragment.updateView(Float.valueOf(list.get(position)
					.get("price").toString()));
			// 更新商品列表添加的数量
			GoodsListAdapter10.updateAllView(
					Integer.valueOf(list.get(position).get("position")
							.toString()), 1);
			StoreFragment.goodsAdapter10.notifyDataSetChanged();
			// 更新购物车列表的商品数量
			list.get(position)
					.put("count",
							Integer.valueOf(list.get(position).get("count")
									.toString()) + 1);
			CartAdapter.this.notifyDataSetChanged();
			break;
		case 10:
			if (list.get(position).get("stock").toString()
					.equals(list.get(position).get("count").toString())) {
				ToastUtil.showToast(activity,"已达最大库存,不能继续添加");
				return;
			}
			GoodsListAdapter11.updateCartList(
					Integer.valueOf(list.get(position).get("position")
							.toString()), true);
			// 更新购物车价格
			StoreFragment.updateView(Float.valueOf(list.get(position)
					.get("price").toString()));
			// 更新商品列表添加的数量
			GoodsListAdapter11.updateAllView(
					Integer.valueOf(list.get(position).get("position")
							.toString()), 1);
			StoreFragment.goodsAdapter11.notifyDataSetChanged();
			// 更新购物车列表的商品数量
			list.get(position)
					.put("count",
							Integer.valueOf(list.get(position).get("count")
									.toString()) + 1);
			CartAdapter.this.notifyDataSetChanged();
			break;
		case 11:
			if (list.get(position).get("stock").toString()
					.equals(list.get(position).get("count").toString())) {
				ToastUtil.showToast(activity,"已达最大库存,不能继续添加");
				return;
			}
			GoodsListAdapter12.updateCartList(
					Integer.valueOf(list.get(position).get("position")
							.toString()), true);
			// 更新购物车价格
			StoreFragment.updateView(Float.valueOf(list.get(position)
					.get("price").toString()));
			// 更新商品列表添加的数量
			GoodsListAdapter12.updateAllView(
					Integer.valueOf(list.get(position).get("position")
							.toString()), 1);
			StoreFragment.goodsAdapter12.notifyDataSetChanged();
			// 更新购物车列表的商品数量
			list.get(position)
					.put("count",
							Integer.valueOf(list.get(position).get("count")
									.toString()) + 1);
			CartAdapter.this.notifyDataSetChanged();
			break;
		case 12:
			if (list.get(position).get("stock").toString()
					.equals(list.get(position).get("count").toString())) {
				ToastUtil.showToast(activity,"已达最大库存,不能继续添加");
				return;
			}
			GoodsListAdapter13.updateCartList(
					Integer.valueOf(list.get(position).get("position")
							.toString()), true);
			// 更新购物车价格
			StoreFragment.updateView(Float.valueOf(list.get(position)
					.get("price").toString()));
			// 更新商品列表添加的数量
			GoodsListAdapter13.updateAllView(
					Integer.valueOf(list.get(position).get("position")
							.toString()), 1);
			StoreFragment.goodsAdapter13.notifyDataSetChanged();
			// 更新购物车列表的商品数量
			list.get(position)
					.put("count",
							Integer.valueOf(list.get(position).get("count")
									.toString()) + 1);
			CartAdapter.this.notifyDataSetChanged();
			break;
		case 13:
			if (list.get(position).get("stock").toString()
					.equals(list.get(position).get("count").toString())) {
				ToastUtil.showToast(activity,"已达最大库存,不能继续添加");
				return;
			}
			GoodsListAdapter14.updateCartList(
					Integer.valueOf(list.get(position).get("position")
							.toString()), true);
			// 更新购物车价格
			StoreFragment.updateView(Float.valueOf(list.get(position)
					.get("price").toString()));
			// 更新商品列表添加的数量
			GoodsListAdapter14.updateAllView(
					Integer.valueOf(list.get(position).get("position")
							.toString()), 1);
			StoreFragment.goodsAdapter14.notifyDataSetChanged();
			// 更新购物车列表的商品数量
			list.get(position)
					.put("count",
							Integer.valueOf(list.get(position).get("count")
									.toString()) + 1);
			CartAdapter.this.notifyDataSetChanged();
			break;
		case 14:
			if (list.get(position).get("stock").toString()
					.equals(list.get(position).get("count").toString())) {
				ToastUtil.showToast(activity,"已达最大库存,不能继续添加");
				return;
			}
			GoodsListAdapter15.updateCartList(
					Integer.valueOf(list.get(position).get("position")
							.toString()), true);
			// 更新购物车价格
			StoreFragment.updateView(Float.valueOf(list.get(position)
					.get("price").toString()));
			// 更新商品列表添加的数量
			GoodsListAdapter15.updateAllView(
					Integer.valueOf(list.get(position).get("position")
							.toString()), 1);
			StoreFragment.goodsAdapter15.notifyDataSetChanged();
			// 更新购物车列表的商品数量
			list.get(position)
					.put("count",
							Integer.valueOf(list.get(position).get("count")
									.toString()) + 1);
			CartAdapter.this.notifyDataSetChanged();
			break;
		}

	}
}
