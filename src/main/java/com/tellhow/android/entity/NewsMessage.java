package com.tellhow.android.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("xml")
public class NewsMessage extends BaseMessage{
	
	@XStreamAlias("ArticleCount")
	private String articleCount;
	
	private List<Article> Articles = new ArrayList<>();
	
	public NewsMessage(Map<String, String> map,List<Article> articles) {
		super(map);
		this.setArticleCount(String.valueOf(articles.size()));
		this.setArticles(articles);
		this.setMsgType("news");
	}
	
	public String getArticleCount() {
		return articleCount;
	}
	public void setArticleCount(String articleCount) {
		this.articleCount = articleCount;
	}
	public List<Article> getArticles() {
		return Articles;
	}
	public void setArticles(List<Article> articles) {
		Articles = articles;
	}
	
}
