package com.tellhow.android.entity;

public class AccessToken {
	private String accessToken;
	private long expires;
	
	public AccessToken(String accessToken,String expireIn) {
		this.accessToken = accessToken;
		this.expires = System.currentTimeMillis() + Long.parseLong(expireIn) * 1000L;
	}
	
	public boolean isExpires() {
		return System.currentTimeMillis() > expires;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public long getExpires() {
		return expires;
	}

	public void setExpires(long expires) {
		this.expires = expires;
	}
}
