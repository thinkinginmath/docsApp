package info.androidhive.slidingmenu;

import android.annotation.SuppressLint;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
 
@SuppressLint("JavascriptInterface")
public class LoginActivity extends Activity {
 
    //private Button button;
    private WebView webView;
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
		
         
       // startWebView("http://api.uubright.com/2225/pic000000.jpg");
        startWebView("http://api.uubright.com/llogin.html");
		
    }
    
    @Override
    public void onResume(){
        Log.e("ERROR", "***onResuem gets called");
        // put your code here...
    	if (webView != null) {
    	    webView.loadUrl("http://api.uubright.com/llogin.html");
    	}
       super.onResume();
    }
    	
     
    private void startWebView(String url) {
         
        //Create new webview Client to show progress dialog
        //When opening a url or click on link
         
        webView.setWebViewClient(new WebViewClient() {      
            ProgressDialog progressDialog;
          
            //If you will not use this method url links are opeen in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {              
                view.loadUrl(url);
                return true;
            }
        
            //Show loader on url load
            public void onLoadResource (WebView view, String url) {
                if (progressDialog == null) {
                    // in standard case YourActivity.this
                    progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setMessage("Loading...");
                    //startActivity(new Intent(LoginActivity.this, MainActivity.class));

                    progressDialog.show();
                }
            }
            public void onPageFinished(WebView view, String url) {
                try{
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;
                    if (url.indexOf("/login.html") > 0){
                    	startActivity(new Intent(LoginActivity.this, ScreenSlideActivity.class));
                        //startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }
                }
                }catch(Exception exception){
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
                  if (hr != null)
                  Log.e("ERROR", "****getExtra = "+ hr.getExtra() + "\t\t Type=" + hr.getType());
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
