package com.loosoo100.campus100.zzboss.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loosoo100.campus100.R;
import com.loosoo100.campus100.beans.MessageInfo;
import com.loosoo100.campus100.view.CircleView;
import com.loosoo100.campus100.view.RoundAngleImageView;

/**
 * 
 * @author yang 消息列表适配器
 */
public class BossMessageAdapter extends BaseAdapter {

	private List<MessageInfo> list;
	private LayoutInflater inflater;
	private Activity activity;

	public BossMessageAdapter(Context context, List<MessageInfo> list) {
		this.list = list;
		activity = (Activity) context;
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
			convertView = inflater.inflate(R.layout.item_boss_message, null);
			viewHolder.iv_picture = (CircleView) convertView
					.findViewById(R.id.iv_picture);
			viewHolder.iv_sex = (ImageView) convertView
					.findViewById(R.id.iv_sex);
			viewHolder.iv_red = (ImageView) convertView
					.findViewById(R.id.iv_red);
			viewHolder.iv_red02 = (ImageView) convertView
					.findViewById(R.id.iv_red02);
			viewHolder.tv_title = (TextView) convertView
					.findViewById(R.id.tv_title);
			viewHolder.tv_content = (TextView) convertView
					.findViewById(R.id.tv_content);
			viewHolder.tv_time = (TextView) convertView
					.findViewById(R.id.tv_time);
			viewHolder.tv_title02 = (TextView) convertView
					.findViewById(R.id.tv_title02);
			viewHolder.tv_time02 = (TextView) convertView
					.findViewById(R.id.tv_time02);
			viewHolder.tv_content02 = (TextView) convertView
					.findViewById(R.id.tv_content02);
			viewHolder.ll_person = (LinearLayout) convertView
					.findViewById(R.id.ll_person);
			viewHolder.ll_system = (LinearLayout) convertView
					.findViewById(R.id.ll_system);
			viewHolder.raiv_picture = (RoundAngleImageView) convertView
					.findViewById(R.id.raiv_picture);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// if (condition) {
		// //显示个人消息
		// viewHolder.ll_person.setVisibility(View.VISIBLE);
		// viewHolder.ll_system.setVisibility(View.GONE);
		// viewHolder.tv_title.setText("" + list.get(position).getTitle());
		// viewHolder.tv_content.setText("" + list.get(position).getContent());
		// viewHolder.tv_time.setText("" + list.get(position).getTime());
		// Glide.with(activity).load().into(viewHolder.iv_picture);
		// if (男) {
		// viewHolder.iv_sex.setImageResource(R.drawable.icon_male_picture);
		// }else {
		// viewHolder.iv_sex.setImageResource(R.drawable.icon_female_picture);
		// }
		// if (未读) {
		// viewHolder.iv_red.setVisibility(View.VISIBLE);
		// }else {
		// viewHolder.iv_red.setVisibility(View.GONE);
		// }
		// }else {
		// //显示系统消息
		// viewHolder.ll_system.setVisibility(View.VISIBLE);
		// viewHolder.ll_person.setVisibility(View.GONE);
		// viewHolder.tv_title02.setText("" + list.get(position).getTitle());
		// viewHolder.tv_content02.setText("" +
		// list.get(position).getContent());
		// viewHolder.tv_time02.setText("" + list.get(position).getTime());
		// Glide.with(activity).load().into(viewHolder.raiv_picture);
		// if (未读) {
		// viewHolder.iv_red02.setVisibility(View.VISIBLE);
		// }else {
		// viewHolder.iv_red02.setVisibility(View.GONE);
		// }
		// }
		return convertView;
	}

	private class ViewHolder {
		private CircleView iv_picture;
		private ImageView iv_sex, iv_red, iv_red02;
		private TextView tv_title, tv_content, tv_time, tv_title02, tv_time02,
				tv_content02;
		private LinearLayout ll_person, ll_system;
		private RoundAngleImageView raiv_picture;
	}

}
