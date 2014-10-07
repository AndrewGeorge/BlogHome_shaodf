package com.example.bloghome_shaodf.collect;

import java.util.ArrayList;
import com.example.bloghome_shaodf.MainActivity;
import com.example.bloghome_shaodf.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class OfflineActivity extends Activity {

	public static TextView blogNull;
	TextView title;
	ListView offListview;
	Context context;
	private TouchListener touchListener;

	public static ArrayList<OffBlogInfo> blogOff;
	OffBlogInfo curBlogs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.offline);

		context = this;
		blogNull = (TextView) findViewById(R.id.offline_null);
		blogNull.setOnTouchListener(new TouchListener(this, true));
		offListview = (ListView) findViewById(R.id.offline_listview);

		flushAdapter();

		offListview.setOnItemClickListener(clickBlog);
		touchListener = new TouchListener(this, false);
		offListview.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (touchListener.onTouch(offListview, event))
					return true;
				else if (MainActivity.isopen)
					return true;
				return false;
			}
		});
	}

	OnItemClickListener clickBlog = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
		}
	};

	public void flushAdapter() {
		blogOff = new ArrayList<OffBlogInfo>();
		blogOff = BlogSQLHelper.getDBInstance(context).getBlogDBData();
		OfflineBlogAdapter adapter = new OfflineBlogAdapter(this, blogOff);
		offListview.setAdapter(adapter);
		if (!blogOff.isEmpty()) {
			blogNull.setVisibility(View.INVISIBLE);
		} else {
			blogNull.setVisibility(View.VISIBLE);
		}

	}

}
