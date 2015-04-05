package com.tsealmandeel.berelevant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.berelevant.R;
import com.koushikdutta.ion.Ion;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class GiphyFeedFragment extends Fragment {

	private ArrayList<String> urls = new ArrayList<String>();
	View rootView;
	ListView mList;
	ImageView gif1;
	ImageView gif2;
	ImageView gif3;
	ImageView gif4;
	ImageView gif5;
	private String urlToUse;
	private GiphyItemAdapter mAdapter;
	private String city;
	private String displayName;
	private String URL = "http://api.giphy.com/v1/gifs/search?q=";
	private String giphy_key = "dc6zaTOxFJmzC";
	private String URLb = "http://theheightsanimalhospital.com/clients/15389/images/playful-kitten-6683.jpg";
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCity toCheckout = new mCity();
		city = toCheckout.getCity();
		displayName = city;
		city = city.replaceAll("\\s+", " ");
		URL = URL + city + "&limit=10&api_key=" + giphy_key;
	    //URL = "http://replygif.net/i/1276.gif";
		//URL = "http://www.themonitordaily.com/wp-content/uploads/2015/03/aplle.jpg";
        //URL = "http://media0.giphy.com/media/iU1NUdMq3sx3O/200.gif";
        
		//new PrepareGIF().execute();
		}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {	
		rootView = inflater.inflate(R.layout.fragment_giphy_feed, container, false);
		TextView location = (TextView) rootView.findViewById(R.id.gf_location);
		location.setText("I honestly wish I was working on this all day - " + city);
		ImageView gif1 = (ImageView) rootView.findViewById(R.id.gf_url1);
		System.out.println(gif1);
		String urlToUse = GET(URL);
		urlToUse = urlToUse.replace("\\", "" );
		urlToUse = "http://33.media.tumblr.com/tumblr_lt0czpRJvw1r2vcn2o1_500.gif";
		System.out.println(urlToUse);
		Ion.with(gif1).load(urlToUse);
		ImageView gif2 = (ImageView) rootView.findViewById(R.id.gf_url2);
		Ion.with(gif2).load("https://s3.amazonaws.com/giphygifs/media/BHZS3OfG1lMhW/giphy.gif");
		ImageView gif3 = (ImageView) rootView.findViewById(R.id.gf_url3);
		Ion.with(gif3).load("http://i.imgur.com/DxO2BtN.gif");
		ImageView gif4 = (ImageView) rootView.findViewById(R.id.gf_url4);
		Ion.with(gif4).load("http://media.giphy.com/media/RGFL2W7ZJvoM8/giphy.gif");
		ImageView gif5 = (ImageView) rootView.findViewById(R.id.gf_url5);
		Ion.with(gif5).load("http://media2.giphy.com/media/c5PHIq9sXsV6o/giphy.gif");
		
		
		return rootView;
	}
	
	public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {
 
            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
 
            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
 
            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
 
            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";
 
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        return result;
    }

	
	
	private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
 
        inputStream.close();
        return result;
 
    }
	
	public static String parseURLs(String URLs){
		int toStartFrom = URLs.indexOf("url");
		toStartFrom = toStartFrom + 4;
		int toEndAt = URLs.indexOf(",");
		toEndAt--;
		String toReturn = URLs.substring(toStartFrom,toEndAt);
		return toReturn;
	}
	
	/*private class PrepareGIF extends AsyncTask<Void, Void, Void> {
	    @Override
	    protected Void doInBackground(Void... none) {
			
			
			return null;
	    }
	    
	    @Override
	    protected void onPostExecute(Void none) {
	    	
	    }
    }*/
	
	
}
		
		

	

