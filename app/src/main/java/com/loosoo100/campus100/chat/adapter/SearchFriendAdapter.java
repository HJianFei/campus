package com.loosoo100.campus100.chat.adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.application.MyApplication;
import com.loosoo100.campus100.chat.bean.SearchInfo;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.view.CircleView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HJianFei on 2016/10/28.
 */

public class SearchFriendAdapter extends BaseAdapter {
    private Context mContext;
    private List<SearchInfo.RootBean> beanList;
    private String mSearchText;
    private String type;
    private String uid;
    private Handler search_friend_handler;
    private int index;
    private Dialog dialog;
    private String ed_name = "";

    public SearchFriendAdapter(Context mContext, List<SearchInfo.RootBean> beanList, String searchText, String type, Handler handler, int index) {
        this.mContext = mContext;
        this.beanList = beanList;
        this.mSearchText = searchText;
        this.type = type;
        this.search_friend_handler = handler;
        this.index = index;
        uid = mContext.getSharedPreferences(UserInfoDB.USERTABLE, mContext.MODE_PRIVATE)
                .getString(UserInfoDB.USERID, "");
    }

    @Override
    public int getCount() {
        return beanList.size();
    }

    @Override
    public Object getItem(int position) {
        return beanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.search_friend_item, null);
            viewHolder.search_avatar = (CircleView) convertView.findViewById(R.id.search_avatar);
            viewHolder.search_name = (TextView) convertView.findViewById(R.id.search_name);
            viewHolder.search_school = (TextView) convertView.findViewById(R.id.search_school);
            viewHolder.search_number = (TextView) convertView.findViewById(R.id.search_number);
            viewHolder.btn_status = (TextView) convertView.findViewById(R.id.btn_status);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final SearchInfo.RootBean bean = beanList.get(position);
        if (index == position) {
            viewHolder.btn_status.setText("申请中");
            viewHolder.btn_status.setBackground(mContext.getResources().getDrawable(R.drawable.selector_btn_gray));
        }
        if (type.equals("nickName")) {
            int changeTextColor;
            ForegroundColorSpan redSpan = new ForegroundColorSpan(mContext.getResources().getColor(R.color.red_fd3c49));

            SpannableStringBuilder builder = new SpannableStringBuilder(bean.getUserName());
            changeTextColor = bean.getUserName().indexOf(mSearchText);
            if (changeTextColor != -1) {
                builder.setSpan(redSpan, changeTextColor, changeTextColor
                                + mSearchText.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                viewHolder.search_name.setText(builder);
            } else {
                viewHolder.search_name.setText(bean.getUserName());
            }
            String phoneNumber = bean.getUserPhone().substring(0, 2) + "*******" + bean.getUserPhone().substring(9);
            viewHolder.search_number.setText(phoneNumber);
        } else if (type.equals("phoneNumber")) {
            int changeTextColor;
            changeTextColor = bean.getUserPhone().indexOf(mSearchText);
            StringBuilder sb = new StringBuilder();
            for (int k = 0; k < 11 - mSearchText.length(); k++) {
                sb.append("*");
            }
            String str = mSearchText + sb;
            ForegroundColorSpan redSpans = new ForegroundColorSpan(mContext.getResources().getColor(R.color.red_fd3c49));

            SpannableStringBuilder builders = new SpannableStringBuilder(str);

            if (changeTextColor != -1) {
                builders.setSpan(redSpans, changeTextColor, changeTextColor
                                + mSearchText.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                viewHolder.search_number.setText(builders);
            } else {
                viewHolder.search_number.setText(bean.getUserPhone());
            }
            viewHolder.search_name.setText(bean.getUserName());
        } else {

            String phoneNumber = bean.getUserPhone().substring(0, 2) + "*******" + bean.getUserPhone().substring(9);
            viewHolder.search_number.setText(phoneNumber);
            viewHolder.search_name.setText(bean.getUserName());
        }
        Glide.with(mContext)
                .load(MyConfig.PIC_AVATAR + bean.getUserPhotoThums())
                .error(R.drawable.default_useravatar)
                .into(viewHolder.search_avatar);
        viewHolder.search_school.setText("(" + bean.getSchool_name() + ")");
        viewHolder.btn_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.btn_status.getText().equals("+好友")) {
                    dialog = new Dialog(mContext, R.style.alertDialog);
                    LayoutInflater inflater = LayoutInflater.from(mContext);
                    View viewDialog = inflater.inflate(R.layout.add_friend_layout, null);
                    dialog.setContentView(viewDialog);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    dialog.setContentView(viewDialog, params);
                    CircleView iv_avatar = (CircleView) viewDialog.findViewById(R.id.iv_avatar);
                    Glide.with(mContext).load(MyConfig.PIC_AVATAR + bean.getUserPhotoThums()).error(R.drawable.default_useravatar).into(iv_avatar);
                    TextView tv_name = (TextView) viewDialog.findViewById(R.id.tv_name);
                    tv_name.setText(bean.getUserName());
                    final EditText ed_description = (EditText) viewDialog.findViewById(R.id.ed_description);
                    TextView btn_ok = (TextView) viewDialog.findViewById(R.id.btn_ok);
                    TextView btn_cancel = (TextView) viewDialog.findViewById(R.id.btn_cancel);
                    btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ed_name = ed_description.getText().toString().trim();
                            dialog.dismiss();
                            sendRequest(ed_name, position);

                        }
                    });
                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (null != dialog) {
                                dialog.dismiss();
                            }
                        }
                    });
                    dialog.show();
                }


            }
        });
        return convertView;
    }

    private void sendRequest(final String msg, final int position) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MyConfig.ADD_FRIEND, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtils.d("response", response);
                Message message = Message.obtain();
                message.arg1 = 1003;
                message.arg2 = position;
                search_friend_handler.sendMessage(message);
                ToastUtil.showToast(mContext, "请求发送成功");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                ToastUtil.showToast(mContext, "请求失败，稍后再试");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("uid", uid);
                map.put("to_uid", beanList.get(position).getUserId());
                map.put("msg", msg);
                return map;
            }
        };
        MyApplication.getRequestQueue().add(stringRequest);
    }

    private static class ViewHolder {
        CircleView search_avatar;
        TextView search_name;
        TextView search_school;
        TextView search_number;
        TextView btn_status;

    }
}
