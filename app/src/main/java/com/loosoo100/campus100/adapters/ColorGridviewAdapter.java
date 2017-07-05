//package com.loosoo100.campus100.adapters;
//
//import java.util.List;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.TextView;
//
//import com.loosoo100.campus100.R;
//import com.loosoo100.campus100.activities.GiftGoodsDetailActivity;
//import com.loosoo100.campus100.beans.GiftGoodsAttrVal;
//
///**
// * 
// * @author yang 规格颜色选择girdview适配器
// */
//public class ColorGridviewAdapter extends BaseAdapter {
//
//	private List<GiftGoodsAttrVal> list;
//	private Context context;
//	private LayoutInflater inflater;
//	
//	private GiftGoodsDetailActivity activity;
//
//	public static int i=-1;
//	private ViewHolder viewHolder = null;
//
//	public ColorGridviewAdapter(Context context, List<GiftGoodsAttrVal> list) {
//		this.context = context;
//		this.list = list;
//		activity=(GiftGoodsDetailActivity) context;
//		inflater = LayoutInflater.from(context);
//	}
//
//	@Override
//	public int getCount() {
//		return list.size();
//	}
//
//	@Override
//	public Object getItem(int position) {
//		return list.get(position);
//	}
//
//	@Override
//	public long getItemId(int position) {
//		return position;
//	}
//
//	@Override
//	public View getView(final int position, View convertView, ViewGroup parent) {
//
//		if (convertView == null) {
//			viewHolder = new ViewHolder();
//			convertView = inflater.inflate(R.layout.item_standrad_gridview,
//					null);
//			viewHolder.btn_standard = (TextView) convertView
//					.findViewById(R.id.btn_standard);
//			convertView.setTag(viewHolder);
//		} else {
//			viewHolder = (ViewHolder) convertView.getTag();
//		}
//		viewHolder.btn_standard.setText(list.get(position).getAttrValue());
//		viewHolder.btn_standard.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.shape_gray_f8f8f8));
//		viewHolder.btn_standard.setTextColor(Color.BLACK);
//		if (i==position) {
//			viewHolder.btn_standard.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.shape_red));
//			viewHolder.btn_standard.setTextColor(Color.WHITE);
//		}
//
//		return convertView;
//	}
//
//	class ViewHolder {
//		private TextView btn_standard;
//	}
//
//}
