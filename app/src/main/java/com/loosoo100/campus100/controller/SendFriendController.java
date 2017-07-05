package com.loosoo100.campus100.controller;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.loosoo100.campus100.R;
import com.loosoo100.campus100.activities.ArticleActivity;
import com.loosoo100.campus100.activities.GiftActivity;
import com.loosoo100.campus100.activities.GiftGoodsDetailActivity;
import com.loosoo100.campus100.adapters.GiftHotAdapter;
import com.loosoo100.campus100.adapters.HListViewAdapter;
import com.loosoo100.campus100.adapters.HListViewDownAdapter;
import com.loosoo100.campus100.beans.GoodsInfo;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.MyHorizontalListViewIntercept;

public class SendFriendController extends BaseController implements
		OnClickListener {

	private LayoutInflater inflater;
	private MyHorizontalListViewIntercept gallery; // 热门购水平滑动图片
	private MyHorizontalListViewIntercept gallery_down; // 打造专场水平滑动图片
	private ListView lv_gift_hot; // 热门推荐列表

	private GiftActivity activity;

	private List<GoodsInfo> goodsInfoDataLabel = new ArrayList<GoodsInfo>();
	private List<GoodsInfo> goodsInfoData = new ArrayList<GoodsInfo>();

	private RelativeLayout rl_pb_friend;

	// 收到通知后处理业务逻辑
	private Handler handlerHot = new Handler() {

		public void handleMessage(android.os.Message msg) {
			rl_pb_friend.setVisibility(View.GONE);
			// 初始化gallery
			gallery.setAdapter(new HListViewAdapter(activity, goodsInfoData));
			gallery.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent intent = new Intent(activity,
							GiftGoodsDetailActivity.class);
					intent.putExtra("position", position);
					activity.startActivity(intent);
				}
			});

			// 为了防止scrollview一开始时不显示顶部位置
			gallery.setFocusable(true);
			gallery.setFocusableInTouchMode(true);
			gallery.requestFocus();

			gallery_down.setAdapter(new HListViewDownAdapter(activity,
					goodsInfoData));
			gallery_down.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent intent = new Intent(activity,
							GiftGoodsDetailActivity.class);
					intent.putExtra("position", position);
					activity.startActivity(intent);
				}
			});

			lv_gift_hot.setAdapter(new GiftHotAdapter(activity,
					goodsInfoDataLabel));
			lv_gift_hot.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent intent = new Intent(activity,ArticleActivity.class);
					activity.startActivity(intent);
				}
			});
			MyUtils.setListViewHeight(lv_gift_hot, 0);
		}
	};

	public SendFriendController(Context context) {
		super(context);

		activity = (GiftActivity) context;
		initView();
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				goodsInfoDataLabel = GetData.hotLabelData(activity);
//				// 获取商品信息
//				goodsInfoData = GetData.goodsInfoData(activity);
//				handlerHot.sendEmptyMessage(0);
//
//			}
//		}).start();
	}

	@Override
	protected View initView(Context context) {
		inflater = LayoutInflater.from(context);
		return inflater.inflate(R.layout.controller_send_friend, null);
	}

	/**
	 * 初始化控件
	 */
	public void initView() {
		gallery = (MyHorizontalListViewIntercept) mRootView.findViewById(R.id.gallery);
		gallery_down = (MyHorizontalListViewIntercept) mRootView
				.findViewById(R.id.gallery_down);
		lv_gift_hot = (ListView) mRootView.findViewById(R.id.lv_gift_hot);
		rl_pb_friend = (RelativeLayout) mRootView
				.findViewById(R.id.rl_pb_friend);
		rl_pb_friend.setVisibility(View.VISIBLE);

	}

	@Override
	public void onClick(View v) {

	}

}
