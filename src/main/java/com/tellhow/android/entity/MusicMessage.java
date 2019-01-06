package com.tellhow.android.entity;

import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("xml")
public class MusicMessage extends BaseMessage	{
	private Music music;
	
	public MusicMessage(Map<String, String> map) {
		super(map);
		this.setMsgType("music");
	}
	
	public Music getMusic() {
		return music;
	}
	
	public void setMusic(Music music) {
		this.music = music;
	}
	
}
