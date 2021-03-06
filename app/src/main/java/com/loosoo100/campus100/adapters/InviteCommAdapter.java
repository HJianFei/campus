package com.loosoo100.campus100.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.beans.CommunityBasicInfo;
import com.loosoo100.campus100.beans.UserInfo;
import com.loosoo100.campus100.view.CircleView;

import java.util.List;

/**
 * @author yang 邀请好友列表适配器
 */
public class InviteCommAdapter extends BaseAdapter {

    private List<CommunityBasicInfo> list;
    private LayoutInflater inflater;
    private Activity activity;

    public InviteCommAdapter(Context context, List<CommunityBasicInfo> list) {
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
            convertView = inflater.inflate(R.layout.item_invite_friend, null);
            viewHolder.cv_headShot = (CircleView) convertView
                    .findViewById(R.id.cv_headShot);
            viewHolder.tv_name = (TextView) convertView
                    .findViewById(R.id.tv_name);
            viewHolder.tv_phone = (TextView) convertView
                    .findViewById(R.id.tv_phone);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (!list.get(position).getHeadthumb().equals("") && !list.get(position).getHeadthumb().equals("null")) {
            Glide.with(activity).load(list.get(position).getHeadthumb())
                    .override(150, 150)
                    .into(viewHolder.cv_headShot);
        } else {
            Glide.with(activity).load(R.drawable.imgloading).asBitmap()
                    .into(viewHolder.cv_headShot);
        }

        viewHolder.tv_name.setText(list.get(position).getCommunityName());
        viewHolder.tv_phone.setText(list.get(position).getId());

        return convertView;
    }

    private class ViewHolder {
        private CircleView cv_headShot;
        private TextView tv_name, tv_phone;
    }

}
