package com.tellhow.android.entity;

import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("xml")
public class VoiceMessage extends BaseMessage{
	private String mediaId;
	
	public VoiceMessage(Map<String, String> map) {
		super(map);
		this.setMsgType("voice");
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	
}
