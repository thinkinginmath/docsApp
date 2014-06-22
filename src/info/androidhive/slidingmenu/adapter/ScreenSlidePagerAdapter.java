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
    String searchUrl;
    String searchPageTitle;
     //private boolean firstTime;
    public ScreenSlidePagerAdapter(android.support.v4.app.FragmentManager fm,
				   String searchUrl,
				   String docId) {
        super(fm);
        //firstTime = true;
	this.searchUrl = searchUrl;
        this.fragments = new ArrayList<Fragment>();

        this.titles    = new ArrayList<String>();
        searchPage = new WebViewFragment();
        searchPage.init(searchUrl + docId, docId, true);

        //firstTime = false;
        //Log.e("ERROR", "***myFragment docID Search " +docId);
        this.docId = docId;
    }

    public String getTitleForPosition(int position) {
	if (position == 0) {
	    return searchPageTitle;
	}
	position = position - 1;
	if (position < this.titles.size()) {
	    return this.titles.get(position);
	}
	return "";
    }
    public void setSearchPageTitle(String title) {
	searchPage.setTitle(title);
	searchPageTitle = title;
    }
    public void addItem(String url, String title) {
        
        WebViewFragment myFragment = new WebViewFragment();
        myFragment.init(url, docId, false);
	myFragment.setTitle(title);
        Log.e("ERROR", "***myFragment " + myFragment);
        //Bundle args = new Bundle();


        
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
       searchPage.updateUrl(searchUrl + docId);
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


