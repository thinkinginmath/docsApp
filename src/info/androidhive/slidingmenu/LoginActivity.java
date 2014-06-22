package info.androidhive.slidingmenu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
 
@SuppressLint("JavascriptInterface")
public class LoginActivity extends Activity {
 
    //private Button button;
    private WebView webView;

    private boolean toLogout;

    private String loginUrl;
    private String logoutUrl;
    private String homePageUrl;
    private String server;

    private void setServer(String server, boolean write) {
	this.server = server;
	String urlBase = "http://" + server;
	loginUrl = urlBase + "/m/login.html";
	logoutUrl = urlBase + "/users/logout?mobile=pad";
	homePageUrl = urlBase + "/pad/pad.html";

	if (write) {
	    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
	    SharedPreferences.Editor editor = preferences.edit();
	    editor.putString("dms.server", server);
	    editor.putString("dms.intent", "login");
	    editor.commit();
	}
    }

    public void onCreate(Bundle savedInstanceState) {         
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        Log.e("Debugging", "*****login debug....");
        //Get webview 
        webView = (WebView) findViewById(R.id.webView1);
        //webView.getSettings().setLoadWithOverviewMode(true);
	webView.getSettings().setUseWideViewPort(true);
	//webView.getSettings().setJavaScriptEnabled(true);
	webView.getSettings().setSupportZoom(true);
	webView.getSettings().setBuiltInZoomControls(true);
	webView.getSettings().setJavaScriptEnabled(true);
	setServer("13.141.43.227", true);

        startWebView(loginUrl);		
    }
    
    @Override
    public void onResume() {
    	SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
    	String server = preferences.getString("dms.server", "13.141.43.227" ); 
    	String reason = preferences.getString("dms.intent", "login");
	
        Log.e("ERROR", "***onResuem gets called");
        // put your code here...
    	if (webView != null) {
    	    if (reason == "logout") {
		webView.loadUrl(logoutUrl);
	    } else if (reason == "login") {
		webView.loadUrl(loginUrl);
	    } else {
		webView.loadUrl(homePageUrl);
	    }
	}
	super.onResume();
    }
     
    private void startWebView(String url) {
        //Create new webview Client to show progress dialog
        //When opening a url or click on link         
        webView.setWebViewClient(new WebViewClient() {      
		ProgressDialog progressDialog;
          
		//If you will not use this method url links are opeen in new brower not in webview
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {              
		    view.loadUrl(url);
		    return true;
		}
        
		//Show loader on url load
		/*@Override
		public void onLoadResource (WebView view, String url) {
		    if (progressDialog == null) {
			// in standard case YourActivity.this
			progressDialog = new ProgressDialog(LoginActivity.this);
			progressDialog.setMessage("Loading...");
                    //startActivity(new Intent(LoginActivity.this, MainActivity.class));

                    //progressDialog.show();
                }
		}*/
		@Override
		public void onPageFinished(WebView view, String url) {
		    Log.e("Debugging", "callback for Page FInished ...." + url);
		    try {
			if (progressDialog != null) {
			    progressDialog.dismiss();
			    progressDialog = null;
			}
			boolean docLoaded = false;
			String docId = "";
			String imageDir = "";
			if (url.indexOf("/docimages/") > 0) {
			    int pos = url.indexOf("/docimages/");
			    String remaining = url.substring(pos + 11);
			    Log.e("ERROR", "****remaining = " + remaining);
			    int pos2 = remaining.indexOf("/");
			    if (pos2 > 0) {
				docLoaded = true;
				imageDir = "docimages";
				docId = remaining.substring(0, pos2);
				Log.e("ERROR", "****remaining docId= " + docId);
			    }
			} else if (url.indexOf("/docimages_mod/") > 0) {
			    int pos = url.indexOf("/docimages_mod/");
			    String remaining = url.substring(pos + 15);
			    Log.e("ERROR", "****remaining = " + remaining);
			    int pos2 = remaining.indexOf("/");
			    
			    if (pos2 > 0) {
				docLoaded = true;
				imageDir = "docimages_mod";
				docId = remaining.substring(0, pos2);
				Log.e("ERROR", "****remaining docId= " + docId);
			    }
			}
			if (docLoaded) {
			    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
			    SharedPreferences.Editor editor = preferences.edit();
			    editor.putString("dms.imageDir", imageDir);
			    editor.putString("dms.docId", docId);
			    editor.commit();
			    startActivity(new Intent(LoginActivity.this, ScreenSlideActivity.class));
			    //startActivity(new Intent(LoginActivity.this, MainActivity.class));
			}
		    } catch(Exception exception){
			exception.printStackTrace();
		    }
		}
	    }); 
    
        // Other webview options
        
       /* webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);
        webView.getSettings().setBuiltInZoomControls(true);*/
        
         
        /*
         String summary = "<html><body>You scored <b>192</b> points.</body></html>";
         webview.loadData(summary, "text/html", null); 
         */
         
        //Load url in webview
        webView.loadUrl(url);
        
        webView.setOnTouchListener(new View.OnTouchListener() {
        	//final WebView webview = webView;
        	//final String surl = url;
		public boolean onTouch(View v, MotionEvent event) {
		    WebView.HitTestResult hr = ((WebView)v).getHitTestResult();
		    if (hr != null) {
			;//Log.e("ERROR", "****getExtra = "+ hr.getExtra() + "\t\t Type=" + hr.getType());
		    }
		    //Log.e("ERROR", "****getExtra = "+ hr);
		    return false;
		}
        });
    }
    
    //SuppressLint("JavascriptInterface")
    public void open(View view){
       //String url = field.getText().toString();
	Log.e("LOGIN::", "*****Clicked1");
       webView.getSettings().setLoadsImagesAutomatically(true);
       webView.getSettings().setJavaScriptEnabled(true);
       webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
      // webView.loadUrl(url);

    }
     
    // Open previous opened link from history on webview when back button pressed
     
    @Override
    // Detect when the back button is pressed
    public void onBackPressed() {
        if(webView.canGoBack()) {
            webView.goBack();
        } else {
            // Let the system handle the back button
            super.onBackPressed();
        }
    }
}
