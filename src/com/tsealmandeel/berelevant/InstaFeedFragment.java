package com.tsealmandeel.berelevant;

import java.util.ArrayList;
import java.util.List;

import org.jinstagram.Instagram;
import org.jinstagram.auth.InstagramAuthService;
import org.jinstagram.auth.model.Token;
import org.jinstagram.auth.oauth.InstagramService;
import org.jinstagram.entity.common.Images;
import org.jinstagram.entity.common.Location;
import org.jinstagram.entity.users.feed.MediaFeed;
import org.jinstagram.entity.users.feed.MediaFeedData;
import org.jinstagram.exceptions.InstagramException;

import twitter4j.GeoLocation;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.berelevant.R;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class InstaFeedFragment extends Fragment {
	
	
	Instagram instagram;
	Location location;
	double longitude;
	double latitude;
	private String city;
	private String displayName;
	private String URL = "https://api.instagram.com/v1/geographies/";
	private String GEO_ID = "";
	private String URL_CONT = "/media/recent?client_id=";
	private static final Token EMPTY_TOKEN = null;
	private ArrayList<Images> images = new ArrayList<Images>();
	private ListView mList;
    private InstaItemAdapter mAdapter;
    public View rootView;
    
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCity toCheckout = new mCity();
		city = toCheckout.getCity();
		InstagramService service = new InstagramAuthService()
        .apiKey("8bd97391e13a4542b7da9c511786e6aa")
        .apiSecret("2f7346954691493f848eabd4b159672e")
        .callback("https://github.com/khalidsalata")     
        .build();
		
		
		instagram = new Instagram("8bd97391e13a4542b7da9c511786e6aa");
		
		GeoLocation place = ((MainActivity) getActivity()).getGeoLocation();
		latitude = place.getLatitude();
		longitude = place.getLongitude();
		searchForInstagramPosts();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			//mCity.replaceAll("\\s+", "");
			rootView = inflater.inflate(R.layout.fragment_insta_feed, container, false);
			TextView location = (TextView) rootView.findViewById(R.id.insta_location);
			location.setText("What marvelous sights does " + city + " have for us?");
			
			return rootView;
		
	}
	
	public void searchForInstagramPosts(){
	
		new SearchForInsta().execute();
		
	    mAdapter = new InstaItemAdapter(this.getActivity(),images);
	    mList = (ListView) rootView.findViewById(R.id.insta_instalist);
	    mList.setAdapter(mAdapter);
	}
	
	
	private class SearchForInsta extends AsyncTask<Void, Void, ArrayList<InstaItem>> {

		@Override
		protected ArrayList<InstaItem> doInBackground(Void... params) {
			try {
				ArrayList<InstaItem> currentItems = new ArrayList<InstaItem>();
				
				MediaFeed mediaFeed = instagram.searchMedia(latitude, longitude);
	            int count = 0;
	            System.out.println("Made it!");
	            do {
	            	List<MediaFeedData> images = mediaFeed.getData();
	                for (MediaFeedData data : images) {
	               
	                    currentItems.add(new InstaItem(data.getImages()));
	                    count++;
	                }
	            } while (((mediaFeed.getData()) != null) && count < 1);
	            System.out.println("Made it2!");
	            return currentItems;
	            
	        } catch (InstagramException te) {
	        	System.out.println("Failed to search instaFotos: " + te.getMessage());
	        	te.printStackTrace();
	            return null;
	        }
			
		}
		protected void onPostExecute(ArrayList<InstaItem> list) {
			System.out.println("Onto the postexecute!");
			images.clear();
			for (InstaItem item : list) {
				images.add(item.getImage());
			}
			
			mAdapter.notifyDataSetChanged();
		}
	}
}
