package com.example.bloghome_shaodf.blog;

import com.example.bloghome_shaodf.R;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class PullToRefreshListView extends ListView implements OnScrollListener {

	private Context context;

	// 状态
	private static final int TAP_TO_REFRESH = 1;
	private static final int PULL_TO_REFRESH = 2;
	private static final int RELEASE_TO_REFRESH = 3;
	private static final int REFRESHING = 4;

	private OnRefreshListener mOnRefreshListener;

	// 监听对ListView的滑动时间
	private OnScrollListener mOnScrollListener;
	private LayoutInflater mInflater;

	// 顶部刷新时出现控件
	private RelativeLayout mRefreshView;
	private TextView mRefreshViewText;
	private ImageView mRefreshViewImage;
	private ProgressBar mRefreshViewProgress;
	private TextView mRefreshViewLastUpdated;

	// 当前滑动状态
	private int mCurrentScrollState;
	// 当前刷新状态
	private int mRefreshState;

	// 箭头动画效果
	private RotateAnimation mFlipAnimation;
	private RotateAnimation mReverseFlipAnimation;

	private int mRefreshViewHeight;
	private int mRefreshOriginalTopPadding;
	private int mLastMotionY;

	private boolean mBounceHack;

	// more
	private int MaxDateNum;
	private View moreView;
	public Button bt;
	private ProgressBar pg;
	private int lastVisibleIndex;
	public boolean isclick;

	public PullToRefreshListView(Context context) {
		super(context);
		init(context);
	}

	public PullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
		this.context = context;
	}

	public PullToRefreshListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	/**
	 * 初始化控件和箭头动画
	 */
	private void init(Context context) {
		mFlipAnimation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mFlipAnimation.setInterpolator(new LinearInterpolator());
		mFlipAnimation.setDuration(250);
		mFlipAnimation.setFillAfter(true);//停留在最后
		mReverseFlipAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mReverseFlipAnimation.setInterpolator(new LinearInterpolator());
		mReverseFlipAnimation.setDuration(250);
		mReverseFlipAnimation.setFillAfter(true);

		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		mRefreshView = (RelativeLayout) mInflater.inflate(
				R.layout.pull_to_refresh_header, this, false);
		mRefreshViewText = (TextView) mRefreshView
				.findViewById(R.id.pull_to_refresh_text);
		mRefreshViewImage = (ImageView) mRefreshView
				.findViewById(R.id.pull_to_refresh_image);
		mRefreshViewProgress = (ProgressBar) mRefreshView
				.findViewById(R.id.pull_to_refresh_progress);
		mRefreshViewLastUpdated = (TextView) mRefreshView
				.findViewById(R.id.pull_to_refresh_updated_at);

		mRefreshViewImage.setMinimumHeight(50);//设置视图最小高度
		mRefreshOriginalTopPadding = mRefreshView.getPaddingTop();

		mRefreshState = TAP_TO_REFRESH;

		// 为listView头部增加一个view
		addHeaderView(mRefreshView);

		super.setOnScrollListener(this);

		measureView(mRefreshView);
		mRefreshViewHeight = mRefreshView.getMeasuredHeight();

		// 实例化底部布局
		MaxDateNum = 20;
		moreView = LayoutInflater.from(context)
				.inflate(R.layout.moredata, null);
		bt = (Button) moreView.findViewById(R.id.bt_load);
		pg = (ProgressBar) moreView.findViewById(R.id.pg);
		addFooterView(moreView);
		bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pg.setVisibility(View.VISIBLE);// 将进度条可见
				bt.setVisibility(View.GONE);// 按钮不可见
				isclick=true;
			}
		});
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		setSelection(1);
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);

		setSelection(1);
	}

	/**
	 * 设置滑动监听
	 * 
	 */
	@Override
	public void setOnScrollListener(AbsListView.OnScrollListener l) {
		mOnScrollListener = l;
	}

	/**
	 * 注册一个list需要刷新时的回调接口
	 * 
	 */
	public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
		mOnRefreshListener = onRefreshListener;
	}

	/**
	 * 璁设置标签显示何时最后被刷新
	 * 
	 * @param lastUpdated
	 *            Last updated at.
	 */
	public void setLastUpdated(CharSequence lastUpdated) {
		if (lastUpdated != null) {
			mRefreshViewLastUpdated.setVisibility(View.VISIBLE);
			mRefreshViewLastUpdated.setText(lastUpdated);
		} else {
			mRefreshViewLastUpdated.setVisibility(View.GONE);
		}
	}

	// 实现该方法处理触摸
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		final int y = (int) event.getY();
		mBounceHack = false;

		switch (event.getAction()) {

		case MotionEvent.ACTION_UP:
			if (!isVerticalScrollBarEnabled()) {
				setVerticalScrollBarEnabled(true);
			}
			if (getFirstVisiblePosition() == 0 && mRefreshState != REFRESHING) {
				// 拖动距离达到刷新需求
				if ((mRefreshView.getBottom() >= mRefreshViewHeight || mRefreshView
						.getTop() >= 0) && mRefreshState == RELEASE_TO_REFRESH) {
					// 把状态设置为正在刷新
					mRefreshState = REFRESHING;
					// 准备刷新
					prepareForRefresh();
					// 刷新
					onRefresh();
				} else if (mRefreshView.getBottom() < mRefreshViewHeight
						|| mRefreshView.getTop() <= 0) {
					// 终止刷新
					resetHeader();
					setSelection(1);
				}
			}
			break;
		case MotionEvent.ACTION_DOWN:
			// 获得按下y轴位置
			mLastMotionY = y;
			break;
		case MotionEvent.ACTION_MOVE:
			// 计算边距
			applyHeaderPadding(event);
			break;
		}
		return super.onTouchEvent(event);
	}

	// 获得hander的边距
	private void applyHeaderPadding(MotionEvent ev) {

		int pointerCount = ev.getHistorySize();

		for (int p = 0; p < pointerCount; p++) {
			if (mRefreshState == RELEASE_TO_REFRESH) {
				if (isVerticalFadingEdgeEnabled()) {
					setVerticalScrollBarEnabled(false);
				}

				int historicalY = (int) ev.getHistoricalY(p);

				// 计算申请的边距，除以1.7，使得拉动的效果更好
				int topPadding = (int) (((historicalY - mLastMotionY) - mRefreshViewHeight) / 1.7);

				mRefreshView.setPadding(mRefreshView.getPaddingLeft(),
						topPadding, mRefreshView.getPaddingRight(),
						mRefreshView.getPaddingBottom());
			}
		}
	}

	/**
	 * 将hander的边距重置为出事的数值
	 */
	private void resetHeaderPadding() {
		mRefreshView.setPadding(mRefreshView.getPaddingLeft(),
				mRefreshOriginalTopPadding, mRefreshView.getPaddingRight(),
				mRefreshView.getPaddingBottom());
	}

	/**
	 * 重置hander为之前的状态
	 */
	private void resetHeader() {
		if (mRefreshState != TAP_TO_REFRESH) {
			mRefreshState = TAP_TO_REFRESH;

			resetHeaderPadding();

			// 将刷新图标换成箭头
			mRefreshViewImage
					.setImageResource(R.drawable.ic_pulltorefresh_arrow);
			// 清除动画
			mRefreshViewImage.clearAnimation();
			// 隐藏图标和进度条
			mRefreshViewImage.setVisibility(View.GONE);
			mRefreshViewProgress.setVisibility(View.GONE);
		}
	}

	// 估算handview的width和height
	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}

		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
