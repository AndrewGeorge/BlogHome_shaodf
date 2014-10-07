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
	 * ���ü��ظ����״̬
	 */
	public void setState(int state) {
		switch (state) {
		// ��listView �����Ǵ��ڸ�״̬
		case STATE_READY:
			mLoadIcon.clearAnimation();
			mLoadIcon.setImageResource(R.drawable.icon_loading_not_selected);
			mHintView.setText(R.string.xlistview_footer_hint_ready);
			break;
		// ������ͼƬ����ת����
		case STATE_LOADING:
			mHintView.setText(R.string.xlistview_header_hint_loading);
			mLoadIcon.setImageResource(R.drawable.icon_loading_selected);
			mLoadIcon.startAnimation(animation);
			break;
		default:
			// �ײ�Ĭ�ϵ���ͼ
			mLoadIcon.clearAnimation();
			mHintView.setText(R.string.xlistview_footer_hint_normal);
			mLoadIcon.setImageResource(R.drawable.icon_loading_not_selected);
			break;
		}
	}

	// �Եײ���ͼ�Ĳ���
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
	 * ��׼״̬�µ���ͼ�Ǹ������ť
	 */
	public void normal() {
		mHintView.setVisibility(View.VISIBLE);
	}

	/**
	 * ����״̬
	 */
	public void loading() {
		// ����ť����
		mHintView.setVisibility(View.GONE);
		mHintView.setText(R.string.xlistview_header_hint_loading);
		// ��������Ϊ����ʱ��ͼƬ
		mLoadIcon.setImageResource(R.drawable.icon_loading_selected);
	}

	/**
	 * ���ؼ��ظ������ø߶�Ϊ0
	 */
	public void hide() {

		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView
				.getLayoutParams();
		lp.height = 0;
		mContentView.setLayoutParams(lp);
	}

	/**
	 * �ٴ���ʾ��ť
	 */
	public void show() {

		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView
				.getLayoutParams();
		lp.height = LayoutParams.WRAP_CONTENT;
		mContentView.setLayoutParams(lp);

	}

	// ��ʼ������
	private void initView(Context context) {

		mContext = context;
		// ��ȡ�����ļ�����
		LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext)
				.inflate(R.layout.xlistview_footer, null);
		addView(moreView);
		// ���ò����ļ���
		// FILL_PARENT,��Viewϣ���͸�����һ����
		// WRAP_CONTENT,ָ��ǰ��View�Ĵ�Сֻ��Ҫ����סView��������ݼ���
		moreView.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		//rretliven�Ǹ�����
		mContentView = moreView.findViewById(R.id.xlistview_footer_content);
		mHintView = (TextView) moreView
				.findViewById(R.id.xlistview_footer_hint_textview);
		mLoadIcon = (ImageView) moreView.findViewById(R.id.iv_load);
		//��ȡ����ʱ�Ķ���
		animation = AnimationUtils.loadAnimation(mContext, R.anim.loadmore);
		//�����ٶ�
		LinearInterpolator lin = new LinearInterpolator();
		animation.setInterpolator(lin);
	}
}
