package com.loosoo100.campus100.adapters;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MessagePagerAdapter extends FragmentPagerAdapter {

	private List<Fragment> list;

	public MessagePagerAdapter(FragmentManager fm, List<Fragment> fragments) {
		super(fm);
		this.list =fragments;

	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Fragment getItem(int arg0) {
		return list.get(arg0);
	}

}
