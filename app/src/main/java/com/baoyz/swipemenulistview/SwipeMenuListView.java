package com.baoyz.swipemenulistview;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Scroller;

import com.hustunique.myapplication.R;

/**
 * 
 * @author baoyz
 * @date 2014-8-18
 * 
 */
public class SwipeMenuListView extends ListView {

	private static final int TOUCH_STATE_NONE = 0;
	private static final int TOUCH_STATE_X = 1;
	private static final int TOUCH_STATE_Y = 2;

	private int MAX_Y = 5;
	private int MAX_X = 3;

    //////////////////////////////
    private int slidposition;
    private boolean isslide=false;
    private int screenwidth;
    private View itemview;
    private Scroller mscroller;
    private static final int SNAP_VELOCITY=600;
    private VelocityTracker velocityTracker;
    private int mTouchSlop;
    private  Removedirection removedirection;
    private enum Removedirection{
        Right,Left;
    }



	private float mDownX,mUpX;
	private float mDownY,mUpY;
	private int mTouchState;
	private int mTouchPosition;
	private SwipeMenuLayout mTouchView;
	private OnSwipeListener mOnSwipeListener;
    private OnrightFlingListener onrightFlingListener;
	private SwipeMenuCreator mMenuCreator;
	private OnMenuItemClickListener mOnMenuItemClickListener;
	private Interpolator mCloseInterpolator;
	private Interpolator mOpenInterpolator;
    public static boolean IsOpen=false;


