package com.tellhow.android.entity;

public class ClickButton extends AbstractButton{
	private String key;
	private String type = "click";
	public ClickButton(String name,String key) {
		super(name);
		this.setKey(key);
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
