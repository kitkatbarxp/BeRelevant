/*
 * Names: Kit Tse, Khalid Almandeel
 * Date: 10/4/2014 - 10/5/2015 @ HackMIT2014
 */

package com.tsealmandeel.berelevant;

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
