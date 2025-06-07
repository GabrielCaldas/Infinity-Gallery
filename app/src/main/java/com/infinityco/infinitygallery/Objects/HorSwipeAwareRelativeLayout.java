package com.infinityco.infinitygallery.Objects;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.widget.RelativeLayout;

/**
 * A RelativeLayout that listens for horizontal swipe gestures.
 * Use #setOnHorizontalSwipeListener(HorSwipeAwareRelativeLayout.OnHorizontalSwipeListener).
 *
 * Justas Medeisis 2015
 */
public class HorSwipeAwareRelativeLayout extends RelativeLayout {

    public interface OnHorizontalSwipeListener {
        public void onRightSwipe();
    }
    private OnHorizontalSwipeListener listener;

    private VelocityTracker velocityTracker;
    private int slop;
    private int minFlingVelocity, maxFlingVelocity;
    private int activePointerId;
    private float downX, downY;

    public HorSwipeAwareRelativeLayout(Context context) {
        super(context);
        init(context);
    }

    public HorSwipeAwareRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HorSwipeAwareRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @SuppressWarnings("UnusedDeclaration")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HorSwipeAwareRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context){
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        slop = viewConfiguration.getScaledTouchSlop();
        minFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        maxFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
    }

    public void setOnHorizontalSwipeListener(OnHorizontalSwipeListener listener){
        this.listener = listener;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event){
        if(null == velocityTracker){
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(event);

        switch(event.getActionMasked()){
            case MotionEvent.ACTION_DOWN: {
                final int pointerIndex = event.getActionIndex();

                downX = event.getX(pointerIndex);
                downY = event.getY(pointerIndex);
                activePointerId = event.getPointerId(pointerIndex);
                return false;
            }
            case MotionEvent.ACTION_MOVE: {
                if(activePointerId < 0) break;
                final int pointerIndex = event.findPointerIndex(activePointerId);

                final float dx = Math.abs(event.getX(pointerIndex) - downX);
                final float dy = Math.abs(event.getY(pointerIndex) - downY);

                return dx > slop && dx > dy;
            }
            case MotionEvent.ACTION_POINTER_UP: {
                final int pointerIndex = event.getActionIndex();
                final int pointerId = event.getPointerId(pointerIndex);

                if(pointerId != activePointerId) break; // otherwise, if active pointer, fall through
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                onTouchEnded(false);
            }
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event){
        if(null == velocityTracker){
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(event);

        switch(event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_MOVE: {
                if (activePointerId < 0) break;
                return true;
            }
            case MotionEvent.ACTION_POINTER_UP: {
                final int pointerIndex = event.getActionIndex();
                final int pointerId = event.getPointerId(pointerIndex);

                if(pointerId != activePointerId) break; // otherwise, if active pointer, fall through
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                onTouchEnded(true);
                return true;
            }
        }

        return false;
    }

    private void onTouchEnded(boolean tracked){
        if(tracked && null != listener){
            velocityTracker.computeCurrentVelocity(1000, maxFlingVelocity);
            final float velocity = velocityTracker.getXVelocity(activePointerId);
            if(Math.abs(velocity) > minFlingVelocity && velocity > 0){
                listener.onRightSwipe();
            }
        }

        if(null != velocityTracker){
            velocityTracker.recycle();
            velocityTracker = null;
        }
        activePointerId = -1;
    }
}