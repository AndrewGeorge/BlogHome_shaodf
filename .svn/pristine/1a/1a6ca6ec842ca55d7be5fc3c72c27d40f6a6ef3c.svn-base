package com.example.bloghome_shaodf.tools;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import com.example.bloghome_shaodf.MainActivity;
import com.example.bloghome_shaodf.R;
import com.example.bloghome_shaodf.blog.BlogerInfo;
import com.example.bloghome_shaodf.collect.CheckNetWork;
import com.example.bloghome_shaodf.collect.TouchListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SearchActivity extends Activity {

	private TouchListener touchListener;
	ListView resList;// 博主搜索结果显示
	Button searchGo;// 搜索按钮
	EditText searchText;// 搜索输入框
	TextView resText,searchNull;// 酱油
	ProgressBar progressBar;
	Button searchClear;
	
	
	Context context;
	BlogerInfo curBloger;
	ArrayList<BlogerInfo> blogerList;
	protected String searchContent;
	
	SearchBloggerAdapter blogerAdapter;
	Handler searchHandler;
	Thread searchThread;
	
	public static final String searchBloggers = "http://wcf.open.cnblogs.com/blog/bloggers/search?t=";// 搜索的网址前缀
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchpage1);
		
		touchListener = new TouchListener(this,false);
		View v = findViewById(R.id.search_body);
		v.setOnTouchListener(new TouchListener(this,true));
		
		context = this;
		blogerList = new ArrayList<BlogerInfo>();
		curBloger = new BlogerInfo();
		
		
		searchNull = (TextView) findViewById(R.id.searchNull);
		progressBar = (ProgressBar) findViewById(R.id.blog_progress);
		resList = (ListView) findViewById(R.id.resultList);
		searchGo = (Button) findViewById(R.id.search_go);
		searchText = (EditText) findViewById(R.id.search_text);
		searchClear = (Button) findViewById(R.id.search_clear);
		
		searchClear.setOnClickListener(clearLisatener);
		searchGo.setOnClickListener(searchListener);
		// 博主搜索结果点击
		resList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {	 
 
				blogerClick(arg2);

			}
		});
		
		resList.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(touchListener.onTouch(resList, event))
					return true;
				else if(MainActivity.isopen)
					return true;
				return false;
			}
		});
		
		// handler掌控UI更新
		searchHandler = new Handler() {

					@Override
					public void handleMessage(Message msg) {

						super.handleMessage(msg);
						Log.v("TAG", "content download -> show");
						if (msg.what == 1) {// 搜索完成后显示搜索内容
							progressBar.setVisibility(View.INVISIBLE);
							if (blogerList != null && blogerList.size() > 0) {
								blogerAdapter = new SearchBloggerAdapter(context,
										blogerList);
								resList.setAdapter(blogerAdapter);

							} 
							else{
								searchNull.setText("没有搜索到相关内容");
								searchNull.setVisibility(View.VISIBLE);
							}
						}
				}
	};
	}
	/**
	 * 清除搜索内容
	 */
	OnClickListener clearLisatener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			 
			blogerAdapter = new SearchBloggerAdapter(context,
					null);
			resList.setAdapter(blogerAdapter);
			searchNull.setVisibility(View.VISIBLE);
			searchNull.setText("没有可显示内容");
		}
	};
	/**
	 * 搜索按钮的点击监听事件
	 */
	OnClickListener searchListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			
			searchNull.setVisibility(View.INVISIBLE);
			searchContent = searchText.getText().toString();
			searchContent = URLEncoder.encode(searchContent);

			if (searchContent == null || searchContent == ""
					|| searchContent.length() <= 0){
				searchText.setError(Html.fromHtml("<font color=#808183>"
	                    + "输入内容不合规范" + "</font>"));
			}

			else {
				if (!CheckNetWork.checkNetworkInfo(context)) {
					AlertDialog.Builder alert = new AlertDialog.Builder(context);
					alert.setTitle("没网 啊，少年！ ");
					alert.setMessage("有网走遍天下，没网蜗居一隅");
					alert.setPositiveButton("返回", null);
					alert.setNegativeButton("设置网络",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									startActivity(new Intent(
											android.provider.Settings.ACTION_WIFI_SETTINGS));// 进入无线网络配置界面
								}

							});
					alert.create().show();
				} else {
					progressBar.setVisibility(View.VISIBLE);
					getBlogerData(searchContent);
				}
			}
		}
	};
				
	/**
	 * 从网络获取搜索的内容
	 * 
	 * @param searchCont
	 *            搜索的博主名称
	 */
	protected void getBlogerData(String searchCont) {
		final String url = searchBloggers + searchCont;
		;
		Log.v("TAG", "getSearchRequest -->" + url);

		searchThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Log.v("TAG", "Thread Run");
					BlogerSAXHandler blogerHand = new BlogerSAXHandler();
					blogerList = blogerHand.getBlogerInfo(url);
					searchHandler.sendEmptyMessage(1);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParserConfigurationException e) {

					e.printStackTrace();
				} catch (SAXException e) {

					e.printStackTrace();
				}
			}

		});
		searchThread.start();
	}
	/**
	 * 博主列表点击事件,跳转
	 */
	protected void blogerClick(int id) {
	
		curBloger = blogerList.get(id);
		
		String blogapp = curBloger.getBlogapp();
		String updateTime = curBloger.getUpdated();
		String blogerName = curBloger.getTitle();
		String blogerImgUrl =curBloger.getAvatar();
        String blogCount = curBloger.getPostcount(); 
        Log.v("intent1", blogerName+"/n"+blogerImgUrl+"/n"+updateTime);
		Intent intent = new Intent(SearchActivity.this,SearchBlogAct.class);
		
		intent.putExtra("blogapp", blogapp);
		intent.putExtra("updateTime", updateTime);
		intent.putExtra("bloger", blogerName);
		intent.putExtra("blogerImgUrl",blogerImgUrl);
		intent.putExtra("blogCount", blogCount);
		
		
		startActivity(intent);
		 		 
	}
	
	
}
