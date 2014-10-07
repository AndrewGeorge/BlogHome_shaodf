package com.example.bloghome_shaodf.blog;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
public class SaxService {

	public SaxService(){
		//TODO
	}
	
	public static List<BlogInfo> readxml(InputStream inputStream,String nodeName){
		List<BlogInfo> list = null;
		try {
			
			
			//创建解析工厂
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser parser = spf.newSAXParser();
			BlogSAX blogSax = new BlogSAX(nodeName);
			parser.parse(inputStream, blogSax);
			inputStream.close();
			return blogSax.getList();
		} catch (Exception e) {
			//TODO
		}
		
		return null;
		
	}
	public static InputStream getXML(String path){
		InputStream input = null;
		try {
			URL url = new URL(path);
			if (url != null) {
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setConnectTimeout(3000);
				connection.setDoInput(true);
				connection.setRequestMethod("GET");
				int code = connection.getResponseCode();
				if (code == 200) {
					input = connection.getInputStream();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
		return input;
		
	}
	
}
