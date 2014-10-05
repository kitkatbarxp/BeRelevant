package com.example.berelevant;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CoreFragment extends Fragment {
		
	private String mCity;

	View rootView;
	ViewPager mViewPager;
	PagerAdapter mPagerAdapter;
	ActionBar actionBar;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		mCity = ((MainActivity) getActivity()).getCurrentCity();
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
	    actionBar.addTab(actionBar.newTab().setText("ARTICLES").setTabListener(tabListener));
	    actionBar.addTab(actionBar.newTab().setText("TWITTER FEED").setTabListener(tabListener));
	    
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
