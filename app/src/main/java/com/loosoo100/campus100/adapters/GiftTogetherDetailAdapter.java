package com.loosoo100.campus100.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.beans.GiftTogetherFriendInfo;
import com.loosoo100.campus100.view.CircleView;

/**
 * @author yang 礼物盒子凑一凑详情适配器
 */
public class GiftTogetherDetailAdapter extends BaseAdapter {

    private List<GiftTogetherFriendInfo> list;
    private LayoutInflater inflater;
    private ViewHolder viewHolder = null;

    private Activity activity;

    public GiftTogetherDetailAdapter(Context context,
                                     List<GiftTogetherFriendInfo> list) {
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

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_gift_together, null);
            viewHolder.tv_money = (TextView) convertView
                    .findViewById(R.id.tv_money);
            viewHolder.tv_name = (TextView) convertView
                    .findViewById(R.id.tv_name);
            viewHolder.tv_say = (TextView) convertView
                    .findViewById(R.id.tv_say);
            viewHolder.tv_time = (TextView) convertView
                    .findViewById(R.id.tv_time);
            viewHolder.cv_headshot = (CircleView) convertView
                    .findViewById(R.id.cv_headshot);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_money.setText("￥" + list.get(position).getMoney());
//		viewHolder.tv_name.setText(list.get(position).getName() + "说:");
        viewHolder.tv_say.setText(list.get(position).getContent());
        if (!list.get(position).getContent().equals("")) {
            viewHolder.tv_say.setVisibility(View.VISIBLE);
            viewHolder.tv_name.setText(list.get(position).getName() + "说:");
        } else {
            viewHolder.tv_say.setVisibility(View.GONE);
            viewHolder.tv_name.setText(list.get(position).getName() + ":");
        }
        viewHolder.tv_time.setText(list.get(position).getTime());

        if (!list.get(position).getHeadShot().equals("")) {
            Glide.with(activity).load(list.get(position).getHeadShot())
                    .override(150, 150).into(viewHolder.cv_headshot);
        } else {
            Glide.with(activity).load("").placeholder(R.drawable.imgloading)
                    .override(150, 150).into(viewHolder.cv_headshot);
        }

        return convertView;
    }

    class ViewHolder {
        public TextView tv_money, tv_name, tv_say, tv_time;
        public CircleView cv_headshot;
    }

}
