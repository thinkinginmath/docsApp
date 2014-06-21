package info.androidhive.slidingmenu;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import info.androidhive.slidingmenu.Parser.JSONParser;
import info.androidhive.slidingmenu.adapter.NavDrawerListAdapter;
import info.androidhive.slidingmenu.adapter.ScreenSlidePagerAdapter;
import info.androidhive.slidingmenu.model.NavDrawerItem;
import info.androidhive.slidingmenu.model.NavMenuItem;
import info.androidhive.slidingmenu.model.NavMenuSection;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

/**
 * Demonstrates a "screen-slide" animation using a {@link ViewPager}. Because {@link ViewPager}
 * automatically plays such an animation when calling {@link ViewPager#setCurrentItem(int)}, there
 * isn't any animation-specific code in this sample.
 *
 * <p>This sample shows a "next" button that advances the user to the next step in a wizard,
 * animating the current screen out (to the left) and the next screen in (from the right). The
 * reverse animation is played when the user presses the "previous" button.</p>
 *
 * @see ScreenSlidePageFragment
 */
public class ScreenSlideActivity extends FragmentActivity {
	
    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private ScreenSlidePagerAdapter mPagerAdapter;
    
    // for drawer
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    
    // nav drawer title
    private CharSequence mDrawerTitle;
    
    // used to store app title
    private CharSequence mTitle;
    
    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    
    private ArrayList<HashMap<String, String>> receiptslist = new ArrayList<HashMap<String, String>>();
    private NavDrawerListAdapter adapter;
    
    float x1, x2;
    float y1, y2;
	//end of for drawer

    public String searchID = null;
	
	
    private static final String FILE_NAME = "FN";
    private static final String FILE_TYPE = "T";
    
    private FragmentActivity currentActivity = this;
    private SearchView searchView;

    private boolean jsonLoaded = false;
    
    private final String DEFAULT_SERVER = "http://13.141.43.227";
    
    private String serverURL = DEFAULT_SERVER;
    
    SharedPreferences preferences = null;
    
    private String loginUrl;
    private String logoutUrl;
    private String homePageUrl;
    private String server;

    private void setServer(String server, boolean write) {
	this.server = server;
	String urlBase = "http://" + server;
	loginUrl = urlBase + "/m/login.html";
	logoutUrl = urlBase + "/m/logout";
	homePageUrl = urlBase + "/pad/pad.html";

	if (write) {
	    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
	    SharedPreferences.Editor editor = preferences.edit();
	    editor.putString("dms.server", server);
	    editor.commit();
	}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      	Log.e("test", "onCreate");
        
        setContentView(R.layout.activity_screen_slide);

        createDrawer();
        retrieveServerURL();
	processSearchDocId();

        //mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
    }
    
    public void loadDocumentJson(String docId) {
    	if (!jsonLoaded) {
	    jsonLoaded = true;
	    new ProgressTask(ScreenSlideActivity.this, docId).execute();
	      // createDrawer();
    	}
    }

    public void cleanup() {
        jsonLoaded = false;
        if (adapter != null){
           adapter.clearPageMenuItems();
        }
    }

    private void retrieveServerURL() {
    	Log.e("test", "retriveServerURL");
    	preferences = PreferenceManager.getDefaultSharedPreferences(currentActivity);
    	server = preferences.getString("dms.server", DEFAULT_SERVER); 
    	searchID = preferences.getString("dms.docId", "test");
      	Log.e("test", "retriveServerURL id" + searchID);
        
    }
    
