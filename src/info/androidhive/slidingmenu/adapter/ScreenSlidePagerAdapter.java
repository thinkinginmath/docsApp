package info.androidhive.slidingmenu.adapter;

import info.androidhive.slidingmenu.ScreenSlidePageFragment;
import info.androidhive.slidingmenu.WebViewFragment;

import java.util.ArrayList;
import java.util.List;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;


/**
 * A simple pager adapter that represents 5 {@link ScreenSlidePageFragment} objects, in
 * sequence.
 */
public class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
	 private List<Fragment> fragments;

     private List<String> titles;
     private WebViewFragment searchPage;
     String docId;
     //private boolean firstTime;
     public ScreenSlidePagerAdapter(android.support.v4.app.FragmentManager fm, String docId) {
        super(fm);
        //firstTime = true;
        this.fragments = new ArrayList<Fragment>();

        this.titles    = new ArrayList<String>();
        searchPage = new WebViewFragment();
	searchPage.init("http://13.141.43.227/docs/" + docId, docId);

        //firstTime = false;
        //Log.e("ERROR", "***myFragment docID Search " +docId);
        this.docId = docId;
    }
    public void addItem(String url, String title) {
        
        WebViewFragment myFragment = new WebViewFragment();
        Log.e("ERROR", "***myFragment " +myFragment);
        //Bundle args = new Bundle();

        myFragment.init(url, docId);
        
        //args.putString("url", url);

        //myFragment.setArguments(args);

        synchronized (this) {
	    this.fragments.add(myFragment);
            this.titles.add(title);
        }
        this.notifyDataSetChanged();
    }
    
    public void searhDoc(String docId) {
       this.fragments.clear(); 
       this.titles.clear();
       this.notifyDataSetChanged();
       searchPage.updateUrl("http://13.141.43.227/docs/" + docId);
    }


    @Override
    public Fragment getItem(int position) {
    	if (position == 0) {
    		return searchPage;
    	}
    			
    	return this.fragments.get(position-1);

    }

    @Override
    public int getCount() {
    	return this.fragments.size() + 1;
    }
}


