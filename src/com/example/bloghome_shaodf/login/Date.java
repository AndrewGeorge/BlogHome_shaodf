package com.example.bloghome_shaodf.login;

import java.util.HashMap;

import cn.sharesdk.facebook.Facebook;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.UIHandler;
import cn.sharesdk.renren.Renren;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;

import com.example.bloghome_shaodf.MainActivity;
import com.example.bloghome_shaodf.PublicData;
import com.example.bloghome_shaodf.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class Date extends Activity implements OnClickListener,
		OnFocusChangeListener ,Callback,PlatformActionListener{
	ImageButton otherLogin;
	LinearLayout popupLinear;
	TextView login_skip;
	ImageButton mPopupImageButton;
	public PopupWindow pop;
	public EditText mAccountsEditText;
	EditText mPassEditText;
	CheckBox mRemPassCheck;
	Button mLoginButton;
	public myAdapter adapter;
	public HashMap<String, String> list;
	Object[] account;
	ListView listView;
	LoginMessage message;
	NotificationManager manger;
	Notification notification;
	SharedPreferences setSavePerson;
	public static SharedPreferences setLoginMessge;
	public static final String ShareName = "login_Message";
	ProgressDialog pro;
	Platform plat;
	private static final int MSG_USERID_FOUND = 1;
	private static final int MSG_LOGIN = 2;
	private static final int MSG_AUTH_CANCEL = 3;
	private static final int MSG_AUTH_ERROR= 4;
	private static final int MSG_AUTH_COMPLETE = 5;
	Handler handler = new Handler() {

		@SuppressWarnings("deprecation")
		@SuppressLint("ShowToast")
		public void handleMessage(Message msg) {

			// 接受完毕信息并进行Activity的跳转
			if (msg.what == 0) {
				pro.dismiss();

				manger = (NotificationManager) Date.this
						.getSystemService(Context.NOTIFICATION_SERVICE);

				notification = new Notification();
				notification.icon = R.drawable.icon;
				notification.tickerText = "登陆成功！";

				// 将数据保存起来在通过通知进来之后还在登陆状态
				Editor edit = setLoginMessge.edit();
				edit.putString("sid", message.sid);
				edit.putBoolean("islogin", true);
				edit.commit();

				Intent inten1 = new Intent(Date.this, MainActivity.class);
				PendingIntent contentIntent = PendingIntent.getActivity(
						Date.this, 0, inten1, 0);

				notification.setLatestEventInfo(Date.this,
						message.getNickname(), null, contentIntent);
				manger.notify(10, notification);
				// 设置登陆标志为true
				PublicData.isLogin = true;

				Intent intent = new Intent(Date.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		};

	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ShareSDK.initSDK(this);
		setContentView(R.layout.loginpage);

		// 本地存储
		setLoginMessge = getSharedPreferences(ShareName, MODE_PRIVATE);
		prepare();
		otherLogin = (ImageButton) findViewById(R.id.login_option);
		otherLogin.setOnClickListener(this);
		mPopupImageButton = (ImageButton) findViewById(R.id.popupwindow);
		mRemPassCheck = (CheckBox) findViewById(R.id.login_cb_savepwd);
		mLoginButton = (Button) findViewById(R.id.login_btn_login);
		mLoginButton.setBackgroundColor(android.graphics.Color
				.parseColor("#EBEEF0"));
		login_skip = (TextView) findViewById(R.id.login_skip);
		login_skip.setOnClickListener(this);
		mAccountsEditText = (EditText) findViewById(R.id.login_edit_account);
		mPassEditText = (EditText) findViewById(R.id.login_edit_pwd);
		mPassEditText.setOnFocusChangeListener(this);
		mAccountsEditText.setOnFocusChangeListener(this);
		mPopupImageButton.setOnClickListener(this);
		mLoginButton.setOnClickListener(this);
	

	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.login_edit_pwd:
			if (hasFocus) {// 获得焦点，则获得密码
				String account = mAccountsEditText.getText().toString();
				if (account.equals("")) {
					break;//
				}
				if (list.containsKey(account)) {
					mPassEditText.setText(list.get(account));
				}
			}
			break;
		case R.id.login_edit_account:
			if (hasFocus) {
				mAccountsEditText.setText("");
				mPassEditText.setText("");
			}
			break;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.popupwindow:

			if (pop == null) {
				if (adapter == null) {
					adapter = new myAdapter();
					listView = new ListView(Date.this);
					pop = new PopupWindow(listView,
							mAccountsEditText.getWidth(),
							LayoutParams.WRAP_CONTENT);
					listView.setAdapter(adapter);
					pop.showAsDropDown(mAccountsEditText);
				} else {
					account = list.keySet().toArray();
					adapter.notifyDataSetChanged();
					pop = new PopupWindow(listView,
							mAccountsEditText.getWidth(),
							LayoutParams.WRAP_CONTENT);
					pop.showAsDropDown(mAccountsEditText);
				}
			} else {
				pop.dismiss();
				pop = null;
			}
			break;

		// 登录按钮
		case R.id.login_btn_login:
			if (mAccountsEditText.getText().toString().equals("")) {
				break;
			}
			String account = mAccountsEditText.getText().toString();
			String pass = mPassEditText.getText().toString();
			ImagedbUtil db = new ImagedbUtil(Date.this);
			db.open();
			Cursor cursor = db.getCursorArgs(new String[] { db.getKEY() },
					new String[] { account });
			int keyindex = cursor.getColumnIndexOrThrow(db.getKEY());
			if (mRemPassCheck.isChecked()) {
				// 保存密码
				if (cursor.getCount() > 0) {
					int id = cursor.getInt(keyindex);
					safeReleaseCursor(cursor);
					db.update(id, pass);
					safeReleaseDatabase(db);
				} else {
					safeReleaseCursor(cursor);
					db.create(account, pass);
					safeReleaseDatabase(db);
				}
				list.put(account, pass);// 重新替换或者添加记录
			}

			else {
				// 不保存密码
				if (cursor.getCount() > 0) {
					int id = cursor.getInt(keyindex);
					safeReleaseCursor(cursor);
					db.update(id, "");
					safeReleaseDatabase(db);
				} else {
					safeReleaseCursor(cursor);
					db.create(account, "");
					safeReleaseDatabase(db);
				}

				list.put(account, "");// 重新替换或者添加记录
			}
			// 对账户名和密码初步判读不能为空
			if (mAccountsEditText.getText().toString().trim().equals("")
					|| mPassEditText.getText().toString().trim().equals("")) {

				Toast.makeText(Date.this, "账户名和密码不能为空！", 1).show();

			} else {

				pro = new ProgressDialog(Date.this);
				pro.setMessage("正在登陆...");
				pro.show();
				// 启动线程登陆
				new Thread(new Runnable() {

					@Override
					public void run() {

						LoginDownLoad Down = new LoginDownLoad(
								"http://cnblogs.davismy.com/Handler.ashx?op=UserLogin&username="
										+ mAccountsEditText.getText()
												.toString().trim()
										+ "&pwd="
										+ mPassEditText.getText().toString()
												.trim()
										+ "&aid=null&authcode=null");
						message = Down.getMessage();
						handler.sendEmptyMessage(0);

					}
				}).start();

			}

			break;
		case R.id.login_skip:

			Intent intent = new Intent(Date.this, MainActivity.class);
			PublicData.isLogin = false;
			startActivity(intent);
			finish();
			break;
		case R.id.login_option:
			//其他登陆方式
			popupOthers();
			break;
			
		case R.id.tvqzone:
			Dialog dlg = (Dialog) v.getTag();
			dlg.dismiss();
			
			
			authorize(plat=new QZone(this));
			break;
		case R.id.tvsinaweibo:
			Dialog dlg1 = (Dialog) v.getTag();
			dlg1.dismiss();
			authorize(plat=new SinaWeibo(this));
			break;
		case R.id.tvrenren:
			Dialog dlg2 = (Dialog) v.getTag();
			dlg2.dismiss();
			authorize(plat=new Renren(this));
			break;
		case R.id.tvfacebook:
			Dialog dlg3 = (Dialog) v.getTag();
			dlg3.dismiss();
			authorize(plat=new Facebook(this));
			break;

		}
	}

	private void prepare() {

		list = new HashMap<String, String>();
		ImagedbUtil db = new ImagedbUtil(this);
		db.open();
		Cursor cursor = db.getCursor(db.getKEY(), db.getACCOUNTS(),
				db.getPASSWORD());
		int accountsindex = cursor.getColumnIndexOrThrow(db.getACCOUNTS());
		int passindex = cursor.getColumnIndexOrThrow(db.getPASSWORD());
		String accounts;
		String pass;
		if (cursor.getCount() > 0) {
			do {
				accounts = cursor.getString(accountsindex);
				pass = cursor.getString(passindex);
				list.put(accounts, pass);
			} while (cursor.moveToNext());
		}
		safeReleaseCursor(cursor);
		safeReleaseDatabase(db);
	}

	private void safeReleaseCursor(Cursor cursor) {
		cursor.close();
		cursor = null;
	}

	private void safeReleaseDatabase(ImagedbUtil db) {
		db.close();
		db = null;
	}

	class myAdapter extends BaseAdapter {
		LayoutInflater mInflater;

		public myAdapter() {
			mInflater = LayoutInflater.from(Date.this);
			account = list.keySet().toArray();
			// TODO Auto-generated constructor stub
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return account.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			Holder holder = null;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.popup, null);
				holder = new Holder();
				holder.view = (TextView) convertView.findViewById(R.id.mQQ);
				holder.button = (ImageButton) convertView
						.findViewById(R.id.mQQDelete);
				convertView.setTag(holder);
			} else {

				holder = (Holder) convertView.getTag();
			}
			if (holder != null) {
				convertView.setId(position);
				holder.setId(position);
				holder.view.setText(account[position].toString());
				holder.view.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						pop.dismiss();
						mAccountsEditText.setText(account[position].toString());
						mPassEditText.setText(list.get(account[position]));
						return true;
					}
				});

				holder.button.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String accounts = account[position].toString();
						list.remove(accounts);
						ImagedbUtil db = new ImagedbUtil(Date.this);
						db.open();
						Cursor cursor = db.getCursorArgs(
								new String[] { db.getKEY() },
								new String[] { accounts });
						int keyindex = cursor.getColumnIndexOrThrow(db.getKEY());
						int id = cursor.getInt(keyindex);
						cursor.close();
						db.delete(id);
						account = list.keySet().toArray();
						adapter.notifyDataSetChanged();
					}
				});
			}
			return convertView;
		}

		class Holder {
			TextView view;
			ImageButton button;

			void setId(int position) {
				view.setId(position);
				button.setId(position);
			}
		}

	}

	@Override
	protected void onDestroy() {

		if(plat!=null){
			
			plat.removeAccount();
		}
		
		ShareSDK.stopSDK(this);
		super.onDestroy();
	}
	
	
	private void popupOthers() {

		Dialog dlg = new Dialog(this);
		
		View dlgView = View.inflate(this, R.layout.other_plat_dialog, null);
		
		View tvqzone = dlgView.findViewById(R.id.tvqzone);
		tvqzone.setTag(dlg);
		tvqzone.setOnClickListener(this);
		View tvsinaweibo = dlgView.findViewById(R.id.tvsinaweibo);
		tvsinaweibo.setTag(dlg);
		tvsinaweibo.setOnClickListener(this);
		View tvrenren = dlgView.findViewById(R.id.tvrenren);
		tvrenren.setTag(dlg);
		tvrenren.setOnClickListener(this);
		View tvfacebook = dlgView.findViewById(R.id.tvfacebook);
		tvfacebook.setTag(dlg);
		tvsinaweibo.setOnClickListener(this);
		
		dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dlg.setContentView(dlgView);
		dlg.show();
	}


	public boolean handleMessage(Message arg0) {
	
		
		switch(arg0.what) {
		case MSG_USERID_FOUND: {
			Toast.makeText(this, R.string.userid_found, Toast.LENGTH_SHORT).show();
		}
		break;
		case MSG_LOGIN: {
			
			String text = getString(R.string.logining, arg0.obj);
			
			Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
			
			manger = (NotificationManager) Date.this
					.getSystemService(Context.NOTIFICATION_SERVICE);

			notification = new Notification();
			notification.icon = R.drawable.icon;
			notification.tickerText = "登陆成功！";
			Intent inten1 = new Intent(Date.this, MainActivity.class);
			PendingIntent contentIntent = PendingIntent.getActivity(
					Date.this, 0, inten1, 0);

			notification.setLatestEventInfo(Date.this,
					"用户昵称！", null, contentIntent);
			manger.notify(10, notification);
			Intent intent = new Intent(Date.this, MainActivity.class);
			startActivity(intent);
			finish();
			
		}
		break;
		case MSG_AUTH_CANCEL: {
			Toast.makeText(this, R.string.auth_cancel, Toast.LENGTH_SHORT).show();
		}
		break;
		case MSG_AUTH_ERROR: {
			Toast.makeText(this, R.string.auth_error, Toast.LENGTH_SHORT).show();
		}
		break;
		case MSG_AUTH_COMPLETE: {
			Toast.makeText(this, R.string.auth_complete, Toast.LENGTH_SHORT).show();
		}
		break;
	}
	return false;
	}

	@Override
	public void onCancel(Platform arg0, int arg1) {
		// TODO Auto-generated method stub
		if (arg1 == Platform.ACTION_USER_INFOR) {
			UIHandler.sendEmptyMessage(MSG_AUTH_CANCEL, this);
		}
	}

	@Override
	public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
		
		if (arg1 == Platform.ACTION_USER_INFOR) {
			
			UIHandler.sendEmptyMessage(MSG_AUTH_COMPLETE, this);
		System.out.println(arg2);
			login(arg0.getName(), arg0.getDb().getUserId(), arg2);
		}
		System.out.println(arg2);
	}

	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2) {
		// TODO Auto-generated method stub
		if (arg1 == Platform.ACTION_USER_INFOR) {
			UIHandler.sendEmptyMessage(MSG_AUTH_ERROR, this);
		}
		arg2.printStackTrace();
	}
	
	private void authorize(Platform plat) {
		if (plat == null) {
			popupOthers();
			return;
		}
		
		if(plat.isValid()) {
			String userId = plat.getDb().getUserId();
			if (userId != null) {
				
				UIHandler.sendEmptyMessage(MSG_USERID_FOUND, this);
				login(plat.getName(), userId, null);
				return;
			}
		}
		plat.setPlatformActionListener(this);
		plat.SSOSetting(true);
		plat.showUser(null);
	}
	
	private void login(String plat, String userId, HashMap<String, Object> userInfo) {
		Message msg = new Message();
		msg.what = MSG_LOGIN;
		msg.obj = plat;
		//在此判断userId是否是你的已注册的用户，如果是进行相应的引到操作
		//不是获取资料来在自己的登陆系统注册。
		UIHandler.sendMessage(msg, this);
	}
	
}
