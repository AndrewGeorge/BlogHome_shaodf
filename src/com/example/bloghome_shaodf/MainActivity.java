package com.example.bloghome_shaodf;

import com.example.bloghome_shaodf.tools.SearchActivity;
import com.example.bloghome_shaodf.tools.SetActivity;
import com.example.bloghome_shaodf.blog.Blog_10_Activity;
import com.example.bloghome_shaodf.blog.Blog_48_Activity;
import com.example.bloghome_shaodf.blog.Blog_Main_Activity;
import com.example.bloghome_shaodf.collect.MarkActivity;
import com.example.bloghome_shaodf.collect.OfflineActivity;
import com.example.bloghome_shaodf.news.NewsMainActivity;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActivityGroup;
import android.app.NotificationManager;
import android.os.AsyncTask;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class MainActivity extends ActivityGroup {
	private LinearLayout myBody;
	public static View leftMenu; // ��߲˵���
	TextView hotNewsBtn, newestBtn, recommendBtn;
	public static boolean isopen = false; // ��߲˵���״̬
	private TextView title;
	private long mExitTime = 0;
	private TextView searchBtn;

	private TextView read48Btn;
	private TextView recommendRankBtn;
	private TextView recommendBlogs;
	private TextView history, exit;
	private TextView set;
	private TextView blogTag;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// �����ޱ���
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ����ȫ��
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.menu);

		// ע��㲥
		IntentFilter inf = new IntentFilter();
		inf.addAction("back");
		inf.addAction("open");
		inf.addAction("move");
		inf.addAction("tohistory");
		inf.addAction("switch_bg");
		registerReceiver(receiver, inf);
		
		
		
		title = (TextView) findViewById(R.id.title);
		myBody = (LinearLayout) findViewById(R.id.body);
		leftMenu = findViewById(R.id.left_menu);
		hotNewsBtn = (TextView) findViewById(R.id.hot_news);
		newestBtn = (TextView) findViewById(R.id.lately_news);
		recommendBtn = (TextView) findViewById(R.id.recommend_news);
		searchBtn = (TextView) findViewById(R.id.search);
		read48Btn = (TextView) findViewById(R.id.read48_rank);
		recommendRankBtn = (TextView) findViewById(R.id.recommend_rank);
		recommendBlogs = (TextView) findViewById(R.id.recommend_blogs);
		exit = (TextView) findViewById(R.id.exit);
		blogTag = (TextView) findViewById(R.id.booktag);
		history = (TextView) findViewById(R.id.history);
		set = (TextView) findViewById(R.id.settings);

		SharedPreferences spf = getSharedPreferences("SP", MODE_WORLD_READABLE);
		PublicData.isDownImg = spf.getBoolean("downImg", true);
		
		
		
		WindowManager wm = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		
		
		int width = wm.getDefaultDisplay().getWidth(); // ��Ļ���
		LinearLayout.LayoutParams lp = (LayoutParams) leftMenu
				.getLayoutParams();
		lp.width = width * 8 / 10; // ������߲˵������Ϊ��Ļ4/5
		lp.leftMargin = -width * 8 / 10; // ������߲˵����������Ϊ -������
		leftMenu.setLayoutParams(lp);
		ViewGroup.LayoutParams lp1 = myBody.getLayoutParams();
		lp1.width = width;
		myBody.setLayoutParams(lp1);

		ViewGroup.LayoutParams lp2 = title.getLayoutParams();
		lp2.width = width;
		title.setLayoutParams(lp2);

		int[] i = { R.drawable.commlist_head_bg, R.drawable.left_menu_bg4,
				R.drawable.left_menu_bg5, R.drawable.lrcb };
		leftMenu.setBackgroundResource(i[spf.getInt("bg", 0)]);

		// ���������activity
		myBody.addView(getLocalActivityManager().startActivity(
				"1",
				new Intent(MainActivity.this, NewsMainActivity.class).putExtra(
						"URL", PublicData.hotNewsPath)).getDecorView());
		title.setText("��������");

		// ��������
		hotNewsBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//
				myBody.removeAllViews();
				new AsynMove().execute();
				myBody.addView(getLocalActivityManager().startActivity(
						"1",
						new Intent(MainActivity.this, NewsMainActivity.class)
								.putExtra("URL", PublicData.hotNewsPath)
								.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
						.getDecorView());
				isopen = false;
				title.setText("��������");
			}
		});

		// ��������
		newestBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//

				myBody.removeAllViews();
				new AsynMove().execute();
				myBody.addView(getLocalActivityManager().startActivity(
						"1",
						new Intent(MainActivity.this, NewsMainActivity.class)
								.putExtra(
										"URL",
										PublicData.TheNewNewsPath.replace(
												"pageIndex", "1").replace(
												"pageSize", "10")).addFlags(
										Intent.FLAG_ACTIVITY_CLEAR_TOP))
						.getDecorView());
				isopen = false;
				title.setText("��������");

			}
		});
		// �Ƽ�����
		recommendBtn.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				//

				myBody.removeAllViews();
				new AsynMove().execute();
				myBody.addView(getLocalActivityManager().startActivity(
						"1",
						new Intent(MainActivity.this, NewsMainActivity.class)
								.putExtra(
										"URL",
										PublicData.RecomendNewsPath.replace(
												"pageIndex", "1").replace(
												"pageSize", "10")).addFlags(
										Intent.FLAG_ACTIVITY_CLEAR_TOP))
						.getDecorView());
				isopen = false;
				title.setText("�Ƽ�����");
			}
		});
		// ���в���
		recommendBlogs.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {

				myBody.removeAllViews();
				new AsynMove().execute();
				myBody.addView(getLocalActivityManager().startActivity(
						"1",
						new Intent(MainActivity.this, Blog_Main_Activity.class)
								.putExtra("URL", PublicData.hotNewsPath)
								.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
						.getDecorView());
				isopen = false;
				title.setText("���в���");
			}
		});
		// 48Сʱ�Ķ�����
		read48Btn.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				//
				myBody.removeAllViews();
				new AsynMove().execute();
				myBody.addView(getLocalActivityManager().startActivity(
						"1",
						new Intent(MainActivity.this, Blog_48_Activity.class)
								.putExtra("URL", PublicData.hotNewsPath)
								.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
						.getDecorView());
				isopen = false;
				title.setText("48Сʱ�Ķ�����");
			}
		});
		// 10�����Ƽ�����
		recommendRankBtn.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				//
				myBody.removeAllViews();
				new AsynMove().execute();
				myBody.addView(getLocalActivityManager().startActivity(
						"1",
						new Intent(MainActivity.this, Blog_10_Activity.class)
								.putExtra("URL", PublicData.hotNewsPath)
								.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
						.getDecorView());
				isopen = false;
				title.setText("10�����Ƽ�����");
			}
		});

		// 7.��ǩ
		blogTag.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				//
				myBody.removeAllViews();
				new AsynMove().execute();
				myBody.addView(getLocalActivityManager().startActivity(
						"7",
						new Intent(MainActivity.this, MarkActivity.class)
								.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
						.getDecorView());
				isopen = false;
				title.setText("��ǩ");
			}
		});
		// 8.�������
		history.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				//
				myBody.removeAllViews();
				new AsynMove().execute();
				myBody.addView(getLocalActivityManager().startActivity(
						"8",
						new Intent(MainActivity.this, OfflineActivity.class)
								.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
						.getDecorView());
				isopen = false;
				title.setText("�������");
			}
		});

		// 9.����
		searchBtn.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				//
				myBody.removeAllViews();
				new AsynMove().execute();
				myBody.addView(getLocalActivityManager().startActivity(
						"9",
						new Intent(MainActivity.this, SearchActivity.class)
								.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
						.getDecorView());
				isopen = false;
				title.setText("����");

			}
		});
		// 10.����
		set.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				//
				myBody.removeAllViews();
				new AsynMove().execute();
				myBody.addView(getLocalActivityManager().startActivity(
						"10",
						new Intent(MainActivity.this, SetActivity.class)
								.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
						.getDecorView());
				isopen = false;
				title.setText("����");

			}
		});
		// 11.�˳�
		exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				NotificationManager manger = (NotificationManager) MainActivity.this
						.getSystemService(Context.NOTIFICATION_SERVICE);
				manger.cancel(10);
				System.exit(-1);
			}
		});
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@SuppressLint("ResourceAsColor")
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stu
			if (intent.getAction().equals("open")) {
				new AsynMove().execute();
			}
			if (intent.getAction().equals("back")) {
				isopen = false;
				new AsynMove().execute();
			}
			if (intent.getAction().equals("move")) {
				float f = intent.getFloatExtra("distance", 0);
				float old = intent.getFloatExtra("old", 150);
				LinearLayout.LayoutParams lp = (LayoutParams) leftMenu
						.getLayoutParams();
				if (isopen && f < 0) {
					lp.leftMargin = (int) f;

				} else if (!isopen && f > 0 && old < 100) {
					lp.leftMargin = (int) (-leftMenu.getWidth() + f);
				}
				leftMenu.setLayoutParams(lp);
			}

			// �л�����ͼƬ
			if (intent.getAction().equals("switch_bg")) {

				// ���ܵ�ͼƬ�㲥��������
				leftMenu.setBackgroundResource(SetActivity.imgs[intent
						.getIntExtra("img", 0)]);
			}
			// �������
			if (intent.getAction().equals("tohistory")) {

			}
		}

	};

	/**
	 * ������߲˵���
	 */
	private class AsynMove extends AsyncTask<Void, Integer, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			LinearLayout.LayoutParams lp = (LayoutParams) leftMenu
					.getLayoutParams();
			int moveX = -lp.leftMargin;

			int times = moveX / 10;
			if (times <= 0) {
				times = leftMenu.getWidth() / 10;
			}
			for (int i = 0; i < times + 2; i++) {
				publishProgress(0);
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			LinearLayout.LayoutParams lp = (LayoutParams) leftMenu
					.getLayoutParams();
			if (isopen) {
				lp.leftMargin = Math.min(lp.leftMargin + 20, 0);
			} else {
				lp.leftMargin = Math.max(lp.leftMargin - 20,
						-leftMenu.getWidth());
			}

			leftMenu.setLayoutParams(lp);
		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_UP) {
			if (!isopen) {
				isopen = true;
				new AsynMove().execute();
			} else {
				if ((System.currentTimeMillis() - mExitTime) > 2000) {// ������ΰ���ʱ��������2000���룬���˳�
					Toast.makeText(this, "�ٰ�һ���˳�����", Toast.LENGTH_SHORT).show();
					mExitTime = System.currentTimeMillis();// ����mExitTime
				} else {
					finish();// �����˳�����
				}
			}
			return true;
		}
		if (event.getKeyCode() == KeyEvent.KEYCODE_MENU
				&& event.getAction() == KeyEvent.ACTION_UP) {
			if (!isopen) {
				isopen = true;
			} else {
				isopen = false;
			}
			new AsynMove().execute();
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		isopen = false;
		unregisterReceiver(receiver);
	}
}
