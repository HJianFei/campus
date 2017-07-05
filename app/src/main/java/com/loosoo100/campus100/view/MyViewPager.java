package com.loosoo100.campus100.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 禁止滑动的viewpager
 * @author yang
 *
 */
public class MyViewPager extends ViewPager {  
	  
    private boolean scrollble = false;  
  
    public MyViewPager(Context context) {  
        super(context);  
    }  
  
    public MyViewPager(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
  
  
    @Override  
    public boolean onTouchEvent(MotionEvent ev) {  
        return false;  
    }  
  
  
    public boolean isScrollble() {  
        return scrollble;  
    }  
}  