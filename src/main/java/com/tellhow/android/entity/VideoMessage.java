package com.tellhow.android.entity;

import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("xml")
public class VideoMessage extends BaseMessage{
	private String mediaId;
	
	public VideoMessage(Map<String, String> map) {
		super(map);
		this.setMsgType("video");
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	
}
