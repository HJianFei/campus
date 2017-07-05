package com.loosoo100.campus100.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 禁止滑动的viewpager
 */
public class NoneScrollViewPager extends ViewPager {

		private boolean isCanScroll = false;

		public NoneScrollViewPager(Context context) {
			super(context);
		}

		public NoneScrollViewPager(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		public void setScanScroll(boolean isCanScroll) {
			this.isCanScroll = isCanScroll;
		}

		@Override
		public void scrollTo(int x, int y) {
			super.scrollTo(x, y);
		}

		@Override
		public boolean onTouchEvent(MotionEvent arg0) {
			// TODO Auto-generated method stub
			if (isCanScroll) {
				return super.onTouchEvent(arg0);
			} else {
				return false;
			}

		}

		@Override
		public void setCurrentItem(int item, boolean smoothScroll) {
			// TODO Auto-generated method stub
			super.setCurrentItem(item, smoothScroll);
		}

		@Override
		public void setCurrentItem(int item) {
			// TODO Auto-generated method stub
			super.setCurrentItem(item);
		}

		@Override
		public boolean onInterceptTouchEvent(MotionEvent arg0) {
			// TODO Auto-generated method stub
			if (isCanScroll) {
				return super.onInterceptTouchEvent(arg0);
			} else {
				return false;
			}

		}
}