    private void createDrawer()
    {
	mTitle = mDrawerTitle = getTitle();
	mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	adapter = new NavDrawerListAdapter(getApplicationContext(), R.layout.drawer_list_item);
	    		                 
	adapter.addSysMenu(NavMenuSection.create(100001,
						 getString(R.string.menu_section_action)));

	adapter.addSysMenu(NavMenuItem.create(100002,
					      "  "+getString(R.string.menu_item_homepage), "action", true,
					      this));
	adapter.addSysMenu(NavMenuItem.create(100003,
					      "  "+getString(R.string.menu_item_setting), "action", true,
					      this));
	    
	adapter.addSysMenu(NavMenuItem.create(100004,
					      "  "+getString(R.string.menu_item_logout), "action", true,
					      this));

	mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
	mDrawerList.setAdapter(adapter.getArrayAdpater());	    
	mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

	getActionBar().setDisplayHomeAsUpEnabled(true);

	mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
						  R.drawable.ic_drawer, //nav menu toggle icon
						  R.string.app_name, // nav drawer open - description for accessibility
						  R.string.app_name // nav drawer close - description for accessibility
						  ) {
		public void onDrawerClosed(View view) {
		    getActionBar().setTitle(mTitle);
		    // calling onPrepareOptionsMenu() to show action bar icons
		    invalidateOptionsMenu();
		}

		public void onDrawerOpened(View drawerView) {
		    getActionBar().setTitle(mDrawerTitle);
		    // calling onPrepareOptionsMenu() to hide action bar icons
		    invalidateOptionsMenu();
		}
	    };
	mDrawerLayout.setDrawerListener(mDrawerToggle);
    }
    
    private class ProgressTask extends AsyncTask<String, Void, Boolean> {
    	private ProgressDialog dialog;

    	private FragmentActivity activity;
    	
    	private String query;

    	// private List<Message> messages;
    	public ProgressTask(FragmentActivity activity, String query) {
    		this.activity = activity;
    		context = activity;
    		dialog = new ProgressDialog(context);
    		this.query = query;
    	}

    	private Context context;

    	protected void onPreExecute() {
    		this.dialog.setMessage("Progress start");
    		//this.dialog.show();
    	}

    	@Override
    	protected void onPostExecute(final Boolean success) {
	    if (dialog.isShowing()) {
		dialog.dismiss();
	    }
	    updateDrawerMenu();						 
    	}
    	
    	private JSONArray json;
    	private String docId;

    	private void updateDrawerMenu() {
	    adapter.addPageMenuItem(NavMenuSection.create(100,
							  getString(R.string.menu_section_receipts)));

	    for (int i = 0; i < json.length(); i++) {
		try {
		    JSONObject c = json.getJSONObject(i);
		    int page = i + 1;
		    String fileType = c.getString(FILE_TYPE);
		    
		    String title = getString(R.string.menu_item_title1) + page
			+ getString(R.string.menu_item_title2)
			+ " (" + fileType + ")";
                     
		    mPagerAdapter.addItem(serverURL+"/docimages/" + docId + "/" + docId + "/" + c.getString("FN"),
					  title);
                     
		    String fileName = c.getString(FILE_NAME);
		    Log.e("ERROR", "****fileName " + fileName);
		    String fileID = fileName.substring(3,
						       fileName.indexOf("."));
		    Log.e("ERROR", "****fileID " + fileID);

		    adapter.addPageMenuItem(NavMenuItem.create(i,"  "+
							       title, fileName,
							       true, this.context));

		} catch (JSONException e) {
		    e.printStackTrace();
		}
	    }
    	}

	@Override
    	protected Boolean doInBackground(final String... args) {
	    if (query != null) {
		//
		docId = query;
		//mPagerAdapter.addItem("http://api.uubright.com/2225/pic000000.jpg",
		//	"WebView 1");
		JSONParser jParser = new JSONParser();
		//JSONArray json = null;
		
		// get JSON data from URL
		try {
		    JSONObject jsonObj = jParser.getJSONFromUrl("http://13.141.43.227/docimages/" + docId + "/"
								+ docId + "/" + docId + ".json");
		    json = jsonObj.getJSONArray("T_blog");
					
		    //JSONObject obj = new JSONObject(loadJSONFromAsset());
		    Log.e("ERROR", "****obj ");
		    //json = obj.getJSONArray("T_blog");
		} catch (Exception ex) {
		}

		Log.e("ERROR", "****length " + json.length());
		
	    }
	    // this.updateDrawerMenu();
	    return null;
    	}
    }
    
    void processSearchDocId() {
    if (searchID != null) {
	      //new ProgressTask(ScreenSlideActivity.this, query).execute();
	      mPager = (ViewPager) findViewById(R.id.pager);
	      Log.e("ScreenSlideActivity", "**** mPager is " + mPager);
	      if (mPagerAdapter == null) {
		  mPagerAdapter = new ScreenSlidePagerAdapter(
							      getSupportFragmentManager(), searchID);
		  mPager.setAdapter(mPagerAdapter);
		  mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			  @Override
			  public void onPageSelected(int position) {
							// When changing pages, reset the action bar actions since
							// they are dependent
							// on which page is currently active. An alternative
							// approach is to have each
							// fragment expose actions itself (rather than the activity
							// exposing actions),
							// but for simplicity, the activity provides the actions in
							// this sample.
			      invalidateOptionsMenu();
			  }
		      });
		  
	      }
				//		
				// Add any number of items to the list of your Fragment

