package com.tellhow.android.entity;

import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("xml")
public class TextMessage extends BaseMessage{
	
	public TextMessage(Map<String, String> map,String content) {
		super(map);
		this.setMsgType("text");
		this.setContent(content);
	}

	@XStreamAlias("Content")
	private String content;
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
