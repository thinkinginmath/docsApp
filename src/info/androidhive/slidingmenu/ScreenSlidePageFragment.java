package info.androidhive.slidingmenu;


import java.util.ArrayList;

import info.androidhive.slidingmenu.adapter.NavDrawerListAdapter;
import info.androidhive.slidingmenu.adapter.ScreenSlidePagerAdapter;
import info.androidhive.slidingmenu.model.NavDrawerItem;
import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

/**
 * A fragment representing a single step in a wizard. The fragment shows a dummy title indicating
 * the page number, along with some dummy text.
 *
 * <p>This class is used by the {@link CardFlipActivity} and {@link
 * ScreenSlideActivity} samples.</p>
 */
public class ScreenSlidePageFragment extends Fragment {
    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_PAGE = "page";

    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private int mPageNumber;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static ScreenSlidePageFragment create(int pageNumber) {
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ScreenSlidePageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
    	Log.e("ERROR", "*****ScreenSlide");
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_screen_slide_page, container, false);

        // Set the title view to show the page number.
        ((TextView) rootView.findViewById(android.R.id.text1)).setText(
                getString(R.string.title_template_step, mPageNumber + 1));
        
        setupSearchView(rootView);

        return rootView;
    }
    
    private void setupSearchView(ViewGroup rootView) {
    	 // Retrieves the system search manager service
        final SearchManager searchManager =
                (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        // Retrieves the SearchView from the search menu item
        final SearchView searchView = (SearchView) rootView.findViewById(R.id.searchView);
        Log.e("ERROR", "***searchView is " + searchView);

        // Assign searchable info to SearchView
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getActivity().getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //@Override
            public boolean onQueryTextSubmit(String queryText) {
                // Nothing needs to happen when the user submits the search string
                return true;
            }

            //@Override
            public boolean onQueryTextChange(String newText) {
                //
            	return true;
            }
        });
 
    
    }

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }
}
