package com.example.bloghome_shaodf.collect;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.example.bloghome_shaodf.R;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class OfflineBlogAdapter extends BaseAdapter {

	final String BLOGER_URL = "http://wcf.open.cnblogs.com/blog/bloggers/search?t=";
	ArrayList<OffBlogInfo> blogOff;
	Context context;
	OffBlogInfo curBlogs;
	LayoutInflater layoutInflater;
	private SharedPreferences spf;
	private boolean change;
	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals("yes")) {
				change = true;
				Log.v("tag", "aaaa");
				notifyDataSetChanged();
			}
			if (intent.getAction().equals("no")) {
				change = false;
				notifyDataSetChanged();
			}
		}
	};

	public OfflineBlogAdapter(Context context,
			ArrayList<OffBlogInfo> blogOff) {
		layoutInflater = LayoutInflater.from(context);
		this.context = context;
		this.blogOff = blogOff;
		spf = context.getSharedPreferences("SP", context.MODE_PRIVATE);
		change = spf.getBoolean("change", false);
	}

	@Override
	public int getCount() {
		if (blogOff == null)
			return 0;
		else
			return blogOff.size();
	}

	@Override
	public Object getItem(int position) {

		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder hold;
		if (convertView == null) {

			hold = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.offlineblog, null);

			hold.blogSet = (LinearLayout) convertView
					.findViewById(R.id.blog_menu);
			hold.bloger = (TextView) convertView.findViewById(R.id.blog_bloger);
			hold.title = (TextView) convertView.findViewById(R.id.blog_title);
			hold.text = (TextView) convertView.findViewById(R.id.blog_summary);
			hold.storeTime = (TextView) convertView
					.findViewById(R.id.blog_storetime);
			hold.upTime = (TextView) convertView.findViewById(R.id.blog_uptime);
			// hold.text.getSettings().setDefaultTextEncodingName("UTF-8");

			convertView.setTag(hold);
		} else
			hold = (ViewHolder) convertView.getTag();
		if(change){
			if(position % 2 == 0)
				convertView.setBackgroundColor(Color.parseColor("#FFF5EE"));
			else
				convertView.setBackgroundColor(Color.parseColor("#FFEFDB"));
		}else{
			if(position % 2 == 0)
				convertView.setBackgroundColor(0xffcdddfa);
			else
				convertView.setBackgroundColor(0xfff5fffa);
		}

		OffBlogInfo info = blogOff.get(position);
		hold.bloger.setText("博主：" + info.getBloger());
		hold.title.setText("   " + info.getBlogTitle());

		// hold.text.loadDataWithBaseURL(null, "预览:<br>"+info.getBlogSumary(),
		// "text/html", "UTF-8", null);
		hold.storeTime.setText("存储时间：" + info.getStoreTimeChange() + "");
//		try {
//			hold.upTime.setText("更新时间："
//					+ ConvertDate.updateToDate(info.getUpdateTime()));
//		} catch (ParseException e) {
//
//			e.printStackTrace();
//		}
		if (info.getBlogSum() == null)
			hold.text.setText("暂无可供预览的内容！");
		else
			hold.text.setText(info.getBlogSum());

		hold.blogSet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				curBlogs = blogOff.get(position);
				clickMenu();
			}
		});

		return convertView;
	}

	class ViewHolder {
		TextView title, storeTime, upTime, bloger, text;
		LinearLayout blogSet;
		// WebView text;
	}

	public void clickMenu() {

		final String itemName = curBlogs.getBlogTitle();
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);

		dialog.setTitle("请选择您要的操作：");
		dialog.setView(null);
		dialog.setItems(new String[] { "查看", "跳转至网页查看", "查看博主其他博文", "删除此项",
				"全部删除", "返回" }, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				switch (which) {
				case 0:

//					Intent intent = new Intent(context, Offline_content.class);
//
//					int blogId = curBlogs.getBlogId();
//
//					intent.putExtra("blogId", blogId);
//
//					context.startActivity(intent);
					break;
				case 1:
//					Intent it = new Intent(context, BlogContentActivity.class);

//					it.putExtra("id", curBlogs.getBlogId());
//					it.putExtra("link", curBlogs.getBlogUrl());
//					it.putExtra("blogTitle", curBlogs.getBlogTitle());
//					it.putExtra("bloger", curBlogs.getBloger());
//					it.putExtra("blogerUrl", curBlogs.getBlogerUrl());
//					it.putExtra("updateTime", curBlogs.getUpdateTime());
//					it.putExtra("blogSummary", curBlogs.getBlogSumary());
//					if(CheckNetWork.checkNetworkInfo(context))
//					context.startActivity(it);

					break;
				case 2:
//					if(CheckNetWork.checkNetworkInfo(context)){
//					BlogerSAXHandler saxHandler = new BlogerSAXHandler();
//					List<BlogerInfo> list = null;
//					BlogerInfo blogerInfo = null;
//					String app = URLEncoder.encode(curBlogs.getBloger());
//					try {
//						list = saxHandler.getBlogerInfo(BLOGER_URL + app);
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (ParserConfigurationException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (SAXException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					if (list != null && list.size() > 0) {
//						blogerInfo = list.get(0);
//						String blogapp = blogerInfo.getBlogapp();
//						String updateTime = blogerInfo.getUpdated();
//						String blogerName = blogerInfo.getTitle();
//						String blogerImgUrl = blogerInfo.getAvatar();
//						String blogCount = blogerInfo.getPostcount();
//
//						Intent intent1 = new Intent(context,
//								SearchBlogPageAct.class);
//
//						intent1.putExtra("blogapp", blogapp);
//						intent1.putExtra("updateTime", updateTime);
//						intent1.putExtra("bloger", blogerName);
//						intent1.putExtra("blogerImgUrl", blogerImgUrl);
//						intent1.putExtra("blogCount", blogCount);
//						
//						context.startActivity(intent1);
//					} else {
//						Toast.makeText(context, "数据获取失败", Toast.LENGTH_SHORT)
//								.show();
//					}
//					}
					break;

				case 3:
					AlertDialog.Builder alert = new AlertDialog.Builder(context);
					alert.setView(null);
					alert.setTitle("删除此项");
					alert.setMessage("警告： 将删除 " + itemName);
					alert.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									BlogSQLHelper.getDBInstance(context)
											.deleteItem(curBlogs.getBlogId());
									// myDb.deleteItem(curBlogs.getBlogId()) ;
									flushAdapter();
								}
							});

					alert.setNegativeButton("取消", null);
					alert.create().show();

					break;
				case 4:
					AlertDialog.Builder alertAll = new AlertDialog.Builder(
							context);
					alertAll.setTitle("全部删除");
					alertAll.setMessage("警告： 将删除所有存储数据");
					alertAll.setView(null);
					alertAll.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									BlogSQLHelper.getDBInstance(context)
											.deleteAll();
									flushAdapter();
								}
							});

					alertAll.setNegativeButton("取消", null);
					alertAll.create().show();

					break;

				case 5:
					dialog.dismiss();
					break;

				}
			}
		});
		dialog.create().show();
		

	}

	public void flushAdapter() {
		blogOff = new ArrayList<OffBlogInfo>();
		blogOff = BlogSQLHelper.getDBInstance(context).getBlogDBData();
		OfflineBlogAdapter.this.notifyDataSetChanged();
		OfflineActivity.blogOff = this.blogOff;
		if(!blogOff.isEmpty()){
			OfflineActivity.blogNull.setVisibility(View.INVISIBLE);
		}else{
			OfflineActivity.blogNull.setVisibility(View.VISIBLE);
		}
	}
}
