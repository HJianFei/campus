package com.loosoo100.campus100.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.loosoo100.campus100.R;
import com.loosoo100.campus100.beans.CustomInfo;

/**
 * 
 * @author yang 客服中心适配器
 */
public class CustomAdapter extends BaseAdapter {

	private List<CustomInfo> list;
	private Context context;
	private LayoutInflater inflater;

	public CustomAdapter(Context context, List<CustomInfo> list) {
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
			convertView = inflater.inflate(R.layout.item_custom, null);
			viewHolder.tv_index = (TextView) convertView
					.findViewById(R.id.tv_index);
			viewHolder.tv_question = (TextView) convertView
					.findViewById(R.id.tv_question);
			viewHolder.tv_answer = (TextView) convertView
					.findViewById(R.id.tv_answer);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tv_index.setText((position+1)+".");
		viewHolder.tv_question.setText(""+list.get(position).getQuestion());
		viewHolder.tv_answer.setText(""+list.get(position).getAnswer());
		return convertView;
	}

	class ViewHolder {
		public TextView tv_question,tv_answer,tv_index;
	}

}
