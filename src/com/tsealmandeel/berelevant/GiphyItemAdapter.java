/*
 * Names: Kit Tse, Khalid Almandeel
 * Date: 10/4/2014 - 10/5/2015 @ HackMIT2014
 * 
 * Some of the code in this class comes from examples on http://www.developers.android.com.
 */

package com.tsealmandeel.berelevant;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.berelevant.R;
import com.koushikdutta.ion.Ion;

/**
 * The TweetItemAdapter class extends BaseAdapter to make a custom adapter for ListView.
 *
 */

public class GiphyItemAdapter extends BaseAdapter { 
	

	private ArrayList<String> url;
	
	private static LayoutInflater inflater = null;
	
	public GiphyItemAdapter(Context context, ArrayList<String> url) {
		this.url = url;
		
		
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return url.size();
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
		if (vi == null) vi = inflater.inflate(R.layout.giphy_item, null);
		
		ImageView gif = (ImageView)vi.findViewById(R.id.gi_url); 
		Ion.with(gif).load(this.url.get(position));
		
		return vi;		
	}
	
	public void updateList(ArrayList<String> url) {
		this.url = url;
		notifyDataSetChanged();
	}
}
