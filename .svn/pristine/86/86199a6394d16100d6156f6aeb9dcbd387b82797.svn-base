package com.example.bloghome_shaodf.login;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class LoadUserImage {

	String path;
	BufferedInputStream buff;
	public LoadUserImage(String path) {
		this.path=path;
	}
	
	/**
	 * œ¬‘ÿÕº∆¨
	 */
	
	public  Bitmap getUserImage() {

		try {
			URL url=new URL(path);
			HttpURLConnection conn=(HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.connect();
			if (conn.getResponseCode()==200) {
				
				InputStream is=conn.getInputStream();
				buff=new BufferedInputStream(is);
			}
			
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return BitmapFactory.decodeStream(buff);
		
		
	}
	
}
