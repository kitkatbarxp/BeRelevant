package com.example.berelevant;

public class TweetItem{
	
	private String username;
	private String tweet;
	
	public TweetItem(String username, String tweet) {
		this.username = username;
		this.tweet = tweet;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getTweet() {
		return this.tweet;
	}

}
