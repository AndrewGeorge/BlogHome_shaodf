package com.example.bloghome_shaodf.news;

import com.example.bloghome_shaodf.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class XListViewHeader extends LinearLayout {
	private LinearLayout mContainer;
	private ImageView mArrowImageView;
	private TextView mHintTextView;
	private int mState = STATE_NORMAL;

	private Animation mRotateUpAnim;
	private Animation mRotateDownAnim;

	private final int ROTATE_ANIM_DURATION = 180;

	public final static int STATE_NORMAL = 0;
	public final static int STATE_READY = 1;
	public final static int STATE_REFRESHING = 2;
	private Animation mPBAnimation;

	public XListViewHeader(Context context) {
		super(context);
		initView(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public XListViewHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	// 设置初始值
	private void initView(Context context) {

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, 0);

		mContainer = (LinearLayout) LayoutInflater.from(context).inflate(
				R.layout.xlistview_header, null);
		addView(mContainer, lp);

		// 设置位置
		setGravity(Gravity.BOTTOM);
		// 箭头图片
		mArrowImageView = (ImageView) findViewById(R.id.xlistview_header_arrow);
		// 提示文本框
		mHintTextView = (TextView) findViewById(R.id.xlistview_header_hint_textview);

		// 获取到加载动画
		mPBAnimation = AnimationUtils.loadAnimation(context, R.anim.loadmore);
		// 设置旋转速度
		LinearInterpolator lin = new LinearInterpolator();
		mPBAnimation.setInterpolator(lin);
		// 以节点中心变化，箭头的变化
		mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		// 判断是否为编辑器依赖的编码。防止无法识别自定义的控件
		if (isInEditMode()) {
			// 结束方法，返回被调用处
			return;
		}
		// 设置持续时间
		mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
		// 终止填充效果
		mRotateUpAnim.setFillAfter(true);
		// 将箭头反向设置
		mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateDownAnim.setFillAfter(true);
	}

	public void setState(int state) {

		if (state == mState)
			return;
		// 箭头图片清楚动画
		mArrowImageView.clearAnimation();
		// 判断是否为开启状态
		if (state == STATE_REFRESHING) {
			// 重新设置 图片加载时的选装图片
			mArrowImageView.setImageResource(R.drawable.icon_loading_selected);
			// 并开启动画
			mArrowImageView.startAnimation(mPBAnimation);
		} else {
			// 未开启就设置为箭头
			mArrowImageView.setImageResource(R.drawable.xlistview_arrow);
		}
		// 箭头的方向的设置
		switch (state) {

		//正常状态
		case STATE_NORMAL:

			if (mState == STATE_READY) {

				mArrowImageView.startAnimation(mRotateDownAnim);
			}
			if (mState == STATE_REFRESHING) {
				mArrowImageView.clearAnimation();
			}
			mHintTextView.setText(R.string.xlistview_header_hint_normal);
			break;
		//准备状态
		case STATE_READY:
			if (mState != STATE_READY) {
				mArrowImageView.clearAnimation();
				mArrowImageView.startAnimation(mRotateUpAnim);
				mHintTextView.setText(R.string.xlistview_header_hint_ready);
			}
			break;
		//加载状态
		case STATE_REFRESHING:
			mHintTextView.setText(R.string.xlistview_header_hint_loading);
			break;
		}
		//一系列动作结束后重新设置为标准状态
		mState = state;
	}

/**
 * 
 * 设置高度
 * @param height
 */
	public void setVisiableHeight(int height) {
		if (height < 0)
			height = 0;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContainer
				.getLayoutParams();
		lp.height = height;
		
		//设置视图参数
		mContainer.setLayoutParams(lp);
	}
/**
 * 获取头部文件的高度
 * @return
 */
	public int getVisiableHeight() {
		return mContainer.getHeight();
	}

}
