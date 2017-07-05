package com.loosoo100.campus100.chat.sortlistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.application.MyApplication;
import com.loosoo100.campus100.view.CircleView;
import com.loosoo100.campus100.zzboss.chat.fragment.SortFollows;

import java.util.List;

public class SortAdapters extends BaseAdapter implements SectionIndexer {
    private List<SortFollows> list = null;
    private Context mContext;

    public SortAdapters(Context mContext, List<SortFollows> list) {
        this.mContext = mContext;
        this.list = list;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<SortFollows> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_company, null);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.friendname);
            viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
            viewHolder.iv_avatar = (CircleView) view.findViewById(R.id.frienduri);
            viewHolder.friend_school_name = (TextView) view.findViewById(R.id.friend_school_name);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        SortFollows sortFollows = list.get(position);

        //根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);

        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText(sortFollows.getLetters());
        } else {
            viewHolder.tvLetter.setVisibility(View.GONE);
        }
        viewHolder.tvTitle.setText(sortFollows.getUserName());
        viewHolder.friend_school_name.setText(sortFollows.getSchool_name() + "");
        Glide.with(MyApplication.getInstance())
                .load(sortFollows.getUserPhotoThums())
                .into(viewHolder.iv_avatar);

        return view;

    }


    final static class ViewHolder {
        TextView tvLetter;
        TextView tvTitle;
        CircleView iv_avatar;
        TextView friend_school_name;
    }


    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return list.get(position).getLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    /**
     * 提取英文的首字母，非英文字母用#代替。
     *
     * @param str
     * @return
     */
    private String getAlpha(String str) {
        String sortStr = str.trim().substring(0, 1).toUpperCase();
        // 正则表达式，判断首字母是否是英文字母
        if (sortStr.matches("[A-Z]")) {
            return sortStr;
        } else {
            return "#";
        }
    }

    @Override
    public Object[] getSections() {
        return null;
    }
}