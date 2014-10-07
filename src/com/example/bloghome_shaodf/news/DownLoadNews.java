package com.example.bloghome_shaodf.news;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import android.util.Xml;

public class DownLoadNews {
	
	String path;
	public DownLoadNews(String path) {
		
		this.path=path;
	}
	/**
	 * 数据下载
	 * @return InputStream
	 */
	private InputStream downLoad( ){
		
		URL url;
		InputStream is = null;
		try {
			url = new URL(path);
			HttpURLConnection hconn=(HttpURLConnection) url.openConnection();
			hconn.setRequestMethod("GET");
			hconn.connect();
			if (hconn.getResponseCode() == 200) {
				is=hconn.getInputStream();
			}else {
				System.out.println("网络请求错误！");
			}
			
			
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return is;
		
	}
	/**
	 * Pull解析数据
	 * @return List<Entry>
	 */
	public List<Entry> getMessage( ){
		
		List<Entry> listentry = null;
		Entry entry = null;
		
	
		try {
			
			
			InputStream is=downLoad();
			
			//XmlPull解析
			XmlPullParser  pull=Xml.newPullParser();
			pull.setInput(is, "UTF-8");
			
			
			int event=pull.getEventType();

			while(event!=XmlPullParser.END_DOCUMENT){
				switch(event){
				case XmlPullParser.START_DOCUMENT://当前事件是文档开始事件
				listentry=new ArrayList<Entry>();
					break;
				case XmlPullParser.START_TAG://标签元素开始事件
					if("entry".equals(pull.getName())){
						entry=new Entry();		
					}
					if(entry!=null){
						if("id".equals(pull.getName())){//判断开始标签元素是否是id
							
							entry.setId(pull.nextText());
							
						}else if("title".equals(pull.getName())){//判断开始标签元素是否是title
							entry.setTitle(pull.nextText());
						}else if ("summary".equals(pull.getName())) {
							
							entry.setSummary(pull.nextText());
						}else if ("updated".equals(pull.getName())) {
							entry.setUpdated(pull.nextText());
						}else if ("sourceName".equals(pull.getName())) {
							
							entry.setAythor_name(pull.nextText());
							
						}else if ("link".equals(pull.getName())) {
							//  获取属性值
							entry.setLink_href(pull.getAttributeValue(1));
							
						}else if ("views".equals(pull.getName())) {
							entry.setViews(pull.nextText());
							
						}else if ("comments".equals(pull.getName())) {
							entry.setComments(pull.nextText());
						}
					}
					break;
				case XmlPullParser.END_TAG://判断是否是结束标签
					if("entry".equals(pull.getName())){//判断结束标签是否是entry
						
						listentry.add(entry);//添加进集合
						entry=null;
						
					}
					break;
				}
				event=pull.next();//进入下一个元素并触发相应事件
			}//结束while
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return listentry;
		
	}
	
}
