package com.example.berelevant;

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

import com.example.berelevant.GoogleNewsRSSFeedParser.Item;

public class ArticlesFeedFragment extends Fragment {
	
	public static String CITY = "city";
	
	private View rootView;
	private String mCity;
	private String cityDisplayName;
	private String URL;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mCity = ((MainActivity) getActivity()).getCurrentCityForSearch("Articles");
		cityDisplayName = ((MainActivity) getActivity()).getCurrentCity();
		URL = "https://news.google.com/news/feeds?q=" + mCity + "&output=rss";
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {
		
		rootView = inflater.inflate(R.layout.fragment_article_feed, container, false);
		TextView location = (TextView) rootView.findViewById(R.id.faf_location);
		location.setText("What's happening at " + cityDisplayName + "?");
		
		return rootView;
	}
	
	@Override
	public void onStart(){
		super.onStart();
		
		new DownloadWebpageTask().execute(URL);
	}
	
	// Uses AsyncTask to create a task away from the main UI thread. This task takes a 
    // URL string and uses it to create an HttpUrlConnection. Once the connection
    // has been established, the AsyncTask downloads the contents of the webpage as
    // an InputStream. Finally, the InputStream is converted into a string, which is
    // displayed in the UI by the AsyncTask's onPostExecute method.
    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
	     @Override
	     protected String doInBackground(String... urls) {
	          
	         // params comes from the execute() call: params[0] is the url.
	         try {
	        	 return loadXmlFromNetwork(urls[0]);
	         } catch (IOException e) {
	             return "Unable to retrieve web page. URL may be invalid.";
	         } catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "XML error";
			}
	     }
	     // onPostExecute displays the results of the AsyncTask.
	     @Override
        protected void onPostExecute(String result) {
            //setContentView(R.layout.activity_main);
            // Displays the HTML string in the UI via a WebView
	    	 
            WebView myWebView = (WebView) rootView.findViewById(R.id.faf_webview);
            myWebView.loadData(result, "text/html", null);
        }
    }
    
	  // Uploads XML from stackoverflow.com, parses it, and combines it with
	  // HTML markup. Returns HTML string.
	  private String loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
	      InputStream stream = null;
	      // Instantiate the parser
	      GoogleNewsRSSFeedParser googleNewsRSSFeedParser = new GoogleNewsRSSFeedParser();
	      List<Item> items = null;
//	      String title = null;
//	      String pubdate = null;
//	      String url = null;
//	      String description = null;
//	      Calendar rightNow = Calendar.getInstance(); 
//	      SimpleDateFormat formatter = new SimpleDateFormat("MMM dd h:mmaa");
	          
//	      // Checks whether the user set the preference to include summary text
//	      SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
//	      boolean pref = sharedPrefs.getBoolean("summaryPref", false);
	          
	      StringBuilder htmlString = new StringBuilder();
	      StringBuilder allHtmlString = new StringBuilder();
	      //htmlString.append("<h3>" + getResources().getString(R.string.page_title) + "</h3>");
	     // htmlString.append("<em>" + getResources().getString(R.string.updated) + " " + 
	       //       formatter.format(rightNow.getTime()) + "</em>");
	          
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
	      
	      // GoogleNewsRSSFeedParser returns a List (called "entries") of Entry objects.
	      // Each Entry object represents a single post in the XML feed.
	      // This section processes the entries list to combine each entry with HTML markup.
	      // Each entry is displayed in the UI as a link that optionally includes
	      // a text summary.
	      
	      int count = 0;
	      
	      for (Item item : items) {       
	          allHtmlString.append("<p><a href='");
	          allHtmlString.append(item.link);
	          allHtmlString.append("'>" + item.title + "</a></p>");
	          // If the user set the preference to include summary text,
	          // adds it to the display.
	          //if (pref) {
	              //htmlString.append(item.description);
	          //}
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
