package com.loosoo100.campus100.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.loosoo100.campus100.R;
import com.loosoo100.campus100.zzboss.beans.BossCityInfo;

/**
 * 
 * @author yang 搜索社团活动适配器
 */
public class CommActiSearchAdapter extends BaseAdapter {

	private List<BossCityInfo> list;
	private LayoutInflater inflater;
	public int mPosition=-1;

	public CommActiSearchAdapter(Context context, List<BossCityInfo> list) {
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_comm_search, null);
			viewHolder.tv_type = (TextView) convertView
					.findViewById(R.id.tv_type);
			viewHolder.iv_red = (ImageView) convertView
					.findViewById(R.id.iv_red);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tv_type.setText(list.get(position).getCity());
		if (position==mPosition) {
			viewHolder.iv_red.setVisibility(View.VISIBLE);
		}else {
			viewHolder.iv_red.setVisibility(View.GONE);
		}
		return convertView;
	}

	class ViewHolder {
		private TextView tv_type;
		private ImageView iv_red;
	}

}
