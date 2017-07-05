package com.loosoo100.campus100.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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
import com.loosoo100.campus100.anyevent.MEventComMem;
import com.loosoo100.campus100.beans.CommunityMemberInfo;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.view.CircleView;
import com.loosoo100.campus100.view.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 
 * @author yang 社团成员管理适配器
 */
public class CommunityMemberManageAdapter extends BaseAdapter {

	private List<CommunityMemberInfo> list;
	private LayoutInflater inflater;

	private Activity activity;

	private int mPosition;
	private String cid = "";
	private String uid = "";
	private String sid = "";

	public CommunityMemberManageAdapter(Context context,
			List<CommunityMemberInfo> list, String cid, String uid, String sid) {
		this.list = list;
		this.cid = cid;
		this.uid = uid;
		this.sid = sid;
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
			convertView = inflater.inflate(
					R.layout.item_community_member_manage, null);
			viewHolder.tv_nickName = (TextView) convertView
					.findViewById(R.id.tv_nickName);
			viewHolder.tv_name = (TextView) convertView
					.findViewById(R.id.tv_name);
//			viewHolder.tv_qq = (TextView) convertView.findViewById(R.id.tv_qq);
			viewHolder.btn_remove = (Button) convertView
					.findViewById(R.id.btn_remove);
			viewHolder.cv_headShot = (CircleView) convertView
					.findViewById(R.id.cv_headShot);
			viewHolder.iv_sex = (ImageView) convertView
					.findViewById(R.id.iv_sex);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (uid.equals(list.get(position).getUserId())) {
			viewHolder.btn_remove.setVisibility(View.GONE);
		} else {
			viewHolder.btn_remove.setVisibility(View.VISIBLE);
		}
		viewHolder.tv_nickName.setText(list.get(position).getNickName());
		viewHolder.tv_name.setText("(" + list.get(position).getName() + ")");
		viewHolder.tv_nickName.setText(list.get(position).getNickName());
		if (list.get(position).getName().equals("")) {
			viewHolder.tv_name.setVisibility(View.GONE);
		}else {
			viewHolder.tv_name.setVisibility(View.VISIBLE);
		}
		
		if (!list.get(position).getHeadShotString().equals("")) {
			Glide.with(activity).load(list.get(position).getHeadShotString())
					.override(200, 200).into(viewHolder.cv_headShot);
		} else {
			Glide.with(activity).load("").placeholder(R.drawable.imgloading)
					.override(200, 200).into(viewHolder.cv_headShot);
		}
		if (list.get(position).getSex() == 0) {
			viewHolder.iv_sex.setImageDrawable(activity.getResources()
					.getDrawable(R.drawable.icon_male_picture));
		} else {
			viewHolder.iv_sex.setImageDrawable(activity.getResources()
					.getDrawable(R.drawable.icon_female_picture));
		}

		viewHolder.btn_remove.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPosition = position;
				CustomDialog.Builder builder = new CustomDialog.Builder(
						activity);
				builder.setMessage("是否确认移除");
				builder.setPositiveButton("是",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// CommunityMemberManageActivity.progress_update
								// .setVisibility(View.VISIBLE);
								EventBus.getDefault()
										.post(new MEventComMem(-1));
								new Thread() {
									public void run() {
										postData(MyConfig.URL_POST_COMMUNITY_REMOVE_MEMBER);
									};
								}.start();
							}
						});
				builder.setNegativeButton("否", null);
				builder.create().show();

			}
		});
		return convertView;
	}

	class ViewHolder {
		private TextView tv_nickName, tv_name;
		private Button btn_remove;
		private CircleView cv_headShot;
		private ImageView iv_sex;
	}

	/**
	 * 提交数据
	 */
	private void postData(String uploadHost) {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("cid", cid);
		params.addBodyParameter("uid", list.get(mPosition).getUserId() + "");
		params.addBodyParameter("shopid", sid);
		httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						LogUtils.d("responseInfo",responseInfo.result);
						// CommunityMemberManageActivity.progress_update
						// .setVisibility(View.GONE);
						EventBus.getDefault().post(new MEventComMem(-2));
						String result = "";
						try {
							JSONObject jsonObject = new JSONObject(
									responseInfo.result);
							result = jsonObject.getString("status");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (result.equals("1")) {
							EventBus.getDefault().post(
									new MEventComMem(mPosition));
							// list.remove(mPosition);
							// CommunityMemberManageAdapter.this
							// .notifyDataSetChanged();
							ToastUtil.showToast(activity,"移除成功");
						} else {
							ToastUtil.showToast(activity,"移除失败");
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.d("error",arg1.toString());
						EventBus.getDefault().post(new MEventComMem(-2));
						// CommunityMemberManageActivity.progress_update
						// .setVisibility(View.GONE);
						ToastUtil.showToast(activity,"移除失败");
					}
				});
	}

}
