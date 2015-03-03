package com.cmeiyuan.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

public class MenuScrollView extends FrameLayout {

	private static final float COEFFICIENT = 0.5f;
	private static final int GESTURE_SLIDE_NONE = 0;
	private static final int GESTURE_SLIDE_LEFT = 1;
	private static final int GESTURE_SLIDE_RIGHT = 2;

	// 显示菜单数量
	private float mShowMenuCount = 4f;
	// 是否正在被拖动
	private boolean mIsBeingDragged = false;
	// 上一次触摸的X点
	private float mLastTouchX;
	// 激知点
	private int mActivePointerId = -1;
	// 复位动画
	private ValueAnimator mResumeAnimator;
	// 位移动画时间
	private long mScrollDuration = 200;
	// 位移动画
	private ValueAnimator mScrollAnimator;
	// 插值器1
	private Interpolator mInterpolator1 = new AccelerateDecelerateInterpolator();
	// 插值器2
	private Interpolator mInterpolator2 = new LinearInterpolator();
	// 是否有弹性
	private boolean mElasticityEnable = false;

	protected int mSlideGesture = GESTURE_SLIDE_NONE;

	// 手势
	private GestureDetector mGestureDetector;
	// menu页监听器
	private OnPageChangeListener mListener;

	public MenuScrollView(Context context) {
		super(context);
		init(context);
	}

	public MenuScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public MenuScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public void setShowMenuCount(float count) {
		this.mShowMenuCount = count;
	}

	public float getShowMenuCount() {
		return mShowMenuCount;
	}

	public OnPageChangeListener getOnPageChangeListener() {
		return mListener;
	}

	public void setOnPageChangeListener(OnPageChangeListener l) {
		this.mListener = l;
	}

