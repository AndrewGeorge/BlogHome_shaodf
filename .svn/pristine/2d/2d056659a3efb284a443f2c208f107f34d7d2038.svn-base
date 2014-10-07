package com.example.bloghome_shaodf.tools;

import java.util.ArrayList;

import com.example.bloghome_shaodf.R;
import com.example.bloghome_shaodf.blog.BlogerInfo;

import android.content.Context;
import android.net.ParseException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SearchBloggerAdapter extends BaseAdapter {

	Context context;
	ArrayList<BlogerInfo> blogerList;
	LayoutInflater layoutInflater;
	 
	
	public SearchBloggerAdapter(Context context, ArrayList<BlogerInfo> blogerList){
		this.context = context;
		this.blogerList = blogerList;
		layoutInflater = LayoutInflater.from(context);
		 
	}
	@Override
	public int getCount() {
	
		if(blogerList == null)
		return 0;
		else 
			return blogerList.size();
	}

	@Override
	public Object getItem(int position) {
		
		return blogerList.get(position);
	}

	@Override
	public long getItemId(int position) {
		 
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if(convertView == null){
			
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.searchbloger, null);
			
			holder.blogerImg = (ImageView) convertView.findViewById(R.id.bloger_img);
			holder.splitImg = (ImageView) convertView.findViewById(R.id.split_img);
			holder.blogerName =  (TextView) convertView.findViewById(R.id.bloger_name);
			holder.blogCount = (TextView) convertView.findViewById(R.id.blog_count);
			holder.timeDay = (TextView) convertView.findViewById(R.id.blog_time);
			 
			
			convertView.setTag(holder);
		}
		else
			holder = (ViewHolder) convertView.getTag();
		
		String time = null;;
		BlogerInfo info = blogerList.get(position);
		try {
			
			time = ChangeTime.publishedToDate(info.getUpdated());
		} catch (ParseException e) {
			
			e.printStackTrace();
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		holder.blogerName.setText(info.getTitle());
		holder.blogCount.setText(info.getPostcount()+"Æª²©ÎÄ");
		holder.timeDay.setText(time);		
		
		String imgUrl = info.getAvatar();
		DownloadImg.getImgloadInstance().getImgDrawable
				(imgUrl, holder.blogerImg);
		 
		
		return convertView;
	}

	OnClickListener chooseListener = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			
			
		}
	};
	
	class ViewHolder{
		ImageView blogerImg,splitImg;
		
		TextView blogerName,timeDay,timeSecond,blogCount;
	}
}
