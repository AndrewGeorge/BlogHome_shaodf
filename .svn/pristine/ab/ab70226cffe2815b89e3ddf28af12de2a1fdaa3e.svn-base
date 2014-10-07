package com.example.bloghome_shaodf.news;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.bloghome_shaodf.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

public class XListView extends ListView implements OnScrollListener {

	// 实现滚动条监听，lietView的移动
	private float mLastY = -1; // 事件的初始状态
	// Scroller类是为了实现View平滑滚动的一个Helper类
	private Scroller mScroller;
	private OnScrollListener mScrollListener;
	private IXListViewListener mListViewListener;
	private XListViewHeader mHeaderView;
	private RelativeLayout mHeaderViewContent;
	private TextView mHeaderTimeView;
	private int mHeaderViewHeight;
	private boolean mEnablePullRefresh = true;
	private boolean mPullRefreshing = false;
	private XListViewFooter mFooterView;
	private boolean mEnablePullLoad;
	private boolean mPullLoading;
	private boolean mIsFooterReady = false;
	private int mTotalItemCount;
	private int mScrollBack;
	private final static int SCROLLBACK_HEADER = 0;
	private final static int SCROLLBACK_FOOTER = 1;
	// 返回所需的时间
	private final static int SCROLL_DURATION = 400;
	// 上拉加载超过50px后触发加载事件
	private final static int PULL_LOAD_MORE_DELTA = 50;
	private final static float OFFSET_RADIO = 1.8f;

	/**
	 * @param context
	 */
	public XListView(Context context) {
		super(context);

		initWithContext(context);
	}

