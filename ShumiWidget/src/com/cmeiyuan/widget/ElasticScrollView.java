package com.cmeiyuan.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ScrollView;

import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

public class ElasticScrollView extends ScrollView {

	protected static final String TAG = "ElasticScrollView";

	/**
	 * 最多拉出高度
	 */
	private static final float OVERSCRLL_ACTOR = 0.8f;

	/**
	 * 弹性系数
	 */
	private static final float ELASTICF_ACTOR = 0.5f;

	/**
	 * An invalid pointer id
	 */
	private static final int INVALID_POINTER_ID = -1;

	/**
	 * 有效移动距离，防止点击事件被拦截
	 */
	private static final int VALID_MOVE_DISTANCE = 20;

	/**
	 * 最新一次的手指触摸位置X
	 */
	private float mLastMotionX;

	/**
	 * 最新一次的手指触摸位置Y
	 */
	private float mLastMotionY;

	/**
	 * 按在屏幕上的手指的id
	 */
	private int mActivePointerId = INVALID_POINTER_ID;

	/**
	 * ScrollView的唯一child
	 */
	private View mChildView;

	/**
	 * 惯性距离
	 */
	private int mInertanceY;

	/**
	 * 正常layout布局
	 */
	private Rect mNormalRect = new Rect();

	private ValueAnimator mResumeAnimator = new ValueAnimator();

	private ValueAnimator mScrollAnimator = new ValueAnimator();

	private long mDuration = 200;

	private Handler mHandler = new Handler();

	private Runnable mRunnable = new Runnable() {

		@Override
		public void run() {
			scrollToNormal();
		}
	};

	private AnimatorUpdateListener mAnimatorUpdateListener = new AnimatorUpdateListener() {

		@Override
		public void onAnimationUpdate(ValueAnimator valueAnimator) {
			try {
				int top = (Integer) valueAnimator.getAnimatedValue();
				int width = mChildView.getMeasuredWidth();
				int height = mChildView.getMeasuredHeight();
				if (mChildView != null) {
					mChildView.layout(0, top, width, top + height);
				}
			} catch (Exception e) {
				Log.e(TAG, "onAnimationUpdate error:" + e.toString());
			}
		}
	};

	public ElasticScrollView(Context context) {
		this(context, null);
	}

