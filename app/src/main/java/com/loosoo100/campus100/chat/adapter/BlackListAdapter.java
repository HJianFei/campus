package com.loosoo100.campus100.chat.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.chat.bean.BlackList;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.view.CircleView;

import java.util.List;

/**
 * Created by HJianFei on 2016/10/17.
 */

public class BlackListAdapter extends BaseAdapter {
    private Context mContext;
    private List<BlackList.RootBean> blackList;

    public BlackListAdapter(Context mContext, List<BlackList.RootBean> blackList) {
        this.mContext = mContext;
        this.blackList = blackList;
    }

    @Override
    public int getCount() {
        return blackList.size();
    }

    @Override
    public Object getItem(int position) {
        return blackList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_black_list, null);
            holder.avatar = (CircleView) convertView.findViewById(R.id.blackuri);
            holder.mName = (TextView) convertView.findViewById(R.id.blackname);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BlackList.RootBean bean = blackList.get(position);
        holder.mName.setText(bean.getUserName());
        Glide.with(mContext)
                .load(MyConfig.PIC_AVATAR + bean.getUserPhotoThums())
                .into(holder.avatar);
        return convertView;
    }

    private static class ViewHolder {
        CircleView avatar;
        TextView mName;
    }
}
