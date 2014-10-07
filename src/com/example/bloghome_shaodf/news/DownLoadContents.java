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
				case XmlPullParser.START_DOCUMENT://��ǰ�¼����ĵ���ʼ�¼�
					listcontents=new ArrayList<Contents>();
					break;
				case XmlPullParser.START_TAG://��ǩԪ�ؿ�ʼ�¼�
					if("NewsBody".equals(pull.getName())){
						contents=new Contents();		
					}
					if(contents!=null){
						if("Title".equals(pull.getName())){//�жϿ�ʼ��ǩԪ���Ƿ���id
							contents.setTitle(pull.nextText());
							
						}else if("SourceName".equals(pull.getName())){//�жϿ�ʼ��ǩԪ���Ƿ���title
							contents.setSourceName(pull.nextText());
						}else if ("SubmitDate".equals(pull.getName())) {
							
							contents.setSubmitDate(pull.nextText());
							
						}else if ("Content".equals(pull.getName())) {
							
							contents.setContent(pull.nextText());
							
						}else if ("ImageUr".equals(pull.getName())) {
							//  ��ȡ����ֵ
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
				case XmlPullParser.END_TAG://�ж��Ƿ��ǽ�����ǩ
					if("NewsBody".equals(pull.getName())){//�жϽ�����ǩ�Ƿ���
						listcontents.add(contents);//���ӽ�����
						contents=null;
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
		
		return listcontents;
		
	}
	

	
}