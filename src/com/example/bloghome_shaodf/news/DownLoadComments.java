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

public class DownLoadComments {

	String path;
	
	public DownLoadComments(String path) {
		this.path=path;
	}
	/**
	 * ��������
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
				System.out.println("�����������");
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
	public List<Comments> getMessage( ){
		
		List<Comments> listComments = null;
		Comments comments = null;
		
	
		try {
			InputStream is=downLoad();
			XmlPullParser  pull=Xml.newPullParser();
			pull.setInput(is, "UTF-8");
			int event=pull.getEventType();
			while(event!=XmlPullParser.END_DOCUMENT){
				switch(event){
				case XmlPullParser.START_DOCUMENT://��ǰ�¼����ĵ���ʼ�¼�
					listComments=new ArrayList<Comments>();
					break;
				case XmlPullParser.START_TAG://��ǩԪ�ؿ�ʼ�¼�
					if("entry".equals(pull.getName())){
						comments=new Comments();		
					}
					if(comments!=null){
						if("published".equals(pull.getName())){//�жϿ�ʼ��ǩԪ���Ƿ���id
							comments.setTime(pull.nextText());
							
						}else if("name".equals(pull.getName())){//�жϿ�ʼ��ǩԪ���Ƿ���title
							comments.setName(pull.nextText());
						}else if ("content".equals(pull.getName())) {
							
							comments.setComment(pull.nextText());
							
						}
					}
					break;
				case XmlPullParser.END_TAG://�ж��Ƿ��ǽ�����ǩ
					if("entry".equals(pull.getName())){//�жϽ�����ǩ�Ƿ���book
						listComments.add(comments);//���ӽ�����
						comments=null;
					}
					break;
				}
				event=pull.next();//������һ��Ԫ�ز�������Ӧ�¼�
			}//����while
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return listComments;
		
	}
	

	
	
}