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

	// ���ó�ʼֵ
	private void initView(Context context) {

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, 0);

		mContainer = (LinearLayout) LayoutInflater.from(context).inflate(
				R.layout.xlistview_header, null);
		addView(mContainer, lp);

		// ����λ��
		setGravity(Gravity.BOTTOM);
		// ��ͷͼƬ
		mArrowImageView = (ImageView) findViewById(R.id.xlistview_header_arrow);
		// ��ʾ�ı���
		mHintTextView = (TextView) findViewById(R.id.xlistview_header_hint_textview);

		// ��ȡ�����ض���
		mPBAnimation = AnimationUtils.loadAnimation(context, R.anim.loadmore);
		// ������ת�ٶ�
		LinearInterpolator lin = new LinearInterpolator();
		mPBAnimation.setInterpolator(lin);
		// �Խڵ����ı仯����ͷ�ı仯
		mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		// �ж��Ƿ�Ϊ�༭�������ı��롣��ֹ�޷�ʶ���Զ���Ŀؼ�
		if (isInEditMode()) {
			// �������������ر����ô�
			return;
		}
		// ���ó���ʱ��
		mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
		// ��ֹ���Ч��
		mRotateUpAnim.setFillAfter(true);
		// ����ͷ��������
		mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateDownAnim.setFillAfter(true);
	}

	public void setState(int state) {

		if (state == mState)
			return;
		// ��ͷͼƬ�������
		mArrowImageView.clearAnimation();
		// �ж��Ƿ�Ϊ����״̬
		if (state == STATE_REFRESHING) {
			// �������� ͼƬ����ʱ��ѡװͼƬ
			mArrowImageView.setImageResource(R.drawable.icon_loading_selected);
			// ����������
			mArrowImageView.startAnimation(mPBAnimation);
		} else {
			// δ����������Ϊ��ͷ
			mArrowImageView.setImageResource(R.drawable.xlistview_arrow);
		}
		// ��ͷ�ķ��������
		switch (state) {

		//����״̬
		case STATE_NORMAL:

			if (mState == STATE_READY) {

				mArrowImageView.startAnimation(mRotateDownAnim);
			}
			if (mState == STATE_REFRESHING) {
				mArrowImageView.clearAnimation();
			}
			mHintTextView.setText(R.string.xlistview_header_hint_normal);
			break;
		//׼��״̬
		case STATE_READY:
			if (mState != STATE_READY) {
				mArrowImageView.clearAnimation();
				mArrowImageView.startAnimation(mRotateUpAnim);
				mHintTextView.setText(R.string.xlistview_header_hint_ready);
			}
			break;
		//����״̬
		case STATE_REFRESHING:
			mHintTextView.setText(R.string.xlistview_header_hint_loading);
			break;
		}
		//һϵ�ж�����������������Ϊ��׼״̬
		mState = state;
	}

/**
 * 
 * ���ø߶�
 * @param height
 */
	public void setVisiableHeight(int height) {
		if (height < 0)
			height = 0;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContainer
				.getLayoutParams();
		lp.height = height;
		
		//������ͼ����
		mContainer.setLayoutParams(lp);
	}
/**
 * ��ȡͷ���ļ��ĸ߶�
 * @return
 */
	public int getVisiableHeight() {
		return mContainer.getHeight();
	}

}
