package com.tsealmandeel.berelevant;

import java.util.ArrayList;
import java.util.List;

import com.example.berelevant.R;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TwitterFeedFragment extends Fragment{
	
	// Constants
    /**
     * Register your here app https://dev.twitter.com/apps/new and get your
     * consumer key and secret
     * */
    static String TWITTER_CONSUMER_KEY = "P4n9DdEUJz7IkcJFgJ9f72r2s";
    static String TWITTER_CONSUMER_SECRET = "JlHRIxEfbUpqbvSVtJIrU11svu7eOH7BthtZKDB2zXFG0uFxrA";
 
    // Preference Constants
    static String PREFERENCE_NAME = "twitter_oauth";
    static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
    static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
    static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";
 
    static final String TWITTER_CALLBACK_URL = "oauth://t4jsample";
 
    // Twitter oauth urls
    static final String URL_TWITTER_AUTH = "auth_url";
    static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
    
    // Twitter
    private static Twitter twitter;
    private boolean TwitterConnected;
     
    // Internet Connection detector
    private ConnectionDetector cd;
    
    String city = "";
    
    private ListView mList;
    private TweetItemAdapter mAdapter;
    
    private ArrayList<String> usernames = new ArrayList<String>();
	private ArrayList<String> content = new ArrayList<String>();
	
	View rootView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mCity checkOut = new mCity();
		city = checkOut.getCity();
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
		
		cd = new ConnectionDetector(this.getActivity().getApplicationContext());
		 
        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
        	Toast.makeText(this.getActivity(), "InternetConnection Error", Toast.LENGTH_LONG).show();
//            alert.showAlertDialog(MainActivity.this, "Internet Connection Error",
//                    "Please connect to working Internet connection", false);
            // stop executing code by return
            return;
        }
         
        // Check if twitter keys are set
        if(TWITTER_CONSUMER_KEY.trim().length() == 0 || TWITTER_CONSUMER_SECRET.trim().length() == 0){
            // Internet Connection is not present
        	Toast.makeText(this.getActivity(), "Twitter oAuth tokens error. Set it!", Toast.LENGTH_LONG).show();
            //alert.showAlertDialog(MainActivity.this, "Twitter oAuth tokens", "Please set your twitter oauth tokens first!", false);
            // stop executing code by return
            return;
        }
        
        new ConnectToTwitterTask().execute();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {
		
		
		city.replaceAll("\\s+", "");
		rootView = inflater.inflate(R.layout.fragment_twitter_feed, container, false);
		TextView location = (TextView) rootView.findViewById(R.id.ftf_location);
		location.setText("I knew you were a nosy one! Here's " + city + "'s twitter feed!");
		
		return rootView;
	}
	
	public void searchForTweets() {
		if (TwitterConnected) {
			
			new SearchForTweets().execute();

		    mAdapter = new TweetItemAdapter(this.getActivity(),usernames,content);
		    mList = (ListView) rootView.findViewById(R.id.ftf_tweetlist);
		    mList.setAdapter(mAdapter);
		}
		
	}	
	
	private class ConnectToTwitterTask extends AsyncTask<Void, Void, Void> {
	    @Override
	    protected Void doInBackground(Void... none) {
	         
	    	 ConfigurationBuilder cb = new ConfigurationBuilder();
	         cb.setApplicationOnlyAuthEnabled(true);

	         twitter = new TwitterFactory(cb.build()).getInstance();
	         twitter.setOAuthConsumer(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);
	         try {
	             twitter.getOAuth2Token();
	             TwitterConnected = true;
	         } catch (TwitterException e) {
	             e.printStackTrace();
	         }
	         return null;
	    }
	    
	    @Override
	    protected void onPostExecute(Void none) {
	    	searchForTweets();
	    }
    }
	
	private class SearchForTweets extends AsyncTask<Void, Void, ArrayList<TweetItem>> {

		@Override
		protected ArrayList<TweetItem> doInBackground(Void... params) {
			try {
				ArrayList<TweetItem> currentItems = new ArrayList<TweetItem>();
				
	            Query query = new Query(city);
	            query.count(15);
	            QueryResult result;
	            int count = 0;
	            do {
	                result = twitter.search(query);
	                List<twitter4j.Status> tweets = result.getTweets();
	                
	                for (twitter4j.Status tweet : tweets) {
	               
	                    currentItems.add(new TweetItem("@" + tweet.getUser().getScreenName(),
	                    		tweet.getText()));
	                    //usernames.add("@" + tweet.getUser().getScreenName());
	                    //content.add(tweet.getText());
	                    count++;
	                }
	            } while (((query = result.nextQuery()) != null) && count < 2);
	            
	            return currentItems;
	            
	        } catch (TwitterException te) {
	            te.printStackTrace();
	            System.out.println("Failed to search tweets: " + te.getMessage());
	            usernames.add("uh-oh. For some reason, the app has failed to search tweets.");
	            return null;
	        }
		}
		
		@Override
		protected void onPostExecute(ArrayList<TweetItem> list) {
			usernames.clear();
			content.clear();
			for (TweetItem item : list) {
				usernames.add(item.getUsername());
				content.add(item.getTweet());
			}
			mAdapter.notifyDataSetChanged();
			
		}
		
	}
	
}
