package com.example.bloghome_shaodf.tools;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.bloghome_shaodf.MainActivity;
import com.example.bloghome_shaodf.R;
import com.example.bloghome_shaodf.R.layout;
import com.example.bloghome_shaodf.collect.TouchListener;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class SetActivity extends Activity {
	Button backBtn;
	Button aboutBtn;
	View view;
	LayoutInflater Inflater;
	GridView gView;
	LinearLayout   setLayout;
	 TouchListener touchListener ;
	ArrayList<HashMap<String, Object>> data;
	// ��̬�������ģ����ڱ���Ŀ��������������
	public static Integer[] imgs = { R.drawable.left_menu_bg1, R.drawable.left_menu_bg2,
		R.drawable.left_menu_bg5, R.drawable.left_menu_bg4 };;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settingpage);
		setLayout=(LinearLayout) findViewById(R.id.setLayout);
		//���һ���
		 touchListener = new TouchListener(this, false);
		 setLayout.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					if (touchListener.onTouch(setLayout, event))
						return true;
					else if (MainActivity.isopen)
						return true;
					return false;
				}
			});
		
		backBtn = (Button) findViewById(R.id.feedback_bt);
		aboutBtn = (Button) findViewById(R.id.about_bt);
		Inflater = getLayoutInflater();
		gView = (GridView) findViewById(R.id.gridview);
		data = new ArrayList<HashMap<String, Object>>();
		
		for (int i = 0; i < 4; i++) {
			// �л��ɲ�ͬͼƬ��������
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("img", imgs[i]);
			// ��һ��Ĭ�ϵı�ֽ
			map.put("name", "��ֽ" + (i + 1));
			data.add(map);
		}
		SimpleAdapter adapter = new SimpleAdapter(this, data,
				R.layout.setting_bg, new String[] { "img", "name"}, new int[] {
						R.id.left_menu_bg, R.id.bg_name});
		gView.setAdapter(adapter);
		gView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// ��һ���㲥�����leftMenu�����չ㲥�󲢽��б����л�
				Intent intent=new Intent();
				intent.setAction("switch_bg");
				intent.putExtra("img", arg2);
				sendBroadcast(intent);
			}
		});
		aboutBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ����Ӧ��������һ�������ļ�������������������ԣ�������һ���Ի���
				view = Inflater.inflate(R.layout.about_text, null);
				Builder builder = new Builder(SetActivity.this);
				builder.setTitle("����");
				builder.setMessage("��ǰ�汾��1.0 \n�˰汾������Android 2.2�����ϲ���ϵͳϵ���ֻ�");
				builder.show();
			}
		});
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				view = Inflater.inflate(R.layout.about_text, null);
				Builder builder = new Builder(SetActivity.this);
				builder.setTitle("�����ʼ���");
				builder.setMessage("û��Ӧ�ÿ�ִ�д˲���");
				builder.show();
				// ������һ������������һ��QQ��Ȩ����QQ���ʼ�
			}
		});
	}
}
