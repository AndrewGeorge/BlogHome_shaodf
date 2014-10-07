package com.example.bloghome_shaodf.tools;

import java.util.ArrayList;
import com.example.bloghome_shaodf.R;
import com.example.bloghome_shaodf.TouchListener;
import com.example.bloghome_shaodf.blog.Blog_Content_Activity;
import com.example.bloghome_shaodf.blog.BlogsInfo;
import com.example.bloghome_shaodf.blog.PullToRefreshListView;
import com.example.bloghome_shaodf.blog.PullToRefreshListView.OnRefreshListener;
import com.example.bloghome_shaodf.collect.CheckNetWork;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SearchBlogAct extends Activity{

	RelativeLayout blog;
	ImageView blog_blogerImg;
	TextView blog_blogerName, blog_blogerCount, blog_blogPart, blogNull;
	PullToRefreshListView blogListview;
	ProgressBar progressBar;

	ProgressDialog proDialog;

	BlogsInfo curBlogs;
	ArrayList<BlogsInfo> blogsList;
	ArrayList<BlogsInfo> addBlogsList = null;

	Context context;
	AlertDialog.Builder builder;

	int blogCount;
	String blogapp;
	int pageIndex = 1;
	final int pageSize = 20;
	private int visibleLastIndex = 0; // ���Ŀ���������
	private static int visibleItemCount; // ��ǰ���ڿɼ�������
	// SearchBlogAdapter blogAdapter;
	BlogListAdapter blogAdapter;
	Handler blogHandler;
	Thread blogThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchpage2);

		context = this;
		progressBar = (ProgressBar) findViewById(R.id.blog_progress);
		blogNull = (TextView) findViewById(R.id.blogNull);
		blog = (RelativeLayout) findViewById(R.id.blog);
		blog.setOnTouchListener(new TouchListener(this, true));
		blogNull.setOnTouchListener(new TouchListener(this, true));

		blog_blogerImg = (ImageView) findViewById(R.id.blog_blogerImg);
		blog_blogerName = (TextView) findViewById(R.id.blog_blogerName);
		blog_blogerCount = (TextView) findViewById(R.id.blog_blogCount);
		blogListview = (PullToRefreshListView) findViewById(R.id.blog_blogshow);
		blogListview.dismissProgress();
		blogListview.setOnTouchListener(new TouchListener(this, false));

		Intent intent = getIntent();

		blogapp = intent.getStringExtra("blogapp");
		String updateTime = intent.getStringExtra("updateTime");
		blogCount = Integer.parseInt(intent.getStringExtra("blogCount"));
		String blogerImgUrl = intent.getStringExtra("blogerImgUrl");
		String bloger = intent.getStringExtra("bloger");
		Log.v("intent", bloger + "/n" + blogerImgUrl + "/n" + updateTime);

		getBlogsData(1);

		blog_blogerCount.setText("�� " + blogCount + " ƪ����");
		blog_blogerName.setText("������" + bloger);

		DownloadImg.getImgloadInstance().getImgDrawable(blogerImgUrl,
				blog_blogerImg);

		blogHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {

				super.handleMessage(msg);
				switch (msg.what) {
				case 150:
					proDialog.dismiss();
					progressBar.setVisibility(View.INVISIBLE);

					if (blogsList == null || blogsList.size() <= 0) {
						blogNull.setVisibility(View.VISIBLE);
						TextView search_null = (TextView) findViewById(R.id.blogNull); 
						search_null.setVisibility(View.VISIBLE);
					} 
					else {

						blogAdapter = new BlogListAdapter(
								SearchBlogAct.this, blogsList);
						blogListview.setAdapter(blogAdapter);
					}
				case 2000:
					Log.v("down", "�����б�������");
					if (addBlogsList != null){
						blogsList.addAll(addBlogsList);
					blogListview.onMoreComplete();
					blogAdapter.notifyDataSetChanged();
					// blogAdapter.setBlogs(blogsList);
					}

				}
			}
		};
		blog.setOnTouchListener(new TouchListener(this, true));
		blogListview.setOnTouchListener(new TouchListener(this, false));
		blogListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				position--;
				if (position < 0)
					position = 0;
				if (position >= blogsList.size())
					position = blogsList.size() - 1;

				int blogId = blogsList.get(position).getId();
				// Blog_Content_Activity.class����������
				Intent intent = new Intent(context, Blog_Content_Activity.class);
				intent.putExtra("id", blogId);
				intent.putExtra("link", blogsList.get(position).getLink());
				intent.putExtra("blogTitle", blogsList.get(position).getTitle());
				intent.putExtra("updateTime", blogsList.get(position)
						.getUpdated());
				intent.putExtra("blogerUrl", blogsList.get(position)
						.getAuthorUrl());
				intent.putExtra("bloger", blogsList.get(position)
						.getAuthorName());
				intent.putExtra("summary", blogsList.get(position).getSummary());
				
				if(CheckNetWork.checkNetworkInfo(context))
				
					startActivity(intent);
			}
		});
		// ���ϻ���
		blogListview.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				new GetDataTask().execute();
			}
		});

		// ���»���
		blogListview.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount1, int totalItemCount) {

				visibleItemCount = totalItemCount;
				visibleLastIndex = firstVisibleItem + visibleItemCount1;

				// Log.v("down","���»�"+visibleLastIndex+"---"+visibleItemCount);
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// if(scrollState == OnScrollListener.SCROLL_STATE_IDLE){
				// scrollState = 0;
				// }

				if (blogsList.size() < blogCount) {
					if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
							&& !blogListview.isclick
							&& visibleItemCount == visibleLastIndex) {
						Log.v("down", "���ס�������");
						addArrayList();
					}
				}
				else{
					 blogListview.dismissProgress();
					
				}
			}
		});

	}


	private class GetDataTask extends
			AsyncTask<Void, Void, ArrayList<BlogsInfo>> {

		@Override
		protected ArrayList<BlogsInfo> doInBackground(Void... params) {
			// �����������һЩ��̨����
			// blogsList = new ArrayList<BlogsInfo>();
			if (addBlogsList != null) {

				addBlogsList.clear();
			} else {
				addBlogsList = new ArrayList<BlogsInfo>();
			}
			addBlogsList = getBlogInfo(1);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return blogsList;
		}

		@Override
		protected void onPostExecute(ArrayList<BlogsInfo> result) {
			// ���������ӵ�����
			if (addBlogsList.get(0).getTitle()
					.equals(blogsList.get(0).getTitle())) {
				blogListview.onRefreshComplete();
			} else {
				Log.v("TAG", "onPostExecute");
				blogsList.addAll(0, addBlogsList);
				blogAdapter.notifyDataSetChanged();

				// ˢ����ɵ��ø÷�����λ
				blogListview.onRefreshComplete();
			}

			super.onPostExecute(result);
		}
	}

	// ƴ��ArrayList
	private void addArrayList() {
		Log.v("down", "�������ݡ�������");
		pageIndex++;
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				Log.v("down", "�����̡߳�������");
				addBlogsList = new ArrayList<BlogsInfo>();
				addBlogsList = getBlogInfo(pageIndex);

			}
		});
		thread.start();
		try {
			thread.sleep(1000);
			blogHandler.sendEmptyMessage(2000);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}

	}

	public void getBlogsData(int page) {

		proDialog = new ProgressDialog(context);
		proDialog.setMessage("���ݼ�����,���Ժ�...");
		proDialog.show();

		progressBar.setVisibility(View.VISIBLE);

		final int pageIndex = page;
		final int pageSize = 20;

		blogThread = new Thread(new Runnable() {

			@Override
			public void run() {
				Log.v("TAG", "Thread Run");
				GetPersonalBlogList blogHand = new GetPersonalBlogList(blogapp,
						pageIndex, pageSize);
				blogsList = blogHand.getBlogsInfo();
				blogHandler.sendEmptyMessage(150);
			}

		});
		blogThread.start();

	}

	private ArrayList<BlogsInfo> getBlogInfo(int page) {
		Log.v("down", "Ϲ�Ӱ����ݡ�������");
		ArrayList<BlogsInfo> blogsList1 = new ArrayList<BlogsInfo>();

		GetPersonalBlogList blogHand = new GetPersonalBlogList(blogapp, page,
				pageSize);
		blogsList1 = blogHand.getBlogsInfo();
		Log.v("down", "Ϲ�Ӱ����ݡ��ɹ�������");
		return blogsList1;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		blogAdapter.closeReceiver();
	}
}
