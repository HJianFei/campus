package com.loosoo100.campus100.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.activities.BasicInfoActivity;
import com.loosoo100.campus100.beans.CommunityBasicInfo;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.view.CircleView;
import com.loosoo100.campus100.view.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * @author yang 社团入驻适配器
 */
public class CommunityJoinAdapter extends BaseAdapter {

    private List<CommunityBasicInfo> list;
    private LayoutInflater inflater;
    private int mPosition;
    private Activity activity;
    private String uid = "";
    private String sid = ""; // 社团学校
    private String mysid = ""; // 所在学校

    private Handler handlerPerfect = new Handler() {
        public void handleMessage(android.os.Message msg) {
            new Thread() {
                public void run() {
                    postData(MyConfig.URL_POST_COMMUNITY_JOIN);
                }

                ;
            }.start();
        }

        ;
    };
    private Handler handlerNoPerfect = new Handler() {
        public void handleMessage(android.os.Message msg) {
            CustomDialog.Builder builder = new CustomDialog.Builder(activity);
            builder.setMessage("请先完善个人资料");
            builder.setPositiveButton("是",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(activity,
                                    BasicInfoActivity.class);
                            activity.startActivity(intent);
                        }
                    });
            builder.setNegativeButton("否", null);
            builder.create().show();
        }

        ;
    };

    public CommunityJoinAdapter(Context context, List<CommunityBasicInfo> list) {
        this.list = list;
        activity = (Activity) context;

        inflater = LayoutInflater.from(context);

        uid = context.getSharedPreferences(UserInfoDB.USERTABLE,
                context.MODE_PRIVATE).getString(UserInfoDB.USERID, "");
        sid = activity.getSharedPreferences(UserInfoDB.USERTABLE,
                activity.MODE_PRIVATE).getString(UserInfoDB.SCHOOL_ID_COMM, "");
        mysid = context.getSharedPreferences(UserInfoDB.USERTABLE,
                context.MODE_PRIVATE).getString(UserInfoDB.SCHOOL_ID, "");

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
            convertView = inflater.inflate(R.layout.item_community_join, null);
            viewHolder.tv_communityName = (TextView) convertView
                    .findViewById(R.id.tv_communityName);
            viewHolder.tv_id = (TextView) convertView.findViewById(R.id.tv_id);
            viewHolder.tv_slogan = (TextView) convertView
                    .findViewById(R.id.tv_slogan);
            viewHolder.tv_name = (TextView) convertView
                    .findViewById(R.id.tv_name);
            viewHolder.tv_time = (TextView) convertView
                    .findViewById(R.id.tv_time);
            viewHolder.btn_join = (Button) convertView
                    .findViewById(R.id.btn_join);
            viewHolder.iv_picture = (CircleView) convertView
                    .findViewById(R.id.iv_picture);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_communityName.setText(list.get(position)
                .getCommunityName());
        viewHolder.tv_id.setText("" + list.get(position).getId());
        viewHolder.tv_slogan.setText(list.get(position).getSlogan());
        viewHolder.tv_name.setText(list.get(position).getName());
        viewHolder.tv_time.setText(list.get(position).getTime());

        if (sid.equals(mysid)) {
            if (list.get(position).getStatus().equals("1")) {
                viewHolder.btn_join.setBackgroundDrawable(activity
                        .getResources()
                        .getDrawable(R.drawable.selector_btn_red));
                viewHolder.btn_join.setText("加入");
            } else if (list.get(position).getStatus().equals("0")) {
                viewHolder.btn_join.setBackgroundDrawable(activity
                        .getResources().getDrawable(
                                R.drawable.shape_gray_b3b3b3));
                viewHolder.btn_join.setText("申请中");
            } else if (list.get(position).getStatus().equals("-1")) {
                viewHolder.btn_join.setBackgroundDrawable(activity
                        .getResources().getDrawable(
                                R.drawable.shape_gray_b3b3b3));
                viewHolder.btn_join.setText("已加入");
            }
        } else {
            viewHolder.btn_join.setVisibility(View.GONE);
        }

        // 加载图片
        Glide.with(activity).load(list.get(position).getHeadthumb())
                .into(viewHolder.iv_picture);

        // 加入按钮
        viewHolder.btn_join.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mPosition = position;
                if (list.get(position).getStatus().equals("1")) {
                    new Thread() {
                        public void run() {
                            postIsDataPerfect(MyConfig.URL_POST_IS_INFO_PERFECT);
                        }

                        ;
                    }.start();
                }
            }
        });
        return convertView;
    }

    class ViewHolder {
        private TextView tv_communityName, tv_id, tv_slogan, tv_name, tv_time;
        private Button btn_join;
        private CircleView iv_picture;
    }

    /**
     * 提交数据
     */
    private void postData(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("userid", uid);
        params.addBodyParameter("comnid", list.get(mPosition).getId() + "");
        params.addBodyParameter("sid", mysid);
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo", responseInfo.result);
                        String result = "";
                        try {
                            JSONObject jsonObject = new JSONObject(
                                    responseInfo.result);
                            result = jsonObject.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result.equals("1")) {
                            ToastUtil.showToast(activity, "提交成功,请耐心等待审核");
                            list.get(mPosition).setStatus("0");
                            CommunityJoinAdapter.this.notifyDataSetChanged();
                        } else {
                            ToastUtil.showToast(activity, "提交失败");
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        ToastUtil.showToast(activity, "提交失败");
                    }
                });
    }

    /**
     * 是否完善资料
     */
    private void postIsDataPerfect(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("type", "0");
        params.addBodyParameter("uid", uid);
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo", responseInfo.result);
                        String result = "";
                        try {
                            JSONObject jsonObject = new JSONObject(
                                    responseInfo.result);
                            result = jsonObject.getString("status1");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result.equals("1")) {
                            handlerPerfect.sendEmptyMessage(0);
                        } else {
                            handlerNoPerfect.sendEmptyMessage(0);
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        ToastUtil.showToast(activity, "操作失败");
                    }
                });
    }
}
