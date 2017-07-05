package com.loosoo100.campus100.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ListView;

/**
 * 禁止滑动listview
 * @author Administrator
 *
 */
public class NoneScrollListView extends ListView implements OnTouchListener {

	public NoneScrollListView(Context context) {
		super(context);
	}

	public NoneScrollListView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public NoneScrollListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}
	
	 @Override
     public boolean onTouch(View view, MotionEvent event) {
         switch (event.getAction()) {
         case MotionEvent.ACTION_MOVE:
             return true;
         default:
             break;
         }
         return false;
     }
}