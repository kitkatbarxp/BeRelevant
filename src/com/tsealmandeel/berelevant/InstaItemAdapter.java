/*
 * Names: Kit Tse, Khalid Almandeel
 * Date: 10/4/2014 - 10/5/2015 @ HackMIT2014
 * 
 * Some of the code in this class comes from examples on http://www.developers.android.com.
 */

package com.tsealmandeel.berelevant;

import java.util.ArrayList;

import org.jinstagram.entity.common.Images;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.berelevant.R;

/**
 * The TweetItemAdapter class extends BaseAdapter to make a custom adapter for ListView.
 *
 */

public class InstaItemAdapter extends BaseAdapter { 
	

	private ArrayList<Images> images;
	
	
	private static LayoutInflater inflater = null;
	
	public InstaItemAdapter(Context context, ArrayList<Images> images) {
		this.images = images;
		
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return images.size();
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
		
		ImageView image = (ImageView)vi.findViewById(R.id.in_image); 
		
		
		return vi;		
	}
	
	public void updateList(ArrayList<Images> images) {
		this.images = images;
		notifyDataSetChanged();
	}
}
