package com.example.bloghome_shaodf.blog;

public class BlogInfo {

	private String id;
	private String name;  //博主name
	private String summary;//博客简化内容
	private String views;  //浏览
	private String comments; //评论
	private String title; //标题
	private String updated;//时间日期
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getViews() {
		return views;
	}
	public void setViews(String views) {
		this.views = views;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUpdated() {
		return updated;
	}
	public void setUpdated(String updated) {
		this.updated = updated;
	}
	
	
	
	
	private String diggs;
	private String avatar;//博主头像
	private String uri; //博主首页uri
	private String link;//博主某个博客
}