	public XListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initWithContext(context);
	}

	public XListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initWithContext(context);
	}

	// 初始化数据
	private void initWithContext(Context context) {

		// 通常在自定义的View时使用，在View中定义一个私有成员mScroller = new
		// Scroller(context)。设置mScroller滚动的位置时，并不会导致View的滚动，通常是用mScroller记录/计算View滚动的位置，
		// 再重写View的computeScroll()，完成实际的滚动。

		mScroller = new Scroller(context, new DecelerateInterpolator());
		super.setOnScrollListener(this);

		// 实例头部视图
		mHeaderView = new XListViewHeader(context);

		mHeaderViewContent = (RelativeLayout) mHeaderView
				.findViewById(R.id.xlistview_header_content);

		mHeaderTimeView = (TextView) mHeaderView
				.findViewById(R.id.xlistview_header_time);
		// 添加头部
		addHeaderView(mHeaderView);

		// 设置底部视图
		mFooterView = new XListViewFooter(context);

		mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@SuppressWarnings("deprecation")
					@Override
					public void onGlobalLayout() {

						mHeaderViewHeight = mHeaderViewContent.getHeight();
						getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
					}
				});
	}

	// 设置填充器
	@Override
	public void setAdapter(ListAdapter adapter) {
		// 确定底部为的视图为最后一个视图
		if (mIsFooterReady == false) {
			mIsFooterReady = true;
			addFooterView(mFooterView);
		}
		super.setAdapter(adapter);
	}

	/**
	 * 是否开启刷新
	 * 
	 * @param enable
	 */
	public void setPullRefreshEnable(boolean enable) {

		mEnablePullRefresh = enable;
		// 判断是否在开启状态
		if (!mEnablePullRefresh) {

			mHeaderViewContent.setVisibility(View.INVISIBLE);
		} else {

			mHeaderViewContent.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 是否开启加载更多
	 * 
	 * @param enable
	 */
	public void setPullLoadEnable(boolean enable) {
		mEnablePullLoad = enable;

		// 不可用时隐藏
		if (!mEnablePullLoad) {

			mFooterView.hide();

			mFooterView.setOnClickListener(null);
		} else {
			// 设置为可用
			mPullLoading = false;
			mFooterView.show();
			mFooterView.setState(XListViewFooter.STATE_NORMAL);
			// 点击加载事件
			mFooterView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					startLoadMore();
				}
			});
		}
	}

	/**
	 * 停止更新后重新设置更新的视图的高度
	 */
	public void stopRefresh() {
		
		if (mPullRefreshing == true) {
			mPullRefreshing = false;
			resetHeaderHeight();
			SimpleDateFormat sDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd    hh:mm:ss");
			Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
			String str = sDateFormat.format(curDate);
			setRefreshTime(str);
		}
	}

	/**
	 * 停止加载后重新设置加载的视图.
	 */
	public void stopLoadMore() {
		if (mPullLoading == true) {
			mPullLoading = false;
			mFooterView.setState(XListViewFooter.STATE_NORMAL);
		}
	}

	/**
	 * 设置最后加载时间
	 * 
	 * @param time
	 */
	public void setRefreshTime(String time) {
		mHeaderTimeView.setText(time);
	}

	private void invokeOnScrolling() {

		// instanceof判断是是否为子类
		if (mScrollListener instanceof OnXScrollListener) {

			OnXScrollListener l = (OnXScrollListener) mScrollListener;
			l.onXScrolling(this);
		}
	}

	private void updateHeaderHeight(float delta) {

		mHeaderView.setVisiableHeight((int) delta
				+ mHeaderView.getVisiableHeight());
		if (mEnablePullRefresh && !mPullRefreshing) {
			if (mHeaderView.getVisiableHeight() > mHeaderViewHeight) {

				mHeaderView.setState(XListViewHeader.STATE_READY);
			} else {
				mHeaderView.setState(XListViewHeader.STATE_NORMAL);
			}
		}
		// 滚回到顶部
		setSelection(0);
	}

	/**
	 * 重新设置刷新的视图的高度。滚动条返回之后头部文件将消失需要重设
	 */
	private void resetHeaderHeight() {
		int height = mHeaderView.getVisiableHeight();
		if (height == 0) // not visible.
			return;

		if (mPullRefreshing && height <= mHeaderViewHeight) {
			return;
		}
		int finalHeight = 0;
		if (mPullRefreshing && height > mHeaderViewHeight) {
			finalHeight = mHeaderViewHeight;
		}
		mScrollBack = SCROLLBACK_HEADER;
		mScroller.startScroll(0, height, 0, finalHeight - height,
				SCROLL_DURATION);
		// trigger computeScroll
		invalidate();
	}

	private void updateFooterHeight(float delta) {
		int height = mFooterView.getBottomMargin() + (int) delta;
		if (mEnablePullLoad && !mPullLoading) {
			if (height > PULL_LOAD_MORE_DELTA) { // height enough to invoke
													// loadmore.
				mFooterView.setState(XListViewFooter.STATE_READY);
			} else {
				mFooterView.setState(XListViewFooter.STATE_NORMAL);
			}
		}
		mFooterView.setBottomMargin(height);
	}

	private void resetFooterHeight() {
		int bottomMargin = mFooterView.getBottomMargin();
		if (bottomMargin > 0) {
			mScrollBack = SCROLLBACK_FOOTER;
			mScroller.startScroll(0, bottomMargin, 0, -bottomMargin,
					SCROLL_DURATION);
			// 设置高度清平重新绘制
			invalidate();
		}
	}

	// 开始加载更多
	private void startLoadMore() {

		mPullLoading = true;
		mFooterView.setState(XListViewFooter.STATE_LOADING);
		if (mListViewListener != null) {

			//调用接口中的方法需要在实现该接口的类中来实现
			mListViewListener.onLoadMore();
		}
	}

	// 手势的监听来判断是否开启
	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		// getY获取相对当前控件的竖直坐标
		// getRawY获取相对左上角

		if (mLastY == -1) {
			mLastY = ev.getRawY();
		}

		switch (ev.getAction()) {

		case MotionEvent.ACTION_DOWN:

			mLastY = ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:

			// 滑动距离
			final float deltaY = ev.getRawY() - mLastY;
			mLastY = ev.getRawY();

			if (getFirstVisiblePosition() == 0

			&& (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)) {

				// 上面的高度设置为所拉距离
				updateHeaderHeight(deltaY / OFFSET_RADIO);
				// 设置监听
				invokeOnScrolling();

			} else if (getLastVisiblePosition() == mTotalItemCount - 1
					&& (mFooterView.getBottomMargin() > 0 || deltaY < 0)
					&& getCount() > 10) {

				updateFooterHeight(-deltaY / OFFSET_RADIO);
			}

			break;
		default:

			mLastY = -1; // reset
			//判断是否为第一个位置
			if (getFirstVisiblePosition() == 0) {
				
				if (mEnablePullRefresh
						&& mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
					
					mPullRefreshing = true;
					//开始刷新
					mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
					//判断接口是否为空
					if (mListViewListener != null) {
						
						//调用接口中的方法需要在实现该接口的类中来实现
						mListViewListener.onRefresh();
					}
				}
				resetHeaderHeight();
			} else if (getLastVisiblePosition() == mTotalItemCount - 1) {
				// invoke load more.
				if (mEnablePullLoad
						&& mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA) {
					startLoadMore();
				}

				// 设置下面的距离拉起事的
				resetFooterHeight();
			}
			break;
		}
		return super.onTouchEvent(ev);
	}

	// 实际的滚动
	@Override
	public void computeScroll() {

		if (mScroller.computeScrollOffset()) {

			if (mScrollBack == SCROLLBACK_HEADER) {
				// 设置可见并设置高度为当前的滚动条的数值位置
				mHeaderView.setVisiableHeight(mScroller.getCurrY());
			} else {
				mFooterView.setBottomMargin(mScroller.getCurrY());
			}
			// 直接调用线程刷新界面
			postInvalidate();
			invokeOnScrolling();
		}
		super.computeScroll();
	}

	@Override
	public void setOnScrollListener(OnScrollListener l) {
		mScrollListener = l;
	}

	// 实现OnScrollListener后将会重写这两个方法
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

		if (mScrollListener != null) {
			mScrollListener.onScrollStateChanged(view, scrollState);
		}

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		// 滚动时一直回调，直到停止滚动时才停止回调。单击时回调一次。
		// firstVisibleItem：当前能看见的第一个列表项ID（从0开始）
		// visibleItemCount：当前能看见的列表项个数（小半个也算）
		// totalItemCount：列表项共数

		mTotalItemCount = totalItemCount;
		if (mScrollListener != null) {
			mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,
					totalItemCount);
		}
	}

	//
	public void setXListViewListener(IXListViewListener l) {
		mListViewListener = l;
	}

	/**
	 * 也可以实现这个接口
	 * 
	 */
	public interface OnXScrollListener extends OnScrollListener {
		public void onXScrolling(View view);
	}

	/**
	 * 自己的接口用来
	 */
	public interface IXListViewListener {

		public void onRefresh();

		public void onLoadMore();
	}

	// 调用方法设置加载时的动画
	public void setFooterBackgroundRes(int resId) {

		if (mFooterView != null) {

			mFooterView.setBackgroundResource(resId);
		}

	}

	// 设置加载更多的背景颜色
	public void setFooterBackgroundColor(int resId) {

		if (mFooterView != null) {

			mFooterView.setBackgroundColor(resId);
			mFooterView.invalidate();
		}

	}

	/**
	 * 是否开启
	 */
	public boolean isLoading() {

		return mPullLoading;
	}

}
