package com.example.bloghome_shaodf.login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.JsonReader;
import android.util.Xml;

import com.example.bloghome_shaodf.news.Entry;

public class LoginDownLoad {

	StringBuilder sBuilder;
	String path;
	String readline;

	public LoginDownLoad(String Path) {

		this.path=Path;

	}

	/**
	 * 数据下载
	 * 
	 * @return InputStream
	 */
	private String downLoad() {

		URL url;
		InputStream is = null;
		try {
			url = new URL(path);
			HttpURLConnection hconn = (HttpURLConnection) url.openConnection();
			hconn.setRequestMethod("GET");
			hconn.connect();
			if (hconn.getResponseCode() == 200) {
				is = hconn.getInputStream();

				BufferedReader bufr = new BufferedReader(new InputStreamReader(
						is));
				sBuilder = new StringBuilder();

				if ((readline = bufr.readLine()) != null) {

					sBuilder.append(readline);
				}
			} else {
				System.out.println("网络请求错误！");
			}

		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sBuilder.toString();

	}

	/**
	 * Jiso数据解析
	 * 
	 * @return List<Entry>
	 */
	public LoginMessage getMessage() {

		try {

			// 获取String字符串
			String logi = downLoad();
			// 解析数据
			// 设置解析以data
			JSONObject jsonobject = new JSONObject(logi).getJSONObject("data");
			LoginMessage message = new LoginMessage();

			message.setAid(jsonobject.getString("aid"));
			System.out.println("aid");
			message.setAvater(jsonobject.getString("avatar"));
			System.out.println("avatar");
			message.setNickname(jsonobject.getString("nickname"));
			message.setState(jsonobject.getString("state"));
			message.setUsername(jsonobject.getString("username"));
			message.setUid(jsonobject.getString("uid"));
			message.setSid(jsonobject.getString("sid"));
			message.setErr0r_text(jsonobject.getString("error_text"));
			
			return message;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
