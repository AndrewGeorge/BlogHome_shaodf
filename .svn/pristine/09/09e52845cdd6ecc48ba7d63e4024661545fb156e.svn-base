package com.example.bloghome_shaodf.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bloghome_shaodf.MainActivity;
import com.example.bloghome_shaodf.PublicData;
import com.example.bloghome_shaodf.R;

public class LoginActivity extends Activity implements OnClickListener {

	EditText username, passward;
	Button Login_btn, cancel_btn;
	LoginMessage message;
	ImageView userimage;
	ImageView loginimage;
	NotificationManager manger;
	Notification notification;
	SharedPreferences setSavePerson;
	CheckBox   cb_Person;
	Editor edit;
	public static  SharedPreferences setLoginMessge;
	
	public static final String ShareName="login_Message";
	
	Handler handler = new Handler() {

		@SuppressWarnings("deprecation")
		@SuppressLint("ShowToast")
		public void handleMessage(Message msg) {

			if (msg.what == 0) {
				
				if (message.getState().equals("0")) {

					// 登陆成功获得头像路径并开启异步图片加载
					new DownLoadImageTask().execute(message.getAvater());

					Toast.makeText(LoginActivity.this, "成功登陆，正在加载账户信息...", 1)
							.show();
				} else {

					username.setText("");
					passward.setText("");
					loginimage.clearAnimation();
					loginimage.setVisibility(View.GONE);
					// 清除动画
					Toast.makeText(LoginActivity.this, message.getErr0r_text(),
							0).show();
				}

			}
			// 接受完毕信息并进行Activity的跳转
			if (msg.what == 3) {

				manger = (NotificationManager) LoginActivity.this
						.getSystemService(Context.NOTIFICATION_SERVICE);

				notification = new Notification();
				notification.icon = R.drawable.icon;
				notification.tickerText = "登陆成功！";

				//将数据保存起来在通过通知进来之后还在登陆状态
				
				Editor edit=setLoginMessge.edit();
				edit.putString("sid", message.sid);
				edit.putBoolean("islogin", true);
				edit.commit();
						
				Intent inten1 = new Intent(LoginActivity.this,
						MainActivity.class);
				PendingIntent contentIntent = PendingIntent.getActivity(
						LoginActivity.this, 0, inten1, 0);

				notification.setLatestEventInfo(LoginActivity.this,
						message.getNickname(), null, contentIntent);
				manger.notify(10, notification);
				//设置登陆标志为true
				PublicData.isLogin = true;

				Intent intent = new Intent(LoginActivity.this,
						MainActivity.class);
				startActivity(intent);
				finish();
			}
		};

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
						| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.login_activity);
		super.onCreate(savedInstanceState);
		setView();
	}

	/**
	 * 初始化数据
	 */
	private void setView() {

		
		setLoginMessge=getSharedPreferences(ShareName, MODE_PRIVATE);
		username = (EditText) findViewById(R.id.username);
		username.setText(setLoginMessge.getString("username", ""));
		loginimage = (ImageView) findViewById(R.id.loginImage);
		passward = (EditText) findViewById(R.id.password);
		passward.setText(setLoginMessge.getString("password", ""));
		
		userimage = (ImageView) findViewById(R.id.userimage);
		Login_btn = (Button) findViewById(R.id.login_btn);
		Login_btn.setOnClickListener(this);
		cancel_btn = (Button) findViewById(R.id.cancel_btn);
		cancel_btn.setOnClickListener(this);
		cb_Person=(CheckBox) findViewById(R.id.cb_Person);
		//判断之前是否保存了密码，保存啦就就将ChexBox设置为true
		if (setLoginMessge.getString("password", "").equals("")) {
			cb_Person.setChecked(false);
		}else{
			cb_Person.setChecked(true);
		}
		
		
		
		cb_Person.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				
				
				if (arg1) {
					
					Editor editor=setLoginMessge.edit();
					editor.putString("username", username.getText().toString().trim());
					editor.putString("password", passward.getText().toString().trim());
					editor.commit();	
				}else {
					
					Editor editor=setLoginMessge.edit();
					editor.putString("username", username.getText().toString().trim());
					editor.putString("password", "");
					editor.commit();	
				}
				
				
				
				
				
			}
		});

	}

	/**
	 * 按钮的监听
	 */
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		int id = arg0.getId();

		switch (id) {
		// 登陆监听
		case R.id.login_btn:

			// 对账户名和密码初步判读不能为空
			if (username.getText().toString().trim().equals("")
					|| passward.getText().toString().trim().equals("")) {

				Toast.makeText(LoginActivity.this, "账户名和密码不能为空！", 1).show();

			} else {

				// 设置加载提示图标
				loginimage.setVisibility(View.VISIBLE);

				// 设置加载提动画
				loginimage.setRotation(R.anim.loadmore);

				Animation operatingAnim = AnimationUtils.loadAnimation(this,
						R.anim.loadmore);
				// 动画效果的几种方式
				LinearInterpolator lin = new LinearInterpolator();
				// 减速
				DecelerateInterpolator ddd = new DecelerateInterpolator();
				// 最后阶段弹球效果
				BounceInterpolator bb = new BounceInterpolator();
				// 在上一个基础上超出终点一小步再回到终点
				AnticipateOvershootInterpolator abt = new AnticipateOvershootInterpolator();
				// 加速
				AccelerateInterpolator acc = new AccelerateInterpolator();
				// 先加速再减速
				AccelerateDecelerateInterpolator asd = new AccelerateDecelerateInterpolator();
				//
				OvershootInterpolator ov = new OvershootInterpolator();

				operatingAnim.setInterpolator(bb);
				if (operatingAnim != null) {
					loginimage.startAnimation(operatingAnim);
				}
				// 启动线程登陆
				new Thread(new Runnable() {

					@Override
					public void run() {

						LoginDownLoad Down = new LoginDownLoad(
								"http://cnblogs.davismy.com/Handler.ashx?op=UserLogin&username="
										+ username.getText().toString().trim()
										+ "&pwd="
										+ passward.getText().toString().trim()
										+ "&aid=null&authcode=null");
						message = Down.getMessage();
						handler.sendEmptyMessage(0);

					}
				}).start();

			}

			break;
		// 取消登陆不能发表评论
		case R.id.cancel_btn:

			Intent intent = new Intent(LoginActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
			break;

		default:
			break;
		}
	}

	/**
	 * 
	 * 下载博主头像
	 * 
	 * @author Administrator
	 * 
	 */
	private class DownLoadImageTask extends AsyncTask<String, Integer, Bitmap> {

		@Override
		protected Bitmap doInBackground(String... params) {

			LoadUserImage login = new LoadUserImage(params[0]);

			return login.getUserImage();
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			// 设置加载图片
			userimage.setImageBitmap(result);
			// 发送加载完毕信息
			handler.sendEmptyMessage(3);
		}

	}

}
