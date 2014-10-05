/*
 * Names: Kit Tse, Khalid Almandeel
 * Date: 10/4/2014 - 10/5/2015 @ HackMIT2014
 * 
 * Some of the code in this class comes from examples on http://www.developers.android.com.
 */

package com.tsealmandeel.berelevant;

import java.util.ArrayList;

import com.example.berelevant.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * The TweetItemAdapter class extends BaseAdapter to make a custom adapter for ListView.
 *
 */

public class TweetItemAdapter extends BaseAdapter { 
	

	private ArrayList<String> usernames;
	private ArrayList<String> tweetContent;
	
	private static LayoutInflater inflater = null;
	
	public TweetItemAdapter(Context context, ArrayList<String> usernames, ArrayList<String> tweetContent) {
		this.usernames = usernames;
		this.tweetContent = tweetContent;
		
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return usernames.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@SuppressLint("InflateParams")
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (vi == null) vi = inflater.inflate(R.layout.tweet_items, null);
		
		TextView userName = (TextView)vi.findViewById(R.id.ti_username); 
		userName.setText(usernames.get(position));
		
		TextView content = (TextView)vi.findViewById(R.id.ti_tweet);
		content.setText(tweetContent.get(position));
		
		return vi;		
	}
	
	public void updateList(ArrayList<String> usernames, ArrayList<String> content) {
		this.usernames = usernames;
		this.tweetContent = content;
		notifyDataSetChanged();
	}
}
