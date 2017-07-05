package com.loosoo100.campus100.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.anyevent.MEventCampusContactsMyFriend;
import com.loosoo100.campus100.beans.CampusContactsLoveInfo;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.view.CircleView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 
 * @author yang 校园圈我的友友请求适配器
 */
public class CampusContactsMyFriendRequestAdapter extends BaseAdapter {

	private List<CampusContactsLoveInfo> list;
	private LayoutInflater inflater;
	private Activity activity;

	private String uid = "";

	public CampusContactsMyFriendRequestAdapter(Context context,
			List<CampusContactsLoveInfo> list) {
		this.list = list;
		activity = (Activity) context;

		inflater = LayoutInflater.from(context);
		uid = activity.getSharedPreferences(UserInfoDB.USERTABLE,
				activity.MODE_PRIVATE).getString(UserInfoDB.USERID, "");
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
			convertView = inflater.inflate(
					R.layout.item_campus_contacts_myfriend_request, null);
			viewHolder.tv_name = (TextView) convertView
					.findViewById(R.id.tv_name);
			viewHolder.tv_school = (TextView) convertView
					.findViewById(R.id.tv_school);
			viewHolder.tv_request = (TextView) convertView
					.findViewById(R.id.tv_request);
			viewHolder.btn_agree = (Button) convertView
					.findViewById(R.id.btn_agree);
			viewHolder.btn_against = (Button) convertView
					.findViewById(R.id.btn_against);
			viewHolder.iv_sex = (ImageView) convertView
					.findViewById(R.id.iv_sex);
			viewHolder.cv_picture = (CircleView) convertView
					.findViewById(R.id.cv_picture);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tv_name.setText(list.get(position).getName());
		viewHolder.tv_school.setText(list.get(position).getSchool());

		if (list.get(position).getSex().equals("1")) {
			viewHolder.iv_sex.setImageDrawable(activity.getResources()
					.getDrawable(R.drawable.icon_female_picture));
		} else {
			viewHolder.iv_sex.setImageDrawable(activity.getResources()
					.getDrawable(R.drawable.icon_male_picture));
		}

		if (list.get(position).getRequestType().equals("0")) {
			viewHolder.tv_request.setText("请求和您互换手机号码");
		} else {
			viewHolder.tv_request.setText("请求和您互换QQ");
		}

		if (!list.get(position).getHeadShot().equals("")
				&& !list.get(position).getHeadShot().equals("null")) {
			// 加载图片
			Glide.with(activity).load(list.get(position).getHeadShot())
					.into(viewHolder.cv_picture);
		} else {
			Glide.with(activity).load(R.drawable.imgloading).asBitmap()
					.into(viewHolder.cv_picture);
		}

		viewHolder.btn_agree.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new Thread() {
					public void run() {
						postAgree(
								MyConfig.URL_POST_CAMPUS_FRIEND_REQUEST_AGREE,
								list.get(position).getChangeId(),
								list.get(position).getRequestType(), position);
					};
				}.start();
			}
		});

		viewHolder.btn_against.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new Thread() {
					public void run() {
						postNoAgree(
								MyConfig.URL_POST_CAMPUS_FRIEND_REQUEST_NOAGREE,
								list.get(position).getChangeId(), position);
					};
				}.start();
			}
		});

		return convertView;
	}

	class ViewHolder {
		private TextView tv_name, tv_school, tv_request;
		private ImageView iv_sex;
		private CircleView cv_picture;
		private Button btn_agree, btn_against;
	}

	/**
	 * 同意
	 * 
	 * @param uploadHost
	 */
	private void postAgree(String uploadHost, String id, String type,
			final int position) {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("id", id);
		params.addBodyParameter("uid", uid);
		params.addBodyParameter("type", type);
		httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						LogUtils.d("responseInfo",responseInfo.result);
						String result = "";
						try {
							JSONObject jsonObject = new JSONObject(
									responseInfo.result);
							result = jsonObject.getString("status");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (result.equals("-3")) {
							ToastUtil.showToast(activity,"您还未填写QQ号");
						} else if (result.equals("1")) {
							// 通知我的友友页面刷新
							EventBus.getDefault().post(
									new MEventCampusContactsMyFriend(true));
							ToastUtil.showToast(activity,"已同意");
							list.remove(position);
							CampusContactsMyFriendRequestAdapter.this
									.notifyDataSetChanged();
						} else {
							ToastUtil.showToast(activity,"操作失败");
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.d("error",arg1.toString());
						ToastUtil.showToast(activity,"操作失败");
					}
				});
	}

	/**
	 * 拒绝
	 * 
	 * @param uploadHost
	 */
	private void postNoAgree(String uploadHost, String id, final int position) {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("id", id);
		httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						LogUtils.d("responseInfo",responseInfo.result);
						String result = "";
						try {
							JSONObject jsonObject = new JSONObject(
									responseInfo.result);
							result = jsonObject.getString("status");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (result.equals("1")) {
							ToastUtil.showToast(activity,"已拒绝");
							list.remove(position);
							CampusContactsMyFriendRequestAdapter.this
									.notifyDataSetChanged();
						} else {
							ToastUtil.showToast(activity,"操作失败");
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.d("error",arg1.toString());
						ToastUtil.showToast(activity,"操作失败");
					}
				});
	}
}
