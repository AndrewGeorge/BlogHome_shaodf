package com.example.bloghome_shaodf.collect;

import java.util.List;
import com.example.bloghome_shaodf.MainActivity;
import com.example.bloghome_shaodf.R;
import com.example.bloghome_shaodf.news.NewsContnetActivity;
import com.example.bloghome_shaodf.news.SaveContent;
import com.example.bloghome_shaodf.sql.NewsDataBaseManger;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MarkActivity extends Activity {
	Button allSelBtn;
	Button deleteBtn;
	TextView tv,tvNull;
	ListView markView;
	View v;
	boolean b=true;
	static List<SaveContent> list;
	MarkAdapter data;
	TouchListener touchListener;
	NewsDataBaseManger manger;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mark);
		
		v=(View)findViewById(R.id.mark);

		//touch事件
		v.setOnTouchListener(new TouchListener(this,true));
		touchListener = new TouchListener(this,false);
		
		markView=(ListView) findViewById(R.id.mark_list);
		
		//左右滑动
		 touchListener = new TouchListener(this, false);
		 markView.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					if (touchListener.onTouch(markView, event))
						return true;
					else if (MainActivity.isopen)
						return true;
					return false;
				}
			});
		allSelBtn=(Button)findViewById(R.id.check_all);
		deleteBtn=(Button)findViewById(R.id.check_delete);
		tvNull=(TextView)findViewById(R.id.paly_null);
		tv=(TextView)findViewById(R.id.count);
		
		
		
		//调用查类查询数据
		//调用对数据库的操作要通过DataDaseManger来操作
		manger=new NewsDataBaseManger(this,"newscontents");
		list=manger.queryData();
		data = new MarkAdapter(this, list);
		markView.setAdapter(data);
		
		
		
		
		tv.setText("共"+list.size()+"条");
		if(list.isEmpty()){
			tvNull.setText("暂无记录");
		}
		markView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(MarkActivity.this, NewsContnetActivity.class);
				intent.putExtra("id", list.get(arg2).get_id());
				intent.putExtra("Title", list.get(arg2).getTitle());
				startActivity(intent);
			}
		});
		markView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(touchListener.onTouch(markView, event))
					return true;
				else if(MainActivity.isopen)
					return true;
				return false;
			}
		});
		allSelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(MarkAdapter.flag){
					MarkAdapter.flag=false;
				}else{
					MarkAdapter.flag=true;
				}
				data.notifyDataSetChanged();
				int size=list.size();
				System.out.println("---list--se--.>"+size);
			}
		});
		
		deleteBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for(SaveContent m:list){
					if(m.isChecked()){
						manger.newsDeledata(m.get_id()+"");
					}
				}

				list =manger.queryData();
				int size=list.size();
				tv.setText("共"+size+"条");
				if(list.isEmpty()){
					tvNull.setText("暂无记录");
				}
				data = new MarkAdapter(MarkActivity.this, list);
				markView.setAdapter(data);
			}
		});
	}
}
