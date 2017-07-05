package com.loosoo100.campus100.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 添加多个顶部view
 * @author Administrator
 *
 */
public class MyListView extends ListView {

	public MyListView(Context context) {
		super(context);
		initHeaderView(context);
	}

	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHeaderView(context);

	}

	public MyListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initHeaderView(context);

	}

	private void initHeaderView(Context context) {
//		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		View one = inflater.inflate(R.layout.campus_contacts_top01, null);
//		View two = inflater.inflate(R.layout.campus_contacts_top02, null);
//		
//		addHeaderView(one);
//		addHeaderView(two);

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		//控制第一个头部布局的位置
//		one.setPadding(0, -1 * one.getHeight()/2, 0, 0);

	}
}