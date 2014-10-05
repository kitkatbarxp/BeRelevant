/*
 * Names: Kit Tse, Khalid Almandeel
 * Date: 10/4/2014 - 10/5/2015 @ HackMIT2014
 * 
 * Some of the code in this class comes from examples on http://www.developers.android.com.
 */

package com.tsealmandeel.berelevant;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import twitter4j.GeoLocation;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.berelevant.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;

/**
 * This is the launcher activity of the application. It checks for device's 
 * Internet connectivity and location services. 
 *
 */

public class MainActivity extends FragmentActivity implements
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener{
    // Global constants
    /*
     * Define a request code to send to Google Play services
     * This code is returned in Activity.onActivityResult
     */
    private final static int
            CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    
    private LocationClient mLocationClient;
    private Location mLocation;
    
    private String currentCityAndState;
    private String currentCity;
    private GeoLocation geoloc;
    
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_main);
    	
    	mLocationClient = new LocationClient(this, this, this); 
    	
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.main, menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	// action with ID action_refresh was selected
    	case R.id.action_refresh:
    		Toast.makeText(this, "Refresh selected", Toast.LENGTH_SHORT)
            	.show();
    		break;
    	// action with ID action_settings was selected
    	case R.id.action_settings:
    		Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT)
            	.show();
    		break;
    	default:
    		break;
    	}

    	return true;
    } 

    /*
     * Called when the Activity becomes visible.
     */
    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        mLocationClient.connect();
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	
    }
    
    /*
     * Called when the Activity is no longer visible.
     */
    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        mLocationClient.disconnect();
        super.onStop();
    }
    
    @Override 
    public void onDestroy() {
        super.onDestroy();

    }

    /*
     * Handle results returned to the FragmentActivity
     * by Google Play services
     */
    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        // Decide what to do based on the original request code
        switch (requestCode) {
            
            case CONNECTION_FAILURE_RESOLUTION_REQUEST :
            /*
             * If the result code is Activity.RESULT_OK, try
             * to connect again
             */
                switch (resultCode) {
                    case Activity.RESULT_OK :
                    /*
                     * Try the request again
                     */
                    
                    break;
                }
            
        }
     }

    /*
     * Called by Location Services when the request to connect the
     * client finishes successfully. At this point, you can
     * request the current location or start periodic updates
     */
    @Override
    public void onConnected(Bundle dataBundle) {
             
	    mLocation = mLocationClient.getLastLocation();
	       
	    AsyncTask<Location, Void, String> getAddress = new GetAddressTask(this);
	    getAddress.execute(mLocation);    

    }

    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onDisconnected() {
        // Display the connection status
        Toast.makeText(this, "Disconnected. Please re-connect.",
                Toast.LENGTH_SHORT).show();
    }

    /*
     * Called by Location Services if the attempt to
     * Location Services fails.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
        	FragmentManager fm = getSupportFragmentManager();
			ErrorDialog newFragment = new ErrorDialog();
			Bundle args = new Bundle();
			args.putString(ErrorDialog.ERROR, "location");
			newFragment.setArguments(args);
			newFragment.show(fm, "error dialog");
        }
    }


	/**
     * A subclass of AsyncTask that calls getFromLocation() in the
     * background. The class definition has these generic types:
     * Location - A Location object containing
     * the current location.
     * Void     - indicates that progress units are not used
     * String   - An address passed to onPostExecute()
     */
     private class GetAddressTask extends
             AsyncTask<Location, Void, String> {
         Context mContext;
         public GetAddressTask(Context context) {
             super();
             mContext = context;
         }
         
         /**
          * Get a Geocoder instance, get the latitude and longitude
          * look up the address, and return it
          *
          * @params params One or more Location objects
          * @return A string containing the address of the current
          * location, or an empty string if no address can be found,
          * or an error message
          */
         @Override
         protected String doInBackground(Location... params) {
             Geocoder geocoder =
                     new Geocoder(mContext, Locale.getDefault());
            
             // Get the current location from the input parameter list
             Location loc = params[0];
             // Create a list to contain the result address
             List<Address> addresses = null;
             
             try {
                 /*
                  * Return 1 address.
                  */
                 addresses = geocoder.getFromLocation(loc.getLatitude(),
                         loc.getLongitude(), 1);
                 
                 geoloc = new GeoLocation(loc.getLatitude(),loc.getLongitude());
                 
             } catch (IOException e1) {
	             Log.e("LocationSampleActivity",
	                     "IO Exception in getFromLocation()");
	             e1.printStackTrace();
	             return ("IO Exception trying to get address");
             } catch (IllegalArgumentException e2) {
	             // Error message to post in the log
	             String errorString = "Illegal arguments " +
	                     Double.toString(loc.getLatitude()) +
	                     " , " +
	                     Double.toString(loc.getLongitude()) +
	                     " passed to address service";
	             Log.e("LocationSampleActivity", errorString);
	             e2.printStackTrace();
	             return errorString;
             } 
             // If the reverse geocode returned an address
             if (addresses != null && addresses.size() > 0) {
                 // Get the first address
                 Address address = addresses.get(0);
                 currentCityAndState = address.getLocality() + ", " + address.getAdminArea(); 
                 currentCity = address.getLocality();
                 /*
                  * Format the first line of address (if available),
                  * city, and country name.
                  */
                 String addressText = String.format(
                         "%s, %s, %s",
                         // If there's a street address, add it
                         address.getMaxAddressLineIndex() > 0 ?
                                 address.getAddressLine(0) : "",
                         // Locality is usually a city
                         address.getLocality(),
                         // The country of the address
                         address.getCountryName());
                 
                 // Return the text
                 return addressText;
             } else {
                 return "No address found";
             }
         }
         
         /**
          * A method that's called once doInBackground() completes. Turn
          * off the indeterminate activity indicator and set
          * the text of the UI element that shows the address. If the
          * lookup failed, display the error message.
          */
         @Override
         protected void onPostExecute(String address) {
        	 
        	 setupCore();
        	 
         }
         
     }
     
     public void setupCore() {
    	 if (getSupportFragmentManager().findFragmentByTag("core") == null) {
    		 CoreFragment corefrag = new CoreFragment();
        	 getSupportFragmentManager().beginTransaction()
        	 	.add(R.id.am_framelayout, corefrag, "core").commit();
    	 }
     }
     
     public String getCurrentCity() {
    	 return currentCity;
     }
     
     public String getCurrentCityForSearch(String id) {
    	 String cityState;
    	 if (id.equals("Articles")) {
        	 cityState = currentCityAndState.replace(", ", "%20");
    	 } else {
    		 cityState = currentCityAndState.replace(",", "");
    	 }
    	 
    	 return cityState;
     }
     
     public GeoLocation getGeoLocation() {
    	 return geoloc;
     }
     
}