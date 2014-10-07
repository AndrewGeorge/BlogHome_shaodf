package com.example.bloghome_shaodf.news;
/*
 * 
 * 保存新闻内容
 */
public class Contents {

	String Title ;
	String SourceName;
	String SubmitDate ;
	String Content;
	String ImageUrl;
	String PrevNews;
	String NextNews;
	String CommentCount;
	
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getSourceName() {
		return SourceName;
	}
	public void setSourceName(String sourceName) {
		SourceName = sourceName;
	}
	public String getSubmitDate() {
		return SubmitDate;
	}
	public void setSubmitDate(String submitDate) {
		SubmitDate = submitDate;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	public String getImageUrl() {
		return ImageUrl;
	}
	public void setImageUrl(String imageUrl) {
		ImageUrl = imageUrl;
	}
	public String getPrevNews() {
		return PrevNews;
	}
	public void setPrevNews(String prevNews) {
		PrevNews = prevNews;
	}
	public String getNextNews() {
		return NextNews;
	}
	public void setNextNews(String nextNews) {
		NextNews = nextNews;
	}
	public String getCommentCount() {
		return CommentCount;
	}
	public void setCommentCount(String commentCount) {
		CommentCount = commentCount;
	}
	
	
}
