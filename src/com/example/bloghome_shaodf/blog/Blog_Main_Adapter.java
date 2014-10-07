package com.example.bloghome_shaodf.blog;

import java.util.List;

import com.example.bloghome_shaodf.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Blog_Main_Adapter extends BaseAdapter {

	private Context context;
	private List<BlogInfo> list;

	
	public Blog_Main_Adapter(Context context, List<BlogInfo> list) {
		super();
		this.context = context;
		this.list = list;
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("unused")
	public View getView(int position, View convertView, ViewGroup parent) {
		MyHold myhold = null;
		if (myhold == null) {
			myhold = new MyHold();
			convertView = LayoutInflater.from(context).inflate(R.layout.blog_list_item, null);
			convertView.setTag(myhold);
			myhold.text1 = (TextView) convertView.findViewById(R.id.blog_list_item_title);
			myhold.text2 = (TextView) convertView.findViewById(R.id.blog_list_item_authorname);
			myhold.text3 = (TextView) convertView.findViewById(R.id.blog_list_item_summary);
			myhold.text4 = (TextView) convertView.findViewById(R.id.blog_list_item_updated);
			myhold.text5 = (TextView) convertView.findViewById(R.id.blog_list_item_views_num);
			myhold.text6 = (TextView) convertView.findViewById(R.id.blog_list_item_comments_num);
		}else {
			myhold = (MyHold) convertView.getTag();
		}
		myhold.text1.setText(list.get(position).getTitle().toString());
		myhold.text2.setText("作者："+ list.get(position).getName().toString());
		myhold.text3.setText(list.get(position).getSummary().toString());
		String time = list.get(position).getUpdated().toString();
		myhold.text4.setText(time.substring(0, 4) + "年" + time.substring(5, 7)
				+ "月" + time.substring(8, 10) + "日" + "  "
				+ time.substring(11, 19));
		myhold.text5.setText(list.get(position).getViews().toString());
		myhold.text6.setText(list.get(position).getComments().toString());
		return convertView;
	}
	
	private final class MyHold {
		TextView text1, text2, text3, text4, text5, text6;
	}

}
