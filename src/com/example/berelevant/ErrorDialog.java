package com.example.berelevant;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.view.View;

public class ErrorDialog extends DialogFragment {
	
	public static final String ERROR = "error";
	
	private View rootView;
	private String error;
	private String message;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (getArguments().containsKey(ERROR)) {
			error = getArguments().getString(ERROR);
		}
		
		if (error.equals("location")) {
			message = "Can't retrieve your location. Please make sure your Location "
					+ "Services is turned on!";
		} else {
			message = "You are currently offline. Please connect to Internet.";
		}
		
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Set the dialog title
	    builder.setTitle("ERROR!")
	    // Set message
	    	   .setMessage(message)
	    // Set the action buttons
	           .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   if (error.equals("location")) {
	            		   Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	            		   getActivity().startActivity(myIntent);
	            	   } else {
	            		   Intent myIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
	            		   getActivity().startActivity(myIntent); 
	            	   }
	               }
	           })
	           .setNegativeButton("OK", new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	                   getActivity().finish();
	               }
	           });

	    return builder.create();
	}

}
