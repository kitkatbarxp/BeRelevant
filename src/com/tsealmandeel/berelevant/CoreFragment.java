/*
 * Names: Kit Tse, Khalid Almandeel
 * Date: 10/4/2014 - 10/5/2015 @ HackMIT2014
 * 
 * This fragment implements a ViewPager for other fragments to use in BeRelevant.
 */

package com.tsealmandeel.berelevant;

import java.io.InputStream;
import java.net.URL;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.berelevant.R;

public class CoreFragment extends Fragment {
		
	private String city;

	View rootView;
	ViewPager mViewPager;
	PagerAdapter mPagerAdapter;
	ActionBar actionBar;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		mCity toCheckout = new mCity();
		city = toCheckout.getCity();
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_core, container, false);
		mViewPager = (ViewPager) rootView.findViewById(R.id.fc_pager);
		mPagerAdapter = new ViewPagerAdapter(getFragmentManager());
		mViewPager.setAdapter(mPagerAdapter);
		
		actionBar = this.getActivity().getActionBar();

	    // Specify that tabs should be displayed in the action bar.
	    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	    
	    ActionBar.TabListener tabListener = new ActionBar.TabListener() {
			@Override
			public void onTabSelected(Tab tab,
					android.app.FragmentTransaction ft) {
				mViewPager.setCurrentItem(tab.getPosition());
			}

			@Override
			public void onTabUnselected(Tab tab,
					android.app.FragmentTransaction ft) {
				
			}

			@Override
			public void onTabReselected(Tab tab,
					android.app.FragmentTransaction ft) {
				
			}
	    };

	    // Add 2 tabs, specifying the tab's text and TabListener
	    actionBar.addTab(actionBar.newTab().setText("BORING NEWS").setTabListener(tabListener));
	    actionBar.addTab(actionBar.newTab().setText("GOSSIP GOSSIP").setTabListener(tabListener));
	    actionBar.addTab(actionBar.newTab().setText("MOVING THINGS").setTabListener(tabListener));
	    actionBar.addTab(actionBar.newTab().setText("~COMING SOON~").setTabListener(tabListener));
	    
	    mViewPager.setOnPageChangeListener(
            new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    // When swiping between pages, select the
                    // corresponding tab.
                    getActivity().getActionBar().setSelectedNavigationItem(position);
                }
        });
	    
	    mViewPager.setCurrentItem(0);
		
		return rootView;
	}
}
