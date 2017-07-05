package com.loosoo100.campus100.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.activities.OverseasActivity;
import com.loosoo100.campus100.beans.OverseasInfo;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 
 * @author yang 海归学校girdview适配器
 */
public class OverseasGridviewAdapter extends BaseAdapter {

	private List<OverseasInfo> list;
	private LayoutInflater inflater;

	private OverseasActivity activity;
	private int mPosition;
	private String uid = "";

	public OverseasGridviewAdapter(Context context, List<OverseasInfo> list,
			String uid) {
		this.list = list;
		this.uid = uid;
		inflater = LayoutInflater.from(context);
		activity = (OverseasActivity) context;
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
			convertView = inflater.inflate(R.layout.item_overseas_school, null);
			viewHolder.iv_school = (ImageView) convertView
					.findViewById(R.id.iv_school);
			viewHolder.ib_collect = (ImageButton) convertView
					.findViewById(R.id.ib_collect);
			viewHolder.tv_schoolName = (TextView) convertView
					.findViewById(R.id.tv_schoolName);
			viewHolder.tv_location = (TextView) convertView
					.findViewById(R.id.tv_location);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tv_schoolName.setText(list.get(position).getSchoolName());
		viewHolder.tv_location.setText(list.get(position).getLocation());
		Glide.with(activity).load(list.get(position).getPicUrl())
				.override(400, 400).placeholder(R.drawable.imgloading)
				.into(viewHolder.iv_school);
		viewHolder.ib_collect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPosition = position;
				if (list.get(position).getStatus() == 0) {
					new Thread() {
						public void run() {
							postCollect(MyConfig.URL_POST_OVERSEAS_COLLECT
									+ list.get(mPosition).getSchoolID()
									+ "&uid=" + uid);
						};
					}.start();
				} else {
					new Thread() {
						public void run() {
							postCollectCancel(MyConfig.URL_POST_OVERSEAS_COLLECT_CANCEL
									+ list.get(mPosition).getSchoolID()
									+ "&uid=" + uid);
						};
					}.start();
				}
				OverseasGridviewAdapter.this.notifyDataSetChanged();
			}
		});

		if (list.get(position).getStatus() == 1) {
			viewHolder.ib_collect.setBackground(activity.getResources()
					.getDrawable(R.drawable.icon_collect_red));
		} else {
			viewHolder.ib_collect.setBackground(activity.getResources()
					.getDrawable(R.drawable.icon_collect));
		}

		return convertView;
	}

	class ViewHolder {
		private ImageView iv_school;
		private ImageButton ib_collect;
		private TextView tv_schoolName, tv_location;
	}

	/**
	 * 提交数据,收藏学校
	 */
	private void postCollect(String uploadHost) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpRequest.HttpMethod.GET, uploadHost,
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
							list.get(mPosition).setStatus(1);
							OverseasGridviewAdapter.this.notifyDataSetChanged();
						} else if (result.equals("0")) {
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
	 * 提交数据,收藏学校
	 */
	private void postCollectCancel(String uploadHost) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpRequest.HttpMethod.GET, uploadHost,
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
					list.get(mPosition).setStatus(0);
					OverseasGridviewAdapter.this.notifyDataSetChanged();
				} else if (result.equals("0")) {
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
