/*
 * Names: Kit Tse, Khalid Almandeel
 * Date: 10/4/2014 - 10/5/2015 @ HackMIT2014
 * 
 * This code came straightly from http://www.androidhive.info/2012/09/android-twitter-oauth-connect-tutorial/.
 */

package com.tsealmandeel.berelevant;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * This checks for Internet connection of the host device. 
 */

public class ConnectionDetector {
  
    private Context _context;
  
    public ConnectionDetector(Context context){
        this._context = context;
    }
  
    /**
     * Checking for all possible internet providers
     * **/
    public boolean isConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
          if (connectivity != null)
          {
              NetworkInfo[] info = connectivity.getAllNetworkInfo();
              if (info != null)
                  for (int i = 0; i < info.length; i++)
                      if (info[i].getState() == NetworkInfo.State.CONNECTED)
                      {
                          return true;
                      }
  
          }
          return false;
    }
}