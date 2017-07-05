package com.loosoo100.campus100.zzboss.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.loosoo100.campus100.R;
import com.loosoo100.campus100.interfaces.ILetterIndexer;
import com.loosoo100.campus100.zzboss.beans.BossCityInfo;

public class LetterIndexAdapter extends BaseAdapter implements ILetterIndexer {
	private List<BossCityInfo> citys;
	private Context context;
	private HashMap<String, Integer> hashMap;

	public LetterIndexAdapter(List<BossCityInfo> citys, Context context,
			HashMap<String, Integer> hashMap) {
		this.context = context;
		this.citys = citys;
		this.hashMap = hashMap;
	}

	@Override
	public int getCount() {
		return citys.size();
	}

	@Override
	public Object getItem(int position) {
		return citys.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		viewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_city_search, null);
			holder = new viewHolder();
			holder.city_letter = (TextView) convertView
					.findViewById(R.id.city_letter);
			holder.city_name = (TextView) convertView
					.findViewById(R.id.city_item_name);
			convertView.setTag(holder);
		} else {
			holder = (viewHolder) convertView.getTag();
		}

		if (!hashMap.containsKey(String.valueOf(citys.get(position).getSortLetter()))) {
			hashMap.put(String.valueOf(citys.get(position).getSortLetter()), position);
		}

		holder.city_name.setText(citys.get(position).getCity());
		String currentLetter = String.valueOf(citys.get(position).getSortLetter());

		String lastLetter = String.valueOf((position - 1) >= 0 ? (String
				.valueOf(citys.get(position - 1).getSortLetter())) : "");
		if (!lastLetter.equals(currentLetter)) {
			holder.city_letter.setText(currentLetter);
			holder.city_letter.setVisibility(View.VISIBLE);
		} else {
			holder.city_letter.setVisibility(View.GONE);

		}

		return convertView;
	}

	public class viewHolder {
		private TextView city_letter;
		private TextView city_name;
	}

	@Override
	public int getPositionForSection(String section) {
		if (!TextUtils.isEmpty(section)) {
			if (hashMap.containsKey(section)) {
				return hashMap.get(section);
			}
		}
		return -1;
	}

}