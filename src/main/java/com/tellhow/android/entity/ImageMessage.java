package com.tellhow.android.entity;

import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("xml")
public class ImageMessage extends BaseMessage{
	private String mediaId;

	public ImageMessage(Map<String, String> map) {
		super(map);
		this.setMsgType("image");
	}
	
	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	
}
