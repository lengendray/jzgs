package com.cmeiyuan.hello123.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2015/2/26.
 */
public class MySwipeRefreshLayout extends SwipeRefreshLayout {

    private float lastX;
    private float lastY;

    public MySwipeRefreshLayout(Context context) {
        super(context);
    }

    public MySwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            lastX = event.getX();
            lastY = event.getY();
        } else if (action == MotionEvent.ACTION_MOVE) {
            float curX = event.getX();
            float curY = event.getY();
            float distanceX = Math.abs(curX - lastX);
            float distanceY = Math.abs(curY - lastY);
            if (distanceX > distanceY) {
                return false;
            }
        }
        return super.onInterceptTouchEvent(event);
    }
}
