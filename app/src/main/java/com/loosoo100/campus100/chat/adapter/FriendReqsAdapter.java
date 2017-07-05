package com.loosoo100.campus100.chat.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.application.MyApplication;
import com.loosoo100.campus100.chat.bean.Friend;
import com.loosoo100.campus100.chat.bean.Friend_Reqs_List;
import com.loosoo100.campus100.chat.db.FriendDao;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.view.CircleView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HJianFei on 2016/10/14.
 */

public class FriendReqsAdapter extends BaseAdapter {
    private Context mContext;
    private List<Friend_Reqs_List.RootBean> rootBeanList = new ArrayList<>();
    private String uid = "";
    private Handler mHandler;
    private int count;

    public FriendReqsAdapter(Context mContext, List<Friend_Reqs_List.RootBean> rootBeanList, Handler handler) {
        this.mContext = mContext;
        this.rootBeanList = rootBeanList;
        this.mHandler = handler;
        uid = mContext.getSharedPreferences(UserInfoDB.USERTABLE, mContext.MODE_PRIVATE)
                .getString(UserInfoDB.USERID, "");
    }

    @Override
    public int getCount() {
        return rootBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return rootBeanList.get(position);
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
            convertView = View.inflate(mContext, R.layout.friend_requs_list_item, null);
            holder.mName = (TextView) convertView.findViewById(R.id.ship_name);
            holder.mMessage = (TextView) convertView.findViewById(R.id.ship_message);
            holder.mHead = (CircleView) convertView.findViewById(R.id.new_header);
            holder.mState = (TextView) convertView.findViewById(R.id.ship_state);
            holder.black_list = (TextView) convertView.findViewById(R.id.ship_black_list);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        count = position;
        final Friend_Reqs_List.RootBean rootBean = rootBeanList.get(position);
        holder.mName.setText(rootBean.getUserName());
        if (!rootBean.getMsg().equals("")) {
            holder.mMessage.setVisibility(View.VISIBLE);
            holder.mMessage.setText("验证消息："+rootBean.getMsg().get(rootBean.getMsg().size() - 1));
        }
        Glide.with(mContext).load(MyConfig.PIC_AVATAR + rootBean.getUserPhotoThums()).into(holder.mHead);
        holder.black_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.black_list.getText().equals("拉黑")) {
                    moveToBlackList(rootBean.getUid(), uid);
                }

            }
        });
        holder.mState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.mState.getText().equals("同意")) {
                    updateReqFriendStatus(rootBean.getUid(), uid);
                }

            }

            private void updateReqFriendStatus(final String to_uid, final String uid) {
                StringRequest agreeRequest = new StringRequest(Request.Method.POST, MyConfig.UPDATE_REQU_STATUS, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Message message = Message.obtain();
                        message.arg1 = 1001;
                        FriendDao friendDao = new FriendDao(mContext);
                        Friend friend = new Friend();
                        friend.setUid(rootBean.getUid());
                        friend.setNickname(rootBean.getUserName());
                        friend.setAvatar(MyConfig.PIC_AVATAR + rootBean.getUserPhotoThums());
                        friendDao.addFriend(friend);
                        mHandler.sendMessage(message);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("uid", to_uid);
                        map.put("to_uid", uid);
                        map.put("status", "2");
                        return map;
                    }
                };
                MyApplication.getRequestQueue().add(agreeRequest);
            }
        });
        if (rootBean.getStatus().equals("0")||rootBean.getStatus().equals("1")) {
            holder.mState.setText("同意");
            holder.mState.setTextColor(mContext.getResources().getColor(R.color.red_fd3c49));
            holder.black_list.setVisibility(View.VISIBLE);
            holder.black_list.setText("拉黑");
            holder.black_list.setTextColor(mContext.getResources().getColor(R.color.red_fd3c49));
        } else if (rootBean.getStatus().equals("2")) {
            holder.mState.setText("已添加");
        }

        return convertView;
    }

    private void moveToBlackList(final String black_uid, final String uid) {
        StringRequest agreeRequest = new StringRequest(Request.Method.POST, MyConfig.MOVE_TO_BLACK_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Message message = Message.obtain();
                message.arg1 = 1002;
                message.arg2 = count;
                mHandler.sendMessage(message);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("uid", uid);
                map.put("black_uid", black_uid);
                return map;
            }
        };
        MyApplication.getRequestQueue().add(agreeRequest);
    }

    private static class ViewHolder {
        CircleView mHead;
        TextView mName;
        TextView mState;
        TextView mMessage;
        TextView black_list;

    }

}
