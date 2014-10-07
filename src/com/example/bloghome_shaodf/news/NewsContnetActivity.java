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

	// �ж�������ť����ʾ������
	boolean shownmenu = true;

	// ���帡�����ڲ���
	LinearLayout mFloatLayout;
	WindowManager.LayoutParams wmParams;
	// ���������������ò��ֲ����Ķ���
	WindowManager mWindowManager;

	Handler hander = new Handler() {

		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {

			case SETGONE:
				mFloatLayout.setVisibility(View.GONE);
				break;
			case SETWEBVIEW:

				if (hotnewswb != null) {

					// �Ƽ�ʹ�ø÷�����
					hotnewswb.loadDataWithBaseURL(null, listContens.get(0)
							.getContent(), "text/html", "UTF-8", null);
					// ���׳�������
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
		// �����ޱ���
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ����ȫ��
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.hotnewscontent_activity);
		// ��ֹ����
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setView();
		new DownLoadThreadContent().start();

	}

	/**
	 * ��ʼ������
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
	 * ��ҳ����
	 * 
	 * @param url
	 */
	private void laodWebView(String contents) {

//		// ����WebView��ִ��JAVAScript
//		hotnewswb.getSettings().setJavaScriptEnabled(true);
//		// �����ʷ��¼
//		hotnewswb.clearHistory();
//		// �����Ⱦ�����ȼ�-���ٴ���
//		hotnewswb.getSettings().setRenderPriority(RenderPriority.HIGH);
//		// ��ͼƬ���ط��������������Ⱦ �������м���
//		hotnewswb.getSettings().setBlockNetworkImage(true);
//		// ��ֹ���ĳ�������
//
		hotnewswb.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		hotnewswb.getSettings().setDefaultTextEncodingName("UTF-8");

		//��Ӧ��Ļ
		hotnewswb.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
		//���Ȼ���
		hotnewswb.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		
	}

	/**
	 * ����������ť
	 */

	private void CreatefloatView() {
		// TODO Auto-generated method stub

		// �������ڲ���
		wmParams = new WindowManager.LayoutParams();

		// ��ȡ ���ص�Window�������뱾Activity�����������Activity�ĳ��ֶ����֣���ʧ����ʧ
		// mWindowManager=getApplication().getSystemService(Context.WINDOW_SERVICE);
		mWindowManager = this.getWindowManager();

		wmParams.type = LayoutParams.TYPE_PHONE;
		// ����ͼƬ��ʽ��Ч��Ϊ����͸��
		wmParams.format = PixelFormat.RGBA_8888;
		// ���ø������ڲ��ɾ۽���ʵ�ֲ���������������������ɼ����ڵĲ�����
		wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
		// ������������ʾ��ͣ��λ��Ϊ����ö�
		wmParams.gravity = Gravity.RIGHT | Gravity.CENTER;
		// ����Ļ���Ͻ�Ϊԭ�㣬����x��y��ʼֵ�������gravity
		wmParams.x = 0;
		wmParams.y = 0;
		// �����������ڳ�������
		wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		LayoutInflater inflater = this.getLayoutInflater();
		mFloatLayout = (LinearLayout) inflater.inflate(
				R.layout.floatview_contentactivity, null);
		// �����ͼ
		mWindowManager.addView(mFloatLayout, wmParams);
		mFloatLayout.setVisibility(View.GONE);
		shownmenu = false;
		// mWindowManager.addView(mFloatLayout, wmParams);
		// ��ť����
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

				Toast.makeText(NewsContnetActivity.this, "����", 0).show();

			}
		});
		btn_save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				saveData();
				Toast.makeText(NewsContnetActivity.this, "����ɹ���", 0).show();

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

		// ������ָ���µĶ���
		case MotionEvent.ACTION_DOWN:

			if (shownmenu == false) {

				mFloatLayout.setVisibility(View.VISIBLE);
				shownmenu = true;
			}

			break;
		// ������ָ̧��Ķ���
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

		// ��Activity����֮���Ƴ�
		mWindowManager.removeView(mFloatLayout);
		super.onDestroy();
	}

	public class DownLoadThreadContent extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();

			// ������������
			DownLoadContents downloadcontents = new DownLoadContents(
					PublicData.hotNewsContentPath.replace("contenID", id));
			listContens = downloadcontents.getMessage();

			// ������ɺ���Hander֪ͨ

			hander.sendEmptyMessage(SETWEBVIEW);

		}
	}

	/**
	 * �������ű��������
	 */
	private void saveData() {

		ContentValues values = new ContentValues();
		values.put("title", listContens.get(0).getTitle());
		values.put("content", listContens.get(0).getContent());
		NewsDataBaseManger manger = new NewsDataBaseManger(this, "newscontents");
		manger.saveData(values);
		
	}

}
