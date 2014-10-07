package com.example.bloghome_shaodf.news;

import com.example.bloghome_shaodf.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class XListViewFooter extends LinearLayout {
	public final static int STATE_NORMAL = 0;
	public final static int STATE_READY = 1;
	public final static int STATE_LOADING = 2;

	private Context mContext;

	private View mContentView;
	private TextView mHintView;
	private ImageView mLoadIcon;
	private Animation animation;

	public XListViewFooter(Context context) {
		super(context);
		initView(context);
	}

	public XListViewFooter(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	/*
	 * 设置加载更多的状态
	 */
	public void setState(int state) {
		switch (state) {
		// 将listView 上拉是处在该状态
		case STATE_READY:
			mLoadIcon.clearAnimation();
			mLoadIcon.setImageResource(R.drawable.icon_loading_not_selected);
			mHintView.setText(R.string.xlistview_footer_hint_ready);
			break;
		// 加载是图片的旋转设置
		case STATE_LOADING:
			mHintView.setText(R.string.xlistview_header_hint_loading);
			mLoadIcon.setImageResource(R.drawable.icon_loading_selected);
			mLoadIcon.startAnimation(animation);
			break;
		default:
			// 底部默认的视图
			mLoadIcon.clearAnimation();
			mHintView.setText(R.string.xlistview_footer_hint_normal);
			mLoadIcon.setImageResource(R.drawable.icon_loading_not_selected);
			break;
		}
	}

	// 对底部视图的布局
	public void setBottomMargin(int height) {
		if (height < 0)
			return;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView
				.getLayoutParams();
		lp.bottomMargin = height;
		mContentView.setLayoutParams(lp);
	}

	public int getBottomMargin() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView
				.getLayoutParams();
		return lp.bottomMargin;
	}

	/**
	 * 标准状态下的视图那个点击按钮
	 */
	public void normal() {
		mHintView.setVisibility(View.VISIBLE);
	}

	/**
	 * 加载状态
	 */
	public void loading() {
		// 将按钮隐藏
		mHintView.setVisibility(View.GONE);
		mHintView.setText(R.string.xlistview_header_hint_loading);
		// 重新设置为加载时的图片
		mLoadIcon.setImageResource(R.drawable.icon_loading_selected);
	}

	/**
	 * 隐藏加载更多设置高度为0
	 */
	public void hide() {

		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView
				.getLayoutParams();
		lp.height = 0;
		mContentView.setLayoutParams(lp);
	}

	/**
	 * 再次显示按钮
	 */
	public void show() {

		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView
				.getLayoutParams();
		lp.height = LayoutParams.WRAP_CONTENT;
		mContentView.setLayoutParams(lp);

	}

	// 初始化数据
	private void initView(Context context) {

		mContext = context;
		// 获取布局文件对象
		LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext)
				.inflate(R.layout.xlistview_footer, null);
		addView(moreView);
		// 设置布局文件的
		// FILL_PARENT,即View希望和父容器一样大；
		// WRAP_CONTENT,指当前的View的大小只需要包裹住View里面的内容即可
		moreView.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		//rretliven那个布局
		mContentView = moreView.findViewById(R.id.xlistview_footer_content);
		mHintView = (TextView) moreView
				.findViewById(R.id.xlistview_footer_hint_textview);
		mLoadIcon = (ImageView) moreView.findViewById(R.id.iv_load);
		//获取加载时的动画
		animation = AnimationUtils.loadAnimation(mContext, R.anim.loadmore);
		//加载速度
		LinearInterpolator lin = new LinearInterpolator();
		animation.setInterpolator(lin);
	}
}
