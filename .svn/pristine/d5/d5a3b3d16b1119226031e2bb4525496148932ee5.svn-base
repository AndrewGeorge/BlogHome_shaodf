package com.example.bloghome_shaodf;

import com.example.bloghome_shaodf.login.LoginActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class WelcomActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcom);
		
		
		new Thread(){
			
			@Override
			public void run() {
				
				
				try {
					
					Thread.sleep(3000);
					Intent intent=new Intent(WelcomActivity.this,LoginActivity.class);
					startActivity(intent);
					finish();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
				
				super.run();
			}
		}.start();
	
	}
	
}
