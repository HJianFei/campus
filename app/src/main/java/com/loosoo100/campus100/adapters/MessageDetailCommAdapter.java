package com.loosoo100.campus100.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.loosoo100.campus100.R;
import com.loosoo100.campus100.beans.MessageInfo;

import java.util.List;

/**
 * @author yang 社团消息详情适配器
 */
public class MessageDetailCommAdapter extends BaseAdapter {

    private List<MessageInfo> list;
    private LayoutInflater inflater;

    public MessageDetailCommAdapter(Context context, List<MessageInfo> list) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_message_detail_comm, null);
            viewHolder.tv_time = (TextView) convertView
                    .findViewById(R.id.tv_time);
            viewHolder.tv_content = (TextView) convertView
                    .findViewById(R.id.tv_content);
            viewHolder.iv_redPoint = (ImageView) convertView
                    .findViewById(R.id.iv_redPoint);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_time.setText(list.get(position).getTime() + "");
        viewHolder.tv_content.setText(list.get(position).getContent() + "");

        if (list.get(position).getStatus().equals("0")) {
            viewHolder.iv_redPoint.setVisibility(View.VISIBLE);
        } else {
            viewHolder.iv_redPoint.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder {
        private TextView tv_time, tv_content;
        private ImageView iv_redPoint;
    }

}
