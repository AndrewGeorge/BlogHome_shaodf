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

	// ʵ�ֹ�����������lietView���ƶ�
	private float mLastY = -1; // �¼��ĳ�ʼ״̬
	// Scroller����Ϊ��ʵ��Viewƽ��������һ��Helper��
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
	// ���������ʱ��
	private final static int SCROLL_DURATION = 400;
	// �������س���50px�󴥷������¼�
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

	// ��ʼ������
	private void initWithContext(Context context) {

		// ͨ�����Զ����Viewʱʹ�ã���View�ж���һ��˽�г�ԱmScroller = new
		// Scroller(context)������mScroller������λ��ʱ�������ᵼ��View�Ĺ�����ͨ������mScroller��¼/����View������λ�ã�
		// ����дView��computeScroll()�����ʵ�ʵĹ�����

		mScroller = new Scroller(context, new DecelerateInterpolator());
		super.setOnScrollListener(this);

		// ʵ��ͷ����ͼ
		mHeaderView = new XListViewHeader(context);

		mHeaderViewContent = (RelativeLayout) mHeaderView
				.findViewById(R.id.xlistview_header_content);

		mHeaderTimeView = (TextView) mHeaderView
				.findViewById(R.id.xlistview_header_time);
		// ���ͷ��
		addHeaderView(mHeaderView);

		// ���õײ���ͼ
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

	// ���������
	@Override
	public void setAdapter(ListAdapter adapter) {
		// ȷ���ײ�Ϊ����ͼΪ���һ����ͼ
		if (mIsFooterReady == false) {
			mIsFooterReady = true;
			addFooterView(mFooterView);
		}
		super.setAdapter(adapter);
	}

	/**
	 * �Ƿ���ˢ��
	 * 
	 * @param enable
	 */
	public void setPullRefreshEnable(boolean enable) {

		mEnablePullRefresh = enable;
		// �ж��Ƿ��ڿ���״̬
		if (!mEnablePullRefresh) {

			mHeaderViewContent.setVisibility(View.INVISIBLE);
		} else {

			mHeaderViewContent.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * �Ƿ������ظ���
	 * 
	 * @param enable
	 */
	public void setPullLoadEnable(boolean enable) {
		mEnablePullLoad = enable;

		// ������ʱ����
		if (!mEnablePullLoad) {

			mFooterView.hide();

			mFooterView.setOnClickListener(null);
		} else {
			// ����Ϊ����
			mPullLoading = false;
			mFooterView.show();
			mFooterView.setState(XListViewFooter.STATE_NORMAL);
			// ��������¼�
			mFooterView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					startLoadMore();
				}
			});
		}
	}

	/**
	 * ֹͣ���º��������ø��µ���ͼ�ĸ߶�
	 */
	public void stopRefresh() {
		
		if (mPullRefreshing == true) {
			mPullRefreshing = false;
			resetHeaderHeight();
			SimpleDateFormat sDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd    hh:mm:ss");
			Date curDate = new Date(System.currentTimeMillis());// ��ȡ��ǰʱ��
			String str = sDateFormat.format(curDate);
			setRefreshTime(str);
		}
	}

	/**
	 * ֹͣ���غ��������ü��ص���ͼ.
	 */
	public void stopLoadMore() {
		if (mPullLoading == true) {
			mPullLoading = false;
			mFooterView.setState(XListViewFooter.STATE_NORMAL);
		}
	}

	/**
	 * ����������ʱ��
	 * 
	 * @param time
	 */
	public void setRefreshTime(String time) {
		mHeaderTimeView.setText(time);
	}

	private void invokeOnScrolling() {

		// instanceof�ж����Ƿ�Ϊ����
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
		// ���ص�����
		setSelection(0);
	}

	/**
	 * ��������ˢ�µ���ͼ�ĸ߶ȡ�����������֮��ͷ���ļ�����ʧ��Ҫ����
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
			// ���ø߶���ƽ���»���
			invalidate();
		}
	}

	// ��ʼ���ظ���
	private void startLoadMore() {

		mPullLoading = true;
		mFooterView.setState(XListViewFooter.STATE_LOADING);
		if (mListViewListener != null) {

			//���ýӿ��еķ�����Ҫ��ʵ�ָýӿڵ�������ʵ��
			mListViewListener.onLoadMore();
		}
	}

	// ���Ƶļ������ж��Ƿ���
	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		// getY��ȡ��Ե�ǰ�ؼ�����ֱ����
		// getRawY��ȡ������Ͻ�

		if (mLastY == -1) {
			mLastY = ev.getRawY();
		}

		switch (ev.getAction()) {

		case MotionEvent.ACTION_DOWN:

			mLastY = ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:

			// ��������
			final float deltaY = ev.getRawY() - mLastY;
			mLastY = ev.getRawY();

			if (getFirstVisiblePosition() == 0

			&& (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)) {

				// ����ĸ߶�����Ϊ��������
				updateHeaderHeight(deltaY / OFFSET_RADIO);
				// ���ü���
				invokeOnScrolling();

			} else if (getLastVisiblePosition() == mTotalItemCount - 1
					&& (mFooterView.getBottomMargin() > 0 || deltaY < 0)
					&& getCount() > 10) {

				updateFooterHeight(-deltaY / OFFSET_RADIO);
			}

			break;
		default:

			mLastY = -1; // reset
			//�ж��Ƿ�Ϊ��һ��λ��
			if (getFirstVisiblePosition() == 0) {
				
				if (mEnablePullRefresh
						&& mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
					
					mPullRefreshing = true;
					//��ʼˢ��
					mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
					//�жϽӿ��Ƿ�Ϊ��
					if (mListViewListener != null) {
						
						//���ýӿ��еķ�����Ҫ��ʵ�ָýӿڵ�������ʵ��
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

				// ��������ľ��������µ�
				resetFooterHeight();
			}
			break;
		}
		return super.onTouchEvent(ev);
	}

	// ʵ�ʵĹ���
	@Override
	public void computeScroll() {

		if (mScroller.computeScrollOffset()) {

			if (mScrollBack == SCROLLBACK_HEADER) {
				// ���ÿɼ������ø߶�Ϊ��ǰ�Ĺ���������ֵλ��
				mHeaderView.setVisiableHeight(mScroller.getCurrY());
			} else {
				mFooterView.setBottomMargin(mScroller.getCurrY());
			}
			// ֱ�ӵ����߳�ˢ�½���
			postInvalidate();
			invokeOnScrolling();
		}
		super.computeScroll();
	}

	@Override
	public void setOnScrollListener(OnScrollListener l) {
		mScrollListener = l;
	}

	// ʵ��OnScrollListener�󽫻���д����������
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

		if (mScrollListener != null) {
			mScrollListener.onScrollStateChanged(view, scrollState);
		}

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		// ����ʱһֱ�ص���ֱ��ֹͣ����ʱ��ֹͣ�ص�������ʱ�ص�һ�Ρ�
		// firstVisibleItem����ǰ�ܿ����ĵ�һ���б���ID����0��ʼ��
		// visibleItemCount����ǰ�ܿ������б��������С���Ҳ�㣩
		// totalItemCount���б����

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
	 * Ҳ����ʵ������ӿ�
	 * 
	 */
	public interface OnXScrollListener extends OnScrollListener {
		public void onXScrolling(View view);
	}

	/**
	 * �Լ��Ľӿ�����
	 */
	public interface IXListViewListener {

		public void onRefresh();

		public void onLoadMore();
	}

	// ���÷������ü���ʱ�Ķ���
	public void setFooterBackgroundRes(int resId) {

		if (mFooterView != null) {

			mFooterView.setBackgroundResource(resId);
		}

	}

	// ���ü��ظ���ı�����ɫ
	public void setFooterBackgroundColor(int resId) {

		if (mFooterView != null) {

			mFooterView.setBackgroundColor(resId);
			mFooterView.invalidate();
		}

	}

	/**
	 * �Ƿ���
	 */
	public boolean isLoading() {

		return mPullLoading;
	}

}
