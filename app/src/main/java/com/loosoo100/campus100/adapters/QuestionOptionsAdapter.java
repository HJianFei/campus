package com.loosoo100.campus100.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;

import com.loosoo100.campus100.R;

/**
 * 
 * @author yang 调查问卷选项适配器
 */
public class QuestionOptionsAdapter extends BaseAdapter {

	private List<String> list;
	private Context context;
	private LayoutInflater inflater;

	private ViewHolder viewHolder = null;

	public int mPosition = -1;

	private String[] alphabet = new String[] { "A", "B", "C", "D", "E", "F",
			"G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
			"T", "U", "V", "W", "X", "Y", "Z" };

	public QuestionOptionsAdapter(Context context, List<String> list) {
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
			convertView = inflater
					.inflate(R.layout.item_question_options, null);
			viewHolder.rb_answer = (RadioButton) convertView
					.findViewById(R.id.rb_answer);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.rb_answer.setText(alphabet[position] + "."
				+ list.get(position));
		if (position == mPosition) {
			viewHolder.rb_answer.setChecked(true);
		} else {
			viewHolder.rb_answer.setChecked(false);
		}

		return convertView;
	}

	class ViewHolder {
		public RadioButton rb_answer;
	}

}
