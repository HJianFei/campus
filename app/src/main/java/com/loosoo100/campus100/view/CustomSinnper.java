package com.loosoo100.campus100.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.loosoo100.campus100.R;

public class CustomSinnper extends Button {

	public PopupWindow popup = null;
	private CustomSinnper topButton;

	protected CornerListView mListView;

	private ArrowView arrow;

	private OnItemSeletedListener changListener;

	private Context mContext;

	private Animation showAnimation;
	private Animation dissAnimation;

	public boolean isShowPopup() {
		return popup.isShowing();
	}

	@SuppressLint({ "NewApi", "Recycle" })
	public CustomSinnper(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		topButton = this;
		initView(mContext);

	}

	@SuppressLint("NewApi")
	private void initView(final Context c) {
		arrow = new ArrowView(c, null, topButton);
		topButton.setCompoundDrawables(null, null, arrow.getDrawable(), null);

		topButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				initPopupWindow(c);
			}
		});

		mListView = new CornerListView(c);
		mListView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		mListView.setBackgroundResource(R.drawable.shape_bg_listview);
		mListView.setCacheColorHint(0x00000000);
		mListView.setPadding(5, 5, 5, 5);
		mListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Object obj = parent.getItemAtPosition(position);
				topButton.setText(obj.toString());
				dismiss();
				changListener.onItemSeleted(parent, view, position, id);
			}
		});
	}

	protected void initPopupWindow(Context context) {
		if (popup == null) {
			popup = new PopupWindow(mContext);
			popup.setWidth(topButton.getWidth());
			popup.setBackgroundDrawable(new ColorDrawable(0x00000000));
			popup.setFocusable(false);
			popup.setHeight(WindowManager.LayoutParams.FILL_PARENT);
			popup.setOutsideTouchable(false);
			popup.setContentView(mListView);
			popup.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
		}
		showPop();

	}

	protected void showPop() {
		if (!popup.isShowing()) {
			if (showAnimation == null) {
				showAnimation = new TranslateAnimation(
						Animation.RELATIVE_TO_SELF, 0.0f,
						Animation.RELATIVE_TO_SELF, 0.0f,
						Animation.RELATIVE_TO_SELF, -1.0f,
						Animation.RELATIVE_TO_SELF, 0.0f);
				showAnimation.setInterpolator(new AccelerateInterpolator());
				showAnimation.setDuration(100);

			}
			popup.getContentView().startAnimation(showAnimation);

			popup.showAsDropDown(topButton);
		}
	}

	public void dismiss() {

		if (popup.isShowing()) {
			if (dissAnimation == null) {
				dissAnimation = new TranslateAnimation(
						Animation.RELATIVE_TO_SELF, 0.0f,
						Animation.RELATIVE_TO_SELF, 0.0f,
						Animation.RELATIVE_TO_SELF, 0.0f,
						Animation.RELATIVE_TO_SELF, -1.0f);
				dissAnimation.setDuration(150);
				dissAnimation.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
					}

					@Override
					public void onAnimationEnd(Animation animation) {

						new Handler().post(new Runnable() {
							@Override
							public void run() {
								popup.dismiss();
							}
						});

					}
				});
			}
			popup.getContentView().startAnimation(dissAnimation);
		}
	}

	private void setTopText(ListAdapter adapter) {
		ListAdapter mAdapter = adapter;
		String text = "";
		if (mAdapter.getCount() <= 0) {
			text = "select";
			topButton.setText(text);
			return;
		} else if (topButton.getText().toString().equals("")) {
			text = (String) mAdapter.getItem(0);
			topButton.setText(text);
		}
		text = null;
	}

	public void setAdapter(ListAdapter adapter) {
		if (mListView == null) {
			throw new NullPointerException("Listview null");
		}
		mListView.setAdapter(adapter);
		setTopText(adapter);
	}

	public void setOnItemSeletedListener(OnItemSeletedListener listener) {
		this.changListener = listener;
	}

	public interface OnItemSeletedListener {

		abstract void onItemSeleted(AdapterView<?> parent, View view,
				int position, long id);

	}

	private final class CornerListView extends ListView {

		public CornerListView(Context context) {
			super(context);
		}

		public CornerListView(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
		}

		public CornerListView(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		@Override
		public boolean onInterceptTouchEvent(MotionEvent ev) {

			final int action = ev.getAction();
			switch (action) {
			case MotionEvent.ACTION_DOWN:
				int x = (int) ev.getX();
				int y = (int) ev.getY();
				final int itemNum = pointToPosition(x, y);

				if (itemNum == AbsListView.INVALID_POSITION) {
					break;
				} else {
					if (itemNum == 0) {
						setSelector(R.drawable.app_list_corner_round_top);
					} else if (itemNum == (getAdapter().getCount() - 1)) {
						setSelector(R.drawable.app_list_corner_round_bottom);
					} else {
						setSelector(R.drawable.app_list_corner_shape);
					}
				}
				break;
			}
			return super.onInterceptTouchEvent(ev);
		}

	}

	@SuppressLint("WrongCall")
	protected final class ArrowView extends View {

		private int width;
		private int height;
		protected ShapeDrawable shape;
		protected boolean isAngle;
		private Matrix matrix;

		public ArrowView(Context context, AttributeSet set, View v) {
			super(context, set);
			width = 30;
			height = 20;
			Path p = new Path();
			p.moveTo(0, 0);
			p.lineTo(width, 0);
			p.lineTo(width / 2, height);
			p.lineTo(0, 0);
			shape = new ShapeDrawable(new PathShape(p, width, height));
			shape.getPaint().setColor(Color.BLACK);
			shape.setBounds(0, 0, width, height);
			matrix = new Matrix();
		}

		public void setColor(int color) {
			shape.getPaint().setColor(color);
		}

		protected Drawable getDrawable() {

			Canvas canvas = new Canvas();
			shape.draw(canvas);
			this.onDraw(canvas);
			return shape;
		}

	}
}
