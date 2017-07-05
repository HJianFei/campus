//package com.loosoo100.campus100.fragments;
//
//import java.util.List;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ListView;
//
//import com.loosoo100.campus100.R;
//import com.loosoo100.campus100.adapters.GoodsListAdapter01;
//import com.loosoo100.campus100.beans.GoodsInfo;
//import com.loosoo100.campus100.utils.MyUtils;
//
///**
// * 
// * @author yang 商品列表fragment
// */
//public class GoodsFragment extends Fragment {
//
//	private Activity activity;
//
//	private ListView lv_goods;
//
//	private View view;
//	public static List<GoodsInfo> goodsInfoData;
//	private GoodsListAdapter01 goodsAdapter;
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		view = inflater.inflate(R.layout.fragment_goods, container, false);
//
//		activity = getActivity();
//		initView();
//		return view;
//	}
//
//	private void initView() {
//		lv_goods = (ListView) view.findViewById(R.id.lv_goods);
//		goodsAdapter = new GoodsListAdapter01(activity, goodsInfoData);
//		lv_goods.setAdapter(goodsAdapter);
//		MyUtils.setListViewHeight(lv_goods, 200);
//	}
//
//}
