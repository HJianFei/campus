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
import com.loosoo100.campus100.beans.QuestionListInfo;

/**
 * 
 * @author yang 调查问卷适配器
 */
public class QuestionAdapter extends BaseAdapter {

	private List<QuestionListInfo> list;
	private Context context;
	private LayoutInflater inflater;

	private ViewHolder viewHolder = null;
	

	public QuestionAdapter(Context context, List<QuestionListInfo> list) {
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

		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_question, null);
			viewHolder.tv_title = (TextView) convertView
					.findViewById(R.id.tv_title);
			viewHolder.tv_content = (TextView) convertView
					.findViewById(R.id.tv_content);
			viewHolder.iv = (ImageView) convertView
					.findViewById(R.id.iv);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tv_title.setText(list.get(position).getTitle());
		viewHolder.tv_content.setText(list.get(position).getContent());
		if (list.get(position).getStatus()==1) {
			viewHolder.iv.setVisibility(View.GONE);
		}else {
			viewHolder.iv.setVisibility(View.VISIBLE);
		}

		return convertView;
	}

	class ViewHolder {
		public TextView tv_title,tv_content;
		public ImageView iv;
	}

}