//			一个MeasureSpec封装了父布局传递给子布局的布局要求，
//			每个MeasureSpec代表了一组宽度和高度的要求。
//			一个MeasureSpec由大小和模式组成。
//			它有三种模式：UNSPECIFIED(未指定),
//			父元素不对子元素施加任何束缚，
//			子元素可以得到任意想要的大小；
//			EXACTLY(完全)，父元素决定自元素的确切大小，
//			子元素将被限定在给定的边界里而忽略它本身大小；
//			AT_MOST(至多)，
//			子元素至多达到指定大小的值。
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
		//第1次：scrollState = SCROLL_STATE_TOUCH_SCROLL(1) 正在滚动    
        //第2次：scrollState = SCROLL_STATE_FLING(2) 手指做了抛的动作（手指离开屏幕前，用力滑了一下）    
        //第3次：scrollState = SCROLL_STATE_IDLE(0) 停止滚动  

		// 在refreshview完全可见时，设置文字为松开刷新，同时翻转箭头
		if (mCurrentScrollState == SCROLL_STATE_TOUCH_SCROLL
				&& mRefreshState != REFRESHING) {
			if (firstVisibleItem == 0) {
				mRefreshViewImage.setVisibility(View.VISIBLE);
				if ((mRefreshView.getBottom() >= mRefreshViewHeight + 20 || mRefreshView
						.getTop() >= 0) && mRefreshState != RELEASE_TO_REFRESH) {
					mRefreshViewText.setText("鏉惧紑鍔犺浇...");
					mRefreshViewImage.clearAnimation();
					mRefreshViewImage.startAnimation(mFlipAnimation);
					mRefreshState = RELEASE_TO_REFRESH;
				} else if (mRefreshView.getBottom() < mRefreshViewHeight + 20
						&& mRefreshState != PULL_TO_REFRESH) {
					mRefreshViewText.setText("涓嬫媺鍒锋柊...");
					if (mRefreshState != TAP_TO_REFRESH) {
						mRefreshViewImage.clearAnimation();
						mRefreshViewImage.startAnimation(mReverseFlipAnimation);
					}
					mRefreshState = PULL_TO_REFRESH;
				}
			} else {
				mRefreshViewImage.setVisibility(View.GONE);
				resetHeader();
			}
		} else if (mCurrentScrollState == SCROLL_STATE_FLING
				&& firstVisibleItem == 0 && mRefreshState != REFRESHING) {
			setSelection(1);
			mBounceHack = true;
		} else if (mBounceHack && mCurrentScrollState == SCROLL_STATE_FLING) {
			setSelection(1);
		}

		if (mOnScrollListener != null) {
			mOnScrollListener.onScroll(view, firstVisibleItem,
					visibleItemCount, totalItemCount);
		}

		//
		if (mCurrentScrollState == OnScrollListener.SCROLL_STATE_IDLE) {
			// 计算最后可见条目的索引
			lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
			// 所有的条目已经和最大条数相等，则移除底部的View
			if (totalItemCount == MaxDateNum + 1) {
				removeFooterView(moreView);
				Toast.makeText(context, "数据全部加载完成，没有更多数据！", Toast.LENGTH_LONG)
						.show();
			}
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		mCurrentScrollState = scrollState;

		if (mOnScrollListener != null) {
			mOnScrollListener.onScrollStateChanged(view, scrollState);
		}
		if (mCurrentScrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& lastVisibleIndex == this.getAdapter().getCount()) {
			// 当滑到底部时自动加载
			pg.setVisibility(View.VISIBLE);
			bt.setVisibility(View.GONE);
		}
	}

	public void prepareForRefresh() {
		resetHeaderPadding();// 恢复header的边距

		mRefreshViewImage.setVisibility(View.GONE);
		// 注意加上，否则仍然显示之前的图片
		mRefreshViewImage.setImageDrawable(null);
		mRefreshViewProgress.setVisibility(View.VISIBLE);

		// 设置文字
		mRefreshViewText.setText("加载中...");

		mRefreshState = REFRESHING;
	}

	public void onRefresh() {

		if (mOnRefreshListener != null) {
			mOnRefreshListener.onRefresh();
		}
	}

	/**
	 * 重置listview为普通的listview，该方法设置最后更新时间
	 * 
	 * @param lastUpdated
	 *            Last updated at.
	 */
	public void onRefreshComplete(CharSequence lastUpdated) {
		setLastUpdated(lastUpdated);
		onRefreshComplete();
	}

	/**
	 * 重置listview为普通的listview，不设置最后更新时间
	 */
	public void onRefreshComplete() {

		resetHeader();

		// 如果refreshview在加载结束后可见，下滑到下一个条目
		if (mRefreshView.getBottom() > 0) {
			invalidateViews();//重画窗体
			setSelection(1);
		}
	}

	/**
	 * 刷新监听器接口
	 */
	public interface OnRefreshListener {
		/**
		 * list需要被刷新时调用
		 */
		public void onRefresh();
	}

	public void onMoreComplete() {
		// TODO Auto-generated method stub
		bt.setVisibility(View.GONE);
        pg.setVisibility(View.VISIBLE);
        isclick=false;
	}
	/**
	 * progress隐藏
	 */
	public void  dismissProgress(){
		 bt.setVisibility(View.GONE);
		 pg.setVisibility(View.INVISIBLE);
		 
	}
}