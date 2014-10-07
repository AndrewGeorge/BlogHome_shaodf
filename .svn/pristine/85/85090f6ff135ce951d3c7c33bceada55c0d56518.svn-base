package com.example.bloghome_shaodf.blog;

import java.io.InputStream;
import java.util.List;

import com.example.bloghome_shaodf.MainActivity;
import com.example.bloghome_shaodf.R;
import com.example.bloghome_shaodf.blog.Blog_Main_Activity.DownLoadTh;
import com.example.bloghome_shaodf.collect.TouchListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class Blog_48_Activity extends Activity{

	String path = "http://wcf.open.cnblogs.com/blog/48HoursTopViewPosts/50";
	private ListView listView;
	List<BlogInfo> list;
	InputStream inputStream;
	TouchListener touchListener;
	Blog_Main_Adapter adapter;
	PullToRefreshListView reflist;
	protected static final int REFRESHING = 0;
	protected static final int LOADMOREING = 1;
	protected static final int SHOWPROGRESS = 2;

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 10) {
				adapter = new Blog_Main_Adapter(Blog_48_Activity.this, list);
				listView.setAdapter(adapter);
			}
		}
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//设置无标题  
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.blog_activity);
		listView = (ListView) findViewById(R.id.blog_listview);
		DownLoadTh loadTh = new DownLoadTh();
		loadTh.start();
		// listview 事件监听
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				String id = list.get(arg2).getId();
				String title = list.get(arg2).getTitle();
				
				Intent intent = new Intent(Blog_48_Activity.this,
						Blog_Content_Activity.class);
				
				intent.putExtra("id", id);
				intent.putExtra("title", title);
				startActivity(intent);

			}
		});
		//左右侧滑
		 touchListener = new TouchListener(this, false);
		listView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (touchListener.onTouch(listView, event))
					return true;
				else if (MainActivity.isopen)
					return true;
				return false;
			}
		});

	}

	public class DownLoadTh extends Thread {
		public void run() {
			super.run();
			inputStream = SaxService.getXML(path);
			list = SaxService.readxml(inputStream, "entry");
			handler.sendEmptyMessage(10);

		}
	}

}