//				mPagerAdapter.searchDoc(query);
	      Log.e("ERROR", "***set current item");
	      mPager.setCurrentItem(0);
	      mPagerAdapter.searhDoc(searchID);
	  }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_screen_slide, menu);

       /* menu.findItem(R.id.action_previous).setEnabled(mPager.getCurrentItem() > 0);

        // Add either a "next" or "finish" button to the action bar, depending on which page
        // is currently selected.
        MenuItem item = menu.add(Menu.NONE, R.id.action_next, Menu.NONE,
                (mPager.getCurrentItem() == mPagerAdapter.getCount() - 1)
                        ? R.string.action_finish
                        : R.string.action_next);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);*/
        
        MenuItem searchItem = menu.findItem(R.id.action_search);
        
        searchView = (SearchView) searchItem.getActionView();


        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
         if(null!=searchManager ) {   
           searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
          }
      searchView.setIconifiedByDefault(false);

        //search box
        //Keep a global variable of this so you can set it within the next listener
        //SearchView receipt_search = (SearchView) search.getActionView();
    	
      searchView.setOnQueryTextListener(new OnQueryTextListener() {
	      @Override
	      public boolean onQueryTextSubmit(String query) {
		  searchView.clearFocus();
		  InputMethodManager in = (InputMethodManager)currentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
		  in.hideSoftInputFromWindow( searchView.getWindowToken(), 0);
			    
		  jsonLoaded = false;		

		  searchID = query;
		  Log.e("searchID", "**** searchID is " + searchID);
		  processSearchDocId();
		  return true;
	      }

	      // @Override
	      public boolean onQueryTextChange(String text) {
		  return true;
	      }
	  });
        
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if (searchID == null)
    		return false;
    	if (mDrawerToggle.onOptionsItemSelected(item)) {
	    return true;
    	}
    	Log.e("ERROR", "**** touched home1");
        switch (item.getItemId()) {
	case android.R.id.home:
		Log.e("ERROR", "**** touched home");
	    // Navigate "up" the demo structure to the launchpad activity.
	    // See http://developer.android.com/design/patterns/navigation.html for more.
	    //NavUtils.navigateUpTo(this, new Intent(this, LoginActivity.class));
	    return true;
	    
           // case R.id.action_previous:
                // Go to the previous step in the wizard. If there is no previous step,
                // setCurrentItem will do nothing.
              //  mPager.setCurrentItem(mPager.getCurrentItem() - 1);
            //    return true;

            //case R.id.action_next:
                // Advance to the next step in the wizard. If there is no next step, setCurrentItem
                // will do nothing.
              //  mPager.setCurrentItem(mPager.getCurrentItem() + 1);
               // return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
	// update the main content by replacing fragments
	// Getting an array of rivers
	//String[] menuItems = getResources().getStringArray(R.array.menus);
	//String[] menuItems = getMenus(position);
	NavDrawerItem item = adapter.getNthItem(position);
	
	// Currently selected river
	//mTitle = menuItems[position];
	
	if (mPager != null) {
	    mPager.setCurrentItem(position);
	}
	// Creating a fragment object
	
	// update selected item and title, then close the drawer
	mDrawerList.setItemChecked(position, true);
	mDrawerList.setSelection(position);
	if (item != null) {
	    setTitle(item.getLabel());
	}
	mDrawerLayout.closeDrawer(mDrawerList);
    }

    /**
     * Slide menu item click listener
     * */
    private class SlideMenuClickListener implements ListView.OnItemClickListener {
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

	    NavDrawerItem item = adapter.getNthItem(position);

	    if (item == null) {
		return;
	    }
	    String itemLabel = item.getLabel();

	    // display view for selected nav drawer item
	    Log.e("EEEOR", "*** postion is " +position + "view:" + itemLabel);

	    //Log.e("EEEOR", "*** postion object is " + t);
	    if (position == 1){
	    	// Set an EditText view to get user input 
	    	final EditText input = new EditText(currentActivity);
            input.setText(serverURL);
	    	new AlertDialog.Builder(currentActivity)
	    	    .setTitle(R.string.menu_item_setting)
	    	    .setMessage(getString(R.string.menu_item_server))
	    	    .setView(input)
	    	    .setPositiveButton(getString(R.string.menu_item_ok), new DialogInterface.OnClickListener() {
	    	         public void onClick(DialogInterface dialog, int whichButton) {
	    	        	 serverURL  = input.getText().toString(); 
	    	        	 Log.e("ERROR", "*** serverURL " + serverURL);
	    	        	 SharedPreferences.Editor editor = preferences.edit();
	    	        	 editor.putString("serverURL", serverURL); // value to store
	    	        	 editor.commit();
	    	             // deal with the editable
	    	         }
	    	    })
	    	    .setNegativeButton(getString(R.string.menu_item_cancel), new DialogInterface.OnClickListener() {
	    	         public void onClick(DialogInterface dialog, int whichButton) {
	    	                // Do nothing.
	    	         }
	    	    }).show();
		
	    }
	    if (position == 2){
	    	
		NavUtils.navigateUpTo(currentActivity,
				      new Intent(view.getContext(), LoginActivity.class));
		    
		
	    }
	    else {
		    displayView(position);
	    }
	    
	}
    }
}

