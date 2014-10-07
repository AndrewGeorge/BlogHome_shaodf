package com.example.bloghome_shaodf.news;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.bloghome_shaodf.PublicData;
import com.example.bloghome_shaodf.R;
import com.example.bloghome_shaodf.login.Date;

public class NewsCommentsActivity extends Activity {

	List<Comments> entrylist;
	TextView no_comments;
	TextView hotnewscomments_title, updatacomments, submitcomments;
	ListView comments_listview;
	EditText comments;
	String id;
	View view;
	ProgressDialog pro;
	CommentResponse Commentresponse;
	public final int DownLaodOver = 0;
	NewsCommentsAdapter adapter;
	Editor editor;
	AlertDialog.Builder builder;

	Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {

			if (msg.what == DownLaodOver) {

				if (entrylist.size() == 0) {

					no_comments.setVisibility(View.VISIBLE);

				} else {

					adapter = new NewsCommentsAdapter(NewsCommentsActivity.this,
							entrylist);
					comments_listview.setAdapter(adapter);
				}
				pro.dismiss();
				Toast.makeText(NewsCommentsActivity.this, "下载完毕", 0).show();
			}

			// 发表评论的相关信息
			if (msg.what == 4) {

				// 不论发表成功均返回结果
				pro.dismiss();
				if (Commentresponse.IsSucces.equals("true")) {

					new DLondComments().start();
					adapter.notifyDataSetChanged();

					Toast.makeText(NewsCommentsActivity.this, "发表成功！", 0).show();

				} else if (Commentresponse.IsSucces.equals("false")) {

					Toast.makeText(NewsCommentsActivity.this,
							Commentresponse.message, 0).show();
				}

			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 设置无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.comments_activity);
		SetView();
		
		new DLondComments().start();
		pro.setMessage("正在努力加载，稍等一会儿哦！");
		pro.show();

	}

	// 初始化数据据
	private void SetView() {

		Intent intent = getIntent();
		id = intent.getStringExtra("ID");
		pro = new ProgressDialog(this);
		hotnewscomments_title = (TextView) findViewById(R.id.comments_list_item_title);
		hotnewscomments_title.setText(intent.getStringExtra("title"));
		no_comments = (TextView) findViewById(R.id.no_comments);
		comments_listview = (ListView) findViewById(R.id.comments_listview);
		updatacomments = (TextView) findViewById(R.id.comment_uptadacomments);
		submitcomments = (TextView) findViewById(R.id.comment_tocomment);

		LayoutInflater inflater = NewsCommentsActivity.this.getLayoutInflater();
		view = inflater.inflate(R.layout.commentstext, null);
		comments = (EditText) view.findViewById(R.id.comments_alertdialog);
		builder = new Builder(NewsCommentsActivity.this);
		updatacomments.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				
				//开启线程更新数据
				new DLondComments().start();
				pro.setMessage("正在努力加载，稍等一会儿哦！");
				pro.show();
				
				
			}
		});
		submitcomments.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (PublicData.isLogin == true) {

					builder.setTitle("请输入你的评论内容：");
				

					builder.setView(view);

					builder.setPositiveButton("提交",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {

									// 通过异步加载，提交评论信息
									new DownloadConnentsRsponse().start();
								
									pro.setMessage("正在提交评论！");
									pro.show();

								}

							});
					builder.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {

								}
							});
					builder.show();

				} else {

					Toast.makeText(NewsCommentsActivity.this, "你还没有登陆，登陆后才能发表评论！",
							0).show();

				}

			}
		});

	}

	public class DLondComments extends Thread {

		@Override
		public void run() {

			String path = PublicData.hotNewsCommentsPath.replace("POSTID", id)
					.replace("PAGEINDEX", "1").replace("PAGESIZE", "100");
			DownLoadComments dlComments = new DownLoadComments(path);
			entrylist = dlComments.getMessage();
			handler.sendEmptyMessage(DownLaodOver);
			super.run();
		}
	}

	public class DownloadConnentsRsponse extends Thread {

		@Override
		public void run() {

			System.out.println(Date.setLoginMessge.getString("sid", "0"));
			Commentresponse = getCommentResponseCode("http://cnblogs.davismy.com/Handler.ashx?op=SendNewsComment&&sid="
					+ Date.setLoginMessge.getString("sid", "0")
					+ "&postId="
					+ id
					+ "&commendBody="
					+ comments.getText()
					+ "&parentCommentID=10&is_add_from=10");

			
			handler.sendEmptyMessage(4);
			super.run();
		}

	}

	/**
	 * 提交评论信息
	 * 
	 * @param path
	 * @return
	 */
	private String submitComments(String path) {

		InputStream is;
		StringBuilder sBuilder = null;
		String readline;

		try {

			URL url = new URL(path);
			HttpURLConnection hconn = (HttpURLConnection) url.openConnection();

			hconn.connect();

			is = hconn.getInputStream();
			BufferedReader bufr = new BufferedReader(new InputStreamReader(is));
			sBuilder = new StringBuilder();
			if ((readline = bufr.readLine()) != null) {

				sBuilder.append(readline);
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sBuilder.toString();

	}

	/**
	 * 解析返回信息
	 */

	private CommentResponse getCommentResponseCode(String path) {

		String IsSucces = null;
		CommentResponse commentsresponse = null;

		// 获取返回信息！
		String reResponse = submitComments(path);

		// 解析数据
		// 设置解析以data
		try {
			JSONObject jsonobject = new JSONObject(reResponse)
					.getJSONObject("data");

			commentsresponse = new CommentResponse(
					jsonobject.getString("IsSuccess"),
					jsonobject.getString("Message"));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return commentsresponse;

	}

}
