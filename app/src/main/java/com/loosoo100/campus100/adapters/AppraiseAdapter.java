package com.loosoo100.campus100.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.loosoo100.campus100.R;
import com.loosoo100.campus100.beans.GoodsInfo;

/**
 * 
 * @author yang 评价适配器
 */
public class AppraiseAdapter extends BaseAdapter {

	private List<GoodsInfo> list;
	private LayoutInflater inflater;

	private ViewHolder viewHolder = null;

	public AppraiseAdapter(Context context, List<GoodsInfo> list) {
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
			convertView = inflater.inflate(R.layout.item_appraise, null);
			viewHolder.tv_goodsName = (TextView) convertView
					.findViewById(R.id.tv_goodsName);
			viewHolder.radioGroup = (RadioGroup) convertView
					.findViewById(R.id.radioGroup);
			viewHolder.rb_like = (RadioButton) convertView
					.findViewById(R.id.rb_like);
			viewHolder.rb_dislike = (RadioButton) convertView
					.findViewById(R.id.rb_dislike);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tv_goodsName.setText("" + list.get(position).getGoodsName());

		viewHolder.rb_like.setTag(position);
		viewHolder.rb_dislike.setTag(position);

		viewHolder.radioGroup
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if (checkedId == viewHolder.rb_like.getId()
								&& Integer.valueOf(viewHolder.rb_like.getTag()
										.toString()) == position) {
							viewHolder.rb_like.setChecked(true);
						} else if (checkedId == viewHolder.rb_dislike.getId()
								&& Integer.valueOf(viewHolder.rb_dislike
										.getTag().toString()) == position) {
							viewHolder.rb_dislike.setChecked(true);
						}
					}
				});

		return convertView;
	}

	class ViewHolder {
		public TextView tv_goodsName;
		public RadioButton rb_like, rb_dislike;
		public RadioGroup radioGroup;
	}

}
