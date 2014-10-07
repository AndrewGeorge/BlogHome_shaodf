package com.example.bloghome_shaodf.blog;

import java.io.InputStream;
import com.example.bloghome_shaodf.R;
import com.example.bloghome_shaodf.collect.BlogSQLHelper;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Blog_Content_Activity extends Activity implements OnClickListener {
	private static final String BLOGER_URL = "http://wcf.open.cnblogs.com/blog/bloggers/search?t=";
	private static final int sDefaultTimeout = 3000;
	private static final int FADE_OUT = 1;
    private static final int SHOW_PROGRESS = 2;
    private static final int BLOGER_OUT = 3;
    private static final int BLOGER_SHOW = 4;
    private String content;
	private String title;
	private String id;
	TextView titleTv;
	WebView webV;
	String url="http://wcf.open.cnblogs.com/blog/post/body/";
	private BlogsInfo blogsInfo;
	private BlogerInfo blogerInfo;
	private RelativeLayout rightPanel;
	private RelativeLayout blogerPanel;
	private TextView toBlogs;
	private boolean blogerShowing;
	private boolean mShowing;
	
	private ImageView blogComment,saveBlog,showBloger,toTop;
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
            switch (msg.what) {
                case FADE_OUT:
                    hideRightPanel();
                    break;
                case SHOW_PROGRESS:
                    if (mShowing) {
                        msg = obtainMessage(SHOW_PROGRESS);
                        sendMessageDelayed(msg, 1000);
                    }
                    break;
                case BLOGER_OUT:
                	hideBlogerPanel();
                	break;
                case BLOGER_SHOW:
                	if (mShowing) {
                        msg = obtainMessage(BLOGER_SHOW);
                        sendMessageDelayed(msg, 1000);
                    }
                	break;
            }
		}
	};
	
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	//设置无标题  
    requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.blog_content);
	setView();
	Bundle bundle = this.getIntent().getExtras();
	title = bundle.getString("title");
	id = bundle.getString("id");
	titleTv.setText(title);
	toBlogs = (TextView)findViewById(R.id.panel_blog_list);
	
	blogerPanel.setOnTouchListener(new View.OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			showBlogerPanel();
			return true;
		}
	});
	toBlogs.setOnClickListener(this);
	
	blogComment.setOnClickListener(this);
	showBloger.setOnClickListener(this);
	saveBlog.setOnClickListener(this);
	toTop.setOnClickListener(this);

	webV.getSettings().setDefaultTextEncodingName("UTF-8");
	webV.getSettings().setBuiltInZoomControls(true);
	webV.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
	new getInfo().execute("http://wcf.open.cnblogs.com/blog/post/body/"+id);
}

public boolean dispatchTouchEvent(MotionEvent ev) {
	showRightPanel();
	return super.dispatchTouchEvent(ev);
}

/**
 * 初始化数据
 */
private void setView() {
	
	
	titleTv = (TextView) findViewById(R.id.blog_content_title);
	webV = (WebView) findViewById(R.id.blog_content);
	rightPanel = (RelativeLayout)findViewById(R.id.right_panel);
	blogerPanel = (RelativeLayout)findViewById(R.id.bloger_panel);
	blogComment = (ImageView)findViewById(R.id.blog_comment);
	saveBlog = (ImageView)findViewById(R.id.save_blog);
	showBloger = (ImageView)findViewById(R.id.show_bloger);
	toTop = (ImageView)findViewById(R.id.to_top);
	Intent intent = getIntent();
	id = intent.getStringExtra("id");
	String name = intent.getStringExtra("title");
	titleTv.setText(name);
}

private  class getInfo extends AsyncTask<String, Void,StringBuffer >{

	protected StringBuffer doInBackground(String... params) {
		InputStream inputstream=Util.getInputstream(params[0]);
		SaxHtml sax=new SaxHtml();
		StringBuffer buffer=new StringBuffer();
		buffer=sax.getString(inputstream);
		return buffer;
	}
	protected void onPostExecute(StringBuffer result) {
		super.onPostExecute(result);
		webV.loadDataWithBaseURL(null, result.toString(), "text/html", "UTF-8", null);
	}
}

//四个悬浮按钮的监听事件
public void onClick(View v) {
	
	switch(v.getId()) {
	case R.id.blog_comment:
		Intent intent=new Intent(Blog_Content_Activity.this,Blog_Comments_Activity.class);
		intent.putExtra("COMURL", "http://wcf.open.cnblogs.com/blog/post/");
		intent.putExtra("ID", blogsInfo.getId());
		intent.putExtra("blogTitle", blogsInfo.getTitle());
		startActivity(intent);
		break;
	case R.id.save_blog:
		BlogSQLHelper.getDBInstance(this).insertIntoDB(content, blogsInfo);
		
		break;
	case R.id.show_bloger:
		showBlogerPanel();
		break;
	case R.id.to_top:
		webV.scrollTo(0, 0);
		break;
	case R.id.panel_blog_list:
		if(null != blogerInfo) {

			String blogapp = blogerInfo.getBlogapp();
			String updateTime = blogerInfo.getUpdated();
			String blogerName = blogerInfo.getTitle();
			String blogerImgUrl =blogerInfo.getAvatar();
	        String blogCount = blogerInfo.getPostcount(); 
	        
	        Intent i = new Intent( this,Blog_Comments_Activity.class);
			
			i.putExtra("blogapp", blogapp);
			i.putExtra("updateTime", updateTime);
			i.putExtra("bloger", blogerName);
			i.putExtra("blogerImgUrl",blogerImgUrl);
			i.putExtra("blogCount", blogCount);
			
			startActivity(i);
		}
		break;
	}
}

private void showRightPanel() {
	if(!mShowing) {
		rightPanel.setVisibility(View.VISIBLE);
		mShowing = true;
	}
	
	
	mHandler.sendEmptyMessage(SHOW_PROGRESS);
	
	Message msg = mHandler.obtainMessage(FADE_OUT);
    mHandler.removeMessages(FADE_OUT);
    mHandler.sendMessageDelayed(msg, sDefaultTimeout);
}

private void hideRightPanel() {
	if (mShowing) {
		mHandler.removeMessages(SHOW_PROGRESS);
		rightPanel.setVisibility(View.GONE);
        mShowing = false;
    }
}

private void showBlogerPanel() {
	if(!blogerShowing) {
		blogerPanel.setVisibility(View.VISIBLE);
		blogerShowing = true;
	}
	mHandler.sendEmptyMessage(BLOGER_SHOW);
	
	Message msg = mHandler.obtainMessage(BLOGER_OUT);
    mHandler.removeMessages(BLOGER_OUT);
    mHandler.sendMessageDelayed(msg, sDefaultTimeout);
}

private void hideBlogerPanel() {
	if (blogerShowing) {
		mHandler.removeMessages(BLOGER_SHOW);
		blogerPanel.setVisibility(View.GONE);
        blogerShowing = false;
    }
}
}
