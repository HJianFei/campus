package com.loosoo100.campus100.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.beans.MessageInfo;

import java.util.List;

/**
 * @author yang 消息列表适配器
 */
public class MessageAdapter extends BaseAdapter {

    private List<MessageInfo> list;
    private LayoutInflater inflater;
    private Activity activity;

    public MessageAdapter(Context context, List<MessageInfo> list) {
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
            convertView = inflater.inflate(R.layout.item_message, null);
            // 消息图标
            viewHolder.iv_message = (ImageView) convertView
                    .findViewById(R.id.iv_message);
            //红点
            viewHolder.iv_redPoint = (ImageView) convertView
                    .findViewById(R.id.iv_redPoint);
            // 消息标题
            viewHolder.tv_title = (TextView) convertView
                    .findViewById(R.id.tv_title);
            // 消息内容
            viewHolder.tv_content = (TextView) convertView
                    .findViewById(R.id.tv_content);
            // 消息时间
            viewHolder.tv_time = (TextView) convertView
                    .findViewById(R.id.tv_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_title.setText("" + list.get(position).getTitle());
        viewHolder.tv_content.setText("" + list.get(position).getContent());
        viewHolder.tv_time.setText("" + list.get(position).getTime());

        if (list.get(position).getContent().equals("")){
            viewHolder.tv_content.setVisibility(View.GONE);
        }else{
            viewHolder.tv_content.setVisibility(View.VISIBLE);
        }

        //红点是否显示
        if (list.get(position).getStatus().equals("0")){
            viewHolder.iv_redPoint.setVisibility(View.VISIBLE);
        }else{
            viewHolder.iv_redPoint.setVisibility(View.GONE);
        }
        if (!list.get(position).getPic().equals("") && !list.get(position).getPic().equals("null")) {
            Glide.with(activity).load(list.get(position).getPic()).placeholder(R.drawable.imgloading).override(200, 200).into(viewHolder.iv_message);
        } else {
            Glide.with(activity).load(R.drawable.imgloading).asBitmap().into(viewHolder.iv_message);
        }
        return convertView;
    }

    private class ViewHolder {
        private ImageView iv_message,iv_redPoint;
        private TextView tv_title, tv_content, tv_time;
    }

}
