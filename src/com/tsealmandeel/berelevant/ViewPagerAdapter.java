package com.tsealmandeel.berelevant;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

	public ViewPagerAdapter(FragmentManager fm) {
		super(fm);
	}
	
	@Override
    public Fragment getItem(int index) {
 
        switch (index) {
        case 0:
            //return UseNewSettingsFragment.newInstance();
        	return new ArticlesFeedFragment();
        case 1:
            return new TwitterFeedFragment();
        }
 
        return null;
    }
	
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 2;
    }

}
