package com.example.bloghome_shaodf.tools;

import java.util.List;
import com.example.bloghome_shaodf.R;
import com.example.bloghome_shaodf.blog.BlogsInfo;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ParseException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BlogListAdapter extends BaseAdapter {
	private Activity context;
	private List<BlogsInfo> blogsList;
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
			}if(intent.getAction().equals("no")){
				change = false;
				notifyDataSetChanged();
			}
		}
	};
	public BlogListAdapter(Activity context) {
		this.context = context;
		spf= context.getSharedPreferences("SP", context.MODE_PRIVATE);
		change = spf.getBoolean("change", false);
		
	}
	public void closeReceiver(){
		context.unregisterReceiver(receiver);
	}
	
	public BlogListAdapter(Activity context, List<BlogsInfo> blogsList) {
		this.context = context;
		this.blogsList = blogsList;
		spf= context.getSharedPreferences("SP", context.MODE_PRIVATE);
		change = spf.getBoolean("change", false);
	}
	
	public void setBlogs(List<BlogsInfo> blogsList) {
		this.blogsList = blogsList;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if(null == blogsList)
			return 0;
		return blogsList.size();
	}

	@Override
	public BlogsInfo getItem(int position) {
		if(null == blogsList)
			return null;
		return blogsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		IntentFilter inf = new IntentFilter();
		inf.addAction("yes");
		inf.addAction("no");
		context.registerReceiver(receiver, inf);
		
		final ViewHolder holder;
		if (convertView == null) {
			final LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.blog_list_item, null);
			holder = new ViewHolder();
			holder.blog_list_item_title = (TextView) convertView
					.findViewById(R.id.blog_list_item_title);
			holder.blog_list_item_summary = (TextView) convertView
					.findViewById(R.id.blog_list_item_summary);
			holder.blog_list_item_authorname = (TextView) convertView
					.findViewById(R.id.blog_list_item_authorname);
			holder.blog_list_item_views_num = (TextView) convertView
					.findViewById(R.id.blog_list_item_views_num);
			holder.blog_list_item_comments_num = (TextView) convertView
					.findViewById(R.id.blog_list_item_comments_num);
			holder.blog_list_item_updated = (TextView) convertView
					.findViewById(R.id.blog_list_item_updated);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
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
		
		holder.blog_list_item_title.setText(blogsList.get(position).getTitle());
		holder.blog_list_item_summary.setText(summary(blogsList.get(position)
				.getSummary()));
		holder.blog_list_item_authorname.setText(blogsList.get(position)
				.getAuthorName());
		holder.blog_list_item_views_num.setText(blogsList.get(position)
				.getViews()+"");
		holder.blog_list_item_comments_num.setText(blogsList.get(position)
				.getComments()+"");
		try {
			try {
				holder.blog_list_item_updated.setText(ChangeTime.publishedToDate(blogsList.get(position).getPublished()));
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return convertView;
	}

	static class ViewHolder {
		TextView blog_list_item_title;
		TextView blog_list_item_summary;
		TextView blog_list_item_authorname;
		TextView blog_list_item_views_num;
		TextView blog_list_item_comments_num;
		TextView blog_list_item_updated;
		
	}
	//内容字数设置为100
	private String summary(String summary){
		String res = "暂无可预览内容";
		
		if( summary != null&&summary.length() > 100 )
			res = summary.substring(0, 100)+"...";
		else if(summary != null && summary.length() <= 100)
			res = summary;
		else res = "暂无可预览内容";
		return summary;
	}
	
}
