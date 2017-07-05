package com.loosoo100.campus100.chat.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.activities.CampusContactsFriendActivity;
import com.loosoo100.campus100.activities.CampusContactsPersonalActivity;
import com.loosoo100.campus100.chat.bean.Groupnumber;
import com.loosoo100.campus100.chat.ui.SelectFriendsActivity;
import com.loosoo100.campus100.chat.widget.SelectableRoundedImageView;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HJianFei on 2016/10/18.
 */

public class GridViewAdapter extends BaseAdapter {

    private Context mContext;
    private Groupnumber groupnumber;
    private String uid = "";
    private String gid = "";
    private List<Groupnumber.RootBean> rootBeanList = new ArrayList<>();

    public GridViewAdapter(Context mContext, Groupnumber groupnumber, String gid) {
        this.mContext = mContext;
        this.groupnumber = groupnumber;
        this.gid = gid;
        if (groupnumber.getRoot().size() >= 20) {
            this.rootBeanList = groupnumber.getRoot().subList(0, 19);
        } else {
            this.rootBeanList = groupnumber.getRoot();
        }
        uid = mContext.getSharedPreferences(UserInfoDB.USERTABLE, mContext.MODE_PRIVATE)
                .getString(UserInfoDB.USERID, "");
    }

    @Override
    public int getCount() {
        if (uid.equals(groupnumber.getUid())) {
            return rootBeanList.size() + 2;
        } else if (groupnumber.getIsGranted() == 1) {
            return rootBeanList.size() + 1;
        } else {
            return rootBeanList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return groupnumber.getRoot().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_gridview_group_member, null);
        }
        SelectableRoundedImageView iv_avatar = (SelectableRoundedImageView) convertView.findViewById(R.id.iv_avatar);
        TextView tv_username = (TextView) convertView.findViewById(R.id.tv_username);
        ImageView badge_delete = (ImageView) convertView.findViewById(R.id.badge_delete);
        // 最后一个item，减人按钮
        if (position == getCount() - 1 && uid.equals(groupnumber.getUid())) {
            tv_username.setText("");
            badge_delete.setVisibility(View.GONE);
            iv_avatar.setImageResource(R.drawable.icon_btn_deleteperson);

            iv_avatar.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, SelectFriendsActivity.class);
                    intent.putExtra("DeleteGroupMember", (Serializable) groupnumber.getRoot());
                    intent.putExtra("gid", gid);
                    intent.putExtra("type", "delete");
                    mContext.startActivity(intent);
                }

            });
        } else if (position == getCount() - 2 && uid.equals(groupnumber.getUid())) {
            tv_username.setText("");
            badge_delete.setVisibility(View.GONE);
            iv_avatar.setImageResource(R.drawable.jy_drltsz_btn_addperson);

            iv_avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, SelectFriendsActivity.class);
                    intent.putExtra("AddGroupMember", (Serializable) groupnumber.getRoot());
                    intent.putExtra("gid", gid);
                    intent.putExtra("type", "add");
                    mContext.startActivity(intent);

                }
            });
        } else if (position == getCount() - 1 && groupnumber.getIsGranted() == 1) {
            tv_username.setText("");
            badge_delete.setVisibility(View.GONE);
            iv_avatar.setImageResource(R.drawable.jy_drltsz_btn_addperson);

            iv_avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, SelectFriendsActivity.class);
                    intent.putExtra("AddGroupMember", (Serializable) groupnumber.getRoot());
                    intent.putExtra("gid", gid);
                    intent.putExtra("type", "add");
                    mContext.startActivity(intent);

                }
            });

        } else { // 普通成员
            final Groupnumber.RootBean bean = groupnumber.getRoot().get(position);
            tv_username.setText(bean.getUserName());
            Glide.with(mContext).load(MyConfig.PIC_AVATAR + bean.getUserPhotoThums()).into(iv_avatar);

            iv_avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (uid.equals(bean.getUserId())) {
                        Intent intent = new Intent(mContext,
                                CampusContactsPersonalActivity.class);
                        mContext.startActivity(intent);
                    } else {
                        Intent intent = new Intent(mContext,
                                CampusContactsFriendActivity.class);
                        intent.putExtra("muid", bean.getUserId());
                        mContext.startActivity(intent);
                    }
                }

            });

        }

        return convertView;
    }
}
