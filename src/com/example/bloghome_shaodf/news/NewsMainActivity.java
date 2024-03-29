package com.example.bloghome_shaodf.news;

import java.util.List;

import com.example.bloghome_shaodf.MainActivity;
import com.example.bloghome_shaodf.PublicData;
import com.example.bloghome_shaodf.R;
import com.example.bloghome_shaodf.collect.TouchListener;
import com.example.bloghome_shaodf.news.XListView.IXListViewListener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class NewsMainActivity extends Activity implements IXListViewListener {

	XListView hotnewslistview;
	ProgressDialog pro;
	List<Entry> entrylist;
	TouchListener touchListener;

	String path;


	int i=10;

	protected static final int REFRESHING = 0;
	protected static final int LOADMOREING = 1;
	protected static final int SHOWPROGRESS= 2;
	
	NewsMainAdapter mainnewsadapter;
	/**
	 * 
	 * 用来更新数据
	 */
	Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			
			case REFRESHING:
				hotnewslistview.stopRefresh();
				break;
			case LOADMOREING:
			
				hotnewslistview.stopLoadMore();
				break;
			
			case SHOWPROGRESS:
				setAdapter();
				pro.dismiss();
				Toast.makeText(NewsMainActivity.this, "下载完成！", 1).show();
				break;
				

			}
			
			if(mainnewsadapter!=null){
				
				mainnewsadapter.setData(entrylist);
				mainnewsadapter.notifyDataSetChanged();
				
			}

		};
	};

	
	//填充自定义listview
	private void setAdapter() {
		// TODO Auto-generated method stub
		mainnewsadapter = new NewsMainAdapter(
				NewsMainActivity.this, entrylist);
		hotnewslistview.setAdapter(mainnewsadapter);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		 //设置无标题  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        //设置全屏  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
                WindowManager.LayoutParams.FLAG_FULLSCREEN); 
        
        
		setContentView(R.layout.hotnews_activity);
	
		setView();
	}

	/**
	 * 初始化View
	 */
	private void setView() {
	
		Intent intent = getIntent();
		path = intent.getStringExtra("URL");
		hotnewslistview = (XListView) findViewById(R.id.hotnews_listview);
		hotnewslistview.setPullRefreshEnable(true);// 开启下拉刷新
		hotnewslistview.setPullLoadEnable(true);// 开启上滑加载更多
		hotnewslistview.setXListViewListener(NewsMainActivity.this);

		 touchListener = new TouchListener(this, false);
		hotnewslistview.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (touchListener.onTouch(hotnewslistview, event))
					return true;
				else if (MainActivity.isopen)
					return true;
				return false;
			}
		});
		
		
		// listview 事件监听
		hotnewslistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				Intent intent = new Intent(NewsMainActivity.this,
						NewsContnetActivity.class);
					
					intent.putExtra("LINK",entrylist.get(arg2-1).getId());
					intent.putExtra("TITLE", entrylist.get(arg2-1).getTitle());
					startActivity(intent);	

			}

		});
		// 开启下载服务程
		new DownLoadThread().start();
		// 信息加载提示框
		pro = new ProgressDialog(NewsMainActivity.this);
		pro.setMessage("正在加载信息.....");
		pro.show();
	}

	/**
	 * 
	 * 下载线程
	 * 
	 * @author Administrator
	 * 
	 */
	public class DownLoadThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
	
			//下载新闻目录
			DownLoadNews down = new DownLoadNews(path.replace("10",""+i));
			entrylist = down.getMessage();
			//下载完成后发送Hander通知
			Message msg = Message.obtain();
			msg.what = SHOWPROGRESS;
			handler.sendMessage(msg);

		}
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		
		new DownLoadThread().start();
		Message msg = Message.obtain();
		msg.what = REFRESHING;
		handler.sendMessageDelayed(msg, 2000);

	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		i=i+5;
		Message msg = Message.obtain();
		msg.what = LOADMOREING;
		path.replace("pageSize", ""+i);
		new DownLoadThread().start();
		handler.sendMessageDelayed(msg, 2000);
	}
	
	

}