	private void init(Context context) {
		mResumeAnimator = new ValueAnimator();
		mResumeAnimator.setInterpolator(mInterpolator1);
		mResumeAnimator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				int value = (Integer) animation.getAnimatedValue();
				scrollTo(value, 0);
			}
		});

		mScrollAnimator = new ValueAnimator();
		mScrollAnimator.setInterpolator(mInterpolator2);
		mScrollAnimator.setDuration(mScrollDuration);
		mScrollAnimator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				int value = (Integer) animation.getAnimatedValue();
				scrollTo(value, 0);
			}
		});

		mGestureDetector = new GestureDetector(context, mGistureListener);
	}

	private SimpleOnGestureListener mGistureListener = new SimpleOnGestureListener() {

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {

			Log.d("MenuScrollView", "onFling:" + velocityX);

			if (velocityX > 0) {
				// mSlideGesture = GESTURE_SLIDE_RIGHT;
			} else if (velocityX < 0) {
				// mSlideGesture = GESTURE_SLIDE_LEFT;
			} else {
				// mSlideGesture = GESTURE_SLIDE_NONE;
			}
			return true;
		}
	};

	public void showPage(int index) {
		int curX = getScrollX();
		int endX = 0;
		if (index == 1) {
			endX = getNormalOverScrollX();
		}

		// int centerX = 0;
		// int width = getWidth();
		//
		// if(endX > curX){
		// centerX = endX + (int) (0.2f * width);
		// }else{
		// centerX = endX - (int) (0.2f * width);
		// }
		// mScrollAnimator.setIntValues(curX, centerX, endX);

		mScrollAnimator.setIntValues(curX, endX);
		mScrollAnimator.start();
	}

	public int getCurrentPage() {
		return getScrollX() / getWidth();
	}

	public int getMaxPage() {
		float showCount = mShowMenuCount;
		return (int) Math.ceil((double) getChildCount() / (double) showCount);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// mIsBeingDragged = false;
			mActivePointerId = event.getPointerId(0);
			mLastTouchX = event.getX();
		}
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			float eventX = event.getX();
			int distance = (int) (mLastTouchX - eventX);
			// 有一定移动距离才拦截move事件，否则不拦截，让子view处理
			if (Math.abs(distance) > 20) {
				mIsBeingDragged = true;
			}
		}
		return mIsBeingDragged;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mGestureDetector.onTouchEvent(event);

		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		// 第一个手指触摸屏幕时
		case MotionEvent.ACTION_DOWN:
			mActivePointerId = event.getPointerId(0);
			mLastTouchX = event.getX();
			break;
		// 另一个手指触摸时
		case MotionEvent.ACTION_POINTER_DOWN:
			int index = event.getActionIndex();
			mLastTouchX = event.getX(index);
			mActivePointerId = event.getPointerId(index);
			break;

		// 一个手指离开屏幕，但至少还有一个手指在触摸屏幕
		case MotionEvent.ACTION_POINTER_UP:
			onSecondaryPointerUp(event);
			if (mActivePointerId != -1) {
				mLastTouchX = event.getX(event
						.findPointerIndex(mActivePointerId));
			}
			break;
		// 手指在屏幕上移动时
		case MotionEvent.ACTION_MOVE:
			
			if(mElasticityEnable){
				int activePointerIndex = event.findPointerIndex(mActivePointerId);
				if (activePointerIndex == -1) {
					break;
				}
				float eventX = event.getX(activePointerIndex);
				int distance = (int) (mLastTouchX - eventX);
				mIsBeingDragged = true;
				scrollBy(calculateScrollX(distance), 0);
				mLastTouchX = eventX;
			}
			
			break;
		// 最后一个手指离开屏幕时
		case MotionEvent.ACTION_UP:
			mIsBeingDragged = false;
			mActivePointerId = -1;
			playResumeAnimation();
			break;
		}

		return true;
	}

	private void onSecondaryPointerUp(MotionEvent ev) {
		final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
		final int pointerId = ev.getPointerId(pointerIndex);
		if (pointerId == mActivePointerId) {
			final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
			mLastTouchX = (int) ev.getX(newPointerIndex);
			mActivePointerId = ev.getPointerId(newPointerIndex);
		}
	}

	protected int getCloseToPage() {

		int scrollWidth = getNormalOverScrollX();

		if (scrollWidth > scrollWidth / 2) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);

		if (l > oldl) {
			mSlideGesture = GESTURE_SLIDE_LEFT;
		} else {
			mSlideGesture = GESTURE_SLIDE_RIGHT;
		}

	}

	private void playResumeAnimation() {
		ValueAnimator animator = mResumeAnimator;
		int scrollX = getScrollX();
		int startX = 0;
		int endX = getNormalOverScrollX();
		if (scrollX < startX) {
			animator.setIntValues(scrollX, startX);
			animator.setDuration(200);
			animator.start();
		} else if (scrollX > startX && scrollX < endX) {
			if (mSlideGesture == GESTURE_SLIDE_NONE) {
				showPage(getCloseToPage());
			} else {
				int index = 0;
				if (mSlideGesture == GESTURE_SLIDE_LEFT) {
					index = getCurrentPage() + 1;
					int maxPage = getMaxPage() - 1;
					index = index > maxPage ? maxPage : index;
				} else {
					index = getCurrentPage() - 1;
					index = index < 0 ? 0 : index;
				}
				showPage(index);
				if (getOnPageChangeListener() != null) {
					getOnPageChangeListener().onPageChanged(index);
				}
				mSlideGesture = GESTURE_SLIDE_NONE;
			}
		} else if (scrollX > endX) {
			animator.setIntValues(scrollX, endX);
			animator.setDuration(200);
			animator.start();
		}
	}

	private int calculateScrollX(int distance) {
		int result = distance;
		int overScroll = 0;

		// 朝左滑
		if (distance < 0) {
			int scrollLeft = getOverScrollLeft();
			if (scrollLeft < 0) {
				overScroll = Math.abs(scrollLeft);
			}
		}

		// 朝右滑
		if (distance > 0) {
			int scrollRight = getOverScrollRight();
			if (scrollRight > 0) {
				overScroll = Math.abs(scrollRight);
			}
		}

		if (overScroll > 0 && overScroll < getWidth()) {
			float overScrollF = overScroll;
			float totalWidthF = getMeasuredWidth();
			float scrollPercent = overScrollF / totalWidthF;
			float percent = 1 - scrollPercent;
			result = (int) (distance * percent * COEFFICIENT);
		}

		return result;
	}

	protected int getOverScrollLeft() {
		return getScrollX();
	}

	protected int getOverScrollRight() {
		return getScrollX() - getNormalOverScrollX();
	}

	protected int getNormalOverScrollX() {
		int result = getChildWidth() - getMeasuredWidth();
		return result < 0 ? 0 : result;
	}

	protected boolean overScrollLeft() {
		return getScrollX() < 0;
	}

	protected boolean overScrollRight() {
		return getScrollX() > getChildWidth() - getMeasuredWidth();
	}

	private int getChildWidth() {
		int childWidth = 0;
		int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			View child = getChildAt(i);
			if (child.getVisibility() == View.VISIBLE) {
				childWidth += child.getMeasuredWidth();
			}
		}
		return childWidth;
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);

		int childCount = getChildCount();
		float showMenuCount = mShowMenuCount;

		if (childCount == 0) {
			return;
		}

		int pLeft = getPaddingLeft();
		int pTop = getPaddingTop();
		int pRight = getPaddingRight();
		int pBottom = getPaddingBottom();

		int width = getMeasuredWidth();
		int height = getMeasuredHeight();

		int childWidth = (int) ((width - pLeft - pRight) / showMenuCount);

		for (int i = 0; i < childCount; i++) {
			View child = getChildAt(i);
			child.layout(pLeft + (i * childWidth), pTop, pLeft + (i + 1)
					* childWidth, height - pBottom);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		float showMenuCount = mShowMenuCount;
		int width = 0;
		int height = 0;
		int maxHeight = 0;
		int maxWidth = 0;

		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			final View child = getChildAt(i);
			float widthPercent = 1f / showMenuCount;
			int childWidthSize = (int) (widthPercent * widthSize);
			int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
					childWidthSize, MeasureSpec.EXACTLY);
			child.measure(childWidthMeasureSpec, heightMeasureSpec);
			final LayoutParams lp = (LayoutParams) child.getLayoutParams();
			maxWidth += child.getMeasuredWidth() + lp.leftMargin
					+ lp.rightMargin;
			maxHeight = Math.max(maxHeight, child.getMeasuredHeight()
					+ lp.topMargin + lp.bottomMargin);
		}

		if (widthMode == MeasureSpec.UNSPECIFIED) {
			width = widthSize;
		} else if (widthMode == MeasureSpec.AT_MOST) {
			width = Math.min(widthSize, maxWidth);
		} else if (widthMode == MeasureSpec.EXACTLY) {
			width = widthSize;
		}

		if (heightMode == MeasureSpec.UNSPECIFIED) {
			height = maxHeight;
			height += getPaddingTop() + getPaddingBottom();
		} else if (heightMode == MeasureSpec.AT_MOST) {
			height = Math.min(heightSize, maxHeight);
			height += getPaddingTop() + getPaddingBottom();
		} else if (heightMode == MeasureSpec.EXACTLY) {
			height = heightSize;
		}

		setMeasuredDimension(width, height);
	}

	public interface OnPageChangeListener {
		public void onPageChanged(int index);
	}

}
