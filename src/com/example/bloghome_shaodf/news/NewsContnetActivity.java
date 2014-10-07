package com.example.bloghome_shaodf.news;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloghome_shaodf.PublicData;
import com.example.bloghome_shaodf.R;
import com.example.bloghome_shaodf.sql.NewsDataBaseManger;

@SuppressLint("SetJavaScriptEnabled")
public class NewsContnetActivity extends Activity implements OnTouchListener {

	Button btn_comment, btn_save, btn_outbox;
	ProgressDialog dialog;
	WebView hotnewswb;
	TextView title;
	String Message;
	String id;
	List<Contents> listContens;
	protected static final int SETGONE = 0;
	protected static final int SETWEBVIEW = 1;

	// 判断悬浮按钮的显示与隐藏
	boolean shownmenu = true;

	// 定义浮动窗口布局
	LinearLayout mFloatLayout;
	WindowManager.LayoutParams wmParams;
	// 创建浮动窗口设置布局参数的对象
	WindowManager mWindowManager;

	Handler hander = new Handler() {

		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {

			case SETGONE:
				mFloatLayout.setVisibility(View.GONE);
				break;
			case SETWEBVIEW:

				if (hotnewswb != null) {

					// 推荐使用该方法，
					hotnewswb.loadDataWithBaseURL(null, listContens.get(0)
							.getContent(), "text/html", "UTF-8", null);
					// 容易出现乱码
					// hotnewswb.loadData(data, mimeType, encoding)

				}
				break;
			default:
				break;
			}

		};

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 设置无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.hotnewscontent_activity);
		// 防止休眠
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setView();
		new DownLoadThreadContent().start();

	}

	/**
	 * 初始化数据
	 */
	private void setView() {

		Intent intent = getIntent();
		id = intent.getStringExtra("LINK");
		Message = intent.getStringExtra("Message");
		hotnewswb = (WebView) findViewById(R.id.hotnewscontnet_wb);
		title = (TextView) findViewById(R.id.hotnewscontents_title);
		title.setText(intent.getStringExtra("TITLE"));
		laodWebView(PublicData.hotNewsContentPath.replace("contenID", id));
		CreatefloatView();
		hotnewswb.setOnTouchListener(this);

	}

	/**
	 * 
	 * 网页加载
	 * 
	 * @param url
	 */
	private void laodWebView(String contents) {

//		// 设置WebView可执行JAVAScript
//		hotnewswb.getSettings().setJavaScriptEnabled(true);
//		// 清楚历史记录
//		hotnewswb.clearHistory();
//		// 提高渲染的优先级-加速处理
//		hotnewswb.getSettings().setRenderPriority(RenderPriority.HIGH);
//		// 把图片加载放在最后来加载渲染 ——进行加速
//		hotnewswb.getSettings().setBlockNetworkImage(true);
//		// 防止中文出现乱码
//
		hotnewswb.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		hotnewswb.getSettings().setDefaultTextEncodingName("UTF-8");

		//适应屏幕
		hotnewswb.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
		//优先缓存
		hotnewswb.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		
	}

	/**
	 * 创建悬浮按钮
	 */

	private void CreatefloatView() {
		// TODO Auto-generated method stub

		// 浮动窗口布局
		wmParams = new WindowManager.LayoutParams();

		// 获取 本地的Window管理器与本Activity相关联，随着Activity的出现而出现，消失而消失
		// mWindowManager=getApplication().getSystemService(Context.WINDOW_SERVICE);
		mWindowManager = this.getWindowManager();

		wmParams.type = LayoutParams.TYPE_PHONE;
		// 设置图片格式，效果为背景透明
		wmParams.format = PixelFormat.RGBA_8888;
		// 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
		wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
		// 调整悬浮窗显示的停靠位置为左侧置顶
		wmParams.gravity = Gravity.RIGHT | Gravity.CENTER;
		// 以屏幕左上角为原点，设置x、y初始值，相对于gravity
		wmParams.x = 0;
		wmParams.y = 0;
		// 设置悬浮窗口长宽数据
		wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		LayoutInflater inflater = this.getLayoutInflater();
		mFloatLayout = (LinearLayout) inflater.inflate(
				R.layout.floatview_contentactivity, null);
		// 添加视图
		mWindowManager.addView(mFloatLayout, wmParams);
		mFloatLayout.setVisibility(View.GONE);
		shownmenu = false;
		// mWindowManager.addView(mFloatLayout, wmParams);
		// 按钮监听
		btn_comment = (Button) mFloatLayout.findViewById(R.id.btn_comment);

		btn_save = (Button) mFloatLayout.findViewById(R.id.btn_save);
		btn_outbox = (Button) mFloatLayout.findViewById(R.id.btn_outbox);

		mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

		btn_comment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(NewsContnetActivity.this,
						NewsCommentsActivity.class);
				intent.putExtra("ID", id);
				intent.putExtra("title", listContens.get(0).getTitle());
				startActivity(intent);

				Toast.makeText(NewsContnetActivity.this, "评论", 0).show();

			}
		});
		btn_save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				saveData();
				Toast.makeText(NewsContnetActivity.this, "保存成功！", 0).show();

			}
		});
		btn_outbox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				hotnewswb.scrollTo(0, 0);

			}
		});

	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub

		switch (arg1.getAction()) {

		// 捕获手指按下的动作
		case MotionEvent.ACTION_DOWN:

			if (shownmenu == false) {

				mFloatLayout.setVisibility(View.VISIBLE);
				shownmenu = true;
			}

			break;
		// 捕获手指抬起的动作
		case MotionEvent.ACTION_UP:

			if (shownmenu == true) {
		
				hander.removeMessages(SETGONE);
				hander.sendEmptyMessageDelayed(SETGONE, 8000);
				
				shownmenu = false;
			}

			break;

		}

		return false;
	}

	@Override
	protected void onDestroy() {

		// 在Activity结束之后移除
		mWindowManager.removeView(mFloatLayout);
		super.onDestroy();
	}

	public class DownLoadThreadContent extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();

			// 下载新闻内容
			DownLoadContents downloadcontents = new DownLoadContents(
					PublicData.hotNewsContentPath.replace("contenID", id));
			listContens = downloadcontents.getMessage();

			// 下载完成后发送Hander通知

			hander.sendEmptyMessage(SETWEBVIEW);

		}
	}

	/**
	 * 保存新闻标题和内容
	 */
	private void saveData() {

		ContentValues values = new ContentValues();
		values.put("title", listContens.get(0).getTitle());
		values.put("content", listContens.get(0).getContent());
		NewsDataBaseManger manger = new NewsDataBaseManger(this, "newscontents");
		manger.saveData(values);
		
	}

}
