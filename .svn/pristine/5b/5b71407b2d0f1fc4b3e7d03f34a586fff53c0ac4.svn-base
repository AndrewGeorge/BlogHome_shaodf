package com.example.bloghome_shaodf.collect;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class OffBlogInfo {
	public String blogTitle;
	public String blogUrl;
	public String bloger;
	public String blogText;
	public String blogSum;
	
	public String getBlogTitle() {
		return blogTitle;
	}
	public void setBlogTitle(String blogTitle) {
		this.blogTitle = blogTitle;
	}
	public String getBlogUrl() {
		return blogUrl;
	}
	public void setBlogUrl(String blogUrl) {
		this.blogUrl = blogUrl;
	}
	public String getBloger() {
		return bloger;
	}
	public void setBloger(String bloger) {
		this.bloger = bloger;
	}
	public String getBlogText() {
		return blogText;
	}
	public void setBlogText(String blogText) {
		this.blogText = blogText;
	}
	public String getBlogSum() {
		return blogSum;
	}
	public void setBlogSum(String blogSum) {
		this.blogSum = blogSum;
	}
	
	String updateTime;
	Long storeTime;
	int blogId;

	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public Long getStoreTime() {
		return storeTime;
	}
	public void setStoreTime(Long storeTime) {
		this.storeTime = storeTime;
	}
	public int getBlogId() {
		return blogId;
	}
	public void setBlogId(int blogId) {
		this.blogId = blogId;
	}
	public String getStoreTimeChange(){
		String resTime=null;
		Date date=new Date(storeTime);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss");
		
		resTime=sdf.format(date);
		System.out.println("毫秒数转化后的时间为："+ resTime); 
		return resTime;
	}
}
