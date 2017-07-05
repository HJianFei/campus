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
import com.loosoo100.campus100.beans.CampusInfo;

/**
 * 
 * @author yang
 *	开机页搜索学校适配器
 */
public class GuideSearchAdapter extends BaseAdapter {
	
	private List<CampusInfo> list;
	private Context context;
	private LayoutInflater inflater;

	public GuideSearchAdapter(Context context,List<CampusInfo>list){
		this.context=context;
		this.list=list;
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
			convertView = inflater.inflate(R.layout.item_search_guide, null);
			viewHolder.tv_school = (TextView) convertView.findViewById(R.id.tv_school);
			viewHolder.imageview = (ImageView) convertView.findViewById(R.id.imageview);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tv_school.setText(list.get(position).getCampusName());
		if (position==list.size()-1) {
			viewHolder.imageview.setVisibility(View.GONE);
		}else {
			viewHolder.imageview.setVisibility(View.VISIBLE);
		}
		return convertView;
	}

	class ViewHolder {
		private TextView tv_school;
		private ImageView imageview;
	}


}
