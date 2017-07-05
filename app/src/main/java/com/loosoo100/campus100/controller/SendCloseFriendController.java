package com.loosoo100.campus100.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.loosoo100.campus100.R;

public class SendCloseFriendController extends BaseController implements
		OnClickListener {

	private LayoutInflater inflater;

	public SendCloseFriendController(Context context) {
		super(context);
	}

	@Override
	protected View initView(Context context) {
		inflater = LayoutInflater.from(context);
		return inflater.inflate(R.layout.controller_send_closefriend, null);
	}


	@Override
	public void onClick(View v) {
		
	}

}
