package com.example.bloghome_shaodf.blog;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class BlogSAX extends DefaultHandler{

	private BlogInfo blog = null;//存储单个解析的完整对象
	private List<BlogInfo> list = null;//存储所有解析对象
	private String currentTag = null;//正在解析元素的标签
	private String currentValue = null;//表示当前解析元素的值
	private String nodeName = null;//表示当前节点的名称
	
	public BlogSAX(String nodeName) {
		this.nodeName = nodeName;
	}
	
	public List<BlogInfo> getList(){
		return list;
	}

	public void startDocument() throws SAXException {
		super.startDocument();
		//当读到第一个标签的时候会触发这个事件
		list = new ArrayList<BlogInfo>();
	}
	
	/**
	 * 
	 */
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		//当遇到文档开头的时候调用
		if (qName.equals(nodeName)) {
			blog = new BlogInfo();
		}
		if (attributes != null && blog != null) {
			//此方法用来判定标签当中的属性
		}
		currentTag = qName;
	}
	
	
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);
		//这个方法是用来处理xml文件所读取到的内容
		if (currentTag != null && blog != null) {
			currentValue = new String(ch, start, length);
			if ("id".equalsIgnoreCase(currentTag)) {
				blog.setId(currentValue);
			}else if ("title".equalsIgnoreCase(currentTag)) {
				blog.setTitle(currentValue);
			}else if ("summary".equalsIgnoreCase(currentTag)) {
				blog.setSummary(currentValue);
			}else if ("updated".equalsIgnoreCase(currentTag)) {
				blog.setUpdated(currentValue);
			}else if ("views".equalsIgnoreCase(currentTag)) {
				blog.setViews(currentValue);
			}else if ("comments".equalsIgnoreCase(currentTag)) {
				blog.setComments(currentValue);
			}else if ("name".equalsIgnoreCase(currentTag)) {
				blog.setName(currentValue);
			}
		}
		currentTag = null;
		currentValue = null;
	}
	
	
	public void endDocument() throws SAXException {
		super.endDocument();
	}

	
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);
		//遇到结束标记的时候调用这个方法
		if (qName.equals(nodeName)) {
			list.add(blog);
			blog = null;
		}
	}
	
	
	
	
}
