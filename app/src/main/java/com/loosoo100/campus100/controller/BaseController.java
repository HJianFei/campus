package com.loosoo100.campus100.controller;

import android.content.Context;
import android.view.View;

public abstract class BaseController{

	protected View mRootView;
	protected Context mContext;
	
	public BaseController(Context context) {
		mContext=context;
		mRootView=initView(mContext);
	}
	public View getmRootView(){
		return mRootView;
	};
	protected abstract View initView(Context context);
	
	public void initData() {
		
	}
	
}
