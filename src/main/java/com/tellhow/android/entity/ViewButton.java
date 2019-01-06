package com.tellhow.android.entity;

public class ViewButton extends AbstractButton{
	private String url;
	private String type = "view";
	public ViewButton(String name,String url) {
		super(name);
		this.setUrl(url);
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
