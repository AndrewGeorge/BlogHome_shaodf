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

/**
 * DownLoad contents  of News and use XMLPull get data;
 * @author Administrator
 *
 */

public class DownLoadContents {

	String path;
	
	public DownLoadContents(String path) {

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
	 * 
	 * 
	 * @return
	 */
	public List<Contents> getMessage( ){
		
		List<Contents> listcontents = null;
		Contents contents = null;
		
	
		try {
			
			InputStream is=downLoad();
			XmlPullParser  pull=Xml.newPullParser();
			pull.setInput(is, "UTF-8");
			int event=pull.getEventType();
			
			
			while(event!=XmlPullParser.END_DOCUMENT){
				switch(event){
				case XmlPullParser.START_DOCUMENT://当前事件是文档开始事件
					listcontents=new ArrayList<Contents>();
					break;
				case XmlPullParser.START_TAG://标签元素开始事件
					if("NewsBody".equals(pull.getName())){
						contents=new Contents();		
					}
					if(contents!=null){
						if("Title".equals(pull.getName())){//判断开始标签元素是否是id
							contents.setTitle(pull.nextText());
							
						}else if("SourceName".equals(pull.getName())){//判断开始标签元素是否是title
							contents.setSourceName(pull.nextText());
						}else if ("SubmitDate".equals(pull.getName())) {
							
							contents.setSubmitDate(pull.nextText());
							
						}else if ("Content".equals(pull.getName())) {
							
							contents.setContent(pull.nextText());
							
						}else if ("ImageUr".equals(pull.getName())) {
							//  获取属性值
							contents.setImageUrl(pull.nextText());
							
						}else if ("PrevNews".equals(pull.getName())) {
							contents.setPrevNews(pull.nextText());
							
						}else if ("NextNews".equals(pull.getName())) {
							contents.setNextNews(pull.nextText());
							
						}else if ("CommentCount".equals(pull.getName())) {
							
						contents.setCommentCount(pull.nextText());
					}
					}
					break;
				case XmlPullParser.END_TAG://判断是否是结束标签
					if("NewsBody".equals(pull.getName())){//判断结束标签是否是
						listcontents.add(contents);//添加进集合
						contents=null;
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
		
		return listcontents;
		
	}
	

	
}
