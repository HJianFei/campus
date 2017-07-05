package com.loosoo100.campus100.chat.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.chat.bean.Groupnumber;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.view.CircleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HJianFei on 2016/11/9.
 */

public class AllGroupMembersAdapter extends BaseAdapter {

    private Context mContext;
    private Groupnumber groupnumber;
    private List<Groupnumber.RootBean> groupRootBeanList = new ArrayList<>();

    public AllGroupMembersAdapter(Context mContext, Groupnumber groupnumber, List<Groupnumber.RootBean> beanList) {
        this.mContext = mContext;
        this.groupnumber = groupnumber;
        groupRootBeanList = beanList;
    }

    @Override
    public int getCount() {
        return groupRootBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return groupRootBeanList.get(position);
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
            convertView = View.inflate(mContext, R.layout.all_group_member_item, null);
            holder.iv_avatar_group_member = (CircleView) convertView.findViewById(R.id.iv_avatar_group_member);
            holder.tv_group_member_name = (TextView) convertView.findViewById(R.id.tv_group_member_name);
            holder.tv_group_member_is_grant = (TextView) convertView.findViewById(R.id.tv_group_member_is_grant);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Groupnumber.RootBean bean = groupnumber.getRoot().get(position);
        Glide.with(mContext).load(MyConfig.PIC_AVATAR + bean.getUserPhotoThums()).into(holder.iv_avatar_group_member);
        holder.tv_group_member_name.setText(bean.getUserName());
        if (bean.getStatus().equals("1")) {
            holder.tv_group_member_is_grant.setText("");

        } else if (bean.getUserId().equals(groupnumber.getUid())) {
            holder.tv_group_member_is_grant.setText("群主");
        }
        return convertView;
    }

    private static class ViewHolder {
        CircleView iv_avatar_group_member;
        TextView tv_group_member_name;
        TextView tv_group_member_is_grant;
    }

}
