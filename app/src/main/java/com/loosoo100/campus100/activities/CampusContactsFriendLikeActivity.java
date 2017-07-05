//package com.loosoo100.campus100.activities;
//
//import java.util.List;
//
//import android.app.Activity;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//
//import com.lidroid.xutils.ViewUtils;
//import com.lidroid.xutils.view.annotation.ViewInject;
//import com.loosoo100.campus100.R;
//import com.loosoo100.campus100.beans.CampusContactsInfo;
//import com.loosoo100.campus100.utils.MyUtils;
//import com.loosoo100.campus100.view.CircleView;
//
///**
// * 
// * @author yang 校园圈点赞朋友的activity
// */
//public class CampusContactsFriendLikeActivity extends Activity implements
//		OnClickListener {
//	@ViewInject(R.id.iv_empty)
//	private ImageView iv_empty; // 空view
//	@ViewInject(R.id.rl_back)
//	private RelativeLayout rl_back;
//	@ViewInject(R.id.gv_headShot)
//	private GridView gv_headShot; // 点赞的头像的表格
//
//	private MyHeadShotAdapter myHeadShotAdapter;
//	private List<Bitmap> headShotList;
//	private List<CampusContactsInfo> list;
//	private int index;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView();
//		ViewUtils.inject(this);
//
//		MyUtils.setStatusBarHeight(this, iv_empty);
//		list = CampusContactsFriendActivity.list;
//		index = getIntent().getExtras().getInt("index");
//		initView();
//	}
//
//	private void initView() {
//		rl_back.setOnClickListener(this);
//
//		// 点赞头像
////		headShotList = list.get(index).getLikeListBitmaps();
//		myHeadShotAdapter = new MyHeadShotAdapter(this, headShotList);
//		gv_headShot.setAdapter(myHeadShotAdapter);
//
//	}
//
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.rl_back:
//			this.finish();
//			break;
//
//		}
//	}
//
//	/**
//	 * 点赞的头像适配器
//	 * 
//	 * @author yang
//	 * 
//	 */
//	class MyHeadShotAdapter extends BaseAdapter {
//		private Context context;
//		private List<Bitmap> list;
//		private LayoutInflater inflater;
//
//		public MyHeadShotAdapter(Context context, List<Bitmap> list) {
//			this.context = context;
//			this.list = list;
//
//			inflater = LayoutInflater.from(context);
//		}
//
//		@Override
//		public int getCount() {
//			return list.size();
//		}
//
//		@Override
//		public Object getItem(int position) {
//			return list.get(position);
//		}
//
//		@Override
//		public long getItemId(int position) {
//			return position;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			ViewHolder viewHolder = null;
//			if (convertView == null) {
//				viewHolder = new ViewHolder();
//				convertView = inflater.inflate(
//						R.layout.item_contacts_gv_headshot_like, null);
//				viewHolder.cv_headShot_like = (CircleView) convertView
//						.findViewById(R.id.cv_headShot_like);
//				convertView.setTag(viewHolder);
//			} else {
//				viewHolder = (ViewHolder) convertView.getTag();
//			}
//			if (list.get(position) != null) {
//				viewHolder.cv_headShot_like.setImageBitmap(list.get(position));
//			}
//
//			return convertView;
//		}
//
//		class ViewHolder {
//			public CircleView cv_headShot_like;
//		}
//
//	}
//}