	public ElasticScrollView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public ElasticScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			setOverScrollMode(View.OVER_SCROLL_NEVER);
		} else {
			ViewCompat.setOverScrollMode(this, ViewCompat.OVER_SCROLL_NEVER);
		}
		mResumeAnimator.setDuration(mDuration);
		mResumeAnimator.setInterpolator(new DecelerateInterpolator());
		mResumeAnimator.addUpdateListener(mAnimatorUpdateListener);

		mScrollAnimator.setDuration(mDuration);
		mScrollAnimator.setInterpolator(new DecelerateInterpolator());
		mScrollAnimator.addUpdateListener(mAnimatorUpdateListener);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if (getChildCount() > 0) {
			mChildView = getChildAt(0);
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (mChildView != null) {
			int left = mChildView.getLeft();
			int top = mChildView.getTop();
			int right = mChildView.getRight();
			int bottom = mChildView.getBottom();
			mNormalRect.set(left, top, right, bottom);
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastMotionX = ev.getX();
			mLastMotionY = ev.getY();
			mActivePointerId = ev.getPointerId(0);
			break;
		case MotionEvent.ACTION_MOVE:
			float curX = ev.getX();
			float curY = ev.getY();
			float distanceX = Math.abs(curX - mLastMotionX);
			float distanceY = Math.abs(curY - mLastMotionY);
			// 如果是横向滑动，不要拦截
			if (distanceX > distanceY) {
				return false;
			}
			// 坚向滑动距离超过有效移动距离
			if (distanceY > VALID_MOVE_DISTANCE) {
				return true;
			}
			break;
		case MotionEvent.ACTION_CANCEL:
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}

	@SuppressLint("ClickableViewAccessibility")
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction() & MotionEvent.ACTION_MASK) {
		// 第一个手指按下
		case MotionEvent.ACTION_DOWN:
			mActivePointerId = ev.getPointerId(0);
			mLastMotionY = (int) ev.getY();
			break;
		// 第二个手指按下
		case MotionEvent.ACTION_POINTER_DOWN:
			final int index = ev.getActionIndex();
			mLastMotionY = ev.getY(index);
			mActivePointerId = ev.getPointerId(index);
			break;
		// 第二个手指放开
		case MotionEvent.ACTION_POINTER_UP:
			onSecondaryPointerUp(ev);
			if (mActivePointerId != INVALID_POINTER_ID) {
				mLastMotionY = ev.getY(ev.findPointerIndex(mActivePointerId));
			}
			break;
		// 手指移动时
		case MotionEvent.ACTION_MOVE:
			int activePointerIndex = ev.findPointerIndex(mActivePointerId);
			if (activePointerIndex == -1) {
				Log.e(TAG, "Invalid activePointerIndex = " + activePointerIndex
						+ " in onTouchEvent");
				break;
			}
			float curY = ev.getY(activePointerIndex);
			float deltaY = curY - mLastMotionY;
			mLastMotionY = curY;
			if (deltaY > 0 && isScrollToTop()) {
				overScrollBy((int) deltaY);
				return true;
			}
			if (deltaY < 0 && isScrollToBottom()) {
				overScrollBy((int) deltaY);
				return true;
			}
			break;
		case MotionEvent.ACTION_CANCEL:
			// 移动到控件边界
		case MotionEvent.ACTION_OUTSIDE:
			// 最后一个手指放开
		case MotionEvent.ACTION_UP:
			// 滑动到正常状态
			scrollToNormal();
			mActivePointerId = INVALID_POINTER_ID;
			break;
		default:
			break;
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * 功能描述: 防止出现pointerIndex out of range异常<br>
	 *
	 * @param ev
	 */
	private void onSecondaryPointerUp(MotionEvent ev) {
		final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
		final int pointerId = ev.getPointerId(pointerIndex);
		if (pointerId == mActivePointerId) {
			// This was our active pointer going up. Choose a new
			// active pointer and adjust accordingly.
			// TODO: Make this decision more intelligent.
			final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
			mLastMotionY = (int) ev.getY(newPointerIndex);
			mActivePointerId = ev.getPointerId(newPointerIndex);
		}
	}

	private void overScrollTo(int targetY) {
		if (mChildView != null) {
			mResumeAnimator.setIntValues(0, -targetY);
			mResumeAnimator.start();
		}
	}

	private void overScrollBy(int deltaY) {
		if (mChildView != null) {
			int left = mChildView.getLeft();
			int right = mChildView.getRight();
			int top = mChildView.getTop();
			int bottom = mChildView.getBottom();
			float offset = Math.abs(top);
			float total = getMeasuredHeight();
			float percent = offset / total;
			if (percent >= OVERSCRLL_ACTOR) {
				return;
			}
			int scrollY = (int) (deltaY * (1 - percent) * ELASTICF_ACTOR);
			top += scrollY;
			bottom += scrollY;
			mChildView.layout(left, top, right, bottom);
			Log.d(TAG, "layout:" + new Rect(left, top, right, bottom));
		}
	}

	private void scrollToNormal() {
		if (mChildView != null) {
			int top = mChildView.getTop();
			mResumeAnimator.setIntValues(top, 0);
			mResumeAnimator.start();
		}
	}

	@Override
	public void fling(int velocityY) {
		super.fling(velocityY);
		mInertanceY = 50 * velocityY / 2500;
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX,
			boolean clampedY) {
		super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
		if (clampedY) {
			overScrollTo(mInertanceY);
		}
		// mHandler.postDelayed(mRunnable, mDuration);
	}

	private boolean isScrollToTop() {
		return getScrollY() == 0;
	}

	private boolean isScrollToBottom() {
		if (mChildView == null) {
			return true;
		}
		int normalY = mChildView.getMeasuredHeight() - getMeasuredHeight();
		return getScrollY() == normalY;
	}
}
