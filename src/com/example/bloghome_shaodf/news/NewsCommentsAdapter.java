package com.example.bloghome_shaodf.news;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bloghome_shaodf.R;

public class NewsCommentsAdapter extends BaseAdapter {

	
	
	 private Context c;
	 
	 List<Comments> entrylist;
	
	public NewsCommentsAdapter( Context c,List<Comments> entrylist2) {
		
		this.c=c;
		this.entrylist=entrylist2;
		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return entrylist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return entrylist.size();
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return entrylist.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		if (convertView==null) {
			
			convertView=LayoutInflater.from(c).inflate(R.layout.comment_list_item, null);
			holder=new ViewHolder();
			holder.name=(TextView) convertView.findViewById(R.id.comments_name);
			holder.time=(TextView) convertView.findViewById(R.id.comments_time);
			holder.comment=(WebView) convertView.findViewById(R.id.comments_comment);
			convertView.setTag(holder);
		}else{
			
			holder=(ViewHolder) convertView.getTag();
		}
		
		holder.name.setText(entrylist.get(position).getName());
		holder.time.setText(entrylist.get(position).getTime().replace("T", "|")
				.replace("Z", "|").replace("+08:00", ""));
		holder.comment.loadDataWithBaseURL(null, entrylist.get(position).getComment(), "text/html", "UTF-8", null
				);
		
		return convertView;
	}
	
	public final class ViewHolder{
		
		TextView name;
		TextView time;
		WebView comment;
		
	}
	
}