	public SwipeMenuListView(Context context) {
		super(context);
        screenwidth=((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
        mscroller=new Scroller(context);
        mTouchSlop= ViewConfiguration.get(getContext()).getScaledTouchSlop();
		init();
	}

	public SwipeMenuListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
        screenwidth=((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
		mscroller=new Scroller(context);
        mTouchSlop= ViewConfiguration.get(getContext()).getScaledTouchSlop();
        init();
	}

	public SwipeMenuListView(Context context, AttributeSet attrs) {
		super(context, attrs);
        screenwidth=((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
        mscroller=new Scroller(context);
        mTouchSlop= ViewConfiguration.get(getContext()).getScaledTouchSlop();
		init();
	}

	private void init() {
		MAX_X = dp2px(MAX_X);
		MAX_Y = dp2px(MAX_Y);
		mTouchState = TOUCH_STATE_NONE;
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(new SwipeMenuAdapter(getContext(), adapter) {
			@Override
			public void createMenu(SwipeMenu menu) {
				if (mMenuCreator != null) {
					mMenuCreator.create(menu);
				}
			}

			@Override
			public void onItemClick(SwipeMenuView view, SwipeMenu menu,
					int index) {
				boolean flag = false;
				if (mOnMenuItemClickListener != null) {
					flag = mOnMenuItemClickListener.onMenuItemClick(
							view.getPosition(), menu, index);
				}
				if (mTouchView != null && !flag) {
					mTouchView.smoothCloseMenu();
				}
			}
		});
	}

    public void setOnRightFlingListener(OnrightFlingListener rightFlingListener){
        this.onrightFlingListener=rightFlingListener;
    }

	public void setCloseInterpolator(Interpolator interpolator) {
		mCloseInterpolator = interpolator;
	}

	public void setOpenInterpolator(Interpolator interpolator) {
		mOpenInterpolator = interpolator;
	}

	public Interpolator getOpenInterpolator() {
		return mOpenInterpolator;
	}

	public Interpolator getCloseInterpolator() {
		return mCloseInterpolator;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev);
	}

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:{
                addVellocityTracker(ev);
                if(!mscroller.isFinished()){
                    return super.dispatchTouchEvent(ev);
                }

                mDownX=(int)ev.getX();
                mDownY=(int)ev.getY();

                slidposition=pointToPosition((int)mDownX,(int)mDownY);
                if(slidposition== AdapterView.INVALID_POSITION){
                    return super.dispatchTouchEvent(ev);
                }

                itemview=getChildAt(slidposition-getFirstVisiblePosition());
                break;
            }
            case MotionEvent.ACTION_HOVER_MOVE:{
                if(Math.abs(getScrollVelocity())>SNAP_VELOCITY||(Math.abs(ev.getX()-mDownX)>mTouchSlop&&Math.abs(ev.getY()-mDownY)<mTouchSlop)){
                    isslide=true;
                    break;
                }
            }
            case MotionEvent.ACTION_UP:
                recycleVelocityTracker();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
	public boolean onTouchEvent(MotionEvent ev) {

        if (isslide && slidposition != AdapterView.INVALID_POSITION) {
            requestDisallowInterceptTouchEvent(true);
            addVellocityTracker(ev);
        }

            if (ev.getAction() != MotionEvent.ACTION_DOWN && mTouchView == null)
                return super.onTouchEvent(ev);
            int action = MotionEventCompat.getActionMasked(ev);
            action = ev.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    int oldPos = mTouchPosition;
                    mDownX = ev.getX();
                    mDownY = ev.getY();
                    mTouchState = TOUCH_STATE_NONE;

                    mTouchPosition = pointToPosition((int) ev.getX(), (int) ev.getY());

                    if (mTouchPosition == oldPos && mTouchView != null
                            && mTouchView.isOpen()) {
                        mTouchState = TOUCH_STATE_X;
                        mTouchView.onSwipe(ev);
                        return true;
                    }

                    View view = getChildAt(mTouchPosition - getFirstVisiblePosition());

                    if (mTouchView != null && mTouchView.isOpen()) {
                        mTouchView.smoothCloseMenu();
                        mTouchView = null;
                        return super.onTouchEvent(ev);
                    }
                    if (view instanceof SwipeMenuLayout) {
                        mTouchView = (SwipeMenuLayout) view;
                    }
                    if (mTouchView != null) {
                        mTouchView.onSwipe(ev);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:

                    float dy = Math.abs((ev.getY() - mDownY));
                    float dx = Math.abs((ev.getX() - mDownX));
                    if (mTouchState == TOUCH_STATE_X) {
                        if (mTouchView != null) {
                            mTouchView.onSwipe(ev);
                        }
                        getSelector().setState(new int[]{0});
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                        super.onTouchEvent(ev);
                        return true;
                    } else if (mTouchState == TOUCH_STATE_NONE) {
                        if (Math.abs(dy) > MAX_Y) {
                            mTouchState = TOUCH_STATE_Y;
                        } else if (dx > MAX_X) {
                            mTouchState = TOUCH_STATE_X;
                            if (mOnSwipeListener != null) {
                                mOnSwipeListener.onSwipeStart(mTouchPosition);
                            }
                        }
                    }
                     /*int x=(int)ev.getX();
                     int deltax=(int)mDownX-x;
                    mDownX=x;
                    itemview.scrollBy(deltax,0);
*/
                    break;
                case MotionEvent.ACTION_UP:
                    this.mUpX = ev.getX();
                    this.mUpY = ev.getY();
                    this.onrightFlingListener.onRightFling(mTouchPosition, this.mUpX - this.mDownX, this.mUpX, this.mUpY);
                    if (mTouchState == TOUCH_STATE_X) {
                        if (mTouchView != null) {
                            mTouchView.onSwipe(ev);
                            if (!mTouchView.isOpen()) {
                                mTouchPosition = -1;
                                mTouchView = null;
                            }
                        }
                        if (mOnSwipeListener != null) {
                            mOnSwipeListener.onSwipeEnd(mTouchPosition);
                        }
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                        super.onTouchEvent(ev);
                        return true;
                    }
                    break;

        }
            return super.onTouchEvent(ev);
        }

	public void smoothOpenMenu(int position) {
		if (position >= getFirstVisiblePosition()
				&& position <= getLastVisiblePosition()) {
			View view = getChildAt(position - getFirstVisiblePosition());
			if (view instanceof SwipeMenuLayout) {
				mTouchPosition = position;
				if (mTouchView != null && mTouchView.isOpen()) {
					mTouchView.smoothCloseMenu();
				}
				mTouchView = (SwipeMenuLayout) view;
				mTouchView.smoothOpenMenu();
			}
		}
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getContext().getResources().getDisplayMetrics());
	}

	public void setMenuCreator(SwipeMenuCreator menuCreator) {
		this.mMenuCreator = menuCreator;
	}

	public void setOnMenuItemClickListener(
			OnMenuItemClickListener onMenuItemClickListener) {
		this.mOnMenuItemClickListener = onMenuItemClickListener;
	}

	public void setOnSwipeListener(OnSwipeListener onSwipeListener) {
		this.mOnSwipeListener = onSwipeListener;
	}


    public static interface OnrightFlingListener{
         boolean onRightFling(int i, float v, float v2, float v3);
    }

	public static interface OnMenuItemClickListener {
		boolean onMenuItemClick(int position, SwipeMenu menu, int index);
	}

	public static interface OnSwipeListener {
		void onSwipeStart(int position);

		void onSwipeEnd(int position);
	}

    private void addVellocityTracker(MotionEvent event){
        if(velocityTracker==null){
            velocityTracker=VelocityTracker.obtain();
        }
        velocityTracker.addMovement(event);
    }

    private void recycleVelocityTracker(){
        if(velocityTracker!=null){
            velocityTracker.recycle();
            velocityTracker=null;
        }
    }

    private int getScrollVelocity(){
        velocityTracker.computeCurrentVelocity(1000);
        int vellocity=(int)velocityTracker.getXVelocity();
        return vellocity;
    }

    private void scrollRight(){
        removedirection=Removedirection.Right;
        final int delta=(screenwidth+itemview.getScrollX());
        mscroller.startScroll(itemview.getScrollX(),0,-delta,0,Math.abs(delta));
        postInvalidate();
    }

    private void scrollLeft(){
        removedirection=Removedirection.Left;
        final int delta=(screenwidth-itemview.getScrollX());
        mscroller.startScroll(itemview.getScrollX(),0,delta,0);
        postInvalidate();
    }

    private void scrollByDistanceX(){
        if(itemview.getScrollX()>=screenwidth/2){
            scrollLeft();
        }else if(itemview.getScrollX()>=-screenwidth/2){
            scrollRight();
        }else{
            itemview.scrollTo(0,0);
        }
    }

    @Override
    public void computeScroll() {

        if(mscroller.computeScrollOffset()){
            itemview.scrollTo(mscroller.getCurrX(),mscroller.getCurrY());
            postInvalidate();

            if(mscroller.isFinished()){
                itemview.scrollTo(0,0);
            }
        }

        super.computeScroll();
    }
}
