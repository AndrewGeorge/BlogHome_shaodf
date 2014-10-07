package com.example.bloghome_shaodf.blog;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class BlogSAX extends DefaultHandler{

	private BlogInfo blog = null;//�洢������������������
	private List<BlogInfo> list = null;//�洢���н�������
	private String currentTag = null;//���ڽ���Ԫ�صı�ǩ
	private String currentValue = null;//��ʾ��ǰ����Ԫ�ص�ֵ
	private String nodeName = null;//��ʾ��ǰ�ڵ������
	
	public BlogSAX(String nodeName) {
		this.nodeName = nodeName;
	}
	
	public List<BlogInfo> getList(){
		return list;
	}

	public void startDocument() throws SAXException {
		super.startDocument();
		//��������һ����ǩ��ʱ��ᴥ������¼�
		list = new ArrayList<BlogInfo>();
	}
	
	/**
	 * 
	 */
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		//�������ĵ���ͷ��ʱ�����
		if (qName.equals(nodeName)) {
			blog = new BlogInfo();
		}
		if (attributes != null && blog != null) {
			//�˷��������ж���ǩ���е�����
		}
		currentTag = qName;
	}
	
	
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);
		//�����������������xml�ļ�����ȡ��������
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
		//����������ǵ�ʱ������������
		if (qName.equals(nodeName)) {
			list.add(blog);
			blog = null;
		}
	}
	
	
	
	
}
