/*
 * Names: Kit Tse, Khalid Almandeel
 * Date: 10/4/2014 - 10/5/2015 @ HackMIT2014
 * 
 * A lot of the code in this class comes from examples on http://www.developers.android.com.
 */

package com.tsealmandeel.berelevant;

/**
 * This class is a fragment in the UI of BeRelevant. It searches for articles that are 
 * related to the user's current location and searches for articles that are related 
 * to the place. Then it takes RSS feed data from Google and parses the data. 
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.berelevant.R;
import com.tsealmandeel.berelevant.GoogleNewsRSSFeedParser.Item;

public class ArticlesFeedFragment extends Fragment {
	
	public static String CITY = "city";
	
	private View rootView;
	private String city;
	private String cityDisplayName;
	private String URL;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCity toCheckout = new mCity();
		city = toCheckout.getCity();
		cityDisplayName = city;
		//mCity = ((MainActivity) getActivity()).getCurrentCityForSearch("Articles");
		city = city.replaceAll(" ", "");
		URL = "https://news.google.com/news/feeds?q=" + city + "&output=rss";
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {
		
		rootView = inflater.inflate(R.layout.fragment_article_feed, container, false);
		TextView location = (TextView) rootView.findViewById(R.id.faf_location);
		location.setText("What's happening in " + cityDisplayName + "? Who really cares!?!");
		
		return rootView;
	}
	
	@Override
	public void onStart(){
		super.onStart();
		new DownloadXMLTask().execute(URL);
	}
	
    private class DownloadXMLTask extends AsyncTask<String, Void, String> {
	     @Override
	     protected String doInBackground(String... urls) {
	          
	         // params comes from the execute() call: params[0] is the url.
	         try {
	        	 return loadXmlFromNetwork(urls[0]);
	         } catch (IOException e) {
	             return "Unable to retrieve web page. URL may be invalid.\n Here's the URL: " + URL;
	         } catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "XML error";
			}
	     }
	     // onPostExecute displays the results of the AsyncTask.
	     @Override
        protected void onPostExecute(String result) {
	    	 
            WebView myWebView = (WebView) rootView.findViewById(R.id.faf_webview);
            myWebView.loadData(result, "text/html", null);
        }
    }
    
	  // Uploads XML from news.google.com.
	  private String loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
	      InputStream stream = null;
	      // Instantiate the parser
	      GoogleNewsRSSFeedParser googleNewsRSSFeedParser = new GoogleNewsRSSFeedParser();
	      List<Item> items = null;
	          
	      StringBuilder htmlString = new StringBuilder();
	      StringBuilder allHtmlString = new StringBuilder();
	      
	      try {
	          stream = downloadUrl(urlString);        
	          items = googleNewsRSSFeedParser.parse(stream);
	      // Makes sure that the InputStream is closed after the app is
	      // finished using it.
	      } finally {
	          if (stream != null) {
	              stream.close();
	          } 
	       }
	      
	      // GoogleNewsRSSFeedParser returns a List (called "items") of Item objects.
	      // Each Item object represents a single post in the XML feed.
	      // This section processes the items list to combine each item with HTML markup.
	      // Each entry is displayed in the UI as a link.
	      
	      int count = 0;
	      
	      for (Item item : items) {       
	          allHtmlString.append("<p><a href='");
	          allHtmlString.append(item.link);
	          allHtmlString.append("'>" + item.title + "</a></p>");
	          if (count < 10) {
	        	  htmlString.append("<p><a href='");
		          htmlString.append(item.link);
		          htmlString.append("'>" + item.title + "</a></p>");
	          }
	          
	          count++;
	      }
	      
	      return htmlString.toString();
	  }
	
	  // Given a string representation of a URL, sets up a connection and gets
	  // an input stream.
	  private InputStream downloadUrl(String urlString) throws IOException {
	      URL url = new URL(urlString);
	      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	      conn.setReadTimeout(10000 /* milliseconds */);
	      conn.setConnectTimeout(15000 /* milliseconds */);
	      conn.setRequestMethod("GET");
	      conn.setDoInput(true);
	      // Starts the query
	      conn.connect();
	      return conn.getInputStream();
	  }
    
    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");        
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }
}
