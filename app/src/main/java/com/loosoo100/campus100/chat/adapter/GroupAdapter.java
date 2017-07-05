package com.loosoo100.campus100.chat.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.chat.bean.Group;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.view.CircleView;

import java.util.List;


/**
 * Created by HJianFei on 2016/10/17.
 */

public class GroupAdapter extends BaseAdapter {
    private Context mContext;
    private List<Group.GroupBean> groupBeanList;

    public GroupAdapter(Context mContext, List<Group.GroupBean> groupBeanList) {
        this.mContext = mContext;
        this.groupBeanList = groupBeanList;
    }

    @Override
    public int getCount() {
        return groupBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return groupBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.group_list_item, null);
            holder.iv_avatar = (CircleView) convertView.findViewById(R.id.iv_avatar_group);
            holder.tv_group_name = (TextView) convertView.findViewById(R.id.tv_group_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Group.GroupBean groupBean = groupBeanList.get(position);
        holder.tv_group_name.setText(groupBean.getName());
        Glide.with(mContext)
                .load(MyConfig.PIC_AVATAR + groupBean.getAvatar())
                .error(R.drawable.default_chatroom)
                .into(holder.iv_avatar);

        return convertView;
    }

    static final class ViewHolder {
        CircleView iv_avatar;
        TextView tv_group_name;
    }
}
