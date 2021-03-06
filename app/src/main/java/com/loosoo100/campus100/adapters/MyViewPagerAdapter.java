package com.loosoo100.campus100.adapters;

import java.util.List;

import com.loosoo100.campus100.controller.BaseController;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * 
 * @author yang
 *	ViewPager适配器
 */
public class MyViewPagerAdapter extends PagerAdapter {
	
	private List<BaseController> list;

	public MyViewPagerAdapter(List<BaseController> list){
		this.list=list;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public void destroyItem(ViewGroup container, int position,
			Object object) {
		container.removeView(list.get(position).getmRootView());
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = list.get(position).getmRootView();
		container.addView(view);
		return view;
	}
}
