package com.loosoo100.campus100.adapters;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.loosoo100.campus100.R;
import com.loosoo100.campus100.activities.AddressActivity;
import com.loosoo100.campus100.activities.AddressEditActivity;
import com.loosoo100.campus100.beans.AddressInfo;

/**
 * 
 * @author yang 收货地址适配器
 */
public class AddressAdapter extends BaseAdapter {

	private List<AddressInfo> list;
	private Context context;
	private LayoutInflater inflater;
	private AddressActivity activity;

	private ViewHolder viewHolder = null;

	public AddressAdapter(Context context, List<AddressInfo> list) {
		this.context = context;
		this.list = list;
		activity = (AddressActivity) context;

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
			convertView = inflater.inflate(R.layout.item_address, null);
			viewHolder.tv_school = (TextView) convertView
					.findViewById(R.id.tv_school);
			viewHolder.tv_name = (TextView) convertView
					.findViewById(R.id.tv_name);
			viewHolder.tv_phone = (TextView) convertView
					.findViewById(R.id.tv_phone);
			viewHolder.tv_detail = (TextView) convertView
					.findViewById(R.id.tv_detail);
			viewHolder.tv_address = (TextView) convertView
					.findViewById(R.id.tv_address);
			viewHolder.btn_edit = (Button) convertView
					.findViewById(R.id.btn_edit);
			viewHolder.tv_type = (TextView) convertView
					.findViewById(R.id.tv_type);
			viewHolder.tv_default = (TextView) convertView
					.findViewById(R.id.tv_default);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (list.get(position).getSchool().equals("")
				|| list.get(position).getSchool().equals("null")) {
			viewHolder.tv_type.setText("礼物盒子");
			viewHolder.tv_detail.setText("详细地址：");
			viewHolder.tv_type.setBackgroundDrawable(activity.getResources()
					.getDrawable(R.drawable.shape_orange_ee761a));
			viewHolder.tv_address.setText(list.get(position).getAddress());
			if (list.get(position).getProvince()
					.equals(list.get(position).getCity())) {
				viewHolder.tv_school.setText("所在地区："
						+ list.get(position).getCity()
						+ list.get(position).getArea());
			} else {
				viewHolder.tv_school.setText("所在地区："
						+ list.get(position).getProvince()
						+ list.get(position).getCity()
						+ list.get(position).getArea());
			}

		} else {
			viewHolder.tv_school
					.setText("学校：" + list.get(position).getSchool());
			viewHolder.tv_detail.setText("地址：");
			viewHolder.tv_type.setText("小卖部");
			viewHolder.tv_type.setBackgroundDrawable(activity.getResources()
					.getDrawable(R.drawable.shape_pink_ff7d80));
			viewHolder.tv_address.setText("" + list.get(position).getAddress());
		}

		viewHolder.tv_name.setText("" + list.get(position).getUserName());
		viewHolder.tv_phone.setText("" + list.get(position).getUserPhone());

		if (list.get(position).getIsDefault().equals("1")) {
			viewHolder.tv_default.setVisibility(View.VISIBLE);
		} else {
			viewHolder.tv_default.setVisibility(View.GONE);
		}
		viewHolder.btn_edit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity, AddressEditActivity.class);
				intent.putExtra("userName", list.get(position).getUserName());
				intent.putExtra("userPhone", list.get(position).getUserPhone());
				intent.putExtra("address", list.get(position).getAddress());
				intent.putExtra("addressId", list.get(position).getAddressId());
				intent.putExtra("school", list.get(position).getSchool());
				intent.putExtra("province", list.get(position).getProvince());
				intent.putExtra("pid", list.get(position).getPid());
				intent.putExtra("city", list.get(position).getCity());
				intent.putExtra("cid", list.get(position).getCid());
				intent.putExtra("area", list.get(position).getArea());
				intent.putExtra("aid", list.get(position).getAid());
				intent.putExtra("isDefault", list.get(position).getIsDefault());
				activity.startActivity(intent);
			}
		});

		return convertView;
	}

	class ViewHolder {
		public TextView tv_name, tv_phone, tv_address, tv_school, tv_type,
				tv_default, tv_detail;
		public Button btn_edit;
	}

}
