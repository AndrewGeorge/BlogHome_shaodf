package com.example.bloghome_shaodf.news;

import java.util.List;
import java.util.zip.Inflater;
import com.example.bloghome_shaodf.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NewsMainAdapter extends BaseAdapter {
	List<Entry> listentry;
	Context c;

	public NewsMainAdapter(Context c, List<Entry> listentry) {
		// TODO Auto-generated constructor stub
		this.c = c;
		this.listentry = listentry;

	}

	public void setData(List<Entry> entrylist) {
		// TODO Auto-generated method stub
		this.listentry = entrylist;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listentry.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub

	View v=LayoutInflater.from(c).inflate(
			R.layout.hotnews_listview_items, null);
	
	if (arg0 % 2 == 0) {

		// arg1.setBackgroundColor(Color.rgb(red, green, blue));
		v.setBackgroundColor(android.graphics.Color
				.parseColor("#DCE6F4"));
	}
		


		TextView titleView = (TextView) v.findViewById(R.id.newstitle);
		TextView author_nameView = (TextView) v
				.findViewById(R.id.newsauthoename);
		TextView summaryView = (TextView) v.findViewById(R.id.newssummary);
		TextView updatedView = (TextView) v.findViewById(R.id.newsupdated);
		TextView viewsView = (TextView) v.findViewById(R.id.newsviews);
		TextView commmentsView = (TextView) v
				.findViewById(R.id.newscomments);
		titleView.setText(listentry.get(arg0).getTitle());
		author_nameView.setText(listentry.get(arg0).getAythor_name());
		summaryView.setText(listentry.get(arg0).getSummary() + "....");
		updatedView.setText(listentry.get(arg0).getUpdated().replace("T", "  ")
				.replace("Z", " "));
		viewsView.setText(listentry.get(arg0).getViews());
		commmentsView.setText(listentry.get(arg0).getComments());
		// System.out.println(listentry.get(arg0).link_href);
		return v;
	}

}
