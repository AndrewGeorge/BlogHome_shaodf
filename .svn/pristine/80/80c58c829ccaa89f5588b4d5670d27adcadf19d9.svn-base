package com.example.bloghome_shaodf.blog;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class SaxHtml extends DefaultHandler {
	StringBuffer buffer;
	boolean flag = false;
	String b=null;

	public StringBuffer getString(InputStream inputstream) {
		SAXParserFactory factory;
		SAXParser parser;
		try {
			factory = SAXParserFactory.newInstance();
			parser = factory.newSAXParser();
			parser.parse(inputstream, this);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return buffer;
	}

@Override
public void setDocumentLocator(Locator locator) {
	super.setDocumentLocator(locator);
	buffer=new StringBuffer();
}
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if (qName.equals("string")) {
			flag = true;
			b = qName;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);
		System.out.println("localName-----------------"+localName);
		System.out.println("qName-----------------"+qName);
		if(localName.equals("b")){
			flag=false;
			b=null;
		}
	}

	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
		if (flag) {
			flag = false;
			b=null;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);
		if ("string".equals(b)) {
			buffer.append(new String(ch, start, length));
//			Log.i("buffer", "buffer=================="+buffer);
		}
	}
}
