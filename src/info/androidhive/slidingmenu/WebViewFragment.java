package info.androidhive.slidingmenu;

import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import info.androidhive.slidingmenu.R;


public class WebViewFragment extends Fragment {
    float x1, x2;
    float y1, y2;
    WebView webView;
    String title;
    String docId;
    private String currentURL;
    private  boolean isSearchPage;
    
    public void init(String url, String docId, boolean isSearch) {
        currentURL = url;
	    this.docId = docId;
	    isSearchPage = isSearch;
    }
    
    public void setTitle(String title) {
	this.title = title;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {        
        super.onActivityCreated(savedInstanceState);
    }

    public void updateUrl(String url) {
       Log.d("SwA", "Update URL ["+url+"] - View ["+getView()+"]");
       currentURL = url;
       
       if (webView!= null) {
	   webView.loadUrl(url);
       }
    }
    
	
	/*OnTouchListener onTouchListener = new View.OnTouchListener() {
	    @Override
	    public boolean onTouch(View v, MotionEvent event) {
	    	Log.e("ERROR " , "******onTouchListener: event was trigger");
	    	webView.getSettings().setSupportZoom(true);
		    webView.getSettings().setBuiltInZoomControls(true);


	        return false;
	    }
	};*/

	
	/*protected String[] getMenus () {
		//sudo code needs hoops up with server to receive data;
		String[] nameList = new String[6];

        for(int i = 0; i < 6; i++){
            nameList[i] = "Receipt " + i;
        }
        return nameList;
		//end of sudo code
        
	}*/
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
	    Log.e("Error", "*** in WebViewFragment");
	    // Retrieving the currently selected item number
	    //int position = getArguments().getInt("position");
	    
	    //Log.e("Error", "*** position " + position);
	    // List of rivers
	    //String[] menus = getResources().getStringArray(R.array.menus);
	    //String[] menus = getMenus();
	    
	    // Creating view corresponding to the fragment
	    View view = inflater.inflate(R.layout.fragment_layout, container, false);
	    
	    // Updating the action bar title
	    //getActivity().getActionBar().setTitle(menus[position]);
	    
	    //Initializing and loading url in webview
	    webView = (WebView)view.findViewById(R.id.webView); 
	    webView.setWebViewClient(new MyBrowser());
	    webView.loadUrl(currentURL);
	    // webView.setOnTouchListener(onTouchListener);
	    WebSettings settings = webView.getSettings();
	    //if (isSearchPage==false) {
		settings.setLoadWithOverviewMode(true);
		settings.setUseWideViewPort(true);
		settings.setMinimumFontSize(32);
	    //}
	    settings.setJavaScriptEnabled(true);
	    settings.setSupportZoom(true);
	    settings.setBuiltInZoomControls(true);
	    webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
	    webView.setScrollbarFadingEnabled(true);
	    // settings.setAppCacheMaxSize(8*1024*1024);
	    settings.setAppCachePath("/data/data/docsApp/cache");
	    settings.setAppCacheEnabled(true);
	    settings.setCacheMode(WebSettings.LOAD_DEFAULT);
	    webView.setWebViewClient(new WebViewClient() {      
            //ProgressDialog progressDialog;
		    
            //If you will not use this method url links are opeen in new brower not in webview
            /*public boolean shouldOverrideUrlLoading(WebView view, String url) {              
                view.loadUrl(url);
                return true;
            }*/
        
            //Show loader on url load
            /*@Override
            public void onLoadResource (WebView view, String url) {
            }*/
            
	    @Override
	    public void onPageFinished(WebView view, String url) {
            	ScreenSlideActivity sa = (ScreenSlideActivity) WebViewFragment.this.getActivity();
		
		Log.e("ERROR", "**** ===========WebViewFragment on Page finished Doc found");


		//sa.updateTitle(title, docId);
		if (isSearchPage == false) {
		    Log.e("ERROR", "**** ===========");
		    Log.e("ERROR", "**** ===========");
		    Log.e("ERROR", "**** ===========");
		    return;
		}

            	int pos = url.indexOf("/docimages/");
                if (pos != -1) {
		    Log.e("ERROR", "**** Doc found");
		    // Load JSON file.
		    //String uri = url.substring(pos + 11);
		    Log.e("ERROR", "********** trying to load json  " + docId);
		    //String docId = uri.substring(0, uri.indexOf("/"));
		    //Log.e("ERROR", "the loaded DOCID is " + docId);
		    sa.loadDocumentJson(docId);
                } else {
		    Log.e("ERROR", "**** Doc not found");
		    sa.cleanup();
                }
            }
        }); 

       /* webView.setOnTouchListener(new View.OnTouchListener() {
        	//final WebView webview = webView;
        	//final String surl = url;
                public boolean onTouch(View v, MotionEvent touchevent) {

                    switch (touchevent.getAction()) {
            		// when user first touches the screen we get x and y coordinate
            		case MotionEvent.ACTION_DOWN: {
            			x1 = touchevent.getX();
            			y1 = touchevent.getY();
            			break;
            		}
            		case MotionEvent.ACTION_UP: {
            			x2 = touchevent.getX();
            			y2 = touchevent.getY();

            			// if left to right sweep event on screen
            			if (x1 < x2) {
            				Log.e("Debugging", "*****Left to Right Swap Performed...");
            				
            				webView.loadUrl(url);
            			}

            			// if right to left sweep event on screen
            			if (x1 > x2) {
            				Log.e("Debugging", "*****right to Left Swap Performed...");
            				webView.loadUrl(url);
            			}

            			break;
            		}
            		}
            		return true;
                    
                }
        });*/

    //here the rest of your code

	    return view;
	}
	
    private class MyBrowser extends WebViewClient {
	
	@Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	    
	    view.loadUrl(url);
	    
	    return true;
	    
	}
    }
}
